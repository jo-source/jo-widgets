/*
 * Copyright (c) 2011, Nikolaus Moll
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
import java.util.Collections;

import org.jowidgets.common.model.ITableColumn;
import org.jowidgets.common.model.ITableColumnModel;
import org.jowidgets.common.model.ITableColumnModelListener;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.tools.controler.TableColumnModelObservable;

final class TableColumnModelSpiAdapter implements ITableColumnModel {

	private final DefaultTableColumnModel columnModel;
	private final TableColumnModelObservable tableColumnModelObservable;
	private final ArrayList<Integer> visibleColumns;

	TableColumnModelSpiAdapter(final DefaultTableColumnModel columnModel) {
		this.columnModel = columnModel;
		this.tableColumnModelObservable = new TableColumnModelObservable();
		this.visibleColumns = new ArrayList<Integer>();
		columnModel.addColumnModelListener(new TableColumnModelListener());
	}

	@Override
	public int getColumnCount() {
		return visibleColumns.size();
	}

	@Override
	public ITableColumn getColumn(final int columnIndex) {
		return columnModel.getColumn(visibleColumns.get(columnIndex));
	}

	@Override
	public ITableColumnModelObservable getTableColumnModelObservable() {
		return tableColumnModelObservable;
	}

	class TableColumnModelListener implements ITableColumnModelListener {

		@Override
		public void columnsAdded(final int[] columnIndices) {
			for (final int columnIndex : columnIndices) {
				final DefaultTableColumn column = (DefaultTableColumn) columnModel.getColumn(columnIndex);
				if (column.isVisible()) {
					visibleColumns.add(columnIndex);
				}
			}
			Collections.sort(visibleColumns);
		}

		@Override
		public void columnsRemoved(final int[] columnIndices) {
			for (final int columnIndex : columnIndices) {
				if (visibleColumns.contains(columnIndex)) {
					visibleColumns.remove(columnIndex);
				}
			}
		}

		@Override
		public void columnsChanged(final int[] viewIndicies) {
			for (final int logicalIndex : viewIndicies) {
				final DefaultTableColumn column = (DefaultTableColumn) columnModel.getColumn(logicalIndex);
				if (column.isVisible() == visibleColumns.contains(logicalIndex)) {
					if (column.isVisible()) {
						// only fire events on visible columns
						final int viewIndex = logicalToView(logicalIndex);
						tableColumnModelObservable.fireColumnsChanged(new int[] {viewIndex});
					}
					continue;
				}

				if (column.isVisible()) {
					// TODO NM improve 
					visibleColumns.add(logicalIndex);
					Collections.sort(visibleColumns);
					final int viewIndex = logicalToView(logicalIndex);
					tableColumnModelObservable.fireColumnsAdded(new int[] {viewIndex});
				}
				else {
					final int viewIndex = logicalToView(logicalIndex);
					visibleColumns.remove(viewIndex);
					tableColumnModelObservable.fireColumnsRemoved(new int[] {viewIndex});
				}
			}
		}
	}

	public int logicalToView(final int logicalIndex) {
		return visibleColumns.indexOf(logicalIndex);
	}

	public int viewToLogical(final int columnIndex) {
		return visibleColumns.get(columnIndex);
	}
}
