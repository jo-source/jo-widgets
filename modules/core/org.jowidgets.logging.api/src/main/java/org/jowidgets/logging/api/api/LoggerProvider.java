/*
 * Copyright (c) 2012, grossmann
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

package org.jowidgets.logging.api.api;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.jowidgets.classloading.api.SharedClassLoader;

public final class LoggerProvider {

    private static ILoggerProvider provider;

    private LoggerProvider() {}

    /**
     * Sets a new logger provider implementation
     * 
     * @param factory The logger provider to set, must not be null
     */
    public static synchronized void setLoggerProvider(final ILoggerProvider provider) {
        assertParamNotNull(provider, "provider");
        LoggerProvider.provider = provider;
    }

    /**
     * Gets the logger with the given name or crates one if not exists
     * 
     * @param name The name to get the logger for
     * 
     * @return The logger for the given name, never null
     */
    public static ILogger get(final String name) {
        return instance().get(name);
    }

    /**
     * Gets the logger with the given class or crates one if not exists
     * 
     * @param clazz The class to get the logger for
     * 
     * @return The logger for the given class, never null
     */
    public static ILogger get(final Class<?> clazz) {
        assertParamNotNull(clazz, "clazz");
        return instance().get(clazz.getName());
    }

    /**
     * Gets the logger provider instance.
     * 
     * @return The logger provider instance, never null
     */
    public static ILoggerProvider instance() {
        if (provider == null) {
            provider = createLoggerProvider();
        }
        return provider;
    }

    private static synchronized ILoggerProvider createLoggerProvider() {
        ILoggerProvider result;

        final ServiceLoader<ILoggerProvider> loader = ServiceLoader.load(
                ILoggerProvider.class,
                SharedClassLoader.getCompositeClassLoader());
        final Iterator<ILoggerProvider> iterator = loader.iterator();

        if (!iterator.hasNext()) {
            result = new DefaultLoggerProvider();
        }
        else {
            result = iterator.next();
            if (iterator.hasNext()) {
                throw new IllegalStateException("More than one implementation found for '"
                    + ILoggerProvider.class.getName()
                    + "'");
            }
        }
        return result;
    }

    private static void assertParamNotNull(final Object object, final String name) {
        if (object == null) {
            throw new IllegalArgumentException("The parameter '" + name + "' must not be null!");
        }
    }

    private static final class DefaultLoggerProvider implements ILoggerProvider {
        @Override
        public ILogger get(final String name) {
            return new DefaultLogger(name);
        }
    }

    private static final class DefaultLogger implements ILogger {

        private final String prefix;

        DefaultLogger(final String name) {
            assertParamNotNull(name, "name");
            this.prefix = name + ": ";
        }

        @Override
        public boolean isTraceEnabled() {
            return true;
        }

        @Override
        public boolean isDebugEnabled() {
            return true;
        }

        @Override
        public boolean isInfoEnabled() {
            return true;
        }

        @Override
        public boolean isWarnEnabled() {
            return true;
        }

        @Override
        public boolean isErrorEnabled() {
            return true;
        }

        @Override
        public void error(final String message) {
            logMessage(LogLevel.ERROR, message);
        }

        @Override
        public void warn(final String message) {
            logMessage(LogLevel.WARN, message);
        }

        @Override
        public void info(final String message) {
            logMessage(LogLevel.INFO, message);
        }

        @Override
        public void debug(final String message) {
            logMessage(LogLevel.DEBUG, message);
        }

        @Override
        public void trace(final String message) {
            logMessage(LogLevel.TRACE, message);
        }

        @Override
        public void error(final Throwable throwable) {
            logMessage(LogLevel.ERROR, throwable);
        }

        @Override
        public void warn(final Throwable throwable) {
            logMessage(LogLevel.WARN, throwable);
        }

        @Override
        public void info(final Throwable throwable) {
            logMessage(LogLevel.INFO, throwable);
        }

        @Override
        public void debug(final Throwable throwable) {
            logMessage(LogLevel.DEBUG, throwable);
        }

        @Override
        public void trace(final Throwable throwable) {
            logMessage(LogLevel.TRACE, throwable);
        }

        @Override
        public void error(final String message, final Throwable throwable) {
            logMessage(LogLevel.ERROR, message, throwable);
        }

        @Override
        public void warn(final String message, final Throwable throwable) {
            logMessage(LogLevel.WARN, message, throwable);
        }

        @Override
        public void info(final String message, final Throwable throwable) {
            logMessage(LogLevel.INFO, message, throwable);
        }

        @Override
        public void debug(final String message, final Throwable throwable) {
            logMessage(LogLevel.DEBUG, message, throwable);
        }

        @Override
        public void trace(final String message, final Throwable throwable) {
            logMessage(LogLevel.TRACE, message, throwable);
        }

        private void logMessage(final LogLevel level, final Throwable throwable) {
            logMessage(level, null, throwable);
        }

        private void logMessage(final LogLevel level, final String message) {
            logMessage(level, message, null);
        }

        private void logMessage(final LogLevel level, final String message, final Throwable throwable) {
            final StringBuilder builder = new StringBuilder(prefix);
            builder.append(level.toString());
            builder.append(" - ");
            if (message != null) {
                builder.append(message);
            }
            //CHECKSTYLE:OFF
            System.out.println(builder.toString());
            if (throwable != null) {
                throwable.printStackTrace();
            }
            //CHECKSTYLE:ON
        }

    }

    private static enum LogLevel {
        ERROR,
        WARN,
        INFO,
        DEBUG,
        TRACE;
    }

}
