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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.impl.swt.color.ColorCache;
import org.jowidgets.impl.swt.color.IColorCache;
import org.jowidgets.impl.swt.image.SwtImageRegistry;
import org.jowidgets.impl.swt.widgets.internal.ButtonWidget;
import org.jowidgets.impl.swt.widgets.internal.CheckBoxWidget;
import org.jowidgets.impl.swt.widgets.internal.ComboBoxSelectionWidget;
import org.jowidgets.impl.swt.widgets.internal.ComboBoxWidget;
import org.jowidgets.impl.swt.widgets.internal.CompositeWidget;
import org.jowidgets.impl.swt.widgets.internal.CompositeWidgetWrapper;
import org.jowidgets.impl.swt.widgets.internal.DialogWidget;
import org.jowidgets.impl.swt.widgets.internal.FrameWidget;
import org.jowidgets.impl.swt.widgets.internal.FrameWidgetWrapper;
import org.jowidgets.impl.swt.widgets.internal.IconWidget;
import org.jowidgets.impl.swt.widgets.internal.ProgressBarWidget;
import org.jowidgets.impl.swt.widgets.internal.ScrollCompositeWidget;
import org.jowidgets.impl.swt.widgets.internal.SeparatorWidget;
import org.jowidgets.impl.swt.widgets.internal.SplitCompositeWidget;
import org.jowidgets.impl.swt.widgets.internal.TextFieldWidget;
import org.jowidgets.impl.swt.widgets.internal.TextLabelWidget;
import org.jowidgets.impl.swt.widgets.internal.ToggleButtonWidget;
import org.jowidgets.spi.IWidgetFactorySpi;
import org.jowidgets.spi.widgets.IButtonSpi;
import org.jowidgets.spi.widgets.ICheckBoxSpi;
import org.jowidgets.spi.widgets.IComboBoxSelectionSpi;
import org.jowidgets.spi.widgets.IComboBoxSpi;
import org.jowidgets.spi.widgets.ICompositeSpi;
import org.jowidgets.spi.widgets.IControlSpi;
import org.jowidgets.spi.widgets.IFrameSpi;
import org.jowidgets.spi.widgets.IIconSpi;
import org.jowidgets.spi.widgets.IProgressBarSpi;
import org.jowidgets.spi.widgets.IScrollCompositeSpi;
import org.jowidgets.spi.widgets.ISplitCompositeSpi;
import org.jowidgets.spi.widgets.ITextFieldSpi;
import org.jowidgets.spi.widgets.ITextLabelSpi;
import org.jowidgets.spi.widgets.IToggleButtonSpi;
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
import org.jowidgets.spi.widgets.setup.ISplitCompositeSetupSpi;
import org.jowidgets.spi.widgets.setup.ITextFieldSetupSpi;
import org.jowidgets.spi.widgets.setup.ITextLabelSetupSpi;
import org.jowidgets.spi.widgets.setup.IToggleButtonSetupSpi;
import org.jowidgets.util.Assert;

public final class SwtWidgetFactory implements IWidgetFactorySpi {

	private final SwtImageRegistry imageRegistry;
	private final IColorCache colorCache;

	public SwtWidgetFactory(final SwtImageRegistry imageRegistry) {
		super();
		this.colorCache = new ColorCache();
		this.imageRegistry = imageRegistry;
	}

	@Override
	public boolean isConvertibleToFrame(final Object uiReference) {
		return uiReference instanceof Shell;
	}

	@Override
	public IFrameSpi createFrame(final IGenericWidgetFactory factory, final Object uiReference) {
		Assert.paramNotNull(uiReference, "uiReference");
		if (uiReference instanceof Shell) {
			return new FrameWidgetWrapper(factory, colorCache, (Shell) uiReference);
		}
		throw new IllegalArgumentException("UiReference must be instanceof of '" + Shell.class.getName() + "'");
	}

	@Override
	public boolean isConvertibleToComposite(final Object uiReference) {
		return uiReference instanceof Composite;
	}

	@Override
	public ICompositeSpi createComposite(final IGenericWidgetFactory factory, final Object uiReference) {
		Assert.paramNotNull(uiReference, "uiReference");
		if (uiReference instanceof Composite) {
			return new CompositeWidgetWrapper(factory, colorCache, (Composite) uiReference);
		}
		throw new IllegalArgumentException("UiReference must be instanceof of '" + Composite.class.getName() + "'");
	}

	@Override
	public IFrameSpi createFrame(final IGenericWidgetFactory factory, final IFrameSetupSpi setup) {
		return new FrameWidget(factory, colorCache, imageRegistry, setup);
	}

	@Override
	public IFrameSpi createDialog(final IGenericWidgetFactory factory, final Object parentUiReference, final IDialogSetupSpi setup) {
		return new DialogWidget(factory, colorCache, imageRegistry, parentUiReference, setup);
	}

	@Override
	public ICompositeSpi createComposite(
		final IGenericWidgetFactory factory,
		final Object parentUiReference,
		final ICompositeSetupSpi setup) {
		return new CompositeWidget(factory, colorCache, parentUiReference, setup);
	}

	@Override
	public IScrollCompositeSpi createScrollComposite(
		final IGenericWidgetFactory factory,
		final Object parentUiReference,
		final IScrollCompositeSetupSpi setup) {
		return new ScrollCompositeWidget(factory, colorCache, parentUiReference, setup);
	}

	@Override
	public ISplitCompositeSpi createSplitComposite(
		final IGenericWidgetFactory factory,
		final Object parentUiReference,
		final ISplitCompositeSetupSpi setup) {
		return new SplitCompositeWidget(factory, colorCache, parentUiReference, setup);
	}

	@Override
	public ITextFieldSpi createTextField(final Object parentUiReference, final ITextFieldSetupSpi setup) {
		return new TextFieldWidget(colorCache, parentUiReference, setup);
	}

	@Override
	public ITextLabelSpi createTextLabel(final Object parentUiReference, final ITextLabelSetupSpi setup) {
		return new TextLabelWidget(colorCache, parentUiReference, setup);
	}

	@Override
	public IIconSpi createIcon(final Object parentUiReference, final IIconSetupSpi setup) {
		return new IconWidget(colorCache, imageRegistry, parentUiReference, setup);
	}

	@Override
	public IButtonSpi createButton(final Object parentUiReference, final IButtonSetupSpi setup) {
		return new ButtonWidget(colorCache, imageRegistry, parentUiReference, setup);
	}

	@Override
	public IControlSpi createSeparator(final Object parentUiReference, final ISeparatorSetupSpi setup) {
		return new SeparatorWidget(colorCache, parentUiReference, setup);
	}

	@Override
	public ICheckBoxSpi createCheckBox(final Object parentUiReference, final ICheckBoxSetupSpi setup) {
		return new CheckBoxWidget(colorCache, parentUiReference, setup);
	}

	@Override
	public IToggleButtonSpi createToggleButton(final Object parentUiReference, final IToggleButtonSetupSpi setup) {
		return new ToggleButtonWidget(colorCache, imageRegistry, parentUiReference, setup);
	}

	@Override
	public IComboBoxSelectionSpi createComboBoxSelection(final Object parentUiReference, final IComboBoxSelectionSetupSpi setup) {
		return new ComboBoxSelectionWidget(parentUiReference, colorCache, setup);
	}

	@Override
	public IComboBoxSpi createComboBox(final Object parentUiReference, final IComboBoxSetupSpi setup) {
		return new ComboBoxWidget(parentUiReference, colorCache, setup);
	}

	@Override
	public IProgressBarSpi createProgressBar(final Object parentUiReference, final IProgressBarSetupSpi setup) {
		return new ProgressBarWidget(colorCache, parentUiReference, setup);
	}

}
