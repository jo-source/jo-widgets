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
package org.jowidgets.impl.widgets.basic.factory;

import org.jowidgets.api.widgets.IButtonWidget;
import org.jowidgets.api.widgets.IChildWidget;
import org.jowidgets.api.widgets.IComboBoxWidget;
import org.jowidgets.api.widgets.ICompositeWidget;
import org.jowidgets.api.widgets.IDialogWidget;
import org.jowidgets.api.widgets.IFrameWidget;
import org.jowidgets.api.widgets.IIconWidget;
import org.jowidgets.api.widgets.IInputWidget;
import org.jowidgets.api.widgets.IScrollCompositeWidget;
import org.jowidgets.api.widgets.ITextLabelWidget;
import org.jowidgets.api.widgets.IToggleButtonWidget;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.descriptor.IButtonDescriptor;
import org.jowidgets.api.widgets.descriptor.ICheckBoxDescriptor;
import org.jowidgets.api.widgets.descriptor.IComboBoxDescriptor;
import org.jowidgets.api.widgets.descriptor.IComboBoxSelectionDescriptor;
import org.jowidgets.api.widgets.descriptor.ICompositeDescriptor;
import org.jowidgets.api.widgets.descriptor.IDialogDescriptor;
import org.jowidgets.api.widgets.descriptor.IFrameDescriptor;
import org.jowidgets.api.widgets.descriptor.IIconDescriptor;
import org.jowidgets.api.widgets.descriptor.IScrollCompositeDescriptor;
import org.jowidgets.api.widgets.descriptor.ISeparatorDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextFieldDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextLabelDescriptor;
import org.jowidgets.api.widgets.descriptor.IToggleButtonDescriptor;
import org.jowidgets.api.widgets.factory.IWidgetFactory;
import org.jowidgets.impl.spi.ISpiBluePrintFactory;
import org.jowidgets.impl.spi.SpiBluePrintFactory;
import org.jowidgets.impl.spi.blueprint.IButtonBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ICheckBoxBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IComboBoxBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IComboBoxSelectionBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ICompositeBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IDialogBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IFrameBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IIconBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IScrollCompositeBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ISeparatorBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ITextFieldBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.ITextLabelBluePrintSpi;
import org.jowidgets.impl.spi.blueprint.IToggleButtonBluePrintSpi;
import org.jowidgets.impl.widgets.basic.ButtonWidget;
import org.jowidgets.impl.widgets.basic.ChildWidget;
import org.jowidgets.impl.widgets.basic.ComboBoxWidget;
import org.jowidgets.impl.widgets.basic.CompositeWidget;
import org.jowidgets.impl.widgets.basic.DialogWidget;
import org.jowidgets.impl.widgets.basic.FrameWidget;
import org.jowidgets.impl.widgets.basic.IconWidget;
import org.jowidgets.impl.widgets.basic.InputWidget;
import org.jowidgets.impl.widgets.basic.ScrollCompositeWidget;
import org.jowidgets.impl.widgets.basic.TextLabelWidget;
import org.jowidgets.impl.widgets.basic.ToggleButtonWidget;
import org.jowidgets.impl.widgets.factory.DefaultGenericWidgetFactory;
import org.jowidgets.impl.widgets.factory.GenericWidgetFactoryWrapper;
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

public class BasicGenericWidgetFactory extends GenericWidgetFactoryWrapper {

	public BasicGenericWidgetFactory(final IWidgetFactorySpi spiWidgetFactory) {
		super(new DefaultGenericWidgetFactory(spiWidgetFactory.getImageRegistry()));

		registerBaseWidgets(spiWidgetFactory, new SpiBluePrintFactory());
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void registerBaseWidgets(final IWidgetFactorySpi spiWidgetFactory, final ISpiBluePrintFactory bpF) {

		register(IFrameDescriptor.class, new IWidgetFactory<IFrameWidget, IFrameDescriptor>() {
			@Override
			public IFrameWidget create(final IWidget parent, final IFrameDescriptor descriptor) {
				final IFrameBluePrintSpi bp = bpF.frame().setDescriptor(descriptor);
				final IFrameWidgetSpi frameSpi = spiWidgetFactory.createFrameWidget(BasicGenericWidgetFactory.this, bp);
				return new FrameWidget(frameSpi, descriptor);
			}
		});

		register(IDialogDescriptor.class, new IWidgetFactory<IDialogWidget, IDialogDescriptor>() {
			@Override
			public IDialogWidget create(final IWidget parent, final IDialogDescriptor descriptor) {
				final IDialogBluePrintSpi bp = bpF.dialog().setDescriptor(descriptor);
				final IFrameWidgetSpi frameSpi = spiWidgetFactory.createDialogWidget(BasicGenericWidgetFactory.this, parent, bp);
				return new DialogWidget(parent, frameSpi, descriptor);
			}
		});

		register(ICompositeDescriptor.class, new IWidgetFactory<ICompositeWidget, ICompositeDescriptor>() {
			@Override
			public ICompositeWidget create(final IWidget parent, final ICompositeDescriptor descriptor) {
				final ICompositeBluePrintSpi bp = bpF.composite().setDescriptor(descriptor);
				final IContainerWidgetSpi containerSpi = spiWidgetFactory.createCompositeWidget(
						BasicGenericWidgetFactory.this,
						parent,
						bp);

				return new CompositeWidget(parent, containerSpi);
			}
		});

		register(IScrollCompositeDescriptor.class, new IWidgetFactory<ICompositeWidget, IScrollCompositeDescriptor>() {
			@Override
			public IScrollCompositeWidget create(final IWidget parent, final IScrollCompositeDescriptor descriptor) {
				final IScrollCompositeBluePrintSpi bp = bpF.scrollComposite().setDescriptor(descriptor);
				final IScrollContainerWidgetSpi scrollContainerSpi = spiWidgetFactory.createScrollCompositeWidget(
						BasicGenericWidgetFactory.this,
						parent,
						bp);

				return new ScrollCompositeWidget(parent, scrollContainerSpi);
			}
		});

		register(ITextFieldDescriptor.class, new IWidgetFactory<IInputWidget<String>, ITextFieldDescriptor>() {
			@Override
			public IInputWidget<String> create(final IWidget parent, final ITextFieldDescriptor descriptor) {
				final ITextFieldBluePrintSpi bp = bpF.textField().setDescriptor(descriptor);
				final IInputWidgetSpi<String> textFieldSpi = spiWidgetFactory.createTextFieldWidget(parent, bp);
				final IInputWidget<String> result = new InputWidget(parent, textFieldSpi, descriptor);
				result.addValidator(descriptor.getTextInputValidator());
				return result;
			}
		});

		register(IIconDescriptor.class, new IWidgetFactory<IIconWidget, IIconDescriptor>() {
			@Override
			public IIconWidget create(final IWidget parent, final IIconDescriptor descriptor) {
				final IIconBluePrintSpi bp = bpF.icon().setDescriptor(descriptor);
				final IIconWidgetSpi iconSpi = spiWidgetFactory.createIconWidget(parent, bp);
				return new IconWidget(parent, iconSpi);
			}
		});

		register(ITextLabelDescriptor.class, new IWidgetFactory<ITextLabelWidget, ITextLabelDescriptor>() {
			@Override
			public ITextLabelWidget create(final IWidget parent, final ITextLabelDescriptor descriptor) {
				final ITextLabelBluePrintSpi bp = bpF.textLabel().setDescriptor(descriptor);
				final ITextLabelWidgetSpi textLabelSpi = spiWidgetFactory.createTextLabelWidget(parent, bp);
				return new TextLabelWidget(parent, textLabelSpi);
			}
		});

		register(IButtonDescriptor.class, new IWidgetFactory<IButtonWidget, IButtonDescriptor>() {
			@Override
			public IButtonWidget create(final IWidget parent, final IButtonDescriptor descriptor) {
				final IButtonBluePrintSpi bp = bpF.button().setDescriptor(descriptor);
				final IButtonWidgetSpi buttonWidgetSpi = spiWidgetFactory.createButtonWidget(parent, bp);
				return new ButtonWidget(parent, buttonWidgetSpi);
			}
		});

		register(ISeparatorDescriptor.class, new IWidgetFactory<IChildWidget, ISeparatorDescriptor>() {
			@Override
			public IChildWidget create(final IWidget parent, final ISeparatorDescriptor descriptor) {
				final ISeparatorBluePrintSpi bp = bpF.separator().setDescriptor(descriptor);
				final IWidgetSpi widget = spiWidgetFactory.createSeparatorWidget(parent, bp);
				return new ChildWidget(parent, widget);
			}
		});

		register(IToggleButtonDescriptor.class, new IWidgetFactory<IToggleButtonWidget, IToggleButtonDescriptor>() {
			@Override
			public IToggleButtonWidget create(final IWidget parent, final IToggleButtonDescriptor descriptor) {
				final IToggleButtonBluePrintSpi bp = bpF.toggleButton().setDescriptor(descriptor);
				final IToggleButtonWidgetSpi widget = spiWidgetFactory.createToggleButtonWidget(parent, bp);
				return new ToggleButtonWidget(parent, widget, descriptor);
			}
		});

		register(ICheckBoxDescriptor.class, new IWidgetFactory<IToggleButtonWidget, ICheckBoxDescriptor>() {
			@Override
			public IToggleButtonWidget create(final IWidget parent, final ICheckBoxDescriptor descriptor) {
				final ICheckBoxBluePrintSpi bp = bpF.checkBox().setDescriptor(descriptor);
				final IToggleButtonWidgetSpi widget = spiWidgetFactory.createCheckBoxWidget(parent, bp);
				return new ToggleButtonWidget(parent, widget, descriptor);
			}
		});

		register(IComboBoxSelectionDescriptor.class, new IWidgetFactory<IComboBoxWidget<?>, IComboBoxSelectionDescriptor<?>>() {
			@Override
			public IComboBoxWidget<?> create(final IWidget parent, final IComboBoxSelectionDescriptor descriptor) {
				final IComboBoxSelectionBluePrintSpi bp = bpF.comboBoxSelection().setDescriptor(descriptor);
				final IComboBoxWidgetSpi<?> widget = spiWidgetFactory.createComboBoxSelectionWidget(parent, bp);
				return new ComboBoxWidget(parent, widget, descriptor);
			}
		});

		register(IComboBoxDescriptor.class, new IWidgetFactory<IComboBoxWidget<?>, IComboBoxDescriptor<?>>() {
			@Override
			public IComboBoxWidget<?> create(final IWidget parent, final IComboBoxDescriptor descriptor) {
				final IComboBoxBluePrintSpi bp = bpF.comboBox().setDescriptor(descriptor);
				final IComboBoxWidgetSpi<?> widget = spiWidgetFactory.createComboBoxWidget(parent, bp);
				return new ComboBoxWidget(parent, widget, descriptor);
			}
		});

	}
}
