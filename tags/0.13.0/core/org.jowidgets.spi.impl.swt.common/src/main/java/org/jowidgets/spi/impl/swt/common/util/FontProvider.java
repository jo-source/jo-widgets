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
package org.jowidgets.spi.impl.swt.common.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.jowidgets.common.types.Markup;
import org.jowidgets.spi.impl.swt.common.font.FontCache;
import org.jowidgets.spi.impl.swt.common.font.FontDataKey;
import org.jowidgets.spi.impl.swt.common.font.IFontCache;
import org.jowidgets.util.Assert;

public final class FontProvider {

	private static final IFontCache FONT_CACHE = new FontCache();

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

	public static Font deriveFont(final Font baseFont, final int height) {
		Assert.paramNotNull(baseFont, "baseFont");
		return deriveFont(baseFont, null, Integer.valueOf(height), null);
	}

	public static Font deriveFont(final Font baseFont, final String newFontName, final Integer newHeight, final Markup newMarkup) {
		Assert.paramNotNull(baseFont, "baseFont");

		Integer newStyle = null;
		if (Markup.DEFAULT.equals(newMarkup)) {
			newStyle = Integer.valueOf(SWT.NORMAL);
		}
		else if (Markup.STRONG.equals(newMarkup)) {
			newStyle = Integer.valueOf(SWT.BOLD);
		}
		else if (Markup.EMPHASIZED.equals(newMarkup)) {
			newStyle = Integer.valueOf(SWT.ITALIC);
		}
		else if (newMarkup != null) {
			throw new IllegalArgumentException("The markup '" + newMarkup + "' is unknown.");
		}

		final FontData[] oldFontData = baseFont.getFontData();
		final FontData[] newFontData = new FontData[oldFontData.length];

		for (int i = 0; i < oldFontData.length; i++) {
			final String fontName = newFontName != null ? newFontName : oldFontData[i].getName();
			final int fontHeight = newHeight != null ? newHeight.intValue() : oldFontData[i].getHeight();
			final int style = newStyle != null ? newStyle.intValue() : oldFontData[i].getStyle();
			newFontData[i] = new FontData(fontName, fontHeight, style);
		}

		return FONT_CACHE.getFont(new FontDataKey(newFontData));
	}
}
