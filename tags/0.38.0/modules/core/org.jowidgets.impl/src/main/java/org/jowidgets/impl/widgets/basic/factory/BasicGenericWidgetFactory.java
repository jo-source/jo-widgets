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
package org.jowidgets.impl.widgets.basic.factory;

import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.ICheckBoxBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxBluePrint;
import org.jowidgets.api.widgets.blueprint.ICompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.IIconBluePrint;
import org.jowidgets.api.widgets.blueprint.IScrollCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.ISplitCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.IToggleButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IToolBarBluePrint;
import org.jowidgets.api.widgets.descriptor.IButtonDescriptor;
import org.jowidgets.api.widgets.descriptor.ICalendarDescriptor;
import org.jowidgets.api.widgets.descriptor.ICanvasDescriptor;
import org.jowidgets.api.widgets.descriptor.ICheckBoxDescriptor;
import org.jowidgets.api.widgets.descriptor.IComboBoxDescriptor;
import org.jowidgets.api.widgets.descriptor.IComboBoxSelectionDescriptor;
import org.jowidgets.api.widgets.descriptor.ICompositeDescriptor;
import org.jowidgets.api.widgets.descriptor.IDialogDescriptor;
import org.jowidgets.api.widgets.descriptor.IDirectoryChooserDescriptor;
import org.jowidgets.api.widgets.descriptor.IFileChooserDescriptor;
import org.jowidgets.api.widgets.descriptor.IFrameDescriptor;
import org.jowidgets.api.widgets.descriptor.IIconDescriptor;
import org.jowidgets.api.widgets.descriptor.IPopupDialogDescriptor;
import org.jowidgets.api.widgets.descriptor.IScrollCompositeDescriptor;
import org.jowidgets.api.widgets.descriptor.ISeparatorDescriptor;
import org.jowidgets.api.widgets.descriptor.ISliderDescriptor;
import org.jowidgets.api.widgets.descriptor.ISplitCompositeDescriptor;
import org.jowidgets.api.widgets.descriptor.ITabFolderDescriptor;
import org.jowidgets.api.widgets.descriptor.ITableDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextAreaDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextFieldDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextLabelDescriptor;
import org.jowidgets.api.widgets.descriptor.IToggleButtonDescriptor;
import org.jowidgets.api.widgets.descriptor.IToolBarDescriptor;
import org.jowidgets.api.widgets.descriptor.ITreeDescriptor;
import org.jowidgets.impl.base.factory.DefaultGenericWidgetFactory;
import org.jowidgets.impl.base.factory.GenericWidgetFactoryWrapper;
import org.jowidgets.impl.spi.ISpiBluePrintFactory;
import org.jowidgets.impl.spi.SpiBluePrintFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.ButtonFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.CalendarFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.CanvasFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.CheckBoxFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.ComboBoxFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.ComboBoxSelectionFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.CompositeFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.DialogFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.DirectoryChooserFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.FileChooserFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.FrameFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.IconFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.PopupDialogFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.ScrollCompositeFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.SeparatorFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.SliderFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.SplitCompositeFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.TabFolderFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.TableFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.TextAreaFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.TextFieldFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.TextLabelFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.ToggleButtonFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.ToolBarFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.TreeFactory;
import org.jowidgets.impl.widgets.basic.factory.internal.UiWidgetFactory;
import org.jowidgets.spi.IOptionalWidgetsFactorySpi;
import org.jowidgets.spi.IWidgetFactorySpi;
import org.jowidgets.spi.IWidgetsServiceProvider;
import org.jowidgets.test.api.widgets.IButtonUi;
import org.jowidgets.test.api.widgets.ICheckBoxUi;
import org.jowidgets.test.api.widgets.IComboBoxUi;
import org.jowidgets.test.api.widgets.ICompositeUi;
import org.jowidgets.test.api.widgets.IFrameUi;
import org.jowidgets.test.api.widgets.IIconUi;
import org.jowidgets.test.api.widgets.IScrollCompositeUi;
import org.jowidgets.test.api.widgets.ISplitCompositeUi;
import org.jowidgets.test.api.widgets.ITextLabelUi;
import org.jowidgets.test.api.widgets.IToggleButtonUi;
import org.jowidgets.test.api.widgets.IToolBarUi;
import org.jowidgets.test.api.widgets.descriptor.IButtonDescriptorUi;
import org.jowidgets.test.api.widgets.descriptor.ICheckBoxDescriptorUi;
import org.jowidgets.test.api.widgets.descriptor.IComboBoxDescriptorUi;
import org.jowidgets.test.api.widgets.descriptor.IComboBoxSelectionDescriptorUi;
import org.jowidgets.test.api.widgets.descriptor.ICompositeDescriptorUi;
import org.jowidgets.test.api.widgets.descriptor.IDialogDescriptorUi;
import org.jowidgets.test.api.widgets.descriptor.IFrameDescriptorUi;
import org.jowidgets.test.api.widgets.descriptor.IIconDescriptorUi;
import org.jowidgets.test.api.widgets.descriptor.IScrollCompositeDescriptorUi;
import org.jowidgets.test.api.widgets.descriptor.ISplitCompositeDescriptorUi;
import org.jowidgets.test.api.widgets.descriptor.ITextFieldDescriptorUi;
import org.jowidgets.test.api.widgets.descriptor.ITextLabelDescriptorUi;
import org.jowidgets.test.api.widgets.descriptor.IToggleButtonDescriptorUi;
import org.jowidgets.test.api.widgets.descriptor.IToolBarDescriptorUi;

public class BasicGenericWidgetFactory extends GenericWidgetFactoryWrapper {

	private final IWidgetsServiceProvider widgetsServiceProvider;
	private final IOptionalWidgetsFactorySpi optionalWidgetsFactorySpi;

	public BasicGenericWidgetFactory(final IWidgetsServiceProvider widgetsServiceProvider) {
		super(new DefaultGenericWidgetFactory());
		this.widgetsServiceProvider = widgetsServiceProvider;
		final SpiBluePrintFactory spiBbf = new SpiBluePrintFactory();
		registerBaseWidgets(widgetsServiceProvider, spiBbf);
		registerUiWidgets(widgetsServiceProvider, spiBbf);

		final IOptionalWidgetsFactorySpi optionalFactorySpi = widgetsServiceProvider.getOptionalWidgetFactory();
		if (optionalFactorySpi == null) {
			this.optionalWidgetsFactorySpi = new DefaultOptionalWidgetsFactorySpi();
		}
		else {
			this.optionalWidgetsFactorySpi = optionalFactorySpi;
		}
		registerOptionalWidgets(widgetsServiceProvider, spiBbf);
	}

	@SuppressWarnings({"unchecked"})
	private void registerBaseWidgets(final IWidgetsServiceProvider widgetsServiceProvider, final ISpiBluePrintFactory bpF) {
		register(IFrameDescriptor.class, new FrameFactory(this, widgetsServiceProvider, bpF));
		register(IDialogDescriptor.class, new DialogFactory(this, widgetsServiceProvider, bpF));
		register(IPopupDialogDescriptor.class, new PopupDialogFactory(this, widgetsServiceProvider, bpF));
		register(ICompositeDescriptor.class, new CompositeFactory(this, widgetsServiceProvider, bpF));
		register(IScrollCompositeDescriptor.class, new ScrollCompositeFactory(this, widgetsServiceProvider, bpF));
		register(ISplitCompositeDescriptor.class, new SplitCompositeFactory(this, widgetsServiceProvider, bpF));
		register(ITextFieldDescriptor.class, new TextFieldFactory(this, widgetsServiceProvider, bpF));
		register(ITextAreaDescriptor.class, new TextAreaFactory(this, widgetsServiceProvider, bpF));
		register(IIconDescriptor.class, new IconFactory(this, widgetsServiceProvider, bpF));
		register(ITextLabelDescriptor.class, new TextLabelFactory(this, widgetsServiceProvider, bpF));
		register(IButtonDescriptor.class, new ButtonFactory(this, widgetsServiceProvider, bpF));
		register(ISeparatorDescriptor.class, new SeparatorFactory(this, widgetsServiceProvider, bpF));
		register(IToggleButtonDescriptor.class, new ToggleButtonFactory(this, widgetsServiceProvider, bpF));
		register(ICheckBoxDescriptor.class, new CheckBoxFactory(this, widgetsServiceProvider, bpF));
		register(IComboBoxSelectionDescriptor.class, new ComboBoxSelectionFactory<Object>(this, widgetsServiceProvider, bpF));
		register(IComboBoxDescriptor.class, new ComboBoxFactory<Object>(this, widgetsServiceProvider, bpF));
		register(IToolBarDescriptor.class, new ToolBarFactory(this, widgetsServiceProvider, bpF));
		register(ITabFolderDescriptor.class, new TabFolderFactory(this, widgetsServiceProvider, bpF));
		register(ITreeDescriptor.class, new TreeFactory(this, widgetsServiceProvider, bpF));
		register(ITableDescriptor.class, new TableFactory(this, widgetsServiceProvider, bpF));
		register(ISliderDescriptor.class, new SliderFactory(this, widgetsServiceProvider, bpF));
		register(ICanvasDescriptor.class, new CanvasFactory(this, widgetsServiceProvider, bpF));
	}

	private void registerOptionalWidgets(final IWidgetsServiceProvider widgetsServiceProvider, final ISpiBluePrintFactory bpF) {

		if (optionalWidgetsFactorySpi.hasFileChooser()) {
			register(IFileChooserDescriptor.class, new FileChooserFactory(this, widgetsServiceProvider, bpF));
		}
		if (optionalWidgetsFactorySpi.hasDirectoryChooser()) {
			register(IDirectoryChooserDescriptor.class, new DirectoryChooserFactory(this, widgetsServiceProvider, bpF));
		}
		if (optionalWidgetsFactorySpi.hasCalendar()) {
			register(ICalendarDescriptor.class, new CalendarFactory(this, widgetsServiceProvider, bpF));
		}
	}

	@SuppressWarnings("unchecked")
	private void registerUiWidgets(final IWidgetsServiceProvider widgetsServiceProvider, final ISpiBluePrintFactory bpF) {
		registerUiWidget(IFrameDescriptorUi.class, IFrameUi.class, IFrameBluePrint.class);
		registerUiWidget(IButtonDescriptorUi.class, IButtonUi.class, IButtonBluePrint.class);
		registerUiWidget(IToolBarDescriptorUi.class, IToolBarUi.class, IToolBarBluePrint.class);
		registerUiWidget(IComboBoxDescriptorUi.class, IComboBoxUi.class, IComboBoxBluePrint.class);
		registerUiWidget(ICheckBoxDescriptorUi.class, ICheckBoxUi.class, ICheckBoxBluePrint.class);
		registerUiWidget(IToggleButtonDescriptorUi.class, IToggleButtonUi.class, IToggleButtonBluePrint.class);
		registerUiWidget(ITextLabelDescriptorUi.class, ITextLabelUi.class, ITextLabelBluePrint.class);
		registerUiWidget(IIconDescriptorUi.class, IIconUi.class, IIconBluePrint.class);
		registerUiWidget(ISplitCompositeDescriptorUi.class, ISplitCompositeUi.class, ISplitCompositeBluePrint.class);
		registerUiWidget(IScrollCompositeDescriptorUi.class, IScrollCompositeUi.class, IScrollCompositeBluePrint.class);
		registerUiWidget(ICompositeDescriptorUi.class, ICompositeUi.class, ICompositeBluePrint.class);
		registerUiWidget(IDialogDescriptorUi.class, IFrameUi.class, IFrameBluePrint.class);
		register(IComboBoxSelectionDescriptorUi.class, new ComboBoxSelectionFactory<Object>(this, widgetsServiceProvider, bpF));
		register(ITextFieldDescriptorUi.class, new TextFieldFactory(this, widgetsServiceProvider, bpF));
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void registerUiWidget(final Class uiDescriptorType, final Class uiWidgetType, final Class bluePrintType) {
		register(uiDescriptorType, new UiWidgetFactory(this, uiWidgetType, bluePrintType));
	}

	protected IWidgetFactorySpi getSpiWidgetFactory() {
		return widgetsServiceProvider.getWidgetFactory();
	}

	protected IWidgetsServiceProvider getWidgetsServiceProvider() {
		return widgetsServiceProvider;
	}

}
