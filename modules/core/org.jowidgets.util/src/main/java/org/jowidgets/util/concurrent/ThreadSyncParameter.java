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

package org.jowidgets.util.concurrent;

import org.jowidgets.util.IObservableValue;
import org.jowidgets.util.IObservableValueListener;
import org.jowidgets.util.parameter.IParameter;
import org.jowidgets.util.parameter.Parameter;
import org.jowidgets.util.parameter.ParameterWrapper;

public final class ThreadSyncParameter<VALUE_TYPE> extends ParameterWrapper<VALUE_TYPE> implements IParameter<VALUE_TYPE> {

	private final IParameter<VALUE_TYPE> originalParameter;
	private final ISingleThreadAccess originalThreadAccess;
	private final ISingleThreadAccess newThreadAccess;

	private final IObservableValueListener<VALUE_TYPE> originalValueListener;
	private final IObservableValueListener<VALUE_TYPE> newValueListener;

	public ThreadSyncParameter(
		final IParameter<VALUE_TYPE> original,
		final ISingleThreadAccess originalThreadAccess,
		final ISingleThreadAccess newThreadAccess) {
		super(Parameter.create(original.getValueType(), original.getLabel(), original.getDescription()));

		this.originalParameter = original;
		this.originalThreadAccess = originalThreadAccess;
		this.newThreadAccess = newThreadAccess;

		this.originalValueListener = new OriginalValueListener();
		this.newValueListener = new NewValueListener();

		originalThreadAccess.invoke(new Runnable() {
			@Override
			public void run() {
				updateNew(originalParameter.getValue());
				originalParameter.addValueListener(originalValueListener);
			}
		});

		addValueListener(newValueListener);
	}

	@Override
	public void setValue(final VALUE_TYPE value) {
		checkThread();
		super.setValue(value);
	}

	@Override
	public VALUE_TYPE getValue() {
		checkThread();
		return super.getValue();
	}

	@Override
	public void addValueListener(final IObservableValueListener<VALUE_TYPE> listener) {
		checkThread();
		super.addValueListener(listener);
	}

	@Override
	public void removeValueListener(final IObservableValueListener<VALUE_TYPE> listener) {
		checkThread();
		super.removeValueListener(listener);
	}

	public void dispose() {
		removeValueListener(newValueListener);
		originalThreadAccess.invoke(new Runnable() {
			@Override
			public void run() {
				originalParameter.removeValueListener(originalValueListener);
			}
		});
	}

	private void updateOriginal(final VALUE_TYPE value) {
		originalThreadAccess.invoke(new Runnable() {
			@Override
			public void run() {
				originalParameter.removeValueListener(originalValueListener);
				originalParameter.setValue(value);
				originalParameter.addValueListener(originalValueListener);
			}
		});
	}

	private void updateNew(final VALUE_TYPE value) {
		newThreadAccess.invoke(new Runnable() {
			@Override
			public void run() {
				removeValueListener(newValueListener);
				setValue(value);
				addValueListener(newValueListener);
			}
		});
	}

	private void checkThread() {
		if (!newThreadAccess.isSingleThread()) {
			throw new IllegalArgumentException("Operation must be invoked in write thread access!");
		}
	}

	private final class OriginalValueListener implements IObservableValueListener<VALUE_TYPE> {
		@Override
		public void changed(final IObservableValue<VALUE_TYPE> observableValue, final VALUE_TYPE value) {
			updateNew(value);
		}
	}

	private final class NewValueListener implements IObservableValueListener<VALUE_TYPE> {
		@Override
		public void changed(final IObservableValue<VALUE_TYPE> observableValue, final VALUE_TYPE value) {
			updateOriginal(value);
		}
	}

}
