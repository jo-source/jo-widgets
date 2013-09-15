/*
 * Copyright (c) 2012, David Bauknecht
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
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.addons.widgets.browser.impl.javafx;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import org.jowidgets.addons.widgets.browser.api.IBrowser;
import org.jowidgets.addons.widgets.browser.api.IBrowserFunction;
import org.jowidgets.addons.widgets.browser.api.IBrowserFunctionHandle;
import org.jowidgets.addons.widgets.browser.api.IBrowserLocationEvent;
import org.jowidgets.addons.widgets.browser.api.IBrowserLocationListener;
import org.jowidgets.addons.widgets.browser.api.IBrowserProgressListener;
import org.jowidgets.addons.widgets.browser.api.IBrowserSetupBuilder;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.spi.impl.javafx.layout.LayoutPane;
import org.jowidgets.spi.impl.javafx.widgets.StyleDelegate;
import org.jowidgets.tools.types.VetoHolder;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.Assert;

class BrowserImpl extends ControlWrapper implements IBrowser {

	private final WebView webView;
	private final WebEngine webEngine;

	private final StyleDelegate styleDelegate;
	private final IColorConstant initialBackgroundColor;
	private final IColorConstant initialForegroundColor;
	private final Set<IBrowserLocationListener> locationListeners;
	private final Set<IBrowserProgressListener> progressListeners;
	private final Boolean initialVisiblityState;

	public BrowserImpl(final IControl control, final IBrowserSetupBuilder<?> setup) {
		super(control);
		final LayoutPane uiReference = (LayoutPane) control.getUiReference();
		webView = new WebView();
		webEngine = webView.getEngine();
		styleDelegate = new StyleDelegate(webView);
		this.initialBackgroundColor = setup.getBackgroundColor();
		this.initialForegroundColor = setup.getForegroundColor();
		this.initialVisiblityState = setup.isVisible();
		if (initialBackgroundColor != null) {
			styleDelegate.setBackgroundColor(initialBackgroundColor);
		}
		if (initialForegroundColor != null) {
			styleDelegate.setForegroundColor(initialForegroundColor);
		}
		if (initialVisiblityState != null) {
			setVisible(initialVisiblityState.booleanValue());
		}

		this.locationListeners = new LinkedHashSet<IBrowserLocationListener>();
		this.progressListeners = new LinkedHashSet<IBrowserProgressListener>();

		webEngine.getLoadWorker().stateProperty().addListener(new LocationChangeListener());

		webEngine.getLoadWorker().progressProperty().addListener(new ProgressListener());
		uiReference.getChildren().add(webView);

	}

	WebView getBrowser() {
		if (webView == null) {
			throw new IllegalStateException("The browser is not initialized.");
		}
		return webView;
	}

	WebEngine getEngine() {
		if (webEngine == null) {
			throw new IllegalStateException("The browser is not initialized.");
		}
		return webEngine;
	}

	@Override
	public void setSize(final int width, final int height) {
		webView.resize(width, height);
	}

	@Override
	public void setUrl(final String url) {

		getEngine().load(url);
	}

	@Override
	public void setHtml(final String html) {
		getEngine().loadContent(html);

	}

	@Override
	public Object evaluateScript(final String javaScript) {
		return getEngine().executeScript(javaScript);
	}

	@Override
	public boolean executeScript(final String javaScript) {
		return getEngine().executeScript(javaScript) != null ? true : false;
	}

	@Override
	public IBrowserFunctionHandle createBrowserFunction(final String functionName, final IBrowserFunction function) {
		Assert.paramNotEmpty(functionName, "functionName");
		Assert.paramNotNull(function, "function");
		final JSObject jsobj = (JSObject) webEngine.executeScript("window");
		final BrowserFunctionHandle browserFunctionHandle = new BrowserFunctionHandle(functionName);
		jsobj.setMember(functionName, function);
		return browserFunctionHandle;
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

	private final class ProgressListener implements ChangeListener<Number> {

		@Override
		public void changed(final ObservableValue<? extends Number> ov, final Number oldValue, final Number newValue) {
			final Worker<Void> worker = getEngine().getLoadWorker();

			if (worker.getWorkDone() == worker.getTotalWork()) {
				for (final IBrowserProgressListener listener : new LinkedList<IBrowserProgressListener>(progressListeners)) {
					listener.loadFinished();
				}

			}
			else {
				for (final IBrowserProgressListener listener : new LinkedList<IBrowserProgressListener>(progressListeners)) {
					listener.loadProgressChanged((int) worker.getWorkDone(), (int) worker.getTotalWork());
				}
			}

		}
	}

	private final class LocationChangeListener implements ChangeListener<State> {

		@Override
		public void changed(final ObservableValue<? extends State> ov, final State oldState, final State newState) {
			if (newState == State.RUNNING) {
				if (fireOnLocationChange()) {
					getEngine().getLoadWorker().cancel();
				}
			}
			if (newState == State.SUCCEEDED) {
				fireLocationChanged();
			}
		}

		private boolean fireOnLocationChange() {
			if (locationListeners.size() > 0) {
				final IBrowserLocationEvent event = new BrowserLocationEvent(getEngine().getLocation());
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

		private void fireLocationChanged() {
			if (locationListeners.size() > 0) {
				final IBrowserLocationEvent event = new BrowserLocationEvent(getEngine().getLocation());
				for (final IBrowserLocationListener listener : new LinkedList<IBrowserLocationListener>(locationListeners)) {
					listener.locationChanged(event);
				}
			}
		}

	}

	private final class BrowserFunctionHandle implements IBrowserFunctionHandle {

		private final String functionName;

		private BrowserFunctionHandle(final String functionName) {
			this.functionName = functionName;
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
		}

		@Override
		public boolean isDisposed() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String getFunctionName() {
			return functionName;
		}
	}

}
