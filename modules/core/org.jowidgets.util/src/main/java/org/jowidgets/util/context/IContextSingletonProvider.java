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

/**
 * A context singleton holds exactly one value for a defined context. A context singleton provider holds all
 * context singletons an can be used to access them
 */
public interface IContextSingletonProvider {

    /**
     * Gets a value for a defined context. If not yet exists, it will be created by the given factory
     * 
     * @param context The context to get the singleton for (must not be null)
     * @param factory The factory to create the context with if not exists (must not be null)
     * @return The value for the context
     */
    <VALUE_TYPE, CONTEXT_TYPE> VALUE_TYPE get(CONTEXT_TYPE context, IContextSingletonFactory<VALUE_TYPE, CONTEXT_TYPE> factory);

    /**
     * Removes the reference from the provider if one exists.
     * 
     * REMARK: If get will be invoked after invoking this method, a new instance will created for the context
     * 
     * @param context The context to remove the reference for
     */
    void removeReference(Object context);

}
