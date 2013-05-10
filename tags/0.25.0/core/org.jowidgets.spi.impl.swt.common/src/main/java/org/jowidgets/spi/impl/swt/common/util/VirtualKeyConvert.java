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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.jowidgets.common.types.VirtualKey;

public final class VirtualKeyConvert {

	private static Map<VirtualKey, Integer> virtualKeyToKeyCode;
	private static Map<Integer, VirtualKey> keyCodeToVirtualKey;

	private VirtualKeyConvert() {};

	public static int convert(final VirtualKey virtualKey) {
		int result;

		if (virtualKey.isCharacter()) {
			result = virtualKey.getCharacter().charValue();
		}
		else {
			result = convertVirtualKeyToKeyCode(virtualKey);
		}

		return result;
	}

	public static VirtualKey convert(final int keyCode) {
		VirtualKey result = getKeyCodeToVirtualKey().get(Integer.valueOf(keyCode));
		if (result == null) {
			result = VirtualKey.toVirtualKey((char) keyCode);
		}
		if (result == null) {
			result = VirtualKey.UNDEFINED;
		}
		return result;
	}

	private static int convertVirtualKeyToKeyCode(final VirtualKey virtualKey) {
		final Integer result = getVirtualKeyToKeyCode().get(virtualKey);
		if (result == null) {
			throw new IllegalArgumentException("The virtual key '" + virtualKey + "' is unkown.");
		}
		else {
			return result.intValue();
		}
	}

	private static Map<VirtualKey, Integer> getVirtualKeyToKeyCode() {
		if (virtualKeyToKeyCode == null) {
			virtualKeyToKeyCode = new HashMap<VirtualKey, Integer>();
			virtualKeyToKeyCode.put(VirtualKey.F1, SWT.F1);
			virtualKeyToKeyCode.put(VirtualKey.F2, SWT.F2);
			virtualKeyToKeyCode.put(VirtualKey.F3, SWT.F3);
			virtualKeyToKeyCode.put(VirtualKey.F4, SWT.F4);
			virtualKeyToKeyCode.put(VirtualKey.F5, SWT.F5);
			virtualKeyToKeyCode.put(VirtualKey.F6, SWT.F6);
			virtualKeyToKeyCode.put(VirtualKey.F7, SWT.F7);
			virtualKeyToKeyCode.put(VirtualKey.F8, SWT.F8);
			virtualKeyToKeyCode.put(VirtualKey.F9, SWT.F9);
			virtualKeyToKeyCode.put(VirtualKey.F10, SWT.F10);
			virtualKeyToKeyCode.put(VirtualKey.F11, SWT.F11);
			virtualKeyToKeyCode.put(VirtualKey.F12, SWT.F12);

			virtualKeyToKeyCode.put(VirtualKey.ENTER, Integer.valueOf(SWT.CR));
			virtualKeyToKeyCode.put(VirtualKey.BACK_SPACE, Integer.valueOf(SWT.BS));
			virtualKeyToKeyCode.put(VirtualKey.TAB, Integer.valueOf(SWT.TAB));
			virtualKeyToKeyCode.put(VirtualKey.SHIFT, SWT.SHIFT);
			virtualKeyToKeyCode.put(VirtualKey.CONTROL, SWT.CTRL);
			virtualKeyToKeyCode.put(VirtualKey.ALT, SWT.ALT);
			virtualKeyToKeyCode.put(VirtualKey.PAUSE, SWT.PAUSE);
			virtualKeyToKeyCode.put(VirtualKey.CAPS_LOCK, SWT.CAPS_LOCK);
			virtualKeyToKeyCode.put(VirtualKey.ESC, Integer.valueOf(SWT.ESC));
			virtualKeyToKeyCode.put(VirtualKey.SPACE, Integer.valueOf(' '));
			virtualKeyToKeyCode.put(VirtualKey.PAGE_UP, SWT.PAGE_UP);
			virtualKeyToKeyCode.put(VirtualKey.PAGE_DOWN, SWT.PAGE_DOWN);
			virtualKeyToKeyCode.put(VirtualKey.END, SWT.END);
			virtualKeyToKeyCode.put(VirtualKey.HOME, SWT.HOME);
			virtualKeyToKeyCode.put(VirtualKey.DELETE, Integer.valueOf(SWT.DEL));
			virtualKeyToKeyCode.put(VirtualKey.END, SWT.INSERT);

			virtualKeyToKeyCode.put(VirtualKey.ARROW_LEFT, SWT.ARROW_LEFT);
			virtualKeyToKeyCode.put(VirtualKey.ARROW_UP, SWT.ARROW_UP);
			virtualKeyToKeyCode.put(VirtualKey.ARROW_RIGHT, SWT.ARROW_RIGHT);
			virtualKeyToKeyCode.put(VirtualKey.ARROW_DOWN, SWT.ARROW_DOWN);
		}
		return virtualKeyToKeyCode;
	}

	private static Map<Integer, VirtualKey> getKeyCodeToVirtualKey() {
		if (keyCodeToVirtualKey == null) {
			keyCodeToVirtualKey = new HashMap<Integer, VirtualKey>();
			keyCodeToVirtualKey.put(SWT.F1, VirtualKey.F1);
			keyCodeToVirtualKey.put(SWT.F2, VirtualKey.F2);
			keyCodeToVirtualKey.put(SWT.F3, VirtualKey.F3);
			keyCodeToVirtualKey.put(SWT.F4, VirtualKey.F4);
			keyCodeToVirtualKey.put(SWT.F5, VirtualKey.F5);
			keyCodeToVirtualKey.put(SWT.F6, VirtualKey.F6);
			keyCodeToVirtualKey.put(SWT.F7, VirtualKey.F7);
			keyCodeToVirtualKey.put(SWT.F8, VirtualKey.F8);
			keyCodeToVirtualKey.put(SWT.F9, VirtualKey.F9);
			keyCodeToVirtualKey.put(SWT.F10, VirtualKey.F10);
			keyCodeToVirtualKey.put(SWT.F11, VirtualKey.F11);
			keyCodeToVirtualKey.put(SWT.F12, VirtualKey.F12);

			keyCodeToVirtualKey.put(Integer.valueOf(SWT.CR), VirtualKey.ENTER);
			keyCodeToVirtualKey.put(Integer.valueOf(SWT.KEYPAD_CR), VirtualKey.ENTER);
			keyCodeToVirtualKey.put(Integer.valueOf(SWT.BS), VirtualKey.BACK_SPACE);
			keyCodeToVirtualKey.put(Integer.valueOf(SWT.TAB), VirtualKey.TAB);
			keyCodeToVirtualKey.put(SWT.SHIFT, VirtualKey.SHIFT);
			keyCodeToVirtualKey.put(SWT.CTRL, VirtualKey.CONTROL);
			keyCodeToVirtualKey.put(SWT.ALT, VirtualKey.ALT);
			keyCodeToVirtualKey.put(SWT.PAUSE, VirtualKey.PAUSE);
			keyCodeToVirtualKey.put(SWT.CAPS_LOCK, VirtualKey.CAPS_LOCK);
			keyCodeToVirtualKey.put(Integer.valueOf(SWT.ESC), VirtualKey.ESC);
			keyCodeToVirtualKey.put(Integer.valueOf(' '), VirtualKey.SPACE);
			keyCodeToVirtualKey.put(SWT.PAGE_UP, VirtualKey.PAGE_UP);
			keyCodeToVirtualKey.put(SWT.PAGE_DOWN, VirtualKey.PAGE_DOWN);
			keyCodeToVirtualKey.put(SWT.END, VirtualKey.END);
			keyCodeToVirtualKey.put(SWT.HOME, VirtualKey.HOME);
			keyCodeToVirtualKey.put(Integer.valueOf(SWT.DEL), VirtualKey.DELETE);
			keyCodeToVirtualKey.put(SWT.INSERT, VirtualKey.END);

			keyCodeToVirtualKey.put(SWT.ARROW_LEFT, VirtualKey.ARROW_LEFT);
			keyCodeToVirtualKey.put(SWT.ARROW_UP, VirtualKey.ARROW_UP);
			keyCodeToVirtualKey.put(SWT.ARROW_RIGHT, VirtualKey.ARROW_RIGHT);
			keyCodeToVirtualKey.put(SWT.ARROW_DOWN, VirtualKey.ARROW_DOWN);
		}
		return keyCodeToVirtualKey;
	}
}
