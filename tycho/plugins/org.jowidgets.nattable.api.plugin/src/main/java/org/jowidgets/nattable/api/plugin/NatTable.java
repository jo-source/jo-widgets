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

package org.jowidgets.nattable.api.plugin;

import org.jowidgets.api.model.table.ITableColumnModel;
import org.jowidgets.api.model.table.ITableModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.util.Assert;

/**
 * Accessor to nat table blue prints
 */
public final class NatTable {

    private NatTable() {}

    /**
     * Creates a INatTableBluePrint for a given table model
     * 
     * @param model The model to use, must not be null
     * 
     * @return A new blue print
     */
    public static INatTableBluePrint bluePrint(final ITableModel model) {
        Assert.paramNotNull(model, "model");
        return bluePrint(model, model);
    }

    /**
     * Creates a INatTableBluePrint for a given column a data model
     * 
     * @param columnModel The column model to use, must not be null
     * @param dataModel The data model to use, must not be null
     * 
     * @return A new blue print
     */
    public static INatTableBluePrint bluePrint(final ITableColumnModel columnModel, final ITableDataModel dataModel) {
        Assert.paramNotNull(columnModel, "columnModel");
        Assert.paramNotNull(dataModel, "dataModel");
        final INatTableBluePrint result = Toolkit.getBluePrintProxyFactory().bluePrint(INatTableBluePrint.class);
        result.setColumnModel(columnModel).setDataModel(dataModel);
        return result;
    }

}
