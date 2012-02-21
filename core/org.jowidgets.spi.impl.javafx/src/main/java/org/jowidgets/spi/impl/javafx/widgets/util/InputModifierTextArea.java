/*
 * Copyright (c) 2012, dabaukne
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

package org.jowidgets.spi.impl.javafx.widgets.util;

import javafx.scene.control.TextArea;

public class InputModifierTextArea extends TextArea implements IInputModfierField {

	private ITextVerifierDelegate textVerifierDelegate;

	public InputModifierTextArea() {
		super();
	}

	@Override
	public void deleteText(final int offs, final int len) {
		if (textVerifierDelegate.doDeleteText(offs, len, getText(), getLength())) {
			super.deleteText(offs, len);
			textVerifierDelegate.fireInputChanged(getText());
		}
	}

	@Override
	public void replaceText(final int offset, final int length, final String text) {
		if (textVerifierDelegate.doReplaceText(offset, length, text, getText(), getLength())) {
			super.replaceText(offset, length, text);
			textVerifierDelegate.fireInputChanged(getText());
		}
	}

	@Override
	public void setTextVerifierDelegate(final ITextVerifierDelegate textVerifierDelegate) {
		this.textVerifierDelegate = textVerifierDelegate;
	}

}
