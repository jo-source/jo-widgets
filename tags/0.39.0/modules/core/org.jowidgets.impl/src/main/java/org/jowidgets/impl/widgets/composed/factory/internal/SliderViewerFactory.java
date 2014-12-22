/*
 * Copyright (c) 2014, grossmann
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

import org.jowidgets.api.widgets.ISliderViewer;
import org.jowidgets.api.widgets.blueprint.ISliderBluePrint;
import org.jowidgets.api.widgets.blueprint.ISliderViewerBluePrint;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactory;
import org.jowidgets.impl.widgets.composed.SliderViewerImpl;
import org.jowidgets.tools.widgets.blueprint.BPF;

public class SliderViewerFactory<VALUE_TYPE> implements
		IWidgetFactory<ISliderViewer<VALUE_TYPE>, ISliderViewerBluePrint<VALUE_TYPE>> {

	private final IGenericWidgetFactory widgetFactory;

	public SliderViewerFactory(final IGenericWidgetFactory widgetFactory) {
		super();
		this.widgetFactory = widgetFactory;
	}

	@Override
	public ISliderViewer<VALUE_TYPE> create(final Object parentUiReference, final ISliderViewerBluePrint<VALUE_TYPE> bluePrint) {
		final ISliderBluePrint sliderBluePrint = BPF.slider();

		//avoid that viewer value will be set by setSetup() on wrapper slider with vylue type integer
		final VALUE_TYPE viewerValue = bluePrint.getValue();
		bluePrint.setValue(null);
		sliderBluePrint.setSetup(bluePrint);
		if (viewerValue != null) {
			bluePrint.setValue(viewerValue);
		}
		return new SliderViewerImpl<VALUE_TYPE>(widgetFactory.create(parentUiReference, sliderBluePrint), bluePrint);
	}

}
