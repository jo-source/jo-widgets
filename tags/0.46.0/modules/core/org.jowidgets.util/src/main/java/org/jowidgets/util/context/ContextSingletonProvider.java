/*
 * Copyright (c) 2013, grossmann
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
package org.jowidgets.util.context;

import java.util.HashMap;
import java.util.Map;

import org.jowidgets.util.Assert;

/**
 * This implementation is not thread save, but for ui tasks that is not necessary
 */
public final class ContextSingletonProvider {

    private static final IContextSingletonProvider INSTANCE = new DefaultContextSingletonProvider();

    private ContextSingletonProvider() {}

    public static IContextSingletonProvider getInstance() {
        return INSTANCE;
    }

    public static <VALUE_TYPE, CONTEXT_TYPE> VALUE_TYPE get(
        final CONTEXT_TYPE context,
        final IContextSingletonFactory<VALUE_TYPE, CONTEXT_TYPE> factory) {
        return getInstance().get(context, factory);
    }

    public static void removeReference(final Object context) {
        getInstance().removeReference(context);
    }

    private static final class DefaultContextSingletonProvider implements IContextSingletonProvider {

        private final Map<Object, Object> singletons;

        private DefaultContextSingletonProvider() {
            this.singletons = new HashMap<Object, Object>();
        }

        @SuppressWarnings("unchecked")
        @Override
        public synchronized <VALUE_TYPE, CONTEXT_TYPE> VALUE_TYPE get(
            final CONTEXT_TYPE context,
            final IContextSingletonFactory<VALUE_TYPE, CONTEXT_TYPE> factory) {
            Assert.paramNotNull(context, "context");
            Assert.paramNotNull(factory, "factory");

            Object result = singletons.get(context);
            if (result == null) {
                result = factory.create(context, this);
                singletons.put(context, result);
            }

            return (VALUE_TYPE) result;
        }

        @Override
        public void removeReference(final Object context) {
            Assert.paramNotNull(context, "context");
            singletons.remove(context);
        }

    }
}
