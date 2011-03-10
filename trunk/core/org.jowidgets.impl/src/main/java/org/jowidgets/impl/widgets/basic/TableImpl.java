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

package org.jowidgets.impl.widgets.basic;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.ITableCell;
import org.jowidgets.api.widgets.ITableColumn;
import org.jowidgets.api.widgets.descriptor.ITableDescriptor;
import org.jowidgets.common.types.TableColumnPackPolicy;
import org.jowidgets.common.widgets.controler.ITableCellEditorListener;
import org.jowidgets.common.widgets.controler.ITableCellListener;
import org.jowidgets.common.widgets.controler.ITableCellPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableColumnListener;
import org.jowidgets.common.widgets.controler.ITableColumnPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableSelectionListener;
import org.jowidgets.impl.base.delegate.ControlDelegate;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.impl.widgets.common.wrapper.ControlSpiWrapper;
import org.jowidgets.spi.widgets.ITableSpi;

public class TableImpl extends ControlSpiWrapper implements ITable {

	private final ControlDelegate controlDelegate;
	private final List<ITableColumn> columns;

	private final int rowCount;

	public TableImpl(final ITableSpi widget, final ITableDescriptor descriptor) {
		super(widget);

		this.controlDelegate = new ControlDelegate();
		this.columns = new LinkedList<ITableColumn>();
		this.rowCount = 0;

		VisibiliySettingsInvoker.setVisibility(descriptor, this);
		ColorSettingsInvoker.setColors(descriptor, this);

	}

	@Override
	public ITableSpi getWidget() {
		return (ITableSpi) super.getWidget();
	}

	@Override
	public IContainer getParent() {
		return controlDelegate.getParent();
	}

	@Override
	public void setParent(final IComponent parent) {
		controlDelegate.setParent(parent);
	}

	@Override
	public boolean isReparentable() {
		return controlDelegate.isReparentable();
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return new PopupMenuImpl(getWidget().createPopupMenu(), this);
	}

	@Override
	public int getRowCount() {
		return rowCount;
	}

	@Override
	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public List<ITableColumn> getColumns() {
		return new LinkedList<ITableColumn>(columns);
	}

	@Override
	public ITableCell getCell(final int rowIndex, final int columnIndex) {
		// TODO MG implement table impl
		return null;
	}

	@Override
	public ITableColumn getColumn(final int columnIndex) {
		// TODO MG implement table impl
		return null;
	}

	@Override
	public ITableColumn insertColumn(final int columnIndex) {
		// TODO MG implement table impl
		return null;
	}

	@Override
	public ITableColumn insertColumns(final int columnIndex, final int columnsCount) {
		// TODO MG implement table impl
		return null;
	}

	@Override
	public void pack(final TableColumnPackPolicy policy) {
		for (final ITableColumn column : getColumns()) {
			column.pack(policy);
		}
	}

	@Override
	public void initialize(final int rowsCount, final int columnsCount) {
		getWidget().initialize(rowsCount, columnsCount);
	}

	@Override
	public List<Integer> getSelection() {
		return getWidget().getSelection();
	}

	@Override
	public void setSelection(final List<Integer> selection) {
		getWidget().setSelection(selection);
	}

	@Override
	public List<Integer> getColumnPermutation() {
		return getWidget().getColumnPermutation();
	}

	@Override
	public void removeColumn(final int columnIndex) {
		getWidget().removeColumn(columnIndex);
	}

	@Override
	public void removeColumns(final int columnIndex, final int columnsCount) {
		getWidget().removeColumns(columnIndex, columnsCount);
	}

	@Override
	public void insertRow(final int index) {
		getWidget().insertRow(index);
	}

	@Override
	public void insertRows(final int index, final int rowsCount) {
		getWidget().insertRows(index, rowsCount);
	}

	@Override
	public void removeRow(final int index) {
		getWidget().removeRow(index);
	}

	@Override
	public void removeRows(final int index, final int rowsCount) {
		getWidget().removeRows(index, rowsCount);
	}

	@Override
	public void addTableItemPopupDetectionListener(final ITableCellPopupDetectionListener listener) {
		getWidget().addTableItemPopupDetectionListener(listener);
	}

	@Override
	public void removeTableItemPopupDetectionListener(final ITableCellPopupDetectionListener listener) {
		getWidget().removeTableItemPopupDetectionListener(listener);
	}

	@Override
	public void addTableItemEditorListener(final ITableCellEditorListener listener) {
		getWidget().addTableItemEditorListener(listener);
	}

	@Override
	public void removeTableItemEditorListener(final ITableCellEditorListener listener) {
		getWidget().removeTableItemEditorListener(listener);
	}

	@Override
	public void addTableItemListener(final ITableCellListener listener) {
		getWidget().addTableItemListener(listener);
	}

	@Override
	public void removeTableItemListener(final ITableCellListener listener) {
		getWidget().removeTableItemListener(listener);
	}

	@Override
	public void addTableColumnPopupDetectionListener(final ITableColumnPopupDetectionListener listener) {
		getWidget().addTableColumnPopupDetectionListener(listener);
	}

	@Override
	public void removeTableColumnPopupDetectionListener(final ITableColumnPopupDetectionListener listener) {
		getWidget().removeTableColumnPopupDetectionListener(listener);
	}

	@Override
	public void addTableSelectionListener(final ITableSelectionListener listener) {
		getWidget().addTableSelectionListener(listener);
	}

	@Override
	public void removeTableSelectionListener(final ITableSelectionListener listener) {
		getWidget().removeTableSelectionListener(listener);
	}

	@Override
	public void addTableColumnListener(final ITableColumnListener listener) {
		getWidget().addTableColumnListener(listener);
	}

	@Override
	public void removeTableColumnListener(final ITableColumnListener listener) {
		getWidget().removeTableColumnListener(listener);
	}

}
