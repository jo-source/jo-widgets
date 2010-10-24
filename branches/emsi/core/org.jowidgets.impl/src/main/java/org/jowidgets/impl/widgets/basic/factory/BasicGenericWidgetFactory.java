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
package org.jowidgets.impl.widgets.basic.factory;

import org.jowidgets.api.widgets.descriptor.IButtonDescriptor;
import org.jowidgets.api.widgets.descriptor.ICheckBoxDescriptor;
import org.jowidgets.api.widgets.descriptor.IComboBoxDescriptor;
import org.jowidgets.api.widgets.descriptor.IComboBoxSelectionDescriptor;
import org.jowidgets.api.widgets.descriptor.ICompositeDescriptor;
import org.jowidgets.api.widgets.descriptor.IDialogDescriptor;
import org.jowidgets.api.widgets.descriptor.IFrameDescriptor;
import org.jowidgets.api.widgets.descriptor.IIconDescriptor;
import org.jowidgets.api.widgets.descriptor.IScrollCompositeDescriptor;
import org.jowidgets.api.widgets.descriptor.ISeparatorDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextFieldDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextLabelDescriptor;
import org.jowidgets.api.widgets.descriptor.IToggleButtonDescriptor;
import org.jowidgets.impl.base.widgets.factory.DefaultGenericWidgetFactory;
import org.jowidgets.impl.base.widgets.factory.GenericWidgetFactoryWrapper;
import org.jowidgets.impl.spi.ISpiBluePrintFactory;
import org.jowidgets.impl.spi.SpiBluePrintFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.ButtonWidgetFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.CheckBoxWidgetFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.ComboBoxSelectionWidgetFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.ComboBoxWidgetFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.CompositeWidgetFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.DialogWidgetFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.FrameWidgetFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.IconWidgetFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.ScrollCompositeWidgetFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.SeparatorWidgetFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.TextFieldWidgetFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.TextLabelWidgetFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.ToggleButtonWidgetFactory;
import org.jowidgets.spi.IWidgetFactorySpi;

public class BasicGenericWidgetFactory extends GenericWidgetFactoryWrapper {

	public BasicGenericWidgetFactory(final IWidgetFactorySpi spiWidgetFactory) {
		super(new DefaultGenericWidgetFactory(spiWidgetFactory.getImageRegistry()));
		registerBaseWidgets(spiWidgetFactory, new SpiBluePrintFactory());
	}

	@SuppressWarnings({"unchecked"})
	private void registerBaseWidgets(final IWidgetFactorySpi spiWidgetFactory, final ISpiBluePrintFactory bpF) {
		register(IFrameDescriptor.class, new FrameWidgetFactory(this, spiWidgetFactory, bpF));
		register(IDialogDescriptor.class, new DialogWidgetFactory(this, spiWidgetFactory, bpF));
		register(ICompositeDescriptor.class, new CompositeWidgetFactory(this, spiWidgetFactory, bpF));
		register(IScrollCompositeDescriptor.class, new ScrollCompositeWidgetFactory(this, spiWidgetFactory, bpF));
		register(ITextFieldDescriptor.class, new TextFieldWidgetFactory(this, spiWidgetFactory, bpF));
		register(IIconDescriptor.class, new IconWidgetFactory(this, spiWidgetFactory, bpF));
		register(ITextLabelDescriptor.class, new TextLabelWidgetFactory(this, spiWidgetFactory, bpF));
		register(IButtonDescriptor.class, new ButtonWidgetFactory(this, spiWidgetFactory, bpF));
		register(ISeparatorDescriptor.class, new SeparatorWidgetFactory(this, spiWidgetFactory, bpF));
		register(IToggleButtonDescriptor.class, new ToggleButtonWidgetFactory(this, spiWidgetFactory, bpF));
		register(ICheckBoxDescriptor.class, new CheckBoxWidgetFactory(this, spiWidgetFactory, bpF));
		register(IComboBoxSelectionDescriptor.class, new ComboBoxSelectionWidgetFactory(this, spiWidgetFactory, bpF));
		register(IComboBoxDescriptor.class, new ComboBoxWidgetFactory(this, spiWidgetFactory, bpF));
	}
}
