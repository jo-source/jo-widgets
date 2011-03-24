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

package org.jowidgets.addons.testtool;

import java.util.List;

import org.jowidgets.addons.testtool.internal.TestDataObject;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.factory.IWidgetFactoryListener;
import org.jowidgets.workbench.api.IWorkbench;
import org.jowidgets.workbench.api.IWorkbenchRunner;

public class TestToolRunner {

	private ITestTool testTool;
	private final IApplication app;
	private IWorkbenchRunner workbenchRunner;
	private IWorkbench workbench;

	public TestToolRunner(final IApplication app) {
		init();
		this.app = app;
	}

	public TestToolRunner(final IWorkbenchRunner workbenchRunner, final IWorkbench workbench) {
		init();
		this.app = null;
		this.workbenchRunner = workbenchRunner;
		this.workbench = workbench;
	}

	public void run() {
		new TestToolView(testTool);
		runApplication();
		runWorkbench();
	}

	public void runAsUnitTest(final String fileName) {
		final List<TestDataObject> tests = testTool.load(fileName);
		testTool.activateReplayMode();
		testTool.replay(tests, 3000);
		runApplication();
		runWorkbench();
	}

	private void init() {
		testTool = new TestToolImpl();
		Toolkit.getWidgetFactory().addWidgetFactoryListener(new IWidgetFactoryListener() {

			@Override
			public void widgetCreated(final IWidgetCommon widget) {
				testTool.register(widget);
			}
		});
	}

	private void runApplication() {
		if (app != null) {
			Toolkit.getInstance().getApplicationRunner().run(app);
		}
	}

	private void runWorkbench() {
		if (workbenchRunner != null) {
			if (workbench != null) {
				workbenchRunner.run(workbench);
			}
		}
	}
}
