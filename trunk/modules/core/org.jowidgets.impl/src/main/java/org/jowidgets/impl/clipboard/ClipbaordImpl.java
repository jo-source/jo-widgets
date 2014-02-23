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

package org.jowidgets.impl.clipboard;

import org.jowidgets.api.clipboard.IClipboard;
import org.jowidgets.api.clipboard.ITransferable;
import org.jowidgets.api.clipboard.TransferType;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.spi.clipboard.IClipboardListenerSpi;
import org.jowidgets.spi.clipboard.IClipboardSpi;
import org.jowidgets.spi.clipboard.ITransferableSpi;
import org.jowidgets.util.Assert;

public final class ClipbaordImpl extends ClipboardObservable implements IClipboard {

	private final IClipboardSpi clipboardSpi;
	private final IClipboardListenerSpi clipboardListenerSpi;

	private final IUiThreadAccess uiThreadAccess;

	public ClipbaordImpl(final IClipboardSpi clipboard) {
		Assert.paramNotNull(clipboard, "clipboard");
		if (clipboard.getObservable() == null) {
			this.clipboardSpi = new ObservableClipbaordSpi(clipboard);
		}
		else {
			this.clipboardSpi = clipboard;
		}

		this.clipboardListenerSpi = new IClipboardListenerSpi() {
			@Override
			public void clipboardChanged() {
				fireClipboardChanged();
			}
		};
		clipboardSpi.getObservable().addClipboardListener(clipboardListenerSpi);

		this.uiThreadAccess = Toolkit.getUiThreadAccess();
	}

	@Override
	public void setContents(final ITransferable contents) {
		if (contents != null) {
			clipboardSpi.setContents(new TransferableSpiAdapter(contents));
		}
		else {
			clipboardSpi.setContents(null);
		}
	}

	@Override
	public ITransferable getContents() {
		final ITransferableSpi contents = clipboardSpi.getContents();
		if (contents != null) {
			return new TransferableAdapter(contents);
		}
		else {
			return null;
		}
	}

	@Override
	public <JAVA_TYPE> JAVA_TYPE getData(final TransferType<JAVA_TYPE> type) {
		final ITransferable content = getContents();
		if (content != null) {
			if (content.getSupportedTypes().contains(type)) {
				return content.getData(type);
			}
		}
		return null;
	}

	@Override
	public void dispose() {
		checkThread();
		clipboardSpi.getObservable().removeClipboardListener(clipboardListenerSpi);
	}

	private void checkThread() {
		if (!uiThreadAccess.isUiThread()) {
			throw new IllegalStateException("The clipboard must be accessed in the ui thread");
		}
	}

}
