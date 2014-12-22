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
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jowidgets.spi.clipboard.ITransferableSpi;
import org.jowidgets.spi.clipboard.TransferContainer;
import org.jowidgets.spi.clipboard.TransferObject;
import org.jowidgets.spi.clipboard.TransferTypeSpi;
import org.jowidgets.spi.impl.clipboard.Serializer;
import org.jowidgets.util.Assert;

final class TransferableSpiAdapter implements ITransferableSpi {

	private static final DataFlavor TRANSFER_CONTAINER_FLAVOR = SwingClipboard.TRANSFER_CONTAINER_FLAVOR;

	private final Map<TransferTypeSpi, Object> dataMap;

	TransferableSpiAdapter(final Transferable contents) {
		Assert.paramNotNull(contents, "contents");

		this.dataMap = new HashMap<TransferTypeSpi, Object>();

		for (final DataFlavor flavor : contents.getTransferDataFlavors()) {
			if (DataFlavor.stringFlavor.equals(flavor)) {
				try {
					final TransferTypeSpi transferType = new TransferTypeSpi(String.class);
					final Object transferData = contents.getTransferData(flavor);
					dataMap.put(transferType, transferData);
				}
				catch (final Exception e) {
				}
			}
			else if (TRANSFER_CONTAINER_FLAVOR.equals(flavor)) {
				try {
					final Object transferData = contents.getTransferData(flavor);
					if (transferData instanceof InputStream) {
						final InputStream inputStream = (InputStream) transferData;
						inputStream.reset();
						final Object deserialized = Serializer.deserialize(inputStream);
						if (deserialized instanceof TransferContainer) {
							final TransferContainer transferContainer = (TransferContainer) deserialized;
							for (final TransferObject transferObject : transferContainer.getTransferObjetcs()) {
								dataMap.put(transferObject.getTransferType(), transferObject.getData());
							}
						}
					}
				}
				catch (final Exception e) {
				}
			}
		}
	}

	@Override
	public Collection<TransferTypeSpi> getSupportedTypes() {
		return dataMap.keySet();
	}

	@Override
	public Object getData(final TransferTypeSpi type) {
		return dataMap.get(type);
	}

}
