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
package org.jowidgets.examples.common.tutorial;

import java.util.Calendar;
import java.util.Date;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IInputComposite;
import org.jowidgets.api.widgets.IInputDialog;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputComponentValidationLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jowidgets.api.widgets.content.IInputContentContainer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.validation.MandatoryValidator;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidationResultBuilder;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

public final class ValidationExample2 implements IApplication {

	@Override
	public void start(final IApplicationLifecycle lifecycle) {

		final IFrameBluePrint frameBp = BPF.frame();
		frameBp.setSize(new Dimension(800, 600)).setTitle("Hello World");
		final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);
		frame.setLayout(new MigLayoutDescriptor("wrap", "[grow, 0::]", "[][]"));

		final IInputComposite<Person> inputComposite = frame.add(BPF.inputComposite(new PersonContentCreator()), "growx, w 0::");
		inputComposite.addInputListener(new IInputListener() {
			@Override
			public void inputChanged() {
				//CHECKSTYLE:OFF
				System.out.println(inputComposite.getValue());
				//CHECKSTYLE:ON
			}
		});

		final IButton button = frame.add(BPF.button("Open Dialog"), "growx, w 0::");
		button.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IInputDialogBluePrint<Person> dialogBp = BPF.inputDialog(new PersonContentCreator());
				dialogBp.setMinPackSize(new Dimension(800, 600));
				dialogBp.setTitle("Edit Person ...");
				dialogBp.setValue(inputComposite.getValue());
				final IInputDialog<Person> dialog = frame.createChildWindow(dialogBp);
				dialog.setVisible(true);
				if (dialog.isOkPressed()) {
					final Person person = dialog.getValue();
					inputComposite.setValue(person);
				}
			}
		});

		//set the root frame visible
		frame.setVisible(true);
	}

	private enum State {
		ADMIN,
		USER,
		DEVELOPER;
	}

	private final class Person {
		private String name;
		private Date dayOfBirth;
		private State state;

		public String getName() {
			return name;
		}

		public void setName(final String name) {
			this.name = name;
		}

		public Date getDayOfBirth() {
			return dayOfBirth;
		}

		public void setDayOfBirth(final Date dayOfBirth) {
			this.dayOfBirth = dayOfBirth;
		}

		public State getState() {
			return state;
		}

		public void setState(final State state) {
			this.state = state;
		}

		@Override
		public String toString() {
			return "Person [name=" + name + ", dayOfBirth=" + dayOfBirth + ", state=" + state + "]";
		}

	}

	private final class PersonContentCreator implements IInputContentCreator<Person> {

		private IInputField<String> nameField;
		private IInputField<Date> dateField;
		private IComboBox<State> stateCmb;

		@Override
		public void setValue(final Person person) {
			nameField.setValue(person != null ? person.getName() : null);
			dateField.setValue(person != null ? person.getDayOfBirth() : null);
			stateCmb.setValue(person != null ? person.getState() : null);
		}

		@Override
		public Person getValue() {
			final Person result = new Person();
			result.setName(nameField.getValue());
			result.setState(stateCmb.getValue());
			result.setDayOfBirth(dateField.getValue());
			return result;
		}

		@Override
		public void createContent(final IInputContentContainer content) {
			content.setLayout(new MigLayoutDescriptor("wrap", "[][grow, 0::][20!]", "[][][]"));

			final ITextLabelBluePrint textLabelBp = BPF.textLabel().alignRight();
			final IInputComponentValidationLabelBluePrint validationLabelBp = BPF.inputComponentValidationLabel();
			validationLabelBp.setShowValidationMessage(false);

			//Name field
			content.add(textLabelBp.setText("Name"));
			nameField = content.add(BPF.inputFieldString(), "growx, w 0::");
			content.add(validationLabelBp.setInputComponent(nameField), "growx, w 0::");
			nameField.addValidator(new MandatoryValidator<String>(createMandatoryResult("Name")));

			//Day of birth field
			content.add(textLabelBp.setText("Day of birth"));
			dateField = content.add(BPF.inputFieldDate(), "growx, w 0::");
			content.add(validationLabelBp.setInputComponent(dateField), "growx, w 0::");
			dateField.addValidator(new DateTestValidator());

			//Day of birth field
			content.add(textLabelBp.setText("State"));
			stateCmb = content.add(BPF.comboBoxSelection(State.values()), "growx, w 0::");
			content.add(validationLabelBp.setInputComponent(stateCmb), "growx, w 0::");
		}

		private IValidationResult createMandatoryResult(final String propName) {
			return ValidationResult.infoError("Bitte " + propName + " ausfüllen!");
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

}
