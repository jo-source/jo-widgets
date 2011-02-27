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

import org.jowidgets.api.controler.ITabItemListener;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.ITabFolder;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.blueprint.ITabFolderBluePrint;
import org.jowidgets.api.widgets.blueprint.ITabItemBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.types.VetoHolder;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IComponentContext;
import org.jowidgets.workbench.api.IComponentTreeNodeContext;
import org.jowidgets.workbench.api.IFolderContext;
import org.jowidgets.workbench.api.IFolderLayout;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewLayout;
import org.jowidgets.workbench.api.IWorkbenchApplicationContext;
import org.jowidgets.workbench.api.IWorkbenchContext;

public class FolderContext implements IFolderContext {

	private final LayoutContext layoutContext;
	private final IFolderLayout folderLayout;
	private final IMenuModel popupMenu;
	private final ITabFolder tabFolder;

	public FolderContext(final IContainer parentContainer, final IFolderLayout folderLayout, final LayoutContext layoutContext) {
		super();
		this.layoutContext = layoutContext;
		this.folderLayout = folderLayout;
		this.popupMenu = new MenuModel();

		parentContainer.setLayout(MigLayoutFactory.growingInnerCellLayout());

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		final ITabFolderBluePrint tabFolderBluePrint = bpf.tabFolder();
		tabFolderBluePrint.setTabsCloseable(folderLayout.getViewsCloseable());

		this.tabFolder = parentContainer.add(tabFolderBluePrint, MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		for (final IViewLayout viewLayout : folderLayout.getViews()) {
			createTabItem(null, viewLayout);
		}
	}

	@Override
	public String getFolderId() {
		return folderLayout.getId();
	}

	@Override
	public String getOriginalFolderId() {
		return folderLayout.getId();
	}

	@Override
	public IMenuModel getPopupMenu() {
		return popupMenu;
	}

	@Override
	public void addView(final IViewLayout viewLayout) {
		Assert.paramNotNull(viewLayout, "viewLayout");
		createTabItem(null, viewLayout);
	}

	@Override
	public void addView(final boolean addToFront, final IViewLayout viewLayout) {
		Assert.paramNotNull(viewLayout, "viewLayout");
		createTabItem(0, viewLayout);
	}

	@Override
	public void removeView(final IView remove) {
		// TODO MG implement remove view
	}

	private ITabItem createTabItem(final Integer index, final IViewLayout viewLayout) {
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
		final ITabItemBluePrint tabItemBp = bpf.tabItem();
		tabItemBp.setText(viewLayout.getLabel());
		tabItemBp.setToolTipText(viewLayout.getTooltip());
		tabItemBp.setIcon(viewLayout.getIcon());

		final ITabItem tabItem;
		if (index != null) {
			tabItem = tabFolder.addItem(index.intValue(), tabItemBp);
		}
		else {
			tabItem = tabFolder.addItem(tabItemBp);
		}

		//TODO MG this must be done lazily (later)
		final ViewContext viewContext = new ViewContext(tabFolder, tabItem, layoutContext.getComponentContext());
		final IView view = layoutContext.getComponent().createView(viewLayout.getId(), viewContext);

		tabItem.addTabItemListener(new ITabItemListener() {

			@Override
			public void selectionChanged(final boolean selected) {
				view.onVisibleStateChanged(selected);
				viewContext.packToolBar();
				tabItem.layoutBegin();
				tabItem.layoutEnd();
			}

			@Override
			public void onClose(final IVetoable vetoable) {
				final VetoHolder vetoHolder = new VetoHolder();
				view.onClose(vetoHolder);
				if (vetoHolder.hasVeto()) {
					vetoable.veto();
				}
			}
		});

		return tabItem;
	}

	@Override
	public IComponentContext getComponentContext() {
		return layoutContext.getComponentContext();
	}

	@Override
	public IComponentTreeNodeContext getComponentTreeNodeContext() {
		return layoutContext.getComponentContext().getComponentTreeNodeContext();
	}

	@Override
	public IWorkbenchApplicationContext getWorkbenchApplicationContext() {
		return layoutContext.getComponentContext().getWorkbenchApplicationContext();
	}

	@Override
	public IWorkbenchContext getWorkbenchContext() {
		return layoutContext.getComponentContext().getWorkbenchContext();
	}

}
