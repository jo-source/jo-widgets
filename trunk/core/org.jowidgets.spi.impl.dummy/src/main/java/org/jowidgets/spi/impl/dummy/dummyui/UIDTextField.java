/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.spi.impl.dummy.dummyui;

import javax.swing.text.BadLocationException;

import org.jowidgets.spi.verify.IInputVerifier;

public class UIDTextField extends UIDTextComponent {

	private final IInputVerifier inputVerifier;

	private boolean editable;

	public UIDTextField(final IInputVerifier inputVerifier) {
		this.inputVerifier = inputVerifier;
	}

	public void setEditable(final boolean editable) {
		this.editable = editable;
	}

	public boolean isEditable() {
		return editable;
	}

	@SuppressWarnings("unused")
	private boolean allowInsert(final int offs, final String str) {
		final String currentText = getText();
		if (inputVerifier.verify(currentText, str, offs, offs + str.length())) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unused")
	private boolean allowRemove(final int offs, final int len) throws BadLocationException {
		final String currentText = getText();
		if (inputVerifier.verify(currentText, "", offs, offs + len)) {
			return true;
		}
		return false;
	}

	public void setSelection(final int start, final int end) {
		//TODO LG implement setSelection()
	}

	public int getCaretPosition() {
		// TODO LG implement getCaretPosition
		return 0;
	}

}
