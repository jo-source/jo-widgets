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

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.toolkit.api.IComponentNodeContainerModelBuilder;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModelBuilder;
import org.jowidgets.workbench.toolkit.api.WorkbenchToolkit;

class ComponentNodeContainerModelBuilder<BUILDER_INSTANCE_TYPE> implements
		IComponentNodeContainerModelBuilder<BUILDER_INSTANCE_TYPE> {

	private final List<IComponentNodeModel> children;
	private String id;

	ComponentNodeContainerModelBuilder() {
		this.children = new LinkedList<IComponentNodeModel>();
	}

	String getId() {
		return id;
	}

	List<IComponentNodeModel> getChildren() {
		return children;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER_INSTANCE_TYPE setId(final String id) {
		this.id = id;
		return (BUILDER_INSTANCE_TYPE) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER_INSTANCE_TYPE addChild(final int index, final IComponentNodeModel childModel) {
		Assert.paramNotNull(childModel, "childModel");
		children.add(index, childModel);
		return (BUILDER_INSTANCE_TYPE) this;
	}

	@Override
	public BUILDER_INSTANCE_TYPE addChild(final IComponentNodeModel childModel) {
		return addChild(children.size(), childModel);
	}

	@Override
	public BUILDER_INSTANCE_TYPE addChild(final IComponentNodeModelBuilder childModelBuilder) {
		return addChild(children.size(), childModelBuilder);
	}

	@Override
	public BUILDER_INSTANCE_TYPE addChild(final int index, final IComponentNodeModelBuilder childModelBuilder) {
		Assert.paramNotNull(childModelBuilder, "childModelBuilder");
		return addChild(index, childModelBuilder.build());
	}

	@Override
	public BUILDER_INSTANCE_TYPE addChild(final String id, final String label, final String tooltip, final IImageConstant icon) {
		return addChild(componentNodeBuilder().setId(id).setLabel(label).setTooltip(tooltip).setIcon(icon));
	}

	@Override
	public BUILDER_INSTANCE_TYPE addChild(final String id, final String label, final IImageConstant icon) {
		return addChild(componentNodeBuilder().setId(id).setLabel(label).setIcon(icon));
	}

	@Override
	public BUILDER_INSTANCE_TYPE addChild(final String id, final String label, final String tooltip) {
		return addChild(componentNodeBuilder().setId(id).setLabel(label).setTooltip(tooltip));
	}

	@Override
	public BUILDER_INSTANCE_TYPE addChild(final String id, final String label) {
		return addChild(componentNodeBuilder().setId(id).setLabel(label));
	}

	@Override
	public BUILDER_INSTANCE_TYPE addChild(final String id) {
		return addChild(componentNodeBuilder().setId(id));
	}

	private IComponentNodeModelBuilder componentNodeBuilder() {
		return WorkbenchToolkit.getWorkbenchPartBuilderFactory().componentNode();
	}

}
