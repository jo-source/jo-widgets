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

package org.jowidgets.spi.impl.swing.widgets;

import java.util.HashSet;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.spi.impl.controler.TreeNodeObservable;
import org.jowidgets.spi.impl.swing.widgets.base.JoTreeNode;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.ITreeNodeSpi;
import org.jowidgets.util.Assert;

public class TreeNodeImpl extends TreeNodeObservable implements ITreeNodeSpi {

	private final Set<IPopupDetectionListener> popupDetectionListeners;

	private final TreeImpl parentTree;
	private final DefaultMutableTreeNode node;

	public TreeNodeImpl(final TreeImpl parentTree, final DefaultMutableTreeNode node) {
		super();
		Assert.paramNotNull(parentTree, "parentTree");

		this.popupDetectionListeners = new HashSet<IPopupDetectionListener>();

		this.parentTree = parentTree;
		this.node = node;
	}

	@Override
	public DefaultMutableTreeNode getUiReference() {
		return node;
	}

	private JoTreeNode getJoTreeNode() {
		if (node instanceof JoTreeNode) {
			return (JoTreeNode) node;
		}
		else {
			throw new UnsupportedOperationException("This Operation is not supported for the root node");
		}
	}

	@Override
	public void setEnabled(final boolean enabled) {
		if (!enabled) {
			throw new UnsupportedOperationException("Tree items can not be disabled");
		}
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void setText(final String text) {
		getJoTreeNode().setText(text);
	}

	@Override
	public void setToolTipText(final String toolTipText) {
		getJoTreeNode().setToolTipText(toolTipText);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		getJoTreeNode().setIcon(icon);
	}

	@Override
	public void setMarkup(final Markup markup) {
		getJoTreeNode().setMarkup(markup);
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		getJoTreeNode().setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		getJoTreeNode().setBackgroundColor(colorValue);
	}

	@Override
	public void setExpanded(final boolean expanded) {
		if (expanded) {
			parentTree.getTree().expandPath(new TreePath(node.getPath()));
		}
		else {
			parentTree.getTree().collapsePath(new TreePath(node.getPath()));
		}
	}

	@Override
	public boolean isExpanded() {
		return parentTree.getTree().isExpanded(new TreePath(node.getPath()));
	}

	@Override
	public void setSelected(final boolean selected) {
		final TreePath treePath = new TreePath(node.getPath());
		if (selected) {
			parentTree.getTree().addSelectionPath(treePath);
		}
		else {
			parentTree.getTree().removeSelectionPath(treePath);
		}
	}

	@Override
	public boolean isSelected() {
		final TreePath thisPath = new TreePath(node.getPath());
		final TreePath[] selectedPaths = parentTree.getTree().getSelectionPaths();
		if (selectedPaths != null) {
			for (final TreePath selectedPath : selectedPaths) {
				if (selectedPath.isDescendant(thisPath)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		popupDetectionListeners.add(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		popupDetectionListeners.remove(listener);
	}

	protected void firePopupDetected(final Position position) {
		for (final IPopupDetectionListener listener : popupDetectionListeners) {
			listener.popupDetected(position);
		}
	}

	@Override
	public ITreeNodeSpi addNode(final Integer index) {
		final JoTreeNode joTreeNode = new JoTreeNode();

		if (index != null) {
			parentTree.getTreeModel().insertNodeInto(joTreeNode, node, index.intValue());
		}
		else {
			parentTree.getTreeModel().insertNodeInto(joTreeNode, node, node.getChildCount());
		}

		//expand the path of the root node to ensure that the child is visible (because root is not)
		//do not remove this (Swing trees are very strange !!!:()
		if (parentTree.getTree().getModel().getRoot() == node) {
			parentTree.getTree().expandPath(new TreePath(node));
		}

		final TreeNodeImpl result = new TreeNodeImpl(parentTree, joTreeNode);
		parentTree.registerNode(joTreeNode, result);
		return result;
	}

	@Override
	public void removeNode(final int index) {
		final TreeNode child = node.getChildAt(index);
		parentTree.getTreeModel().removeNodeFromParent((JoTreeNode) child);
		if (child != null) {
			parentTree.unRegisterNode((JoTreeNode) child);
		}
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return new PopupMenuImpl(parentTree.getUiReference());
	}

}
