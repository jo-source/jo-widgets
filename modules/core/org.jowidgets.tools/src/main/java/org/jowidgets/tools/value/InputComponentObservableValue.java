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
import org.jowidgets.util.ObservableValueWrapper;

public final class InputComponentObservableValue<VALUE_TYPE> extends ObservableValueWrapper<VALUE_TYPE> {

	private final IInputListener inputListener;
	private final IObservableValueListener<VALUE_TYPE> observableValueListener;

	public InputComponentObservableValue(final IInputComponent<VALUE_TYPE> inputComponent) {
		super(new ObservableValue<VALUE_TYPE>());
		Assert.paramNotNull(inputComponent, "inputComponent");
		final IObservableValue<VALUE_TYPE> original = getOriginal();

		if (original.getValue() != null) {
			inputComponent.setValue(original.getValue());
		}
		else if (inputComponent.getValue() != null) {
			original.setValue(inputComponent.getValue());
		}

		this.inputListener = new IInputListener() {
			@Override
			public void inputChanged() {
				original.removeValueListener(observableValueListener);
				original.setValue(inputComponent.getValue());
				original.addValueListener(observableValueListener);
			}
		};
		inputComponent.addInputListener(inputListener);

		this.observableValueListener = new IObservableValueListener<VALUE_TYPE>() {
			@Override
			public void changed(final IObservableValue<VALUE_TYPE> observableValue, final VALUE_TYPE value) {
				inputComponent.removeInputListener(inputListener);
				inputComponent.setValue(value);
				inputComponent.addInputListener(inputListener);
			}
		};
		original.addValueListener(observableValueListener);

		inputComponent.addDisposeListener(new IDisposeListener() {
			@Override
			public void onDispose() {
				inputComponent.removeInputListener(inputListener);
				original.removeValueListener(observableValueListener);
			}
		});
	}

}
