/*
 * Copyright (c) 2013, grossmann
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

package org.jowidgets.examples.common.workbench.demo3.form;

import java.util.Date;

import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.blueprint.IInputComponentValidationLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jowidgets.api.widgets.content.IInputContentContainer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.examples.common.workbench.demo3.model.Gender;
import org.jowidgets.examples.common.workbench.demo3.model.Person;
import org.jowidgets.tools.validation.MandatoryValidator;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class PersonContentCreator implements IInputContentCreator<Person> {

	private final boolean readonly;

	private IInputField<String> nameField;
	private IInputField<Date> dayOfBirthField;
	private IComboBox<Gender> genderCmb;

	public PersonContentCreator(final boolean readonly) {
		this.readonly = readonly;
	}

	@Override
	public void setValue(final Person person) {
		if (person != null) {
			nameField.setValue(person.getName());
			dayOfBirthField.setValue(person.getDayOfBirth());
			genderCmb.setValue(person.getGender());
		}
		else {
			nameField.setValue(null);
			dayOfBirthField.setValue(null);
			genderCmb.setValue(null);
		}
	}

	@Override
	public Person getValue() {
		final Person result = new Person();
		result.setName(nameField.getValue());
		result.setDayOfBirth(dayOfBirthField.getValue());
		result.setGender(genderCmb.getValue());
		return result;
	}

	@Override
	public void createContent(final IInputContentContainer content) {
		if (readonly) {
			content.setLayout(new MigLayoutDescriptor("wrap", "0[][grow, 0::]0", "[][][]"));
		}
		else {
			content.setLayout(new MigLayoutDescriptor("wrap", "0[][grow, 0::][]0", "[][][]"));
		}

		final ITextLabelBluePrint labelBp = BPF.textLabel().alignRight();

		final IInputComponentValidationLabelBluePrint validationLabelBp = BPF.inputComponentValidationLabel();
		validationLabelBp.setShowValidationMessage(false);

		//name property
		content.add(labelBp.setText("Name"), "alignx r");
		nameField = content.add("Name", BPF.inputFieldString(), "growx, w 0::");
		if (!readonly) {
			nameField.addValidator(new MandatoryValidator<String>("Name must not empty!"));
			content.add(validationLabelBp.setInputComponent(nameField));
		}

		//day of birth property
		content.add(labelBp.setText("Day of birth"), "alignx r");
		dayOfBirthField = content.add("Day of birth", BPF.inputFieldDate(), "growx, w 0::");
		if (!readonly) {
			content.add(validationLabelBp.setInputComponent(dayOfBirthField));
		}

		//gender property
		content.add(labelBp.setText("Gender"), "alignx r");
		genderCmb = content.add("Gender", BPF.comboBoxSelection(Gender.values()), "growx, w 0::");
		if (!readonly) {
			content.add(validationLabelBp.setInputComponent(genderCmb));
		}

	}
}
