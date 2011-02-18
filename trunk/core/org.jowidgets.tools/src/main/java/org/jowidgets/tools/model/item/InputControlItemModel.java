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

package org.jowidgets.tools.model.item;

import java.util.HashMap;
import java.util.Map;

import org.jowidgets.api.model.item.IContainerContentCreator;
import org.jowidgets.api.model.item.IContainerItemModel;
import org.jowidgets.api.model.item.IContainerItemModelBuilder;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.descriptor.setup.IInputComponentSetup;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.controler.IInputObservable;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controler.InputObservable;
import org.jowidgets.util.Tuple;

/**
 * This class represents an IContainerItemModel that creates input controls from an descriptor
 * for their contents an synchronizes their values if the model is used in more than one toolbar.
 * 
 * @param <VALUE_TYPE> The value type of the input control
 */
public class InputControlItemModel<VALUE_TYPE> extends AbstractItemModelWrapper implements IContainerItemModel, IInputObservable {

	private final IContainerContentCreator contentCreator;
	private final Map<IContainer, Tuple<IInputControl<VALUE_TYPE>, IInputListener>> controls;
	private final InputObservable inputObservable;

	private VALUE_TYPE value;

	@SuppressWarnings("unchecked")
	public InputControlItemModel(final IWidgetDescriptor<? extends IInputControl<VALUE_TYPE>> descriptor) {
		super(Toolkit.getModelFactoryProvider().getItemModelFactory().containerItem());

		this.inputObservable = new InputObservable();
		this.controls = new HashMap<IContainer, Tuple<IInputControl<VALUE_TYPE>, IInputListener>>();

		if (descriptor instanceof IInputComponentSetup) {
			this.value = ((IInputComponentSetup<VALUE_TYPE>) descriptor).getValue();
		}

		this.contentCreator = new IContainerContentCreator() {

			@Override
			public void createContent(final IContainer container) {
				container.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "0[grow, 0::]0"));
				final IInputControl<VALUE_TYPE> control = container.add(descriptor, "growx, growy, 0::, 0::");
				control.setValue(value);

				final IInputListener syncListener = new InputListener(control);
				control.addInputListener(syncListener);
				controls.put(container, new Tuple<IInputControl<VALUE_TYPE>, IInputListener>(control, syncListener));
			}

			@Override
			public void containerDisposed(final IContainer container) {
				final Tuple<IInputControl<VALUE_TYPE>, IInputListener> controlTuple = controls.remove(container);
				if (controlTuple != null) {
					controlTuple.getFirst().removeInputListener(controlTuple.getSecond());
				}
			}
		};
	}

	@Override
	public IContainerContentCreator getContentCreator() {
		return contentCreator;
	}

	@Override
	public void setContentCreator(final IContainerContentCreator contentCreator) {
		throw new UnsupportedOperationException("The content creator of this model is imutable");
	}

	public VALUE_TYPE getValue() {
		return value;
	}

	public void setValue(final VALUE_TYPE value) {
		this.value = value;
		changeValues(value, null);
	}

	@Override
	public IContainerItemModel createCopy() {
		final IContainerItemModelBuilder result = Toolkit.getModelFactoryProvider().getItemModelFactory().containerItemBuilder();
		result.setId(getId());
		result.setText(getText());
		result.setToolTipText(getToolTipText());
		result.setIcon(getIcon());
		result.setAccelerator(getAccelerator());
		result.setMnemonic(getMnemonic());
		result.setEnabled(isEnabled());
		return result.build();
	}

	private void changeValues(final VALUE_TYPE value, final IInputControl<VALUE_TYPE> sourceControl) {
		for (final Tuple<IInputControl<VALUE_TYPE>, IInputListener> controlTuple : controls.values()) {
			final IInputListener listener = controlTuple.getSecond();
			final IInputControl<VALUE_TYPE> childControl = controlTuple.getFirst();
			if (sourceControl != childControl) {
				childControl.removeInputListener(listener);
				childControl.setValue(value);
				childControl.addInputListener(listener);
			}
		}
		inputObservable.fireInputChanged(sourceControl);
	}

	private class InputListener implements IInputListener {

		private final IInputControl<VALUE_TYPE> control;

		public InputListener(final IInputControl<VALUE_TYPE> control) {
			this.control = control;
		}

		@Override
		public void inputChanged(final Object source) {
			value = control.getValue();
			changeValues(value, control);
		}
	}

	@Override
	public void addInputListener(final IInputListener listener) {
		inputObservable.addInputListener(listener);
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		inputObservable.removeInputListener(listener);
	}
}
