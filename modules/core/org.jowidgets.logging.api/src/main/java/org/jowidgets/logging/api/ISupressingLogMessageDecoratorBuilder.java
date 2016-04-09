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

import java.util.concurrent.TimeUnit;

import org.jowidgets.util.ISystemTimeProvider;

/**
 * This decorator removes log messages if they occur more often than defined max period.
 * 
 * Create a instance for each message type you want to deduce log message amount for
 * 
 */
public interface ISupressingLogMessageDecoratorBuilder {

    /**
     * If set to true, a message will be appended that some messages will be suppressed
     * 
     * @param appendMessage The flag to set
     * 
     * @return This builder
     */
    ISupressingLogMessageDecoratorBuilder appendMessage(boolean appendMessage);

    /**
     * Sets the maximal period log messages should be logged.
     * 
     * @param period The max period to use
     * @param timeUnit The time unit
     * 
     * @return This builder
     */
    ISupressingLogMessageDecoratorBuilder setMaxPeriod(long period, TimeUnit timeUnit);

    /**
     * Sets the maximal period in milliseconds log messages should be logged.
     * 
     * @param period The max period in milliseconds to use
     * 
     * @return This builder
     */
    ISupressingLogMessageDecoratorBuilder setMaxPeriod(long period);

    /**
     * Sets the system time provider the logger uses for system time
     * 
     * @param systemTimeProvider The system time provider to use
     * 
     * @return This builder
     */
    ISupressingLogMessageDecoratorBuilder setSystemTimeProvider(ISystemTimeProvider systemTimeProvider);

    /**
     * Creates a new log message decorator
     * 
     * @return A new decorator
     */
    ILogMessageDecorator build();

}
