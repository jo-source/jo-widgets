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
package org.jowidgets.api.color;

import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.color.IColorConstant;

public enum Colors implements IColorConstant {

	DEFAULT(new ColorValue(0, 0, 0)),
	ERROR(new ColorValue(220, 0, 0)),
	WARNING(new ColorValue(209, 124, 34)),
	STRONG(new ColorValue(0, 70, 213)),
	DISABLED(new ColorValue(130, 130, 130)),
	DEFAULT_TABLE_EVEN_BACKGROUND_COLOR(new ColorValue(222, 235, 235)),
	SELECTED_BACKGROUND(new ColorValue(16, 63, 149)),

	BLACK(new ColorValue(0, 0, 0)),
	WHITE(new ColorValue(255, 255, 255)),
	DARK_GREY(new ColorValue(80, 80, 80)),
	GREY(new ColorValue(140, 140, 140)),
	LIGHT_GREY(new ColorValue(225, 225, 225)),
	GREEN(new ColorValue(7, 106, 3));

	private ColorValue colorValue;

	private Colors(final ColorValue colorValue) {
		this.colorValue = colorValue;
	}

	@Override
	public ColorValue getDefaultValue() {
		return colorValue;
	}

}
