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

package org.jowidgets.examples.common.demo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.validation.IValidator;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.IInputComponent;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.IValidationLabel;
import org.jowidgets.api.widgets.blueprint.IComboBoxBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.IValidationLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.content.IInputContentContainer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.controler.IKeyEvent;
import org.jowidgets.common.widgets.controler.IMouseButtonEvent;
import org.jowidgets.common.widgets.controler.IMouseEvent;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controler.KeyAdapter;
import org.jowidgets.tools.controler.MouseAdapter;

public class DemoForm1ContentCreator implements IInputContentCreator<List<String>> {

	private IComboBox<String> gender;
	private IInputComponent<String> lastname;
	private IInputComponent<String> firstName;
	private IInputComponent<String> street;
	private IInputComponent<String> city;
	private IInputField<Integer> postalCode;
	private IComboBox<String> country;
	private IInputComponent<String> phoneNumber;
	private IInputComponent<String> mail;

	private IValidationLabel genderValidationWidget;
	private IValidationLabel firstnameValidationWidget;
	private IValidationLabel lastnameValidationWidget;
	private IValidationLabel streetValidationWidget;
	private IValidationLabel postalCodeValidationWidget;
	private IValidationLabel cityValidationWidget;
	private IValidationLabel countryValidationWidget;
	private IValidationLabel phoneValidationWidget;
	private IValidationLabel mailValidationWidget;

	@Override
	public void createContent(final IInputContentContainer container) {
		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();
		container.setLayout(new MigLayoutDescriptor("[][grow][20::]", "[]"));

		final String inputWidgetConstraints = "w 270:270:, grow, sg fg";

		final IValidator<String> moreThanOneWordValidator = new IValidator<String>() {

			@Override
			public ValidationResult validate(final String validationInput) {
				final ValidationResult result = new ValidationResult();
				if (validationInput != null && validationInput.trim().length() > 0 && validationInput.trim().contains(" ")) {
					result.addValidationWarning("Input contains more than one word");
				}
				return result;
			};

		};

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
		genderValidationWidget = container.add(validationLabelBp, "wrap");
		genderValidationWidget.registerInputWidget(gender);

		gender.addInputListener(new IInputListener() {
			@Override
			public void inputChanged() {
				//CHECKSTYLE:OFF
				System.out.println(gender.getValue());
				//CHECKSTYLE:ON
			}
		});

		container.add(textLabelBp.setText("Firstname*"), "right, sg lg");
		firstName = container.add(stringMandatoryFieldBp, inputWidgetConstraints);
		firstnameValidationWidget = container.add(validationLabelBp, "wrap");
		firstnameValidationWidget.registerInputWidget(firstName);
		firstName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final IKeyEvent event) {
				//CHECKSTYLE:OFF
				System.out.println(event);
				//CHECKSTYLE:ON
			}
		});

		container.add(textLabelBp.setText("Lastname*"), "right, sg lg");
		lastname = container.add(stringMandatoryFieldBp, inputWidgetConstraints);
		lastnameValidationWidget = container.add(validationLabelBp, "wrap");
		lastnameValidationWidget.registerInputWidget(lastname);

		lastname.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final IMouseButtonEvent mouseEvent) {
				//CHECKSTYLE:OFF
				System.out.println("mousePressed " + mouseEvent);
				//CHECKSTYLE:ON
			}

			@Override
			public void mouseReleased(final IMouseButtonEvent mouseEvent) {
				//CHECKSTYLE:OFF
				System.out.println("mouseReleased " + mouseEvent);
				//CHECKSTYLE:ON
			}

			@Override
			public void mouseDoubleClicked(final IMouseButtonEvent mouseEvent) {
				//CHECKSTYLE:OFF
				System.out.println("mouseDoubleClicked " + mouseEvent);
				//CHECKSTYLE:ON
			}

			@Override
			public void mouseEnter(final IMouseEvent mouseEvent) {
				//CHECKSTYLE:OFF
				System.out.println("mouseEnter " + mouseEvent);
				//CHECKSTYLE:ON
			}

			@Override
			public void mouseExit(final IMouseEvent mouseEvent) {
				//CHECKSTYLE:OFF
				System.out.println("mouseExit " + mouseEvent);
				//CHECKSTYLE:ON
			}
		});

		container.add(textLabelBp.setText("Street*"), "right, sg lg");
		street = container.add(stringMandatoryFieldBp, inputWidgetConstraints);
		streetValidationWidget = container.add(validationLabelBp, "wrap");
		streetValidationWidget.registerInputWidget(street);

		container.add(textLabelBp.setText("Postal code*"), "right, sg lg");
		postalCode = container.add(bpF.inputFieldIntegerNumber().setMaxLength(5).setMandatory(true), inputWidgetConstraints);
		postalCodeValidationWidget = container.add(validationLabelBp, "wrap");
		postalCodeValidationWidget.registerInputWidget(postalCode);

		postalCode.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final IKeyEvent event) {
				//CHECKSTYLE:OFF
				System.out.println("Caret pos: " + postalCode.getCaretPosition());
				//CHECKSTYLE:ON
			}
		});

		postalCode.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final IMouseButtonEvent event) {
				//CHECKSTYLE:OFF
				System.out.println("Caret pos: " + postalCode.getCaretPosition());
				//CHECKSTYLE:ON
			}
		});

		container.add(textLabelBp.setText("City*"), "right, sg lg");
		city = container.add(stringMandatoryFieldBp, inputWidgetConstraints);
		cityValidationWidget = container.add(validationLabelBp, "wrap");
		cityValidationWidget.registerInputWidget(city);

		container.add(textLabelBp.setText("Country*"), "right, sg lg");
		final IComboBoxBluePrint<String> countryBp = bpF.comboBox("Germany", "Spain", "Italy", "United States");
		countryBp.setMandatory(true).setValidator(maxLengthValidator);
		country = container.add(countryBp, inputWidgetConstraints);
		countryValidationWidget = container.add(validationLabelBp, "wrap");
		countryValidationWidget.registerInputWidget(country);

		container.add(textLabelBp.setText("Phone number"), "right, sg lg");
		phoneNumber = container.add(stringFieldBp, inputWidgetConstraints);
		phoneValidationWidget = container.add(validationLabelBp, "wrap");
		phoneValidationWidget.registerInputWidget(phoneNumber);

		container.add(textLabelBp.setText("Email"), "right, sg lg");
		mail = container.add(stringFieldBp, inputWidgetConstraints);
		mail.addValidator(moreThanOneWordValidator);
		mailValidationWidget = container.add(validationLabelBp, "wrap");
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
	public void setValue(final List<String> value) {
		if (value != null) {
			final ArrayList<String> values = new ArrayList<String>(value);
			try {
				gender.setValue(values.get(0));
			}
			catch (final Exception e1) {
				//DO NOTHING
			}
			firstName.setValue(values.get(1));
			lastname.setValue(value.get(2));
			street.setValue(values.get(3));
			try {
				postalCode.setValue(Integer.valueOf(values.get(4)));
			}
			catch (final NumberFormatException e) {
				//DO NOTHING
			}
			city.setValue(values.get(5));
			country.setValue(values.get(6));
			phoneNumber.setValue(values.get(7));
			mail.setValue(values.get(8));
		}
		else {
			gender.setValue(" ");
			lastname.setValue(null);
			firstName.setValue(null);
			street.setValue(null);
			postalCode.setValue(null);
			city.setValue(null);
			country.setValue(null);
			phoneNumber.setValue(null);
			mail.setValue(null);
		}

		genderValidationWidget.resetValidation();
		firstnameValidationWidget.resetValidation();
		lastnameValidationWidget.resetValidation();
		streetValidationWidget.resetValidation();
		postalCodeValidationWidget.resetValidation();
		cityValidationWidget.resetValidation();
		countryValidationWidget.resetValidation();
		phoneValidationWidget.resetValidation();
		mailValidationWidget.resetValidation();
	}

	@Override
	public List<String> getValue() {
		final List<String> result = new LinkedList<String>();
		result.add(getStringValue(gender.getValue()));
		result.add(getStringValue(firstName.getValue()));
		result.add(getStringValue(lastname.getValue()));
		result.add(getStringValue(street.getValue()));
		result.add(getStringValue(postalCode.getValue()));
		result.add(getStringValue(city.getValue()));
		result.add(getStringValue(country.getValue()));
		result.add(getStringValue(phoneNumber.getValue()));
		result.add(getStringValue(mail.getValue()));
		return result;
	}

	private String getStringValue(final Object object) {
		if (object == null) {
			return "";
		}
		else {
			return object.toString();
		}
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
