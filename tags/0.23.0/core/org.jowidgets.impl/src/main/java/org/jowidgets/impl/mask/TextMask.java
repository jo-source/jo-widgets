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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jowidgets.common.mask.ICharacterMask;
import org.jowidgets.common.mask.ITextMask;
import org.jowidgets.common.mask.TextMaskMode;
import org.jowidgets.util.Assert;

final class TextMask implements ITextMask {

	private final ArrayList<ICharacterMask> characterMasks;
	private final TextMaskMode mode;

	TextMask(final List<? extends ICharacterMask> characterMasks, final TextMaskMode mode) {
		Assert.paramNotNull(characterMasks, "characterMasks");
		Assert.paramNotNull(mode, "mode");
		this.characterMasks = new ArrayList<ICharacterMask>(characterMasks);
		this.mode = mode;
	}

	@Override
	public int getLength() {
		return characterMasks.size();
	}

	@Override
	public ICharacterMask getCharacterMask(final int index) {
		return characterMasks.get(index);
	}

	@Override
	public TextMaskMode getMode() {
		return mode;
	}

	@Override
	public String getPlaceholder() {
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < getLength(); i++) {
			final ICharacterMask mask = getCharacterMask(i);
			if (mask.getPlaceholder() != null) {
				result.append(mask.getPlaceholder().charValue());
			}
		}
		return result.toString();
	}

	@Override
	public Iterator<ICharacterMask> iterator() {
		return characterMasks.iterator();
	}

}
