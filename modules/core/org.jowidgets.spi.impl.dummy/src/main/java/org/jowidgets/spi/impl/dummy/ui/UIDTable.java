/*
 * Copyright (c) 2017, grossmann
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

package org.jowidgets.spi.impl.dummy.ui;

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
import org.jowidgets.util.Interval;

/**
 * Table implementation for test
 * 
 * TODO implement necessary methods
 */
public class UIDTable extends UIDContainer {

    public void setEditable(final boolean editable) {}

    public boolean editCell(final int row, final int column) {
        return false;
    }

    public void stopEditing() {}

    public void cancelEditing() {}

    public boolean isEditing() {
        return false;
    }

    public void resetFromModel() {}

    public void setRowHeight(final int height) {}

    public Position getCellPosition(final int rowIndex, final int columnIndex) {
        return new Position(0, 0);
    }

    public int getColumnAtPosition(final Position position) {
        return -1;
    }

    public int getRowAtPosition(final Position position) {
        return -1;
    }

    public Dimension getCellSize(final int rowIndex, final int columnIndex) {
        return new Dimension(0, 0);
    }

    public ArrayList<Integer> getColumnPermutation() {
        return new ArrayList<Integer>();
    }

    public void setColumnPermutation(final List<Integer> permutation) {}

    public ArrayList<Integer> getSelection() {
        return new ArrayList<Integer>();
    }

    public void setSelection(final List<Integer> selection) {}

    public void scrollToRow(final int rowIndex) {}

    public void pack(final TablePackPolicy policy) {}

    public void pack(final int columnIndex, final TablePackPolicy policy) {}

    public boolean isColumnPopupDetectionSupported() {
        return true;
    }

    public Interval<Integer> getVisibleRows() {
        return new Interval<Integer>(0, 0);
    }

    public void addTableSelectionListener(final ITableSelectionListener listener) {}

    public void removeTableSelectionListener(final ITableSelectionListener listener) {}

    public void addTableCellListener(final ITableCellListener listener) {}

    public void removeTableCellListener(final ITableCellListener listener) {}

    public void addTableCellPopupDetectionListener(final ITableCellPopupDetectionListener listener) {}

    public void removeTableCellPopupDetectionListener(final ITableCellPopupDetectionListener listener) {}

    public void addTableColumnListener(final ITableColumnListener listener) {}

    public void removeTableColumnListener(final ITableColumnListener listener) {}

    public void addTableColumnPopupDetectionListener(final ITableColumnPopupDetectionListener listener) {}

    public void removeTableColumnPopupDetectionListener(final ITableColumnPopupDetectionListener listener) {}

}
