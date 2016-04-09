/*
 * Copyright (c) 2016, grossmann
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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.jowidgets.logging.tools.DefaultLoggerProvider;
import org.jowidgets.logging.tools.ILoggerFactory;
import org.jowidgets.logging.tools.LogLevel;
import org.jowidgets.logging.tools.LoggerComposite;
import org.jowidgets.logging.tools.LoggerMock;
import org.jowidgets.logging.tools.LoggerMockMessage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class LoggerProviderTest {

    private PrintStream oldErrStream;
    private ByteArrayOutputStream systemErrorStream;

    @Before
    public void setUp() {
        oldErrStream = System.err;
        systemErrorStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(systemErrorStream));
        LoggerProvider.setLoggerProvider(null);
    }

    @After
    public void tearDown() {
        System.setErr(oldErrStream);
        systemErrorStream = null;
        LoggerProvider.setLoggerProvider(null);
    }

    @Test
    public void testHasInitialLoggerProvider() throws UnsupportedEncodingException {
        Assert.assertNotNull(LoggerProvider.get("FOO"));
        Assert.assertTrue(systemErrorStream.toString().startsWith(LoggerProvider.DEFAULT_LOGGER_WARNING));
    }

    @Test
    public void testLoggerCreationOnSetProvider() {

        final ILoggerProvider loggerProvider = Mockito.mock(ILoggerProvider.class);
        LoggerProvider.setLoggerProvider(loggerProvider);

        final String loggerName1 = "LOGGER_1";
        final String loggerName2 = "LOGGER_2";

        final String wrapperFQCN1 = "WRAPPER_1";
        final String wrapperFQCN2 = "WRAPPER_2";

        LoggerProvider.get(loggerName2);

        LoggerProvider.get(loggerName1);
        LoggerProvider.get(loggerName1);

        LoggerProvider.get(loggerName2);
        LoggerProvider.get(loggerName2);

        LoggerProvider.get(loggerName2, wrapperFQCN1);

        LoggerProvider.get(loggerName1, wrapperFQCN1);
        LoggerProvider.get(loggerName1, wrapperFQCN1);

        LoggerProvider.get(loggerName2, wrapperFQCN1);
        LoggerProvider.get(loggerName2, wrapperFQCN1);

        LoggerProvider.get(loggerName2, wrapperFQCN2);

        LoggerProvider.get(loggerName1, wrapperFQCN2);
        LoggerProvider.get(loggerName1, wrapperFQCN2);

        LoggerProvider.get(loggerName2, wrapperFQCN2);
        LoggerProvider.get(loggerName2, wrapperFQCN2);

        Mockito.verify(loggerProvider, Mockito.times(2)).get(loggerName1, null);
        Mockito.verify(loggerProvider, Mockito.times(3)).get(loggerName2, null);

        Mockito.verify(loggerProvider, Mockito.times(2)).get(loggerName1, wrapperFQCN1);
        Mockito.verify(loggerProvider, Mockito.times(3)).get(loggerName2, wrapperFQCN1);

        Mockito.verify(loggerProvider, Mockito.times(2)).get(loggerName1, wrapperFQCN2);
        Mockito.verify(loggerProvider, Mockito.times(3)).get(loggerName2, wrapperFQCN2);
    }

    @Test
    public void testAddAndRemoveLoggerProvider() {

        final ILoggerProvider loggerProvider = Mockito.mock(ILoggerProvider.class);

        LoggerProvider.addLoggerProvider(loggerProvider);
        LoggerProvider.get("FOO");
        Mockito.verify(loggerProvider, Mockito.times(1)).get(Mockito.anyString(), Mockito.anyString());

        LoggerProvider.removeLoggerProvider(loggerProvider);
        LoggerProvider.get("FOO");
        Mockito.verify(loggerProvider, Mockito.times(1)).get(Mockito.anyString(), Mockito.anyString());
        Assert.assertTrue(systemErrorStream.toString().startsWith(LoggerProvider.DEFAULT_LOGGER_WARNING));

        LoggerProvider.addLoggerProvider(loggerProvider);
        LoggerProvider.get("FOO");
        Mockito.verify(loggerProvider, Mockito.times(2)).get(Mockito.anyString(), Mockito.anyString());

        LoggerProvider.removeLoggerProvider(loggerProvider);
        LoggerProvider.get("FOO");
        Mockito.verify(loggerProvider, Mockito.times(2)).get(Mockito.anyString(), Mockito.anyString());
        Assert.assertTrue(systemErrorStream.toString().startsWith(LoggerProvider.DEFAULT_LOGGER_WARNING));

    }

    @Test
    public void testDefaultLoggerProviderLazyLoggerCreation() {
        final ILoggerFactory loggerFactory = Mockito.mock(ILoggerFactory.class);
        Mockito.when(loggerFactory.create(Mockito.anyString(), Mockito.anyString())).thenReturn(Mockito.mock(ILogger.class));
        LoggerProvider.setLoggerProvider(new DefaultLoggerProvider(loggerFactory));

        final String loggerName1 = "LOGGER_1";
        final String loggerName2 = "LOGGER_2";
        final String loggerName3 = "LOGGER_3";

        final String wrapperFQCN1 = "WRAPPER_1";
        final String wrapperFQCN2 = "WRAPPER_2";

        LoggerProvider.get(loggerName1, null);
        LoggerProvider.get(loggerName1, null);
        LoggerProvider.get(loggerName1, null);

        LoggerProvider.get(loggerName1, wrapperFQCN1);
        LoggerProvider.get(loggerName1, wrapperFQCN1);
        LoggerProvider.get(loggerName1, wrapperFQCN1);

        LoggerProvider.get(loggerName1, wrapperFQCN2);
        LoggerProvider.get(loggerName1, wrapperFQCN2);
        LoggerProvider.get(loggerName1, wrapperFQCN2);

        LoggerProvider.get(loggerName2, null);
        LoggerProvider.get(loggerName2, null);
        LoggerProvider.get(loggerName2, null);

        LoggerProvider.get(loggerName2, wrapperFQCN1);
        LoggerProvider.get(loggerName2, wrapperFQCN1);
        LoggerProvider.get(loggerName2, wrapperFQCN1);

        LoggerProvider.get(loggerName2, wrapperFQCN2);
        LoggerProvider.get(loggerName2, wrapperFQCN2);
        LoggerProvider.get(loggerName2, wrapperFQCN2);

        LoggerProvider.get(loggerName3, null);
        LoggerProvider.get(loggerName3, null);
        LoggerProvider.get(loggerName3, null);

        LoggerProvider.get(loggerName3, wrapperFQCN1);
        LoggerProvider.get(loggerName3, wrapperFQCN1);
        LoggerProvider.get(loggerName3, wrapperFQCN1);

        LoggerProvider.get(loggerName3, wrapperFQCN2);
        LoggerProvider.get(loggerName3, wrapperFQCN2);
        LoggerProvider.get(loggerName3, wrapperFQCN2);

        Mockito.verify(loggerFactory, Mockito.times(1)).create(loggerName1, null);
        Mockito.verify(loggerFactory, Mockito.times(1)).create(loggerName1, wrapperFQCN1);
        Mockito.verify(loggerFactory, Mockito.times(1)).create(loggerName1, wrapperFQCN2);

        Mockito.verify(loggerFactory, Mockito.times(1)).create(loggerName2, null);
        Mockito.verify(loggerFactory, Mockito.times(1)).create(loggerName2, wrapperFQCN1);
        Mockito.verify(loggerFactory, Mockito.times(1)).create(loggerName2, wrapperFQCN2);

        Mockito.verify(loggerFactory, Mockito.times(1)).create(loggerName3, null);
        Mockito.verify(loggerFactory, Mockito.times(1)).create(loggerName3, wrapperFQCN1);
        Mockito.verify(loggerFactory, Mockito.times(1)).create(loggerName3, wrapperFQCN2);

    }

    @Test
    public void testLoggerCreationWithCompositeProvider() {
        final ILoggerProvider loggerProvider1 = Mockito.mock(ILoggerProvider.class);
        LoggerProvider.addLoggerProvider(loggerProvider1);

        final ILoggerProvider loggerProvider2 = Mockito.mock(ILoggerProvider.class);
        LoggerProvider.addLoggerProvider(loggerProvider2);

        final String loggerName1 = "LOGGER_1";
        final String loggerName2 = "LOGGER_2";

        final String wrapperFQCN = "WRAPPER";

        LoggerProvider.get(loggerName1);
        LoggerProvider.get(loggerName2);

        LoggerProvider.get(loggerName1, wrapperFQCN);
        LoggerProvider.get(loggerName2, wrapperFQCN);

        //if no higher level wrapperFQCN is defined, the LoggerComposite class name must be set 
        //to ignore the logger composite (what is a wrapper) in location determination. 
        //This test may fail if the logger composite implementation will be changed or replaced!!!
        Mockito.verify(loggerProvider1, Mockito.times(1)).get(loggerName1, LoggerComposite.class.getName());
        Mockito.verify(loggerProvider1, Mockito.times(1)).get(loggerName2, LoggerComposite.class.getName());

        Mockito.verify(loggerProvider2, Mockito.times(1)).get(loggerName1, wrapperFQCN);
        Mockito.verify(loggerProvider2, Mockito.times(1)).get(loggerName2, wrapperFQCN);
    }

    @Test
    public void testLoggerComposite() {

        final LoggerMock logger1 = new LoggerMock();
        final LoggerMock logger2 = new LoggerMock();

        final ILoggerProvider loggerProvider2 = createLoggerProviderMockForLogger(logger2);
        final ILoggerProvider loggerProvider1 = createLoggerProviderMockForLogger(logger1);

        LoggerProvider.addLoggerProvider(loggerProvider1);
        LoggerProvider.addLoggerProvider(loggerProvider2);

        ILogger compositeLogger = LoggerProvider.get("FOO");
        Assert.assertTrue(compositeLogger.isErrorEnabled());
        Assert.assertTrue(compositeLogger.isWarnEnabled());
        Assert.assertTrue(compositeLogger.isInfoEnabled());
        Assert.assertTrue(compositeLogger.isDebugEnabled());
        Assert.assertTrue(compositeLogger.isTraceEnabled());

        compositeLogger.error("FOO");
        LoggerMockMessage message1 = logger1.popLastMessage();
        LoggerMockMessage message2 = logger2.popLastMessage();
        Assert.assertEquals(LogLevel.ERROR, message1.getLevel());
        Assert.assertEquals(LogLevel.ERROR, message2.getLevel());
        Assert.assertEquals("FOO", message1.getMessage());
        Assert.assertEquals("FOO", message2.getMessage());

        compositeLogger.warn("FOO");
        message1 = logger1.popLastMessage();
        message2 = logger2.popLastMessage();
        Assert.assertEquals(LogLevel.WARN, message1.getLevel());
        Assert.assertEquals(LogLevel.WARN, message2.getLevel());
        Assert.assertEquals("FOO", message1.getMessage());
        Assert.assertEquals("FOO", message2.getMessage());

        compositeLogger.info("FOO");
        message1 = logger1.popLastMessage();
        message2 = logger2.popLastMessage();
        Assert.assertEquals(LogLevel.INFO, message1.getLevel());
        Assert.assertEquals(LogLevel.INFO, message2.getLevel());
        Assert.assertEquals("FOO", message1.getMessage());
        Assert.assertEquals("FOO", message2.getMessage());

        compositeLogger.debug("FOO");
        message1 = logger1.popLastMessage();
        message2 = logger2.popLastMessage();
        Assert.assertEquals(LogLevel.DEBUG, message1.getLevel());
        Assert.assertEquals(LogLevel.DEBUG, message2.getLevel());
        Assert.assertEquals("FOO", message1.getMessage());
        Assert.assertEquals("FOO", message2.getMessage());

        compositeLogger.trace("FOO");
        message1 = logger1.popLastMessage();
        message2 = logger2.popLastMessage();
        Assert.assertEquals(LogLevel.TRACE, message1.getLevel());
        Assert.assertEquals(LogLevel.TRACE, message2.getLevel());
        Assert.assertEquals("FOO", message1.getMessage());
        Assert.assertEquals("FOO", message2.getMessage());

        //now disable logger 1
        logger1.setEnabled(false);
        Assert.assertTrue(compositeLogger.isErrorEnabled());
        Assert.assertTrue(compositeLogger.isWarnEnabled());
        Assert.assertTrue(compositeLogger.isInfoEnabled());
        Assert.assertTrue(compositeLogger.isDebugEnabled());
        Assert.assertTrue(compositeLogger.isTraceEnabled());

        compositeLogger.error("FOO");
        Assert.assertNull(logger1.popLastMessage());
        message2 = logger2.popLastMessage();
        Assert.assertEquals(LogLevel.ERROR, message2.getLevel());
        Assert.assertEquals("FOO", message2.getMessage());

        compositeLogger.warn("FOO");
        Assert.assertNull(logger1.popLastMessage());
        message2 = logger2.popLastMessage();
        Assert.assertEquals(LogLevel.WARN, message2.getLevel());
        Assert.assertEquals("FOO", message2.getMessage());

        compositeLogger.info("FOO");
        Assert.assertNull(logger1.popLastMessage());
        message2 = logger2.popLastMessage();
        Assert.assertEquals(LogLevel.INFO, message2.getLevel());
        Assert.assertEquals("FOO", message2.getMessage());

        compositeLogger.debug("FOO");
        Assert.assertNull(logger1.popLastMessage());
        message2 = logger2.popLastMessage();
        Assert.assertEquals(LogLevel.DEBUG, message2.getLevel());
        Assert.assertEquals("FOO", message2.getMessage());

        compositeLogger.trace("FOO");
        Assert.assertNull(logger1.popLastMessage());
        message2 = logger2.popLastMessage();
        Assert.assertEquals(LogLevel.TRACE, message2.getLevel());
        Assert.assertEquals("FOO", message2.getMessage());

        //now enable logger 1 and disable logger 2
        logger2.setEnabled(false);
        logger1.setEnabled(true);

        Assert.assertTrue(compositeLogger.isErrorEnabled());
        Assert.assertTrue(compositeLogger.isWarnEnabled());
        Assert.assertTrue(compositeLogger.isInfoEnabled());
        Assert.assertTrue(compositeLogger.isDebugEnabled());
        Assert.assertTrue(compositeLogger.isTraceEnabled());

        compositeLogger.error("FOO");
        Assert.assertNull(logger2.popLastMessage());
        message1 = logger1.popLastMessage();
        Assert.assertEquals(LogLevel.ERROR, message1.getLevel());
        Assert.assertEquals("FOO", message1.getMessage());

        compositeLogger.warn("FOO");
        Assert.assertNull(logger2.popLastMessage());
        message1 = logger1.popLastMessage();
        Assert.assertEquals(LogLevel.WARN, message1.getLevel());
        Assert.assertEquals("FOO", message1.getMessage());

        compositeLogger.info("FOO");
        Assert.assertNull(logger2.popLastMessage());
        message1 = logger1.popLastMessage();
        Assert.assertEquals(LogLevel.INFO, message1.getLevel());
        Assert.assertEquals("FOO", message1.getMessage());

        compositeLogger.debug("FOO");
        Assert.assertNull(logger2.popLastMessage());
        message1 = logger1.popLastMessage();
        Assert.assertEquals(LogLevel.DEBUG, message1.getLevel());
        Assert.assertEquals("FOO", message1.getMessage());

        compositeLogger.trace("FOO");
        Assert.assertNull(logger2.popLastMessage());
        message1 = logger1.popLastMessage();
        Assert.assertEquals(LogLevel.TRACE, message1.getLevel());
        Assert.assertEquals("FOO", message1.getMessage());

        //now disable logger 1 and disable logger 2
        logger2.setEnabled(false);
        logger1.setEnabled(false);

        Assert.assertFalse(compositeLogger.isErrorEnabled());
        Assert.assertFalse(compositeLogger.isWarnEnabled());
        Assert.assertFalse(compositeLogger.isInfoEnabled());
        Assert.assertFalse(compositeLogger.isDebugEnabled());
        Assert.assertFalse(compositeLogger.isTraceEnabled());

        compositeLogger.error("FOO");
        Assert.assertNull(logger1.popLastMessage());
        Assert.assertNull(logger2.popLastMessage());

        compositeLogger.warn("FOO");
        Assert.assertNull(logger1.popLastMessage());
        Assert.assertNull(logger2.popLastMessage());

        compositeLogger.info("FOO");
        Assert.assertNull(logger1.popLastMessage());
        Assert.assertNull(logger2.popLastMessage());

        compositeLogger.debug("FOO");
        Assert.assertNull(logger1.popLastMessage());
        Assert.assertNull(logger2.popLastMessage());

        compositeLogger.trace("FOO");
        Assert.assertNull(logger1.popLastMessage());
        Assert.assertNull(logger2.popLastMessage());

        //now enable both logger remove logger 2
        logger1.setEnabled(true);
        logger2.setEnabled(true);
        Assert.assertTrue(compositeLogger instanceof LoggerComposite);
        Assert.assertTrue(LoggerProvider.removeLoggerProvider(loggerProvider2));
        compositeLogger = LoggerProvider.get("FOO");

        //The logger should no longer be a composite after second provider removed
        Assert.assertFalse(compositeLogger instanceof LoggerComposite);

        Assert.assertTrue(compositeLogger.isErrorEnabled());
        Assert.assertTrue(compositeLogger.isWarnEnabled());
        Assert.assertTrue(compositeLogger.isInfoEnabled());
        Assert.assertTrue(compositeLogger.isDebugEnabled());
        Assert.assertTrue(compositeLogger.isTraceEnabled());

        compositeLogger.error("FOO");
        Assert.assertNull(logger2.popLastMessage());
        message1 = logger1.popLastMessage();
        Assert.assertEquals(LogLevel.ERROR, message1.getLevel());
        Assert.assertEquals("FOO", message1.getMessage());

        compositeLogger.warn("FOO");
        Assert.assertNull(logger2.popLastMessage());
        message1 = logger1.popLastMessage();
        Assert.assertEquals(LogLevel.WARN, message1.getLevel());
        Assert.assertEquals("FOO", message1.getMessage());

        compositeLogger.info("FOO");
        Assert.assertNull(logger2.popLastMessage());
        message1 = logger1.popLastMessage();
        Assert.assertEquals(LogLevel.INFO, message1.getLevel());
        Assert.assertEquals("FOO", message1.getMessage());

        compositeLogger.debug("FOO");
        Assert.assertNull(logger2.popLastMessage());
        message1 = logger1.popLastMessage();
        Assert.assertEquals(LogLevel.DEBUG, message1.getLevel());
        Assert.assertEquals("FOO", message1.getMessage());

        compositeLogger.trace("FOO");
        Assert.assertNull(logger2.popLastMessage());
        message1 = logger1.popLastMessage();
        Assert.assertEquals(LogLevel.TRACE, message1.getLevel());
        Assert.assertEquals("FOO", message1.getMessage());
    }

    private static ILoggerProvider createLoggerProviderMockForLogger(final ILogger logger) {
        final ILoggerProvider result = Mockito.mock(ILoggerProvider.class);
        Mockito.when(result.get(Mockito.anyString(), Mockito.anyString())).thenReturn(logger);
        return result;
    }
}
