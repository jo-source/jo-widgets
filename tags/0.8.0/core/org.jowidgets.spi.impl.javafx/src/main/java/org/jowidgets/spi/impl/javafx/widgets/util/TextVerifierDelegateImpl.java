/*
 * Copyright (c) 2012, grossmann
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

import java.awt.Toolkit;

import org.jowidgets.common.mask.ITextMask;
import org.jowidgets.common.types.InputChangeEventPolicy;
import org.jowidgets.common.verify.IInputVerifier;
import org.jowidgets.spi.impl.controller.InputObservable;
import org.jowidgets.spi.impl.mask.TextMaskVerifierFactory;
import org.jowidgets.spi.widgets.ITextControlSpi;
import org.jowidgets.util.IProvider;

public final class TextVerifierDelegateImpl implements ITextVerifierDelegate {

	private final InputObservable inputObservable;
	private final InputChangeEventPolicy inputChangeEventPolicy;
	private final Integer maxLength;
	private final IInputVerifier maskVerifier;
	private final IProvider<Boolean> programmaticChangeProvider;

	public TextVerifierDelegateImpl(
		final ITextMask textMask,
		final InputObservable inputObservable,
		final InputChangeEventPolicy inputChangeEventPolicy,
		final Integer maxLength,
		final ITextControlSpi textControl,
		final IProvider<Boolean> programmaticChangeProvider) {

		this.inputObservable = inputObservable;
		this.inputChangeEventPolicy = inputChangeEventPolicy;
		this.maxLength = maxLength;
		this.maskVerifier = TextMaskVerifierFactory.create(textControl, textMask);
		this.programmaticChangeProvider = programmaticChangeProvider;
	}

	@Override
	public boolean doDeleteText(final int offs, final int len, final String currentText, final int currentLength) {
		if (maskVerifier == null || maskVerifier.verify(currentText, "", offs, currentLength) || programmaticChangeProvider.get()) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean doReplaceText(
		final int offset,
		final int length,
		final String text,
		final String currentText,
		final int currentLength) {

		if (maxLength != null) {
			int entireLength = currentText != null ? currentText.length() : 0;
			entireLength = entireLength + (text != null ? text.length() : 0);
			if (entireLength > maxLength.intValue()) {
				Toolkit.getDefaultToolkit().beep();
				return false;
			}
		}

		if (maskVerifier == null
			|| maskVerifier.verify(currentText, text, offset, offset + length)
			|| programmaticChangeProvider.get()) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void fireInputChanged(final String currentText) {
		if (inputChangeEventPolicy == InputChangeEventPolicy.ANY_CHANGE) {
			inputObservable.fireInputChanged(currentText);
		}
	}

}
