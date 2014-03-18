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

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.jowidgets.spi.clipboard.TransferContainer;
import org.jowidgets.spi.impl.clipboard.Serializer;

public final class ObjectTransfer extends ByteArrayTransfer {

	private static final String MIME_TYPE = TransferContainer.MIME_TYPE;
	private static final int MIME_TYPE_ID = registerType(MIME_TYPE);

	private static final ObjectTransfer INSTANCE = new ObjectTransfer();

	public static Transfer getInstance() {
		return INSTANCE;
	}

	@Override
	protected int[] getTypeIds() {
		return new int[] {MIME_TYPE_ID};
	}

	@Override
	protected String[] getTypeNames() {
		return new String[] {MIME_TYPE};
	}

	@Override
	public void javaToNative(final Object object, final TransferData transferData) {
		if (!validate(object) || !isSupportedType(transferData)) {
			DND.error(DND.ERROR_INVALID_DATA);
		}
		super.javaToNative(Serializer.serialize(object), transferData);
	}

	@Override
	public Object nativeToJava(final TransferData transferData) {
		if (!isSupportedType(transferData)) {
			return null;
		}
		final byte[] bytes = (byte[]) super.nativeToJava(transferData);
		return bytes == null ? null : Serializer.deserialize(bytes);
	}

	@Override
	protected boolean validate(final Object object) {
		if (object == null) {
			return false;
		}
		else {
			return true;
		}
	}

}
