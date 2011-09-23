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

package org.jowidgets.tools.widgets.wrapper;

import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.tools.controller.InputObservable;
import org.jowidgets.tools.validation.CompoundValidator;
import org.jowidgets.tools.validation.ValidationCache;
import org.jowidgets.tools.validation.ValidationCache.IValidationResultCreator;
import org.jowidgets.util.Assert;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidationResultBuilder;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

public abstract class AbstractInputControl<VALUE_TYPE> extends ControlWrapper implements IInputControl<VALUE_TYPE> {

	private final ValidationCache validationCache;
	private final CompoundValidator<VALUE_TYPE> compoundValidator;
	private final InputObservable inputObservable;

	public AbstractInputControl(final IControl control) {
		super(control);

		this.inputObservable = new InputObservable();

		this.compoundValidator = new CompoundValidator<VALUE_TYPE>();
		this.validationCache = new ValidationCache(new IValidationResultCreator() {
			@Override
			public IValidationResult createValidationResult() {
				final IValidationResultBuilder builder = ValidationResult.builder();

				builder.addResult(AbstractInputControl.this.createValidationResult());

				builder.addResult(compoundValidator.validate(getValue()));
				return builder.build();
			}
		});

	}

	protected abstract IValidationResult createValidationResult();

	@Override
	public final void addValidator(final IValidator<VALUE_TYPE> validator) {
		Assert.paramNotNull(validator, "validator");
		compoundValidator.addValidator(validator);
	}

	@Override
	public final IValidationResult validate() {
		return validationCache.validate();
	}

	@Override
	public final void addValidationConditionListener(final IValidationConditionListener listener) {
		validationCache.addValidationConditionListener(listener);
	}

	@Override
	public final void removeValidationConditionListener(final IValidationConditionListener listener) {
		validationCache.removeValidationConditionListener(listener);
	}

	@Override
	public final void addInputListener(final IInputListener listener) {
		inputObservable.addInputListener(listener);
	}

	@Override
	public final void removeInputListener(final IInputListener listener) {
		inputObservable.removeInputListener(listener);
	}

	protected final void fireInputChanged() {
		inputObservable.fireInputChanged();
	}
}
