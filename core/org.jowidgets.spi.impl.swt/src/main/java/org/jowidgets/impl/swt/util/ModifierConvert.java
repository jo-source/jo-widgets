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
package org.jowidgets.impl.swt.util;

import java.util.List;

import org.eclipse.swt.SWT;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.util.Assert;

public final class ModifierConvert {

	private ModifierConvert() {};

	public static String acceleratorText(final List<Modifier> modifiers) {
		final StringBuilder result = new StringBuilder();
		for (final Modifier modifier : modifiers) {
			result.append(acceleratorText(modifier));
			result.append("+");
		}
		return result.toString();
	}

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
			return SWT.ALT;
		}
		else if (modifier == Modifier.CTRL) {
			return SWT.CTRL;
		}
		else if (modifier == Modifier.SHIFT) {
			return SWT.SHIFT;
		}

		else {
			throw new IllegalArgumentException("Modifier '" + modifier + "' is unkwon");
		}

	}

	//TODO i18n
	public static String acceleratorText(final Modifier modifier) {
		Assert.paramNotNull(modifier, "modifier");

		if (modifier == Modifier.ALT) {
			return "Alt";
		}
		else if (modifier == Modifier.CTRL) {
			return "Ctrl";
		}
		else if (modifier == Modifier.SHIFT) {
			return "Shift";
		}

		else {
			throw new IllegalArgumentException("Modifier '" + modifier + "' is unkwon");
		}

	}

}
