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

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.descriptor.setup.IInputFieldSetup;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.tools.widgets.base.AbstractInputControl;

public class InputFieldWidget<VALUE_TYPE> extends AbstractInputControl<VALUE_TYPE> implements IInputControl<VALUE_TYPE> {

	private final IInputControl<String> textFieldWidget;
	private final IConverter<VALUE_TYPE> converter;

	public InputFieldWidget(final IInputControl<String> textFieldWidget, final IInputFieldSetup<VALUE_TYPE> setup) {

		super(textFieldWidget, setup);

		this.textFieldWidget = textFieldWidget;
		this.converter = setup.getConverter();

		registerInputWidget(textFieldWidget);

		setEditable(setup.isEditable());
		VisibiliySettingsInvoker.setVisibility(setup, this);
		ColorSettingsInvoker.setColors(setup, this);

		if (setup.getValue() != null) {
			setValue(setup.getValue());
		}
	}

	@Override
	public VALUE_TYPE getValue() {
		return converter.convertToObject(textFieldWidget.getValue());
	}

	@Override
	public void setValue(final VALUE_TYPE value) {
		textFieldWidget.setValue(converter.convertToString(value));
	}

}
