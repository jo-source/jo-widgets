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

package org.jowidgets.impl.content;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.content.IInputContentContainer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.util.Assert;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.ValidationResult;

class SingleControlContent<INPUT_TYPE> implements IInputContentCreator<INPUT_TYPE> {

	private final String label;
	private final IWidgetDescriptor<? extends IInputControl<INPUT_TYPE>> descriptor;
	private final String layoutConstraints;
	private final boolean mandatory;

	private IInputControl<INPUT_TYPE> control;
	private INPUT_TYPE value;

	SingleControlContent(
		final String label,
		final IWidgetDescriptor<? extends IInputControl<INPUT_TYPE>> descriptor,
		final String layoutConstraints,
		final boolean mandatory) {

		Assert.paramNotNull(label, "label");
		Assert.paramNotNull(descriptor, "descriptor");
		this.label = label;
		this.descriptor = descriptor;
		if (layoutConstraints != null) {
			this.layoutConstraints = layoutConstraints;
		}
		else {
			this.layoutConstraints = "w 150::, grow";
		}
		this.mandatory = mandatory;
	}

	@Override
	public void createContent(final IInputContentContainer contentContainer) {
		if (control != null) {
			throw new IllegalStateException("Content must only be created once");
		}
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
		contentContainer.setLayout(new MigLayoutDescriptor("[][grow, 0::]", "[]"));
		contentContainer.add(bpf.textLabel(label), "");
		control = contentContainer.add(descriptor, layoutConstraints);
		if (value != null) {
			control.setValue(value);
		}
		contentContainer.registerInputWidget(null, control);
	}

	@Override
	public void setValue(final INPUT_TYPE value) {
		if (control != null) {
			control.setValue(value);
		}
		else {
			this.value = value;
		}
	}

	@Override
	public INPUT_TYPE getValue() {
		if (control != null) {
			return control.getValue();
		}
		else {
			return this.value;
		}
	}

	@Override
	public IValidationResult validate() {
		return ValidationResult.ok();
	}

	@Override
	public boolean isMandatory() {
		return mandatory;
	}

}
