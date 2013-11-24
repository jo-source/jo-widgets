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
import java.util.Collection;
import java.util.List;

import org.jowidgets.api.model.table.IDefaultTableColumn;
import org.jowidgets.api.model.table.IDefaultTableColumnBuilder;
import org.jowidgets.api.model.table.ISimpleTableModel;
import org.jowidgets.api.model.table.ISimpleTableModelBuilder;
import org.jowidgets.api.model.table.ITableCellBuilder;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelListener;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.model.ITableDataModelListener;
import org.jowidgets.common.model.ITableDataModelObservable;
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
	public final int getColumnCount() {
		return tableModel.getColumnCount();
	}

	@Override
	public final int getRowCount() {
		return tableModel.getRowCount();
	}

	@Override
	public final ITableCell getCell(final int rowIndex, final int columnIndex) {
		return tableModel.getCell(rowIndex, columnIndex);
	}

	@Override
	public final ITableColumnModelObservable getTableColumnModelObservable() {
		return tableModel.getTableColumnModelObservable();
	}

	@Override
	public final ArrayList<Integer> getSelection() {
		return tableModel.getSelection();
	}

	@Override
	public final void setSelection(final Collection<Integer> selection) {
		tableModel.setSelection(selection);
	}

	@Override
	public final int getFirstSelectedRow() {
		return tableModel.getFirstSelectedRow();
	}

	@Override
	public final int getLastSelectedRow() {
		return tableModel.getLastSelectedRow();
	}

	@Override
	public final ITableDataModelObservable getTableDataModelObservable() {
		return tableModel.getTableDataModelObservable();
	}

	@Override
	public final void addDataModelListener(final ITableDataModelListener listener) {
		tableModel.addDataModelListener(listener);
	}

	@Override
	public final void removeDataModelListener(final ITableDataModelListener listener) {
		tableModel.removeDataModelListener(listener);
	}

	@Override
	public final void addColumnModelListener(final ITableColumnModelListener listener) {
		tableModel.addColumnModelListener(listener);
	}

	@Override
	public final void removeColumnModelListener(final ITableColumnModelListener listener) {
		tableModel.removeColumnModelListener(listener);
	}

	@Override
	public final ArrayList<ITableCell> getRow(final int rowIndex) {
		return tableModel.getRow(rowIndex);
	}

	@Override
	public final ArrayList<String> getRowTexts(final int rowIndex) {
		return tableModel.getRowTexts(rowIndex);
	}

	@Override
	public final void addRow() {
		tableModel.addRow();
	}

	@Override
	public final void addRow(final int rowIndex) {
		tableModel.addRow(rowIndex);
	}

	@Override
	public final IDefaultTableColumn getColumn(final int columnIndex) {
		return tableModel.getColumn(columnIndex);
	}

	@Override
	public final void addRows(final int rowIndex, final int rowCount) {
		tableModel.addRows(rowIndex, rowCount);
	}

	@Override
	public final void addRow(final ITableCell... cells) {
		tableModel.addRow(cells);
	}

	@Override
	public final ArrayList<IDefaultTableColumn> getColumns() {
		return tableModel.getColumns();
	}

	@Override
	public final void addRow(final int rowIndex, final ITableCell... cells) {
		tableModel.addRow(rowIndex, cells);
	}

	@Override
	public final void addRow(final List<String> cellTexts) {
		tableModel.addRow(cellTexts);
	}

	@Override
	public final void addRow(final int rowIndex, final List<String> cellTexts) {
		tableModel.addRow(rowIndex, cellTexts);
	}

	@Override
	public final IDefaultTableColumn addColumn() {
		return tableModel.addColumn();
	}

	@Override
	public final IDefaultTableColumn addColumn(final int columnIndex) {
		return tableModel.addColumn(columnIndex);
	}

	@Override
	public final void addRow(final ITableCellBuilder... cellBuilders) {
		tableModel.addRow(cellBuilders);
	}

	@Override
	public final void addRow(final int rowIndex, final ITableCellBuilder... cellBuilders) {
		tableModel.addRow(rowIndex, cellBuilders);
	}

	@Override
	public final void addColumn(final IDefaultTableColumn column) {
		tableModel.addColumn(column);
	}

	@Override
	public final void addColumn(final int columnIndex, final IDefaultTableColumn column) {
		tableModel.addColumn(columnIndex, column);
	}

	@Override
	public final void addRow(final String... cellTexts) {
		tableModel.addRow(cellTexts);
	}

	@Override
	public final void addRow(final int rowIndex, final String... cellTexts) {
		tableModel.addRow(rowIndex, cellTexts);
	}

	@Override
	public final IDefaultTableColumn addColumn(final IDefaultTableColumnBuilder columnBuilder) {
		return tableModel.addColumn(columnBuilder);
	}

	@Override
	public final void removeRow(final int index) {
		tableModel.removeRow(index);
	}

	@Override
	public final void removeRows(final int fromIndex, final int toIndex) {
		tableModel.removeRows(fromIndex, toIndex);
	}

	@Override
	public final void removeRows(final int... rows) {
		tableModel.removeRows(rows);
	}

	@Override
	public final void removeRows(final List<Integer> rows) {
		tableModel.removeRows(rows);
	}

	@Override
	public final IDefaultTableColumn addColumn(final int columnIndex, final IDefaultTableColumnBuilder columnBuilder) {
		return tableModel.addColumn(columnIndex, columnBuilder);
	}

	@Override
	public final void removeAllRows() {
		tableModel.removeAllRows();
	}

	@Override
	public final void setCell(final int rowIndex, final int columnIndex, final ITableCell cell) {
		tableModel.setCell(rowIndex, columnIndex, cell);
	}

	@Override
	public final IDefaultTableColumn addColumn(final String text) {
		return tableModel.addColumn(text);
	}

	@Override
	public final void setCell(final int rowIndex, final int columnIndex, final ITableCellBuilder cellBuilder) {
		tableModel.setCell(rowIndex, columnIndex, cellBuilder);
	}

	@Override
	public final IDefaultTableColumn addColumn(final String text, final String toolTipText) {
		return tableModel.addColumn(text, toolTipText);
	}

	@Override
	public final void setCell(final int rowIndex, final int columnIndex, final String text) {
		tableModel.setCell(rowIndex, columnIndex, text);
	}

	@Override
	public final void removeColumn(final int columnIndex) {
		tableModel.removeColumn(columnIndex);
	}

	@Override
	public final void removeColumns(final int fromColumnIndex, final int toColumnIndex) {
		tableModel.removeColumns(fromColumnIndex, toColumnIndex);
	}

	@Override
	public final void setCell(final int rowIndex, final int columnIndex, final String text, final IImageConstant icon) {
		tableModel.setCell(rowIndex, columnIndex, text, icon);
	}

	@Override
	public final void removeColumns(final int... columns) {
		tableModel.removeColumns(columns);
	}

	@Override
	public final void setCell(final int rowIndex, final int columnIndex, final String text, final boolean editable) {
		tableModel.setCell(rowIndex, columnIndex, text, editable);
	}

	@Override
	public final void setRow(final int rowIndex, final ITableCell... cells) {
		tableModel.setRow(rowIndex, cells);
	}

	@Override
	public final void setRow(final int rowIndex, final ITableCellBuilder... cellBuilders) {
		tableModel.setRow(rowIndex, cellBuilders);
	}

	@Override
	public final void setCellText(final int rowIndex, final int columnIndex, final String text) {
		tableModel.setCellText(rowIndex, columnIndex, text);
	}

	@Override
	public final void setCellTooltipText(final int rowIndex, final int columnIndex, final String tooltipText) {
		tableModel.setCellTooltipText(rowIndex, columnIndex, tooltipText);
	}

	@Override
	public final void setCellIcon(final int rowIndex, final int columnIndex, final IImageConstant icon) {
		tableModel.setCellIcon(rowIndex, columnIndex, icon);
	}

	@Override
	public final void setRowTexts(final int rowIndex, final String... cellTexts) {
		tableModel.setRowTexts(rowIndex, cellTexts);
	}

	@Override
	public final void setRowTexts(final int rowIndex, final List<String> cellTexts) {
		tableModel.setRowTexts(rowIndex, cellTexts);
	}

	@Override
	public final void removeAllColumns() {
		tableModel.removeAllColumns();
	}

	@Override
	public final void modifyModelStart() {
		tableModel.modifyModelStart();
	}

	@Override
	public final void setCellEditable(final int rowIndex, final int columnIndex, final boolean editable) {
		tableModel.setCellEditable(rowIndex, columnIndex, editable);
	}

	@Override
	public final void modifyModelEnd() {
		tableModel.modifyModelEnd();
	}

	@Override
	public void setFireEvents(final boolean fireEvents) {
		tableModel.setFireEvents(fireEvents);
	}

	@Override
	public final void setColumn(final int columnIndex, final IDefaultTableColumn column) {
		tableModel.setColumn(columnIndex, column);
	}

	@Override
	public final IDefaultTableColumn setColumn(final int columnIndex, final IDefaultTableColumnBuilder columnBuilder) {
		return tableModel.setColumn(columnIndex, columnBuilder);
	}

	@Override
	public final void setColumnText(final int columnIndex, final String text) {
		tableModel.setColumnText(columnIndex, text);
	}

	@Override
	public final void setColumnToolTipText(final int columnIndex, final String tooltipText) {
		tableModel.setColumnToolTipText(columnIndex, tooltipText);
	}

	@Override
	public final void setColumnIcon(final int columnIndex, final IImageConstant icon) {
		tableModel.setColumnIcon(columnIndex, icon);
	}

	@Override
	public final void setColumnAlignment(final int columnIndex, final AlignmentHorizontal alignment) {
		tableModel.setColumnAlignment(columnIndex, alignment);
	}

	public static ISimpleTableModelBuilder builder() {
		return Toolkit.getModelFactoryProvider().getTableModelFactory().simpleTableModelBuilder();
	}

	public static ISimpleTableModelBuilder builder(final int rowCount, final int columnCount) {
		return builder().setRowCount(rowCount).setColumnCount(columnCount);
	}

}
