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

import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.common.widgets.IFrameCommon;

public interface IFrame extends IWindow, IContainer, IFrameCommon {

	/**
	 * Creates a new menu bar or returns a earlier created menu bar.
	 * 
	 * @return The menu bar of the frame.
	 */
	IMenuBar createMenuBar();

	/**
	 * Gets the menu bar model of this frame. Invocation of this method will create a menu bar,
	 * if not already a menu bar exist.
	 * 
	 * @return The menu bar model of this frame
	 */
	IMenuBarModel getMenuBarModel();

	/**
	 * Sets the menu bar for this frame. If this frame already has a menu bar, the old menu bar will
	 * be removed, and the new menu bar will be set.
	 * 
	 * @param model The model of the menu bar to add, must not be null;
	 * 
	 * @throws IllegalArgumentException if the model is null
	 */
	void setMenuBar(IMenuBarModel model);

	void setDefaultButton(IButton button);

}
