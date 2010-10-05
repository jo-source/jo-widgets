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
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jo.widgets.impl.swing.factory;

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
import org.jo.widgets.api.widgets.factory.IGenericWidgetFactory;
import org.jo.widgets.api.widgets.factory.INativeWidgetFactory;
import org.jo.widgets.impl.swing.factory.internal.ButtonWidget;
import org.jo.widgets.impl.swing.factory.internal.CheckBoxWidget;
import org.jo.widgets.impl.swing.factory.internal.ComboBoxSelectionWidget;
import org.jo.widgets.impl.swing.factory.internal.ComboBoxWidget;
import org.jo.widgets.impl.swing.factory.internal.CompositeWidget;
import org.jo.widgets.impl.swing.factory.internal.DialogWidget;
import org.jo.widgets.impl.swing.factory.internal.IconWidget;
import org.jo.widgets.impl.swing.factory.internal.RootWindowWidget;
import org.jo.widgets.impl.swing.factory.internal.ScrollCompositeWidget;
import org.jo.widgets.impl.swing.factory.internal.SeparatorWidget;
import org.jo.widgets.impl.swing.factory.internal.TextFieldWidget;
import org.jo.widgets.impl.swing.factory.internal.TextLabelWidget;
import org.jo.widgets.impl.swing.factory.internal.ToggleButtonWidget;
import org.jo.widgets.impl.swing.internal.image.SwingImageRegistry;

public final class SwingNativeWidgetFactory implements INativeWidgetFactory {

	private static final SwingNativeWidgetFactory INSTANCE = new SwingNativeWidgetFactory();

	private final SwingImageRegistry imageRegistry;

	private SwingNativeWidgetFactory() {
		super();
		this.imageRegistry = new SwingImageRegistry();
	}

	@Override
	public IImageRegistry getImageRegistry() {
		return imageRegistry;
	}

	@Override
	public IDialogWidget createDialogWidget(
		final IGenericWidgetFactory factory,
		final IWidget parent,
		final IDialogDescriptor descriptor) {
		return new DialogWidget(factory, imageRegistry, parent, descriptor);
	}

	@Override
	public IRootWindowWidget createRootWindowWidget(final IGenericWidgetFactory factory, final IRootWindowDescriptor descriptor) {
		return new RootWindowWidget(factory, imageRegistry, descriptor);
	}

	@Override
	public ICompositeWidget createCompositeWidget(
		final IGenericWidgetFactory factory,
		final IWidget parent,
		final ICompositeDescriptor descriptor) {
		return new CompositeWidget(factory, parent, descriptor);
	}

	@Override
	public IScrollCompositeWidget createScrollPaneWidget(
		final IGenericWidgetFactory factory,
		final IWidget parent,
		final IScrollCompositeDescriptor descriptor) {
		return new ScrollCompositeWidget(factory, parent, descriptor);
	}

	@Override
	public IInputWidget<String> createTextFieldWidget(final IWidget parent, final ITextFieldDescriptor descriptor) {
		return new TextFieldWidget(parent, descriptor);
	}

	@Override
	public ITextLabelWidget createTextLabelWidget(final IWidget parent, final ITextLabelDescriptor descriptor) {
		return new TextLabelWidget(parent, descriptor);
	}

	@Override
	public IIconWidget createIconWidget(final IWidget parent, final IIconDescriptor descriptor) {
		return new IconWidget(imageRegistry, parent, descriptor);
	}

	@Override
	public IButtonWidget createButtonWidget(final IWidget parent, final IButtonDescriptor descriptor) {
		return new ButtonWidget(imageRegistry, parent, descriptor);
	}

	@Override
	public ISeparatorWidget createSeparatorWidget(final IWidget parent, final ISeparatorDescriptor descriptor) {
		return new SeparatorWidget(parent, descriptor);
	}

	@Override
	public IToggleButtonWidget createCheckBoxWidget(final IWidget parent, final ICheckBoxDescriptor descriptor) {
		return new CheckBoxWidget(imageRegistry, parent, descriptor);
	}

	@Override
	public IToggleButtonWidget createToggleButtonWidget(final IWidget parent, final IToggleButtonDescriptor descriptor) {
		return new ToggleButtonWidget(imageRegistry, parent, descriptor);
	}

	@Override
	public <INPUT_TYPE> IComboBoxWidget<INPUT_TYPE> createComboBoxSelectionWidget(
		final IWidget parent,
		final IComboBoxSelectionDescriptor<INPUT_TYPE> descriptor) {
		return new ComboBoxSelectionWidget<INPUT_TYPE>(parent, descriptor);
	}

	@Override
	public <INPUT_TYPE> IComboBoxWidget<INPUT_TYPE> createComboBoxWidget(
		final IWidget parent,
		final IComboBoxDescriptor<INPUT_TYPE> descriptor) {
		return new ComboBoxWidget<INPUT_TYPE>(parent, descriptor);
	}

	public static SwingNativeWidgetFactory getInstance() {
		return INSTANCE;
	}

}
