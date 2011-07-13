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

		frame.setLayout(new MigLayoutDescriptor("0[grow, 400::]0", "0[]0[grow]0[12!]0"));

		if (setup.getLogo() != null) {
			frame.add(bpf.icon(setup.getLogo()), "growx, growy, wrap");
		}
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

		final IComposite content = frame.add(bpf.composite(), "alignx r, wrap");

		content.setLayout(new MigLayoutDescriptor("20[grow]8[grow, 200!]20", "20[20!]15[][]45[grow]10"));

		validationResultLabel = content.add(bpf.validationResultLabel(), "span2, growx, wrap");
		content.add(bpf.textLabel("Username").alignRight(), "alignx r");//TODO i18n
		usernameField = content.add(bpf.inputFieldString(), "growx, wrap");
		content.add(bpf.textLabel("Password").alignRight(), "alignx r");//TODO i18n
		passwordField = content.add(bpf.inputFieldString().setPasswordPresentation(true), "growx, wrap");

		content.add(bpf.textLabel(""), "aligny bottom");
		final IComposite buttonBar = content.add(bpf.composite(), "alignx r, growy");
		buttonBar.setLayout(new MigLayoutDescriptor("0[][]0", "0[]0"));
		final String buttonCellConstraints = "w 80::, aligny b, sg bg";

		final IButton loginButton = buttonBar.add(setup.getLoginButton(), buttonCellConstraints);
		final IButton cancelButton = buttonBar.add(setup.getCancelButton(), buttonCellConstraints);

		loginButton.setBackgroundColor(frame.getBackgroundColor());
		cancelButton.setBackgroundColor(frame.getBackgroundColor());

		final IProgressBar progressBar = frame.add(bpf.progressBar().setIndeterminate(true), "growx, growy, aligny b");
		progressBar.setVisible(false);

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
