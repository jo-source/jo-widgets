/*
 * Copyright (c) 2017, MGrossmann
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
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.AbstractTextPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.CellStyleUtil;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.nebula.widgets.nattable.tooltip.NatTableContentTooltip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelSpi;
import org.jowidgets.common.model.ITableColumnSpi;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.types.Markup;
import org.jowidgets.nattable.impl.plugin.painter.CellPainterFactory;
import org.jowidgets.spi.impl.swt.common.util.FontProvider;
import org.jowidgets.util.Assert;
import org.jowidgets.util.EmptyCheck;

final class NatTableTooltip extends NatTableContentTooltip {

    private static final String NEW_LINE_REGEX = AbstractTextPainter.NEW_LINE_REGEX;
    private static final int MAX_CELL_TEXT_TOOLTIP_LENGTH = 2500;
    private static final int PADDING = CellPainterFactory.CELL_PADDING * 2;
    private static final int Y_OFFSET = 18;

    private final ITableDataModel dataModel;
    private final ITableColumnModelSpi columnModel;

    private final Shell dummyShell;
    private final GC graphicContext;

    NatTableTooltip(
        final NatTable natTable,
        final ITableDataModel dataModel,
        final ITableColumnModelSpi columnModel,
        final String... tooltipRegions) {

        super(natTable, tooltipRegions);

        Assert.paramNotNull(dataModel, "dataModel");
        Assert.paramNotNull(columnModel, "columnModel");

        this.dataModel = dataModel;
        this.columnModel = columnModel;

        this.dummyShell = new Shell(SWT.NONE);
        this.graphicContext = new GC(new Label(dummyShell, SWT.NONE));

        setShift(new Point(0, Y_OFFSET));
    }

    @Override
    protected String getText(final Event event) {
        final int col = natTable.getColumnPositionByX(event.x);
        final int row = natTable.getRowPositionByY(event.y);

        final ILayerCell cell = natTable.getCellByPosition(col, row);
        if (cell != null) {
            // if the registered cell painter is the PasswordCellPainter, there
            // will be no tooltip
            final ICellPainter painter = natTable.getConfigRegistry().getConfigAttribute(
                    CellConfigAttributes.CELL_PAINTER,
                    DisplayMode.NORMAL,
                    cell.getConfigLabels().getLabels());
            if (isVisibleContentPainter(painter)) {
                return getToolTipText(event);
            }
        }
        return null;
    }

    private String getToolTipText(final Event event) {
        final int rowPosition = natTable.getRowPositionByY(event.y);
        final int columnPosition = natTable.getColumnPositionByX(event.x);

        final int rowIndex = natTable.getRowIndexByPosition(rowPosition);
        final int columnIndex = natTable.getColumnIndexByPosition(columnPosition);

        if (rowPosition == 0 && rowIndex >= 0 && columnIndex >= 0) {
            return getColumnToolTipText(columnIndex);
        }
        else if (rowIndex >= 0 && columnIndex >= 0) {
            return getCellToolTipText(rowPosition, columnPosition, rowIndex, columnIndex);
        }
        return null;
    }

    private String getCellToolTipText(
        final int rowPosition,
        final int columnPosition,
        final int rowIndex,
        final int columnIndex) {

        final ITableCell tableCell = dataModel.getCell(rowIndex, columnIndex);
        if (tableCell != null) {
            final String result = tableCell.getToolTipText();
            if (!EmptyCheck.isEmpty(result)) {
                return result;
            }
            else {
                return getTooltipTextIfCellTextTruncated(tableCell, rowPosition, columnPosition, rowIndex, columnIndex);
            }
        }
        return null;

    }

    private String getTooltipTextIfCellTextTruncated(
        final ITableCell tableCell,
        final int rowPosition,
        final int columnPosition,
        final int rowIndex,
        final int columnIndex) {
        //at the moment this feature is only supported if no icons are used in the cell
        if (!EmptyCheck.isEmpty(tableCell.getText()) && tableCell.getIcon() == null) {
            final ILayerCell layerCell = natTable.getCellByPosition(columnPosition, rowPosition);
            if (layerCell != null) {
                return getTooltipTextIfCellTextTruncated(tableCell, layerCell);
            }
        }
        return null;
    }

    private String getTooltipTextIfCellTextTruncated(final ITableCell tableCell, final ILayerCell layerCell) {

        final String cellText = getCellText(tableCell);

        final IStyle style = CellStyleUtil.getCellStyle(layerCell, natTable.getConfigRegistry());
        final Font font = getFont(tableCell, style);
        graphicContext.setFont(font);

        final int border = 1;//remove the border
        final int innerCellWidth = layerCell.getBounds().width - border;

        final int textWidth = graphicContext.textExtent(cellText).x + PADDING;

        if (textWidth > innerCellWidth) {
            return cellText;
        }
        else {
            return null;
        }
    }

    private String getCellText(final ITableCell tableCell) {
        String result = tableCell.getText();
        if (!EmptyCheck.isEmpty(result)) {
            result = result.replaceAll(NEW_LINE_REGEX, "").trim();
            if (result.length() > MAX_CELL_TEXT_TOOLTIP_LENGTH) {
                result = result.substring(0, MAX_CELL_TEXT_TOOLTIP_LENGTH);
            }
        }
        return result;
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

    private String getColumnToolTipText(final int columnIndex) {
        final ITableColumnSpi column = columnModel.getColumn(columnIndex);
        if (column != null) {
            return column.getToolTipText();
        }
        else {
            return null;
        }
    }

    void dispose() {
        graphicContext.dispose();
        dummyShell.dispose();
    }

}
