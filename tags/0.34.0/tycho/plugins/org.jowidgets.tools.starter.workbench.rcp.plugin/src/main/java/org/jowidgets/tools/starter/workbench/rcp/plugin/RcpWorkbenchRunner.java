/*
 * Copyright (c) 2014, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package org.jowidgets.tools.starter.workbench.rcp.plugin;

import java.io.File;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IWorkbenchFactory;
import org.jowidgets.workbench.impl.rcp.FileConfigService;
import org.jowidgets.workbench.impl.rcp.WorkbenchRunner;

public class RcpWorkbenchRunner implements IApplication {

	private static final WorkbenchRunner WORKBENCH_RUNNER = new WorkbenchRunner();

	private final IWorkbenchFactory workbenchFactory;

	public RcpWorkbenchRunner(final IWorkbenchFactory workbenchFactory) {
		Assert.paramNotNull(workbenchFactory, "workbenchFactory");
		this.workbenchFactory = workbenchFactory;
	}

	@Override
	public Object start(final IApplicationContext context) throws Exception {
		final String configFilePath = System.getProperty("user.home") + File.separator + getClass().getName() + ".config";
		final FileConfigService configurationService = new FileConfigService(configFilePath);
		WORKBENCH_RUNNER.run(workbenchFactory, configurationService);
		return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {}

}
