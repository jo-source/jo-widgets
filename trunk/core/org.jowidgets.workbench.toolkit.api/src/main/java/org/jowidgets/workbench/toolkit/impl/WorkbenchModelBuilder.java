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

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.content.IContentCreator;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.ICloseCallback;
import org.jowidgets.workbench.api.ILoginCallback;
import org.jowidgets.workbench.api.IWorkbenchContext;
import org.jowidgets.workbench.api.IWorkbenchDescriptor;
import org.jowidgets.workbench.toolkit.api.IViewFactory;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModelBuilder;
import org.jowidgets.workbench.toolkit.api.IWorkbenchInitializeCallback;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModelBuilder;
import org.jowidgets.workbench.toolkit.api.WorkbenchToolkit;

class WorkbenchModelBuilder extends WorkbenchPartBuilder<IWorkbenchModelBuilder> implements IWorkbenchModelBuilder {

	private Dimension initialDimension;
	private Position initialPosition;
	private double initialSplitWeight;
	private boolean hasApplicationNavigator;
	private boolean applicationsCloseable;
	private IToolBarModel toolBar;
	private IMenuBarModel menuBar;
	private IContentCreator statusBarCreator;
	private ILoginCallback loginCallback;
	private ICloseCallback closeCallback;
	private IViewFactory viewFactory;

	private final Set<IWorkbenchInitializeCallback> initializeCallbacks;
	private final List<Runnable> shutdownHooks;
	private final List<IWorkbenchApplicationModel> applications;

	WorkbenchModelBuilder() {
		this.initializeCallbacks = new LinkedHashSet<IWorkbenchInitializeCallback>();
		this.applications = new LinkedList<IWorkbenchApplicationModel>();
		this.shutdownHooks = new LinkedList<Runnable>();
		this.initialSplitWeight = 0.25;
		this.hasApplicationNavigator = true;
		this.applicationsCloseable = false;
		this.toolBar = Toolkit.getModelFactoryProvider().getItemModelFactory().toolBar();
		this.menuBar = Toolkit.getModelFactoryProvider().getItemModelFactory().menuBar();
		this.viewFactory = new DummyViewFactory();
	}

	@Override
	public IWorkbenchModelBuilder setDescriptor(final IWorkbenchDescriptor descriptor) {
		Assert.paramNotNull(descriptor, "descriptor");
		setLabel(descriptor.getLabel());
		setTooltip(descriptor.getTooltip());
		setIcon(descriptor.getIcon());
		setInitialDimension(descriptor.getInitialDimension());
		setInitialPosition(descriptor.getInitialPosition());
		setInitialSplitWeight(descriptor.getInitialSplitWeight());
		setApplicationNavigator(descriptor.hasApplicationNavigator());
		setApplicationsCloseable(descriptor.getApplicationsCloseable());
		return this;
	}

	@Override
	public IWorkbenchModelBuilder setInitialDimension(final Dimension initialDimension) {
		this.initialDimension = initialDimension;
		return this;
	}

	@Override
	public IWorkbenchModelBuilder setInitialPosition(final Position initialPosition) {
		this.initialPosition = initialPosition;
		return this;
	}

	@Override
	public IWorkbenchModelBuilder setInitialSplitWeight(final double initialSplitWeigth) {
		this.initialSplitWeight = initialSplitWeigth;
		return this;
	}

	@Override
	public IWorkbenchModelBuilder setApplicationNavigator(final boolean hasApplicationNavigator) {
		this.hasApplicationNavigator = hasApplicationNavigator;
		return this;
	}

	@Override
	public IWorkbenchModelBuilder setApplicationsCloseable(final boolean applicationsCloseable) {
		this.applicationsCloseable = applicationsCloseable;
		return this;
	}

	@Override
	public IWorkbenchModelBuilder setToolBar(final IToolBarModel toolBarModel) {
		this.toolBar = toolBarModel;
		return this;
	}

	@Override
	public IWorkbenchModelBuilder setMenuBar(final IMenuBarModel menuBarModel) {
		this.menuBar = menuBarModel;
		return this;
	}

	@Override
	public IWorkbenchModelBuilder setStatusBarCreator(final IContentCreator statusBarContentCreator) {
		this.statusBarCreator = statusBarContentCreator;
		return this;
	}

	@Override
	public IWorkbenchModelBuilder setLoginCallback(final ILoginCallback loginCallback) {
		this.loginCallback = loginCallback;
		return this;
	}

	@Override
	public IWorkbenchModelBuilder setCloseCallback(final ICloseCallback closeCallback) {
		this.closeCallback = closeCallback;
		return this;
	}

	@Override
	public IWorkbenchModelBuilder setViewFactoy(final IViewFactory viewFactory) {
		Assert.paramNotNull(viewFactory, "viewFactory");
		this.viewFactory = viewFactory;
		return this;
	}

	@Override
	public IWorkbenchModelBuilder setInitializeCallback(final IWorkbenchInitializeCallback initializeCallback) {
		initializeCallbacks.clear();
		return addInitializeCallback(initializeCallback);
	}

	@Override
	public IWorkbenchModelBuilder addInitializeCallback(final IWorkbenchInitializeCallback initializeCallback) {
		Assert.paramNotNull(initializeCallback, "initializeCallback");
		initializeCallbacks.add(initializeCallback);
		return this;
	}

	@Override
	public IWorkbenchModelBuilder addShutdownHook(final Runnable shutdownHook) {
		shutdownHooks.add(shutdownHook);
		return this;
	}

	@Override
	public IWorkbenchModelBuilder addApplication(final int index, final IWorkbenchApplicationModel applicationModel) {
		applications.add(index, applicationModel);
		return this;
	}

	@Override
	public IWorkbenchModelBuilder addApplication(final IWorkbenchApplicationModel applicationModel) {
		return addApplication(applications.size(), applicationModel);
	}

	@Override
	public IWorkbenchModelBuilder addApplication(final IWorkbenchApplicationModelBuilder applicationModelBuilder) {
		return addApplication(applicationModelBuilder.build());
	}

	@Override
	public IWorkbenchModelBuilder addApplication(final int index, final IWorkbenchApplicationModelBuilder applicationModelBuilder) {
		return addApplication(index, applicationModelBuilder.build());
	}

	@Override
	public IWorkbenchModelBuilder addApplication(
		final String id,
		final String label,
		final String tooltip,
		final IImageConstant icon) {
		return addApplication(applicationBuilder().setId(id).setLabel(label).setTooltip(tooltip).setIcon(icon));
	}

	@Override
	public IWorkbenchModelBuilder addApplication(final String id, final String label, final IImageConstant icon) {
		return addApplication(applicationBuilder().setId(id).setLabel(label).setIcon(icon));
	}

	@Override
	public IWorkbenchModelBuilder addApplication(final String id, final String label, final String tooltip) {
		return addApplication(applicationBuilder().setId(id).setLabel(label).setTooltip(tooltip));
	}

	@Override
	public IWorkbenchModelBuilder addApplication(final String id, final String label) {
		return addApplication(applicationBuilder().setId(id).setLabel(label));
	}

	@Override
	public IWorkbenchModelBuilder addApplication(final String id) {
		return addApplication(applicationBuilder().setId(id));
	}

	@Override
	public IWorkbenchModel build() {
		return new WorkbenchModel(
			getLabel(),
			getTooltip(),
			getIcon(),
			initialDimension,
			initialPosition,
			initialSplitWeight,
			hasApplicationNavigator,
			applicationsCloseable,
			toolBar,
			menuBar,
			statusBarCreator,
			loginCallback,
			closeCallback,
			new InitializeCallbackComposite(initializeCallbacks),
			viewFactory,
			applications,
			shutdownHooks);
	}

	private IWorkbenchApplicationModelBuilder applicationBuilder() {
		return WorkbenchToolkit.getWorkbenchPartBuilderFactory().application();
	}

	private static final class InitializeCallbackComposite implements IWorkbenchInitializeCallback {

		private final List<IWorkbenchInitializeCallback> callbacks;

		private InitializeCallbackComposite(final Collection<? extends IWorkbenchInitializeCallback> callbacks) {
			this.callbacks = new LinkedList<IWorkbenchInitializeCallback>(callbacks);
		}

		@Override
		public void onContextInitialize(final IWorkbenchModel model, final IWorkbenchContext context) {
			for (final IWorkbenchInitializeCallback callback : callbacks) {
				callback.onContextInitialize(model, context);
			}

		}

	}

}
