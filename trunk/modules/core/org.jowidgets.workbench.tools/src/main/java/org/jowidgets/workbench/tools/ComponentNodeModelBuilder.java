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
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.api.IDisposeCallback;
import org.jowidgets.workbench.toolkit.api.IComponentFactory;
import org.jowidgets.workbench.toolkit.api.IComponentNodeInitializeCallback;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModelBuilder;
import org.jowidgets.workbench.toolkit.api.WorkbenchToolkit;

public class ComponentNodeModelBuilder implements IComponentNodeModelBuilder {

	private final IComponentNodeModelBuilder builder;

	public ComponentNodeModelBuilder() {
		this(builder());
	}

	public ComponentNodeModelBuilder(final IComponentFactory componentFactory) {
		this(builder(componentFactory));
	}

	public ComponentNodeModelBuilder(final IComponentFactory componentFactory, final String label) {
		this(builder(componentFactory, label));
	}

	public ComponentNodeModelBuilder(final IComponentFactory componentFactory, final String label, final IImageConstant icon) {
		this(builder(componentFactory, label, icon));
	}

	public ComponentNodeModelBuilder(final IComponentFactory componentFactory, final String label, final String tooltip) {
		this(builder(componentFactory, label, tooltip));
	}

	public ComponentNodeModelBuilder(
		final IComponentFactory componentFactory,
		final String label,
		final String tooltip,
		final IImageConstant icon) {
		this(builder(componentFactory, label, tooltip, icon));
	}

	public ComponentNodeModelBuilder(final Class<? extends IComponent> componentType) {
		this(builder(componentType));
	}

	public ComponentNodeModelBuilder(final Class<? extends IComponent> componentType, final String label) {
		this(builder(componentType, label));
	}

	public ComponentNodeModelBuilder(
		final Class<? extends IComponent> componentType,
		final String label,
		final IImageConstant icon) {
		this(builder(componentType, label, icon));
	}

	public ComponentNodeModelBuilder(final Class<? extends IComponent> componentType, final String label, final String tooltip) {
		this(builder(componentType, label, tooltip));
	}

	public ComponentNodeModelBuilder(
		final Class<? extends IComponent> componentType,
		final String label,
		final String tooltip,
		final IImageConstant icon) {
		this(builder(componentType, label, tooltip, icon));
	}

	public ComponentNodeModelBuilder(final String label) {
		this(builder(label));
	}

	public ComponentNodeModelBuilder(final String label, final IImageConstant icon) {
		this(builder(label, icon));
	}

	public ComponentNodeModelBuilder(final String label, final String tooltip) {
		this(builder(label, tooltip));
	}

	public ComponentNodeModelBuilder(final String label, final String tooltip, final IImageConstant icon) {
		this(builder(label, tooltip, icon));
	}

	public ComponentNodeModelBuilder(final IComponentNodeModelBuilder builder) {
		Assert.paramNotNull(builder, "builder");
		this.builder = builder;
	}

	@Override
	public final IComponentNodeModelBuilder setLabel(final String label) {
		builder.setLabel(label);
		return this;
	}

	@Override
	public final IComponentNodeModelBuilder setId(final String id) {
		builder.setId(id);
		return this;
	}

	@Override
	public final IComponentNodeModelBuilder setTooltip(final String toolTiptext) {
		builder.setTooltip(toolTiptext);
		return this;
	}

	@Override
	public final IComponentNodeModelBuilder addChild(final IComponentNodeModel childModel) {
		builder.addChild(childModel);
		return this;
	}

	@Override
	public final IComponentNodeModelBuilder setIcon(final IImageConstant icon) {
		builder.setIcon(icon);
		return this;
	}

	@Override
	public final IComponentNodeModelBuilder addChild(final int index, final IComponentNodeModel childModel) {
		builder.addChild(index, childModel);
		return this;
	}

	@Override
	public final IComponentNodeModelBuilder setSelected(final boolean selected) {
		builder.setSelected(selected);
		return this;
	}

	@Override
	public final IComponentNodeModelBuilder addChild(final IComponentNodeModelBuilder childModel) {
		builder.addChild(childModel);
		return this;
	}

	@Override
	public final IComponentNodeModelBuilder setExpanded(final boolean expanded) {
		builder.setExpanded(expanded);
		return this;
	}

	@Override
	public final IComponentNodeModelBuilder addChild(final int index, final IComponentNodeModelBuilder childModel) {
		builder.addChild(index, childModel);
		return this;
	}

	@Override
	public final IComponentNodeModelBuilder setPopupMenu(final IMenuModel popupMenu) {
		builder.setPopupMenu(popupMenu);
		return this;
	}

	@Override
	public final IComponentNodeModelBuilder setComponentFactory(final IComponentFactory componentFactory) {
		builder.setComponentFactory(componentFactory);
		return this;
	}

	@Override
	public final IComponentNodeModelBuilder addChild(
		final String id,
		final String label,
		final String tooltip,
		final IImageConstant icon) {
		builder.addChild(id, label, tooltip, icon);
		return this;
	}

	@Override
	public final IComponentNodeModelBuilder setComponentFactory(final Class<? extends IComponent> componentType) {
		builder.setComponentFactory(componentType);
		return this;
	}

	@Override
	public final IComponentNodeModelBuilder addChild(final String id, final String label, final IImageConstant icon) {
		builder.addChild(id, label, icon);
		return this;
	}

	@Override
	public final IComponentNodeModelBuilder setInitializeCallback(final IComponentNodeInitializeCallback initializeCallback) {
		builder.setInitializeCallback(initializeCallback);
		return this;
	}

	@Override
	public final IComponentNodeModelBuilder setDisposeCallback(final IDisposeCallback disposeCallback) {
		builder.setDisposeCallback(disposeCallback);
		return this;
	}

	@Override
	public final IComponentNodeModelBuilder addChild(final String id, final String label, final String tooltip) {
		builder.addChild(id, label, tooltip);
		return this;
	}

	@Override
	public final IComponentNodeModelBuilder addChild(final String id, final String label) {
		builder.addChild(id, label);
		return this;
	}

	@Override
	public final IComponentNodeModelBuilder addChild(final String id) {
		builder.addChild(id);
		return this;
	}

	@Override
	public final IComponentNodeModel build() {
		return builder.build();
	}

	public static IComponentNodeModelBuilder builder() {
		return WorkbenchToolkit.getWorkbenchPartBuilderFactory().componentNode();
	}

	public static IComponentNodeModelBuilder builder(final String label) {
		return builder().setLabel(label);
	}

	public static IComponentNodeModelBuilder builder(final String label, final IImageConstant icon) {
		return builder(label).setIcon(icon);
	}

	public static IComponentNodeModelBuilder builder(final String label, final String tooltip) {
		return builder(label).setTooltip(tooltip);
	}

	public static IComponentNodeModelBuilder builder(final String label, final String tooltip, final IImageConstant icon) {
		return builder(label, tooltip).setIcon(icon);
	}

	public static IComponentNodeModelBuilder builder(final IComponentFactory componentFactory) {
		return builder().setComponentFactory(componentFactory);
	}

	public static IComponentNodeModelBuilder builder(final IComponentFactory componentFactory, final String label) {
		return builder(label).setComponentFactory(componentFactory);
	}

	public static IComponentNodeModelBuilder builder(
		final IComponentFactory componentFactory,
		final String label,
		final IImageConstant icon) {
		return builder(label, icon).setComponentFactory(componentFactory);
	}

	public static IComponentNodeModelBuilder builder(
		final IComponentFactory componentFactory,
		final String label,
		final String tooltip) {
		return builder(label, tooltip).setComponentFactory(componentFactory);
	}

	public static IComponentNodeModelBuilder builder(
		final IComponentFactory componentFactory,
		final String label,
		final String tooltip,
		final IImageConstant icon) {
		return builder(label, tooltip, icon).setComponentFactory(componentFactory);
	}

	public static IComponentNodeModelBuilder builder(final Class<? extends IComponent> componentType) {
		return builder().setComponentFactory(componentType);
	}

	public static IComponentNodeModelBuilder builder(final Class<? extends IComponent> componentType, final String label) {
		return builder(label).setComponentFactory(componentType);
	}

	public static IComponentNodeModelBuilder builder(
		final Class<? extends IComponent> componentType,
		final String label,
		final IImageConstant icon) {
		return builder(label, icon).setComponentFactory(componentType);
	}

	public static IComponentNodeModelBuilder builder(
		final Class<? extends IComponent> componentType,
		final String label,
		final String tooltip) {
		return builder(label, tooltip).setComponentFactory(componentType);
	}

	public static IComponentNodeModelBuilder builder(
		final Class<? extends IComponent> componentType,
		final String label,
		final String tooltip,
		final IImageConstant icon) {
		return builder(label, tooltip, icon).setComponentFactory(componentType);
	}

}
