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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.controler.ITreeNodeListener;
import org.jowidgets.workbench.api.IDisposeCallback;
import org.jowidgets.workbench.toolkit.api.IComponentFactory;
import org.jowidgets.workbench.toolkit.api.IComponentNodeContainerModel;
import org.jowidgets.workbench.toolkit.api.IComponentNodeInitializeCallback;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchPartModelListener;

class ComponentNodeModel extends ComponentNodeContainerModel implements IComponentNodeModel {

	private final WorkbenchPartModelObservable workbenchPartModelObservable;
	private final Set<ITreeNodeListener> treeNodeListeners;

	private String label;
	private String tooltip;
	private IImageConstant icon;
	private boolean selected;
	private boolean expanded;
	private IMenuModel popupMenu;
	private final IComponentFactory componentFactory;
	private final IComponentNodeInitializeCallback initializeCallback;
	private final IDisposeCallback disposeCallback;

	private IComponentNodeContainerModel parentContainer;

	ComponentNodeModel(
		final String id,
		final String label,
		final String tooltip,
		final IImageConstant icon,
		final boolean selected,
		final boolean expanded,
		final IMenuModel popupMenu,
		final IComponentFactory componentFactory,
		final IComponentNodeInitializeCallback initializeCallback,
		final IDisposeCallback disposeCallback,
		final List<IComponentNodeModel> children) {
		super(id, children);

		this.workbenchPartModelObservable = new WorkbenchPartModelObservable();
		this.treeNodeListeners = new HashSet<ITreeNodeListener>();

		this.label = label;
		this.tooltip = tooltip;
		this.icon = icon;
		this.selected = selected;
		this.expanded = expanded;
		this.popupMenu = popupMenu;
		this.componentFactory = componentFactory;
		this.initializeCallback = initializeCallback;
		this.disposeCallback = disposeCallback;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getTooltip() {
		return tooltip;
	}

	@Override
	public IImageConstant getIcon() {
		return icon;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public boolean isExpanded() {
		return expanded;
	}

	@Override
	public IMenuModel getPopupMenu() {
		return popupMenu;
	}

	@Override
	public IComponentFactory getComponentFactory() {
		return componentFactory;
	}

	@Override
	public IComponentNodeInitializeCallback getInitializeCallback() {
		return initializeCallback;
	}

	@Override
	public IDisposeCallback getDisposeCallback() {
		return disposeCallback;
	}

	@Override
	public void setLabel(final String label) {
		this.label = label;
		fireModelChanged();
	}

	@Override
	public void setTooltip(final String tooltip) {
		this.tooltip = tooltip;
		fireModelChanged();
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		this.icon = icon;
		fireModelChanged();
	}

	@Override
	public void setSelected(final boolean selected) {
		this.selected = selected;
		fireModelChanged();
		fireSelectionChanged(selected);
	}

	@Override
	public void setExpanded(final boolean expanded) {
		this.expanded = expanded;
		fireModelChanged();
		fireExpandedChanged(expanded);
	}

	@Override
	public void setPopupMenu(final IMenuModel popupMenu) {
		this.popupMenu = popupMenu;
		fireModelChanged();
	}

	@Override
	public String getPathId() {
		final LinkedList<IComponentNodeContainerModel> containerList = new LinkedList<IComponentNodeContainerModel>();
		IComponentNodeContainerModel container = this;
		containerList.add(this);
		while (container.getParentContainer() != null) {
			container = container.getParentContainer();
			containerList.addFirst(container);
		}
		final StringBuilder result = new StringBuilder();
		for (final IComponentNodeContainerModel containerModel : containerList) {
			result.append(containerModel.getId().toString() + "#");
		}
		return result.substring(0, result.length() - 1);
	}

	@Override
	public void setParentContainer(final IComponentNodeContainerModel parentContainer) {
		if (this.parentContainer != parentContainer) {
			if (this.parentContainer != null) {
				this.parentContainer.remove(getUnwrappedThis());
			}
			if (parentContainer != null) {
				if (!parentContainer.getChildren().contains(getUnwrappedThis())) {
					parentContainer.addChild(getUnwrappedThis());
				}
			}
			this.parentContainer = parentContainer;
		}
	}

	@Override
	public IComponentNodeContainerModel getParentContainer() {
		return parentContainer;
	}

	@Override
	public IComponentNodeModel getParent() {
		final IComponentNodeContainerModel parent = getParentContainer();
		if (parent instanceof IComponentNodeModel) {
			return (IComponentNodeModel) parent;
		}
		return null;
	}

	@Override
	public IWorkbenchApplicationModel getApplication() {
		IComponentNodeContainerModel parent = getParentContainer();
		while (parent.getParentContainer() != null) {
			parent = parent.getParentContainer();
		}
		if (parent instanceof IWorkbenchApplicationModel) {
			return (IWorkbenchApplicationModel) parent;
		}
		return null;
	}

	@Override
	public IWorkbenchModel getWorkbench() {
		final IWorkbenchApplicationModel application = getApplication();
		if (application != null) {
			return application.getWorkbench();
		}
		return null;
	}

	@Override
	public void addWorkbenchPartModelListener(final IWorkbenchPartModelListener listener) {
		workbenchPartModelObservable.addWorkbenchPartModelListener(listener);
	}

	@Override
	public void removeWorkbenchPartModelListener(final IWorkbenchPartModelListener listener) {
		workbenchPartModelObservable.removeWorkbenchPartModelListener(listener);
	}

	@Override
	public void addTreeNodeListener(final ITreeNodeListener listener) {
		treeNodeListeners.add(listener);
	}

	@Override
	public void removeTreeNodeListener(final ITreeNodeListener listener) {
		treeNodeListeners.remove(listener);
	}

	@Override
	public IComponentNodeModel getUnwrappedThis() {
		return this;
	}

	private void fireSelectionChanged(final boolean selected) {
		for (final ITreeNodeListener listener : treeNodeListeners) {
			listener.selectionChanged(selected);
		}
	}

	private void fireExpandedChanged(final boolean expanded) {
		for (final ITreeNodeListener listener : treeNodeListeners) {
			listener.expandedChanged(expanded);
		}
	}

	private void fireModelChanged() {
		workbenchPartModelObservable.fireModelChanged();
	}

}
