/*
 * Copyright (c) 2012, dabaukne
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

package org.jowidgets.spi.impl.javafx.widgets.base;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import org.jowidgets.common.model.ITableColumnModelSpi;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.model.ITableDataModelListener;
import org.jowidgets.common.model.ITableDataModelObservable;

public class VirtualList implements ObservableList<JoTableRow> {

	private final Map<Integer, JoTableRow> rows;

	private final ITableDataModel dataModel;
	private final ITableColumnModelSpi columnModel;

	private final TableView table;

	public VirtualList(final ITableDataModel dataModel, final ITableColumnModelSpi columnModel, final TableView table) {
		this.dataModel = dataModel;
		this.columnModel = columnModel;
		this.table = table;
		this.rows = new HashMap<Integer, JoTableRow>();

		final ITableDataModelObservable dataModelObservable = dataModel.getTableDataModelObservable();
		if (dataModelObservable != null) {
			final ITableDataModelListener tableModelListener = new ITableDataModelListener() {

				@Override
				public void selectionChanged() {
					// TODO DB implement selection change
				}

				@Override
				public void rowsRemoved(final int[] rowIndices) {
					//TODO DB implement better
					dataChanged();
				}

				@Override
				public void rowsChanged(final int[] rowIndices) {
					//TODO DB implement better
					dataChanged();
				}

				@Override
				public void rowsAdded(final int[] rowIndices) {
					//TODO DB implement better
					dataChanged();
				}

				@Override
				public void dataChanged() {
					rows.clear();
					//fire invalidation and change events
					setList();
				}
			};
			dataModelObservable.addDataModelListener(tableModelListener);
		}
	}

	protected void setList() {
		table.getItems().clear();
		table.setItems(this);
	}

	@Override
	public int size() {
		return dataModel.getRowCount();
	}

	@Override
	public boolean isEmpty() {
		return dataModel.getRowCount() == 0;
	}

	@Override
	public boolean contains(final Object o) {
		// TODO DB Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<JoTableRow> iterator() {
		return new VLIterator(this);
	}

	@Override
	public Object[] toArray() {
		final Object[] array = new Object[dataModel.getRowCount()];
		for (int j = 0; j < array.length; j++) {
			array[j] = dataModel.getCell(j, 0);
		}
		return array;
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		// TODO DB Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(final JoTableRow e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(final Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		// TODO DB Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(final Collection<? extends JoTableRow> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends JoTableRow> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		// TODO DB Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public JoTableRow get(final int rowIndex) {
		final Integer rowIndexWrapper = Integer.valueOf(rowIndex);
		JoTableRow result = rows.get(rowIndexWrapper);
		if (result == null) {
			result = new JoTableRow(dataModel, rowIndex);
			rows.put(rowIndexWrapper, result);
		}
		return result;
	}

	@Override
	public JoTableRow set(final int index, final JoTableRow element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(final int index, final JoTableRow element) {
		throw new UnsupportedOperationException();

	}

	@Override
	public JoTableRow remove(final int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int indexOf(final Object o) {
		// TODO DB Auto-generated method stub
		return 0;
	}

	@Override
	public int lastIndexOf(final Object o) {
		// TODO DB Auto-generated method stub
		return 0;
	}

	@Override
	public ListIterator<JoTableRow> listIterator() {
		// TODO DB Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<JoTableRow> listIterator(final int index) {
		// TODO DB Auto-generated method stub
		return null;
	}

	@Override
	public List<JoTableRow> subList(final int fromIndex, final int toIndex) {
		// TODO DB Auto-generated method stub
		return null;
	}

	@Override
	public void addListener(final InvalidationListener paramInvalidationListener) {
		// TODO DB Auto-generated method stub

	}

	@Override
	public void removeListener(final InvalidationListener paramInvalidationListener) {
		// TODO DB Auto-generated method stub

	}

	@Override
	public void addListener(final ListChangeListener<? super JoTableRow> paramListChangeListener) {
		// TODO DB Auto-generated method stub

	}

	@Override
	public void removeListener(final ListChangeListener<? super JoTableRow> paramListChangeListener) {
		// TODO DB Auto-generated method stub

	}

	@Override
	public boolean addAll(final JoTableRow... paramArrayOfE) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setAll(final JoTableRow... paramArrayOfE) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setAll(final Collection<? extends JoTableRow> paramCollection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(final JoTableRow... paramArrayOfE) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(final JoTableRow... paramArrayOfE) {
		// TODO DB Auto-generated method stub
		return false;
	}

	@Override
	public void remove(final int paramInt1, final int paramInt2) {
		throw new UnsupportedOperationException();
	}

	private class VLIterator implements Iterator<JoTableRow> {
		private final VirtualList list;
		int index = 0;

		public VLIterator(final VirtualList list) {
			this.list = list;

		}

		@Override
		public boolean hasNext() {
			return list.get(index) != null;
		}

		@Override
		public JoTableRow next() {
			final JoTableRow joTableRow = list.get(index);
			index++;
			return joTableRow;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();

		}

	}

}
