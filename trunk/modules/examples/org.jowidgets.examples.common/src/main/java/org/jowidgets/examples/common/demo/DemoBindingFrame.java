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

package org.jowidgets.examples.common.demo;

import java.util.ArrayList;

import org.jowidgets.api.widgets.ICheckBox;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.ISliderViewerBluePrint;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.powo.JoFrame;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.util.IObservableValue;
import org.jowidgets.util.ObservableBoolean;
import org.jowidgets.util.ObservableValue;
import org.jowidgets.util.StringUtils;
import org.jowidgets.util.binding.Bind;
import org.jowidgets.util.binding.IBinding;

final class DemoBindingFrame extends JoFrame {

	private static final int COLUMNS = 10;
	private static final double MIN_VALUE = -1.0d;
	private static final double MAX_VALUE = 1.0d;
	private static final double DEFAULT_VALUE = 0.0d;

	DemoBindingFrame() {
		super("Binding demo");

		setLayout(new MigLayoutDescriptor("wrap", StringUtils.loop("[]10", COLUMNS - 1) + "[]", "[]0[]20[]"));

		//create observable values and binding
		final ArrayList<IObservableValue<Double>> observableValues = new ArrayList<IObservableValue<Double>>(COLUMNS);
		final ArrayList<IBinding> bindings = new ArrayList<IBinding>(COLUMNS - 1);
		for (int i = 0; i < COLUMNS; i++) {
			final IObservableValue<Double> observableValue = new ObservableValue<Double>();
			observableValues.add(observableValue);
			if (i > 0) {
				//bind next value to the previous
				final IBinding binding = Bind.bind(observableValues.get(i - 1), observableValue);
				bindings.add(binding);
			}
		}

		//add sliders
		for (int i = 0; i < COLUMNS; i++) {
			final ISliderViewerBluePrint<Double> sliderBp = BPF.sliderViewerDouble(MIN_VALUE, MAX_VALUE);
			sliderBp.setVertical();
			sliderBp.setDefaultValue(DEFAULT_VALUE);
			sliderBp.setObservableValue(observableValues.get(i));
			add(sliderBp, "sgx g1");
		}

		//add input fields
		for (int i = 0; i < COLUMNS; i++) {
			final IInputFieldBluePrint<Double> inputFieldBp = BPF.inputFieldDoubleNumber();
			inputFieldBp.setObservableValue(observableValues.get(i));
			add(inputFieldBp, "sgx g1");
		}

		//add binding checkbox
		final ObservableBoolean booleanValue = new ObservableBoolean(true);
		final ICheckBox bindingCb = add(BPF.checkBox().setText("Bind").setValue(true).setObservableValue(booleanValue));
		bindingCb.addInputListener(new IInputListener() {
			@Override
			public void inputChanged() {
				for (final IBinding binding : bindings) {
					binding.setBindingState(bindingCb.isSelected());
				}
			}
		});

	}
}
