/*
 * Copyright (c) 2016, MGrossmann
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

package org.jowidgets.nattable.impl.plugin;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.IOverlayPainter;
import org.eclipse.nebula.widgets.nattable.reorder.action.ColumnReorderDragMode;
import org.eclipse.nebula.widgets.nattable.ui.util.CellEdgeEnum;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.jowidgets.nattable.impl.plugin.layer.Constants;
import org.jowidgets.nattable.impl.plugin.movetojo.ColorProvider;
import org.jowidgets.util.Assert;

final class JoColumnReorderDragMode extends ColumnReorderDragMode {

    private static final ColorProvider OVERLAY_COLOR = new ColorProvider(0, 102, 204);

    private final HoveredColumnConfigLabelAccumulator hoveredColumnLabelAccumulator;
    private final ColumnReorderOverlayPainter overlayPainter;

    JoColumnReorderDragMode(final HoveredColumnConfigLabelAccumulator hoveredColumnLabelAccumulator) {
        Assert.paramNotNull(hoveredColumnLabelAccumulator, "hoveredColumnLabelAccumulator");
        this.hoveredColumnLabelAccumulator = hoveredColumnLabelAccumulator;
        this.overlayPainter = new ColumnReorderOverlayPainter();
    }

    @Override
    public void mouseDown(final NatTable natTable, final MouseEvent event) {
        this.natTable = natTable;
        this.initialEvent = event;
        this.currentEvent = this.initialEvent;
        this.dragFromGridColumnPosition = getDragFromGridColumnPosition();

        natTable.addOverlayPainter(this.overlayPainter);
        fireMoveStartCommand(natTable, this.dragFromGridColumnPosition);
    }

    @Override
    public void mouseUp(final NatTable natTable, final MouseEvent event) {
        final int col = natTable.getColumnPositionByX(event.x);
        final int row = natTable.getRowPositionByY(event.y);
        if (row == 0 && col >= 0) {
            hoveredColumnLabelAccumulator.setColumnIndex(col);
        }
        else {
            hoveredColumnLabelAccumulator.clearColumnIndex();
        }
        natTable.removeOverlayPainter(overlayPainter);
        super.mouseUp(natTable, event);
    }

    private ILayerCell getColumnCell(final int x) {
        final int gridColumnPosition = this.natTable.getColumnPositionByX(x);
        final int gridRowPosition = this.natTable.getRowPositionByY(this.initialEvent.y);
        return this.natTable.getCellByPosition(gridColumnPosition, gridRowPosition);
    }

    private class ColumnReorderOverlayPainter implements IOverlayPainter {

        @Override
        public void paintOverlay(final GC gc, final ILayer layer) {

            final int dragFromGridColumnPos = getDragFromGridColumnPosition();

            if (JoColumnReorderDragMode.this.currentEvent.x > JoColumnReorderDragMode.this.natTable.getWidth()) {
                return;
            }

            final CellEdgeEnum moveDirection = getMoveDirection(JoColumnReorderDragMode.this.currentEvent.x);
            final int dragToGridColumnPosition = getDragToGridColumnPosition(
                    moveDirection,
                    JoColumnReorderDragMode.this.natTable.getColumnPositionByX(JoColumnReorderDragMode.this.currentEvent.x));

            if (isValidTargetColumnPosition(
                    JoColumnReorderDragMode.this.natTable,
                    dragFromGridColumnPos,
                    dragToGridColumnPosition)) {
                int dragToColumnHandleX = -1;

                if (moveDirection != null) {
                    final Rectangle selectedColumnHeaderRect = getColumnCell(JoColumnReorderDragMode.this.currentEvent.x)
                            .getBounds();

                    switch (moveDirection) {
                        case LEFT:
                            dragToColumnHandleX = selectedColumnHeaderRect.x;
                            break;
                        case RIGHT:
                            dragToColumnHandleX = selectedColumnHeaderRect.x + selectedColumnHeaderRect.width;
                            break;
                        default:
                            break;
                    }
                }

                if (dragToColumnHandleX > 0) {
                    final Color orgBgColor = gc.getBackground();
                    gc.setBackground(OVERLAY_COLOR.get());
                    gc.fillRectangle(dragToColumnHandleX - 1, 0, 2, Constants.COLUMN_HEADER_HEIGHT - 1);
                    gc.setBackground(orgBgColor);
                }
            }
        }

    }

}
