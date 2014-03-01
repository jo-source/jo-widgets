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
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.FlavorMap;
import java.awt.datatransfer.SystemFlavorMap;
import java.awt.datatransfer.Transferable;

import javax.swing.SwingUtilities;

import org.jowidgets.spi.clipboard.IClipboardObservableSpi;
import org.jowidgets.spi.clipboard.IClipboardSpi;
import org.jowidgets.spi.clipboard.ITransferableSpi;
import org.jowidgets.spi.clipboard.TransferContainer;
import org.jowidgets.spi.impl.clipboard.ClipboardObservableSpi;

public final class SwingClipboard implements IClipboardSpi {

	static final DataFlavor TRANSFER_CONTAINER_FLAVOR = createTransferContainerFlavor();

	private final ClipboardObservableSpi clipboardObservable;
	private final Clipboard systemClipboard;
	private final FlavorListener flavorListener;

	public SwingClipboard() {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("The clipboard must be created in the event dispatcher thread");
		}

		this.clipboardObservable = new ClipboardObservableSpi();
		this.systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		this.flavorListener = new FlavorListener() {
			@Override
			public void flavorsChanged(final FlavorEvent e) {
				clipboardObservable.fireClipboardChanged();
			}
		};
		systemClipboard.addFlavorListener(flavorListener);

		final FlavorMap map = SystemFlavorMap.getDefaultFlavorMap();
		if (map instanceof SystemFlavorMap) {
			final SystemFlavorMap systemMap = (SystemFlavorMap) map;
			systemMap.addFlavorForUnencodedNative(TransferContainer.MIME_TYPE, TRANSFER_CONTAINER_FLAVOR);
			systemMap.addUnencodedNativeForFlavor(TRANSFER_CONTAINER_FLAVOR, TransferContainer.MIME_TYPE);
		}

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
	public IClipboardObservableSpi getObservable() {
		return clipboardObservable;
	}

	@Override
	public void dispose() {
		systemClipboard.removeFlavorListener(flavorListener);
	}

}
