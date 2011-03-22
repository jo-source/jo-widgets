/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.examples.common.workbench.demo1;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.table.IDefaultTableColumnBuilder;
import org.jowidgets.api.model.table.IDefaultTableColumnModel;
import org.jowidgets.api.model.table.ITableCellBuilder;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.types.TableColumnPackPolicy;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.ITableCellEditEvent;
import org.jowidgets.common.widgets.controler.ITableCellEditorListener;
import org.jowidgets.common.widgets.controler.ITableCellEvent;
import org.jowidgets.common.widgets.controler.ITableCellListener;
import org.jowidgets.common.widgets.controler.ITableCellMouseEvent;
import org.jowidgets.common.widgets.controler.ITableCellPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableCellPopupEvent;
import org.jowidgets.common.widgets.controler.ITableColumnListener;
import org.jowidgets.common.widgets.controler.ITableColumnMouseEvent;
import org.jowidgets.common.widgets.controler.ITableColumnPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableColumnPopupEvent;
import org.jowidgets.common.widgets.controler.ITableColumnResizeEvent;
import org.jowidgets.common.widgets.controler.ITableSelectionListener;
import org.jowidgets.examples.common.demo.DemoMenuProvider;
import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.examples.common.workbench.base.AbstractView;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.model.table.AbstractTableDataModel;
import org.jowidgets.tools.model.table.DefaultTableColumnBuilder;
import org.jowidgets.tools.model.table.DefaultTableColumnModel;
import org.jowidgets.tools.model.table.TableCellBuilder;
import org.jowidgets.util.ValueHolder;
import org.jowidgets.workbench.api.IComponentTreeNodeContext;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;

public class ViewDemo6 extends AbstractView implements IView {

	public static final String ID = ViewDemo6.class.getName();
	public static final String DEFAULT_LABEL = "View6";
	public static final String DEFAULT_TOOLTIP = "View6 tooltip";
	public static final IImageConstant DEFAULT_ICON = SilkIcons.ROSETTE;

	public ViewDemo6(final IViewContext context, final DemoMenuProvider menuProvider) {
		super(ID);
		context.getToolBar().addItemsOfModel(menuProvider.getToolBarModel());
		context.getToolBarMenu().addItemsOfModel(menuProvider.getMenuModel());

		final ActionFactory actionFactory = new ActionFactory();
		final IComponentTreeNodeContext treeNodeContent = context.getComponentTreeNodeContext();
		treeNodeContent.getPopupMenu().addSeparator();
		treeNodeContent.getPopupMenu().addAction(actionFactory.createActivateViewAction(context, DEFAULT_LABEL));
		treeNodeContent.getPopupMenu().addAction(actionFactory.createUnHideViewAction(context, DEFAULT_LABEL));
		context.getToolBarMenu().addSeparator();
		context.getToolBarMenu().addAction(actionFactory.createHideViewAction(context));

		createContent(context.getContainer());
	}

	private void createContent(final IContainer container) {

		container.setLayout(MigLayoutFactory.growingInnerCellLayout());
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		final int rowCount = 20000;
		final int columnCount = 50;

		final IDefaultTableColumnModel columnModel = new DefaultTableColumnModel(columnCount);
		for (int columnIndex = 0; columnIndex < columnModel.getColumnCount(); columnIndex++) {
			final IDefaultTableColumnBuilder columnBuilder = new DefaultTableColumnBuilder();
			columnBuilder.setText("Column " + columnIndex);
			columnBuilder.setToolTipText("Tooltip of column " + columnIndex);
			columnBuilder.setWidth(100);
			columnModel.setColumn(columnIndex, columnBuilder);
		}

		final ITableDataModel dataModel = new AbstractTableDataModel() {

			@Override
			public int getRowCount() {
				return rowCount;
			}

			@Override
			public ITableCell getCell(final int rowIndex, final int columnIndex) {
				final ITableCellBuilder cellBuilder = new TableCellBuilder();
				cellBuilder.setText("Cell (" + rowIndex + " / " + columnIndex + ")");
				if (rowIndex % 2 == 0) {
					cellBuilder.setBackgroundColor(Colors.DEFAULT_TABLE_EVEN_BACKGROUND_COLOR);
				}
				return cellBuilder.build();
			}
		};

		final ITable table = container.add(bpf.table(columnModel, dataModel), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		table.addTableSelectionListener(new ITableSelectionListener() {
			@Override
			public void selectionChanged() {
				//CHECKSTYLE:OFF
				System.out.println("New selection: " + table.getSelection());
				//CHECKSTYLE:ON
			}
		});

		table.addTableCellListener(new ITableCellListener() {

			@Override
			public void mouseReleased(final ITableCellMouseEvent event) {
				//CHECKSTYLE:OFF
				System.out.println("mouseReleased: " + event);
				//CHECKSTYLE:ON
			}

			@Override
			public void mousePressed(final ITableCellMouseEvent event) {
				//CHECKSTYLE:OFF
				System.out.println("mousePressed: " + event);
				//CHECKSTYLE:ON
			}

			@Override
			public void mouseDoubleClicked(final ITableCellMouseEvent event) {
				//CHECKSTYLE:OFF
				System.out.println("mouseDoubleClicked: " + event);
				//CHECKSTYLE:ON
			}
		});

		table.addTableColumnListener(new ITableColumnListener() {

			@Override
			public void mouseClicked(final ITableColumnMouseEvent event) {
				//CHECKSTYLE:OFF
				System.out.println("mouseClicked: " + event);
				//CHECKSTYLE:ON
			}

			@Override
			public void columnResized(final ITableColumnResizeEvent event) {
				//CHECKSTYLE:OFF
				System.out.println("columnResized: " + event);
				//CHECKSTYLE:ON
			}

			@Override
			public void columnPermutationChanged() {
				//CHECKSTYLE:OFF
				System.out.println("columnPermutationChanged: " + table.getColumnPermutation());
				//CHECKSTYLE:ON
			}
		});

		table.addTableCellEditorListener(new ITableCellEditorListener() {

			@Override
			public void onEdit(final IVetoable veto, final ITableCellEditEvent event) {
				//CHECKSTYLE:OFF
				System.out.println("onEdit: " + event);
				//CHECKSTYLE:ON
			}

			@Override
			public void editFinished(final ITableCellEditEvent event) {
				//CHECKSTYLE:OFF
				System.out.println("editFinished: " + event);
				//CHECKSTYLE:ON
			}

			@Override
			public void editCanceled(final ITableCellEvent event) {
				//CHECKSTYLE:OFF
				System.out.println("editCanceled: " + event);
				//CHECKSTYLE:ON
			}
		});

		final ValueHolder<Integer> selectedColumn = new ValueHolder<Integer>();
		final ValueHolder<Integer> selectedRow = new ValueHolder<Integer>();

		final IPopupMenu popupMenu = table.createPopupMenu();
		final IMenuModel popupMenuModel = popupMenu.getModel();
		final IActionItemModel item1 = popupMenuModel.addActionItem();
		final IActionItemModel reloadAction = popupMenuModel.addActionItem("Reload", SilkIcons.ARROW_REFRESH_SMALL);
		final IActionItemModel packTableAction = popupMenuModel.addActionItem("Fit all columns", SilkIcons.ARROW_INOUT);
		final IActionItemModel packColumnAction = popupMenuModel.addActionItem("Fit column", SilkIcons.ARROW_INOUT);

		reloadAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				table.setCursor(Cursor.WAIT);
				table.resetFromModel();
				table.setCursor(Cursor.DEFAULT);
			}
		});

		packTableAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				table.setCursor(Cursor.WAIT);
				table.pack(TableColumnPackPolicy.HEADER_AND_CONTENT);
				table.setCursor(Cursor.DEFAULT);
			}
		});

		packColumnAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				table.setCursor(Cursor.WAIT);
				table.pack(selectedColumn.get().intValue(), TableColumnPackPolicy.HEADER_AND_CONTENT);
				table.setCursor(Cursor.DEFAULT);
			}
		});

		table.addTableCellPopupDetectionListener(new ITableCellPopupDetectionListener() {
			@Override
			public void popupDetected(final ITableCellPopupEvent event) {
				selectedColumn.set(Integer.valueOf(event.getColumnIndex()));
				selectedRow.set(Integer.valueOf(event.getRowIndex()));
				item1.setText("Item1 (" + event.getRowIndex() + " / " + event.getColumnIndex() + ")");
				popupMenu.show(event.getPosition());
			}
		});

		table.addTableColumnPopupDetectionListener(new ITableColumnPopupDetectionListener() {
			@Override
			public void popupDetected(final ITableColumnPopupEvent event) {
				selectedColumn.set(Integer.valueOf(event.getColumnIndex()));
				item1.setText("Item1 (" + event.getColumnIndex() + ")");
				popupMenu.show(event.getPosition());
			}
		});
	}

}
