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

import java.util.concurrent.Callable;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IMenuBar;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.workbench.api.ITray;
import org.jowidgets.workbench.api.IWorkbench;
import org.jowidgets.workbench.api.IWorkbenchApplication;
import org.jowidgets.workbench.api.IWorkbenchContext;

public final class WorkbenchContext implements IWorkbenchContext {

	private final JoWorkbenchAdvisor workbenchAdvisor;
	private final IApplicationLifecycle lifecycle;

	public WorkbenchContext(final IWorkbench workbench, final IApplicationLifecycle lifecycle) {
		workbenchAdvisor = new JoWorkbenchAdvisor(workbench, this);
		this.lifecycle = lifecycle;
	}

	public void run() {
		try {
			PlatformUI.createAndRunWorkbench(Display.getCurrent(), workbenchAdvisor);
		}
		finally {
			lifecycle.finish();
		}
	}

	@Override
	public void add(final IWorkbenchApplication workbenchApplication) {
		workbenchAdvisor.getWorkbenchWindowAdvisor().getApplicationFolder().addApplication(workbenchApplication);
	}

	@Override
	public void add(final int index, final IWorkbenchApplication workbenchApplication) {
		workbenchAdvisor.getWorkbenchWindowAdvisor().getApplicationFolder().addApplication(index, workbenchApplication);
	}

	@Override
	public void remove(final IWorkbenchApplication workbenchApplication) {
		workbenchAdvisor.getWorkbenchWindowAdvisor().getApplicationFolder().removeApplication(workbenchApplication);
	}

	@Override
	public void finish() {
		try {
			PlatformUI.getWorkbench().close();
		}
		finally {
			lifecycle.finish();
		}
	}

	@Override
	public IMenuBar getMenuBar() {
		return workbenchAdvisor.getWorkbenchWindowAdvisor().getMenuBar();
	}

	@Override
	public IToolBar getToolBar() {
		return workbenchAdvisor.getWorkbenchWindowAdvisor().getToolBar();
	}

	@Override
	public IContainer getStatusBar() {
		return workbenchAdvisor.getWorkbenchWindowAdvisor().getStatusBar();
	}

	@Override
	public ITray getTray() {
		return workbenchAdvisor.getWorkbenchWindowAdvisor().getTray();
	}

	@Override
	public void setCloseHandler(final Callable<Boolean> closeHandler) {
		workbenchAdvisor.getWorkbenchWindowAdvisor().setCloseHandler(closeHandler);
	}
}
