/*
 * Copyright (c) 2013, MGrossmann
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

public class ObservableValue<VALUE_TYPE> implements IObservableValue<VALUE_TYPE> {

	private final Set<IObservableValueListener<VALUE_TYPE>> listeners;
	private final Class<VALUE_TYPE> valueType;

	private VALUE_TYPE value;

	public ObservableValue(final Class<VALUE_TYPE> valueType) {
		Assert.paramNotNull(valueType, "valueType");
		this.valueType = valueType;
		this.listeners = new LinkedHashSet<IObservableValueListener<VALUE_TYPE>>();
	}

	@Override
	public final Class<VALUE_TYPE> getValueType() {
		return valueType;
	}

	@Override
	public final void setValue(final VALUE_TYPE value) {
		if (!NullCompatibleEquivalence.equals(this.value, value)) {
			this.value = value;
			fireChanged();
		}
	}

	@Override
	public final VALUE_TYPE getValue() {
		return value;
	}

	@Override
	public final void addValueListener(final IObservableValueListener<VALUE_TYPE> listener) {
		Assert.paramNotNull(listener, "listener");
		listeners.add(listener);
	}

	@Override
	public final void removeValueListener(final IObservableValueListener<VALUE_TYPE> listener) {
		Assert.paramNotNull(listener, "listener");
		listeners.remove(listener);
	}

	private void fireChanged() {
		for (final IObservableValueListener<VALUE_TYPE> listener : new LinkedList<IObservableValueListener<VALUE_TYPE>>(listeners)) {
			listener.changed(this, value);
		}
	}

}
