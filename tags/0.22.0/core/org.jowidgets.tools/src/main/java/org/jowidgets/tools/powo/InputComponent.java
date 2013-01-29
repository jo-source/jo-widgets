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

package org.jowidgets.tools.powo;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.widgets.IInputComponent;
import org.jowidgets.api.widgets.blueprint.builder.IInputComponentSetupBuilder;
import org.jowidgets.api.widgets.descriptor.setup.IInputComponentSetup;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.util.Assert;
import org.jowidgets.util.NullCompatibleEquivalence;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;

class InputComponent<WIDGET_TYPE extends IInputComponent<VALUE_TYPE>, BLUE_PRINT_TYPE extends IWidgetDescriptor<WIDGET_TYPE> & IInputComponentSetup<VALUE_TYPE> & IInputComponentSetupBuilder<?, VALUE_TYPE>, VALUE_TYPE> extends
		Component<WIDGET_TYPE, BLUE_PRINT_TYPE> implements IInputComponent<VALUE_TYPE> {

	private final List<IValidator<VALUE_TYPE>> validators;
	private final List<IInputListener> inputListeners;
	private final List<IValidationConditionListener> validationStateListeners;

	private VALUE_TYPE lastUnmodifiedValue;

	public InputComponent(final BLUE_PRINT_TYPE bluePrint) {
		super(bluePrint);
		this.validators = new LinkedList<IValidator<VALUE_TYPE>>();
		this.inputListeners = new LinkedList<IInputListener>();
		this.validationStateListeners = new LinkedList<IValidationConditionListener>();
		this.lastUnmodifiedValue = null;
	}

	@Override
	void initialize(final WIDGET_TYPE widget) {
		super.initialize(widget);
		for (final IValidator<VALUE_TYPE> validator : validators) {
			widget.addValidator(validator);
		}
		for (final IInputListener inputListener : inputListeners) {
			widget.addInputListener(inputListener);
		}
		for (final IValidationConditionListener listener : validationStateListeners) {
			widget.addValidationConditionListener(listener);
		}
	}

	@Override
	public void setEditable(final boolean editable) {
		if (isInitialized()) {
			getWidget().setEditable(editable);
		}
		else {
			getBluePrint().setEditable(editable);
		}
	}

	@Override
	public void setValue(final VALUE_TYPE value) {
		if (isInitialized()) {
			getWidget().setValue(value);
		}
		else {
			getBluePrint().setValue(value);
		}
	}

	@Override
	public VALUE_TYPE getValue() {
		if (isInitialized()) {
			return getWidget().getValue();
		}
		else {
			return getBluePrint().getValue();
		}
	}

	@Override
	public boolean hasModifications() {
		if (isInitialized()) {
			return getWidget().hasModifications();
		}
		else {
			return !NullCompatibleEquivalence.equals(lastUnmodifiedValue, getValue());
		}
	}

	@Override
	public void resetModificationState() {
		if (isInitialized()) {
			getWidget().resetModificationState();
		}
		else {
			lastUnmodifiedValue = getValue();
		}

	}

	@Override
	public void addValidator(final IValidator<VALUE_TYPE> validator) {
		Assert.paramNotNull(validator, "validator");
		if (isInitialized()) {
			getWidget().addValidator(validator);
		}
		else {
			validators.add(validator);
		}
	}

	@Override
	public void addInputListener(final IInputListener listener) {
		Assert.paramNotNull(listener, "listener");
		if (isInitialized()) {
			getWidget().addInputListener(listener);
		}
		else {
			inputListeners.add(listener);
		}
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		Assert.paramNotNull(listener, "listener");
		if (isInitialized()) {
			getWidget().removeInputListener(listener);
		}
		else {
			inputListeners.remove(listener);
		}
	}

	@Override
	public void addValidationConditionListener(final IValidationConditionListener listener) {
		Assert.paramNotNull(listener, "listener");
		if (isInitialized()) {
			getWidget().addValidationConditionListener(listener);
		}
		else {
			validationStateListeners.add(listener);
		}
	}

	@Override
	public void removeValidationConditionListener(final IValidationConditionListener listener) {
		Assert.paramNotNull(listener, "listener");
		if (isInitialized()) {
			getWidget().removeValidationConditionListener(listener);
		}
		else {
			validationStateListeners.remove(listener);
		}
	}

	@Override
	public IValidationResult validate() {
		checkInitialized();
		return getWidget().validate();
	}

}
