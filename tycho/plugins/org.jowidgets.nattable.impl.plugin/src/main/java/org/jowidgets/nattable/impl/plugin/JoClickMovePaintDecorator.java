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
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.CellStyleUtil;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.nebula.widgets.nattable.style.VerticalAlignmentEnum;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

final class JoClickMovePaintDecorator extends CellPainterWrapper {

    JoClickMovePaintDecorator(final ICellPainter interiorPainter) {
        super(interiorPainter);
    }

    private int getKlickOffset(final ILayerCell cell) {
        if (cell.getConfigLabels().hasLabel(ClickedColumnConfigLabelAccumulator.CLICKED_COLUMN_LABEL)) {
            return 1;
        }
        else {
            return 0;
        }
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

    private Rectangle getInteriorBounds(final ILayerCell cell, final Rectangle adjustedCellBounds) {
        final int klickOffset = getKlickOffset(cell);
        return new Rectangle(
            adjustedCellBounds.x + klickOffset,
            adjustedCellBounds.y + klickOffset,
            adjustedCellBounds.width - klickOffset,
            adjustedCellBounds.height - klickOffset);
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

        // need to take the alignment into account
        final IStyle cellStyle = CellStyleUtil.getCellStyle(cell, configRegistry);

        final HorizontalAlignmentEnum horizontalAlignment = cellStyle.getAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT);
        int horizontalAlignmentPadding = 0;
        switch (horizontalAlignment) {
            case LEFT:
                horizontalAlignmentPadding = klickOffset;
                break;
            case CENTER:
                horizontalAlignmentPadding = klickOffset;
                break;
            case RIGHT:
                break;
            default:
                break;
        }

        final VerticalAlignmentEnum verticalAlignment = cellStyle.getAttributeValue(CellStyleAttributes.VERTICAL_ALIGNMENT);
        int verticalAlignmentPadding = 0;
        switch (verticalAlignment) {
            case TOP:
                verticalAlignmentPadding = klickOffset;
                break;
            case MIDDLE:
                verticalAlignmentPadding = klickOffset;
                break;
            case BOTTOM:
                break;
            default:
                break;
        }

        return super.getCellPainterAt(
                x - horizontalAlignmentPadding,
                y - verticalAlignmentPadding,
                cell,
                gc,
                adjustedCellBounds,
                configRegistry);
    }

}
