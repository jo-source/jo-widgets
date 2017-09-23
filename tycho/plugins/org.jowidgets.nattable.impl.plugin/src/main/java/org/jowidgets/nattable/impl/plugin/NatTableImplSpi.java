/*
 * Copyright (c) 2016, grossmann
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.command.VisualRefreshCommand;
import org.eclipse.nebula.widgets.nattable.coordinate.Range;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayerListener;
import org.eclipse.nebula.widgets.nattable.layer.LabelStack;
import org.eclipse.nebula.widgets.nattable.layer.cell.AggregateConfigLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.event.ColumnDeleteEvent;
import org.eclipse.nebula.widgets.nattable.layer.event.ColumnInsertEvent;
import org.eclipse.nebula.widgets.nattable.layer.event.ILayerEvent;
import org.eclipse.nebula.widgets.nattable.painter.layer.NatGridLayerPainter;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.reorder.event.ColumnReorderEvent;
import org.eclipse.nebula.widgets.nattable.resize.command.InitializeAutoResizeColumnsCommand;
import org.eclipse.nebula.widgets.nattable.resize.event.ColumnResizeEvent;
import org.eclipse.nebula.widgets.nattable.selection.IRowSelectionModel;
import org.eclipse.nebula.widgets.nattable.selection.event.CellSelectionEvent;
import org.eclipse.nebula.widgets.nattable.ui.action.IMouseAction;
import org.eclipse.nebula.widgets.nattable.ui.matcher.IMouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.util.GCFactory;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelListener;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.model.ITableColumnModelSpi;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.model.ITableDataModelListener;
import org.jowidgets.common.model.ITableDataModelObservable;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.MouseButton;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.TablePackPolicy;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.controller.ITableCellListener;
import org.jowidgets.common.widgets.controller.ITableCellMouseEvent;
import org.jowidgets.common.widgets.controller.ITableCellPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableColumnListener;
import org.jowidgets.common.widgets.controller.ITableColumnPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableSelectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.editor.ITableCellEditor;
import org.jowidgets.common.widgets.editor.ITableCellEditorFactory;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.nattable.impl.plugin.configuration.JoNatTableConfigurator;
import org.jowidgets.nattable.impl.plugin.layer.JoColumnReorderLayer;
import org.jowidgets.nattable.impl.plugin.layer.NatTableLayers;
import org.jowidgets.nattable.impl.plugin.listener.CellSelectionListener;
import org.jowidgets.nattable.impl.plugin.painter.ClickedColumnConfigLabelAccumulator;
import org.jowidgets.nattable.impl.plugin.painter.HoveredColumnConfigLabelAccumulator;
import org.jowidgets.spi.dnd.IDragSourceSpi;
import org.jowidgets.spi.impl.controller.TableCellMouseEvent;
import org.jowidgets.spi.impl.controller.TableCellObservable;
import org.jowidgets.spi.impl.controller.TableCellPopupDetectionObservable;
import org.jowidgets.spi.impl.controller.TableCellPopupEvent;
import org.jowidgets.spi.impl.controller.TableColumnMouseEvent;
import org.jowidgets.spi.impl.controller.TableColumnObservable;
import org.jowidgets.spi.impl.controller.TableColumnPopupDetectionObservable;
import org.jowidgets.spi.impl.controller.TableColumnPopupEvent;
import org.jowidgets.spi.impl.controller.TableColumnResizeEvent;
import org.jowidgets.spi.impl.controller.TableSelectionObservable;
import org.jowidgets.spi.impl.swt.common.dnd.SwtDragSource;
import org.jowidgets.spi.impl.swt.common.image.SwtImageRegistry;
import org.jowidgets.spi.impl.swt.common.util.MouseUtil;
import org.jowidgets.spi.impl.swt.common.widgets.SwtControl;
import org.jowidgets.spi.widgets.ITableSpi;
import org.jowidgets.spi.widgets.setup.ITableSetupSpi;
import org.jowidgets.util.EmptyCheck;
import org.jowidgets.util.Interval;
import org.jowidgets.util.NullCompatibleEquivalence;

class NatTableImplSpi extends SwtControl implements ITableSpi {

    private final NatTable table;
    private final NatTableLayers tableLayers;
    private final IRowSelectionModel<Integer> rowSelectionModel;
    private final ITableDataModel dataModel;
    private final ITableColumnModelSpi columnModel;
    private final IGenericWidgetFactory widgetFactory;

    private final IDragSourceSpi dragSource;

    private final TableCellObservable tableCellObservable;
    private final TableCellPopupDetectionObservable tableCellPopupDetectionObservable;
    private final TableColumnPopupDetectionObservable tableColumnPopupDetectionObservable;
    private final TableColumnObservable tableColumnObservable;
    private final TableSelectionObservable tableSelectionObservable;

    private final TableModelListener tableModelListener;
    private final TableColumnModelListener tableColumnModelListener;
    private final ColumnLayerListener columnLayerListener;
    private final TableSelectionListener tableSelectionListener;

    private final HoveredColumnConfigLabelAccumulator hoveredColumnLabelAccumulator;
    private final ClickedColumnConfigLabelAccumulator clickedColumnLabelAccumulator;

    //ignore and use later for cell editing support (finals) -> begin
    @SuppressWarnings("unused")
    private final ITableCellEditorFactory<? extends ITableCellEditor> editorFactory;
    @SuppressWarnings("unused")
    private final ICustomWidgetFactory editorCustomWidgetFactory;
    // <-end ignore and use later for cell editing support

    //ignore and use later for cell editing support (mutables) -> begin
    @SuppressWarnings("unused")
    private boolean editable;
    @SuppressWarnings("unused")
    private ITableCellEditor tableCellEditor;
    @SuppressWarnings("unused")
    private ITableCell editTableCell;
    @SuppressWarnings("unused")
    private final int editRowIndex;
    @SuppressWarnings("unused")
    private final int editColumnIndex;
    @SuppressWarnings("unused")
    private long stopEditTimestamp;
    // <-end ignore and use later for cell editing support

    private boolean setWidthInvokedOnModel;
    private ArrayList<Integer> lastSelection;

    NatTableImplSpi(
        final NatTableLayers tableLayers,
        final IGenericWidgetFactory widgetFactory,
        final Object parentUiReference,
        final ITableSetupSpi setup,
        final SwtImageRegistry imageRegistry) {
        super(new NatTable((Composite) parentUiReference, getStyle(setup), tableLayers.getGridLayer()), imageRegistry);

        this.table = getUiReference();
        this.tableLayers = tableLayers;
        this.rowSelectionModel = tableLayers.getSelectionModel();
        this.widgetFactory = widgetFactory;

        this.tableCellObservable = new TableCellObservable();
        this.tableCellPopupDetectionObservable = new TableCellPopupDetectionObservable();
        this.tableColumnPopupDetectionObservable = new TableColumnPopupDetectionObservable();
        this.tableColumnObservable = new TableColumnObservable();
        this.tableSelectionObservable = new TableSelectionObservable();

        this.tableModelListener = new TableModelListener();
        this.tableColumnModelListener = new TableColumnModelListener();
        this.columnLayerListener = new ColumnLayerListener();
        this.tableSelectionListener = new TableSelectionListener();

        this.hoveredColumnLabelAccumulator = new HoveredColumnConfigLabelAccumulator();
        this.clickedColumnLabelAccumulator = new ClickedColumnConfigLabelAccumulator();
        this.editorCustomWidgetFactory = new EditorCustomWidgetFactory();

        this.dataModel = setup.getDataModel();
        this.columnModel = setup.getColumnModel();
        this.editorFactory = setup.getEditor();

        this.editable = true;
        this.editRowIndex = -1;
        this.editColumnIndex = -1;

        final SwtDragSource swtDragSource = (SwtDragSource) super.getDragSource();
        this.dragSource = new NatTableDragSource(swtDragSource, this);

        setMenuDetectListener(new TableMenuDetectListener());

        table.addMouseListener(new CellSelectionListener(table, swtDragSource, rowSelectionModel));
        table.addMouseListener(new TableCellMouseListener());
        table.addMouseMoveListener(new HeaderHoverMouseMoveListener());
        table.addMouseTrackListener(new HeaderHoverAndClickMouseTrackAdapter());
        table.addMouseListener(new HeaderMouseClickModeMouseListener());
        table.getUiBindingRegistry().registerSingleClickBinding(new TableHeaderMouseEventMatcher(), new TableHeaderClickAction());
        table.addDisposeListener(new NatTableDisposeListener());

        tableLayers.getSelectionLayer().addLayerListener(tableSelectionListener);
        tableLayers.getColumnReorderLayer().addLayerListener(columnLayerListener);

        final AggregateConfigLabelAccumulator columnLabelAccumulator = new AggregateConfigLabelAccumulator();
        columnLabelAccumulator.add(hoveredColumnLabelAccumulator, clickedColumnLabelAccumulator);
        tableLayers.getColumnHeaderLayer().setConfigLabelAccumulator(columnLabelAccumulator);

        JoNatTableConfigurator.configureNatTable(table, setup.getColumnModel(), imageRegistry);
        JoNatTableConfigurator.registerUiBindingsToNatTable(table, hoveredColumnLabelAccumulator);

        final NatTableTooltip tooltip = new NatTableTooltip(
            table,
            dataModel,
            columnModel,
            GridRegion.COLUMN_HEADER,
            GridRegion.BODY);

        table.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(final DisposeEvent e) {
                tooltip.dispose();
            }
        });

    }

    private static int getStyle(final ITableSetupSpi setup) {
        int result = SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE | SWT.DOUBLE_BUFFERED | SWT.V_SCROLL | SWT.H_SCROLL;
        if (setup.hasBorder()) {
            result = result | SWT.BORDER;
        }
        return result;
    }

    @Override
    public NatTable getUiReference() {
        return (NatTable) super.getUiReference();
    }

    @Override
    public IDragSourceSpi getDragSource() {
        return dragSource;
    }

    @Override
    public void addTableCellListener(final ITableCellListener listener) {
        tableCellObservable.addTableCellListener(listener);
    }

    @Override
    public void removeTableCellListener(final ITableCellListener listener) {
        tableCellObservable.removeTableCellListener(listener);
    }

    @Override
    public void addTableCellPopupDetectionListener(final ITableCellPopupDetectionListener listener) {
        tableCellPopupDetectionObservable.addTableCellPopupDetectionListener(listener);
    }

    @Override
    public void removeTableCellPopupDetectionListener(final ITableCellPopupDetectionListener listener) {
        tableCellPopupDetectionObservable.removeTableCellPopupDetectionListener(listener);
    }

    @Override
    public void addTableColumnPopupDetectionListener(final ITableColumnPopupDetectionListener listener) {
        tableColumnPopupDetectionObservable.addTableColumnPopupDetectionListener(listener);
    }

    @Override
    public void removeTableColumnPopupDetectionListener(final ITableColumnPopupDetectionListener listener) {
        tableColumnPopupDetectionObservable.addTableColumnPopupDetectionListener(listener);
    }

    @Override
    public void addTableColumnListener(final ITableColumnListener listener) {
        tableColumnObservable.addTableColumnListener(listener);
    }

    @Override
    public void removeTableColumnListener(final ITableColumnListener listener) {
        tableColumnObservable.removeTableColumnListener(listener);
    }

    @Override
    public void addTableSelectionListener(final ITableSelectionListener listener) {
        tableSelectionObservable.addTableSelectionListener(listener);
    }

    @Override
    public void removeTableSelectionListener(final ITableSelectionListener listener) {
        tableSelectionObservable.removeTableSelectionListener(listener);
    }

    @Override
    public void setRowHeight(final int height) {
        tableLayers.getDataLayer().setDefaultRowHeight(height);
        final NatGridLayerPainter gridPainter = (NatGridLayerPainter) table.getLayerPainter();
        gridPainter.setDefaultRowHeight(height);
        table.refresh(false);
    }

    @Override
    public void setEditable(final boolean editable) {
        this.editable = editable;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        if (isEditing() && !enabled) {
            stopEditing();
        }
        super.setEnabled(enabled);
    }

    @Override
    public Dimension getMinSize() {
        return new Dimension(40, 40);
    }

    @Override
    public void resetFromModel() {
        final ITableDataModelObservable dataModelObservable = dataModel.getTableDataModelObservable();
        if (dataModelObservable != null) {
            dataModelObservable.removeDataModelListener(tableModelListener);
        }
        final ITableColumnModelObservable columnModelObservable = columnModel.getTableColumnModelObservable();
        if (columnModelObservable != null) {
            columnModelObservable.removeColumnModelListener(tableColumnModelListener);
        }

        setColumnWidthFromModel();
        setSelection(dataModel.getSelection());

        if (dataModelObservable != null) {
            dataModelObservable.addDataModelListener(tableModelListener);
        }
        if (columnModelObservable != null) {
            columnModelObservable.addColumnModelListener(tableColumnModelListener);
        }

        table.refresh(false);
    }

    private void setColumnWidthFromModel() {
        for (int columnIndex = 0; columnIndex < columnModel.getColumnCount(); columnIndex++) {
            setColumnWidthFromModel(columnIndex);
        }
    }

    private void setColumnWidthFromModel(final int columnIndex) {
        final DataLayer dataLayer = tableLayers.getDataLayer();
        final int width = columnModel.getColumn(columnIndex).getWidth();
        if (width >= 0) {
            //the data layer is not reordered so the index must be used here instead 
            //of position (index and position are the same in this layer)
            if (width != dataLayer.getColumnWidthByPosition(columnIndex)) {
                dataLayer.setColumnWidthByPosition(columnIndex, width);
            }
        }
    }

    @Override
    public Position getCellPosition(final int rowIndex, final int columnIndex) {
        final Rectangle rectangle = table.getBoundsByPosition(columnIndex, rowIndex);
        if (rectangle != null) {
            return new Position(rectangle.x, rectangle.y);
        }
        else {
            return null;
        }
    }

    @Override
    public Dimension getCellSize(final int rowIndex, final int columnIndex) {
        final Rectangle rectangle = table.getBoundsByPosition(columnIndex, rowIndex);
        if (rectangle != null) {
            return new Dimension(rectangle.width, rectangle.height);
        }
        else {
            return null;
        }
    }

    @Override
    public int getColumnAtPosition(final Position position) {
        final Point point = new Point(position.getX(), position.getY());
        final int columnPositionByX = table.getColumnPositionByX(point.x);
        final int columnIndex = table.getColumnIndexByPosition(columnPositionByX);
        if (columnIndex >= 0) {
            return columnIndex;
        }
        else {
            return -1;
        }
    }

    @Override
    public int getRowAtPosition(final Position position) {
        final Point point = new Point(position.getX(), position.getY());
        final int rowPositionByY = table.getRowPositionByY(point.y);
        if (rowPositionByY <= 0) {
            return -1;
        }
        else {
            return table.getRowIndexByPosition(rowPositionByY);
        }
    }

    @Override
    public void pack(final TablePackPolicy policy) {
        //TODO consider TablePackPolicy
        for (int columnPosition = 0; columnPosition < table.getColumnCount(); columnPosition++) {
            pack(columnPosition);
        }
    }

    @Override
    public void pack(final int columnIndex, final TablePackPolicy policy) {
        //TODO consider TablePackPolicy
        pack(tableLayers.getColumnReorderLayer().getColumnPositionByIndex(columnIndex));
    }

    private void pack(final int columnPosition) {
        final InitializeAutoResizeColumnsCommand command = new InitializeAutoResizeColumnsCommand(
            table,
            columnPosition,
            table.getConfigRegistry(),
            new GCFactory(table));
        table.doCommand(command);
    }

    @Override
    public ArrayList<Integer> getColumnPermutation() {
        final ColumnReorderLayer reorderLayer = tableLayers.getColumnReorderLayer();
        final ArrayList<Integer> result = new ArrayList<Integer>();
        for (int position = 0; position < reorderLayer.getColumnCount(); position++) {
            result.add(Integer.valueOf(reorderLayer.getColumnIndexByPosition(position)));
        }
        return result;
    }

    @Override
    public void setColumnPermutation(final List<Integer> permutation) {
        final JoColumnReorderLayer reorderLayer = tableLayers.getColumnReorderLayer();
        reorderLayer.setColumnIndexOrder(permutation);
    }

    @Override
    public ArrayList<Integer> getSelection() {
        if (!EmptyCheck.isEmpty(lastSelection)) {
            return lastSelection;
        }
        else {
            return new ArrayList<Integer>();
        }
    }

    @Override
    public void setSelection(final List<Integer> selection) {
        if (!NullCompatibleEquivalence.equals(selection, lastSelection)) {
            tableLayers.getSelectionLayer().removeLayerListener(tableSelectionListener);
            if (selection != null) {
                lastSelection = new ArrayList<Integer>(selection);
                rowSelectionModel.clearSelection();
                for (final Integer selected : selection) {
                    rowSelectionModel.addSelection(0, selected.intValue());
                }
            }
            else {
                lastSelection = new ArrayList<Integer>();
                rowSelectionModel.clearSelection();
            }
            tableSelectionObservable.fireSelectionChanged();
            tableLayers.getSelectionLayer().addLayerListener(tableSelectionListener);
            table.refresh(false);
        }
    }

    @Override
    public void scrollToRow(final int rowIndex) {
        final ViewportLayer viewPort = tableLayers.getViewPortLayer();
        viewPort.moveRowPositionIntoViewport(rowIndex);
    }

    @Override
    public boolean isColumnPopupDetectionSupported() {
        return true;
    }

    @Override
    public Interval<Integer> getVisibleRows() {
        final ViewportLayer viewPort = tableLayers.getViewPortLayer();
        final int originY = viewPort.getOrigin().getY();
        final int firstRow = viewPort.getRowPositionByY(originY);
        final int lastRow = viewPort.getRowPositionByY(originY + viewPort.getClientAreaHeight());
        return new Interval<Integer>(getWrapperInteger(firstRow), getWrapperInteger(lastRow));
    }

    private Integer getWrapperInteger(final int value) {
        if (value != -1) {
            return Integer.valueOf(value);
        }
        else {
            return null;
        }
    }

    private CellIndices getExternalCellIndices(final Point point) {
        final int rowPositionByY = table.getRowPositionByY(point.y);
        if (rowPositionByY <= 0) {
            return null;
        }
        final int columnPositionByX = table.getColumnPositionByX(point.x);

        final int columnIndex = table.getColumnIndexByPosition(columnPositionByX);
        final int rowIndex = table.getRowIndexByPosition(rowPositionByY);

        if (columnIndex >= 0 && rowIndex >= 0) {
            return new CellIndices(rowIndex, columnIndex);
        }
        else {
            return null;
        }
    }

    private void setSelectionChangedIfNeccessary() {
        if (!NullCompatibleEquivalence.equals(lastSelection, rowSelectionModel.getSelectedRowObjects())) {
            lastSelection = new ArrayList<Integer>(rowSelectionModel.getSelectedRowObjects());
            tableSelectionObservable.fireSelectionChanged();
        }
    }

    @Override
    public boolean editCell(final int row, final int column) {
        //TODO must be implemented
        return false;
    }

    @Override
    public void stopEditing() {
        //TODO must be implemented
    }

    @Override
    public void cancelEditing() {
        //TODO must be implemented
    }

    @Override
    public boolean isEditing() {
        //TODO must be implemented
        return false;
    }

    private final class EditorCustomWidgetFactory implements ICustomWidgetFactory {
        @Override
        public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE create(
            final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
            return widgetFactory.create(table, descriptor);
        }
    }

    private final class HeaderHoverMouseMoveListener implements MouseMoveListener {

        @Override
        public void mouseMove(final MouseEvent event) {
            final int col = table.getColumnPositionByX(event.x);
            final int row = table.getRowPositionByY(event.y);
            final boolean button1Pressed = (event.stateMask & SWT.BUTTON1) != 0;

            final boolean hoverChanged;
            if (row == 0 && col >= 0 && !button1Pressed) {//do not show hovered state when resizing or clicking
                hoverChanged = hoveredColumnLabelAccumulator.setColumnIndex(col);
            }
            else if (!button1Pressed) {
                hoverChanged = hoveredColumnLabelAccumulator.clearColumnIndex();
            }
            else {
                hoverChanged = false;
            }

            if (hoverChanged) {
                tableLayers.getColumnHeaderLayer().doCommand(new VisualRefreshCommand());
            }
        }
    }

    private final class HeaderMouseClickModeMouseListener extends MouseAdapter {

        @Override
        public void mouseUp(final MouseEvent e) {
            final boolean changed = clickedColumnLabelAccumulator.clearColumnIndex();
            if (changed) {
                tableLayers.getColumnHeaderLayer().doCommand(new VisualRefreshCommand());
            }
        }

        @Override
        public void mouseDown(final MouseEvent event) {
            final int col = table.getColumnPositionByX(event.x);
            final int row = table.getRowPositionByY(event.y);

            final boolean changed;
            if (row == 0 && col >= 0 && event.button == 1 && table.getCursor() == null) {
                //Hack description for: "table.getCursor() == null" 
                //->do not set click mode if resize cursor is shown
                changed = clickedColumnLabelAccumulator.setColumnIndex(col);
            }
            else {
                changed = clickedColumnLabelAccumulator.clearColumnIndex();
            }

            if (changed) {
                tableLayers.getColumnHeaderLayer().doCommand(new VisualRefreshCommand());
            }
        }

    }

    private final class HeaderHoverAndClickMouseTrackAdapter extends MouseTrackAdapter {
        @Override
        public void mouseExit(final MouseEvent e) {
            final boolean hoverChanged = hoveredColumnLabelAccumulator.clearColumnIndex();
            final boolean clickChanged = clickedColumnLabelAccumulator.clearColumnIndex();
            if (hoverChanged || clickChanged) {
                tableLayers.getColumnHeaderLayer().doCommand(new VisualRefreshCommand());
            }
        }
    }

    private final class TableCellMouseListener extends MouseAdapter {
        @Override
        public void mouseUp(final MouseEvent e) {
            final ITableCellMouseEvent mouseEvent = getMouseEvent(e, 1);
            if (mouseEvent != null) {
                tableCellObservable.fireMouseReleased(mouseEvent);
            }
        }

        @Override
        public void mouseDown(final MouseEvent e) {
            final ITableCellMouseEvent mouseEvent = getMouseEvent(e, 1);
            if (mouseEvent != null) {
                tableCellObservable.fireMousePressed(mouseEvent);
            }
        }

        @Override
        public void mouseDoubleClick(final MouseEvent e) {
            final ITableCellMouseEvent mouseEvent = getMouseEvent(e, 2);
            if (mouseEvent != null) {
                tableCellObservable.fireMouseDoubleClicked(mouseEvent);
            }
        }

        private ITableCellMouseEvent getMouseEvent(final MouseEvent event, final int maxCount) {
            try {
                if (event.count > maxCount) {
                    return null;
                }
            }
            catch (final NoSuchFieldError e) {
                //RWT doesn't support count field :-( 
                //so the mouse down and mouse up may be fired twice at double clicks :-(
            }
            final MouseButton mouseButton = MouseUtil.getMouseButton(event);
            if (mouseButton == null) {
                return null;
            }
            final Point point = new Point(event.x, event.y);
            final CellIndices indices = getExternalCellIndices(point);
            if (indices != null) {
                return new TableCellMouseEvent(
                    indices.getRowIndex(),
                    indices.getColumnIndex(),
                    mouseButton,
                    MouseUtil.getModifier(event.stateMask));
            }
            return null;
        }
    }

    private final class TableHeaderClickAction implements IMouseAction {
        @Override
        public void run(final NatTable natTable, final MouseEvent e) {
            if (e.button == 1) {
                final int columnPositionByX = table.getColumnPositionByX(e.x);
                final int columnIndex = table.getColumnIndexByPosition(columnPositionByX);
                final Set<Modifier> modifier = MouseUtil.getModifier(e.stateMask);
                tableColumnObservable.fireMouseClicked(new TableColumnMouseEvent(columnIndex, modifier));
            }
        }
    }

    private final class TableHeaderMouseEventMatcher implements IMouseEventMatcher {
        @Override
        public boolean matches(final NatTable natTable, final MouseEvent event, final LabelStack regionLabels) {
            if (regionLabels != null && regionLabels.hasLabel(GridRegion.COLUMN_HEADER)) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    private final class TableMenuDetectListener implements MenuDetectListener {

        @Override
        public void menuDetected(final MenuDetectEvent e) {

            //if a drag source is installed, selection event fires after menu detect but selection
            //should always change before menu detection
            //Not sure if this is necessary for nattable but it does not harm anyway
            setSelectionChangedIfNeccessary();

            //stop editing before popup opens
            stopEditing();

            Point point = new Point(e.x, e.y);
            point = table.toControl(point);

            final Position position = new Position(point.x, point.y);

            final int rowPositionByY = table.getRowPositionByY(point.y);
            final int columnPositionByX = table.getColumnPositionByX(point.x);

            final int rowIndex = table.getRowIndexByPosition(rowPositionByY);
            final int columnIndex = table.getColumnIndexByPosition(columnPositionByX);

            if (rowPositionByY == 0 && columnIndex != -1) {
                tableColumnPopupDetectionObservable.firePopupDetected(new TableColumnPopupEvent(columnIndex, position));
            }
            else if (rowIndex != -1 && columnIndex != -1) {
                tableCellPopupDetectionObservable.firePopupDetected(new TableCellPopupEvent(rowIndex, columnIndex, position));
            }
            else {
                getPopupDetectionObservable().firePopupDetected(position);
            }
        }

    }

    private final class TableSelectionListener implements ILayerListener {
        @Override
        public void handleLayerEvent(final ILayerEvent event) {
            if (event instanceof CellSelectionEvent) {
                setSelectionChangedIfNeccessary();
            }
        }
    }

    private final class TableModelListener implements ITableDataModelListener {

        @Override
        public void rowsAdded(final int[] rowIndices) {
            updateRows(rowIndices);
        }

        @Override
        public void rowsRemoved(final int[] rowIndices) {
            updateRows(rowIndices);
        }

        @Override
        public void rowsChanged(final int[] rowIndices) {
            updateRows(rowIndices);
        }

        @Override
        public void dataChanged() {
            table.refresh(false);
        }

        @Override
        public void selectionChanged() {
            setSelection(dataModel.getSelection());
        }

        private void updateRows(final int[] rowIndices) {
            table.refresh(false);
        }

    }

    private final class TableColumnModelListener implements ITableColumnModelListener {

        @Override
        public void columnsAdded(final int[] columnIndices) {
            stopEditing();

            final JoColumnReorderLayer reorderLayer = tableLayers.getColumnReorderLayer();
            reorderLayer.handleLayerEvent(new ColumnInsertEvent(reorderLayer, toRanges(columnIndices)));

            table.refresh(false);
        }

        @Override
        public void columnsRemoved(final int[] columnIndices) {
            stopEditing();

            final JoColumnReorderLayer reorderLayer = tableLayers.getColumnReorderLayer();
            reorderLayer.handleLayerEvent(new ColumnDeleteEvent(reorderLayer, toRanges(columnIndices)));

            table.refresh(false);
        }

        private Range[] toRanges(final int[] columnIndices) {
            final Range[] ranges = new Range[columnIndices.length];
            for (int i = 0; i < columnIndices.length; i++) {
                final int columnIndex = columnIndices[i];
                ranges[i] = new Range(columnIndex, columnIndex + 1);
            }
            return ranges;
        }

        @Override
        public void columnsChanged(final int[] columnIndices) {
            if (!setWidthInvokedOnModel) {
                stopEditing();
                for (int i = 0; i < columnIndices.length; i++) {
                    setColumnWidthFromModel(columnIndices[i]);
                }
                table.refresh(false);
            }
        }
    }

    private final class ColumnLayerListener implements ILayerListener {

        @Override
        public void handleLayerEvent(final ILayerEvent event) {
            if (event instanceof ColumnReorderEvent) {
                tableColumnObservable.fireColumnPermutationChanged();
            }
            else if (event instanceof ColumnResizeEvent) {
                final ColumnReorderLayer reorderLayer = tableLayers.getColumnReorderLayer();

                final int columnPosition = ((ColumnResizeEvent) event).getColumnPositionRanges().iterator().next().start;
                final int columnIndex = reorderLayer.getColumnIndexByPosition(columnPosition);
                final int width = reorderLayer.getColumnWidthByPosition(columnPosition);

                setWidthInvokedOnModel = true;
                columnModel.getColumn(columnIndex).setWidth(width);
                setWidthInvokedOnModel = false;

                final TableColumnResizeEvent joResizeEvent = new TableColumnResizeEvent(columnIndex, width);
                tableColumnObservable.fireColumnResized(joResizeEvent);
            }
        }
    }

    private final class CellIndices {

        private final int rowIndex;
        private final int columnIndex;

        private CellIndices(final int rowIndex, final int columnIndex) {
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
        }

        private int getRowIndex() {
            return rowIndex;
        }

        private int getColumnIndex() {
            return columnIndex;
        }

        @Override
        public String toString() {
            return "CellIndices [rowIndex=" + rowIndex + ", columnIndex=" + columnIndex + "]";
        }

    }

    private final class NatTableDisposeListener implements DisposeListener {
        @Override
        public void widgetDisposed(final DisposeEvent event) {
            final ITableDataModelObservable dataModelObservable = dataModel.getTableDataModelObservable();
            if (dataModelObservable != null) {
                dataModelObservable.removeDataModelListener(tableModelListener);
            }
            final ITableColumnModelObservable columnModelObservable = columnModel.getTableColumnModelObservable();
            if (columnModelObservable != null) {
                columnModelObservable.removeColumnModelListener(tableColumnModelListener);
            }
        }
    }

}
