/*
 * Copyright (c) 2010, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
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

package org.jowidgets.api.widgets;

import java.util.Collection;
import java.util.List;

import org.jowidgets.api.controller.IContainerObservable;
import org.jowidgets.api.controller.IContainerRegistry;
import org.jowidgets.api.controller.IRecursiveContainerObservable;
import org.jowidgets.api.layout.ILayoutFactory;
import org.jowidgets.common.widgets.IContainerCommon;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.layout.ILayouter;

public interface IContainer extends IComponent, IContainerObservable, IRecursiveContainerObservable, IContainerCommon {

	/**
	 * Sets the layouter for this container defined by a layout factory
	 * 
	 * @param layoutFactory The layout to set
	 * 
	 * @return The layouter created from the factory
	 */
	<LAYOUT_TYPE extends ILayouter> LAYOUT_TYPE setLayout(ILayoutFactory<LAYOUT_TYPE> layoutFactory);

	/**
	 * Layouts the container
	 */
	void layout();

	/**
	 * Layouts the container later in the event queue.
	 * If this method will be invoked more than once before the layout
	 * occurs, only one layout invocation will be done on this container.
	 */
	void layoutLater();

	/**
	 * Adds a container registry recursively.
	 * 
	 * With help of a container registry its is possible to get an access to all children
	 * of the container currently existing and all eventually added or removed children
	 * in the future
	 * 
	 * @param registry The registry to add
	 */
	void addContainerRegistry(IContainerRegistry registry);

	/**
	 * Removes a container registry
	 * 
	 * @param registry The registry to remove
	 */
	void removeContainerRegistry(IContainerRegistry registry);

	/**
	 * Gets the containers children
	 * 
	 * @return a unmodifiable copy of the containers children
	 */
	List<IControl> getChildren();

	/**
	 * Removes a control from this container.
	 * 
	 * Removing controls should be nested with layoutBegin() - layoutEnd() to ensure
	 * that the container revalidates its layout.
	 * 
	 * The removed controls will be disposed an can no longer be used
	 * 
	 * @param control The control to remove
	 * 
	 * @return true if the widget was removed, false otherwise
	 */
	boolean remove(IControl control);

	/**
	 * Creates and adds an control to this container.
	 * 
	 * @param <WIDGET_TYPE> The type of the widget that is created, added and returned
	 * @param index The index at which the control should be added in the container
	 * @param descriptor The descriptor that describes the control to add
	 * @param layoutConstraints The layout constraints / data for the added control
	 * 
	 * @return the created and added control
	 */
	<WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		int index,
		IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		Object layoutConstraints);

	/**
	 * Creates and adds an control to this container.
	 * 
	 * @param <WIDGET_TYPE> The type of the widget that is created, added and returned
	 * @param index The index at which the control should be added in the container
	 * @param descriptor The descriptor that describes the control to add
	 * 
	 * @return the created and added control
	 */
	<WIDGET_TYPE extends IControl> WIDGET_TYPE add(int index, IWidgetDescriptor<? extends WIDGET_TYPE> descriptor);

	/**
	 * Creates and adds an control to this container
	 * 
	 * @param <WIDGET_TYPE> The type of the widget that is created, added and returned
	 * @param index The index at which the control should be added in the container
	 * @param creator The creator that creates the control
	 * @param layoutConstraints The layout constraints / data for the added control
	 * 
	 * @return the created and added control
	 */
	<WIDGET_TYPE extends IControl> WIDGET_TYPE add(int index, ICustomWidgetCreator<WIDGET_TYPE> creator, Object layoutConstraints);

	/**
	 * Creates and adds an control to this container
	 * 
	 * @param <WIDGET_TYPE> The type of the widget that is created, added and returned
	 * @param index The index at which the control should be added in the container
	 * @param creator The creator that creates the control
	 * 
	 * @return the created and added control
	 */
	<WIDGET_TYPE extends IControl> WIDGET_TYPE add(int index, ICustomWidgetCreator<WIDGET_TYPE> creator);

	/**
	 * Creates and adds an control to this container.
	 * 
	 * @param <WIDGET_TYPE> The type of the widget that is created, added and returned
	 * @param descriptor The descriptor that describes the control to add
	 * @param layoutConstraints The layout constraints / data for the added control
	 * 
	 * @return the created and added control
	 */
	<WIDGET_TYPE extends IControl> WIDGET_TYPE add(IWidgetDescriptor<? extends WIDGET_TYPE> descriptor, Object layoutConstraints);

	/**
	 * Creates and adds an control to this container
	 * 
	 * @param <WIDGET_TYPE> The type of the widget that is created, added and returned
	 * @param creator The creator that creates the control
	 * @param layoutConstraints The layout constraints / data for the added control
	 * 
	 * @return the created and added control
	 */
	<WIDGET_TYPE extends IControl> WIDGET_TYPE add(ICustomWidgetCreator<WIDGET_TYPE> creator, Object layoutConstraints);

	/**
	 * Creates and adds an control to this container.
	 * 
	 * @param <WIDGET_TYPE> The type of the widget that is created, added and returned
	 * @param descriptor The descriptor that describes the control to add
	 * 
	 * @return the created and added control
	 */
	<WIDGET_TYPE extends IControl> WIDGET_TYPE add(IWidgetDescriptor<? extends WIDGET_TYPE> descriptor);

	/**
	 * Creates and adds an control to this container
	 * 
	 * @param <WIDGET_TYPE> The type of the widget that is created, added and returned
	 * @param creator The creator that creates the control
	 * 
	 * @return the created and added control
	 */
	<WIDGET_TYPE extends IControl> WIDGET_TYPE add(ICustomWidgetCreator<WIDGET_TYPE> creator);

	/**
	 * Sets the tab order of the container's controls.
	 * 
	 * @param tabOrder the tab order to set or null to set the default tab behavior.
	 */
	void setTabOrder(Collection<? extends IControl> tabOrder);

	/**
	 * Sets the tab order of the container's controls.
	 * 
	 * @param tabOrder the tab order to set or null to set the default tab behavior.
	 */
	void setTabOrder(IControl... controls);

}
