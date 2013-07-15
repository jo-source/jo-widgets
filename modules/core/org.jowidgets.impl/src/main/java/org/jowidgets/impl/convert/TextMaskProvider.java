/*
 * Copyright (c) 2011, Nikolaus Moll, Michael Grossmann
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

package org.jowidgets.impl.convert;

import org.jowidgets.api.mask.ITextMaskBuilder;
import org.jowidgets.common.mask.ITextMask;
import org.jowidgets.impl.mask.TextMaskBuilder;

final class TextMaskProvider {
	private TextMaskProvider() {}

	static ITextMask dateMaskUS() {
		return appendDateMaskUS(new TextMaskBuilder()).build();
	}

	static ITextMask dateTimeMaskUS() {
		final ITextMaskBuilder builder = new TextMaskBuilder();
		appendDateMaskUS(builder);
		builder.addDelimiter(' ');
		appendTimeMask(builder);
		return builder.build();
	}

	static ITextMask dateMaskDE() {
		return appendDateMaskDE(new TextMaskBuilder()).build();
	}

	static ITextMask dateTimeMaskDE() {
		final ITextMaskBuilder builder = new TextMaskBuilder();
		appendDateMaskDE(builder);
		builder.addDelimiter(' ');
		appendTimeMask(builder);
		return builder.build();
	}

	static ITextMask timeMask() {
		return appendTimeMask(new TextMaskBuilder()).build();
	}

	private static ITextMaskBuilder appendDateMaskUS(final ITextMaskBuilder builder) {
		builder.addCharacterMask("[0-1]", '_');
		builder.addNumericMask('_');
		builder.addDelimiter('/');
		builder.addCharacterMask("[0-3]", '_');
		builder.addNumericMask('_');
		builder.addDelimiter('/');
		builder.addNumericMask('_');
		builder.addNumericMask('_');
		builder.addNumericMask('_');
		builder.addNumericMask('_');
		return builder;
	}

	private static ITextMaskBuilder appendDateMaskDE(final ITextMaskBuilder builder) {
		builder.addCharacterMask("[0-3]", '_');
		builder.addNumericMask('_');
		builder.addDelimiter('-');
		builder.addCharacterMask("[0-1]", '_');
		builder.addNumericMask('_');
		builder.addDelimiter('-');
		builder.addNumericMask('_');
		builder.addNumericMask('_');
		builder.addNumericMask('_');
		builder.addNumericMask('_');
		return builder;
	}

	private static ITextMaskBuilder appendTimeMask(final ITextMaskBuilder builder) {
		builder.addCharacterMask("[0-2]", '_');
		builder.addNumericMask('_');
		builder.addDelimiter(':');
		builder.addCharacterMask("[0-5]", '_');
		builder.addNumericMask('_');
		builder.addDelimiter(':');
		builder.addCharacterMask("[0-5]", '_');
		builder.addNumericMask('_');
		return builder;
	}

}
