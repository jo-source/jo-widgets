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
package org.jowidgets.impl.swing;

import org.jowidgets.api.image.IImageRegistry;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.impl.swing.image.SwingImageRegistry;
import org.jowidgets.impl.swing.widgets.internal.ButtonWidget;
import org.jowidgets.impl.swing.widgets.internal.CheckBoxWidget;
import org.jowidgets.impl.swing.widgets.internal.ComboBoxSelectionWidget;
import org.jowidgets.impl.swing.widgets.internal.ComboBoxWidget;
import org.jowidgets.impl.swing.widgets.internal.CompositeWidget;
import org.jowidgets.impl.swing.widgets.internal.DialogWidget;
import org.jowidgets.impl.swing.widgets.internal.FrameWidget;
import org.jowidgets.impl.swing.widgets.internal.IconWidget;
import org.jowidgets.impl.swing.widgets.internal.ScrollCompositeWidget;
import org.jowidgets.impl.swing.widgets.internal.SeparatorWidget;
import org.jowidgets.impl.swing.widgets.internal.TextFieldWidget;
import org.jowidgets.impl.swing.widgets.internal.TextLabelWidget;
import org.jowidgets.impl.swing.widgets.internal.ToggleButtonWidget;
import org.jowidgets.spi.IWidgetFactorySpi;
import org.jowidgets.spi.widgets.IButtonWidgetSpi;
import org.jowidgets.spi.widgets.IComboBoxWidgetSpi;
import org.jowidgets.spi.widgets.IContainerWidgetSpi;
import org.jowidgets.spi.widgets.IFrameWidgetSpi;
import org.jowidgets.spi.widgets.IIconWidgetSpi;
import org.jowidgets.spi.widgets.IScrollContainerWidgetSpi;
import org.jowidgets.spi.widgets.ITextInputWidgetSpi;
import org.jowidgets.spi.widgets.ITextLabelWidgetSpi;
import org.jowidgets.spi.widgets.IToggleButtonWidgetSpi;
import org.jowidgets.spi.widgets.IWidgetSpi;
import org.jowidgets.spi.widgets.setup.IButtonSetupSpi;
import org.jowidgets.spi.widgets.setup.ICheckBoxSetupSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSelectionSetupSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSetupSpi;
import org.jowidgets.spi.widgets.setup.ICompositeSetupSpi;
import org.jowidgets.spi.widgets.setup.IDialogSetupSpi;
import org.jowidgets.spi.widgets.setup.IFrameSetupSpi;
import org.jowidgets.spi.widgets.setup.IIconSetupSpi;
import org.jowidgets.spi.widgets.setup.IScrollCompositeSetupSpi;
import org.jowidgets.spi.widgets.setup.ISeparatorSetupSpi;
import org.jowidgets.spi.widgets.setup.ITextInputWidgetSetupSpi;
import org.jowidgets.spi.widgets.setup.ITextLabelSetupSpi;
import org.jowidgets.spi.widgets.setup.IToggleButtonSetupSpi;

public final class SwingWidgetFactory implements IWidgetFactorySpi {

	private static final SwingWidgetFactory INSTANCE = new SwingWidgetFactory();

	private final SwingImageRegistry imageRegistry;

	private SwingWidgetFactory() {
		super();
		this.imageRegistry = new SwingImageRegistry();
	}

	@Override
	public IImageRegistry getImageRegistry() {
		return imageRegistry;
	}

	@Override
	public IFrameWidgetSpi createFrameWidget(final IGenericWidgetFactory factory, final IFrameSetupSpi setup) {
		return new FrameWidget(factory, imageRegistry, setup);
	}

	@Override
	public IFrameWidgetSpi createDialogWidget(
		final IGenericWidgetFactory factory,
		final IWidget parent,
		final IDialogSetupSpi setup) {
		return new DialogWidget(factory, imageRegistry, parent, setup);
	}

	@Override
	public IContainerWidgetSpi createCompositeWidget(
		final IGenericWidgetFactory factory,
		final IWidget parent,
		final ICompositeSetupSpi setup) {
		return new CompositeWidget(factory, setup);
	}

	@Override
	public IScrollContainerWidgetSpi createScrollCompositeWidget(
		final IGenericWidgetFactory factory,
		final IWidget parent,
		final IScrollCompositeSetupSpi setup) {
		return new ScrollCompositeWidget(factory, setup);
	}

	@Override
	public ITextInputWidgetSpi createTextFieldWidget(final IWidget parent, final ITextInputWidgetSetupSpi setup) {
		return new TextFieldWidget(parent, setup);
	}

	@Override
	public ITextLabelWidgetSpi createTextLabelWidget(final IWidget parent, final ITextLabelSetupSpi setup) {
		return new TextLabelWidget(parent, setup);
	}

	@Override
	public IIconWidgetSpi createIconWidget(final IWidget parent, final IIconSetupSpi setup) {
		return new IconWidget(imageRegistry, parent, setup);
	}

	@Override
	public IButtonWidgetSpi createButtonWidget(final IWidget parent, final IButtonSetupSpi setup) {
		return new ButtonWidget(imageRegistry, setup);
	}

	@Override
	public IWidgetSpi createSeparatorWidget(final IWidget parent, final ISeparatorSetupSpi setup) {
		return new SeparatorWidget(setup);
	}

	@Override
	public IToggleButtonWidgetSpi createCheckBoxWidget(final IWidget parent, final ICheckBoxSetupSpi setup) {
		return new CheckBoxWidget(imageRegistry, setup);
	}

	@Override
	public IToggleButtonWidgetSpi createToggleButtonWidget(final IWidget parent, final IToggleButtonSetupSpi setup) {
		return new ToggleButtonWidget(imageRegistry, setup);
	}

	@Override
	public <INPUT_TYPE> IComboBoxWidgetSpi<INPUT_TYPE> createComboBoxSelectionWidget(
		final IWidget parent,
		final IComboBoxSelectionSetupSpi<INPUT_TYPE> setup) {
		return new ComboBoxSelectionWidget<INPUT_TYPE>(setup);
	}

	@Override
	public <INPUT_TYPE> IComboBoxWidgetSpi<INPUT_TYPE> createComboBoxWidget(
		final IWidget parent,
		final IComboBoxSetupSpi<INPUT_TYPE> setup) {
		return new ComboBoxWidget<INPUT_TYPE>(setup);
	}

	public static SwingWidgetFactory getInstance() {
		return INSTANCE;
	}

}
