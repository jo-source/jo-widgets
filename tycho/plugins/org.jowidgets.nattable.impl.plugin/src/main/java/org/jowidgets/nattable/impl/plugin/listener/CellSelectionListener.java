/*
 * Copyright (c) 2017, MGrossmann
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

package org.jowidgets.nattable.impl.plugin.listener;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.selection.IRowSelectionModel;
import org.eclipse.nebula.widgets.nattable.selection.action.SelectCellAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.jowidgets.spi.impl.swt.common.dnd.SwtDragSource;
import org.jowidgets.util.Assert;

public final class CellSelectionListener extends MouseAdapter {

    private static final int DELAY = 500;

    private final NatTable natTable;
    private final SwtDragSource dragSource;
    private final IRowSelectionModel<Integer> rowSelectionModel;

    private int lastMouseDownTimestamp = -1;
    private int lastRowIndex = -1;

    public CellSelectionListener(
        final NatTable natTable,
        final SwtDragSource dragSource,
        final IRowSelectionModel<Integer> rowSelectionModel) {

        Assert.paramNotNull(rowSelectionModel, "rowSelectionModel");
        Assert.paramNotNull(dragSource, "dragSource");
        Assert.paramNotNull(rowSelectionModel, "rowSelectionModel");

        this.natTable = natTable;
        this.dragSource = dragSource;
        this.rowSelectionModel = rowSelectionModel;
    }

    @Override
    public void mouseDown(final MouseEvent e) {
        handleMouseEvent(e, true);
    }

    @Override
    public void mouseUp(final MouseEvent e) {
        handleMouseEvent(e, false);
    }

    private void handleMouseEvent(final MouseEvent event, final boolean down) {
        final int rowIndex = getRowIndex(event);
        if (rowIndex == -1) {
            return;
        }

        final boolean leftButton = event.button == 1;
        final boolean rightButton = event.button == 3;
        final boolean shift = (event.stateMask & SWT.SHIFT) > 0;
        final boolean ctrl = (event.stateMask & SWT.CTRL) > 0;
        final boolean isSelected = rowSelectionModel.isRowPositionSelected(rowIndex);
        final boolean isDragSourceActive = dragSource.isActive();

        if (!isSelected && down && rightButton && !shift && !ctrl) {
            runSelectionAction(event);
        }
        else if (!isDragSourceActive && down && leftButton) {
            runSelectionAction(event);
        }
        else if (isDragSourceActive && down && leftButton) {
            if (!isSelected) {
                runSelectionAction(event);
            }
            else {
                lastMouseDownTimestamp = event.time;
                lastRowIndex = rowIndex;
            }
        }
        else if (isDragSourceActive && !down && (event.time - lastMouseDownTimestamp) < DELAY && lastRowIndex == rowIndex) {
            runSelectionAction(event);
        }
    }

    private int getRowIndex(final MouseEvent event) {
        final int rowPositionByY = natTable.getRowPositionByY(event.y);
        if (rowPositionByY <= 0) {
            return -1;
        }
        else {
            return natTable.getRowIndexByPosition(rowPositionByY);
        }
    }

    private void runSelectionAction(final MouseEvent event) {
        new SelectCellAction().run(natTable, event);
    }

}
