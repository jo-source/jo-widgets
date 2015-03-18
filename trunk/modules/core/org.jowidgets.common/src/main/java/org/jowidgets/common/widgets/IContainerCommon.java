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
package org.jowidgets.common.widgets;

import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;

public interface IContainerCommon extends IComponentCommon {

    /**
     * Sets the layout for this container
     * 
     * @param layoutDescriptor The layout to set
     */
    void setLayout(ILayoutDescriptor layoutDescriptor);

    /**
     * Informs that more than one operation on the containers children be
     * done and that now redraw should be done until layout end will be invoked
     * to avoid flickering.
     */
    void layoutBegin();

    /**
     * Layouts the container
     */
    void layoutEnd();

    /**
     * Removes all child controls from the container.
     * 
     * The removed controls will be disposed
     */
    void removeAll();

    /**
     * Gets the client area of the container.
     * Thats the area where child controls can be layouted into.
     * 
     * @return The client area, never null
     */
    Rectangle getClientArea();

    /**
     * Gets the decorated size (size with border / insets ) for a given client area size
     * 
     * @param clientAreaSize The client area size to get the decorated size for.
     * 
     * @return The decorated size
     */
    Dimension computeDecoratedSize(Dimension clientAreaSize);

}
