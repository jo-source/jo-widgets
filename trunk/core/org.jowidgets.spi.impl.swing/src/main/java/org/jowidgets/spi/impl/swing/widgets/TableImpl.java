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

package org.jowidgets.spi.impl.swing.widgets;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumn;
import org.jowidgets.common.model.ITableColumnModel;
import org.jowidgets.common.model.ITableColumnModelListener;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.model.ITableDataModelListener;
import org.jowidgets.common.model.ITableDataModelObservable;
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.MouseButton;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.SelectionPolicy;
import org.jowidgets.common.types.TableColumnPackPolicy;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableCellEditEvent;
import org.jowidgets.common.widgets.controler.ITableCellEditorListener;
import org.jowidgets.common.widgets.controler.ITableCellEvent;
import org.jowidgets.common.widgets.controler.ITableCellListener;
import org.jowidgets.common.widgets.controler.ITableCellMouseEvent;
import org.jowidgets.common.widgets.controler.ITableCellPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableColumnListener;
import org.jowidgets.common.widgets.controler.ITableColumnMouseEvent;
import org.jowidgets.common.widgets.controler.ITableColumnPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableSelectionListener;
import org.jowidgets.spi.impl.controler.PopupDetectionObservable;
import org.jowidgets.spi.impl.controler.TableCellEditEvent;
import org.jowidgets.spi.impl.controler.TableCellEditorObservable;
import org.jowidgets.spi.impl.controler.TableCellEvent;
import org.jowidgets.spi.impl.controler.TableCellMouseEvent;
import org.jowidgets.spi.impl.controler.TableCellObservable;
import org.jowidgets.spi.impl.controler.TableCellPopupDetectionObservable;
import org.jowidgets.spi.impl.controler.TableCellPopupEvent;
import org.jowidgets.spi.impl.controler.TableColumnMouseEvent;
import org.jowidgets.spi.impl.controler.TableColumnObservable;
import org.jowidgets.spi.impl.controler.TableColumnPopupDetectionObservable;
import org.jowidgets.spi.impl.controler.TableColumnPopupEvent;
import org.jowidgets.spi.impl.controler.TableColumnResizeEvent;
import org.jowidgets.spi.impl.controler.TableSelectionObservable;
import org.jowidgets.spi.impl.swing.image.SwingImageRegistry;
import org.jowidgets.spi.impl.swing.util.AlignmentConvert;
import org.jowidgets.spi.impl.swing.util.ColorConvert;
import org.jowidgets.spi.impl.swing.util.FontProvider;
import org.jowidgets.spi.impl.swing.util.PositionConvert;
import org.jowidgets.spi.impl.swing.widgets.base.TableColumnModelAdapter;
import org.jowidgets.spi.widgets.ITableSpi;
import org.jowidgets.spi.widgets.setup.ITableSetupSpi;
import org.jowidgets.util.ArrayUtils;
import org.jowidgets.util.Assert;

public class TableImpl extends SwingControl implements ITableSpi {

	private final JTable table;
	private final ITableDataModel dataModel;
	private final ITableColumnModel columnModel;
	private final CellRenderer cellRenderer;
	private final CellEditor cellEditor;
	private final TableCellRenderer headerRenderer;

	private final PopupDetectionObservable popupDetectionObservable;
	private final TableCellObservable tableCellObservable;
	private final TableCellPopupDetectionObservable tableCellPopupDetectionObservable;
	private final TableColumnPopupDetectionObservable tableColumnPopupDetectionObservable;
	private final TableColumnObservable tableColumnObservable;
	private final TableSelectionObservable tableSelectionObservable;
	private final TableCellEditorObservable tableCellEditorObservable;

	private final TableColumnResizeListener tableColumnResizeListener;
	private final TableSelectionListener tableSelectionListener;
	private final TableModelListener tableModelListener;
	private final TableColumnModelListener tableColumnModelListener;
	private final TableCellMenuDetectListener tableCellMenuDetectListener;
	private final TableColumnListener tableColumnListener;
	private final TableColumnMoveListener tableColumnMoveListener;

	private final boolean columnsResizeable;

	private SwingTableModel swingTableModel;
	private ArrayList<Integer> lastColumnPermutation;
	private boolean columnMoveOccured;

	public TableImpl(final ITableSetupSpi setup) {
		super(new JScrollPane(new JTable()));

		this.cellRenderer = new CellRenderer();
		this.cellEditor = new CellEditor();

		this.popupDetectionObservable = new PopupDetectionObservable();
		this.tableCellObservable = new TableCellObservable();
		this.tableCellPopupDetectionObservable = new TableCellPopupDetectionObservable();
		this.tableColumnPopupDetectionObservable = new TableColumnPopupDetectionObservable();
		this.tableColumnObservable = new TableColumnObservable();
		this.tableSelectionObservable = new TableSelectionObservable();
		this.tableCellEditorObservable = new TableCellEditorObservable();

		this.tableColumnResizeListener = new TableColumnResizeListener();
		this.tableSelectionListener = new TableSelectionListener();
		this.tableModelListener = new TableModelListener();
		this.tableColumnModelListener = new TableColumnModelListener();
		this.tableCellMenuDetectListener = new TableCellMenuDetectListener();
		this.tableColumnListener = new TableColumnListener();
		this.tableColumnMoveListener = new TableColumnMoveListener();

		this.columnMoveOccured = false;

		this.dataModel = setup.getDataModel();
		this.columnModel = setup.getColumnModel();

		this.columnsResizeable = setup.getColumnsResizeable();

		getUiReference().setBorder(BorderFactory.createEmptyBorder());

		this.table = (JTable) getUiReference().getViewport().getView();
		table.setAutoCreateColumnsFromModel(false);
		this.headerRenderer = new TableHeaderRenderer();

		table.setBorder(BorderFactory.createEmptyBorder());
		table.setGridColor(new Color(230, 230, 230));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(setup.getColumnsMoveable());

		if (setup.getSelectionPolicy() == SelectionPolicy.SINGLE_SELECTION) {
			table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		}
		else {
			table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}

		table.getTableHeader().setDefaultRenderer(headerRenderer);
		table.getTableHeader().addMouseListener(tableColumnListener);
		table.getTableHeader().addMouseListener(new TableColumnMenuDetectListener());

		table.getColumnModel().addColumnModelListener(tableColumnMoveListener);
		table.getSelectionModel().addListSelectionListener(tableSelectionListener);

		table.addMouseListener(new TableCellListener());
		table.addMouseListener(tableCellMenuDetectListener);
		getUiReference().addMouseListener(tableCellMenuDetectListener);
	}

	@Override
	public JScrollPane getUiReference() {
		return (JScrollPane) super.getUiReference();
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		popupDetectionObservable.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		popupDetectionObservable.removePopupDetectionListener(listener);
	}

	@Override
	public void initialize() {
		final ITableDataModelObservable dataModelObservable = dataModel.getTableDataModelObservable();
		if (dataModelObservable != null) {
			dataModelObservable.removeDataModelListener(tableModelListener);
		}

		final ITableColumnModelObservable columnModelObservable = columnModel.getTableColumnModelObservable();
		if (columnModelObservable != null) {
			columnModelObservable.removeColumnModelListener(tableColumnModelListener);
		}

		table.getSelectionModel().removeListSelectionListener(tableSelectionListener);

		this.swingTableModel = new SwingTableModel();

		table.setModel(swingTableModel);

		final int columnCount = columnModel.getColumnCount();
		lastColumnPermutation = new ArrayList<Integer>(columnCount);
		for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
			final TableColumn column = new TableColumn();
			column.setHeaderValue(columnModel.getColumn(columnIndex));
			column.setCellRenderer(cellRenderer);
			column.setCellEditor(cellEditor);
			column.setResizable(columnsResizeable);
			column.addPropertyChangeListener(tableColumnResizeListener);
			column.setModelIndex(columnIndex);
			table.getColumnModel().addColumn(column);
			lastColumnPermutation.add(Integer.valueOf(columnIndex));
		}

		setSelection(dataModel.getSelection());

		if (dataModelObservable != null) {
			dataModelObservable.addDataModelListener(tableModelListener);
		}

		if (columnModelObservable != null) {
			columnModelObservable.addColumnModelListener(tableColumnModelListener);
		}

		table.getSelectionModel().addListSelectionListener(tableSelectionListener);

		table.repaint();
	}

	@Override
	public Position getCellPosition(final int rowIndex, final int columnIndex) {
		final Rectangle cellRect = table.getCellRect(rowIndex, columnIndex, true);
		return PositionConvert.convert(SwingUtilities.convertPoint(table, cellRect.getLocation(), getUiReference()));
	}

	@Override
	public Dimension getCellSize(final int rowIndex, final int columnIndex) {
		final Rectangle cellRect = table.getCellRect(rowIndex, columnIndex, true);
		return new Dimension(cellRect.width, cellRect.height);
	}

	@Override
	public void pack(final TableColumnPackPolicy policy) {
		for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
			pack(columnIndex, getRowRange(policy), policy);
		}
	}

	@Override
	public void pack(final int columnIndex, final TableColumnPackPolicy policy) {
		pack(columnIndex, getRowRange(policy), policy);
	}

	private void pack(final int columnIndex, final RowRange rowRange, final TableColumnPackPolicy policy) {

		final TableColumn column = table.getColumnModel().getColumn(table.convertColumnIndexToView(columnIndex));

		final boolean header = TableColumnPackPolicy.HEADER == policy || TableColumnPackPolicy.HEADER_AND_CONTENT == policy;
		final boolean data = TableColumnPackPolicy.CONTENT == policy || TableColumnPackPolicy.HEADER_AND_CONTENT == policy;
		int maxWidth = 0;

		if (data) {
			for (int rowIndex = rowRange.getStartIndex(); rowIndex <= rowRange.getEndIndex(); rowIndex++) {
				final Object value = table.getValueAt(rowIndex, columnIndex);
				final TableCellRenderer renderer = table.getCellRenderer(rowIndex, columnIndex);
				final Component comp = renderer.getTableCellRendererComponent(table, value, false, false, rowIndex, columnIndex);
				maxWidth = Math.max(maxWidth, (int) comp.getPreferredSize().getWidth());
			}
		}

		if (header) {
			final Object value = column.getHeaderValue();
			final TableCellRenderer renderer = table.getTableHeader().getDefaultRenderer();
			final Component comp = renderer.getTableCellRendererComponent(table, value, false, false, 0, columnIndex);
			maxWidth = Math.max(maxWidth, (int) comp.getPreferredSize().getWidth());
		}

		column.setPreferredWidth(maxWidth + 5);
	}

	private RowRange getRowRange(final TableColumnPackPolicy policy) {
		final Rectangle viewRect = getUiReference().getViewport().getViewRect();
		final int firstVisibleRowIndex = table.rowAtPoint(new Point(0, viewRect.y));
		int lastVisibleRowIndex = table.rowAtPoint(new Point(0, viewRect.y + viewRect.height - 1));
		if (lastVisibleRowIndex == -1) {
			lastVisibleRowIndex = table.getRowCount() - 1;
		}
		return new RowRange(firstVisibleRowIndex, lastVisibleRowIndex);
	}

	@Override
	public ArrayList<Integer> getColumnPermutation() {
		final ArrayList<Integer> result = new ArrayList<Integer>();
		final TableColumnModel swingColumnModel = table.getColumnModel();
		for (int i = 0; i < swingColumnModel.getColumnCount(); i++) {
			result.add(Integer.valueOf(swingColumnModel.getColumn(i).getModelIndex()));
		}
		return result;
	}

	@Override
	public void setColumnPermutation(final List<Integer> permutation) {
		Assert.paramNotNull(permutation, "permutation");
		table.getColumnModel().removeColumnModelListener(tableColumnMoveListener);

		int columnIndex = 0;
		for (final Integer permutatedIndex : permutation) {
			table.getColumnModel().moveColumn(table.convertColumnIndexToView(permutatedIndex.intValue()), columnIndex);
			columnIndex++;
		}

		table.getTableHeader().repaint();
		final ArrayList<Integer> newPermutation = getColumnPermutation();
		if (!newPermutation.equals(lastColumnPermutation)) {
			lastColumnPermutation = newPermutation;
			tableColumnObservable.fireColumnPermutationChanged();
		}
		table.getColumnModel().addColumnModelListener(tableColumnMoveListener);
	}

	@Override
	public ArrayList<Integer> getSelection() {
		final ArrayList<Integer> result = new ArrayList<Integer>();
		final int[] selectedRows = table.getSelectedRows();
		for (int i = 0; i < selectedRows.length; i++) {
			result.add(Integer.valueOf(selectedRows[i]));
		}
		return result;
	}

	@Override
	public void setSelection(final List<Integer> selection) {
		if (!getSelection().equals(selection)) {
			final ListSelectionModel selectionModel = table.getSelectionModel();
			selectionModel.clearSelection();
			if (selection != null && selection.size() != 0) {
				for (int i = 0; i < selection.size(); i++) {
					if (i < selection.size() - 1) {
						selectionModel.setValueIsAdjusting(true);
					}
					else {
						selectionModel.setValueIsAdjusting(false);
					}
					final int selectionIndex = selection.get(i);
					selectionModel.addSelectionInterval(selectionIndex, selectionIndex);
				}
			}
		}
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
	public void addTableColumnPopupDetectionListener(final ITableColumnPopupDetectionListener listener) {
		tableColumnPopupDetectionObservable.addTableColumnPopupDetectionListener(listener);
	}

	@Override
	public void removeTableColumnPopupDetectionListener(final ITableColumnPopupDetectionListener listener) {
		tableColumnPopupDetectionObservable.addTableColumnPopupDetectionListener(listener);
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
	public void addTableSelectionListener(final ITableSelectionListener listener) {
		tableSelectionObservable.addTableSelectionListener(listener);
	}

	@Override
	public void removeTableSelectionListener(final ITableSelectionListener listener) {
		tableSelectionObservable.removeTableSelectionListener(listener);
	}

	@Override
	public void addTableCellEditorListener(final ITableCellEditorListener listener) {
		tableCellEditorObservable.addTableCellEditorListener(listener);
	}

	@Override
	public void removeTableCellEditorListener(final ITableCellEditorListener listener) {
		tableCellEditorObservable.removeTableCellEditorListener(listener);
	}

	private CellIndices getCellIndices(final MouseEvent event) {
		return getCellIndices(new Point(event.getX(), event.getY()));
	}

	private CellIndices getCellIndices(final Point point) {
		final int columnIndex = table.columnAtPoint(point);
		final int rowIndex = table.rowAtPoint(point);
		if (columnIndex != -1 && rowIndex != -1) {
			return new CellIndices(table.convertRowIndexToModel(rowIndex), table.convertColumnIndexToModel(columnIndex));
		}
		return null;
	}

	private static MouseButton getMouseButton(final MouseEvent event) {
		if (SwingUtilities.isLeftMouseButton(event)) {
			return MouseButton.LEFT;
		}
		else if (SwingUtilities.isRightMouseButton(event)) {
			return MouseButton.RIGTH;
		}
		else {
			return null;
		}
	}

	private static Set<Modifier> getModifier(final MouseEvent event) {
		final Set<Modifier> modifier = new HashSet<Modifier>();
		if (event.isShiftDown()) {
			modifier.add(Modifier.SHIFT);
		}
		if (event.isControlDown()) {
			modifier.add(Modifier.CTRL);
		}
		if (event.isAltDown()) {
			modifier.add(Modifier.ALT);
		}
		return modifier;
	}

	final class TableCellListener extends MouseAdapter {

		@Override
		public void mouseClicked(final MouseEvent e) {
			final ITableCellMouseEvent mouseEvent = getMouseEvent(e, 2);
			if (mouseEvent != null) {
				tableCellObservable.fireMouseDoubleClicked(mouseEvent);
			}
		}

		@Override
		public void mousePressed(final MouseEvent e) {
			final ITableCellMouseEvent mouseEvent = getMouseEvent(e, 1);
			if (mouseEvent != null) {
				tableCellObservable.fireMousePressed(mouseEvent);
			}
		}

		@Override
		public void mouseReleased(final MouseEvent e) {
			final ITableCellMouseEvent mouseEvent = getMouseEvent(e, 1);
			if (mouseEvent != null) {
				tableCellObservable.fireMouseReleased(mouseEvent);
			}
		}

		private ITableCellMouseEvent getMouseEvent(final MouseEvent event, final int clickCount) {
			if (event.getClickCount() != clickCount) {
				return null;
			}

			final MouseButton mouseButton = getMouseButton(event);
			if (mouseButton == null) {
				return null;
			}

			final CellIndices indices = getCellIndices(event);
			if (indices != null) {
				return new TableCellMouseEvent(indices.getRowIndex(), indices.getColumnIndex(), mouseButton, getModifier(event));
			}
			return null;
		}
	}

	final class TableCellMenuDetectListener extends MouseAdapter {

		@Override
		public void mouseReleased(final MouseEvent e) {
			fireMenuDetect(e);
		}

		@Override
		public void mousePressed(final MouseEvent e) {
			fireMenuDetect(e);
		}

		private void fireMenuDetect(final MouseEvent e) {
			if (e.isPopupTrigger()) {
				final Point point = new Point(e.getX(), e.getY());

				final Point popupPosition = new Point(e.getLocationOnScreen());
				SwingUtilities.convertPointFromScreen(popupPosition, getUiReference());
				final Position position = PositionConvert.convert(popupPosition);

				final CellIndices indices = getCellIndices(point);
				if (indices != null) {
					final int rowIndex = indices.getRowIndex();
					final int colIndex = indices.getColumnIndex();
					//add default windows selection behavior
					if (!table.getSelectionModel().isSelectedIndex(rowIndex) && !e.isControlDown()) {
						table.getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
					}
					tableCellPopupDetectionObservable.firePopupDetected(new TableCellPopupEvent(rowIndex, colIndex, position));
				}
				else {
					popupDetectionObservable.firePopupDetected(position);
				}
			}
		}
	}

	final class TableColumnListener extends MouseAdapter {

		@Override
		public void mouseReleased(final MouseEvent e) {
			if (columnMoveOccured && SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
				columnMoveOccured = false;
				final ArrayList<Integer> currentPermutation = getColumnPermutation();
				if (!currentPermutation.equals(lastColumnPermutation)) {
					lastColumnPermutation = currentPermutation;
					tableColumnObservable.fireColumnPermutationChanged();
				}
			}
		}

		@Override
		public void mouseClicked(final MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
				int columnIndex = table.getTableHeader().getColumnModel().getColumnIndexAtX(e.getX());
				columnIndex = table.convertColumnIndexToModel(columnIndex);
				final ITableColumnMouseEvent mouseEvent = new TableColumnMouseEvent(columnIndex, getModifier(e));
				tableColumnObservable.fireMouseClicked(mouseEvent);
			}
		}
	}

	final class TableColumnMoveListener extends TableColumnModelAdapter {
		@Override
		public void columnMoved(final TableColumnModelEvent e) {
			if (e.getFromIndex() != e.getToIndex()) {
				columnMoveOccured = true;
			}
		}
	}

	final class TableColumnMenuDetectListener extends MouseAdapter {

		@Override
		public void mouseReleased(final MouseEvent e) {
			fireMenuDetect(e);
		}

		@Override
		public void mousePressed(final MouseEvent e) {
			fireMenuDetect(e);
		}

		private void fireMenuDetect(final MouseEvent e) {
			if (e.isPopupTrigger()) {
				final Point popupPosition = new Point(e.getLocationOnScreen());
				SwingUtilities.convertPointFromScreen(popupPosition, getUiReference());
				final Position position = PositionConvert.convert(popupPosition);

				int columnIndex = table.getTableHeader().getColumnModel().getColumnIndexAtX(e.getX());
				columnIndex = table.convertColumnIndexToModel(columnIndex);
				if (columnIndex != -1) {
					tableColumnPopupDetectionObservable.firePopupDetected(new TableColumnPopupEvent(columnIndex, position));
				}
				else {
					popupDetectionObservable.firePopupDetected(position);
				}

			}
		}
	}

	final class TableColumnResizeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(final PropertyChangeEvent evt) {
			final TableColumn column = (TableColumn) evt.getSource();
			if (column != null) {
				final Object newObject = evt.getNewValue();
				if (newObject instanceof Integer) {
					final int newWidth = ((Integer) newObject).intValue();
					tableColumnObservable.fireColumnResized(new TableColumnResizeEvent(column.getModelIndex(), newWidth));
				}
			}
		}
	}

	final class TableSelectionListener implements ListSelectionListener {
		@Override
		public void valueChanged(final ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				dataModel.setSelection(getSelection());
				tableSelectionObservable.fireSelectionChanged();
			}
		}
	}

	final class TableModelListener implements ITableDataModelListener {

		@Override
		public void rowsAdded(final int[] rowIndices) {
			if (swingTableModel != null) {
				swingTableModel.rowsAdded(rowIndices);
			}
		}

		@Override
		public void rowsRemoved(final int[] rowIndices) {
			if (swingTableModel != null) {
				swingTableModel.rowsRemoved(rowIndices);
			}
		}

		@Override
		public void rowsChanged(final int[] rowIndices) {
			if (swingTableModel != null) {
				swingTableModel.rowsChanged(rowIndices);
			}
		}

		@Override
		public void dataChanged() {
			if (swingTableModel != null) {
				swingTableModel.dataChanged();
			}
		}

		@Override
		public void selectionChanged() {
			setSelection(dataModel.getSelection());
		}

	}

	final class TableColumnModelListener implements ITableColumnModelListener {

		@Override
		public void columnsAdded(final int[] columnIndices) {

			if (columnIndices.length == 0) {
				return;
			}

			//first sort the columns by model index
			final TableColumn[] columns = getColumnsSortedByModelIndex();

			//fix the model indices and add the new columns to the proper position
			Arrays.sort(columnIndices);

			int addedColumns = 0;
			int nextIndexToAdd = columnIndices[0];
			for (int columnIndex = 0; columnIndex <= columns.length; columnIndex++) {
				if (columnIndex == nextIndexToAdd) {
					final TableColumn column = new TableColumn();
					column.setHeaderValue(columnModel.getColumn(columnIndex));
					column.setCellRenderer(cellRenderer);
					column.setCellEditor(cellEditor);
					column.setResizable(columnsResizeable);
					column.addPropertyChangeListener(tableColumnResizeListener);
					column.setModelIndex(columnIndex);
					addedColumns++;
					if (addedColumns < columnIndices.length) {
						nextIndexToAdd = columnIndices[addedColumns];
					}
					table.addColumn(column);
					table.moveColumn(table.getColumnCount() - 1, columnIndex);
				}
				//fix the index in the model, for each previously added row, the
				//index must be increased by one
				if (columnIndex < columns.length) {
					final TableColumn swingColumn = columns[columnIndex];
					swingColumn.setModelIndex(swingColumn.getModelIndex() + addedColumns);
				}
			}
			table.getTableHeader().repaint();
		}

		@Override
		public void columnsRemoved(final int[] columnIndices) {

			if (columnIndices.length == 0) {
				return;
			}

			//first sort the columns by model index
			final TableColumn[] columns = getColumnsSortedByModelIndex();

			//fix the model indices and determine the columns to remove
			Arrays.sort(columnIndices);
			final Set<TableColumn> columnsToRemove = new HashSet<TableColumn>();
			int removedColumns = 0;
			int nextIndexToRemove = columnIndices[0];
			for (int columnIndex = 0; columnIndex < columns.length; columnIndex++) {
				final TableColumn swingColumn = columns[columnIndex];
				if (columnIndex == nextIndexToRemove) {
					columnsToRemove.add(columns[columnIndex]);
					swingColumn.removePropertyChangeListener(tableColumnResizeListener);
					removedColumns++;
					if (removedColumns < columnIndices.length) {
						nextIndexToRemove = columnIndices[removedColumns];
					}
				}
				else {
					//fix the index in the model, for each previously removed row, the
					//index must be decreased by one
					swingColumn.setModelIndex(swingColumn.getModelIndex() - removedColumns);
				}
			}

			//now remove the columns from the model
			for (final TableColumn column : columnsToRemove) {
				table.removeColumn(column);
			}

		}

		@Override
		public void columnsChanged(final int[] columnIndices) {
			for (int i = 0; i < columnIndices.length; i++) {
				final int columnIndex = columnIndices[i];
				final int modelIndex = table.convertColumnIndexToView(columnIndex);
				final int viewWidth = table.getColumnModel().getColumn(modelIndex).getWidth();
				final int modelWidth = columnModel.getColumn(columnIndex).getWidth();
				if (viewWidth != modelWidth) {
					table.getColumnModel().getColumn(modelIndex).setPreferredWidth(modelWidth);
				}
			}
			table.getTableHeader().repaint();
		}

		private TableColumn[] getColumnsSortedByModelIndex() {
			final TableColumn[] columns = new TableColumn[table.getColumnCount()];
			for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
				final int modelIndex = table.convertColumnIndexToModel(columnIndex);
				columns[modelIndex] = table.getColumnModel().getColumn(columnIndex);
			}
			return columns;
		}

	}

	class SwingTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 6096723765272552285L;

		public SwingTableModel() {
			super();
		}

		@Override
		public String getColumnName(final int column) {
			return columnModel.getColumn(column).getText();
		}

		@Override
		public Object getValueAt(final int rowIndex, final int columnIndex) {
			return dataModel.getCell(rowIndex, columnIndex);
		}

		@Override
		public int getRowCount() {
			return dataModel.getRowCount();
		}

		@Override
		public int getColumnCount() {
			return table.getColumnModel().getColumnCount();
		}

		@Override
		public boolean isCellEditable(final int rowIndex, final int columnIndex) {
			return dataModel.getCell(rowIndex, columnIndex).isEditable();
		}

		void rowsAdded(final int[] rowIndices) {
			if (rowIndices.length == 1) {
				fireTableRowsInserted(rowIndices[0], rowIndices[0]);
			}
			else {
				dataChanged();
			}
		}

		void rowsRemoved(final int[] rowIndices) {
			if (rowIndices.length == 1) {
				fireTableRowsDeleted(rowIndices[0], rowIndices[0]);
			}
			else {
				dataChanged();
			}
		}

		void rowsChanged(final int[] rowIndices) {
			fireTableRowsUpdated(ArrayUtils.getMin(rowIndices), ArrayUtils.getMax(rowIndices));
		}

		void dataChanged() {
			table.getSelectionModel().removeListSelectionListener(tableSelectionListener);
			fireTableDataChanged();
			setSelection(dataModel.getSelection());
			table.getSelectionModel().addListSelectionListener(tableSelectionListener);
		}

	}

	class TableHeaderRenderer implements TableCellRenderer {

		private final TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();

		@Override
		public Component getTableCellRendererComponent(
			final JTable table,
			final Object value,
			final boolean isSelected,
			final boolean hasFocus,
			final int row,
			final int columnIndex) {
			final JLabel defaultComponent = (JLabel) headerRenderer.getTableCellRendererComponent(
					table,
					value,
					isSelected,
					hasFocus,
					row,
					columnIndex);

			final ITableColumn column = (ITableColumn) value;

			if (column.getIcon() != null) {
				defaultComponent.setIcon(SwingImageRegistry.getInstance().getImageIcon(column.getIcon()));
			}
			defaultComponent.setText(column.getText());
			defaultComponent.setToolTipText(column.getToolTipText());

			return defaultComponent;
		}

	};

	class CellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1581242312833196056L;

		@Override
		public Component getTableCellRendererComponent(
			final JTable table,
			final Object value,
			final boolean isSelected,
			final boolean hasFocus,
			final int row,
			final int column) {

			final ITableCell cell = (ITableCell) value;

			final Component result = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (cell != null) {
				final IImageConstant icon = cell.getIcon();
				final IColorConstant backgroundColor = cell.getBackgroundColor();
				final IColorConstant foregroundColor = cell.getForegroundColor();
				final Markup markup = cell.getMarkup();
				final AlignmentHorizontal alignment = columnModel.getColumn(column).getAlignment();

				setText(cell.getText());
				setToolTipText(cell.getToolTipText());

				if (icon != null) {
					setIcon(SwingImageRegistry.getInstance().getImageIcon(icon));
				}
				else {
					setIcon(null);
				}

				if (markup != null) {
					setFont(FontProvider.deriveFont(getFont(), markup));
				}
				else {
					setFont(FontProvider.deriveFont(getFont(), Markup.DEFAULT));
				}

				if (alignment != null) {
					setHorizontalAlignment(AlignmentConvert.convert(alignment));
				}
				else {
					setHorizontalAlignment(JLabel.LEFT);
				}

				if (!isSelected) {
					if (backgroundColor != null) {
						setBackground(ColorConvert.convert(backgroundColor));
					}
					else {
						setBackground(null);
					}

					if (foregroundColor != null) {
						setForeground(ColorConvert.convert(foregroundColor));
					}
					else {
						setForeground(null);
					}
				}
			}

			return result;
		}
	}

	final class CellEditor extends DefaultCellEditor {

		private static final long serialVersionUID = -5014746984033398117L;

		private int currentRow;
		private int currentColumn;

		public CellEditor() {
			super(new JTextField());
		}

		@Override
		public Component getTableCellEditorComponent(
			final JTable table,
			final Object value,
			final boolean isSelected,
			final int row,
			final int column) {

			this.currentRow = row;
			this.currentColumn = column;

			final JTextField textField = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);

			final String text = dataModel.getCell(row, column).getText();
			if (text != null) {

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						startEditing(textField, text, row, column);
					}
				});

			}
			else {
				textField.setText(null);
			}

			return textField;
		}

		@Override
		public int getClickCountToStart() {
			return 2;
		}

		@Override
		public boolean stopCellEditing() {
			final String text = ((JTextField) getComponent()).getText();
			final ITableCellEditEvent editEvent = new TableCellEditEvent(currentRow, currentColumn, text);
			tableCellEditorObservable.fireEditFinished(editEvent);
			return super.stopCellEditing();
		}

		@Override
		public void cancelCellEditing() {
			final ITableCellEvent event = new TableCellEvent(currentRow, currentColumn);
			tableCellEditorObservable.fireEditCanceled(event);
			super.cancelCellEditing();
		}

		private void startEditing(final JTextField textField, final String text, final int row, final int column) {
			textField.setText(text);
			textField.selectAll();
			textField.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(final DocumentEvent e) {
					fireEditEvent(e);
				}

				@Override
				public void insertUpdate(final DocumentEvent e) {
					fireEditEvent(e);
				}

				@Override
				public void changedUpdate(final DocumentEvent e) {
					fireEditEvent(e);
				}

				private void fireEditEvent(final DocumentEvent e) {
					final String text = textField.getText();
					final ITableCellEditEvent editEvent = new TableCellEditEvent(row, column, text);
					final boolean veto = tableCellEditorObservable.fireOnEdit(editEvent);

					//CHECKSTYLE:OFF
					if (veto) {
						//TODO MG handle veto
					}
					//CHECKSTYLE:ON
				}

			});
		}

	}

	final class CellIndices {

		private final int rowIndex;
		private final int columnIndex;

		public CellIndices(final int rowIndex, final int columnIndex) {
			super();
			this.rowIndex = rowIndex;
			this.columnIndex = columnIndex;
		}

		public int getRowIndex() {
			return rowIndex;
		}

		public int getColumnIndex() {
			return columnIndex;
		}
	}

	final class RowRange {

		private final int startIndex;
		private final int endIndex;

		public RowRange(final int startIndex, final int endIndex) {
			super();
			this.startIndex = startIndex;
			this.endIndex = endIndex;
		}

		public int getStartIndex() {
			return startIndex;
		}

		public int getEndIndex() {
			return endIndex;
		}

	}

}
