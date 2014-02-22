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

package org.jowidgets.tools.clipboard;

import java.util.Collection;
import java.util.Collections;

import org.jowidgets.api.clipboard.ITransferType;
import org.jowidgets.api.clipboard.ITransferable;
import org.jowidgets.api.clipboard.StringType;

public final class StringTransfer implements ITransferable {

	private static final long serialVersionUID = -4679417069294285597L;

	@SuppressWarnings("unchecked")
	private static final Collection<ITransferType<?>> TRANSFER_TYPES = createTransferTypes();

	private final String data;

	public StringTransfer(final String data) {
		this.data = data;
	}

	@Override
	public Collection<ITransferType<?>> getTransferTypes() {
		return TRANSFER_TYPES;
	}

	@SuppressWarnings("rawtypes")
	private static Collection createTransferTypes() {
		return Collections.unmodifiableSet(Collections.singleton(StringType.getInstance()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <DATA_TYPE> DATA_TYPE getData(final ITransferType<DATA_TYPE> type) {
		if (StringType.isStringType(type)) {
			return (DATA_TYPE) data;
		}
		else {
			return null;
		}
	}

}
