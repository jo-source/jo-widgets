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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactoryListener;
import org.jowidgets.impl.widgets.basic.FrameImpl;
import org.jowidgets.spi.IWidgetsServiceProvider;
import org.jowidgets.spi.widgets.IFrameSpi;

public class WindowProvider {

	private final IGenericWidgetFactory genericWidgetFactory;
	private final IWidgetsServiceProvider widgetsServiceProvider;
	private final Map<Object, IWindow> uiReferenceToWindow;

	public WindowProvider(final IGenericWidgetFactory genericWidgetFactory, final IWidgetsServiceProvider widgetsServiceProvider) {

		this.genericWidgetFactory = genericWidgetFactory;
		this.uiReferenceToWindow = new HashMap<Object, IWindow>();
		this.widgetsServiceProvider = widgetsServiceProvider;

		genericWidgetFactory.addWidgetFactoryListener(new IWidgetFactoryListener() {

			@Override
			public void widgetCreated(final IWidgetCommon widget) {
				if (widget instanceof IWindow) {
					final IWindow windowWidget = (IWindow) widget;
					//If a WindowWidget wraps another WindowWidget it will be assumed, 
					//that the newer window wraps the previous window with the same ui reference.
					//From now, the wrapping window will returned for the active window
					uiReferenceToWindow.put(windowWidget.getUiReference(), windowWidget);
				}

			}
		});
	}

	public IWindow getActiveWindow() {
		final Object activeWindowUiReference = widgetsServiceProvider.getActiveWindowUiReference();
		IWindow activeWindow = uiReferenceToWindow.get(widgetsServiceProvider.getActiveWindowUiReference());

		//maybe window would be created without jo-widgets, so create a wrapper, if possible
		if (activeWindowUiReference != null && activeWindow == null) {
			activeWindow = createWrapper(activeWindowUiReference);

		}
		return activeWindow;
	}

	public List<IWindow> getAllWindows() {
		final List<IWindow> result = new LinkedList<IWindow>();
		for (final Object uiRef : widgetsServiceProvider.getAllWindowsUiReference()) {
			IWindow window = uiReferenceToWindow.get(uiRef);
			if (window == null) {
				window = createWrapper(uiRef);
			}
			if (window != null) {
				result.add(window);
			}
		}
		return result;
	}

	private IWindow createWrapper(final Object uiRef) {
		if (widgetsServiceProvider.getWidgetFactory().isConvertibleToFrame(uiRef)) {

			final IFrameBluePrint bp = Toolkit.getBluePrintFactory().frame().autoCenterOff();

			final IFrameSpi frameSpi = widgetsServiceProvider.getWidgetFactory().createFrame(genericWidgetFactory, uiRef);

			final IWindow result = new FrameImpl(frameSpi, bp);

			//register the created frame to avoid a new creation for every call of this method
			uiReferenceToWindow.put(uiRef, result);

			return result;
		}
		return null;
	}
}
