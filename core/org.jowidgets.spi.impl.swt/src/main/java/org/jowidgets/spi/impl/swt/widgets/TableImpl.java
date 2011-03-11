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
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jowidgets.common.types.SelectionPolicy;
import org.jowidgets.common.widgets.controler.ITableCellEditorListener;
import org.jowidgets.common.widgets.controler.ITableCellListener;
import org.jowidgets.common.widgets.controler.ITableCellPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableColumnListener;
import org.jowidgets.common.widgets.controler.ITableColumnPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableSelectionListener;
import org.jowidgets.spi.widgets.ITableCellSpi;
import org.jowidgets.spi.widgets.ITableColumnSpi;
import org.jowidgets.spi.widgets.ITableSpi;
import org.jowidgets.spi.widgets.setup.ITableSetupSpi;

public class TableImpl extends SwtControl implements ITableSpi {

	private final boolean columnsMoveable;
	private final boolean columnsResizeable;

	private ArrayList<TableColumnImpl> columns;
	private ArrayList<ArrayList<TableCellImpl>> data;

	public TableImpl(final Object parentUiReference, final ITableSetupSpi setup) {
		super(new Table((Composite) parentUiReference, getStyle(setup)));

		this.columns = new ArrayList<TableColumnImpl>();
		this.data = new ArrayList<ArrayList<TableCellImpl>>();

		this.columnsMoveable = setup.getColumnsMoveable();
		this.columnsResizeable = setup.getColumnsResizeable();

		getUiReference().setLinesVisible(true);
		getUiReference().setHeaderVisible(true);
	}

	@Override
	public Table getUiReference() {
		return (Table) super.getUiReference();
	}

	@Override
	public void initialize(final int rowsCount, final int columnsCount) {
		getUiReference().setRedraw(false);
		for (final ArrayList<TableCellImpl> rowList : data) {
			rowList.clear();
		}
		data = new ArrayList<ArrayList<TableCellImpl>>(rowsCount);
		columns = new ArrayList<TableColumnImpl>(columnsCount);

		for (int rowIndex = 0; rowIndex < rowsCount; rowIndex++) {
			final TableItem item = new TableItem(getUiReference(), SWT.NONE);
			final ArrayList<TableCellImpl> columnList = new ArrayList<TableCellImpl>(columnsCount);
			data.add(columnList);
			for (int columnIndex = 0; columnIndex < columnsCount; columnIndex++) {
				columnList.add(new TableCellImpl(item, columnIndex));
			}
		}

		for (int columnIndex = 0; columnIndex < columnsCount; columnIndex++) {
			final TableColumn column = new TableColumn(getUiReference(), SWT.NONE);
			column.setWidth(50);
			columns.add(new TableColumnImpl(column, columnsMoveable, columnsResizeable));
		}
		getUiReference().setRedraw(true);
	}

	@Override
	public ITableCellSpi getCell(final int rowIndex, final int columnIndex) {
		return data.get(rowIndex).get(columnIndex);
	}

	@Override
	public ITableColumnSpi getColumn(final int columnIndex) {
		return columns.get(columnIndex);
	}

	@Override
	public ITableColumnSpi insertColumn(final int columnIndex) {
		return null;
	}

	@Override
	public ITableColumnSpi insertColumns(final int columnIndex, final int columnsCount) {
		return null;
	}

	@Override
	public void removeColumn(final int columnIndex) {

	}

	@Override
	public void removeColumns(final int columnIndex, final int columnsCount) {

	}

	@Override
	public void insertRow(final int index) {

	}

	@Override
	public void insertRows(final int index, final int rowsCount) {

	}

	@Override
	public void removeRow(final int index) {

	}

	@Override
	public void removeRows(final int index, final int rowsCount) {

	}

	@Override
	public ArrayList<Integer> getSelection() {
		final ArrayList<Integer> result = new ArrayList<Integer>();
		for (final int index : getUiReference().getSelectionIndices()) {
			result.add(Integer.valueOf(index));
		}
		return result;
	}

	@Override
	public void setSelection(final List<Integer> selection) {
		final int[] selectionArray = new int[selection.size()];
		int arrayIndex = 0;
		for (final Integer index : selection) {
			selectionArray[arrayIndex] = index;
			arrayIndex++;
		}
		getUiReference().setSelection(selectionArray);
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
	public void addTableItemListener(final ITableCellListener listener) {

	}

	@Override
	public void removeTableItemListener(final ITableCellListener listener) {

	}

	@Override
	public void addTableItemEditorListener(final ITableCellEditorListener listener) {

	}

	@Override
	public void removeTableItemEditorListener(final ITableCellEditorListener listener) {

	}

	@Override
	public void addTableItemPopupDetectionListener(final ITableCellPopupDetectionListener listener) {

	}

	@Override
	public void removeTableItemPopupDetectionListener(final ITableCellPopupDetectionListener listener) {

	}

	@Override
	public void addTableSelectionListener(final ITableSelectionListener listener) {

	}

	@Override
	public void removeTableSelectionListener(final ITableSelectionListener listener) {

	}

	@Override
	public void addTableColumnListener(final ITableColumnListener listener) {

	}

	@Override
	public void removeTableColumnListener(final ITableColumnListener listener) {

	}

	@Override
	public void addTableColumnPopupDetectionListener(final ITableColumnPopupDetectionListener listener) {

	}

	@Override
	public void removeTableColumnPopupDetectionListener(final ITableColumnPopupDetectionListener listener) {

	}

	private static int getStyle(final ITableSetupSpi setup) {
		int result = SWT.FULL_SELECTION;

		if (SelectionPolicy.MULTI_SELECTION == setup.getSelectionPolicy()) {
			result = result | SWT.MULTI;
		}
		else if (SelectionPolicy.SINGLE_SELECTION != setup.getSelectionPolicy()) {
			throw new IllegalArgumentException("SelectionPolicy '" + setup.getSelectionPolicy() + "' is not known");
		}

		return result;
	}
}
