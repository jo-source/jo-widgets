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

package org.jowidgets.common.widgets;

import java.util.ArrayList;
import java.util.List;

import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Interval;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.TablePackPolicy;
import org.jowidgets.common.widgets.controller.ITableCellEditorObservable;
import org.jowidgets.common.widgets.controller.ITableCellObservable;
import org.jowidgets.common.widgets.controller.ITableCellPopupDetectionObservable;
import org.jowidgets.common.widgets.controller.ITableColumnObservable;
import org.jowidgets.common.widgets.controller.ITableColumnPopupDetectionObservable;
import org.jowidgets.common.widgets.controller.ITableSelectionObservable;

public interface ITableCommon extends
		ITableSelectionObservable,
		ITableCellObservable,
		ITableCellPopupDetectionObservable,
		ITableCellEditorObservable,
		ITableColumnObservable,
		ITableColumnPopupDetectionObservable,
		IControlCommon {

	/**
	 * Sets the tables global editable property. The global editable state and the cells editable state will
	 * be conjuncted with a logical AND
	 * 
	 * @param editable The editable state to set
	 */
	void setEditable(boolean editable);

	void resetFromModel();

	Position getCellPosition(int rowIndex, int columnIndex);

	Dimension getCellSize(int rowIndex, int columnIndex);

	ArrayList<Integer> getColumnPermutation();

	void setColumnPermutation(List<Integer> permutation);

	ArrayList<Integer> getSelection();

	void setSelection(List<Integer> selection);

	void showSelection();

	void pack(TablePackPolicy policy);

	void pack(int columnIndex, TablePackPolicy policy);

	boolean isColumnPopupDetectionSupported();

	/**
	 * Gets the interval of the visible rows (visible in viewport).
	 * 
	 * Remark: If the table is empty or now row is visible, the left and the right boundary is null.
	 * 
	 * @return The interval of the visible rows
	 */
	Interval<Integer> getVisibleRows();

}
