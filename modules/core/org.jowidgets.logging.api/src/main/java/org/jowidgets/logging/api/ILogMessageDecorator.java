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
 * Decorates log messages to an original logger.
 * 
 * For each logging method of a {@link ILogger} there is log method that has
 * the original logger as parameter to allow to decorate and filter log messages.
 * 
 * @see {@linkplain ISuppressingLogMessageDecoratorBuilder} as an example of an log message decorator
 */
public interface ILogMessageDecorator {

    void error(String message, ILogger original);

    void error(Throwable throwable, ILogger original);

    void error(String message, Throwable throwable, ILogger original);

    void warn(String message, ILogger original);

    void warn(Throwable throwable, ILogger original);

    void warn(String message, Throwable throwable, ILogger original);

    void info(String message, ILogger original);

    void info(Throwable throwable, ILogger original);

    void info(String message, Throwable throwable, ILogger original);

    void debug(String message, ILogger original);

    void debug(Throwable throwable, ILogger original);

    void debug(String message, Throwable throwable, ILogger original);

    void trace(String message, ILogger original);

    void trace(Throwable throwable, ILogger original);

    void trace(String message, Throwable throwable, ILogger original);

}
