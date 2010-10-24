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
import java.util.Map;

import org.jowidgets.common.widgets.IWidget;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactory;
import org.jowidgets.util.Assert;

public final class DefaultGenericWidgetFactory implements IGenericWidgetFactory {

	@SuppressWarnings("rawtypes")
	private final Map factories;

	@SuppressWarnings("rawtypes")
	public DefaultGenericWidgetFactory() {
		this.factories = new HashMap();
	}

	@Override
	public <WIDGET_TYPE extends IWidget, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE create(
		final DESCRIPTOR_TYPE descriptor) {
		return create(null, descriptor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <WIDGET_TYPE extends IWidget, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE create(
		final IWidget parent,
		final DESCRIPTOR_TYPE descriptor) {
		return (WIDGET_TYPE) createWidget(parent, descriptor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <WIDGET_TYPE extends IWidget, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> void register(
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
	public <WIDGET_TYPE extends IWidget, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> void unRegister(
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
	public <WIDGET_TYPE extends IWidget, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> IWidgetFactory<WIDGET_TYPE, DESCRIPTOR_TYPE> getFactory(
		final Class<? extends DESCRIPTOR_TYPE> descriptorClass) {
		return (IWidgetFactory<WIDGET_TYPE, DESCRIPTOR_TYPE>) factories.get(descriptorClass);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private Object createWidget(final IWidget parent, final IWidgetDescriptor descriptor) {
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

}
