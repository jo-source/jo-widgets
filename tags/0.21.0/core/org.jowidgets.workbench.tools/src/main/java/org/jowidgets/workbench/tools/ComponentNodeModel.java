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

import java.util.List;

import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.controller.ITreeNodeListener;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.api.IDisposeCallback;
import org.jowidgets.workbench.toolkit.api.IComponentFactory;
import org.jowidgets.workbench.toolkit.api.IComponentNodeContainerModel;
import org.jowidgets.workbench.toolkit.api.IComponentNodeInitializeCallback;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModelBuilder;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchPartModelListener;
import org.jowidgets.workbench.toolkit.api.WorkbenchToolkit;

public class ComponentNodeModel implements IComponentNodeModel {

	private final IComponentNodeModel model;

	public ComponentNodeModel() {
		this(builder());
	}

	public ComponentNodeModel(final IComponentFactory componentFactory) {
		this(builder(componentFactory));
	}

	public ComponentNodeModel(final IComponentFactory componentFactory, final String label) {
		this(builder(componentFactory, label));
	}

	public ComponentNodeModel(final IComponentFactory componentFactory, final String label, final IImageConstant icon) {
		this(builder(componentFactory, label, icon));
	}

	public ComponentNodeModel(final IComponentFactory componentFactory, final String label, final String tooltip) {
		this(builder(componentFactory, label, tooltip));
	}

	public ComponentNodeModel(
		final IComponentFactory componentFactory,
		final String label,
		final String tooltip,
		final IImageConstant icon) {
		this(builder(componentFactory, label, tooltip, icon));
	}

	public ComponentNodeModel(final Class<? extends IComponent> componentType) {
		this(builder(componentType));
	}

	public ComponentNodeModel(final Class<? extends IComponent> componentType, final String label) {
		this(builder(componentType, label));
	}

	public ComponentNodeModel(final Class<? extends IComponent> componentType, final String label, final IImageConstant icon) {
		this(builder(componentType, label, icon));
	}

	public ComponentNodeModel(final Class<? extends IComponent> componentType, final String label, final String tooltip) {
		this(builder(componentType, label, tooltip));
	}

	public ComponentNodeModel(
		final Class<? extends IComponent> componentType,
		final String label,
		final String tooltip,
		final IImageConstant icon) {
		this(builder(componentType, label, tooltip, icon));
	}

	public ComponentNodeModel(final String label) {
		this(builder(label));
	}

	public ComponentNodeModel(final String label, final IImageConstant icon) {
		this(builder(label, icon));
	}

	public ComponentNodeModel(final String label, final String tooltip) {
		this(builder(label, tooltip));
	}

	public ComponentNodeModel(final String label, final String tooltip, final IImageConstant icon) {
		this(builder(label, tooltip, icon));
	}

	public ComponentNodeModel(final IComponentNodeModelBuilder builder) {
		this(build(builder));
	}

	public ComponentNodeModel(final IComponentNodeModel model) {
		Assert.paramNotNull(model, "model");
		this.model = model;
	}

	@Override
	public final void addListModelListener(final IListModelListener listener) {
		model.addListModelListener(listener);
	}

	@Override
	public final void addTreeNodeListener(final ITreeNodeListener listener) {
		model.addTreeNodeListener(listener);
	}

	@Override
	public final void addWorkbenchPartModelListener(final IWorkbenchPartModelListener listener) {
		model.addWorkbenchPartModelListener(listener);
	}

	@Override
	public final String getId() {
		return model.getId();
	}

	@Override
	public final void removeListModelListener(final IListModelListener listener) {
		model.removeListModelListener(listener);
	}

	@Override
	public final String getLabel() {
		return model.getLabel();
	}

	@Override
	public final void removeTreeNodeListener(final ITreeNodeListener listener) {
		model.removeTreeNodeListener(listener);
	}

	@Override
	public final String getTooltip() {
		return model.getTooltip();
	}

	@Override
	public final void removeWorkbenchPartModelListener(final IWorkbenchPartModelListener listener) {
		model.removeWorkbenchPartModelListener(listener);
	}

	@Override
	public final IImageConstant getIcon() {
		return model.getIcon();
	}

	@Override
	public final IComponentNodeContainerModel getParentContainer() {
		return model.getParentContainer();
	}

	@Override
	public final List<IComponentNodeModel> getChildren() {
		return model.getChildren();
	}

	@Override
	public final int getChildrenCount() {
		return model.getChildrenCount();
	}

	@Override
	public final IComponentNodeModel addChild(final IComponentNodeModel childModel) {
		return model.addChild(childModel);
	}

	@Override
	public final boolean isSelected() {
		return model.isSelected();
	}

	@Override
	public final boolean isExpanded() {
		return model.isExpanded();
	}

	@Override
	public final IComponentNodeModel addChild(final int index, final IComponentNodeModel childModel) {
		return model.addChild(index, childModel);
	}

	@Override
	public final IMenuModel getPopupMenu() {
		return model.getPopupMenu();
	}

	@Override
	public final IComponentFactory getComponentFactory() {
		return model.getComponentFactory();
	}

	@Override
	public final IComponentNodeModel addChild(final IComponentNodeModelBuilder childModelBuilder) {
		return model.addChild(childModelBuilder);
	}

	@Override
	public final IComponentNodeInitializeCallback getInitializeCallback() {
		return model.getInitializeCallback();
	}

	@Override
	public IDisposeCallback getDisposeCallback() {
		return model.getDisposeCallback();
	}

	@Override
	public final void setLabel(final String label) {
		model.setLabel(label);
	}

	@Override
	public final IComponentNodeModel addChild(final int index, final IComponentNodeModelBuilder childModelBuilder) {
		return model.addChild(index, childModelBuilder);
	}

	@Override
	public final void setTooltip(final String toolTip) {
		model.setTooltip(toolTip);
	}

	@Override
	public final void setIcon(final IImageConstant icon) {
		model.setIcon(icon);
	}

	@Override
	public final IComponentNodeModel addChild(final String id, final String label, final String tooltip, final IImageConstant icon) {
		return model.addChild(id, label, tooltip, icon);
	}

	@Override
	public final void setSelected(final boolean selected) {
		model.setSelected(selected);
	}

	@Override
	public final void setExpanded(final boolean expanded) {
		model.setExpanded(expanded);
	}

	@Override
	public final void setPopupMenu(final IMenuModel popupMenu) {
		model.setPopupMenu(popupMenu);
	}

	@Override
	public final IComponentNodeModel addChild(final String id, final String label, final IImageConstant icon) {
		return model.addChild(id, label, icon);
	}

	@Override
	public final String getPathId() {
		return model.getPathId();
	}

	@Override
	public final void setParentContainer(final IComponentNodeContainerModel parentContainer) {
		model.setParentContainer(parentContainer);
	}

	@Override
	public final IComponentNodeModel addChild(final String id, final String label, final String tooltip) {
		return model.addChild(id, label, tooltip);
	}

	@Override
	public final IComponentNodeModel addChild(final String id, final String label) {
		return model.addChild(id, label);
	}

	@Override
	public final IComponentNodeModel addChild(final String id) {
		return model.addChild(id);
	}

	@Override
	public final void remove(final int index) {
		model.remove(index);
	}

	@Override
	public final void remove(final IComponentNodeModel childModel) {
		model.remove(childModel);
	}

	@Override
	public final void removeAll() {
		model.removeAll();
	}

	@Override
	public final IComponentNodeModel getParent() {
		return model.getParent();
	}

	@Override
	public final IWorkbenchApplicationModel getApplication() {
		return model.getApplication();
	}

	@Override
	public final IWorkbenchModel getWorkbench() {
		return model.getWorkbench();
	}

	@Override
	public final IComponentNodeModel unwrap() {
		return model.unwrap();
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
