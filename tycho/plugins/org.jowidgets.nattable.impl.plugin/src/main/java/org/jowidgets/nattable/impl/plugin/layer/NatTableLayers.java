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

package org.jowidgets.nattable.impl.plugin.layer;

import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.selection.IRowSelectionModel;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.jowidgets.common.model.ITableColumnModelSpi;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.types.TableSelectionPolicy;
import org.jowidgets.util.Assert;

public final class NatTableLayers {

    private final GridLayer gridLayer;
    private final SelectionLayer selectionLayer;
    private final IRowSelectionModel<Integer> selectionModel;
    private final ColumnHeaderLayerStack columnHeaderLayer;
    private final JoColumnReorderLayer columnReorderLayer;
    private final DataLayer dataLayer;
    private final ViewportLayer viewPortLayer;

    public NatTableLayers(
        final ITableDataModel dataModel,
        final ITableColumnModelSpi columnModel,
        final boolean columsResizable,
        final boolean columnsMoveable,
        final TableSelectionPolicy selectionPolicy) {

        Assert.paramNotNull(dataModel, "dataModel");
        Assert.paramNotNull(columnModel, "columnModel");
        Assert.paramNotNull(selectionPolicy, "selectionPolicy");

        final IDataProvider dataProvider = new DefaultDataProvider(dataModel, columnModel);
        final IDataProvider rowDataProvider = new DefaultRowDataProvider();
        final IDataProvider columnDataProvider = new DefaultColumnDataProvider(columnModel);

        final BodyLayerStack bodyLayer = new BodyLayerStack(dataProvider, columsResizable, columnsMoveable, selectionPolicy);
        this.dataLayer = bodyLayer.getDataLayer();
        this.selectionModel = bodyLayer.getSelectionModel();
        this.selectionLayer = bodyLayer.getSelectionLayer();
        this.columnReorderLayer = bodyLayer.getColumnReorderLayer();
        this.viewPortLayer = bodyLayer.getViewportLayer();

        this.columnHeaderLayer = new ColumnHeaderLayerStack(columnDataProvider, bodyLayer);
        final RowHeaderLayerStack rowHeaderLayer = new RowHeaderLayerStack(rowDataProvider, bodyLayer);

        final DefaultCornerDataProvider cornerDataProvider = new DefaultCornerDataProvider(columnDataProvider, rowDataProvider);
        final CornerLayer cornerLayer = new CornerLayer(new DataLayer(cornerDataProvider), rowHeaderLayer, columnHeaderLayer);

        //remark: false also disable editing, but when editing is enabled selection drag mode is enabled and must
        //be disabled to allow drag and drop on tables
        this.gridLayer = new GridLayer(bodyLayer, columnHeaderLayer, rowHeaderLayer, cornerLayer, false);
    }

    public GridLayer getGridLayer() {
        return gridLayer;
    }

    public AbstractLayerTransform getColumnHeaderLayer() {
        return columnHeaderLayer;
    }

    public SelectionLayer getSelectionLayer() {
        return selectionLayer;
    }

    public IRowSelectionModel<Integer> getSelectionModel() {
        return selectionModel;
    }

    public JoColumnReorderLayer getColumnReorderLayer() {
        return columnReorderLayer;
    }

    public DataLayer getDataLayer() {
        return dataLayer;
    }

    public ViewportLayer getViewPortLayer() {
        return viewPortLayer;
    }

}
