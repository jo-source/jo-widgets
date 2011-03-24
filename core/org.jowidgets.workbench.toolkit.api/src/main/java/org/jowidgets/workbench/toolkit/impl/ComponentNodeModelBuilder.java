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

package org.jowidgets.workbench.toolkit.impl;

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.toolkit.api.IComponentFactory;
import org.jowidgets.workbench.toolkit.api.IComponentNodeInitializeCallback;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModelBuilder;

class ComponentNodeModelBuilder extends ComponentNodeContainerModelBuilder<IComponentNodeModelBuilder> implements
		IComponentNodeModelBuilder {

	private String label;
	private String tooltip;
	private IImageConstant icon;
	private boolean selected;
	private boolean expanded;
	private IMenuModel popupMenu;
	private IComponentFactory componentFactory;
	private IComponentNodeInitializeCallback initializeCallback;

	ComponentNodeModelBuilder() {
		super();
		this.selected = false;
		this.expanded = false;
	}

	@Override
	public IComponentNodeModelBuilder setLabel(final String label) {
		this.label = label;
		return this;
	}

	@Override
	public IComponentNodeModelBuilder setTooltip(final String tooltip) {
		this.tooltip = tooltip;
		return this;
	}

	@Override
	public IComponentNodeModelBuilder setIcon(final IImageConstant icon) {
		this.icon = icon;
		return this;
	}

	@Override
	public IComponentNodeModelBuilder setSelected(final boolean selected) {
		this.selected = selected;
		return this;
	}

	@Override
	public IComponentNodeModelBuilder setExpanded(final boolean expanded) {
		this.expanded = expanded;
		return this;
	}

	@Override
	public IComponentNodeModelBuilder setPopupMenu(final IMenuModel popupMenu) {
		this.popupMenu = popupMenu;
		return this;
	}

	@Override
	public IComponentNodeModelBuilder setComponentFactory(final IComponentFactory componentFactory) {
		this.componentFactory = componentFactory;
		return this;
	}

	@Override
	public IComponentNodeModelBuilder setComponentFactory(final Class<? extends IComponent> componentType) {
		this.componentFactory = new TypeBasedComponentFactory(componentType);
		return this;
	}

	@Override
	public IComponentNodeModelBuilder setInitializeCallback(final IComponentNodeInitializeCallback initializeCallback) {
		this.initializeCallback = initializeCallback;
		return this;
	}

	@Override
	public IComponentNodeModel build() {
		return new ComponentNodeModel(
			getId(),
			label,
			tooltip,
			icon,
			selected,
			expanded,
			popupMenu,
			componentFactory,
			initializeCallback,
			getChildren());
	}

}
