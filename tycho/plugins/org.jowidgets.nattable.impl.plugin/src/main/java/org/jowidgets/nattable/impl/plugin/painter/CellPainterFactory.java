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

import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.PaddingDecorator;
import org.jowidgets.common.model.ITableColumnModelSpi;
import org.jowidgets.spi.impl.swt.common.image.SwtImageRegistry;

public final class CellPainterFactory {

    public static final int CELL_PADDING = 6;

    private static final int ICON_TEXT_SPACING = 8;

    private CellPainterFactory() {}

    public static ICellPainter createCellPainter(final ITableColumnModelSpi columnModel, final SwtImageRegistry imageRegistry) {
        final JoCellImagePainter imagePainter = new JoCellImagePainter(imageRegistry);
        final JoCellTextPainter textPainter = new JoCellTextPainter();
        final ICellPainter iconTextPainter = new JoIconAndTextCellPainter(imagePainter, textPainter, ICON_TEXT_SPACING);
        final ICellPainter alignmentPainter = new JoCellHorizontalAlignmentPainter(iconTextPainter, columnModel);
        final PaddingDecorator paddingPainter = new PaddingDecorator(alignmentPainter, 0, CELL_PADDING, 0, CELL_PADDING, false);
        return new JoCellBackgroundPainter(paddingPainter);
    }

    public static ICellPainter createHeaderPainter(final SwtImageRegistry imageRegistry) {
        final ICellPainter imagePainter = new JoColumnImagePainter(imageRegistry);
        final ICellPainter textPainter = new JoColumnTextPainter();
        final ICellPainter iconTextPainter = new JoIconAndTextCellPainter(imagePainter, textPainter, ICON_TEXT_SPACING);
        final ICellPainter alignmentPainter = new JoColumnHorizontalAlignmentPainter(iconTextPainter);
        final PaddingDecorator paddingPainter = new PaddingDecorator(alignmentPainter, 0, CELL_PADDING, 0, CELL_PADDING, false);
        final JoMoveOnMouseClickPaintDecorator klickMovePainter = new JoMoveOnMouseClickPaintDecorator(paddingPainter);
        return new JoColumnBackgroundPainter(klickMovePainter);
    }

}
