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

import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.blueprint.IInputComponentValidationLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jowidgets.api.widgets.content.IInputContentContainer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.examples.common.workbench.demo3.model.Role;
import org.jowidgets.tools.validation.MandatoryValidator;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class RoleContentCreator implements IInputContentCreator<Role> {

	private final boolean readonly;

	private IInputField<String> idField;
	private IInputField<String> descriptionField;

	public RoleContentCreator(final boolean readonly) {
		this.readonly = readonly;
	}

	@Override
	public void setValue(final Role role) {
		if (role != null) {
			idField.setValue(role.getId());
			descriptionField.setValue(role.getDescription());
		}
		else {
			idField.setValue(null);
			descriptionField.setValue(null);
		}
	}

	@Override
	public Role getValue() {
		final Role result = new Role();
		result.setId(idField.getValue());
		result.setDescription(descriptionField.getValue());
		return result;
	}

	@Override
	public void createContent(final IInputContentContainer content) {
		if (readonly) {
			content.setLayout(new MigLayoutDescriptor("wrap", "0[][grow, 0::]0", "[][]"));
		}
		else {
			content.setLayout(new MigLayoutDescriptor("wrap", "0[][grow, 0::][]0", "[][]"));
		}

		final ITextLabelBluePrint labelBp = BPF.textLabel().alignRight();

		final IInputComponentValidationLabelBluePrint validationLabelBp = BPF.inputComponentValidationLabel();
		validationLabelBp.setShowValidationMessage(false);

		//id property
		content.add(labelBp.setText("Id"), "alignx r");
		idField = content.add("Id", BPF.inputFieldString(), "growx, w 0::");
		if (!readonly) {
			idField.addValidator(new MandatoryValidator<String>("Id must not empty!"));
			content.add(validationLabelBp.setInputComponent(idField));
		}

		//description property
		content.add(labelBp.setText("Description"), "alignx r");
		descriptionField = content.add("Description", BPF.inputFieldString(), "growx, w 0::");
		if (!readonly) {
			content.add(validationLabelBp.setInputComponent(descriptionField));
		}

	}
}
