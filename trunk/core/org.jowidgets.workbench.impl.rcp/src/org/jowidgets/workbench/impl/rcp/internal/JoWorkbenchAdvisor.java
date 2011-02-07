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

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.jowidgets.workbench.api.IWorkbench;
import org.jowidgets.workbench.api.IWorkbenchContext;

public final class JoWorkbenchAdvisor extends WorkbenchAdvisor {

	private final IWorkbench workbench;
	private final IWorkbenchContext context;
	private JoWorkbenchWindowAdvisor workbenchWindowAdvisor;

	public JoWorkbenchAdvisor(final IWorkbench workbench, final IWorkbenchContext context) {
		this.workbench = workbench;
		this.context = context;
	}

	public JoWorkbenchWindowAdvisor getWorkbenchWindowAdvisor() {
		return workbenchWindowAdvisor;
	}

	@Override
	public void initialize(final IWorkbenchConfigurer configurer) {
		configurer.setSaveAndRestore(true);
		super.initialize(configurer);
	}

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {
		workbenchWindowAdvisor = new JoWorkbenchWindowAdvisor(configurer, workbench, context);
		return workbenchWindowAdvisor;
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return DynamicPerspective.ID;
	}

	@Override
	public void postStartup() {
		workbench.initialize(context);
	}

	@Override
	public boolean preShutdown() {
		// Resetting perspective so it doesn't open uninitialized on next start
		/*
		 * Activator.getDefault().getPartSupport().openEmptyPerspective();
		 * final IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
		 * final IPerspectiveDescriptor perspectiveDescriptor = perspectiveRegistry.findPerspectiveWithId(DynamicPerspective.ID);
		 * final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		 * activePage.setPerspective(perspectiveDescriptor);
		 * perspectiveRegistry.setDefaultPerspective(DynamicPerspective.ID);
		 */
		return super.preShutdown();
	}

}
