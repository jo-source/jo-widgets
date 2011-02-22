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

import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.workbench.api.ITrayItem;
import org.jowidgets.workbench.api.IWorkbench;
import org.jowidgets.workbench.api.IWorkbenchApplication;
import org.jowidgets.workbench.api.IWorkbenchContext;

public class WorkbenchContext implements IWorkbenchContext {

	public WorkbenchContext(final IWorkbench workbench, final boolean b) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void add(final IWorkbenchApplication workbenchApplication) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(final int index, final IWorkbenchApplication workbenchApplication) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(final IWorkbenchApplication workbenchApplication) {
		// TODO Auto-generated method stub

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

	@Override
	public IContainer getStatusBar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITrayItem getTrayItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addShutdownHook(final Runnable shutdownHook) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeShutdownHook(final Runnable shutdownHook) {
		// TODO Auto-generated method stub

	}

	public void setSelectedTreeNode(final String[] selectedTreeNode) {
		// TODO Auto-generated method stub

	}

	public void setFolderRatio(final double folderRatio) {
		// TODO Auto-generated method stub

	}

	public String[] getSelectedTreeNode() {
		// TODO Auto-generated method stub
		return null;
	}

	public double getFolderRatio() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setLifecycle(final IApplicationLifecycle lifecycle) {
		// TODO Auto-generated method stub

	}

	public void run() {
		// TODO Auto-generated method stub

	}

}
