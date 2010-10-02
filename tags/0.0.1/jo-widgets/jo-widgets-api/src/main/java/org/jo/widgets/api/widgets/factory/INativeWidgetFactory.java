/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of the jo-widgets.org nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
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
package org.jo.widgets.api.widgets.factory;

import org.jo.widgets.api.image.IImageRegistry;
import org.jo.widgets.api.widgets.IButtonWidget;
import org.jo.widgets.api.widgets.IComboBoxWidget;
import org.jo.widgets.api.widgets.ICompositeWidget;
import org.jo.widgets.api.widgets.IDialogWidget;
import org.jo.widgets.api.widgets.IIconWidget;
import org.jo.widgets.api.widgets.IInputWidget;
import org.jo.widgets.api.widgets.IRootWindowWidget;
import org.jo.widgets.api.widgets.IScrollCompositeWidget;
import org.jo.widgets.api.widgets.ISeparatorWidget;
import org.jo.widgets.api.widgets.ITextLabelWidget;
import org.jo.widgets.api.widgets.IToggleButtonWidget;
import org.jo.widgets.api.widgets.IWidget;
import org.jo.widgets.api.widgets.descriptor.IButtonDescriptor;
import org.jo.widgets.api.widgets.descriptor.ICheckBoxDescriptor;
import org.jo.widgets.api.widgets.descriptor.IComboBoxDescriptor;
import org.jo.widgets.api.widgets.descriptor.IComboBoxSelectionDescriptor;
import org.jo.widgets.api.widgets.descriptor.ICompositeDescriptor;
import org.jo.widgets.api.widgets.descriptor.IDialogDescriptor;
import org.jo.widgets.api.widgets.descriptor.IIconDescriptor;
import org.jo.widgets.api.widgets.descriptor.IRootWindowDescriptor;
import org.jo.widgets.api.widgets.descriptor.IScrollCompositeDescriptor;
import org.jo.widgets.api.widgets.descriptor.ISeparatorDescriptor;
import org.jo.widgets.api.widgets.descriptor.ITextFieldDescriptor;
import org.jo.widgets.api.widgets.descriptor.ITextLabelDescriptor;
import org.jo.widgets.api.widgets.descriptor.IToggleButtonDescriptor;

public interface INativeWidgetFactory {

	IImageRegistry getImageRegistry();

	IRootWindowWidget createRootWindowWidget(IGenericWidgetFactory factory,
			IRootWindowDescriptor descriptor);

	IDialogWidget createDialogWidget(IGenericWidgetFactory factory,
			IWidget parent, IDialogDescriptor descriptor);

	ICompositeWidget createCompositeWidget(IGenericWidgetFactory factory,
			IWidget parent, ICompositeDescriptor descriptor);

	IScrollCompositeWidget createScrollPaneWidget(
			IGenericWidgetFactory factory, IWidget parent,
			IScrollCompositeDescriptor descriptor);

	IInputWidget<String> createTextFieldWidget(IWidget parent,
			ITextFieldDescriptor descritor);

	ITextLabelWidget createTextLabelWidget(IWidget parent,
			ITextLabelDescriptor descriptor);

	IIconWidget createIconWidget(IWidget parent, IIconDescriptor descriptor);

	IButtonWidget createButtonWidget(final IWidget parent,
			IButtonDescriptor descriptor);

	ISeparatorWidget createSeparatorWidget(final IWidget parent,
			ISeparatorDescriptor descriptor);

	IToggleButtonWidget createCheckBoxWidget(final IWidget parent,
			ICheckBoxDescriptor descriptor);

	IToggleButtonWidget createToggleButtonWidget(final IWidget parent,
			IToggleButtonDescriptor descriptor);

	<INPUT_TYPE> IComboBoxWidget<INPUT_TYPE> createComboBoxSelectionWidget(
			final IWidget parent,
			IComboBoxSelectionDescriptor<INPUT_TYPE> descriptor);

	<INPUT_TYPE> IComboBoxWidget<INPUT_TYPE> createComboBoxWidget(
			final IWidget parent, IComboBoxDescriptor<INPUT_TYPE> descriptor);

}
