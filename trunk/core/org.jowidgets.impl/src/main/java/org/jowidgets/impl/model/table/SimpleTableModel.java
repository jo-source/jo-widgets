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

package org.jowidgets.impl.model.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jowidgets.api.model.table.IDefaultTableColumn;
import org.jowidgets.api.model.table.ISimpleTableModel;
import org.jowidgets.api.model.table.ITableCellBuilder;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableDataModelListener;
import org.jowidgets.common.model.ITableDataModelObservable;
import org.jowidgets.tools.controler.TableDataModelObservable;
import org.jowidgets.util.Assert;

public class SimpleTableModel extends DefaultTableColumnModel implements ISimpleTableModel, ITableDataModelObservable {

	private final TableDataModelObservable dataModelObservable;

	private final ArrayList<ArrayList<ITableCell>> data;

	private final IColorConstant evenBackgroundColor;
	private final IColorConstant oddBackgroundColor;

	private final boolean cellsEditableDefault;

	private ArrayList<Integer> selection;

	SimpleTableModel(
		final int rowCount,
		final int columnCount,
		final boolean cellsEditableDefault,
		final IColorConstant evenBackgroundColor,
		final IColorConstant oddBackgroundColor) {

		super(columnCount);

		this.dataModelObservable = new TableDataModelObservable();

		this.cellsEditableDefault = cellsEditableDefault;
		this.evenBackgroundColor = evenBackgroundColor;
		this.oddBackgroundColor = oddBackgroundColor;

		this.selection = new ArrayList<Integer>();

		this.data = new ArrayList<ArrayList<ITableCell>>();
		final ITableCellBuilder cellBuilder = cellBuilder();
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
			final ArrayList<ITableCell> row = new ArrayList<ITableCell>();
			data.add(row);
			for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
				row.add(cellBuilder.build());
			}
		}
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public ITableCell getCell(final int rowIndex, final int columnIndex) {
		final TableCell result = new TableCell(data.get(rowIndex).get(columnIndex));
		if (result.getBackgroundColor() == null) {
			if (rowIndex % 2 == 0) {
				result.setBackgroundColor(evenBackgroundColor);
			}
			else {
				result.setBackgroundColor(oddBackgroundColor);
			}
		}
		return result;
	}

	@Override
	public void setCell(final int rowIndex, final int columnIndex, final ITableCell cell) {
		Assert.paramNotNull(cell, "cell");
		data.get(rowIndex).set(columnIndex, cell);
		dataModelObservable.fireRowsChanged(new int[] {rowIndex});
	}

	@Override
	public void addRows(final int startRowIndex, final int rowCount) {
		final int[] rowIndices = new int[rowCount];
		final ITableCellBuilder cellBuilder = cellBuilder();
		final int columnCount = getColumnCount();
		for (int i = 0; i < rowCount; i++) {
			rowIndices[i] = startRowIndex + i;
			final ArrayList<ITableCell> row = new ArrayList<ITableCell>();
			data.add(startRowIndex, row);
			for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
				row.add(cellBuilder.build());
			}
		}
		dataModelObservable.fireRowsAdded(rowIndices);
	}

	@Override
	public void addRow(final int rowIndex, final ITableCell... cells) {
		Assert.paramNotNull(cells, "cells");

		final ITableCellBuilder cellBuilder = cellBuilder();
		final int columnCount = getColumnCount();

		final ArrayList<ITableCell> row = new ArrayList<ITableCell>();
		data.add(rowIndex, row);
		for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
			if (columnIndex < cells.length) {
				row.add(cells[columnIndex]);
			}
			else {
				row.add(cellBuilder.build());
			}
		}
		dataModelObservable.fireRowsAdded(new int[] {rowIndex});
	}

	@Override
	public void removeRow(final int index) {
		data.remove(index);
		final boolean selectionChanged = selection.remove(Integer.valueOf(index));
		dataModelObservable.fireRowsRemoved(new int[] {index});
		if (selectionChanged) {
			dataModelObservable.fireSelectionChanged();
		}
	}

	@Override
	public void removeRows(final int fromIndex, final int toIndex) {
		if (fromIndex > toIndex) {
			throw new IllegalArgumentException("From index must be less or equal than to index.");
		}

		final int[] indices = new int[1 + toIndex - fromIndex];
		boolean selectionChanged = false;
		for (int i = 0; i < indices.length; i++) {
			indices[i] = fromIndex + i;
			data.remove(fromIndex);
			selectionChanged = selectionChanged || selection.remove(Integer.valueOf(fromIndex + i));
		}
		dataModelObservable.fireRowsRemoved(indices);
		if (selectionChanged) {
			dataModelObservable.fireSelectionChanged();
		}
	}

	@Override
	public void removeRows(final int... rows) {
		Assert.paramNotNull(rows, "rows");
		//If rows is sorted, for each already removed row the
		//index must be reduced by one
		Arrays.sort(rows);
		int removedRowCount = 0;
		boolean selectionChanged = false;
		for (final int row : rows) {
			data.remove(row - removedRowCount);
			selectionChanged = selection.remove(Integer.valueOf(row)) || selectionChanged;
			removedRowCount++;
		}
		dataModelObservable.fireRowsRemoved(rows);
		if (selectionChanged) {
			dataModelObservable.fireSelectionChanged();
		}
	}

	@Override
	public void removeAllRows() {
		final boolean selectionChanged = selection.size() > 0;
		data.clear();
		dataModelObservable.fireRowsStructureChanged();
		if (selectionChanged) {
			dataModelObservable.fireSelectionChanged();
		}
	}

	@Override
	public ArrayList<Integer> getSelection() {
		return new ArrayList<Integer>(selection);
	}

	@Override
	public void setSelection(List<Integer> selection) {
		if (selection == null) {
			selection = new ArrayList<Integer>();
		}
		if (!this.selection.equals(selection)) {
			this.selection = new ArrayList<Integer>(selection);
			dataModelObservable.fireSelectionChanged();
		}
	}

	@Override
	public ITableDataModelObservable getTableDataModelObservable() {
		return this;
	}

	@Override
	public void addDataModelListener(final ITableDataModelListener listener) {
		dataModelObservable.addDataModelListener(listener);
	}

	@Override
	public void removeDataModelListener(final ITableDataModelListener listener) {
		dataModelObservable.removeDataModelListener(listener);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	//Overridden from column model start 
	////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void addColumn(final int columnIndex, final IDefaultTableColumn column) {
		if (data != null) {
			final ITableCellBuilder cellBuilder = cellBuilder();
			for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
				data.get(rowIndex).add(columnIndex, cellBuilder.build());
			}
		}
		super.addColumn(columnIndex, column);
	}

	@Override
	public void removeColumns(final int... columnIndices) {
		Assert.paramNotNull(columnIndices, "columnIndices");
		//If rows is sorted, for each already removed row the
		//index must be reduced by one
		Arrays.sort(columnIndices);

		for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
			int removedColumnCount = 0;
			for (final int columnIndex : columnIndices) {
				data.get(rowIndex).remove(columnIndex - removedColumnCount);
				removedColumnCount++;
			}
		}
		super.removeColumns(columnIndices);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	//Overridden from column model end
	////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////
	//Convenience methods start
	////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void addRow() {
		addRows(getRowCount(), 1);
	}

	@Override
	public void addRow(final int rowIndex) {
		addRows(rowIndex, 1);
	}

	@Override
	public void addRow(final ITableCell... cells) {
		addRow(getRowCount(), cells);
	}

	@Override
	public void addRow(final ITableCellBuilder... cellBuilders) {
		addRow(getRowCount(), cellBuilders);
	}

	@Override
	public void addRow(final int rowIndex, final ITableCellBuilder... cellBuilders) {
		Assert.paramNotNull(cellBuilders, "cellBuilders");
		final ITableCell[] cells = new ITableCell[cellBuilders.length];
		for (int i = 0; i < cellBuilders.length; i++) {
			cells[i] = cellBuilders[i].build();
		}
		addRow(rowIndex, cells);
	}

	@Override
	public void addRow(final String... cellTexts) {
		addRow(getRowCount(), cellTexts);
	}

	@Override
	public void addRow(final int rowIndex, final String... cellTexts) {
		Assert.paramNotNull(cellTexts, "cellTexts");
		final ITableCell[] cells = new ITableCell[cellTexts.length];
		for (int i = 0; i < cellTexts.length; i++) {
			cells[i] = cellBuilder().setText(cellTexts[i]).build();
		}
		addRow(rowIndex, cells);
	}

	@Override
	public void addRow(final List<String> cellTexts) {
		addRow(getRowCount(), cellTexts);
	}

	@Override
	public void addRow(final int rowIndex, final List<String> cellTexts) {
		Assert.paramNotNull(cellTexts, "cellTexts");
		final ITableCell[] cells = new ITableCell[cellTexts.size()];
		int i = 0;
		for (final String cellText : cellTexts) {
			cells[i] = cellBuilder().setText(cellText).build();
			i++;
		}
		addRow(rowIndex, cells);
	}

	@Override
	public void setCell(final int rowIndex, final int columnIndex, final ITableCellBuilder cellBuilder) {
		setCell(rowIndex, columnIndex, cellBuilder.build());
	}

	@Override
	public void setCell(final int rowIndex, final int columnIndex, final String text) {
		setCell(rowIndex, columnIndex, cellBuilder().setText(text));
	}

	@Override
	public void setCell(final int rowIndex, final int columnIndex, final String text, final IImageConstant icon) {
		setCell(rowIndex, columnIndex, cellBuilder().setText(text).setIcon(icon));
	}

	@Override
	public void setCell(final int rowIndex, final int columnIndex, final String text, final boolean editable) {
		setCell(rowIndex, columnIndex, cellBuilder().setText(text).setEditable(editable));
	}

	@Override
	public void setEditable(final int rowIndex, final int columnIndex, final boolean editable) {
		final TableCell cell = new TableCell(getCell(rowIndex, columnIndex));
		cell.setEditable(editable);
		setCell(rowIndex, columnIndex, cell);
	}

	@Override
	public void removeRows(final List<Integer> rows) {
		final int[] rowsArray = new int[rows.size()];
		int index = 0;
		for (final Integer row : rows) {
			rowsArray[index] = row.intValue();
			index++;
		}
		removeRows(rowsArray);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	//Convenience methods end
	////////////////////////////////////////////////////////////////////////////////////////////////

	private ITableCellBuilder cellBuilder() {
		return new TableCellBuilder().setEditable(cellsEditableDefault);
	}

}
