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

package org.jowidgets.workbench.tools;

import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.widgets.content.IContentCreator;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.workbench.api.ICloseCallback;
import org.jowidgets.workbench.api.ILoginCallback;
import org.jowidgets.workbench.api.IWorkbenchDescriptor;
import org.jowidgets.workbench.toolkit.api.IViewFactory;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModelBuilder;
import org.jowidgets.workbench.toolkit.api.IWorkbenchInitializeCallback;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModelBuilder;
import org.jowidgets.workbench.toolkit.api.WorkbenchToolkit;

public class WorkbenchModelBuilder implements IWorkbenchModelBuilder {

	private final IWorkbenchModelBuilder builder;

	public WorkbenchModelBuilder() {
		this(builder());
	}

	public WorkbenchModelBuilder(final IWorkbenchDescriptor descriptor) {
		this(builder(descriptor));
	}

	public WorkbenchModelBuilder(final String label) {
		this(builder(label));
	}

	public WorkbenchModelBuilder(final String label, final IImageConstant icon) {
		this(builder(label, icon));
	}

	public WorkbenchModelBuilder(final IWorkbenchModelBuilder builder) {
		super();
		this.builder = builder;
	}

	@Override
	public final IWorkbenchModelBuilder setDescriptor(final IWorkbenchDescriptor descriptor) {
		this.builder.setDescriptor(descriptor);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder setLabel(final String label) {
		this.builder.setLabel(label);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder setTooltip(final String toolTiptext) {
		this.builder.setTooltip(toolTiptext);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder setIcon(final IImageConstant icon) {
		this.builder.setIcon(icon);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder setInitialDimension(final Dimension initialDimension) {
		this.builder.setInitialDimension(initialDimension);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder setInitialPosition(final Position initialiPosition) {
		this.builder.setInitialPosition(initialiPosition);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder setInitialSplitWeight(final double initialSplitWeigth) {
		this.builder.setInitialSplitWeight(initialSplitWeigth);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder setApplicationNavigator(final boolean hasApplicationNavigator) {
		this.builder.setApplicationNavigator(hasApplicationNavigator);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder setApplicationsCloseable(final boolean applicationsCloseable) {
		this.builder.setApplicationsCloseable(applicationsCloseable);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder setToolBar(final IToolBarModel toolBarModel) {
		this.builder.setToolBar(toolBarModel);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder setMenuBar(final IMenuBarModel menuBarModel) {
		this.builder.setMenuBar(menuBarModel);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder setStatusBarCreator(final IContentCreator statusBarContentCreator) {
		this.builder.setStatusBarCreator(statusBarContentCreator);
		return this;
	}

	@Override
	public IWorkbenchModelBuilder setLoginCallback(final ILoginCallback loginCallback) {
		this.builder.setLoginCallback(loginCallback);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder setCloseCallback(final ICloseCallback closeCallback) {
		this.builder.setCloseCallback(closeCallback);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder setInitializeCallback(final IWorkbenchInitializeCallback initializeCallback) {
		this.builder.setInitializeCallback(initializeCallback);
		return this;
	}

	@Override
	public IWorkbenchModelBuilder addInitializeCallback(final IWorkbenchInitializeCallback initializeCallback) {
		this.builder.addInitializeCallback(initializeCallback);
		return this;
	}

	@Override
	public IWorkbenchModelBuilder setViewFactoy(final IViewFactory viewFactory) {
		this.builder.setViewFactoy(viewFactory);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder addShutdownHook(final Runnable shutdownHook) {
		this.builder.addShutdownHook(shutdownHook);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder addApplication(final IWorkbenchApplicationModel applicationModel) {
		this.builder.addApplication(applicationModel);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder addApplication(final int index, final IWorkbenchApplicationModel applicationModel) {
		this.builder.addApplication(index, applicationModel);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder addApplication(final IWorkbenchApplicationModelBuilder applicationModelBuilder) {
		this.builder.addApplication(applicationModelBuilder);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder addApplication(
		final int index,
		final IWorkbenchApplicationModelBuilder applicationModelBuilder) {
		this.builder.addApplication(index, applicationModelBuilder);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder addApplication(
		final String id,
		final String label,
		final String tooltip,
		final IImageConstant icon) {
		this.builder.addApplication(id, label, tooltip, icon);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder addApplication(final String id, final String label, final IImageConstant icon) {
		this.builder.addApplication(id, label, icon);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder addApplication(final String id, final String label, final String tooltip) {
		this.builder.addApplication(id, label, tooltip);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder addApplication(final String id, final String label) {
		this.builder.addApplication(id, label);
		return this;
	}

	@Override
	public final IWorkbenchModelBuilder addApplication(final String id) {
		this.builder.addApplication(id);
		return this;
	}

	@Override
	public final IWorkbenchModel build() {
		return builder.build();
	}

	public static IWorkbenchModelBuilder builder() {
		return WorkbenchToolkit.getWorkbenchPartBuilderFactory().workbench();
	}

	public static IWorkbenchModelBuilder builder(final String label, final IImageConstant icon) {
		return builder().setLabel(label).setIcon(icon);
	}

	public static IWorkbenchModelBuilder builder(final String label) {
		return builder().setLabel(label);
	}

	public static IWorkbenchModelBuilder builder(final IWorkbenchDescriptor descriptor) {
		return builder().setDescriptor(descriptor);
	}

}
