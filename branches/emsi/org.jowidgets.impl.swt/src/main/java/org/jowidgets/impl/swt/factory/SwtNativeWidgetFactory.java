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
package org.jowidgets.impl.swt.factory;

import org.jowidgets.api.image.IImageRegistry;
import org.jowidgets.api.widgets.IButtonWidget;
import org.jowidgets.api.widgets.IComboBoxWidget;
import org.jowidgets.api.widgets.ICompositeWidget;
import org.jowidgets.api.widgets.IDialogWidget;
import org.jowidgets.api.widgets.IIconWidget;
import org.jowidgets.api.widgets.IInputWidget;
import org.jowidgets.api.widgets.IRootWindowWidget;
import org.jowidgets.api.widgets.IScrollCompositeWidget;
import org.jowidgets.api.widgets.ISeparatorWidget;
import org.jowidgets.api.widgets.ITextLabelWidget;
import org.jowidgets.api.widgets.IToggleButtonWidget;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.descriptor.IButtonDescriptor;
import org.jowidgets.api.widgets.descriptor.ICheckBoxDescriptor;
import org.jowidgets.api.widgets.descriptor.IComboBoxDescriptor;
import org.jowidgets.api.widgets.descriptor.IComboBoxSelectionDescriptor;
import org.jowidgets.api.widgets.descriptor.ICompositeDescriptor;
import org.jowidgets.api.widgets.descriptor.IDialogDescriptor;
import org.jowidgets.api.widgets.descriptor.IIconDescriptor;
import org.jowidgets.api.widgets.descriptor.IRootWindowDescriptor;
import org.jowidgets.api.widgets.descriptor.IScrollCompositeDescriptor;
import org.jowidgets.api.widgets.descriptor.ISeparatorDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextFieldDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextLabelDescriptor;
import org.jowidgets.api.widgets.descriptor.IToggleButtonDescriptor;
import org.jowidgets.api.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.api.widgets.factory.INativeWidgetFactory;
import org.jowidgets.impl.swt.factory.internal.ButtonWidget;
import org.jowidgets.impl.swt.factory.internal.CheckBoxWidget;
import org.jowidgets.impl.swt.factory.internal.ComboBoxSelectionWidget;
import org.jowidgets.impl.swt.factory.internal.ComboBoxWidget;
import org.jowidgets.impl.swt.factory.internal.CompositeWidget;
import org.jowidgets.impl.swt.factory.internal.DialogWidget;
import org.jowidgets.impl.swt.factory.internal.IconWidget;
import org.jowidgets.impl.swt.factory.internal.RootWindowWidget;
import org.jowidgets.impl.swt.factory.internal.ScrollCompositeWidget;
import org.jowidgets.impl.swt.factory.internal.SeparatorWidget;
import org.jowidgets.impl.swt.factory.internal.TextFieldWidget;
import org.jowidgets.impl.swt.factory.internal.TextLabelWidget;
import org.jowidgets.impl.swt.factory.internal.ToggleButtonWidget;
import org.jowidgets.impl.swt.internal.color.ColorCache;
import org.jowidgets.impl.swt.internal.color.IColorCache;
import org.jowidgets.impl.swt.internal.image.SwtImageRegistry;

public final class SwtNativeWidgetFactory implements INativeWidgetFactory {

	private static final SwtNativeWidgetFactory INSTANCE = new SwtNativeWidgetFactory();

	private final SwtImageRegistry imageRegistry;
	private final IColorCache colorCache;

	private SwtNativeWidgetFactory() {
		super();
		this.colorCache = new ColorCache();
		this.imageRegistry = new SwtImageRegistry();
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
		return new DialogWidget(factory, colorCache, imageRegistry, parent, descriptor);
	}

	@Override
	public IRootWindowWidget createRootWindowWidget(final IGenericWidgetFactory factory, final IRootWindowDescriptor descriptor) {
		return new RootWindowWidget(factory, colorCache, imageRegistry, descriptor);
	}

	@Override
	public ICompositeWidget createCompositeWidget(
		final IGenericWidgetFactory factory,
		final IWidget parent,
		final ICompositeDescriptor descriptor) {
		return new CompositeWidget(factory, colorCache, parent, descriptor);
	}

	@Override
	public IScrollCompositeWidget createScrollPaneWidget(
		final IGenericWidgetFactory factory,
		final IWidget parent,
		final IScrollCompositeDescriptor descriptor) {
		return new ScrollCompositeWidget(factory, colorCache, parent, descriptor);
	}

	@Override
	public IInputWidget<String> createTextFieldWidget(final IWidget parent, final ITextFieldDescriptor descriptor) {
		return new TextFieldWidget(colorCache, parent, descriptor);
	}

	@Override
	public ITextLabelWidget createTextLabelWidget(final IWidget parent, final ITextLabelDescriptor descriptor) {
		return new TextLabelWidget(colorCache, parent, descriptor);
	}

	@Override
	public IIconWidget createIconWidget(final IWidget parent, final IIconDescriptor descriptor) {
		return new IconWidget(colorCache, imageRegistry, parent, descriptor);
	}

	@Override
	public IButtonWidget createButtonWidget(final IWidget parent, final IButtonDescriptor descriptor) {
		return new ButtonWidget(colorCache, imageRegistry, parent, descriptor);
	}

	@Override
	public ISeparatorWidget createSeparatorWidget(final IWidget parent, final ISeparatorDescriptor descriptor) {
		return new SeparatorWidget(colorCache, parent, descriptor);
	}

	@Override
	public IToggleButtonWidget createCheckBoxWidget(final IWidget parent, final ICheckBoxDescriptor descriptor) {
		return new CheckBoxWidget(colorCache, imageRegistry, parent, descriptor);
	}

	@Override
	public IToggleButtonWidget createToggleButtonWidget(final IWidget parent, final IToggleButtonDescriptor descriptor) {
		return new ToggleButtonWidget(colorCache, imageRegistry, parent, descriptor);
	}

	@Override
	public <INPUT_TYPE> IComboBoxWidget<INPUT_TYPE> createComboBoxSelectionWidget(
		final IWidget parent,
		final IComboBoxSelectionDescriptor<INPUT_TYPE> descriptor) {
		return new ComboBoxSelectionWidget<INPUT_TYPE>(parent, colorCache, descriptor);
	}

	@Override
	public <INPUT_TYPE> IComboBoxWidget<INPUT_TYPE> createComboBoxWidget(
		final IWidget parent,
		final IComboBoxDescriptor<INPUT_TYPE> descriptor) {
		return new ComboBoxWidget<INPUT_TYPE>(parent, colorCache, descriptor);
	}

	public static SwtNativeWidgetFactory getInstance() {
		return INSTANCE;
	}

}
