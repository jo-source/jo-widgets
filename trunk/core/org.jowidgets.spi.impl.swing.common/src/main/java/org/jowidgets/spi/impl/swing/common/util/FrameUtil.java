/*
 * Copyright (c) 2012, grossmann
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

import java.awt.Frame;

public final class FrameUtil {

	private FrameUtil() {}

	public static void setMaximized(final Frame frame, final boolean maximized) {
		final int extendedState = frame.getExtendedState();
		if (maximized) {
			frame.setExtendedState(extendedState | Frame.MAXIMIZED_BOTH);
		}
		else {
			frame.setExtendedState(extendedState & ~Frame.MAXIMIZED_BOTH);
		}

	}

	public static boolean isMaximized(final Frame frame) {
		return (frame.getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;
	}

	public static void setIconfied(final Frame frame, final boolean iconfied) {
		final int extendedState = frame.getExtendedState();
		if (iconfied) {
			frame.setExtendedState(extendedState | Frame.ICONIFIED);
		}
		else {
			frame.setExtendedState(extendedState & ~Frame.ICONIFIED);
		}
	}

	public static boolean isIconfied(final Frame frame) {
		return (frame.getExtendedState() & Frame.ICONIFIED) == Frame.ICONIFIED;
	}

}
