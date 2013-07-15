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

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.common.mask.ICharacterMask;
import org.jowidgets.common.mask.ITextMask;
import org.jowidgets.common.mask.TextMaskMode;
import org.jowidgets.util.Assert;

public final class TextMaskBuilder {

	private final List<CharacterMask> characterMasks;
	private Character defaultPlaceholder;
	private TextMaskMode mode;

	public TextMaskBuilder() {
		this.characterMasks = new LinkedList<CharacterMask>();
		this.mode = TextMaskMode.FULL_MASK;
	}

	public TextMaskBuilder defaultPlaceholder(final char placeholder) {
		this.defaultPlaceholder = Character.valueOf(placeholder);
		return this;
	}

	public TextMaskBuilder setMode(final TextMaskMode mode) {
		Assert.paramNotNull(mode, "mode");
		this.mode = mode;
		return this;
	}

	public TextMaskBuilder addDelimiter(final char placeholder) {
		characterMasks.add(new CharacterMask(true, null, null, Character.valueOf(placeholder)));
		return this;
	}

	public TextMaskBuilder addCharacterMask(final char placeholder) {
		characterMasks.add(new CharacterMask(false, null, null, Character.valueOf(placeholder)));
		return this;
	}

	public TextMaskBuilder addCharacterMask(final String acceptingRegExp, final char placeholder) {
		characterMasks.add(new CharacterMask(false, acceptingRegExp, null, Character.valueOf(placeholder)));
		return this;
	}

	public TextMaskBuilder addCharacterMask(final String acceptingRegExp, final String rejectingRegExp, final char placeholder) {
		characterMasks.add(new CharacterMask(false, acceptingRegExp, rejectingRegExp, Character.valueOf(placeholder)));
		return this;
	}

	public TextMaskBuilder addCharacterMask() {
		characterMasks.add(new CharacterMask(false, null, null, null));
		return this;
	}

	public TextMaskBuilder addCharacterMask(final String acceptingRegExp) {
		characterMasks.add(new CharacterMask(false, acceptingRegExp, null, null));
		return this;
	}

	public TextMaskBuilder addCharacterMask(final String acceptingRegExp, final String rejectingRegExp) {
		characterMasks.add(new CharacterMask(false, acceptingRegExp, rejectingRegExp, null));
		return this;
	}

	public TextMaskBuilder addNumericMask(final char placeholder) {
		return addCharacterMask("[0-9]", placeholder);
	}

	public TextMaskBuilder addNumericMask() {
		return addCharacterMask("[0-9]");
	}

	public TextMaskBuilder addAlphabeticMask(final char placeholder) {
		return addCharacterMask("[A-Z]", placeholder);
	}

	public TextMaskBuilder addAlphaNumericMask(final char placeholder) {
		return addCharacterMask("[A-Z]|[0-9]", placeholder);
	}

	public TextMaskBuilder addAlphabeticMask() {
		return addCharacterMask("[A-Z]");
	}

	public TextMaskBuilder addAlphaNumericMask() {
		return addCharacterMask("[A-Z]|[0-9]");
	}

	public TextMaskBuilder addCharacterMask(final ICharacterMask mask) {
		characterMasks.add(new CharacterMask(
			mask.isReadonly(),
			mask.getAcceptingRegExp(),
			mask.getRejectingRegExp(),
			mask.getPlaceholder()));
		return this;
	}

	public ITextMask build() {
		if (defaultPlaceholder != null) {
			for (final CharacterMask characterMask : characterMasks) {
				characterMask.setDefaultPlaceholder(defaultPlaceholder);
			}
		}
		return new TextMask(characterMasks, mode);
	}

}
