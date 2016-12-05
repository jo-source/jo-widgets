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

public abstract class AbstractLoggerAdapter implements ILogger {

    private static final String THIS_WRAPPER_FQCN = AbstractLoggerAdapter.class.getName();

    private final String wrapperFQCN;

    protected AbstractLoggerAdapter() {
        this(null);
    }

    protected AbstractLoggerAdapter(final String wrapperFQCN) {
        this.wrapperFQCN = wrapperFQCN != null ? wrapperFQCN : THIS_WRAPPER_FQCN;
    }

    protected abstract void error(String wrapperFQCN, String message, Throwable throwable);

    protected abstract void warn(String wrapperFQCN, String message, Throwable throwable);

    protected abstract void info(String wrapperFQCN, String message, Throwable throwable);

    protected abstract void debug(String wrapperFQCN, String message, Throwable throwable);

    protected abstract void trace(String wrapperFQCN, String message, Throwable throwable);

    @Override
    public final void error(final String message) {
        error(wrapperFQCN, message, null);
    }

    @Override
    public final void error(final Throwable throwable) {
        error(wrapperFQCN, null, throwable);
    }

    @Override
    public final void error(final String message, final Throwable throwable) {
        error(wrapperFQCN, message, throwable);
    }

    @Override
    public final void warn(final String message) {
        warn(wrapperFQCN, message, null);
    }

    @Override
    public final void warn(final Throwable throwable) {
        warn(wrapperFQCN, null, throwable);
    }

    @Override
    public final void warn(final String message, final Throwable throwable) {
        warn(wrapperFQCN, message, throwable);
    }

    @Override
    public final void info(final String message) {
        info(wrapperFQCN, message, null);
    }

    @Override
    public final void info(final Throwable throwable) {
        info(wrapperFQCN, null, throwable);
    }

    @Override
    public final void info(final String message, final Throwable throwable) {
        info(wrapperFQCN, message, throwable);
    }

    @Override
    public final void debug(final String message) {
        debug(wrapperFQCN, message, null);
    }

    @Override
    public final void debug(final Throwable throwable) {
        debug(wrapperFQCN, null, throwable);
    }

    @Override
    public final void debug(final String message, final Throwable throwable) {
        debug(wrapperFQCN, message, throwable);
    }

    @Override
    public final void trace(final String message) {
        trace(wrapperFQCN, message, null);
    }

    @Override
    public final void trace(final Throwable throwable) {
        trace(wrapperFQCN, null, throwable);
    }

    @Override
    public final void trace(final String message, final Throwable throwable) {
        trace(wrapperFQCN, message, throwable);
    }

}
