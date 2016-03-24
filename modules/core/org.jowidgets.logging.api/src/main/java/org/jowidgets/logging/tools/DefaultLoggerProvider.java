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
 * 
 * @author grossmann
 */
public final class DefaultLoggerProvider implements ILoggerProvider {

    private final ILoggerFactory factory;
    private final ConcurrentMap<String, ILogger> loggers;

    public DefaultLoggerProvider(final ILoggerFactory factory) {
        Assert.paramNotNull(factory, "factory");
        this.factory = factory;
        this.loggers = new ConcurrentHashMap<String, ILogger>();
    }

    @Override
    public ILogger get(final String name) {
        Assert.paramNotNull(name, "name");
        final ILogger result = loggers.get(name);
        if (result != null) {
            return result;
        }
        else {
            final ILogger newLogger = factory.create(name);
            final ILogger oldLogger = loggers.putIfAbsent(name, newLogger);
            return oldLogger == null ? newLogger : oldLogger;
        }
    }

}
