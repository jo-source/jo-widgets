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

package org.jowidgets.workbench.impl;

import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.controler.ITreeNodeListener;
import org.jowidgets.tools.controller.ListModelAdapter;
import org.jowidgets.tools.controller.TreeNodeObservable;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.types.VetoHolder;
import org.jowidgets.workbench.api.IComponentNode;
import org.jowidgets.workbench.api.IComponentNodeContext;

public class ComponentNodeContext extends ComponentNodeContainerContext implements IComponentNodeContext {

	private final ComponentNodeContext parentNodeContext;
	private final TreeNodeObservable treeNodeObservable;

	private final IComponentNode componentNode;
	private final ITreeNode treeNode;
	private final IListModelListener popupMenuListener;
	private final ITreeNodeListener treeNodeListener;
	private final IMenuModel popupMenuModel;

	private ComponentContext componentContext;
	private boolean active;

	public ComponentNodeContext(
		final IComponentNode componentNode,
		final ITreeNode treeNode,
		final ComponentNodeContext parentNodeContext,
		final WorkbenchApplicationContext applicationContext,
		final WorkbenchContext workbenchContext) {

		super(treeNode, applicationContext, workbenchContext);

		this.active = false;

		this.treeNodeObservable = new TreeNodeObservable();
		this.parentNodeContext = parentNodeContext;
		this.componentNode = componentNode;

		this.treeNode = treeNode;
		this.treeNode.setText(componentNode.getLabel());
		this.treeNode.setToolTipText(componentNode.getTooltip());
		if (componentNode.getIcon() != null) {
			this.treeNode.setIcon(componentNode.getIcon());
		}

		this.treeNodeListener = createTreeNodeListener();
		treeNode.addTreeNodeListener(treeNodeListener);

		this.popupMenuModel = new MenuModel();
		this.popupMenuListener = createPopupMenuListener();
		popupMenuModel.addListModelListener(popupMenuListener);
	}

	public void activate() {
		this.active = true;
		getComponentContextLazy().activate();
	}

	public boolean isActive() {
		return active;
	}

	public VetoHolder tryDeactivate() {
		final VetoHolder vetoHolder = getComponentContextLazy().deactivate();
		if (!vetoHolder.hasVeto()) {
			this.active = false;
		}
		return vetoHolder;
	}

	@Override
	public void setSelected(final boolean selected) {
		treeNode.setSelected(selected);
	}

	@Override
	public void setLabel(final String label) {
		treeNode.setText(label);
	}

	@Override
	public void setTooltip(final String tooltip) {
		treeNode.setToolTipText(tooltip);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		treeNode.setIcon(icon);
	}

	@Override
	public void setExpanded(final boolean expanded) {
		treeNode.setExpanded(expanded);
	}

	@Override
	public ComponentNodeContext getParent() {
		return parentNodeContext;
	}

	@Override
	public IMenuModel getPopupMenu() {
		return popupMenuModel;
	}

	@Override
	public void addTreeNodeListener(final ITreeNodeListener listener) {
		treeNodeObservable.addTreeNodeListener(listener);
	}

	@Override
	public void removeTreeNodeListener(final ITreeNodeListener listener) {
		treeNodeObservable.removeTreeNodeListener(listener);
	}

	protected String getGlobalId() {
		if (getParent() != null) {
			return getParent().getGlobalId() + "#" + componentNode.getId();
		}
		else {
			return componentNode.getId();
		}
	}

	protected ITreeNode getTreeNode() {
		return treeNode;
	}

	protected void dispose() {
		componentNode.onDispose();
		if (componentContext != null) {
			componentContext.onDispose();
		}
		popupMenuModel.removeListModelListener(popupMenuListener);
		treeNode.removeTreeNodeListener(treeNodeListener);
	}

	private IListModelListener createPopupMenuListener() {
		return new ListModelAdapter() {

			@Override
			public void afterChildRemoved(final int index) {
				if (popupMenuModel.getChildren().size() == 0) {
					treeNode.setPopupMenu(null);
				}
			}

			@Override
			public void afterChildAdded(final int index) {
				if (popupMenuModel.getChildren().size() == 1) {
					treeNode.setPopupMenu(popupMenuModel);
				}
			}
		};
	}

	private ITreeNodeListener createTreeNodeListener() {
		return new ITreeNodeListener() {
			@Override
			public void selectionChanged(final boolean selected) {
				treeNodeObservable.fireSelectionChanged(selected);
			}

			@Override
			public void expandedChanged(final boolean expanded) {
				treeNodeObservable.fireExpandedChanged(expanded);
			}
		};
	}

	private ComponentContext getComponentContextLazy() {
		if (componentContext == null) {
			componentContext = new ComponentContext(componentNode, this);
		}
		return componentContext;
	}

}
