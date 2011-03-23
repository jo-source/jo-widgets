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

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.api.IComponentContext;
import org.jowidgets.workbench.api.IComponentTreeNode;
import org.jowidgets.workbench.api.IComponentTreeNodeContext;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;

class ComponentNode implements IComponentTreeNode {

	private final IComponentNodeModel model;

	ComponentNode(final IComponentNodeModel model) {
		super();
		this.model = model;
	}

	@Override
	public void onContextInitialize(final IComponentTreeNodeContext context) {
		// TODO MG implement onContextInitialize
	}

	@Override
	public String getId() {
		return model.getId();
	}

	@Override
	public String getLabel() {
		return model.getLabel();
	}

	@Override
	public String getTooltip() {
		return model.getTooltip();
	}

	@Override
	public IImageConstant getIcon() {
		return model.getIcon();
	}

	@Override
	public IComponent createComponent(final IComponentContext context) {
		if (model.getComponentFactory() != null) {
			return model.getComponentFactory().createComponent(model, context);
		}
		return null;
	}

}
