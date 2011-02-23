/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.workbench.impl.internal;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.ISplitComposite;
import org.jowidgets.api.widgets.ITabFolder;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.types.SplitResizePolicy;
import org.jowidgets.common.widgets.controler.IWindowListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controler.WindowAdapter;
import org.jowidgets.util.ValueHolder;
import org.jowidgets.workbench.api.ITrayItem;
import org.jowidgets.workbench.api.IWorkbench;
import org.jowidgets.workbench.api.IWorkbenchApplication;
import org.jowidgets.workbench.api.IWorkbenchContext;

public class WorkbenchContext implements IWorkbenchContext {

	private final IApplicationLifecycle lifecycle;
	private final List<Runnable> shutdownHooks;
	private final IBluePrintFactory bpf;
	private final IWindowListener windowListener;

	private final IFrame rootFrame;
	private final IContainer statusBar;
	private final ITabFolder applicationTabFolder;
	private final IContainer contentContainer;

	public WorkbenchContext(final IWorkbench workbench, final IApplicationLifecycle lifecycle) {
		this.lifecycle = lifecycle;

		this.shutdownHooks = new LinkedList<Runnable>();
		this.bpf = Toolkit.getBluePrintFactory();

		this.windowListener = new WindowAdapter() {

			@Override
			public void windowClosed() {
				final ValueHolder<Boolean> veto = new ValueHolder<Boolean>(Boolean.FALSE);
				final IVetoable vetoable = new IVetoable() {
					@Override
					public void veto() {
						veto.set(Boolean.TRUE);
					}
				};
				workbench.onWindowClose(vetoable);
				if (veto.get().booleanValue()) {
					//TODO MG JoWidgets window listener does not support veto at the moment
					//CHECKSTYLE:OFF
					System.out.println("JoWidgets window listener does not support veto at the moment");
					//CHECKSTYLE:ON
				}
				runShutdownHooks();
				lifecycle.finish();
			}

		};

		final IFrameBluePrint rootFrameBp = bpf.frame();
		rootFrameBp.setTitle(workbench.getLabel());
		rootFrameBp.setIcon(workbench.getIcon());
		rootFrameBp.setPosition(workbench.getInitialPosition());
		rootFrameBp.setSize(workbench.getInitialDimension());

		rootFrame = Toolkit.createRootFrame(rootFrameBp);
		rootFrame.addWindowListener(windowListener);

		final IMenuBarModel menuBarModel = workbench.createMenuBar();
		final IToolBarModel toolBarModel = workbench.createToolBar();

		if (menuBarModel != null) {
			rootFrame.setMenuBar(menuBarModel);
		}
		if (toolBarModel != null) {
			rootFrame.setLayout(new MigLayoutDescriptor("3[grow]3", "3[]3[grow][]"));
			final IToolBar toolBar = rootFrame.add(bpf.toolBar(), "grow, wrap");
			toolBar.setModel(toolBarModel);
		}
		else {
			rootFrame.setLayout(new MigLayoutDescriptor("3[grow]3", "3[grow][]"));
		}

		final IComposite rootComposite = rootFrame.add(bpf.composite(), "growx, growy, w 0::, h 0::, wrap");
		rootComposite.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "0[grow, 0::]0"));

		final ISplitComposite splitComposite = rootComposite.add(
				bpf.splitHorizontal().setResizePolicy(SplitResizePolicy.RESIZE_SECOND).setWeight(0.25).disableBorders(),
				"growx, growy, h 0::, w 0::");

		final IContainer applicationsContainer = splitComposite.getFirst();
		applicationsContainer.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "0[grow, 0::]0"));

		applicationTabFolder = applicationsContainer.add(
				bpf.tabFolder().setTabsCloseable(workbench.getApplicationsCloseable()),
				"growx, growy, h 0::, w 0::");

		contentContainer = splitComposite.getSecond();
		contentContainer.add(bpf.compositeWithBorder(), "growx, growy");

		if (workbench.hasStatusBar()) {
			statusBar = rootFrame.add(bpf.composite(), "growx, h 22!");
		}
		else {
			statusBar = null;
		}

		for (final IWorkbenchApplication application : workbench.createWorkbenchApplications()) {
			add(application);
		}

		workbench.onContextInitialize(this);
	}

	public void run() {
		if (!this.rootFrame.isVisible()) {
			this.rootFrame.setVisible(true);
		}
	}

	@Override
	public void finish() {
		if (this.rootFrame.isVisible()) {
			rootFrame.removeWindowListener(windowListener);
			this.rootFrame.dispose();
			runShutdownHooks();
			lifecycle.finish();
		}
	}

	@Override
	public void add(final IWorkbenchApplication workbenchApplication) {
		final ITabItem tabItem = applicationTabFolder.addItem(bpf.tabItem());
		new WorkbenchApplicationContext(this, tabItem, contentContainer, workbenchApplication);
	}

	@Override
	public void add(final int index, final IWorkbenchApplication workbenchApplication) {
		// TODO MG implement application add
	}

	@Override
	public void remove(final IWorkbenchApplication workbenchApplication) {
		// TODO MG implement application remove
	}

	@Override
	public IContainer getStatusBar() {
		return statusBar;
	}

	@Override
	public ITrayItem getTrayItem() {
		// TODO MG support trayItem
		return null;
	}

	@Override
	public void addShutdownHook(final Runnable shutdownHook) {
		shutdownHooks.add(shutdownHook);
	}

	@Override
	public void removeShutdownHook(final Runnable shutdownHook) {
		shutdownHooks.remove(shutdownHook);
	}

	private void runShutdownHooks() {
		for (final Runnable shutdownHook : shutdownHooks) {
			shutdownHook.run();
		}
	}

}
