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
package org.jowidgets.spi.impl.swt.common;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.IWidgetFactorySpi;
import org.jowidgets.spi.impl.swt.common.options.SwtOptions;
import org.jowidgets.spi.impl.swt.common.widgets.ButtonImpl;
import org.jowidgets.spi.impl.swt.common.widgets.CanvasImpl;
import org.jowidgets.spi.impl.swt.common.widgets.CheckBoxImpl;
import org.jowidgets.spi.impl.swt.common.widgets.ComboBoxImpl;
import org.jowidgets.spi.impl.swt.common.widgets.CompositeImpl;
import org.jowidgets.spi.impl.swt.common.widgets.CompositeWrapper;
import org.jowidgets.spi.impl.swt.common.widgets.DialogImpl;
import org.jowidgets.spi.impl.swt.common.widgets.FrameImpl;
import org.jowidgets.spi.impl.swt.common.widgets.FrameWrapper;
import org.jowidgets.spi.impl.swt.common.widgets.IconImpl;
import org.jowidgets.spi.impl.swt.common.widgets.PopupDialogImpl;
import org.jowidgets.spi.impl.swt.common.widgets.ProgressBarImpl;
import org.jowidgets.spi.impl.swt.common.widgets.ScrollCompositeImpl;
import org.jowidgets.spi.impl.swt.common.widgets.SeparatorImpl;
import org.jowidgets.spi.impl.swt.common.widgets.SliderImpl;
import org.jowidgets.spi.impl.swt.common.widgets.SplitCompositeImpl;
import org.jowidgets.spi.impl.swt.common.widgets.TabFolderImpl;
import org.jowidgets.spi.impl.swt.common.widgets.TableImpl;
import org.jowidgets.spi.impl.swt.common.widgets.TextAreaImpl;
import org.jowidgets.spi.impl.swt.common.widgets.TextAreaNativeScrollBarImpl;
import org.jowidgets.spi.impl.swt.common.widgets.TextFieldImpl;
import org.jowidgets.spi.impl.swt.common.widgets.TextLabelImpl;
import org.jowidgets.spi.impl.swt.common.widgets.ToggleButtonImpl;
import org.jowidgets.spi.impl.swt.common.widgets.ToolBarImpl;
import org.jowidgets.spi.impl.swt.common.widgets.TreeImpl;
import org.jowidgets.spi.widgets.IButtonSpi;
import org.jowidgets.spi.widgets.ICanvasSpi;
import org.jowidgets.spi.widgets.ICheckBoxSpi;
import org.jowidgets.spi.widgets.IComboBoxSelectionSpi;
import org.jowidgets.spi.widgets.IComboBoxSpi;
import org.jowidgets.spi.widgets.ICompositeSpi;
import org.jowidgets.spi.widgets.IControlSpi;
import org.jowidgets.spi.widgets.IFrameSpi;
import org.jowidgets.spi.widgets.IIconSpi;
import org.jowidgets.spi.widgets.IPopupDialogSpi;
import org.jowidgets.spi.widgets.IProgressBarSpi;
import org.jowidgets.spi.widgets.IScrollCompositeSpi;
import org.jowidgets.spi.widgets.ISliderSpi;
import org.jowidgets.spi.widgets.ISplitCompositeSpi;
import org.jowidgets.spi.widgets.ITabFolderSpi;
import org.jowidgets.spi.widgets.ITableSpi;
import org.jowidgets.spi.widgets.ITextAreaSpi;
import org.jowidgets.spi.widgets.ITextControlSpi;
import org.jowidgets.spi.widgets.ITextLabelSpi;
import org.jowidgets.spi.widgets.IToggleButtonSpi;
import org.jowidgets.spi.widgets.IToolBarSpi;
import org.jowidgets.spi.widgets.ITreeSpi;
import org.jowidgets.spi.widgets.setup.IButtonSetupSpi;
import org.jowidgets.spi.widgets.setup.ICanvasSetupSpi;
import org.jowidgets.spi.widgets.setup.ICheckBoxSetupSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSelectionSetupSpi;
import org.jowidgets.spi.widgets.setup.IComboBoxSetupSpi;
import org.jowidgets.spi.widgets.setup.ICompositeSetupSpi;
import org.jowidgets.spi.widgets.setup.IDialogSetupSpi;
import org.jowidgets.spi.widgets.setup.IFrameSetupSpi;
import org.jowidgets.spi.widgets.setup.IIconSetupSpi;
import org.jowidgets.spi.widgets.setup.IPopupDialogSetupSpi;
import org.jowidgets.spi.widgets.setup.IProgressBarSetupSpi;
import org.jowidgets.spi.widgets.setup.IScrollCompositeSetupSpi;
import org.jowidgets.spi.widgets.setup.ISeparatorSetupSpi;
import org.jowidgets.spi.widgets.setup.ISliderSetupSpi;
import org.jowidgets.spi.widgets.setup.ISplitCompositeSetupSpi;
import org.jowidgets.spi.widgets.setup.ITabFolderSetupSpi;
import org.jowidgets.spi.widgets.setup.ITableSetupSpi;
import org.jowidgets.spi.widgets.setup.ITextAreaSetupSpi;
import org.jowidgets.spi.widgets.setup.ITextFieldSetupSpi;
import org.jowidgets.spi.widgets.setup.ITextLabelSetupSpi;
import org.jowidgets.spi.widgets.setup.IToggleButtonSetupSpi;
import org.jowidgets.spi.widgets.setup.IToolBarSetupSpi;
import org.jowidgets.spi.widgets.setup.ITreeSetupSpi;
import org.jowidgets.util.Assert;

public final class SwtWidgetFactory implements IWidgetFactorySpi {

	public SwtWidgetFactory() {
		super();
	}

	@Override
	public boolean hasMigLayoutSupport() {
		return SwtOptions.hasNativeMigLayout();
	}

	@Override
	public boolean isConvertibleToFrame(final Object uiReference) {
		return uiReference instanceof Shell;
	}

	@Override
	public IFrameSpi createFrame(final IGenericWidgetFactory factory, final Object uiReference) {
		Assert.paramNotNull(uiReference, "uiReference");
		if (uiReference instanceof Shell) {
			return new FrameWrapper(factory, (Shell) uiReference);
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
			return new CompositeWrapper(factory, (Composite) uiReference);
		}
		throw new IllegalArgumentException("UiReference must be instanceof of '" + Composite.class.getName() + "'");
	}

	@Override
	public IFrameSpi createFrame(final IGenericWidgetFactory factory, final IFrameSetupSpi setup) {
		return new FrameImpl(factory, setup);
	}

	@Override
	public IFrameSpi createDialog(final IGenericWidgetFactory factory, final Object parentUiReference, final IDialogSetupSpi setup) {
		return new DialogImpl(factory, parentUiReference, setup);
	}

	@Override
	public IPopupDialogSpi createPopupDialog(
		final IGenericWidgetFactory factory,
		final Object parentUiReference,
		final IPopupDialogSetupSpi setup) {
		return new PopupDialogImpl(factory, parentUiReference, setup);
	}

	@Override
	public ICompositeSpi createComposite(
		final IGenericWidgetFactory factory,
		final Object parentUiReference,
		final ICompositeSetupSpi setup) {
		return new CompositeImpl(factory, parentUiReference, setup);
	}

	@Override
	public IScrollCompositeSpi createScrollComposite(
		final IGenericWidgetFactory factory,
		final Object parentUiReference,
		final IScrollCompositeSetupSpi setup) {
		return new ScrollCompositeImpl(factory, parentUiReference, setup);
	}

	@Override
	public ISplitCompositeSpi createSplitComposite(
		final IGenericWidgetFactory factory,
		final Object parentUiReference,
		final ISplitCompositeSetupSpi setup) {
		return new SplitCompositeImpl(factory, parentUiReference, setup);
	}

	@Override
	public ITextControlSpi createTextField(final Object parentUiReference, final ITextFieldSetupSpi setup) {
		return new TextFieldImpl(parentUiReference, setup);
	}

	@Override
	public ITextAreaSpi createTextArea(final Object parentUiReference, final ITextAreaSetupSpi setup) {
		if (SwtOptions.hasNativeTextAreaScrollBars() || setup.isAlwaysShowBars()) {
			return new TextAreaNativeScrollBarImpl(parentUiReference, setup);
		}
		else {
			return new TextAreaImpl(parentUiReference, setup);
		}
	}

	@Override
	public ITextLabelSpi createTextLabel(final Object parentUiReference, final ITextLabelSetupSpi setup) {
		return new TextLabelImpl(parentUiReference, setup);
	}

	@Override
	public IIconSpi createIcon(final Object parentUiReference, final IIconSetupSpi setup) {
		return new IconImpl(parentUiReference, setup);
	}

	@Override
	public IButtonSpi createButton(final Object parentUiReference, final IButtonSetupSpi setup) {
		return new ButtonImpl(parentUiReference, setup);
	}

	@Override
	public IControlSpi createSeparator(final Object parentUiReference, final ISeparatorSetupSpi setup) {
		return new SeparatorImpl(parentUiReference, setup);
	}

	@Override
	public ICheckBoxSpi createCheckBox(final Object parentUiReference, final ICheckBoxSetupSpi setup) {
		return new CheckBoxImpl(parentUiReference, setup);
	}

	@Override
	public IToggleButtonSpi createToggleButton(final Object parentUiReference, final IToggleButtonSetupSpi setup) {
		return new ToggleButtonImpl(parentUiReference, setup);
	}

	@Override
	public IComboBoxSelectionSpi createComboBoxSelection(final Object parentUiReference, final IComboBoxSelectionSetupSpi setup) {
		return new ComboBoxImpl(parentUiReference, setup);
	}

	@Override
	public IComboBoxSpi createComboBox(final Object parentUiReference, final IComboBoxSetupSpi setup) {
		return new ComboBoxImpl(parentUiReference, setup);
	}

	@Override
	public IProgressBarSpi createProgressBar(final Object parentUiReference, final IProgressBarSetupSpi setup) {
		return new ProgressBarImpl(parentUiReference, setup);
	}

	@Override
	public IToolBarSpi createToolBar(
		final IGenericWidgetFactory factory,
		final Object parentUiReference,
		final IToolBarSetupSpi setup) {
		return new ToolBarImpl(factory, parentUiReference, setup);
	}

	@Override
	public ITabFolderSpi createTabFolder(
		final IGenericWidgetFactory factory,
		final Object parentUiReference,
		final ITabFolderSetupSpi setup) {
		return new TabFolderImpl(factory, parentUiReference, setup);
	}

	@Override
	public ITreeSpi createTree(final Object parentUiReference, final ITreeSetupSpi setup) {
		return new TreeImpl(parentUiReference, setup);
	}

	@Override
	public ITableSpi createTable(final Object parentUiReference, final ITableSetupSpi setup) {
		return new TableImpl(parentUiReference, setup);
	}

	@Override
	public ISliderSpi createSlider(final Object parentUiReference, final ISliderSetupSpi setup) {
		return new SliderImpl(parentUiReference, setup);
	}

	@Override
	public ICanvasSpi createCanvas(
		final IGenericWidgetFactory factory,
		final Object parentUiReference,
		final ICanvasSetupSpi setup) {
		return new CanvasImpl(factory, parentUiReference, setup);
	}

}
