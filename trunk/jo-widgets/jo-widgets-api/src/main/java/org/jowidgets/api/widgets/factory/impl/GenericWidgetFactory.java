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
package org.jowidgets.api.widgets.factory.impl;

import java.util.HashMap;
import java.util.Map;

import org.jowidgets.api.image.IImageRegistry;
import org.jowidgets.api.image.defaults.Icons;
import org.jowidgets.api.widgets.IButtonWidget;
import org.jowidgets.api.widgets.IChildWidget;
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
import org.jowidgets.api.widgets.IWindowWidget;
import org.jowidgets.api.widgets.descriptor.IButtonDescriptor;
import org.jowidgets.api.widgets.descriptor.ICheckBoxDescriptor;
import org.jowidgets.api.widgets.descriptor.IComboBoxDescriptor;
import org.jowidgets.api.widgets.descriptor.IComboBoxSelectionDescriptor;
import org.jowidgets.api.widgets.descriptor.ICompositeDescriptor;
import org.jowidgets.api.widgets.descriptor.IDialogDescriptor;
import org.jowidgets.api.widgets.descriptor.IIconDescriptor;
import org.jowidgets.api.widgets.descriptor.IInputCompositeDescriptor;
import org.jowidgets.api.widgets.descriptor.IInputDialogDescriptor;
import org.jowidgets.api.widgets.descriptor.IInputFieldDescriptor;
import org.jowidgets.api.widgets.descriptor.ILabelDescriptor;
import org.jowidgets.api.widgets.descriptor.IRootWindowDescriptor;
import org.jowidgets.api.widgets.descriptor.IScrollCompositeDescriptor;
import org.jowidgets.api.widgets.descriptor.ISeparatorDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextFieldDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextLabelDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextSeparatorDescriptor;
import org.jowidgets.api.widgets.descriptor.IToggleButtonDescriptor;
import org.jowidgets.api.widgets.descriptor.IValidationLabelDescriptor;
import org.jowidgets.api.widgets.descriptor.base.IBaseWidgetDescriptor;
import org.jowidgets.api.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.api.widgets.factory.INativeWidgetFactory;
import org.jowidgets.api.widgets.factory.IWidgetFactory;
import org.jowidgets.api.widgets.factory.impl.internal.InputCompositeWidgetFactory;
import org.jowidgets.api.widgets.factory.impl.internal.InputDialogWidgetFactory;
import org.jowidgets.api.widgets.factory.impl.internal.InputFieldWidgetFactory;
import org.jowidgets.api.widgets.factory.impl.internal.LabelWidgetFactory;
import org.jowidgets.api.widgets.factory.impl.internal.TextSeparatorWidgetFactory;
import org.jowidgets.api.widgets.factory.impl.internal.ValidationLabelWidgetFactory;
import org.jowidgets.util.Assert;

public class GenericWidgetFactory implements IGenericWidgetFactory {

	@SuppressWarnings("rawtypes")
	private final Map factories;
	private final INativeWidgetFactory nativeWidgetFactory;
	private final IImageRegistry imageRegistry;

	@SuppressWarnings("rawtypes")
	public GenericWidgetFactory(final INativeWidgetFactory nativeWidgetFactory) {
		Assert.paramNotNull(nativeWidgetFactory, "nativeWidgetFactory");

		this.nativeWidgetFactory = nativeWidgetFactory;
		this.imageRegistry = nativeWidgetFactory.getImageRegistry();
		this.factories = new HashMap();

		imageRegistry.registerImageConstants(Icons.class);

		registerNativeWidgetFactories(nativeWidgetFactory);
		registerCustomWidgetFactories();
	}

	public IImageRegistry getImageRegistry() {
		return imageRegistry;
	}

	@Override
	public IRootWindowWidget createRootWindow(final IRootWindowDescriptor descriptor) {
		return nativeWidgetFactory.createRootWindowWidget(this, descriptor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <WIDGET_TYPE extends IChildWidget, DESCRIPTOR_TYPE extends IBaseWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE create(
		final IWidget parent,
		final DESCRIPTOR_TYPE descriptor) {
		return (WIDGET_TYPE) createWidget(parent, descriptor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <WIDGET_TYPE extends IWindowWidget, DESCRIPTOR_TYPE extends IBaseWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE createChildWindow(
		final IWindowWidget parent,
		final DESCRIPTOR_TYPE descriptor) {
		return (WIDGET_TYPE) createWidget(parent, descriptor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <WIDGET_TYPE extends IChildWidget, DESCRIPTOR_TYPE extends IBaseWidgetDescriptor<? extends WIDGET_TYPE>> void register(
		final Class<? extends DESCRIPTOR_TYPE> descriptorClass,
		final IWidgetFactory<WIDGET_TYPE, ? extends DESCRIPTOR_TYPE> widgetFactory) {

		Assert.paramNotNull(descriptorClass, "descriptorClass");
		Assert.paramNotNull(widgetFactory, "widgetFactory");

		if (factories.containsKey(descriptorClass)) {
			throw new IllegalArgumentException("There was already registered a factory for the descriptor '"
				+ descriptorClass
				+ "' get name.");
		}

		factories.put(descriptorClass, widgetFactory);
	}

	@Override
	public <WIDGET_TYPE extends IChildWidget, DESCRIPTOR_TYPE extends IBaseWidgetDescriptor<? extends WIDGET_TYPE>> void unRegister(
		final Class<? extends DESCRIPTOR_TYPE> descriptorClass) {
		Assert.paramNotNull(descriptorClass, "descriptorClass");

		if (factories.containsKey(descriptorClass)) {
			factories.remove(descriptorClass);
		}
		else {
			throw new IllegalArgumentException("There was already registered a factory for the descriptor '"
				+ descriptorClass
				+ "' get name.");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <WIDGET_TYPE extends IChildWidget, DESCRIPTOR_TYPE extends IBaseWidgetDescriptor<? extends WIDGET_TYPE>> IWidgetFactory<WIDGET_TYPE, DESCRIPTOR_TYPE> getFactory(
		final Class<? extends DESCRIPTOR_TYPE> descriptorClass) {
		return (IWidgetFactory<WIDGET_TYPE, DESCRIPTOR_TYPE>) factories.get(descriptorClass);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private Object createWidget(final IWidget parent, final IBaseWidgetDescriptor descriptor) {
		Assert.paramNotNull(parent, "parent");
		Assert.paramNotNull(descriptor, "descriptor");

		final IWidgetFactory factory = (IWidgetFactory) factories.get(descriptor.getDescriptorInterface());
		if (factory != null) {
			return factory.create(parent, descriptor);
		}
		else {
			throw new IllegalArgumentException("No factory found for descriptor interface'"
				+ descriptor.getDescriptorInterface()
				+ "  / implClass:'"
				+ descriptor.getClass().getName()
				+ "'");
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void registerNativeWidgetFactories(final INativeWidgetFactory nativeWidgetFactory) {

		register(IDialogDescriptor.class, new IWidgetFactory<IDialogWidget, IDialogDescriptor>() {
			@Override
			public IDialogWidget create(final IWidget parent, final IDialogDescriptor descriptor) {
				return nativeWidgetFactory.createDialogWidget(GenericWidgetFactory.this, parent, descriptor);
			}
		});

		register(ICompositeDescriptor.class, new IWidgetFactory<ICompositeWidget, ICompositeDescriptor>() {
			@Override
			public ICompositeWidget create(final IWidget parent, final ICompositeDescriptor descriptor) {
				return nativeWidgetFactory.createCompositeWidget(GenericWidgetFactory.this, parent, descriptor);
			}
		});

		register(IScrollCompositeDescriptor.class, new IWidgetFactory<IScrollCompositeWidget, IScrollCompositeDescriptor>() {
			@Override
			public IScrollCompositeWidget create(final IWidget parent, final IScrollCompositeDescriptor descriptor) {
				return nativeWidgetFactory.createScrollPaneWidget(GenericWidgetFactory.this, parent, descriptor);
			}
		});

		register(ITextFieldDescriptor.class, new IWidgetFactory<IInputWidget<String>, ITextFieldDescriptor>() {
			@Override
			public IInputWidget<String> create(final IWidget parent, final ITextFieldDescriptor descriptor) {
				return nativeWidgetFactory.createTextFieldWidget(parent, descriptor);
			}
		});

		register(IIconDescriptor.class, new IWidgetFactory<IIconWidget, IIconDescriptor>() {
			@Override
			public IIconWidget create(final IWidget parent, final IIconDescriptor descriptor) {
				return nativeWidgetFactory.createIconWidget(parent, descriptor);
			}
		});

		register(ITextLabelDescriptor.class, new IWidgetFactory<ITextLabelWidget, ITextLabelDescriptor>() {
			@Override
			public ITextLabelWidget create(final IWidget parent, final ITextLabelDescriptor descriptor) {
				return nativeWidgetFactory.createTextLabelWidget(parent, descriptor);
			}
		});

		register(IButtonDescriptor.class, new IWidgetFactory<IButtonWidget, IButtonDescriptor>() {
			@Override
			public IButtonWidget create(final IWidget parent, final IButtonDescriptor descriptor) {
				return nativeWidgetFactory.createButtonWidget(parent, descriptor);
			}
		});

		register(ISeparatorDescriptor.class, new IWidgetFactory<ISeparatorWidget, ISeparatorDescriptor>() {
			@Override
			public ISeparatorWidget create(final IWidget parent, final ISeparatorDescriptor descriptor) {
				return nativeWidgetFactory.createSeparatorWidget(parent, descriptor);
			}
		});

		register(ICheckBoxDescriptor.class, new IWidgetFactory<IToggleButtonWidget, ICheckBoxDescriptor>() {
			@Override
			public IToggleButtonWidget create(final IWidget parent, final ICheckBoxDescriptor descriptor) {
				return nativeWidgetFactory.createCheckBoxWidget(parent, descriptor);
			}
		});

		register(IToggleButtonDescriptor.class, new IWidgetFactory<IToggleButtonWidget, IToggleButtonDescriptor>() {
			@Override
			public IToggleButtonWidget create(final IWidget parent, final IToggleButtonDescriptor descriptor) {
				return nativeWidgetFactory.createToggleButtonWidget(parent, descriptor);
			}
		});

		register(IComboBoxSelectionDescriptor.class, new IWidgetFactory<IComboBoxWidget<?>, IComboBoxSelectionDescriptor<?>>() {
			@Override
			public IComboBoxWidget create(final IWidget parent, final IComboBoxSelectionDescriptor descriptor) {
				return nativeWidgetFactory.createComboBoxSelectionWidget(parent, descriptor);
			}
		});

		register(IComboBoxDescriptor.class, new IWidgetFactory<IComboBoxWidget<?>, IComboBoxDescriptor<?>>() {
			@Override
			public IComboBoxWidget create(final IWidget parent, final IComboBoxDescriptor descriptor) {
				return nativeWidgetFactory.createComboBoxWidget(parent, descriptor);
			}
		});
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void registerCustomWidgetFactories() {
		register(IInputFieldDescriptor.class, new InputFieldWidgetFactory(nativeWidgetFactory));

		register(ILabelDescriptor.class, new LabelWidgetFactory(this, nativeWidgetFactory));

		register(ITextSeparatorDescriptor.class, new TextSeparatorWidgetFactory(this, nativeWidgetFactory));

		register(IInputDialogDescriptor.class, new InputDialogWidgetFactory(this, nativeWidgetFactory));

		register(IInputCompositeDescriptor.class, new InputCompositeWidgetFactory(this, nativeWidgetFactory));

		register(IValidationLabelDescriptor.class, new ValidationLabelWidgetFactory(this));
	}

}
