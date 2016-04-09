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

public class LoggerWrapper implements ILogger {

    private final ILogger original;

    public LoggerWrapper(final ILogger original) {
        Assert.paramNotNull(original, "original");
        this.original = original;
    }

    public ILogger getOriginal() {
        return original;
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
        original.error(message);
    }

    @Override
    public void warn(final String message) {
        original.warn(message);
    }

    @Override
    public void info(final String message) {
        original.info(message);
    }

    @Override
    public void debug(final String message) {
        original.debug(message);
    }

    @Override
    public void trace(final String message) {
        original.trace(message);
    }

    @Override
    public void error(final Throwable throwable) {
        original.error(throwable);
    }

    @Override
    public void warn(final Throwable throwable) {
        original.warn(throwable);
    }

    @Override
    public void info(final Throwable throwable) {
        original.info(throwable);
    }

    @Override
    public void debug(final Throwable throwable) {
        original.debug(throwable);
    }

    @Override
    public void trace(final Throwable throwable) {
        original.trace(throwable);
    }

    @Override
    public void error(final String message, final Throwable throwable) {
        original.error(message, throwable);
    }

    @Override
    public void warn(final String message, final Throwable throwable) {
        original.warn(message, throwable);
    }

    @Override
    public void info(final String message, final Throwable throwable) {
        original.info(message, throwable);
    }

    @Override
    public void debug(final String message, final Throwable throwable) {
        original.debug(message, throwable);
    }

    @Override
    public void trace(final String message, final Throwable throwable) {
        original.trace(message, throwable);
    }

}
