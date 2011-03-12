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
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jowidgets.common.types.Dimension;
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
import org.jowidgets.common.widgets.model.ITableCellModel;
import org.jowidgets.common.widgets.model.ITableColumnModel;
import org.jowidgets.common.widgets.model.ITableModel;
import org.jowidgets.spi.impl.controler.TableCellMouseEvent;
import org.jowidgets.spi.impl.controler.TableCellObservable;
import org.jowidgets.spi.impl.controler.TableCellPopupDetectionObservable;
import org.jowidgets.spi.impl.controler.TableCellPopupEvent;
import org.jowidgets.spi.impl.controler.TableColumnMouseEvent;
import org.jowidgets.spi.impl.controler.TableColumnObservable;
import org.jowidgets.spi.impl.controler.TableColumnPopupDetectionObservable;
import org.jowidgets.spi.impl.controler.TableColumnPopupEvent;
import org.jowidgets.spi.impl.swt.color.ColorCache;
import org.jowidgets.spi.widgets.ITableSpi;
import org.jowidgets.spi.widgets.setup.ITableSetupSpi;

public class TableImpl extends SwtControl implements ITableSpi {

	private final Table table;

	private final TableCellObservable tableCellObservable;
	private final TableCellPopupDetectionObservable tableCellPopupDetectionObservable;
	private final TableColumnPopupDetectionObservable tableColumnPopupDetectionObservable;
	private final TableColumnObservable tableColumnObservable;

	private final ColumnSelectionListener columnSelectionListener;

	private final boolean columnsMoveable;
	private final boolean columnsResizeable;
	private final ITableModel model;

	public TableImpl(final Object parentUiReference, final ITableSetupSpi setup) {
		super(new Table((Composite) parentUiReference, getStyle(setup)));

		this.tableCellObservable = new TableCellObservable();
		this.tableCellPopupDetectionObservable = new TableCellPopupDetectionObservable();
		this.tableColumnPopupDetectionObservable = new TableColumnPopupDetectionObservable();
		this.tableColumnObservable = new TableColumnObservable();

		this.columnSelectionListener = new ColumnSelectionListener();

		this.model = setup.getModel();
		this.columnsMoveable = setup.getColumnsMoveable();
		this.columnsResizeable = setup.getColumnsResizeable();

		this.table = getUiReference();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		table.addListener(SWT.SetData, new DataListener());
		table.addMouseListener(new TableCellListener());
		table.addMenuDetectListener(new TableMenuDetectListener());
	}

	@Override
	public Table getUiReference() {
		return (Table) super.getUiReference();
	}

	@Override
	public void initialize(final int rowsCount, final int columnsCount) {

		table.setRedraw(false);

		for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
			removeColumn(0);
		}

		table.clearAll();
		table.setItemCount(rowsCount);

		for (int columnIndex = 0; columnIndex < columnsCount; columnIndex++) {
			addColumn(columnIndex, model.getColumn(columnIndex));
		}

		table.setRedraw(true);
	}

	private void addColumn(final int index, final ITableColumnModel columnModel) {
		final TableColumn column = new TableColumn(getUiReference(), SWT.NONE, index);
		column.addSelectionListener(columnSelectionListener);
		column.setMoveable(columnsMoveable);
		column.setResizable(columnsResizeable);
		column.setWidth(columnModel.getWidth());
		column.setText(columnModel.getText());
		column.setToolTipText(columnModel.getToolTipText());
		//TODO MG support the other attributes of table column model
	}

	private void removeColumn(final int index) {
		final TableColumn column = table.getColumn(index);
		if (column != null && !column.isDisposed()) {
			column.removeSelectionListener(columnSelectionListener);
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
	public void addTableCellEditorListener(final ITableCellEditorListener listener) {}

	@Override
	public void removeTableCellEditorListener(final ITableCellEditorListener listener) {}

	@Override
	public void addTableSelectionListener(final ITableSelectionListener listener) {}

	@Override
	public void removeTableSelectionListener(final ITableSelectionListener listener) {}

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
				final ITableCellModel cellModel = model.getCell(rowIndex, columnIndex);
				if (cellModel.getBackgroundColor() != null) {
					item.setBackground(columnIndex, ColorCache.getInstance().getColor(cellModel.getBackgroundColor()));
				}
				if (cellModel.getText() != null) {
					item.setText(columnIndex, cellModel.getText());
				}
				//TODO MG support the other attributes of table cell model
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
			//Menu detect on header. Table has item(s)
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
			final TableColumn selectedColumn = (TableColumn) e.widget;
			if (selectedColumn != null) {
				final TableColumn[] columns = getUiReference().getColumns();
				for (int columnIndex = 0; columnIndex < columns.length; columnIndex++) {
					if (columns[columnIndex] == selectedColumn) {
						final TableColumnMouseEvent event = new TableColumnMouseEvent(columnIndex, getModifier(e.stateMask));
						tableColumnObservable.fireMouseClicked(event);
					}
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
