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

package org.jowidgets.spi.impl.swing.common.clipboard;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jowidgets.spi.clipboard.ITransferableSpi;
import org.jowidgets.spi.clipboard.TransferContainer;
import org.jowidgets.spi.clipboard.TransferObject;
import org.jowidgets.spi.clipboard.TransferTypeSpi;
import org.jowidgets.spi.impl.clipboard.Serializer;
import org.jowidgets.util.Assert;

final class TransferableAdapter implements Transferable, Serializable {

    private static final long serialVersionUID = -5633005828108967675L;

    private static final DataFlavor TRANSFER_CONTAINER_FLAVOR = SwingClipboard.TRANSFER_CONTAINER_FLAVOR;

    private final DataFlavor[] flavors;
    private final Map<DataFlavor, Object> dataMap;

    TransferableAdapter(final ITransferableSpi transferableSpi) {
        Assert.paramNotNull(transferableSpi, "transferableSpi");

        final Collection<TransferTypeSpi> transferTypes = transferableSpi.getSupportedTypes();

        this.dataMap = new LinkedHashMap<DataFlavor, Object>();

        final List<TransferObject> transferObjects = new LinkedList<TransferObject>();

        for (final TransferTypeSpi transferType : transferTypes) {
            if (String.class.equals(transferType.getJavaType())) {
                final DataFlavor flavor = DataFlavor.stringFlavor;
                dataMap.put(flavor, transferableSpi.getData(transferType));
            }
            else {
                final Object data = transferableSpi.getData(transferType);
                transferObjects.add(new TransferObject(transferType, data));
            }
        }

        if (!transferObjects.isEmpty()) {
            final byte[] serializied = Serializer.serialize(new TransferContainer(transferObjects));
            if (serializied != null) {
                dataMap.put(TRANSFER_CONTAINER_FLAVOR, new ByteArrayInputStream(serializied));
            }
        }

        this.flavors = new DataFlavor[dataMap.size()];
        int index = 0;
        for (final DataFlavor flavor : dataMap.keySet()) {
            flavors[index] = flavor;
            index++;
        }
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(final DataFlavor flavor) {
        for (final DataFlavor supportedFlavor : flavors) {
            if (supportedFlavor.equals(flavor)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(flavor)) {
            return dataMap.get(flavor);
        }
        else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

}
