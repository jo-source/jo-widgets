/*
 * Copyright (c) 2011, M. Grossmann, M. Woelker, H. Westphal
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

package org.jowidgets.workbench.legacy.impl.rcp.internal;

import org.eclipse.swt.widgets.ToolItem;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.workbench.legacy.api.IComponentContext;
import org.jowidgets.workbench.legacy.api.IViewContext;

public final class ViewContext implements IViewContext {

	private IContainer container;
	private IMenu menu;
	private ToolItem menuToolItem;
	private IToolBar toolBar;

	public void setContainer(final IContainer container) {
		this.container = container;
	}

	public void setMenu(final IMenu menu) {
		this.menu = menu;
	}

	public void setMenuToolItem(final ToolItem menuToolItem) {
		this.menuToolItem = menuToolItem;
	}

	public void setToolBar(final IToolBar toolBar) {
		this.toolBar = toolBar;
	}

	@Override
	public IComponentContext getComponentContext() {
		return null;
	}

	@Override
	public IContainer getContainer() {
		return container;
	}

	@Override
	public IMenu getMenu() {
		return menu;
	}

	@Override
	public void setMenuTooltip(final String tooltip) {
		if (menuToolItem != null && !menuToolItem.isDisposed()) {
			menuToolItem.setToolTipText(tooltip);
		}
	}

	@Override
	public IToolBar getToolBar() {
		return toolBar;
	}

}
