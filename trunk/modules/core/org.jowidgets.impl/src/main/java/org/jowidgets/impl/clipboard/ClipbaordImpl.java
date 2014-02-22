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
import org.jowidgets.api.clipboard.ITransferType;
import org.jowidgets.api.clipboard.ITransferable;
import org.jowidgets.api.clipboard.StringType;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.spi.clipboard.IClipboardListenerSpi;
import org.jowidgets.spi.clipboard.IClipboardSpi;
import org.jowidgets.tools.clipboard.StringTransfer;
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
	public void setContent(final ITransferable content) {
		checkThread();
		if (content == null) {
			clipboardSpi.set(null);
		}
		else if (contentIsOnlyString(content)) {
			clipboardSpi.set(content.getData(StringType.getInstance()));
		}
		else {
			throw new UnsupportedOperationException("Only StringType is supported yet");
		}
	}

	private boolean contentIsOnlyString(final ITransferable content) {
		if (content.getTransferTypes().size() == 1) {
			return StringType.isStringType(content.getTransferTypes().iterator().next());
		}
		return false;
	}

	@Override
	public ITransferable getContent() {
		checkThread();
		final String result = clipboardSpi.get();
		if (result != null) {
			return new StringTransfer(result);
		}
		else {
			return null;
		}
	}

	@Override
	public <DATA_TYPE> DATA_TYPE getData(final ITransferType<DATA_TYPE> type) {
		final ITransferable content = getContent();
		if (content != null) {
			if (content.getTransferTypes().contains(type)) {
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
