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

import org.jowidgets.api.controller.IParentObservable;
import org.jowidgets.api.dnd.IDragSource;
import org.jowidgets.api.dnd.IDropTarget;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.IControlCommon;

public interface IControl extends IComponent, IControlCommon, IParentObservable<IContainer> {

	/**
	 * Sets the parent of the container.
	 * 
	 * If the parent is already set and the control is not reparentable an
	 * IllegalStateException will be thrown.
	 * 
	 * @param parent The parent to set
	 */
	void setParent(IContainer parent);

	@Override
	IContainer getParent();

	@Override
	IControl getRoot();

	/**
	 * Gets the drag source of the control
	 * 
	 * @return The drag source, never null
	 */
	IDragSource getDragSource();

	/**
	 * Gets the drop target of the control
	 * 
	 * @return The drop target, never null
	 */
	IDropTarget getDropTarget();

	/**
	 * Sets the minimum size of this control.
	 * 
	 * If set to null, the default minimum size will be used.
	 * 
	 * @param minSize The minimum size to set or null for default min size
	 */
	void setMinSize(final Dimension minSize);

	/**
	 * Sets the preferred size of this control.
	 * 
	 * If set to null, the default preferred size will be used
	 * 
	 * @param preferredSize The preferred size to set or null for default preferred size
	 */
	void setPreferredSize(Dimension preferredSize);

	/**
	 * Sets the maximal size
	 * 
	 * If set to null, the default maximal size will be used
	 * 
	 * @param maxSize The maximal size to set or null for default max size
	 */
	void setMaxSize(Dimension maxSize);

}
