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
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jo.widgets.api.widgets.blueprint.convenience.impl;

import org.jo.widgets.api.look.Border;
import org.jo.widgets.api.widgets.blueprint.IValidationLabelBluePrint;
import org.jo.widgets.api.widgets.blueprint.base.IBaseInputCompositeBluePrint;
import org.jo.widgets.api.widgets.blueprint.convenience.IInputCompositeBluePrintConvenience;
import org.jo.widgets.api.widgets.blueprint.factory.impl.BluePrintFactory;
import org.jo.widgets.api.widgets.descriptor.IValidationLabelDescriptor;

public class InputCompositeBluePrintConvenience extends AbstractBluePrintConvenience<IBaseInputCompositeBluePrint<?, ?, ?>> implements
		IInputCompositeBluePrintConvenience<IBaseInputCompositeBluePrint<?, ?, ?>> {

	@Override
	public IBaseInputCompositeBluePrint<?, ?, ?> setBorder(final String borderTitle) {
		return getBluePrint().setBorder(new Border(borderTitle));
	}

	@Override
	public IBaseInputCompositeBluePrint<?, ?, ?> setBorder() {
		return getBluePrint().setBorder(new Border());
	}

	@Override
	public IBaseInputCompositeBluePrint<?, ?, ?> setContentBorder(final String borderTitle) {
		return getBluePrint().setContentBorder(new Border(borderTitle));
	}

	@Override
	public IBaseInputCompositeBluePrint<?, ?, ?> setContentBorder() {
		return getBluePrint().setContentBorder(new Border());
	}

	@Override
	public IBaseInputCompositeBluePrint<?, ?, ?> setMissingInputText(final String text) {
		getValidationLabelBluePrint().setMissingInputText(text);
		return getBluePrint();
	}

	private IValidationLabelBluePrint getValidationLabelBluePrint() {
		final IValidationLabelDescriptor validationLabelDescriptor = getBluePrint().getValidationLabel();
		if (validationLabelDescriptor == null) {
			return new BluePrintFactory().validationLabel();
		}
		else if (validationLabelDescriptor instanceof IValidationLabelBluePrint) {
			return (IValidationLabelBluePrint) validationLabelDescriptor;
		}
		else {
			return new BluePrintFactory().validationLabel().setDescriptor(validationLabelDescriptor);
		}
	}

}
