/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.impl.widgets.composed;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.ITextControl;
import org.jowidgets.api.widgets.descriptor.setup.IInputFieldSetup;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.tools.validation.CompoundValidator;
import org.jowidgets.tools.validation.ValidationCache;
import org.jowidgets.tools.validation.ValidationCache.IValidationResultCreator;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.NullCompatibleEquivalence;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.Validator;

public class InputFieldImpl<VALUE_TYPE> extends ControlWrapper implements IInputField<VALUE_TYPE> {

	private final IConverter<VALUE_TYPE> converter;
	private final CompoundValidator<VALUE_TYPE> compoundValidator;
	private final IValidator<String> stringValidator;
	private final ValidationCache validationCache;

	private String lastUnmodifiedTextValue;

	public InputFieldImpl(final ITextControl textField, final IInputFieldSetup<VALUE_TYPE> setup) {

		super(textField);

		this.compoundValidator = new CompoundValidator<VALUE_TYPE>();
		this.converter = setup.getConverter();

		if (converter.getStringValidator() != null) {
			this.stringValidator = converter.getStringValidator();
		}
		else {
			this.stringValidator = Validator.okValidator();
		}

		compoundValidator.addValidator(setup.getValidator());

		this.validationCache = new ValidationCache(new IValidationResultCreator() {
			@Override
			public IValidationResult createValidationResult() {
				final IValidationResult result = stringValidator.validate(getText());
				//if the converter could not parse the input, do not make more validation
				if (!result.isValid()) {
					return result;
				}
				else {
					return result.withResult(compoundValidator.validate(getValue()));
				}
			}
		});

		textField.addInputListener(new IInputListener() {
			@Override
			public void inputChanged() {
				validationCache.setDirty();
			}
		});

		setEditable(setup.isEditable());
		VisibiliySettingsInvoker.setVisibility(setup, this);
		ColorSettingsInvoker.setColors(setup, this);

		if (setup.getValue() != null) {
			setValue(setup.getValue());
		}

		resetModificationState();
	}

	@Override
	protected ITextControl getWidget() {
		return (ITextControl) super.getWidget();
	}

	@Override
	public boolean hasModifications() {
		return !NullCompatibleEquivalence.equals(lastUnmodifiedTextValue, getText());
	}

	@Override
	public void resetModificationState() {
		this.lastUnmodifiedTextValue = getText();
	}

	@Override
	public VALUE_TYPE getValue() {
		return converter.convertToObject(getWidget().getText());
	}

	@Override
	public void setValue(final VALUE_TYPE value) {
		getWidget().setText(converter.convertToString(value));
	}

	@Override
	public void selectAll() {
		getWidget().selectAll();
	}

	@Override
	public void setSelection(final int start, final int end) {
		getWidget().setSelection(start, end);
	}

	@Override
	public void setCaretPosition(final int pos) {
		getWidget().setCaretPosition(pos);
	}

	@Override
	public int getCaretPosition() {
		return getWidget().getCaretPosition();
	}

	@Override
	public String getText() {
		return getWidget().getText();
	}

	@Override
	public void addValidator(final IValidator<VALUE_TYPE> validator) {
		compoundValidator.addValidator(validator);
	}

	@Override
	public IValidationResult validate() {
		return validationCache.validate();
	}

	@Override
	public void addValidationConditionListener(final IValidationConditionListener listener) {
		validationCache.addValidationConditionListener(listener);
	}

	@Override
	public void removeValidationConditionListener(final IValidationConditionListener listener) {
		validationCache.removeValidationConditionListener(listener);
	}

	@Override
	public void setEditable(final boolean editable) {
		getWidget().setEditable(editable);
	}

	@Override
	public void addInputListener(final IInputListener listener) {
		getWidget().addInputListener(listener);
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		getWidget().removeInputListener(listener);
	}

}
