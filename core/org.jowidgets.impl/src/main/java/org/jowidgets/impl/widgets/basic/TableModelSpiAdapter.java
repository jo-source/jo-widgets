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
import org.jowidgets.common.model.ITableColumnModelListener;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.model.ITableColumnModelSpi;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.model.ITableDataModelObservable;
import org.jowidgets.tools.controler.TableColumnModelObservable;
import org.jowidgets.util.Assert;

public class TableModelSpiAdapter implements ITableColumnModelSpi, ITableDataModel {

	private final ITableColumnModel columnModel;
	private final ITableDataModel dataModel;

	private final TableColumnModelObservable columnModelObservable;

	private final int[] modelToView;
	private int[] viewToModel; // visible List

	public TableModelSpiAdapter(final ITableColumnModel columnModel, final ITableDataModel dataModel) {
		Assert.paramNotNull(columnModel, "columnModel");
		Assert.paramNotNull(dataModel, "dataModel");
		this.columnModel = columnModel;
		this.dataModel = dataModel;

		this.columnModelObservable = new TableColumnModelObservable();

		modelToView = new int[columnModel.getColumnCount()];
		updateMappings();

		// Delegate events from app model to spi model
		columnModel.getTableColumnModelObservable().addColumnModelListener(new ITableColumnModelListener() {

			@Override
			public void columnsRemoved(final int[] columnIndices) {}

			@Override
			public void columnsChanged(final int[] columnIndices) {
				for (final int columnIndex : columnIndices) {
					final ITableColumn column = columnModel.getColumn(columnIndex);
					if (column.isVisible()) {
						if (modelToView[columnIndex] < 0) {
							final int index = showColumn(columnIndex);
							columnModelObservable.fireColumnsAdded(new int[] {index});
						}
						else {
							columnModelObservable.fireColumnsChanged(new int[] {modelToView[columnIndex]});
						}

					}
					else if (modelToView[columnIndex] >= 0) {
						final int index = hideColumn(columnIndex);
						columnModelObservable.fireColumnsRemoved(new int[] {index});
					}
				}
			}

			@Override
			public void columnsAdded(final int[] columnIndex) {}
		});
	}

	public int viewToModel(final int columnIndex) {
		return viewToModel(columnIndex);
	}

	private void updateMappings() {
		final ArrayList<Integer> visibleList = new ArrayList<Integer>();
		for (int i = 0; i < modelToView.length; i++) {
			if (modelToView[i] >= 0) {
				modelToView[i] = visibleList.size();
				visibleList.add(i);
			}
			else {
				modelToView[i] = -1;
			}
		}

		viewToModel = new int[visibleList.size()];
		for (int i = 0; i < viewToModel.length; i++) {
			viewToModel[i] = visibleList.get(i);
		}
	}

	private int showColumn(final int columnIndex) {
		if (modelToView[columnIndex] < 0) {
			modelToView[columnIndex] = 1;
			updateMappings();
		}
		return modelToView[columnIndex];
	}

	private int hideColumn(final int columnIndex) {
		final int result = modelToView[columnIndex];
		if (modelToView[columnIndex] >= 0) {
			modelToView[columnIndex] = -1;
			updateMappings();
		}
		return result;
	}

	@Override
	public int getColumnCount() {
		return viewToModel.length;
	}

	@Override
	public ITableColumn getColumn(final int columnIndex) {
		return columnModel.getColumn(viewToModel[columnIndex]);
	}

	@Override
	public ITableColumnModelObservable getTableColumnModelObservable() {
		return columnModelObservable;
	}

	@Override
	public int getRowCount() {
		return dataModel.getRowCount();
	}

	@Override
	public ITableCell getCell(final int rowIndex, final int columnIndex) {
		return dataModel.getCell(rowIndex, viewToModel[columnIndex]);
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

	public int convertViewToModel(final int columnIndex) {
		return viewToModel[columnIndex];
	}

}
