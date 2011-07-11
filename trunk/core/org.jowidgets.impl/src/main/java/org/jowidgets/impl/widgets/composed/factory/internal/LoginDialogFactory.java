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
package org.jowidgets.impl.widgets.composed.factory.internal;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.ILoginDialog;
import org.jowidgets.api.widgets.blueprint.IInputDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.content.IInputContentContainer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.api.widgets.descriptor.ILoginDialogDescriptor;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.impl.widgets.composed.LoginData;
import org.jowidgets.impl.widgets.composed.LoginDialogImpl;

public class LoginDialogFactory extends AbstractDialogFactory<ILoginDialog, ILoginDialogDescriptor> {

	public LoginDialogFactory(final IGenericWidgetFactory genericWidgetFactory) {
		super(genericWidgetFactory);
	}

	@Override
	protected ILoginDialog createWidget(final IFrame dialogWidget, final ILoginDialogDescriptor descriptor) {
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
		final IInputDialogBluePrint<LoginData> inputDialogBluePrint = bpf.inputDialog(new LoginDialogContentCreator());
		inputDialogBluePrint.setSetup(descriptor);
		return new LoginDialogImpl(dialogWidget, inputDialogBluePrint);
	}

	private class LoginDialogContentCreator implements IInputContentCreator<LoginData> {

		private IInputField<String> usernameField;
		private IInputField<String> passwordField;

		@Override
		public void setValue(final LoginData value) {
			if (value != null) {
				usernameField.setValue(value.getUsername());
				passwordField.setValue(value.getPassword());
			}
			else {
				usernameField.setValue(null);
				passwordField.setValue(null);
			}
		}

		@Override
		public LoginData getValue() {
			return new LoginData(usernameField.getValue(), passwordField.getValue());
		}

		@Override
		public void createContent(final IInputContentContainer contentContainer) {
			final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
			contentContainer.setLayout(new MigLayoutDescriptor("[][grow, 200!]", "[][]"));
			contentContainer.add(bpf.textLabel("Username"));//TODO i18n
			usernameField = contentContainer.add(bpf.inputFieldString(), "grow, wrap");
			contentContainer.add(bpf.textLabel("Password"));//TODO i18n
			passwordField = contentContainer.add(bpf.inputFieldString().setPasswordPresentation(true), "grow, wrap");
		}

	}
}
