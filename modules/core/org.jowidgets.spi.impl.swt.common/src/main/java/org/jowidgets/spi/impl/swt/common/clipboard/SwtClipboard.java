/*
 * Copyright (c) 2014, Michael
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

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.jowidgets.spi.clipboard.IClipboardObservableSpi;
import org.jowidgets.spi.clipboard.IClipboardSpi;
import org.jowidgets.spi.clipboard.ITransferableSpi;
import org.jowidgets.util.IProvider;

public final class SwtClipboard implements IClipboardSpi {

	private static final Transfer TEXT_TRANSFER = TextTransfer.getInstance();
	private static final Transfer[] TEXT_TRANSFERS = new Transfer[] {TEXT_TRANSFER};

	private final IProvider<Display> displayProvider;

	private Clipboard systemClipboard;

	public SwtClipboard(final IProvider<Display> displayProvider) {
		this.displayProvider = displayProvider;
	}

	//	@Override
	//	public String get() {
	//		final Clipboard clipboard = getClipboard();
	//		if (clipboard != null) {
	//			final Object contents = clipboard.getContents(TEXT_TRANSFER);
	//			if (contents instanceof String) {
	//				return (String) contents;
	//			}
	//		}
	//		return null;
	//	}
	//
	//	@Override
	//	public void set(final String data) {
	//		final Clipboard clipboard = getClipboard();
	//		if (clipboard != null) {
	//			clipboard.setContents(new String[] {data}, TEXT_TRANSFERS);
	//		}
	//	}

	@Override
	public void setContents(final ITransferableSpi contents) {
		// TODO MG must be implemented
	}

	@Override
	public ITransferableSpi getContents() {
		// TODO MG must be implemented
		return null;
	}

	@Override
	public IClipboardObservableSpi getObservable() {
		return null;
	}

	@Override
	public void dispose() {
		systemClipboard.dispose();
	}

	private Clipboard getClipboard() {
		if (systemClipboard == null && displayProvider.get() != null) {
			systemClipboard = new Clipboard(displayProvider.get());
		}
		return systemClipboard;
	}

}
