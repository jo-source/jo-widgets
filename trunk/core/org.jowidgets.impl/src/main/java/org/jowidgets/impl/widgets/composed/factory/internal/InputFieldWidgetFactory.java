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
package org.jowidgets.impl.widgets.composed.factory.internal;

import org.jowidgets.api.validation.ITextInputValidator;
import org.jowidgets.api.validation.ValidationMessage;
import org.jowidgets.api.validation.ValidationMessageType;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.IInputComponent;
import org.jowidgets.api.widgets.blueprint.ITextFieldBluePrint;
import org.jowidgets.api.widgets.descriptor.IInputFieldDescriptor;
import org.jowidgets.api.widgets.descriptor.setup.ITextFieldSetup;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactory;
import org.jowidgets.impl.widgets.composed.InputFieldWidget;
import org.jowidgets.impl.widgets.composed.blueprint.BluePrintFactory;
import org.jowidgets.spi.IWidgetFactorySpi;

public class InputFieldWidgetFactory<VALUE_TYPE> implements
		IWidgetFactory<IInputComponent<VALUE_TYPE>, IInputFieldDescriptor<VALUE_TYPE>> {

	private final IGenericWidgetFactory genericFactory;

	public InputFieldWidgetFactory(final IGenericWidgetFactory genericFactory) {
		super();
		this.genericFactory = genericFactory;
	}

	@Override
	public IInputComponent<VALUE_TYPE> create(final Object parentUiReference, final IInputFieldDescriptor<VALUE_TYPE> descriptor) {

		final BluePrintFactory bpF = new BluePrintFactory();

		final ITextInputValidator inputValidator = descriptor.getConverter();

		final ITextFieldBluePrint textFieldBluePrint = bpF.textField();
		textFieldBluePrint.setTextInputValidator(new ITextInputValidator() {

			@Override
			public ValidationResult validate(final String validationInput) {
				return inputValidator.validate(validationInput);
			}

			@Override
			public ValidationMessage isCompletableToValid(final String string) {
				if (string != null && string.trim().length() > descriptor.getMaxLength()) {
					return new ValidationMessage(ValidationMessageType.ERROR, "Only '"
						+ descriptor.getMaxLength()
						+ "' are allowed");
				}
				return inputValidator.isCompletableToValid(string);
			}
		});

		final IInputControl<String> textFieldWidget = genericFactory.create(parentUiReference, textFieldBluePrint);

		if (textFieldWidget == null) {
			throw new IllegalStateException("Could not create widget with descriptor interface class '"
				+ ITextFieldSetup.class
				+ "' from '"
				+ IWidgetFactorySpi.class.getName()
				+ "'");
		}

		return new InputFieldWidget<VALUE_TYPE>(textFieldWidget, descriptor);
	}
}
