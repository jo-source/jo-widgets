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

package org.jowidgets.impl.toolkit;

import org.jowidgets.api.toolkit.IWidgetWrapperFactory;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.blueprint.ICompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.impl.widgets.basic.CompositeWidget;
import org.jowidgets.impl.widgets.basic.FrameWidget;
import org.jowidgets.spi.IWidgetFactorySpi;

public class DefaultWidgetWrapperFactory implements IWidgetWrapperFactory {

	private final IGenericWidgetFactory widgetFactory;
	private final IWidgetFactorySpi widgetFactorySpi;

	public DefaultWidgetWrapperFactory(final IGenericWidgetFactory widgetFactory, final IWidgetFactorySpi widgetFactorySpi) {
		super();
		this.widgetFactory = widgetFactory;
		this.widgetFactorySpi = widgetFactorySpi;
	}

	@Override
	public boolean isConvertibleToFrame(final Object uiReference) {
		return widgetFactorySpi.isConvertibleToFrame(uiReference);
	}

	@Override
	public IFrame createFrame(final Object uiReference) {
		final IFrameBluePrint bp = Toolkit.getBluePrintFactory().frame().autoCenterOff();
		return new FrameWidget(widgetFactorySpi.createFrame(widgetFactory, uiReference), bp);
	}

	@Override
	public IComposite createComposite(final Object uiReference) {
		final ICompositeBluePrint bp = Toolkit.getBluePrintFactory().composite();
		return new CompositeWidget(widgetFactorySpi.createComposite(widgetFactory, uiReference), bp);
	}

	@Override
	public boolean isConvertibleToComposite(final Object uiReference) {
		return widgetFactorySpi.isConvertibleToComposite(uiReference);
	}

}
