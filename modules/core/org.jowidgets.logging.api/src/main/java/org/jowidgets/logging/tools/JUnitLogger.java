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

import java.util.LinkedList;

import org.jowidgets.logging.api.ILogger;
import org.jowidgets.util.IFactory;

/**
 * Logger that can be used for JUnit tests to verify logging aspects
 */
public final class JUnitLogger extends AbstractLoggerAdapter implements ILogger {

    private static final Factory FACTORY = new Factory();

    private boolean traceEnabled;
    private boolean debugEnabled;
    private boolean infoEnabled;
    private boolean warnEnabled;
    private boolean errorEnabled;

    private final LinkedList<LogMessage> messages;

    /**
     * Creates a new logger that have all log levels enabled
     */
    public JUnitLogger() {
        this.traceEnabled = true;
        this.debugEnabled = true;
        this.infoEnabled = true;
        this.warnEnabled = true;
        this.errorEnabled = true;
        this.messages = new LinkedList<LogMessage>();
    }

    /**
     * Gets a factory that creates instances of this class
     * 
     * @return A factory, never null
     */
    public static IFactory<JUnitLogger> factory() {
        return FACTORY;
    }

    /**
     * Gets the last logged message without changing it, may be null
     * 
     * @return The last logged message, may be null
     */
    public synchronized LogMessage peekLastMessage() {
        return messages.peekLast();
    }

    /**
     * Removes the last logged message and return it
     * 
     * @return The last logged message, may be null
     */
    public synchronized LogMessage popLastMessage() {
        return messages.pollLast();
    }

    /**
     * Get's the number of logged messages
     * 
     * @return The number of logged messages
     */
    public synchronized int getMessageCount() {
        return messages.size();
    }

    /**
     * Resets the logger
     */
    public synchronized void reset() {
        messages.clear();
        setEnabled(true);
    }

    /**
     * @return True if a message is available
     */
    public boolean hasMessage() {
        return getMessageCount() > 0;
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

    /**
     * Sets the enabled state for trace logs
     * 
     * @param enabled The state to set
     */
    public void setTraceEnabled(final boolean enabled) {
        this.traceEnabled = enabled;
    }

    @Override
    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    /**
     * Sets the enabled state for debug logs
     * 
     * @param enabled The state to set
     */
    public void setDebugEnabled(final boolean enabled) {
        this.debugEnabled = enabled;
    }

    @Override
    public boolean isInfoEnabled() {
        return infoEnabled;
    }

    /**
     * Sets the enabled state for info logs
     * 
     * @param enabled The state to set
     */
    public void setInfoEnabled(final boolean enabled) {
        this.infoEnabled = enabled;
    }

    @Override
    public boolean isWarnEnabled() {
        return warnEnabled;
    }

    /**
     * Sets the enabled state for warn logs
     * 
     * @param enabled The state to set
     */
    public void setWarnEnabled(final boolean enabled) {
        this.warnEnabled = enabled;
    }

    @Override
    public boolean isErrorEnabled() {
        return errorEnabled;
    }

    /**
     * Sets the enabled state for error logs
     * 
     * @param enabled The state to set
     */
    public void setErrorEnabled(final boolean enabled) {
        this.errorEnabled = enabled;
    }

    @Override
    public void error(final String wrapperFQCN, final String message, final Throwable throwable) {
        logMessage(LogLevel.ERROR, message, throwable);
    }

    @Override
    public void warn(final String wrapperFQCN, final String message, final Throwable throwable) {
        logMessage(LogLevel.WARN, message, throwable);
    }

    @Override
    public void info(final String wrapperFQCN, final String message, final Throwable throwable) {
        logMessage(LogLevel.INFO, message, throwable);
    }

    @Override
    public void debug(final String wrapperFQCN, final String message, final Throwable throwable) {
        logMessage(LogLevel.DEBUG, message, throwable);
    }

    @Override
    public void trace(final String wrapperFQCN, final String message, final Throwable throwable) {
        logMessage(LogLevel.TRACE, message, throwable);
    }

    private synchronized void addMessage(final LogMessage message) {
        messages.add(message);
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
            addMessage(new LogMessage(level, message, throwable));
        }

    }

    private static class Factory implements IFactory<JUnitLogger> {

        @Override
        public JUnitLogger create() {
            return new JUnitLogger();
        }

    }
}
