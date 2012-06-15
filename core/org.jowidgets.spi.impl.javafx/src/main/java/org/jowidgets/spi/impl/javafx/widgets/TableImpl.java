/*
 * Copyright (c) 2012, David Bauknecht
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

package org.jowidgets.spi.impl.javafx.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelListener;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.model.ITableColumnModelSpi;
import org.jowidgets.common.model.ITableColumnSpi;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Interval;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.TablePackPolicy;
import org.jowidgets.common.types.TableSelectionPolicy;
import org.jowidgets.common.widgets.controller.ITableCellEditorListener;
import org.jowidgets.common.widgets.controller.ITableCellListener;
import org.jowidgets.common.widgets.controller.ITableCellPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableColumnListener;
import org.jowidgets.common.widgets.controller.ITableColumnPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableSelectionListener;
import org.jowidgets.spi.impl.controller.TableColumnObservable;
import org.jowidgets.spi.impl.controller.TableColumnPopupDetectionObservable;
import org.jowidgets.spi.impl.controller.TableColumnPopupEvent;
import org.jowidgets.spi.impl.controller.TableColumnResizeEvent;
import org.jowidgets.spi.impl.controller.TableSelectionObservable;
import org.jowidgets.spi.impl.javafx.widgets.base.VirtualList;
import org.jowidgets.spi.widgets.ITableSpi;
import org.jowidgets.spi.widgets.setup.ITableSetupSpi;

import com.sun.javafx.scene.control.skin.TableColumnHeader;

public class TableImpl extends JavafxControl implements ITableSpi {

	private final ITableDataModel dataModel;
	private final ITableColumnModelSpi columnModel;
	private final boolean columnsResizeable;
	private final TableSelectionListener tableSelectionListener;
	private final TableSelectionObservable tableSelectionObservable;
	private final TableColumnPopupDetectionObservable tableColumnPopupDetectionObservable;
	private final TableColumnModelListener tableColumnModelListener;
	private final TableColumnObservable tableColumnObservable;

	public TableImpl(final ITableSetupSpi setup) {
		super(new TableView<ITableCell>(), false);
		this.tableSelectionListener = new TableSelectionListener();
		this.tableSelectionObservable = new TableSelectionObservable();
		this.tableColumnPopupDetectionObservable = new TableColumnPopupDetectionObservable();
		this.tableColumnModelListener = new TableColumnModelListener();
		this.tableColumnObservable = new TableColumnObservable();

		if (setup.getSelectionPolicy() == TableSelectionPolicy.MULTI_ROW_SELECTION) {
			getUiReference().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		}
		else if (setup.getSelectionPolicy() == TableSelectionPolicy.SINGLE_ROW_SELECTION) {
			getUiReference().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		}
		else if (setup.getSelectionPolicy() == TableSelectionPolicy.NO_SELECTION) {
			getUiReference().getSelectionModel().setCellSelectionEnabled(false);
		}
		else {
			throw new IllegalArgumentException("SelectionPolicy '" + setup.getSelectionPolicy() + "' is not known");
		}
		dataModel = setup.getDataModel();
		columnModel = setup.getColumnModel();
		columnsResizeable = setup.getColumnsResizeable();
		getUiReference().setRowFactory(new TableRowFactory());

	}

	@SuppressWarnings({"unchecked"})
	@Override
	public TableView<ITableCell> getUiReference() {
		return (TableView<ITableCell>) super.getUiReference();
	}

	@Override
	public void setEditable(final boolean editable) {
		getUiReference().setEditable(editable);
	}

	@Override
	public void resetFromModel() {

		getUiReference().getColumns().clear();

		final ITableColumnModelObservable columnModelObservable = columnModel.getTableColumnModelObservable();
		if (columnModelObservable != null) {
			columnModelObservable.removeColumnModelListener(tableColumnModelListener);
		}

		for (int i = 0; i < columnModel.getColumnCount(); i++) {

			final ITableColumnSpi column = columnModel.getColumn(i);
			addColumn(i, column);
		}
		if (columnModelObservable != null) {
			columnModelObservable.addColumnModelListener(tableColumnModelListener);
		}
		getUiReference().getSelectionModel().getSelectedCells().addListener(tableSelectionListener);

		getUiReference().setItems(new VirtualList(dataModel));

	}

	@Override
	public Position getCellPosition(final int rowIndex, final int columnIndex) {
		// TODO DB Auto-generated method stub
		return null;
	}

	@Override
	public Dimension getCellSize(final int rowIndex, final int columnIndex) {
		double height = -1;
		for (final Node n : getUiReference().lookupAll("table-row-cell")) {
			if (n instanceof TableRow) {
				final TableRow<?> row = (TableRow<?>) n;
				if (row.getIndex() == rowIndex) {
					height = row.getHeight();
				}
			}
		}

		@SuppressWarnings("unchecked")
		final TableColumn<ITableCell, Object> tableColumn = (TableColumn<ITableCell, Object>) getUiReference().getColumns().get(
				columnIndex);
		final double width = tableColumn.getWidth();
		return new Dimension((int) width, (int) height);

	}

	@Override
	public ArrayList<Integer> getColumnPermutation() {
		// TODO DB Auto-generated method stub
		return null;
	}

	@Override
	public void setColumnPermutation(final List<Integer> permutation) {
		// TODO DB Auto-generated method stub

	}

	@Override
	public ArrayList<Integer> getSelection() {
		final ArrayList<Integer> arrayList = new ArrayList<Integer>();
		arrayList.addAll(getUiReference().getSelectionModel().getSelectedIndices());
		return arrayList;
	}

	@Override
	public void setSelection(final List<Integer> selection) {
		// TODO DB Auto-generated method stub
	}

	@Override
	public void scrollToRow(final int rowIndex) {
		getUiReference().scrollTo(rowIndex);
	}

	@Override
	public void pack(final TablePackPolicy policy) {
		// TODO DB Auto-generated method stub

	}

	@Override
	public void pack(final int columnIndex, final TablePackPolicy policy) {
		// TODO DB Auto-generated method stub

	}

	@Override
	public boolean isColumnPopupDetectionSupported() {
		// TODO DB Auto-generated method stub
		return false;
	}

	@Override
	public Interval<Integer> getVisibleRows() {
		return null;
	}

	@Override
	public void addTableSelectionListener(final ITableSelectionListener listener) {
		tableSelectionObservable.addTableSelectionListener(listener);

	}

	@Override
	public void removeTableSelectionListener(final ITableSelectionListener listener) {
		tableSelectionObservable.removeTableSelectionListener(listener);

	}

	@Override
	public void addTableCellListener(final ITableCellListener listener) {
		// TODO DB Auto-generated method stub

	}

	@Override
	public void removeTableCellListener(final ITableCellListener listener) {
		// TODO DB Auto-generated method stub

	}

	@Override
	public void addTableCellPopupDetectionListener(final ITableCellPopupDetectionListener listener) {
		// TODO DB Auto-generated method stub

	}

	@Override
	public void removeTableCellPopupDetectionListener(final ITableCellPopupDetectionListener listener) {
		// TODO DB Auto-generated method stub

	}

	@Override
	public void addTableCellEditorListener(final ITableCellEditorListener listener) {
		// TODO DB Auto-generated method stub

	}

	@Override
	public void removeTableCellEditorListener(final ITableCellEditorListener listener) {
		// TODO DB Auto-generated method stub

	}

	@Override
	public void addTableColumnListener(final ITableColumnListener listener) {
		tableColumnObservable.addTableColumnListener(listener);

	}

	@Override
	public void removeTableColumnListener(final ITableColumnListener listener) {
		tableColumnObservable.removeTableColumnListener(listener);

	}

	@Override
	public void addTableColumnPopupDetectionListener(final ITableColumnPopupDetectionListener listener) {
		tableColumnPopupDetectionObservable.addTableColumnPopupDetectionListener(listener);

	}

	@Override
	public void removeTableColumnPopupDetectionListener(final ITableColumnPopupDetectionListener listener) {
		tableColumnPopupDetectionObservable.addTableColumnPopupDetectionListener(listener);

	}

	private void setColumnData(final TableColumn<ITableCell, Object> tableColumn, final ITableColumnSpi joColumn) {
		final String text = joColumn.getText();

		if (text != null) {
			tableColumn.setText(text);
		}
		else {
			tableColumn.setText("");
		}
		if (joColumn.getWidth() != -1) {
			if (tableColumn.getWidth() != joColumn.getWidth()) {
				tableColumn.setPrefWidth(joColumn.getWidth());
			}
		}
		else {
			if (tableColumn.getWidth() != 100) {
				tableColumn.setPrefWidth(100);
			}
		}
	}

	class TableRowFactory implements Callback<TableView<ITableCell>, TableRow<ITableCell>> {

		@Override
		public TableRow<ITableCell> call(final TableView<ITableCell> paramP) {
			final TableRow<ITableCell> row = new TableRow<ITableCell>() {
				@Override
				protected void updateItem(final ITableCell item, final boolean isEmpty) {

				};
			};
			return row;
		}
	}

	class TableCellFactory implements Callback<TableColumn<ITableCell, Object>, TableCell<ITableCell, Object>> {

		@Override
		public TableCell<ITableCell, Object> call(final TableColumn<ITableCell, Object> column) {

			final TableCell<ITableCell, Object> cell = new TableCell<ITableCell, Object>() {
				@Override
				protected void updateItem(final Object item, final boolean isEmpty) {
					super.updateItem(item, isEmpty);
					final ITableCell joCell = dataModel.getCell(getIndex(), getUiReference().getColumns().indexOf(column));
					setText(joCell.getText());
				}
			};

			return cell;
		}
	}

	@SuppressWarnings("rawtypes")
	final class TableSelectionListener implements ListChangeListener<TablePosition> {

		@Override
		public void onChanged(final Change<? extends TablePosition> paramChange) {
			dataModel.setSelection(getSelection());
			tableSelectionObservable.fireSelectionChanged();
		}
	}

	final class TableColumnModelListener implements ITableColumnModelListener {

		@Override
		public void columnsAdded(final int[] columnIndices) {
			Arrays.sort(columnIndices);
			for (int i = 0; i < columnIndices.length; i++) {
				final int addedIndex = columnIndices[i];
				addColumn(addedIndex, columnModel.getColumn(addedIndex));
			}
		}

		@Override
		public void columnsRemoved(final int[] columnIndices) {
			Arrays.sort(columnIndices);
			int removedColumnsCount = 0;
			for (int i = 0; i < columnIndices.length; i++) {
				final int removedIndex = columnIndices[i] - removedColumnsCount;
				getUiReference().getColumns().remove(removedIndex);
				removedColumnsCount++;
			}
		}

		@Override
		public void columnsChanged(final int[] columnIndices) {
			// TODO DB Auto-generated method stub

		}

	}

	private void addColumn(final int externalIndex, final ITableColumnSpi joColumn) {
		final TableColumn<ITableCell, Object> tableColumn = new TableColumn<ITableCell, Object>(joColumn.getText());
		tableColumn.setCellValueFactory(new PropertyValueFactory<ITableCell, Object>("data"));
		tableColumn.setResizable(columnsResizeable);
		setColumnData(tableColumn, joColumn);
		getUiReference().getColumns().add(externalIndex, tableColumn);

		tableColumn.setCellFactory(new TableCellFactory());
		final ContextMenu contextMenu = new ContextMenu(new MenuItem()) {

			@Override
			public void show(final Node node, final double x, final double y) {
				final int index = getUiReference().getColumns().indexOf(((TableColumnHeader) node).getTableColumn());
				if (index < columnModel.getColumnCount()) {
					tableColumnPopupDetectionObservable.firePopupDetected(new TableColumnPopupEvent(index, new Position(
						(int) x,
						(int) y)));
				}
			}

		};
		tableColumn.setContextMenu(contextMenu);

		tableColumn.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(
				final ObservableValue<? extends Number> observableValue,
				final Number oldValue,
				final Number newValue) {
				columnModel.getColumn(getUiReference().getColumns().indexOf(tableColumn)).setWidth(newValue.intValue());
				tableColumnObservable.fireColumnResized(new TableColumnResizeEvent(getUiReference().getColumns().indexOf(
						tableColumn), newValue.intValue()));
			}
		});
	}
}
