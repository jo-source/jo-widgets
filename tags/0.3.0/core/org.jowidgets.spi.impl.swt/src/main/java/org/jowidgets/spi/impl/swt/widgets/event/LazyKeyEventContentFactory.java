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

package org.jowidgets.spi.impl.swt.widgets.event;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.spi.impl.controler.ILazyKeyEventContentFactory;
import org.jowidgets.spi.impl.swt.util.VirtualKeyConvert;

public class LazyKeyEventContentFactory implements ILazyKeyEventContentFactory {

	private final KeyEvent keyEvent;

	public LazyKeyEventContentFactory(final KeyEvent keyEvent) {
		super();
		this.keyEvent = keyEvent;
	}

	@Override
	public VirtualKey createVirtualKey() {
		return VirtualKeyConvert.convert(keyEvent.keyCode);
	}

	@Override
	public Character createCharacter() {
		if (keyEvent.character != 0) {
			return Character.valueOf((char) keyEvent.keyCode);
		}
		else {
			return null;
		}
	}

	@Override
	public Character createResultingCharacter() {
		if (keyEvent.character != 0) {
			return Character.valueOf(keyEvent.character);
		}
		else {
			return null;
		}
	}

	@Override
	public Set<Modifier> createModifier() {
		final Set<Modifier> result = new HashSet<Modifier>();
		if ((keyEvent.stateMask & SWT.SHIFT) > 0) {
			result.add(Modifier.SHIFT);
		}
		if ((keyEvent.stateMask & SWT.CTRL) > 0) {
			result.add(Modifier.CTRL);
		}
		if ((keyEvent.stateMask & SWT.ALT) > 0) {
			result.add(Modifier.ALT);
		}
		return result;
	}

}
