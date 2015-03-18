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

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IComponentObservable;
import org.jowidgets.common.widgets.controller.IFocusObservable;
import org.jowidgets.common.widgets.controller.IKeyObservable;
import org.jowidgets.common.widgets.controller.IMouseMotionObservable;
import org.jowidgets.common.widgets.controller.IMouseObservable;
import org.jowidgets.common.widgets.controller.IPopupDetectionObservable;

public interface IComponentCommon extends
        IWidgetCommon,
        IComponentObservable,
        IFocusObservable,
        IKeyObservable,
        IMouseObservable,
        IMouseMotionObservable,
        IPopupDetectionObservable {

    /**
     * Marks the widget that a redraw is necessary
     */
    void redraw();

    /**
     * Enables or disabled the redraw of a component and its children.
     * 
     * If redraw is disabled, all changes of the component will not be made visible until
     * redraw will be enabled again.
     * 
     * REMARK: This is a hint and will not work for all platforms
     * 
     * @param enabled The enabled state
     */
    void setRedrawEnabled(boolean enabled);

    /**
     * Try's to get the focus for the component.
     * 
     * Remark: This is not always possible, so developers can not assume that requesting
     * the focus guarantee's that the component gets the focus.
     * Only if the focusGained event was fired, the component has the focus.
     * 
     * @return false if the request definitively fails, true if it may succeed.
     */
    boolean requestFocus();

    /**
     * Sets the foreground color.
     * If set to null, the default color is used.
     * 
     * @param colorValue The color to set, may be null
     */
    void setForegroundColor(final IColorConstant colorValue);

    /**
     * Sets the background color.
     * If set to null, the default color is used.
     * 
     * @param colorValue The color to set, may be null
     */
    void setBackgroundColor(final IColorConstant colorValue);

    /**
     * Gets the foreground color
     * 
     * @return The foreground color or null if no specific color is set
     */
    IColorConstant getForegroundColor();

    /**
     * Gets the background color
     * 
     * @return The background color or null if no specific color is set
     */
    IColorConstant getBackgroundColor();

    /**
     * Sets the cursor
     * 
     * @param cursor The cursor to set
     */
    void setCursor(final Cursor cursor);

    /**
     * Sets the components visibility state.
     * 
     * @param visible The visibility state to set
     */
    void setVisible(final boolean visible);

    /**
     * Gets the visibility state.
     * 
     * @return The visibility state
     */
    boolean isVisible();

    /**
     * Gets the current size of the component.
     * 
     * @return The size of the component, never null
     */
    Dimension getSize();

    /**
     * Sets the size of the component.
     * 
     * @param size The size to set, must not be null
     */
    void setSize(final Dimension size);

    /**
     * Gets the position of the component
     * 
     * @return The position of the component, never null
     */
    Position getPosition();

    /**
     * Sets the position of the component
     * 
     * @param position The position of the component, must not be null
     */
    void setPosition(final Position position);

}
