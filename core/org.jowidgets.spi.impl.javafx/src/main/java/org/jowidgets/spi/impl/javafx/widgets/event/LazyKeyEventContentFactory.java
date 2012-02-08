/*
 * Copyright (c) 2012, David Bauknecht
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

package org.jowidgets.spi.impl.javafx.widgets.event;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.spi.impl.controller.ILazyKeyEventContentFactory;

public class LazyKeyEventContentFactory implements ILazyKeyEventContentFactory {

	private final KeyEvent keyEvent;

	public LazyKeyEventContentFactory(final KeyEvent keyEvent) {
		super();
		this.keyEvent = keyEvent;
	}

	@Override
	public VirtualKey createVirtualKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Character createCharacter() {
		if (!KeyEvent.CHAR_UNDEFINED.equals(keyEvent.getCode())) {
			return Character.valueOf(keyEvent.getCharacter().charAt(0));
		}
		else {
			return null;
		}
	}

	@Override
	public Character createResultingCharacter() {
		if (!KeyEvent.CHAR_UNDEFINED.equals(keyEvent.getCode())) {
			return Character.valueOf(keyEvent.getCharacter().charAt(0));
		}
		else {
			return null;
		}
	}

	@Override
	public Set<Modifier> createModifier() {
		final Set<Modifier> result = new HashSet<Modifier>();
		if (keyEvent.isAltDown() && keyEvent.getCode() != KeyCode.ALT) {
			result.add(Modifier.ALT);
		}
		if (keyEvent.isShiftDown() && keyEvent.getCode() != KeyCode.SHIFT) {
			result.add(Modifier.SHIFT);
		}
		if (keyEvent.isControlDown() && keyEvent.getCode() != KeyCode.CONTROL) {
			result.add(Modifier.CTRL);
		}
		return result;
	}

}
