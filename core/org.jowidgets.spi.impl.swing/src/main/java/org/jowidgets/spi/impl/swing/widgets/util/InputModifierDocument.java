/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.spi.impl.swing.widgets.util;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import org.jowidgets.common.verify.IInputVerifier;
import org.jowidgets.spi.impl.controler.InputObservable;
import org.jowidgets.util.Assert;

public class InputModifierDocument extends PlainDocument {

	private static final InputObservable DUMMY_INPUT_OBSERVABLE = new InputObservable();

	private static final long serialVersionUID = 6900501331487160350L;

	private final JTextComponent textComponent;
	private final IInputVerifier inputVerifier;
	private InputObservable inputObservable;
	private final Integer maxLength;

	public InputModifierDocument(
		final JTextComponent textComponent,
		final IInputVerifier inputVerifier,
		final InputObservable inputObservable,
		final Integer maxLength) {
		super();
		Assert.paramNotNull(textComponent, "textComponent");

		this.textComponent = textComponent;
		this.inputVerifier = inputVerifier;
		this.maxLength = maxLength;
		setInputObservable(inputObservable);
	}

	@Override
	public void remove(final int offs, final int len) throws BadLocationException {
		final String currentText = textComponent.getText();
		if (inputVerifier == null || inputVerifier.verify(currentText, "", offs, offs + len)) {
			super.remove(offs, len);
			inputObservable.fireInputChanged(textComponent.getText());
		}
	}

	@Override
	public void replace(final int offset, final int length, final String text, final AttributeSet attrs) throws BadLocationException {
		final String currentText = textComponent.getText();

		if (maxLength != null) {
			int entireLength = currentText != null ? currentText.length() : 0;
			entireLength = entireLength + (text != null ? text.length() : 0);
			if (entireLength > maxLength.intValue()) {
				Toolkit.getDefaultToolkit().beep();
				return;
			}
		}

		if (inputVerifier == null || inputVerifier.verify(currentText, text, offset, offset + length)) {
			super.replace(offset, length, text, attrs);
			inputObservable.fireInputChanged(textComponent.getText());
		}
	}

	public void setInputObservable(final InputObservable inputObservable) {
		if (inputObservable == null) {
			this.inputObservable = DUMMY_INPUT_OBSERVABLE;
		}
		else {
			this.inputObservable = inputObservable;
		}
	}

}
