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

import java.util.ArrayList;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.model.ITableColumnModel;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.types.AlignmentHorizontal;

public interface IDefaultTableColumnModel extends ITableColumnModel, ITableColumnModelObservable {

	@Override
	IDefaultTableColumn getColumn(int columnIndex);

	ArrayList<IDefaultTableColumn> getColumns();

	IDefaultTableColumn addColumn();

	IDefaultTableColumn addColumn(final int columnIndex);

	void addColumn(IDefaultTableColumn column);

	void addColumn(final int columnIndex, IDefaultTableColumn column);

	IDefaultTableColumn addColumn(IDefaultTableColumnBuilder columnBuilder);

	IDefaultTableColumn addColumn(final int columnIndex, IDefaultTableColumnBuilder columnBuilder);

	IDefaultTableColumn addColumn(String text);

	IDefaultTableColumn addColumn(String text, String toolTipText);

	void removeColumn(final int columnIndex);

	void removeColumns(final int fromColumnIndex, final int toColumnIndex);

	void removeColumns(final int... columns);

	void removeAllColumns();

	void modifyModelStart();

	void modifyModelEnd();

	void setFireEvents(boolean fireEvents);

	void setColumn(final int columnIndex, final IDefaultTableColumn column);

	IDefaultTableColumn setColumn(final int columnIndex, final IDefaultTableColumnBuilder columnBuilder);

	void setColumnText(final int columnIndex, String text);

	void setColumnToolTipText(final int columnIndex, String tooltipText);

	void setColumnIcon(final int columnIndex, IImageConstant icon);

	void setColumnAlignment(final int columnIndex, AlignmentHorizontal alignment);

}
