/*
 * Copyright (c) 2011, Michael Grossmann, Lukas Gross
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

package org.jowidgets.impl.widgets.basic.factory.internal;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.blueprint.builder.IComponentSetupBuilder;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactory;
import org.jowidgets.test.api.widgets.IWidgetUi;

@SuppressWarnings("rawtypes")
public class UiWidgetFactory<WIDGET_TYPE extends IWidget, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> implements
		IWidgetFactory<WIDGET_TYPE, DESCRIPTOR_TYPE> {

	private final IGenericWidgetFactory genericWidgetFactory;
	private final Class<?> uiWidgetType;
	private final Class bluePrintType;
	private final Class descriptorType;

	public UiWidgetFactory(
		final IGenericWidgetFactory genericWidgetFactory,
		final Class<?> uiWidgetType,
		final Class bluePrintType,
		final Class descriptorType) {

		this.genericWidgetFactory = genericWidgetFactory;
		this.uiWidgetType = uiWidgetType;
		this.bluePrintType = bluePrintType;
		this.descriptorType = descriptorType;
	}

	// TODO LG better exception descriptions
	@SuppressWarnings("unchecked")
	@Override
	public WIDGET_TYPE create(final Object parentUiReference, final DESCRIPTOR_TYPE descriptor) {
		final IBluePrintFactory factory = Toolkit.getBluePrintFactory();

		// Don't remove cast from Object to IComponentSetupBuilder, its necessary for sun compiler. 
		// Its not possible to set this cast before factory.bluePrint(...) 
		// because save action of eclipse think its a unnecessary cast and will remove it.
		final Object obj = factory.bluePrint(bluePrintType, descriptorType);
		final IComponentSetupBuilder<?> bluePrint = (IComponentSetupBuilder<?>) obj;
		final IWidgetDescriptor<?> uiBluePrint = (IWidgetDescriptor<?>) bluePrint.setSetup(descriptor);

		final Object result = genericWidgetFactory.create(parentUiReference, uiBluePrint);

		if (result != null) {
			if (uiWidgetType.isAssignableFrom(result.getClass())) {

				if (result instanceof IWidgetUi) {
					if (((IWidgetUi) result).isTestable()) {
						return (WIDGET_TYPE) result;
					}
					else {
						throw new IllegalStateException("The created Widget is not testable.");
					}
				}
				else {
					throw new IllegalStateException("The created Widget is a unknown UiWidget.");
				}

			}
			else {
				throw new IllegalStateException("The created Widget is no UiWidget.");
			}
		}
		else {
			throw new IllegalStateException("Error while creating Widget.");
		}
	}

}
