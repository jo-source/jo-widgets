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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.IWorkbenchThemeConstants;
import org.eclipse.ui.themes.ITheme;
import org.jowidgets.tools.types.VetoHolder;
import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.api.IWorkbench;
import org.jowidgets.workbench.api.IWorkbenchApplication;
import org.jowidgets.workbench.impl.rcp.internal.part.PartSupport;
import org.jowidgets.workbench.impl.rcp.internal.util.ImageHelper;

@SuppressWarnings("restriction")
public final class WorkbenchApplicationFolder extends Composite {

	private final CTabFolder tabFolder;
	private final WorkbenchContext workbenchContext;
	private WorkbenchApplicationTree selectedApplicationTree;
	private ComponentTreeNodeContext selectedComponentTreeNodeContext;

	public WorkbenchApplicationFolder(final Composite parent, final IWorkbench workbench, final WorkbenchContext workbenchContext) {
		super(parent, SWT.NONE);
		this.workbenchContext = workbenchContext;

		setLayout(new FillLayout());
		final int tabStyle = PlatformUI.getPreferenceStore().getInt(IWorkbenchPreferenceConstants.VIEW_TAB_POSITION);
		tabFolder = new CTabFolder(this, tabStyle
			| SWT.NO_BACKGROUND
			| SWT.BORDER
			| (workbench.getApplicationsCloseable() ? SWT.CLOSE : SWT.NONE));
		tabFolder.setUnselectedCloseVisible(false);
		setFont();
		setTabHeight();

		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final CTabItem currentItem = tabFolder.getSelection();
				final WorkbenchApplicationTree appTree = (WorkbenchApplicationTree) currentItem.getControl();
				updateTopRightComposite(appTree.getFolderComposite());
			}
		});

		tabFolder.addCTabFolder2Listener(new CTabFolder2Adapter() {
			@Override
			public void close(final CTabFolderEvent event) {
				final CTabItem tabItem = (CTabItem) event.item;
				final IWorkbenchApplication application = (IWorkbenchApplication) tabItem.getData();
				final VetoHolder vetoHolder = new VetoHolder();
				application.onClose(vetoHolder);
				if (!vetoHolder.hasVeto()) {
					final WorkbenchApplicationTree tree = (WorkbenchApplicationTree) tabItem.getControl();
					tree.clearSelection(true);
					tree.dispose();
				}
				event.doit = !vetoHolder.hasVeto();
			}
		});

		getShell().addShellListener(new ShellAdapter() {
			@Override
			public void shellActivated(final ShellEvent e) {
				setFocusColors();
			}

			@Override
			public void shellDeactivated(final ShellEvent e) {
				setNoFocusColors();
			}
		});
	}

	public void addApplication(final IWorkbenchApplication app) {
		addApplication(tabFolder.getItemCount(), app);
	}

	public void addApplication(int index, final IWorkbenchApplication app) {
		if (index < 0) {
			index = 0;
		}
		else if (index > tabFolder.getItemCount()) {
			index = tabFolder.getItemCount();
		}
		final CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE, index);
		tabItem.setText(app.getLabel());
		tabItem.setImage(ImageHelper.getImage(
				app.getIcon(),
				PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT)));
		tabItem.setToolTipText(app.getTooltip());
		final WorkbenchApplicationTree appTree = new WorkbenchApplicationTree(tabFolder, app, this);
		final WorkbenchApplicationContext context = new WorkbenchApplicationContext(workbenchContext, app, appTree);
		tabItem.setControl(appTree);
		tabItem.setData(app);
		app.onContextInitialize(context);
	}

	public void removeApplication(final IWorkbenchApplication app) {
		for (final CTabItem item : tabFolder.getItems()) {
			if (item.getData() == app) {
				// unselect to clear perspective
				final WorkbenchApplicationTree tree = (WorkbenchApplicationTree) item.getControl();
				tree.clearSelection(true);
				tree.dispose();
				item.dispose();
				break;
			}
		}
	}

	private void setTabHeight() {
		final ToolBar tmpToolBar = new ToolBar(tabFolder, SWT.FLAT | SWT.NO_BACKGROUND);
		try {
			final GC gc = new GC(tabFolder);
			try {
				final int tabHeight = Math.max(
						tmpToolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT).y,
						gc.getFontMetrics().getHeight());
				tabFolder.setTabHeight(tabHeight);
			}
			finally {
				gc.dispose();
			}
		}
		finally {
			tmpToolBar.dispose();
		}
	}

	private void setFocusColors() {
		final ITheme theme = getTheme();
		final ColorRegistry colorRegistry = theme.getColorRegistry();
		final Color textColor = colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_TEXT_COLOR);
		if (textColor != null) {
			tabFolder.setSelectionForeground(textColor);
		}
		final Color startBgColor = colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_BG_START);
		final Color endBgColor = colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_BG_END);
		if (startBgColor != null && endBgColor != null) {
			final int percent = theme.getInt(IWorkbenchThemeConstants.ACTIVE_TAB_PERCENT);
			final boolean vertical = theme.getBoolean(IWorkbenchThemeConstants.ACTIVE_TAB_VERTICAL);
			tabFolder.setSelectionBackground(new Color[] {startBgColor, endBgColor}, new int[] {percent}, vertical);
		}
	}

	private void setNoFocusColors() {
		final ITheme theme = getTheme();
		final ColorRegistry colorRegistry = theme.getColorRegistry();
		final Color textColor = colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_NOFOCUS_TAB_TEXT_COLOR);
		if (textColor != null) {
			tabFolder.setSelectionForeground(textColor);
		}
		final Color startBgColor = colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_NOFOCUS_TAB_BG_START);
		final Color endBgColor = colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_NOFOCUS_TAB_BG_END);
		if (startBgColor != null && endBgColor != null) {
			final int percent = theme.getInt(IWorkbenchThemeConstants.ACTIVE_NOFOCUS_TAB_PERCENT);
			final boolean vertical = theme.getBoolean(IWorkbenchThemeConstants.ACTIVE_NOFOCUS_TAB_VERTICAL);
			tabFolder.setSelectionBackground(new Color[] {startBgColor, endBgColor}, new int[] {percent}, vertical);
		}
	}

	private void setFont() {
		final Font font = getTheme().getFontRegistry().get(IWorkbenchThemeConstants.TAB_TEXT_FONT);
		if (font != null) {
			tabFolder.setFont(font);
		}
	}

	private ITheme getTheme() {
		return PlatformUI.getWorkbench().getThemeManager().getCurrentTheme();
	}

	public void selectTreeNode(final String[] selectedTreeNode) {
		if (selectedTreeNode != null && selectedTreeNode.length > 0) {
			final String appId = selectedTreeNode[0];
			for (final CTabItem item : tabFolder.getItems()) {
				final IWorkbenchApplication app = (IWorkbenchApplication) item.getData();
				if (app.getId().equals(appId)) {
					final WorkbenchApplicationTree tree = (WorkbenchApplicationTree) item.getControl();
					tree.selectTreeNode(Arrays.asList(selectedTreeNode).subList(1, selectedTreeNode.length));
					updateTopRightComposite(tree.getFolderComposite());
					tabFolder.setSelection(item);
					break;
				}
			}
		}
	}

	public void onTreeNodeUnselection() {
		onTreeNodeSelectionChange(null, null);
	}

	public void onTreeNodeSelectionChange(
		final WorkbenchApplicationTree tree,
		final ComponentTreeNodeContext selectedComponentTreeNodeContext) {

		if (selectedComponentTreeNodeContext == this.selectedComponentTreeNodeContext) {
			return;
		}

		if (selectedComponentTreeNodeContext != null && selectedApplicationTree != null && selectedApplicationTree != tree) {
			selectedApplicationTree.clearSelection(false);
		}

		final IComponent currentComponent = workbenchContext.getCurrentComponent();
		if (currentComponent != null && selectedComponentTreeNodeContext != null) {
			final VetoHolder vetoHolder = new VetoHolder();
			currentComponent.onDeactivation(vetoHolder);
			if (vetoHolder.hasVeto()) {
				tree.clearSelection(false);
				this.selectedComponentTreeNodeContext.select();
				return;
			}
		}

		selectedApplicationTree = tree;
		this.selectedComponentTreeNodeContext = selectedComponentTreeNodeContext;

		if (selectedComponentTreeNodeContext != null) {
			final ComponentContext componentContext = selectedComponentTreeNodeContext.getComponentContext();
			if (componentContext != null) {
				final IComponent newComponent = componentContext.getComponent();
				workbenchContext.setCurrentComponent(newComponent);
				newComponent.onActivation();
				PartSupport.getInstance().showPerspective(
						selectedComponentTreeNodeContext,
						componentContext,
						componentContext.getPerspective());
				return;
			}
		}
		workbenchContext.setCurrentComponent(null);
		PartSupport.getInstance().showEmptyPerspective();
	}

	public String[] getSelectedTreeNode() {
		if (selectedComponentTreeNodeContext == null) {
			return null;
		}
		final List<String> result = new LinkedList<String>();
		ComponentTreeNodeContext context = selectedComponentTreeNodeContext;
		while (context != null) {
			result.add(0, context.getId());
			context = (ComponentTreeNodeContext) context.getParent();
		}
		result.add(0, selectedApplicationTree.getApplication().getId());
		return result.toArray(new String[0]);
	}

	private void updateTopRightComposite(final Control newControl) {
		final Control oldControl = tabFolder.getTopRight();
		if (oldControl != null) {
			oldControl.setVisible(false);
		}
		tabFolder.setTopRight(newControl);
		newControl.setVisible(true);
	}

}
