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

import java.util.ArrayList;
import java.util.List;

import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.descriptor.ITableDescriptor;
import org.jowidgets.common.model.ITableColumnModel;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.TablePackPolicy;
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

	private static final TablePackPolicy DEFAULT_PACK_POLICY = TablePackPolicy.HEADER_AND_DATA_VISIBLE;

	private final ControlDelegate controlDelegate;
	private final ITableDataModel dataModel;
	private final ITableColumnModel columnModel;

	public TableImpl(final ITableSpi widget, final ITableDescriptor setup) {
		super(widget);

		this.controlDelegate = new ControlDelegate();
		this.dataModel = setup.getDataModel();
		this.columnModel = setup.getColumnModel();

		VisibiliySettingsInvoker.setVisibility(setup, this);
		ColorSettingsInvoker.setColors(setup, this);

		resetFromModel();
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
		return dataModel.getRowCount();
	}

	@Override
	public int getColumnCount() {
		return columnModel.getColumnCount();
	}

	@Override
	public void resetFromModel() {
		getWidget().resetFromModel();
	}

	@Override
	public int convertColumnIndexToView(final int modelIndex) {
		final ArrayList<Integer> permutation = getColumnPermutation();
		return permutation.indexOf(Integer.valueOf(modelIndex));
	}

	@Override
	public int convertColumnIndexToModel(final int viewIndex) {
		return getColumnPermutation().get(viewIndex).intValue();
	}

	@Override
	public void moveColumn(final int oldViewIndex, final int newViewIndex) {
		final ArrayList<Integer> permutation = getColumnPermutation();
		final int oldPermutationValue = permutation.get(oldViewIndex).intValue();
		permutation.remove(oldViewIndex);
		permutation.add(newViewIndex, oldPermutationValue);
		setColumnPermutation(permutation);
	}

	@Override
	public void pack() {
		pack(DEFAULT_PACK_POLICY);
	}

	@Override
	public void pack(final int columnIndex) {
		pack(columnIndex, DEFAULT_PACK_POLICY);
	}

	@Override
	public void pack(final TablePackPolicy policy) {
		getWidget().pack(policy);
	}

	@Override
	public void pack(final int columnIndex, final TablePackPolicy policy) {
		getWidget().pack(columnIndex, policy);
	}

	@Override
	public Position getCellPosition(final int rowIndex, final int columnIndex) {
		return getWidget().getCellPosition(rowIndex, columnIndex);
	}

	@Override
	public Dimension getCellSize(final int rowIndex, final int columnIndex) {
		return getWidget().getCellSize(rowIndex, columnIndex);
	}

	@Override
	public ArrayList<Integer> getColumnPermutation() {
		return getWidget().getColumnPermutation();
	}

	@Override
	public void setColumnPermutation(final List<Integer> permutation) {
		getWidget().setColumnPermutation(permutation);
	}

	@Override
	public ArrayList<Integer> getSelection() {
		return getWidget().getSelection();
	}

	@Override
	public void setSelection(final List<Integer> selection) {
		getWidget().setSelection(selection);
	}

	@Override
	public void addTableCellPopupDetectionListener(final ITableCellPopupDetectionListener listener) {
		getWidget().addTableCellPopupDetectionListener(listener);
	}

	@Override
	public void removeTableCellPopupDetectionListener(final ITableCellPopupDetectionListener listener) {
		getWidget().removeTableCellPopupDetectionListener(listener);
	}

	@Override
	public void addTableCellEditorListener(final ITableCellEditorListener listener) {
		getWidget().addTableCellEditorListener(listener);
	}

	@Override
	public void removeTableCellEditorListener(final ITableCellEditorListener listener) {
		getWidget().removeTableCellEditorListener(listener);
	}

	@Override
	public void addTableCellListener(final ITableCellListener listener) {
		getWidget().addTableCellListener(listener);
	}

	@Override
	public void removeTableCellListener(final ITableCellListener listener) {
		getWidget().removeTableCellListener(listener);
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
