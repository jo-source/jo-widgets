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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.common.mask.ICharacterMask;
import org.jowidgets.common.mask.ITextMask;

public final class TextMaskMatcher {

	private final ITextMask textMask;

	public TextMaskMatcher(final ITextMask textMask) {
		super();
		this.textMask = textMask;
	}

	public MatchResult match(final int startMaskPos, final String input) {
		if (input == null || input.isEmpty()) {
			final List<Integer> matchResult = Collections.emptyList();
			return new MatchResult(matchResult, 0, true);
		}

		final List<Integer> matchResult = new LinkedList<Integer>();

		final List<Integer> matchPositions = getMatchPositions(startMaskPos, input.charAt(0));

		if (matchPositions.isEmpty()) {
			return new MatchResult(matchResult, input.length(), false);
		}
		else {
			if (input.length() > 1) {
				int bestMatchPos = 0;
				MatchResult bestMatchResult = null;
				int bestMatchIndex = Integer.MIN_VALUE;
				for (final Integer matchPos : matchPositions) {
					final MatchResult result = match((matchPos.intValue() + 1), input.substring(1, input.length()));
					if (result.isMatching()) {
						matchResult.add(Integer.valueOf(matchPos));
						for (int i = 0; i < input.length() - 1; i++) {
							matchResult.add(Integer.valueOf(result.getMaskIndex(i)));
						}
						return new MatchResult(matchResult, input.length(), true);
					}
					else if (result.getLastMatch() > bestMatchIndex) {
						bestMatchPos = matchPos;
						bestMatchResult = result;
						bestMatchIndex = result.getLastMatch();
					}
				}
				matchResult.add(Integer.valueOf(bestMatchPos));
				for (int i = 0; i < input.length() - 1; i++) {
					matchResult.add(Integer.valueOf(bestMatchResult.getMaskIndex(i)));
				}
				return new MatchResult(matchResult, input.length(), false);
			}
			else {//input has one character
				matchResult.add(Integer.valueOf(matchPositions.get(0)));
				return new MatchResult(matchResult, input.length(), true);
			}
		}

	}

	private List<Integer> getMatchPositions(final int startMaskPos, final Character character) {
		final String characterString = String.valueOf(character.charValue());
		final List<Integer> result = new LinkedList<Integer>();
		for (int maskIndex = startMaskPos; maskIndex < textMask.getLength(); maskIndex++) {
			final ICharacterMask mask = textMask.getCharacterMask(maskIndex);
			boolean accept = !mask.isReadonly();
			if (mask.getAcceptingRegExp() != null) {
				accept = accept && characterString.matches(mask.getAcceptingRegExp());
			}
			if (mask.getRejectingRegExp() != null) {
				accept = accept && !characterString.matches(mask.getRejectingRegExp());
			}
			if (mask.getPlaceholder() != null) {
				accept = accept || character.equals(mask.getPlaceholder());
			}
			if (accept) {
				result.add(Integer.valueOf(maskIndex));
			}

			if (mask.getPlaceholder() != null) {//the field is mandatory, so no more positions could be considered
				break;
			}
		}
		return result;
	}
}
