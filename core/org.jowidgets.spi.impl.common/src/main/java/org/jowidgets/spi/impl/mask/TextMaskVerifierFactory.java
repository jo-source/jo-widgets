/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.spi.impl.mask;

import org.jowidgets.common.mask.ICharacterMask;
import org.jowidgets.common.mask.ITextMask;
import org.jowidgets.common.threads.IUiThreadAccessCommon;
import org.jowidgets.common.verify.IInputVerifier;
import org.jowidgets.spi.widgets.ITextControlSpi;

public final class TextMaskVerifierFactory {

	private TextMaskVerifierFactory() {}

	public static IInputVerifier create(
		final ITextControlSpi textControl,
		final ITextMask textMask,
		final IUiThreadAccessCommon uiThreadAccess) {
		if (textMask == null) {
			return null;
		}

		return new IInputVerifier() {

			private boolean onVerify = false;

			@Override
			public boolean verify(final String currentValue, final String input, final int start, final int end) {
				if (onVerify) {
					return true;
				}
				onVerify = true;
				if (start == 0 && end == 0 && input != null && input.length() == textMask.getLength()) {
					onVerify = false;
					return true;
				}
				if (start == end && input != null && input.length() == 1 && start < textMask.getLength()) {
					final ICharacterMask mask = textMask.getCharacterMask(start);
					boolean accept = !mask.isReadonly() || Character.valueOf(input.charAt(0)).equals(mask.getPlaceholder());
					if (mask.getAcceptingRegExp() != null) {
						accept = accept && input.matches(mask.getAcceptingRegExp());
					}
					if (mask.getRejectingRegExp() != null) {
						accept = accept && !input.matches(mask.getRejectingRegExp());
					}
					if (accept) {
						uiThreadAccess.invokeLater(new Runnable() {
							@Override
							public void run() {
								String text = currentValue;
								text = text.substring(0, start) + input + text.substring(start + 1, text.length());
								textControl.setText(text);
								int caretPos = start + 1;
								while (caretPos < textMask.getLength() && textMask.getCharacterMask(caretPos).isReadonly()) {
									caretPos++;
								}
								int caretEnd = caretPos;
								if (caretPos + 1 < textMask.getLength()) {
									caretEnd++;
								}
								textControl.setSelection(caretPos, caretPos);
								onVerify = false;
							}
						});
					}
					else {
						onVerify = false;
					}
				}
				//TODO MG more mask formatter
				//else if replace
				else {
					onVerify = false;
				}
				return false;
			}

		};
	}
}
