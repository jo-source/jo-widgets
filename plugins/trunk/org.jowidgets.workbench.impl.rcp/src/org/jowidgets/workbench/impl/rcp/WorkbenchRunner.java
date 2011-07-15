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
package org.jowidgets.workbench.impl.rcp;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IWorkbenchConfigurationService;
import org.jowidgets.workbench.api.IWorkbenchFactory;
import org.jowidgets.workbench.api.IWorkbenchRunner;
import org.jowidgets.workbench.impl.rcp.internal.WorkbenchContext;
import org.jowidgets.workbench.impl.rcp.internal.util.RcpWorkbenchConfigurationSupport;

//TODO HW check if the IWorkbechFactory refactoring works
public final class WorkbenchRunner implements IWorkbenchRunner {

	@Override
	public void run(final IWorkbenchFactory workbenchFactory) {
		Toolkit.getApplicationRunner().run(new IApplication() {
			@Override
			public void start(final IApplicationLifecycle lifecycle) {
				final WorkbenchContext context = new WorkbenchContext(workbenchFactory.create(), false);
				context.setLifecycle(lifecycle);
				context.run();
			}
		});
	}

	@Override
	public void run(final IWorkbenchFactory workbenchFactory, final IWorkbenchConfigurationService configurationService) {
		Assert.paramNotNull(workbenchFactory, "workbenchFactory");
		Assert.paramNotNull(configurationService, "configurationService");

		Toolkit.getApplicationRunner().run(new IApplication() {
			@Override
			public void start(final IApplicationLifecycle lifecycle) {
				final WorkbenchConfiguration config = (WorkbenchConfiguration) configurationService.loadConfiguration();
				final WorkbenchContext context = new WorkbenchContext(workbenchFactory.create(), true);
				final RcpWorkbenchConfigurationSupport rcpSupport = new RcpWorkbenchConfigurationSupport();
				if (config != null) {
					context.setSelectedTreeNode(config.getSelectedTreeNode());
					context.setFolderRatio(config.getFolderRatio());
					rcpSupport.setConfiguration(config.getWorkbenchXml());
				}
				else {
					rcpSupport.setConfiguration(null);
				}
				context.setLifecycle(lifecycle);
				context.run();
				configurationService.saveConfiguration(new WorkbenchConfiguration(
					context.getSelectedTreeNode(),
					context.getFolderRatio(),
					rcpSupport.getConfiguration()));
			}
		});

	}

}
