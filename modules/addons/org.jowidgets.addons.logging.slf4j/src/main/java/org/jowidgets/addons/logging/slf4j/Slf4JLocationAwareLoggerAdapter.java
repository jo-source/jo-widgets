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

final class Slf4JLocationAwareLoggerAdapter extends AbstractSlf4JLoggerAdapter implements ILogger {

    private final LocationAwareLogger original;

    public Slf4JLocationAwareLoggerAdapter(final LocationAwareLogger original, final String wrapperFQCN) {
        super(original, wrapperFQCN);
        Assert.paramNotNull(original, "original");
        this.original = original;
    }

    @Override
    public void error(final String wrapperFQCN, final String message, final Throwable throwable) {
        original.log(null, wrapperFQCN, LocationAwareLogger.ERROR_INT, message, null, throwable);
    }

    @Override
    public void warn(final String wrapperFQCN, final String message, final Throwable throwable) {
        original.log(null, wrapperFQCN, LocationAwareLogger.WARN_INT, message, null, throwable);
    }

    @Override
    public void info(final String wrapperFQCN, final String message, final Throwable throwable) {
        original.log(null, wrapperFQCN, LocationAwareLogger.INFO_INT, message, null, throwable);
    }

    @Override
    public void debug(final String wrapperFQCN, final String message, final Throwable throwable) {
        original.log(null, wrapperFQCN, LocationAwareLogger.DEBUG_INT, message, null, throwable);
    }

    @Override
    public void trace(final String wrapperFQCN, final String message, final Throwable throwable) {
        original.log(null, wrapperFQCN, LocationAwareLogger.TRACE_INT, message, null, throwable);
    }

}
