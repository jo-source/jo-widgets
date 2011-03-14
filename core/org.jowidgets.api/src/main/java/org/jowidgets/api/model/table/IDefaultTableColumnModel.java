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
import org.jowidgets.common.model.ITableColumn;
import org.jowidgets.common.model.ITableColumnModel;
import org.jowidgets.common.types.AlignmentHorizontal;

public interface IDefaultTableColumnModel extends ITableColumnModel {

	@Override
	IDefaultTableColumn getColumn(int columnIndex);

	void addColumn(ITableColumn column);

	void addColumn(final int columnIndex, ITableColumn column);

	ITableColumn addColumn(ITableColumnBuilder columnBuilder);

	ITableColumn addColumn(final int columnIndex, ITableColumnBuilder columnBuilder);

	void addColumn(String text);

	void addColumn(String text, String toolTipText);

	void removeColumn(final int columnIndex);

	void removeColumns(final int fromColumnIndex, final int toColumnIndex);

	void removeColumns(final int[] columns);

	void removeAllColumns();

	void modifyModelStart();

	void modifyModelEnd();

	void setColumn(final int columnIndex, final ITableColumn column);

	ITableColumn setColumn(final int columnIndex, final ITableColumnBuilder columnBuilder);

	void setText(final int columnIndex, String text);

	void setToolTipText(final int columnIndex, String tooltipText);

	void setIcon(final int columnIndex, IImageConstant icon);

	void setAlignment(final int columnIndex, AlignmentHorizontal alignment);

}
