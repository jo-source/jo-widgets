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

import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.workbench.api.IComponentTreeNode;
import org.jowidgets.workbench.api.IWorkbenchApplicationContext;
import org.jowidgets.workbench.api.IWorkbenchContext;

public final class WorkbenchApplicationContext implements IWorkbenchApplicationContext {

	private final WorkbenchApplicationTree composite;
	private final IWorkbenchContext workbenchContext;

	public WorkbenchApplicationContext(final WorkbenchApplicationTree composite, final IWorkbenchContext workbenchContext) {
		this.composite = composite;
		this.workbenchContext = workbenchContext;
	}

	@Override
	public void add(final IComponentTreeNode componentTreeNode) {
		// TODO implement
	}

	@Override
	public void add(final int index, final IComponentTreeNode componentTreeNode) {
		// TODO implement
	}

	@Override
	public void remove(final IComponentTreeNode componentTreeNode) {
		// TODO implement
	}

	@Override
	public IWorkbenchContext getWorkbenchContext() {
		return workbenchContext;
	}

	@Override
	public IMenu getMenu() {
		return composite.getJoMenu();
	}

	@Override
	public void setMenuTooltip(final String tooltip) {
		composite.setMenuTooltip(tooltip);
	}

	@Override
	public IToolBar getToolBar() {
		return composite.getToolBar();
	}

}
