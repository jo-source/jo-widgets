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
import org.jowidgets.util.IFactory;

final class JUnitThreadLocalLogger<LOGGER_TYPE extends ILogger> implements ILogger {

    private final IFactory<LOGGER_TYPE> loggerFactory;
    private final ThreadLocal<LOGGER_TYPE> threadLocal;

    JUnitThreadLocalLogger(final IFactory<LOGGER_TYPE> loggerFactory) {
        Assert.paramNotNull(loggerFactory, "loggerFactory");
        this.loggerFactory = loggerFactory;
        this.threadLocal = new ThreadLocal<LOGGER_TYPE>();
    }

    LOGGER_TYPE getLoggerForThread() {
        LOGGER_TYPE result = threadLocal.get();
        if (result == null) {
            result = loggerFactory.create();
            threadLocal.set(result);
        }
        return result;
    }

    @Override
    public boolean isErrorEnabled() {
        return getLoggerForThread().isErrorEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return getLoggerForThread().isWarnEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return getLoggerForThread().isInfoEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return getLoggerForThread().isDebugEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return getLoggerForThread().isTraceEnabled();
    }

    @Override
    public void error(final String message) {
        getLoggerForThread().error(message);
    }

    @Override
    public void error(final Throwable throwable) {
        getLoggerForThread().error(throwable);
    }

    @Override
    public void error(final String message, final Throwable throwable) {
        getLoggerForThread().error(message, throwable);
    }

    @Override
    public void warn(final String message) {
        getLoggerForThread().warn(message);
    }

    @Override
    public void warn(final Throwable throwable) {
        getLoggerForThread().warn(throwable);
    }

    @Override
    public void warn(final String message, final Throwable throwable) {
        getLoggerForThread().warn(message, throwable);
    }

    @Override
    public void info(final String message) {
        getLoggerForThread().info(message);
    }

    @Override
    public void info(final Throwable throwable) {
        getLoggerForThread().info(throwable);
    }

    @Override
    public void info(final String message, final Throwable throwable) {
        getLoggerForThread().info(message, throwable);
    }

    @Override
    public void debug(final String message) {
        getLoggerForThread().debug(message);
    }

    @Override
    public void debug(final Throwable throwable) {
        getLoggerForThread().debug(throwable);
    }

    @Override
    public void debug(final String message, final Throwable throwable) {
        getLoggerForThread().debug(message, throwable);
    }

    @Override
    public void trace(final String message) {
        getLoggerForThread().trace(message);
    }

    @Override
    public void trace(final Throwable throwable) {
        getLoggerForThread().trace(throwable);
    }

    @Override
    public void trace(final String message, final Throwable throwable) {
        getLoggerForThread().trace(message, throwable);
    }

}
