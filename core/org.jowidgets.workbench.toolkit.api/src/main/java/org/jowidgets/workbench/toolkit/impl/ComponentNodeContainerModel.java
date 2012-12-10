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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.toolkit.api.IComponentNodeContainerModel;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModelBuilder;
import org.jowidgets.workbench.toolkit.api.WorkbenchToolkit;

class ComponentNodeContainerModel implements IComponentNodeContainerModel {

	private final ListModelObservable listModelObservable;
	private final List<IComponentNodeModel> children;
	private final String id;

	ComponentNodeContainerModel(final String id, final List<IComponentNodeModel> children) {
		Assert.paramNotEmpty(id, "id");
		this.listModelObservable = new ListModelObservable();
		this.children = new LinkedList<IComponentNodeModel>();
		this.id = id;

		//Add the children that way to ensure that the parents will be set correctly
		for (final IComponentNodeModel childModel : children) {
			addChild(childModel);
		}
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void addListModelListener(final IListModelListener listener) {
		listModelObservable.addListModelListener(listener);
	}

	@Override
	public void removeListModelListener(final IListModelListener listener) {
		listModelObservable.removeListModelListener(listener);
	}

	@Override
	public IComponentNodeContainerModel getParentContainer() {
		return null;
	}

	@Override
	public List<IComponentNodeModel> getChildren() {
		return Collections.unmodifiableList(children);
	}

	@Override
	public int getChildrenCount() {
		return children.size();
	}

	@Override
	public IComponentNodeModel addChild(final int index, final IComponentNodeModel childModel) {
		Assert.paramNotNull(childModel, "childModel");
		if (childModel.getParentContainer() != null) {
			throw new IllegalArgumentException("The given model was alreay added to another container. "
				+ "To add this model to this container, it must be removed from its current container first.");
		}
		children.add(index, childModel);
		childModel.setParentContainer(this);

		listModelObservable.fireAfterChildAdded(index);
		return childModel;
	}

	@Override
	public void remove(final int index) {
		if (children.size() < index) {
			listModelObservable.fireBeforeChildRemove(index);
		}
		final IComponentNodeModel child = children.remove(index);
		if (child != null) {
			child.setParentContainer(null);
			listModelObservable.fireAfterChildRemoved(index);
		}
	}

	@Override
	public IComponentNodeModel addChild(final IComponentNodeModel childModel) {
		return addChild(children.size(), childModel);
	}

	@Override
	public IComponentNodeModel addChild(final IComponentNodeModelBuilder childModelBuilder) {
		return addChild(children.size(), childModelBuilder);
	}

	@Override
	public IComponentNodeModel addChild(final int index, final IComponentNodeModelBuilder childModelBuilder) {
		Assert.paramNotNull(childModelBuilder, "childModelBuilder");
		return addChild(index, childModelBuilder.build());
	}

	@Override
	public IComponentNodeModel addChild(final String id, final String label, final String tooltip, final IImageConstant icon) {
		return addChild(componentNodeBuilder().setId(id).setLabel(label).setTooltip(tooltip).setIcon(icon));
	}

	@Override
	public IComponentNodeModel addChild(final String id, final String label, final IImageConstant icon) {
		return addChild(componentNodeBuilder().setId(id).setLabel(label).setIcon(icon));
	}

	@Override
	public IComponentNodeModel addChild(final String id, final String label, final String tooltip) {
		return addChild(componentNodeBuilder().setId(id).setLabel(label).setTooltip(tooltip));
	}

	@Override
	public IComponentNodeModel addChild(final String id, final String label) {
		return addChild(componentNodeBuilder().setId(id).setLabel(label));
	}

	@Override
	public IComponentNodeModel addChild(final String id) {
		return addChild(componentNodeBuilder().setId(id));
	}

	@Override
	public void remove(final IComponentNodeModel childModel) {
		final int index = children.indexOf(childModel);
		if (index != -1) {
			remove(index);
		}
	}

	@Override
	public void removeAll() {
		final int childrenCount = children.size();
		for (int i = 0; i < childrenCount; i++) {
			remove(0);
		}
	}

	private IComponentNodeModelBuilder componentNodeBuilder() {
		return WorkbenchToolkit.getWorkbenchPartBuilderFactory().componentNode();
	}

}
