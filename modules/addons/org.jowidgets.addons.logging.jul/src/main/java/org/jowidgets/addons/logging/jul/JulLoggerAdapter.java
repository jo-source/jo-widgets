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

import org.jowidgets.logging.api.api.ILogger;
import org.jowidgets.util.Assert;

final class JulLoggerAdapter implements ILogger {

    private static final String FQCN = JulLoggerAdapter.class.getName();

    private final Logger original;

    public JulLoggerAdapter(final Logger original) {
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
    public void error(final String message) {
        log(Level.SEVERE, message);
    }

    @Override
    public void warn(final String message) {
        log(Level.WARNING, message);
    }

    @Override
    public void info(final String message) {
        log(Level.INFO, message);
    }

    @Override
    public void debug(final String message) {
        log(Level.FINE, message);
    }

    @Override
    public void trace(final String message) {
        log(Level.FINEST, message);
    }

    @Override
    public void error(final Throwable throwable) {
        log(Level.SEVERE, throwable);
    }

    @Override
    public void warn(final Throwable throwable) {
        log(Level.WARNING, throwable);
    }

    @Override
    public void info(final Throwable throwable) {
        log(Level.INFO, throwable);
    }

    @Override
    public void debug(final Throwable throwable) {
        log(Level.FINE, throwable);
    }

    @Override
    public void trace(final Throwable throwable) {
        log(Level.FINEST, throwable);
    }

    @Override
    public void error(final String message, final Throwable throwable) {
        log(Level.SEVERE, message, throwable);
    }

    @Override
    public void warn(final String message, final Throwable throwable) {
        log(Level.WARNING, message, throwable);
    }

    @Override
    public void info(final String message, final Throwable throwable) {
        log(Level.INFO, message, throwable);
    }

    @Override
    public void debug(final String message, final Throwable throwable) {
        log(Level.FINE, message, throwable);
    }

    @Override
    public void trace(final String message, final Throwable throwable) {
        log(Level.FINEST, message, throwable);
    }

    private void log(final Level level, final Throwable throwable) {
        log(level, null, throwable);
    }

    private void log(final Level level, final String message) {
        log(level, message, null);
    }

    private void log(final Level level, final String message, final Throwable throwable) {
        final LogRecord record = new LogRecord(level, message);
        record.setLoggerName(original.getName());
        record.setThrown(throwable);
        fillCallerDataToRecord(record);
        original.log(record);

    }

    private void fillCallerDataToRecord(final LogRecord record) {
        final StackTraceElement[] steArray = new Throwable().getStackTrace();

        int selfIndex = -1;
        for (int i = 0; i < steArray.length; i++) {
            final String className = steArray[i].getClassName();
            if (className.equals(FQCN)) {
                selfIndex = i;
                break;
            }
        }

        int found = -1;
        for (int i = selfIndex + 1; i < steArray.length; i++) {
            final String className = steArray[i].getClassName();
            if (!(className.equals(FQCN))) {
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
