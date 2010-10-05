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
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.impl.swing.factory.internal.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import org.jowidgets.api.validation.ITextInputValidator;
import org.jowidgets.api.validation.ValidationMessageType;

public class ValidatedInputDocument extends PlainDocument {

	private static final long serialVersionUID = 6900501331487160350L;

	private final JTextComponent textComponent;
	private final ITextInputValidator validator;

	public ValidatedInputDocument(final JTextComponent textComponent, final ITextInputValidator validator) {
		super();
		this.textComponent = textComponent;
		this.validator = validator;
	}

	@Override
	public void insertString(final int offs, final String str, final AttributeSet a) throws BadLocationException {

		final String currentText = textComponent.getText();
		final StringBuilder newTextBuilder = new StringBuilder();
		if (currentText != null) {
			if (offs > 0) {
				newTextBuilder.append(currentText.substring(0, offs));
			}
			newTextBuilder.append(str);
			newTextBuilder.append(currentText.substring(offs, currentText.length()));
		}
		if (ValidationMessageType.OK.equals(validator.isCompletableToValid(newTextBuilder.toString()).getType())) {
			super.insertString(offs, str, a);
		}
	}

}
