/*
 * Copyright (c) 2011, Nikolaus Moll
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

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IMainMenu;
import org.jowidgets.api.widgets.IMenuBar;
import org.jowidgets.api.widgets.IMenuItem;
import org.jowidgets.api.widgets.ITabFolder;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.blueprint.IActionMenuItemBluePrint;
import org.jowidgets.api.widgets.blueprint.ITabItemBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WidgetDisposeTest {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();
	private List<DisposeListener> disposeListeners;

	@Before
	public void before() {
		disposeListeners = new LinkedList<DisposeListener>();
	}

	@After
	public void after() {
		disposeListeners = null;
	}

	@Test
	public void testAddAndDispose() {
		Toolkit.getApplicationRunner().run(new IApplication() {

			@Override
			public void start(final IApplicationLifecycle lifecycle) {

				final IFrame frame = Toolkit.createRootFrame(BPF.frame(), lifecycle);
				frame.addDisposeListener(new DisposeListener(frame));

				frame.setVisible(true);

				final IButton button = createWidget(frame, BPF.button());
				final int sizeBefore = frame.getChildren().size();
				button.dispose();

				Assert.assertEquals(sizeBefore - 1, frame.getChildren().size());
				Assert.assertFalse(frame.getChildren().contains(button));

				frame.dispose();

				testDisposeListenerCount();
			}
		});
	}

	@Test
	public void testMultipleDisposal() {
		Toolkit.getApplicationRunner().run(new IApplication() {

			@Override
			public void start(final IApplicationLifecycle lifecycle) {

				final IFrame frame = Toolkit.createRootFrame(BPF.frame(), lifecycle);
				frame.addDisposeListener(new DisposeListener(frame));

				frame.setVisible(true);

				final IButton button = createWidget(frame, BPF.button());
				button.dispose();
				button.dispose();

				frame.dispose();

				testDisposeListenerCount();
			}
		});
	}

	@Test
	public void testChildWindowDisposal() {
		Toolkit.getApplicationRunner().run(new IApplication() {

			@Override
			public void start(final IApplicationLifecycle lifecycle) {

				final IFrame frame = Toolkit.createRootFrame(BPF.frame(), lifecycle);
				frame.addDisposeListener(new DisposeListener(frame));

				frame.setVisible(true);

				final IFrame childWindow = frame.createChildWindow(BPF.frame());
				childWindow.addDisposeListener(new DisposeListener(childWindow));

				createWidget(childWindow, BPF.button());
				createWidget(childWindow, BPF.textLabel());

				frame.dispose();

				testDisposeListenerCount();
			}
		});
	}

	@Test
	public void testMenuBarDisposal() {
		Toolkit.getApplicationRunner().run(new IApplication() {

			@Override
			public void start(final IApplicationLifecycle lifecycle) {

				final IFrame frame = Toolkit.createRootFrame(BPF.frame(), lifecycle);

				frame.setVisible(true);
				frame.addDisposeListener(new DisposeListener(frame));

				final IMenuBar menuBar = frame.createMenuBar();
				menuBar.addDisposeListener(new DisposeListener(menuBar));

				final IMainMenu menu1 = createMenu(menuBar, "Menu 1");
				final IMainMenu menu2 = createMenu(menuBar, "Menu 2");
				final IMenuItem item1 = createMenuItem(menu1, BPF.menuItem("Item 1"));

				// TODO MG,NM don't know how to create a cascaded menu without models

				menu1.dispose();
				Assert.assertTrue(item1.isDisposed());

				Assert.assertFalse(menu2.isDisposed());
				menuBar.dispose();
				Assert.assertTrue(menu2.isDisposed());

				frame.dispose();

				testDisposeListenerCount();
			}

		});
	}

	@Test
	public void testMenuModel() {
		Toolkit.getApplicationRunner().run(new IApplication() {

			@Override
			public void start(final IApplicationLifecycle lifecycle) {

				final IFrame frame = Toolkit.createRootFrame(BPF.frame(), lifecycle);

				frame.setVisible(true);
				frame.addDisposeListener(new DisposeListener(frame));

				// TODO MG,NM don't know how to add a model and dispose the items then:
				// add model several times, dispose one menu, check if other menus still work

				frame.dispose();

				testDisposeListenerCount();
			}

		});
	}

	@Test
	public void testManyWidgetsDisposal() {
		Toolkit.getApplicationRunner().run(new IApplication() {

			@Override
			public void start(final IApplicationLifecycle lifecycle) {

				final IFrame frame = Toolkit.createRootFrame(BPF.frame(), lifecycle);
				frame.addDisposeListener(new DisposeListener(frame));

				frame.setVisible(true);

				createWidget(frame, BPF.button());
				createWidget(frame, BPF.calendar());
				createWidget(frame, BPF.checkBox());
				createWidget(frame, BPF.comboBox());
				createWidget(frame, BPF.composite());
				createWidget(frame, BPF.inputFieldDate());
				createWidget(frame, BPF.label());
				createWidget(frame, BPF.scrollComposite());

				final ITabFolder tabFolder = createWidget(frame, BPF.tabFolder());
				final ITabItem tabItem1 = createTabItem(tabFolder, BPF.tabItem());
				createTabItem(tabFolder, BPF.tabItem());
				createTabItem(tabFolder, BPF.tabItem());
				createTabItem(tabFolder, BPF.tabItem());

				tabItem1.dispose();

				frame.dispose();

				testDisposeListenerCount();
			}
		});
	}

	@Test
	public void testTree() {
		Toolkit.getApplicationRunner().run(new IApplication() {

			@Override
			public void start(final IApplicationLifecycle lifecycle) {

				final IFrame frame = Toolkit.createRootFrame(BPF.frame(), lifecycle);
				frame.addDisposeListener(new DisposeListener(frame));

				frame.setVisible(true);

				final ITree tree = createWidget(frame, BPF.tree());
				final ITreeNode rootNode = createNode(tree);
				createNode(createNode(rootNode));
				createNode(createNode(rootNode));
				createNode(createNode(rootNode));

				final ITreeNode subNode1 = createNode(rootNode);
				final ITreeNode subSubNode1 = createNode(subNode1);
				final ITreeNode subSubSubNode1 = createNode(subSubNode1);

				subNode1.dispose();
				Assert.assertTrue(subSubNode1.isDisposed());
				Assert.assertTrue(subSubSubNode1.isDisposed());

				frame.dispose();

				testDisposeListenerCount();
			}
		});
	}

	@Test
	public void testTabFolder() {
		Toolkit.getApplicationRunner().run(new IApplication() {

			@Override
			public void start(final IApplicationLifecycle lifecycle) {

				final IFrame frame = Toolkit.createRootFrame(BPF.frame(), lifecycle);
				frame.addDisposeListener(new DisposeListener(frame));

				frame.setVisible(true);

				final ITabFolder tabFolder = createWidget(frame, BPF.tabFolder());
				final ITabItem tabItem1 = createTabItem(tabFolder, BPF.tabItem());
				createTabItem(tabFolder, BPF.tabItem());
				createTabItem(tabFolder, BPF.tabItem());

				final IControl control = createWidget(tabItem1, BPF.button());

				Assert.assertFalse(control.isDisposed());
				tabItem1.dispose();
				Assert.assertTrue(control.isDisposed());

				frame.dispose();

				testDisposeListenerCount();
			}
		});
	}

	private <WIDGET_TYPE extends IControl> WIDGET_TYPE createWidget(
		final IContainer container,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
		final WIDGET_TYPE result = container.add(descriptor);
		result.addDisposeListener(new DisposeListener(result));
		return result;
	}

	private ITabItem createTabItem(final ITabFolder tabFolder, final ITabItemBluePrint descriptor) {
		final ITabItem result = tabFolder.addItem(descriptor);
		result.addDisposeListener(new DisposeListener(result));
		return result;
	}

	private ITreeNode createNode(final ITree tree) {
		final ITreeNode result = tree.addNode();
		result.addDisposeListener(new DisposeListener(result));
		return result;
	}

	private ITreeNode createNode(final ITreeNode treeNode) {
		final ITreeNode result = treeNode.addNode();
		result.addDisposeListener(new DisposeListener(result));
		return result;
	}

	private IMainMenu createMenu(final IMenuBar menuBar, final String text) {
		final IMainMenu result = menuBar.addMenu(text);
		result.addDisposeListener(new DisposeListener(result));
		return result;
	}

	private IMenuItem createMenuItem(final IMainMenu menu1, final IActionMenuItemBluePrint menuItemBp) {
		final IMenuItem result = menu1.addItem(menuItemBp);
		result.addDisposeListener(new DisposeListener(result));
		return result;
	}

	private void testDisposeListenerCount() {
		for (final DisposeListener disposeListener : disposeListeners) {
			Assert.assertEquals(1, disposeListener.disposeCount);
		}
	}

	final class DisposeListener implements IDisposeListener {
		private final IWidget widget;
		private int disposeCount;

		DisposeListener(final IWidget widget) {
			disposeListeners.add(this);
			this.widget = widget;
			this.disposeCount = 0;
		}

		@Override
		public void onDispose() {
			Assert.assertFalse(widget.isDisposed());
			disposeCount++;
		}
	}
}
