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
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITabFolder;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.junit.Test;

public class TabFolderTest {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

	@Test
	public void createTabFolderTest() {
		Toolkit.getApplicationRunner().run(new IApplication() {

			@Override
			public void start(final IApplicationLifecycle lifecycle) {
				final IFrame rootFrame = Toolkit.createRootFrame(BPF.frame(), lifecycle);
				final ITabFolder tabFolder = rootFrame.add(BPF.tabFolder().setTabsCloseable(true), "");
				final ITabItem item1 = tabFolder.addItem(BPF.tabItem());
				final ITabItem item2 = tabFolder.addItem(BPF.tabItem());

				tabFolder.setSelectedItem(0);
				Assert.assertEquals(0, tabFolder.getSelectedIndex());
				tabFolder.setSelectedItem(1);
				Assert.assertEquals(1, tabFolder.getSelectedIndex());

				final IPopupMenu tabPopupMenu = item1.createTabPopupMenu();
				tabPopupMenu.addSeparator();
				tabPopupMenu.addItem(BPF.menuItem("test"));

				final IPopupMenu popupMenu = item1.createPopupMenu();
				popupMenu.addItem(BPF.menuItem());
				popupMenu.addSeparator();
				popupMenu.addItem(BPF.menuItem());

				final IToolBar toolBar = item2.add(BPF.toolBar(), "");
				toolBar.addItem(BPF.toolBarButton());
				toolBar.addItem(BPF.toolBarSeparator());
				toolBar.addItem(BPF.toolBarPopupButton());

				tabFolder.detachItem(item1);
				tabFolder.attachItem(item1);

				rootFrame.dispose();
			}
		});
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(TabFolderTest.class);
	}
}
