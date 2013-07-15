/*
 * Copyright (c) 2012, grossmann
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
import org.jowidgets.workbench.api.IDisposeCallback;
import org.jowidgets.workbench.toolkit.api.IComponentFactory;
import org.jowidgets.workbench.toolkit.api.IComponentNodeContainerModel;
import org.jowidgets.workbench.toolkit.api.IComponentNodeInitializeCallback;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModelBuilder;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchPartModelListener;

public class ComponentNodeModelWrapper implements IComponentNodeModel {

	private final IComponentNodeModel original;

	public ComponentNodeModelWrapper(final IComponentNodeModel original) {
		Assert.paramNotNull(original, "original");
		this.original = original;
	}

	@Override
	public void addListModelListener(final IListModelListener listener) {
		original.addListModelListener(listener);
	}

	@Override
	public void addTreeNodeListener(final ITreeNodeListener listener) {
		original.addTreeNodeListener(listener);
	}

	@Override
	public void addWorkbenchPartModelListener(final IWorkbenchPartModelListener listener) {
		original.addWorkbenchPartModelListener(listener);
	}

	@Override
	public String getId() {
		return original.getId();
	}

	@Override
	public void removeListModelListener(final IListModelListener listener) {
		original.removeListModelListener(listener);
	}

	@Override
	public String getLabel() {
		return original.getLabel();
	}

	@Override
	public void removeTreeNodeListener(final ITreeNodeListener listener) {
		original.removeTreeNodeListener(listener);
	}

	@Override
	public String getTooltip() {
		return original.getTooltip();
	}

	@Override
	public void removeWorkbenchPartModelListener(final IWorkbenchPartModelListener listener) {
		original.removeWorkbenchPartModelListener(listener);
	}

	@Override
	public IImageConstant getIcon() {
		return original.getIcon();
	}

	@Override
	public IComponentNodeContainerModel getParentContainer() {
		return original.getParentContainer();
	}

	@Override
	public List<IComponentNodeModel> getChildren() {
		return original.getChildren();
	}

	@Override
	public int getChildrenCount() {
		return original.getChildrenCount();
	}

	@Override
	public IComponentNodeModel addChild(final IComponentNodeModel childModel) {
		return original.addChild(childModel);
	}

	@Override
	public IComponentNodeModel addChild(final int index, final IComponentNodeModel childModel) {
		return original.addChild(index, childModel);
	}

	@Override
	public boolean isSelected() {
		return original.isSelected();
	}

	@Override
	public boolean isExpanded() {
		return original.isExpanded();
	}

	@Override
	public IMenuModel getPopupMenu() {
		return original.getPopupMenu();
	}

	@Override
	public IComponentNodeModel addChild(final IComponentNodeModelBuilder childModelBuilder) {
		return original.addChild(childModelBuilder);
	}

	@Override
	public IComponentFactory getComponentFactory() {
		return original.getComponentFactory();
	}

	@Override
	public IComponentNodeInitializeCallback getInitializeCallback() {
		return original.getInitializeCallback();
	}

	@Override
	public IComponentNodeModel addChild(final int index, final IComponentNodeModelBuilder childModelBuilder) {
		return original.addChild(index, childModelBuilder);
	}

	@Override
	public IDisposeCallback getDisposeCallback() {
		return original.getDisposeCallback();
	}

	@Override
	public IComponentNodeModel addChild(final String id, final String label, final String tooltip, final IImageConstant icon) {
		return original.addChild(id, label, tooltip, icon);
	}

	@Override
	public void setLabel(final String label) {
		original.setLabel(label);
	}

	@Override
	public void setTooltip(final String toolTip) {
		original.setTooltip(toolTip);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		original.setIcon(icon);
	}

	@Override
	public IComponentNodeModel addChild(final String id, final String label, final IImageConstant icon) {
		return original.addChild(id, label, icon);
	}

	@Override
	public void setSelected(final boolean selected) {
		original.setSelected(selected);
	}

	@Override
	public void setExpanded(final boolean expanded) {
		original.setExpanded(expanded);
	}

	@Override
	public IComponentNodeModel addChild(final String id, final String label, final String tooltip) {
		return original.addChild(id, label, tooltip);
	}

	@Override
	public void setPopupMenu(final IMenuModel popupMenu) {
		original.setPopupMenu(popupMenu);
	}

	@Override
	public String getPathId() {
		return original.getPathId();
	}

	@Override
	public IComponentNodeModel addChild(final String id, final String label) {
		return original.addChild(id, label);
	}

	@Override
	public void setParentContainer(final IComponentNodeContainerModel parentContainer) {
		original.setParentContainer(parentContainer);
	}

	@Override
	public IComponentNodeModel addChild(final String id) {
		return original.addChild(id);
	}

	@Override
	public void remove(final int index) {
		original.remove(index);
	}

	@Override
	public void remove(final IComponentNodeModel childModel) {
		original.remove(childModel);
	}

	@Override
	public void removeAll() {
		original.removeAll();
	}

	@Override
	public IComponentNodeModel getParent() {
		return original.getParent();
	}

	@Override
	public IWorkbenchApplicationModel getApplication() {
		return original.getApplication();
	}

	@Override
	public IWorkbenchModel getWorkbench() {
		return original.getWorkbench();
	}

	@Override
	public IComponentNodeModel unwrap() {
		return original.unwrap();
	}

}
