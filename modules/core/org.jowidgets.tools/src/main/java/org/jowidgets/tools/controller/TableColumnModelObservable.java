/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.tools.controller;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jowidgets.common.model.ITableColumnModelListener;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.util.Assert;

public class TableColumnModelObservable implements ITableColumnModelObservable {

    private final Set<ITableColumnModelListener> listeners;

    public TableColumnModelObservable() {
        this.listeners = new LinkedHashSet<ITableColumnModelListener>();
    }

    @Override
    public void addColumnModelListener(final ITableColumnModelListener listener) {
        Assert.paramNotNull(listener, "listener");
        listeners.add(listener);
    }

    @Override
    public void removeColumnModelListener(final ITableColumnModelListener listener) {
        Assert.paramNotNull(listener, "listener");
        listeners.remove(listener);
    }

    public void fireColumnsAdded(final int[] columnIndices) {
        for (final ITableColumnModelListener listener : new LinkedList<ITableColumnModelListener>(listeners)) {
            listener.columnsAdded(columnIndices);
        }
    }

    public void fireColumnsRemoved(final int[] columnIndices) {
        for (final ITableColumnModelListener listener : new LinkedList<ITableColumnModelListener>(listeners)) {
            listener.columnsRemoved(columnIndices);
        }
    }

    public void fireColumnsChanged(final int[] columnIndices) {
        for (final ITableColumnModelListener listener : new LinkedList<ITableColumnModelListener>(listeners)) {
            listener.columnsChanged(columnIndices);
        }
    }

}
