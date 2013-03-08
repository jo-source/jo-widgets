/*
 * Copyright (c) 2013, grossmann
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

package org.jowidgets.examples.common.workbench.demo3.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jowidgets.api.model.table.ITableColumn;
import org.jowidgets.api.model.table.ITableModel;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.tools.model.table.AbstractTableDataModel;
import org.jowidgets.util.Assert;
import org.jowidgets.util.EmptyCheck;

public final class BeanTableModel<BEAN_TYPE> extends AbstractTableDataModel implements ITableModel {

	private final List<BEAN_TYPE> data;
	private final IBeanTableRenderer<BEAN_TYPE> renderer;

	public BeanTableModel(final IBeanTableRenderer<BEAN_TYPE> beanAccessor) {
		Assert.paramNotNull(beanAccessor, "beanAccessor");
		this.renderer = beanAccessor;
		this.data = new ArrayList<BEAN_TYPE>();
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public ITableCell getCell(final int rowIndex, final int columnIndex) {
		return renderer.getCell(rowIndex, columnIndex, data.get(rowIndex));
	}

	public void addBean(final BEAN_TYPE bean, final boolean select) {
		Assert.paramNotNull(bean, "bean");
		data.add(bean);
		fireDataChanged();
		if (select) {
			final List<Integer> selection = new LinkedList<Integer>();
			selection.add(Integer.valueOf(data.size() - 1));
			setSelection(selection);
		}
	}

	public void removeBean(final BEAN_TYPE bean) {
		Assert.paramNotNull(bean, "bean");
		data.remove(bean);
		final Set<Integer> emptySet = Collections.emptySet();
		setSelection(emptySet);
		fireDataChanged();
	}

	@Override
	public int getColumnCount() {
		return renderer.getColumnCount();
	}

	@Override
	public ITableColumn getColumn(final int columnIndex) {
		return renderer.getColumn(columnIndex);
	}

	@Override
	public ITableColumnModelObservable getTableColumnModelObservable() {
		return renderer.getTableColumnModelObservable();
	}

	public BEAN_TYPE getSelectedBean() {
		final ArrayList<Integer> selection = getSelection();
		if (!EmptyCheck.isEmpty(selection)) {
			final Integer index = selection.get(0);
			return data.get(index.intValue());
		}
		else {
			return null;
		}
	}

}
