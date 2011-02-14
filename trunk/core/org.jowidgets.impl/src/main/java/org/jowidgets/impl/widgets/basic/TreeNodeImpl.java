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

import java.util.List;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeContainer;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.descriptor.ITreeNodeDescriptor;
import org.jowidgets.impl.base.delegate.TreeContainerDelegate;
import org.jowidgets.impl.widgets.common.wrapper.TreeNodeSpiWrapper;
import org.jowidgets.spi.widgets.ITreeNodeSpi;
import org.jowidgets.util.EmptyCheck;

public class TreeNodeImpl extends TreeNodeSpiWrapper implements ITreeNode {

	private final TreeImpl parentTree;
	private final TreeNodeImpl parentNode;
	private final TreeContainerDelegate treeContainerDelegate;

	public TreeNodeImpl(final TreeImpl parentTree, final TreeNodeImpl parentNode, final ITreeNodeSpi widget) {
		this(parentTree, parentNode, widget, Toolkit.getBluePrintFactory().treeNode());
	}

	public TreeNodeImpl(
		final TreeImpl parentTree,
		final TreeNodeImpl parentNode,
		final ITreeNodeSpi widget,
		final ITreeNodeDescriptor descriptor) {
		super(widget);

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

		this.treeContainerDelegate = new TreeContainerDelegate(parentTree, parentNode, this, widget);
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
	public IPopupMenu createPopupMenu() {
		return new PopupMenuImpl(getWidget().createPopupMenu(), parentTree);
	}

	@Override
	public ITreeContainer getParentContainer() {
		return treeContainerDelegate.getParentContainer();
	}

	@Override
	public ITreeNode addNode() {
		return treeContainerDelegate.addNode();
	}

	@Override
	public ITreeNode addNode(final int index) {
		return treeContainerDelegate.addNode(index);
	}

	@Override
	public ITreeNode addNode(final ITreeNodeDescriptor descriptor) {
		return treeContainerDelegate.addNode(descriptor);
	}

	@Override
	public ITreeNode addNode(final int index, final ITreeNodeDescriptor descriptor) {
		return treeContainerDelegate.addNode(index, descriptor);
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
