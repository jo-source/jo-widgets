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

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.addons.widgets.browser.api.IBrowser;
import org.jowidgets.addons.widgets.browser.api.IBrowserFunction;
import org.jowidgets.addons.widgets.browser.api.IBrowserFunctionHandle;
import org.jowidgets.addons.widgets.browser.api.IBrowserLocationEvent;
import org.jowidgets.addons.widgets.browser.api.IBrowserLocationListener;
import org.jowidgets.addons.widgets.browser.api.IBrowserProgressListener;
import org.jowidgets.addons.widgets.browser.api.IBrowserSetupBuilder;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.spi.impl.swt.common.color.ColorCache;
import org.jowidgets.tools.types.VetoHolder;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.Assert;

class BrowserImpl extends ControlWrapper implements IBrowser {

	private final Set<IBrowserLocationListener> locationListeners;
	private final Set<IBrowserProgressListener> progressListeners;

	private final Browser swtBrowser;

	BrowserImpl(final IControl control, final Composite swtComposite, final IBrowserSetupBuilder<?> setup) {
		super(control);

		this.locationListeners = new LinkedHashSet<IBrowserLocationListener>();
		this.progressListeners = new LinkedHashSet<IBrowserProgressListener>();

		final Composite content;
		if (setup.hasBorder()) {
			swtComposite.setLayout(new FillLayout());
			content = new Composite(swtComposite, SWT.BORDER);
		}
		else {
			content = swtComposite;
		}
		content.setLayout(new MigLayout("", "0[grow, 0::]0", "0[grow, 0::]0"));

		content.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(final ControlEvent e) {
				final IWindow activeWindow = Toolkit.getActiveWindow();
				if (activeWindow != null) {
					activeWindow.redraw();
				}
			}
		});

		this.swtBrowser = new Browser(content, SWT.NONE);
		swtBrowser.setLayoutData("growx, growy, w 0::, h 0::");

		swtBrowser.addLocationListener(new LocationListenerImpl());
		swtBrowser.addProgressListener(new ProgressListenerImpl());

		if (setup.isVisible() != null) {
			setVisible(setup.isVisible().booleanValue());
		}

		if (setup.getBackgroundColor() != null) {
			swtBrowser.setBackground(ColorCache.getInstance().getColor(setup.getBackgroundColor()));
		}

		if (setup.getForegroundColor() != null) {
			swtBrowser.setForeground(ColorCache.getInstance().getColor(setup.getForegroundColor()));
		}
	}

	final Browser getSwtBrowser() {
		return swtBrowser;
	}

	@Override
	public final void setUrl(String url) {
		if (url == null) {
			url = "";
		}
		swtBrowser.setText("");
		swtBrowser.setUrl(url);
	}

	@Override
	public final void setHtml(String html) {
		if (html == null) {
			html = "";
		}
		swtBrowser.setUrl("");
		swtBrowser.setText(html);
	}

	@Override
	public final Object evaluateScript(final String javaScript) {
		Assert.paramNotNull(javaScript, "javaScript");
		return swtBrowser.evaluate(javaScript);
	}

	@Override
	public final boolean executeScript(final String javaScript) {
		Assert.paramNotNull(javaScript, "javaScript");
		return swtBrowser.execute(javaScript);
	}

	@Override
	public IBrowserFunctionHandle createBrowserFunction(final String functionName, final IBrowserFunction function) {
		Assert.paramNotEmpty(functionName, "functionName");
		Assert.paramNotNull(function, "function");
		return new BrowserFunctionHandle(functionName, new BrowserFunctionAdapter(getSwtBrowser(), functionName, function));
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
		private final BrowserFunction browserFunction;

		private BrowserFunctionHandle(final String functionName, final BrowserFunction browserFunction) {
			this.functionName = functionName;
			this.browserFunction = browserFunction;
		}

		@Override
		public void dispose() {
			browserFunction.dispose();
		}

		@Override
		public boolean isDisposed() {
			return browserFunction.isDisposed();
		}

		@Override
		public String getFunctionName() {
			return functionName;
		}

	}
}
