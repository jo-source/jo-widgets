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

package org.jowidgets.spi.impl.swt.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
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
import org.jowidgets.common.widgets.controler.ITableCellEditorListener;
import org.jowidgets.common.widgets.controler.ITableCellListener;
import org.jowidgets.common.widgets.controler.ITableCellMouseEvent;
import org.jowidgets.common.widgets.controler.ITableCellPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableColumnListener;
import org.jowidgets.common.widgets.controler.ITableColumnPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableSelectionListener;
import org.jowidgets.spi.impl.controler.TableCellEditEvent;
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
import org.jowidgets.spi.impl.swt.color.ColorCache;
import org.jowidgets.spi.impl.swt.image.SwtImageRegistry;
import org.jowidgets.spi.impl.swt.util.AlignmentConvert;
import org.jowidgets.spi.impl.swt.util.FontProvider;
import org.jowidgets.spi.impl.swt.util.MouseUtil;
import org.jowidgets.spi.widgets.ITableSpi;
import org.jowidgets.spi.widgets.setup.ITableSetupSpi;
import org.jowidgets.util.ArrayUtils;

public class TableImpl extends SwtControl implements ITableSpi {

	private final Table table;
	private final ITableDataModel dataModel;
	private final ITableColumnModelSpi columnModel;
	private TableCursor cursor;
	private ControlEditor editor;

	private final TableCellObservable tableCellObservable;
	private final TableCellPopupDetectionObservable tableCellPopupDetectionObservable;
	private final TableColumnPopupDetectionObservable tableColumnPopupDetectionObservable;
	private final TableColumnObservable tableColumnObservable;
	private final TableSelectionObservable tableSelectionObservable;
	private final TableCellEditorObservable tableCellEditorObservable;

	private final ColumnSelectionListener columnSelectionListener;
	private final ColumnControlListener columnControlListener;
	private final TableModelListener tableModelListener;
	private final TableColumnModelListener tableColumnModelListener;
	private final DataListener dataListener;

	private final boolean columnsMoveable;
	private final boolean columnsResizeable;

	private int[] lastColumnOrder;
	private boolean setWidthInvokedOnModel;

	public TableImpl(final Object parentUiReference, final ITableSetupSpi setup) {
		super(new Table((Composite) parentUiReference, getStyle(setup)));

		this.setWidthInvokedOnModel = false;

		this.tableCellObservable = new TableCellObservable();
		this.tableCellPopupDetectionObservable = new TableCellPopupDetectionObservable();
		this.tableColumnPopupDetectionObservable = new TableColumnPopupDetectionObservable();
		this.tableColumnObservable = new TableColumnObservable();
		this.tableSelectionObservable = new TableSelectionObservable();
		this.tableCellEditorObservable = new TableCellEditorObservable();

		this.dataListener = new DataListener();
		this.columnSelectionListener = new ColumnSelectionListener();
		this.columnControlListener = new ColumnControlListener();
		this.tableModelListener = new TableModelListener();
		this.tableColumnModelListener = new TableColumnModelListener();

		this.dataModel = setup.getDataModel();
		this.columnModel = setup.getColumnModel();

		this.columnsMoveable = setup.getColumnsMoveable();
		this.columnsResizeable = setup.getColumnsResizeable();

		this.table = getUiReference();
		table.setLinesVisible(true);
		table.setHeaderVisible(setup.isHeaderVisible());

		// fake column to fix windows table bug and to support no selection
		final TableColumn fakeColumn = new TableColumn(table, SWT.NONE);
		fakeColumn.setResizable(false);
		fakeColumn.setMoveable(false);
		fakeColumn.setWidth(0);
		fakeColumn.setText("FAKE");

		try {
			this.cursor = new TableCursor(table, SWT.NONE);
			this.cursor.setVisible(false);
			this.editor = new ControlEditor(table);
			this.editor.grabHorizontal = true;
			this.editor.grabVertical = true;
			table.addMouseListener(new TableEditListener());
		}
		catch (final NoClassDefFoundError e) {
			//RWT does not support TableCursor yet :-(
			this.cursor = null;
			this.editor = null;
		}

		table.addListener(SWT.SetData, dataListener);
		table.addMouseListener(new TableCellListener());
		table.addSelectionListener(new TableSelectionListener());

		setMenuDetectListener(new TableMenuDetectListener());
	}

	private int getColumnCount() {
		return table.getColumnCount() - 1;
	}

	@Override
	public Table getUiReference() {
		return (Table) super.getUiReference();
	}

	@Override
	public Dimension getMinSize() {
		return new Dimension(40, 40);
	}

	@Override
	public void resetFromModel() {
		table.setRedraw(false);

		final ITableDataModelObservable dataModelObservable = dataModel.getTableDataModelObservable();
		if (dataModelObservable != null) {
			dataModelObservable.removeDataModelListener(tableModelListener);
		}

		final ITableColumnModelObservable columnModelObservable = columnModel.getTableColumnModelObservable();
		if (columnModelObservable != null) {
			columnModelObservable.removeColumnModelListener(tableColumnModelListener);
		}

		table.setItemCount(dataModel.getRowCount());
		table.clearAll();

		removeAllColumns();
		addAllColumns();

		setSelection(dataModel.getSelection());

		if (dataModelObservable != null) {
			dataModelObservable.addDataModelListener(tableModelListener);
		}

		if (columnModelObservable != null) {
			columnModelObservable.addColumnModelListener(tableColumnModelListener);
		}

		table.setRedraw(true);
	}

	private void removeAllColumns() {
		final int oldColumnCount = getColumnCount();
		for (int columnIndex = 0; columnIndex < oldColumnCount; columnIndex++) {
			removeColumnListener(columnIndex);
		}
		for (int columnIndex = 0; columnIndex < oldColumnCount; columnIndex++) {
			removeColumn(0);
		}
	}

	private void addAllColumns() {
		final int columnsCount = columnModel.getColumnCount();

		for (int columnIndex = 0; columnIndex < columnsCount; columnIndex++) {
			addColumn(columnIndex, columnModel.getColumn(columnIndex));
		}

		for (int columnIndex = 0; columnIndex < columnsCount; columnIndex++) {
			addColumnListener(columnIndex);
		}
	}

	private void addColumn(final int externalIndex, final ITableColumnSpi joColumn) {
		final int internalIndex = externalIndex + 1;
		final TableColumn swtColumn = new TableColumn(table, SWT.NONE, internalIndex);
		swtColumn.setMoveable(columnsMoveable);
		swtColumn.setResizable(columnsResizeable);
		setColumnData(swtColumn, joColumn);
	}

	private void setColumnData(final TableColumn swtColumn, final ITableColumnSpi joColumn) {
		final String text = joColumn.getText();
		final IImageConstant icon = joColumn.getIcon();
		final AlignmentHorizontal alignment = joColumn.getAlignment();

		if (text != null) {
			swtColumn.setText(text);
		}
		else {
			swtColumn.setText("");
		}
		if (icon != null) {
			swtColumn.setImage(SwtImageRegistry.getInstance().getImage(icon));
		}
		else {
			swtColumn.setImage(null);
		}
		if (alignment != null) {
			swtColumn.setAlignment(AlignmentConvert.convert(alignment));
		}
		else {
			swtColumn.setAlignment(SWT.LEFT);
		}
		if (joColumn.getWidth() != -1) {
			if (swtColumn.getWidth() != joColumn.getWidth()) {
				swtColumn.setWidth(joColumn.getWidth());
			}
		}
		else {
			if (swtColumn.getWidth() != 100) {
				swtColumn.setWidth(100);
			}
		}
		swtColumn.setToolTipText(joColumn.getToolTipText());
	}

	private void addColumnListener(final int externalIndex) {
		final int internalIndex = externalIndex + 1;
		final TableColumn column = table.getColumn(internalIndex);
		if (column != null && !column.isDisposed()) {
			column.addSelectionListener(columnSelectionListener);
			column.addControlListener(columnControlListener);
		}
	}

	private void removeColumnListener(final int externalIndex) {
		final int internalIndex = externalIndex + 1;
		final TableColumn column = table.getColumn(internalIndex);
		if (column != null && !column.isDisposed()) {
			column.removeSelectionListener(columnSelectionListener);
			column.removeControlListener(columnControlListener);
		}
	}

	private void removeColumn(final int externalIndex) {
		final int internalIndex = externalIndex + 1;
		final TableColumn column = table.getColumn(internalIndex);
		if (column != null && !column.isDisposed()) {
			column.dispose();
		}
	}

	@Override
	public Position getCellPosition(final int rowIndex, final int columnIndex) {
		final int internalIndex = columnIndex + 1;
		final Rectangle bounds = table.getItem(rowIndex).getBounds(internalIndex);
		return new Position(bounds.x, bounds.y);
	}

	@Override
	public Dimension getCellSize(final int rowIndex, final int columnIndex) {
		final int internalIndex = columnIndex + 1;
		final Rectangle bounds = table.getItem(rowIndex).getBounds(internalIndex);
		return new Dimension(bounds.width, bounds.height);
	}

	@Override
	public void pack(final TablePackPolicy policy) {
		table.setRedraw(false);
		for (int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++) {
			packColumn(columnIndex, policy);
		}
		table.setRedraw(true);
	}

	@Override
	public void pack(final int columnIndex, final TablePackPolicy policy) {
		table.setRedraw(false);
		packColumn(columnIndex, policy);
		table.setRedraw(true);
	}

	private void packColumn(final int columnIndex, final TablePackPolicy policy) {
		final int internalIndex = columnIndex + 1;
		final Label textLabel = new Label(table, SWT.NONE);
		final GC context = new GC(textLabel);

		final TableColumn[] columns = table.getColumns();
		final TableColumn column = columns[internalIndex];
		boolean packed = false;

		int max = 10;

		if (policy.considerHeader()) {
			context.setFont(table.getFont());
			textLabel.setFont(table.getFont());
			int width = context.textExtent(column.getText()).x;
			if (column.getImage() != null) {
				width += column.getImage().getBounds().width;
			}
			max = Math.max(max, width);
		}

		if (policy.considerData() && policy.considerAllData()) {
			for (int i = 0; i < table.getItemCount(); i++) {
				final TableItem item = table.getItem(i);

				context.setFont(item.getFont(internalIndex));
				textLabel.setFont(item.getFont(internalIndex));

				int width = context.textExtent(item.getText(internalIndex)).x;
				if (item.getImage(internalIndex) != null) {
					width += item.getImage(internalIndex).getBounds().width + 5;
				}
				max = Math.max(max, width);
			}
		}
		else if (policy.considerData()) {
			packed = true;
			column.pack();
		}

		if (packed) {
			column.setWidth(Math.max(max + 15, column.getWidth()));
		}
		else {
			column.setWidth(max + 15);
		}

		context.dispose();
		textLabel.dispose();
	}

	@Override
	public ArrayList<Integer> getColumnPermutation() {
		final ArrayList<Integer> result = new ArrayList<Integer>();
		for (final int index : table.getColumnOrder()) {
			if (index == 0) {
				continue;
			}
			result.add(Integer.valueOf(index - 1));
		}
		return result;
	}

	@Override
	public void setColumnPermutation(final List<Integer> permutation) {
		final int[] columnOrder = new int[permutation.size() + 1];
		columnOrder[0] = 0;
		int i = 1;
		for (final Integer permutatedIndex : permutation) {
			columnOrder[i] = permutatedIndex.intValue() + 1;
			i++;
		}
		table.setRedraw(false);
		table.setColumnOrder(columnOrder);
		table.setRedraw(true);
	}

	@Override
	public ArrayList<Integer> getSelection() {
		final ArrayList<Integer> result = new ArrayList<Integer>();
		final int[] selection = table.getSelectionIndices();
		if (selection != null) {
			for (final int index : selection) {
				result.add(Integer.valueOf(index));
			}
		}
		return result;
	}

	@Override
	public void setSelection(final List<Integer> selection) {
		if (!isSelectionEqualWithView(selection)) {
			if (selection == null || selection.size() == 0) {
				table.setSelection(new int[0]);
			}
			else {
				final int[] newSelection = new int[selection.size()];
				for (int i = 0; i < newSelection.length; i++) {
					newSelection[i] = selection.get(i).intValue();
					table.setSelection(newSelection);
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

	private CellIndices getExternalCellIndices(final Point point) {
		final TableItem item = table.getItem(point);
		if (item != null) {
			for (int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++) {
				final int internalIndex = columnIndex + 1;
				final Rectangle rect = item.getBounds(internalIndex);
				if (rect.contains(point)) {
					final int rowIndex = table.indexOf(item);
					if (rowIndex != -1) {
						return new CellIndices(rowIndex, columnIndex);
					}
				}
			}
		}
		return null;
	}

	private int getColumnIndex(final TableColumn columnOfInterest) {
		if (columnOfInterest != null) {
			final TableColumn[] columns = table.getColumns();
			for (int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++) {
				final int internalIndex = columnIndex + 1;
				if (columns[internalIndex] == columnOfInterest) {
					return columnIndex;
				}
			}
		}
		return -1;
	}

	private boolean isSelectionEqualWithView(final List<Integer> selection) {
		final int[] tableSelection = table.getSelectionIndices();
		if (selection == null && (tableSelection == null || tableSelection.length == 0)) {
			return true;
		}
		if (selection.size() != tableSelection.length) {
			return false;
		}
		for (final int tablesSelected : tableSelection) {
			if (!selection.contains(Integer.valueOf(tablesSelected))) {
				return false;
			}
		}
		return true;
	}

	private static int getStyle(final ITableSetupSpi setup) {
		int result = SWT.VIRTUAL;

		if (setup.hasBorder()) {
			result = result | SWT.BORDER;
		}

		if (TableSelectionPolicy.MULTI_ROW_SELECTION == setup.getSelectionPolicy()) {
			result = result | SWT.FULL_SELECTION | SWT.MULTI;
		}
		else if (TableSelectionPolicy.SINGLE_ROW_SELECTION == setup.getSelectionPolicy()) {
			result = result | SWT.FULL_SELECTION;
		}
		else if (TableSelectionPolicy.NO_SELECTION == setup.getSelectionPolicy()) {
			result = result | SWT.HIDE_SELECTION;
		}
		else {
			throw new IllegalArgumentException("SelectionPolicy '" + setup.getSelectionPolicy() + "' is not known");
		}

		return result;
	}

	final class DataListener implements Listener {
		@Override
		public void handleEvent(final Event event) {
			final TableItem item = (TableItem) event.item;
			final int rowIndex = table.indexOf(item);
			for (int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++) {
				final int internalIndex = columnIndex + 1;
				final ITableCell cell = dataModel.getCell(rowIndex, columnIndex);

				final String text = cell.getText();
				final IImageConstant icon = cell.getIcon();
				final IColorConstant backgroundColor = cell.getBackgroundColor();
				final IColorConstant foregroundColor = cell.getForegroundColor();
				final Markup markup = cell.getMarkup();
				//TODO BM support tooltip like in SwtMenu
				//final String toolTipText = cell.getToolTipText(); 

				if (text != null) {
					item.setText(internalIndex, text);
				}
				else {
					item.setText(internalIndex, "");
				}
				if (icon != null) {
					item.setImage(internalIndex, SwtImageRegistry.getInstance().getImage(icon));
				}
				if (markup != null) {
					final Font newFont = FontProvider.deriveFont(item.getFont(), markup);
					item.setFont(internalIndex, newFont);
				}
				if (backgroundColor != null) {
					item.setBackground(internalIndex, ColorCache.getInstance().getColor(backgroundColor));
				}
				if (foregroundColor != null) {
					item.setForeground(internalIndex, ColorCache.getInstance().getColor(foregroundColor));
				}
			}
		}
	}

	final class TableCellListener extends MouseAdapter {
		@Override
		public void mouseUp(final MouseEvent e) {
			final ITableCellMouseEvent mouseEvent = getMouseEvent(e, 1);
			if (mouseEvent != null) {
				tableCellObservable.fireMouseReleased(mouseEvent);
			}
		}

		@Override
		public void mouseDown(final MouseEvent e) {
			final ITableCellMouseEvent mouseEvent = getMouseEvent(e, 1);
			if (mouseEvent != null) {
				tableCellObservable.fireMousePressed(mouseEvent);
			}
		}

		@Override
		public void mouseDoubleClick(final MouseEvent e) {
			final ITableCellMouseEvent mouseEvent = getMouseEvent(e, 2);
			if (mouseEvent != null) {
				tableCellObservable.fireMouseDoubleClicked(mouseEvent);
			}
		}

		private ITableCellMouseEvent getMouseEvent(final MouseEvent event, final int maxCount) {
			try {
				if (event.count > maxCount) {
					return null;
				}
			}
			catch (final NoSuchFieldError e) {
				//RWT doesn't support count field :-( 
				//so the mouse down and mouse up may be fired twice at double clicks :-(
			}
			final MouseButton mouseButton = MouseUtil.getMouseButton(event);
			if (mouseButton == null) {
				return null;
			}
			final Point point = new Point(event.x, event.y);
			final CellIndices indices = getExternalCellIndices(point);
			if (indices != null) {
				return new TableCellMouseEvent(
					indices.getRowIndex(),
					indices.getColumnIndex(),
					mouseButton,
					MouseUtil.getModifier(event.stateMask));
			}
			return null;
		}
	}

	final class TableEditListener extends MouseAdapter {

		@Override
		public void mouseDoubleClick(final MouseEvent e) {
			final CellIndices indices = getExternalCellIndices(new Point(e.x, e.y));
			if (indices != null) {
				final ITableCell cell = dataModel.getCell(indices.getRowIndex(), indices.getColumnIndex());
				if (cell.isEditable()) {
					activateEditor(indices);
				}
			}
		}

		private void activateEditor(final CellIndices indices) {
			final int rowIndex = indices.getRowIndex();
			final int columnIndex = indices.getColumnIndex();
			final int internalIndex = columnIndex + 1;

			cursor.setSelection(rowIndex, internalIndex);
			cursor.setVisible(true);

			final Text textField = new Text(cursor, SWT.NONE);
			final TableItem item = cursor.getRow();
			textField.setText(item.getText(internalIndex));
			textField.setSelection(0, textField.getText().length());

			textField.addVerifyListener(new VerifyListener() {
				@Override
				public void verifyText(final VerifyEvent verifyEvent) {
					final String newText = getNewText(textField, verifyEvent);
					final TableCellEditEvent editEvent = new TableCellEditEvent(rowIndex, columnIndex, newText);
					final boolean veto = tableCellEditorObservable.fireOnEdit(editEvent);
					if (veto) {
						verifyEvent.doit = false;
					}
				}

			});
			textField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(final KeyEvent keyEvent) {
					final TableCellEditEvent editEvent = new TableCellEditEvent(rowIndex, columnIndex, textField.getText());
					if (keyEvent.character == SWT.CR) {
						editFinished(rowIndex, columnIndex, item, textField, editEvent);
					}
					else if (keyEvent.character == SWT.ESC) {
						tableCellEditorObservable.fireEditCanceled(editEvent);
						textField.dispose();
						cursor.setVisible(false);
					}
				}
			});
			textField.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(final FocusEvent e) {
					final TableCellEditEvent editEvent = new TableCellEditEvent(rowIndex, columnIndex, textField.getText());
					editFinished(rowIndex, columnIndex, item, textField, editEvent);
				}
			});
			editor.setEditor(textField);
			textField.setFocus();
		}

		private void editFinished(
			final int rowIndex,
			final int columnIndex,
			final TableItem item,
			final Text textField,
			final TableCellEditEvent editEvent) {
			final int internalIndex = columnIndex + 1;
			tableCellEditorObservable.fireEditFinished(editEvent);
			final String newModelText = dataModel.getCell(rowIndex, columnIndex).getText();
			item.setText(internalIndex, newModelText);

			textField.dispose();
			cursor.setVisible(false);
		}

		private String getNewText(final Text textField, final VerifyEvent verifyEvent) {
			final StringBuilder result = new StringBuilder(textField.getText());
			result.replace(verifyEvent.start, verifyEvent.end, verifyEvent.text);
			return result.toString();
		}
	}

	final class TableMenuDetectListener implements MenuDetectListener {

		@Override
		public void menuDetected(final MenuDetectEvent e) {
			Point point = new Point(e.x, e.y);
			point = table.toControl(point);
			final Position position = new Position(point.x, point.y);
			TableItem item = table.getItem(point);

			//Menu detect on table cell
			if (item != null && point.y > table.getHeaderHeight()) {
				for (int colIndex = 0; colIndex < getColumnCount(); colIndex++) {
					final Rectangle rect = item.getBounds(colIndex);
					if (rect.contains(point)) {
						final int rowIndex = table.indexOf(item);
						if (rowIndex != -1) {
							tableCellPopupDetectionObservable.firePopupDetected(new TableCellPopupEvent(
								rowIndex,
								colIndex,
								position));
						}
					}
				}
			}
			//Menu detect on header. Table has some item(s)
			else if (table.getItemCount() > 0 && point.y < table.getHeaderHeight()) {
				item = table.getItem(0);
				fireColumnPopupDetected(item, point, position);
			}
			//Menu detect on header but table has no item.
			//Just temporarily add an item to the table an remove it, after
			//position was calculated.
			else if (point.y < table.getHeaderHeight()) {
				table.setRedraw(false);
				table.removeListener(SWT.SetData, dataListener);
				final TableItem dummyItem = new TableItem(table, SWT.NONE);
				fireColumnPopupDetected(dummyItem, point, position);
				dummyItem.dispose();
				table.addListener(SWT.SetData, dataListener);
				table.setRedraw(true);
			}
			else {
				getPopupDetectionObservable().firePopupDetected(position);
			}
		}

		private void fireColumnPopupDetected(final TableItem item, final Point point, final Position position) {
			for (int colIndex = 0; colIndex < getColumnCount(); colIndex++) {
				final int internalIndex = colIndex + 1;
				final Rectangle rect = item.getBounds(internalIndex);
				if (rect.x <= point.x && point.x <= rect.x + rect.width) {
					tableColumnPopupDetectionObservable.firePopupDetected(new TableColumnPopupEvent(colIndex, position));
				}
			}
		}
	}

	final class ColumnSelectionListener extends SelectionAdapter {
		@Override
		public void widgetSelected(final SelectionEvent e) {
			final TableColumn column = (TableColumn) e.widget;
			if (column != null) {
				final int columnIndex = getColumnIndex(column);
				final Set<Modifier> modifier = MouseUtil.getModifier(e.stateMask);
				tableColumnObservable.fireMouseClicked(new TableColumnMouseEvent(columnIndex, modifier));
			}
		}
	}

	final class ColumnControlListener implements ControlListener {

		private long lastResizeTime = 0;

		@Override
		public void controlResized(final ControlEvent e) {
			final TableColumn column = (TableColumn) e.widget;
			final long time = getTime(e);
			if (time != -1) {
				lastResizeTime = time;
			}
			if (column != null) {
				final int columnIndex = getColumnIndex(column);
				final int width = column.getWidth();
				setWidthInvokedOnModel = true;
				columnModel.getColumn(columnIndex).setWidth(width);
				setWidthInvokedOnModel = false;
				tableColumnObservable.fireColumnResized(new TableColumnResizeEvent(columnIndex, width));
			}
		}

		@Override
		public void controlMoved(final ControlEvent e) {
			if (getTime(e) != lastResizeTime) {
				final int[] columnOrder = table.getColumnOrder();
				if (lastColumnOrder == null || !Arrays.equals(columnOrder, lastColumnOrder)) {
					lastColumnOrder = columnOrder;
					tableColumnObservable.fireColumnPermutationChanged();
				}
			}
		}

		private long getTime(final ControlEvent event) {
			try {
				return event.time;
			}
			catch (final NoSuchFieldError e) {
				//RWT doesn't support time field :-( 
			}
			return -1;
		}
	}

	final class TableSelectionListener extends SelectionAdapter {
		@Override
		public void widgetSelected(final SelectionEvent e) {
			dataModel.setSelection(getSelection());
			tableSelectionObservable.fireSelectionChanged();
		}
	}

	final class TableModelListener implements ITableDataModelListener {

		@Override
		public void rowsAdded(final int[] rowIndices) {
			updateRows(rowIndices);
		}

		@Override
		public void rowsRemoved(final int[] rowIndices) {
			updateRows(rowIndices);
		}

		@Override
		public void rowsChanged(final int[] rowIndices) {
			table.clear(rowIndices);
		}

		@Override
		public void dataChanged() {
			table.setItemCount(dataModel.getRowCount());
			table.clearAll();
		}

		@Override
		public void selectionChanged() {
			setSelection(dataModel.getSelection());
		}

		private void updateRows(final int[] rowIndices) {
			if (rowIndices != null && rowIndices.length > 0) {
				table.setItemCount(dataModel.getRowCount());
				table.clear(ArrayUtils.getMin(rowIndices), dataModel.getRowCount() - 1);
			}
		}

	}

	final class TableColumnModelListener implements ITableColumnModelListener {

		@Override
		public void columnsAdded(final int[] columnIndices) {
			table.setRedraw(false);
			table.clearAll();
			Arrays.sort(columnIndices);
			for (int i = 0; i < columnIndices.length; i++) {
				final int addedIndex = columnIndices[i];
				addColumn(addedIndex, columnModel.getColumn(addedIndex));
				addColumnListener(addedIndex);
			}
			table.setRedraw(true);
		}

		@Override
		public void columnsRemoved(final int[] columnIndices) {
			table.setRedraw(false);
			table.clearAll();
			Arrays.sort(columnIndices);
			int removedColumnsCount = 0;
			for (int i = 0; i < columnIndices.length; i++) {
				final int removedIndex = columnIndices[i] - removedColumnsCount;
				removeColumnListener(removedIndex);
				removeColumn(removedIndex);
				removedColumnsCount++;
			}
			table.setRedraw(true);
		}

		@Override
		public void columnsChanged(final int[] columnIndices) {
			if (!setWidthInvokedOnModel) {
				final TableColumn[] columns = table.getColumns();
				for (final int changedIndex : columnIndices) {
					final int internalIndex = changedIndex + 1;
					setColumnData(columns[internalIndex], columnModel.getColumn(changedIndex));
				}
			}
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
