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

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.api.IComponentContext;
import org.jowidgets.workbench.api.IComponentNode;
import org.jowidgets.workbench.api.IComponentNodeContext;
import org.jowidgets.workbench.toolkit.api.IComponentFactory;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModelBuilder;
import org.jowidgets.workbench.toolkit.api.WorkbenchToolkit;

public class ComponentNode implements IComponentNode {

	private final IComponentNode node;
	private final IComponentNodeModel model;

	public ComponentNode(final IComponentFactory componentFactory) {
		this(builder(componentFactory));
	}

	public ComponentNode(final IComponentFactory componentFactory, final String label) {
		this(builder(componentFactory, label));
	}

	public ComponentNode(final IComponentFactory componentFactory, final String label, final IImageConstant icon) {
		this(builder(componentFactory, label, icon));
	}

	public ComponentNode(final IComponentFactory componentFactory, final String label, final String tooltip) {
		this(builder(componentFactory, label, tooltip));
	}

	public ComponentNode(
		final IComponentFactory componentFactory,
		final String label,
		final String tooltip,
		final IImageConstant icon) {
		this(builder(componentFactory, label, tooltip, icon));
	}

	public ComponentNode(final Class<? extends IComponent> componentType) {
		this(builder(componentType));
	}

	public ComponentNode(final Class<? extends IComponent> componentType, final String label) {
		this(builder(componentType, label));
	}

	public ComponentNode(final Class<? extends IComponent> componentType, final String label, final IImageConstant icon) {
		this(builder(componentType, label, icon));
	}

	public ComponentNode(final Class<? extends IComponent> componentType, final String label, final String tooltip) {
		this(builder(componentType, label, tooltip));
	}

	public ComponentNode(
		final Class<? extends IComponent> componentType,
		final String label,
		final String tooltip,
		final IImageConstant icon) {
		this(builder(componentType, label, tooltip, icon));
	}

	public ComponentNode() {
		this(builder());
	}

	public ComponentNode(final String label) {
		this(builder(label));
	}

	public ComponentNode(final String label, final IImageConstant icon) {
		this(builder(label, icon));
	}

	public ComponentNode(final String label, final String tooltip) {
		this(builder(label, tooltip));
	}

	public ComponentNode(final String label, final String tooltip, final IImageConstant icon) {
		this(builder(label, tooltip, icon));
	}

	public ComponentNode(final IComponentNodeModelBuilder builder) {
		this(build(builder));
	}

	public ComponentNode(final IComponentNodeModel model) {
		Assert.paramNotNull(model, "model");
		this.node = WorkbenchToolkit.getWorkbenchPartFactory().componentNode(model);
		this.model = model;
	}

	public IComponentNodeModel getModel() {
		return model;
	}

	@Override
	public String getId() {
		return node.getId();
	}

	@Override
	public String getLabel() {
		return node.getLabel();
	}

	@Override
	public String getTooltip() {
		return node.getTooltip();
	}

	@Override
	public IImageConstant getIcon() {
		return node.getIcon();
	}

	@Override
	public final IComponent createComponent(final IComponentContext context) {
		return node.createComponent(context);
	}

	@Override
	public final void onContextInitialize(final IComponentNodeContext context) {
		node.onContextInitialize(context);
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

	private static IComponentNodeModel build(final IComponentNodeModelBuilder builder) {
		Assert.paramNotNull(builder, "builder");
		return builder.build();
	}

}
