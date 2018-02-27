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

/**
 * Console logger that logs all log levels
 */
public class ConsoleLogger extends AbstractConsoleLoggerAdapter implements ILogger {

    public ConsoleLogger(final String name) {
        super(name);
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public final void error(final String wrapperFQCN, final String message, final Throwable throwable) {
        if (isErrorEnabled()) {
            logMessage(LogLevel.ERROR, message, throwable);
        }
    }

    @Override
    public final void warn(final String wrapperFQCN, final String message, final Throwable throwable) {
        if (isWarnEnabled()) {
            logMessage(LogLevel.WARN, message, throwable);
        }
    }

    @Override
    public final void info(final String wrapperFQCN, final String message, final Throwable throwable) {
        if (isInfoEnabled()) {
            logMessage(LogLevel.INFO, message, throwable);
        }
    }

    @Override
    public final void debug(final String wrapperFQCN, final String message, final Throwable throwable) {
        if (isDebugEnabled()) {
            logMessage(LogLevel.DEBUG, message, throwable);
        }
    }

    @Override
    public final void trace(final String wrapperFQCN, final String message, final Throwable throwable) {
        if (isTraceEnabled()) {
            logMessage(LogLevel.TRACE, message, throwable);
        }
    }

}
