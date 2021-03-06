/*
 * Copyright (c) 2012, grossmann
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

package org.jowidgets.tools.toolkit;

import org.jowidgets.api.toolkit.IToolkitInterceptor;
import org.jowidgets.api.toolkit.IToolkitInterceptorHolder;

public abstract class AbstractToolkitInterceptorHolder implements IToolkitInterceptorHolder {

    public static final int DEFAULT_ORDER = IToolkitInterceptorHolder.DEFAULT_ORDER;

    private final int order;

    private IToolkitInterceptor toolkitInterceptor;

    /**
     * Creates a new instance with default order
     */
    protected AbstractToolkitInterceptorHolder() {
        this(DEFAULT_ORDER);
    }

    /**
     * Creates a new instance with given order
     * 
     * @param order The order of this holder to use
     */
    protected AbstractToolkitInterceptorHolder(final int order) {
        this.order = order;
    }

    /**
     * Creates the toolkit interceptor. This method will only be invoked once.
     * 
     * @return A new toolkit interceptor, never null
     */
    protected abstract IToolkitInterceptor createToolkitInterceptor();

    @Override
    public final IToolkitInterceptor getToolkitInterceptor() {
        if (toolkitInterceptor == null) {
            toolkitInterceptor = createToolkitInterceptor();
        }
        return toolkitInterceptor;
    }

    @Override
    public final int getOrder() {
        return order;
    }

}
