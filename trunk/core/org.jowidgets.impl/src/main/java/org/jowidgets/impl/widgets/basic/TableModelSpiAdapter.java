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

package org.jowidgets.impl.widgets.basic;

import java.util.ArrayList;
import java.util.List;

import org.jowidgets.api.model.table.ITableColumn;
import org.jowidgets.api.model.table.ITableColumnModel;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.model.ITableColumnModelSpi;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.model.ITableDataModelObservable;
import org.jowidgets.util.Assert;

public class TableModelSpiAdapter implements ITableColumnModelSpi, ITableDataModel {

	private final ITableColumnModel columnModel;
	private final ITableDataModel dataModel;

	public TableModelSpiAdapter(final ITableColumnModel columnModel, final ITableDataModel dataModel) {
		Assert.paramNotNull(columnModel, "columnModel");
		Assert.paramNotNull(dataModel, "dataModel");
		this.columnModel = columnModel;
		this.dataModel = dataModel;
	}

	@Override
	public int getColumnCount() {
		return columnModel.getColumnCount();
	}

	@Override
	public ITableColumn getColumn(final int columnIndex) {
		return columnModel.getColumn(columnIndex);
	}

	@Override
	public ITableColumnModelObservable getTableColumnModelObservable() {
		return columnModel.getTableColumnModelObservable();
	}

	@Override
	public int getRowCount() {
		return dataModel.getRowCount();
	}

	@Override
	public ITableCell getCell(final int rowIndex, final int columnIndex) {
		return dataModel.getCell(rowIndex, columnIndex);
	}

	@Override
	public ArrayList<Integer> getSelection() {
		return dataModel.getSelection();
	}

	@Override
	public void setSelection(final List<Integer> selection) {
		dataModel.setSelection(selection);
	}

	@Override
	public ITableDataModelObservable getTableDataModelObservable() {
		return dataModel.getTableDataModelObservable();
	}

}
