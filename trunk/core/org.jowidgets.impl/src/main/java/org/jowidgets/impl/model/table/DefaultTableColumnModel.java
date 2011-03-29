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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jowidgets.api.controler.IChangeListener;
import org.jowidgets.api.model.table.IDefaultTableColumn;
import org.jowidgets.api.model.table.IDefaultTableColumnBuilder;
import org.jowidgets.api.model.table.IDefaultTableColumnModel;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.model.ITableColumnModelListener;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.tools.controler.TableColumnModelObservable;
import org.jowidgets.util.ArrayUtils;
import org.jowidgets.util.Assert;

class DefaultTableColumnModel implements IDefaultTableColumnModel, ITableColumnModelObservable {

	private final TableColumnModelObservable tableColumnModelObservable;
	private final ArrayList<IDefaultTableColumn> columns;

	private final Map<Integer, IChangeListener> columnChangeListeners;

	private boolean fireEvents;
	private boolean eventsFreezed;
	private ArrayList<IDefaultTableColumn> freezedColumns;
	private Set<IDefaultTableColumn> modifiedColumns;

	DefaultTableColumnModel(final int columnCount) {
		this.eventsFreezed = false;

		if (columnCount < 0) {
			throw new IllegalArgumentException("Column count must be a positive number.");
		}

		this.tableColumnModelObservable = new TableColumnModelObservable();
		this.columns = new ArrayList<IDefaultTableColumn>(columnCount);
		this.columnChangeListeners = new HashMap<Integer, IChangeListener>();

		for (int i = 0; i < columnCount; i++) {
			addColumn();
		}
	}

	@Override
	public void modifyModelStart() {
		if (eventsFreezed) {
			throw new IllegalStateException("The method 'modifyModelStart()' was already invoked.");
		}
		this.freezedColumns = new ArrayList<IDefaultTableColumn>(columns);
		this.modifiedColumns = new HashSet<IDefaultTableColumn>();
		this.eventsFreezed = true;
	}

	@Override
	public void modifyModelEnd() {
		if (!eventsFreezed) {
			throw new IllegalStateException("The method 'modifyModelStart()' must be invoked first.");
		}

		//determine the added columns
		final List<Integer> addedColumnsIndices = new ArrayList<Integer>();
		for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
			if (!freezedColumns.contains(columns.get(columnIndex))) {
				addedColumnsIndices.add(Integer.valueOf(columnIndex));
			}
		}

		//determine the removed columns
		final List<Integer> removedColumnsIndices = new ArrayList<Integer>();
		for (int columnIndex = 0; columnIndex < freezedColumns.size(); columnIndex++) {
			if (!columns.contains(freezedColumns.get(columnIndex))) {
				removedColumnsIndices.add(Integer.valueOf(columnIndex));
			}
		}

		//determine the (residual) modified columns
		final List<Integer> modifiedColumnIndices = new ArrayList<Integer>();
		for (final IDefaultTableColumn column : modifiedColumns) {
			if (!addedColumnsIndices.contains(column) && !removedColumnsIndices.contains(column)) {
				final int modifiedColumnIndex = columns.indexOf(column);
				if (modifiedColumnIndex != -1) {
					modifiedColumnIndices.add(Integer.valueOf(modifiedColumnIndex));
				}
			}
		}

		//now fire the events
		Collections.sort(addedColumnsIndices);
		Collections.sort(removedColumnsIndices);
		Collections.sort(modifiedColumnIndices);

		final int[] added = ArrayUtils.toArray(addedColumnsIndices);
		final int[] removed = ArrayUtils.toArray(removedColumnsIndices);
		final int[] modified = ArrayUtils.toArray(modifiedColumnIndices);

		if (removed.length > 0 && fireEvents) {
			tableColumnModelObservable.fireColumnsRemoved(removed);
		}
		if (added.length > 0 && fireEvents) {
			tableColumnModelObservable.fireColumnsAdded(added);
		}
		if (modified.length > 0 && fireEvents) {
			tableColumnModelObservable.fireColumnsAdded(modified);
		}

		this.eventsFreezed = false;
		this.freezedColumns = null;
		this.modifiedColumns = null;
	}

	@Override
	public void setFireEvents(final boolean fireEvents) {
		this.fireEvents = fireEvents;
	}

	protected boolean isFireEvents() {
		return fireEvents;
	}

	@Override
	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public IDefaultTableColumn getColumn(final int columnIndex) {
		checkIndex(columnIndex);
		return columns.get(columnIndex);
	}

	@Override
	public ArrayList<IDefaultTableColumn> getColumns() {
		return new ArrayList<IDefaultTableColumn>(columns);
	}

	@Override
	public void addColumn(final int columnIndex, final IDefaultTableColumn column) {
		checkIndexForAdd(columnIndex);
		columns.add(columnIndex, column);
		columnAdded(columnIndex, column);
	}

	@Override
	public void setColumn(final int columnIndex, final IDefaultTableColumn column) {
		checkIndex(columnIndex);
		columns.set(columnIndex, column);
		columnChanged(columnIndex);
	}

	@Override
	public void removeColumns(final int... columnIndices) {
		Assert.paramNotNull(columnIndices, "columnIndices");

		//first convert array into a set
		final Set<Integer> columnsToRemove = new HashSet<Integer>();
		for (int i = 0; i < columnIndices.length; i++) {
			columnsToRemove.add(Integer.valueOf(columnIndices[i]));
		}

		//create a new array list that will hold the not removed columns
		final int newColumnsCount = columns.size() - columnsToRemove.size();
		final ArrayList<IDefaultTableColumn> newColumns = new ArrayList<IDefaultTableColumn>(newColumnsCount);

		//iterate over all columns, add columns that are not removed to the new array list and unregister removed columns
		for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
			final IDefaultTableColumn column = columns.get(columnIndex);
			if (columnsToRemove.contains(Integer.valueOf(columnIndex))) {
				final IChangeListener changeListener = columnChangeListeners.remove(Integer.valueOf(columnIndex));
				if (changeListener != null) {
					column.removeChangeListener(changeListener);
				}
			}
			else {
				newColumns.add(column);
			}
		}

		columns.clear();
		columns.addAll(newColumns);
		columnsRemoved(columnIndices);
	}

	@Override
	public ITableColumnModelObservable getTableColumnModelObservable() {
		return this;
	}

	@Override
	public void addColumnModelListener(final ITableColumnModelListener listener) {
		tableColumnModelObservable.addColumnModelListener(listener);
	}

	@Override
	public void removeColumnModelListener(final ITableColumnModelListener listener) {
		tableColumnModelObservable.removeColumnModelListener(listener);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	//Convenience methods start
	////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public IDefaultTableColumn addColumn() {
		return addColumn(columns.size());
	}

	@Override
	public IDefaultTableColumn addColumn(final int columnIndex) {
		final IDefaultTableColumn result = new DefaultTableColumnBuilder().build();
		addColumn(columnIndex, result);
		return result;
	}

	@Override
	public void addColumn(final IDefaultTableColumn column) {
		addColumn(columns.size(), column);
	}

	@Override
	public IDefaultTableColumn addColumn(final IDefaultTableColumnBuilder columnBuilder) {
		return addColumn(columns.size(), columnBuilder);
	}

	@Override
	public IDefaultTableColumn addColumn(final int columnIndex, final IDefaultTableColumnBuilder columnBuilder) {
		final IDefaultTableColumn result = columnBuilder.build();
		addColumn(columnIndex, result);
		return result;
	}

	@Override
	public IDefaultTableColumn addColumn(final String text) {
		return addColumn(new DefaultTableColumnBuilder().setText(text));
	}

	@Override
	public IDefaultTableColumn addColumn(final String text, final String toolTipText) {
		return addColumn(new DefaultTableColumnBuilder().setText(text).setToolTipText(toolTipText));
	}

	@Override
	public IDefaultTableColumn setColumn(final int columnIndex, final IDefaultTableColumnBuilder columnBuilder) {
		checkIndex(columnIndex);
		final IDefaultTableColumn result = columnBuilder.build();
		setColumn(columnIndex, result);
		return result;
	}

	@Override
	public void setColumnText(final int columnIndex, final String text) {
		checkIndex(columnIndex);
		getColumn(columnIndex).setText(text);
	}

	@Override
	public void setColumnToolTipText(final int columnIndex, final String tooltipText) {
		checkIndex(columnIndex);
		getColumn(columnIndex).setToolTipText(tooltipText);
	}

	@Override
	public void setColumnIcon(final int columnIndex, final IImageConstant icon) {
		checkIndex(columnIndex);
		getColumn(columnIndex).setIcon(icon);
	}

	@Override
	public void setColumnAlignment(final int columnIndex, final AlignmentHorizontal alignment) {
		checkIndex(columnIndex);
		getColumn(columnIndex).setAlignment(alignment);
	}

	@Override
	public void removeColumn(final int columnIndex) {
		removeColumns(new int[] {columnIndex});
	}

	@Override
	public void removeColumns(final int fromColumnIndex, final int toColumnIndex) {
		if (fromColumnIndex > toColumnIndex) {
			throw new IllegalArgumentException("From index must be less or equal than to index.");
		}
		checkIndex(fromColumnIndex);
		checkIndex(toColumnIndex);

		final int[] indices = new int[1 + toColumnIndex - fromColumnIndex];
		for (int i = 0; i < indices.length; i++) {
			indices[i] = fromColumnIndex + i;
		}
		removeColumns(indices);
	}

	@Override
	public void removeAllColumns() {
		if (columns.size() > 0) {
			removeColumns(0, columns.size() - 1);
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	//Convenience methods end
	////////////////////////////////////////////////////////////////////////////////////////////////

	private void columnAdded(final int columnIndex, final IDefaultTableColumn column) {
		final ColumnChangeListener changeListener = new ColumnChangeListener(columnIndex);
		columnChangeListeners.put(Integer.valueOf(columnIndex), changeListener);
		column.addChangeListener(changeListener);
		if (!eventsFreezed && fireEvents) {
			tableColumnModelObservable.fireColumnsAdded(new int[] {columnIndex});
		}
	}

	protected boolean isEventsFreezed() {
		return eventsFreezed;
	}

	private void columnChanged(final int columnIndex) {
		if (!eventsFreezed && fireEvents) {
			tableColumnModelObservable.fireColumnsChanged(new int[] {columnIndex});
		}
		else if (eventsFreezed) {
			modifiedColumns.add(columns.get(columnIndex));
		}
	}

	private void columnsRemoved(final int[] columnIndices) {
		if (!eventsFreezed && fireEvents) {
			tableColumnModelObservable.fireColumnsRemoved(columnIndices);
		}
	}

	private void checkIndexForAdd(final int columnIndex) {
		checkIndex(columnIndex, columns.size());
	}

	private void checkIndex(final int columnIndex) {
		checkIndex(columnIndex, columns.size() - 1);
	}

	private void checkIndex(final int columnIndex, final int maxIndex) {
		if (columnIndex < 0 || columnIndex > maxIndex) {
			throw new IllegalArgumentException("Column index must be between '"
				+ 0
				+ "' and '"
				+ maxIndex
				+ "', but is '"
				+ columnIndex
				+ "'.");
		}
	}

	class ColumnChangeListener implements IChangeListener {

		private final int columnIndex;

		public ColumnChangeListener(final int columnIndex) {
			super();
			this.columnIndex = columnIndex;
		}

		@Override
		public void changedEvent() {
			columnChanged(columnIndex);
		}
	}

}
