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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jowidgets.logging.api.ILogger;
import org.jowidgets.logging.api.ILoggerProvider;
import org.jowidgets.util.Assert;

/**
 * Default implementation for an logger provider that creates a logger only one time for each name.
 * 
 * Uses a concurrent hash map to maintain the loggers
 */
public final class DefaultLoggerProvider implements ILoggerProvider {

    private final ILoggerFactory factory;
    private final ConcurrentMap<LoggerKey, ILogger> loggers;

    /**
     * Creates a new default logger provider for a given logger factory
     * 
     * @param factory The factory to use to create loggers, must not be null
     */
    public DefaultLoggerProvider(final ILoggerFactory factory) {
        Assert.paramNotNull(factory, "factory");
        this.factory = factory;
        this.loggers = new ConcurrentHashMap<LoggerKey, ILogger>();
    }

    @Override
    public ILogger get(final String name, final String callerFQCN) {
        Assert.paramNotNull(name, "name");
        final LoggerKey key = new LoggerKey(name, callerFQCN);
        final ILogger result = loggers.get(key);
        if (result != null) {
            return result;
        }
        else {
            final ILogger newLogger = factory.create(name, callerFQCN);
            final ILogger oldLogger = loggers.putIfAbsent(key, newLogger);
            return oldLogger == null ? newLogger : oldLogger;
        }
    }

    private static final class LoggerKey {

        private final String name;
        private final String callerFQCN;

        LoggerKey(final String name, final String callerFQCN) {
            this.name = name;
            this.callerFQCN = callerFQCN;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((callerFQCN == null) ? 0 : callerFQCN.hashCode());
            result = prime * result + name.hashCode();
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            //Assumption: never use null keys and all comparisons will be done with same type
            //if (obj == null) {
            //    return false;
            //}
            //if (!(obj instanceof LoggerKey)) {
            //    return false;
            //}
            final LoggerKey other = (LoggerKey) obj;
            if (name == null) {
                if (other.name != null) {
                    return false;
                }
            }
            else if (!name.equals(other.name)) {
                return false;
            }
            if (callerFQCN == null) {
                if (other.callerFQCN != null) {
                    return false;
                }
            }
            else if (!callerFQCN.equals(other.callerFQCN)) {
                return false;
            }

            return true;
        }

    }
}
