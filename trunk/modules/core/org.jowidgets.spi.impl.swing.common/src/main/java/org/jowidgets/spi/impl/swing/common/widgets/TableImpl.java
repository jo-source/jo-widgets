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

package org.jowidgets.spi.impl.swing.common.widgets;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
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
import java.util.Collections;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

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
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.MouseButton;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.TablePackPolicy;
import org.jowidgets.common.types.TableSelectionPolicy;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyEvent;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableCellListener;
import org.jowidgets.common.widgets.controller.ITableCellMouseEvent;
import org.jowidgets.common.widgets.controller.ITableCellPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableColumnListener;
import org.jowidgets.common.widgets.controller.ITableColumnMouseEvent;
import org.jowidgets.common.widgets.controller.ITableColumnPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableSelectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.editor.EditActivation;
import org.jowidgets.common.widgets.editor.ITableCellEditor;
import org.jowidgets.common.widgets.editor.ITableCellEditorFactory;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.impl.controller.FocusObservable;
import org.jowidgets.spi.impl.controller.KeyObservable;
import org.jowidgets.spi.impl.controller.PopupDetectionObservable;
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
import org.jowidgets.spi.impl.swing.common.image.SwingImageRegistry;
import org.jowidgets.spi.impl.swing.common.options.SwingOptions;
import org.jowidgets.spi.impl.swing.common.util.AlignmentConvert;
import org.jowidgets.spi.impl.swing.common.util.ColorConvert;
import org.jowidgets.spi.impl.swing.common.util.FontProvider;
import org.jowidgets.spi.impl.swing.common.util.MouseUtil;
import org.jowidgets.spi.impl.swing.common.util.PositionConvert;
import org.jowidgets.spi.impl.swing.common.widgets.base.SwingActionWrapper;
import org.jowidgets.spi.impl.swing.common.widgets.base.TableColumnModelAdapter;
import org.jowidgets.spi.impl.swing.common.widgets.event.LazyKeyEventContentFactory;
import org.jowidgets.spi.widgets.ITableSpi;
import org.jowidgets.spi.widgets.setup.ITableSetupSpi;
import org.jowidgets.util.ArrayUtils;
import org.jowidgets.util.Assert;
import org.jowidgets.util.Interval;
import org.jowidgets.util.ValueHolder;
import org.jowidgets.util.event.IObservableCallback;

public class TableImpl extends SwingControl implements ITableSpi {

	private final IGenericWidgetFactory factory;
	private final ICustomWidgetFactory editorCustomWidgetFactory;

	private final JTable table;
	private final ITableDataModel dataModel;
	private final ITableColumnModelSpi columnModel;
	private final CellRenderer cellRenderer;
	private final EditorFactoryBasedCellEditor cellEditor;
	private final TableCellRenderer headerRenderer;

	private final PopupDetectionObservable popupDetectionObservable;
	private final TableCellObservable tableCellObservable;
	private final TableCellPopupDetectionObservable tableCellPopupDetectionObservable;
	private final TableColumnPopupDetectionObservable tableColumnPopupDetectionObservable;
	private final TableColumnObservable tableColumnObservable;
	private final TableSelectionObservable tableSelectionObservable;
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
	private boolean programmaticClearSelection;

	public TableImpl(final IGenericWidgetFactory factory, final ITableSetupSpi setup) {
		super(new JScrollPane(new JTable()));

		this.factory = factory;
		this.editorCustomWidgetFactory = new EditorCustomWidgetFactory();
		this.editable = true;

		this.cellRenderer = new CellRenderer();

		if (setup.getEditor() != null) {
			this.cellEditor = new EditorFactoryBasedCellEditor(setup.getEditor());
		}
		else {
			this.cellEditor = null;
		}

		this.popupDetectionObservable = new PopupDetectionObservable();
		this.tableCellObservable = new TableCellObservable();
		this.tableCellPopupDetectionObservable = new TableCellPopupDetectionObservable();
		this.tableColumnPopupDetectionObservable = new TableColumnPopupDetectionObservable();
		this.tableColumnObservable = new TableColumnObservable();
		this.tableSelectionObservable = new TableSelectionObservable();
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

		if (!SwingOptions.isDefaultTableTransferHandler()) {
			table.setTransferHandler(null);
		}

		table.setSurrendersFocusOnKeystroke(true);

		getUiReference().addMouseListener(tableCellMenuDetectListener);
		getUiReference().setBorder(BorderFactory.createEmptyBorder());
		getUiReference().setViewportBorder(BorderFactory.createEmptyBorder());

		//disable default table key actions during editing
		final ActionMap actionMap = table.getActionMap();
		for (final Object actionKey : actionMap.allKeys()) {
			Action originalAction = actionMap.get(actionMap);
			if (originalAction == null) {
				originalAction = actionMap.getParent().get(actionKey);
			}
			final Action originalActionFinal = originalAction;
			if (originalActionFinal != null) {
				actionMap.put(actionKey, new SwingActionWrapper(originalAction) {

					private static final long serialVersionUID = -5138227823410592157L;

					@Override
					public boolean isEnabled() {
						if (isEditing()) {
							return false;
						}
						else {
							return originalActionFinal.isEnabled();
						}
					}

				});
			}
		}

		final KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enterKey, "Action.enter");
		table.getActionMap().put("Action.enter", new AbstractAction() {

			private static final long serialVersionUID = -6159891640633692648L;

			@Override
			public void actionPerformed(final ActionEvent evt) {
				final int selectedRow = table.getSelectedRow();
				final int selectedColumn = table.getSelectedColumn();
				editCell(selectedRow, table.convertColumnIndexToModel(selectedColumn));
			}
		});

	}

	@Override
	public JScrollPane getUiReference() {
		return (JScrollPane) super.getUiReference();
	}

	@Override
	public boolean editCell(final int row, final int column) {
		if (isEditing()) {
			stopEditing();
		}
		final int viewColumnIndex = table.convertColumnIndexToView(column);
		final StartEditEvent event = new StartEditEvent(table, row, viewColumnIndex);
		return table.editCellAt(row, viewColumnIndex, event);
	}

	@Override
	public void stopEditing() {
		if (cellEditor != null) {
			cellEditor.stopCellEditing();
			table.editingStopped(null);
		}
	}

	@Override
	public void cancelEditing() {
		if (cellEditor != null) {
			cellEditor.cancelCellEditing();
			table.editingCanceled(null);
		}
	}

	@Override
	public boolean isEditing() {
		if (cellEditor != null) {
			return cellEditor.isEditing();
		}
		else {
			return false;
		}
	}

	@Override
	public void setRowHeight(final int height) {
		table.setRowHeight(height);
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

		maxWidth = maxWidth + 5;

		column.setPreferredWidth(maxWidth);

		setWidthInvokedOnModel = true;
		columnModel.getColumn(column.getModelIndex()).setWidth(maxWidth);
		setWidthInvokedOnModel = false;
		tableColumnObservable.fireColumnResized(new TableColumnResizeEvent(column.getModelIndex(), maxWidth));
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

			if (selection != null && selection.size() > 0) {
				programmaticClearSelection = true;
				selectionModel.clearSelection();
				programmaticClearSelection = false;
				for (int i = 0; i < selection.size(); i++) {
					if (i == selection.size() - 1) {
						selectionModel.setValueIsAdjusting(false);
					}
					final int selectionIndex = selection.get(i);
					selectionModel.addSelectionInterval(selectionIndex, selectionIndex);
				}
			}
			else {
				selectionModel.clearSelection();
			}
		}
	}

	private void scrollToCell(final int rowIndex, final int columnIndex) {
		Assert.paramInBounds(table.getRowCount() - 1, rowIndex, "rowIndex");
		Assert.paramInBounds(table.getColumnCount() - 1, columnIndex, "columnIndex");
		final Rectangle rectangle = table.getCellRect(rowIndex, columnIndex, false);
		final Rectangle visibleRectangle = table.getVisibleRect();
		if (!visibleRectangle.contains(rectangle)) {
			table.scrollRectToVisible(rectangle);
		}
	}

	@Override
	public void scrollToRow(final int rowIndex) {
		Assert.paramInBounds(table.getRowCount() - 1, rowIndex, "rowIndex");
		final Rectangle rectangle = table.getCellRect(rowIndex, 0, false);
		final Rectangle visibleRectangle = table.getVisibleRect();
		if (!visibleRectangle.contains(rectangle)) {
			table.scrollRectToVisible(rectangle);
		}
	}

	@Override
	public boolean isColumnPopupDetectionSupported() {
		return true;
	}

	@Override
	public Interval<Integer> getVisibleRows() {
		if (dataModel.getRowCount() > 0) {
			final Rectangle visibleRect = table.getVisibleRect();
			if (visibleRect.height >= table.getRowHeight() - 1) {
				final Point firstPoint = new Point(0, visibleRect.y);
				final Point secondPoint = new Point(0, visibleRect.y + visibleRect.height - 1);
				final int leftBoundary = table.rowAtPoint(firstPoint);
				final int rightBoundary = table.rowAtPoint(secondPoint);
				return new Interval<Integer>(leftBoundary, rightBoundary);
			}
		}
		return new Interval<Integer>(null, null);
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
				stopEditing();
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
			if (!e.getValueIsAdjusting() && !programmaticClearSelection) {
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
					final int viewWidth = table.getColumnModel().getColumn(modelIndex).getPreferredWidth();
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

	final class EditorFactoryBasedCellEditor extends AbstractCellEditor implements TableCellEditor {

		private static final long serialVersionUID = 3420610701760793714L;

		private final ITableCellEditorFactory<? extends ITableCellEditor> editorFactory;

		private ITableCellEditor tableCellEditor;
		private int column;
		private int row;
		private long stopEditTimestamp;

		public EditorFactoryBasedCellEditor(final ITableCellEditorFactory<? extends ITableCellEditor> editorFactory) {
			this.editorFactory = editorFactory;
		}

		public boolean isEditing() {
			return tableCellEditor != null;
		}

		@Override
		public Object getCellEditorValue() {
			return null;
		}

		@Override
		public boolean isCellEditable(final EventObject event) {
			if (event == null) {
				return true;
			}
			else if (event instanceof StartEditEvent) {
				final StartEditEvent startEditEvent = (StartEditEvent) event;
				final int rowIndex = startEditEvent.getRow();
				final int columnIndex = table.convertColumnIndexToModel(startEditEvent.getColum());
				final ITableCell cell = dataModel.getCell(rowIndex, columnIndex);
				return cell.isEditable() && editable;
			}
			else if (event instanceof MouseEvent) {
				final MouseEvent mouseEvent = (MouseEvent) event;
				final CellIndices cellIndices = getCellIndices(mouseEvent);
				final ITableCell cell = dataModel.getCell(cellIndices.getRowIndex(), cellIndices.getColumnIndex());

				if (!cell.isEditable() || !editable) {
					return false;
				}

				final EditActivation activation = editorFactory.getActivation(
						cell,
						cellIndices.getRowIndex(),
						cellIndices.getColumnIndex(),
						isEditing(),
						stopEditTimestamp);
				if (EditActivation.DOUBLE_CLICK.equals(activation)) {
					return SwingUtilities.isLeftMouseButton(mouseEvent) && mouseEvent.getClickCount() == 2;
				}
				else if (EditActivation.SINGLE_CLICK.equals(activation)) {
					return SwingUtilities.isLeftMouseButton(mouseEvent) && mouseEvent.getClickCount() == 1;
				}
			}
			else if (event instanceof KeyEvent) {
				if (isEditing() || dataModel.getRowCount() == 0) {
					return false;
				}
				final KeyEvent keyEvent = (KeyEvent) event;
				if ((keyEvent.getKeyCode() == KeyEvent.VK_ENTER)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean shouldSelectCell(final EventObject anEvent) {
			return true;
		}

		@Override
		public boolean stopCellEditing() {
			if (tableCellEditor != null && this.row != -1 && this.column != -1) {
				tableCellEditor.stopEditing(dataModel.getCell(this.row, column), this.row, this.column);
				//Workaround to avoid getting exceptions on comboxes that want to
				//show their popup when navigated to another cell vio arrow up or down
				((JComponent) tableCellEditor.getUiReference()).setEnabled(false);
			}
			this.tableCellEditor = null;
			this.row = -1;
			this.column = -1;
			this.stopEditTimestamp = System.currentTimeMillis();
			fireEditingStopped();
			return true;
		}

		@Override
		public void cancelCellEditing() {
			if (tableCellEditor != null && row != -1 && column != -1) {
				tableCellEditor.cancelEditing(dataModel.getCell(this.row, column), this.row, this.column);
			}
			this.tableCellEditor = null;
			this.row = -1;
			this.column = -1;
			this.stopEditTimestamp = 0;
			fireEditingCanceled();
		}

		@Override
		public Component getTableCellEditorComponent(
			final JTable table,
			final Object value,
			final boolean isSelected,
			final int row,
			final int viewColumn) {

			if (isEditing()) {
				stopCellEditing();
			}

			this.row = row;
			this.column = table.convertColumnIndexToModel(viewColumn);
			final ITableCell tableCell = dataModel.getCell(row, column);

			this.tableCellEditor = editorFactory.create(tableCell, row, column, editorCustomWidgetFactory);

			if (tableCellEditor == null) {
				this.row = -1;
				this.column = -1;
				return null;
			}

			tableCellEditor.addKeyListener(new IKeyListener() {

				@Override
				public void keyReleased(final IKeyEvent event) {}

				@Override
				public void keyPressed(final IKeyEvent event) {

					final boolean ctrl = event.getModifier().contains(Modifier.CTRL);
					final boolean shift = event.getModifier().contains(Modifier.SHIFT);

					final boolean enter = VirtualKey.ENTER.equals(event.getVirtualKey());
					final boolean esc = VirtualKey.ESC.equals(event.getVirtualKey());

					boolean left = VirtualKey.ARROW_LEFT.equals(event.getVirtualKey()) && shift;
					left = left || (VirtualKey.TAB.equals(event.getVirtualKey()) && shift);

					boolean right = VirtualKey.ARROW_RIGHT.equals(event.getVirtualKey()) && shift;
					right = right || (VirtualKey.TAB.equals(event.getVirtualKey()) && !shift);

					final boolean up = VirtualKey.ARROW_UP.equals(event.getVirtualKey()) && shift;

					final boolean down = VirtualKey.ARROW_DOWN.equals(event.getVirtualKey()) && shift;

					if (enter && ctrl) {
						stopEditing();
					}
					if (enter) {
						navigateDownLeft();
					}
					else if (esc) {
						cancelEditing();
					}
					else if (right) {
						navigateRight();
					}
					else if (left) {
						navigateLeft();
					}
					else if (up) {
						navigateUp();
					}
					else if (down) {
						navigateDown();
					}
				}
			});

			tableCellEditor.startEditing(tableCell, row, column);

			final int height = tableCellEditor.getPreferredSize().getHeight() + 1;

			if (table.getRowHeight() < height) {
				table.setRowHeight(height);
			}

			final JComponent component = (JComponent) tableCellEditor.getUiReference();

			//ensure that cell editor get the focus when swing auto focus gives the focus to the
			//component, but the component is a container that wraps other input controls
			final ValueHolder<FocusListener> focusListenerHolder = new ValueHolder<FocusListener>();
			final FocusAdapter focusListener = new FocusAdapter() {
				@Override
				public void focusGained(final FocusEvent e) {
					component.removeFocusListener(focusListenerHolder.get());
					if (tableCellEditor != null) {
						tableCellEditor.requestFocus();
					}
				}
			};
			focusListenerHolder.set(focusListener);
			component.addFocusListener(focusListener);

			return component;
		}

		private boolean navigateRight() {
			if (isEditing()) {
				return navigateRight(row, row, convertColumnIndexToView(column));
			}
			else {
				return false;
			}
		}

		private boolean navigateDown() {
			if (isEditing()) {
				return navigateDown(row, row, convertColumnIndexToView(column));
			}
			else {
				return false;
			}
		}

		private boolean navigateLeft() {
			if (isEditing()) {
				return navigateLeft(row, row, convertColumnIndexToView(column));
			}
			else {
				return false;
			}
		}

		private boolean navigateUp() {
			if (isEditing()) {
				return navigateUp(row, row, convertColumnIndexToView(column));
			}
			else {
				return false;
			}
		}

		private boolean navigateDownLeft() {
			if (isEditing()) {
				return navigateDown(row, row, 0);
			}
			else {
				return false;
			}
		}

		private boolean navigateRight(final int startRow, final int row, final int viewColumnIndex) {
			if (viewColumnIndex + 1 < columnModel.getColumnCount()) {
				if (editCell(row, convertColumnIndexToModel(viewColumnIndex + 1))) {
					scrollToCell(row, convertColumnIndexToModel(viewColumnIndex + 1));
					setSelection(Collections.singletonList(Integer.valueOf(row)));
					return true;
				}
				else {
					return navigateRight(startRow, row, viewColumnIndex + 1);
				}
			}
			else if (row - startRow < 2) {
				return navigateDown(startRow, row, 0);
			}
			else {
				return false;
			}
		}

		private boolean navigateDown(final int startRow, final int row, final int viewColumnIndex) {
			if (dataModel.getRowCount() > row + 1) {
				if (editCell(row + 1, convertColumnIndexToModel(viewColumnIndex))) {
					scrollToCell(row + 1, convertColumnIndexToModel(viewColumnIndex));
					setSelection(Collections.singletonList(Integer.valueOf(row + 1)));
					return true;
				}
				else if (row - startRow < 2) {
					setSelection(Collections.singletonList(Integer.valueOf(row + 1)));
					return navigateRight(startRow, row + 1, viewColumnIndex);
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}

		private boolean navigateLeft(final int startRow, final int row, final int viewColumnIndex) {
			if (viewColumnIndex > 0) {
				if (editCell(row, convertColumnIndexToModel(viewColumnIndex - 1))) {
					scrollToCell(row, convertColumnIndexToModel(viewColumnIndex - 1));
					setSelection(Collections.singletonList(Integer.valueOf(row)));
					return true;
				}
				else {
					return navigateLeft(startRow, row, viewColumnIndex - 1);
				}
			}
			else if (startRow - row < 2) {
				return navigateUp(startRow, row, columnModel.getColumnCount() - 1);
			}
			else {
				return false;
			}
		}

		private boolean navigateUp(final int startRow, final int row, final int viewColumnIndex) {
			if (row > 0) {
				if (editCell(row - 1, convertColumnIndexToModel(viewColumnIndex))) {
					scrollToCell(row - 1, convertColumnIndexToModel(viewColumnIndex));
					setSelection(Collections.singletonList(Integer.valueOf(row - 1)));
					return true;
				}
				else if (startRow - row < 2) {
					return navigateLeft(startRow, row - 1, viewColumnIndex);
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}

		private int convertColumnIndexToView(final int modelIndex) {
			return table.convertColumnIndexToView(modelIndex);
		}

		private int convertColumnIndexToModel(final int viewIndex) {
			return table.convertColumnIndexToModel(viewIndex);
		}
	}

	private final class EditorCustomWidgetFactory implements ICustomWidgetFactory {
		@Override
		public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE create(final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
			return factory.create(table, descriptor);
		}
	}

	private final class StartEditEvent extends EventObject {

		private static final long serialVersionUID = -1143486782609023655L;

		private final int row;
		private final int colum;

		private StartEditEvent(final Object source, final int row, final int colum) {
			super(source);
			this.row = row;
			this.colum = colum;
		}

		private int getRow() {
			return row;
		}

		private int getColum() {
			return colum;
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
