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

package org.jowidgets.api.toolkit;

/**
 * A toolkit interceptor holder allows to plugin a Toolkit interceptor into any {@link IToolkit} that will be created.
 * 
 * The interceptor can be registered explicitly with help of
 * the method {@link ToolkitInterceptor#registerToolkitInterceptorHolder(IToolkitInterceptorHolder)} (not recommended)
 * or with help of the Java Service Loader Mechanism:
 * (http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html)
 */
public interface IToolkitInterceptorHolder {

    /**
     * The default order for toolkit interceptors.
     * 
     * Uses values below this order to do interception before interceptors with default order.
     * 
     * Use value above this order to do interception after interceptors with default order.
     */
    int DEFAULT_ORDER = 2;

    /**
     * Gets the toolkit interceptor for this toolkit
     * 
     * @return The toolkit interceptor, never null
     */
    IToolkitInterceptor getToolkitInterceptor();

    /**
     * Gets the order of the holder.
     * 
     * Interceptors with lower order will be invoked before interceptors with higher order.
     * 
     * @return The order
     */
    int getOrder();
}
