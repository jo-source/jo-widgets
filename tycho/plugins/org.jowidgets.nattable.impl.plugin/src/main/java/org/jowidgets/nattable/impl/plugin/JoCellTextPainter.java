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
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.CellStyleUtil;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelSpi;
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.common.types.Markup;
import org.jowidgets.spi.impl.swt.common.color.ColorCache;
import org.jowidgets.spi.impl.swt.common.util.FontProvider;
import org.jowidgets.util.Assert;

final class JoCellTextPainter extends TextPainter {

    private final ITableColumnModelSpi columnModel;

    private Color originalBackground;
    private Color originalForeground;
    private Font originalFont;

    JoCellTextPainter(final ITableColumnModelSpi columnModel) {
        super(false, false);
        Assert.paramNotNull(columnModel, "columnModel");
        this.columnModel = columnModel;
    }

    @Override
    public void paintCell(
        final ILayerCell layerCell,
        final GC gc,
        final Rectangle rectangle,
        final IConfigRegistry configRegistry) {

        final ITableCell tableCell = (ITableCell) layerCell.getDataValue();

        final Rectangle originalClipping = gc.getClipping();
        gc.setClipping(rectangle.intersection(originalClipping));

        final IStyle cellStyle = CellStyleUtil.getCellStyle(layerCell, configRegistry);
        setupGC(gc, tableCell, layerCell, cellStyle);

        final int fontHeight = gc.getFontMetrics().getHeight();
        String text = convertDataType(layerCell, configRegistry);

        // Draw Text
        text = getTextToDisplay(layerCell, gc, rectangle.width, text);

        // if the content height is bigger than the available row height
        // we're extending the row height (only if word wrapping is enabled)
        final int contentHeight = fontHeight + (this.spacing * 2);
        final int contentToCellDiff = (layerCell.getBounds().height - rectangle.height);

        if (performRowResize(contentHeight, rectangle)) {
            final ILayer layer = layerCell.getLayer();
            layer.doCommand(new RowResizeCommand(layer, layerCell.getRowPosition(), contentHeight + contentToCellDiff));
        }

        final int contentWidth = Math.min(getLengthFromCache(gc, text), rectangle.width);
        final AlignmentHorizontal horizontalAlignment = getHorizontalAlignment(layerCell);
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

    private AlignmentHorizontal getHorizontalAlignment(final ILayerCell cell) {
        return columnModel.getColumn(cell.getColumnIndex()).getAlignment();
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

    private void setupGC(final GC gc, final ITableCell tableCell, final ILayerCell layerCell, final IStyle cellStyle) {
        this.originalFont = gc.getFont();
        this.originalForeground = gc.getForeground();
        this.originalBackground = gc.getBackground();

        gc.setAntialias(GUIHelper.DEFAULT_ANTIALIAS);
        gc.setTextAntialias(GUIHelper.DEFAULT_TEXT_ANTIALIAS);
        gc.setFont(getFont(tableCell, cellStyle));
        gc.setForeground(getForegroundColor(tableCell, layerCell, cellStyle));
        gc.setBackground(GUIHelper.COLOR_LIST_BACKGROUND);
    }

    @Override
    public void resetGC(final GC gc) {
        gc.setFont(this.originalFont);
        gc.setForeground(this.originalForeground);
        gc.setBackground(this.originalBackground);
    }

    private Font getFont(final ITableCell cell, final IStyle style) {
        final Font result = style.getAttributeValue(CellStyleAttributes.FONT);
        final Markup markup = cell.getMarkup();
        if (markup != null && !Markup.DEFAULT.equals(markup)) {
            return FontProvider.deriveFont(result, markup);
        }
        else {
            return result;
        }
    }

    private Color getForegroundColor(final ITableCell tableCell, final ILayerCell layerCell, final IStyle style) {
        final Color defaultForeground = style.getAttributeValue(CellStyleAttributes.FOREGROUND_COLOR);

        if (DisplayMode.NORMAL.equals(layerCell.getDisplayMode())) {
            final IColorConstant foreground = tableCell.getForegroundColor();
            if (foreground != null) {
                return ColorCache.getInstance().getColor(foreground);
            }
        }
        else if (DisplayMode.SELECT.equals(layerCell.getDisplayMode())) {
            final IColorConstant selectedForeground = tableCell.getSelectedForegroundColor();
            if (selectedForeground != null) {
                return ColorCache.getInstance().getColor(selectedForeground);
            }
        }

        return defaultForeground != null ? defaultForeground : GUIHelper.COLOR_LIST_FOREGROUND;
    }

}
