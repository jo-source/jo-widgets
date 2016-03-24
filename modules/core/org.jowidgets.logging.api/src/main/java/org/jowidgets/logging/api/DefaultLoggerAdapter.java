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

package org.jowidgets.logging.api;

import org.jowidgets.util.Assert;
import org.jowidgets.util.EmptyCheck;

final class DefaultLoggerAdapter implements ILogger {

    private final String prefix;

    DefaultLoggerAdapter(final String name) {
        Assert.paramNotNull(name, "name");
        this.prefix = name + ": ";
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public void error(final String message) {
        logMessage(LogLevel.ERROR, message);
    }

    @Override
    public void warn(final String message) {
        logMessage(LogLevel.WARN, message);
    }

    @Override
    public void error(final Throwable throwable) {
        logMessage(LogLevel.ERROR, throwable);
    }

    @Override
    public void warn(final Throwable throwable) {
        logMessage(LogLevel.WARN, throwable);
    }

    @Override
    public void error(final String message, final Throwable throwable) {
        logMessage(LogLevel.ERROR, message, throwable);
    }

    @Override
    public void warn(final String message, final Throwable throwable) {
        logMessage(LogLevel.WARN, message, throwable);
    }

    @Override
    public void info(final String message) {}

    @Override
    public void debug(final String message) {}

    @Override
    public void trace(final String message) {}

    @Override
    public void info(final Throwable throwable) {}

    @Override
    public void debug(final Throwable throwable) {}

    @Override
    public void trace(final Throwable throwable) {}

    @Override
    public void info(final String message, final Throwable throwable) {}

    @Override
    public void debug(final String message, final Throwable throwable) {}

    @Override
    public void trace(final String message, final Throwable throwable) {}

    private void logMessage(final LogLevel level, final Throwable throwable) {
        logMessage(level, null, throwable);
    }

    private void logMessage(final LogLevel level, final String message) {
        logMessage(level, message, null);
    }

    private void logMessage(final LogLevel level, final String message, final Throwable throwable) {
        final StringBuilder builder = new StringBuilder(prefix);
        builder.append(level.toString());
        if (!EmptyCheck.isEmpty(message)) {
            builder.append(" - ");
            builder.append(message);
        }
        //CHECKSTYLE:OFF
        System.out.println(builder.toString());
        if (throwable != null) {
            throwable.printStackTrace();
        }
        //CHECKSTYLE:ON
    }

    private static enum LogLevel {
        ERROR,
        WARN;
    }

}
