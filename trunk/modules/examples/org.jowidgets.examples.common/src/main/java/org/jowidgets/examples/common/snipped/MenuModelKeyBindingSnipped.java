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

import org.jowidgets.api.layout.FillLayout;
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.IItemModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.model.item.MenuModelKeyBinding;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class MenuModelKeyBindingSnipped implements IApplication {

	@Override
	public void start(final IApplicationLifecycle lifecycle) {

		//create a root frame
		final IFrameBluePrint frameBp = BPF.frame();
		frameBp.setSize(new Dimension(400, 300)).setTitle("Menu model key binding");
		final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);
		frame.setLayout(FillLayout.get());

		//create a popup menu with some actions
		final MenuModel popup = new MenuModel();

		final IActionItemModel action1 = popup.addActionItem("Action1");
		action1.setAccelerator(VirtualKey.DIGIT_1, Modifier.CTRL);

		final IActionItemModel action2 = popup.addActionItem("Action2");
		action2.setAccelerator(VirtualKey.DIGIT_2, Modifier.CTRL);

		final IActionItemModel action3 = popup.addActionItem("Action3");
		action3.setAccelerator(VirtualKey.DIGIT_3, Modifier.CTRL);

		//set the popup menu for the frame
		frame.setPopupMenu(popup);

		//do the key binding to the frame (recursive)
		MenuModelKeyBinding.bind(popup, frame);

		//set the root frame visible
		frame.setVisible(true);

		//add some actions after binding to show that they are bound too
		final IMenuModel submenu = popup.addMenu("Submenu");

		final IActionItemModel action4 = submenu.addActionItem("Action4");
		action4.setAccelerator(VirtualKey.DIGIT_4, Modifier.CTRL);

		final IActionItemModel action5 = submenu.addActionItem("Action5");
		action5.setAccelerator(VirtualKey.DIGIT_5, Modifier.CTRL);

		//add listeners to the items
		action1.addActionListener(new SysoutActionListener(action1));
		action2.addActionListener(new SysoutActionListener(action2));
		action3.addActionListener(new SysoutActionListener(action3));
		action4.addActionListener(new SysoutActionListener(action4));
		action5.addActionListener(new SysoutActionListener(action5));

	}

	private final class SysoutActionListener implements IActionListener {

		private final IItemModel item;

		private SysoutActionListener(final IItemModel item) {
			this.item = item;
		}

		@Override
		public void actionPerformed() {
			//CHECKSTYLE:OFF
			System.out.println("Invocation on: " + item.getText());
			//CHECKSTYLE:ON
		}

	}

}
