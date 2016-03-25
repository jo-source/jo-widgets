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

import java.util.ArrayList;
import java.util.Collection;

import org.jowidgets.logging.api.ILogger;
import org.jowidgets.util.Assert;

/**
 * Implements composite pattern for ILoggerFactory
 * 
 * Creates a LoggerComposite with the loggers created from each given factory
 */
public final class LoggerFactoryComposite implements ILoggerFactory {

    private static final String WRAPPER_FQCN = LoggerComposite.class.getName();

    private final Collection<ILoggerFactory> factories;

    /**
     * Creates a new instance
     * 
     * @param factories The factories to create the loggers from, must not be null
     */
    public LoggerFactoryComposite(final Collection<? extends ILoggerFactory> factories) {
        Assert.paramNotNull(factories, "factories");
        this.factories = new ArrayList<ILoggerFactory>(factories);
    }

    @Override
    public ILogger create(final String name, final String wrapperFQCN) {
        final Collection<ILogger> loggers = new ArrayList<ILogger>(factories.size());
        final String usedWrapperFQCn = wrapperFQCN != null ? wrapperFQCN : WRAPPER_FQCN;
        for (final ILoggerFactory factory : factories) {
            loggers.add(factory.create(name, usedWrapperFQCn));
        }
        return new LoggerComposite(loggers);
    }

}
