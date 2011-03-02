/*
 * Copyright (c) 2011, M. Woelker, H. Westphal
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of jo-widgets.org nor the
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

package org.jowidgets.workbench.impl.rcp.internal;

import org.eclipse.swt.widgets.Composite;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.workbench.api.IComponentContext;
import org.jowidgets.workbench.api.IComponentTreeNodeContext;
import org.jowidgets.workbench.api.IViewContext;
import org.jowidgets.workbench.api.IWorkbenchApplicationContext;
import org.jowidgets.workbench.api.IWorkbenchContext;
import org.jowidgets.workbench.impl.rcp.internal.part.PartSupport;
import org.jowidgets.workbench.impl.rcp.internal.util.ToolBarHelper;

public final class ViewContext implements IViewContext {

	private final String primaryViewId;
	private final Composite parent;
	private final IComponentContext componentContext;
	private final ToolBarHelper toolBarHelper;
	private boolean hidden;

	public ViewContext(final String primaryViewId, final Composite parent, final IComponentContext componentContext) {
		this.primaryViewId = primaryViewId;
		this.parent = parent;
		this.componentContext = componentContext;

		final IComposite composite = Toolkit.getWidgetWrapperFactory().createComposite(parent);
		composite.setLayout(new MigLayoutDescriptor("0[grow]0", "0[]0[grow]0"));

		toolBarHelper = new ToolBarHelper(composite);
	}

	public String getPrimaryViewId() {
		return primaryViewId;
	}

	public Composite getParent() {
		return parent;
	}

	@Override
	public IComponentContext getComponentContext() {
		return componentContext;
	}

	@Override
	public IContainer getContainer() {
		return toolBarHelper.getContent();
	}

	@Override
	public void activate() {
		if (!hidden) {
			// do not activate hidden views
			PartSupport.getInstance().activateView(this);
		}
	}

	@Override
	public void setHidden(final boolean newHidden) {
		if (hidden && !newHidden) {
			PartSupport.getInstance().unhideView(this);
			hidden = false;
		}
		else if (!hidden && newHidden) {
			PartSupport.getInstance().hideView(this);
			hidden = true;
		}
	}

	@Override
	public IToolBarModel getToolBar() {
		return toolBarHelper.getToolBarModel();
	}

	@Override
	public IMenuModel getToolBarMenu() {
		return toolBarHelper.getToolBarMenuModel();
	}

	@Override
	public IComponentTreeNodeContext getComponentTreeNodeContext() {
		return getComponentContext().getComponentTreeNodeContext();
	}

	@Override
	public IWorkbenchApplicationContext getWorkbenchApplicationContext() {
		return getComponentTreeNodeContext().getWorkbenchApplicationContext();
	}

	@Override
	public IWorkbenchContext getWorkbenchContext() {
		return getWorkbenchApplicationContext().getWorkbenchContext();
	}

	/**
	 * Sets a stale hidden state to false.
	 */
	public void unsetHidden() {
		hidden = false;
	}

}
