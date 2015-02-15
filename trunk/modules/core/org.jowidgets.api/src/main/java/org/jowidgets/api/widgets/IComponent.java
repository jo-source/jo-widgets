/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.api.widgets;

import org.jowidgets.api.controller.IShowingStateObservable;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IComponentCommon;

public interface IComponent extends IWidget, IComponentCommon, IShowingStateObservable {

	/**
	 * Gets the information if the component is reparentable
	 * 
	 * @return True if the component is reparentable, false otherwise
	 */
	boolean isReparentable();

	/**
	 * A root component is showing if it is visible.
	 * A child component is showing if it is visible and if it's parent component is showing
	 * 
	 * @return True if the component is showing, false otherwise
	 */
	boolean isShowing();

	/**
	 * Gets the input focus state of the component.
	 * 
	 * Only one component can have the focus at the same time
	 * 
	 * @return true if the component has the focus, false otherwise
	 */
	boolean hasFocus();

	/**
	 * Creates a popup menu as a child of this component
	 * 
	 * @return The created popup menu, never null
	 */
	IPopupMenu createPopupMenu();

	/**
	 * Sets a popup menu for this component defined by a model.
	 * 
	 * The popup menu will be shown, when an popup event occurs on this component.
	 * 
	 * @param menuModel
	 *            The model of the popup menu or null, if no popup should be shown on popup events
	 */
	void setPopupMenu(IMenuModel popupMenu);

	/**
	 * Transforms a position in the coordinate system of this component to the screen coordinate system
	 * 
	 * @param localPosition The local position, must not be null
	 * 
	 * @return The corresponding screen position, never null
	 */
	Position toScreen(final Position localPosition);

	/**
	 * Transforms a position in the screen coordinate system to this components coordinate system
	 * 
	 * @param screenPosition The screen position to transform, must not ne null
	 * 
	 * @return The corresponding local position, never null
	 */
	Position toLocal(final Position screenPosition);

	/**
	 * Sets the size of the component defined by width and height
	 * 
	 * @param width The width
	 * @param height The height
	 */
	void setSize(int width, int height);

	/**
	 * Sets the position of the component defined by the x-coordinate and the y-coordinate
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	void setPosition(int x, int y);

	/**
	 * Gets the bounds of the component
	 * 
	 * @return The bounds, never null
	 */
	Rectangle getBounds();

	/**
	 * The the bounds of the component
	 * 
	 * @param bounds The bounds to set, never null
	 */
	void setBounds(Rectangle bounds);

	/**
	 * Transforms a position from another components coordinate system to
	 * this components coordinate system
	 * 
	 * @param component Component, which the position is relative to, must not be null
	 * @param componentPosition Position The position to transform, must not be null
	 * 
	 * @return The transformed position
	 */
	Position fromComponent(final IComponentCommon component, final Position componentPosition);

	/**
	 * Transforms a position from this components coordinate system to another components
	 * coordinate system
	 * 
	 * @param componentPosition The position in this coordinate system
	 * @param component Component, in what coordinate system the position is transformed to
	 * 
	 * @return The transformed position
	 */
	Position toComponent(final Position componentPosition, final IComponentCommon component);

}
