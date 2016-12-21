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

package org.jowidgets.nattable.impl.plugin.configuration;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.painter.layer.NatGridLayerPainter;
import org.eclipse.nebula.widgets.nattable.resize.event.ColumnResizeEventMatcher;
import org.eclipse.nebula.widgets.nattable.selection.SelectionConfigAttributes;
import org.eclipse.nebula.widgets.nattable.style.BorderStyle;
import org.eclipse.nebula.widgets.nattable.style.BorderStyle.LineStyleEnum;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.theme.ModernNatTableThemeConfiguration;
import org.eclipse.nebula.widgets.nattable.style.theme.ThemeConfiguration;
import org.eclipse.nebula.widgets.nattable.ui.action.AggregateDragMode;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.jowidgets.api.color.Colors;
import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.model.ITableColumnModelSpi;
import org.jowidgets.nattable.impl.plugin.painter.CellPainterFactory;
import org.jowidgets.nattable.impl.plugin.painter.HoveredColumnConfigLabelAccumulator;
import org.jowidgets.spi.impl.swt.common.color.ColorCache;
import org.jowidgets.spi.impl.swt.common.image.SwtImageRegistry;

public final class JoNatTableConfigurator {

    private static final IColorConstant GRID_COLOR = new ColorValue(240, 240, 240);

    private JoNatTableConfigurator() {}

    public static void configureNatTable(
        final NatTable table,
        final ITableColumnModelSpi columnModel,
        final SwtImageRegistry imageRegistry) {

        final IConfigRegistry config = table.getConfigRegistry();

        //use modern theme as base theme (so fonts look like win 7 and win 10)
        final ThemeConfiguration modernTheme = new ModernNatTableThemeConfiguration();
        table.setTheme(modernTheme);

        //use white background instead of grey to be more close to the swt win7 table
        table.setBackground(ColorCache.getInstance().getColor(Colors.WHITE));

        //use grid color from the swt table under win7
        final Color gridColor = ColorCache.getInstance().getColor(GRID_COLOR);
        config.registerConfigAttribute(CellConfigAttributes.GRID_LINE_COLOR, gridColor, DisplayMode.NORMAL, GridRegion.BODY);

        //use grid for remainder space to be more close to the swt win7 table
        final NatGridLayerPainter gridPainter = new NatGridLayerPainter(table, gridColor, DataLayer.DEFAULT_ROW_HEIGHT);
        table.setLayerPainter(gridPainter);

        //set table header painter
        final ICellPainter headerPainter = CellPainterFactory.createHeaderPainter(imageRegistry);
        config.registerConfigAttribute(
                CellConfigAttributes.CELL_PAINTER,
                headerPainter,
                DisplayMode.NORMAL,
                GridRegion.COLUMN_HEADER);

        //disable grid because header painter paints grid by itself to allow hovered and clicked grid colors
        config.registerConfigAttribute(
                CellConfigAttributes.RENDER_GRID_LINES,
                false,
                DisplayMode.NORMAL,
                GridRegion.COLUMN_HEADER);

        //set cell painter
        final ICellPainter cellPainter = CellPainterFactory.createCellPainter(columnModel, imageRegistry);
        config.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, cellPainter, DisplayMode.NORMAL, GridRegion.BODY);

        //do not render dotted line for selected cell because this is ugly and selected cell can be determined by selected background
        config.registerConfigAttribute(
                SelectionConfigAttributes.SELECTION_GRID_LINE_STYLE,
                new BorderStyle(1, gridColor, LineStyleEnum.SOLID),
                DisplayMode.SELECT);

    }

    public static void registerUiBindingsToNatTable(
        final NatTable table,
        final HoveredColumnConfigLabelAccumulator hoveredColumnLabelAccumulator) {

        final UiBindingRegistry uiBindingRegistry = table.getUiBindingRegistry();

        //avoid selection removed on reorder, use swt win style reorder overlay color and
        //do not allow to draw overlay column outside the header
        uiBindingRegistry.unregisterMouseDragMode(MouseEventMatcher.columnHeaderLeftClick(SWT.NONE));
        uiBindingRegistry.registerMouseDragMode(
                MouseEventMatcher.columnHeaderLeftClick(SWT.NONE),
                new AggregateDragMode(
                    new JoColumnReorderCellDragMode(),
                    new JoColumnReorderDragMode(hoveredColumnLabelAccumulator)));

        //do resize immediate when user changes column width
        uiBindingRegistry.registerFirstMouseDragMode(
                new ColumnResizeEventMatcher(SWT.NONE, GridRegion.COLUMN_HEADER, 1),
                new ResizeImediateDragMode());
    }

}
