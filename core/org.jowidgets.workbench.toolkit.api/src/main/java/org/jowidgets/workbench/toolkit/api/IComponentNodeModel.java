/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.workbench.toolkit.api;

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.workbench.api.IComponentNodeDescriptor;

public interface IComponentNodeModel extends IComponentNodeDescriptor, IComponentNodeContainerModel, IWorkbenchPartModel {

	boolean isSelected();

	boolean isExpanded();

	IMenuModel getPopupMenu();

	IComponentFactory getComponentFactory();

	IComponentNodeInitializeCallback getInitializeCallback();

	void setLabel(String label);

	void setTooltip(String toolTip);

	void setIcon(IImageConstant icon);

	void setSelected(boolean selected);

	void setExpanded(boolean expanded);

	void setPopupMenu(IMenuModel popupMenu);

	String getPathId();

	/**
	 * Sets the parent container of this component node. This method will be invoked
	 * by the API implementation, when this node will be added as a child to another container or
	 * when it was removed from its parent.
	 * 
	 * If this method will be invoked by the API user (client code) the following happens:
	 * 
	 * 1. If this node already has a parent, it will be removed from this.
	 * 
	 * 2. If the given parent is not null and this node is not already a child of the given parent,
	 * this node will be appended to the given parent.
	 * 
	 * @param parentContainer The parent to set or null if the node was/should be removed from its parent
	 */
	void setParentContainer(IComponentNodeContainerModel parentContainer);

	IComponentNodeModel getParent();

	IWorkbenchApplicationModel getApplication();

	IWorkbenchModel getWorkbench();

}
