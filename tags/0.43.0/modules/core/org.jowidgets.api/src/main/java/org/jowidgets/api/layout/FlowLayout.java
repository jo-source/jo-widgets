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
import org.jowidgets.common.widgets.layout.ILayouter;

/**
 * Accessor for 'FlowLayout'
 * 
 * layout(): set the sizes of the controls to the preferred size, positions the
 * controls side by side (alignment == VERTICAL) or below each other (alignment == HORIZONTAL)
 * with the given gap between the controls
 * 
 * getPreferredSize: get the size needed to layout all controls
 * 
 * getMinSize(): gets the preferred size
 * 
 * getMaxSize(): returns new Dimension(Short.MAX_VALUE, Short.MAX_VALUE)
 */
public final class FlowLayout {

    private FlowLayout() {}

    /**
     * Gets a layout factory for an 'FlowLayout'
     * 
     * @return A layout factory that produces 'FlowLayout'
     */
    public static ILayoutFactory<ILayouter> get() {
        return Toolkit.getLayoutFactoryProvider().flowLayout();
    }

    /**
     * Gets a builder for an layout factory of an 'FlowLayout'
     * 
     * @return A builder for an layout factory of an 'FlowLayout'
     */
    public static IFlowLayoutFactoryBuilder builder() {
        return Toolkit.getLayoutFactoryProvider().flowLayoutBuilder();
    }

}
