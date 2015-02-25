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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.jowidgets.api.clipboard.ITransferable;
import org.jowidgets.api.clipboard.TransferType;
import org.jowidgets.spi.clipboard.ITransferableSpi;
import org.jowidgets.spi.clipboard.TransferTypeSpi;
import org.jowidgets.util.Assert;

final class TransferableAdapter implements ITransferable {

	private final Collection<TransferType<?>> supportedTypes;
	private final Map<TransferType<?>, Object> dataMap;

	TransferableAdapter(final ITransferableSpi contents) {
		Assert.paramNotNull(contents, "contents");

		final Set<TransferType<?>> typesSet = new LinkedHashSet<TransferType<?>>();
		this.dataMap = new HashMap<TransferType<?>, Object>();

		for (final TransferTypeSpi transferTypeSpi : contents.getSupportedTypes()) {
			@SuppressWarnings("unchecked")
			final TransferType<?> transferType = new TransferType<Object>((Class<Object>) transferTypeSpi.getJavaType());
			typesSet.add(transferType);
			dataMap.put(transferType, contents.getData(transferTypeSpi));
		}

		this.supportedTypes = Collections.unmodifiableSet(typesSet);
	}

	@Override
	public Collection<TransferType<?>> getSupportedTypes() {
		return supportedTypes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <JAVA_TYPE> JAVA_TYPE getData(final TransferType<JAVA_TYPE> type) {
		Assert.paramNotNull(type, "type");
		return (JAVA_TYPE) dataMap.get(type);
	}

}
