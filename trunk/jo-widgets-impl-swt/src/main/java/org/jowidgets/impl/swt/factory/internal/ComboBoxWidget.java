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
package org.jowidgets.impl.swt.factory.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.api.convert.IStringObjectConverter;
import org.jowidgets.api.validation.ValidationMessageType;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.IComboBoxWidget;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.descriptor.base.IBaseComboBoxDescriptor;
import org.jowidgets.impl.swt.internal.color.IColorCache;

public class ComboBoxWidget<INPUT_TYPE> extends ComboBoxSelectionWidget<INPUT_TYPE> {

	private final IStringObjectConverter<INPUT_TYPE> stringObjectConverter;

	public ComboBoxWidget(
		final IWidget parent,
		final IColorCache colorCache,
		final IBaseComboBoxDescriptor<IComboBoxWidget<INPUT_TYPE>, INPUT_TYPE> descriptor) {
		super(
			parent,
			colorCache,
			new Combo((Composite) parent.getUiReference(), SWT.NONE | SWT.DROP_DOWN),
			descriptor,
			descriptor.getValidator());

		this.stringObjectConverter = descriptor.getStringObjectConverter();

		getUiReference().addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(final VerifyEvent verifyEvent) {
				final String newText = verifyEvent.text;

				if (ValidationMessageType.OK.equals(stringObjectConverter.isCompletableToValid(newText).getType())) {
					verifyEvent.doit = true;
				}
				else {
					verifyEvent.doit = false;
				}
			}
		});

	}

	@Override
	public INPUT_TYPE getValue() {
		final INPUT_TYPE result = stringObjectConverter.convertToObject(getUiReference().getText());
		return result;
	}

	@Override
	protected ValidationResult additionalValidation() {
		final ValidationResult superResult = super.additionalValidation();
		superResult.addValidationResult(stringObjectConverter.validate(getUiReference().getText()));
		return superResult;
	}

}
