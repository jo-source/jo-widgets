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

package org.jowidgets.spi.clipboard;

import java.io.Serializable;

import org.jowidgets.util.Assert;

public final class TransferObject implements Serializable {

	public static final String MIME_TYPE = "application/x-jowidgets-transfer-container";

	private static final long serialVersionUID = 7633172392759619262L;

	private final TransferTypeSpi transferType;
	private final Object data;

	public TransferObject(final TransferTypeSpi transferType, final Object data) {
		Assert.paramNotNull(transferType, "transferType");
		this.transferType = transferType;
		this.data = data;
	}

	public TransferTypeSpi getTransferType() {
		return transferType;
	}

	public Object getData() {
		return data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((transferType == null) ? 0 : transferType.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TransferObject)) {
			return false;
		}
		final TransferObject other = (TransferObject) obj;
		if (data == null) {
			if (other.data != null) {
				return false;
			}
		}
		else if (!data.equals(other.data)) {
			return false;
		}
		if (transferType == null) {
			if (other.transferType != null) {
				return false;
			}
		}
		else if (!transferType.equals(other.transferType)) {
			return false;
		}
		return true;
	}

}
