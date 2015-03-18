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

package org.jowidgets.spi.impl.swt.common.clipboard;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Display;
import org.jowidgets.spi.clipboard.IClipboardSpi;
import org.jowidgets.spi.clipboard.ITransferableSpi;
import org.jowidgets.spi.clipboard.TransferContainer;
import org.jowidgets.spi.clipboard.TransferObject;
import org.jowidgets.spi.clipboard.TransferTypeSpi;
import org.jowidgets.spi.impl.clipboard.AbstractPollingClipboardObservableSpi;
import org.jowidgets.spi.impl.swt.common.options.SwtOptions;
import org.jowidgets.util.IProvider;
import org.jowidgets.util.NullCompatibleEquivalence;
import org.jowidgets.util.Tuple;

public final class SwtClipboard extends AbstractPollingClipboardObservableSpi implements IClipboardSpi {

    private static final Transfer TEXT_TRANSFER = TextTransfer.getInstance();
    private static final Transfer OBJECT_TRANSFER = ObjectTransfer.getInstance();

    private final IProvider<Display> displayProvider;

    private Clipboard systemClipboard;

    private ITransferableSpi lastContents;

    public SwtClipboard(final IProvider<Display> displayProvider) {
        super(SwtOptions.getClipbaordPollingMillis());
        this.displayProvider = displayProvider;
    }

    @Override
    public void setContents(final ITransferableSpi contents) {
        final Clipboard clipboard = getClipboard();
        if (clipboard != null) {
            if (contents != null) {
                setContentsImpl(clipboard, contents);
            }
            else {
                clipboard.clearContents();
            }
            checkContentChangedInUiThread();
        }
    }

    private void setContentsImpl(final Clipboard clipboard, final ITransferableSpi contents) {
        final Collection<TransferTypeSpi> typesSpi = contents.getSupportedTypes();

        final List<Tuple<Transfer, Object>> transferTupleList = new LinkedList<Tuple<Transfer, Object>>();
        final List<TransferObject> transferObjectsList = new LinkedList<TransferObject>();

        for (final TransferTypeSpi typeSpi : typesSpi) {
            if (String.class.equals(typeSpi.getJavaType())) {
                transferTupleList.add(new Tuple<Transfer, Object>(TEXT_TRANSFER, contents.getData(typeSpi)));
            }
            else {
                transferObjectsList.add(new TransferObject(typeSpi, contents.getData(typeSpi)));
            }
        }

        if (transferObjectsList != null) {
            transferTupleList.add(new Tuple<Transfer, Object>(OBJECT_TRANSFER, new TransferContainer(transferObjectsList)));
        }

        final Transfer[] transfers = new Transfer[transferTupleList.size()];
        final Object[] data = new Object[transferTupleList.size()];

        int index = 0;
        for (final Tuple<Transfer, Object> tuple : transferTupleList) {
            transfers[index] = tuple.getFirst();
            data[index] = tuple.getSecond();
            index++;
        }

        clipboard.setContents(data, transfers);
    }

    @Override
    public ITransferableSpi getContents() {
        final Clipboard clipboard = getClipboard();
        if (clipboard != null) {
            return getContents(clipboard);
        }
        else {
            return null;
        }
    }

    private ITransferableSpi getContents(final Clipboard clipboard) {
        final Map<TransferTypeSpi, Object> transferMap = new LinkedHashMap<TransferTypeSpi, Object>();
        for (final TransferData transferData : clipboard.getAvailableTypes()) {
            if (TEXT_TRANSFER.isSupportedType(transferData)) {
                transferMap.put(new TransferTypeSpi(String.class), clipboard.getContents(TEXT_TRANSFER));
            }
            else if (OBJECT_TRANSFER.isSupportedType(transferData)) {
                final Object data = clipboard.getContents(OBJECT_TRANSFER);
                if (data instanceof TransferContainer) {
                    for (final TransferObject transferObject : ((TransferContainer) data).getTransferObjetcs()) {
                        transferMap.put(transferObject.getTransferType(), transferObject.getData());
                    }
                }
            }
        }
        if (!transferMap.isEmpty()) {
            return new TransferableSpiAdapter(transferMap);
        }
        else {
            return null;
        }
    }

    @Override
    protected synchronized void checkContentChanged() {
        final Display display = displayProvider.get();
        if (display == null || display.isDisposed()) {
            return;
        }
        display.asyncExec(new Runnable() {
            @Override
            public void run() {
                checkContentChangedInUiThread();
            }
        });

    }

    protected synchronized void checkContentChangedInUiThread() {
        final Clipboard clipboard = getClipboard();
        if (clipboard == null) {
            return;
        }
        final ITransferableSpi contents = getContents();
        if (!NullCompatibleEquivalence.equals(contents, lastContents)) {
            fireClipboardChanged();
        }
        lastContents = contents;
    }

    private boolean isUiThread(final Display display) {
        final Display currentDisplay = Display.getCurrent();
        return currentDisplay != null && currentDisplay == display;
    }

    @Override
    public void dispose() {
        super.dispose();
        systemClipboard.dispose();
    }

    private synchronized Clipboard getClipboard() {
        if (systemClipboard == null && displayProvider.get() != null) {
            final Display display = displayProvider.get();
            if (isUiThread(display)) {
                systemClipboard = new Clipboard(displayProvider.get());
                lastContents = getContents(systemClipboard);
            }
            else {
                display.asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        systemClipboard = new Clipboard(displayProvider.get());
                        lastContents = getContents(systemClipboard);
                    }
                });
                return null;
            }
        }
        return systemClipboard;
    }

}
