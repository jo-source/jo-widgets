/*
 * Copyright (c) 2010, Nikolaus Moll
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

import java.util.ArrayList;
import java.util.List;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.model.table.ITableColumn;
import org.jowidgets.api.model.table.ITableModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.model.ITableDataModelObservable;
import org.jowidgets.common.widgets.controler.ITableColumnPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableColumnPopupEvent;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controler.TableColumnModelObservable;
import org.jowidgets.tools.controler.TableDataModelObservable;
import org.jowidgets.tools.model.table.DefaultTableColumn;
import org.jowidgets.tools.model.table.TableCell;

public final class DemoTableComposite {

	private final TableModel tableModel;
	private final ITable table;

	private int popupColumn;

	public DemoTableComposite(final IContainer parentContainer) {
		tableModel = new TableModel();
		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();

		final ILayoutDescriptor fillLayoutDescriptor = new MigLayoutDescriptor("0[grow, 0::]0", "0[grow, 0::]0");
		parentContainer.setLayout(fillLayoutDescriptor);

		table = parentContainer.add(bpF.table(tableModel), "growx, growy, w 0::, h 0::");

		final IPopupMenu columnPopupMenu = table.createPopupMenu();
		columnPopupMenu.addAction(createHideColumnAction());
		columnPopupMenu.addAction(createUnhideAllColumnAction());
		columnPopupMenu.addSeparator();
		columnPopupMenu.addAction(createNewColumnAction());
		columnPopupMenu.addAction(createRemoveColumnAction());

		table.addTableColumnPopupDetectionListener(new ITableColumnPopupDetectionListener() {

			@Override
			public void popupDetected(final ITableColumnPopupEvent event) {
				popupColumn = event.getColumnIndex();
				columnPopupMenu.show(event.getPosition());
			}
		});
	}

	private IAction createHideColumnAction() {
		final IActionBuilder builder = Toolkit.getActionBuilderFactory().create();
		builder.setText("Hide column");
		builder.setCommand(new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				tableModel.hideColumn(popupColumn);
			}
		});
		return builder.build();
	}

	private IAction createUnhideAllColumnAction() {
		final IActionBuilder builder = Toolkit.getActionBuilderFactory().create();
		builder.setText("Unhide all columns");
		builder.setCommand(new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				tableModel.showAllColumns();
			}
		});
		return builder.build();
	}

	private IAction createNewColumnAction() {
		final IActionBuilder builder = Toolkit.getActionBuilderFactory().create();
		builder.setText("New column after current");
		builder.setCommand(new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				tableModel.addColumnAfter(popupColumn);
			}
		});
		return builder.build();
	}

	private IAction createRemoveColumnAction() {
		final IActionBuilder builder = Toolkit.getActionBuilderFactory().create();
		builder.setText("Remove column");
		builder.setCommand(new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				tableModel.removeColumn(popupColumn);
			}
		});
		return builder.build();
	}

	private final class TableModel implements ITableModel {

		private final TableDataModelObservable tableDataModelObservable;
		private final TableColumnModelObservable tableColumnModelObservable;

		private final ArrayList<DefaultTableColumn> columns;

		private final int rows = 2000;
		private char nextColumn;

		private TableModel() {
			tableDataModelObservable = new TableDataModelObservable();
			tableColumnModelObservable = new TableColumnModelObservable();

			columns = new ArrayList<DefaultTableColumn>();

			nextColumn = 'A';

			columns.add(createColumn());
			columns.add(createColumn());
			columns.add(createColumn());
			columns.add(createColumn());
		}

		public void removeColumn(final int popupColumn) {
			columns.remove(popupColumn);
			tableColumnModelObservable.fireColumnsRemoved(new int[] {popupColumn});
		}

		public void addColumnAfter(final int popupColumn) {
			columns.add(popupColumn + 1, createColumn());
			tableColumnModelObservable.fireColumnsAdded(new int[] {popupColumn + 1});
		}

		public void showAllColumns() {
			for (final DefaultTableColumn column : columns) {
				if (column.isVisible()) {
					continue;
				}

				column.setVisible(true);
				tableColumnModelObservable.fireColumnsChanged(new int[] {columns.indexOf(column)});
			}
		}

		public void hideColumn(final int popupColumn) {
			columns.get(popupColumn).setVisible(false);
			tableColumnModelObservable.fireColumnsChanged(new int[] {popupColumn});
		}

		private DefaultTableColumn createColumn() {
			final DefaultTableColumn result = new DefaultTableColumn(String.valueOf(nextColumn));
			nextColumn = (char) (nextColumn + 1);
			return result;
		}

		@Override
		public int getColumnCount() {
			return columns.size();
		}

		@Override
		public ITableColumn getColumn(final int columnIndex) {
			return columns.get(columnIndex);
		}

		@Override
		public ITableColumnModelObservable getTableColumnModelObservable() {
			return tableColumnModelObservable;
		}

		@Override
		public int getRowCount() {
			return rows;
		}

		@Override
		public ITableCell getCell(final int rowIndex, final int columnIndex) {
			return new TableCell(columns.get(columnIndex).getText() + rowIndex, "Tooltip for " + rowIndex + "/" + columnIndex);
		}

		@Override
		public ArrayList<Integer> getSelection() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setSelection(final List<Integer> selection) {
			// TODO Auto-generated method stub

		}

		@Override
		public ITableDataModelObservable getTableDataModelObservable() {
			return tableDataModelObservable;
		}

	}
}
