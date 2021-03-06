/*
 * Copyright (c) 2014, grossmann
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

package org.jowidgets.impl.dnd;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jowidgets.api.clipboard.TransferType;
import org.jowidgets.common.dnd.DropAction;
import org.jowidgets.spi.clipboard.TransferTypeSpi;
import org.jowidgets.util.Assert;

final class DragDropDelegate {

    private final IDragDropSpiSupport dragDropSpiSupport;

    private Map<TransferTypeSpi, TransferType<?>> supportedTypes;

    DragDropDelegate(final IDragDropSpiSupport dragDropSpiSupport) {
        this.dragDropSpiSupport = dragDropSpiSupport;
    }

    void setTransferTypes(final Collection<TransferType<?>> types) {
        Assert.paramNotNull(types, "types");
        this.supportedTypes = new LinkedHashMap<TransferTypeSpi, TransferType<?>>();
        final List<TransferTypeSpi> transferTypesSpi = new LinkedList<TransferTypeSpi>();
        for (final TransferType<?> transferType : types) {
            final TransferTypeSpi transferTypeSpi = new TransferTypeSpi(transferType.getJavaType());
            transferTypesSpi.add(transferTypeSpi);
            supportedTypes.put(transferTypeSpi, transferType);
        }
        dragDropSpiSupport.setTransferTypesSpi(transferTypesSpi);
    }

    void setTransferTypes(final TransferType<?>... supportedTypes) {
        Assert.paramNotNull(supportedTypes, "supportedTypes");
        setTransferTypes(Arrays.asList(supportedTypes));
    }

    void setActions(final Set<DropAction> actions) {
        Assert.paramNotNull(actions, "actions");
        dragDropSpiSupport.setActionsSpi(actions);
    }

    void setActions(final DropAction... actions) {
        Assert.paramNotNull(actions, "actions");
        final Set<DropAction> actionsSet = new HashSet<DropAction>();
        for (int i = 0; i < actions.length; i++) {
            actionsSet.add(actions[i]);
        }
        setActions(actionsSet);
    }

    TransferType<?> getTransferType(final TransferTypeSpi transferTypeSpi) {
        if (TransferTypeSpi.UNKNOWN_TYPE.equals(transferTypeSpi)) {
            return TransferType.UNKOWN_TYPE;
        }
        else {
            return supportedTypes.get(transferTypeSpi);
        }
    }

}
