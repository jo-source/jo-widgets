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
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.IInputComponent;
import org.jowidgets.api.widgets.IInputComponentValidationLabel;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.blueprint.ICollectionInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputComponentValidationLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.content.IInputContentContainer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.controller.IKeyEvent;
import org.jowidgets.common.widgets.controller.IMouseButtonEvent;
import org.jowidgets.common.widgets.controller.IMouseEvent;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controller.KeyAdapter;
import org.jowidgets.tools.controller.MouseAdapter;
import org.jowidgets.tools.validation.MandatoryValidator;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

public class DemoForm1ContentCreator implements IInputContentCreator<List<String>> {

	private IComboBox<String> gender;
	private IInputComponent<String> lastname;
	private IInputComponent<String> firstName;
	private IInputField<Date> dateOfBirth;
	private IInputComponent<String> street;
	private IInputComponent<String> city;
	private IInputField<Integer> postalCode;
	private IComboBox<String> country;
	private IInputControl<Collection<String>> languages;
	private IInputComponent<String> phoneNumber;
	private IInputComponent<String> mail;

	private IInputComponentValidationLabel genderValidationWidget;
	private IInputComponentValidationLabel firstnameValidationWidget;
	private IInputComponentValidationLabel lastnameValidationWidget;
	private IInputComponentValidationLabel dateOfBirthValidationWidget;
	private IInputComponentValidationLabel streetValidationWidget;
	private IInputComponentValidationLabel postalCodeValidationWidget;
	private IInputComponentValidationLabel cityValidationWidget;
	private IInputComponentValidationLabel countryValidationWidget;
	private IInputComponentValidationLabel languagesValidationWidget;
	private IInputComponentValidationLabel phoneValidationWidget;
	private IInputComponentValidationLabel mailValidationWidget;

	@Override
	public void createContent(final IInputContentContainer container) {
		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();
		container.setLayout(new MigLayoutDescriptor("[][grow][20::]", "[]"));

		final String inputWidgetConstraints = "w 270:270:, grow, sg fg";

		final IValidator<String> moreThanOneWordValidator = new IValidator<String>() {

			@Override
			public IValidationResult validate(final String validationInput) {
				if (validationInput != null && validationInput.trim().length() > 0 && validationInput.trim().contains(" ")) {
					return ValidationResult.warning("Input contains more than one word");
				}
				return ValidationResult.ok();
			};

		};

		final IValidator<String> maxLengthValidator = new IValidator<String>() {

			@Override
			public IValidationResult validate(final String validationInput) {
				if (validationInput != null && validationInput.length() > 50) {
					return ValidationResult.error("Input must not have more than 50 characters");
				}
				return ValidationResult.ok();
			};

		};

		final IValidator<String> mandatoryValidator = new MandatoryValidator<String>("Must not be null");

		final ITextLabelBluePrint textLabelBp = bpF.textLabel().alignRight();

		final IInputComponentValidationLabelBluePrint validationLabelBp = bpF.inputComponentValidationLabel();
		validationLabelBp.setShowValidationMessage(false);

		final IInputFieldBluePrint<String> stringFieldBp = bpF.inputFieldString().setMaxLength(51);
		stringFieldBp.setValidator(maxLengthValidator);

		container.add(textLabelBp.setText("Gender"), "right, sg lg");
		gender = container.add("Gender", bpF.comboBoxSelection("Male", "Female", ""), inputWidgetConstraints);
		genderValidationWidget = container.add(validationLabelBp.setInputComponent(gender), "wrap");

		gender.addInputListener(new IInputListener() {
			@Override
			public void inputChanged() {
				//CHECKSTYLE:OFF
				System.out.println(gender.getValue());
				//CHECKSTYLE:ON
			}
		});

		container.add(textLabelBp.setText("Firstname*"), "right, sg lg");
		firstName = container.add("Firstname", stringFieldBp, inputWidgetConstraints);
		firstName.addValidator(mandatoryValidator);
		firstnameValidationWidget = container.add(validationLabelBp.setInputComponent(firstName), "wrap");
		firstName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final IKeyEvent event) {
				//CHECKSTYLE:OFF
				System.out.println(event);
				//CHECKSTYLE:ON
			}
		});

		container.add(textLabelBp.setText("Lastname*"), "right, sg lg");
		lastname = container.add("Lastname", stringFieldBp, inputWidgetConstraints);
		lastname.addValidator(mandatoryValidator);
		lastnameValidationWidget = container.add(validationLabelBp.setInputComponent(lastname), "wrap");
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

		container.add(textLabelBp.setText("Day of dirth*"), "right, sg lg");
		dateOfBirth = container.add("Day of dirth", bpF.inputFieldDate(), inputWidgetConstraints);
		dateOfBirth.addValidator(new MandatoryValidator<Date>("Must not be null"));
		dateOfBirthValidationWidget = container.add(validationLabelBp.setInputComponent(dateOfBirth), "wrap");

		container.add(textLabelBp.setText("Street*"), "right, sg lg");
		street = container.add("Street", stringFieldBp, inputWidgetConstraints);
		street.addValidator(mandatoryValidator);
		streetValidationWidget = container.add(validationLabelBp.setInputComponent(street), "wrap");

		container.add(textLabelBp.setText("Postal code*"), "right, sg lg");
		postalCode = container.add("Postal code", bpF.inputFieldIntegerNumber().setMaxLength(5), inputWidgetConstraints);
		postalCode.addValidator(new MandatoryValidator<Integer>("Must not be null"));
		postalCodeValidationWidget = container.add(validationLabelBp.setInputComponent(postalCode), "wrap");

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
		city = container.add("City", stringFieldBp, inputWidgetConstraints);
		city.addValidator(mandatoryValidator);
		cityValidationWidget = container.add(validationLabelBp.setInputComponent(city), "wrap");

		container.add(textLabelBp.setText("Country*"), "right, sg lg");
		final IComboBoxBluePrint<String> countryBp = bpF.comboBox("Germany", "Spain", "Italy", "United States");
		countryBp.setValidator(maxLengthValidator);
		countryBp.setMaxLength(Integer.valueOf(51));
		country = container.add("Country", countryBp, inputWidgetConstraints);
		country.addValidator(mandatoryValidator);
		countryValidationWidget = container.add(validationLabelBp.setInputComponent(country), "wrap");

		container.add(textLabelBp.setText("Languages"), "right, sg lg");
		final ICollectionInputFieldBluePrint<String> collectionInputFieldBp = bpF.collectionInputField(Toolkit.getConverterProvider().string());
		collectionInputFieldBp.setCollectionInputDialogSetup(bpF.collectionInputDialog(bpF.comboBox("English", "German", "French")));
		languages = container.add("Languages", collectionInputFieldBp, inputWidgetConstraints);
		languagesValidationWidget = container.add(validationLabelBp.setInputComponent(languages), "wrap");

		container.add(textLabelBp.setText("Phone number"), "right, sg lg");
		phoneNumber = container.add("Phone number", stringFieldBp, inputWidgetConstraints);
		phoneValidationWidget = container.add(validationLabelBp.setInputComponent(phoneNumber), "wrap");

		container.add(textLabelBp.setText("Email"), "right, sg lg");
		mail = container.add("Email", stringFieldBp, inputWidgetConstraints);
		mail.addValidator(moreThanOneWordValidator);
		mailValidationWidget = container.add(validationLabelBp.setInputComponent(mail), "wrap");

		resetValidation();
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
			gender.setValue("");
			lastname.setValue(null);
			firstName.setValue(null);
			dateOfBirth.setValue(null);
			street.setValue(null);
			postalCode.setValue(null);
			city.setValue(null);
			country.setValue(null);
			phoneNumber.setValue(null);
			mail.setValue(null);
			languages.setValue(new LinkedList<String>());
		}
		resetValidation();
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

	private void resetValidation() {
		genderValidationWidget.resetValidation();
		firstnameValidationWidget.resetValidation();
		lastnameValidationWidget.resetValidation();
		dateOfBirthValidationWidget.resetValidation();
		streetValidationWidget.resetValidation();
		postalCodeValidationWidget.resetValidation();
		cityValidationWidget.resetValidation();
		countryValidationWidget.resetValidation();
		phoneValidationWidget.resetValidation();
		mailValidationWidget.resetValidation();
		languagesValidationWidget.resetValidation();
	}

	private String getStringValue(final Object object) {
		if (object == null) {
			return "";
		}
		else {
			return object.toString();
		}
	}

}
