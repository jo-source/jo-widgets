/*
 * Copyright (c) 2011, Nikolaus Moll
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

package org.jowidgets.impl.widgets.basic;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jowidgets.common.widgets.controller.ITableColumnListener;
import org.jowidgets.common.widgets.controller.ITableColumnMouseEvent;
import org.jowidgets.common.widgets.controller.ITableColumnObservable;
import org.jowidgets.common.widgets.controller.ITableColumnResizeEvent;
import org.jowidgets.impl.event.TableColumnMouseEvent;
import org.jowidgets.impl.event.TableColumnResizeEvent;
import org.jowidgets.util.Assert;

class TableColumnObservableSpiAdapter implements ITableColumnObservable {

    private final Set<ITableColumnListener> listeners;

    TableColumnObservableSpiAdapter() {
        this.listeners = new LinkedHashSet<ITableColumnListener>();
    }

    @Override
    public void addTableColumnListener(final ITableColumnListener listener) {
        Assert.paramNotNull(listener, "listener");
        listeners.add(listener);
    }

    @Override
    public void removeTableColumnListener(final ITableColumnListener listener) {
        Assert.paramNotNull(listener, "listener");
        listeners.remove(listener);
    }

    public void fireColumnPermutationChanged() {
        if (!listeners.isEmpty()) {
            for (final ITableColumnListener listener : new LinkedList<ITableColumnListener>(listeners)) {
                listener.columnPermutationChanged();
            }
        }
    }

    public void fireColumnResized(final ITableColumnResizeEvent event, final TableModelSpiAdapter modelSpiAdapter) {
        if (!listeners.isEmpty()) {
            final ITableColumnResizeEvent decoratedEvent = new TableColumnResizeEvent(
                modelSpiAdapter.convertViewToModel(event.getColumnIndex()),
                event.getWidth());
            for (final ITableColumnListener listener : new LinkedList<ITableColumnListener>(listeners)) {
                listener.columnResized(decoratedEvent);
            }
        }
    }

    public void fireMouseClicked(final ITableColumnMouseEvent event, final TableModelSpiAdapter modelSpiAdapter) {
        if (!listeners.isEmpty()) {
            final ITableColumnMouseEvent decoratedEvent = new TableColumnMouseEvent(
                modelSpiAdapter.convertViewToModel(event.getColumnIndex()),
                event.getModifiers());
            for (final ITableColumnListener listener : new LinkedList<ITableColumnListener>(listeners)) {
                listener.mouseClicked(decoratedEvent);
            }
        }
    }
}
