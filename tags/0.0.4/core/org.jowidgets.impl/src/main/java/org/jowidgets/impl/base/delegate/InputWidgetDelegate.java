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

package org.jowidgets.impl.base.delegate;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.validation.IValidateable;
import org.jowidgets.api.validation.IValidator;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.access.IInputValueAccessor;
import org.jowidgets.api.widgets.descriptor.setup.IInputWidgetSetup;
import org.jowidgets.common.widgets.controler.impl.InputObservable;
import org.jowidgets.util.Assert;
import org.jowidgets.util.EmptyCheck;

public class InputWidgetDelegate<VALUE_TYPE> extends InputObservable {

	private final IInputValueAccessor<VALUE_TYPE> valueAccessor;
	private final List<IValidator<VALUE_TYPE>> validators;
	private final List<IValidateable> validatables;

	private boolean mandatory;

	public InputWidgetDelegate(final IInputValueAccessor<VALUE_TYPE> valueAccessor, final IInputWidgetSetup<VALUE_TYPE> setup) {
		this(valueAccessor, setup.getValidator(), setup.isMandatory(), setup.isEditable());
	}

	public InputWidgetDelegate(
		final IInputValueAccessor<VALUE_TYPE> valueAccessor,
		final IValidator<VALUE_TYPE> validator,
		final boolean mandatory,
		final boolean editable) {
		Assert.paramNotNull(valueAccessor, "valueAccessor");

		this.validators = new LinkedList<IValidator<VALUE_TYPE>>();
		this.validatables = new LinkedList<IValidateable>();
		this.valueAccessor = valueAccessor;
		this.mandatory = mandatory;

		if (validator != null) {
			addValidator(validator);
		}
	}

	public final ValidationResult validate() {
		final ValidationResult result = new ValidationResult();

		for (final IValidator<VALUE_TYPE> validator : validators) {
			result.addValidationResult(validator.validate(valueAccessor.getValue()));
		}

		for (final IValidateable validateable : validatables) {
			result.addValidationResult(validateable.validate());
		}

		return result;
	}

	public final boolean isMandatory() {
		return mandatory;
	}

	public final void setMandatory(final boolean mandatory) {
		this.mandatory = mandatory;
	}

	public final void addValidator(final IValidator<VALUE_TYPE> validator) {
		Assert.paramNotNull(validator, "validator");
		validators.add(validator);
	}

	public final void addValidatable(final IValidateable validateable) {
		Assert.paramNotNull(validateable, "validateable");
		validatables.add(validateable);
	}

	public boolean isEmpty() {
		return EmptyCheck.isEmpty(valueAccessor.getValue());
	}

}
