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

package org.jowidgets.workbench.impl.internal;

import java.util.HashMap;
import java.util.Map;

import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.types.VetoHolder;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IComponentTreeNode;
import org.jowidgets.workbench.api.IComponentTreeNodeContext;

public class ComponentTreeNodeContext implements IComponentTreeNodeContext {

	private final ComponentTreeNodeContext parentTreeNodeContext;
	private final WorkbenchContext workbenchContext;
	private final WorkbenchApplicationContext applicationContext;

	private final IComponentTreeNode componentTreeNode;
	private final ITreeNode treeNode;
	private final IListModelListener listModelListener;
	private final IMenuModel popupMenuModel;
	private final Map<IComponentTreeNode, ITreeNode> createdNodes;

	private ComponentContext componentContext;

	public ComponentTreeNodeContext(
		final IComponentTreeNode componentTreeNode,
		final ITreeNode treeNode,
		final ComponentTreeNodeContext parentTreeNodeContext,
		final WorkbenchApplicationContext workbenchApplicationContext,
		final WorkbenchContext workbenchContext) {

		this.parentTreeNodeContext = parentTreeNodeContext;
		this.workbenchContext = workbenchContext;
		this.applicationContext = workbenchApplicationContext;
		this.createdNodes = new HashMap<IComponentTreeNode, ITreeNode>();

		this.componentTreeNode = componentTreeNode;
		this.treeNode = treeNode;
		this.treeNode.setText(componentTreeNode.getLabel());
		this.treeNode.setToolTipText(componentTreeNode.getTooltip());
		if (componentTreeNode.getIcon() != null) {
			this.treeNode.setIcon(componentTreeNode.getIcon());
		}

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

		componentTreeNode.onContextInitialize(this);
	}

	public void activate() {
		getComponentContextLazy().activate();
	}

	public VetoHolder deactivate() {
		return getComponentContextLazy().deactivate();
	}

	@Override
	public void add(final IComponentTreeNode componentTreeNode) {
		add(treeNode.getChildren().size(), componentTreeNode);
	}

	@Override
	public void add(final int index, final IComponentTreeNode componentTreeNode) {
		Assert.paramNotNull(componentTreeNode, "componentTreeNode");
		final ITreeNode node = treeNode.addNode(index);
		createdNodes.put(componentTreeNode, node);
		final ComponentTreeNodeContext nodeContext = new ComponentTreeNodeContext(
			componentTreeNode,
			node,
			this,
			applicationContext,
			workbenchContext);
		applicationContext.registerNodeContext(nodeContext);
	}

	@Override
	public void remove(final IComponentTreeNode componentTreeNode) {
		Assert.paramNotNull(componentTreeNode, "componentTreeNode");
		final ITreeNode node = createdNodes.remove(componentTreeNode);
		if (node != null) {
			node.setSelected(false);
			treeNode.removeNode(node);
			applicationContext.unRegisterNodeContext(node);
		}
	}

	@Override
	public void select() {
		treeNode.setSelected(true);
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
	public IComponentTreeNodeContext getParent() {
		return parentTreeNodeContext;
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

	protected ITreeNode getTreeNode() {
		return treeNode;
	}

	private ComponentContext getComponentContextLazy() {
		if (componentContext == null) {
			componentContext = new ComponentContext(componentTreeNode, ComponentTreeNodeContext.this);
		}
		return componentContext;
	}

}
