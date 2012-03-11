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
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.util.Assert;

public final class AcceleratorConvert {

	private AcceleratorConvert() {};

	public static String acceleratorText(final Accelerator accelerator) {
		final StringBuilder result = new StringBuilder();
		for (final Modifier modifier : accelerator.getModifier()) {
			result.append(acceleratorText(modifier));
			result.append("+"); //$NON-NLS-1$
		}
		final Character character = accelerator.getCharacter();
		if (character != null) {
			result.append(character.charValue());
		}
		else {
			result.append(accelerator.getVirtualKey().getLabel());
		}
		return result.toString();
	}

	public static int convert(final Accelerator accelerator) {
		int result;

		final Character character = accelerator.getCharacter();
		if (character != null) {
			result = character.charValue();
		}
		else {
			result = VirtualKeyConvert.convert(accelerator.getVirtualKey());
		}

		for (final Modifier modifier : accelerator.getModifier()) {
			result += convert(modifier);
		}
		return result;
	}

	public static int convert(final Modifier modifier) {
		Assert.paramNotNull(modifier, "modifier"); //$NON-NLS-1$

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
			throw new IllegalArgumentException("Modifier '" + modifier + "' is unknown"); //$NON-NLS-1$ //$NON-NLS-2$
		}

	}

	private static String acceleratorText(final Modifier modifier) {
		Assert.paramNotNull(modifier, "modifier"); //$NON-NLS-1$

		if (modifier == Modifier.ALT) {
			return Messages.getString("AcceleratorConvert.alt"); //$NON-NLS-1$
		}
		else if (modifier == Modifier.CTRL) {
			return Messages.getString("AcceleratorConvert.ctrl"); //$NON-NLS-1$
		}
		else if (modifier == Modifier.SHIFT) {
			return Messages.getString("AcceleratorConvert.shift"); //$NON-NLS-1$
		}

		else {
			throw new IllegalArgumentException("Modifier '" + modifier + "' is unknown"); //$NON-NLS-1$ //$NON-NLS-2$
		}

	}

}
