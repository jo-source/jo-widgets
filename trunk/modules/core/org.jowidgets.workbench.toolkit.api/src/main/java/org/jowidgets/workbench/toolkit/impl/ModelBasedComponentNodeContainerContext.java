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

package org.jowidgets.workbench.toolkit.impl;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.api.IComponentContext;
import org.jowidgets.workbench.api.IComponentNode;
import org.jowidgets.workbench.api.IComponentNodeContext;
import org.jowidgets.workbench.toolkit.api.IComponentFactory;
import org.jowidgets.workbench.toolkit.api.IComponentNodeContainerModel;
import org.jowidgets.workbench.toolkit.api.IComponentNodeInitializeCallback;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;

class ModelBasedComponentNodeContainerContext {

	private final IComponentNodeContainerModel nodeModel;
	private final List<IComponentNode> addedComponentNodes;

	ModelBasedComponentNodeContainerContext(final IComponentNodeContainerModel nodeModel) {
		Assert.paramNotNull(nodeModel, "nodeModel");
		this.nodeModel = nodeModel;
		this.addedComponentNodes = new LinkedList<IComponentNode>();
	}

	public final void add(final int index, final IComponentNode componentNode) {
		Assert.paramNotNull(componentNode, "componentNode");

		final ComponentNodeModelBuilder modelBuilder = new ComponentNodeModelBuilder();
		modelBuilder.setId(componentNode.getId());
		modelBuilder.setLabel(componentNode.getLabel());
		modelBuilder.setTooltip(componentNode.getTooltip());
		modelBuilder.setIcon(componentNode.getIcon());
		modelBuilder.setComponentFactory(new IComponentFactory() {
			@Override
			public IComponent createComponent(final IComponentNodeModel treeNodeModel, final IComponentContext context) {
				return componentNode.createComponent(context);
			}
		});
		modelBuilder.setInitializeCallback(new IComponentNodeInitializeCallback() {
			@Override
			public void onContextInitialize(final IComponentNodeContext context) {
				componentNode.onContextInitialize(context);
			}
		});

		addedComponentNodes.add(index, componentNode);
		nodeModel.addChild(index, modelBuilder);
	}

	public final void add(final IComponentNode componentNode) {
		add(addedComponentNodes.size(), componentNode);
	}

	public final void remove(final IComponentNode componentNode) {
		final int index = addedComponentNodes.indexOf(componentNode);
		if (index != -1) {
			nodeModel.remove(index);
		}
	}

}
