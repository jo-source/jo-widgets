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
package org.jowidgets.workbench.impl.rcp.internal;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.ISplitComposite;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.types.VetoHolder;
import org.jowidgets.workbench.api.ITrayItem;
import org.jowidgets.workbench.api.IWorkbench;
import org.jowidgets.workbench.api.IWorkbenchContext;
import org.jowidgets.workbench.impl.rcp.internal.util.ImageHelper;

public final class JoWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private final IWorkbench workbench;
	private final IWorkbenchContext context;
	private IFrame frame;
	private IContainer statusBar;
	private ITrayItem tray;
	private IMenuBarModel menuBarModel;
	private IToolBar toolBar;
	private IToolBarModel toolBarModel;
	private WorkbenchApplicationFolder applicationFolder;
	private double folderRatio = 0.2;
	private String[] selectedTreeNode;

	public JoWorkbenchWindowAdvisor(
		final IWorkbenchWindowConfigurer configurer,
		final IWorkbench workbench,
		final IWorkbenchContext context) {
		super(configurer);
		this.workbench = workbench;
		this.context = context;
	}

	public IContainer getStatusBar() {
		if (statusBar == null) {
			frame.layoutBegin();
			statusBar = frame.add(Toolkit.getBluePrintFactory().composite(), "growx");
			frame.layoutEnd();
		}
		return statusBar;
	}

	public ITrayItem getTrayItem() {
		if (tray == null && frame != null) {
			tray = new WorkbenchTrayItem(frame, workbench);
		}
		return tray;
	}

	public WorkbenchApplicationFolder getApplicationFolder() {
		return applicationFolder;
	}

	@Override
	public void preWindowOpen() {
		final IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		final Dimension initialDimension = workbench.getInitialDimension();
		if (initialDimension != null) {
			configurer.setInitialSize(new Point(initialDimension.getWidth(), initialDimension.getHeight()));
		}
		configurer.setTitle(workbench.getLabel());
	}

	@Override
	public boolean preWindowShellClose() {
		final VetoHolder vetoHolder = new VetoHolder();
		workbench.onClose(vetoHolder);
		return !vetoHolder.hasVeto();
	}

	@Override
	public void createWindowContents(final Shell shell) {
		shell.setImage(ImageHelper.getImage(workbench.getIcon(), null));

		frame = Toolkit.getWidgetWrapperFactory().createFrame(shell);

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
		frame.setLayout(new MigLayoutDescriptor("3[grow]3", "0[]0[][grow][]0"));

		// dummy coolbar control
		final Control coolBar = getWindowConfigurer().createCoolBarControl(shell);
		coolBar.setEnabled(false);
		coolBar.setVisible(false);
		coolBar.setLayoutData("wrap");

		toolBar = frame.add(bpf.toolBar(), "hidemode 2, wrap");
		toolBar.setVisible(false);

		final ISplitComposite splitComposite = frame.add(
				bpf.splitHorizontal().setWeight(folderRatio).disableBorders(),
				"wmin 0, hmin 0, grow, wrap");
		final SashForm sashForm = (SashForm) splitComposite.getUiReference();
		sashForm.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(final DisposeEvent e) {
				// save sash weight before it is disposed
				folderRatio = sashForm.getWeights()[0] / 1000.0;
			}
		});

		final IContainer leftContainer = splitComposite.getFirst();
		leftContainer.setLayout(new MigLayoutDescriptor("0[grow]0", "0[grow]0"));
		applicationFolder = new WorkbenchApplicationFolder((Composite) leftContainer.getUiReference(), workbench, context);
		applicationFolder.setLayoutData("grow");
		applicationFolder.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(final DisposeEvent e) {
				selectedTreeNode = applicationFolder.getSelectedTreeNode();
			}
		});

		final IContainer rightContainer = splitComposite.getSecond();
		rightContainer.setLayout(new MigLayoutDescriptor("0[grow]0", "0[grow]0"));
		final Control pageComposite = getWindowConfigurer().createPageComposite((Composite) rightContainer.getUiReference());
		pageComposite.setLayoutData("wmin 0, hmin 0, grow");
	}

	public double getFolderRatio() {
		return folderRatio;
	}

	public void setFolderRatio(final double folderRatio) {
		this.folderRatio = folderRatio;
	}

	public String[] getSelectedTreeNode() {
		return selectedTreeNode;
	}

	public void setSelectedTreeNode(final String[] selectedTreeNode) {
		this.selectedTreeNode = selectedTreeNode;
		if (applicationFolder != null && !applicationFolder.isDisposed()) {
			applicationFolder.setSelectedTreeNode(selectedTreeNode);
		}
	}

	public IToolBarModel getToolBar() {
		if (toolBarModel == null && toolBar != null) {
			toolBarModel = Toolkit.getModelFactoryProvider().getItemModelFactory().toolBar();
			toolBar.setModel(toolBarModel);
			frame.layoutBegin();
			toolBar.setVisible(true);
			frame.layoutEnd();
		}
		return toolBarModel;
	}

	public IMenuBarModel getMenuBar() {
		if (menuBarModel == null && frame != null) {
			menuBarModel = Toolkit.getModelFactoryProvider().getItemModelFactory().menuBar();
			frame.createMenuBar().setModel(menuBarModel);
		}
		return menuBarModel;
	}

}
