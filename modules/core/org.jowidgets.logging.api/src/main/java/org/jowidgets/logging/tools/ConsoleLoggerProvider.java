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
import org.jowidgets.logging.api.ILoggerProvider;

public final class ConsoleLoggerProvider {

    private ConsoleLoggerProvider() {}

    /**
     * Creates a ILoggerProvider that logs errors to the console
     * 
     * @return a ILoggerProvider that logs errors to the console
     */
    public static ILoggerProvider errorLoggerProvider() {
        return new DefaultLoggerProvider(new ILoggerFactory() {
            @Override
            public ILogger create(final String name) {
                return new ConsoleErrorLoggerAdapter(name);
            }
        });
    }

    /**
     * Creates a ILoggerProvider that logs errors and warning to the console
     * 
     * @return a ILoggerProvider that logs errors and warning to the console
     */
    public static ILoggerProvider warnLoggerProvider() {
        return new DefaultLoggerProvider(new ILoggerFactory() {
            @Override
            public ILogger create(final String name) {
                return new ConsoleWarnLoggerAdapter(name);
            }
        });
    }

    /**
     * Creates a ILoggerProvider that logs errors, warning and infos to the console
     * 
     * @return a ILoggerProvider that logs errors, warning and infos to the console
     */
    public static ILoggerProvider infoLoggerProvider() {
        return new DefaultLoggerProvider(new ILoggerFactory() {
            @Override
            public ILogger create(final String name) {
                return new ConsoleInfoLoggerAdapter(name);
            }
        });
    }

    /**
     * Creates a ILoggerProvider that logs errors, warning, infos and debug to the console
     * 
     * @return a ILoggerProvider that logs errors, warning, infos and debug to the console
     */
    public static ILoggerProvider debugLoggerProvider() {
        return new DefaultLoggerProvider(new ILoggerFactory() {
            @Override
            public ILogger create(final String name) {
                return new ConsoleDebugLoggerAdapter(name);
            }
        });
    }

    /**
     * Creates a ILoggerProvider that logs all log levels to the console
     * 
     * @return a ILoggerProvider that logs all log levels to the console
     */
    public static ILoggerProvider traceLoggerProvider() {
        return new DefaultLoggerProvider(new ILoggerFactory() {
            @Override
            public ILogger create(final String name) {
                return new ConsoleTraceLoggerAdapter(name);
            }
        });
    }

}
