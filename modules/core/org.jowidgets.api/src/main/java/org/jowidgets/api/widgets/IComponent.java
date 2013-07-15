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

	boolean isReparentable();

	/**
	 * A root component is showing if it is visible.
	 * A child component is showing if it is visible and if it's parent component is showing
	 * 
	 * @return True if the component is showing, false otherwise
	 */
	boolean isShowing();

	boolean hasFocus();

	IPopupMenu createPopupMenu();

	/**
	 * Sets a popup menu for this component.
	 * The popup menu will be shown, when an popup event occurs on this component.
	 * 
	 * @param menuModel
	 *            The model of the popup menu or null, if no popup should be shown on popup events
	 */
	void setPopupMenu(IMenuModel popupMenu);

	Position toScreen(final Position localPosition);

	Position toLocal(final Position screenPosition);

	void setSize(int width, int height);

	void setPosition(int x, int y);

	Rectangle getBounds();

	void setBounds(Rectangle bounds);

	/**
	 * Transforms a position from another component to the current component
	 * 
	 * @param component Component, which the position is relative to
	 * @param componentPosition Position
	 * @return transformed local position
	 */
	Position fromComponent(final IComponentCommon component, final Position componentPosition);

	/**
	 * Transforms a local position to another component
	 * 
	 * @param componentPosition Local position
	 * @param component Component, which the position is transformed to
	 * @return transformed position
	 */
	Position toComponent(final Position componentPosition, final IComponentCommon component);

}
