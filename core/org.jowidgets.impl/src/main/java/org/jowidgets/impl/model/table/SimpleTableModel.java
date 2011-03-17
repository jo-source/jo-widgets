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

import org.jowidgets.api.model.table.ISimpleTableModel;
import org.jowidgets.api.model.table.ITableCellBuilder;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableModelListener;
import org.jowidgets.common.model.ITableModelObservable;
import org.jowidgets.tools.controler.TableModelObservable;
import org.jowidgets.util.Assert;

public class SimpleTableModel extends DefaultTableColumnModel implements ISimpleTableModel, ITableModelObservable {

	private final TableModelObservable tableModelObservable;

	private final ArrayList<ArrayList<ITableCell>> data;

	private final IColorConstant evenBackgroundColor;
	private final IColorConstant oddBackgroundColor;

	private ArrayList<Integer> selection;

	SimpleTableModel(
		final int rowCount,
		final int columnCount,
		final IColorConstant evenBackgroundColor,
		final IColorConstant oddBackgroundColor) {

		super(columnCount);

		this.tableModelObservable = new TableModelObservable();

		this.evenBackgroundColor = evenBackgroundColor;
		this.oddBackgroundColor = oddBackgroundColor;

		this.selection = new ArrayList<Integer>();

		this.data = new ArrayList<ArrayList<ITableCell>>();
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
			final ArrayList<ITableCell> row = new ArrayList<ITableCell>();
			data.add(row);
			for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
				row.add(new TableCellBuilder().build());
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
		tableModelObservable.fireRowsChanged(new int[] {rowIndex});
	}

	@Override
	public void addRow() {

	}

	@Override
	public void addRow(final int rowIndex) {

	}

	@Override
	public void addRows(final int rowIndex, final int rowCount) {

	}

	@Override
	public void addRow(final ITableCell... cells) {

	}

	@Override
	public void addRow(final int rowIndex, final ITableCell... cells) {

	}

	@Override
	public void addRow(final ITableCellBuilder... cellBuilders) {

	}

	@Override
	public void addRow(final int rowIndex, final ITableCellBuilder... cellBuilders) {

	}

	@Override
	public void addRow(final String... cellTexts) {

	}

	@Override
	public void addRow(final int rowIndex, final String... cellTexts) {

	}

	@Override
	public void removeRow(final int index) {
		data.remove(index);
		tableModelObservable.fireRowsRemoved(new int[] {index});
	}

	@Override
	public void removeRows(final int fromIndex, final int toIndex) {
		if (fromIndex > toIndex) {
			throw new IllegalArgumentException("From index must be less or equal than to index.");
		}

		final int[] indices = new int[1 + toIndex - fromIndex];
		for (int i = 0; i < indices.length; i++) {
			indices[i] = fromIndex + i;
			data.remove(fromIndex);
		}
		tableModelObservable.fireRowsRemoved(indices);
	}

	@Override
	public ArrayList<Integer> getSelection() {
		return new ArrayList<Integer>(selection);
	}

	@Override
	public void setSelection(ArrayList<Integer> selection) {
		if (selection == null) {
			selection = new ArrayList<Integer>();
		}
		if (!this.selection.equals(selection)) {
			this.selection = selection;
			tableModelObservable.fireSelectionChanged();
		}
	}

	@Override
	public ITableModelObservable getTableModelObservable() {
		return this;
	}

	@Override
	public void addTableModelListener(final ITableModelListener listener) {
		tableModelObservable.addTableModelListener(listener);
	}

	@Override
	public void removeTableModelListener(final ITableModelListener listener) {
		tableModelObservable.removeTableModelListener(listener);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	//Convenience methods start
	////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void setCell(final int rowIndex, final int columnIndex, final ITableCellBuilder cellBuilder) {
		setCell(rowIndex, columnIndex, cellBuilder.build());
	}

	@Override
	public void setCell(final int rowIndex, final int columnIndex, final String text) {
		setCell(rowIndex, columnIndex, new TableCellBuilder().setText(text));
	}

	@Override
	public void setCell(final int rowIndex, final int columnIndex, final String text, final IImageConstant icon) {
		setCell(rowIndex, columnIndex, new TableCellBuilder().setText(text).setIcon(icon));
	}

	@Override
	public void setCell(final int rowIndex, final int columnIndex, final String text, final boolean editable) {
		setCell(rowIndex, columnIndex, new TableCellBuilder().setText(text).setEditable(editable));
	}

	@Override
	public void setEditable(final int rowIndex, final int columnIndex, final boolean editable) {
		final TableCell cell = new TableCell(getCell(rowIndex, columnIndex));
		cell.setEditable(editable);
		setCell(rowIndex, columnIndex, cell);
	}

	@Override
	public void removeAllRows() {
		removeRows(0, getRowCount() - 1);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	//Convenience methods end
	////////////////////////////////////////////////////////////////////////////////////////////////

}
