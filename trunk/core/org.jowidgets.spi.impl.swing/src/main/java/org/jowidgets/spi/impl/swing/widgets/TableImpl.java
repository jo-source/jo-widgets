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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
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
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.MouseButton;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.SelectionPolicy;
import org.jowidgets.common.types.TableColumnPackPolicy;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableCellEditorListener;
import org.jowidgets.common.widgets.controler.ITableCellListener;
import org.jowidgets.common.widgets.controler.ITableCellMouseEvent;
import org.jowidgets.common.widgets.controler.ITableCellPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableColumnListener;
import org.jowidgets.common.widgets.controler.ITableColumnMouseEvent;
import org.jowidgets.common.widgets.controler.ITableColumnPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableSelectionListener;
import org.jowidgets.common.widgets.model.ITableCell;
import org.jowidgets.common.widgets.model.ITableColumn;
import org.jowidgets.common.widgets.model.ITableColumnModel;
import org.jowidgets.common.widgets.model.ITableColumnModelListener;
import org.jowidgets.common.widgets.model.ITableModel;
import org.jowidgets.common.widgets.model.ITableModelListener;
import org.jowidgets.spi.impl.controler.PopupDetectionObservable;
import org.jowidgets.spi.impl.controler.TableCellEditorObservable;
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

public class TableImpl extends SwingControl implements ITableSpi {

	private final JTable table;
	private final ITableModel tableModel;
	private final ITableColumnModel columnModel;
	private final CellRenderer cellRenderer;

	private final PopupDetectionObservable popupDetectionObservable;
	private final TableCellObservable tableCellObservable;
	private final TableCellPopupDetectionObservable tableCellPopupDetectionObservable;
	private final TableColumnPopupDetectionObservable tableColumnPopupDetectionObservable;
	private final TableColumnObservable tableColumnObservable;
	private final TableSelectionObservable tableSelectionObservable;
	private final TableCellEditorObservable tableCellEditorObservable;

	private final TableColumnResizeListener tableColumnResizeListener;
	private final TableSelectionListener tableSelectionListener;

	private final boolean columnsResizeable;

	private ArrayList<Integer> lastColumnPermutation;
	private boolean columnMoveOccured;

	public TableImpl(final ITableSetupSpi setup) {
		super(new JScrollPane(new JTable()));

		this.popupDetectionObservable = new PopupDetectionObservable();
		this.tableCellObservable = new TableCellObservable();
		this.tableCellPopupDetectionObservable = new TableCellPopupDetectionObservable();
		this.tableColumnPopupDetectionObservable = new TableColumnPopupDetectionObservable();
		this.tableColumnObservable = new TableColumnObservable();
		this.tableSelectionObservable = new TableSelectionObservable();
		this.tableCellEditorObservable = new TableCellEditorObservable();

		this.tableColumnResizeListener = new TableColumnResizeListener();
		this.tableSelectionListener = new TableSelectionListener();

		this.columnMoveOccured = false;

		this.tableModel = setup.getTableModel();
		this.columnModel = setup.getColumnModel();

		this.columnsResizeable = setup.getColumnsResizeable();

		getUiReference().setBorder(BorderFactory.createEmptyBorder());

		this.table = (JTable) getUiReference().getViewport().getView();
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

		table.getTableHeader().setDefaultRenderer(new TableHeaderRenderer());
		table.getTableHeader().addMouseListener(new TableColumnMenuDetectListener());
		table.getTableHeader().addMouseListener(new TableColumnListener());

		table.getColumnModel().addColumnModelListener(new TableColumnMoveListener());
		table.getSelectionModel().addListSelectionListener(tableSelectionListener);

		table.addMouseListener(new TableCellListener());
		table.addMouseListener(new TableCellMenuDetectListener());

		this.cellRenderer = new CellRenderer();

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
		table.getSelectionModel().removeListSelectionListener(tableSelectionListener);

		table.setModel(new SwingTableModel(tableModel, columnModel));

		final TableColumnModel swingColumnModel = table.getColumnModel();
		final int columnCount = swingColumnModel.getColumnCount();
		lastColumnPermutation = new ArrayList<Integer>(columnCount);
		for (int i = 0; i < columnCount; i++) {
			final TableColumn column = swingColumnModel.getColumn(i);
			column.setCellRenderer(cellRenderer);
			column.setResizable(columnsResizeable);
			column.addPropertyChangeListener(tableColumnResizeListener);
			lastColumnPermutation.add(Integer.valueOf(i));
		}

		setSelection(tableModel.getSelection());

		table.getSelectionModel().addListSelectionListener(tableSelectionListener);
	}

	@Override
	public Position getCellPosition(final int rowIndex, final int columnIndex) {
		return null;
	}

	@Override
	public Dimension getCellSize(final int rowIndex, final int columnIndex) {
		return null;
	}

	@Override
	public void pack(final TableColumnPackPolicy policy) {

	}

	@Override
	public void pack(final int columnIndex, final TableColumnPackPolicy policy) {

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
	public ArrayList<Integer> getSelection() {
		final ArrayList<Integer> result = new ArrayList<Integer>();
		final int[] selectedRows = table.getSelectedRows();
		for (int i = 0; i < selectedRows.length; i++) {
			result.add(Integer.valueOf(selectedRows[i]));
		}
		return result;
	}

	@Override
	public void setSelection(final ArrayList<Integer> selection) {
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
					popupDetectionObservable.firePopupDetected(position);
					tableCellPopupDetectionObservable.firePopupDetected(new TableCellPopupEvent(rowIndex, colIndex, position));
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
				final Point point = new Point(e.getX(), e.getY());
				final Position position = PositionConvert.convert(point);

				int columnIndex = table.getTableHeader().getColumnModel().getColumnIndexAtX(e.getX());
				columnIndex = table.convertColumnIndexToModel(columnIndex);
				popupDetectionObservable.firePopupDetected(position);
				tableColumnPopupDetectionObservable.firePopupDetected(new TableColumnPopupEvent(columnIndex, position));
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
				tableModel.setSelection(getSelection());
				tableSelectionObservable.fireSelectionChanged();
			}
		}
	}

	final class TableModelListener implements ITableModelListener {

		@Override
		public void rowsAdded(final int[] rowIndices) {
			// TODO MG better implementation of rowsAdded observe
			rowsStructureChanged();
		}

		@Override
		public void rowsRemoved(final int[] rowIndices) {
			// TODO MG better implementation of rowsRemoved observe
			rowsStructureChanged();
		}

		@Override
		public void rowsChanged(final int[] rowIndices) {
			// TODO MG implement rowsChanged observe
		}

		@Override
		public void rowsStructureChanged() {
			// TODO MG implement rowsChanged observe
		}

		@Override
		public void selectionChanged() {
			setSelection(tableModel.getSelection());
		}

	}

	final class TableColumnsModelListener implements ITableColumnModelListener {

		@Override
		public void columnsAdded(final int[] columnIndices) {
			// TODO MG better implementation of columnsAdded observe
			columnsStructureChanged();
		}

		@Override
		public void columnsRemoved(final int[] columnIndices) {
			// TODO MG better implementation of columnsRemoved observe
			columnsStructureChanged();
		}

		@Override
		public void columnsChanged(final int[] columnIndices) {
			// TODO MG implement columnsChanged observe
		}

		@Override
		public void columnsStructureChanged() {
			// TODO MG implement columnsStructureChanged observe
		}

	}

	class SwingTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 6096723765272552285L;

		private final ITableModel tableModel;
		private final ITableColumnModel columnModel;

		public SwingTableModel(final ITableModel tableModel, final ITableColumnModel columnModel) {
			super();
			this.tableModel = tableModel;
			this.columnModel = columnModel;
		}

		@Override
		public String getColumnName(final int column) {
			return columnModel.getColumn(column).getText();
		}

		@Override
		public Object getValueAt(final int rowIndex, final int columnIndex) {
			return tableModel.getCell(rowIndex, columnIndex);
		}

		@Override
		public int getRowCount() {
			return tableModel.getRowCount();
		}

		@Override
		public int getColumnCount() {
			return columnModel.getColumnCount();
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

			final ITableColumn column = columnModel.getColumn(table.convertColumnIndexToModel(columnIndex));

			if (column.getIcon() != null) {
				defaultComponent.setIcon(SwingImageRegistry.getInstance().getImageIcon(column.getIcon()));
			}
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

}
