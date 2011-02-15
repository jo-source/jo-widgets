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

package org.jowidgets.workbench.impl.rcp.internal;

import org.eclipse.swt.widgets.ToolItem;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.workbench.api.IComponentContext;
import org.jowidgets.workbench.api.IViewContext;

public final class ViewContext implements IViewContext {

	private final IContainer container;
	private final IMenu menu;
	private final ToolItem menuItem;

	public ViewContext(final IContainer container) {
		this(container, null, null);
	}

	public ViewContext(final IContainer container, final IMenu menu, final ToolItem menuItem) {
		this.container = container;
		this.menu = menu;
		this.menuItem = menuItem;
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
		if (menuItem != null && !menuItem.isDisposed()) {
			menuItem.setToolTipText(tooltip);
		}
	}

	@Override
	public IToolBar getToolBar() {
		return null;
	}

}
