/*
 * Copyright (c) 2015, grossmann
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

package org.jowidgets.api.layout;

import org.jowidgets.api.toolkit.Toolkit;

/**
 * Accessor for 'CachedFillLayout'
 * 
 * This layouter renders the first visible child of the container into the whole available space and caches the size
 * of its children, so the next time the layout will be done, the cached values will be used until
 * the cache is cleared by the user.
 * 
 * In many cases, the min, max and pref size of the child container will not change until the child container
 * has not been changed by adding or removing children. So using this layout manager as a parent for a complex
 * layout can improve performance extremely. The tradeoff is, that the programmer has to take care for container
 * changes itself and clearing the cache.
 * 
 * layout(): The size of the visible control is set to the containers clientAreaSize
 * 
 * getPreferredSize(): Returns the preferred size of the visible control
 * getMinSize(): Returns the min size of the visible control
 * getMaxSize(): returns new Dimension(Short.MAX_VALUE, Short.MAX_VALUE)
 * 
 * The PreferredSize, MinSize() and MaxSize() will be cached, until clear cache will be invoked
 */
public final class CachedFillLayout {

    private CachedFillLayout() {}

    /**
     * Gets a layout factory for an 'CachedFillLayout'
     * 
     * @return A layout factory that produces 'CachedFillLayout'
     */
    public static ILayoutFactory<ICachedFillLayout> get() {
        return Toolkit.getLayoutFactoryProvider().cachedFillLayout();
    }

}
