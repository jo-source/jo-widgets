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
import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.spi.impl.swt.common.color.ColorCache;

final class JoColumnBackgroundPainter extends CellPainterWrapper {

    private static final int SPLIT = 9;

    private static final IColorConstant TOP = new ColorValue(255, 255, 255);
    private static final IColorConstant BOTTOM = new ColorValue(243, 244, 246);

    private static final IColorConstant TOP_HOVER = new ColorValue(227, 247, 255);
    private static final IColorConstant BOTTOM_HOVER = new ColorValue(185, 229, 252);

    private static final IColorConstant TOP_CLICKED = new ColorValue(188, 228, 249);
    private static final IColorConstant BOTTOM_CLICKED = new ColorValue(139, 214, 246);

    private static final IColorConstant VERT_TOP_BORDER = new ColorValue(240, 240, 240);
    private static final IColorConstant VERT_BOTTOM_BORDER = new ColorValue(229, 230, 231);
    private static final IColorConstant BOTTOM_BORDER = new ColorValue(213, 213, 213);

    private static final IColorConstant VERT_TOP_BORDER_HOVER = new ColorValue(136, 203, 235);
    private static final IColorConstant VERT_BOTTOM_BORDER_HOVER = new ColorValue(105, 187, 227);
    private static final IColorConstant BOTTOM_BORDER_HOVER = new ColorValue(147, 201, 227);

    private static final IColorConstant VERT_TOP_BORDER_CLICKED = new ColorValue(122, 158, 177);
    private static final IColorConstant VERT_BOTTOM_BORDER_CLICKED = new ColorValue(79, 144, 174);
    private static final IColorConstant BOTTOM_BORDER_CLICKED = new ColorValue(147, 201, 227);

    private static final IColorConstant VERT_TOP_INNER_BORDER_CLICKED = new ColorValue(162, 203, 224);
    private static final IColorConstant VERT_BOTTOM_INNER_BORDER_CLICKED = new ColorValue(112, 187, 222);
    private static final IColorConstant TOP_BORDER_CLICKED = VERT_TOP_BORDER_CLICKED;
    private static final IColorConstant TOP_INNER_BORDER_CLICKED = VERT_TOP_INNER_BORDER_CLICKED;

    JoColumnBackgroundPainter(final ICellPainter painter) {
        super(painter);
    }

    @Override
    public void paintCell(final ILayerCell cell, final GC gc, final Rectangle bounds, final IConfigRegistry configRegistry) {
        if (cell.getConfigLabels().hasLabel(ClickedColumnConfigLabelAccumulator.CLICKED_COLUMN_LABEL)) {
            paintCellClicked(gc, bounds);
        }
        else if (cell.getConfigLabels().hasLabel(HoveredColumnConfigLabelAccumulator.HOVERED_COLUMN_LABEL)) {
            paintCellHover(gc, bounds);
        }
        else {
            paintCellDefault(gc, bounds);
        }
        super.paintCell(cell, gc, bounds, configRegistry);
    }

    private void paintCellClicked(final GC gc, final Rectangle bounds) {
        paintCellBackground(gc, bounds, TOP_CLICKED, BOTTOM_CLICKED);
        paintBorder(gc, bounds, VERT_TOP_BORDER_CLICKED, VERT_BOTTOM_BORDER_CLICKED, BOTTOM_BORDER_CLICKED, true);
        paintClickedBorder(gc, bounds);
    }

    private void paintCellHover(final GC gc, final Rectangle bounds) {
        paintCellBackground(gc, bounds, TOP_HOVER, BOTTOM_HOVER);
        paintBorder(gc, bounds, VERT_TOP_BORDER_HOVER, VERT_BOTTOM_BORDER_HOVER, BOTTOM_BORDER_HOVER, true);
    }

    private void paintCellDefault(final GC gc, final Rectangle bounds) {
        paintCellBackground(gc, bounds, TOP, BOTTOM);
        paintBorder(gc, bounds, VERT_TOP_BORDER, VERT_BOTTOM_BORDER, BOTTOM_BORDER, false);
    }

    private void paintBorder(
        final GC gc,
        final Rectangle bounds,
        final IColorConstant verticalTopBorderColor,
        final IColorConstant verticalBottomBorderColor,
        final IColorConstant horizontalBottomBorderColor,
        final boolean leftBorder) {

        gc.setBackground(ColorCache.getInstance().getColor(verticalTopBorderColor));
        if (leftBorder) {
            gc.fillRectangle(bounds.x, bounds.y, 1, SPLIT);
        }
        gc.fillRectangle(bounds.x + bounds.width - 1, bounds.y, 1, SPLIT);

        gc.setBackground(ColorCache.getInstance().getColor(verticalBottomBorderColor));
        if (leftBorder) {
            gc.fillRectangle(bounds.x, bounds.y + SPLIT, 1, bounds.height - SPLIT - 2);
        }
        gc.fillRectangle(bounds.x + bounds.width - 1, bounds.y + SPLIT, 1, bounds.height - SPLIT - 2);

        gc.setBackground(ColorCache.getInstance().getColor(horizontalBottomBorderColor));
        gc.fillRectangle(bounds.x, bounds.y + bounds.height - 2, bounds.width, 1);
    }

    private void paintClickedBorder(final GC gc, final Rectangle bounds) {
        gc.setBackground(ColorCache.getInstance().getColor(VERT_TOP_INNER_BORDER_CLICKED));
        gc.fillRectangle(bounds.x + 1, bounds.y, 1, SPLIT);
        gc.fillRectangle(bounds.x + bounds.width - 2, bounds.y, 1, SPLIT);

        gc.setBackground(ColorCache.getInstance().getColor(VERT_BOTTOM_INNER_BORDER_CLICKED));
        gc.fillRectangle(bounds.x + 1, bounds.y + SPLIT, 1, bounds.height - SPLIT - 2);
        gc.fillRectangle(bounds.x + bounds.width - 2, bounds.y + SPLIT, 1, bounds.height - SPLIT - 2);

        gc.setBackground(ColorCache.getInstance().getColor(TOP_BORDER_CLICKED));
        gc.fillRectangle(bounds.x, bounds.y, bounds.width, 1);

        gc.setBackground(ColorCache.getInstance().getColor(TOP_INNER_BORDER_CLICKED));
        gc.fillRectangle(bounds.x + 2, bounds.y + 1, bounds.width - 4, 1);
    }

    private void paintCellBackground(
        final GC gc,
        final Rectangle bounds,
        final IColorConstant topColor,
        final IColorConstant bottomColor) {
        gc.setBackground(ColorCache.getInstance().getColor(topColor));
        gc.fillRectangle(bounds.x, bounds.y, bounds.width, SPLIT);
        gc.setBackground(ColorCache.getInstance().getColor(bottomColor));
        gc.fillRectangle(bounds.x, bounds.y + SPLIT, bounds.width, bounds.height - SPLIT - 1);
    }

}
