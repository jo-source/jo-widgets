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

import org.jowidgets.logging.api.ILoggerEnablement;

/**
 * Default implementation for {@link ILoggerEnablement}
 */
public class LoggerEnablement implements ILoggerEnablement {

    private boolean traceEnabled;
    private boolean debugEnabled;
    private boolean infoEnabled;
    private boolean warnEnabled;
    private boolean errorEnabled;

    /**
     * Creates a new instance where all log levels are enabled by default
     */
    public LoggerEnablement() {
        this.traceEnabled = true;
        this.debugEnabled = true;
        this.infoEnabled = true;
        this.warnEnabled = true;
        this.errorEnabled = true;
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

}
