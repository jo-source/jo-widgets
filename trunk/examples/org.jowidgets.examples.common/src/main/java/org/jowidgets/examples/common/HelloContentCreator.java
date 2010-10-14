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

import org.jowidgets.api.look.Markup;
import org.jowidgets.api.validation.IValidator;
import org.jowidgets.api.validation.ValidationMessageType;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.IInputWidget;
import org.jowidgets.api.widgets.IValidationLabelWidget;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.IValidationLabelBluePrint;
import org.jowidgets.api.widgets.content.IInputContentContainer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.api.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.impl.widgets.composed.blueprint.BluePrintFactory;

public class HelloContentCreator implements IInputContentCreator<String> {

	private final int textFieldCount = 10;

	@SuppressWarnings("unchecked")
	private final IInputWidget<String>[] widgets = new IInputWidget[textFieldCount];

	@Override
	public void createContent(final IInputContentContainer widgetContainer) {
		final BluePrintFactory bpF = new BluePrintFactory();

		final IValidator<String> characterLenghtValidator = new IValidator<String>() {

			@Override
			public ValidationResult validate(final String validationInput) {
				if (validationInput != null && validationInput.length() > 15) {
					return new ValidationResult(ValidationMessageType.ERROR, "Only 15 characters allowed");
				}
				return new ValidationResult();
			}

		};

		widgetContainer.setLayout(new MigLayoutDescriptor("[][grow, 250:250:800][250::800]", ""));
		for (int i = 0; i < widgets.length; i++) {

			final boolean mandatory = i <= 5;
			final String mandatoryMarker = mandatory ? "*" : "";

			final String label = "Field" + i;

			widgetContainer.add(bpF.textLabel(label + mandatoryMarker), "");

			final IInputFieldBluePrint<String> textFieldBp = bpF.inputFieldString();

			textFieldBp.setMandatory(mandatory);
			textFieldBp.setValidator(characterLenghtValidator);

			widgets[i] = widgetContainer.add(textFieldBp, "growx");
			widgetContainer.registerInputWidget(label, widgets[i]);

			final IValidationLabelBluePrint validationLabelDescr = bpF.validationLabel();
			if (mandatory) {
				validationLabelDescr.setMissingInputText("*mandatory field");
			}
			else {
				validationLabelDescr.setMissingInputText("optional field").setMissingInputMarkup(Markup.DEFAULT);
			}

			final IValidationLabelWidget validationLabelWidget = widgetContainer.add(validationLabelDescr, "wrap");
			validationLabelWidget.registerInputWidget(widgets[i]);
		}

	}

	@Override
	public void setValue(final String content) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getValue() {
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < widgets.length; i++) {
			builder.append(widgets[i].getValue() + "\n");
		}
		return builder.toString();
	}

	@Override
	public ValidationResult validate() {
		return new ValidationResult();
	}

	@Override
	public boolean isMandatory() {
		return true;
	}

}
