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
package org.jowidgets.impl.widgets.composed;

import org.jowidgets.api.widgets.IValidateableStateLabel;
import org.jowidgets.api.widgets.IValidationResultLabel;
import org.jowidgets.api.widgets.descriptor.setup.IValidateableStateLabelSetup;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.Assert;
import org.jowidgets.validation.IValidateable;
import org.jowidgets.validation.IValidationConditionListener;

public class ValidateableStateLabelImpl extends ControlWrapper implements IValidateableStateLabel {

	private final IValidationResultLabel resultLabel;

	public ValidateableStateLabelImpl(final IValidationResultLabel resultLabel, final IValidateableStateLabelSetup setup) {
		super(resultLabel);
		Assert.paramNotNull(setup.getValidateable(), "setup.getValidateable()");

		this.resultLabel = resultLabel;

		ColorSettingsInvoker.setColors(setup, this);
		VisibiliySettingsInvoker.setVisibility(setup, this);

		final IValidateable validateable = setup.getValidateable();

		validateable.addValidationConditionListener(new IValidationConditionListener() {
			@Override
			public void validationConditionsChanged() {
				resultLabel.setResult(validateable.validate());
			}
		});
	}

	@Override
	public void resetValidation() {
		resultLabel.setEmpty();
	}

}
