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
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.controler.ITreeNodeListener;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.api.IComponentContext;
import org.jowidgets.workbench.api.IComponentTreeNode;
import org.jowidgets.workbench.api.IComponentTreeNodeContext;
import org.jowidgets.workbench.api.ILayout;
import org.jowidgets.workbench.api.IWorkbenchApplicationContext;
import org.jowidgets.workbench.api.IWorkbenchContext;

public class ComponentTreeNodeContext implements IComponentTreeNodeContext, IComponentContext {

	private final ComponentTreeNodeContext parentTreeNodeContext;
	private final WorkbenchContext workbenchContext;
	private final WorkbenchApplicationContext workbenchApplicationContext;
	private final ITreeNode treeNode;
	private final IListModelListener listModelListener;
	private final IMenuModel popupMenuModel;
	private final Map<IComponentTreeNode, ITreeNode> createdNodes;

	private org.jowidgets.api.widgets.IComponent content;
	private IComponent component;

	public ComponentTreeNodeContext(
		final IComponentTreeNode componentTreeNode,
		final ITreeNode treeNode,
		final ComponentTreeNodeContext parentTreeNodeContext,
		final WorkbenchApplicationContext workbenchApplicationContext,
		final WorkbenchContext workbenchContext) {

		this.parentTreeNodeContext = parentTreeNodeContext;
		this.workbenchContext = workbenchContext;
		this.workbenchApplicationContext = workbenchApplicationContext;
		this.createdNodes = new HashMap<IComponentTreeNode, ITreeNode>();

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

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		this.treeNode.addTreeNodeListener(new ITreeNodeListener() {

			@Override
			public void selectionChanged(final boolean selected) {
				final IContainer wbContent = workbenchApplicationContext.getContentContainer();
				if (selected) {
					wbContent.layoutBegin();
					if (component == null) {
						component = componentTreeNode.createComponent(ComponentTreeNodeContext.this);
						if (component != null) {
							content = wbContent.add(bpf.textLabel("TODO: " + componentTreeNode.getLabel()), "growx, hidemode 3");
							workbenchContext.setEmptyContentVisible(false);
						}
						else {
							workbenchContext.setEmptyContentVisible(true);
						}
					}
					else {
						workbenchContext.setEmptyContentVisible(false);
						content.setVisible(true);
					}
					wbContent.layoutEnd();

				}
				else {
					if (content != null) {
						wbContent.layoutBegin();
						content.setVisible(false);
						workbenchContext.setEmptyContentVisible(true);
						wbContent.layoutEnd();
					}
				}
			}

			@Override
			public void expandedChanged(final boolean expanded) {}
		});

		componentTreeNode.onContextInitialize(this);
	}

	@Override
	public void setLayout(final ILayout layout) {
		// TODO MG implement setLayout
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
		new ComponentTreeNodeContext(componentTreeNode, node, this, workbenchApplicationContext, workbenchContext);
	}

	@Override
	public void remove(final IComponentTreeNode componentTreeNode) {
		Assert.paramNotNull(componentTreeNode, "componentTreeNode");
		final ITreeNode node = createdNodes.get(componentTreeNode);
		if (node != null) {
			node.setSelected(false);
			treeNode.removeNode(node);
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
	public IWorkbenchApplicationContext getWorkbenchApplicationContext() {
		return workbenchApplicationContext;
	}

	@Override
	public IMenuModel getPopupMenu() {
		return popupMenuModel;
	}

	@Override
	public IWorkbenchContext getWorkbenchContext() {
		return workbenchApplicationContext.getWorkbenchContext();
	}

	@Override
	public IComponentTreeNodeContext getComponentTreeNodeContext() {
		return this;
	}

}
