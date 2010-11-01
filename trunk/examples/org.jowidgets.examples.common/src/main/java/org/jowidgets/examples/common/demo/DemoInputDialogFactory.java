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
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.IInputDialogWidget;
import org.jowidgets.api.widgets.IInputWidget;
import org.jowidgets.api.widgets.IValidationLabelWidget;
import org.jowidgets.api.widgets.IWindowWidget;
import org.jowidgets.api.widgets.blueprint.IInputDialogBluePrint;
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
		inputDialogBp.setMissingInputText("Please fill out all mandatory (*) fields");
		if (parent != null) {
			return parent.createChildWindow(inputDialogBp);
		}
		else {
			return Toolkit.getWidgetFactory().create(inputDialogBp);
		}
	}

	private class DemoInputDialogContent implements IInputContentCreator<String> {

		private IInputWidget<String> name;
		private IInputWidget<String> firstName;

		@Override
		public void createContent(final IInputContentContainer container) {
			final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();
			container.setLayout(new MigLayoutDescriptor("[][grow, 180::][20::]", "[]"));

			final ITextLabelBluePrint textLabelBp = bpF.textLabel().alignRight();
			final IValidationLabelBluePrint validationLabelBp = bpF.validationLabel().setShowValidationMessage(false);

			container.add(textLabelBp.setText("Name*"), "right");
			name = container.add(bpF.inputFieldString().setMandatory(true), "grow");
			final IValidationLabelWidget nameValidationWidget = container.add(validationLabelBp, "wrap");
			nameValidationWidget.registerInputWidget(name);

			container.add(textLabelBp.setText("Firstname*"), "right");
			firstName = container.add(bpF.inputFieldString().setMandatory(true), "grow");
			final IValidationLabelWidget firstnameValidationWidget = container.add(validationLabelBp, "wrap");
			firstnameValidationWidget.registerInputWidget(firstName);

			container.registerInputWidget("Name", name);
			container.registerInputWidget("Firstname", firstName);
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
