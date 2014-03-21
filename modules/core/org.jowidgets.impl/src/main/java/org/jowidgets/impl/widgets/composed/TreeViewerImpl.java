/*
 * Copyright (c) 2014, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.impl.widgets.composed;

import org.jowidgets.api.model.tree.ITreeNodeModel;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.ITreeViewer;
import org.jowidgets.api.widgets.descriptor.setup.ITreeViewerSetup;
import org.jowidgets.tools.widgets.wrapper.TreeWrapper;
import org.jowidgets.util.Assert;

public final class TreeViewerImpl<ROOT_NODE_VALUE_TYPE> extends TreeWrapper implements ITreeViewer<ROOT_NODE_VALUE_TYPE> {

	private final ITreeNodeModel<ROOT_NODE_VALUE_TYPE> rootNode;

	public TreeViewerImpl(final ITree tree, final ITreeViewerSetup<ROOT_NODE_VALUE_TYPE> setup) {
		super(tree);
		Assert.paramNotNull(setup.getRootNodeModel(), "setup.getRootNodeModel()");
		this.rootNode = setup.getRootNodeModel();

		//TODO must be implemented
		for (int i = 0; i < 10; i++) {
			final ITreeNode node = tree.addNode();
			node.setText("TODO, implement TreeViewer NODE: " + i);
			for (int j = 0; j < 10; j++) {
				final ITreeNode subNode = node.addNode();
				subNode.setText("SUB NODE: " + j);
			}
		}
	}

	@Override
	public ITreeNodeModel<ROOT_NODE_VALUE_TYPE> getRootNodeModel() {
		return rootNode;
	}

}
