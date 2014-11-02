/*
 * Copyright (c) 2014, grossmann
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

package org.jowidgets.tools.value;

import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.widgets.IInputComponent;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.util.Assert;
import org.jowidgets.util.IObservableValue;
import org.jowidgets.util.IObservableValueListener;
import org.jowidgets.util.ObservableValue;
import org.jowidgets.util.ValueHolder;

public final class InputComponentBind {

	private InputComponentBind() {}

	/**
	 * Binds an input component to a newly created observable value and returns the observable value.
	 * If the input component will be disposed, the binding will be disposed too.
	 * 
	 * @param inputComponent The input component to bind
	 * 
	 * @return The created observable value
	 */
	public static <VALUE_TYPE> IObservableValue<VALUE_TYPE> bind(final IInputComponent<VALUE_TYPE> inputComponent) {
		Assert.paramNotNull(inputComponent, "inputComponent");
		final IObservableValue<VALUE_TYPE> result = new ObservableValue<VALUE_TYPE>();
		bind(result, inputComponent);
		return result;
	}

	/**
	 * Binds an input component to an observable value. If the input component will be disposed, the binding will be disposed too.
	 * 
	 * @param source The source observable value to bind to
	 * @param inputComponent The input component to bind
	 */
	public static <VALUE_TYPE> void bind(
		final IObservableValue<VALUE_TYPE> source,
		final IInputComponent<VALUE_TYPE> inputComponent) {

		Assert.paramNotNull(inputComponent, "inputComponent");

		if (source.getValue() != null) {
			inputComponent.setValue(source.getValue());
		}
		else if (inputComponent.getValue() != null) {
			source.setValue(inputComponent.getValue());
		}

		final ValueHolder<IInputListener> inputListener = new ValueHolder<IInputListener>();
		final ValueHolder<IObservableValueListener<VALUE_TYPE>> observableValueListener = new ValueHolder<IObservableValueListener<VALUE_TYPE>>();

		inputListener.set(new IInputListener() {
			@Override
			public void inputChanged() {
				source.removeValueListener(observableValueListener.get());
				source.setValue(inputComponent.getValue());
				source.addValueListener(observableValueListener.get());
			}
		});
		inputComponent.addInputListener(inputListener.get());

		observableValueListener.set(new IObservableValueListener<VALUE_TYPE>() {
			@Override
			public void changed(final IObservableValue<VALUE_TYPE> observableValue, final VALUE_TYPE value) {
				inputComponent.removeInputListener(inputListener.get());
				inputComponent.setValue(value);
				inputComponent.addInputListener(inputListener.get());
			}
		});
		source.addValueListener(observableValueListener.get());

		inputComponent.addDisposeListener(new IDisposeListener() {
			@Override
			public void onDispose() {
				inputComponent.removeInputListener(inputListener.get());
				source.removeValueListener(observableValueListener.get());
			}
		});

	}
}
