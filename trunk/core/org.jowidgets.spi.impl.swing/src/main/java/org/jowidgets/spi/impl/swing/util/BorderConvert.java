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

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.jowidgets.common.types.Border;
import org.jowidgets.spi.impl.swing.widgets.defaults.Colors;

public final class BorderConvert {

	private static final javax.swing.border.Border BORDER = createBorder();
	private static final javax.swing.border.Border EMPTY_BORDER = BorderFactory.createEmptyBorder();

	private BorderConvert() {};

	public static javax.swing.border.Border convert(final Border border) {
		if (border != null) {
			final String title = border.getTitle();
			if (title != null && !title.isEmpty()) {
				final TitledBorder result = BorderFactory.createTitledBorder(title);
				result.setTitleColor(ColorConvert.convert(Colors.BORDER_TITLE));
				return result;
			}
			else {
				return BORDER;
			}
		}
		else {
			return EMPTY_BORDER;
		}

	}

	private static javax.swing.border.Border createBorder() {
		javax.swing.border.Border textFieldBorder = UIManager.getBorder("TextField.border");
		if (textFieldBorder == null) {
			textFieldBorder = BorderFactory.createLineBorder(Color.gray, 1);
		}
		return new CompoundBorder(textFieldBorder, new EmptyBorder(1, 1, 1, 1));
	}
}
