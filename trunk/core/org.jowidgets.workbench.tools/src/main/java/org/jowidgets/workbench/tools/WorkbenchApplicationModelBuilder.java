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

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.ILifecycleCallback;
import org.jowidgets.workbench.api.IWorkbenchApplicationDescriptor;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModelBuilder;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationInitializeCallback;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModelBuilder;
import org.jowidgets.workbench.toolkit.api.WorkbenchToolkit;

public class WorkbenchApplicationModelBuilder implements IWorkbenchApplicationModelBuilder {

	private final IWorkbenchApplicationModelBuilder builder;

	public WorkbenchApplicationModelBuilder() {
		this(builder());
	}

	public WorkbenchApplicationModelBuilder(final String label) {
		this(builder(label));
	}

	public WorkbenchApplicationModelBuilder(final String label, final IImageConstant icon) {
		this(builder(label, icon));
	}

	public WorkbenchApplicationModelBuilder(final String label, final String tooltip) {
		this(builder(label, tooltip));
	}

	public WorkbenchApplicationModelBuilder(final String label, final String tooltip, final IImageConstant icon) {
		this(builder(label, tooltip, icon));
	}

	public WorkbenchApplicationModelBuilder(final IWorkbenchApplicationModelBuilder builder) {
		Assert.paramNotNull(builder, "builder");
		this.builder = builder;
	}

	@Override
	public IWorkbenchApplicationModelBuilder setLabel(final String label) {
		builder.setLabel(label);
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder setId(final String id) {
		builder.setId(id);
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder setTooltip(final String toolTiptext) {
		builder.setTooltip(toolTiptext);
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder addChild(final IComponentNodeModel childModel) {
		builder.addChild(childModel);
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder setIcon(final IImageConstant icon) {
		builder.setIcon(icon);
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder addChild(final int index, final IComponentNodeModel childModel) {
		builder.addChild(index, childModel);
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder addChild(final IComponentNodeModelBuilder childModel) {
		builder.addChild(childModel);
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder addChild(final int index, final IComponentNodeModelBuilder childModel) {
		builder.addChild(index, childModel);
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder setPopupMenu(final IMenuModel menuModel) {
		builder.setPopupMenu(menuModel);
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder addChild(
		final String id,
		final String label,
		final String tooltip,
		final IImageConstant icon) {
		builder.addChild(id, label, tooltip, icon);
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder setToolBar(final IToolBarModel toolBarModel) {
		builder.setToolBar(toolBarModel);
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder setToolBarMenu(final IMenuModel toolBarMenu) {
		builder.setToolBarMenu(toolBarMenu);
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder addChild(final String id, final String label, final IImageConstant icon) {
		builder.addChild(id, label, icon);
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder setLifecycleCallback(final ILifecycleCallback lifecycleCallback) {
		builder.setLifecycleCallback(lifecycleCallback);
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder addChild(final String id, final String label, final String tooltip) {
		builder.addChild(id, label, tooltip);
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder addChild(final String id, final String label) {
		builder.addChild(id, label);
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder setInitializeCallback(
		final IWorkbenchApplicationInitializeCallback initializeCallback) {
		builder.setInitializeCallback(initializeCallback);
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder addChild(final String id) {
		builder.addChild(id);
		return this;
	}

	@Override
	public IWorkbenchApplicationModelBuilder setDescriptor(final IWorkbenchApplicationDescriptor descriptor) {
		builder.setDescriptor(descriptor);
		return this;
	}

	@Override
	public IWorkbenchApplicationModel build() {
		return builder.build();
	}

	public static IWorkbenchApplicationModelBuilder builder() {
		return WorkbenchToolkit.getWorkbenchPartBuilderFactory().application();
	}

	public static IWorkbenchApplicationModelBuilder builder(final String label) {
		return builder().setLabel(label);
	}

	public static IWorkbenchApplicationModelBuilder builder(final String label, final IImageConstant icon) {
		return builder(label).setIcon(icon);
	}

	public static IWorkbenchApplicationModelBuilder builder(final String label, final String tooltip) {
		return builder(label).setTooltip(tooltip);
	}

	public static IWorkbenchApplicationModelBuilder builder(final String label, final String tooltip, final IImageConstant icon) {
		return builder(label, tooltip).setIcon(icon);
	}

}
