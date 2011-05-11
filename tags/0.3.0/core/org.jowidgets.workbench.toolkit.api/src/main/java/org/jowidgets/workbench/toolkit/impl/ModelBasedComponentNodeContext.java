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

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.controler.ITreeNodeListener;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IComponentNodeContext;
import org.jowidgets.workbench.api.IWorkbenchApplicationContext;
import org.jowidgets.workbench.api.IWorkbenchContext;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;

class ModelBasedComponentNodeContext extends ModelBasedComponentNodeContainerContext implements IComponentNodeContext {

	private final IComponentNodeContext nodeContext;
	private final IComponentNodeModel nodeModel;

	private IComponentNodeContext parentNodeContext;
	private IWorkbenchApplicationContext workbenchApplicationContext;

	ModelBasedComponentNodeContext(final IComponentNodeContext nodeContext, final IComponentNodeModel nodeModel) {
		super(nodeModel);
		Assert.paramNotNull(nodeContext, "nodeContext");
		Assert.paramNotNull(nodeModel, "nodeModel");
		this.nodeContext = nodeContext;
		this.nodeModel = nodeModel;
	}

	@Override
	public void setSelected(final boolean selected) {
		nodeModel.setSelected(selected);
	}

	@Override
	public void setExpanded(final boolean expanded) {
		nodeModel.setExpanded(true);
	}

	@Override
	public void setLabel(final String label) {
		nodeModel.setLabel(label);
	}

	@Override
	public void setTooltip(final String tooltip) {
		nodeModel.setTooltip(tooltip);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		nodeModel.setIcon(icon);
	}

	@Override
	public IMenuModel getPopupMenu() {
		return nodeModel.getPopupMenu();
	}

	@Override
	public IComponentNodeContext getParent() {
		if (nodeContext.getParent() != null) {
			if (parentNodeContext == null) {
				parentNodeContext = new ModelBasedComponentNodeContext(nodeContext.getParent(), nodeModel.getParent());
			}
		}
		return parentNodeContext;
	}

	@Override
	public IWorkbenchApplicationContext getWorkbenchApplicationContext() {
		if (workbenchApplicationContext == null) {
			final IWorkbenchApplicationModel applicationModel = nodeModel.getApplication();
			final IWorkbenchApplicationContext applicationContext = nodeContext.getWorkbenchApplicationContext();
			workbenchApplicationContext = new ModelBasedWorkbenchApplicationContext(applicationContext, applicationModel);
		}
		return workbenchApplicationContext;
	}

	@Override
	public IWorkbenchContext getWorkbenchContext() {
		return getWorkbenchApplicationContext().getWorkbenchContext();
	}

	@Override
	public void addTreeNodeListener(final ITreeNodeListener listener) {
		nodeContext.addTreeNodeListener(listener);
	}

	@Override
	public void removeTreeNodeListener(final ITreeNodeListener listener) {
		nodeContext.removeTreeNodeListener(listener);
	}

}
