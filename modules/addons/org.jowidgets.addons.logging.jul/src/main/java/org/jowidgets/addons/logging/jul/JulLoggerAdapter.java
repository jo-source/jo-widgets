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

package org.jowidgets.addons.logging.jul;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.jowidgets.logging.api.ILogger;
import org.jowidgets.logging.tools.AbstractLoggerAdapter;
import org.jowidgets.util.Assert;

final class JulLoggerAdapter extends AbstractLoggerAdapter implements ILogger {

    private final Logger original;

    JulLoggerAdapter(final Logger original, final String wrapperFQCN) {
        super(wrapperFQCN);
        Assert.paramNotNull(original, "original");
        this.original = original;
    }

    @Override
    public boolean isTraceEnabled() {
        return original.isLoggable(Level.FINEST);
    }

    @Override
    public boolean isDebugEnabled() {
        return original.isLoggable(Level.FINE);
    }

    @Override
    public boolean isInfoEnabled() {
        return original.isLoggable(Level.INFO);
    }

    @Override
    public boolean isWarnEnabled() {
        return original.isLoggable(Level.WARNING);
    }

    @Override
    public boolean isErrorEnabled() {
        return original.isLoggable(Level.SEVERE);
    }

    @Override
    public void error(final String wrapperFQCN, final String message, final Throwable throwable) {
        log(wrapperFQCN, Level.SEVERE, message, throwable);
    }

    @Override
    public void warn(final String wrapperFQCN, final String message, final Throwable throwable) {
        log(wrapperFQCN, Level.WARNING, message, throwable);
    }

    @Override
    public void info(final String wrapperFQCN, final String message, final Throwable throwable) {
        log(wrapperFQCN, Level.INFO, message, throwable);
    }

    @Override
    public void debug(final String wrapperFQCN, final String message, final Throwable throwable) {
        log(wrapperFQCN, Level.FINE, message, throwable);
    }

    @Override
    public void trace(final String wrapperFQCN, final String message, final Throwable throwable) {
        log(wrapperFQCN, Level.FINEST, message, throwable);
    }

    private void log(final String wrapperFQCN, final Level level, final String message, final Throwable throwable) {
        final LogRecord record = new LogRecord(level, message);
        record.setLoggerName(original.getName());
        record.setThrown(throwable);
        fillCallerDataToRecord(wrapperFQCN, record);
        original.log(record);

    }

    private void fillCallerDataToRecord(final String wrapperFQCN, final LogRecord record) {
        final StackTraceElement[] steArray = new Throwable().getStackTrace();

        int selfIndex = -1;
        for (int i = 0; i < steArray.length; i++) {
            final String className = steArray[i].getClassName();
            if (className.equals(wrapperFQCN)) {
                selfIndex = i;
                break;
            }
        }

        int found = -1;
        for (int i = selfIndex + 1; i < steArray.length; i++) {
            final String className = steArray[i].getClassName();
            if (!(className.equals(wrapperFQCN))) {
                found = i;
                break;
            }
        }

        if (found != -1) {
            final StackTraceElement ste = steArray[found];
            // setting the class name has the side effect of setting
            // the needToInferCaller variable to false.
            record.setSourceClassName(ste.getClassName());
            record.setSourceMethodName(ste.getMethodName());
        }
    }
}
