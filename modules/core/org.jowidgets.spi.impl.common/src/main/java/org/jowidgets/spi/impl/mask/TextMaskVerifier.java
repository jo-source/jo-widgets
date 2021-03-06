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
import org.jowidgets.common.mask.TextMaskMode;
import org.jowidgets.common.verify.IInputVerifier;
import org.jowidgets.spi.widgets.ITextControlSpi;
import org.jowidgets.util.Assert;

public final class TextMaskVerifier implements IInputVerifier {

    private final ITextControlSpi textControl;
    private final ITextMask textMask;

    private final TextMaskMatcher matcher;

    private boolean onVerify;

    public TextMaskVerifier(final ITextControlSpi textControl, final ITextMask textMask) {
        Assert.paramNotNull(textControl, "textControl");
        Assert.paramNotNull(textMask, "textMask");

        this.textControl = textControl;
        this.textMask = textMask;

        this.onVerify = false;
        this.matcher = new TextMaskMatcher(textMask);
    }

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

        final String prefix = currentValue.substring(0, start);

        final String overrideSuffix = currentValue.substring(
                Math.min(currentValue.length(), end + input.length()),
                currentValue.length());

        final String insertSuffix = currentValue.substring(Math.min(currentValue.length(), end), currentValue.length());

        if (insert) {
            if (matcher.match(0, prefix + input + overrideSuffix).isMatching()) { //override mode possible
                final int maskMatchPos = matcher.match(0, prefix + input).getLastMatchMaskPos();

                int currentMaskPos = maskMatchPos + 1;
                final StringBuilder delimiterBuilder = new StringBuilder();
                while (currentMaskPos < textMask.getLength() && textMask.getCharacterMask(currentMaskPos).isReadonly()) {
                    delimiterBuilder.append(textMask.getCharacterMask(currentMaskPos).getPlaceholder());
                    currentMaskPos++;
                }
                final String delimiter = delimiterBuilder.toString();

                final String suffix = overrideSuffix.substring(
                        Math.min(overrideSuffix.length(), delimiter.length()),
                        overrideSuffix.length());

                final String insertString = currentValue.substring(0, start) + input + delimiter + suffix;

                textControl.setText(insertString);
                int caretPos = start + inputLength;
                int maskPos = maskMatchPos + 1;
                while (maskPos < textMask.getLength() && textMask.getCharacterMask(maskPos).isReadonly()) {
                    caretPos++;
                    maskPos++;
                }
                textControl.setSelection(caretPos, caretPos);
                onVerify = false;

                return false;
            }
            else if (matcher.match(0, prefix + input + insertSuffix).isMatching()) { //insert mode possible
                final int maskMatchPos = matcher.match(0, prefix + input).getLastMatchMaskPos();
                if ((maskMatchPos + 1 < textMask.getLength() && textMask.getCharacterMask(maskMatchPos + 1).isReadonly())) {
                    //determine delimiters to add
                    int maskPos = maskMatchPos + 1;
                    final StringBuilder delimiterBuilder = new StringBuilder();
                    while (maskPos < textMask.getLength() && textMask.getCharacterMask(maskPos).isReadonly()) {
                        delimiterBuilder.append(textMask.getCharacterMask(maskPos).getPlaceholder());
                        maskPos++;
                    }
                    final String delimiter = delimiterBuilder.toString();

                    final String suffix = currentValue.substring(
                            Math.min(currentValue.length(), end + delimiter.length()),
                            currentValue.length());

                    final String insertString = currentValue.substring(0, start) + input + delimiter + suffix;

                    textControl.setText(insertString);
                    final int caretPos = start + inputLength + delimiter.length();
                    textControl.setSelection(caretPos, caretPos);
                    onVerify = false;

                    return false;
                }
                else {//no delimiter insert is needed, just accept
                    onVerify = false;
                    return true;
                }
            }
            else {//neither override nor insert input matches
                onVerify = false;
                return false;
            }
        }
        else if (delete) {
            final MatchResult firstResult = matcher.match(0, prefix);
            final MatchResult secondResult;
            final boolean lastLetter;
            if (end + 1 < currentValue.length()) {
                secondResult = matcher.match(0, currentValue.substring(0, end + 1));
                lastLetter = false;
            }
            else {
                secondResult = matcher.match(0, currentValue.substring(0, end));
                lastLetter = true;
            }
            if (firstResult.isMatching() && secondResult.isMatching()) {
                final int firstMaskPos = firstResult.getLastMatchMaskPos() + 1;
                final int secondMaskPos;
                if (lastLetter) {
                    secondMaskPos = secondResult.getLastMatchMaskPos();
                }
                else {
                    secondMaskPos = secondResult.getLastMatchMaskPos() - 1;
                }

                final StringBuilder replacementBuilder = new StringBuilder();
                if (textMask.getMode() == TextMaskMode.FULL_MASK || !lastLetter) {
                    for (int maskPos = firstMaskPos; maskPos <= secondMaskPos; maskPos++) {
                        final ICharacterMask mask = textMask.getCharacterMask(maskPos);
                        if (mask.getPlaceholder() != null) {
                            replacementBuilder.append(mask.getPlaceholder());
                        }
                    }
                }
                final String replacement = replacementBuilder.toString();
                if (!replacement.isEmpty()) {

                    textControl.setText(prefix + replacement + insertSuffix);
                    final int caretPos = start;
                    textControl.setSelection(caretPos, caretPos);
                    onVerify = false;

                    return false;
                }
                else if (matcher.match(0, prefix + insertSuffix).isMatching()) {
                    onVerify = false;
                    return true;
                }
            }

            onVerify = false;
            return false;
        }
        else if (replace) {
            final MatchResult insertResult = matcher.match(0, prefix + input + insertSuffix);
            if (insertResult.isMatching()) {//does the new text match the pattern
                final int endMaskPos = insertResult.getMaskIndex(prefix.length() + input.length() + insertSuffix.length() - 1) + 1;
                final StringBuilder fillTextBuilder = new StringBuilder();
                if (endMaskPos < textMask.getLength() && textMask.getMode() == TextMaskMode.FULL_MASK) {
                    for (int maskPos = endMaskPos; maskPos < textMask.getLength(); maskPos++) {
                        final ICharacterMask mask = textMask.getCharacterMask(maskPos);
                        if (mask.getPlaceholder() != null) {
                            fillTextBuilder.append(mask.getPlaceholder());
                        }
                    }
                }
                final String fillText = fillTextBuilder.toString();

                final String text = prefix + input + insertSuffix + fillText;

                textControl.setText(text);
                final int caretPos = start + inputLength;
                textControl.setSelection(caretPos, caretPos);
                onVerify = false;

                return false;

            }

            final MatchResult firstResult = matcher.match(0, prefix + input);
            if (!firstResult.isMatching()) {
                onVerify = false;
                return false;
            }

            final MatchResult secondResult;
            final boolean lastLetter;
            if (end + 1 < currentValue.length()) {
                secondResult = matcher.match(0, currentValue.substring(0, end + 1));
                lastLetter = false;
            }
            else {
                secondResult = matcher.match(0, currentValue.substring(0, end));
                lastLetter = true;
            }

            if (firstResult.isMatching() && secondResult.isMatching()) {
                final int firstMaskPos = firstResult.getLastMatchMaskPos() + 1;
                final int secondMaskPos;
                if (lastLetter) {
                    secondMaskPos = secondResult.getLastMatchMaskPos();
                }
                else {
                    secondMaskPos = secondResult.getLastMatchMaskPos() - 1;
                }

                final StringBuilder replacementBuilder = new StringBuilder();
                if (textMask.getMode() == TextMaskMode.FULL_MASK || !lastLetter) {
                    for (int maskPos = firstMaskPos; maskPos <= secondMaskPos; maskPos++) {
                        final ICharacterMask mask = textMask.getCharacterMask(maskPos);
                        if (mask.getPlaceholder() != null) {
                            replacementBuilder.append(mask.getPlaceholder());
                        }
                    }
                }
                final String replacement = input + replacementBuilder.toString();
                if (!replacement.isEmpty()) {

                    textControl.setText(prefix + replacement + insertSuffix);
                    final int caretPos = start + inputLength;
                    textControl.setSelection(caretPos, caretPos);
                    onVerify = false;

                    return false;
                }
                else if (matcher.match(0, prefix + input + insertSuffix).isMatching()) {
                    onVerify = false;
                    return true;
                }
            }

            onVerify = false;
            return false;
        }
        else {
            onVerify = false;
            return false;
        }

    }
}
