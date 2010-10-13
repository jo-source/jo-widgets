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
package org.jowidgets.spi;

import org.jowidgets.api.image.IImageRegistry;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.widgets.IButtonWidgetSpi;
import org.jowidgets.spi.widgets.IComboBoxWidgetSpi;
import org.jowidgets.spi.widgets.IContainerWidgetSpi;
import org.jowidgets.spi.widgets.IFrameWidgetSpi;
import org.jowidgets.spi.widgets.IIconWidgetSpi;
import org.jowidgets.spi.widgets.IInputWidgetSpi;
import org.jowidgets.spi.widgets.IScrollContainerWidgetSpi;
import org.jowidgets.spi.widgets.ITextLabelWidgetSpi;
import org.jowidgets.spi.widgets.IToggleButtonWidgetSpi;
import org.jowidgets.spi.widgets.IWidgetSpi;
import org.jowidgets.spi.widgets.descriptor.IButtonDescriptorSpi;
import org.jowidgets.spi.widgets.descriptor.ICheckBoxDescriptorSpi;
import org.jowidgets.spi.widgets.descriptor.IComboBoxDescriptorSpi;
import org.jowidgets.spi.widgets.descriptor.IComboBoxSelectionDescriptorSpi;
import org.jowidgets.spi.widgets.descriptor.ICompositeDescriptorSpi;
import org.jowidgets.spi.widgets.descriptor.IDialogDescriptorSpi;
import org.jowidgets.spi.widgets.descriptor.IFrameDescriptorSpi;
import org.jowidgets.spi.widgets.descriptor.IIconDescriptorSpi;
import org.jowidgets.spi.widgets.descriptor.IScrollCompositeDescriptorSpi;
import org.jowidgets.spi.widgets.descriptor.ISeparatorDescriptorSpi;
import org.jowidgets.spi.widgets.descriptor.ITextFieldDescriptorSpi;
import org.jowidgets.spi.widgets.descriptor.ITextLabelDescriptorSpi;
import org.jowidgets.spi.widgets.descriptor.IToggleButtonDescriptorSpi;

public interface IWidgetFactorySpi {

	IImageRegistry getImageRegistry();

	IFrameWidgetSpi createFrameWidget(IGenericWidgetFactory factory, IFrameDescriptorSpi descriptor);

	IFrameWidgetSpi createDialogWidget(IGenericWidgetFactory factory, IWidget parent, IDialogDescriptorSpi descriptor);

	IContainerWidgetSpi createCompositeWidget(IGenericWidgetFactory factory, IWidget parent, ICompositeDescriptorSpi descriptor);

	IScrollContainerWidgetSpi createScrollCompositeWidget(
		IGenericWidgetFactory factory,
		IWidget parent,
		IScrollCompositeDescriptorSpi descriptor);

	IInputWidgetSpi<String> createTextFieldWidget(IWidget parent, ITextFieldDescriptorSpi descritor);

	ITextLabelWidgetSpi createTextLabelWidget(IWidget parent, ITextLabelDescriptorSpi descriptor);

	IIconWidgetSpi createIconWidget(IWidget parent, IIconDescriptorSpi descriptor);

	IButtonWidgetSpi createButtonWidget(final IWidget parent, IButtonDescriptorSpi descriptor);

	IWidgetSpi createSeparatorWidget(final IWidget parent, ISeparatorDescriptorSpi descriptor);

	IToggleButtonWidgetSpi createCheckBoxWidget(final IWidget parent, ICheckBoxDescriptorSpi descriptor);

	IToggleButtonWidgetSpi createToggleButtonWidget(final IWidget parent, IToggleButtonDescriptorSpi descriptor);

	<INPUT_TYPE> IComboBoxWidgetSpi<INPUT_TYPE> createComboBoxSelectionWidget(
		final IWidget parent,
		IComboBoxSelectionDescriptorSpi<INPUT_TYPE> descriptor);

	<INPUT_TYPE> IComboBoxWidgetSpi<INPUT_TYPE> createComboBoxWidget(
		final IWidget parent,
		IComboBoxDescriptorSpi<INPUT_TYPE> descriptor);

}
