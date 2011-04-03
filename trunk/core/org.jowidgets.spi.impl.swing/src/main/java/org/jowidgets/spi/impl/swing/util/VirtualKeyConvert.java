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

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

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
			virtualKeyToKeyCode.put(VirtualKey.F1, KeyEvent.VK_F1);
			virtualKeyToKeyCode.put(VirtualKey.F2, KeyEvent.VK_F2);
			virtualKeyToKeyCode.put(VirtualKey.F3, KeyEvent.VK_F3);
			virtualKeyToKeyCode.put(VirtualKey.F4, KeyEvent.VK_F4);
			virtualKeyToKeyCode.put(VirtualKey.F5, KeyEvent.VK_F5);
			virtualKeyToKeyCode.put(VirtualKey.F6, KeyEvent.VK_F6);
			virtualKeyToKeyCode.put(VirtualKey.F7, KeyEvent.VK_F7);
			virtualKeyToKeyCode.put(VirtualKey.F8, KeyEvent.VK_F8);
			virtualKeyToKeyCode.put(VirtualKey.F9, KeyEvent.VK_F9);
			virtualKeyToKeyCode.put(VirtualKey.F10, KeyEvent.VK_F10);
			virtualKeyToKeyCode.put(VirtualKey.F11, KeyEvent.VK_F11);
			virtualKeyToKeyCode.put(VirtualKey.F12, KeyEvent.VK_F12);

			virtualKeyToKeyCode.put(VirtualKey.ENTER, KeyEvent.VK_ENTER);
			virtualKeyToKeyCode.put(VirtualKey.BACK_SPACE, KeyEvent.VK_BACK_SPACE);
			virtualKeyToKeyCode.put(VirtualKey.TAB, KeyEvent.VK_TAB);
			virtualKeyToKeyCode.put(VirtualKey.SHIFT, KeyEvent.VK_SHIFT);
			virtualKeyToKeyCode.put(VirtualKey.CONTROL, KeyEvent.VK_CONTROL);
			virtualKeyToKeyCode.put(VirtualKey.ALT, KeyEvent.VK_ALT);
			virtualKeyToKeyCode.put(VirtualKey.PAUSE, KeyEvent.VK_PAUSE);
			virtualKeyToKeyCode.put(VirtualKey.CAPS_LOCK, KeyEvent.VK_CAPS_LOCK);
			virtualKeyToKeyCode.put(VirtualKey.ESC, KeyEvent.VK_ESCAPE);
			virtualKeyToKeyCode.put(VirtualKey.SPACE, KeyEvent.VK_SPACE);
			virtualKeyToKeyCode.put(VirtualKey.PAGE_UP, KeyEvent.VK_PAGE_UP);
			virtualKeyToKeyCode.put(VirtualKey.PAGE_DOWN, KeyEvent.VK_PAGE_DOWN);
			virtualKeyToKeyCode.put(VirtualKey.END, KeyEvent.VK_END);
			virtualKeyToKeyCode.put(VirtualKey.HOME, KeyEvent.VK_HOME);

			virtualKeyToKeyCode.put(VirtualKey.ARROW_LEFT, KeyEvent.VK_LEFT);
			virtualKeyToKeyCode.put(VirtualKey.ARROW_UP, KeyEvent.VK_UP);
			virtualKeyToKeyCode.put(VirtualKey.ARROW_RIGHT, KeyEvent.VK_RIGHT);
			virtualKeyToKeyCode.put(VirtualKey.ARROW_DOWN, KeyEvent.VK_DOWN);
		}
		return virtualKeyToKeyCode;
	}

	private static Map<Integer, VirtualKey> getKeyCodeToVirtualKey() {
		if (keyCodeToVirtualKey == null) {
			keyCodeToVirtualKey = new HashMap<Integer, VirtualKey>();
			keyCodeToVirtualKey.put(KeyEvent.VK_F1, VirtualKey.F1);
			keyCodeToVirtualKey.put(KeyEvent.VK_F2, VirtualKey.F2);
			keyCodeToVirtualKey.put(KeyEvent.VK_F3, VirtualKey.F3);
			keyCodeToVirtualKey.put(KeyEvent.VK_F4, VirtualKey.F4);
			keyCodeToVirtualKey.put(KeyEvent.VK_F5, VirtualKey.F5);
			keyCodeToVirtualKey.put(KeyEvent.VK_F6, VirtualKey.F6);
			keyCodeToVirtualKey.put(KeyEvent.VK_F7, VirtualKey.F7);
			keyCodeToVirtualKey.put(KeyEvent.VK_F8, VirtualKey.F8);
			keyCodeToVirtualKey.put(KeyEvent.VK_F9, VirtualKey.F9);
			keyCodeToVirtualKey.put(KeyEvent.VK_F10, VirtualKey.F10);
			keyCodeToVirtualKey.put(KeyEvent.VK_F11, VirtualKey.F11);
			keyCodeToVirtualKey.put(KeyEvent.VK_F12, VirtualKey.F12);

			keyCodeToVirtualKey.put(KeyEvent.VK_ENTER, VirtualKey.ENTER);
			keyCodeToVirtualKey.put(KeyEvent.VK_BACK_SPACE, VirtualKey.BACK_SPACE);
			keyCodeToVirtualKey.put(KeyEvent.VK_TAB, VirtualKey.TAB);
			keyCodeToVirtualKey.put(KeyEvent.VK_SHIFT, VirtualKey.SHIFT);
			keyCodeToVirtualKey.put(KeyEvent.VK_CONTROL, VirtualKey.CONTROL);
			keyCodeToVirtualKey.put(KeyEvent.VK_ALT, VirtualKey.ALT);
			keyCodeToVirtualKey.put(KeyEvent.VK_PAUSE, VirtualKey.PAUSE);
			keyCodeToVirtualKey.put(KeyEvent.VK_CAPS_LOCK, VirtualKey.CAPS_LOCK);
			keyCodeToVirtualKey.put(KeyEvent.VK_ESCAPE, VirtualKey.ESC);
			keyCodeToVirtualKey.put(KeyEvent.VK_SPACE, VirtualKey.SPACE);
			keyCodeToVirtualKey.put(KeyEvent.VK_PAGE_UP, VirtualKey.PAGE_UP);
			keyCodeToVirtualKey.put(KeyEvent.VK_PAGE_DOWN, VirtualKey.PAGE_DOWN);
			keyCodeToVirtualKey.put(KeyEvent.VK_END, VirtualKey.END);
			keyCodeToVirtualKey.put(KeyEvent.VK_HOME, VirtualKey.HOME);

			keyCodeToVirtualKey.put(KeyEvent.VK_LEFT, VirtualKey.ARROW_LEFT);
			keyCodeToVirtualKey.put(KeyEvent.VK_UP, VirtualKey.ARROW_UP);
			keyCodeToVirtualKey.put(KeyEvent.VK_RIGHT, VirtualKey.ARROW_RIGHT);
			keyCodeToVirtualKey.put(KeyEvent.VK_DOWN, VirtualKey.ARROW_DOWN);
		}
		return keyCodeToVirtualKey;
	}
}
