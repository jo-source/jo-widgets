/*
 * Copyright (c) 2011, Lukas Gross
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

package org.jowidgets.addons.workbench;

import org.jowidgets.addons.testtool.ITestTool;
import org.jowidgets.addons.testtool.TestToolImpl;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.factory.IWidgetFactoryListener;
import org.jowidgets.workbench.api.IWorkbench;
import org.jowidgets.workbench.api.IWorkbenchConfigurationService;
import org.jowidgets.workbench.api.IWorkbenchRunner;
import org.jowidgets.workbench.impl.WorkbenchRunner;

public class TestWorkbenchRunner implements IWorkbenchRunner {

	private final IWorkbenchRunner runner;

	public TestWorkbenchRunner() {
		runner = new WorkbenchRunnerWrapper(new WorkbenchRunner());
	}

	@Override
	public void run(final IWorkbench workbench) {
		run(workbench, null);
	}

	@Override
	public void run(final IWorkbench workbench, final IWorkbenchConfigurationService configurationService) {
		final ITestTool testTool = new TestToolImpl();
		Toolkit.getWidgetFactory().addWidgetFactoryListener(new IWidgetFactoryListener() {

			@Override
			public void widgetCreated(final IWidgetCommon widget) {
				// CHECKSTYLE:OFF
				System.out.println("Register Widget: " + widget.getClass().getSimpleName());
				// CHECKSTYLE:ON
				testTool.register(widget);
			}
		});
		runner.run(workbench, configurationService);
	}

}
