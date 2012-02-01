/*
 * Copyright (c) 2011, grossmann, Nikolaus Moll
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
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelListener;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.model.ITableColumnModelSpi;
import org.jowidgets.common.model.ITableColumnSpi;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.model.ITableDataModelListener;
import org.jowidgets.common.model.ITableDataModelObservable;
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.types.MouseButton;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.TablePackPolicy;
import org.jowidgets.common.types.TableSelectionPolicy;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableCellEditEvent;
import org.jowidgets.common.widgets.controller.ITableCellEditorListener;
import org.jowidgets.common.widgets.controller.ITableCellEvent;
import org.jowidgets.common.widgets.controller.ITableCellListener;
import org.jowidgets.common.widgets.controller.ITableCellMouseEvent;
import org.jowidgets.common.widgets.controller.ITableCellPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableColumnListener;
import org.jowidgets.common.widgets.controller.ITableColumnMouseEvent;
import org.jowidgets.common.widgets.controller.ITableColumnPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableSelectionListener;
import org.jowidgets.spi.impl.controller.FocusObservable;
import org.jowidgets.spi.impl.controller.IObservableCallback;
import org.jowidgets.spi.impl.controller.KeyObservable;
import org.jowidgets.spi.impl.controller.PopupDetectionObservable;
import org.jowidgets.spi.impl.controller.TableCellEditEvent;
import org.jowidgets.spi.impl.controller.TableCellEditorObservable;
import org.jowidgets.spi.impl.controller.TableCellEvent;
import org.jowidgets.spi.impl.controller.TableCellMouseEvent;
import org.jowidgets.spi.impl.controller.TableCellObservable;
import org.jowidgets.spi.impl.controller.TableCellPopupDetectionObservable;
import org.jowidgets.spi.impl.controller.TableCellPopupEvent;
import org.jowidgets.spi.impl.controller.TableColumnMouseEvent;
import org.jowidgets.spi.impl.controller.TableColumnObservable;
import org.jowidgets.spi.impl.controller.TableColumnPopupDetectionObservable;
import org.jowidgets.spi.impl.controller.TableColumnPopupEvent;
import org.jowidgets.spi.impl.controller.TableColumnResizeEvent;
import org.jowidgets.spi.impl.controller.TableSelectionObservable;
import org.jowidgets.spi.impl.swing.image.SwingImageRegistry;
import org.jowidgets.spi.impl.swing.util.AlignmentConvert;
import org.jowidgets.spi.impl.swing.util.ColorConvert;
import org.jowidgets.spi.impl.swing.util.FontProvider;
import org.jowidgets.spi.impl.swing.util.MouseUtil;
import org.jowidgets.spi.impl.swing.util.PositionConvert;
import org.jowidgets.spi.impl.swing.widgets.base.TableColumnModelAdapter;
import org.jowidgets.spi.impl.swing.widgets.event.LazyKeyEventContentFactory;
import org.jowidgets.spi.widgets.ITableSpi;
import org.jowidgets.spi.widgets.setup.ITableSetupSpi;
import org.jowidgets.util.ArrayUtils;
import org.jowidgets.util.Assert;

public class TableImpl extends SwingControl implements ITableSpi {

	private final JTable table;
	private final ITableDataModel dataModel;
	private final ITableColumnModelSpi columnModel;
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
	private final FocusObservable focusObservable;
	private final KeyObservable keyObservable;
	private final KeyListener keyListener;

	private final TableColumnResizeListener tableColumnResizeListener;
	private final TableSelectionListener tableSelectionListener;
	private final TableModelListener tableModelListener;
	private final TableColumnModelListener tableColumnModelListener;
	private final TableCellMenuDetectListener tableCellMenuDetectListener;
	private final TableColumnListener tableColumnListener;
	private final TableColumnMoveListener tableColumnMoveListener;

	private final boolean columnsResizeable;
	private final boolean hasBorder;

	private SwingTableModel swingTableModel;
	private ArrayList<Integer> lastColumnPermutation;
	private boolean columnMoveOccured;
	private boolean setWidthInvokedOnModel;
	private boolean editable;

	public TableImpl(final ITableSetupSpi setup) {
		super(new JScrollPane(new JTable()));

		this.editable = true;

		this.cellRenderer = new CellRenderer();
		this.cellEditor = new CellEditor();

		this.popupDetectionObservable = new PopupDetectionObservable();
		this.tableCellObservable = new TableCellObservable();
		this.tableCellPopupDetectionObservable = new TableCellPopupDetectionObservable();
		this.tableColumnPopupDetectionObservable = new TableColumnPopupDetectionObservable();
		this.tableColumnObservable = new TableColumnObservable();
		this.tableSelectionObservable = new TableSelectionObservable();
		this.tableCellEditorObservable = new TableCellEditorObservable();
		this.focusObservable = new FocusObservable();

		this.tableColumnResizeListener = new TableColumnResizeListener();
		this.tableSelectionListener = new TableSelectionListener();
		this.tableModelListener = new TableModelListener();
		this.tableColumnModelListener = new TableColumnModelListener();
		this.tableCellMenuDetectListener = new TableCellMenuDetectListener();
		this.tableColumnListener = new TableColumnListener();
		this.tableColumnMoveListener = new TableColumnMoveListener();

		this.columnMoveOccured = false;
		this.setWidthInvokedOnModel = false;

		this.dataModel = setup.getDataModel();
		this.columnModel = setup.getColumnModel();

		this.columnsResizeable = setup.getColumnsResizeable();

		this.hasBorder = setup.hasBorder();

		if (!hasBorder) {
			getUiReference().setBorder(BorderFactory.createEmptyBorder());
		}

		this.table = (JTable) getUiReference().getViewport().getView();
		table.setAutoCreateColumnsFromModel(false);
		this.headerRenderer = new TableHeaderRenderer();

		table.setBorder(BorderFactory.createEmptyBorder());
		table.setGridColor(new Color(230, 230, 230));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(setup.getColumnsMoveable());

		if (setup.getSelectionPolicy() == TableSelectionPolicy.SINGLE_ROW_SELECTION) {
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		else if (setup.getSelectionPolicy() == TableSelectionPolicy.MULTI_ROW_SELECTION) {
			table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
		else if (setup.getSelectionPolicy() == TableSelectionPolicy.NO_SELECTION) {
			table.setRowSelectionAllowed(false);
			table.setColumnSelectionAllowed(false);
		}
		else {
			throw new IllegalArgumentException("SelectionPolicy '" + setup.getSelectionPolicy() + "' is not known");
		}

		if (!setup.isHeaderVisible()) {
			table.getTableHeader().setVisible(false);
			table.getTableHeader().setPreferredSize(new java.awt.Dimension(-1, 0));
		}

		table.getTableHeader().setDefaultRenderer(headerRenderer);
		table.getTableHeader().addMouseListener(tableColumnListener);
		table.getTableHeader().addMouseListener(new TableColumnMenuDetectListener());

		table.getColumnModel().addColumnModelListener(tableColumnMoveListener);
		table.getSelectionModel().addListSelectionListener(tableSelectionListener);

		table.addMouseListener(new TableCellListener());
		table.addMouseListener(tableCellMenuDetectListener);

		table.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(final FocusEvent e) {
				focusObservable.focusLost();
			}

			@Override
			public void focusGained(final FocusEvent e) {
				focusObservable.focusGained();
			}
		});

		this.keyListener = new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				keyObservable.fireKeyReleased(new LazyKeyEventContentFactory(e));
			}

			@Override
			public void keyPressed(final KeyEvent e) {
				keyObservable.fireKeyPressed(new LazyKeyEventContentFactory(e));
			}
		};

		final IObservableCallback keyObservableCallback = new IObservableCallback() {
			@Override
			public void onLastUnregistered() {
				table.removeKeyListener(keyListener);
			}

			@Override
			public void onFirstRegistered() {
				table.addKeyListener(keyListener);
			}
		};

		this.keyObservable = new KeyObservable(keyObservableCallback);

		getUiReference().addMouseListener(tableCellMenuDetectListener);
	}

	@Override
	public JScrollPane getUiReference() {
		return (JScrollPane) super.getUiReference();
	}

	@Override
	public void setEditable(final boolean editable) {
		this.editable = editable;
	}

	@Override
	public Dimension getPreferredSize() {
		final java.awt.Dimension preferredSize = table.getPreferredSize();
		final java.awt.Dimension headerSize = table.getTableHeader().getPreferredSize();
		final Insets insets = table.getBorder().getBorderInsets(table);
		int width = preferredSize.width + insets.left + insets.right;
		int height = preferredSize.height + insets.top + insets.bottom + headerSize.height;

		final Insets spInsets = getUiReference().getInsets();

		width = width + spInsets.left + spInsets.right;
		height = height + spInsets.bottom + spInsets.top;

		return new Dimension(width, height);
	}

	@Override
	public Dimension getMinSize() {
		final java.awt.Dimension minSize = getUiReference().getMinimumSize();

		final java.awt.Dimension headerSize = table.getTableHeader().getMinimumSize();

		final Insets insets = table.getBorder().getBorderInsets(table);
		int width = minSize.width + insets.left + insets.right;
		int height = minSize.height + insets.top + insets.bottom + (int) headerSize.getHeight();

		final Insets spInsets = getUiReference().getInsets();

		width = width + spInsets.left + spInsets.right + 17;
		height = height + spInsets.bottom + spInsets.top + 17;

		return new Dimension(width, height);
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
	public void resetFromModel() {
		//unregister listeners
		final ITableDataModelObservable dataModelObservable = dataModel.getTableDataModelObservable();
		if (dataModelObservable != null) {
			dataModelObservable.removeDataModelListener(tableModelListener);
		}
		final ITableColumnModelObservable columnModelObservable = columnModel.getTableColumnModelObservable();
		if (columnModelObservable != null) {
			columnModelObservable.removeColumnModelListener(tableColumnModelListener);
		}
		table.getSelectionModel().removeListSelectionListener(tableSelectionListener);

		//remove all old columns
		final TableColumnModel swingColumnModel = table.getColumnModel();
		final int oldColumnCount = swingColumnModel.getColumnCount();
		for (int columnIndex = 0; columnIndex < oldColumnCount; columnIndex++) {
			swingColumnModel.removeColumn(swingColumnModel.getColumn(0));
		}

		//set a new model
		this.swingTableModel = new SwingTableModel();
		table.setModel(swingTableModel);

		//add all new columns
		final int columnCount = columnModel.getColumnCount();
		lastColumnPermutation = new ArrayList<Integer>(columnCount);
		for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
			table.getColumnModel().addColumn(createColumn(columnIndex));
			lastColumnPermutation.add(Integer.valueOf(columnIndex));
		}

		//set the current selection
		setSelection(dataModel.getSelection());

		//register listeners
		if (dataModelObservable != null) {
			dataModelObservable.addDataModelListener(tableModelListener);
		}
		if (columnModelObservable != null) {
			columnModelObservable.addColumnModelListener(tableColumnModelListener);
		}
		table.getSelectionModel().addListSelectionListener(tableSelectionListener);

		//make changes appear in table view
		table.repaint();
	}

	private TableColumn createColumn(final int modelIndex) {
		final TableColumn column = new TableColumn();
		column.setHeaderValue(columnModel.getColumn(modelIndex));
		column.setCellRenderer(cellRenderer);
		column.setCellEditor(cellEditor);
		column.setResizable(columnsResizeable);
		column.addPropertyChangeListener(tableColumnResizeListener);
		column.setPreferredWidth(columnModel.getColumn(modelIndex).getWidth());
		column.setModelIndex(modelIndex);
		return column;
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
	public void pack(final TablePackPolicy policy) {
		for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
			pack(columnIndex, getRowRange(policy), policy);
		}
	}

	@Override
	public void pack(final int columnIndex, final TablePackPolicy policy) {
		pack(columnIndex, getRowRange(policy), policy);
	}

	private void pack(final int columnIndex, final RowRange rowRange, final TablePackPolicy policy) {

		final TableColumn column = table.getColumnModel().getColumn(table.convertColumnIndexToView(columnIndex));

		int maxWidth = 0;

		if (policy.considerData()) {
			final int viewColumn = table.convertColumnIndexToView(columnIndex);
			for (int rowIndex = rowRange.getStartIndex(); rowIndex <= rowRange.getEndIndex(); rowIndex++) {
				final Object value = table.getValueAt(rowIndex, viewColumn);
				final TableCellRenderer renderer = column.getCellRenderer();
				final Component comp = renderer.getTableCellRendererComponent(table, value, false, false, rowIndex, columnIndex);
				maxWidth = Math.max(maxWidth, (int) comp.getPreferredSize().getWidth());
			}
		}

		if (policy.considerHeader()) {
			final Object value = column.getHeaderValue();
			final TableCellRenderer renderer = table.getTableHeader().getDefaultRenderer();
			final Component comp = renderer.getTableCellRendererComponent(table, value, false, false, 0, columnIndex);
			maxWidth = Math.max(maxWidth, (int) comp.getPreferredSize().getWidth());
		}

		column.setPreferredWidth(maxWidth + 5);
	}

	private RowRange getRowRange(final TablePackPolicy policy) {
		if (policy.considerAllData()) {
			return new RowRange(0, table.getRowCount() - 1);
		}
		else {
			final Rectangle viewRect = getUiReference().getViewport().getViewRect();
			final int firstVisibleRowIndex = Math.max(table.rowAtPoint(new Point(0, viewRect.y)), 0);
			int lastVisibleRowIndex = table.rowAtPoint(new Point(0, viewRect.y + viewRect.height - 1));
			if (lastVisibleRowIndex == -1) {
				lastVisibleRowIndex = table.getRowCount() - 1;
			}
			return new RowRange(firstVisibleRowIndex, lastVisibleRowIndex);
		}
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
	public void showSelection() {
		final int selectedRow = table.getSelectedRow();
		if (selectedRow != -1) {
			final Rectangle rectangle = table.getCellRect(selectedRow, 0, false);
			final Rectangle visibleRectangle = table.getVisibleRect();
			if (!visibleRectangle.contains(rectangle)) {
				table.scrollRectToVisible(rectangle);
			}
		}
	}

	@Override
	public boolean isColumnPopupDetectionSupported() {
		return true;
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		super.addKeyListener(listener);
		keyObservable.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		super.removeKeyListener(listener);
		keyObservable.removeKeyListener(listener);
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		super.addFocusListener(listener);
		focusObservable.addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		super.removeFocusListener(listener);
		focusObservable.removeFocusListener(listener);
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

			final MouseButton mouseButton = MouseUtil.getMouseButton(event);
			if (mouseButton == null) {
				return null;
			}

			final CellIndices indices = getCellIndices(event);
			if (indices != null) {
				return new TableCellMouseEvent(
					indices.getRowIndex(),
					indices.getColumnIndex(),
					mouseButton,
					MouseUtil.getModifier(event));
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
				final ITableColumnMouseEvent mouseEvent = new TableColumnMouseEvent(columnIndex, MouseUtil.getModifier(e));
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
			if ("width".equals(evt.getPropertyName())) {
				final TableColumn column = (TableColumn) evt.getSource();
				if (column != null) {
					final Object newObject = evt.getNewValue();
					if (newObject instanceof Integer) {
						final int newWidth = ((Integer) newObject).intValue();
						setWidthInvokedOnModel = true;
						columnModel.getColumn(column.getModelIndex()).setWidth(newWidth);
						setWidthInvokedOnModel = false;
						tableColumnObservable.fireColumnResized(new TableColumnResizeEvent(column.getModelIndex(), newWidth));
					}
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

			//Trivial case. If nothing added return
			if (columnIndices.length == 0) {
				return;
			}

			//Sort the indices to add
			Arrays.sort(columnIndices);

			//Merge the new columns into the current columns
			int addedColumns = 0;
			int nextIndexToAdd = columnIndices[0];
			final TableColumn[] columns = getColumnsSortedByModelIndex();
			for (int columnIndex = 0; columnIndex < columns.length; columnIndex++) {
				if (columnIndex == nextIndexToAdd) {
					final TableColumn column = createColumn(columnIndex);
					addedColumns++;
					table.addColumn(column);
					table.moveColumn(table.getColumnCount() - 1, columnIndex);
					if (addedColumns < columnIndices.length) {
						nextIndexToAdd = columnIndices[addedColumns];
					}
					else {
						nextIndexToAdd = -1;
					}
				}
				//Fix the index in the model. For each previously added row, the
				//index must be increased by one.
				final TableColumn swingColumn = columns[columnIndex];
				swingColumn.setModelIndex(swingColumn.getModelIndex() + addedColumns);
			}
			//Then append the residual columns
			while (nextIndexToAdd != -1) {
				final TableColumn column = createColumn(nextIndexToAdd);
				addedColumns++;
				table.addColumn(column);
				if (addedColumns < columnIndices.length) {
					nextIndexToAdd = columnIndices[addedColumns];
				}
				else {
					nextIndexToAdd = -1;
				}
			}

			//Make changes appear on table
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
			if (!setWidthInvokedOnModel) {
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

			final ITableColumnSpi column = (ITableColumnSpi) value;

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
				final AlignmentHorizontal alignment = columnModel.getColumn(table.convertColumnIndexToModel(column)).getAlignment();

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
			textField.setDocument(new TableCellDocument(row, column));

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
		}

		@Override
		public boolean isCellEditable(final EventObject evt) {
			if (!editable) {
				return false;
			}
			else if (evt instanceof MouseEvent) {
				return ((MouseEvent) evt).getClickCount() >= 2;
			}
			else {
				return false;
			}
		}

	}

	/**
	 * This class implements the editor veto capabilities.
	 * Limitation: If selected text is replaced by new text, and new input fails, selected text stays removed and won't be
	 * restored
	 */
	final class TableCellDocument extends PlainDocument {

		private static final long serialVersionUID = -3417284499762627374L;
		private final int row;
		private final int column;

		public TableCellDocument(final int row, final int column) {
			this.row = row;
			this.column = column;
		}

		@Override
		public void remove(final int offset, final int length) throws BadLocationException {
			final String text = getText(0, offset) + getText(offset + length, getLength() - (offset + length));
			if (!fireEditEvent(text)) {
				super.remove(offset, length);
			}
		}

		@Override
		public void insertString(final int offset, final String str, final AttributeSet a) throws BadLocationException {
			final String text = getText(0, offset) + str + getText(offset, getLength() - offset);
			if (!fireEditEvent(text)) {
				super.insertString(offset, str, a);
			}
		}

		private boolean fireEditEvent(final String text) {
			final ITableCellEditEvent editEvent = new TableCellEditEvent(row, column, text);
			return tableCellEditorObservable.fireOnEdit(editEvent);
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
