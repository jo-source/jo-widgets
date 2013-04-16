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

import org.jowidgets.api.password.IPasswordChangeExecutor;
import org.jowidgets.api.password.IPasswordChangeResult;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.types.InputDialogDefaultButtonPolicy;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.IPasswordChangeDialog;
import org.jowidgets.api.widgets.IProgressBar;
import org.jowidgets.api.widgets.IValidationResultLabel;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.api.widgets.descriptor.setup.IPasswordChangeDialogSetup;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.validation.MandatoryValidator;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.tools.widgets.wrapper.WindowWrapper;
import org.jowidgets.util.Assert;
import org.jowidgets.util.EmptyCheck;
import org.jowidgets.util.EmptyCompatibleEquivalence;
import org.jowidgets.util.event.CancelObservable;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidationResultBuilder;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

public final class PasswordChangeDialogImpl extends WindowWrapper implements IPasswordChangeDialog {

	private static final IMessage OLD_PASSWORD = Messages.getMessage("PasswordChangeDialogImpl.oldPassword");
	private static final IMessage NEW_PASSWORD = Messages.getMessage("PasswordChangeDialogImpl.newPassword");
	private static final IMessage NEW_PASSWORD_REPEAT = Messages.getMessage("PasswordChangeDialogImpl.newPasswordRepeat");
	private static final IMessage FILL_MANDATORY_FIELDS = Messages.getMessage("PasswordChangeDialogImpl.fillMandatoryFields");
	private static final IMessage TRY_TO_CHANGE_PASSWORD = Messages.getMessage("PasswordChangeDialogImpl.tryToChangePassword");
	private static final IMessage PASSWORDS_EQUAL = Messages.getMessage("PasswordChangeDialogImpl.passwordsEqual");
	private static final IMessage REPEAT_PASSWORD = Messages.getMessage("PasswordChangeDialogImpl.repeatPassword");
	private static final IMessage PASSWORD_REPEAT_MISMATCH = Messages.getMessage("PasswordChangeDialogImpl.passwordRepeatMissmatch");
	private static final IMessage PASSWORD_CHANGED = Messages.getMessage("PasswordChangeDialogImpl.passwordChanged");

	private final String title;
	private final IImageConstant icon;

	private final IValidationResultLabel validationResultLabel;
	private final IInputField<String> oldPassword;
	private final IInputField<String> newPassword;
	private final IInputField<String> newPasswordRepeat;
	private final IButton cancelButton;
	private final IButton okButton;
	private final IProgressBar progressBar;

	private final IPasswordChangeExecutor executor;

	public PasswordChangeDialogImpl(final IFrame dialog, final IPasswordChangeDialogSetup setup) {
		super(dialog);
		Assert.paramNotNull(dialog, "dialog");
		Assert.paramNotNull(setup, "setup");
		Assert.paramNotNull(setup.getExecutor(), "setup.getExecutor()");

		this.title = setup.getTitle();
		this.icon = setup.getIcon();

		this.executor = setup.getExecutor();

		dialog.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "0[grow, 0::][]0"));

		final IComposite content = dialog.add(BPF.scrollComposite(), MigLayoutFactory.GROWING_CELL_CONSTRAINTS + ",wrap");

		this.progressBar = dialog.add(BPF.progressBar().setIndeterminate(true), "growx, w 0::, h 10!");
		progressBar.setVisible(false);

		content.setLayout(new MigLayoutDescriptor("[][grow, 160::]", "[20!]25[]10[][]15[grow]"));

		this.validationResultLabel = content.add(BPF.validationResultLabel(), "span 2, growx, w 0::, wrap");

		this.oldPassword = addInputField(content, OLD_PASSWORD.get(), setup.getPasswordMaxLength());
		this.newPassword = addInputField(content, NEW_PASSWORD.get(), setup.getPasswordMaxLength());
		this.newPasswordRepeat = addInputField(content, NEW_PASSWORD_REPEAT.get(), setup.getPasswordMaxLength());

		final IValidator<String> passwordValidator = setup.getPasswordValidator();
		if (passwordValidator != null) {
			newPassword.addValidator(passwordValidator);
		}

		final IComposite buttonBar = content.add(BPF.composite(), "alignx r, aligny b, span 2");
		buttonBar.setLayout(new MigLayoutDescriptor("0[][]0", "0[]0"));
		this.okButton = buttonBar.add(setup.getOkButton(), "sg bg");
		this.cancelButton = buttonBar.add(setup.getCancelButton(), "sg bg");

		cancelButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				setVisible(false);
			}
		});

		okButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				changePassword();
			}
		});

		if (InputDialogDefaultButtonPolicy.OK == setup.getDefaultButtonPolicy()) {
			dialog.setDefaultButton(okButton);
		}
		else if (InputDialogDefaultButtonPolicy.CANCEL == setup.getDefaultButtonPolicy()) {
			dialog.setDefaultButton(cancelButton);
		}

		validate();
	}

	private IInputField<String> addInputField(final IContainer content, final String label, final int maxLength) {
		content.add(BPF.textLabel(label).alignRight(), "alignx r");
		final IInputFieldBluePrint<String> inputFieldBp = BPF.inputFieldString().setPasswordPresentation(true);
		inputFieldBp.setMaxLength(maxLength);
		final IInputField<String> result = content.add(inputFieldBp, "growx, w 0::, wrap");
		result.addValidator(new MandatoryValidator<String>(ValidationResult.infoError(FILL_MANDATORY_FIELDS.get())));
		result.addValidationConditionListener(new IValidationConditionListener() {
			@Override
			public void validationConditionsChanged() {
				validate();
			}
		});
		return result;
	}

	private void validate() {
		final IValidationResultBuilder builder = ValidationResult.builder();
		builder.addResult(oldPassword.validate());
		if (newPassword.hasModifications()) {
			builder.addResult(newPassword.validate().withContext(NEW_PASSWORD.get()));
		}
		builder.addResult(newPasswordRepeat.validate());
		builder.addResult(validatePasswordEquality());
		final IValidationResult validationResult = builder.build();

		validationResultLabel.setResult(validationResult);

		if (validationResult.isValid()) {
			okButton.setEnabled(true);
			okButton.setToolTipText(null);
		}
		else {
			okButton.setEnabled(false);
			okButton.setToolTipText(validationResult.getWorstFirst().getText());
		}
	}

	private IValidationResult validatePasswordEquality() {
		final String oldPasswordValue = oldPassword.getValue();
		final String newPasswordValue = newPassword.getValue();
		final String newPasswordRepeatValue = newPasswordRepeat.getValue();

		if (!EmptyCheck.isEmpty(newPasswordValue) && newPasswordValue.equals(oldPasswordValue)) {
			return ValidationResult.error(PASSWORDS_EQUAL.get());
		}
		if (!EmptyCompatibleEquivalence.equals(newPasswordRepeatValue, newPasswordValue)) {
			if (newPasswordRepeatValue != null
				&& newPasswordValue != null
				&& newPasswordValue.length() > newPasswordRepeatValue.length()) {
				return ValidationResult.infoError(REPEAT_PASSWORD.get());
			}
			else {
				return ValidationResult.error(PASSWORD_REPEAT_MISMATCH.get());
			}
		}
		else {
			return ValidationResult.ok();
		}
	}

	private void changePassword() {
		validationResultLabel.setResult(ValidationResult.infoError(TRY_TO_CHANGE_PASSWORD.get()));
		setControlsEnabled(false);
		progressBar.setVisible(true);

		final CancelObservable cancelObservable = new CancelObservable();

		final IActionListener cancelListener = new IActionListener() {
			@Override
			public void actionPerformed() {
				cancelObservable.fireCanceledEvent();
			}
		};

		final IPasswordChangeResult resultCallback = new IPasswordChangeResult() {

			@Override
			public void success() {
				finishedCommon();
				setVisible(false);
				Toolkit.getMessagePane().showInfo(title, icon, PASSWORD_CHANGED.get());
			}

			@Override
			public void error(final String errorText) {
				finishedCommon();
				validationResultLabel.setResult(ValidationResult.error(errorText));
			}

			private void finishedCommon() {
				cancelButton.removeActionListener(cancelListener);
				progressBar.setVisible(false);
				setControlsEnabled(true);
			}
		};

		cancelButton.addActionListener(cancelListener);

		executor.changePassword(resultCallback, oldPassword.getValue(), newPassword.getValue(), cancelObservable);
	}

	private void setControlsEnabled(final boolean enabled) {
		oldPassword.setEnabled(enabled);
		newPassword.setEnabled(enabled);
		newPasswordRepeat.setEnabled(enabled);
		okButton.setEnabled(enabled);
	}

}
