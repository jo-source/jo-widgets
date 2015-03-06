/*
 * Copyright (c) 2013, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.examples.common.snipped;

import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.layout.BorderLayout;
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.IItemModel;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.ISelectableItemModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.controller.IItemStateListener;
import org.jowidgets.tools.model.item.ActionItemModel;
import org.jowidgets.tools.model.item.CheckedItemModel;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class ItemModelSnipped implements IApplication {

	@Override
	public void start(final IApplicationLifecycle lifecycle) {

		//create a root frame
		final IFrameBluePrint frameBp = BPF.frame();
		frameBp.setSize(new Dimension(400, 300)).setTitle("Menu and Item Models");
		final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);

		//Create the menu bar
		final IMenuBarModel menuBar = frame.getMenuBarModel();

		//Use a border layout, add toolbar and composite
		frame.setLayout(BorderLayout.builder().gap(0).build());
		final IToolBarModel toolBar = frame.add(BPF.toolBar(), BorderLayout.TOP).getModel();
		final IComposite composite = frame.add(BPF.composite().setBorder(), BorderLayout.CENTER);

		//create a checked item for filter
		final CheckedItemModel filter = new CheckedItemModel("Filter", IconsSmall.FILTER);
		filter.setSelected(true);

		//create save action (with constructor)
		final ActionItemModel save = new ActionItemModel("Save", IconsSmall.DISK);
		save.setAccelerator(VirtualKey.S, Modifier.CTRL);

		//create copy action (with constructor)
		final ActionItemModel copy = new ActionItemModel("Copy", IconsSmall.COPY);
		copy.setAccelerator(VirtualKey.C, Modifier.CTRL);

		//create paste action (with builder)
		final IActionItemModel paste = ActionItemModel.builder().setText("Paste").setIcon(IconsSmall.PASTE).setAccelerator(
				VirtualKey.V,
				Modifier.CTRL).build();

		//create a menu and add items
		final MenuModel menu = new MenuModel("Menu");
		menu.addItem(save);
		menu.addItem(copy);
		menu.addItem(paste);
		menu.addSeparator();
		menu.addItem(filter);
		menu.addSeparator();

		//create a sub menu and add some items
		final IMenuModel subMenu = menu.addMenu("Sub Menu");
		final IActionItemModel action1 = subMenu.addActionItem("Action 1");
		final IActionItemModel action2 = subMenu.addActionItem("Action 2");
		final IActionItemModel action3 = subMenu.addActionItem("Action 3");

		//add the menu to the menu bar
		menuBar.addMenu(menu);

		//sets the menu as popup menu on the composite
		composite.setPopupMenu(menu);

		//add some actions and items to the toolbar
		toolBar.addItem(save);
		toolBar.addItem(copy);
		toolBar.addItem(paste);
		toolBar.addSeparator();
		toolBar.addItem(filter);
		toolBar.addSeparator();

		//add menu to the toolbar
		toolBar.addItem(menu);

		//add listeners to the items items
		filter.addItemListener(new SysoutSelectionListener(filter));
		save.addActionListener(new SysoutActionListener(save));
		copy.addActionListener(new SysoutActionListener(copy));
		paste.addActionListener(new SysoutActionListener(paste));
		action1.addActionListener(new SysoutActionListener(action1));
		action2.addActionListener(new SysoutActionListener(action2));
		action3.addActionListener(new SysoutActionListener(action3));

		//set the root frame visible
		frame.setVisible(true);
	}

	private final class SysoutActionListener implements IActionListener {

		private final IItemModel item;

		private SysoutActionListener(final IItemModel item) {
			this.item = item;
		}

		@Override
		public void actionPerformed() {
			//CHECKSTYLE:OFF
			System.out.println("Invokation on: " + item.getText());
			//CHECKSTYLE:ON
		}

	}

	private final class SysoutSelectionListener implements IItemStateListener {

		private final ISelectableItemModel item;

		private SysoutSelectionListener(final ISelectableItemModel item) {
			this.item = item;
		}

		@Override
		public void itemStateChanged() {
			//CHECKSTYLE:OFF
			System.out.println("Selected changed: " + item.isSelected() + " (" + item.getText() + ")");
			//CHECKSTYLE:ON
		}

	}
}
