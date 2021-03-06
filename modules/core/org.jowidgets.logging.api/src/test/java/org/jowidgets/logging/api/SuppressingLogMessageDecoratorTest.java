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

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import org.jowidgets.logging.tools.JUnitLogger;
import org.jowidgets.logging.tools.JUnitLoggerComposite;
import org.jowidgets.logging.tools.JUnitLoggerProvider;
import org.jowidgets.util.DummySystemTimeProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class SuppressingLogMessageDecoratorTest {

    private static final String DEFAULT_MESSAGE = "FOO";
    private final long SYSTEM_TIME_PERIOD = 100;

    private DummySystemTimeProvider systemTimeProvider;

    private static JUnitLogger globalLogger;

    @Before
    public void setUp() {
        JUnitLoggerProvider.reset();
        globalLogger = JUnitLoggerProvider.getGlobalLogger();
        this.systemTimeProvider = new DummySystemTimeProvider(SYSTEM_TIME_PERIOD);
    }

    @After
    public void tearDown() {
        JUnitLoggerProvider.reset();
    }

    private ILogMessageDecorator createLogMessageDecorator(final long maxPeriod) {
        final ISuppressingLogMessageDecoratorBuilder builder = SuppressingLogMessageDecorators.builder();
        builder.setMaxPeriod(maxPeriod);
        builder.setSystemTimeProvider(systemTimeProvider);
        return builder.build();
    }

    @Test
    public void testLogMessageDecorator() {
        final long maxPeriod = 1000;
        final ILogMessageDecorator suppressDecorator = createLogMessageDecorator(maxPeriod);

        final IDecoratingLogger logger = LoggerProvider.getDecoratingLogger("Foo");

        final String expextedSupressMessage = MessageFormat
                .format(SuppressingLogMessageDecorators.DEFAULT_SUPPRESS_MESSAGE_TEXT, maxPeriod, TimeUnit.MILLISECONDS);

        //first message will be unsupressed
        logger.error(suppressDecorator, DEFAULT_MESSAGE);
        Assert.assertTrue(globalLogger.hasMessage());
        String message = globalLogger.popLastMessage().getMessage();
        Assert.assertTrue(message.startsWith(DEFAULT_MESSAGE));
        Assert.assertTrue(message.contains(expextedSupressMessage));

        //next 9 messages will be supressed
        for (int i = 0; i < 9; i++) {
            logger.error(suppressDecorator, DEFAULT_MESSAGE);
            Assert.assertFalse(globalLogger.hasMessage());
        }

        //first message will be unsupressed
        logger.warn(suppressDecorator, DEFAULT_MESSAGE);
        Assert.assertTrue(globalLogger.hasMessage());
        message = globalLogger.popLastMessage().getMessage();
        Assert.assertTrue(message.startsWith(DEFAULT_MESSAGE));
        Assert.assertTrue(message.contains(expextedSupressMessage));
        assertSuppressedMessageContained(message, 9);

        //next 9 messages will be supressed
        for (int i = 0; i < 9; i++) {
            logger.warn(suppressDecorator, DEFAULT_MESSAGE);
            Assert.assertFalse(globalLogger.hasMessage());
        }

        //first message will be unsupressed
        logger.info(suppressDecorator, DEFAULT_MESSAGE);
        Assert.assertTrue(globalLogger.hasMessage());
        message = globalLogger.popLastMessage().getMessage();
        Assert.assertTrue(message.startsWith(DEFAULT_MESSAGE));
        Assert.assertTrue(message.contains(expextedSupressMessage));
        assertSuppressedMessageContained(message, 9);

        //next 9 messages will be supressed
        for (int i = 0; i < 9; i++) {
            logger.info(suppressDecorator, DEFAULT_MESSAGE);
            Assert.assertFalse(globalLogger.hasMessage());
        }

        //first message will be unsupressed
        logger.debug(suppressDecorator, DEFAULT_MESSAGE);
        Assert.assertTrue(globalLogger.hasMessage());
        message = globalLogger.popLastMessage().getMessage();
        Assert.assertTrue(message.startsWith(DEFAULT_MESSAGE));
        Assert.assertTrue(message.contains(expextedSupressMessage));
        assertSuppressedMessageContained(message, 9);

        //next 9 messages will be supressed
        for (int i = 0; i < 9; i++) {
            logger.debug(suppressDecorator, DEFAULT_MESSAGE);
            Assert.assertFalse(globalLogger.hasMessage());
        }

        //first message will be unsupressed
        logger.trace(suppressDecorator, DEFAULT_MESSAGE);
        message = globalLogger.popLastMessage().getMessage();
        Assert.assertTrue(message.startsWith(DEFAULT_MESSAGE));
        Assert.assertTrue(message.contains(expextedSupressMessage));
        assertSuppressedMessageContained(message, 9);

        //next 9 messages will be supressed
        for (int i = 0; i < 9; i++) {
            logger.trace(suppressDecorator, DEFAULT_MESSAGE);
            Assert.assertFalse(globalLogger.hasMessage());
        }

        //Message 10 will be unsupressed
        logger.trace(suppressDecorator, DEFAULT_MESSAGE);
        message = globalLogger.popLastMessage().getMessage();
        Assert.assertTrue(message.startsWith(DEFAULT_MESSAGE));
        Assert.assertTrue(message.contains(expextedSupressMessage));
        assertSuppressedMessageContained(message, 9);
    }

    private void assertSuppressedMessageContained(final String message, final int expectedSupressedMessages) {
        final String expextedSupressedMessage = MessageFormat
                .format(SuppressingLogMessageDecorators.DEFAULT_SUPPRESSED_MESSAGE_TEXT, expectedSupressedMessages);

        Assert.assertTrue(message.contains(expextedSupressedMessage));
    }

    @Test
    public void testConsiderLogLevels() {
        final long maxPeriod = 1000;
        final ILogMessageDecorator suppressDecorator = createLogMessageDecorator(maxPeriod);

        final IDecoratingLogger logger = LoggerProvider.getDecoratingLogger("FooLoggerName");

        final JUnitLoggerComposite original = JUnitLoggerProvider
                .getLoggerComposite("FooLoggerName", "org.jowidgets.logging.api.LoggerProvider$DecoratingLoggerImpl");

        original.setEnabled(false);

        logger.error(suppressDecorator, DEFAULT_MESSAGE);
        Assert.assertFalse(globalLogger.hasMessage());
        Assert.assertEquals(0, systemTimeProvider.getLastCurrentTime());

        logger.warn(suppressDecorator, DEFAULT_MESSAGE);
        Assert.assertFalse(globalLogger.hasMessage());
        Assert.assertEquals(0, systemTimeProvider.getLastCurrentTime());

        logger.info(suppressDecorator, DEFAULT_MESSAGE);
        Assert.assertFalse(globalLogger.hasMessage());
        Assert.assertEquals(0, systemTimeProvider.getLastCurrentTime());

        logger.debug(suppressDecorator, DEFAULT_MESSAGE);
        Assert.assertFalse(globalLogger.hasMessage());
        Assert.assertEquals(0, systemTimeProvider.getLastCurrentTime());

        logger.trace(suppressDecorator, DEFAULT_MESSAGE);
        Assert.assertFalse(globalLogger.hasMessage());
        Assert.assertEquals(0, systemTimeProvider.getLastCurrentTime());

        original.setErrorEnabled(true);
        //now log on some disabled levels
        logger.warn(suppressDecorator, DEFAULT_MESSAGE);
        logger.info(suppressDecorator, DEFAULT_MESSAGE);
        logger.debug(suppressDecorator, DEFAULT_MESSAGE);
        logger.trace(suppressDecorator, DEFAULT_MESSAGE);

        //first message will be unsupressed
        logger.error(suppressDecorator, DEFAULT_MESSAGE);
        Assert.assertTrue(globalLogger.hasMessage());
        Assert.assertTrue(globalLogger.popLastMessage().getMessage().startsWith(DEFAULT_MESSAGE));

        //next 9 messages will be supressed
        for (int i = 0; i < 9; i++) {
            logger.error(suppressDecorator, DEFAULT_MESSAGE);
            Assert.assertFalse(globalLogger.hasMessage());
        }

        //first message will be unsupressed
        logger.error(suppressDecorator, DEFAULT_MESSAGE);
        Assert.assertTrue(globalLogger.hasMessage());
        Assert.assertTrue(globalLogger.popLastMessage().getMessage().startsWith(DEFAULT_MESSAGE));
    }

}
