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

package org.jowidgets.nattable.impl.plugin.listener;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.CellStyleUtil;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelSpi;
import org.jowidgets.common.model.ITableColumnSpi;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.types.Markup;
import org.jowidgets.nattable.impl.plugin.painter.CellPainterFactory;
import org.jowidgets.spi.impl.swt.common.util.FontProvider;
import org.jowidgets.util.Assert;
import org.jowidgets.util.EmptyCheck;

public final class ToolTipListener implements Listener {

    private static final int PADDING = CellPainterFactory.CELL_PADDING * 2;
    private static final int Y_OFFSET = 18;

    private final NatTable table;
    private final ITableDataModel dataModel;
    private final ITableColumnModelSpi columnModel;
    private final ToolTip toolTip;

    private final Shell dummyShell;
    private final GC graphicContext;

    public ToolTipListener(
        final ToolTip toolTip,
        final NatTable table,
        final ITableColumnModelSpi columnModel,
        final ITableDataModel dataModel) {

        Assert.paramNotNull(table, "table");
        Assert.paramNotNull(columnModel, "columnModel");
        Assert.paramNotNull(dataModel, "dataModel");
        Assert.paramNotNull(toolTip, "toolTip");

        this.toolTip = toolTip;
        this.table = table;
        this.columnModel = columnModel;
        this.dataModel = dataModel;

        this.dummyShell = new Shell(SWT.NONE);
        this.graphicContext = new GC(new Label(dummyShell, SWT.NONE));

        table.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(final DisposeEvent e) {
                dispose();
            }
        });
    }

    @Override
    public void handleEvent(final Event event) {
        if (event.type == SWT.MouseHover) {
            final int rowPosition = table.getRowPositionByY(event.y);
            final int columnPosition = table.getColumnPositionByX(event.x);

            final int rowIndex = table.getRowIndexByPosition(rowPosition);
            final int columnIndex = table.getColumnIndexByPosition(columnPosition);

            if (rowPosition == 0 && rowIndex >= 0 && columnIndex >= 0) {
                showToolTip(getColumnToolTipText(columnIndex));
            }
            else if (rowIndex >= 0 && columnIndex >= 0) {
                showToolTip(getCellToolTipText(rowPosition, columnPosition, rowIndex, columnIndex));
            }
        }
        else {
            if (!toolTip.isDisposed()) {
                toolTip.setVisible(false);
            }
        }
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
            final ILayerCell layerCell = table.getCellByPosition(columnPosition, rowPosition);
            if (layerCell != null) {
                return getTooltipTextIfCellTextTruncated(tableCell, layerCell);
            }
        }
        return null;
    }

    private String getTooltipTextIfCellTextTruncated(final ITableCell tableCell, final ILayerCell layerCell) {

        final String cellText = tableCell.getText().trim();
        final IStyle style = CellStyleUtil.getCellStyle(layerCell, table.getConfigRegistry());
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

    private void showToolTip(final String message) {
        if (EmptyCheck.isEmpty(message)) {
            return;
        }
        if (!toolTip.isDisposed()) {
            toolTip.setVisible(false);
            toolTip.setMessage(message);
            final Point location = Display.getCurrent().getCursorLocation();
            toolTip.setLocation(location.x, location.y + Y_OFFSET);
            toolTip.setVisible(true);
        }
    }

    private void dispose() {
        graphicContext.dispose();
        dummyShell.dispose();
    }

}
