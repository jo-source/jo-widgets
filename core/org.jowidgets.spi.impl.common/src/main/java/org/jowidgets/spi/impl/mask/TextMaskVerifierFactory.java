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
			public boolean verify(final String currentValue, String input, final int start, final int end) {

				if (onVerify) {
					return true;
				}
				onVerify = true;

				if (input == null) {
					input = "";
				}

				final int inputLength = input.length();

				final boolean delete = start != end && input.isEmpty();
				final boolean replace = start != end && !input.isEmpty();
				final boolean insert = !delete && !replace;

				final String insertSuffix = currentValue.substring(Math.min(currentValue.length(), end), currentValue.length());

				final String overrideSuffix = currentValue.substring(
						Math.min(currentValue.length(), end + input.length()),
						currentValue.length());

				//first check if the prefix (text from pos 0 inclusive input) matches
				final String prefix = currentValue.substring(0, start) + input;
				final Integer matchPos = match(0, prefix, insert);
				if (matchPos == null) {
					onVerify = false;
					return false;
				}
				//after prefix matches, check if insertion is possible
				else if (insertSuffix.length() == 0
					|| textMask.getLength() - (matchPos.intValue() + 1) >= insertSuffix.length()
					&& match(matchPos.intValue() + 1, insertSuffix, insert) != null) {

					final int maskPos = matchPos.intValue() + 1;

					if (replace || delete) {
						final int deletedCharacterCount = end - (start + input.length());
						final StringBuilder replacementBuilder = new StringBuilder();
						for (int i = 0; i < deletedCharacterCount; i++) {
							if (maskPos + i < textMask.getLength()) {
								final ICharacterMask mask = textMask.getCharacterMask(maskPos + i);
								if (mask.getPlaceholder() != null) {
									replacementBuilder.append(mask.getPlaceholder());
								}
							}
							else {
								break;
							}
						}
						final String replacement = input + replacementBuilder.toString();
						uiThreadAccess.invokeLater(new Runnable() {
							@Override
							public void run() {
								textControl.setText(currentValue.substring(0, start) + replacement + insertSuffix);
								int caretPos = start + inputLength;
								if (replace) {
									while (caretPos < textMask.getLength() && textMask.getCharacterMask(caretPos).isReadonly()) {
										caretPos++;
									}
								}
								textControl.setSelection(caretPos, caretPos);
								onVerify = false;
							}
						});
						return false;
					}
					else if (maskPos < textMask.getLength() && textMask.getCharacterMask(maskPos).isReadonly()) {
						//determine delimiters to add
						int pos = maskPos;
						final StringBuilder delimiterBuilder = new StringBuilder();
						while (pos < textMask.getLength() && textMask.getCharacterMask(pos).isReadonly()) {
							delimiterBuilder.append(textMask.getCharacterMask(pos).getPlaceholder());
							pos++;
						}
						final String delimiter = delimiterBuilder.toString();

						final String suffix = currentValue.substring(
								Math.min(currentValue.length(), end + delimiter.length()),
								currentValue.length());

						final String insertString = currentValue.substring(0, start) + input + delimiter + suffix;

						uiThreadAccess.invokeLater(new Runnable() {
							@Override
							public void run() {
								textControl.setText(insertString);
								final int caretPos = start + inputLength + delimiter.length();
								textControl.setSelection(caretPos, caretPos);
								onVerify = false;
							}
						});
						return false;
					}
					else {
						onVerify = false;
						return true;
					}
				}
				//if no insertion is possible, check if input with override mode is possible
				else if (insert && match(matchPos.intValue() + 1, overrideSuffix, insert) != null) {
					final String overrideString = currentValue.substring(0, start) + input + overrideSuffix;
					uiThreadAccess.invokeLater(new Runnable() {
						@Override
						public void run() {
							textControl.setText(overrideString);
							int caretPos = start + inputLength;
							while (caretPos < textMask.getLength() && textMask.getCharacterMask(caretPos).isReadonly()) {
								caretPos++;
							}
							textControl.setSelection(caretPos, caretPos);
							onVerify = false;
						}
					});
					return false;
				}
				//if neither insert nor override is possible, reject the input
				else {
					onVerify = false;
					return false;
				}

			}

			private Integer match(final int startPos, final String string, final boolean insert) {
				boolean match = false;
				int lastMatch = startPos - 1;
				if (string.length() > 0) {
					final char[] stringArray = string.toCharArray();
					int letterIndex = 0;
					String currentLetter = String.valueOf(stringArray[letterIndex]);
					for (int maskIndex = startPos; maskIndex < textMask.getLength(); maskIndex++) {
						final ICharacterMask mask = textMask.getCharacterMask(maskIndex);
						boolean accept = !mask.isReadonly();
						if (mask.getAcceptingRegExp() != null) {
							accept = accept && currentLetter.matches(mask.getAcceptingRegExp());
						}
						if (mask.getRejectingRegExp() != null) {
							accept = accept && !currentLetter.matches(mask.getRejectingRegExp());
						}
						accept = accept || Character.valueOf(currentLetter.charAt(0)).equals(mask.getPlaceholder());
						if (accept) {
							lastMatch = maskIndex;
							letterIndex++;
							if (letterIndex < stringArray.length) {
								currentLetter = String.valueOf(stringArray[letterIndex]);
							}
							else {
								break;
							}
						}
						else if (insert && mask.isReadonly()) {
							return null;
						}
					}
					if (letterIndex == stringArray.length) {
						match = true;
						return Integer.valueOf(lastMatch);
					}
				}

				if (string.length() != 0 && !match) {
					return null;
				}
				else {
					return Integer.valueOf(lastMatch);
				}
			}

		};
	}
}
