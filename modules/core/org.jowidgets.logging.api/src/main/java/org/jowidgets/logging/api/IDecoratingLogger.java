/*
 * Copyright (c) 2016, MGrossmann
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

/**
 * Logger that can be used to log with using an ILogMessageDecorator.
 * 
 * For each logging method of a {@link ILogger} there is log method that has {@link ILogMessageDecorator} as parameter to allow to
 * decorate and filter the log messages.
 * 
 * A decorating logger can be obtained from the LoggerProvider, e.g. {@link LoggerProvider#getDecoratingLogger(String, String)}
 */
public interface IDecoratingLogger extends ILogger {

    void error(ILogMessageDecorator decorator, String message);

    void error(ILogMessageDecorator decorator, Throwable throwable);

    void error(ILogMessageDecorator decorator, String message, Throwable throwable);

    void warn(ILogMessageDecorator decorator, String message);

    void warn(ILogMessageDecorator decorator, Throwable throwable);

    void warn(ILogMessageDecorator decorator, String message, Throwable throwable);

    void info(ILogMessageDecorator decorator, String message);

    void info(ILogMessageDecorator decorator, Throwable throwable);

    void info(ILogMessageDecorator decorator, String message, Throwable throwable);

    void debug(ILogMessageDecorator decorator, String message);

    void debug(ILogMessageDecorator decorator, Throwable throwable);

    void debug(ILogMessageDecorator decorator, String message, Throwable throwable);

    void trace(ILogMessageDecorator decorator, String message);

    void trace(ILogMessageDecorator decorator, Throwable throwable);

    void trace(ILogMessageDecorator decorator, String message, Throwable throwable);

}
