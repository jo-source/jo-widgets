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

package org.jowidgets.api.model.table;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableModel;

public interface ISimpleTableModel extends ITableModel, IDefaultTableColumnModel {

	void addRow();

	void addRow(int rowIndex);

	void addRows(int rowIndex, int rowCount);

	void addRow(ITableCell... cells);

	void addRow(int rowIndex, ITableCell... cells);

	void addRow(ITableCellBuilder... cellBuilders);

	void addRow(int rowIndex, ITableCellBuilder... cellBuilders);

	void addRow(String... cellTexts);

	void addRow(int rowIndex, String... cellTexts);

	void removeRow(int index);

	void removeRows(int fromIndex, int toIndex);

	void removeAllRows();

	void setCell(int rowIndex, int columnIndex, ITableCell cell);

	void setCell(int rowIndex, int columnIndex, ITableCellBuilder cellBuilder);

	void setCell(int rowIndex, int columnIndex, String text);

	void setCell(int rowIndex, int columnIndex, String text, IImageConstant icon);

	void setCell(int rowIndex, int columnIndex, String text, boolean editable);

	void setEditable(int rowIndex, int columnIndex, boolean editable);

}
