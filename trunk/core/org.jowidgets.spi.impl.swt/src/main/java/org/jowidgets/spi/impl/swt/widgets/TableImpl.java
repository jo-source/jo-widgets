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

package org.jowidgets.spi.impl.swt.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
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
import org.jowidgets.spi.widgets.ITableSpi;
import org.jowidgets.spi.widgets.setup.ITableSetupSpi;

public class TableImpl extends SwtControl implements ITableSpi {

	private final Table table;
	private final ITableDataModel dataModel;
	private final ITableColumnModel columnModel;
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

	private final boolean columnsMoveable;
	private final boolean columnsResizeable;

	private int[] lastColumnOrder;

	public TableImpl(final Object parentUiReference, final ITableSetupSpi setup) {
		super(new Table((Composite) parentUiReference, getStyle(setup)));

		this.tableCellObservable = new TableCellObservable();
		this.tableCellPopupDetectionObservable = new TableCellPopupDetectionObservable();
		this.tableColumnPopupDetectionObservable = new TableColumnPopupDetectionObservable();
		this.tableColumnObservable = new TableColumnObservable();
		this.tableSelectionObservable = new TableSelectionObservable();
		this.tableCellEditorObservable = new TableCellEditorObservable();

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
		table.setHeaderVisible(true);

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

		table.addListener(SWT.SetData, new DataListener());
		table.addMouseListener(new TableCellListener());
		table.addMenuDetectListener(new TableMenuDetectListener());
		table.addSelectionListener(new TableSelectionListener());
	}

	@Override
	public Table getUiReference() {
		return (Table) super.getUiReference();
	}

	@Override
	public void initialize() {
		table.setRedraw(false);

		final ITableDataModelObservable dataModelObservable = dataModel.getTableDataModelObservable();
		if (dataModelObservable != null) {
			dataModelObservable.removeDataModelListener(tableModelListener);
		}

		final ITableColumnModelObservable columnModelObservable = columnModel.getTableColumnModelObservable();
		if (columnModelObservable != null) {
			columnModelObservable.removeColumnModelListener(tableColumnModelListener);
		}

		removeAllColumns();
		table.clearAll();

		addAllColumns();

		table.setItemCount(dataModel.getRowCount());
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
		final int oldColumnCount = table.getColumnCount();
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

	private void addColumn(final int index, final ITableColumn joColumn) {
		final TableColumn swtColumn = new TableColumn(getUiReference(), SWT.NONE, index);
		swtColumn.setMoveable(columnsMoveable);
		swtColumn.setResizable(columnsResizeable);
		setColumnData(swtColumn, joColumn);
	}

	private void setColumnData(final TableColumn swtColumn, final ITableColumn joColumn) {
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
			swtColumn.setWidth(joColumn.getWidth());
		}
		else {
			swtColumn.setWidth(100);
		}
		swtColumn.setToolTipText(joColumn.getToolTipText());
	}

	private void addColumnListener(final int index) {
		final TableColumn column = table.getColumn(index);
		if (column != null && !column.isDisposed()) {
			column.addSelectionListener(columnSelectionListener);
			column.addControlListener(columnControlListener);
		}
	}

	private void removeColumnListener(final int index) {
		final TableColumn column = table.getColumn(index);
		if (column != null && !column.isDisposed()) {
			column.removeSelectionListener(columnSelectionListener);
			column.removeControlListener(columnControlListener);
		}
	}

	private void removeColumn(final int index) {
		final TableColumn column = table.getColumn(index);
		if (column != null && !column.isDisposed()) {
			column.dispose();
		}
	}

	@Override
	public Position getCellPosition(final int rowIndex, final int columnIndex) {
		final Rectangle bounds = getUiReference().getItem(rowIndex).getBounds(columnIndex);
		return new Position(bounds.x, bounds.y);
	}

	@Override
	public Dimension getCellSize(final int rowIndex, final int columnIndex) {
		final Rectangle bounds = getUiReference().getItem(rowIndex).getBounds(columnIndex);
		return new Dimension(bounds.width, bounds.height);
	}

	@Override
	public void pack(final TableColumnPackPolicy policy) {
		table.setRedraw(false);
		final TableColumn[] columns = table.getColumns();
		for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
			//TODO MG consider pack policy
			columns[columnIndex].pack();
		}
		table.setRedraw(true);
	}

	@Override
	public void pack(final int columnIndex, final TableColumnPackPolicy policy) {
		table.setRedraw(false);
		//TODO MG consider pack policy
		getUiReference().getColumn(columnIndex).pack();
		table.setRedraw(true);
	}

	@Override
	public ArrayList<Integer> getColumnPermutation() {
		final ArrayList<Integer> result = new ArrayList<Integer>();
		for (final int index : getUiReference().getColumnOrder()) {
			result.add(Integer.valueOf(index));
		}
		return result;
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

	private CellIndices getCellIndices(final Point point) {
		final TableItem item = table.getItem(point);
		if (item != null) {
			for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
				final Rectangle rect = item.getBounds(columnIndex);
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
			final TableColumn[] columns = getUiReference().getColumns();
			for (int columnIndex = 0; columnIndex < columns.length; columnIndex++) {
				if (columns[columnIndex] == columnOfInterest) {
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

	private static MouseButton getMouseButton(final MouseEvent event) {
		if (event.button == 1) {
			return MouseButton.LEFT;
		}
		else if (event.button == 3) {
			return MouseButton.RIGTH;
		}
		else {
			return null;
		}
	}

	private static Set<Modifier> getModifier(final int stateMask) {
		final Set<Modifier> modifier = new HashSet<Modifier>();
		if ((stateMask & SWT.SHIFT) > 0) {
			modifier.add(Modifier.SHIFT);
		}
		if ((stateMask & SWT.CTRL) > 0) {
			modifier.add(Modifier.CTRL);
		}
		if ((stateMask & SWT.ALT) > 0) {
			modifier.add(Modifier.ALT);
		}
		return modifier;
	}

	private static int getStyle(final ITableSetupSpi setup) {
		int result = SWT.FULL_SELECTION | SWT.VIRTUAL;

		if (SelectionPolicy.MULTI_SELECTION == setup.getSelectionPolicy()) {
			result = result | SWT.MULTI;
		}
		else if (SelectionPolicy.SINGLE_SELECTION != setup.getSelectionPolicy()) {
			throw new IllegalArgumentException("SelectionPolicy '" + setup.getSelectionPolicy() + "' is not known");
		}

		return result;
	}

	final class DataListener implements Listener {
		@Override
		public void handleEvent(final Event event) {
			final TableItem item = (TableItem) event.item;
			final int rowIndex = getUiReference().indexOf(item);
			for (int columnIndex = 0; columnIndex < getUiReference().getColumnCount(); columnIndex++) {

				final ITableCell cell = dataModel.getCell(rowIndex, columnIndex);

				final String text = cell.getText();
				final IImageConstant icon = cell.getIcon();
				final IColorConstant backgroundColor = cell.getBackgroundColor();
				final IColorConstant foregroundColor = cell.getForegroundColor();
				final Markup markup = cell.getMarkup();
				//TODO BM support tooltip like in SwtMenu
				//final String toolTipText = cell.getToolTipText(); 

				if (text != null) {
					item.setText(columnIndex, text);
				}
				else {
					item.setText(columnIndex, "");
				}
				if (icon != null) {
					item.setImage(columnIndex, SwtImageRegistry.getInstance().getImage(icon));
				}
				if (markup != null) {
					final Font newFont = FontProvider.deriveFont(item.getFont(), markup);
					item.setFont(columnIndex, newFont);
				}
				if (backgroundColor != null) {
					item.setBackground(columnIndex, ColorCache.getInstance().getColor(backgroundColor));
				}
				if (foregroundColor != null) {
					item.setForeground(columnIndex, ColorCache.getInstance().getColor(foregroundColor));
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
			final MouseButton mouseButton = getMouseButton(event);
			if (mouseButton == null) {
				return null;
			}
			final Point point = new Point(event.x, event.y);
			final CellIndices indices = getCellIndices(point);
			if (indices != null) {
				return new TableCellMouseEvent(
					indices.getRowIndex(),
					indices.getColumnIndex(),
					mouseButton,
					getModifier(event.stateMask));
			}
			return null;
		}
	}

	final class TableEditListener extends MouseAdapter {

		@Override
		public void mouseDoubleClick(final MouseEvent e) {
			final CellIndices indices = getCellIndices(new Point(e.x, e.y));
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

			cursor.setSelection(rowIndex, columnIndex);
			cursor.setVisible(true);

			final Text textField = new Text(cursor, SWT.NONE);
			final TableItem item = cursor.getRow();
			textField.setText(item.getText(columnIndex));
			textField.setSelection(0, textField.getText().length());

			textField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(final KeyEvent keyEvent) {
					final String newText = getNewText(textField, keyEvent);
					final TableCellEditEvent editEvent = new TableCellEditEvent(rowIndex, columnIndex, newText);
					if (keyEvent.character == SWT.CR) {
						editFinished(rowIndex, columnIndex, item, textField, editEvent);
					}
					else if (keyEvent.character == SWT.ESC) {
						tableCellEditorObservable.fireEditCanceled(editEvent);
						textField.dispose();
						cursor.setVisible(false);
					}
					else {
						final boolean veto = tableCellEditorObservable.fireOnEdit(editEvent);
						if (veto) {
							keyEvent.doit = false;
						}
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

			tableCellEditorObservable.fireEditFinished(editEvent);
			final String newModelText = dataModel.getCell(rowIndex, columnIndex).getText();
			item.setText(columnIndex, newModelText);

			textField.dispose();
			cursor.setVisible(false);
		}

		private String getNewText(final Text textField, final KeyEvent keyEvent) {
			//TODO MG get the correct new text
			return textField.getText();
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
				for (int colIndex = 0; colIndex < table.getColumnCount(); colIndex++) {
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
			else if (table.getItemCount() > 0) {
				item = table.getItem(0);
				fireColumnPopupDetected(item, point, position);
			}
			//Menu detect on header but table has no item.
			//Just temporarily add an item to the table an remove it, after
			//position was calculated.
			else {
				table.setRedraw(false);
				final TableItem dummyItem = new TableItem(table, SWT.NONE);
				fireColumnPopupDetected(item, point, position);
				dummyItem.dispose();
				table.setRedraw(true);
			}
		}

		private void fireColumnPopupDetected(final TableItem item, final Point point, final Position position) {
			for (int colIndex = 0; colIndex < table.getColumnCount(); colIndex++) {
				final Rectangle rect = item.getBounds(colIndex);
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
				final Set<Modifier> modifier = getModifier(e.stateMask);
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
				columnModel.getColumn(columnIndex).setWidth(width);
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
			table.clear(rowIndices);
		}

		@Override
		public void rowsStructureChanged() {
			table.clearAll();
			table.setItemCount(dataModel.getRowCount());
		}

		@Override
		public void selectionChanged() {
			setSelection(dataModel.getSelection());
		}

	}

	final class TableColumnModelListener implements ITableColumnModelListener {

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
			final TableColumn[] columns = table.getColumns();
			for (int i = 0; i < columnIndices.length; i++) {
				final int changedIndex = columnIndices[i];
				setColumnData(columns[changedIndex], columnModel.getColumn(changedIndex));
			}
		}

		@Override
		public void columnsStructureChanged() {
			table.setRedraw(false);

			table.clearAll();
			removeAllColumns();
			addAllColumns();

			table.setRedraw(true);
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
