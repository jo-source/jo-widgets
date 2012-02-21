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
package org.jowidgets.spi.impl.javafx.util;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.input.KeyCode;

import org.jowidgets.common.types.VirtualKey;

public final class VirtualKeyConvert {

	private static Map<VirtualKey, KeyCode> virtualKeyToKeyCode;
	private static Map<KeyCode, VirtualKey> keyCodeToVirtualKey;

	private VirtualKeyConvert() {};

	//TODO DB test class
	public static KeyCode convert(final VirtualKey virtualKey) {
		KeyCode result;
		if (virtualKey.isCharacter()) {
			result = KeyCode.getKeyCode(virtualKey.getCharacter().toString());
		}
		else {
			result = convertVirtualKeyToKeyCode(virtualKey);
		}
		return result;
	}

	public static VirtualKey convert(final KeyCode keyCode) {
		VirtualKey result = getKeyCodeToVirtualKey().get(keyCode);
		if (result == null) {
			result = VirtualKey.toVirtualKey(keyCode.getName().charAt(0));
		}
		if (result == null) {
			result = VirtualKey.UNDEFINED;
		}
		return result;
	}

	private static KeyCode convertVirtualKeyToKeyCode(final VirtualKey virtualKey) {
		final KeyCode result = getVirtualKeyToKeyCode().get(virtualKey);
		if (result == null) {
			throw new IllegalArgumentException("The virtual key '" + virtualKey + "' is unkown.");
		}
		else {
			return result;
		}
	}

	private static Map<VirtualKey, KeyCode> getVirtualKeyToKeyCode() {
		if (virtualKeyToKeyCode == null) {
			virtualKeyToKeyCode = new HashMap<VirtualKey, KeyCode>();
			virtualKeyToKeyCode.put(VirtualKey.F1, KeyCode.F1);
			virtualKeyToKeyCode.put(VirtualKey.F2, KeyCode.F2);
			virtualKeyToKeyCode.put(VirtualKey.F3, KeyCode.F3);
			virtualKeyToKeyCode.put(VirtualKey.F4, KeyCode.F4);
			virtualKeyToKeyCode.put(VirtualKey.F5, KeyCode.F5);
			virtualKeyToKeyCode.put(VirtualKey.F6, KeyCode.F6);
			virtualKeyToKeyCode.put(VirtualKey.F7, KeyCode.F7);
			virtualKeyToKeyCode.put(VirtualKey.F8, KeyCode.F8);
			virtualKeyToKeyCode.put(VirtualKey.F9, KeyCode.F9);
			virtualKeyToKeyCode.put(VirtualKey.F10, KeyCode.F10);
			virtualKeyToKeyCode.put(VirtualKey.F11, KeyCode.F11);
			virtualKeyToKeyCode.put(VirtualKey.F12, KeyCode.F12);

			virtualKeyToKeyCode.put(VirtualKey.ENTER, KeyCode.ENTER);
			virtualKeyToKeyCode.put(VirtualKey.BACK_SPACE, KeyCode.BACK_SPACE);
			virtualKeyToKeyCode.put(VirtualKey.TAB, KeyCode.TAB);
			virtualKeyToKeyCode.put(VirtualKey.SHIFT, KeyCode.SHIFT);
			virtualKeyToKeyCode.put(VirtualKey.CONTROL, KeyCode.CONTROL);
			virtualKeyToKeyCode.put(VirtualKey.ALT, KeyCode.ALT);
			virtualKeyToKeyCode.put(VirtualKey.PAUSE, KeyCode.PAUSE);
			virtualKeyToKeyCode.put(VirtualKey.CAPS_LOCK, KeyCode.CAPS);
			virtualKeyToKeyCode.put(VirtualKey.ESC, KeyCode.ESCAPE);
			virtualKeyToKeyCode.put(VirtualKey.SPACE, KeyCode.SPACE);
			virtualKeyToKeyCode.put(VirtualKey.PAGE_UP, KeyCode.PAGE_UP);
			virtualKeyToKeyCode.put(VirtualKey.PAGE_DOWN, KeyCode.PAGE_DOWN);
			virtualKeyToKeyCode.put(VirtualKey.END, KeyCode.END);
			virtualKeyToKeyCode.put(VirtualKey.HOME, KeyCode.HOME);
			virtualKeyToKeyCode.put(VirtualKey.DELETE, KeyCode.DELETE);
			virtualKeyToKeyCode.put(VirtualKey.INSERT, KeyCode.INSERT);

			virtualKeyToKeyCode.put(VirtualKey.ARROW_LEFT, KeyCode.LEFT);
			virtualKeyToKeyCode.put(VirtualKey.ARROW_UP, KeyCode.UP);
			virtualKeyToKeyCode.put(VirtualKey.ARROW_RIGHT, KeyCode.RIGHT);
			virtualKeyToKeyCode.put(VirtualKey.ARROW_DOWN, KeyCode.DOWN);
		}
		return virtualKeyToKeyCode;
	}

	private static Map<KeyCode, VirtualKey> getKeyCodeToVirtualKey() {
		if (keyCodeToVirtualKey == null) {
			keyCodeToVirtualKey = new HashMap<KeyCode, VirtualKey>();
			keyCodeToVirtualKey.put(KeyCode.F1, VirtualKey.F1);
			keyCodeToVirtualKey.put(KeyCode.F2, VirtualKey.F2);
			keyCodeToVirtualKey.put(KeyCode.F3, VirtualKey.F3);
			keyCodeToVirtualKey.put(KeyCode.F4, VirtualKey.F4);
			keyCodeToVirtualKey.put(KeyCode.F5, VirtualKey.F5);
			keyCodeToVirtualKey.put(KeyCode.F6, VirtualKey.F6);
			keyCodeToVirtualKey.put(KeyCode.F7, VirtualKey.F7);
			keyCodeToVirtualKey.put(KeyCode.F8, VirtualKey.F8);
			keyCodeToVirtualKey.put(KeyCode.F9, VirtualKey.F9);
			keyCodeToVirtualKey.put(KeyCode.F10, VirtualKey.F10);
			keyCodeToVirtualKey.put(KeyCode.F11, VirtualKey.F11);
			keyCodeToVirtualKey.put(KeyCode.F12, VirtualKey.F12);

			keyCodeToVirtualKey.put(KeyCode.ENTER, VirtualKey.ENTER);
			keyCodeToVirtualKey.put(KeyCode.BACK_SPACE, VirtualKey.BACK_SPACE);
			keyCodeToVirtualKey.put(KeyCode.TAB, VirtualKey.TAB);
			keyCodeToVirtualKey.put(KeyCode.SHIFT, VirtualKey.SHIFT);
			keyCodeToVirtualKey.put(KeyCode.CONTROL, VirtualKey.CONTROL);
			keyCodeToVirtualKey.put(KeyCode.ALT, VirtualKey.ALT);
			keyCodeToVirtualKey.put(KeyCode.PAUSE, VirtualKey.PAUSE);
			keyCodeToVirtualKey.put(KeyCode.CAPS, VirtualKey.CAPS_LOCK);
			keyCodeToVirtualKey.put(KeyCode.ESCAPE, VirtualKey.ESC);
			keyCodeToVirtualKey.put(KeyCode.SPACE, VirtualKey.SPACE);
			keyCodeToVirtualKey.put(KeyCode.PAGE_UP, VirtualKey.PAGE_UP);
			keyCodeToVirtualKey.put(KeyCode.PAGE_DOWN, VirtualKey.PAGE_DOWN);
			keyCodeToVirtualKey.put(KeyCode.END, VirtualKey.END);
			keyCodeToVirtualKey.put(KeyCode.HOME, VirtualKey.HOME);
			keyCodeToVirtualKey.put(KeyCode.DELETE, VirtualKey.DELETE);
			keyCodeToVirtualKey.put(KeyCode.INSERT, VirtualKey.INSERT);

			keyCodeToVirtualKey.put(KeyCode.LEFT, VirtualKey.ARROW_LEFT);
			keyCodeToVirtualKey.put(KeyCode.UP, VirtualKey.ARROW_UP);
			keyCodeToVirtualKey.put(KeyCode.RIGHT, VirtualKey.ARROW_RIGHT);
			keyCodeToVirtualKey.put(KeyCode.DOWN, VirtualKey.ARROW_DOWN);
		}
		return keyCodeToVirtualKey;
	}
}
