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
package org.jowidgets.common.widgets.factory;

import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.util.IDecorator;

public interface IGenericWidgetFactory {

    /**
     * Creates a root widget (with no parent) for a given descriptor
     * 
     * @param descriptor The descriptor of the widget
     * 
     * @return A new widget, never null
     */
    <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> WIDGET_TYPE create(
        DESCRIPTOR_TYPE descriptor);

    /**
     * Creates a widget for a given descriptor
     * 
     * @param parentUiReference The native ui reference of the parent widget
     * @param descriptor The descriptor of the widget
     * 
     * @return A new widget, never null
     */
    <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> WIDGET_TYPE create(
        Object parentUiReference,
        DESCRIPTOR_TYPE descriptor);

    /**
     * Gets a factory for a specific descriptor type
     * 
     * @param descriptorClass The type to get the factory for
     * 
     * @return The factory for the given type if registered, null otherwise
     */
    <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> IWidgetFactory<WIDGET_TYPE, DESCRIPTOR_TYPE> getFactory(
        Class<? extends DESCRIPTOR_TYPE> descriptorClass);

    /**
     * Registers a factory for a given descriptor type
     * 
     * The widget for the type must not already be registered
     * 
     * @param descriptorClass The type to register the factory for
     * @param widgetFactory The widget factory to register
     * 
     * @throws IllegalArgumentException if already a widget is registered for the given type, use {@link #unRegister(Class)}
     *             explicitly in the case that widget should be replaced
     */
    <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> void register(
        Class<? extends DESCRIPTOR_TYPE> descriptorClass,
        IWidgetFactory<WIDGET_TYPE, ? extends DESCRIPTOR_TYPE> widgetFactory);

    /**
     * Unregisters a factory for a given descriptor type
     * 
     * @param descriptorClass The type to unregister the factory for
     */
    <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> void unRegister(
        Class<? extends DESCRIPTOR_TYPE> descriptorClass);

    /**
     * Adds a widget decorator
     * 
     * The decorators {@link IDecorator#decorate(Object)} will be invoked for each created widget and the decorated widget will be
     * returned
     * 
     * @param descriptorClass The type to add the decorator for
     * @param decorator The decorator to add
     */
    <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> void addWidgetDecorator(
        Class<? extends DESCRIPTOR_TYPE> descriptorClass,
        IDecorator<? extends WIDGET_TYPE> decorator);

    /**
     * Adds a widget factory decorator
     * 
     * The decorators {@link IDecorator#decorate(Object)} will be invoked for the factory with the given type and the decorated
     * factory will be used to create widgets with the given type
     * 
     * @param descriptorClass The type to add the decorator for
     * @param decorator The decorator to add
     */
    <WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> void addWidgetFactoryDecorator(
        Class<? extends DESCRIPTOR_TYPE> descriptorClass,
        IDecorator<? extends IWidgetFactory<WIDGET_TYPE, ? extends DESCRIPTOR_TYPE>> decorator);

    /**
     * Adds a widget factory listener
     * 
     * @param listener The listener to add
     */
    void addWidgetFactoryListener(IWidgetFactoryListener listener);

    /**
     * Removes a widget factory listener
     * 
     * @param listener The listener to remove
     */
    void removeWidgetFactoryListener(IWidgetFactoryListener listener);

}
