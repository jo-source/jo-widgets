/*
 * Copyright (c) 2013, grossmann
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
package org.jowidgets.helloworld.common;

import java.util.Calendar;
import java.util.Date;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputComponentValidationLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.converter.AbstractConverter;
import org.jowidgets.tools.validation.MandatoryValidator;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidationResultBuilder;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

public final class HelloWorldApplicationValidation1 implements IApplication {

	@Override
	public void start(final IApplicationLifecycle lifecycle) {

		final IFrameBluePrint frameBp = BPF.frame();
		frameBp.setSize(new Dimension(800, 600)).setTitle("Hello World");
		final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);
		frame.setLayout(new MigLayoutDescriptor("wrap", "[][grow, 0::][300!]", "[][][]"));

		final ITextLabelBluePrint textLabelBp = BPF.textLabel().alignRight();
		final IInputComponentValidationLabelBluePrint validationLabelBp = BPF.inputComponentValidationLabel();
		validationLabelBp.setShowValidationMessage(true);

		//Integer Field
		frame.add(textLabelBp.setText("Integer Field"));
		final IInputField<Integer> inputField = frame.add(BPF.inputFieldIntegerNumber(), "growx, w 0::");
		frame.add(validationLabelBp.setInputComponent(inputField), "growx, w 0::");
		inputField.addValidator(new IntegerTestValidator());

		//Date Field
		frame.add(textLabelBp.setText("Date Field"));
		final IInputField<Date> dateField = frame.add(BPF.inputFieldDate(), "growx, w 0::");
		frame.add(validationLabelBp.setInputComponent(dateField), "growx, w 0::");
		dateField.addValidator(new DateTestValidator());

		//Custom converter field
		frame.add(textLabelBp.setText("Monat"));
		final IInputField<Integer> monthField = frame.add(BPF.inputField(new MonthConverter()), "growx, w 0::");
		frame.add(validationLabelBp.setInputComponent(monthField), "growx, w 0::");
		monthField.addValidator(new MandatoryValidator<Integer>("Darf nicht leer sein!"));
		monthField.addInputListener(new IInputListener() {
			@Override
			public void inputChanged() {
				System.out.println("Monat nr: " + monthField.getValue());
			}
		});

		//set the root frame visible
		frame.setVisible(true);
	}

	private final class IntegerTestValidator implements IValidator<Integer> {
		@Override
		public IValidationResult validate(final Integer value) {
			final IValidationResultBuilder builder = ValidationResult.builder();
			if (value != null) {
				final int intValue = value.intValue();
				if (intValue > 1000) {
					builder.addError("Must be less or equal 1000!");
				}
				else if (intValue % 12 == 0) {
					builder.addWarning("Number is divisible by 12!");
				}
				else if (intValue == 303) {
					builder.addInfo("Cool number!");
				}
			}
			return builder.build();
		}
	}

	private final class DateTestValidator implements IValidator<Date> {
		@Override
		public IValidationResult validate(final Date value) {
			final IValidationResultBuilder builder = ValidationResult.builder();
			if (value != null) {
				final Calendar calendar = Calendar.getInstance();
				calendar.setTime(value);
				final int year = calendar.get(Calendar.YEAR);
				final int month = calendar.get(Calendar.MONTH);
				final int day = calendar.get(Calendar.DAY_OF_MONTH);
				if (year < 1972) {
					builder.addError("Sorry, too old!");
				}
				else if (year == 1972 && month == 2 && day == 1) {
					builder.addInfo("Happy birthday!");
				}
			}
			return builder.build();
		}
	}

	private final class MonthConverter extends AbstractConverter<Integer> {

		@Override
		public Integer convertToObject(final String string) {
			if ("Januar".equals(string)) {
				return Integer.valueOf(1);
			}
			else if ("Februar".equals(string)) {
				return Integer.valueOf(1);
			}
			else if ("März".equals(string)) {
				return Integer.valueOf(1);
			}
			return null;
		}

		@Override
		public IValidator<String> getStringValidator() {
			return new IValidator<String>() {
				@Override
				public IValidationResult validate(final String value) {
					if (value != null) {
						final Integer integer = convertToObject(value);
						if (integer != null) {
							return ValidationResult.ok();
						}
						else if ("Januar".startsWith(value) || "Februar".startsWith(value) || "März".startsWith(value)) {
							return ValidationResult.ok();
						}
						else {
							return ValidationResult.infoError("Geben Sie 'Januar', 'Februar' oder 'März' ein!");
						}
					}
					else {
						return ValidationResult.ok();
					}
				}
			};
		}

		@Override
		public String getAcceptingRegExp() {
			return "(([J|a|n|u|r|M|ä|z|F|e|b]{0,7}))";
		}

		@Override
		public String convertToString(final Integer value) {
			if (Integer.valueOf(1) == value) {
				return "Januar";
			}
			else if (Integer.valueOf(2) == value) {
				return "Februar";
			}
			else if (Integer.valueOf(3) == value) {
				return "März";
			}
			return null;
		}

	}
}
