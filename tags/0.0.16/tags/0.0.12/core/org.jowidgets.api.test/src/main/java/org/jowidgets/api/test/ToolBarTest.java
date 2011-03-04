/*
 * Copyright (c) 2011, Lukas Gross
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

import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.IToolBarButton;
import org.jowidgets.api.widgets.IToolBarContainerItem;
import org.jowidgets.api.widgets.IToolBarItem;
import org.jowidgets.api.widgets.IToolBarPopupButton;
import org.jowidgets.api.widgets.IToolBarToggleButton;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.junit.Test;

public class ToolBarTest {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();
	private static final String DEFAULT_ITEM_TEXT = "test";
	private static final String DEFAULT_ITEM_TOOLTIP_TEXT = "tooltipp";

	// TODO make more tests!
	@Test
	public void createToolBarTest() {
		Toolkit.getApplicationRunner().run(new IApplication() {

			@Override
			public void start(final IApplicationLifecycle lifecycle) {
				final IFrame frame = Toolkit.createRootFrame(BPF.frame(), lifecycle);
				frame.setVisible(true);

				final IToolBar toolBar = frame.add(BPF.toolBar(), "");

				// test toolBarButton
				IToolBarButton toolBarButton = toolBar.addItem(BPF.toolBarButton());
				Assert.assertTrue(toolBar.getChildren().contains(toolBarButton));
				Assert.assertTrue(toolBar.getChildren().size() == 1);
				testToolBarItem(toolBarButton);

				IToolBarToggleButton toolBarToggleButton = toolBar.addItem(BPF.toolBarToggleButton());
				Assert.assertTrue(toolBar.getChildren().contains(toolBarToggleButton));
				Assert.assertTrue(toolBar.getChildren().size() == 2);
				testToolBarItem(toolBarToggleButton);
				// test toggleButton selected/deselected
				toolBarToggleButton.setSelected(true);
				Assert.assertTrue(toolBarToggleButton.isSelected());
				toolBarToggleButton.setSelected(false);
				Assert.assertTrue(!toolBarToggleButton.isSelected());

				IToolBarPopupButton toolBarPopupButton = toolBar.addItem(BPF.toolBarPopupButton());
				Assert.assertTrue(toolBar.getChildren().contains(toolBarPopupButton));
				Assert.assertTrue(toolBar.getChildren().size() == 3);
				testToolBarItem(toolBarPopupButton);

				IToolBarContainerItem toolBarContainerItem = toolBar.addItem(BPF.toolBarContainerItem());
				Assert.assertTrue(toolBar.getChildren().contains(toolBarContainerItem));
				Assert.assertTrue(toolBar.getChildren().size() == 4);

				IToolBarItem separator = toolBar.addSeparator();
				Assert.assertTrue(toolBar.getChildren().contains(separator));
				Assert.assertTrue(toolBar.getChildren().size() == 5);
				testToolBarItem(separator);

				// remove items from toolBar
				testRemoveToolBarItem(toolBar, toolBarButton);
				testRemoveToolBarItem(toolBar, toolBarPopupButton);
				testRemoveToolBarItem(toolBar, toolBarToggleButton);
				testRemoveToolBarItem(toolBar, toolBarContainerItem);
				testRemoveToolBarItem(toolBar, separator);

				// add items at specific positions
				toolBarButton = toolBar.addItem(0, BPF.toolBarButton());
				toolBarContainerItem = toolBar.addItem(1, BPF.toolBarContainerItem());
				toolBarPopupButton = toolBar.addItem(2, BPF.toolBarPopupButton());
				toolBarToggleButton = toolBar.addItem(3, BPF.toolBarToggleButton());
				separator = toolBar.addItem(4, BPF.toolBarSeparator());

				frame.dispose();
			}
		});
	}

	private <T extends IToolBarItem> void testToolBarItem(final T item) {
		item.setText(DEFAULT_ITEM_TEXT);
		item.setToolTipText(DEFAULT_ITEM_TOOLTIP_TEXT);

		item.setEnabled(true);
		Assert.assertTrue(item.isEnabled());

		item.setEnabled(false);
		Assert.assertTrue(!item.isEnabled());
	}

	private <T extends IToolBarItem> void testRemoveToolBarItem(final IToolBar parent, final T item) {
		final int oldSize = parent.getChildren().size();
		parent.remove(item);
		Assert.assertFalse(parent.getChildren().contains(item));
		Assert.assertTrue((parent.getChildren().size() + 1) == oldSize);
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(ToolBarTest.class);
	}
}
