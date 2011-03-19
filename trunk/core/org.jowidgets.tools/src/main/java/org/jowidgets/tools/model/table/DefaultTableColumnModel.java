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

import org.jowidgets.api.model.table.IDefaultTableColumn;
import org.jowidgets.api.model.table.IDefaultTableColumnBuilder;
import org.jowidgets.api.model.table.IDefaultTableColumnModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.types.AlignmentHorizontal;

public class DefaultTableColumnModel implements IDefaultTableColumnModel {

	private final IDefaultTableColumnModel model;

	public DefaultTableColumnModel() {
		this(0);
	}

	public DefaultTableColumnModel(final int columnCount) {
		this.model = Toolkit.getModelFactoryProvider().getTableModelFactory().columnModel(columnCount);
	}

	@Override
	public int getColumnCount() {
		return model.getColumnCount();
	}

	@Override
	public ITableColumnModelObservable getTableColumnModelObservable() {
		return model.getTableColumnModelObservable();
	}

	@Override
	public IDefaultTableColumn getColumn(final int columnIndex) {
		return model.getColumn(columnIndex);
	}

	@Override
	public ArrayList<IDefaultTableColumn> getColumns() {
		return model.getColumns();
	}

	@Override
	public IDefaultTableColumn addColumn() {
		return model.addColumn();
	}

	@Override
	public IDefaultTableColumn addColumn(final int columnIndex) {
		return model.addColumn(columnIndex);
	}

	@Override
	public void addColumn(final IDefaultTableColumn column) {
		model.addColumn(column);
	}

	@Override
	public void addColumn(final int columnIndex, final IDefaultTableColumn column) {
		model.addColumn(columnIndex, column);
	}

	@Override
	public IDefaultTableColumn addColumn(final IDefaultTableColumnBuilder columnBuilder) {
		return model.addColumn(columnBuilder);
	}

	@Override
	public IDefaultTableColumn addColumn(final int columnIndex, final IDefaultTableColumnBuilder columnBuilder) {
		return model.addColumn(columnIndex, columnBuilder);
	}

	@Override
	public IDefaultTableColumn addColumn(final String text) {
		return model.addColumn(text);
	}

	@Override
	public IDefaultTableColumn addColumn(final String text, final String toolTipText) {
		return model.addColumn(text, toolTipText);
	}

	@Override
	public void removeColumn(final int columnIndex) {
		model.removeColumn(columnIndex);
	}

	@Override
	public void removeColumns(final int fromColumnIndex, final int toColumnIndex) {
		model.removeColumns(fromColumnIndex, toColumnIndex);
	}

	@Override
	public void removeColumns(final int... columns) {
		model.removeColumns(columns);
	}

	@Override
	public void removeAllColumns() {
		model.removeAllColumns();
	}

	@Override
	public void modifyModelStart() {
		model.modifyModelStart();
	}

	@Override
	public void modifyModelEnd() {
		model.modifyModelEnd();
	}

	@Override
	public void setColumn(final int columnIndex, final IDefaultTableColumn column) {
		model.setColumn(columnIndex, column);
	}

	@Override
	public IDefaultTableColumn setColumn(final int columnIndex, final IDefaultTableColumnBuilder columnBuilder) {
		return model.setColumn(columnIndex, columnBuilder);
	}

	@Override
	public void setColumnText(final int columnIndex, final String text) {
		model.setColumnText(columnIndex, text);
	}

	@Override
	public void setColumnToolTipText(final int columnIndex, final String tooltipText) {
		model.setColumnToolTipText(columnIndex, tooltipText);
	}

	@Override
	public void setColumnIcon(final int columnIndex, final IImageConstant icon) {
		model.setColumnIcon(columnIndex, icon);
	}

	@Override
	public void setColumnAlignment(final int columnIndex, final AlignmentHorizontal alignment) {
		model.setColumnAlignment(columnIndex, alignment);
	}
}
