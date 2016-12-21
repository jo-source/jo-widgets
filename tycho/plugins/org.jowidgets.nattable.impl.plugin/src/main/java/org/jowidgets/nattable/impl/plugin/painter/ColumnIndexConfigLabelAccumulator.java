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

import org.eclipse.nebula.widgets.nattable.layer.LabelStack;
import org.eclipse.nebula.widgets.nattable.layer.cell.IConfigLabelAccumulator;
import org.jowidgets.util.Assert;

public class ColumnIndexConfigLabelAccumulator implements IConfigLabelAccumulator {

    private final String label;

    private Integer columnIndex;

    ColumnIndexConfigLabelAccumulator(final String label) {
        Assert.paramNotEmpty(label, "label");
        this.label = label;
    }

    @Override
    public final void accumulateConfigLabels(final LabelStack configLabels, final int columnPosition, final int rowPosition) {
        if (columnIndex != null && columnIndex.intValue() == columnPosition) {
            configLabels.addLabel(label);
        }
    }

    public final boolean setColumnIndex(final int hoveredColumnIndex) {
        if (this.columnIndex == null || this.columnIndex != hoveredColumnIndex) {
            this.columnIndex = Integer.valueOf(hoveredColumnIndex);
            return true;
        }
        else {
            return false;
        }
    }

    public final boolean clearColumnIndex() {
        if (this.columnIndex != null) {
            this.columnIndex = null;
            return true;
        }
        else {
            return false;
        }
    }

}
