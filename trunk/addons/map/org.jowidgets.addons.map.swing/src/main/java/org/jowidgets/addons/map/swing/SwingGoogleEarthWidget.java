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
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.swing.JPanel;

import org.eclipse.swt.widgets.Shell;
import org.jowidgets.addons.map.common.IAvailableCallback;
import org.jowidgets.addons.map.common.IMapContext;
import org.jowidgets.addons.map.common.IViewChangeListener;
import org.jowidgets.addons.map.common.impl.GoogleEarth;
import org.jowidgets.addons.map.common.widget.IMapWidget;
import org.jowidgets.addons.map.common.widget.IMapWidgetBlueprint;
import org.jowidgets.addons.map.swing.AbstractSwtThread.IWidgetCallback;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IComponentCommon;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;

final class SwingGoogleEarthWidget implements IMapWidget {

	private final String apiKey;
	private final Canvas canvas;
	private final IComposite composite;
	private final ConcurrentMap<IViewChangeListener, IViewChangeListener> viewChangeListeners = new ConcurrentHashMap<IViewChangeListener, IViewChangeListener>();
	private volatile String language;
	private volatile GoogleEarth map;

	SwingGoogleEarthWidget(final IMapWidgetBlueprint descriptor, final String apiKey) {
		this.apiKey = apiKey;

		final JPanel panel = new JPanel();
		composite = Toolkit.getWidgetWrapperFactory().createComposite(panel);
		composite.setLayout(Toolkit.getLayoutFactoryProvider().fillLayout());
		canvas = new Canvas();
		panel.add(canvas);

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
		new AbstractSwtThread<GoogleEarth>(canvas, new IWidgetCallback<GoogleEarth>() {
			@Override
			public void onWidgetCreated(final GoogleEarth widget) {
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
							Toolkit.getUiThreadAccess().invokeLater(new Runnable() {
								@Override
								public void run() {
									callback.onAvailable(new MapContextAdapter(widget.getDisplay(), mapContext));
								}
							});
						}
					};
				}
				widget.initialize(callbackProxy);
			}
		}) {
			@Override
			protected GoogleEarth createWidget(final Shell shell) {
				final GoogleEarth widget = new GoogleEarth(shell, apiKey);
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

	@Override
	public void addKeyListener(final IKeyListener listener) {
		composite.addKeyListener(listener);
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		composite.addFocusListener(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener mouseListener) {
		composite.addMouseListener(mouseListener);
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		composite.addComponentListener(componentListener);
	}

	@Override
	public Object getUiReference() {
		return composite.getUiReference();
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		composite.addPopupDetectionListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		composite.removeKeyListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		composite.removeFocusListener(listener);
	}

	@Override
	public void removeMouseListener(final IMouseListener mouseListener) {
		composite.removeMouseListener(mouseListener);
	}

	@Override
	public void setLayoutConstraints(final Object layoutConstraints) {
		composite.setLayoutConstraints(layoutConstraints);
	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		composite.removeComponentListener(componentListener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		composite.removePopupDetectionListener(listener);
	}

	@Override
	public void setParent(final IContainer parent) {
		composite.setParent(parent);
	}

	@Override
	public void setToolTipText(final String toolTip) {
		composite.setToolTipText(toolTip);
	}

	@Override
	public Object getLayoutConstraints() {
		return composite.getLayoutConstraints();
	}

	@Override
	public Dimension getMinSize() {
		return composite.getMinSize();
	}

	@Override
	public IContainer getParent() {
		return composite.getParent();
	}

	@Override
	public boolean isReparentable() {
		return composite.isReparentable();
	}

	@Override
	public Dimension getPreferredSize() {
		return composite.getPreferredSize();
	}

	@Override
	public void setMinSize(final Dimension minSize) {
		composite.setMinSize(minSize);
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return composite.createPopupMenu();
	}

	@Override
	public Dimension getMaxSize() {
		return composite.getMaxSize();
	}

	@Override
	public void setPopupMenu(final IMenuModel popupMenu) {
		composite.setPopupMenu(popupMenu);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		composite.setEnabled(enabled);
	}

	@Override
	public void setPreferredSize(final Dimension preferredSize) {
		composite.setPreferredSize(preferredSize);
	}

	@Override
	public boolean isEnabled() {
		return composite.isEnabled();
	}

	@Override
	public void setMaxSize(final Dimension maxSize) {
		composite.setMaxSize(maxSize);
	}

	@Override
	public Position toScreen(final Position localPosition) {
		return composite.toScreen(localPosition);
	}

	@Override
	public Position toLocal(final Position screenPosition) {
		return composite.toLocal(screenPosition);
	}

	@Override
	public void setSize(final int width, final int height) {
		composite.setSize(width, height);
	}

	@Override
	public void redraw() {
		composite.redraw();
	}

	@Override
	public void setPosition(final int x, final int y) {
		composite.setPosition(x, y);
	}

	@Override
	public Position fromComponent(final IComponentCommon component, final Position componentPosition) {
		return composite.fromComponent(component, componentPosition);
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		composite.setRedrawEnabled(enabled);
	}

	@Override
	public Position toComponent(final Position componentPosition, final IComponentCommon component) {
		return composite.toComponent(componentPosition, component);
	}

	@Override
	public boolean requestFocus() {
		return composite.requestFocus();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		composite.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		composite.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return composite.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return composite.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		composite.setCursor(cursor);
	}

	@Override
	public void setVisible(final boolean visible) {
		composite.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return composite.isVisible();
	}

	@Override
	public Dimension getSize() {
		return composite.getSize();
	}

	@Override
	public void setSize(final Dimension size) {
		composite.setSize(size);
	}

	@Override
	public Position getPosition() {
		return composite.getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		composite.setPosition(position);
	}

}
