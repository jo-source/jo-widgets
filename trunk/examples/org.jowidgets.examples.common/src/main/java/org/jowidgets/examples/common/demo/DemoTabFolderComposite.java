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

import org.jowidgets.api.controler.ITabItemListener;
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.types.QuestionResult;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.ISplitComposite;
import org.jowidgets.api.widgets.ITabFolder;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.blueprint.ITabItemBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;

public final class DemoTabFolderComposite {

	protected DemoTabFolderComposite(final IContainer parentContainer) {

		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();

		final ILayoutDescriptor fillLayoutDescriptor = new MigLayoutDescriptor("0[grow, 0::]0", "0[grow, 0::]0");
		parentContainer.setLayout(fillLayoutDescriptor);

		final ISplitComposite split = parentContainer.add(
				bpF.splitHorizontal().setFirstBorder(null).setSecondBorder(null),
				"growx, growy");

		final ITabFolder folder1 = addFolder(split.getFirst());
		final ITabFolder folder2 = addFolder(split.getSecond());

		final ITabItem item = folder1.getItem(0);
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
	}

	private ITabFolder addFolder(final IContainer parentContainer) {
		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();

		final ILayoutDescriptor fillLayoutDescriptor = new MigLayoutDescriptor("0[grow, 0::]0", "0[grow, 0::]0");
		parentContainer.setLayout(fillLayoutDescriptor);

		final ITabFolder tabFolder = parentContainer.add(bpF.tabFolder().setTabsCloseable(true), "growx, growy, w 0::, h 0::");

		ITabItemBluePrint tabItemBp = bpF.tabItem();
		tabItemBp.setText("Tab1").setToolTipText("Tooltip of tab1").setIcon(IconsSmall.INFO);
		final ITabItem tabItem1 = tabFolder.addItem(tabItemBp);
		addTabItemListener(tabItem1);
		tabItem1.add(bpF.textLabel("Tab content1"), "");

		tabItemBp = bpF.tabItem();
		tabItemBp.setText("Tab2").setToolTipText("Tooltip of tab2").setIcon(IconsSmall.QUESTION);
		final ITabItem tabItem2 = tabFolder.addItem(tabItemBp);
		addTabItemListener(tabItem2);
		tabItem2.add(bpF.textLabel("Tab content2"), "");

		tabItemBp = bpF.tabItem();
		tabItemBp.setText("Tab3").setToolTipText("Tooltip of tab3").setIcon(IconsSmall.WARNING);
		final ITabItem tabItem3 = tabFolder.addItem(tabItemBp);
		addTabItemListener(tabItem3);
		tabItem3.add(bpF.textLabel("Tab content3"), "");

		tabFolder.changeItemIndex(tabItem1, 2);

		tabFolder.setSelectedItem(tabItem1);

		return tabFolder;
	}

	private void addTabItemListener(final ITabItem tabItem) {
		tabItem.addTabItemListener(new ITabItemListener() {

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
	}
}
