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

package org.jowidgets.tools.widgets.delegate;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.widgets.descriptor.setup.IInputComponentSetup;
import org.jowidgets.util.Assert;
import org.jowidgets.util.Tuple;
import org.jowidgets.validation.IValidateable;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidationResultBuilder;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

public class InputValidationDelegate<VALUE_TYPE> {

	private final List<IValidator<VALUE_TYPE>> validators;
	private final List<Tuple<IValidateable, String>> validateables;

	private boolean mandatory;

	public InputValidationDelegate(final IInputComponentSetup<VALUE_TYPE> setup) {
		this(setup.getValidator(), setup.isMandatory());
	}

	public InputValidationDelegate(final boolean mandatory) {
		this(null, mandatory);
	}

	public InputValidationDelegate(final IValidator<VALUE_TYPE> validator, final boolean mandatory) {
		super();
		this.validateables = new LinkedList<Tuple<IValidateable, String>>();
		this.validators = new LinkedList<IValidator<VALUE_TYPE>>();
		if (validator != null) {
			validators.add(validator);
		}
		this.mandatory = mandatory;
	}

	public void addValidatable(final IValidateable validateable) {
		addValidatable(validateable, null);
	}

	public void addValidatable(final IValidateable validateable, final String validationContext) {
		Assert.paramNotNull(validateable, "validateable");
		validateables.add(new Tuple<IValidateable, String>(validateable, validationContext));
	}

	public void removeValidatable(final IValidateable validateable) {
		Assert.paramNotNull(validateable, "validateable");
		final Tuple<IValidateable, String> validatableTuple = findValidatable(validateable);
		if (validatableTuple != null) {
			validateables.remove(validatableTuple);
		}
	}

	public IValidationResult validate(final VALUE_TYPE value) {
		final IValidationResultBuilder builder = ValidationResult.builder();

		for (final Tuple<IValidateable, String> validateable : validateables) {
			if (validateable.getSecond() == null) {
				builder.addResult(validateable.getFirst().validate());
			}
			else {
				builder.addResult(validateable.getSecond(), validateable.getFirst().validate());
			}
		}

		for (final IValidator<VALUE_TYPE> validator : validators) {
			builder.addResult(validator.validate(value));
		}
		return builder.build();
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(final boolean mandatory) {
		this.mandatory = mandatory;
	}

	public void addValidator(final IValidator<VALUE_TYPE> validator) {
		Assert.paramNotNull(validator, "validator");
		validators.add(validator);
	}

	private Tuple<IValidateable, String> findValidatable(final IValidateable validateable) {
		for (final Tuple<IValidateable, String> validateableTuple : validateables) {
			if (validateableTuple.getFirst() == validateable) {
				return validateableTuple;
			}
		}
		return null;
	}

}
