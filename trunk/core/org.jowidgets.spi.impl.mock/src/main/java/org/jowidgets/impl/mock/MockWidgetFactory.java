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
package org.jowidgets.impl.mock;

import org.jowidgets.common.widgets.IContainerWidgetCommon;
import org.jowidgets.common.widgets.IFrameWidgetCommon;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.impl.mock.image.MockImageRegistry;
import org.jowidgets.impl.mock.mockui.UIMContainer;
import org.jowidgets.impl.mock.mockui.UIMWindow;
import org.jowidgets.impl.mock.widgets.internal.ButtonWidget;
import org.jowidgets.impl.mock.widgets.internal.CheckBoxWidget;
import org.jowidgets.impl.mock.widgets.internal.ComboBoxSelectionWidget;
import org.jowidgets.impl.mock.widgets.internal.ComboBoxWidget;
import org.jowidgets.impl.mock.widgets.internal.CompositeWidget;
import org.jowidgets.impl.mock.widgets.internal.CompositeWidgetWrapper;
import org.jowidgets.impl.mock.widgets.internal.DialogWidget;
import org.jowidgets.impl.mock.widgets.internal.FrameWidget;
import org.jowidgets.impl.mock.widgets.internal.FrameWidgetWrapper;
import org.jowidgets.impl.mock.widgets.internal.IconWidget;
import org.jowidgets.impl.mock.widgets.internal.ProgressBarWidget;
import org.jowidgets.impl.mock.widgets.internal.ScrollCompositeWidget;
import org.jowidgets.impl.mock.widgets.internal.SeparatorWidget;
import org.jowidgets.impl.mock.widgets.internal.SplitPaneWidget;
import org.jowidgets.impl.mock.widgets.internal.TextFieldWidget;
import org.jowidgets.impl.mock.widgets.internal.TextLabelWidget;
import org.jowidgets.impl.mock.widgets.internal.ToggleButtonWidget;
import org.jowidgets.spi.IWidgetFactorySpi;
import org.jowidgets.spi.widgets.IButtonWidgetSpi;
import org.jowidgets.spi.widgets.ICheckBoxWidgetSpi;
import org.jowidgets.spi.widgets.IComboBoxSelectionWidgetSpi;
import org.jowidgets.spi.widgets.IComboBoxWidgetSpi;
import org.jowidgets.spi.widgets.IContainerWidgetSpi;
import org.jowidgets.spi.widgets.IFrameWidgetSpi;
import org.jowidgets.spi.widgets.IIconWidgetSpi;
import org.jowidgets.spi.widgets.IProgressBarWidgetSpi;
import org.jowidgets.spi.widgets.IScrollContainerWidgetSpi;
import org.jowidgets.spi.widgets.ISplitContainerWidgetSpi;
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
import org.jowidgets.spi.widgets.setup.IProgressBarSetupSpi;
import org.jowidgets.spi.widgets.setup.IScrollCompositeSetupSpi;
import org.jowidgets.spi.widgets.setup.ISeparatorSetupSpi;
import org.jowidgets.spi.widgets.setup.ISplitContainerSetupSpi;
import org.jowidgets.spi.widgets.setup.ITextFieldSetupSpi;
import org.jowidgets.spi.widgets.setup.ITextLabelSetupSpi;
import org.jowidgets.spi.widgets.setup.IToggleButtonSetupSpi;
import org.jowidgets.util.Assert;

public final class MockWidgetFactory implements IWidgetFactorySpi {

	private final MockImageRegistry imageRegistry;

	public MockWidgetFactory(final MockImageRegistry imageRegistry) {
		super();
		this.imageRegistry = imageRegistry;
	}

	@Override
	public boolean isConvertibleToFrame(final Object uiReference) {
		return uiReference instanceof UIMWindow;
	}

	@Override
	public IFrameWidgetCommon createFrameWidget(final IGenericWidgetFactory factory, final Object uiReference) {
		Assert.paramNotNull(uiReference, "uiReference");
		if (uiReference instanceof UIMWindow) {
			return new FrameWidgetWrapper(factory, (UIMWindow) uiReference);
		}
		throw new IllegalArgumentException("UiReference must be instanceof of '" + UIMWindow.class.getName() + "'");
	}

	@Override
	public boolean isConvertibleToContainer(final Object uiReference) {
		return uiReference instanceof UIMContainer;
	}

	@Override
	public IContainerWidgetCommon createContainerWidget(final IGenericWidgetFactory factory, final Object uiReference) {
		Assert.paramNotNull(uiReference, "uiReference");
		if (uiReference instanceof UIMContainer) {
			return new CompositeWidgetWrapper(factory, (UIMContainer) uiReference);
		}
		throw new IllegalArgumentException("UiReference must be instanceof of '" + UIMContainer.class.getName() + "'");
	}

	@Override
	public IFrameWidgetSpi createFrameWidget(final IGenericWidgetFactory factory, final IFrameSetupSpi setup) {
		return new FrameWidget(factory, imageRegistry, setup);
	}

	@Override
	public IFrameWidgetSpi createDialogWidget(
		final IGenericWidgetFactory factory,
		final IWidgetCommon parent,
		final IDialogSetupSpi setup) {
		return new DialogWidget(factory, imageRegistry, parent, setup);
	}

	@Override
	public IContainerWidgetSpi createCompositeWidget(
		final IGenericWidgetFactory factory,
		final IWidgetCommon parent,
		final ICompositeSetupSpi setup) {
		return new CompositeWidget(factory, setup);
	}

	@Override
	public IScrollContainerWidgetSpi createScrollCompositeWidget(
		final IGenericWidgetFactory factory,
		final IWidgetCommon parent,
		final IScrollCompositeSetupSpi setup) {
		return new ScrollCompositeWidget(factory, setup);
	}

	@Override
	public ISplitContainerWidgetSpi createSplitContainerWidget(
		final IGenericWidgetFactory factory,
		final IWidgetCommon parent,
		final ISplitContainerSetupSpi setup) {
		return new SplitPaneWidget(factory, setup);
	}

	@Override
	public ITextInputWidgetSpi createTextFieldWidget(final IWidgetCommon parent, final ITextFieldSetupSpi setup) {
		return new TextFieldWidget(parent, setup);
	}

	@Override
	public ITextLabelWidgetSpi createTextLabelWidget(final IWidgetCommon parent, final ITextLabelSetupSpi setup) {
		return new TextLabelWidget(parent, setup);
	}

	@Override
	public IIconWidgetSpi createIconWidget(final IWidgetCommon parent, final IIconSetupSpi setup) {
		return new IconWidget(imageRegistry, parent, setup);
	}

	@Override
	public IButtonWidgetSpi createButtonWidget(final IWidgetCommon parent, final IButtonSetupSpi setup) {
		return new ButtonWidget(imageRegistry, setup);
	}

	@Override
	public IWidgetSpi createSeparatorWidget(final IWidgetCommon parent, final ISeparatorSetupSpi setup) {
		return new SeparatorWidget(setup);
	}

	@Override
	public ICheckBoxWidgetSpi createCheckBoxWidget(final IWidgetCommon parent, final ICheckBoxSetupSpi setup) {
		return new CheckBoxWidget(setup);
	}

	@Override
	public IToggleButtonWidgetSpi createToggleButtonWidget(final IWidgetCommon parent, final IToggleButtonSetupSpi setup) {
		return new ToggleButtonWidget(imageRegistry, setup);
	}

	@Override
	public IComboBoxSelectionWidgetSpi createComboBoxSelectionWidget(final IWidgetCommon parent, final IComboBoxSelectionSetupSpi setup) {
		return new ComboBoxSelectionWidget(setup);
	}

	@Override
	public IComboBoxWidgetSpi createComboBoxWidget(final IWidgetCommon parent, final IComboBoxSetupSpi setup) {
		return new ComboBoxWidget(setup);
	}

	@Override
	public IProgressBarWidgetSpi createProgressBar(final IWidgetCommon parent, final IProgressBarSetupSpi setup) {
		return new ProgressBarWidget(imageRegistry, parent, setup);
	}

}
