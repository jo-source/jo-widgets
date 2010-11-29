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
package org.jowidgets.impl.widgets.composed.factory;

import org.jowidgets.api.widgets.descriptor.IInputCompositeDescriptor;
import org.jowidgets.api.widgets.descriptor.IInputDialogDescriptor;
import org.jowidgets.api.widgets.descriptor.IInputFieldDescriptor;
import org.jowidgets.api.widgets.descriptor.ILabelDescriptor;
import org.jowidgets.api.widgets.descriptor.IMessageDialogDescriptor;
import org.jowidgets.api.widgets.descriptor.IProgressBarDescriptor;
import org.jowidgets.api.widgets.descriptor.IQuestionDialogDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextSeparatorDescriptor;
import org.jowidgets.api.widgets.descriptor.IValidationLabelDescriptor;
import org.jowidgets.impl.widgets.basic.factory.BasicGenericWidgetFactory;
import org.jowidgets.impl.widgets.composed.factory.internal.InputCompositeWidgetFactory;
import org.jowidgets.impl.widgets.composed.factory.internal.InputDialogWidgetFactory;
import org.jowidgets.impl.widgets.composed.factory.internal.InputFieldWidgetFactory;
import org.jowidgets.impl.widgets.composed.factory.internal.LabelWidgetFactory;
import org.jowidgets.impl.widgets.composed.factory.internal.MessageDialogWidgetFactory;
import org.jowidgets.impl.widgets.composed.factory.internal.ProgressBarWidgetFactory;
import org.jowidgets.impl.widgets.composed.factory.internal.QuestionDialogWidgetFactory;
import org.jowidgets.impl.widgets.composed.factory.internal.TextSeparatorWidgetFactory;
import org.jowidgets.impl.widgets.composed.factory.internal.ValidationLabelWidgetFactory;
import org.jowidgets.spi.IWidgetFactorySpi;

public class GenericWidgetFactory extends BasicGenericWidgetFactory {

	public GenericWidgetFactory(final IWidgetFactorySpi spiWidgetsFactory) {
		super(spiWidgetsFactory);
		registerCustomWidgetFactories();
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void registerCustomWidgetFactories() {
		register(IInputFieldDescriptor.class, new InputFieldWidgetFactory(this));
		register(ILabelDescriptor.class, new LabelWidgetFactory());
		register(ITextSeparatorDescriptor.class, new TextSeparatorWidgetFactory());
		register(IMessageDialogDescriptor.class, new MessageDialogWidgetFactory(this));
		register(IQuestionDialogDescriptor.class, new QuestionDialogWidgetFactory(this));
		register(IInputDialogDescriptor.class, new InputDialogWidgetFactory(this));
		register(IInputCompositeDescriptor.class, new InputCompositeWidgetFactory());
		register(IValidationLabelDescriptor.class, new ValidationLabelWidgetFactory(this));
		register(IProgressBarDescriptor.class, new ProgressBarWidgetFactory(getSpiWidgetFactory()));
	}

}
