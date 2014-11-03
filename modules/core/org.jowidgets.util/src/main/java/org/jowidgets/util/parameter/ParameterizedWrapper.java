/*
 * Copyright (c) 2013, grossmann
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

package org.jowidgets.util.parameter;

import java.util.List;

import org.jowidgets.util.Assert;
import org.jowidgets.util.IObservableValue;
import org.jowidgets.util.IObservableValueListener;
import org.jowidgets.util.ITypedKey;
import org.jowidgets.util.event.ChangeObservable;
import org.jowidgets.util.event.IChangeListener;
import org.jowidgets.util.event.IChangeObservable;

public class ParameterizedWrapper implements IParameterized, IChangeObservable {

	private final IParameterized original;
	private final ChangeObservable changeObservable;

	@SuppressWarnings("rawtypes")
	private IObservableValueListener observableValueListener;

	public ParameterizedWrapper(final IParameterized original) {
		Assert.paramNotNull(original, "original");
		this.original = original;
		this.changeObservable = new ChangeObservable();
	}

	protected IParameterized getOriginal() {
		return original;
	}

	@Override
	public void addChangeListener(final IChangeListener listener) {
		initChangeObservable();
		changeObservable.addChangeListener(listener);
	}

	@Override
	public void removeChangeListener(final IChangeListener listener) {
		changeObservable.removeChangeListener(listener);
	}

	@Override
	public final List<ITypedKey<?>> getAvailableParameters() {
		return original.getAvailableParameters();
	}

	@Override
	public final <VALUE_TYPE> IParameter<VALUE_TYPE> getParameter(final ITypedKey<VALUE_TYPE> key) {
		return original.getParameter(key);
	}

	public float getFloatValue(final ITypedKey<Float> key, final float defaultValue) {
		final Float value = getParameterMandatory(key).getValue();
		if (value != null) {
			return value.floatValue();
		}
		else {
			return defaultValue;
		}
	}

	public void setFloatValue(final ITypedKey<Float> key, final float value) {
		getParameterMandatory(key).setValue(Float.valueOf(value));
	}

	public int getIntValue(final ITypedKey<Integer> key, final int defaultValue) {
		final Integer value = getParameterMandatory(key).getValue();
		if (value != null) {
			return value.intValue();
		}
		else {
			return defaultValue;
		}
	}

	public void setIntValue(final ITypedKey<Integer> key, final int value) {
		getParameterMandatory(key).setValue(Integer.valueOf(value));
	}

	public boolean getBooleanValue(final ITypedKey<Boolean> key, final boolean defaultValue) {
		final Boolean value = getParameterMandatory(key).getValue();
		if (value != null) {
			return value.booleanValue();
		}
		else {
			return defaultValue;
		}
	}

	public void setBooleanValue(final ITypedKey<Boolean> key, final boolean value) {
		getParameterMandatory(key).setValue(Boolean.valueOf(value));
	}

	public final <VALUE_TYPE> IParameter<VALUE_TYPE> getParameterMandatory(final ITypedKey<VALUE_TYPE> key) {
		Assert.paramNotNull(key, "key");
		final IParameter<VALUE_TYPE> result = getParameter(key);
		if (result == null) {
			throw new IllegalArgumentException("No parameter for the key '" + key + "' defined");
		}
		return result;
	}

	@SuppressWarnings({"rawtypes"})
	private void initChangeObservable() {
		if (observableValueListener == null) {
			observableValueListener = new IObservableValueListener() {
				@Override
				public void changed(final IObservableValue observableValue, final Object value) {
					changeObservable.fireChangedEvent();
				}
			};
			for (final ITypedKey<?> key : original.getAvailableParameters()) {
				original.getParameter(key).addValueListener(observableValueListener);
			}
		}
	}
}
