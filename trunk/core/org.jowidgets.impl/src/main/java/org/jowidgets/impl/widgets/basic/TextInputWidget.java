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

import org.jowidgets.api.validation.IValidator;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.api.widgets.IInputWidget;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.controler.IInputListener;
import org.jowidgets.api.widgets.descriptor.setup.IInputWidgetSetup;
import org.jowidgets.impl.widgets.basic.delegate.ChildWidgetDelegate;
import org.jowidgets.impl.widgets.basic.delegate.InputWidgetDelegate;
import org.jowidgets.impl.widgets.common.wrapper.WidgetCommonWrapper;
import org.jowidgets.spi.widgets.ITextInputWidgetSpi;

public class TextInputWidget extends WidgetCommonWrapper implements IInputWidget<String> {

	private final ChildWidgetDelegate childWidgetDelegate;
	private final InputWidgetDelegate<String> inputWidgetDelegate;
	private final ITextInputWidgetSpi inputWidgetSpi;

	public TextInputWidget(final IWidget parent, final ITextInputWidgetSpi inputWidgetSpi, final IInputWidgetSetup<String> setup) {
		super(inputWidgetSpi);
		this.inputWidgetSpi = inputWidgetSpi;
		this.childWidgetDelegate = new ChildWidgetDelegate(parent);

		//must be last statement of constructor
		this.inputWidgetDelegate = new InputWidgetDelegate<String>(this, setup);
	}

	@Override
	public IWidget getParent() {
		return childWidgetDelegate.getParent();
	}

	@Override
	public void setEditable(final boolean editable) {
		inputWidgetSpi.setEditable(editable);
	}

	@Override
	public void addInputListener(final IInputListener listener) {
		inputWidgetSpi.addInputListener(listener);
	}

	@Override
	public void removeInputListener(final IInputListener listener) {
		inputWidgetSpi.removeInputListener(listener);
	}

	@Override
	public void setValue(final String value) {
		inputWidgetSpi.setText(value);
	}

	@Override
	public String getValue() {
		return inputWidgetSpi.getText();
	}

	@Override
	public ValidationResult validate() {
		return inputWidgetDelegate.validate();
	}

	@Override
	public boolean isMandatory() {
		return inputWidgetDelegate.isMandatory();
	}

	@Override
	public void setMandatory(final boolean mandatory) {
		inputWidgetDelegate.setMandatory(mandatory);
	}

	@Override
	public boolean hasInput() {
		return inputWidgetDelegate.hasInput();
	}

	@Override
	public void addValidator(final IValidator<String> validator) {
		inputWidgetDelegate.addValidator(validator);
	}

}
