/*
 * Copyright (c) 2011, H.Westphal
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

package org.jowidgets.addons.map.swing;

import java.awt.Canvas;
import java.awt.Container;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.addons.map.common.IAvailableCallback;
import org.jowidgets.addons.map.common.IMap;
import org.jowidgets.addons.map.common.IMapContext;
import org.jowidgets.addons.map.common.IViewChangeListener;
import org.jowidgets.addons.map.common.impl.GoogleEarth;
import org.jowidgets.addons.map.common.widget.IMapWidget;
import org.jowidgets.addons.map.common.widget.IMapWidgetBlueprint;
import org.jowidgets.addons.map.swing.AbstractSwtThread.IWidgetCallback;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;

final class SwingGoogleEarthWidget extends ControlWrapper implements IMapWidget {

	private final String apiKey;
	private final Canvas canvas;
	private final ConcurrentMap<IViewChangeListener, IViewChangeListener> viewChangeListeners = new ConcurrentHashMap<IViewChangeListener, IViewChangeListener>();
	private volatile String language;
	private volatile IMap map;

	SwingGoogleEarthWidget(final IMapWidgetBlueprint descriptor, final String apiKey) {
		super(Toolkit.getWidgetFactory().create(Toolkit.getBluePrintFactory().composite()));

		this.apiKey = apiKey;
		((IComposite) getWidget()).setLayout(new MigLayoutDescriptor("0[grow]0", "0[grow]0"));
		canvas = new Canvas();
		final Container container = (Container) getWidget().getUiReference();
		container.add(canvas, "grow, wmin 0, hmin 0");

		VisibiliySettingsInvoker.setVisibility(descriptor, this);
		ColorSettingsInvoker.setColors(descriptor, this);
	}

	@Override
	public void setLanguage(final String language) {
		if (map == null) {
			this.language = language;
		}
		else {
			map.setLanguage(language);
		}
	}

	@Override
	public void initialize(final IAvailableCallback callback) {
		final IUiThreadAccess uiThreadAccess = Toolkit.getUiThreadAccess();
		new AbstractSwtThread<IMap>(canvas, new IWidgetCallback<IMap>() {
			@Override
			public void onWidgetCreated(final IMap widget) {
				synchronized (SwingGoogleEarthWidget.this) {
					map = widget;
				}
				final IAvailableCallback callbackProxy;
				if (callback == null) {
					callbackProxy = null;
				}
				else {
					callbackProxy = new IAvailableCallback() {
						@Override
						public void onAvailable(final IMapContext mapContext) {
							uiThreadAccess.invokeLater(new Runnable() {
								@Override
								public void run() {
									callback.onAvailable(new MapContextAdapter(((Composite) widget).getDisplay(), mapContext));
								}
							});
						}
					};
				}
				widget.initialize(callbackProxy);
			}
		}) {
			@Override
			protected IMap createWidget(final Shell shell) {
				final IMap widget = new GoogleEarth(shell, apiKey);
				if (language != null) {
					widget.setLanguage(language);
				}
				for (final IViewChangeListener viewChangeListener : viewChangeListeners.values()) {
					widget.addViewChangeListener(viewChangeListener);
				}
				return widget;
			}
		}.start();
	}

	@Override
	public synchronized boolean isInitialized() {
		if (map == null) {
			return false;
		}
		return map.isInitialized();
	}

	@Override
	public synchronized boolean isAvailable() {
		if (map == null) {
			return false;
		}
		return map.isAvailable();
	}

	@Override
	public void addViewChangeListener(final IViewChangeListener listener) {
		if (!viewChangeListeners.containsKey(listener)) {
			final IViewChangeListener swtListener = new IViewChangeListener() {
				@Override
				public void onViewChange(final double north, final double south, final double east, final double west) {
					Toolkit.getUiThreadAccess().invokeLater(new Runnable() {
						@Override
						public void run() {
							listener.onViewChange(north, south, east, west);
						}
					});
				}
			};
			viewChangeListeners.putIfAbsent(listener, swtListener);
		}
		synchronized (this) {
			if (map != null) {
				map.addViewChangeListener(viewChangeListeners.get(listener));
			}
		}
	}

	@Override
	public boolean removeViewChangeListener(final IViewChangeListener listener) {
		final IViewChangeListener swtListener = viewChangeListeners.remove(listener);
		if (swtListener == null) {
			return false;
		}
		synchronized (this) {
			if (map == null) {
				return true;
			}
			return map.removeViewChangeListener(swtListener);
		}
	}

	@Override
	public synchronized Set<Class<?>> getSupportedDesignationClasses() {
		if (map == null) {
			return Collections.emptySet();
		}
		return map.getSupportedDesignationClasses();
	}

}
