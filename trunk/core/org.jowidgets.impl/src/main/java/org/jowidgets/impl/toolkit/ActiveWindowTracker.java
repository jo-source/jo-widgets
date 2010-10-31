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
import java.util.Map;

import org.jowidgets.api.widgets.IWindowWidget;
import org.jowidgets.common.widgets.IWidget;
import org.jowidgets.common.widgets.controler.impl.WindowAdapter;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactoryListener;

public class ActiveWindowTracker {

	private final Map<Object, IWindowWidget> uiReferenceToWindow;

	private Object activeWindowUiReference;

	public ActiveWindowTracker(final IGenericWidgetFactory genericWidgetFactory) {

		this.uiReferenceToWindow = new HashMap<Object, IWindowWidget>();

		genericWidgetFactory.addWidgetFactoryListener(new IWidgetFactoryListener() {

			@Override
			public void widgetCreated(final IWidget widget) {
				if (widget instanceof IWindowWidget) {
					final IWindowWidget windowWidget = (IWindowWidget) widget;
					if (uiReferenceToWindow.get(windowWidget.getUiReference()) != null) {
						//It will be assumed, that the created window wraps a window, because the uiReference 
						//is already registered for a different window widget.
						//From now, the wrapped window will returned for the active window
						uiReferenceToWindow.put(windowWidget.getUiReference(), windowWidget);
					}
					else {
						uiReferenceToWindow.put(windowWidget.getUiReference(), windowWidget);
						new WindowListenerImpl((IWindowWidget) widget);
					}
				}

			}
		});
	}

	public IWindowWidget getActiveWindow() {
		if (activeWindowUiReference != null) {
			return uiReferenceToWindow.get(activeWindowUiReference);
		}
		return null;
	}

	private class WindowListenerImpl extends WindowAdapter {

		private final IWindowWidget window;

		public WindowListenerImpl(final IWindowWidget window) {
			super();
			this.window = window;
			this.window.addWindowListener(this);
		}

		@Override
		public void windowActivated() {
			if (activeWindowUiReference != null && activeWindowUiReference != window.getUiReference()) {
				throw new IllegalStateException("More than one active window. Report this, its a bug");
			}
			else {
				activeWindowUiReference = window.getUiReference();
			}
		}

		@Override
		public void windowDeactivated() {
			if (activeWindowUiReference != window.getUiReference()) {
				throw new IllegalStateException("This window is not active. Report this, its a bug");
			}
			activeWindowUiReference = null;
		}

		@Override
		public void windowClosed() {
			window.removeWindowListener(this);
		}

	}
}
