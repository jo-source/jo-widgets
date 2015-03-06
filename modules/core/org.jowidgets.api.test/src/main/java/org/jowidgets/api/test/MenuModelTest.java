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

import org.jowidgets.api.model.item.IItemModel;
import org.jowidgets.api.model.item.IMenuItemModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IMainMenu;
import org.jowidgets.api.widgets.IMenuBar;
import org.jowidgets.api.widgets.IMenuItem;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.tools.model.item.ActionItemModel;
import org.jowidgets.tools.model.item.CheckedItemModel;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.model.item.RadioItemModel;
import org.jowidgets.tools.model.item.SeparatorItemModel;
import org.junit.Assert;
import org.junit.Test;

public class MenuModelTest {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

	@Test
	public void testMenuModelAddRemove() {
		testAddAndRemoveMenuItem(new MenuModel());
		testAddAndRemoveMenuItem(new SeparatorItemModel());
		testAddAndRemoveMenuItem(new CheckedItemModel());
		testAddAndRemoveMenuItem(new RadioItemModel());
		testAddAndRemoveMenuItem(new ActionItemModel());
	}

	private void testAddAndRemoveMenuItem(final IMenuItemModel childModel) {
		final MenuModel model = new MenuModel();

		model.addItem(childModel);
		Assert.assertTrue(model.getChildren().contains(childModel));

		final IMenuItemModel childModelOfList = model.getChildren().iterator().next();
		Assert.assertTrue(childModelOfList.equals(childModel));
		Assert.assertTrue(childModelOfList == childModel);

		model.removeItem(childModel);
		Assert.assertFalse(model.getChildren().contains(childModel));
	}

	@Test
	public void testMenuModelRejectIdDoubles() {

		final MenuModel model = new MenuModel();

		final String id = "ID_OF_ITEM";

		final SeparatorItemModel separator = new SeparatorItemModel(id);

		model.addItem(separator);

		final IMenuItemModel foundItem = model.findItemByPath(id);
		Assert.assertEquals(separator, foundItem);

		int exceptionCount = 0;

		try {
			model.addItem(separator);
		}
		catch (final IllegalArgumentException e) {
			exceptionCount++;
		}
		Assert.assertEquals(exceptionCount, 1);

		try {
			model.addItem(new SeparatorItemModel(id));
		}
		catch (final IllegalArgumentException e) {
			exceptionCount++;
		}
		Assert.assertEquals(exceptionCount, 2);

		try {
			model.addItem(MenuModel.builder().setId(id));
		}
		catch (final IllegalArgumentException e) {
			exceptionCount++;
		}
		Assert.assertEquals(exceptionCount, 3);

		try {
			model.addItem(CheckedItemModel.builder().setId(id));
		}
		catch (final IllegalArgumentException e) {
			exceptionCount++;
		}
		Assert.assertEquals(exceptionCount, 4);

		try {
			model.addItem(RadioItemModel.builder().setId(id));
		}
		catch (final IllegalArgumentException e) {
			exceptionCount++;
		}
		Assert.assertEquals(exceptionCount, 5);

		try {
			model.addItem(ActionItemModel.builder().setId(id));
		}
		catch (final IllegalArgumentException e) {
			exceptionCount++;
		}
		Assert.assertEquals(exceptionCount, 6);
	}

	@Test
	public void testMenuItemVisibility() {
		Toolkit.getApplicationRunner().run(new IApplication() {

			@Override
			public void start(final IApplicationLifecycle lifecycle) {
				final IFrame frame = Toolkit.createRootFrame(BPF.frame(), lifecycle);
				frame.setVisible(true);

				final IMenuBar menuBar = frame.createMenuBar();

				final IMainMenu menu = menuBar.addMenu("menu");
				final IMenuModel menuModel = menu.getModel();

				testMenuItemVisibility(menu, menuModel);

				frame.dispose();
			}
		});
	}

	private void testMenuItemVisibility(final IMainMenu menu, final IMenuModel menuModel) {
		final int iterationCount = 5;
		final IMenuItem[] menuItems = new IMenuItem[iterationCount];
		//create actions and test model and menu size
		for (int i = 0; i < iterationCount; i++) {
			Assert.assertEquals(i, menu.getChildren().size());
			Assert.assertEquals(i, menuModel.getChildren().size());

			menuModel.addActionItem("TestAction" + i);
			menuItems[i] = menu.getChildren().get(i);

			Assert.assertEquals(i + 1, menu.getChildren().size());
			Assert.assertEquals(i + 1, menuModel.getChildren().size());
		}

		//hide actions and test model and menu size
		int i = iterationCount;
		for (final IItemModel itemModel : menuModel.getChildren()) {
			Assert.assertEquals(i, menu.getChildren().size());
			Assert.assertEquals(iterationCount, menuModel.getChildren().size());

			itemModel.setVisible(false);

			Assert.assertEquals(i - 1, menu.getChildren().size());
			Assert.assertEquals(iterationCount, menuModel.getChildren().size());

			i--;
		}

		//unhide actions and test model and menu size
		for (final IItemModel itemModel : menuModel.getChildren()) {
			Assert.assertEquals(i, menu.getChildren().size());
			Assert.assertEquals(iterationCount, menuModel.getChildren().size());

			itemModel.setVisible(true);

			Assert.assertEquals(menuItems[i].getText(), menu.getChildren().get(i).getText());

			Assert.assertEquals(i + 1, menu.getChildren().size());
			Assert.assertEquals(iterationCount, menuModel.getChildren().size());

			i++;
		}

		//remove all items
		menuModel.removeAllItems();
		Assert.assertEquals(0, menu.getChildren().size());
		Assert.assertEquals(0, menuModel.getChildren().size());

	}

}
