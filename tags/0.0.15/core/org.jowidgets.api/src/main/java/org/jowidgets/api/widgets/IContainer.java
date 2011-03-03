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

import java.util.List;

import org.jowidgets.common.widgets.IContainerCommon;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;

public interface IContainer extends IComponent, IContainerCommon {

	/**
	 * Gets the containers children
	 * 
	 * @return a defensive copy of the containers children
	 */
	List<IControl> getChildren();

	/**
	 * Allows to remove a control from an container.
	 * Removing controls should be nested with layoutBegin() - layoutEnd() to ensure
	 * that the container revalidates its layout.
	 * 
	 * A further using / access of removed controls is not indented and may lead to arbitrary behavior
	 * 
	 * @param control The control to remove
	 * @return true if the widget could be removed, false otherwise
	 */
	boolean remove(IControl control);

	/**
	 * Creates and adds an control to this container.
	 * 
	 * @param <WIDGET_TYPE> The type of the widget that is created, added and returned
	 * @param descriptor The descriptor that describes the control to add
	 * @param layoutConstraints The layout constraints / data for the added control
	 * @return the created and added control
	 */
	<WIDGET_TYPE extends IControl> WIDGET_TYPE add(IWidgetDescriptor<? extends WIDGET_TYPE> descriptor, Object layoutConstraints);

	/**
	 * Creates and adds an control to this container
	 * 
	 * @param <WIDGET_TYPE> The type of the widget that is created, added and returned
	 * @param factory The factory that can create the control
	 * @param layoutConstraints The layout constraints / data for the added control
	 * @return the created and added control
	 */
	<WIDGET_TYPE extends IControl> WIDGET_TYPE add(ICustomWidgetFactory<WIDGET_TYPE> factory, Object layoutConstraints);

}
