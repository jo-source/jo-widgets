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

package org.jowidgets.tools.powo;

import org.jowidgets.api.widgets.descriptor.ICompositeDescriptor;
import org.jowidgets.api.widgets.descriptor.ITextLabelDescriptor;
import org.jowidgets.common.widgets.IWidget;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.util.Assert;

class WidgetFactory {

	@SuppressWarnings("unchecked")
	<WIDGET_TYPE extends IWidget, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> Widget<WIDGET_TYPE, ?> create(
		final DESCRIPTOR_TYPE descriptor) {

		Assert.paramNotNull(descriptor, "descriptor");

		if (descriptor instanceof ICompositeDescriptor) {
			return (Widget<WIDGET_TYPE, ?>) new JoComposite((ICompositeDescriptor) descriptor);
		}
		else if (descriptor instanceof ITextLabelDescriptor) {
			return (Widget<WIDGET_TYPE, ?>) new JoTextLabel((ITextLabelDescriptor) descriptor);
		}
		else {
			throw new IllegalArgumentException("Could not create a Widget from descriptor '"
				+ descriptor.getDescriptorInterface().getName()
				+ "' Descriptor is not yet supported");
		}
	}

}
