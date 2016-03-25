/*
 * Copyright (c) 2016, Michael
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

/**
 * Logger mock that can be used for JUnit tests
 */
public class LoggerMock implements ILogger {

    private boolean traceEnabled;
    private boolean debugEnabled;
    private boolean infoEnabled;
    private boolean warnEnabled;
    private boolean errorEnabled;

    private LoggerMockMessage lastMessage;

    /**
     * Creates a new logger that have all log levels enabled
     */
    public LoggerMock() {
        this.traceEnabled = true;
        this.debugEnabled = true;
        this.infoEnabled = true;
        this.warnEnabled = true;
        this.errorEnabled = true;
    }

    /**
     * Gets the last logged message without changing it, may be null
     * 
     * @return The last logged message, may be null
     */
    public LoggerMockMessage peekLastMessage() {
        return lastMessage;
    }

    /**
     * Removes the last logged message and return it
     * 
     * @return The last logged message, may be null
     */
    public LoggerMockMessage popLastMessage() {
        final LoggerMockMessage result = lastMessage;
        lastMessage = null;
        return result;
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

    @Override
    public boolean isTraceEnabled() {
        return traceEnabled;
    }

    public void setTraceEnabled(final boolean traceEnabled) {
        this.traceEnabled = traceEnabled;
    }

    @Override
    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(final boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    @Override
    public boolean isInfoEnabled() {
        return infoEnabled;
    }

    public void setInfoEnabled(final boolean infoEnabled) {
        this.infoEnabled = infoEnabled;
    }

    @Override
    public boolean isWarnEnabled() {
        return warnEnabled;
    }

    public void setWarnEnabled(final boolean warnEnabled) {
        this.warnEnabled = warnEnabled;
    }

    @Override
    public boolean isErrorEnabled() {
        return errorEnabled;
    }

    public void setErrorEnabled(final boolean errorEnabled) {
        this.errorEnabled = errorEnabled;
    }

    @Override
    public void error(final String message) {
        logMessage(LogLevel.ERROR, message);
    }

    @Override
    public void error(final Throwable throwable) {
        logMessage(LogLevel.ERROR, throwable);
    }

    @Override
    public void error(final String message, final Throwable throwable) {
        logMessage(LogLevel.ERROR, message, throwable);
    }

    @Override
    public void warn(final String message) {
        logMessage(LogLevel.WARN, message);
    }

    @Override
    public void warn(final Throwable throwable) {
        logMessage(LogLevel.WARN, throwable);
    }

    @Override
    public void warn(final String message, final Throwable throwable) {
        logMessage(LogLevel.WARN, message, throwable);
    }

    @Override
    public void info(final String message) {
        logMessage(LogLevel.INFO, message);
    }

    @Override
    public void info(final Throwable throwable) {
        logMessage(LogLevel.INFO, throwable);
    }

    @Override
    public void info(final String message, final Throwable throwable) {
        logMessage(LogLevel.INFO, message, throwable);
    }

    @Override
    public void debug(final String message) {
        logMessage(LogLevel.DEBUG, message);
    }

    @Override
    public void debug(final Throwable throwable) {
        logMessage(LogLevel.DEBUG, throwable);
    }

    @Override
    public void debug(final String message, final Throwable throwable) {
        logMessage(LogLevel.DEBUG, message, throwable);
    }

    @Override
    public void trace(final String message) {
        logMessage(LogLevel.TRACE, message);
    }

    @Override
    public void trace(final Throwable throwable) {
        logMessage(LogLevel.TRACE, throwable);
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
        if (LogLevel.ERROR.equals(level) && !isErrorEnabled()) {
            return;
        }
        else if (LogLevel.WARN.equals(level) && !isWarnEnabled()) {
            return;
        }
        else if (LogLevel.INFO.equals(level) && !isInfoEnabled()) {
            return;
        }
        else if (LogLevel.DEBUG.equals(level) && !isDebugEnabled()) {
            return;
        }
        else if (LogLevel.TRACE.equals(level) && !isTraceEnabled()) {
            return;
        }
        else {
            lastMessage = new LoggerMockMessage(level, message, throwable);
        }

    }
}
