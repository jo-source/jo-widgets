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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.jowidgets.spi.clipboard.ITransferableSpi;
import org.jowidgets.spi.clipboard.TransferTypeSpi;
import org.jowidgets.util.Assert;

final class TransferableSpiAdapter implements ITransferableSpi {

	private final Collection<TransferTypeSpi> transferTypes;
	private final Map<TransferTypeSpi, Object> dataMap;

	TransferableSpiAdapter(final Transferable contents) {
		Assert.paramNotNull(contents, "contents");

		this.dataMap = new HashMap<TransferTypeSpi, Object>();
		final Set<TransferTypeSpi> typeSet = new LinkedHashSet<TransferTypeSpi>();

		for (final DataFlavor flavor : contents.getTransferDataFlavors()) {
			final TransferTypeSpi transferType = new TransferTypeSpi(flavor.getRepresentationClass());

			try {
				final Object transferData = contents.getTransferData(flavor);
				typeSet.add(transferType);
				dataMap.put(transferType, transferData);
			}
			catch (final Exception e) {
			}

		}

		this.transferTypes = Collections.unmodifiableSet(typeSet);
	}

	@Override
	public Collection<TransferTypeSpi> getSupportedTypes() {
		return transferTypes;
	}

	@Override
	public Object getData(final TransferTypeSpi type) {
		return dataMap.get(type);
	}

}
