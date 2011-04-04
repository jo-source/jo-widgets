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

package org.jowidgets.addons.testtool;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.addons.testtool.internal.TestDataListModel;
import org.jowidgets.addons.testtool.internal.TestDataObject;
import org.jowidgets.addons.testtool.internal.TestDataXmlPersister;
import org.jowidgets.addons.testtool.internal.TestPlayer;
import org.jowidgets.addons.testtool.internal.TestToolUtilities;
import org.jowidgets.addons.testtool.internal.UserAction;
import org.jowidgets.addons.testtool.internal.WidgetRegistry;
import org.jowidgets.api.controler.ITreeListener;
import org.jowidgets.api.controler.ITreePopupDetectionListener;
import org.jowidgets.api.controler.ITreePopupEvent;
import org.jowidgets.api.controler.ITreeSelectionEvent;
import org.jowidgets.api.controler.ITreeSelectionListener;
import org.jowidgets.api.model.IListItemListener;
import org.jowidgets.api.model.IListItemObservable;
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IMenuItemModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.ISeparatorItemModel;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITabFolder;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.IToolBarButton;
import org.jowidgets.api.widgets.IToolBarMenu;
import org.jowidgets.api.widgets.IToolBarPopupButton;
import org.jowidgets.api.widgets.IToolBarToggleButton;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.IItemStateListener;
import org.jowidgets.common.widgets.controler.IMenuListener;
import org.jowidgets.test.api.widgets.IButtonUi;
import org.jowidgets.tools.controler.TabItemAdapter;
import org.jowidgets.tools.controler.WindowAdapter;

public final class TestToolImpl implements ITestTool {

	private final TestToolUtilities testToolUtilities;
	private final ITestDataPersister persister;
	private final TestDataListModel listModel;
	private final TestPlayer player;
	private boolean record;
	private boolean replay;
	private final List<ITree> trees;
	private final List<ITabFolder> tabFolders;

	public TestToolImpl() {
		this("");
	}

	public TestToolImpl(final String filePath) {
		this.persister = new TestDataXmlPersister(filePath);
		this.testToolUtilities = new TestToolUtilities();
		this.listModel = new TestDataListModel();
		this.player = new TestPlayer();
		this.record = false;
		this.replay = false;
		this.trees = new LinkedList<ITree>();
		this.tabFolders = new LinkedList<ITabFolder>();
	}

	@Override
	public void record(final IWidgetCommon widget, final UserAction action, final String id) {
		if (record) {
			final TestDataObject obj = new TestDataObject();
			obj.setId(id);
			obj.setAction(action);
			obj.setType(widget.getUiReference().getClass().getSimpleName());
			listModel.addItem(obj);
		}
	}

	@Override
	public void register(final IWidgetCommon widget) {
		// TODO LG remove disposed widgets
		WidgetRegistry.getInstance().addWidget(widget);
		addListener(widget);
	}

	private void addListener(final IWidgetCommon widget) {
		// TODO LG use IFrameUi
		if (widget instanceof IFrame) {
			final IFrame frame = (IFrame) widget;
			frame.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosed() {
					record(frame, UserAction.CLOSE, testToolUtilities.createWidgetID(frame));
				}

				@Override
				public void windowActivated() {
					registerTreeItems();
					registerMenuItems(frame);
					registerTabFolderItems();
				}
			});
		}
		if (widget instanceof IButtonUi) {
			final IButtonUi button = (IButtonUi) widget;
			button.addActionListener(new IActionListener() {

				@Override
				public void actionPerformed() {
					record(widget, UserAction.CLICK, testToolUtilities.createWidgetID(button));
				}
			});
		}
		// TODO LG use IToolBarUi
		if (widget instanceof IToolBar) {
			final IListItemObservable toolBarObs = (IListItemObservable) widget;
			toolBarObs.addItemContainerListener(new IListItemListener() {

				@Override
				public void itemAdded(final IWidget item) {
					WidgetRegistry.getInstance().addWidget(item);
					if (item instanceof IToolBarButton) {
						final IToolBarButton button = (IToolBarButton) item;
						button.addActionListener(new IActionListener() {
							@Override
							public void actionPerformed() {
								record(button, UserAction.CLICK, testToolUtilities.createWidgetID(button));
							}
						});
					}
					else if (item instanceof IToolBarToggleButton) {
						final IToolBarToggleButton button = (IToolBarToggleButton) item;
						button.addItemListener(new IItemStateListener() {

							@Override
							public void itemStateChanged() {
								record(button, UserAction.CLICK, testToolUtilities.createWidgetID(button));
							}
						});
					}
					else if (item instanceof IToolBarPopupButton) {
						final IToolBarPopupButton button = (IToolBarPopupButton) item;
						button.addActionListener(new IActionListener() {
							@Override
							public void actionPerformed() {
								record(button, UserAction.CLICK, testToolUtilities.createWidgetID(button));
							}
						});
					}
					else if (item instanceof IToolBarMenu) {
						final IToolBarMenu menu = (IToolBarMenu) item;
						final IPopupMenu popupMenu = menu.getPopupMenu();
						WidgetRegistry.getInstance().addWidget(popupMenu);
						popupMenu.addMenuListener(new IMenuListener() {

							@Override
							public void menuDeactivated() {
								record(popupMenu, UserAction.CLICK, testToolUtilities.createWidgetID(popupMenu));
							}

							@Override
							public void menuActivated() {
								record(popupMenu, UserAction.CLICK, testToolUtilities.createWidgetID(popupMenu));
							}
						});
					}
				}

				@Override
				public void itemRemoved(final IWidget item) {
					WidgetRegistry.getInstance().removeWidget(item);
				}
			});
		}
		// TODO LG use ITreeUi instead of ITree
		if (widget instanceof ITree) {
			final ITree tree = (ITree) widget;
			trees.add(tree);
			tree.addTreeSelectionListener(new ITreeSelectionListener() {

				@Override
				public void selectionChanged(final ITreeSelectionEvent event) {
					for (final ITreeNode node : event.getSelected()) {
						if (!WidgetRegistry.getInstance().getWidgets().contains(node)) {
							WidgetRegistry.getInstance().addWidget(node);
						}
						record(widget, UserAction.SELECT, testToolUtilities.createWidgetID(node));
					}
				}
			});
			tree.addTreeListener(new ITreeListener() {

				@Override
				public void nodeExpanded(final ITreeNode node) {
					if (!WidgetRegistry.getInstance().getWidgets().contains(node)) {
						WidgetRegistry.getInstance().addWidget(node);
					}
					record(widget, UserAction.EXPAND, testToolUtilities.createWidgetID(node));
				}

				@Override
				public void nodeCollapsed(final ITreeNode node) {
					if (!WidgetRegistry.getInstance().getWidgets().contains(node)) {
						WidgetRegistry.getInstance().addWidget(node);
					}
					record(widget, UserAction.COLLAPSE, testToolUtilities.createWidgetID(node));
				}
			});
			tree.addTreePopupDetectionListener(new ITreePopupDetectionListener() {

				@Override
				public void popupDetected(final ITreePopupEvent event) {
					final ITreeNode node = event.getNode();
					if (!WidgetRegistry.getInstance().getWidgets().contains(node)) {
						WidgetRegistry.getInstance().addWidget(node);
					}
					record(widget, UserAction.CLICK, testToolUtilities.createWidgetID(node));
				}
			});
		}
		// TODO LG user ITabFolderUi
		if (widget instanceof ITabFolder) {
			final ITabFolder tab = (ITabFolder) widget;
			tabFolders.add(tab);
			// TODO LG support recording/replay of TabFolder
		}
		// TODO LG use ITableUi
		// TODO LG support recording/replay of table
	}

	@Override
	public void save(final List<TestDataObject> list, final String fileName) {
		persister.save(list, fileName);
	}

	@Override
	public List<TestDataObject> load(final String fileName) {
		return persister.load(fileName);
	}

	@Override
	public TestDataListModel getListModel() {
		return listModel;
	}

	@Override
	public void activateRecordMode() {
		this.record = true;
		this.replay = false;
	}

	@Override
	public void activateReplayMode() {
		this.record = false;
		this.replay = true;
	}

	@Override
	public void deactivateReplayAndRecord() {
		this.record = false;
		this.replay = false;
	}

	@Override
	public void replay(final List<TestDataObject> list, final int delay) {
		if (replay) {
			player.replayTest(list, delay);
		}
	}

	private void registerTreeItems() {
		for (final ITree tree : trees) {
			for (final ITreeNode node : tree.getChildren()) {
				WidgetRegistry.getInstance().addWidget(node);
			}
		}
	}

	private void registerTabFolderItems() {
		for (final ITabFolder folder : tabFolders) {
			for (final ITabItem item : folder.getItems()) {
				WidgetRegistry.getInstance().addWidget(item);
				item.addTabItemListener(new TabItemAdapter() {

					@Override
					public void selectionChanged(final boolean selected) {
						if (item.isVisible()) {
							record(item, UserAction.CLICK, testToolUtilities.createWidgetID(item));
						}
					}

					@Override
					public void onClose(final IVetoable vetoable) {}
				});
			}
		}
	}

	private void registerMenuItems(final IFrame frame) {
		final IMenuBarModel menuBarModel = frame.getMenuBarModel();
		for (final IMenuModel menu : menuBarModel.getMenus()) {
			for (final IMenuItemModel item : menu.getChildren()) {
				if (!(item instanceof ISeparatorItemModel)) {
					final IActionItemModel actionItem = (IActionItemModel) item;
					actionItem.addActionListener(new IActionListener() {

						@Override
						public void actionPerformed() {}
					});
				}
			}
		}
	}
}
