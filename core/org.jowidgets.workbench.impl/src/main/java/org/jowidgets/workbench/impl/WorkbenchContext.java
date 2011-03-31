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

package org.jowidgets.workbench.impl;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.controler.ITabFolderListener;
import org.jowidgets.api.controler.ITabSelectionEvent;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.ISplitComposite;
import org.jowidgets.api.widgets.ITabFolder;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.ISplitCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.ITabItemBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.types.SplitResizePolicy;
import org.jowidgets.common.widgets.controler.IWindowListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controler.WindowAdapter;
import org.jowidgets.tools.model.item.MenuBarModel;
import org.jowidgets.tools.types.VetoHolder;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.ITrayItem;
import org.jowidgets.workbench.api.IWorkbench;
import org.jowidgets.workbench.api.IWorkbenchApplication;
import org.jowidgets.workbench.api.IWorkbenchContext;

public class WorkbenchContext implements IWorkbenchContext {

	private final IApplicationLifecycle lifecycle;
	private final List<Runnable> shutdownHooks;
	private final IBluePrintFactory bpf;
	private final IWindowListener windowListener;

	private final List<IWorkbenchApplication> addedApplications;
	private final List<WorkbenchApplicationContext> addedApplicationContexts;
	private final List<ITabItem> addedTabItems;

	private final IFrame rootFrame;
	private final IContainer statusBar;
	private final WorkbenchToolbar toolBar;
	private final IMenuBarModel menuBarModel;
	private final IToolBarModel toolBarModel;
	private final IContainer workbenchContentContainer;
	private final WorkbenchContentPanel workbenchContentPanel;

	private ITabFolder applicationTabFolder;
	private boolean disposed;

	public WorkbenchContext(final IWorkbench workbench, final IApplicationLifecycle lifecycle) {

		this.disposed = false;

		this.bpf = Toolkit.getBluePrintFactory();
		this.addedApplications = new LinkedList<IWorkbenchApplication>();
		this.addedApplicationContexts = new LinkedList<WorkbenchApplicationContext>();
		this.addedTabItems = new LinkedList<ITabItem>();

		this.lifecycle = lifecycle;
		this.shutdownHooks = new LinkedList<Runnable>();
		this.windowListener = createWindowListener(workbench);
		this.rootFrame = createRootFrame(workbench);

		this.menuBarModel = new MenuBarModel();
		rootFrame.setMenuBar(menuBarModel);

		this.toolBar = createToolBar();
		this.toolBarModel = toolBar.getToolBarModel();

		this.workbenchContentContainer = createWorkbenchContentContainer(workbench);
		this.workbenchContentPanel = new WorkbenchContentPanel(workbenchContentContainer);

		this.statusBar = createStatusBar();

		applicationTabFolder.addTabFolderListener(createTabFolderListener());

		workbench.onContextInitialize(this);
	}

	@Override
	public IContainer getStatusBar() {
		return statusBar;
	}

	@Override
	public IToolBarModel getToolBar() {
		return toolBarModel;
	}

	@Override
	public IMenuBarModel getMenuBar() {
		return menuBarModel;
	}

	@Override
	public void add(final IWorkbenchApplication application) {
		add(addedApplications.size(), application);
	}

	@Override
	public void add(final int index, final IWorkbenchApplication application) {
		Assert.paramNotNull(application, "application");

		final ITabItemBluePrint tabItemBp = bpf.tabItem();
		tabItemBp.setText(application.getLabel());
		tabItemBp.setToolTipText(application.getTooltip());
		tabItemBp.setIcon(application.getIcon());

		final ITabItem tabItem = applicationTabFolder.addItem(index, tabItemBp);
		final WorkbenchApplicationContext applicationContext = new WorkbenchApplicationContext(this, tabItem, application);

		addedTabItems.add(tabItem);
		addedApplications.add(index, application);
		addedApplicationContexts.add(index, applicationContext);

		application.onContextInitialize(applicationContext);
	}

	@Override
	public void remove(final IWorkbenchApplication workbenchApplication) {
		Assert.paramNotNull(workbenchApplication, "workbenchApplication");
		final int index = addedApplications.indexOf(workbenchApplication);
		if (index != -1) {
			addedApplications.remove(index);
			final WorkbenchApplicationContext applicationContext = addedApplicationContexts.remove(index);
			applicationContext.dispose();
			final ITabItem tabItem = addedTabItems.remove(index);
			applicationTabFolder.removeItem(tabItem);
			if (addedTabItems.size() == 0) {
				selectComponentNode(null);
			}
		}
	}

	@Override
	public void addShutdownHook(final Runnable shutdownHook) {
		shutdownHooks.add(shutdownHook);
	}

	@Override
	public void removeShutdownHook(final Runnable shutdownHook) {
		shutdownHooks.remove(shutdownHook);
	}

	@Override
	public void finish() {
		if (!disposed) {
			dispose();
		}
	}

	@Override
	public ITrayItem getTrayItem() {
		// TODO MG support trayItem
		return null;
	}

	public void run() {
		if (!this.rootFrame.isVisible()) {
			this.rootFrame.setVisible(true);
		}
	}

	protected void unregsiterApplication(final ITabItem item) {
		final int index = addedTabItems.indexOf(item);
		if (index != -1) {
			addedApplications.remove(index);
			addedTabItems.remove(index);
			addedApplicationContexts.remove(index);
		}
		if (addedTabItems.size() == 0) {
			selectComponentNode(null);
		}
	}

	protected void selectComponentNode(final ComponentNodeContext newComponentNode) {
		workbenchContentContainer.layoutBegin();
		toolBar.layoutBegin();
		rootFrame.setCursor(Cursor.WAIT);

		if (newComponentNode != null) {
			if (!newComponentNode.isActive()) {
				newComponentNode.activate();
			}
		}
		else {
			workbenchContentPanel.setEmptyContent();
		}

		workbenchContentContainer.layoutEnd();
		toolBar.layoutEnd();
		rootFrame.setCursor(Cursor.DEFAULT);
	}

	protected WorkbenchContentPanel getWorkbenchContentPanel() {
		return workbenchContentPanel;
	}

	private IFrame createRootFrame(final IWorkbench workbench) {
		final IFrameBluePrint rootFrameBp = bpf.frame();
		rootFrameBp.setTitle(workbench.getLabel());
		rootFrameBp.setIcon(workbench.getIcon());
		rootFrameBp.setPosition(workbench.getInitialPosition());
		rootFrameBp.setSize(workbench.getInitialDimension());
		final IFrame result = Toolkit.createRootFrame(rootFrameBp);
		result.setLayout(new MigLayoutDescriptor("3[grow]3", "3[]3[grow]3[]3"));
		result.addWindowListener(windowListener);
		return result;
	}

	private WorkbenchToolbar createToolBar() {
		final IContainer toolBarContainer = rootFrame.add(bpf.composite(), "grow ,wrap");
		toolBarContainer.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "0[grow]0"));
		return new WorkbenchToolbar(toolBarContainer);
	}

	private IContainer createWorkbenchContentContainer(final IWorkbench workbench) {
		IContainer result;

		final IComposite rootComposite = rootFrame.add(bpf.composite(), "growx, growy, w 0::, h 0::, wrap");
		rootComposite.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "0[grow, 0::]0"));

		if (workbench.hasApplicationNavigator()) {
			final ISplitCompositeBluePrint splitCompositeBp = bpf.splitHorizontal().disableBorders();
			splitCompositeBp.setResizePolicy(SplitResizePolicy.RESIZE_SECOND);
			splitCompositeBp.setWeight(workbench.getInitialSplitWeight());
			final ISplitComposite splitComposite = rootComposite.add(splitCompositeBp, "growx, growy, h 0::, w 0::");

			final IContainer applicationsContainer = splitComposite.getFirst();
			applicationsContainer.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "0[grow, 0::]0"));
			applicationsContainer.setVisible(workbench.hasApplicationNavigator());
			applicationTabFolder = applicationsContainer.add(
					bpf.tabFolder().setTabsCloseable(workbench.getApplicationsCloseable()),
					"growx, growy, h 0::, w 0::");

			result = splitComposite.getSecond();
		}
		else {
			applicationTabFolder = rootComposite.add(
					bpf.tabFolder().setTabsCloseable(workbench.getApplicationsCloseable()),
					"hidemode 3, growx, growy, h 0::, w 0::");
			applicationTabFolder.setVisible(false);
			result = rootComposite;
		}
		return result;
	}

	private IContainer createStatusBar() {
		return new WorkbenchStatusBar(rootFrame.add(bpf.composite(), "hidemode 2, growx"));
	}

	private ITabFolderListener createTabFolderListener() {
		return new ITabFolderListener() {

			@Override
			public void itemSelected(final ITabSelectionEvent selectionEvent) {
				final ITabItem selectedTab = selectionEvent.getNewSelected();
				if (selectedTab != null) {
					final int selectedIndex = addedTabItems.indexOf(selectedTab);
					if (selectedIndex != -1) {
						final WorkbenchApplicationContext applicationContext = addedApplicationContexts.get(selectedIndex);
						applicationContext.getApplication().onVisibleStateChanged(true);
						selectComponentNode(applicationContext.getSelectedNodeContext());
					}
					//else{this could happen if the application was just added}
				}
			}

			@Override
			public void onDeselection(final IVetoable vetoable, final ITabSelectionEvent selectionEvent) {
				final ITabItem selectedTab = selectionEvent.getLastSelected();
				if (selectedTab != null) {
					final int selectedIndex = addedTabItems.indexOf(selectedTab);
					if (selectedIndex != -1) {
						final WorkbenchApplicationContext applicationContext = addedApplicationContexts.get(selectedIndex);
						final ComponentNodeContext selectedNodeContext = applicationContext.getSelectedNodeContext();
						if (selectedNodeContext != null && selectedNodeContext.isActive()) {
							final VetoHolder vetoHolder = selectedNodeContext.tryDeactivate();
							if (vetoHolder.hasVeto()) {
								vetoable.veto();
								return;
							}
						}
						applicationContext.getApplication().onVisibleStateChanged(false);
					}
				}
			}

		};
	}

	private IWindowListener createWindowListener(final IWorkbench workbench) {
		return new WindowAdapter() {

			@Override
			public void windowClosing(final IVetoable windowCloseVetoable) {
				final VetoHolder vetoHolder = new VetoHolder();

				workbench.onClose(vetoHolder);
				if (vetoHolder.hasVeto()) {
					windowCloseVetoable.veto();
				}
				else {
					dispose();
				}
			}

		};
	}

	private void dispose() {
		if (!disposed) {
			disposed = true;

			rootFrame.removeWindowListener(windowListener);

			//dispose the application
			for (final WorkbenchApplicationContext applicationContext : addedApplicationContexts) {
				applicationContext.dispose();
			}

			runShutdownHooks();
			lifecycle.finish();
		}
	}

	private void runShutdownHooks() {
		for (final Runnable shutdownHook : shutdownHooks) {
			shutdownHook.run();
		}
	}

}
