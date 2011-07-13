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

package org.jowidgets.impl.widgets.composed;

import java.util.HashSet;
import java.util.Set;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.login.ILoginCancelListener;
import org.jowidgets.api.login.ILoginInterceptor;
import org.jowidgets.api.login.ILoginResult;
import org.jowidgets.api.login.ILoginResultCallback;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.ILoginDialog;
import org.jowidgets.api.widgets.IProgressBar;
import org.jowidgets.api.widgets.IValidationResultLabel;
import org.jowidgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.descriptor.setup.ILoginDialogSetup;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.widgets.wrapper.WindowWrapper;
import org.jowidgets.util.Assert;
import org.jowidgets.validation.ValidationResult;

public class LoginDialogImpl extends WindowWrapper implements ILoginDialog {

	private final IUiThreadAccess uiThreadAccess;
	private final Set<ILoginCancelListener> cancelListeners;

	private final IFrame frame;
	private final ILoginInterceptor loginInterceptor;

	private final IValidationResultLabel validationResultLabel;
	private final IInputField<String> usernameField;
	private final IInputField<String> passwordField;
	private final IButton loginButton;
	private final IButton cancelButton;
	private final IProgressBar progressBar;

	private ILoginResult result;
	private boolean disposed;
	private boolean loginButtonPressed;

	public LoginDialogImpl(final IFrame frame, final ILoginDialogSetup setup) {
		super(frame);
		Assert.paramNotNull(frame, "frame");
		Assert.paramNotNull(setup, "setup");
		Assert.paramNotNull(setup.getInterceptor(), "setup.getInterceptor()");

		this.uiThreadAccess = Toolkit.getUiThreadAccess();
		this.cancelListeners = new HashSet<ILoginCancelListener>();
		this.frame = frame;
		this.loginInterceptor = setup.getInterceptor();

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		frame.setLayout(new MigLayoutDescriptor("0[grow, 400::]0", "0[]0[grow]0[12!]0"));

		//set logo, or if not exists
		if (setup.getLogo() != null) {
			frame.add(bpf.icon(setup.getLogo()), "growx, growy, wrap");
		}
		//set login label
		else {
			if (setup.getLoginLabel() != null) {
				final IComposite labelComposite = frame.add(bpf.composite().setBackgroundColor(Colors.WHITE), "grow, wrap");
				labelComposite.setLayout(new MigLayoutDescriptor("15[grow]15", "15[grow]15"));
				final ITextLabelBluePrint labelBp = bpf.textLabel().setFontSize(25).setStrong().setFontName("Arial");
				labelBp.setText(setup.getLoginLabel()).setMarkup(Markup.DEFAULT);
				labelBp.setForegroundColor(Colors.DARK_GREY);
				labelComposite.add(labelBp, "grow");
			}
			else {
				frame.add(bpf.textLabel(""), "grow, wrap");
			}
		}

		//create content pane
		final IComposite content = frame.add(bpf.composite(), "alignx r, wrap");
		content.setLayout(new MigLayoutDescriptor("20[grow]8[grow, 200!]20", "20[20!]15[][]45[grow]10"));

		//validation label
		validationResultLabel = content.add(bpf.validationResultLabel(), "span2, growx, wrap");

		//input fields
		content.add(bpf.textLabel("Username").alignRight(), "alignx r");//TODO i18n
		usernameField = content.add(bpf.inputFieldString(), "growx, wrap");
		content.add(bpf.textLabel("Password").alignRight(), "alignx r");//TODO i18n
		passwordField = content.add(bpf.inputFieldString().setPasswordPresentation(true), "growx, wrap");

		//button bar
		final IComposite buttonBar = content.add(bpf.composite(), "span2, alignx r, growy");
		buttonBar.setLayout(new MigLayoutDescriptor("0[][]0", "0[]0"));
		this.loginButton = buttonBar.add(setup.getLoginButton(), "w 80::, aligny b, sg bg");
		this.cancelButton = buttonBar.add(setup.getCancelButton(), "w 80::, aligny b, sg bg");
		loginButton.setBackgroundColor(frame.getBackgroundColor());
		cancelButton.setBackgroundColor(frame.getBackgroundColor());
		frame.setDefaultButton(loginButton);

		//progress bar
		this.progressBar = frame.add(bpf.progressBar().setIndeterminate(true), "growx, growy, aligny b");
		progressBar.setVisible(false);

		//register listeners
		usernameField.addInputListener(new ValidationInputListener());
		passwordField.addInputListener(new ValidationInputListener());
		loginButton.addActionListener(new LoginActionListener());
		cancelButton.addActionListener(new CancelActionListener());
	}

	@Override
	public ILoginResult doLogin() {
		setVisible(true);
		return result;
	}

	@Override
	public void dispose() {
		if (!disposed) {
			this.disposed = true;
			super.dispose();
		}
	}

	private void loginButtonPressed() {
		loginButtonPressed = true;
		loginButton.setEnabled(false);
		usernameField.setEnabled(false);
		passwordField.setEnabled(false);
		progressBar.setIndeterminate(true);
		progressBar.setVisible(true);
		validationResultLabel.setEmpty();
	}

	private void loginGranted() {
		uiThreadAccess.invokeLater(new Runnable() {
			@Override
			public void run() {
				result = new LoginResult(true);
				dispose();
			}
		});
	}

	private void loginDenied(final String reason) {
		uiThreadAccess.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (!disposed) {
					validationResultLabel.setResult(ValidationResult.error(reason));
					loginButtonPressed = false;
					loginButton.setEnabled(true);
					usernameField.setEnabled(true);
					passwordField.setEnabled(true);
					progressBar.setVisible(false);
					frame.layoutBegin();
					frame.layoutEnd();
					frame.setDefaultButton(loginButton);
				}
			}
		});
	}

	private final class LoginActionListener implements IActionListener {

		@Override
		public void actionPerformed() {
			if (!loginButtonPressed) {
				loginButtonPressed();

				final String username = usernameField.getValue();
				final String password = passwordField.getValue();

				final Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						loginInterceptor.login(new LoginResultCallback(), username, password);
					}
				});
				thread.setDaemon(true);
				thread.start();
			}
		}
	}

	private final class LoginResultCallback implements ILoginResultCallback {

		@Override
		public void granted() {
			loginGranted();
		}

		@Override
		public void denied(final String reason) {
			loginDenied(reason);
		}

		@Override
		public void addCancelListener(final ILoginCancelListener cancelListener) {
			cancelListeners.add(cancelListener);
		}

	}

	private final class CancelActionListener implements IActionListener {
		@Override
		public void actionPerformed() {
			result = new LoginResult(false);
			for (final ILoginCancelListener listener : cancelListeners) {
				listener.canceled();
			}
			dispose();
		}
	}

	private final class ValidationInputListener implements IInputListener {
		@Override
		public void inputChanged() {
			validationResultLabel.setEmpty();
		}
	}

	private final class LoginResult implements ILoginResult {

		private final boolean isLoggedOn;

		private LoginResult(final boolean isLoggedOn) {
			this.isLoggedOn = isLoggedOn;
		}

		@Override
		public boolean isLoggedOn() {
			return isLoggedOn;
		}

	}

}
