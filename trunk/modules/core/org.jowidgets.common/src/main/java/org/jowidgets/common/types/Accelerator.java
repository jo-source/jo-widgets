/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.common.types;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jowidgets.util.Assert;

public final class Accelerator {

	private final Character character;
	private final VirtualKey virtualKey;
	private final List<Modifier> modifier;

	public Accelerator(final VirtualKey virtualKey, final Modifier... modifier) {
		this(virtualKey, Arrays.asList(modifier));
	}

	public Accelerator(final VirtualKey virtualKey, final List<Modifier> modifier) {
		this(null, virtualKey, modifier);
		Assert.paramNotNull(virtualKey, "virtualKey");
	}

	public Accelerator(final char key, final Modifier... modifier) {
		this(key, Arrays.asList(modifier));
	}

	public Accelerator(final char key, final List<Modifier> modifier) {
		this(key, null, modifier);
	}

	private Accelerator(final Character character, final VirtualKey virtualKey, final List<Modifier> modifier) {
		Assert.paramNotNull(modifier, "modifier");
		if (virtualKey == VirtualKey.UNDEFINED) {
			throw new IllegalArgumentException("The virtual key '"
				+ VirtualKey.UNDEFINED
				+ "' must not be used "
				+ "for accelerators.");
		}
		this.character = character;
		this.virtualKey = virtualKey;
		this.modifier = Collections.unmodifiableList(modifier);
	}

	/**
	 * Gets the accelerators character or null if the accelerator is defined with a virtual key.
	 * 
	 * Remark: If the character is null, the virtual key is not null and vice versa
	 * 
	 * @return The character or null
	 */
	public Character getCharacter() {
		return character;
	}

	/**
	 * Gets the accelerators virtual key or null if the accelerator is defined with a character.
	 * 
	 * Remark: If the character is null, the virtual key is not null and vice versa
	 * 
	 * @return The virtual key or null
	 */
	public VirtualKey getVirtualKey() {
		return virtualKey;
	}

	/**
	 * The modifiers or an empty list if no modifiers are defined
	 * 
	 * @return The odifers or an empry list
	 */
	public List<Modifier> getModifier() {
		return modifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((character == null) ? 0 : character.hashCode());
		result = prime * result + ((modifier == null) ? 0 : modifier.hashCode());
		result = prime * result + ((virtualKey == null) ? 0 : virtualKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Accelerator other = (Accelerator) obj;
		if (character == null) {
			if (other.character != null) {
				return false;
			}
		}
		else if (!character.equals(other.character)) {
			return false;
		}
		if (modifier == null) {
			if (other.modifier != null) {
				return false;
			}
		}
		else if (!modifier.equals(other.modifier)) {
			return false;
		}
		if (virtualKey != other.virtualKey) {
			return false;
		}
		return true;
	}

}
