/*
 * Copyright (c) 2016, MGrossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package org.jowidgets.logging.api;

import java.util.concurrent.TimeUnit;

import org.jowidgets.logging.tools.AbstractLogMessageDecorator;
import org.jowidgets.util.Assert;
import org.jowidgets.util.DefaultSystemTimeProvider;
import org.jowidgets.util.EmptyCheck;
import org.jowidgets.util.ISystemTimeProvider;

public final class SupressingLogMessageDecorators {

    public static final long DEFAULT_PERIOD = 1000;
    public static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MILLISECONDS;

    private static final String N_LOG_MESSAGES_WAS_SUPRESSED = " log messages was supressed after last unsupressed message";
    private static final String LOG_MESSAGES_WILL_BE_SUPRESSED_FOR = " More log messages will be suppressed the next ";

    private SupressingLogMessageDecorators() {}

    /**
     * Creates a new suppressing log message decorator with default period (1000 ms)
     * 
     * @param maxPeriod The max period to use
     * 
     * @return a new suppressing log message decorator
     */
    public static ILogMessageDecorator create() {
        return create(DEFAULT_PERIOD, DEFAULT_TIME_UNIT);
    }

    /**
     * Creates a new suppressing log message decorator with a given maxPeriod in milliseconds
     * 
     * @param maxPeriod The max period to use
     * 
     * @return a new suppressing log message decorator
     */
    public static ILogMessageDecorator create(final long maxPeriod) {
        return create(maxPeriod, DEFAULT_TIME_UNIT);
    }

    /**
     * Creates a new suppressing log message decorator
     * 
     * @param maxPeriod The max period to use
     * @param timeUnit The time unit for the period
     * 
     * @return a new suppressing log message decorator
     */
    public static ILogMessageDecorator create(final long maxPeriod, final TimeUnit timeUnit) {
        return builder().setMaxPeriod(maxPeriod, timeUnit).build();
    }

    /**
     * Creates a new builder
     * 
     * @return a new builder
     */
    public static ISupressingLogMessageDecoratorBuilder builder() {
        return new SupressingLogMessageDecoratorBuilderImpl();
    }

    private static final class SupressingLogMessageDecoratorBuilderImpl implements ISupressingLogMessageDecoratorBuilder {

        private ISystemTimeProvider systemTimeProvider;
        private boolean appendMessage;
        private long period;
        private TimeUnit timeUnit;

        private SupressingLogMessageDecoratorBuilderImpl() {
            this.period = DEFAULT_PERIOD;
            this.timeUnit = DEFAULT_TIME_UNIT;
            this.appendMessage = true;
            this.systemTimeProvider = DefaultSystemTimeProvider.getInstance();
        }

        @Override
        public ISupressingLogMessageDecoratorBuilder setMaxPeriod(final long period) {
            return setMaxPeriod(period, TimeUnit.MILLISECONDS);
        }

        @Override
        public ISupressingLogMessageDecoratorBuilder setMaxPeriod(final long period, final TimeUnit timeUnit) {
            Assert.paramNotNull(timeUnit, "timeUnit");
            this.period = period;
            this.timeUnit = timeUnit;
            return this;
        }

        @Override
        public ISupressingLogMessageDecoratorBuilder appendMessage(final boolean appendMessage) {
            this.appendMessage = appendMessage;
            return this;
        }

        @Override
        public ISupressingLogMessageDecoratorBuilder setSystemTimeProvider(final ISystemTimeProvider systemTimeProvider) {
            Assert.paramNotNull(systemTimeProvider, "systemTimeProvider");
            this.systemTimeProvider = systemTimeProvider;
            return this;
        }

        long getPeriod() {
            return period;
        }

        TimeUnit getTimeUnit() {
            return timeUnit;
        }

        boolean isAppendMessage() {
            return appendMessage;
        }

        ISystemTimeProvider getSystemTimeProvider() {
            return systemTimeProvider;
        }

        @Override
        public ILogMessageDecorator build() {
            return new SupressingLogMessageDecoratorImpl(this);
        }

    }

    private static final class SupressingLogMessageDecoratorImpl extends AbstractLogMessageDecorator implements
            ILogMessageDecorator {

        private final boolean appendMessage;
        private final long period;
        private final TimeUnit timeUnit;
        private final ISystemTimeProvider systemTimeProvider;

        private long supressedLogMessageCount;
        private long lastLogEventTimestamp;

        SupressingLogMessageDecoratorImpl(final SupressingLogMessageDecoratorBuilderImpl builder) {
            this.appendMessage = builder.isAppendMessage();
            this.period = builder.getPeriod();
            this.timeUnit = builder.getTimeUnit();
            this.systemTimeProvider = builder.getSystemTimeProvider();
            this.supressedLogMessageCount = 0;
            this.lastLogEventTimestamp = -1;
        }

        @Override
        public void error(final String message, final Throwable throwable, final ILogger original) {
            if (original.isErrorEnabled() && !(handleMessageAndDetermineIfSupress())) {
                original.error(getDecoratedMessage(message), throwable);
            }
        }

        @Override
        public void warn(final String message, final Throwable throwable, final ILogger original) {
            if (original.isWarnEnabled() && !(handleMessageAndDetermineIfSupress())) {
                original.warn(getDecoratedMessage(message), throwable);
            }
        }

        @Override
        public void info(final String message, final Throwable throwable, final ILogger original) {
            if (original.isWarnEnabled() && !(handleMessageAndDetermineIfSupress())) {
                original.info(getDecoratedMessage(message), throwable);
            }
        }

        @Override
        public void debug(final String message, final Throwable throwable, final ILogger original) {
            if (original.isDebugEnabled() && !(handleMessageAndDetermineIfSupress())) {
                original.debug(getDecoratedMessage(message), throwable);
            }
        }

        @Override
        public void trace(final String message, final Throwable throwable, final ILogger original) {
            if (original.isTraceEnabled() && !(handleMessageAndDetermineIfSupress())) {
                original.trace(getDecoratedMessage(message), throwable);
            }
        }

        private boolean handleMessageAndDetermineIfSupress() {
            final long currentTime = systemTimeProvider.currentTimeMillis();
            final boolean supress;

            if (lastLogEventTimestamp == -1) {
                supress = false;
            }
            else {
                supress = isDurationBelowPeriod(currentTime - lastLogEventTimestamp);
            }

            if (supress) {
                supressedLogMessageCount++;
            }
            else {
                lastLogEventTimestamp = currentTime;
            }

            return supress;
        }

        private boolean isDurationBelowPeriod(final long durationMillis) {
            return durationMillis < TimeUnit.MILLISECONDS.convert(period, timeUnit);
        }

        private String getDecoratedMessage(final String message) {
            if (!appendMessage) {
                return message;
            }
            else if (EmptyCheck.isEmpty(message)) {
                return getMessageSuffix();
            }
            else {
                return message + " " + getMessageSuffix();
            }
        }

        private String getMessageSuffix() {
            if (supressedLogMessageCount > 0) {
                return supressedLogMessageCount + N_LOG_MESSAGES_WAS_SUPRESSED;
            }
            else {
                return LOG_MESSAGES_WILL_BE_SUPRESSED_FOR + period + " " + timeUnit;
            }
        }
    }
}
