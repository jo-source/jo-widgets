/*
 * Copyright (c) 2018, grossmann
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

package org.jowidgets.util;

/**
 * Default implementation that uses a {@link IFactory} to create the value on first invocation of the {@link #get()} method or
 * uses a value, e.g. if a value is available but a IProvider interface is necessary.
 * 
 * @param <VALUE_TYPE> The value type
 */
public final class DefaultProvider<VALUE_TYPE> implements IProvider<VALUE_TYPE> {

    private final IFactory<VALUE_TYPE> factory;

    private VALUE_TYPE value;

    /**
     * Creates a new instance with a given value
     * 
     * @param value The value to return on get, may be null
     */
    public DefaultProvider(final VALUE_TYPE value) {
        this(new ValueFactoryAdapter<VALUE_TYPE>(value));
    }

    /**
     * Creates a new instance with a given factory
     * 
     * @param factory The factory to use, must not be null
     */
    public DefaultProvider(final IFactory<VALUE_TYPE> factory) {
        Assert.paramNotNull(factory, "factory");
        this.factory = factory;
    }

    @Override
    public synchronized VALUE_TYPE get() {
        if (value == null) {
            value = factory.create();
        }
        return value;
    }

}
