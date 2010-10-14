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
package org.jowidgets.impl.swt;

import org.jowidgets.api.image.IImageRegistry;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.impl.swt.color.ColorCache;
import org.jowidgets.impl.swt.color.IColorCache;
import org.jowidgets.impl.swt.image.SwtImageRegistry;
import org.jowidgets.impl.swt.widgets.internal.ButtonWidget;
import org.jowidgets.impl.swt.widgets.internal.CheckBoxWidget;
import org.jowidgets.impl.swt.widgets.internal.ComboBoxSelectionWidget;
import org.jowidgets.impl.swt.widgets.internal.ComboBoxWidget;
import org.jowidgets.impl.swt.widgets.internal.CompositeWidget;
import org.jowidgets.impl.swt.widgets.internal.DialogWidget;
import org.jowidgets.impl.swt.widgets.internal.FrameWidget;
import org.jowidgets.impl.swt.widgets.internal.IconWidget;
import org.jowidgets.impl.swt.widgets.internal.ScrollCompositeWidget;
import org.jowidgets.impl.swt.widgets.internal.SeparatorWidget;
import org.jowidgets.impl.swt.widgets.internal.TextFieldWidget;
import org.jowidgets.impl.swt.widgets.internal.TextLabelWidget;
import org.jowidgets.impl.swt.widgets.internal.ToggleButtonWidget;
import org.jowidgets.spi.IWidgetFactorySpi;
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

public final class SwtWidgetFactory implements IWidgetFactorySpi {

	private static final SwtWidgetFactory INSTANCE = new SwtWidgetFactory();

	private final SwtImageRegistry imageRegistry;
	private final IColorCache colorCache;

	private SwtWidgetFactory() {
		super();
		this.colorCache = new ColorCache();
		this.imageRegistry = new SwtImageRegistry();
	}

	@Override
	public IImageRegistry getImageRegistry() {
		return imageRegistry;
	}

	@Override
	public IFrameWidgetSpi createDialogWidget(
		final IGenericWidgetFactory factory,
		final IWidget parent,
		final IDialogDescriptorSpi descriptor) {
		return new DialogWidget(factory, colorCache, imageRegistry, parent, descriptor);
	}

	@Override
	public IFrameWidgetSpi createFrameWidget(final IGenericWidgetFactory factory, final IFrameDescriptorSpi descriptor) {
		return new FrameWidget(factory, colorCache, imageRegistry, descriptor);
	}

	@Override
	public IContainerWidgetSpi createCompositeWidget(
		final IGenericWidgetFactory factory,
		final IWidget parent,
		final ICompositeDescriptorSpi descriptor) {
		return new CompositeWidget(factory, colorCache, parent, descriptor);
	}

	@Override
	public IScrollContainerWidgetSpi createScrollCompositeWidget(
		final IGenericWidgetFactory factory,
		final IWidget parent,
		final IScrollCompositeDescriptorSpi descriptor) {
		return new ScrollCompositeWidget(factory, colorCache, parent, descriptor);
	}

	@Override
	public IInputWidgetSpi<String> createTextFieldWidget(final IWidget parent, final ITextFieldDescriptorSpi descriptor) {
		return new TextFieldWidget(colorCache, parent, descriptor);
	}

	@Override
	public ITextLabelWidgetSpi createTextLabelWidget(final IWidget parent, final ITextLabelDescriptorSpi descriptor) {
		return new TextLabelWidget(colorCache, parent, descriptor);
	}

	@Override
	public IIconWidgetSpi createIconWidget(final IWidget parent, final IIconDescriptorSpi descriptor) {
		return new IconWidget(colorCache, imageRegistry, parent, descriptor);
	}

	@Override
	public IButtonWidgetSpi createButtonWidget(final IWidget parent, final IButtonDescriptorSpi descriptor) {
		return new ButtonWidget(colorCache, imageRegistry, parent, descriptor);
	}

	@Override
	public IWidgetSpi createSeparatorWidget(final IWidget parent, final ISeparatorDescriptorSpi descriptor) {
		return new SeparatorWidget(colorCache, parent, descriptor);
	}

	@Override
	public IToggleButtonWidgetSpi createCheckBoxWidget(final IWidget parent, final ICheckBoxDescriptorSpi descriptor) {
		return new CheckBoxWidget(colorCache, imageRegistry, parent, descriptor);
	}

	@Override
	public IToggleButtonWidgetSpi createToggleButtonWidget(final IWidget parent, final IToggleButtonDescriptorSpi descriptor) {
		return new ToggleButtonWidget(colorCache, imageRegistry, parent, descriptor);
	}

	@Override
	public <INPUT_TYPE> IComboBoxWidgetSpi<INPUT_TYPE> createComboBoxSelectionWidget(
		final IWidget parent,
		final IComboBoxSelectionDescriptorSpi<INPUT_TYPE> descriptor) {
		return new ComboBoxSelectionWidget<INPUT_TYPE>(parent, colorCache, descriptor);
	}

	@Override
	public <INPUT_TYPE> IComboBoxWidgetSpi<INPUT_TYPE> createComboBoxWidget(
		final IWidget parent,
		final IComboBoxDescriptorSpi<INPUT_TYPE> descriptor) {
		return new ComboBoxWidget<INPUT_TYPE>(parent, colorCache, descriptor);
	}

	public static SwtWidgetFactory getInstance() {
		return INSTANCE;
	}

}
