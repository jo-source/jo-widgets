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
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.IOverlayPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.ui.action.IDragMode;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Copied from CellDragMode and modified to draw the overlay in win7 style not movable in y direction
 */
final class JoColumnReorderCellDragMode implements IDragMode {

    private final CellImageOverlayPainter cellImageOverlayPainter = new CellImageOverlayPainter();

    private MouseEvent initialEvent;
    private MouseEvent currentEvent;

    private int xOffset;
    private Image cellImage;

    @Override
    public void mouseDown(final NatTable natTable, final MouseEvent event) {
        this.initialEvent = event;
        this.currentEvent = this.initialEvent;

        setCellImage(natTable);

        natTable.forceFocus();

        natTable.addOverlayPainter(this.cellImageOverlayPainter);
    }

    @Override
    public void mouseMove(final NatTable natTable, final MouseEvent event) {
        this.currentEvent = event;

        natTable.redraw(0, 0, natTable.getWidth(), natTable.getHeight(), false);
    }

    @Override
    public void mouseUp(final NatTable natTable, final MouseEvent event) {
        natTable.removeOverlayPainter(this.cellImageOverlayPainter);
        this.cellImage.dispose();

        natTable.redraw(0, 0, natTable.getWidth(), natTable.getHeight(), false);
    }

    protected MouseEvent getInitialEvent() {
        return this.initialEvent;
    }

    protected MouseEvent getCurrentEvent() {
        return this.currentEvent;
    }

    private void setCellImage(final NatTable natTable) {
        final int columnPosition = natTable.getColumnPositionByX(this.currentEvent.x);
        final int rowPosition = natTable.getRowPositionByY(this.currentEvent.y);
        final ILayerCell cell = natTable.getCellByPosition(columnPosition, rowPosition);

        final Rectangle cellBounds = cell.getBounds();
        this.xOffset = this.currentEvent.x - cellBounds.x;
        final Image image = new Image(natTable.getDisplay(), cellBounds.width, cellBounds.height);

        final GC gc = new GC(image);
        final IConfigRegistry configRegistry = natTable.getConfigRegistry();
        final ICellPainter cellPainter = cell.getLayer().getCellPainter(columnPosition, rowPosition, cell, configRegistry);
        if (cellPainter != null) {
            cellPainter.paintCell(cell, gc, new Rectangle(0, 0, cellBounds.width, cellBounds.height), configRegistry);
        }
        gc.dispose();

        final ImageData imageData = image.getImageData();
        image.dispose();
        imageData.alpha = 150;

        this.cellImage = new Image(natTable.getDisplay(), imageData);
    }

    private class CellImageOverlayPainter implements IOverlayPainter {

        @Override
        public void paintOverlay(final GC gc, final ILayer layer) {
            if (JoColumnReorderCellDragMode.this.cellImage != null & !JoColumnReorderCellDragMode.this.cellImage.isDisposed()) {
                gc.drawImage(
                        JoColumnReorderCellDragMode.this.cellImage,
                        JoColumnReorderCellDragMode.this.currentEvent.x - JoColumnReorderCellDragMode.this.xOffset,
                        0);
            }
        }

    }

}
