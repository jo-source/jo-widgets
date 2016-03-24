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

package org.jowidgets.addons.logging.slf4j;

import org.jowidgets.logging.api.ILogger;
import org.jowidgets.util.Assert;
import org.slf4j.spi.LocationAwareLogger;

final class Slf4JLocationAwareLoggerAdapter implements ILogger {

    private static final String FQCN = Slf4JLocationAwareLoggerAdapter.class.getName();

    private final LocationAwareLogger original;

    public Slf4JLocationAwareLoggerAdapter(final LocationAwareLogger original) {
        Assert.paramNotNull(original, "original");
        this.original = original;
    }

    @Override
    public boolean isTraceEnabled() {
        return original.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return original.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return original.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return original.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return original.isErrorEnabled();
    }

    @Override
    public void error(final String message) {
        original.log(null, FQCN, LocationAwareLogger.ERROR_INT, message, null, null);
    }

    @Override
    public void warn(final String message) {
        original.log(null, FQCN, LocationAwareLogger.WARN_INT, message, null, null);
    }

    @Override
    public void info(final String message) {
        original.log(null, FQCN, LocationAwareLogger.INFO_INT, message, null, null);
    }

    @Override
    public void debug(final String message) {
        original.log(null, FQCN, LocationAwareLogger.DEBUG_INT, message, null, null);
    }

    @Override
    public void trace(final String message) {
        original.log(null, FQCN, LocationAwareLogger.TRACE_INT, message, null, null);
    }

    @Override
    public void error(final Throwable throwable) {
        original.log(null, FQCN, LocationAwareLogger.ERROR_INT, null, null, throwable);
    }

    @Override
    public void warn(final Throwable throwable) {
        original.log(null, FQCN, LocationAwareLogger.WARN_INT, null, null, throwable);
    }

    @Override
    public void info(final Throwable throwable) {
        original.log(null, FQCN, LocationAwareLogger.INFO_INT, null, null, throwable);
    }

    @Override
    public void debug(final Throwable throwable) {
        original.log(null, FQCN, LocationAwareLogger.DEBUG_INT, null, null, throwable);
    }

    @Override
    public void trace(final Throwable throwable) {
        original.log(null, FQCN, LocationAwareLogger.TRACE_INT, null, null, throwable);
    }

    @Override
    public void error(final String message, final Throwable throwable) {
        original.log(null, FQCN, LocationAwareLogger.ERROR_INT, message, null, throwable);
    }

    @Override
    public void warn(final String message, final Throwable throwable) {
        original.log(null, FQCN, LocationAwareLogger.WARN_INT, message, null, throwable);
    }

    @Override
    public void info(final String message, final Throwable throwable) {
        original.log(null, FQCN, LocationAwareLogger.INFO_INT, message, null, throwable);
    }

    @Override
    public void debug(final String message, final Throwable throwable) {
        original.log(null, FQCN, LocationAwareLogger.DEBUG_INT, message, null, throwable);
    }

    @Override
    public void trace(final String message, final Throwable throwable) {
        original.log(null, FQCN, LocationAwareLogger.TRACE_INT, message, null, throwable);
    }

}
