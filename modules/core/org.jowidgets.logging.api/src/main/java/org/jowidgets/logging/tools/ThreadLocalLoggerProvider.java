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

package org.jowidgets.logging.tools;

import org.jowidgets.logging.api.ILogger;
import org.jowidgets.logging.api.ILoggerProvider;
import org.jowidgets.util.Assert;

/**
 * Logger Provider that can be used in JUnit tests.
 * 
 * Each thread can have its own ILoggerProvider
 */
public final class ThreadLocalLoggerProvider implements ILoggerProvider {

    private final ThreadLocal<ILoggerProvider> threadLocalProvider;

    public ThreadLocalLoggerProvider() {
        this.threadLocalProvider = new ThreadLocal<ILoggerProvider>();
    }

    @Override
    public ILogger get(final String name, final String wrapperFQCN) {
        return getOrCreateLoggerProviderForCurrentThread().get(name, wrapperFQCN);
    }

    private ILoggerProvider getOrCreateLoggerProviderForCurrentThread() {
        ILoggerProvider result = threadLocalProvider.get();
        if (result == null) {
            result = new DefaultLoggerProvider(new ILoggerFactory() {
                @Override
                public ILogger create(final String name, final String wrapperFQCN) {
                    return new LoggerMock();
                }
            });
            threadLocalProvider.set(result);
        }
        return result;
    }

    /**
     * Sets the logger provider for the current thread
     * 
     * @param provider The provider to set, must not be null
     */
    public void setLoggerProviderForCurrentThread(final ILoggerProvider provider) {
        Assert.paramNotNull(provider, "provider");
        threadLocalProvider.set(provider);
    }

    /**
     * Sets the logger provider for the current thread with help of a logger factory
     * 
     * @param factory The factory to create loggers, must not be null
     */
    public void setLoggerProviderForCurrentThread(final ILoggerFactory factory) {
        Assert.paramNotNull(factory, "factory");
        setLoggerProviderForCurrentThread(new DefaultLoggerProvider(factory));
    }

    /**
     * Clears the logger provider for the current thread.
     * This should be done in AfterClass annotated method if a logger or provider was set in an BeforeClass annotated method
     */
    public void clearLoggerProviderForCurrentThread() {
        threadLocalProvider.set(null);
    }

    /**
     * Sets the logger for the current thread.
     * 
     * This sets a logger provider that will always return the given logger for all logger names and fqcn's
     * for invocations in this thread.
     * 
     * Remark: This overrides the logger provider set for this thread
     * 
     * @param logger The logger to set, must not be null
     */
    public void setLoggerForCurrentThread(final ILogger logger) {
        setLoggerProviderForCurrentThread(new ILoggerFactory() {
            @Override
            public ILogger create(final String name, final String wrapperFQCN) {
                return logger;
            }
        });
    }

    /**
     * Clears the logger provider for the current thread. Has the same effect as {@link #clearLoggerProviderForCurrentThread()}
     * 
     * This should be done in AfterClass annotated method if a logger or provider was set in an BeforeClass annotated method
     */
    public void clearLoggerForCurrentThread() {
        threadLocalProvider.set(null);
    }

}
