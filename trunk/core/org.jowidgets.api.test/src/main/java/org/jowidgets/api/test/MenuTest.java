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

package org.jowidgets.api.test;

import junit.framework.JUnit4TestAdapter;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IActionMenuItem;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.IMenuItem;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ISelectableMenuItem;
import org.jowidgets.api.widgets.ISubMenu;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Position;
import org.junit.Assert;
import org.junit.Test;

public class MenuTest {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

	@Test
	public void createWidgetsTest() {
		Toolkit.getApplicationRunner().run(new IApplication() {

			@Override
			public void start(final IApplicationLifecycle lifecycle) {

				final IFrame frame = Toolkit.createRootFrame(BPF.frame(), lifecycle);

				frame.setVisible(true);
				testMenus(frame);
				frame.dispose();
			}
		});
	}

	public void testMenus(final IFrame frame) {
		final IPopupMenu popupMenu = frame.createPopupMenu();
		Assert.assertNotNull(popupMenu);

		//add a menu item
		final IActionMenuItem item1 = testMenuItem(popupMenu, popupMenu.addMenuItem(BPF.menuItem("test1")));

		//add a separator
		final IMenuItem separator1 = popupMenu.addSeparator();

		//add radio items
		final ISelectableMenuItem radio1 = testMenuItem(popupMenu, popupMenu.addMenuItem(BPF.radioMenuItem("radio1")));
		final ISelectableMenuItem radio2 = testMenuItem(popupMenu, popupMenu.addMenuItem(BPF.radioMenuItem("radio1")));
		final ISelectableMenuItem radio3 = testMenuItem(popupMenu, popupMenu.addMenuItem(BPF.radioMenuItem("radio1")));
		Assert.assertFalse(radio1.isSelected());
		Assert.assertFalse(radio2.isSelected());
		Assert.assertFalse(radio3.isSelected());
		radio1.setSelected(true);
		Assert.assertTrue(radio1.isSelected());

		//add a second separator
		final IMenuItem separator2 = popupMenu.addSeparator();

		//add checked item
		final ISelectableMenuItem checked = testMenuItem(popupMenu, popupMenu.addMenuItem(BPF.checkedMenuItem("checked")));
		Assert.assertFalse(checked.isSelected());
		checked.setSelected(true);
		Assert.assertTrue(checked.isSelected());
		checked.setSelected(false);
		Assert.assertFalse(checked.isSelected());

		//add sub menu
		final ISubMenu subMenu = popupMenu.addMenuItem(BPF.subMenu("subMenu"));
		subMenu.addMenuItem(BPF.menuItem("subSub1"));
		subMenu.addMenuItem(BPF.menuItem("subSub2"));
		Assert.assertTrue(subMenu.getChildren().size() == 2);

		//add at position
		final IActionMenuItem itemAtPos3 = testMenuItem(popupMenu, popupMenu.addMenuItem(3, BPF.menuItem("test1")));
		Assert.assertTrue(itemAtPos3 == popupMenu.getChildren().get(3));

		//show the menu
		popupMenu.show(new Position(0, 0));

		//count the items
		Assert.assertTrue(popupMenu.getChildren().size() == 9);

		//remove the items
		popupMenu.remove(item1);
		Assert.assertFalse(popupMenu.getChildren().contains(item1));
		popupMenu.remove(separator1);
		Assert.assertFalse(popupMenu.getChildren().contains(separator1));
		popupMenu.remove(radio1);
		Assert.assertFalse(popupMenu.getChildren().contains(radio1));
		popupMenu.remove(radio2);
		Assert.assertFalse(popupMenu.getChildren().contains(radio2));
		popupMenu.remove(radio3);
		Assert.assertFalse(popupMenu.getChildren().contains(radio3));
		popupMenu.remove(separator2);
		Assert.assertFalse(popupMenu.getChildren().contains(separator2));
		popupMenu.remove(checked);
		Assert.assertFalse(popupMenu.getChildren().contains(checked));
		popupMenu.remove(subMenu);
		Assert.assertFalse(popupMenu.getChildren().contains(subMenu));
		popupMenu.remove(itemAtPos3);
		Assert.assertFalse(popupMenu.getChildren().contains(itemAtPos3));

		//menu must be empty now
		Assert.assertTrue(popupMenu.getChildren().size() == 0);
	}

	public <RESULT_TYPE extends IMenuItem> RESULT_TYPE testMenuItem(final IMenu parent, final RESULT_TYPE item) {
		//test parent - child
		Assert.assertTrue(item.getParent() == parent);
		Assert.assertTrue(parent.getChildren().contains(item));

		//test setEnabled
		Assert.assertTrue(item.isEnabled());
		item.setEnabled(false);
		Assert.assertFalse(item.isEnabled());
		item.setEnabled(true);
		Assert.assertTrue(item.isEnabled());

		//test setters
		item.setText("text1");
		item.setMnemonic('e');

		return item;
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(MenuTest.class);
	}

}
