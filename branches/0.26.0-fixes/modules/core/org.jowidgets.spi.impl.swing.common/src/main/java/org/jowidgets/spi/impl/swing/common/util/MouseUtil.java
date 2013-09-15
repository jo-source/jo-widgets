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

package org.jowidgets.spi.impl.swing.common.util;

import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;

import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.MouseButton;

public final class MouseUtil {

	private MouseUtil() {}

	public static MouseButton getMouseButton(final MouseEvent event) {
		if (SwingUtilities.isLeftMouseButton(event)) {
			return MouseButton.LEFT;
		}
		else if (SwingUtilities.isRightMouseButton(event)) {
			return MouseButton.RIGHT;
		}
		else {
			return null;
		}
	}

	public static Set<Modifier> getModifier(final MouseEvent event) {
		final Set<Modifier> modifier = new HashSet<Modifier>();
		if (event.isShiftDown()) {
			modifier.add(Modifier.SHIFT);
		}
		if (event.isControlDown()) {
			modifier.add(Modifier.CTRL);
		}
		if (event.isAltDown()) {
			modifier.add(Modifier.ALT);
		}
		return modifier;
	}
}
