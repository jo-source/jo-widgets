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
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.nebula.widgets.nattable.resize.command.RowResizeCommand;
import org.eclipse.nebula.widgets.nattable.style.CellStyleUtil;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.jowidgets.common.types.AlignmentHorizontal;

abstract class AbstractJoTextPainter extends TextPainter {

    AbstractJoTextPainter() {
        super(false, false);
    }

    abstract AlignmentHorizontal getHorizontalAlignment(ILayerCell cell);

    void setupGC(final GC gc, final ILayerCell layerCell, final IStyle cellStyle) {
        setupGCFromConfig(gc, cellStyle);
    }

    @Override
    public void paintCell(
        final ILayerCell cell,
        final GC gc,
        final Rectangle rectangle,
        final IConfigRegistry configRegistry) {

        final Rectangle originalClipping = gc.getClipping();
        gc.setClipping(rectangle.intersection(originalClipping));

        final IStyle cellStyle = CellStyleUtil.getCellStyle(cell, configRegistry);
        setupGC(gc, cell, cellStyle);

        final int fontHeight = gc.getFontMetrics().getHeight();
        String text = convertDataType(cell, configRegistry);

        // Draw Text
        text = getTextToDisplay(cell, gc, rectangle.width, text);

        // if the content height is bigger than the available row height
        // we're extending the row height (only if word wrapping is enabled)
        final int contentHeight = fontHeight + (this.spacing * 2);
        final int contentToCellDiff = (cell.getBounds().height - rectangle.height);

        if (performRowResize(contentHeight, rectangle)) {
            final ILayer layer = cell.getLayer();
            layer.doCommand(new RowResizeCommand(layer, cell.getRowPosition(), contentHeight + contentToCellDiff));
        }

        final int contentWidth = Math.min(getLengthFromCache(gc, text), rectangle.width);
        final AlignmentHorizontal horizontalAlignment = getHorizontalAlignment(cell);
        final int horizontalAligmentPadding = getHorizontalAlignmentPadding(horizontalAlignment, rectangle, contentWidth);

        final int verticalAlignmentPadding = CellStyleUtil.getVerticalAlignmentPadding(cellStyle, rectangle, contentHeight);
        gc.drawText(
                text,
                rectangle.x + horizontalAligmentPadding + this.spacing,
                rectangle.y + verticalAlignmentPadding + this.spacing,
                SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER);

        // start x of line = start x of text
        final int x = rectangle.x + horizontalAligmentPadding + this.spacing;
        // y = start y of text
        final int y = rectangle.y + verticalAlignmentPadding + this.spacing;
        final int length = gc.textExtent(text).x;
        paintDecoration(cellStyle, gc, x, y, length, fontHeight);

        gc.setClipping(originalClipping);
        resetGC(gc);
    }

    private int getHorizontalAlignmentPadding(
        final AlignmentHorizontal alignment,
        final Rectangle rectangle,
        final int contentWidth) {

        if (AlignmentHorizontal.CENTER.equals(alignment)) {
            return Math.max(0, (rectangle.width - contentWidth) / 2);
        }
        else if (AlignmentHorizontal.RIGHT.equals(alignment)) {
            return Math.max(0, rectangle.width - contentWidth);
        }
        else {
            return 0;
        }
    }

}
