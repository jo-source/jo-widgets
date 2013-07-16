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

package org.jowidgets.addons.map.common.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.jowidgets.addons.map.common.IAvailableCallback;
import org.jowidgets.addons.map.common.IDesignationListener;
import org.jowidgets.addons.map.common.IMap;
import org.jowidgets.addons.map.common.IMapContext;
import org.jowidgets.addons.map.common.IViewChangeListener;
import org.jowidgets.util.Assert;

import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.Point;

public final class GoogleEarth extends Composite implements IMap, IMapContext {

	private final Map<Class<?>, IDesignationCallback<?>> designationCallbacks = new HashMap<Class<?>, IDesignationCallback<?>>() {
		private static final long serialVersionUID = 1L;
		{
			put(Point.class, new PointDesignationCallback());
		}
	};

	private final List<IViewChangeListener> viewChangeListeners = new CopyOnWriteArrayList<IViewChangeListener>();
	private final String apiKey;
	private final Browser browser;
	private volatile String pluginName;
	private volatile boolean initialized;
	private volatile boolean initializing;
	private String language = Locale.getDefault().getLanguage();
	private IAvailableCallback availableCallback;
	private IDesignationCallback<?> designationCallback;

	public GoogleEarth(final Composite parent, final String apiKey) {
		this(parent, apiKey, SWT.NONE);
	}

	public GoogleEarth(final Composite parent, final String apiKey, final int style) {
		super(parent, style);
		Assert.paramNotEmpty(apiKey, "apiKey");
		this.apiKey = apiKey;
		super.setLayout(new FillLayout());
		browser = new Browser(this, style & SWT.MOZILLA);
		createCallbacks();
	}

	@Override
	public void setLayout(final Layout layout) {
		// ignore
	}

	@Override
	public void setLanguage(final String language) {
		Assert.paramNotEmpty(language, "language");
		if (initialized || initializing) {
			throw new IllegalStateException("language cannot be set during or after initialization");
		}
		this.language = language;
	}

	@Override
	public void initialize(final IAvailableCallback availableCallback) {
		checkWidget();
		if (initialized) {
			throw new IllegalStateException("widget is already initialized");
		}
		if (initializing) {
			throw new IllegalStateException("widget is currently initializing");
		}
		initializing = true;
		this.availableCallback = availableCallback;
		try {
			final String html = IOUtils.toString(GoogleEarth.class.getResourceAsStream("googleearth.html"), "UTF-8").replace(
					"@API_KEY@",
					apiKey).replace("@LANG@", language);
			if (!browser.setText(html)) {
				throw new RuntimeException("initialization failed");
			}
		}
		catch (final IOException e) {
			throw new Error(e);
		}
	}

	private void createCallbacks() {
		new BrowserFunction(browser, "onMapInitialized") {
			@Override
			public Object function(final Object[] arguments) {
				super.function(arguments);
				pluginName = (String) arguments[0];
				initialized = true;
				initializing = false;
				if (availableCallback != null) {
					getBrowser().getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							availableCallback.onAvailable(GoogleEarth.this);
						}
					});
				}
				return null;
			}
		};

		new BrowserFunction(browser, "onMapError") {
			@Override
			public Object function(final Object[] arguments) {
				super.function(arguments);
				final String error = (String) arguments[0];
				final Label label = new Label(GoogleEarth.this, SWT.NONE);
				label.setText("Google Earth could not be initialized: " + error); // TODO i18n
				browser.dispose();
				GoogleEarth.this.layout();
				initialized = true;
				initializing = false;
				return null;
			}
		};

		new BrowserFunction(browser, "onViewChange") {
			@Override
			public Object function(final Object[] arguments) {
				super.function(arguments);
				getBrowser().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						for (final IViewChangeListener listener : viewChangeListeners) {
							listener.onViewChange(
									(Double) arguments[0],
									(Double) arguments[1],
									(Double) arguments[2],
									(Double) arguments[3]);
						}
					}
				});
				return null;
			}
		};
	}

	@Override
	public double[] getBoundingBox() {
		checkWidget();
		checkAvailable();
		final Object[] res = (Object[]) browser.evaluate("return new Array(ge.getView().getViewportGlobeBounds().getNorth(), ge.getView().getViewportGlobeBounds().getSouth(), ge.getView().getViewportGlobeBounds().getEast(), ge.getView().getViewportGlobeBounds().getWest())");
		return new double[] {(Double) res[0], (Double) res[1], (Double) res[2], (Double) res[3]};
	}

	@Override
	public boolean flyTo(final String placemarkId, final double range) {
		checkWidget();
		checkAvailable();
		final Object result = browser.evaluate("return flyToPlacemark('"
			+ StringEscapeUtils.escapeJavaScript(placemarkId)
			+ "', "
			+ range
			+ ");");
		return result instanceof Boolean ? (Boolean) result : false;
	}

	@Override
	public boolean flyTo(final double latitude, final double longitude, final double range) {
		checkWidget();
		checkAvailable();
		return browser.execute("flyTo(" + latitude + "," + longitude + "," + range + ");");
	}

	@Override
	public boolean addFeature(final Feature feature) {
		checkWidget();
		checkAvailable();
		final Kml kml = KmlFactory.createKml();
		kml.setFeature(feature);
		final StringWriter sw = new StringWriter();
		kml.marshal(sw);
		return browser.execute(pluginName
			+ ".getFeatures().appendChild("
			+ pluginName
			+ ".parseKml('"
			+ StringEscapeUtils.escapeJavaScript(sw.toString())
			+ "'));");
	}

	@Override
	public boolean removeFeature(final String featureId) {
		checkWidget();
		checkAvailable();
		final Object result = browser.evaluate("return removeFeature('" + StringEscapeUtils.escapeJavaScript(featureId) + "');");
		return result instanceof Boolean ? (Boolean) result : false;
	}

	@Override
	public boolean removeAllFeatures() {
		checkWidget();
		checkAvailable();
		return browser.execute("removeAllFeatures();");
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}

	@Override
	public boolean isAvailable() {
		return pluginName != null;
	}

	private void checkAvailable() {
		if (!isAvailable()) {
			throw new IllegalStateException("widget is not available");
		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public boolean startDesignation(final Class type, final IDesignationListener listener) {
		checkWidget();
		checkAvailable();
		Assert.paramNotNull(listener, "listener");
		if (designationCallback != null) {
			throw new IllegalStateException("widget is already in designation mode");
		}

		designationCallback = designationCallbacks.get(type);
		if (designationCallback == null) {
			throw new UnsupportedOperationException(type.getName() + " is not supported as designation type");
		}

		return designationCallback.register(browser, listener);
	}

	@Override
	public boolean endDesignation() {
		checkWidget();
		checkAvailable();
		if (designationCallback == null) {
			throw new IllegalStateException("widget is not in designation mode");
		}
		try {
			return designationCallback.unregister();
		}
		finally {
			designationCallback = null;
		}
	}

	@Override
	public boolean isDesignationRunning() {
		checkWidget();
		checkAvailable();
		return designationCallback != null;
	}

	@Override
	public void addViewChangeListener(final IViewChangeListener listener) {
		viewChangeListeners.add(listener);
	}

	@Override
	public boolean removeViewChangeListener(final IViewChangeListener listener) {
		return viewChangeListeners.remove(listener);
	}

	@Override
	public Set<Class<?>> getSupportedDesignationClasses() {
		return designationCallbacks.keySet();
	}

}
