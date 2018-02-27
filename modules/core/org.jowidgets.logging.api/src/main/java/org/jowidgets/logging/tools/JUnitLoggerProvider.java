/*
 * Copyright (c) 2018, grossmann
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

import org.jowidgets.logging.api.ILogger;
import org.jowidgets.logging.api.ILoggerProvider;
import org.jowidgets.logging.api.LoggerProvider;
import org.jowidgets.util.Assert;
import org.jowidgets.util.IFactory;

/**
 * Logger provider that can be used for JUnit tests.
 * 
 * The provider uses the class {@link JUnitLogger} for logging that can be verified for expected log messages.
 * 
 * It is common practice to use static loggers in java, so after a class was loaded, the logger for the class will never be
 * changed again even if the logging api retrieves new logging registrations. Because of that, this provider should be registered
 * before any other class was loaded whose logging should be verified.
 * 
 * Remark: To allow parallel tests, the loggers are thread local which means, each thread has it's own logger. When verifying
 * logging aspects, the logger to verify must be retrieved in the same thread the logging occurs. When modifying the console
 * logger, the logger must be retrieved in the same thread the log levels should changed for.
 */
public final class JUnitLoggerProvider implements ILoggerProvider {

    private static JUnitLoggerProvider instance;

    private final JUnitThreadLocalLoggerEnablement<LoggerEnablement> consoleLoggerEnablement;
    private final JUnitThreadLocalLogger<JUnitLogger> globalLogger;
    private final DefaultLoggerProvider localLoggerProvider;
    private final DefaultLoggerProvider consoleLoggerProvider;
    private final DefaultLoggerProvider loggerCompositeProvider;

    private JUnitLoggerProvider() {
        this.consoleLoggerEnablement = new JUnitThreadLocalLoggerEnablement<LoggerEnablement>(new LoggerEnablementFactory());
        this.globalLogger = new JUnitThreadLocalLogger<JUnitLogger>(JUnitLogger.factory());
        this.localLoggerProvider = new DefaultLoggerProvider(new JUnitThreadLocalLoggerFactory());
        this.consoleLoggerProvider = new DefaultLoggerProvider(new JUnitConsoleLoggerFactory());
        this.loggerCompositeProvider = new DefaultLoggerProvider(new LoggerCompositeFactory());

    }

    @Override
    public JUnitLoggerComposite get(final String name, final String wrapperFQCN) {
        return (JUnitLoggerComposite) loggerCompositeProvider.get(name, wrapperFQCN);
    }

    /**
     * Resets all loggers for the current thread and ensure this provider is registered if not yet done.
     * Furthermore, the console logger enablement will be reset, so only errors and warning will be logged to console.
     */
    public static void reset() {
        getGlobalLogger().reset();
        resetLoggerEnablement(getInstance().consoleLoggerEnablement.getLoggerEnablementForThread());
        for (final ILogger logger : getInstance().loggerCompositeProvider.getLoggers()) {
            final JUnitLoggerComposite loggerComposite = (JUnitLoggerComposite) logger;
            loggerComposite.getLocalLogger().reset();
        }
    }

    /**
     * Initializes and registers this logger provider to the logging framework.
     * 
     * Remark that the logger provider will be added so other registrations remain unchanged.
     */
    private static synchronized void initIfNotDone() {
        if (instance == null) {
            instance = new JUnitLoggerProvider();
            LoggerProvider.addLoggerProvider(instance);
        }
    }

    static JUnitLoggerProvider getInstance() {
        initIfNotDone();
        return instance;
    }

    DefaultLoggerProvider getLoggerCompositeProvider() {
        return loggerCompositeProvider;
    }

    /**
     * Gets the global logger for the current thread.
     * 
     * The global logger retrieves all log message that will be logged from any local logger for the current
     * thread.
     * 
     * @return The global logger, never null.
     */
    public static JUnitLogger getGlobalLogger() {
        return getInstance().globalLogger.getLoggerForThread();
    }

    /**
     * Gets the logger for a given logger name and wrapperFQCN for the current thread.
     * 
     * @param name The name to get the logger for
     * @param wrapperFQCN The full qualified class name of the wrapper if the created logger will be
     *            used by a logger wrapper. May be null. The wrapper class name will be used to correctly
     *            determine the calling method of the logger log method omitting the wrapper class
     * 
     * @return The local logger, never null
     */
    @SuppressWarnings("unchecked")
    public static JUnitLogger getLogger(final String name, final String wrapperFQCN) {
        return ((JUnitThreadLocalLogger<JUnitLogger>) getInstance().localLoggerProvider.get(name, wrapperFQCN))
                .getLoggerForThread();
    }

    /**
     * Gets the logger for a given class name for the current thread.
     * 
     * @param clazz The class name to get the logger for
     * 
     * @return The logger, never null
     */
    public static JUnitLogger getLogger(final Class<?> clazz) {
        Assert.paramNotNull(clazz, "clazz");
        return getLogger(clazz.getName(), null);
    }

    /**
     * Gets the logger enablement for all console loggers of the current thread.
     * 
     * @return The logger enablement, never null
     */
    public static LoggerEnablement getConsoleLoggerEnablement() {
        return getInstance().consoleLoggerEnablement.getLoggerEnablementForThread();
    }

    /**
     * Get's the logger composite that will be provided to the logging framework by this provider.
     * 
     * @param name The name to get the logger for
     * @param wrapperFQCN The full qualified class name of the wrapper if the created logger will be
     *            used by a logger wrapper. May be null. The wrapper class name will be used to correctly
     *            determine the calling method of the logger log method omitting the wrapper class
     * 
     * @return The local logger, never null
     */
    public static JUnitLoggerComposite getLoggerComposite(final String name, final String wrapperFQCN) {
        return getInstance().get(name, wrapperFQCN);
    }

    private static void resetLoggerEnablement(final LoggerEnablement enablement) {
        enablement.setErrorEnabled(true);
        enablement.setWarnEnabled(true);
        enablement.setInfoEnabled(false);
        enablement.setDebugEnabled(false);
        enablement.setTraceEnabled(false);
    }

    private class LoggerEnablementFactory implements IFactory<LoggerEnablement> {
        @Override
        public LoggerEnablement create() {
            final LoggerEnablement result = new LoggerEnablement();
            resetLoggerEnablement(result);
            return result;
        }
    }

    private class JUnitThreadLocalLoggerFactory implements ILoggerFactory {
        @Override
        public ILogger create(final String name, final String wrapperFQCN) {
            return new JUnitThreadLocalLogger<JUnitLogger>(JUnitLogger.factory());
        }
    }

    private class JUnitConsoleLoggerFactory implements ILoggerFactory {
        @Override
        public ILogger create(final String name, final String wrapperFQCN) {
            return new JUnitConsoleLogger(name, consoleLoggerEnablement);
        }
    }

    private class LoggerCompositeFactory implements ILoggerFactory {
        @SuppressWarnings("unchecked")
        @Override
        public ILogger create(final String name, final String wrapperFQCN) {
            return new JUnitLoggerComposite(
                (JUnitThreadLocalLogger<JUnitLogger>) localLoggerProvider.get(name, wrapperFQCN),
                globalLogger,
                (JUnitConsoleLogger) consoleLoggerProvider.get(name, wrapperFQCN));

        }
    }
}
