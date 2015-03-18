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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorMap;
import java.awt.datatransfer.SystemFlavorMap;
import java.awt.datatransfer.Transferable;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import org.jowidgets.spi.clipboard.IClipboardSpi;
import org.jowidgets.spi.clipboard.ITransferableSpi;
import org.jowidgets.spi.clipboard.TransferContainer;
import org.jowidgets.spi.impl.clipboard.AbstractPollingClipboardObservableSpi;
import org.jowidgets.spi.impl.swing.common.options.SwingOptions;
import org.jowidgets.util.NullCompatibleEquivalence;

public final class SwingClipboard extends AbstractPollingClipboardObservableSpi implements IClipboardSpi {

    static final DataFlavor TRANSFER_CONTAINER_FLAVOR = createTransferContainerFlavor();

    private final Clipboard systemClipboard;

    private Transferable lastContents;

    public SwingClipboard() {
        super(SwingOptions.getClipbaordPollingMillis());
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("The clipboard must be created in the event dispatcher thread");
        }

        this.systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        final FlavorMap map = SystemFlavorMap.getDefaultFlavorMap();
        if (map instanceof SystemFlavorMap) {
            final SystemFlavorMap systemMap = (SystemFlavorMap) map;
            systemMap.addFlavorForUnencodedNative(TransferContainer.MIME_TYPE, TRANSFER_CONTAINER_FLAVOR);
            systemMap.addUnencodedNativeForFlavor(TRANSFER_CONTAINER_FLAVOR, TransferContainer.MIME_TYPE);
        }

        checkContentChanged();
    }

    private static DataFlavor createTransferContainerFlavor() {
        try {
            return new DataFlavor(TransferContainer.MIME_TYPE);
        }
        catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setContents(final ITransferableSpi content) {
        if (content != null) {
            systemClipboard.setContents(new TransferableAdapter(content), null);
        }
        else {
            systemClipboard.setContents(null, null);
        }
        checkContentChanged();
    }

    @Override
    public ITransferableSpi getContents() {
        final Transferable contents = systemClipboard.getContents(null);
        if (contents != null) {
            return new TransferableSpiAdapter(contents);
        }
        else {
            return null;
        }
    }

    @Override
    protected synchronized void checkContentChanged() {
        final Transferable contents = systemClipboard.getContents(null);
        if (hasContentChanged(contents)) {
            if (SwingUtilities.isEventDispatchThread()) {
                fireClipboardChanged();
            }
            else {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        fireClipboardChanged();
                    }
                });
            }
        }
        lastContents = contents;
    }

    private boolean hasContentChanged(final Transferable contents) {

        if (contents == lastContents) {
            return false;
        }
        else if (lastContents == null && contents == null) {
            return false;
        }
        else if (contents == null || lastContents == null) {
            return true;
        }
        else {//both not null	
            final DataFlavor[] lastFlavors = lastContents.getTransferDataFlavors();
            final DataFlavor[] flavors = contents.getTransferDataFlavors();

            if (!Arrays.equals(lastFlavors, flavors)) {
                return true;
            }
            else {//flavors are equal
                for (int i = 0; i < flavors.length; i++) {
                    final DataFlavor flavor = flavors[i];
                    if (TRANSFER_CONTAINER_FLAVOR.equals(flavor) || DataFlavor.stringFlavor.equals(flavor)) {
                        try {
                            final Object lastObject = lastContents.getTransferData(lastFlavors[i]);
                            final Object object = contents.getTransferData(flavor);
                            if (object instanceof String || object instanceof TransferContainer) {
                                if (!NullCompatibleEquivalence.equals(lastObject, object)) {
                                    return true;
                                }
                            }
                        }
                        catch (final Exception e) {
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
