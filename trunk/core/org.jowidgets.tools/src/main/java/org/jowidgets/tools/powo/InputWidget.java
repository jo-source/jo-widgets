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

import org.jowidgets.api.validation.IValidator;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.IInputWidget;
import org.jowidgets.api.widgets.blueprint.builder.IInputWidgetSetupBuilder;
import org.jowidgets.api.widgets.descriptor.setup.IInputWidgetSetup;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.util.Assert;

class InputWidget<WIDGET_TYPE extends IInputWidget<VALUE_TYPE>, BLUE_PRINT_TYPE extends IWidgetDescriptor<WIDGET_TYPE> & IInputWidgetSetup<VALUE_TYPE> & IInputWidgetSetupBuilder<?, VALUE_TYPE>, VALUE_TYPE> extends
		ChildWidget<WIDGET_TYPE, BLUE_PRINT_TYPE> implements IInputWidget<VALUE_TYPE> {

	private final List<IValidator<VALUE_TYPE>> validators;
	private final List<IInputListener> inputListeners;

	public InputWidget(final BLUE_PRINT_TYPE bluePrint) {
		super(bluePrint);
		this.validators = new LinkedList<IValidator<VALUE_TYPE>>();
		this.inputListeners = new LinkedList<IInputListener>();
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
	public boolean isMandatory() {
		if (isInitialized()) {
			return getWidget().isMandatory();
		}
		else {
			return getBluePrint().isMandatory();
		}
	}

	@Override
	public void setMandatory(final boolean mandatory) {
		if (isInitialized()) {
			getWidget().setMandatory(mandatory);
		}
		else {
			getBluePrint().setMandatory(mandatory);
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
	public ValidationResult validate() {
		checkInitialized();
		return getWidget().validate();
	}

	@Override
	public boolean isEmpty() {
		checkInitialized();
		return getWidget().isEmpty();
	}

}
