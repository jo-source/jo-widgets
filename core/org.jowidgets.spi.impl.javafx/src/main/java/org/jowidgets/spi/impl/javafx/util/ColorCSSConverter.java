/*
 * Copyright (c) 2012, David Bauknecht
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

package org.jowidgets.spi.impl.javafx.util;

import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.color.IColorConstant;

public final class ColorCSSConverter {

	private ColorCSSConverter() {

	}

	public static IColorConstant cssToColor(final String csscode) {
		final String hex = csscode.substring(csscode.indexOf("#") + 1, csscode.indexOf(";"));
		final int r = Integer.parseInt(hex.substring(0, 1), 16);
		final int g = Integer.parseInt(hex.substring(2, 3), 16);
		final int b = Integer.parseInt(hex.substring(4, 5), 16);

		return new ColorValue(r, g, b);
	}

	public static String colorToCSS(final IColorConstant color) {
		if (color != null) {

			final String colorString = Integer.toHexString(0x100 | color.getDefaultValue().getRed()).substring(1)
				+ ""
				+ Integer.toHexString(0x100 | color.getDefaultValue().getGreen()).substring(1)
				+ ""
				+ Integer.toHexString(0x100 | color.getDefaultValue().getBlue()).substring(1);

			return colorString;
		}
		return "";
	}

}
