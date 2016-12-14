/*
 * Copyright (c) 2011, grossmann, Nikolaus Moll
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
import org.eclipse.nebula.widgets.nattable.layer.ILayerListener;
import org.eclipse.nebula.widgets.nattable.layer.event.ILayerEvent;
import org.eclipse.nebula.widgets.nattable.selection.IRowSelectionModel;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.style.theme.ModernNatTableThemeConfiguration;
import org.eclipse.nebula.widgets.nattable.style.theme.ThemeConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolTip;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelListener;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.model.ITableColumnModelSpi;
import org.jowidgets.common.model.ITableColumnSpi;
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
import org.jowidgets.nattable.impl.plugin.layer.NatTableLayers;
import org.jowidgets.spi.impl.controller.TableCellMouseEvent;
import org.jowidgets.spi.impl.controller.TableCellObservable;
import org.jowidgets.spi.impl.controller.TableCellPopupDetectionObservable;
import org.jowidgets.spi.impl.controller.TableCellPopupEvent;
import org.jowidgets.spi.impl.controller.TableColumnMouseEvent;
import org.jowidgets.spi.impl.controller.TableColumnObservable;
import org.jowidgets.spi.impl.controller.TableColumnPopupDetectionObservable;
import org.jowidgets.spi.impl.controller.TableColumnPopupEvent;
import org.jowidgets.spi.impl.controller.TableSelectionObservable;
import org.jowidgets.spi.impl.swt.common.image.SwtImageRegistry;
import org.jowidgets.spi.impl.swt.common.options.SwtOptions;
import org.jowidgets.spi.impl.swt.common.util.MouseUtil;
import org.jowidgets.spi.impl.swt.common.widgets.SwtControl;
import org.jowidgets.spi.widgets.ITableSpi;
import org.jowidgets.spi.widgets.setup.ITableSetupSpi;
import org.jowidgets.util.EmptyCheck;
import org.jowidgets.util.Interval;
import org.jowidgets.util.NullCompatibleEquivalence;

@SuppressWarnings(value = {"all"})
public class NatTableImplSpi extends SwtControl implements ITableSpi {

    private final SwtImageRegistry imageRegistry;

    private final NatTable table;
    private final ITableDataModel dataModel;
    private final ITableColumnModelSpi columnModel;
    private final IGenericWidgetFactory factory;
    private final ICustomWidgetFactory editorCustomWidgetFactory;

    private final TableCellObservable tableCellObservable;
    private final TableCellPopupDetectionObservable tableCellPopupDetectionObservable;
    private final TableColumnPopupDetectionObservable tableColumnPopupDetectionObservable;
    private final TableColumnObservable tableColumnObservable;
    private final TableSelectionObservable tableSelectionObservable;

    private final TableModelListener tableModelListener;
    private final TableColumnModelListener tableColumnModelListener;
    private final ITableCellEditorFactory<? extends ITableCellEditor> editorFactory;

    private final boolean columnsMoveable;
    private final boolean columnsResizeable;

    private final IColorConstant selectedForegroundColor;
    private final IColorConstant selectedBackgroundColor;

    private final SelectionLayer selectionLayer;
    private final IRowSelectionModel<Integer> rowSelectionModel;

    private int[] lastColumnOrder;
    private ToolTip toolTip;
    private boolean editable;
    private TableEditor editor;
    private ITableCellEditor tableCellEditor;
    private ITableCell editTableCell;
    private final int editRowIndex;
    private final int editColumnIndex;
    private long stopEditTimestamp;

    private Integer rowHeight;

    private ArrayList<Integer> lastSelection;

    public NatTableImplSpi(
        final NatTableLayers natTableLayers,
        final IGenericWidgetFactory factory,
        final Object parentUiReference,
        final ITableSetupSpi setup,
        final SwtImageRegistry imageRegistry) {
        super(new NatTable((Composite) parentUiReference, getStyle(setup), natTableLayers.getGridLayer()), imageRegistry);

        this.table = getUiReference();

        final ThemeConfiguration modernTheme = new ModernNatTableThemeConfiguration();
        table.setTheme(modernTheme);

        this.rowSelectionModel = natTableLayers.getSelectionModel();
        this.selectionLayer = natTableLayers.getSelectionLayer();

        this.imageRegistry = imageRegistry;

        this.factory = factory;
        this.editorCustomWidgetFactory = new EditorCustomWidgetFactory();

        this.selectedForegroundColor = SwtOptions.getTableSelectedForegroundColor();
        this.selectedBackgroundColor = SwtOptions.getTableSelectedBackgroundColor();

        this.editable = true;

        this.tableCellObservable = new TableCellObservable();
        this.tableCellPopupDetectionObservable = new TableCellPopupDetectionObservable();
        this.tableColumnPopupDetectionObservable = new TableColumnPopupDetectionObservable();
        this.tableColumnObservable = new TableColumnObservable();
        this.tableSelectionObservable = new TableSelectionObservable();
        this.tableModelListener = new TableModelListener();
        this.tableColumnModelListener = new TableColumnModelListener();

        this.dataModel = setup.getDataModel();
        this.columnModel = setup.getColumnModel();

        this.columnsMoveable = setup.getColumnsMoveable();
        this.columnsResizeable = setup.getColumnsResizeable();

        this.editorFactory = setup.getEditor();

        this.editRowIndex = -1;
        this.editColumnIndex = -1;

        selectionLayer.addLayerListener(new TableSelectionListener());

        table.addMouseListener(new TableCellListener());
        table.addMouseListener(new TableHeaderClickListener());
        setMenuDetectListener(new TableMenuDetectListener());

        // ToolTip support
        try {
            this.toolTip = new ToolTip(table.getShell(), SWT.NONE);
        }
        catch (final NoClassDefFoundError error) {
            //TODO MG rwt has no tooltip, may use a window instead. 
            //(New rwt version supports tooltips)
        }

        if (toolTip != null) {
            final ToolTipListener toolTipListener = new ToolTipListener();
            table.addListener(SWT.Dispose, toolTipListener);
            table.addListener(SWT.KeyDown, toolTipListener);
            table.addListener(SWT.MouseHover, toolTipListener);
            table.addListener(SWT.MouseMove, toolTipListener);
        }
        //
        table.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(final DisposeEvent arg0) {
                final ITableDataModelObservable dataModelObservable = dataModel.getTableDataModelObservable();
                if (dataModelObservable != null) {
                    dataModelObservable.removeDataModelListener(tableModelListener);
                }
                final ITableColumnModelObservable columnModelObservable = columnModel.getTableColumnModelObservable();
                if (columnModelObservable != null) {
                    columnModelObservable.removeColumnModelListener(tableColumnModelListener);
                }
            }
        });

    }

    @Override
    public NatTable getUiReference() {
        return (NatTable) super.getUiReference();
    }

    @Override
    public void setRowHeight(final int height) {
        //TODO row height must be considered
        this.rowHeight = Integer.valueOf(height);
        table.redraw();
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
        table.setRedraw(false);

        final ITableDataModelObservable dataModelObservable = dataModel.getTableDataModelObservable();
        if (dataModelObservable != null) {
            dataModelObservable.removeDataModelListener(tableModelListener);
        }

        final ITableColumnModelObservable columnModelObservable = columnModel.getTableColumnModelObservable();
        if (columnModelObservable != null) {
            columnModelObservable.removeColumnModelListener(tableColumnModelListener);
        }

        setSelection(dataModel.getSelection());

        if (dataModelObservable != null) {
            dataModelObservable.addDataModelListener(tableModelListener);
        }

        if (columnModelObservable != null) {
            columnModelObservable.addColumnModelListener(tableColumnModelListener);
        }

        table.setRedraw(true);
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
    public void pack(final TablePackPolicy policy) {
        table.pack();
    }

    @Override
    public void pack(final int columnIndex, final TablePackPolicy policy) {
        //TODO must be implemented
        //		table.setRedraw(false);
        //		packColumn(columnIndex, policy);
        //		table.setRedraw(true);
    }

    @Override
    public ArrayList<Integer> getColumnPermutation() {
        //TODO must be implemented or checked if necessary anymore
        //		final ArrayList<Integer> result = new ArrayList<Integer>();
        //		for (final int index : table.getColumnOrder()) {
        //			if (index == 0) {
        //				continue;
        //			}
        //			result.add(Integer.valueOf(index - 1));
        //		}
        final ArrayList<Integer> result = new ArrayList<Integer>();
        for (int index = 0; index < table.getColumnCount(); index++) {
            result.add(Integer.valueOf(index));
        }
        return result;
    }

    @Override
    public void setColumnPermutation(final List<Integer> permutation) {
        //TODO must be implemented or checked if necessary anymore
        //		if (!table.isDisposed()) {
        //			final int[] columnOrder = new int[permutation.size() + 1];
        //			columnOrder[0] = 0;
        //			int i = 1;
        //			for (final Integer permutatedIndex : permutation) {
        //				columnOrder[i] = permutatedIndex.intValue() + 1;
        //				i++;
        //			}
        //			table.setRedraw(false);
        //			table.setColumnOrder(columnOrder);
        //			table.setRedraw(true);
        //		}
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
        //CHECKSTYLE:OFF
        if (!NullCompatibleEquivalence.equals(selection, lastSelection)) {
            //TODO must be implemented
        }
        //CHECKSTYLE:ON
    }

    @Override
    public void scrollToRow(final int rowIndex) {
        //TODO must be implemented
    }

    @Override
    public boolean isColumnPopupDetectionSupported() {
        return true;
    }

    @Override
    public Interval<Integer> getVisibleRows() {
        final int rowCount = dataModel.getRowCount();
        //TODO must be implemented
        //		if (rowCount > 0) {
        //			final int leftBoundary = table.getTopIndex();
        //			final Dimension cellSize = getCellSize(leftBoundary, 0);
        //			final Rectangle clientArea = table.getClientArea();
        //			if (clientArea.height >= cellSize.getHeight()) {
        //				final int visibleRows = (clientArea.height / cellSize.getHeight());
        //				final int rigthBoundary = Math.min(rowCount - 1, leftBoundary + visibleRows);
        //				return new Interval<Integer>(leftBoundary, rigthBoundary);
        //			}
        //		}
        return new Interval<Integer>(0, rowCount - 1);
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

    private void showToolTip(final String message) {
        if (EmptyCheck.isEmpty(message)) {
            return;
        }
        toolTip.setVisible(false);
        toolTip.setMessage(message);
        final Point location = Display.getCurrent().getCursorLocation();
        toolTip.setLocation(location.x + 16, location.y + 16);
        toolTip.setVisible(true);
    }

    private void fireSelectionChangedIfNeccessary() {
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

    private int convertColumnIndexToView(final int modelIndex) {
        final ArrayList<Integer> permutation = getColumnPermutation();
        return permutation.indexOf(Integer.valueOf(modelIndex));
    }

    private int convertColumnIndexToModel(final int viewIndex) {
        return getColumnPermutation().get(viewIndex).intValue();
    }

    private static int getStyle(final ITableSetupSpi setup) {
        int result = SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE | SWT.DOUBLE_BUFFERED | SWT.V_SCROLL | SWT.H_SCROLL;

        if (setup.hasBorder()) {
            result = result | SWT.BORDER;
        }

        return result;
    }

    private final class EditorCustomWidgetFactory implements ICustomWidgetFactory {
        @Override
        public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE create(
            final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
            return factory.create(table, descriptor);
        }
    }

    private final class TableCellListener extends MouseAdapter {
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

    private final class TableHeaderClickListener extends MouseAdapter {
        @Override
        public void mouseUp(final MouseEvent e) {
            final int rowPositionByY = table.getRowPositionByY(e.y);
            if (rowPositionByY == 0) {
                final int columnPositionByX = table.getColumnPositionByX(e.x);
                final int columnIndex = table.getColumnIndexByPosition(columnPositionByX);
                final Set<Modifier> modifier = MouseUtil.getModifier(e.stateMask);
                tableColumnObservable.fireMouseClicked(new TableColumnMouseEvent(columnIndex, modifier));
            }
        }
    }

    private final class TableMenuDetectListener implements MenuDetectListener {

        @Override
        public void menuDetected(final MenuDetectEvent e) {

            //if a drag source is installed, selection event fires after menu detect but selection
            //should always change before menu detection
            //TODO proof if this is necessary for nattable
            fireSelectionChangedIfNeccessary();

            //stop editing before popup opens
            stopEditing();

            Point point = new Point(e.x, e.y);
            point = table.toControl(point);

            final Position position = new Position(point.x, point.y);

            final int rowPositionByY = table.getRowPositionByY(point.y);
            final int columnPositionByX = table.getColumnPositionByX(point.x);

            final int rowIndex = table.getRowIndexByPosition(rowPositionByY);
            final int columnIndex = table.getColumnIndexByPosition(columnPositionByX);

            if (rowPositionByY == 0) {
                tableColumnPopupDetectionObservable.firePopupDetected(new TableColumnPopupEvent(columnIndex, position));
            }
            else {
                tableCellPopupDetectionObservable.firePopupDetected(new TableCellPopupEvent(rowIndex, columnIndex, position));
            }
        }

    }

    private final class TableSelectionListener implements ILayerListener {

        @Override
        public void handleLayerEvent(final ILayerEvent event) {
            fireSelectionChangedIfNeccessary();
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
            table.refresh();
        }

        @Override
        public void selectionChanged() {
            setSelection(dataModel.getSelection());
        }

        private void updateRows(final int[] rowIndices) {
            table.refresh();
        }

    }

    private final class TableColumnModelListener implements ITableColumnModelListener {

        @Override
        public void columnsAdded(final int[] columnIndices) {
            stopEditing();
            table.refresh();
        }

        @Override
        public void columnsRemoved(final int[] columnIndices) {
            stopEditing();
            table.refresh();
        }

        @Override
        public void columnsChanged(final int[] columnIndices) {
            stopEditing();
            table.refresh();
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

    private final class ToolTipListener implements Listener {

        @Override
        public void handleEvent(final Event event) {
            if (event.type == SWT.MouseHover) {
                final int rowPositionByY = table.getRowPositionByY(event.y);
                final int columnPositionByX = table.getColumnPositionByX(event.x);

                final int rowIndex = table.getRowIndexByPosition(rowPositionByY);
                final int columnIndex = table.getColumnIndexByPosition(columnPositionByX);

                if (rowPositionByY == 0) {
                    showToolTip(getColumnToolTipText(columnIndex));
                }
                else {
                    showToolTip(getCellToolTipText(rowIndex, columnIndex));
                }
            }
            else {
                if (toolTip != null && !toolTip.isDisposed()) {
                    toolTip.setVisible(false);
                }
            }
        }

        private String getCellToolTipText(final int rowIndex, final int columnIndex) {
            final ITableCell cell = dataModel.getCell(rowIndex, columnIndex);
            if (cell != null) {
                return cell.getToolTipText();
            }
            else {
                return null;
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
    }

}
