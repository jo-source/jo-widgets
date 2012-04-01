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
package org.jowidgets.impl.widgets.composed.blueprint.defaults;

import org.jowidgets.api.types.InputDialogDefaultButtonPolicy;
import org.jowidgets.api.widgets.blueprint.IInputComponentValidationLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.builder.IInputDialogSetupBuilder;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultInitializer;
import org.jowidgets.impl.widgets.composed.blueprint.BluePrintFactory;
import org.jowidgets.util.IDecorator;
import org.jowidgets.validation.IValidationResult;

public class InputDialogDefaults implements IDefaultInitializer<IInputDialogSetupBuilder<?, ?>> {

	@Override
	public void initialize(final IInputDialogSetupBuilder<?, ?> builder) {
		builder.setContentScrolled(true);
		final BluePrintFactory bpF = new BluePrintFactory();
		builder.setOkButton(bpF.button(Messages.getString("InputDialogDefaults.ok"))); //$NON-NLS-1$
		builder.setCancelButton(bpF.button(Messages.getString("InputDialogDefaults.cancel"))); //$NON-NLS-1$
		builder.setValidationLabel(bpF.inputComponentValidationLabel());
		builder.setDefaultButtonPolicy(InputDialogDefaultButtonPolicy.OK);
		builder.setCloseable(false);

		final IInputComponentValidationLabelBluePrint validationLabelBp = bpF.inputComponentValidationLabel();
		validationLabelBp.setInitialValidationDecorator(new IDecorator<IValidationResult>() {
			@Override
			public IValidationResult decorate(final IValidationResult original) {
				if (!original.isValid()) {
					return original;
				}
				return null;
			}
		});

		builder.setValidationLabel(validationLabelBp);
	}

}
