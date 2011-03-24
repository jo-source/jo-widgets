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

import java.util.HashMap;
import java.util.Map;

import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.controler.ITreeNodeListener;
import org.jowidgets.tools.controler.TreeNodeObservable;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.types.VetoHolder;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IComponentNode;
import org.jowidgets.workbench.api.IComponentNodeContext;

public class ComponentNodeContext implements IComponentNodeContext {

	private final ComponentNodeContext parentNodeContext;
	private final WorkbenchContext workbenchContext;
	private final WorkbenchApplicationContext applicationContext;
	private final TreeNodeObservable treeNodeObservable;

	private final IComponentNode componentNode;
	private final ITreeNode treeNode;
	private final IListModelListener listModelListener;
	private final IMenuModel popupMenuModel;
	private final Map<IComponentNode, ITreeNode> createdNodes;

	private ComponentContext componentContext;

	public ComponentNodeContext(
		final IComponentNode componentNode,
		final ITreeNode treeNode,
		final ComponentNodeContext parentTreeNodeContext,
		final WorkbenchApplicationContext workbenchApplicationContext,
		final WorkbenchContext workbenchContext) {

		this.treeNodeObservable = new TreeNodeObservable();
		this.parentNodeContext = parentTreeNodeContext;
		this.workbenchContext = workbenchContext;
		this.applicationContext = workbenchApplicationContext;
		this.createdNodes = new HashMap<IComponentNode, ITreeNode>();

		this.componentNode = componentNode;
		this.treeNode = treeNode;
		this.treeNode.setText(componentNode.getLabel());
		this.treeNode.setToolTipText(componentNode.getTooltip());
		if (componentNode.getIcon() != null) {
			this.treeNode.setIcon(componentNode.getIcon());
		}

		this.treeNode.addTreeNodeListener(new ITreeNodeListener() {
			@Override
			public void selectionChanged(final boolean selected) {
				treeNodeObservable.fireSelectionChanged(selected);
			}

			@Override
			public void expandedChanged(final boolean expanded) {
				treeNodeObservable.fireExpandedChanged(expanded);
			}
		});

		this.listModelListener = new IListModelListener() {

			@Override
			public void childRemoved(final int index) {
				if (popupMenuModel.getChildren().size() == 0) {
					treeNode.setPopupMenu(null);
				}
			}

			@Override
			public void childAdded(final int index) {
				if (popupMenuModel.getChildren().size() == 1) {
					treeNode.setPopupMenu(popupMenuModel);
				}
			}
		};

		this.popupMenuModel = new MenuModel();
		this.popupMenuModel.addListModelListener(listModelListener);
		if (popupMenuModel.getChildren().size() > 0) {
			treeNode.setPopupMenu(popupMenuModel);
		}

		componentNode.onContextInitialize(this);
	}

	public void activate() {
		getComponentContextLazy().activate();
	}

	public VetoHolder deactivate() {
		return getComponentContextLazy().deactivate();
	}

	@Override
	public void add(final IComponentNode componentNode) {
		add(treeNode.getChildren().size(), componentNode);
	}

	@Override
	public void add(final int index, final IComponentNode componentNode) {
		Assert.paramNotNull(componentNode, "componentNode");
		final ITreeNode node = treeNode.addNode(index);
		createdNodes.put(componentNode, node);
		final ComponentNodeContext nodeContext = new ComponentNodeContext(
			componentNode,
			node,
			this,
			applicationContext,
			workbenchContext);
		applicationContext.registerNodeContext(nodeContext);
	}

	@Override
	public void remove(final IComponentNode componentNode) {
		Assert.paramNotNull(componentNode, "componentNode");
		final ITreeNode node = createdNodes.remove(componentNode);
		if (node != null) {
			node.setSelected(false);
			treeNode.removeNode(node);
			applicationContext.unRegisterNodeContext(node);
		}
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
	public IComponentNodeContext getParent() {
		return parentNodeContext;
	}

	@Override
	public IMenuModel getPopupMenu() {
		return popupMenuModel;
	}

	@Override
	public WorkbenchApplicationContext getWorkbenchApplicationContext() {
		return applicationContext;
	}

	@Override
	public WorkbenchContext getWorkbenchContext() {
		return applicationContext.getWorkbenchContext();
	}

	@Override
	public void addTreeNodeListener(final ITreeNodeListener listener) {
		treeNodeObservable.addTreeNodeListener(listener);
	}

	@Override
	public void removeTreeNodeListener(final ITreeNodeListener listener) {
		treeNodeObservable.removeTreeNodeListener(listener);
	}

	protected ITreeNode getTreeNode() {
		return treeNode;
	}

	private ComponentContext getComponentContextLazy() {
		if (componentContext == null) {
			componentContext = new ComponentContext(componentNode, ComponentNodeContext.this);
		}
		return componentContext;
	}

}
