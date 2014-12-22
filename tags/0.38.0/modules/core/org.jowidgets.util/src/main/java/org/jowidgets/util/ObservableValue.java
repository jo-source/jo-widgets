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

import org.jowidgets.util.collection.IObserverSet;
import org.jowidgets.util.collection.IObserverSetFactory.Strategy;
import org.jowidgets.util.collection.ObserverSetFactory;

public class ObservableValue<VALUE_TYPE> implements IObservableValue<VALUE_TYPE> {

	private final IObserverSet<IObservableValueListener<?>> listeners;

	private VALUE_TYPE value;

	public ObservableValue() {
		this(null);
	}

	public ObservableValue(final VALUE_TYPE value) {
		this.value = value;
		this.listeners = ObserverSetFactory.create(Strategy.LOW_MEMORY);
	}

	@Override
	public void setValue(final VALUE_TYPE value) {
		if (!NullCompatibleEquivalence.equals(this.value, value)) {
			this.value = value;
			fireChanged();
		}
	}

	@Override
	public VALUE_TYPE getValue() {
		return value;
	}

	@Override
	public final void addValueListener(final IObservableValueListener<?> listener) {
		Assert.paramNotNull(listener, "listener");
		listeners.add(listener);
	}

	@Override
	public final void removeValueListener(final IObservableValueListener<?> listener) {
		Assert.paramNotNull(listener, "listener");
		listeners.remove(listener);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void fireChanged() {
		for (final IObservableValueListener listener : listeners) {
			listener.changed(this, value);
		}
	}

	@Override
	public String toString() {
		return "ObservableValue [value=" + value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		if (!(obj instanceof IObservableValue)) {
			return false;
		}
		final IObservableValue<?> other = (IObservableValue<?>) obj;
		if (value == null) {
			if (other.getValue() != null) {
				return false;
			}
		}
		else if (!value.equals(other.getValue())) {
			return false;
		}
		return true;
	}

}
