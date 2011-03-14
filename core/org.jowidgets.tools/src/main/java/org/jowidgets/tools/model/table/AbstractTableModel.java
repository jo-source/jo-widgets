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

import org.jowidgets.common.model.ITableModel;
import org.jowidgets.common.model.ITableModelListener;
import org.jowidgets.common.model.ITableModelObservable;
import org.jowidgets.tools.controler.TableModelObservable;

public abstract class AbstractTableModel implements ITableModel, ITableModelObservable {

	private final TableModelObservable tableModelObservable;

	private ArrayList<Integer> selection;

	public AbstractTableModel() {
		this.tableModelObservable = new TableModelObservable();
		this.selection = new ArrayList<Integer>();
	}

	@Override
	public final ArrayList<Integer> getSelection() {
		return selection;
	}

	@Override
	public final void setSelection(ArrayList<Integer> selection) {
		if (selection == null) {
			selection = new ArrayList<Integer>();
		}
		if (!this.selection.equals(selection)) {
			this.selection = selection;
			fireSelectionChanged();
		}
	}

	@Override
	public final ITableModelObservable getTableModelObservable() {
		return this;
	}

	@Override
	public final void addTableModelListener(final ITableModelListener listener) {
		tableModelObservable.addTableModelListener(listener);
	}

	@Override
	public final void removeTableModelListener(final ITableModelListener listener) {
		tableModelObservable.removeTableModelListener(listener);
	}

	protected final void fireRowsAdded(final int[] rowIndices) {
		tableModelObservable.fireRowsAdded(rowIndices);
	}

	protected final void fireRowsRemoved(final int[] rowIndices) {
		tableModelObservable.fireRowsRemoved(rowIndices);
	}

	protected final void fireRowsChanged(final int[] rowIndices) {
		tableModelObservable.fireRowsChanged(rowIndices);
	}

	protected final void fireRowsStructureChanged() {
		tableModelObservable.fireRowsStructureChanged();
	}

	private void fireSelectionChanged() {
		tableModelObservable.fireSelectionChanged();
	}

}
