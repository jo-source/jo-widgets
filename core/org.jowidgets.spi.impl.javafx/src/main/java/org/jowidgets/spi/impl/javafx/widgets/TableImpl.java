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

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.ContextMenuEvent;
import javafx.util.Callback;

import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelListener;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.model.ITableColumnModelSpi;
import org.jowidgets.common.model.ITableColumnSpi;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.types.AlignmentHorizontal;
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
import org.jowidgets.spi.impl.controller.TableCellEditEvent;
import org.jowidgets.spi.impl.controller.TableCellEditorObservable;
import org.jowidgets.spi.impl.controller.TableCellEvent;
import org.jowidgets.spi.impl.controller.TableCellObservable;
import org.jowidgets.spi.impl.controller.TableCellPopupDetectionObservable;
import org.jowidgets.spi.impl.controller.TableCellPopupEvent;
import org.jowidgets.spi.impl.controller.TableColumnObservable;
import org.jowidgets.spi.impl.controller.TableColumnPopupDetectionObservable;
import org.jowidgets.spi.impl.controller.TableColumnPopupEvent;
import org.jowidgets.spi.impl.controller.TableColumnResizeEvent;
import org.jowidgets.spi.impl.controller.TableSelectionObservable;
import org.jowidgets.spi.impl.javafx.util.AlignmentConvert;
import org.jowidgets.spi.impl.javafx.widgets.base.JoCell;
import org.jowidgets.spi.impl.javafx.widgets.base.JoTableRow;
import org.jowidgets.spi.impl.javafx.widgets.base.VirtualList;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.ITableSpi;
import org.jowidgets.spi.widgets.setup.ITableSetupSpi;

import com.sun.javafx.scene.control.skin.TableColumnHeader;

@SuppressWarnings({"rawtypes", "unchecked"})
public class TableImpl extends JavafxControl implements ITableSpi {

	private final ITableDataModel dataModel;
	private final ITableColumnModelSpi columnModel;
	private final boolean columnsResizeable;

	private final TableCellObservable tableCellObservable;
	private final TableCellPopupDetectionObservable tableCellPopupDetectionObservable;
	private final TableColumnPopupDetectionObservable tableColumnPopupDetectionObservable;
	private final TableColumnObservable tableColumnObservable;
	private final TableSelectionObservable tableSelectionObservable;
	private final TableCellEditorObservable tableCellEditorObservable;

	private final TableSelectionListener tableSelectionListener;
	private final TableColumnModelListener tableColumnModelListener;

	private final boolean hasBorder;
	private boolean setWidthInvokedOnModel;
	private final StyleDelegate styleDelegate;
	private int popupIndex;
	private ObservableList<? extends TableColumn> permutationList;

	public TableImpl(final ITableSetupSpi setup) {
		super(new TableView(), false);
		this.tableSelectionListener = new TableSelectionListener();
		this.tableSelectionObservable = new TableSelectionObservable();
		this.tableColumnPopupDetectionObservable = new TableColumnPopupDetectionObservable();
		this.tableColumnModelListener = new TableColumnModelListener();
		this.tableColumnObservable = new TableColumnObservable();
		this.tableCellEditorObservable = new TableCellEditorObservable();
		this.tableCellPopupDetectionObservable = new TableCellPopupDetectionObservable();
		this.tableCellObservable = new TableCellObservable();

		styleDelegate = new StyleDelegate(getUiReference());

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

		this.hasBorder = setup.hasBorder();
		this.setWidthInvokedOnModel = false;
		if (!hasBorder) {
			styleDelegate.setNoBorder();
		}

		dataModel = setup.getDataModel();
		columnModel = setup.getColumnModel();
		columnsResizeable = setup.getColumnsResizeable();

		getUiReference().setItems(new VirtualList(dataModel, columnModel, getUiReference()));

		getUiReference().setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

			@Override
			public void handle(final ContextMenuEvent event) {
				final Position position = new Position((int) event.getScreenX(), (int) event.getScreenY());
				tableColumnPopupDetectionObservable.firePopupDetected(new TableColumnPopupEvent(0, position));

			}
		});
		getUiReference().getColumns().addListener(new ListChangeListener<TableColumn>() {

			@Override
			public void onChanged(final Change<? extends TableColumn> change) {

			}

		});

	}

	@Override
	public TableView getUiReference() {
		return (TableView) super.getUiReference();
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

		for (int columnIndex = 0; columnIndex < columnModel.getColumnCount(); columnIndex++) {
			final ITableColumnSpi column = columnModel.getColumn(columnIndex);
			final TableColumn columnfx = addColumn(columnIndex, column);
			getUiReference().getColumns().add(columnIndex, columnfx);
		}
		permutationList = getUiReference().getColumns();
		if (columnModelObservable != null) {
			columnModelObservable.addColumnModelListener(tableColumnModelListener);
		}
		getUiReference().setItems(new VirtualList(dataModel, columnModel, getUiReference()));
		getUiReference().getSelectionModel().getSelectedCells().addListener(tableSelectionListener);

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

		final TableColumn<ITableCell, Object> tableColumn = (TableColumn<ITableCell, Object>) getUiReference().getColumns().get(
				columnIndex);
		final double width = tableColumn.getWidth();
		return new Dimension((int) width, (int) height);

	}

	@Override
	public ArrayList<Integer> getColumnPermutation() {
		final ArrayList<Integer> result = new ArrayList<Integer>();
		final ObservableList<TableColumn<ITableCell, ?>> javafxColumns = getUiReference().getColumns();
		for (int i = 0; i < javafxColumns.size(); i++) {
			result.add(Integer.valueOf(permutationList.indexOf(javafxColumns.get(i))));
		}
		return result;
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
		getUiReference().getSelectionModel().clearSelection();

		final int[] indices = new int[selection.size()];

		for (int i = 0; i < selection.size(); i++) {
			indices[i] = selection.get(i);
		}
		getUiReference().getSelectionModel().selectIndices(-1, indices);
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
		// TODO DB Auto-generated method stub
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
		tableCellObservable.addTableCellListener(listener);

	}

	@Override
	public void removeTableCellListener(final ITableCellListener listener) {
		tableCellObservable.removeTableCellListener(listener);

	}

	@Override
	public void addTableCellPopupDetectionListener(final ITableCellPopupDetectionListener listener) {
		tableCellPopupDetectionObservable.addTableCellPopupDetectionListener(listener);
	}

	@Override
	public void removeTableCellPopupDetectionListener(final ITableCellPopupDetectionListener listener) {
		tableCellPopupDetectionObservable.removeTableCellPopupDetectionListener(listener);
	}

	@Override
	public void addTableCellEditorListener(final ITableCellEditorListener listener) {
		tableCellEditorObservable.addTableCellEditorListener(listener);
	}

	@Override
	public void removeTableCellEditorListener(final ITableCellEditorListener listener) {
		tableCellEditorObservable.removeTableCellEditorListener(listener);
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

	@Override
	public IPopupMenuSpi createPopupMenu() {
		if (popupIndex == -1) {
			return new PopupMenuImpl(getUiReference());
		}
		return new PopupMenuImpl(getUiReference().getColumns().get(popupIndex));
	}

	private TableColumn addColumn(final int externalIndex, final ITableColumnSpi joColumn) {
		final TableColumn tableColumn = new TableColumn(joColumn.getText());
		tableColumn.setResizable(columnsResizeable);
		tableColumn.setSortable(false);

		setColumnData(tableColumn, joColumn);

		tableColumn.setCellFactory(new TableCellFactory());
		tableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<JoTableRow, Object>, ObservableValue<Object>>() {

			@Override
			public ObservableValue call(final CellDataFeatures<JoTableRow, Object> pFeature) {
				final JoTableRow data = pFeature.getValue();
				if (data == null) {
					return new SimpleStringProperty("");
				}
				else {
					return new SimpleObjectProperty(data);
				}
			}

		});

		final ContextMenu contextMenu = new ContextMenu(new MenuItem()) {

			@Override
			public void show(final Node node, final double x, final double y) {
				final int index = getUiReference().getColumns().indexOf(((TableColumnHeader) node).getTableColumn());
				popupIndex = index;
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
				setWidthInvokedOnModel = true;
				columnModel.getColumn(getUiReference().getColumns().indexOf(tableColumn)).setWidth(newValue.intValue());
				setWidthInvokedOnModel = false;
				tableColumnObservable.fireColumnResized(new TableColumnResizeEvent(getUiReference().getColumns().indexOf(
						tableColumn), newValue.intValue()));
			}
		});

		tableColumn.setOnEditCommit(new EventHandler<CellEditEvent>() {
			@Override
			public void handle(final CellEditEvent event) {
				final int row = event.getTablePosition().getRow();
				final int column = event.getTablePosition().getColumn();
				final String newValue = (String) event.getNewValue();
				tableCellEditorObservable.fireEditFinished(new TableCellEditEvent(row, column, newValue));
			}
		});

		tableColumn.setOnEditCancel(new EventHandler<CellEditEvent>() {

			@Override
			public void handle(final CellEditEvent event) {
				final int currentRow = event.getTablePosition().getRow();
				final int currentColumn = getUiReference().getColumns().indexOf(tableColumn);
				tableCellEditorObservable.fireEditCanceled(new TableCellEvent(currentRow, currentColumn));
			}
		});

		return tableColumn;

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

	final class TableCellFactory implements Callback<TableColumn, TableCell> {

		@Override
		public TableCell call(final TableColumn column) {
			final JoCell joCell = new JoCell(tableCellObservable);
			joCell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

				@Override
				public void handle(final ContextMenuEvent event) {
					final Position position = new Position((int) event.getScreenX(), (int) event.getScreenY());
					if (joCell.getItem() != null) {
						final int columnIndex = getUiReference().getColumns().indexOf(joCell.getTableColumn());
						popupIndex = -1;
						tableCellPopupDetectionObservable.firePopupDetected(new TableCellPopupEvent(
							joCell.getIndex(),
							columnIndex,
							position));
					}
				}

			});
			final AlignmentHorizontal alignment = columnModel.getColumn(getUiReference().getColumns().indexOf(column)).getAlignment();
			if (alignment != null) {
				joCell.setAlignment(AlignmentConvert.convertPosAlignment(alignment));
			}
			return joCell;

		}
	}

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
			if (!setWidthInvokedOnModel) {
				for (int i = 0; i < columnIndices.length; i++) {
					final int columnIndex = columnIndices[i];
					final int viewWidth = (int) ((TableColumn) getUiReference().getColumns().get(columnIndex)).getWidth();
					final int modelWidth = columnModel.getColumn(columnIndex).getWidth();
					if (viewWidth != modelWidth) {
						((TableColumn) getUiReference().getColumns().get(columnIndex)).setPrefWidth(modelWidth);
					}
				}
			}
		}
	}

}
