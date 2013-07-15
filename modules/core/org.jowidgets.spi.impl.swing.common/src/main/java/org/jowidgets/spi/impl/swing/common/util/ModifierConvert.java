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
package org.jowidgets.spi.impl.swing.common.util;

import java.awt.event.InputEvent;
import java.util.List;

import org.jowidgets.common.types.Modifier;
import org.jowidgets.util.Assert;

public final class ModifierConvert {

	private ModifierConvert() {};

	public static int convert(final List<Modifier> modifiers) {
		int result = 0;
		for (final Modifier modifier : modifiers) {
			result += convert(modifier);
		}
		return result;
	}

	public static int convert(final Modifier modifier) {
		Assert.paramNotNull(modifier, "modifier");

		if (modifier == Modifier.ALT) {
			return InputEvent.ALT_DOWN_MASK;
		}
		else if (modifier == Modifier.CTRL) {
			return InputEvent.CTRL_DOWN_MASK;
		}
		else if (modifier == Modifier.SHIFT) {
			return InputEvent.SHIFT_DOWN_MASK;
		}

		else {
			throw new IllegalArgumentException("Modifier '" + modifier + "' is unknown");
		}

	}

}
