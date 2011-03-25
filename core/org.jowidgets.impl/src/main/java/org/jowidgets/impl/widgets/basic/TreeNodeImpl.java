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

package org.jowidgets.impl.widgets.basic;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeContainer;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.descriptor.ITreeNodeDescriptor;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.impl.base.delegate.TreeContainerDelegate;
import org.jowidgets.impl.event.TreePopupEvent;
import org.jowidgets.impl.widgets.common.wrapper.TreeNodeSpiWrapper;
import org.jowidgets.spi.widgets.ITreeNodeSpi;
import org.jowidgets.tools.controler.TreeNodeAdapter;
import org.jowidgets.util.EmptyCheck;

public class TreeNodeImpl extends TreeNodeSpiWrapper implements ITreeNode {

	private final TreeImpl parentTree;
	private final TreeNodeImpl parentNode;
	private final TreeContainerDelegate treeContainerDelegate;

	private final IPopupDetectionListener popupListener;
	private IMenuModel popupMenuModel;
	private IPopupMenu popupMenu;

	public TreeNodeImpl(final TreeImpl parentTree, final TreeNodeImpl parentNode, final ITreeNodeSpi widget) {
		this(parentTree, parentNode, widget, Toolkit.getBluePrintFactory().treeNode());
	}

	public TreeNodeImpl(
		final TreeImpl parentTree,
		final TreeNodeImpl parentNode,
		final ITreeNodeSpi widget,
		final ITreeNodeDescriptor descriptor) {
		super(widget);

		this.popupListener = new IPopupDetectionListener() {

			@Override
			public void popupDetected(final Position position) {
				if (popupMenuModel != null) {
					if (popupMenu == null) {
						popupMenu = new PopupMenuImpl(getWidget().createPopupMenu(), parentTree);
					}
					if (popupMenu.getModel() != popupMenuModel) {
						popupMenu.setModel(popupMenuModel);
					}
					popupMenu.show(position);
				}
			}
		};

		this.parentTree = parentTree;
		this.parentNode = parentNode;

		if (descriptor.getForegroundColor() != null) {
			setForegroundColor(descriptor.getForegroundColor());
		}
		if (descriptor.getBackgroundColor() != null) {
			setBackgroundColor(descriptor.getBackgroundColor());
		}

		setExpanded(descriptor.isExpanded());
		setSelected(descriptor.isSelected());
		setText(descriptor.getText());
		setToolTipText(descriptor.getToolTipText());
		setIcon(descriptor.getIcon());
		setMarkup(descriptor.getMarkup());

		addTreeNodeListener(new TreeNodeAdapter() {
			@Override
			public void expandedChanged(final boolean expanded) {
				if (expanded) {
					parentTree.getTreeObservable().fireNodeExpanded(TreeNodeImpl.this);
				}
				else {
					parentTree.getTreeObservable().fireNodeCollapsed(TreeNodeImpl.this);
				}
			}
		});

		addPopupDetectionListener(new IPopupDetectionListener() {
			@Override
			public void popupDetected(final Position position) {
				parentTree.getTreePopupDetectionObservable().firePopupDetected(new TreePopupEvent(position, TreeNodeImpl.this));
			}
		});

		this.treeContainerDelegate = new TreeContainerDelegate(parentTree, parentNode, this, widget);

		checkIcon();
	}

	@Override
	public ITreeNode getParent() {
		return parentNode;
	}

	@Override
	public ITree getTree() {
		return parentTree;
	}

	@Override
	public List<ITreeNode> getPath() {
		final LinkedList<ITreeNode> result = new LinkedList<ITreeNode>();
		ITreeNode node = this;
		while (node != null) {
			result.push(node);
			node = node.getParent();
		}
		return result;
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return new PopupMenuImpl(getWidget().createPopupMenu(), parentTree);
	}

	@Override
	public ITreeContainer getParentContainer() {
		return treeContainerDelegate.getParentContainer();
	}

	@Override
	public ITreeNode addNode() {
		final ITreeNode result = treeContainerDelegate.addNode();
		checkIcon();
		return result;
	}

	@Override
	public ITreeNode addNode(final int index) {
		final ITreeNode result = treeContainerDelegate.addNode(index);
		checkIcon();
		return result;
	}

	@Override
	public ITreeNode addNode(final ITreeNodeDescriptor descriptor) {
		final ITreeNode result = treeContainerDelegate.addNode(descriptor);
		checkIcon();
		return result;
	}

	@Override
	public ITreeNode addNode(final int index, final ITreeNodeDescriptor descriptor) {
		final ITreeNode result = treeContainerDelegate.addNode(index, descriptor);
		checkIcon();
		return result;
	}

	@Override
	public void removeNode(final ITreeNode node) {
		treeContainerDelegate.removeNode(node);
	}

	@Override
	public void removeNode(final int index) {
		treeContainerDelegate.removeNode(index);
	}

	@Override
	public void removeAllNodes() {
		treeContainerDelegate.removeAllNodes();
	}

	@Override
	public List<ITreeNode> getChildren() {
		return treeContainerDelegate.getChildren();
	}

	@Override
	public void setAllChildrenExpanded(final boolean expanded) {
		setExpanded(expanded);
		treeContainerDelegate.setAllChildrenExpanded(expanded);
	}

	@Override
	public boolean isLeaf() {
		return getChildren().size() == 0;
	}

	@Override
	public boolean isTopLevel() {
		return getParent() == null;
	}

	@Override
	public void setPopupMenu(final IMenuModel popupMenuModel) {
		if (popupMenuModel == null && this.popupMenuModel != null) {
			removePopupDetectionListener(popupListener);
		}
		else if (popupMenuModel != null && this.popupMenuModel == null) {
			addPopupDetectionListener(popupListener);
		}
		this.popupMenuModel = popupMenuModel;
	}

	private void checkIcon() {
		if (getIcon() == null) {
			if (isLeaf()) {
				getWidget().setIcon(parentTree.getDefaultLeafIcon());
			}
			else {
				getWidget().setIcon(parentTree.getDefaultInnerIcon());
			}
		}
	}

	@Override
	public String toString() {
		final String text = getText();
		if (!EmptyCheck.isEmpty(text)) {
			return text;
		}
		else {
			return super.toString();
		}
	}

}
