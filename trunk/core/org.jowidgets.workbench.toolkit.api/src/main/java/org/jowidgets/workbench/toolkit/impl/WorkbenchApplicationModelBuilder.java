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
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.ILifecycleCallback;
import org.jowidgets.workbench.api.IWorkbenchApplicationDescriptor;
import org.jowidgets.workbench.toolkit.api.IViewFactory;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationInitializeCallback;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModelBuilder;

class WorkbenchApplicationModelBuilder extends ComponentNodeContainerModelBuilder<IWorkbenchApplicationModelBuilder> implements
		IWorkbenchApplicationModelBuilder {

	private String label;
	private String tooltip;
	private IImageConstant icon;
	private IMenuModel popupMenu;
	private IToolBarModel toolBarModel;
	private IMenuModel toolBarMenu;
	private ILifecycleCallback lifecycleCallback;
	private IWorkbenchApplicationInitializeCallback initializeCallback;
	private IViewFactory viewFactory;

	WorkbenchApplicationModelBuilder() {
		super();
		this.popupMenu = Toolkit.getModelFactoryProvider().getItemModelFactory().menu();
		this.toolBarMenu = Toolkit.getModelFactoryProvider().getItemModelFactory().menu();
		this.toolBarModel = Toolkit.getModelFactoryProvider().getItemModelFactory().toolBar();
		this.viewFactory = new DummyViewFactory();
	}

	@Override
	public IWorkbenchApplicationModelBuilder setLabel(final String label) {
		this.label = label;
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder setTooltip(final String tooltip) {
		this.tooltip = tooltip;
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder setIcon(final IImageConstant icon) {
		this.icon = icon;
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder setDescriptor(final IWorkbenchApplicationDescriptor descriptor) {
		Assert.paramNotNull(descriptor, "descriptor");
		setId(descriptor.getId()).setLabel(descriptor.getLabel());
		setTooltip(descriptor.getTooltip()).setIcon(descriptor.getIcon());
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder setPopupMenu(final IMenuModel popupMenu) {
		this.popupMenu = popupMenu;
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder setToolBar(final IToolBarModel toolBarModel) {
		this.toolBarModel = toolBarModel;
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder setToolBarMenu(final IMenuModel toolBarMenu) {
		this.toolBarMenu = toolBarMenu;
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder setViewFactoy(final IViewFactory viewFactory) {
		Assert.paramNotNull(viewFactory, "viewFactory");
		this.viewFactory = viewFactory;
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder setLifecycleCallback(final ILifecycleCallback lifecycleCallback) {
		this.lifecycleCallback = lifecycleCallback;
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder setInitializeCallback(
		final IWorkbenchApplicationInitializeCallback initializeCallback) {
		this.initializeCallback = initializeCallback;
		return this;
	}

	@Override
	public IWorkbenchApplicationModel build() {
		return new WorkbenchApplicationModel(
			getId(),
			label,
			tooltip,
			icon,
			popupMenu,
			toolBarModel,
			toolBarMenu,
			lifecycleCallback,
			initializeCallback,
			viewFactory,
			getChildren());
	}

}
