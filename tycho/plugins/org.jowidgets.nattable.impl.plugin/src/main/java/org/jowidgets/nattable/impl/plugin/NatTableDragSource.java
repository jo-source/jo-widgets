/*
 * Copyright (c) 2017, MGrossmann
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

import java.util.Collection;
import java.util.Set;

import org.jowidgets.common.dnd.DropAction;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.spi.clipboard.TransferTypeSpi;
import org.jowidgets.spi.dnd.IDragDataResponseSpi;
import org.jowidgets.spi.dnd.IDragEventSpi;
import org.jowidgets.spi.dnd.IDragSourceListenerSpi;
import org.jowidgets.spi.dnd.IDragSourceSpi;
import org.jowidgets.spi.widgets.ITableSpi;
import org.jowidgets.util.Assert;

final class NatTableDragSource implements IDragSourceSpi {

    private final IDragSourceSpi original;
    private final ITableSpi table;

    NatTableDragSource(final IDragSourceSpi original, final ITableSpi table) {
        Assert.paramNotNull(original, "original");
        Assert.paramNotNull(table, "table");
        this.original = original;
        this.table = table;
    }

    @Override
    public void addDragSourceListenerSpi(final IDragSourceListenerSpi listener) {
        original.addDragSourceListenerSpi(new NatTableDragSourceListener(listener));
    }

    @Override
    public void removeDragSourceListenerSpi(final IDragSourceListenerSpi listener) {
        original.removeDragSourceListenerSpi(new NatTableDragSourceListener(listener));
    }

    @Override
    public void setTransferTypes(final Collection<TransferTypeSpi> supportedTypes) {
        original.setTransferTypes(supportedTypes);
    }

    @Override
    public void setActions(final Set<DropAction> actions) {
        original.setActions(actions);
    }

    private final class NatTableDragSourceListener implements IDragSourceListenerSpi {

        private final IDragSourceListenerSpi original;

        NatTableDragSourceListener(final IDragSourceListenerSpi original) {
            Assert.paramNotNull(original, "original");
            this.original = original;
        }

        @Override
        public void dragStart(final IDragEventSpi event, final IVetoable veto) {
            if (table.getRowAtPosition(event.getPosition()) < 0) {
                veto.veto();
            }
            else {
                original.dragStart(event, veto);
            }
        }

        @Override
        public void dragSetData(
            final IDragEventSpi event,
            final IVetoable veto,
            final TransferTypeSpi transferType,
            final IDragDataResponseSpi dragData) {
            original.dragSetData(event, veto, transferType, dragData);
        }

        @Override
        public void dragFinished(final IDragEventSpi event, final DropAction dropAction) {
            original.dragFinished(event, dropAction);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((original == null) ? 0 : original.hashCode());
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final NatTableDragSourceListener other = (NatTableDragSourceListener) obj;
            if (!getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (original == null) {
                if (other.original != null) {
                    return false;
                }
            }
            else if (!original.equals(other.original)) {
                return false;
            }
            return true;
        }

        private NatTableDragSource getOuterType() {
            return NatTableDragSource.this;
        }

    }

}
