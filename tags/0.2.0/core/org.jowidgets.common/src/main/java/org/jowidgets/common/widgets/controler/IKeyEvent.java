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

package org.jowidgets.common.widgets.controler;

import java.util.Set;

import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.VirtualKey;

public interface IKeyEvent {

	/**
	 * Gets the virtual key that was typed.
	 * 
	 * If the typed key is not defined in the virtual key enumeration,
	 * VirtualKey.UNKOWN will be returned.
	 * 
	 * @return The virtual key of the event, may be VirtualKey.UNKNOWN
	 */
	VirtualKey getVirtualKey();

	/**
	 * Gets the character representation of the typed key if the key is representable
	 * by an character.
	 * 
	 * Remark: Modifiers have not applied on this character.
	 * i.e.: On a German Keyboard 'SHIFT + 1' will return '1' and not '!'. To get the resulting
	 * Character use the method 'IKeyEvent#getResultingCharacter()'.
	 * 
	 * @return The character representation or null, if the key has no character representation.
	 * @see IKeyEvent#getResultingCharacter()
	 */
	Character getCharacter();

	/**
	 * Gets the character representation of the typed key after all modifiers have been applied.
	 * 
	 * Remark: Modifiers have applied on this character.
	 * i.e.: On a German Keyboard 'SHIFT + 1' will return '!' and not '1'. To get the character
	 * representation of the typed key use the method 'IKeyEvent#getCharacter()'.
	 * 
	 * @return The character representation or null, if the key has no character representation.
	 * @see IKeyEvent#getCharacter()
	 */
	Character getResultingCharacter();

	/**
	 * Gets the modifiers of the key event.
	 * 
	 * @return The set of the applied modifiers. May be empty but is never null
	 */
	Set<Modifier> getModifier();

}
