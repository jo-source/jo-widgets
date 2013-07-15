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

package org.jowidgets.util;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Implements the mutable value interface.
 * Remark: This class is not thread save.
 * 
 * @param <VALUE_TYPE> The type of the mutable value
 */
public final class MutableValue<VALUE_TYPE> implements IMutableValue<VALUE_TYPE> {

	private final Set<IMutableValueListener<VALUE_TYPE>> valueListeners;

	private VALUE_TYPE value;

	public MutableValue() {
		this(null);
	}

	public MutableValue(final VALUE_TYPE value) {
		this.valueListeners = new LinkedHashSet<IMutableValueListener<VALUE_TYPE>>();
		this.value = value;
	}

	@Override
	public void addMutableValueListener(final IMutableValueListener<VALUE_TYPE> listener) {
		Assert.paramNotNull(valueListeners, "valueListeners");
		this.valueListeners.add(listener);
	}

	@Override
	public void removeMutableValueListener(final IMutableValueListener<VALUE_TYPE> listener) {
		Assert.paramNotNull(valueListeners, "valueListeners");
		this.valueListeners.remove(listener);
	}

	@Override
	public VALUE_TYPE getValue() {
		return value;
	}

	public void setValue(final VALUE_TYPE value) {
		final VALUE_TYPE oldValue = this.value;
		if (!NullCompatibleEquivalence.equals(oldValue, value)) {
			this.value = value;
			fireChangedEvent(oldValue, value);
		}
	}

	private void fireChangedEvent(final VALUE_TYPE oldValue, final VALUE_TYPE newValue) {
		final IValueChangedEvent<VALUE_TYPE> event = new ValueChangedEventImpl<VALUE_TYPE>(oldValue, value, this);
		for (final IMutableValueListener<VALUE_TYPE> listener : new LinkedList<IMutableValueListener<VALUE_TYPE>>(valueListeners)) {
			listener.changed(event);
		}
	}

}
