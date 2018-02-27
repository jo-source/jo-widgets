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
import org.jowidgets.util.Assert;

/**
 * Console logger adapter that will be used by {@link JUnitLoggerProvider}.
 * 
 * This class can be used to enable or disable console logging during JUnit tests
 */
public class JUnitConsoleLogger extends ConsoleLogger implements ILogger {

    private final JUnitThreadLocalLoggerEnablement<LoggerEnablement> enablement;

    /**
     * Creates a new logger that have error and warning log levels enabled by default
     */
    public JUnitConsoleLogger(final String name, final JUnitThreadLocalLoggerEnablement<LoggerEnablement> enablement) {
        super(name);
        Assert.paramNotNull(enablement, "enablement");
        this.enablement = enablement;
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
        enablement.getLoggerEnablementForThread().setTraceEnabled(enabled);
    }

    /**
     * Sets the enabled state for debug logs
     * 
     * @param enabled The state to set
     */
    public void setDebugEnabled(final boolean enabled) {
        enablement.getLoggerEnablementForThread().setDebugEnabled(enabled);
    }

    /**
     * Sets the enabled state for info logs
     * 
     * @param enabled The state to set
     */
    public void setInfoEnabled(final boolean enabled) {
        enablement.getLoggerEnablementForThread().setInfoEnabled(enabled);
    }

    /**
     * Sets the enabled state for warn logs
     * 
     * @param enabled The state to set
     */
    public void setWarnEnabled(final boolean enabled) {
        enablement.getLoggerEnablementForThread().setWarnEnabled(enabled);
    }

    /**
     * Sets the enabled state for error logs
     * 
     * @param enabled The state to set
     */
    public void setErrorEnabled(final boolean enabled) {
        enablement.getLoggerEnablementForThread().setErrorEnabled(enabled);
    }

    @Override
    public boolean isTraceEnabled() {
        return enablement.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return enablement.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return enablement.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return enablement.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return enablement.isErrorEnabled();
    }

}
