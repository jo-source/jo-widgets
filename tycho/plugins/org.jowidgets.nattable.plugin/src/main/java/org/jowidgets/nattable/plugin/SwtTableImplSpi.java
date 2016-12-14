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

package org.jowidgets.nattable.plugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolTip;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelListener;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.model.ITableColumnModelSpi;
import org.jowidgets.common.model.ITableColumnSpi;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.model.ITableDataModelListener;
import org.jowidgets.common.model.ITableDataModelObservable;
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.MouseButton;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.TablePackPolicy;
import org.jowidgets.common.types.TableSelectionPolicy;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.controller.IKeyEvent;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.ITableCellListener;
import org.jowidgets.common.widgets.controller.ITableCellMouseEvent;
import org.jowidgets.common.widgets.controller.ITableCellPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableColumnListener;
import org.jowidgets.common.widgets.controller.ITableColumnPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableSelectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.editor.EditActivation;
import org.jowidgets.common.widgets.editor.ITableCellEditor;
import org.jowidgets.common.widgets.editor.ITableCellEditorFactory;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
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
import org.jowidgets.spi.impl.swt.common.color.ColorCache;
import org.jowidgets.spi.impl.swt.common.image.SwtImageRegistry;
import org.jowidgets.spi.impl.swt.common.options.SwtOptions;
import org.jowidgets.spi.impl.swt.common.util.AlignmentConvert;
import org.jowidgets.spi.impl.swt.common.util.FontProvider;
import org.jowidgets.spi.impl.swt.common.util.ListenerUtil;
import org.jowidgets.spi.impl.swt.common.util.MouseUtil;
import org.jowidgets.spi.impl.swt.common.widgets.SwtControl;
import org.jowidgets.spi.widgets.ITableSpi;
import org.jowidgets.spi.widgets.setup.ITableSetupSpi;
import org.jowidgets.util.ArrayUtils;
import org.jowidgets.util.Assert;
import org.jowidgets.util.EmptyCheck;
import org.jowidgets.util.Interval;
import org.jowidgets.util.NullCompatibleEquivalence;

public class SwtTableImplSpi extends SwtControl implements ITableSpi {

    private final SwtImageRegistry imageRegistry;

    private final Table table;
    private final ITableDataModel dataModel;
    private final ITableColumnModelSpi columnModel;
    private final IGenericWidgetFactory factory;
    private final ICustomWidgetFactory editorCustomWidgetFactory;

    private final TableCellObservable tableCellObservable;
    private final TableCellPopupDetectionObservable tableCellPopupDetectionObservable;
    private final TableColumnPopupDetectionObservable tableColumnPopupDetectionObservable;
    private final TableColumnObservable tableColumnObservable;
    private final TableSelectionObservable tableSelectionObservable;

    private final ColumnSelectionListener columnSelectionListener;
    private final ColumnControlListener columnControlListener;
    private final TableModelListener tableModelListener;
    private final TableColumnModelListener tableColumnModelListener;
    private final DataListener dataListener;
    private final EraseItemListener eraseItemListener;
    private final ITableCellEditorFactory<? extends ITableCellEditor> editorFactory;

    private final boolean columnsMoveable;
    private final boolean columnsResizeable;

    private final IColorConstant selectedForegroundColor;
    private final IColorConstant selectedBackgroundColor;

    private Method setFocusIndexMethod;

    private int[] lastColumnOrder;
    private boolean setWidthInvokedOnModel;
    private ToolTip toolTip;
    private boolean editable;
    private TableEditor editor;
    private ITableCellEditor tableCellEditor;
    private ITableCell editTableCell;
    private int editRowIndex;
    private int editColumnIndex;
    private long stopEditTimestamp;

    private Integer rowHeight;

    private int[] lastSelection;

    public SwtTableImplSpi(
        final IGenericWidgetFactory factory,
        final Object parentUiReference,
        final ITableSetupSpi setup,
        final SwtImageRegistry imageRegistry) {
        super(new Table((Composite) parentUiReference, getStyle(setup)), imageRegistry);

        this.imageRegistry = imageRegistry;

        this.factory = factory;
        this.editorCustomWidgetFactory = new EditorCustomWidgetFactory();

        this.selectedForegroundColor = SwtOptions.getTableSelectedForegroundColor();
        this.selectedBackgroundColor = SwtOptions.getTableSelectedBackgroundColor();

        this.editable = true;

        this.setWidthInvokedOnModel = false;

        this.tableCellObservable = new TableCellObservable();
        this.tableCellPopupDetectionObservable = new TableCellPopupDetectionObservable();
        this.tableColumnPopupDetectionObservable = new TableColumnPopupDetectionObservable();
        this.tableColumnObservable = new TableColumnObservable();
        this.tableSelectionObservable = new TableSelectionObservable();

        this.dataListener = new DataListener();
        this.eraseItemListener = new EraseItemListener();
        this.columnSelectionListener = new ColumnSelectionListener();
        this.columnControlListener = new ColumnControlListener();
        this.tableModelListener = new TableModelListener();
        this.tableColumnModelListener = new TableColumnModelListener();

        this.dataModel = setup.getDataModel();
        this.columnModel = setup.getColumnModel();

        this.columnsMoveable = setup.getColumnsMoveable();
        this.columnsResizeable = setup.getColumnsResizeable();

        this.editorFactory = setup.getEditor();

        this.table = getUiReference();

        try {
            setFocusIndexMethod = table.getClass().getDeclaredMethod("setFocusIndex", int.class);
            setFocusIndexMethod.setAccessible(true);
        }
        catch (final Exception e) {
            //DO NOTHING, SET FOCUS INDEX WILL NOT WORK ONLY
        }

        table.setLinesVisible(true);
        table.setHeaderVisible(setup.isHeaderVisible());

        // fake column to fix windows table bug and to support no selection
        final TableColumn fakeColumn = new TableColumn(table, SWT.NONE);
        fakeColumn.setResizable(false);
        fakeColumn.setMoveable(false);
        fakeColumn.setWidth(0);
        fakeColumn.setText("FAKE");

        try {
            this.editor = new TableEditor(table);
            this.editor.grabHorizontal = true;
            this.editor.grabVertical = true;
            table.addMouseListener(new TableEditListener());
        }
        catch (final NoClassDefFoundError e) {
            //RWT does not support TableCursor yet :-(
            //this.cursor = null;
            this.editor = null;
        }

        this.editRowIndex = -1;
        this.editColumnIndex = -1;

        table.addListener(SWT.SetData, dataListener);
        table.addListener(SWT.EraseItem, eraseItemListener);
        table.addMouseListener(new TableCellListener());
        table.addSelectionListener(new TableSelectionListener());

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

        table.addListener(SWT.MeasureItem, new Listener() {
            @Override
            public void handleEvent(final Event event) {
                handleMeasureEvent(event);
            }
        });

        table.addListener(SWT.EraseItem, new Listener() {
            @Override
            public void handleEvent(final Event event) {
                handleMeasureEvent(event);
            }
        });

        table.addListener(SWT.PaintItem, new Listener() {
            @Override
            public void handleEvent(final Event event) {
                final TableItem item = (TableItem) event.item;
                final int row = table.indexOf(item);
                final int column = event.index - 1;
                if (tableCellEditor != null && editColumnIndex == column && editRowIndex == row) {
                    final Rectangle bounds = item.getBounds(event.index);
                    tableCellEditor.setSize(new Dimension(bounds.width, event.height));
                }
            }
        });

        table.addTraverseListener(new TraverseListener() {
            @Override
            public void keyTraversed(final TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_ESCAPE) {
                    cancelEditing();
                }
                if (e.detail == SWT.TRAVERSE_RETURN) {
                    if (editable) {
                        final ArrayList<Integer> selection = getSelection();
                        if (!isEditing() && getSelection().size() > 0) {
                            final int rowIndex = selection.get(0).intValue() - 1;
                            //this method adds one the row index for edit mode
                            navigateDown(rowIndex, rowIndex, 0);
                        }
                        else if (!isEditing() && dataModel.getRowCount() > 0) {
                            final int rowIndex = -1;
                            //this method adds one the row index for edit mode
                            navigateDown(rowIndex, rowIndex, 0);
                        }
                    }
                }
            }
        });
    }

    private void handleMeasureEvent(final Event event) {
        if (rowHeight != null) {
            event.height = rowHeight.intValue();
        }
        if (SwtOptions.isTablePackWorkaround()) {
            event.width = event.width + 1;
        }
        final TableItem item = (TableItem) event.item;
        final int row = table.indexOf(item);
        final int column = event.index - 1;
        if (tableCellEditor != null && editColumnIndex == column && editRowIndex == row) {
            final int newHeigth = tableCellEditor.getPreferredSize().getHeight() + 1;
            if (rowHeight == null || newHeigth > rowHeight.intValue()) {
                event.height = newHeigth;
                rowHeight = newHeigth;
                editor.layout();
            }
        }
    }

    @Override
    public void setRowHeight(final int height) {
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

    private int getColumnCount() {
        return table.getColumnCount() - 1;
    }

    @Override
    public Table getUiReference() {
        return (Table) super.getUiReference();
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

        table.setItemCount(dataModel.getRowCount());
        table.clearAll();

        removeAllColumns();
        addAllColumns();

        setSelection(dataModel.getSelection());

        if (dataModelObservable != null) {
            dataModelObservable.addDataModelListener(tableModelListener);
        }

        if (columnModelObservable != null) {
            columnModelObservable.addColumnModelListener(tableColumnModelListener);
        }

        table.setRedraw(true);
    }

    private void removeAllColumns() {
        final int oldColumnCount = getColumnCount();
        for (int columnIndex = 0; columnIndex < oldColumnCount; columnIndex++) {
            removeColumnListener(columnIndex);
        }
        for (int columnIndex = 0; columnIndex < oldColumnCount; columnIndex++) {
            removeColumn(0);
        }
    }

    private void addAllColumns() {
        final int columnsCount = columnModel.getColumnCount();

        for (int columnIndex = 0; columnIndex < columnsCount; columnIndex++) {
            addColumn(columnIndex, columnModel.getColumn(columnIndex));
        }

        for (int columnIndex = 0; columnIndex < columnsCount; columnIndex++) {
            addColumnListener(columnIndex);
        }
    }

    private void addColumn(final int externalIndex, final ITableColumnSpi joColumn) {
        final int internalIndex = externalIndex + 1;
        final TableColumn swtColumn = new TableColumn(table, SWT.NONE, internalIndex);
        swtColumn.setMoveable(columnsMoveable);
        swtColumn.setResizable(columnsResizeable);
        setColumnData(swtColumn, joColumn);
    }

    private void setColumnData(final TableColumn swtColumn, final ITableColumnSpi joColumn) {
        final String text = joColumn.getText();
        final IImageConstant icon = joColumn.getIcon();
        final AlignmentHorizontal alignment = joColumn.getAlignment();

        if (text != null) {
            swtColumn.setText(text);
        }
        else {
            swtColumn.setText("");
        }
        if (icon != null) {
            swtColumn.setImage(imageRegistry.getImage(icon));
        }
        else {
            swtColumn.setImage(null);
        }
        if (alignment != null) {
            swtColumn.setAlignment(AlignmentConvert.convert(alignment));
        }
        else {
            swtColumn.setAlignment(SWT.LEFT);
        }
        if (joColumn.getWidth() != -1) {
            if (swtColumn.getWidth() != joColumn.getWidth()) {
                swtColumn.setWidth(joColumn.getWidth());
            }
        }
        else {
            if (swtColumn.getWidth() != 100) {
                swtColumn.setWidth(100);
            }
        }
        swtColumn.setToolTipText(joColumn.getToolTipText());
    }

    private void addColumnListener(final int externalIndex) {
        final int internalIndex = externalIndex + 1;
        final TableColumn column = table.getColumn(internalIndex);
        if (column != null && !column.isDisposed()) {
            column.addSelectionListener(columnSelectionListener);
            column.addControlListener(columnControlListener);
        }
    }

    private void removeColumnListener(final int externalIndex) {
        final int internalIndex = externalIndex + 1;
        final TableColumn column = table.getColumn(internalIndex);
        if (column != null && !column.isDisposed()) {
            column.removeSelectionListener(columnSelectionListener);
            column.removeControlListener(columnControlListener);
        }
    }

    private void removeColumn(final int externalIndex) {
        final int internalIndex = externalIndex + 1;
        final TableColumn column = table.getColumn(internalIndex);
        if (column != null && !column.isDisposed()) {
            column.dispose();
        }
    }

    @Override
    public Position getCellPosition(final int rowIndex, final int columnIndex) {
        final int internalIndex = columnIndex + 1;
        final Rectangle bounds = table.getItem(rowIndex).getBounds(internalIndex);
        return new Position(bounds.x, bounds.y);
    }

    @Override
    public Dimension getCellSize(final int rowIndex, final int columnIndex) {
        final int internalIndex = columnIndex + 1;
        final Rectangle bounds = table.getItem(rowIndex).getBounds(internalIndex);
        return new Dimension(bounds.width, bounds.height);
    }

    @Override
    public void pack(final TablePackPolicy policy) {
        table.setRedraw(false);
        for (int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++) {
            packColumn(columnIndex, policy);
        }
        table.setRedraw(true);
    }

    @Override
    public void pack(final int columnIndex, final TablePackPolicy policy) {
        table.setRedraw(false);
        packColumn(columnIndex, policy);
        table.setRedraw(true);
    }

    private void packColumn(final int columnIndex, final TablePackPolicy policy) {
        final int internalIndex = columnIndex + 1;
        final Label textLabel = new Label(table, SWT.NONE);
        final GC context = new GC(textLabel);

        final TableColumn[] columns = table.getColumns();
        final TableColumn column = columns[internalIndex];
        boolean packed = false;

        int max = 10;

        if (policy.considerHeader()) {
            context.setFont(table.getFont());
            textLabel.setFont(table.getFont());
            int width = context.textExtent(column.getText()).x;
            if (column.getImage() != null) {
                width += column.getImage().getBounds().width;
            }
            max = Math.max(max, width);
        }

        if (policy.considerData() && policy.considerAllData()) {
            for (int i = 0; i < table.getItemCount(); i++) {
                final TableItem item = table.getItem(i);

                context.setFont(item.getFont(internalIndex));
                textLabel.setFont(item.getFont(internalIndex));

                int width = context.textExtent(item.getText(internalIndex)).x;
                if (item.getImage(internalIndex) != null) {
                    width += item.getImage(internalIndex).getBounds().width + 5;
                }
                max = Math.max(max, width);
            }
        }
        else if (policy.considerData()) {
            packed = true;
            column.pack();
        }

        if (packed) {
            column.setWidth(Math.max(max + 15, column.getWidth()));
        }
        else {
            column.setWidth(max + 15);
        }

        context.dispose();
        textLabel.dispose();
    }

    @Override
    public ArrayList<Integer> getColumnPermutation() {
        final ArrayList<Integer> result = new ArrayList<Integer>();
        for (final int index : table.getColumnOrder()) {
            if (index == 0) {
                continue;
            }
            result.add(Integer.valueOf(index - 1));
        }
        return result;
    }

    @Override
    public void setColumnPermutation(final List<Integer> permutation) {
        if (!table.isDisposed()) {
            final int[] columnOrder = new int[permutation.size() + 1];
            columnOrder[0] = 0;
            int i = 1;
            for (final Integer permutatedIndex : permutation) {
                columnOrder[i] = permutatedIndex.intValue() + 1;
                i++;
            }
            table.setRedraw(false);
            table.setColumnOrder(columnOrder);
            table.setRedraw(true);
        }
    }

    @Override
    public ArrayList<Integer> getSelection() {
        final ArrayList<Integer> result = new ArrayList<Integer>();
        final int[] selection = table.getSelectionIndices();
        if (selection != null) {
            for (final int index : selection) {
                result.add(Integer.valueOf(index));
            }
        }
        return result;
    }

    @Override
    public void setSelection(final List<Integer> selection) {
        if (!isSelectionEqualWithView(selection)) {
            if (!EmptyCheck.isEmpty(selection)) {
                final int[] newSelection = new int[selection.size()];
                for (int i = 0; i < newSelection.length; i++) {
                    newSelection[i] = selection.get(i).intValue();
                }
                setSelectionWithoutShowSelection(newSelection);
            }
            else {
                setSelectionWithoutShowSelection(new int[0]);
            }
            fireSelectionChangedIfNeccessary();
        }
    }

    private void setSelectionWithoutShowSelection(final int[] indices) {
        table.deselectAll();
        if (indices.length == 0) {
            return;
        }
        table.select(indices);
        final int focusIndex = indices[0];
        if (setFocusIndexMethod != null && focusIndex != -1) {
            try {
                setFocusIndexMethod.invoke(table, focusIndex);
            }
            catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void scrollToRow(final int rowIndex) {
        Assert.paramInBounds(table.getItemCount() - 1, rowIndex, "rowIndex");
        table.showItem(table.getItem(rowIndex));
    }

    @Override
    public boolean isColumnPopupDetectionSupported() {
        return true;
    }

    @Override
    public Interval<Integer> getVisibleRows() {
        final int rowCount = dataModel.getRowCount();
        if (rowCount > 0) {
            final int leftBoundary = table.getTopIndex();
            final Dimension cellSize = getCellSize(leftBoundary, 0);
            final Rectangle clientArea = table.getClientArea();
            if (clientArea.height >= cellSize.getHeight()) {
                final int visibleRows = (clientArea.height / cellSize.getHeight());
                final int rigthBoundary = Math.min(rowCount - 1, leftBoundary + visibleRows);
                return new Interval<Integer>(leftBoundary, rigthBoundary);
            }
        }
        return new Interval<Integer>(null, null);
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
        final TableItem item = table.getItem(point);
        if (item != null) {
            for (int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++) {
                final int internalIndex = columnIndex + 1;
                final Rectangle rect = item.getBounds(internalIndex);
                if (rect.contains(point)) {
                    final int rowIndex = table.indexOf(item);
                    if (rowIndex != -1) {
                        return new CellIndices(rowIndex, columnIndex);
                    }
                }
            }
        }
        return null;
    }

    private int getColumnIndex(final TableColumn columnOfInterest) {
        if (columnOfInterest != null) {
            final TableColumn[] columns = table.getColumns();
            for (int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++) {
                final int internalIndex = columnIndex + 1;
                if (columns[internalIndex] == columnOfInterest) {
                    return columnIndex;
                }
            }
        }
        return -1;
    }

    private boolean isSelectionEqualWithView(final List<Integer> selection) {
        final int[] tableSelection = table.getSelectionIndices();
        if (selection == null && (tableSelection == null || tableSelection.length == 0)) {
            return true;
        }
        if (selection.size() != tableSelection.length) {
            return false;
        }
        for (final int tablesSelected : tableSelection) {
            if (!selection.contains(Integer.valueOf(tablesSelected))) {
                return false;
            }
        }
        return true;
    }

    private void showToolTip(final String message) {
        toolTip.setMessage(message);
        final Point location = Display.getCurrent().getCursorLocation();
        toolTip.setLocation(location.x + 16, location.y + 16);
        toolTip.setVisible(true);
    }

    private void fireSelectionChangedIfNeccessary() {
        if (!NullCompatibleEquivalence.equals(lastSelection, table.getSelectionIndices())) {
            lastSelection = table.getSelectionIndices();
            tableSelectionObservable.fireSelectionChanged();
        }
    }

    @Override
    public boolean editCell(final int row, final int column) {
        if (editor != null && this.editRowIndex != row || editColumnIndex != column) {
            stopEditing();
            this.editRowIndex = row;
            this.editColumnIndex = column;
            editTableCell = dataModel.getCell(editRowIndex, editColumnIndex);
            activateEditorFromFactory();
        }
        return isEditing();
    }

    @Override
    public void stopEditing() {
        if (tableCellEditor != null) {
            tableCellEditor.stopEditing(editTableCell, editRowIndex, editColumnIndex);
            disposeEditor();
            table.forceFocus();
        }
    }

    @Override
    public void cancelEditing() {
        if (tableCellEditor != null) {
            tableCellEditor.cancelEditing(editTableCell, editRowIndex, editColumnIndex);
            disposeEditor();
            table.forceFocus();
        }
    }

    private void disposeEditor() {
        ((Control) tableCellEditor.getUiReference()).dispose();
        tableCellEditor = null;
        editTableCell = null;
        editRowIndex = -1;
        editColumnIndex = -1;
        editor.setEditor(null);
        editor.layout();
        stopEditTimestamp = System.currentTimeMillis();
    }

    @Override
    public boolean isEditing() {
        return tableCellEditor != null;
    }

    private void activateEditorFromFactory() {
        tableCellEditor = editorFactory.create(editTableCell, editRowIndex, editColumnIndex, editorCustomWidgetFactory);
        if (tableCellEditor == null) {
            return;
        }

        final int internalIndex = editColumnIndex + 1;

        final Control uiReference = (Control) tableCellEditor.getUiReference();

        tableCellEditor.startEditing(editTableCell, editRowIndex, editColumnIndex);
        editor.setEditor(uiReference, table.getItem(editRowIndex), internalIndex);

        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run() {
                if (isEditing()) {
                    editor.layout();
                    tableCellEditor.requestFocus();
                }
            }
        });

        final TraverseListener traverseListener = new TraverseListener() {
            @Override
            public void keyTraversed(final TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_TAB_NEXT) {
                    e.doit = false;
                }
                else if (e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
                    e.doit = false;
                }
            }
        };

        ListenerUtil.addRecursiveTraverseListener(uiReference, traverseListener);

        tableCellEditor.addKeyListener(new IKeyListener() {

            @Override
            public void keyReleased(final IKeyEvent event) {}

            @Override
            public void keyPressed(final IKeyEvent event) {

                final boolean ctrl = event.getModifier().contains(Modifier.CTRL);
                final boolean shift = event.getModifier().contains(Modifier.SHIFT);

                final boolean enter = VirtualKey.ENTER.equals(event.getVirtualKey());
                final boolean esc = VirtualKey.ESC.equals(event.getVirtualKey());

                boolean left = VirtualKey.ARROW_LEFT.equals(event.getVirtualKey()) && shift;
                left = left || (VirtualKey.TAB.equals(event.getVirtualKey()) && shift);

                boolean right = VirtualKey.ARROW_RIGHT.equals(event.getVirtualKey()) && shift;
                right = right || (VirtualKey.TAB.equals(event.getVirtualKey()) && !shift);

                final boolean up = VirtualKey.ARROW_UP.equals(event.getVirtualKey()) && shift;

                final boolean down = VirtualKey.ARROW_DOWN.equals(event.getVirtualKey()) && shift;

                if (enter && ctrl) {
                    stopEditing();
                }
                if (enter) {
                    navigateDownLeft();
                }
                else if (esc) {
                    cancelEditing();
                }
                else if (right) {
                    navigateRight();
                }
                else if (left) {
                    navigateLeft();
                }
                else if (up) {
                    navigateUp();
                }
                else if (down) {
                    navigateDown();
                }
            }
        });
    }

    private boolean navigateRight() {
        if (isEditing()) {
            return navigateRight(editRowIndex, editRowIndex, convertColumnIndexToView(editColumnIndex));
        }
        else {
            return false;
        }
    }

    private boolean navigateDown() {
        if (isEditing()) {
            return navigateDown(editRowIndex, editRowIndex, convertColumnIndexToView(editColumnIndex));
        }
        else {
            return false;
        }
    }

    private boolean navigateLeft() {
        if (isEditing()) {
            return navigateLeft(editRowIndex, editRowIndex, convertColumnIndexToView(editColumnIndex));
        }
        else {
            return false;
        }
    }

    private boolean navigateUp() {
        if (isEditing()) {
            return navigateUp(editRowIndex, editRowIndex, convertColumnIndexToView(editColumnIndex));
        }
        else {
            return false;
        }
    }

    private boolean navigateDownLeft() {
        if (isEditing()) {
            return navigateDown(editRowIndex, editRowIndex, 0);
        }
        else {
            return false;
        }
    }

    private boolean navigateRight(final int startRow, final int row, final int viewColumnIndex) {
        if (viewColumnIndex + 1 < columnModel.getColumnCount()) {
            if (editCell(row, convertColumnIndexToModel(viewColumnIndex + 1))) {
                table.showColumn(table.getColumn(editor.getColumn()));
                editor.layout();
                return true;
            }
            else {
                return navigateRight(startRow, row, viewColumnIndex + 1);
            }
        }
        else if (row - startRow < 2) {
            return navigateDown(startRow, row, 0);
        }
        else {
            return false;
        }
    }

    private boolean navigateDown(final int startRow, final int row, final int viewColumnIndex) {
        if (dataModel.getRowCount() > row + 1) {
            if (editCell(row + 1, convertColumnIndexToModel(viewColumnIndex))) {
                setSelection(Collections.singletonList(Integer.valueOf(row + 1)));
                table.showSelection();
                table.showColumn(table.getColumn(editor.getColumn()));
                editor.layout();
                return true;
            }
            else if (row - startRow < 2) {
                setSelection(Collections.singletonList(Integer.valueOf(row + 1)));
                table.showSelection();
                editor.layout();
                return navigateRight(startRow, row + 1, viewColumnIndex);
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    private boolean navigateLeft(final int startRow, final int row, final int viewColumnIndex) {
        if (viewColumnIndex > 0) {
            if (editCell(row, convertColumnIndexToModel(viewColumnIndex - 1))) {
                table.showColumn(table.getColumn(editor.getColumn()));
                editor.layout();
                return true;
            }
            else {
                return navigateLeft(startRow, row, viewColumnIndex - 1);
            }
        }
        else if (startRow - row < 2) {
            return navigateUp(startRow, row, columnModel.getColumnCount() - 1);
        }
        else {
            return false;
        }
    }

    private boolean navigateUp(final int startRow, final int row, final int viewColumnIndex) {
        if (row > 0) {
            if (editCell(row - 1, convertColumnIndexToModel(viewColumnIndex))) {
                setSelection(Collections.singletonList(Integer.valueOf(row - 1)));
                table.showSelection();
                table.showColumn(table.getColumn(editor.getColumn()));
                editor.layout();
                return true;
            }
            else if (startRow - row < 2) {
                setSelection(Collections.singletonList(Integer.valueOf(row - 1)));
                table.showSelection();
                table.showColumn(table.getColumn(editor.getColumn()));
                editor.layout();
                return navigateLeft(startRow, row - 1, viewColumnIndex);
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    private int convertColumnIndexToView(final int modelIndex) {
        final ArrayList<Integer> permutation = getColumnPermutation();
        return permutation.indexOf(Integer.valueOf(modelIndex));
    }

    private int convertColumnIndexToModel(final int viewIndex) {
        return getColumnPermutation().get(viewIndex).intValue();
    }

    private static int getStyle(final ITableSetupSpi setup) {
        int result = SWT.VIRTUAL;

        if (setup.hasBorder()) {
            result = result | SWT.BORDER;
        }

        if (TableSelectionPolicy.MULTI_ROW_SELECTION == setup.getSelectionPolicy()) {
            result = result | SWT.FULL_SELECTION | SWT.MULTI;
        }
        else if (TableSelectionPolicy.SINGLE_ROW_SELECTION == setup.getSelectionPolicy()) {
            result = result | SWT.FULL_SELECTION;
        }
        else if (TableSelectionPolicy.NO_SELECTION == setup.getSelectionPolicy()) {
            result = result | SWT.HIDE_SELECTION;
        }
        else {
            throw new IllegalArgumentException("SelectionPolicy '" + setup.getSelectionPolicy() + "' is not known");
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

    private final class EraseItemListener implements Listener {
        @Override
        public void handleEvent(final Event event) {
            final GC gc = event.gc;
            final Color oldBackground = gc.getBackground();
            final TableItem item = (TableItem) event.item;
            final int viewColumnIndex = event.index;
            final int rowIndex = table.indexOf(item);
            if (rowIndex < 0 || viewColumnIndex < 1) {
                return;
            }

            final int modelColumnIndex = convertColumnIndexToModel(viewColumnIndex - 1);
            final ITableCell cell = dataModel.getCell(rowIndex, modelColumnIndex);

            final IColorConstant foreground = cell.getForegroundColor();
            final IColorConstant background = cell.getBackgroundColor();

            IColorConstant selectedForeground = cell.getSelectedForegroundColor();
            IColorConstant selectedBackground = cell.getSelectedBackgroundColor();

            if (selectedForeground == null) {
                selectedForeground = selectedForegroundColor;
            }

            if (selectedBackground == null) {
                selectedBackground = selectedBackgroundColor;
            }

            if ((selectedForeground != null || selectedBackground != null) && (event.detail & SWT.SELECTED) != 0) {
                final Rectangle rect = item.getBounds(viewColumnIndex);
                if (selectedForeground != null) {
                    gc.setForeground(ColorCache.getInstance().getColor(selectedForeground));
                }
                if (selectedBackground != null) {
                    gc.setBackground(ColorCache.getInstance().getColor(selectedBackground));
                }
                gc.fillRectangle(rect);
                event.detail &= ~SWT.SELECTED;
                return;
            }
            else if ((selectedForeground != null || selectedBackground != null) && (event.detail & SWT.HOT) != 0) {
                final Rectangle rect = item.getBounds(viewColumnIndex);
                if (foreground != null) {
                    gc.setForeground(ColorCache.getInstance().getColor(foreground));
                }
                if (background != null) {
                    gc.setBackground(ColorCache.getInstance().getColor(background));
                }
                gc.fillRectangle(rect);
                event.detail &= ~SWT.HOT;
                return;
            }

            //This fixes Bug 50163 by using workaround from comment 13
            //(Visited url: https://bugs.eclipse.org/bugs/show_bug.cgi?id=50163)
            final Color backgroundColor = item.getBackground(viewColumnIndex);
            if (backgroundColor != null) {
                gc.setBackground(backgroundColor);
                final Rectangle bounds = item.getBounds(viewColumnIndex);
                gc.fillRectangle(bounds.x, bounds.y, bounds.width, bounds.height);
                gc.setBackground(oldBackground);
            }
        }
    }

    private final class DataListener implements Listener {
        @Override
        public void handleEvent(final Event event) {
            final TableItem item = (TableItem) event.item;
            final int rowIndex = event.index;
            for (int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++) {
                final int internalIndex = columnIndex + 1;
                if (dataModel.getRowCount() > rowIndex) {
                    final ITableCell cell = dataModel.getCell(rowIndex, columnIndex);

                    final String text = cell.getText();
                    final IImageConstant icon = cell.getIcon();
                    final IColorConstant backgroundColor = cell.getBackgroundColor();
                    final IColorConstant foregroundColor = cell.getForegroundColor();
                    final Markup markup = cell.getMarkup();

                    if (text != null) {
                        item.setText(internalIndex, text);
                    }
                    else {
                        item.setText(internalIndex, "");
                    }
                    if (icon != null) {
                        item.setImage(internalIndex, imageRegistry.getImage(icon));
                    }
                    if (markup != null) {
                        final Font newFont = FontProvider.deriveFont(item.getFont(), markup);
                        item.setFont(internalIndex, newFont);
                    }
                    if (backgroundColor != null) {
                        item.setBackground(internalIndex, ColorCache.getInstance().getColor(backgroundColor));
                    }
                    if (foregroundColor != null) {
                        item.setForeground(internalIndex, ColorCache.getInstance().getColor(foregroundColor));
                    }
                }
            }
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

    private final class TableEditListener extends MouseAdapter {

        private Point lastPoint;

        @Override
        public void mouseUp(final MouseEvent e) {
            if (((e.stateMask & SWT.BUTTON1) != 0) && editorFactory != null) {
                final Point point = new Point(e.x, e.y);
                if (!point.equals(lastPoint)) {
                    final CellIndices indices = getExternalCellIndices(point);
                    if (indices != null) {
                        final ITableCell cell = dataModel.getCell(indices.getRowIndex(), indices.getColumnIndex());
                        if (cell.isEditable() && editable && editor != null) {
                            if (EditActivation.SINGLE_CLICK.equals(editorFactory.getActivation(
                                    cell,
                                    indices.getRowIndex(),
                                    indices.getColumnIndex(),
                                    isEditing(),
                                    stopEditTimestamp))) {
                                lastPoint = point;
                                startEdit(indices);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void mouseDoubleClick(final MouseEvent e) {
            final Point point = new Point(e.x, e.y);
            if (!point.equals(lastPoint)) {
                final CellIndices indices = getExternalCellIndices(point);
                if (indices != null) {
                    final ITableCell cell = dataModel.getCell(indices.getRowIndex(), indices.getColumnIndex());
                    if (cell.isEditable() && editable && editor != null) {
                        if (editorFactory != null) {
                            if (EditActivation.DOUBLE_CLICK.equals(editorFactory.getActivation(
                                    cell,
                                    indices.getRowIndex(),
                                    indices.getColumnIndex(),
                                    isEditing(),
                                    stopEditTimestamp))) {
                                lastPoint = point;
                                startEdit(indices);
                            }
                        }
                        else {
                            startEdit(indices);
                        }
                    }
                }
            }
        }

        private void startEdit(final CellIndices indices) {
            if (editorFactory != null) {
                editCell(indices.getRowIndex(), indices.getColumnIndex());
            }
        }

    }

    private final class TableMenuDetectListener implements MenuDetectListener {

        @Override
        public void menuDetected(final MenuDetectEvent e) {

            //if a drag source is installed, selection event fires after menu detect but selection
            //should always change before menu detection
            fireSelectionChangedIfNeccessary();

            //stop editing before popup opens
            stopEditing();

            // calculate position manually due to different behavior of Windows and Linux
            final Rectangle tableBounds = table.getBounds();
            final Point tableOrigin = table.getParent().toDisplay(new Point(tableBounds.x, tableBounds.y));
            final Point tableEventPoint = new Point(e.x - tableOrigin.x, e.y - tableOrigin.y);

            Point point = new Point(e.x, e.y);
            point = table.toControl(point);
            final Position position = new Position(point.x, point.y);
            TableItem item = table.getItem(point);

            //Menu detect on table cell
            if (item != null && tableEventPoint.y > table.getHeaderHeight()) {
                for (int colIndex = 0; colIndex < getColumnCount(); colIndex++) {
                    final int internalColIndex = colIndex + 1;
                    final Rectangle rect = item.getBounds(internalColIndex);
                    if (rect.contains(point)) {
                        final int rowIndex = table.indexOf(item);

                        if (rowIndex != -1) {
                            tableCellPopupDetectionObservable
                                    .firePopupDetected(new TableCellPopupEvent(rowIndex, colIndex, position));
                        }
                    }
                }
            }
            //Menu detect on header. Table has some item(s)
            else if (table.getItemCount() > 0 && tableEventPoint.y < table.getHeaderHeight()) {
                item = table.getItem(0);
                fireColumnPopupDetected(item, point, position);
            }
            //Menu detect on header but table has no item.
            //Just temporarily add an item to the table an remove it, after
            //position was calculated.
            else if (tableEventPoint.y < table.getHeaderHeight()) {
                table.setRedraw(false);
                table.removeListener(SWT.SetData, dataListener);
                final TableItem dummyItem = new TableItem(table, SWT.NONE);
                fireColumnPopupDetected(dummyItem, point, position);
                dummyItem.dispose();
                table.addListener(SWT.SetData, dataListener);
                table.setRedraw(true);
            }
            else {
                getPopupDetectionObservable().firePopupDetected(position);
            }
        }

        private void fireColumnPopupDetected(final TableItem item, final Point point, final Position position) {
            for (int colIndex = 0; colIndex < getColumnCount(); colIndex++) {
                final int internalIndex = colIndex + 1;
                final Rectangle rect = item.getBounds(internalIndex);
                if (rect.x <= point.x && point.x <= rect.x + rect.width) {
                    tableColumnPopupDetectionObservable.firePopupDetected(new TableColumnPopupEvent(colIndex, position));
                }
            }
        }
    }

    private final class ColumnSelectionListener extends SelectionAdapter {
        @Override
        public void widgetSelected(final SelectionEvent e) {
            final TableColumn column = (TableColumn) e.widget;
            if (column != null) {
                final int columnIndex = getColumnIndex(column);
                final Set<Modifier> modifier = MouseUtil.getModifier(e.stateMask);
                tableColumnObservable.fireMouseClicked(new TableColumnMouseEvent(columnIndex, modifier));
            }
        }
    }

    private final class ColumnControlListener implements ControlListener {

        private long lastResizeTime = 0;

        @Override
        public void controlResized(final ControlEvent e) {
            final TableColumn column = (TableColumn) e.widget;
            final long time = getTime(e);
            if (time != -1) {
                lastResizeTime = time;
            }
            if (column != null) {
                final int columnIndex = getColumnIndex(column);
                final int width = column.getWidth();
                setWidthInvokedOnModel = true;
                columnModel.getColumn(columnIndex).setWidth(width);
                setWidthInvokedOnModel = false;
                tableColumnObservable.fireColumnResized(new TableColumnResizeEvent(columnIndex, width));
            }
        }

        @Override
        public void controlMoved(final ControlEvent e) {
            if (getTime(e) != lastResizeTime) {
                final int[] columnOrder = table.getColumnOrder();
                if (lastColumnOrder == null || !Arrays.equals(columnOrder, lastColumnOrder)) {
                    lastColumnOrder = columnOrder;
                    tableColumnObservable.fireColumnPermutationChanged();
                }
            }
        }

        private long getTime(final ControlEvent event) {
            try {
                return event.time;
            }
            catch (final NoSuchFieldError e) {
                //RWT doesn't support time field :-( 
            }
            return -1;
        }
    }

    private final class TableSelectionListener extends SelectionAdapter {
        @Override
        public void widgetSelected(final SelectionEvent e) {
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
            table.clear(rowIndices);
        }

        @Override
        public void dataChanged() {
            table.clearAll();
            table.setItemCount(dataModel.getRowCount());
            //Do redraw after invoke of virtual table clearAll(), 
            //see Bug: 202237 https://bugs.eclipse.org/bugs/show_bug.cgi?id=202237
            table.redraw();
        }

        @Override
        public void selectionChanged() {
            setSelection(dataModel.getSelection());
        }

        private void updateRows(final int[] rowIndices) {
            if (rowIndices != null && rowIndices.length > 0) {
                table.clear(ArrayUtils.getMin(rowIndices), dataModel.getRowCount() - 1);
                table.setItemCount(dataModel.getRowCount());
                //Do redraw after invoke of virtual table clearAll(), 
                //see Bug: 202237 https://bugs.eclipse.org/bugs/show_bug.cgi?id=202237
                table.redraw();
            }
        }

    }

    private final class TableColumnModelListener implements ITableColumnModelListener {

        @Override
        public void columnsAdded(final int[] columnIndices) {
            stopEditing();
            table.setRedraw(false);
            table.clearAll();
            Arrays.sort(columnIndices);
            for (int i = 0; i < columnIndices.length; i++) {
                final int addedIndex = columnIndices[i];
                addColumn(addedIndex, columnModel.getColumn(addedIndex));
                addColumnListener(addedIndex);
            }
            table.setRedraw(true);
        }

        @Override
        public void columnsRemoved(final int[] columnIndices) {
            stopEditing();
            table.setRedraw(false);
            table.clearAll();
            Arrays.sort(columnIndices);
            int removedColumnsCount = 0;
            for (int i = 0; i < columnIndices.length; i++) {
                final int removedIndex = columnIndices[i] - removedColumnsCount;
                removeColumnListener(removedIndex);
                removeColumn(removedIndex);
                removedColumnsCount++;
            }
            table.setRedraw(true);
        }

        @Override
        public void columnsChanged(final int[] columnIndices) {
            stopEditing();
            if (!setWidthInvokedOnModel) {
                final TableColumn[] columns = table.getColumns();
                for (final int changedIndex : columnIndices) {
                    final int internalIndex = changedIndex + 1;
                    setColumnData(columns[internalIndex], columnModel.getColumn(changedIndex));
                }
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

    private final class ToolTipListener implements Listener {

        @Override
        public void handleEvent(final Event event) {
            if (event.type == SWT.MouseHover) {
                final CellIndices indices = getExternalCellIndices(new Point(event.x, event.y));
                if (indices != null) {
                    toolTip.setVisible(false);
                    final ITableCell cell = dataModel.getCell(indices.rowIndex, indices.columnIndex);
                    if (cell.getToolTipText() != null) {
                        showToolTip(cell.getToolTipText());
                    }
                }
            }
            else {
                if (toolTip != null && !toolTip.isDisposed()) {
                    toolTip.setVisible(false);
                }
            }
        }
    }
}
