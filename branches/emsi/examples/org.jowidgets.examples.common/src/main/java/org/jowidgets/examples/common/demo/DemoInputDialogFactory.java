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

package org.jowidgets.examples.common.demo;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.validation.IValidator;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.IComboBoxWidget;
import org.jowidgets.api.widgets.IInputDialogWidget;
import org.jowidgets.api.widgets.IInputWidget;
import org.jowidgets.api.widgets.IValidationLabelWidget;
import org.jowidgets.api.widgets.IWindowWidget;
import org.jowidgets.api.widgets.blueprint.IComboBoxBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.IValidationLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.content.IInputContentContainer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;

public class DemoInputDialogFactory {

	public IInputDialogWidget<String> create(final IWindowWidget parent) {
		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();
		final IInputDialogBluePrint<String> inputDialogBp = bpF.inputDialog(new DemoInputDialogContent());
		inputDialogBp.setTitle("Input dialog demo");
		inputDialogBp.setMissingInputText("Please fill out all mandatory (*) fields");
		if (parent != null) {
			return parent.createChildWindow(inputDialogBp);
		}
		else {
			return Toolkit.getWidgetFactory().create(inputDialogBp);
		}
	}

	private class DemoInputDialogContent implements IInputContentCreator<String> {

		private IComboBoxWidget<String> gender;
		private IInputWidget<String> lastname;
		private IInputWidget<String> firstName;
		private IInputWidget<String> street;
		private IInputWidget<String> city;
		private IInputWidget<Integer> postalCode;
		private IComboBoxWidget<String> country;
		private IInputWidget<String> phoneNumber;
		private IInputWidget<String> mail;

		@Override
		public void createContent(final IInputContentContainer container) {
			final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();
			container.setLayout(new MigLayoutDescriptor("[][grow][20::]", "[]"));

			final String inputWidgetConstraints = "w 270:270:, grow, sg fg";

			final IValidator<String> maxLengthValidator = new IValidator<String>() {

				@Override
				public ValidationResult validate(final String validationInput) {
					final ValidationResult result = new ValidationResult();
					if (validationInput != null && validationInput.length() > 50) {
						result.addValidationError("Input must not have more than 50 characters");
					}
					return result;
				};

			};

			final ITextLabelBluePrint textLabelBp = bpF.textLabel().alignRight();

			final IValidationLabelBluePrint validationLabelBp = bpF.validationLabel().setShowValidationMessage(false);

			final IInputFieldBluePrint<String> stringMandatoryFieldBp = bpF.inputFieldString().setMandatory(true).setMaxLength(51);
			final IInputFieldBluePrint<String> stringFieldBp = bpF.inputFieldString().setMandatory(false).setMaxLength(51);
			stringMandatoryFieldBp.setValidator(maxLengthValidator);
			stringFieldBp.setValidator(maxLengthValidator);

			container.add(textLabelBp.setText("Gender"), "right, sg lg");
			gender = container.add(bpF.comboBoxSelection("Male", "Female", " ").setMandatory(false), inputWidgetConstraints);
			final IValidationLabelWidget genderValidationWidget = container.add(validationLabelBp, "wrap");
			genderValidationWidget.registerInputWidget(gender);

			container.add(textLabelBp.setText("Firstname*"), "right, sg lg");
			firstName = container.add(stringMandatoryFieldBp, inputWidgetConstraints);
			final IValidationLabelWidget firstnameValidationWidget = container.add(validationLabelBp, "wrap");
			firstnameValidationWidget.registerInputWidget(firstName);

			container.add(textLabelBp.setText("Lastname*"), "right, sg lg");
			lastname = container.add(stringMandatoryFieldBp, inputWidgetConstraints);
			final IValidationLabelWidget lastnameValidationWidget = container.add(validationLabelBp, "wrap");
			lastnameValidationWidget.registerInputWidget(lastname);

			container.add(textLabelBp.setText("Street*"), "right, sg lg");
			street = container.add(stringMandatoryFieldBp, inputWidgetConstraints);
			final IValidationLabelWidget streetValidationWidget = container.add(validationLabelBp, "wrap");
			streetValidationWidget.registerInputWidget(street);

			container.add(textLabelBp.setText("Postal code*"), "right, sg lg");
			postalCode = container.add(bpF.inputFieldIntegerNumber().setMaxLength(5).setMandatory(true), inputWidgetConstraints);
			final IValidationLabelWidget postalCodeValidationWidget = container.add(validationLabelBp, "wrap");
			postalCodeValidationWidget.registerInputWidget(postalCode);

			container.add(textLabelBp.setText("City*"), "right, sg lg");
			city = container.add(stringMandatoryFieldBp, inputWidgetConstraints);
			final IValidationLabelWidget cityValidationWidget = container.add(validationLabelBp, "wrap");
			cityValidationWidget.registerInputWidget(city);

			container.add(textLabelBp.setText("Country*"), "right, sg lg");
			final IComboBoxBluePrint<String> countryBp = bpF.comboBox("Germany", "Spain", "Italy", "United States");
			countryBp.setMandatory(true).setValidator(maxLengthValidator);
			country = container.add(countryBp, inputWidgetConstraints);
			final IValidationLabelWidget countryValidationWidget = container.add(validationLabelBp, "wrap");
			countryValidationWidget.registerInputWidget(country);

			container.add(textLabelBp.setText("Phone number"), "right, sg lg");
			phoneNumber = container.add(stringFieldBp, inputWidgetConstraints);
			final IValidationLabelWidget phoneValidationWidget = container.add(validationLabelBp, "wrap");
			phoneValidationWidget.registerInputWidget(phoneNumber);

			container.add(textLabelBp.setText("Email"), "right, sg lg");
			mail = container.add(stringFieldBp, inputWidgetConstraints);
			final IValidationLabelWidget mailValidationWidget = container.add(validationLabelBp, "wrap");
			mailValidationWidget.registerInputWidget(mail);

			container.registerInputWidget("Gender", gender);
			container.registerInputWidget("Lastname", lastname);
			container.registerInputWidget("Firstname", firstName);
			container.registerInputWidget("Street", street);
			container.registerInputWidget("Postal code", postalCode);
			container.registerInputWidget("City", city);
			container.registerInputWidget("Country", country);
			container.registerInputWidget("Phone Number", phoneNumber);
			container.registerInputWidget("Mail", mail);
		}

		@Override
		public void setValue(final String value) {}

		@Override
		public String getValue() {
			return null;
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

}
