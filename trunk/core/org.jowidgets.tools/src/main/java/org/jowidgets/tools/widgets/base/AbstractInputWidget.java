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

package org.jowidgets.tools.widgets.base;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.validation.IValidateable;
import org.jowidgets.api.validation.IValidator;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.IInputWidget;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.descriptor.setup.IInputWidgetSetup;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.controler.impl.InputObservable;
import org.jowidgets.tools.widgets.delegate.InputValidationDelegate;
import org.jowidgets.tools.widgets.wrapper.WidgetWrapper;
import org.jowidgets.util.Assert;
import org.jowidgets.util.EmptyCheck;

public abstract class AbstractInputWidget<VALUE_TYPE> extends WidgetWrapper implements IInputWidget<VALUE_TYPE> {

	private final InputObservable inputObservable;
	private final InputValidationDelegate<VALUE_TYPE> inputValidationDelegate;
	private final IInputListener inputListener;
	private final List<IInputWidget<?>> inputWidgets;

	public AbstractInputWidget(final IWidget widget, final IInputWidgetSetup<VALUE_TYPE> setup) {
		this(widget, setup.getValidator(), setup.isMandatory());
	}

	public AbstractInputWidget(final IWidget widget, final boolean mandatory) {
		this(widget, null, mandatory);
	}

	public AbstractInputWidget(final IWidget widget, final IValidator<VALUE_TYPE> validator, final boolean mandatory) {
		super(widget);
		this.inputObservable = new InputObservable();
		this.inputValidationDelegate = new InputValidationDelegate<VALUE_TYPE>(validator, mandatory);
		this.inputWidgets = new LinkedList<IInputWidget<?>>();

		this.inputListener = new IInputListener() {

			@Override
			public void inputChanged(final Object source) {
				fireInputChanged(source);
			}
		};
	}

	@Override
	public void setEditable(final boolean editable) {
		for (final IInputWidget<?> inputWidget : inputWidgets) {
			inputWidget.setEditable(editable);
		}
	}

	@Override
	public boolean isEmpty() {
		return EmptyCheck.isEmpty(getValue());
	}

	@Override
	public boolean isMandatory() {
		return inputValidationDelegate.isMandatory();
	}

	@Override
	public void setMandatory(final boolean mandatory) {
		inputValidationDelegate.setMandatory(mandatory);
	}

	@Override
	public void addValidator(final IValidator<VALUE_TYPE> validator) {
		inputValidationDelegate.addValidator(validator);
	}

	public void addValidatable(final IValidateable validateable, final String context) {
		inputValidationDelegate.addValidatable(validateable, context);
	}

	public void addValidatable(final IValidateable validateable) {
		inputValidationDelegate.addValidatable(validateable);
	}

	public void removeValidatable(final IValidateable validateable) {
		inputValidationDelegate.removeValidatable(validateable);
	}

	@Override
	public ValidationResult validate() {
		return inputValidationDelegate.validate(getValue());
	}

	@Override
	public void addInputListener(final IInputListener listener) {
		inputObservable.addInputListener(listener);
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		inputObservable.removeInputListener(listener);
	}

	protected void fireInputChanged(final Object source) {
		inputObservable.fireInputChanged(source);
	}

	protected void registerInputWidget(final IInputWidget<?> inputWidget) {
		registerInputWidget(inputWidget, null);
	}

	protected void registerInputWidget(final IInputWidget<?> inputWidget, final String validationContext) {
		Assert.paramNotNull(inputWidget, "inputWidget");
		inputWidgets.add(inputWidget);
		inputWidget.addInputListener(inputListener);
		inputValidationDelegate.addValidatable(inputWidget, validationContext);
	}

	protected void unRegisterInputWidget(final IInputWidget<?> inputWidget) {
		Assert.paramNotNull(inputWidget, "inputWidget");
		if (inputWidgets.contains(inputWidget)) {
			inputWidgets.remove(inputWidget);
		}
		else {
			throw new IllegalArgumentException("Input widget '" + inputWidget + "' is not registered");
		}
		inputWidget.removeInputListener(inputListener);
		inputValidationDelegate.removeValidatable(inputWidget);
	}

	protected List<IInputWidget<?>> getRegisteredWidgets() {
		return new LinkedList<IInputWidget<?>>(inputWidgets);
	}
}
