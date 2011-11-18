/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.examples.common.demo;

import org.jowidgets.addons.icons.silkicons.SilkIcons;
import org.jowidgets.api.color.Colors;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.types.QuestionResult;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ISplitComposite;
import org.jowidgets.api.widgets.ITabFolder;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.blueprint.ITabItemBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controller.TabItemAdapter;
import org.jowidgets.tools.model.item.MenuModel;

public final class DemoTabFolderComposite {

	private final IToolBarModel toolBarModel;

	protected DemoTabFolderComposite(final IContainer parentContainer) {

		this.toolBarModel = new DemoMenuProvider(false).getToolBarModel();

		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();

		final ILayoutDescriptor fillLayoutDescriptor = new MigLayoutDescriptor("0[grow, 0::]0", "0[grow, 0::]0");
		parentContainer.setLayout(fillLayoutDescriptor);

		final ISplitComposite split = parentContainer.add(
				bpF.splitHorizontal().setFirstBorder(null).setSecondBorder(null),
				"growx, growy, w 0::, h 0::");

		final ITabFolder folder1 = addFolder(split.getFirst());
		final ITabFolder folder2 = addFolder(split.getSecond());

		final ITabItem item = folder1.getItem(1);
		if (item.isReparentable()) {
			item.setText(item.getText() + " (r)");
			folder1.detachItem(item);
			folder2.attachItem(item);
		}

		final ITabItem item2 = folder2.getItem(0);
		if (item2.isReparentable()) {
			item2.setText(item2.getText() + " (r)");
			folder2.detachItem(item2);
			folder1.attachItem(item2);
		}

		final IPopupMenu popupMenu = folder1.createPopupMenu();
		fillPopupMenu(popupMenu);
		folder1.addPopupDetectionListener(new IPopupDetectionListener() {

			@Override
			public void popupDetected(final Position position) {
				popupMenu.show(position);
			}
		});

		final IWindow parentWindow = Toolkit.getWidgetUtils().getWindowAncestor(parentContainer);
		final IFrame childWindow = Toolkit.createRootFrame(bpF.frame("Child"));
		childWindow.setSize(parentWindow.getSize());
		childWindow.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "0[grow, 0::]0"));

		final ITabFolder folder = childWindow.add(bpF.tabFolder().setTabsCloseable(true), "growx, growy, w 0::, h 0::");
		if (item2.isReparentable()) {
			folder1.detachItem(item2);
			folder.attachItem(item2);
		}
		if (item.isReparentable()) {
			folder2.detachItem(item);
			folder.attachItem(item);
		}

		childWindow.setPosition(new Position(0, 0));
		childWindow.setVisible(true);
	}

	private void fillPopupMenu(final IPopupMenu popupMenu) {
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
		popupMenu.addItem(bpf.menuItem("item1"));
		popupMenu.addItem(bpf.menuItem("item2"));
		popupMenu.addItem(bpf.menuItem("item3"));
		popupMenu.addItem(bpf.menuItem("item4"));
	}

	private ITabFolder addFolder(final IContainer parentContainer) {
		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();

		final ILayoutDescriptor fillLayoutDescriptor = new MigLayoutDescriptor("0[grow, 0::]0", "0[grow, 0::]0");
		parentContainer.setLayout(fillLayoutDescriptor);

		final ITabFolder tabFolder = parentContainer.add(bpF.tabFolder().setTabsCloseable(true), "growx, growy, w 0::, h 0::");

		ITabItemBluePrint tabItemBp = bpF.tabItem();
		tabItemBp.setText("Tab1").setToolTipText("Tooltip of tab1").setIcon(SilkIcons.ATTACH);
		final ITabItem tabItem1 = tabFolder.addItem(tabItemBp);
		addTabContent(tabItem1);

		tabItemBp = bpF.tabItem();
		tabItemBp.setText("Tab2").setToolTipText("Tooltip of tab2").setIcon(SilkIcons.PALETTE);
		final ITabItem tabItem2 = tabFolder.addItem(tabItemBp);
		addTabContent(tabItem2);

		tabItemBp = bpF.tabItem();
		tabItemBp.setText("Tab3").setToolTipText("Tooltip of tab3").setIcon(SilkIcons.STATUS_ONLINE);
		final ITabItem tabItem3 = tabFolder.addItem(tabItemBp);
		addTabContent(tabItem3);

		tabItemBp = bpF.tabItem();
		tabItemBp.setText("Tab4").setToolTipText("Tooltip of tab4").setIcon(SilkIcons.ROSETTE);
		final ITabItem tabItem4 = tabFolder.addItem(tabItemBp);
		addTabContent(tabItem4);
		tabItem4.addTabItemListener(new TabItemAdapter() {

			@Override
			public void onDeselection(final IVetoable vetoable) {
				final QuestionResult questionResult = Toolkit.getQuestionPane().askYesNoQuestion("Should tab4 be deselected?");
				if (questionResult != QuestionResult.YES) {
					vetoable.veto();
				}
			}

		});

		tabItemBp = bpF.tabItem();
		tabItemBp.setText("Tab5").setToolTipText("Tooltip of tab5").setIcon(SilkIcons.CHART_PIE);
		final ITabItem tabItem5 = tabFolder.addItem(tabItemBp);
		addTabContent(tabItem5);

		tabItemBp = bpF.tabItem();
		tabItemBp.setText("Tab6").setToolTipText("Tooltip of tab6").setIcon(SilkIcons.GROUP);
		final ITabItem tabItem6 = tabFolder.addItem(tabItemBp);
		addTabContent(tabItem6);

		tabFolder.changeItemIndex(tabItem1, 2);
		tabFolder.setSelectedItem(tabItem1);

		return tabFolder;
	}

	private void addTabContent(final ITabItem tabItem) {
		tabItem.addTabItemListener(new TabItemAdapter() {

			@Override
			public void selectionChanged(final boolean selected) {
				// CHECKSTYLE:OFF
				System.out.println("Item '" + tabItem.getText() + "' selected: " + selected);
				// CHECKSTYLE:ON
			}

			@Override
			public void onClose(final IVetoable vetoable) {
				final QuestionResult result = Toolkit.getQuestionPane().askYesNoQuestion(
						"Close item ?",
						"Should the item be closed ?");
				if (result != QuestionResult.YES) {
					vetoable.veto();
				}
			}
		});

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		final IPopupMenu popupMenu = tabItem.createTabPopupMenu();
		popupMenu.addItem(bpf.menuItem(tabItem.getParent().toString() + tabItem.getText() + " item1"));
		popupMenu.addItem(bpf.menuItem(tabItem.getText() + " item2"));
		popupMenu.addItem(bpf.menuItem(tabItem.getText() + " item3"));
		popupMenu.addItem(bpf.menuItem(tabItem.getText() + " item4"));
		tabItem.addTabPopupDetectionListener(new IPopupDetectionListener() {
			@Override
			public void popupDetected(final Position position) {
				popupMenu.show(position);
			}
		});

		final IMenuModel popupMenu2 = new MenuModel();
		popupMenu2.addActionItem(tabItem.getParent().toString() + tabItem.getText() + " item1");
		popupMenu2.addActionItem(tabItem.getText() + " item2");
		popupMenu2.addActionItem(tabItem.getText() + " item3");
		popupMenu2.addActionItem(tabItem.getText() + " item4");

		tabItem.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "0[]0[]0[grow, 0::]0"));
		final IToolBar toolBar = tabItem.add(bpf.toolBar(), "alignx right, w 0::, wrap");
		toolBar.setModel(toolBarModel);

		tabItem.add(bpf.separator(), "growx, wrap");

		final IComposite composite = tabItem.add(bpf.scrollComposite(), "growx, growy, w 0::, h 0::");
		composite.setLayout(new MigLayoutDescriptor("[]", "[]"));
		composite.setBackgroundColor(Colors.WHITE);

		final StringBuilder text = new StringBuilder();
		for (int i = 0; i < 1; i++) {
			text.append("Content of " + tabItem.getText() + "\n");
		}
		composite.add(bpf.textLabel().setText(text.toString()), "wrap");
		composite.setPopupMenu(popupMenu2);
	}
}
