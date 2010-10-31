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

import org.jowidgets.common.widgets.IWidget;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactoryListener;
import org.jowidgets.util.Assert;

public class GenericWidgetFactoryWrapper implements IGenericWidgetFactory {

	private final IGenericWidgetFactory genericFactory;

	public GenericWidgetFactoryWrapper(final IGenericWidgetFactory genericWidgetFactory) {
		Assert.paramNotNull(genericWidgetFactory, "genericWidgetFactory");

		this.genericFactory = genericWidgetFactory;
	}

	@Override
	public <WIDGET_TYPE extends IWidget, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE create(
		final DESCRIPTOR_TYPE descriptor) {
		return genericFactory.create(descriptor);
	}

	@Override
	public <WIDGET_TYPE extends IWidget, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE create(
		final IWidget parent,
		final DESCRIPTOR_TYPE descriptor) {
		return genericFactory.create(parent, descriptor);
	}

	@Override
	public final <WIDGET_TYPE extends IWidget, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> IWidgetFactory<WIDGET_TYPE, DESCRIPTOR_TYPE> getFactory(
		final Class<? extends DESCRIPTOR_TYPE> descriptorClass) {
		return genericFactory.getFactory(descriptorClass);
	}

	@Override
	public final <WIDGET_TYPE extends IWidget, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> void register(
		final Class<? extends DESCRIPTOR_TYPE> descriptorClass,
		final IWidgetFactory<WIDGET_TYPE, ? extends DESCRIPTOR_TYPE> widgetFactory) {
		genericFactory.register(descriptorClass, widgetFactory);
	}

	@Override
	public final <WIDGET_TYPE extends IWidget, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> void unRegister(
		final Class<? extends DESCRIPTOR_TYPE> descriptorClass) {
		genericFactory.unRegister(descriptorClass);
	}

	@Override
	public void addWidgetFactoryListener(final IWidgetFactoryListener widgetFactoryListener) {
		genericFactory.addWidgetFactoryListener(widgetFactoryListener);
	}

	@Override
	public void removeWidgetFactoryListener(final IWidgetFactoryListener widgetFactoryListener) {
		genericFactory.removeWidgetFactoryListener(widgetFactoryListener);
	}

}
