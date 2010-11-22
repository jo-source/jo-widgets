/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.impl.widgets.basic;

import org.jowidgets.api.validation.IValidateable;
import org.jowidgets.api.validation.IValidator;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.descriptor.setup.IInputWidgetSetup;
import org.jowidgets.common.widgets.IInputWidgetCommon;
import org.jowidgets.impl.base.delegate.ControlDelegate;
import org.jowidgets.impl.base.delegate.InputWidgetDelegate;
import org.jowidgets.impl.widgets.common.wrapper.InputWidgetCommonWrapper;

public abstract class AbstractBasicInputControl<VALUE_TYPE> extends InputWidgetCommonWrapper implements IInputControl<VALUE_TYPE> {

	private final ControlDelegate controlDelegate;
	private final InputWidgetDelegate<VALUE_TYPE> inputWidgetDelegate;

	public AbstractBasicInputControl(final IInputWidgetCommon inputWidgetCommon, final IInputWidgetSetup<VALUE_TYPE> setup) {
		super(inputWidgetCommon);

		this.controlDelegate = new ControlDelegate();

		//this must be last statement
		this.inputWidgetDelegate = new InputWidgetDelegate<VALUE_TYPE>(this, setup);
	}

	@Override
	public final IContainer getParent() {
		return controlDelegate.getParent();
	}

	@Override
	public void setParent(final IWidget parent) {
		controlDelegate.setParent(parent);
	}

	@Override
	public boolean isReparentable() {
		return controlDelegate.isReparentable();
	}

	@Override
	public final ValidationResult validate() {
		return inputWidgetDelegate.validate();
	}

	@Override
	public final boolean isMandatory() {
		return inputWidgetDelegate.isMandatory();
	}

	@Override
	public final void setMandatory(final boolean mandatory) {
		inputWidgetDelegate.setMandatory(mandatory);
	}

	@Override
	public final boolean isEmpty() {
		return inputWidgetDelegate.isEmpty();
	}

	@Override
	public final void addValidator(final IValidator<VALUE_TYPE> validator) {
		inputWidgetDelegate.addValidator(validator);
	}

	protected final void addValidatable(final IValidateable validateable) {
		inputWidgetDelegate.addValidatable(validateable);
	}

}
