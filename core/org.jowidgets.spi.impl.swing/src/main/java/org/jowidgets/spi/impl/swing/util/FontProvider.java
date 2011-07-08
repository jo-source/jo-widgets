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
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.spi.impl.swing.util;

import java.awt.Font;

import org.jowidgets.common.types.Markup;
import org.jowidgets.util.Assert;

public final class FontProvider {

	private FontProvider() {};

	public static Font deriveFont(final Font baseFont, final Markup markup) {
		Assert.paramNotNull(baseFont, "baseFont");
		Assert.paramNotNull(markup, "markup");
		return deriveFont(baseFont, null, null, markup);
	}

	public static Font deriveFont(final Font baseFont, final String fontName) {
		Assert.paramNotNull(baseFont, "baseFont");
		Assert.paramNotNull(fontName, "fontName");
		return deriveFont(baseFont, fontName, null, null);
	}

	public static Font deriveFont(final Font baseFont, final int size) {
		Assert.paramNotNull(baseFont, "baseFont");
		return deriveFont(baseFont, null, Integer.valueOf(size), null);
	}

	public static Font deriveFont(final Font baseFont, final String newFontName, final Integer newSize, final Markup newMarkup) {
		Assert.paramNotNull(baseFont, "baseFont");

		Integer newStyle = null;
		if (Markup.DEFAULT.equals(newMarkup)) {
			newStyle = Integer.valueOf(Font.PLAIN);
		}
		else if (Markup.STRONG.equals(newMarkup)) {
			newStyle = Integer.valueOf(Font.BOLD);
		}
		else if (Markup.EMPHASIZED.equals(newMarkup)) {
			newStyle = Integer.valueOf(Font.ITALIC);
		}

		final String fontName = newFontName != null ? newFontName : baseFont.getName();
		final int size = newSize != null ? (newSize.intValue() * 100 / 72) : baseFont.getSize();
		final int style = newStyle != null ? newStyle.intValue() : baseFont.getStyle();

		return new Font(fontName, style, size);
	}

}
