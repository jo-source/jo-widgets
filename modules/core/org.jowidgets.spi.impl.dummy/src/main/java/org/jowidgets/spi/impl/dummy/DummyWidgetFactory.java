/*
 * Copyright (c) 2010, Michael Grossmann, Lukas Gross
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
package org.jowidgets.spi.impl.dummy;

import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.IWidgetFactorySpi;
import org.jowidgets.spi.impl.dummy.image.DummyImageRegistry;
import org.jowidgets.spi.impl.dummy.ui.UIDContainer;
import org.jowidgets.spi.impl.dummy.ui.UIDWindow;
import org.jowidgets.spi.impl.dummy.widgets.ButtonImpl;
import org.jowidgets.spi.impl.dummy.widgets.CheckBoxImpl;
import org.jowidgets.spi.impl.dummy.widgets.ComboBoxImpl;
import org.jowidgets.spi.impl.dummy.widgets.ComboBoxSelectionImpl;
import org.jowidgets.spi.impl.dummy.widgets.CompositeImpl;
import org.jowidgets.spi.impl.dummy.widgets.CompositeWrapper;
import org.jowidgets.spi.impl.dummy.widgets.DialogImpl;
import org.jowidgets.spi.impl.dummy.widgets.FrameImpl;
import org.jowidgets.spi.impl.dummy.widgets.IconImpl;
import org.jowidgets.spi.impl.dummy.widgets.NativeDummyFrameWrapper;
import org.jowidgets.spi.impl.dummy.widgets.ProgressBarImpl;
import org.jowidgets.spi.impl.dummy.widgets.ScrollCompositeImpl;
import org.jowidgets.spi.impl.dummy.widgets.SeparatorImpl;
import org.jowidgets.spi.impl.dummy.widgets.SplitCompositeImpl;
import org.jowidgets.spi.impl.dummy.widgets.TabFolderImpl;
import org.jowidgets.spi.impl.dummy.widgets.TableImpl;
import org.jowidgets.spi.impl.dummy.widgets.TextFieldImpl;
import org.jowidgets.spi.impl.dummy.widgets.TextLabelImpl;
import org.jowidgets.spi.impl.dummy.widgets.ToggleButtonImpl;
import org.jowidgets.spi.impl.dummy.widgets.ToolBarImpl;
import org.jowidgets.spi.impl.dummy.widgets.TreeImpl;
import org.jowidgets.spi.widgets.IButtonSpi;
import org.jowidgets.spi.widgets.ICanvasSpi;
import org.jowidgets.spi.widgets.ICheckBoxSpi;
import org.jowidgets.spi.widgets.IComboBoxSelectionSpi;
import org.jowidgets.spi.widgets.IComboBoxSpi;
import org.jowidgets.spi.widgets.ICompositeSpi;
import org.jowidgets.spi.widgets.ICompositeWrapperSpi;
import org.jowidgets.spi.widgets.IControlSpi;
import org.jowidgets.spi.widgets.IFrameSpi;
import org.jowidgets.spi.widgets.IFrameWrapperSpi;
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

public class DummyWidgetFactory implements IWidgetFactorySpi {

    private final DummyImageRegistry imageRegistry;

    public DummyWidgetFactory(final DummyImageRegistry imageRegistry) {
        super();
        this.imageRegistry = imageRegistry;
    }

    @Override
    public boolean hasMigLayoutSupport() {
        return false;
    }

    @Override
    public boolean isConvertibleToFrame(final Object uiReference) {
        return uiReference instanceof UIDWindow;
    }

    @Override
    public IFrameWrapperSpi createFrame(final IGenericWidgetFactory factory, final Object uiReference) {
        Assert.paramNotNull(uiReference, "uiReference");
        if (uiReference instanceof UIDWindow) {
            return new NativeDummyFrameWrapper(factory, (UIDWindow) uiReference);
        }
        throw new IllegalArgumentException("UiReference must be instanceof of '" + UIDWindow.class.getName() + "'");
    }

    @Override
    public boolean isConvertibleToComposite(final Object uiReference) {
        return uiReference instanceof UIDContainer;
    }

    @Override
    public ICompositeWrapperSpi createComposite(final IGenericWidgetFactory factory, final Object uiReference) {
        Assert.paramNotNull(uiReference, "uiReference");
        if (uiReference instanceof UIDContainer) {
            return new CompositeWrapper(factory, (UIDContainer) uiReference);
        }
        throw new IllegalArgumentException("UiReference must be instanceof of '" + UIDContainer.class.getName() + "'");
    }

    @Override
    public IFrameSpi createFrame(final IGenericWidgetFactory factory, final IFrameSetupSpi setup) {
        return new FrameImpl(factory, imageRegistry, setup);
    }

    @Override
    public IFrameSpi createDialog(
        final IGenericWidgetFactory factory,
        final Object parentUiReference,
        final IDialogSetupSpi setup) {
        return new DialogImpl(factory, imageRegistry, parentUiReference, setup);
    }

    @Override
    public IPopupDialogSpi createPopupDialog(
        final IGenericWidgetFactory factory,
        final Object parentUiReference,
        final IPopupDialogSetupSpi setup) {
        // TODO must be implemented
        return null;
    }

    @Override
    public ICompositeSpi createComposite(
        final IGenericWidgetFactory factory,
        final Object parentUiReference,
        final ICompositeSetupSpi setup) {
        return new CompositeImpl(factory, setup);
    }

    @Override
    public IScrollCompositeSpi createScrollComposite(
        final IGenericWidgetFactory factory,
        final Object parentUiReference,
        final IScrollCompositeSetupSpi setup) {
        return new ScrollCompositeImpl(factory, setup);
    }

    @Override
    public ISplitCompositeSpi createSplitComposite(
        final IGenericWidgetFactory factory,
        final Object parentUiReference,
        final ISplitCompositeSetupSpi setup) {
        return new SplitCompositeImpl(factory, setup);
    }

    @Override
    public ITextControlSpi createTextField(final Object parentUiReference, final ITextFieldSetupSpi setup) {
        return new TextFieldImpl(setup);
    }

    @Override
    public ITextAreaSpi createTextArea(final Object parentUiReference, final ITextAreaSetupSpi setup) {
        // TODO must be implemented
        return null;
    }

    @Override
    public ITextLabelSpi createTextLabel(final Object parentUiReference, final ITextLabelSetupSpi setup) {
        return new TextLabelImpl(setup);
    }

    @Override
    public IIconSpi createIcon(final Object parentUiReference, final IIconSetupSpi setup) {
        return new IconImpl(imageRegistry, setup);
    }

    @Override
    public IButtonSpi createButton(final Object parentUiReference, final IButtonSetupSpi setup) {
        return new ButtonImpl(imageRegistry, setup);
    }

    @Override
    public IControlSpi createSeparator(final Object parentUiReference, final ISeparatorSetupSpi setup) {
        return new SeparatorImpl(setup);
    }

    @Override
    public ICheckBoxSpi createCheckBox(final Object parentUiReference, final ICheckBoxSetupSpi setup) {
        return new CheckBoxImpl(setup);
    }

    @Override
    public IToggleButtonSpi createToggleButton(final Object parentUiReference, final IToggleButtonSetupSpi setup) {
        return new ToggleButtonImpl(imageRegistry, setup);
    }

    @Override
    public IComboBoxSelectionSpi createComboBoxSelection(final Object parentUiReference, final IComboBoxSelectionSetupSpi setup) {
        return new ComboBoxSelectionImpl(setup);
    }

    @Override
    public IComboBoxSpi createComboBox(final Object parentUiReference, final IComboBoxSetupSpi setup) {
        return new ComboBoxImpl(setup);
    }

    @Override
    public IProgressBarSpi createProgressBar(final Object parentUiReference, final IProgressBarSetupSpi setup) {
        return new ProgressBarImpl(imageRegistry, setup);
    }

    @Override
    public IToolBarSpi createToolBar(
        final IGenericWidgetFactory factory,
        final Object parentUiReference,
        final IToolBarSetupSpi setup) {
        return new ToolBarImpl(imageRegistry, factory);
    }

    @Override
    public ITabFolderSpi createTabFolder(
        final IGenericWidgetFactory factory,
        final Object parentUiReference,
        final ITabFolderSetupSpi setup) {
        return new TabFolderImpl(factory, setup);
    }

    @Override
    public ITreeSpi createTree(final Object parentUiReference, final ITreeSetupSpi setup) {
        return new TreeImpl(setup);
    }

    @Override
    public ITableSpi createTable(
        final IGenericWidgetFactory factory,
        final Object parentUiReference,
        final ITableSetupSpi setup) {

        return new TableImpl(setup);
    }

    @Override
    public ISliderSpi createSlider(final Object parentUiReference, final ISliderSetupSpi setup) {
        // TODO must be implemented
        return null;
    }

    @Override
    public ICanvasSpi createCanvas(
        final IGenericWidgetFactory factory,
        final Object parentUiReference,
        final ICanvasSetupSpi setup) {
        // TODO must be implemented
        return null;
    }

}
