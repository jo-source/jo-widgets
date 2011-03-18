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

package org.jowidgets.tools.model.table;

import java.util.ArrayList;
import java.util.List;

import org.jowidgets.api.model.table.IDefaultTableColumn;
import org.jowidgets.api.model.table.IDefaultTableColumnBuilder;
import org.jowidgets.api.model.table.ISimpleTableModel;
import org.jowidgets.api.model.table.ISimpleTableModelBuilder;
import org.jowidgets.api.model.table.ITableCellBuilder;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.model.ITableModelObservable;
import org.jowidgets.common.types.AlignmentHorizontal;

public class SimpleTableModel implements ISimpleTableModel {

	private final ISimpleTableModel tableModel;

	public SimpleTableModel() {
		this(builder());
	}

	public SimpleTableModel(final int rowCount, final int columnCount) {
		this(builder(rowCount, columnCount));
	}

	public SimpleTableModel(final ISimpleTableModelBuilder tableModelBuilder) {
		this.tableModel = tableModelBuilder.build();
	}

	@Override
	public int getColumnCount() {
		return tableModel.getColumnCount();
	}

	@Override
	public int getRowCount() {
		return tableModel.getRowCount();
	}

	@Override
	public ITableCell getCell(final int rowIndex, final int columnIndex) {
		return tableModel.getCell(rowIndex, columnIndex);
	}

	@Override
	public ITableColumnModelObservable getTableColumnModelObservable() {
		return tableModel.getTableColumnModelObservable();
	}

	@Override
	public ArrayList<Integer> getSelection() {
		return tableModel.getSelection();
	}

	@Override
	public void setSelection(final ArrayList<Integer> selection) {
		tableModel.setSelection(selection);
	}

	@Override
	public ITableModelObservable getTableModelObservable() {
		return tableModel.getTableModelObservable();
	}

	@Override
	public void addRow() {
		tableModel.addRow();
	}

	@Override
	public void addRow(final int rowIndex) {
		tableModel.addRow(rowIndex);
	}

	@Override
	public IDefaultTableColumn getColumn(final int columnIndex) {
		return tableModel.getColumn(columnIndex);
	}

	@Override
	public void addRows(final int rowIndex, final int rowCount) {
		tableModel.addRows(rowIndex, rowCount);
	}

	@Override
	public void addRow(final ITableCell... cells) {
		tableModel.addRow(cells);
	}

	@Override
	public ArrayList<IDefaultTableColumn> getColumns() {
		return tableModel.getColumns();
	}

	@Override
	public void addRow(final int rowIndex, final ITableCell... cells) {
		tableModel.addRow(rowIndex, cells);
	}

	@Override
	public IDefaultTableColumn addColumn() {
		return tableModel.addColumn();
	}

	@Override
	public IDefaultTableColumn addColumn(final int columnIndex) {
		return tableModel.addColumn(columnIndex);
	}

	@Override
	public void addRow(final ITableCellBuilder... cellBuilders) {
		tableModel.addRow(cellBuilders);
	}

	@Override
	public void addRow(final int rowIndex, final ITableCellBuilder... cellBuilders) {
		tableModel.addRow(rowIndex, cellBuilders);
	}

	@Override
	public void addColumn(final IDefaultTableColumn column) {
		tableModel.addColumn(column);
	}

	@Override
	public void addColumn(final int columnIndex, final IDefaultTableColumn column) {
		tableModel.addColumn(columnIndex, column);
	}

	@Override
	public void addRow(final String... cellTexts) {
		tableModel.addRow(cellTexts);
	}

	@Override
	public void addRow(final int rowIndex, final String... cellTexts) {
		tableModel.addRow(rowIndex, cellTexts);
	}

	@Override
	public IDefaultTableColumn addColumn(final IDefaultTableColumnBuilder columnBuilder) {
		return tableModel.addColumn(columnBuilder);
	}

	@Override
	public void removeRow(final int index) {
		tableModel.removeRow(index);
	}

	@Override
	public void removeRows(final int fromIndex, final int toIndex) {
		tableModel.removeRows(fromIndex, toIndex);
	}

	@Override
	public void removeRows(final int... rows) {
		tableModel.removeRows(rows);
	}

	@Override
	public void removeRows(final List<Integer> rows) {
		tableModel.removeRows(rows);
	}

	@Override
	public IDefaultTableColumn addColumn(final int columnIndex, final IDefaultTableColumnBuilder columnBuilder) {
		return tableModel.addColumn(columnIndex, columnBuilder);
	}

	@Override
	public void removeAllRows() {
		tableModel.removeAllRows();
	}

	@Override
	public void setCell(final int rowIndex, final int columnIndex, final ITableCell cell) {
		tableModel.setCell(rowIndex, columnIndex, cell);
	}

	@Override
	public IDefaultTableColumn addColumn(final String text) {
		return tableModel.addColumn(text);
	}

	@Override
	public void setCell(final int rowIndex, final int columnIndex, final ITableCellBuilder cellBuilder) {
		tableModel.setCell(rowIndex, columnIndex, cellBuilder);
	}

	@Override
	public IDefaultTableColumn addColumn(final String text, final String toolTipText) {
		return tableModel.addColumn(text, toolTipText);
	}

	@Override
	public void setCell(final int rowIndex, final int columnIndex, final String text) {
		tableModel.setCell(rowIndex, columnIndex, text);
	}

	@Override
	public void removeColumn(final int columnIndex) {
		tableModel.removeColumn(columnIndex);
	}

	@Override
	public void removeColumns(final int fromColumnIndex, final int toColumnIndex) {
		tableModel.removeColumns(fromColumnIndex, toColumnIndex);
	}

	@Override
	public void setCell(final int rowIndex, final int columnIndex, final String text, final IImageConstant icon) {
		tableModel.setCell(rowIndex, columnIndex, text, icon);
	}

	@Override
	public void removeColumns(final int[] columns) {
		tableModel.removeColumns(columns);
	}

	@Override
	public void setCell(final int rowIndex, final int columnIndex, final String text, final boolean editable) {
		tableModel.setCell(rowIndex, columnIndex, text, editable);
	}

	@Override
	public void removeAllColumns() {
		tableModel.removeAllColumns();
	}

	@Override
	public void modifyModelStart() {
		tableModel.modifyModelStart();
	}

	@Override
	public void setEditable(final int rowIndex, final int columnIndex, final boolean editable) {
		tableModel.setEditable(rowIndex, columnIndex, editable);
	}

	@Override
	public void modifyModelEnd() {
		tableModel.modifyModelEnd();
	}

	@Override
	public void setColumn(final int columnIndex, final IDefaultTableColumn column) {
		tableModel.setColumn(columnIndex, column);
	}

	@Override
	public IDefaultTableColumn setColumn(final int columnIndex, final IDefaultTableColumnBuilder columnBuilder) {
		return tableModel.setColumn(columnIndex, columnBuilder);
	}

	@Override
	public void setText(final int columnIndex, final String text) {
		tableModel.setText(columnIndex, text);
	}

	@Override
	public void setToolTipText(final int columnIndex, final String tooltipText) {
		tableModel.setToolTipText(columnIndex, tooltipText);
	}

	@Override
	public void setIcon(final int columnIndex, final IImageConstant icon) {
		tableModel.setIcon(columnIndex, icon);
	}

	@Override
	public void setAlignment(final int columnIndex, final AlignmentHorizontal alignment) {
		tableModel.setAlignment(columnIndex, alignment);
	}

	public static ISimpleTableModelBuilder builder() {
		return Toolkit.getModelFactoryProvider().getTableModelFactory().simpleTableModelBuilder();
	}

	public static ISimpleTableModelBuilder builder(final int rowCount, final int columnCount) {
		return builder().setRowCount(rowCount).setColumnCount(columnCount);
	}

}
