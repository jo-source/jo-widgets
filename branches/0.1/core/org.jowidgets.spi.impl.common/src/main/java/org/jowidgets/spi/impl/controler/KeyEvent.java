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

package org.jowidgets.spi.impl.controler;

import java.util.Collections;
import java.util.Set;

import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.common.widgets.controler.IKeyEvent;
import org.jowidgets.util.Assert;

public class KeyEvent implements IKeyEvent {

	private final ILazyKeyEventContentFactory contentFactory;

	private VirtualKey virtualKey;
	private Character character;
	private Character resultingCharacter;
	private Set<Modifier> modifier;

	private boolean virtualKeyInitialized;
	private boolean characterInitialized;
	private boolean resultingCharacterInitialized;
	private boolean modifierInitialized;

	public KeyEvent(final ILazyKeyEventContentFactory contentFactory) {
		Assert.paramNotNull(contentFactory, "contentFactory");
		this.contentFactory = contentFactory;

		this.virtualKeyInitialized = false;
		this.characterInitialized = false;
		this.resultingCharacterInitialized = false;
		this.modifierInitialized = false;
	}

	@Override
	public VirtualKey getVirtualKey() {
		if (!virtualKeyInitialized) {
			virtualKey = contentFactory.createVirtualKey();
			virtualKeyInitialized = true;
		}
		return virtualKey;
	}

	@Override
	public Character getCharacter() {
		if (!characterInitialized) {
			character = contentFactory.createCharacter();
			characterInitialized = true;
		}
		return character;
	}

	@Override
	public Character getResultingCharacter() {
		if (!resultingCharacterInitialized) {
			resultingCharacter = contentFactory.createResultingCharacter();
			resultingCharacterInitialized = true;
		}
		return resultingCharacter;
	}

	@Override
	public Set<Modifier> getModifier() {
		if (!modifierInitialized) {
			final Set<Modifier> createdModifier = contentFactory.createModifier();
			if (createdModifier != null) {
				modifier = Collections.unmodifiableSet(contentFactory.createModifier());
			}
			else {
				modifier = Collections.emptySet();
			}
			modifierInitialized = true;
		}
		return modifier;
	}

	@Override
	public String toString() {
		return "LazyKeyEvent [virtualKey="
			+ getVirtualKey()
			+ ", character="
			+ getCharacter()
			+ ", resultingCharacter="
			+ getResultingCharacter()
			+ ", modifier="
			+ getModifier()
			+ "]";
	}

}
