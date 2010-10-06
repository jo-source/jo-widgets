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
package org.jowidgets.api.widgets.factory.impl.internal;

import org.jowidgets.api.widgets.IInputWidget;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.blueprint.factory.impl.BluePrintFactory;
import org.jowidgets.api.widgets.descriptor.base.IBaseInputFieldDescriptor;
import org.jowidgets.api.widgets.descriptor.base.IBaseTextFieldDescriptor;
import org.jowidgets.api.widgets.factory.INativeWidgetFactory;
import org.jowidgets.api.widgets.factory.IWidgetFactory;

public class InputFieldWidgetFactory<VALUE_TYPE> implements
		IWidgetFactory<IInputWidget<VALUE_TYPE>, IBaseInputFieldDescriptor<IInputWidget<VALUE_TYPE>, VALUE_TYPE>> {

	private final INativeWidgetFactory nativeWidgetFactory;

	public InputFieldWidgetFactory(final INativeWidgetFactory nativeWidgetFactory) {
		super();
		this.nativeWidgetFactory = nativeWidgetFactory;
	}

	@Override
	public IInputWidget<VALUE_TYPE> create(
		final IWidget parent,
		final IBaseInputFieldDescriptor<IInputWidget<VALUE_TYPE>, VALUE_TYPE> descriptor) {

		final IInputWidget<String> textFieldWidget = nativeWidgetFactory.createTextFieldWidget(
				parent,
				new BluePrintFactory().textField().setTextInputValidator(descriptor.getConverter()));

		if (textFieldWidget == null) {
			throw new IllegalStateException("Could not create widget with descriptor interface class '"
				+ IBaseTextFieldDescriptor.class
				+ "' from '"
				+ INativeWidgetFactory.class.getName()
				+ "'");
		}

		return new InputFieldWidget<VALUE_TYPE>(textFieldWidget, descriptor);
	}

}
