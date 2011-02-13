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

package org.jowidgets.impl.base.delegate;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.widgets.ITreeContainer;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.descriptor.ITreeNodeDescriptor;
import org.jowidgets.impl.widgets.basic.TreeImpl;
import org.jowidgets.impl.widgets.basic.TreeNodeImpl;
import org.jowidgets.spi.widgets.ITreeNodeSpi;

public class TreeContainerDelegate implements ITreeContainer {

	private final List<ITreeNode> children;

	private final TreeImpl parentTree;
	private final TreeNodeImpl parentNode;
	private final TreeNodeImpl treeNode;
	private final ITreeNodeSpi treeNodeSpi;

	public TreeContainerDelegate(
		final TreeImpl parentTree,
		final TreeNodeImpl parentNode,
		final TreeNodeImpl treeNode,
		final ITreeNodeSpi treeNodeSpi) {
		super();

		this.children = new LinkedList<ITreeNode>();

		this.parentTree = parentTree;
		this.parentNode = parentNode;
		this.treeNode = treeNode;
		this.treeNodeSpi = treeNodeSpi;
	}

	@Override
	public ITreeContainer getParentContainer() {
		if (parentNode != null) {
			return parentNode;
		}
		else {
			return parentTree;
		}
	}

	@Override
	public ITreeNode addNode() {
		final ITreeNodeSpi childTreeNodeSpi = treeNodeSpi.addNode(null);
		final ITreeNode result = new TreeNodeImpl(parentTree, treeNode, childTreeNodeSpi);
		children.add(result);
		return result;
	}

	@Override
	public ITreeNode addNode(final int index) {
		final ITreeNodeSpi childTreeNodeSpi = treeNodeSpi.addNode(Integer.valueOf(index));
		final ITreeNode result = new TreeNodeImpl(parentTree, treeNode, childTreeNodeSpi);
		children.add(index, result);
		return result;
	}

	@Override
	public ITreeNode addNode(final ITreeNodeDescriptor descriptor) {
		final ITreeNodeSpi childTreeNodeSpi = treeNodeSpi.addNode(null);
		final ITreeNode result = new TreeNodeImpl(parentTree, treeNode, childTreeNodeSpi, descriptor);
		children.add(result);
		return result;
	}

	@Override
	public ITreeNode addNode(final int index, final ITreeNodeDescriptor descriptor) {
		final ITreeNodeSpi childTreeNodeSpi = treeNodeSpi.addNode(Integer.valueOf(index));
		final ITreeNode result = new TreeNodeImpl(parentTree, treeNode, childTreeNodeSpi, descriptor);
		children.add(index, result);
		return result;
	}

	@Override
	public void removeNode(final ITreeNode node) {
		final int index = children.indexOf(node);
		if (index != -1) {
			children.remove(index);
			treeNodeSpi.removeNode(index);
		}
	}

	@Override
	public void removeNode(final int index) {
		children.remove(index);
		treeNodeSpi.removeNode(index);
	}

	@Override
	public void removeAllNodes() {
		for (int i = 0; i < children.size(); i++) {
			treeNodeSpi.removeNode(i);
		}
		children.clear();
	}

	@Override
	public List<ITreeNode> getChildren() {
		return new LinkedList<ITreeNode>(children);
	}

}
