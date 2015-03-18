/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.api.widgets;

import java.util.List;

import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.IWindowCommon;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;

public interface IWindow extends IDisplay, IComponent, IWindowCommon {

    /**
     * Centers the location relative to the parent bounds
     * 
     * @see #getParentBounds()
     */
    void centerLocation();

    /**
     * Creates a child window with this window as parent
     * 
     * @param <WIDGET_TYPE> The type of the created child window
     * @param <DESCRIPTOR_TYPE> The type of the child windows descriptor
     * @param descriptor The child windows descriptor
     * @return The created window
     */
    <WIDGET_TYPE extends IDisplay, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> WIDGET_TYPE createChildWindow(
        final DESCRIPTOR_TYPE descriptor);

    /**
     * Gets the child windows of this window as a unmodifiable copy
     * 
     * @return all children of this window, may be empty but never null
     */
    List<IDisplay> getChildWindows();

    /**
     * Sets the min pack size of the window.
     * 
     * If the window will be packed and a min pack size was defined
     * before, the window will not become smaller than the min pack size.
     * 
     * The min pack size can be used to avoid that window becomes to small by packing.
     * 
     * @param size The min pack size to set
     * 
     * @see #pack()
     */
    void setMinPackSize(Dimension size);

    /**
     * Sets the max pack size of the window.
     * 
     * If the window will be packed and a max pack size was defined
     * before, the window will not become larger than the max pack size.
     * 
     * The max pack size can be used to avoid that window becomes to big by packing.
     * 
     * @param size The max pack size to set
     * 
     * @see #pack()
     */
    void setMaxPackSize(Dimension size);

}
