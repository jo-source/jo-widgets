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

package org.jowidgets.impl.widgets.basic.factory.internal;

import org.jowidgets.api.validation.ITextInputValidator;
import org.jowidgets.api.validation.ValidationMessage;
import org.jowidgets.api.validation.ValidationMessageType;
import org.jowidgets.api.widgets.IInputWidget;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.descriptor.ITextFieldDescriptor;
import org.jowidgets.api.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.api.widgets.factory.IWidgetFactory;
import org.jowidgets.impl.spi.ISpiBluePrintFactory;
import org.jowidgets.impl.spi.blueprint.ITextFieldBluePrintSpi;
import org.jowidgets.impl.widgets.basic.TextInputWidget;
import org.jowidgets.spi.IWidgetFactorySpi;
import org.jowidgets.spi.verify.IInputVerifier;
import org.jowidgets.spi.widgets.ITextInputWidgetSpi;

public class TextFieldWidgetFactory extends AbstractWidgetFactory implements
		IWidgetFactory<IInputWidget<String>, ITextFieldDescriptor> {

	public TextFieldWidgetFactory(
		final IGenericWidgetFactory genericWidgetFactory,
		final IWidgetFactorySpi spiWidgetFactory,
		final ISpiBluePrintFactory bpF) {

		super(genericWidgetFactory, spiWidgetFactory, bpF);
	}

	@Override
	public IInputWidget<String> create(final IWidget parent, final ITextFieldDescriptor descriptor) {
		final ITextFieldBluePrintSpi bp = getSpiBluePrintFactory().textField();

		final ITextInputValidator textInputValidator = descriptor.getTextInputValidator();

		bp.setInputVerifier(new IInputVerifier() {

			@Override
			public boolean verify(final String currentValue, final String input, final int start, final int end) {
				//TODO must check veto on NEW current value
				final ValidationMessage validationMessage = textInputValidator.isCompletableToValid(input);
				return validationMessage.getType() == ValidationMessageType.OK;
			}

		});

		final ITextInputWidgetSpi textFieldSpi = getSpiWidgetFactory().createTextFieldWidget(parent, bp);

		final IInputWidget<String> result = new TextInputWidget(parent, textFieldSpi, descriptor);
		result.addValidator(descriptor.getTextInputValidator());
		return result;
	}

}
