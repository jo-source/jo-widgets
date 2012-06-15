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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableDataModel;

public class VirtualList implements ObservableList<ITableCell> {

	private final ITableDataModel dataModel;

	public VirtualList(final ITableDataModel dataModel) {
		this.dataModel = dataModel;
	}

	@Override
	public int size() {
		return dataModel.getRowCount();
	}

	@Override
	public boolean isEmpty() {
		if (dataModel.getRowCount() == 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean contains(final Object o) {
		// TODO DB Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<ITableCell> iterator() {
		// TODO DB Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		// TODO DB Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		// TODO DB Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(final ITableCell e) {
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
	public boolean addAll(final Collection<? extends ITableCell> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends ITableCell> c) {
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
	public ITableCell get(final int index) {
		// TODO DB Auto-generated method stub
		//		final Data data = new Data(dataModel.getCell(index, columnIndex));
		//		if (columnIndex < columnModel.getColumnCount() - 1) {
		//			columnIndex++;
		//		}
		//		else {
		//			columnIndex = 0;
		//		}
		//		return data;
		return dataModel.getCell(0, 0);
	}

	@Override
	public ITableCell set(final int index, final ITableCell element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(final int index, final ITableCell element) {
		throw new UnsupportedOperationException();

	}

	@Override
	public ITableCell remove(final int index) {
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
	public ListIterator<ITableCell> listIterator() {
		// TODO DB Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<ITableCell> listIterator(final int index) {
		// TODO DB Auto-generated method stub
		return null;
	}

	@Override
	public List<ITableCell> subList(final int fromIndex, final int toIndex) {
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
	public void addListener(final ListChangeListener<? super ITableCell> paramListChangeListener) {
		// TODO DB Auto-generated method stub

	}

	@Override
	public void removeListener(final ListChangeListener<? super ITableCell> paramListChangeListener) {
		// TODO DB Auto-generated method stub

	}

	@Override
	public boolean addAll(final ITableCell... paramArrayOfE) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setAll(final ITableCell... paramArrayOfE) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setAll(final Collection<? extends ITableCell> paramCollection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(final ITableCell... paramArrayOfE) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(final ITableCell... paramArrayOfE) {
		// TODO DB Auto-generated method stub
		return false;
	}

	@Override
	public void remove(final int paramInt1, final int paramInt2) {
		throw new UnsupportedOperationException();
	}

}
