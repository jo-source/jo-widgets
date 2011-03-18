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

import java.util.HashMap;
import java.util.List;

import org.jowidgets.addons.testtool.internal.ListModel;
import org.jowidgets.addons.testtool.internal.TestToolUtilities;
import org.jowidgets.addons.testtool.internal.UserAction;
import org.jowidgets.api.controler.ITabItemListener;
import org.jowidgets.api.controler.ITreeListener;
import org.jowidgets.api.controler.ITreePopupDetectionListener;
import org.jowidgets.api.controler.ITreePopupEvent;
import org.jowidgets.api.controler.ITreeSelectionEvent;
import org.jowidgets.api.controler.ITreeSelectionListener;
import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IMainMenu;
import org.jowidgets.api.widgets.ITabFolder;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.IMenuListener;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableCellEditEvent;
import org.jowidgets.common.widgets.controler.ITableCellEditorListener;
import org.jowidgets.common.widgets.controler.ITableCellEvent;
import org.jowidgets.common.widgets.controler.ITableCellListener;
import org.jowidgets.common.widgets.controler.ITableCellMouseEvent;
import org.jowidgets.common.widgets.controler.ITableColumnListener;
import org.jowidgets.common.widgets.controler.ITableColumnMouseEvent;
import org.jowidgets.common.widgets.controler.ITableColumnResizeEvent;
import org.jowidgets.common.widgets.controler.IWindowListener;
import org.jowidgets.test.api.widgets.IButtonUi;

public final class TestToolImpl implements ITestTool {

	private final TestToolUtilities testToolUtilities;
	private final HashMap<String, IWidgetCommon> widgetRegistry;
	private final ITestDataPersister persister;
	private final ListModel<TestDataObject> listModel;

	public TestToolImpl() {
		this.testToolUtilities = new TestToolUtilities();
		this.widgetRegistry = new HashMap<String, IWidgetCommon>();
		this.persister = new TestDataXmlPersister();
		this.listModel = new ListModel<TestDataObject>();
	}

	// TODO LG Remove Checkstyle off/on
	// TODO LG Add recorded user actions to table in TestToolGui
	@Override
	public void record(final IWidgetCommon widget, final UserAction action, final String id) {
		final TestDataObject obj = new TestDataObject();
		obj.setId(id);
		obj.setAction(action);
		obj.setType(widget.getUiReference().getClass().getSimpleName());
		listModel.addItem(obj);
		// CHECKSTYLE:OFF
		System.out.println("Recorded: "
			+ widget.getClass().getSimpleName()
			+ "\nwith ID : "
			+ id
			+ "\nAction was: "
			+ action.toString());
		//persister.save(testObjects, "test");
		// CHECKSTYLE:ON
	}

	// TODO LG implement
	@Override
	public void replay() {}

	@Override
	public void register(final IWidgetCommon widget) {
		addListener(widget);
	}

	//CHECKSTYLE:OFF
	private void addListener(final IWidgetCommon widget) {
		// TODO LG use IFrameUi
		if (widget instanceof IFrame) {
			final IFrame frame = (IFrame) widget;
			frame.getMenuBarModel().addListModelListener(new IListModelListener() {

				@Override
				public void childRemoved(final int index) {
					System.out.println("TT: child removed from menubar at index: " + index);
				}

				@Override
				public void childAdded(final int index) {
					System.out.println("TT: child added to menubar at index: " + index);
				}
			});
			// TODO LG dock TestToolGui to frame. But first somthing like getPosition.toDisplay must be added to Jo Widgets;)
			frame.addWindowListener(new IWindowListener() {

				@Override
				public void windowIconified() {
					System.out.println("TT: window iconified!");
				}

				@Override
				public void windowDeiconified() {
					System.out.println("TT: window deiconified!");
				}

				@Override
				public void windowDeactivated() {
					System.out.println("TT: window deactivated!");
				}

				@Override
				public void windowClosing(final IVetoable vetoable) {
					System.out.println("TT: window closing vetoable: " + vetoable.toString());
				}

				@Override
				public void windowClosed() {
					System.out.println("TT: Window closed!");
				}

				@Override
				public void windowActivated() {
					System.out.println("TT: Window activated!");
				}
			});
		}
		if (widget instanceof IButtonUi) {
			final IButtonUi button = (IButtonUi) widget;
			button.addActionListener(new IActionListener() {

				@Override
				public void actionPerformed() {
					record(widget, UserAction.CLICK, testToolUtilities.createWidgetID(button, "buttonText"));
				}
			});
		}
		// TODO LG check why the listener doesn't work on toolbar
		// TODO LG use IToolBarUi
		if (widget instanceof IToolBar) {
			final IToolBar toolBar = (IToolBar) widget;
			toolBar.getModel().addListModelListener(new IListModelListener() {

				@Override
				public void childRemoved(final int index) {
					System.out.println("TT: child removed from toolbar at index: " + index);
				}

				@Override
				public void childAdded(final int index) {
					System.out.println("TT: child added to toolbar at index: " + index);
				}
			});
		}
		// TODO LG use ITreeUi instead of ITree
		if (widget instanceof ITree) {
			final ITree tree = (ITree) widget;
			tree.addTreeSelectionListener(new ITreeSelectionListener() {

				@Override
				public void selectionChanged(final ITreeSelectionEvent event) {
					final ITreeNode node = event.getSelectedSingle();
					record(widget, UserAction.SELECT, testToolUtilities.createWidgetID(node));
				}
			});
			tree.addTreeListener(new ITreeListener() {

				@Override
				public void nodeExpanded(final ITreeNode node) {
					record(widget, UserAction.EXPAND, testToolUtilities.createWidgetID(node));
				}

				@Override
				public void nodeCollapsed(final ITreeNode node) {
					record(widget, UserAction.COLLAPS, testToolUtilities.createWidgetID(node));
				}
			});
			tree.addTreePopupDetectionListener(new ITreePopupDetectionListener() {

				@Override
				public void popupDetected(final ITreePopupEvent event) {
					final ITreeNode node = event.getNode();
					record(widget, UserAction.CLICK, testToolUtilities.createWidgetID(node));
				}
			});
		}
		// TODO LG user ITabFolderUi
		if (widget instanceof ITabFolder) {
			final ITabFolder folder = (ITabFolder) widget;
			for (final ITabItem item : folder.getItems()) {
				item.addTabItemListener(new ITabItemListener() {

					@Override
					public void selectionChanged(final boolean selected) {
						System.out.println("TT: tabItem selected");
						//record(item, UserAction.SELECT, testToolUtilities.createWidgetID(item));
					}

					@Override
					public void onClose(final IVetoable vetoable) {
						System.out.println("TT: tabItem closed");
					}
				});
			}
			folder.addPopupDetectionListener(new IPopupDetectionListener() {

				@Override
				public void popupDetected(final Position position) {
					System.out.println("TT: popup detected at: " + position);

				}
			});
		}
		if (widget instanceof IMainMenu) {
			final IMainMenu menu = (IMainMenu) widget;
			menu.addMenuListener(new IMenuListener() {

				@Override
				public void menuDeactivated() {
					System.out.println("TT: Menu deactivated!");
				}

				@Override
				public void menuActivated() {
					System.out.println("TT: Menu activated!");
				}
			});
		}
		// TODO LG user ITableUi
		if (widget instanceof ITable) {
			final ITable table = (ITable) widget;
			table.addTableCellEditorListener(new ITableCellEditorListener() {

				@Override
				public void onEdit(final IVetoable veto, final ITableCellEditEvent event) {
					System.out.println("TT: test onedit");
				}

				@Override
				public void editFinished(final ITableCellEditEvent event) {
					System.out.println("TT: test editfinished");
				}

				@Override
				public void editCanceled(final ITableCellEvent event) {
					System.out.println("TT: test editcanceled");
				}
			});
			table.addTableCellListener(new ITableCellListener() {

				@Override
				public void mouseReleased(final ITableCellMouseEvent event) {
					System.out.println("TT: test mousereleased");
				}

				@Override
				public void mousePressed(final ITableCellMouseEvent event) {
					System.out.println("TT: test mousepressed");
				}

				@Override
				public void mouseDoubleClicked(final ITableCellMouseEvent event) {
					System.out.println("TT: test mousedoubleclicked");
				}
			});
			table.addTableColumnListener(new ITableColumnListener() {

				@Override
				public void mouseClicked(final ITableColumnMouseEvent event) {
					System.out.println("TT: test mouseclicked");
				}

				@Override
				public void columnResized(final ITableColumnResizeEvent event) {
					System.out.println("TT: test resized");
				}

				@Override
				public void columnPermutationChanged() {
					System.out.println("TT: test permutation");
				}
			});
		}
	}

	// TODO LG use this method when id generation is completed
	@SuppressWarnings("unused")
	private void registerInternal(final IWidgetCommon widget, final String identifier) {
		// TODO LG use unique Widget ID, add only new and tested widgets to registry
		if (!widgetRegistry.containsKey(testToolUtilities.createWidgetID((IWidget) widget, identifier))) {
			widgetRegistry.put(testToolUtilities.createWidgetID((IWidget) widget, identifier), widget);
		}
	}

	@Override
	public void save(final List<TestDataObject> list, final String fileName) {
		persister.save(list, fileName);
	}

	@Override
	public ListModel<TestDataObject> getListModel() {
		return listModel;
	}
}
