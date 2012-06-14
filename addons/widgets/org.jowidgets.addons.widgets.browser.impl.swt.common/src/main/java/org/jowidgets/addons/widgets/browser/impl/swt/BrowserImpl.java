/*
 * Copyright (c) 2012, grossmann
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

package org.jowidgets.addons.widgets.browser.impl.swt;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.addons.widgets.browser.api.IBrowser;
import org.jowidgets.addons.widgets.browser.api.IBrowserFunction;
import org.jowidgets.addons.widgets.browser.api.IBrowserFunctionHandle;
import org.jowidgets.addons.widgets.browser.api.IBrowserLocationEvent;
import org.jowidgets.addons.widgets.browser.api.IBrowserLocationListener;
import org.jowidgets.addons.widgets.browser.api.IBrowserProgressListener;
import org.jowidgets.addons.widgets.browser.api.IBrowserSetupBuilder;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.spi.impl.swt.common.color.ColorCache;
import org.jowidgets.tools.types.VetoHolder;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.Assert;
import org.jowidgets.util.IFutureValue;
import org.jowidgets.util.IFutureValueCallback;
import org.jowidgets.util.Tuple;

class BrowserImpl extends ControlWrapper implements IBrowser {

	private final Boolean initialVisiblityState;
	private final IColorConstant initialBackgroundColor;
	private final IColorConstant initialForegroundColor;

	private final Set<IBrowserLocationListener> locationListeners;
	private final Set<IBrowserProgressListener> progressListeners;

	private final Map<String, Tuple<IBrowserFunction, BrowserFunctionHandle>> browserFunctions;

	private final boolean border;

	private Browser swtBrowser;
	private String url;
	private String html;

	BrowserImpl(final IControl control, final IFutureValue<Composite> swtComposite, final IBrowserSetupBuilder<?> setup) {
		super(control);

		this.border = setup.hasBorder();
		this.initialVisiblityState = setup.isVisible();
		this.initialBackgroundColor = setup.getBackgroundColor();
		this.initialForegroundColor = setup.getForegroundColor();

		this.locationListeners = new LinkedHashSet<IBrowserLocationListener>();
		this.progressListeners = new LinkedHashSet<IBrowserProgressListener>();
		this.browserFunctions = new LinkedHashMap<String, Tuple<IBrowserFunction, BrowserFunctionHandle>>();

		swtComposite.addFutureCallback(new IFutureValueCallback<Composite>() {
			@Override
			public void initialized(final Composite value) {
				swtBrowser = createSwtBrowser(value);
			}
		});
	}

	final Browser getSwtBrowser() {
		if (swtBrowser == null) {
			throw new IllegalStateException("The browser was not yet initialized.");
		}
		return swtBrowser;
	}

	Browser createSwtBrowser(final Composite swtComposite) {
		final Composite content;
		if (border) {
			swtComposite.setLayout(new FillLayout());
			content = new Composite(swtComposite, SWT.BORDER);
		}
		else {
			content = swtComposite;
		}

		content.setLayout(new FillLayout());
		final Browser result = new Browser(content, SWT.NONE);

		if (initialVisiblityState != null) {
			setVisible(initialVisiblityState.booleanValue());
		}

		if (initialBackgroundColor != null) {
			result.setBackground(ColorCache.getInstance().getColor(initialBackgroundColor));
		}

		if (initialForegroundColor != null) {
			result.setForeground(ColorCache.getInstance().getColor(initialForegroundColor));
		}

		result.addLocationListener(new LocationListenerImpl());
		result.addProgressListener(new ProgressListenerImpl());

		for (final Entry<String, Tuple<IBrowserFunction, BrowserFunctionHandle>> entry : browserFunctions.entrySet()) {
			final BrowserFunctionHandle handle = entry.getValue().getSecond();
			if (!handle.isDisposed()) {
				final IBrowserFunction function = entry.getValue().getFirst();
				final BrowserFunction browserFunction = new BrowserFunction(result, entry.getKey()) {
					@Override
					public Object function(final Object[] arguments) {
						return function.invoke(arguments);
					}
				};
				handle.setBrowserFunction(browserFunction);
			}
		}
		browserFunctions.clear();

		if (url != null) {
			result.setUrl(url);
		}
		if (html != null) {
			result.setText(html);
		}

		return result;
	}

	@Override
	public final void setUrl(final String url) {
		if (isInitialized()) {
			getSwtBrowser().setUrl(url);
		}
		else {
			this.html = null;
			this.url = url;
		}
	}

	@Override
	public final void setHtml(final String html) {
		if (isInitialized()) {
			getSwtBrowser().setText(html);
		}
		else {
			this.url = null;
			this.html = html;
		}
	}

	@Override
	public final Object evaluateScript(final String javaScript) {
		Assert.paramNotNull(javaScript, "javaScript");
		return getSwtBrowser().evaluate(javaScript);
	}

	@Override
	public final boolean executeScript(final String javaScript) {
		Assert.paramNotNull(javaScript, "javaScript");
		return getSwtBrowser().execute(javaScript);
	}

	@Override
	public IBrowserFunctionHandle createBrowserFunction(final String functionName, final IBrowserFunction function) {
		Assert.paramNotEmpty(functionName, "functionName");
		Assert.paramNotNull(function, "function");
		if (isInitialized()) {
			final BrowserFunction browserFunction = new BrowserFunctionAdapter(getSwtBrowser(), functionName, function);
			return new BrowserFunctionHandle(functionName, browserFunction);
		}
		else {
			final BrowserFunctionHandle functionHandle = new BrowserFunctionHandle(functionName);
			Tuple<IBrowserFunction, BrowserFunctionHandle> tuple;
			tuple = new Tuple<IBrowserFunction, BrowserFunctionHandle>(function, functionHandle);
			browserFunctions.put(functionName, tuple);
			return functionHandle;
		}
	}

	@Override
	public final void addLocationListener(final IBrowserLocationListener listener) {
		Assert.paramNotNull(listener, "listener");
		locationListeners.add(listener);
	}

	@Override
	public final void removeLocationListener(final IBrowserLocationListener listener) {
		Assert.paramNotNull(listener, "listener");
		locationListeners.remove(listener);
	}

	@Override
	public final void addProgressListener(final IBrowserProgressListener listener) {
		Assert.paramNotNull(listener, "listener");
		progressListeners.add(listener);
	}

	@Override
	public final void removeProgressListener(final IBrowserProgressListener listener) {
		Assert.paramNotNull(listener, "listener");
		progressListeners.remove(listener);
	}

	final boolean isInitialized() {
		return swtBrowser != null;
	}

	String getUrl() {
		return url;
	}

	String getHtml() {
		return html;
	}

	private final class LocationListenerImpl implements LocationListener {

		@Override
		public void changing(final LocationEvent event) {
			if (fireOnLocationChange(event)) {
				event.doit = false;
			}
		}

		@Override
		public void changed(final LocationEvent event) {
			fireLocationChanged(event);
		}

		/**
		 * Fires an on location change event.
		 * 
		 * @param swtEvent
		 * @return true, if a veto occurred
		 */
		private boolean fireOnLocationChange(final LocationEvent swtEvent) {
			if (locationListeners.size() > 0) {
				final IBrowserLocationEvent event = new BrowserLocationEvent(swtEvent);
				final VetoHolder vetoHolder = new VetoHolder();
				for (final IBrowserLocationListener listener : new LinkedList<IBrowserLocationListener>(locationListeners)) {
					listener.onLocationChange(event, vetoHolder);
					if (vetoHolder.hasVeto()) {
						return true;
					}
				}
			}
			return false;
		}

		private void fireLocationChanged(final LocationEvent swtEvent) {
			if (locationListeners.size() > 0) {
				final IBrowserLocationEvent event = new BrowserLocationEvent(swtEvent);
				for (final IBrowserLocationListener listener : new LinkedList<IBrowserLocationListener>(locationListeners)) {
					listener.locationChanged(event);
				}
			}
		}
	}

	private final class ProgressListenerImpl implements ProgressListener {

		@Override
		public void changed(final ProgressEvent event) {
			for (final IBrowserProgressListener listener : new LinkedList<IBrowserProgressListener>(progressListeners)) {
				listener.loadProgressChanged(event.current, event.total);
			}
		}

		@Override
		public void completed(final ProgressEvent event) {
			for (final IBrowserProgressListener listener : new LinkedList<IBrowserProgressListener>(progressListeners)) {
				listener.loadFinished();
			}
		}

	}

	private final class BrowserFunctionAdapter extends BrowserFunction {

		private final IBrowserFunction function;

		public BrowserFunctionAdapter(final Browser browser, final String functionName, final IBrowserFunction function) {
			super(browser, functionName);
			this.function = function;
		}

		@Override
		public Object function(final Object[] arguments) {
			return function.invoke(arguments);
		}
	}

	private final class BrowserFunctionHandle implements IBrowserFunctionHandle {

		private final String functionName;

		private BrowserFunction browserFunction;
		private Boolean disposed;

		private BrowserFunctionHandle(final String functionName) {
			this(functionName, null);
		}

		private BrowserFunctionHandle(final String functionName, final BrowserFunction browserFunction) {
			this.functionName = functionName;
		}

		private void setBrowserFunction(final BrowserFunction browserFunction) {
			this.browserFunction = browserFunction;
		}

		@Override
		public void dispose() {
			if (browserFunction != null) {
				browserFunction.dispose();
			}
			else {
				disposed = Boolean.TRUE;
			}
		}

		@Override
		public boolean isDisposed() {
			if (browserFunction != null) {
				return browserFunction.isDisposed();
			}
			else {
				return disposed != null && disposed.booleanValue();
			}
		}

		@Override
		public String getFunctionName() {
			return functionName;
		}

	}
}
