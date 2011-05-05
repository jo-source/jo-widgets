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

package org.jowidgets.impl.mask;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.mask.ITextMaskBuilder;
import org.jowidgets.common.mask.ICharacterMask;
import org.jowidgets.common.mask.ITextMask;

public final class TextMaskBuilder implements ITextMaskBuilder {

	private final List<CharacterMask> characterMasks;
	private Character defaultPlaceholder;

	public TextMaskBuilder() {
		this.characterMasks = new LinkedList<CharacterMask>();
	}

	@Override
	public ITextMaskBuilder defaultPlaceholder(final char placeholder) {
		this.defaultPlaceholder = Character.valueOf(placeholder);
		return this;
	}

	@Override
	public ITextMaskBuilder addDelimiterMask(final char placeholder) {
		characterMasks.add(new CharacterMask(true, null, null, Character.valueOf(placeholder)));
		return this;
	}

	@Override
	public ITextMaskBuilder addAcceptingAllMask(final char placeholder) {
		characterMasks.add(new CharacterMask(false, null, null, Character.valueOf(placeholder)));
		return this;
	}

	@Override
	public ITextMaskBuilder addAcceptingMask(final String acceptingRegExp, final char placeholder) {
		characterMasks.add(new CharacterMask(false, acceptingRegExp, null, Character.valueOf(placeholder)));
		return this;
	}

	@Override
	public ITextMaskBuilder addRejectingMask(final String rejectingRegExp, final char placeholder) {
		characterMasks.add(new CharacterMask(false, null, rejectingRegExp, Character.valueOf(placeholder)));
		return this;
	}

	@Override
	public ITextMaskBuilder addCharacterMask(final String acceptingRegExp, final String rejectingRegExp, final char placeholder) {
		characterMasks.add(new CharacterMask(false, acceptingRegExp, rejectingRegExp, Character.valueOf(placeholder)));
		return this;
	}

	@Override
	public ITextMaskBuilder addAcceptingAllMask() {
		characterMasks.add(new CharacterMask(false, null, null, null));
		return this;
	}

	@Override
	public ITextMaskBuilder addAcceptingMask(final String acceptingRegExp) {
		characterMasks.add(new CharacterMask(false, acceptingRegExp, null, null));
		return this;
	}

	@Override
	public ITextMaskBuilder addRejectingMask(final String rejectingRegExp) {
		characterMasks.add(new CharacterMask(false, null, rejectingRegExp, null));
		return this;
	}

	@Override
	public ITextMaskBuilder addCharacterMask(final String acceptingRegExp, final String rejectingRegExp) {
		characterMasks.add(new CharacterMask(false, acceptingRegExp, rejectingRegExp, null));
		return this;
	}

	@Override
	public ITextMaskBuilder addNumericMask(final char placeholder) {
		return addAcceptingMask("[0-9]", placeholder);
	}

	@Override
	public ITextMaskBuilder addNumericMask() {
		return addAcceptingMask("[0-9]");
	}

	@Override
	public ITextMaskBuilder addAlphabeticMask(final char placeholder) {
		return addAcceptingMask("[A-Z]", placeholder);
	}

	@Override
	public ITextMaskBuilder addAlphaNumericMask(final char placeholder) {
		return addAcceptingMask("[A-Z]|[0-9]", placeholder);
	}

	@Override
	public ITextMaskBuilder addAlphabeticMask() {
		return addAcceptingMask("[A-Z]");
	}

	@Override
	public ITextMaskBuilder addAlphaNumericMask() {
		return addAcceptingMask("[A-Z]|[0-9]");
	}

	@Override
	public ITextMaskBuilder addCharacterMask(final ICharacterMask mask) {
		characterMasks.add(new CharacterMask(
			mask.isReadonly(),
			mask.getAcceptingRegExp(),
			mask.getRejectingRegExp(),
			mask.getPlaceholder()));
		return this;
	}

	@Override
	public ITextMask build() {
		if (defaultPlaceholder != null) {
			for (final CharacterMask characterMask : characterMasks) {
				characterMask.setDefaultPlaceholder(defaultPlaceholder);
			}
		}
		return new TextMask(characterMasks);
	}

}
