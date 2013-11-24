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
package org.jowidgets.impl.widgets.composed.factory.internal;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.ITextControl;
import org.jowidgets.api.widgets.blueprint.ITextFieldBluePrint;
import org.jowidgets.api.widgets.descriptor.IInputFieldDescriptor;
import org.jowidgets.api.widgets.descriptor.setup.ITextFieldSetup;
import org.jowidgets.common.verify.IInputVerifier;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactory;
import org.jowidgets.impl.widgets.composed.InputFieldImpl;
import org.jowidgets.impl.widgets.composed.blueprint.BluePrintFactory;
import org.jowidgets.spi.IWidgetFactorySpi;
import org.jowidgets.tools.verify.InputVerifierComposite;

public class InputFieldFactory<VALUE_TYPE> implements IWidgetFactory<IInputField<VALUE_TYPE>, IInputFieldDescriptor<VALUE_TYPE>> {

	private final IGenericWidgetFactory genericFactory;

	public InputFieldFactory(final IGenericWidgetFactory genericFactory) {
		super();
		this.genericFactory = genericFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IInputField<VALUE_TYPE> create(final Object parentUiReference, final IInputFieldDescriptor<VALUE_TYPE> descriptor) {

		final BluePrintFactory bpF = new BluePrintFactory();

		final IConverter<VALUE_TYPE> converter;
		final Object converterObject = descriptor.getConverter();
		if (converterObject instanceof IConverter<?>) {
			converter = (IConverter<VALUE_TYPE>) descriptor.getConverter();
		}
		else {
			converter = null;
		}

		final IInputVerifier inputVerifier = descriptor.getInputVerifier();
		final IInputVerifier converterInputVerifier = converter != null ? converter.getInputVerifier() : null;

		InputVerifierComposite tfInputVerifier = null;
		if (inputVerifier != null || converterInputVerifier != null) {
			tfInputVerifier = new InputVerifierComposite();
			if (inputVerifier != null) {
				tfInputVerifier.addVerifier(inputVerifier);
			}
			if (converterInputVerifier != null) {
				tfInputVerifier.addVerifier(converterInputVerifier);
			}
		}

		final List<String> regExps = descriptor.getAcceptingRegExps();
		final String converterRegExp = converter != null ? converter.getAcceptingRegExp() : null;

		final List<String> tfRegExps = new LinkedList<String>();
		tfRegExps.addAll(regExps);
		if (converterRegExp != null) {
			tfRegExps.add(converterRegExp);
		}

		final ITextFieldBluePrint textFieldBluePrint = bpF.textField();
		textFieldBluePrint.setSetup(descriptor);
		textFieldBluePrint.setInputVerifier(tfInputVerifier);
		textFieldBluePrint.setAcceptingRegExps(tfRegExps);
		if (converter != null) {
			textFieldBluePrint.setMask(converter.getMask());
		}
		textFieldBluePrint.setEditable(descriptor.isEditable());
		textFieldBluePrint.setMaxLength(descriptor.getMaxLength());
		textFieldBluePrint.setPasswordPresentation(descriptor.isPasswordPresentation());

		final ITextControl textField = genericFactory.create(parentUiReference, textFieldBluePrint);

		if (textField == null) {
			throw new IllegalStateException("Could not create widget with descriptor interface class '"
				+ ITextFieldSetup.class
				+ "' from '"
				+ IWidgetFactorySpi.class.getName()
				+ "'");
		}

		return new InputFieldImpl<VALUE_TYPE>(textField, descriptor);
	}
}
