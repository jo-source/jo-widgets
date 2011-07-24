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
package org.jowidgets.examples.common;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IInputComponent;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.content.IInputContentContainer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.validation.MandatoryValidator;
import org.jowidgets.util.IDecorator;
import org.jowidgets.validation.IValidationMessage;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.MessageType;
import org.jowidgets.validation.ValidationResult;

public class HelloContentCreator implements IInputContentCreator<String> {

	private final int textFieldCount = 10;

	@SuppressWarnings("unchecked")
	private final IInputComponent<String>[] widgets = new IInputComponent[textFieldCount];

	@Override
	public void createContent(final IInputContentContainer widgetContainer) {
		final IBluePrintFactory bpF = Toolkit.getInstance().getBluePrintFactory();

		final IValidator<String> characterLenghtValidator = new IValidator<String>() {
			@Override
			public IValidationResult validate(final String validationInput) {
				if (validationInput != null && validationInput.length() > 15) {
					return ValidationResult.error("Only 15 characters allowed");
				}
				return ValidationResult.ok();
			}
		};

		widgetContainer.setLayout(new MigLayoutDescriptor("[][grow, 250:250:800][250::800]", ""));
		for (int i = 0; i < widgets.length; i++) {

			final boolean mandatory = i <= 5;
			final String mandatoryMarker = mandatory ? "*" : "";

			final String label = "Field" + i;

			widgetContainer.add(bpF.textLabel(label + mandatoryMarker), "");

			final IInputFieldBluePrint<String> textFieldBp = bpF.inputFieldString();

			textFieldBp.setValidator(characterLenghtValidator);

			widgets[i] = widgetContainer.add(label, textFieldBp, "growx");
			if (mandatory) {
				widgets[i].addValidator(new MandatoryValidator<String>("*mandatory field"));
			}

			final IDecorator<IValidationResult> initialDecorator = new IDecorator<IValidationResult>() {
				@Override
				public IValidationResult decorate(final IValidationResult original) {
					final IValidationMessage worstFirst = original.getWorstFirst();
					if (worstFirst.getType().equalOrWorse(MessageType.ERROR)) {
						return ValidationResult.infoError("*mandator field");
					}
					else {
						return ValidationResult.create().withInfo("optional");
					}
				}
			};

			widgetContainer.add(
					bpF.inputComponentValidationLabel(widgets[i]).setInitialValidationDecorator(initialDecorator),
					"wrap");
		}

	}

	@Override
	public void setValue(final String content) {}

	@Override
	public String getValue() {
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < widgets.length; i++) {
			builder.append(widgets[i].getValue() + "\n");
		}
		return builder.toString();
	}

}
