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

package org.jowidgets.logging.tools;

import org.jowidgets.logging.api.ILogger;
import org.jowidgets.util.Assert;

/**
 * Composite pattern for the JUnitLoggers.
 */
public final class JUnitLoggerComposite implements ILogger {

    private final JUnitThreadLocalLogger<JUnitLogger> localLogger;
    private final JUnitThreadLocalLogger<JUnitLogger> globalLogger;
    private final JUnitConsoleLogger consoleLogger;

    JUnitLoggerComposite(
        final JUnitThreadLocalLogger<JUnitLogger> localLogger,
        final JUnitThreadLocalLogger<JUnitLogger> globalLogger,
        final JUnitConsoleLogger consoleLogger) {

        Assert.paramNotNull(localLogger, "localLogger");
        Assert.paramNotNull(globalLogger, "globalLogger");
        Assert.paramNotNull(consoleLogger, "consoleLogger");

        this.localLogger = localLogger;
        this.globalLogger = globalLogger;
        this.consoleLogger = consoleLogger;
    }

    /**
     * Gets the local logger part of this composite for the current thread.
     * 
     * The local logger is the specific logger for the 'name' and 'wrapperFQCN' used at creation and retrieves log messages for
     * the current thread.
     * 
     * @return The local logger, never null.
     */
    public JUnitLogger getLocalLogger() {
        return localLogger.getLoggerForThread();
    }

    /**
     * Gets the global logger part of this composite for the current thread.
     * 
     * The global logger retrieves any log message that will be logged from any local logger for the current
     * thread.
     * 
     * @return The global logger, never null.
     */
    public JUnitLogger getGlobalLogger() {
        return globalLogger.getLoggerForThread();
    }

    /**
     * Get's the console logger part of this composite
     * 
     * @return The console logger, never null
     */
    public JUnitConsoleLogger getConsoleLogger() {
        return consoleLogger;
    }

    /**
     * Sets the enabled state for all log levels
     * 
     * @param enabled The enabled state to set
     */
    public void setEnabled(final boolean enabled) {
        setErrorEnabled(enabled);
        setWarnEnabled(enabled);
        setInfoEnabled(enabled);
        setDebugEnabled(enabled);
        setTraceEnabled(enabled);
    }

    /**
     * Sets the enabled state for trace logs
     * 
     * @param enabled The state to set
     */
    public void setTraceEnabled(final boolean enabled) {
        localLogger.getLoggerForThread().setTraceEnabled(enabled);
        globalLogger.getLoggerForThread().setTraceEnabled(enabled);
        consoleLogger.setTraceEnabled(enabled);
    }

    /**
     * Sets the enabled state for debug logs
     * 
     * @param enabled The state to set
     */
    public void setDebugEnabled(final boolean enabled) {
        localLogger.getLoggerForThread().setDebugEnabled(enabled);
        globalLogger.getLoggerForThread().setDebugEnabled(enabled);
        consoleLogger.setDebugEnabled(enabled);
    }

    /**
     * Sets the enabled state for info logs
     * 
     * @param enabled The state to set
     */
    public void setInfoEnabled(final boolean enabled) {
        localLogger.getLoggerForThread().setInfoEnabled(enabled);
        globalLogger.getLoggerForThread().setInfoEnabled(enabled);
        consoleLogger.setInfoEnabled(enabled);
    }

    /**
     * Sets the enabled state for warn logs
     * 
     * @param enabled The state to set
     */
    public void setWarnEnabled(final boolean enabled) {
        localLogger.getLoggerForThread().setWarnEnabled(enabled);
        globalLogger.getLoggerForThread().setWarnEnabled(enabled);
        consoleLogger.setWarnEnabled(enabled);
    }

    /**
     * Sets the enabled state for error logs
     * 
     * @param enabled The state to set
     */
    public void setErrorEnabled(final boolean enabled) {
        localLogger.getLoggerForThread().setErrorEnabled(enabled);
        globalLogger.getLoggerForThread().setErrorEnabled(enabled);
        consoleLogger.setErrorEnabled(enabled);
    }

    /**
     * @return True, is at least for one logger trace is enabled
     */
    @Override
    public boolean isTraceEnabled() {
        return localLogger.isTraceEnabled() || globalLogger.isTraceEnabled() || consoleLogger.isTraceEnabled();
    }

    /**
     * @return True, is at least for one logger debug is enabled
     */
    @Override
    public boolean isDebugEnabled() {
        return localLogger.isDebugEnabled() || globalLogger.isDebugEnabled() || consoleLogger.isDebugEnabled();
    }

    /**
     * @return True, is at least for one logger info is enabled
     */
    @Override
    public boolean isInfoEnabled() {
        return localLogger.isInfoEnabled() || globalLogger.isInfoEnabled() || consoleLogger.isInfoEnabled();
    }

    /**
     * @return True, is at least for one logger warn is enabled
     */
    @Override
    public boolean isWarnEnabled() {
        return localLogger.isWarnEnabled() || globalLogger.isWarnEnabled() || consoleLogger.isWarnEnabled();
    }

    /**
     * @return True, is at least for one logger error is enabled
     */
    @Override
    public boolean isErrorEnabled() {
        return localLogger.isErrorEnabled() || globalLogger.isErrorEnabled() || consoleLogger.isErrorEnabled();
    }

    @Override
    public void error(final String message) {
        localLogger.error(message);
        globalLogger.error(message);
        consoleLogger.error(message);
    }

    @Override
    public void warn(final String message) {
        localLogger.warn(message);
        globalLogger.warn(message);
        consoleLogger.warn(message);
    }

    @Override
    public void info(final String message) {
        localLogger.info(message);
        globalLogger.info(message);
        consoleLogger.info(message);
    }

    @Override
    public void debug(final String message) {
        localLogger.debug(message);
        globalLogger.debug(message);
        consoleLogger.debug(message);
    }

    @Override
    public void trace(final String message) {
        localLogger.trace(message);
        globalLogger.trace(message);
        consoleLogger.trace(message);
    }

    @Override
    public void error(final Throwable throwable) {
        localLogger.error(throwable);
        globalLogger.error(throwable);
        consoleLogger.error(throwable);
    }

    @Override
    public void warn(final Throwable throwable) {
        localLogger.warn(throwable);
        globalLogger.warn(throwable);
        consoleLogger.warn(throwable);
    }

    @Override
    public void info(final Throwable throwable) {
        localLogger.info(throwable);
        globalLogger.info(throwable);
        consoleLogger.info(throwable);
    }

    @Override
    public void debug(final Throwable throwable) {
        localLogger.debug(throwable);
        globalLogger.debug(throwable);
        consoleLogger.debug(throwable);
    }

    @Override
    public void trace(final Throwable throwable) {
        localLogger.trace(throwable);
        globalLogger.trace(throwable);
        consoleLogger.trace(throwable);
    }

    @Override
    public void error(final String message, final Throwable throwable) {
        localLogger.error(message, throwable);
        globalLogger.error(message, throwable);
        consoleLogger.error(message, throwable);
    }

    @Override
    public void warn(final String message, final Throwable throwable) {
        localLogger.warn(message, throwable);
        globalLogger.warn(message, throwable);
        consoleLogger.warn(message, throwable);
    }

    @Override
    public void info(final String message, final Throwable throwable) {
        localLogger.info(message, throwable);
        globalLogger.info(message, throwable);
        consoleLogger.info(message, throwable);
    }

    @Override
    public void debug(final String message, final Throwable throwable) {
        localLogger.debug(message, throwable);
        globalLogger.debug(message, throwable);
        consoleLogger.debug(message, throwable);
    }

    @Override
    public void trace(final String message, final Throwable throwable) {
        localLogger.trace(message, throwable);
        globalLogger.trace(message, throwable);
        consoleLogger.trace(message, throwable);
    }

}
