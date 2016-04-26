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

import org.jowidgets.logging.tools.ConsoleLoggerProvider;
import org.jowidgets.logging.tools.ILoggerFactory;
import org.jowidgets.logging.tools.NopLoggerProvider;
import org.jowidgets.logging.tools.ThreadLocalLoggerProvider;

/**
 * Logger provider access for unit tests.
 * 
 * This access initializes a {@link ThreadLocalLoggerProvider} that provides different {@link ILoggerProvider} instances
 * for each thread was is good in junit tests where each test may be run in an separate thread when tests run in parallel.
 * 
 * If u set a LoggerMock for example for the test running thread, the LoggerMock can be used to verify if logging
 * has been done as wanted. Of course any other Mock can also be used for the interface {@link ILogger}.
 * 
 * It is recommended to set a logger provider or logger for the current thread in a method annotated with "BeforeClass" (and clear
 * in "AfterClass") to ensure that static loggers will be initialized as wanted. For non static logger, the initialization may
 * also be done in the "Before" annotated class.
 * 
 * Do not forget to clear the logger for the current thread, e.g. in an "After" or "AfterClass" annotated method after test
 * finished to avoid memory leak or side effects in other tests.
 * 
 * Important: Use this class for all junit test you run or for no one.
 */
public final class TestLoggerProvider {

    private TestLoggerProvider() {}

    /**
     * Gets the thread local logger provider for unit tests or creates one if not yet exists
     * 
     * @return The thread local logger provider, never null
     */
    public static synchronized ThreadLocalLoggerProvider get() {
        initializeThreadLocalLoggerProvider();
        return (ThreadLocalLoggerProvider) LoggerProvider.instance();
    }

    /**
     * Sets the logger provider for the current thread
     * 
     * @param provider The provider to set, must not be null
     */
    public static void setLoggerProviderForCurrentThread(final ILoggerProvider provider) {
        get().setLoggerProviderForCurrentThread(provider);
    }

    /**
     * Sets the logger provider for the current thread with help of a logger factory
     * 
     * @param factory The factory to create loggers, must not be null
     */
    public static void setLoggerProviderForCurrentThread(final ILoggerFactory factory) {
        get().setLoggerProviderForCurrentThread(factory);
    }

    /**
     * Sets the logger for the current thread.
     * 
     * This sets a logger provider that will always return the given logger for all logger names and fqcn's
     * for invocations in this thread.
     * 
     * Remark: This overrides the logger provider currently set for this thread
     * 
     * @param logger The logger to set, must not be null
     */
    public static void setLoggerForCurrentThread(final ILogger logger) {
        setLoggerProviderForCurrentThread(new ILoggerFactory() {
            @Override
            public ILogger create(final String name, final String wrapperFQCN) {
                return logger;
            }
        });
    }

    /**
     * Sets a nop logger provider for the current thread
     */
    public static void setNopLoggerForCurrentThread() {
        get().setLoggerProviderForCurrentThread(NopLoggerProvider.instance());
    }

    /**
     * Sets a console logger for the current thread
     */
    public static void setConsoleLoggerForCurrentThread() {
        setConsoleWarnLoggerForCurrentThread();
    }

    /**
     * Sets a console logger that only logs error for the current thread
     */
    public static void setConsoleErrorLoggerForCurrentThread() {
        get().setLoggerProviderForCurrentThread(ConsoleLoggerProvider.errorLoggerProvider());
    }

    /**
     * Sets a console logger that logs warnings and errors for the current thread
     */
    public static void setConsoleWarnLoggerForCurrentThread() {
        get().setLoggerProviderForCurrentThread(ConsoleLoggerProvider.warnLoggerProvider());
    }

    /**
     * Sets a console logger that logs warnings, errors and infos for the current thread
     */
    public static void setConsoleInfoLoggerForCurrentThread() {
        get().setLoggerProviderForCurrentThread(ConsoleLoggerProvider.infoLoggerProvider());
    }

    /**
     * Sets a console logger that logs warnings, errors, infos and debug for the current thread
     */
    public static void setConsoleDebugLoggerForCurrentThread() {
        get().setLoggerProviderForCurrentThread(ConsoleLoggerProvider.debugLoggerProvider());
    }

    /**
     * Sets a console logger that logs all log levels for the current thread
     */
    public static void setConsoleTraceLoggerForCurrentThread() {
        get().setLoggerProviderForCurrentThread(ConsoleLoggerProvider.traceLoggerProvider());
    }

    /**
     * Clears the logger provider for the current thread
     */
    public static void clearLoggerProviderForCurrentThread() {
        get().clearLoggerProviderForCurrentThread();
    }

    /**
     * Clears the logger for the current thread, has the same effect as {@link #clearLoggerProviderForCurrentThread()}
     */
    public static void clearLoggerForCurrentThread() {
        get().clearLoggerForCurrentThread();
    }

    private static synchronized void initializeThreadLocalLoggerProvider() {
        if (!LoggerProvider.isInitialized()) {
            LoggerProvider.setLoggerProvider(new ThreadLocalLoggerProvider());
        }
        else {
            final ILoggerProvider loggerProvider = LoggerProvider.instance();
            if (!(loggerProvider instanceof ThreadLocalLoggerProvider)) {
                LoggerProvider.setLoggerProvider(new ThreadLocalLoggerProvider());
            }
        }
    }

}
