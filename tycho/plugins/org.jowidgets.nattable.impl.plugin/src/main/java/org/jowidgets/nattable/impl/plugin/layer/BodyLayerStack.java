/*
 * Copyright (c) 2016, herrg
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
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.IRowSelectionModel;
import org.eclipse.nebula.widgets.nattable.selection.RowSelectionModel;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.jowidgets.common.types.TableSelectionPolicy;

final class BodyLayerStack extends AbstractLayerTransform {

    private final SelectionLayer selectionLayer;
    private final IRowSelectionModel<Integer> selectionModel;

    BodyLayerStack(final IDataProvider dataProvider, final TableSelectionPolicy selectionPolicy) {
        final DataLayer bodyDataLayer = new DataLayer(dataProvider);
        final ColumnReorderLayer columnReorderLayer = new ColumnReorderLayer(bodyDataLayer);
        this.selectionLayer = new SelectionLayer(columnReorderLayer);

        if (TableSelectionPolicy.NO_SELECTION.equals(selectionPolicy)) {
            this.selectionModel = new NoSelectionRowSelectionModel();
        }
        else {
            //TODO extract to class
            this.selectionModel = new RowSelectionModel<Integer>(
                selectionLayer,
                new RowIndexDataProvider(dataProvider),
                new RowIndexAccessor()) {

                @Override
                public boolean isColumnPositionSelected(final int columnPosition) {
                    return false;
                }

                @Override
                public int[] getFullySelectedColumnPositions(final int fullySelectedColumnRowCount) {
                    return new int[0];
                }

                @Override
                public boolean isColumnPositionFullySelected(final int columnPosition, final int fullySelectedColumnRowCount) {
                    return false;
                }
            };
            if (TableSelectionPolicy.SINGLE_ROW_SELECTION.equals(selectionPolicy)) {
                selectionModel.setMultipleSelectionAllowed(false);
            }
        }
        selectionLayer.setSelectionModel(selectionModel);
        selectionLayer.clearConfiguration();
        selectionLayer.addConfiguration(new SelectionLayerConfiguration());

        final ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);
        setUnderlyingLayer(viewportLayer);
    }

    SelectionLayer getSelectionLayer() {
        return selectionLayer;
    }

    IRowSelectionModel<Integer> getSelectionModel() {
        return selectionModel;
    }

}
