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
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.descriptor.setup.ILoginDialogSetup;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.widgets.wrapper.WindowWrapper;
import org.jowidgets.validation.ValidationResult;

public class LoginDialogImpl extends WindowWrapper implements ILoginDialog {

	private final Set<ILoginCancelListener> cancelListeners;

	private final IValidationResultLabel validationResultLabel;
	private final IInputField<String> usernameField;
	private final IInputField<String> passwordField;

	private ILoginResult result;

	public LoginDialogImpl(final IFrame frame, final ILoginDialogSetup setup) {
		super(frame);

		this.cancelListeners = new HashSet<ILoginCancelListener>();

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
		frame.setLayout(new MigLayoutDescriptor("20[]8[grow, 200!]20", "15[20!]15[][]15[][]"));

		validationResultLabel = frame.add(bpf.validationResultLabel(), "span2, grow, wrap");
		frame.add(bpf.textLabel("Username").alignRight(), "alignx r");//TODO i18n
		usernameField = frame.add(bpf.inputFieldString(), "grow, wrap");
		frame.add(bpf.textLabel("Password").alignRight(), "alignx r");//TODO i18n
		passwordField = frame.add(bpf.inputFieldString().setPasswordPresentation(true), "grow, wrap");

		final IProgressBar progressBar = frame.add(bpf.progressBar().setIndeterminate(true), "span 2, growx, wrap");
		progressBar.setVisible(false);

		final IComposite buttonBar = frame.add(bpf.composite(), "span2, align right, wrap");
		buttonBar.setLayout(new MigLayoutDescriptor("0[][]0", "[]"));
		final String buttonCellConstraints = "w 80::, sg bg";

		final IButton loginButton = buttonBar.add(setup.getLoginButton(), buttonCellConstraints);
		final IButton cancelButton = buttonBar.add(setup.getCancelButton(), buttonCellConstraints);

		final ILoginInterceptor loginInterceptor = setup.getInterceptor();

		final IUiThreadAccess uiThreadAccess = Toolkit.getUiThreadAccess();

		frame.setDefaultButton(loginButton);

		loginButton.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				loginButton.setEnabled(false);
				progressBar.setVisible(true);
				validationResultLabel.setEmpty();

				final ILoginResultCallback resultCallback = new ILoginResultCallback() {

					@Override
					public void granted() {
						uiThreadAccess.invokeLater(new Runnable() {
							@Override
							public void run() {
								result = new LoginResult(true);
								dispose();
							}
						});
					}

					@Override
					public void denied(final String reason) {
						uiThreadAccess.invokeLater(new Runnable() {
							@Override
							public void run() {
								validationResultLabel.setResult(ValidationResult.error(reason));
								progressBar.setVisible(false);
								loginButton.setEnabled(true);
								frame.layoutBegin();
								frame.layoutEnd();
							}
						});
					}

					@Override
					public void addCancelListener(final ILoginCancelListener cancelListener) {
						cancelListeners.add(cancelListener);
					}

				};

				final String username = usernameField.getValue();
				final String password = passwordField.getValue();
				final Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						loginInterceptor.login(resultCallback, username, password);
					}
				});
				thread.setDaemon(true);
				thread.start();

			}
		});

		cancelButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				result = new LoginResult(false);
				fireCanceled();
				dispose();
			}
		});
	}

	@Override
	public ILoginResult doLogin() {
		setVisible(true);
		return result;
	}

	private void fireCanceled() {
		for (final ILoginCancelListener listener : cancelListeners) {
			listener.canceled();
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
