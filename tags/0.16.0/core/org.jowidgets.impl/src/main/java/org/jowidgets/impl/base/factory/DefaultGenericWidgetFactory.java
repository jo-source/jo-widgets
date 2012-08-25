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
package org.jowidgets.impl.base.factory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactoryListener;
import org.jowidgets.util.Assert;
import org.jowidgets.util.IDecorator;

public final class DefaultGenericWidgetFactory implements IGenericWidgetFactory {

	@SuppressWarnings("rawtypes")
	private final Map factories;

	@SuppressWarnings("rawtypes")
	private final Map decorators;

	@SuppressWarnings("rawtypes")
	private final Map factoryDecorators;

	private final Set<IWidgetFactoryListener> widgetFactoryListeners;

	@SuppressWarnings("rawtypes")
	public DefaultGenericWidgetFactory() {
		this.factories = new HashMap();
		this.decorators = new HashMap();
		this.factoryDecorators = new HashMap();
		this.widgetFactoryListeners = new HashSet<IWidgetFactoryListener>();
	}

	@Override
	public void addWidgetFactoryListener(final IWidgetFactoryListener widgetFactoryListener) {
		this.widgetFactoryListeners.add(widgetFactoryListener);
	}

	@Override
	public void removeWidgetFactoryListener(final IWidgetFactoryListener widgetFactoryListener) {
		this.widgetFactoryListeners.remove(widgetFactoryListener);
	}

	@Override
	public <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE create(
		final DESCRIPTOR_TYPE descriptor) {
		return create(null, descriptor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE create(
		final Object parentUiReference,
		final DESCRIPTOR_TYPE descriptor) {
		return (WIDGET_TYPE) createWidget(parentUiReference, descriptor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> void register(
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
	public <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> void unRegister(
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
	public <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> IWidgetFactory<WIDGET_TYPE, DESCRIPTOR_TYPE> getFactory(
		final Class<? extends DESCRIPTOR_TYPE> descriptorClass) {
		return decorateFactory((IWidgetFactory<WIDGET_TYPE, DESCRIPTOR_TYPE>) factories.get(descriptorClass), descriptorClass);
	}

	@SuppressWarnings("unchecked")
	private <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> IWidgetFactory<WIDGET_TYPE, DESCRIPTOR_TYPE> decorateFactory(
		final IWidgetFactory<WIDGET_TYPE, DESCRIPTOR_TYPE> widgetFactory,
		final Class<? extends DESCRIPTOR_TYPE> descriptorClass) {
		IWidgetFactory<WIDGET_TYPE, DESCRIPTOR_TYPE> result = widgetFactory;
		final List<IDecorator<Object>> decoratorsList = (List<IDecorator<Object>>) factoryDecorators.get(descriptorClass);
		if (decoratorsList != null) {
			for (final IDecorator<Object> decorator : decoratorsList) {
				final Object decorated = decorator.decorate(result);
				if (decorated instanceof IWidgetFactory) {
					result = (IWidgetFactory<WIDGET_TYPE, DESCRIPTOR_TYPE>) decorated;
				}
				else {
					throw new IllegalStateException("Decorator must return an instance of '"
						+ IWidgetFactory.class.getName()
						+ "'");
				}
			}
		}
		return result;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> void addWidgetDecorator(
		final Class<? extends DESCRIPTOR_TYPE> descriptorClass,
		final IDecorator<WIDGET_TYPE> decorator) {
		Assert.paramNotNull(descriptorClass, "descriptorClass");
		Assert.paramNotNull(decorator, "decorator");

		List decoratorsList = (List) decorators.get(descriptorClass);
		if (decoratorsList == null) {
			decoratorsList = new LinkedList();
			decorators.put(descriptorClass, decoratorsList);
		}
		decoratorsList.add(decorator);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> void addWidgetFactoryDecorator(
		final Class<? extends DESCRIPTOR_TYPE> descriptorClass,
		final IDecorator<IWidgetFactory<WIDGET_TYPE, ? extends DESCRIPTOR_TYPE>> decorator) {
		Assert.paramNotNull(descriptorClass, "descriptorClass");
		Assert.paramNotNull(decorator, "decorator");
		List factoryDecoratorsList = (List) factoryDecorators.get(descriptorClass);
		if (factoryDecoratorsList == null) {
			factoryDecoratorsList = new LinkedList();
			factoryDecorators.put(descriptorClass, factoryDecoratorsList);
		}
		factoryDecoratorsList.add(decorator);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private Object createWidget(final Object parentUiReference, final IWidgetDescriptor descriptor) {
		Assert.paramNotNull(descriptor, "descriptor");

		final IWidgetFactory factory = getFactory(descriptor.getDescriptorInterface());
		if (factory != null) {
			Object result = factory.create(parentUiReference, descriptor);
			if (result instanceof IWidgetCommon) {
				result = decorateWidget((IWidgetCommon) result, descriptor);
				fireWidgetCreated((IWidgetCommon) result);
			}
			else {
				throw new IllegalStateException("Created widget must be assignable from '" + IWidgetCommon.class.getName() + "'");
			}
			return result;
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
	private IWidgetCommon decorateWidget(final IWidgetCommon widget, final IWidgetDescriptor descriptor) {
		IWidgetCommon result = widget;
		final List<IDecorator<Object>> decoratorsList = (List<IDecorator<Object>>) decorators.get(descriptor.getDescriptorInterface());
		if (decoratorsList != null) {
			for (final IDecorator<Object> decorator : decoratorsList) {
				final Object decorated = decorator.decorate(result);
				if (decorated instanceof IWidgetCommon) {
					result = (IWidgetCommon) decorated;
				}
				else {
					throw new IllegalStateException("Decorated widget must be assignable from '"
						+ IWidgetCommon.class.getName()
						+ "'");
				}
			}
		}
		return result;
	}

	private void fireWidgetCreated(final IWidgetCommon widget) {
		Assert.paramNotNull(widget, "widget");
		for (final IWidgetFactoryListener listener : widgetFactoryListeners) {
			listener.widgetCreated(widget);
		}
	}

}
