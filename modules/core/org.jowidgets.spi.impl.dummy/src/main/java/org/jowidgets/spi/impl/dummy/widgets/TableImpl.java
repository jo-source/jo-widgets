/*
 * Copyright (c) 2017, herrg
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

package org.jowidgets.spi.impl.dummy.widgets;

import java.util.ArrayList;
import java.util.List;

import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.TablePackPolicy;
import org.jowidgets.common.widgets.controller.ITableCellListener;
import org.jowidgets.common.widgets.controller.ITableCellPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableColumnListener;
import org.jowidgets.common.widgets.controller.ITableColumnPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableSelectionListener;
import org.jowidgets.spi.impl.dummy.ui.UIDTable;
import org.jowidgets.spi.widgets.ITableSpi;
import org.jowidgets.spi.widgets.setup.ITableSetupSpi;
import org.jowidgets.util.Interval;

public final class TableImpl extends DummyControl implements ITableSpi {

    private final UIDTable original;

    public TableImpl(final ITableSetupSpi setup) {
        super(new UIDTable());
        this.original = getUiReference();
    }

    @Override
    public UIDTable getUiReference() {
        return (UIDTable) super.getUiReference();
    }

    @Override
    public void setEditable(final boolean editable) {
        original.setEditable(editable);
    }

    @Override
    public boolean editCell(final int row, final int column) {
        return original.editCell(row, column);
    }

    @Override
    public void stopEditing() {
        original.stopEditing();
    }

    @Override
    public void cancelEditing() {
        original.cancelEditing();
    }

    @Override
    public boolean isEditing() {
        return original.isEditing();
    }

    @Override
    public void resetFromModel() {
        original.resetFromModel();
    }

    @Override
    public void setRowHeight(final int height) {
        original.setRowHeight(height);
    }

    @Override
    public Position getCellPosition(final int rowIndex, final int columnIndex) {
        return original.getCellPosition(rowIndex, columnIndex);
    }

    @Override
    public int getColumnAtPosition(final Position position) {
        return original.getColumnAtPosition(position);
    }

    @Override
    public int getRowAtPosition(final Position position) {
        return original.getRowAtPosition(position);
    }

    @Override
    public Dimension getCellSize(final int rowIndex, final int columnIndex) {
        return original.getCellSize(rowIndex, columnIndex);
    }

    @Override
    public ArrayList<Integer> getColumnPermutation() {
        return original.getColumnPermutation();
    }

    @Override
    public void setColumnPermutation(final List<Integer> permutation) {
        original.setColumnPermutation(permutation);
    }

    @Override
    public ArrayList<Integer> getSelection() {
        return original.getSelection();
    }

    @Override
    public void setSelection(final List<Integer> selection) {
        original.setSelection(selection);
    }

    @Override
    public void scrollToRow(final int rowIndex) {
        original.scrollToRow(rowIndex);
    }

    @Override
    public void pack(final TablePackPolicy policy) {
        original.pack(policy);
    }

    @Override
    public void pack(final int columnIndex, final TablePackPolicy policy) {
        original.pack(columnIndex, policy);
    }

    @Override
    public boolean isColumnPopupDetectionSupported() {
        return original.isColumnPopupDetectionSupported();
    }

    @Override
    public Interval<Integer> getVisibleRows() {
        return original.getVisibleRows();
    }

    @Override
    public void addTableSelectionListener(final ITableSelectionListener listener) {
        original.addTableSelectionListener(listener);
    }

    @Override
    public void removeTableSelectionListener(final ITableSelectionListener listener) {
        original.removeTableSelectionListener(listener);
    }

    @Override
    public void addTableCellListener(final ITableCellListener listener) {
        original.addTableCellListener(listener);
    }

    @Override
    public void removeTableCellListener(final ITableCellListener listener) {
        original.removeTableCellListener(listener);
    }

    @Override
    public void addTableCellPopupDetectionListener(final ITableCellPopupDetectionListener listener) {
        original.addTableCellPopupDetectionListener(listener);
    }

    @Override
    public void removeTableCellPopupDetectionListener(final ITableCellPopupDetectionListener listener) {
        original.removeTableCellPopupDetectionListener(listener);
    }

    @Override
    public void addTableColumnListener(final ITableColumnListener listener) {
        original.addTableColumnListener(listener);
    }

    @Override
    public void removeTableColumnListener(final ITableColumnListener listener) {
        original.removeTableColumnListener(listener);
    }

    @Override
    public void addTableColumnPopupDetectionListener(final ITableColumnPopupDetectionListener listener) {
        original.addTableColumnPopupDetectionListener(listener);
    }

    @Override
    public void removeTableColumnPopupDetectionListener(final ITableColumnPopupDetectionListener listener) {
        original.removeTableColumnPopupDetectionListener(listener);
    }

}
