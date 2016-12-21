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

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.jowidgets.util.Assert;

/**
 * Paints an icon (if available) and an text (if available)
 */
final class JoIconAndTextCellPainter implements ICellPainter {

    private final ICellPainter textPainter;
    private final ICellPainter iconPainter;
    private final int spacing;

    JoIconAndTextCellPainter(final ICellPainter iconPainter, final ICellPainter textPainter, final int spacing) {
        Assert.paramNotNull(iconPainter, "iconPainter");
        Assert.paramNotNull(textPainter, "textPainter");

        this.iconPainter = iconPainter;
        this.textPainter = textPainter;
        this.spacing = spacing;
    }

    @Override
    public int getPreferredWidth(final ILayerCell cell, final GC gc, final IConfigRegistry configRegistry) {

        final int textWidth = this.textPainter.getPreferredWidth(cell, gc, configRegistry);
        final int iconWidth = this.iconPainter.getPreferredWidth(cell, gc, configRegistry);

        if (textWidth > 0 && iconWidth > 0) {
            return textWidth + spacing + iconWidth;
        }
        else {//do not use spacing if one of both is empty
            return textWidth + iconWidth;
        }
    }

    @Override
    public int getPreferredHeight(final ILayerCell cell, final GC gc, final IConfigRegistry configRegistry) {
        return Math.max(
                this.textPainter.getPreferredHeight(cell, gc, configRegistry),
                this.iconPainter.getPreferredHeight(cell, gc, configRegistry));
    }

    @Override
    public void paintCell(
        final ILayerCell cell,
        final GC gc,
        final Rectangle adjustedCellBounds,
        final IConfigRegistry configRegistry) {
        final Rectangle textPainterBounds = getTextPainterBounds(cell, gc, adjustedCellBounds, configRegistry);
        final Rectangle iconPainterBounds = getIconPainterBounds(cell, gc, adjustedCellBounds, configRegistry);

        this.iconPainter.paintCell(cell, gc, iconPainterBounds, configRegistry);
        this.textPainter.paintCell(cell, gc, textPainterBounds, configRegistry);
    }

    private Rectangle getTextPainterBounds(
        final ILayerCell cell,
        final GC gc,
        final Rectangle adjustedCellBounds,
        final IConfigRegistry configRegistry) {

        final int preferredIconPainterWidth = this.iconPainter.getPreferredWidth(cell, gc, configRegistry);

        int usedSpacing = 0;
        if (preferredIconPainterWidth > 0 && adjustedCellBounds.width > 0) {
            usedSpacing = spacing;
        }

        final int grabbedPreferredWidth = adjustedCellBounds.width - preferredIconPainterWidth - usedSpacing;

        return new Rectangle(
            adjustedCellBounds.x + preferredIconPainterWidth + usedSpacing,
            adjustedCellBounds.y,
            grabbedPreferredWidth,
            adjustedCellBounds.height).intersection(adjustedCellBounds);
    }

    private Rectangle getIconPainterBounds(
        final ILayerCell cell,
        final GC gc,
        final Rectangle adjustedCellBounds,
        final IConfigRegistry configRegistry) {

        final int preferredIconPainterWidth = this.iconPainter.getPreferredWidth(cell, gc, configRegistry);
        final int preferredIconPainterHeight = this.iconPainter.getPreferredHeight(cell, gc, configRegistry);

        return new Rectangle(
            adjustedCellBounds.x,
            adjustedCellBounds.y + ((adjustedCellBounds.height - preferredIconPainterHeight) / 2),
            preferredIconPainterWidth,
            preferredIconPainterHeight);

    }

    @Override
    public ICellPainter getCellPainterAt(
        final int x,
        final int y,
        final ILayerCell cell,
        final GC gc,
        final Rectangle adjustedCellBounds,
        final IConfigRegistry configRegistry) {

        final Rectangle iconPainterBounds = getIconPainterBounds(cell, gc, adjustedCellBounds, configRegistry);
        if (this.iconPainter != null && iconPainterBounds.contains(x, y)) {
            return this.iconPainter.getCellPainterAt(x, y, cell, gc, iconPainterBounds, configRegistry);
        }
        else {
            final Rectangle textPainterBounds = getTextPainterBounds(cell, gc, adjustedCellBounds, configRegistry);
            if (this.textPainter != null && textPainterBounds.contains(x, y)) {
                return this.textPainter.getCellPainterAt(x, y, cell, gc, textPainterBounds, configRegistry);
            }
        }
        return this;
    }

}
