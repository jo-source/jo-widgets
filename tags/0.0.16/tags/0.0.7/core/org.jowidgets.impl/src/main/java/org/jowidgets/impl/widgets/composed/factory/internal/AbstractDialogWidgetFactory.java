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
package org.jowidgets.impl.widgets.composed.factory.internal;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.descriptor.setup.ICompositeSetup;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactory;

public abstract class AbstractDialogWidgetFactory<WIDGET_TYPE extends IWidgetCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> implements
		IWidgetFactory<WIDGET_TYPE, DESCRIPTOR_TYPE> {

	private final IGenericWidgetFactory genericWidgetFactory;

	public AbstractDialogWidgetFactory(final IGenericWidgetFactory genericWidgetFactory) {
		super();
		this.genericWidgetFactory = genericWidgetFactory;
	}

	protected abstract WIDGET_TYPE createWidget(IFrame dialogWidget, DESCRIPTOR_TYPE descriptor);

	@Override
	public WIDGET_TYPE create(final Object parentUiReference, final DESCRIPTOR_TYPE descriptor) {

		final IFrame dialogWidget = genericWidgetFactory.create(
				parentUiReference,
				Toolkit.getBluePrintFactory().dialog().setSetup(descriptor));

		if (dialogWidget == null) {
			throw new IllegalStateException("Could not create widget with descriptor interface class '"
				+ ICompositeSetup.class
				+ "' from '"
				+ IGenericWidgetFactory.class.getName()
				+ "'");
		}

		return createWidget(dialogWidget, descriptor);
	}
}
