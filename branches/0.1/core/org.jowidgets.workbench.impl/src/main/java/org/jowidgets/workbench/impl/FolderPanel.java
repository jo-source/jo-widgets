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

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.ITabFolder;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.blueprint.ITabFolderBluePrint;
import org.jowidgets.api.widgets.blueprint.ITabItemBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.tools.controler.TabItemAdapter;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.types.VetoHolder;
import org.jowidgets.workbench.api.IFolderLayout;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewLayout;

public class FolderPanel implements ILayoutPanel {

	private final ITabFolder tabFolder;
	private ComponentContext currentComponent;

	public FolderPanel(
		final IFolderLayout folderLayout,
		final IContainer parentContainer,
		final ComponentContext component) {
		super();

		this.currentComponent = component;

		parentContainer.setLayout(MigLayoutFactory.growingInnerCellLayout());

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		final ITabFolderBluePrint tabFolderBluePrint = bpf.tabFolder();
		tabFolderBluePrint.setTabsCloseable(folderLayout.getViewsCloseable());

		this.tabFolder = parentContainer.add(tabFolderBluePrint, MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		for (final IViewLayout viewLayout : folderLayout.getViews()) {
			createTabItem(null, viewLayout);
		}

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
		final ViewContext viewContext = new ViewContext(tabFolder, tabItem, viewLayout.getScope(), currentComponent);
		final IView view = currentComponent.getComponent().createView(viewLayout.getId(), viewContext);

		tabItem.addTabItemListener(new TabItemAdapter() {

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
	public void setComponent(final ComponentContext component) {
		currentComponent = component;
	}

}
