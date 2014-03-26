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
package org.jowidgets.api.widgets.content;

import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.validation.IValidateable;

public interface IInputContentContainer extends IContainer {

	void register(String validationContext, IValidateable validateable);

	void unregister(String validationContext, IValidateable validateable);

	/**
	 * Creates and adds an control to this container.
	 * 
	 * @param <WIDGET_TYPE> The type of the widget that is created, added and returned
	 * @param validationContext The validation context that should be used for validation
	 * @param index The index at which the control should be added in the container
	 * @param descriptor The descriptor that describes the control to add
	 * @param layoutConstraints The layout constraints / data for the added control
	 * 
	 * @return the created and added control
	 */
	<WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		String validationContext,
		int index,
		IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		Object layoutConstraints);

	/**
	 * Creates and adds an control to this container
	 * 
	 * @param <WIDGET_TYPE> The type of the widget that is created, added and returned
	 * @param validationContext The validation context that should be used for validation
	 * @param index The index at which the control should be added in the container
	 * @param creator The creator that creates the control
	 * @param layoutConstraints The layout constraints / data for the added control
	 * 
	 * @return the created and added control
	 */
	<WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		String validationContext,
		int index,
		ICustomWidgetCreator<WIDGET_TYPE> creator,
		Object layoutConstraints);

	/**
	 * Creates and adds an control to this container.
	 * 
	 * @param <WIDGET_TYPE> The type of the widget that is created, added and returned
	 * @param validationContext The validation context that should be used for validation
	 * @param descriptor The descriptor that describes the control to add
	 * @param layoutConstraints The layout constraints / data for the added control
	 * @return the created and added control
	 */
	<WIDGET_TYPE extends IInputControl<?>> WIDGET_TYPE add(
		String validationContext,
		IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		Object layoutConstraints);

	/**
	 * Creates and adds an control to this container
	 * 
	 * @param <WIDGET_TYPE> The type of the widget that is created, added and returned
	 * @param validationContext The validation context that should be used for validation
	 * @param creator The creator that creates the control
	 * @param layoutConstraints The layout constraints / data for the added control
	 * @return the created and added control
	 */
	<WIDGET_TYPE extends IInputControl<?>> WIDGET_TYPE add(
		String validationContext,
		ICustomWidgetCreator<WIDGET_TYPE> creator,
		Object layoutConstraints);

	/**
	 * Creates and adds an control to this container.
	 * 
	 * @param <WIDGET_TYPE> The type of the widget that is created, added and returned
	 * @param validationContext The validation context that should be used for validation
	 * @param descriptor The descriptor that describes the control to add
	 * @return the created and added control
	 */
	<WIDGET_TYPE extends IInputControl<?>> WIDGET_TYPE add(
		String validationContext,
		IWidgetDescriptor<? extends WIDGET_TYPE> descriptor);

	/**
	 * Creates and adds an control to this container
	 * 
	 * @param <WIDGET_TYPE> The type of the widget that is created, added and returned
	 * @param validationContext The validation context that should be used for validation
	 * @param creator The creator that creates the control
	 * @return the created and added control
	 */
	<WIDGET_TYPE extends IInputControl<?>> WIDGET_TYPE add(String validationContext, ICustomWidgetCreator<WIDGET_TYPE> creator);

}
