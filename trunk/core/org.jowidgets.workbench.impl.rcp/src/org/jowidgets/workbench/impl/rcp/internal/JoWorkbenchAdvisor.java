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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.jowidgets.workbench.api.IWorkbench;
import org.jowidgets.workbench.api.IWorkbenchContext;
import org.jowidgets.workbench.impl.rcp.internal.part.DynamicPerspective;
import org.jowidgets.workbench.impl.rcp.internal.part.PartRegistry;

public final class JoWorkbenchAdvisor extends WorkbenchAdvisor {

	private final IWorkbench workbench;
	private final IWorkbenchContext context;
	private final boolean saveAndRestore;
	private JoWorkbenchWindowAdvisor workbenchWindowAdvisor;
	private final List<Runnable> shutdownHooks = new CopyOnWriteArrayList<Runnable>();
	private Double folderRatio;
	private String[] selectedTreeNode;

	public JoWorkbenchAdvisor(final IWorkbench workbench, final IWorkbenchContext context, final boolean saveAndRestore) {
		this.workbench = workbench;
		this.context = context;
		this.saveAndRestore = saveAndRestore;
	}

	public JoWorkbenchWindowAdvisor getWorkbenchWindowAdvisor() {
		return workbenchWindowAdvisor;
	}

	@Override
	public void initialize(final IWorkbenchConfigurer configurer) {
		configurer.setSaveAndRestore(saveAndRestore);
		super.initialize(configurer);
	}

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {
		workbenchWindowAdvisor = new JoWorkbenchWindowAdvisor(configurer, workbench, context);
		if (folderRatio != null) {
			workbenchWindowAdvisor.setFolderRatio(folderRatio);
		}
		return workbenchWindowAdvisor;
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return DynamicPerspective.ID;
	}

	@Override
	public void postStartup() {
		workbench.initialize(context);
		workbenchWindowAdvisor.setSelectedTreeNode(selectedTreeNode);
	}

	@Override
	public boolean preShutdown() {
		// reset perspective
		PartRegistry.getInstance().showEmptyPerspective();

		for (final Runnable shutdownHook : shutdownHooks) {
			shutdownHook.run();
		}
		return true;
	}

	public void addShutdownHook(final Runnable shutdownHook) {
		shutdownHooks.add(shutdownHook);
	}

	public void removeShutdownHook(final Runnable shutdownHook) {
		shutdownHooks.remove(shutdownHook);
	}

	public void setFolderRatio(final double folderRatio) {
		this.folderRatio = folderRatio;
	}

	public void setSelectedTreeNode(final String[] selectedTreeNode) {
		this.selectedTreeNode = selectedTreeNode;
	}

}
