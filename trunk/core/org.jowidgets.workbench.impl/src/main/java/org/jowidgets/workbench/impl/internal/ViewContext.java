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

package org.jowidgets.workbench.impl.internal;

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.ITabFolder;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.workbench.api.IComponentContext;
import org.jowidgets.workbench.api.IComponentTreeNodeContext;
import org.jowidgets.workbench.api.IViewContext;
import org.jowidgets.workbench.api.IWorkbenchApplicationContext;
import org.jowidgets.workbench.api.IWorkbenchContext;

public class ViewContext implements IViewContext {

	private final ITabFolder tabFolder;
	private final ITabItem tabItem;
	private final ToolBarHelper toolBarHelper;;

	private final IComponentContext componentContext;

	public ViewContext(final ITabFolder tabFolder, final ITabItem tabItem, final IComponentContext componentContext) {
		super();
		this.tabFolder = tabFolder;
		this.tabItem = tabItem;
		this.componentContext = componentContext;

		this.toolBarHelper = new ToolBarHelper(tabItem);
	}

	@Override
	public void activate() {
		tabFolder.setSelectedItem(tabItem);
	}

	@Override
	public void setHidden(final boolean hidden) {
		// TODO MG implement hide / unhide
	}

	@Override
	public IContainer getContainer() {
		return toolBarHelper.getContent();
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
	public IComponentContext getComponentContext() {
		return componentContext;
	}

	@Override
	public IComponentTreeNodeContext getComponentTreeNodeContext() {
		return componentContext.getComponentTreeNodeContext();
	}

	@Override
	public IWorkbenchApplicationContext getWorkbenchApplicationContext() {
		return componentContext.getWorkbenchApplicationContext();
	}

	@Override
	public IWorkbenchContext getWorkbenchContext() {
		return componentContext.getWorkbenchContext();
	}

}
