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

package org.jowidgets.nattable.impl.plugin.painter;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.painter.layer.NatGridLayerPainter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.jowidgets.api.color.Colors;
import org.jowidgets.nattable.impl.plugin.layer.CellConstants;
import org.jowidgets.spi.impl.swt.common.color.ColorProvider;

/**
 * Renders the same background as the column header to the residual space right to the table
 */
public final class JoNatTableGridLayerPainter extends NatGridLayerPainter {

    private static final ColorProvider WHITE = new ColorProvider(Colors.WHITE);

    private final JoColumnBackgroundPainter backgroundPainter;

    public JoNatTableGridLayerPainter(final NatTable natTable, final Color gridColor, final int defaultRowHeight) {
        super(natTable, gridColor, defaultRowHeight);
        this.backgroundPainter = new JoColumnBackgroundPainter(null);
    }

    @Override
    protected void paintBackground(
        final ILayer natLayer,
        final GC gc,
        final int xOffset,
        final int yOffset,
        final Rectangle rectangle,
        final IConfigRegistry configRegistry) {

        //paint the grid
        super.paintBackground(natLayer, gc, xOffset, yOffset, rectangle, configRegistry);

        //paint the default column header for the full width
        final Rectangle headerBackgroundPainterBounds = new Rectangle(
            rectangle.x,
            0,
            rectangle.width,
            CellConstants.COLUMN_HEADER_HEIGHT);
        backgroundPainter.paintCellDefault(gc, headerBackgroundPainterBounds);

        //remove the first grid line under the header because it looks weird and its like in win7 (first grid line is white there too)
        gc.setBackground(WHITE.get());
        gc.fillRectangle(0, CellConstants.COLUMN_HEADER_HEIGHT - 1, rectangle.width, 1);
    }

}
