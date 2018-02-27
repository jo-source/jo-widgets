/*
 * Copyright (c) 2018, herrg
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

package org.jowidgets.logging.tools;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;

import org.jowidgets.logging.api.ILogger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class JUnitLoggerProviderTest {

    private LoggingTestClass loggingClass;

    @Before
    public void setUp() {
        JUnitLoggerProvider.reset();
        loggingClass = new LoggingTestClass();
    }

    @After
    public void tearDown() {
        JUnitLoggerProvider.reset();
    }

    @Test
    public void testReset() {
        Assert.assertTrue(JUnitLoggerProvider.getConsoleLoggerEnablement().isErrorEnabled());
        Assert.assertTrue(JUnitLoggerProvider.getConsoleLoggerEnablement().isWarnEnabled());
        Assert.assertFalse(JUnitLoggerProvider.getConsoleLoggerEnablement().isInfoEnabled());
        Assert.assertFalse(JUnitLoggerProvider.getConsoleLoggerEnablement().isDebugEnabled());
        Assert.assertFalse(JUnitLoggerProvider.getConsoleLoggerEnablement().isTraceEnabled());
        Assert.assertFalse(hasLogMessages());

        JUnitLoggerProvider.getConsoleLoggerEnablement().setEnabled(true);
        Assert.assertTrue(JUnitLoggerProvider.getConsoleLoggerEnablement().isErrorEnabled());
        Assert.assertTrue(JUnitLoggerProvider.getConsoleLoggerEnablement().isWarnEnabled());
        Assert.assertTrue(JUnitLoggerProvider.getConsoleLoggerEnablement().isInfoEnabled());
        Assert.assertTrue(JUnitLoggerProvider.getConsoleLoggerEnablement().isDebugEnabled());
        Assert.assertTrue(JUnitLoggerProvider.getConsoleLoggerEnablement().isTraceEnabled());

        JUnitLoggerProvider.reset();
        Assert.assertTrue(JUnitLoggerProvider.getConsoleLoggerEnablement().isErrorEnabled());
        Assert.assertTrue(JUnitLoggerProvider.getConsoleLoggerEnablement().isWarnEnabled());
        Assert.assertFalse(JUnitLoggerProvider.getConsoleLoggerEnablement().isInfoEnabled());
        Assert.assertFalse(JUnitLoggerProvider.getConsoleLoggerEnablement().isDebugEnabled());
        Assert.assertFalse(JUnitLoggerProvider.getConsoleLoggerEnablement().isTraceEnabled());

        JUnitLoggerProvider.getConsoleLoggerEnablement().setEnabled(false);
        loggingClass.logError("ErrorMessage");
        loggingClass.logWarning("WarningMessage");
        loggingClass.logInfo("InfoMessage");
        loggingClass.logDebug("DebugoMessage");
        loggingClass.logTrace("TraceMessage");
        Assert.assertTrue(hasLocalLogMessages());
        Assert.assertEquals(5, JUnitLoggerProvider.getGlobalLogger().getMessageCount());

        JUnitLoggerProvider.reset();
        Assert.assertTrue(JUnitLoggerProvider.getConsoleLoggerEnablement().isErrorEnabled());
        Assert.assertTrue(JUnitLoggerProvider.getConsoleLoggerEnablement().isWarnEnabled());
        Assert.assertFalse(JUnitLoggerProvider.getConsoleLoggerEnablement().isInfoEnabled());
        Assert.assertFalse(JUnitLoggerProvider.getConsoleLoggerEnablement().isDebugEnabled());
        Assert.assertFalse(JUnitLoggerProvider.getConsoleLoggerEnablement().isTraceEnabled());
        Assert.assertFalse(hasLogMessages());
    }

    private boolean hasLogMessages() {
        return JUnitLoggerProvider.getGlobalLogger().hasMessage() || hasLocalLogMessages();
    }

    private boolean hasLocalLogMessages() {
        for (final ILogger logger : JUnitLoggerProvider.getInstance().getLoggerCompositeProvider().getLoggers()) {
            final JUnitLoggerComposite loggerComposite = (JUnitLoggerComposite) logger;
            if (loggerComposite.getLocalLogger().hasMessage()) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testLocalLogger() {
        JUnitLoggerProvider.getConsoleLoggerEnablement().setEnabled(false);
        final String logMessage = "FooMessage1";
        loggingClass.logError(logMessage);
        assertErrorMessage(JUnitLoggerProvider.getLogger(LoggingTestClass.class), logMessage);
    }

    @Test
    public void testGlobalLogger() {
        JUnitLoggerProvider.getConsoleLoggerEnablement().setEnabled(false);
        final String logMessage = "FooMessage2";
        loggingClass.logError(logMessage);
        assertErrorMessage(JUnitLoggerProvider.getGlobalLogger(), logMessage);
    }

    @Test
    public void testConsoleLogger() {
        final PrintStream oldErrStream = System.err;
        final PrintStream oldOutStream = System.out;
        final ByteArrayOutputStream systemErrorStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(systemErrorStream));

        final ByteArrayOutputStream systemOutStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutStream));

        final String logMessage = "FooMessage3";

        loggingClass.logTrace(logMessage);
        Assert.assertEquals(0, systemErrorStream.size());
        Assert.assertEquals(0, systemOutStream.size());

        loggingClass.logDebug(logMessage);
        Assert.assertEquals(0, systemErrorStream.size());
        Assert.assertEquals(0, systemOutStream.size());

        loggingClass.logInfo(logMessage);
        Assert.assertEquals(0, systemErrorStream.size());
        Assert.assertEquals(0, systemOutStream.size());

        loggingClass.logWarning(logMessage);
        Assert.assertEquals(0, systemErrorStream.size());
        Assert.assertTrue(systemOutStream.toString().contains(logMessage));

        loggingClass.logError(logMessage);
        Assert.assertTrue(systemErrorStream.toString().contains(logMessage));

        System.setOut(oldOutStream);
        System.setErr(oldErrStream);
    }

    @Test
    public void testConsoleLoggerTraceEnablement() {

        final PrintStream oldOutStream = System.out;
        ByteArrayOutputStream systemOutStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutStream));

        final String logMessage = "FooMessage4";

        loggingClass.logTrace(logMessage);
        Assert.assertEquals(0, systemOutStream.size());

        JUnitLoggerProvider.getConsoleLoggerEnablement().setTraceEnabled(true);
        loggingClass.logTrace(logMessage);
        Assert.assertTrue(systemOutStream.toString().contains(logMessage));

        systemOutStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutStream));
        JUnitLoggerProvider.getConsoleLoggerEnablement().setTraceEnabled(false);
        loggingClass.logTrace(logMessage);
        Assert.assertEquals(0, systemOutStream.size());

        System.setOut(oldOutStream);
    }

    @Test
    public void testConsoleLoggerDebugEnablement() {

        final PrintStream oldOutStream = System.out;
        ByteArrayOutputStream systemOutStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutStream));

        final String logMessage = "FooMessage5";

        loggingClass.logDebug(logMessage);
        Assert.assertEquals(0, systemOutStream.size());

        JUnitLoggerProvider.getConsoleLoggerEnablement().setDebugEnabled(true);
        loggingClass.logDebug(logMessage);
        Assert.assertTrue(systemOutStream.toString().contains(logMessage));

        systemOutStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutStream));
        JUnitLoggerProvider.getConsoleLoggerEnablement().setDebugEnabled(false);
        loggingClass.logDebug(logMessage);
        Assert.assertEquals(0, systemOutStream.size());

        System.setOut(oldOutStream);
    }

    @Test
    public void testConsoleLoggerInfoEnablement() {

        final PrintStream oldOutStream = System.out;
        ByteArrayOutputStream systemOutStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutStream));

        final String logMessage = "FooMessage6";

        loggingClass.logInfo(logMessage);
        Assert.assertEquals(0, systemOutStream.size());

        JUnitLoggerProvider.getConsoleLoggerEnablement().setInfoEnabled(true);
        loggingClass.logInfo(logMessage);
        Assert.assertTrue(systemOutStream.toString().contains(logMessage));

        systemOutStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutStream));
        JUnitLoggerProvider.getConsoleLoggerEnablement().setInfoEnabled(false);
        loggingClass.logInfo(logMessage);
        Assert.assertEquals(0, systemOutStream.size());

        System.setOut(oldOutStream);
    }

    @Test
    public void testConsoleLoggerWarnEnablement() {

        final PrintStream oldOutStream = System.out;
        ByteArrayOutputStream systemOutStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutStream));

        final String logMessage = "FooMessage7";

        loggingClass.logWarning(logMessage);
        Assert.assertTrue(systemOutStream.toString().contains(logMessage));

        systemOutStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutStream));
        JUnitLoggerProvider.getConsoleLoggerEnablement().setWarnEnabled(false);
        loggingClass.logWarning(logMessage);
        Assert.assertEquals(0, systemOutStream.size());

        systemOutStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutStream));
        JUnitLoggerProvider.getConsoleLoggerEnablement().setWarnEnabled(true);
        loggingClass.logWarning(logMessage);
        Assert.assertTrue(systemOutStream.toString().contains(logMessage));

        System.setOut(oldOutStream);
    }

    @Test
    public void testConsoleLoggerErrorEnablement() {

        final PrintStream oldErrStream = System.err;
        ByteArrayOutputStream systemErrorStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(systemErrorStream));

        final String logMessage = "FooMessage8";

        loggingClass.logError(logMessage);
        Assert.assertTrue(systemErrorStream.toString().contains(logMessage));

        systemErrorStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(systemErrorStream));
        JUnitLoggerProvider.getConsoleLoggerEnablement().setErrorEnabled(false);
        loggingClass.logError(logMessage);
        Assert.assertEquals(0, systemErrorStream.size());

        systemErrorStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(systemErrorStream));
        JUnitLoggerProvider.getConsoleLoggerEnablement().setErrorEnabled(true);
        loggingClass.logError(logMessage);
        Assert.assertTrue(systemErrorStream.toString().contains(logMessage));

        System.setErr(oldErrStream);
    }

    @Test
    public void testConsoleLoggerEnablementForMultipleThread() throws InterruptedException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);

        final LogEnablementRunnable runnable1 = new LogEnablementRunnable(latch1);
        final LogEnablementRunnable runnable2 = new LogEnablementRunnable(latch2);

        final LoggerEnablement enablement = JUnitLoggerProvider.getConsoleLoggerEnablement();
        enablement.setTraceEnabled(true);

        new Thread(runnable1).start();
        latch1.await();
        final LoggerEnablement enablement1 = runnable1.enablement;
        Assert.assertNotSame(enablement, enablement1);

        new Thread(runnable2).start();
        latch2.await();
        final LoggerEnablement enablement2 = runnable2.enablement;
        Assert.assertNotSame(enablement, enablement2);
        Assert.assertNotSame(enablement1, enablement2);

        Assert.assertTrue(enablement.isTraceEnabled());
        Assert.assertFalse(enablement1.isTraceEnabled());
        Assert.assertFalse(enablement2.isTraceEnabled());

        enablement.setTraceEnabled(false);
        enablement1.setTraceEnabled(true);
        enablement2.setTraceEnabled(true);

        Assert.assertFalse(enablement.isTraceEnabled());
        Assert.assertTrue(enablement1.isTraceEnabled());
        Assert.assertTrue(enablement2.isTraceEnabled());
    }

    private class LogEnablementRunnable implements Runnable {

        private final CountDownLatch latch;

        private LoggerEnablement enablement;

        LogEnablementRunnable(final CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            enablement = JUnitLoggerProvider.getConsoleLoggerEnablement();
            latch.countDown();
        }
    }

    @Test
    public void testGlobalLoggerForMultipleThreads() throws InterruptedException {
        final String logMessage1 = "MultiMessage1";
        final String logMessage2 = "MultiMessage2";
        final String logMessage3 = "MultiMessage3";

        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);

        final LogRunnable logRunnable1 = new LogRunnable(logMessage1, latch1);
        final LogRunnable logRunnable2 = new LogRunnable(logMessage2, latch2);
        final LogRunnable logRunnable3 = new LogRunnable(logMessage3, latch3);

        new Thread(logRunnable1).start();
        latch1.await();
        assertErrorMessage(logRunnable1.loggerOfThread, logMessage1);

        new Thread(logRunnable2).start();
        latch2.await();
        assertErrorMessage(logRunnable2.loggerOfThread, logMessage2);

        new Thread(logRunnable3).start();
        latch3.await();
        assertErrorMessage(logRunnable3.loggerOfThread, logMessage3);

        Assert.assertEquals(logRunnable1.loggerOfThread.getMessageCount(), 1);
        Assert.assertEquals(logRunnable2.loggerOfThread.getMessageCount(), 1);
        Assert.assertEquals(logRunnable3.loggerOfThread.getMessageCount(), 1);
    }

    private void assertErrorMessage(final JUnitLogger logger, final String message) {
        final LogMessage lastMessage = logger.peekLastMessage();
        Assert.assertNotNull(lastMessage);
        Assert.assertEquals(LogLevel.ERROR, lastMessage.getLevel());
        Assert.assertEquals(message, lastMessage.getMessage());
    }

    private class LogRunnable implements Runnable {

        private final String logMessage;
        private final CountDownLatch latch;

        private JUnitLogger loggerOfThread;

        LogRunnable(final String logMessage, final CountDownLatch latch) {
            this.logMessage = logMessage;
            this.latch = latch;
        }

        @Override
        public void run() {
            JUnitLoggerProvider.getConsoleLoggerEnablement().setEnabled(false);
            new LoggingTestClass().logError(logMessage);
            loggerOfThread = JUnitLoggerProvider.getGlobalLogger();
            latch.countDown();
        }

    }
}
