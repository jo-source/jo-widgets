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
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.ILifecycleCallback;
import org.jowidgets.workbench.toolkit.api.IComponentNodeContainerModel;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModelBuilder;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationInitializeCallback;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModelBuilder;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchPartModelListener;
import org.jowidgets.workbench.toolkit.api.WorkbenchToolkit;

public class WorkbenchApplicationModel implements IWorkbenchApplicationModel {

	private final IWorkbenchApplicationModel model;

	public WorkbenchApplicationModel() {
		this(builder());
	}

	public WorkbenchApplicationModel(final String label) {
		this(builder(label));
	}

	public WorkbenchApplicationModel(final String label, final IImageConstant icon) {
		this(builder(label, icon));
	}

	public WorkbenchApplicationModel(final String label, final String tooltip) {
		this(builder(label, tooltip));
	}

	public WorkbenchApplicationModel(final String label, final String tooltip, final IImageConstant icon) {
		this(builder(label, tooltip, icon));
	}

	public WorkbenchApplicationModel(final IWorkbenchApplicationModelBuilder builder) {
		Assert.paramNotNull(builder, "builder");
		this.model = builder.build();
	}

	@Override
	public String getId() {
		return model.getId();
	}

	@Override
	public String getLabel() {
		return model.getLabel();
	}

	@Override
	public String getTooltip() {
		return model.getTooltip();
	}

	@Override
	public IImageConstant getIcon() {
		return model.getIcon();
	}

	@Override
	public IComponentNodeContainerModel getParentContainer() {
		return model.getParentContainer();
	}

	@Override
	public List<IComponentNodeModel> getChildren() {
		return model.getChildren();
	}

	@Override
	public int getChildrenCount() {
		return model.getChildrenCount();
	}

	@Override
	public IComponentNodeModel addChild(final IComponentNodeModel childModel) {
		return model.addChild(childModel);
	}

	@Override
	public IWorkbenchApplicationInitializeCallback getInitializeCallback() {
		return model.getInitializeCallback();
	}

	@Override
	public IComponentNodeModel addChild(final int index, final IComponentNodeModel childModel) {
		return model.addChild(index, childModel);
	}

	@Override
	public IMenuModel getPopupMenu() {
		return model.getPopupMenu();
	}

	@Override
	public IToolBarModel getToolBar() {
		return model.getToolBar();
	}

	@Override
	public IComponentNodeModel addChild(final IComponentNodeModelBuilder childModelBuilder) {
		return model.addChild(childModelBuilder);
	}

	@Override
	public IMenuModel getToolBarMenu() {
		return model.getToolBarMenu();
	}

	@Override
	public ILifecycleCallback getLifecycleCallback() {
		return model.getLifecycleCallback();
	}

	@Override
	public IComponentNodeModel addChild(final int index, final IComponentNodeModelBuilder childModelBuilder) {
		return model.addChild(index, childModelBuilder);
	}

	@Override
	public void setPopupMenu(final IMenuModel menuModel) {
		model.setPopupMenu(menuModel);
	}

	@Override
	public void setToolBar(final IToolBarModel toolBarModel) {
		model.setToolBar(toolBarModel);
	}

	@Override
	public IComponentNodeModel addChild(final String id, final String label, final String tooltip, final IImageConstant icon) {
		return model.addChild(id, label, tooltip, icon);
	}

	@Override
	public void setToolBarMenu(final IMenuModel toolBarMenu) {
		model.setToolBarMenu(toolBarMenu);
	}

	@Override
	public void setLifecycleCallback(final ILifecycleCallback lifecycleCallback) {
		model.setLifecycleCallback(lifecycleCallback);
	}

	@Override
	public IComponentNodeModel addChild(final String id, final String label, final IImageConstant icon) {
		return model.addChild(id, label, icon);
	}

	@Override
	public IWorkbenchModel getWorkbench() {
		return model.getWorkbench();
	}

	@Override
	public void setWorkbench(final IWorkbenchModel workbench) {
		model.setWorkbench(workbench);
	}

	@Override
	public IComponentNodeModel addChild(final String id, final String label, final String tooltip) {
		return model.addChild(id, label, tooltip);
	}

	@Override
	public IComponentNodeModel addChild(final String id, final String label) {
		return model.addChild(id, label);
	}

	@Override
	public IComponentNodeModel addChild(final String id) {
		return model.addChild(id);
	}

	@Override
	public void remove(final int index) {
		model.remove(index);
	}

	@Override
	public void remove(final IComponentNodeModel childModel) {
		model.remove(childModel);
	}

	@Override
	public void removeAll() {
		model.removeAll();
	}

	@Override
	public void addListModelListener(final IListModelListener listener) {
		model.addListModelListener(listener);
	}

	@Override
	public void removeListModelListener(final IListModelListener listener) {
		model.removeListModelListener(listener);
	}

	@Override
	public void addWorkbenchPartModelListener(final IWorkbenchPartModelListener listener) {
		model.addWorkbenchPartModelListener(listener);
	}

	@Override
	public void removeWorkbenchPartModelListener(final IWorkbenchPartModelListener listener) {
		model.removeWorkbenchPartModelListener(listener);
	}

	@Override
	public IWorkbenchApplicationModel getUnwrappedThis() {
		return model.getUnwrappedThis();
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
