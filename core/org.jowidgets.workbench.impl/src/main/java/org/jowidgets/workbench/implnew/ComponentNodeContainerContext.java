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

package org.jowidgets.workbench.implnew;

import java.util.HashMap;
import java.util.Map;

import org.jowidgets.api.widgets.ITreeContainer;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IComponentNode;
import org.jowidgets.workbench.api.IComponentNodeContainerContext;

public class ComponentNodeContainerContext implements IComponentNodeContainerContext {

	private final ITreeContainer treeContainer;
	private final WorkbenchContext workbenchContext;
	private final WorkbenchApplicationContext applicationContext;

	private final Map<IComponentNode, ITreeNode> createdNodes;

	public ComponentNodeContainerContext(
		final ITreeContainer treeContainer,
		final WorkbenchApplicationContext applicationContext,
		final WorkbenchContext workbenchContext) {

		this.treeContainer = treeContainer;
		this.workbenchContext = workbenchContext;
		this.applicationContext = applicationContext;
		this.createdNodes = new HashMap<IComponentNode, ITreeNode>();
	}

	@Override
	public void add(final IComponentNode componentNode) {
		add(treeContainer.getChildren().size(), componentNode);
	}

	@Override
	public void add(final int index, final IComponentNode componentNode) {
		Assert.paramNotNull(componentNode, "componentNode");
		final ITreeNode node = treeContainer.addNode(index);
		createdNodes.put(componentNode, node);
		final ComponentNodeContext nodeContext = new ComponentNodeContext(
			componentNode,
			node,
			(this instanceof ComponentNodeContext) ? (ComponentNodeContext) this : null,
			applicationContext,
			workbenchContext);
		applicationContext.registerNodeContext(nodeContext);
		componentNode.onContextInitialize(nodeContext);
	}

	@Override
	public void remove(final IComponentNode componentNode) {
		Assert.paramNotNull(componentNode, "componentNode");
		final ITreeNode node = createdNodes.remove(componentNode);
		if (node != null) {
			node.setSelected(false);
			treeContainer.removeNode(node);
			applicationContext.unRegisterNodeContext(node);
		}
	}

	@Override
	public WorkbenchContext getWorkbenchContext() {
		return applicationContext.getWorkbenchContext();
	}

	public WorkbenchApplicationContext getWorkbenchApplicationContext() {
		return applicationContext;
	}

}
