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
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.factory.IWidgetFactoryListener;
import org.jowidgets.workbench.api.IWorkbench;
import org.jowidgets.workbench.api.IWorkbenchRunner;

public class TestToolRunner {

	private final IApplication app;
	private final IWorkbenchRunner workbenchRunner;
	private final IWorkbench workbench;

	public TestToolRunner(final IApplication app) {
		this.app = app;
		this.workbench = null;
		this.workbenchRunner = null;
	}

	public TestToolRunner(final IWorkbenchRunner workbenchRunner, final IWorkbench workbench) {
		this.app = null;
		this.workbenchRunner = workbenchRunner;
		this.workbench = workbench;
	}

	public void run() {
		final ITestTool testTool = new TestToolImpl();
		new TestToolView(testTool);
		Toolkit.getWidgetFactory().addWidgetFactoryListener(new IWidgetFactoryListener() {

			@Override
			public void widgetCreated(final IWidgetCommon widget) {
				testTool.register(widget);
			}
		});
		runApplication();
		runWorkbench();
	}

	public void runAsUnitTest(final String fileName) {
		final ITestTool testTool = new TestToolImpl();
		Toolkit.getWidgetFactory().addWidgetFactoryListener(new IWidgetFactoryListener() {

			@Override
			public void widgetCreated(final IWidgetCommon widget) {
				testTool.register(widget);
			}
		});
		initApplicationRunner();
		final Thread thread = new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					Thread.sleep(200);
				}
				catch (final InterruptedException e) {
				}
				final List<TestDataObject> tests = testTool.load(fileName);
				testTool.activateReplayMode();
				testTool.replay(tests, 200);
			}
		};
		Toolkit.getUiThreadAccess().invokeLater(thread);
		runApplication();
		runWorkbench();
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

	// Workaround for swing and dummy applicationrunner. 
	// Needed to initialize the event-dispatcher-Thread, before starting the application.
	// TODO LG remove this workaround, when there is a callback for applications (to regonize start/stop of an application).
	private void initApplicationRunner() {
		Toolkit.getApplicationRunner().run(new IApplication() {

			@Override
			public void start(final IApplicationLifecycle lifecycle) {
				lifecycle.finish();
			}
		});
	}
}
