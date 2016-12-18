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

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.CellPainterWrapper;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Decorator that moves the content on mouse click by offset pixel in x and y direction
 */
final class JoMoveOnMouseClickPaintDecorator extends CellPainterWrapper {

    private static final int OFFSET = 1;

    JoMoveOnMouseClickPaintDecorator(final ICellPainter interiorPainter) {
        super(interiorPainter);
    }

    @Override
    public int getPreferredWidth(final ILayerCell cell, final GC gc, final IConfigRegistry configRegistry) {
        return getKlickOffset(cell) + super.getPreferredWidth(cell, gc, configRegistry);
    }

    @Override
    public int getPreferredHeight(final ILayerCell cell, final GC gc, final IConfigRegistry configRegistry) {
        return getKlickOffset(cell) + super.getPreferredHeight(cell, gc, configRegistry);
    }

    @Override
    public void paintCell(
        final ILayerCell cell,
        final GC gc,
        final Rectangle adjustedCellBounds,
        final IConfigRegistry configRegistry) {
        final Rectangle interiorBounds = getInteriorBounds(cell, adjustedCellBounds);
        if (interiorBounds.width > 0 && interiorBounds.height > 0) {
            super.paintCell(cell, gc, interiorBounds, configRegistry);
        }
    }

    @Override
    public ICellPainter getCellPainterAt(
        final int x,
        final int y,
        final ILayerCell cell,
        final GC gc,
        final Rectangle adjustedCellBounds,
        final IConfigRegistry configRegistry) {
        final int klickOffset = getKlickOffset(cell);
        return super.getCellPainterAt(x - klickOffset, y - klickOffset, cell, gc, adjustedCellBounds, configRegistry);
    }

    private Rectangle getInteriorBounds(final ILayerCell cell, final Rectangle adjustedCellBounds) {
        final int klickOffset = getKlickOffset(cell);
        return new Rectangle(
            adjustedCellBounds.x + klickOffset,
            adjustedCellBounds.y + klickOffset,
            adjustedCellBounds.width - klickOffset,
            adjustedCellBounds.height - klickOffset);
    }

    private int getKlickOffset(final ILayerCell cell) {
        if (cell.getConfigLabels().hasLabel(ClickedColumnConfigLabelAccumulator.CLICKED_COLUMN_LABEL)) {
            return OFFSET;
        }
        else {
            return 0;
        }
    }

}
