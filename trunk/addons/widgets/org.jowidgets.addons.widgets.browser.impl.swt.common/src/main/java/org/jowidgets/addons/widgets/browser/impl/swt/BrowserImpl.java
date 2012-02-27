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

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.addons.widgets.browser.api.BrowserBPF;
import org.jowidgets.addons.widgets.browser.api.IBrowser;
import org.jowidgets.addons.widgets.browser.api.IBrowserBluePrint;
import org.jowidgets.addons.widgets.browser.api.IBrowserDocumentListener;
import org.jowidgets.addons.widgets.browser.api.IBrowserLocationEvent;
import org.jowidgets.addons.widgets.browser.api.IBrowserLocationListener;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.spi.impl.swt.common.color.ColorCache;
import org.jowidgets.tools.types.VetoHolder;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.Assert;
import org.jowidgets.util.IProvider;

final class BrowserImpl extends ControlWrapper implements IBrowser {

	private final IBrowserBluePrint bluePrint;
	private final IProvider<Composite> swtCompositeProvider;
	private final Set<IBrowserLocationListener> locationListeners;
	private final Set<IBrowserDocumentListener> documentListeners;

	private Browser swtBrowser;

	BrowserImpl(final IControl control, final IProvider<Composite> swtCompositeProvider, final IBrowserBluePrint bluePrint) {
		super(control);
		this.swtCompositeProvider = swtCompositeProvider;
		this.bluePrint = BrowserBPF.browser();
		this.bluePrint.setSetup(bluePrint);

		this.locationListeners = new LinkedHashSet<IBrowserLocationListener>();
		this.documentListeners = new LinkedHashSet<IBrowserDocumentListener>();
	}

	private Browser getSwtBrowser() {
		if (swtBrowser == null) {
			final Composite swtComposite = swtCompositeProvider.get();
			swtComposite.setLayout(new FillLayout());
			this.swtBrowser = new Browser(swtComposite, SWT.NONE);

			if (bluePrint.isVisible() != null) {
				setVisible(bluePrint.isVisible());
			}

			if (bluePrint.getBackgroundColor() != null) {
				swtBrowser.setBackground(ColorCache.getInstance().getColor(bluePrint.getBackgroundColor()));
			}

			if (bluePrint.getForegroundColor() != null) {
				swtBrowser.setForeground(ColorCache.getInstance().getColor(bluePrint.getBackgroundColor()));
			}

			swtBrowser.addLocationListener(new LocationListenerImpl());
			swtBrowser.addTitleListener(new TitleListenerImpl());
			swtBrowser.addStatusTextListener(new StatusTextListenerImpl());
			swtBrowser.addProgressListener(new ProgressListenerImpl());
		}
		return swtBrowser;
	}

	@Override
	public void setUrl(final String url) {
		getSwtBrowser().setUrl(url);
	}

	@Override
	public String getUrl() {
		return getSwtBrowser().getUrl();
	}

	@Override
	public void setHtml(final String html) {
		getSwtBrowser().setText(html);
	}

	@Override
	public String getHtml() {
		return getSwtBrowser().getText();
	}

	@Override
	public Object evaluateScript(final String javaScript) {
		Assert.paramNotNull(javaScript, "javaScript");
		return getSwtBrowser().evaluate(javaScript);
	}

	@Override
	public boolean executeScript(final String javaScript) {
		Assert.paramNotNull(javaScript, "javaScript");
		return getSwtBrowser().execute(javaScript);
	}

	@Override
	public void setJavascriptEnabled(final boolean enabled) {
		getSwtBrowser().setJavascriptEnabled(enabled);
	}

	@Override
	public boolean isJavascriptEnabled() {
		return getSwtBrowser().getJavascriptEnabled();
	}

	@Override
	public boolean setCookie(final String url, final String cookieValue) {
		return Browser.setCookie(cookieValue, url);
	}

	@Override
	public String getCookie(final String url, final String cookieName) {
		return Browser.getCookie(cookieName, url);
	}

	@Override
	public void clearAllCookies() {
		Browser.clearSessions();
	}

	@Override
	public void back() {
		getSwtBrowser().back();
	}

	@Override
	public boolean isBackEnabled() {
		return getSwtBrowser().isBackEnabled();
	}

	@Override
	public void forward() {
		getSwtBrowser().forward();
	}

	@Override
	public boolean isForwardEnabled() {
		return getSwtBrowser().isForwardEnabled();
	}

	@Override
	public void reload() {
		getSwtBrowser().refresh();
	}

	@Override
	public void cancel() {
		getSwtBrowser().stop();
	}

	@Override
	public void addLocationListener(final IBrowserLocationListener listener) {
		Assert.paramNotNull(listener, "listener");
		locationListeners.add(listener);
	}

	@Override
	public void removeLocationListener(final IBrowserLocationListener listener) {
		Assert.paramNotNull(listener, "listener");
		locationListeners.remove(listener);
	}

	@Override
	public void addDocumentListener(final IBrowserDocumentListener listener) {
		Assert.paramNotNull(listener, "listener");
		documentListeners.add(listener);
	}

	@Override
	public void removeDocumentListener(final IBrowserDocumentListener listener) {
		Assert.paramNotNull(listener, "listener");
		documentListeners.remove(listener);
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

	private final class TitleListenerImpl implements TitleListener {
		@Override
		public void changed(final TitleEvent event) {
			for (final IBrowserDocumentListener listener : new LinkedList<IBrowserDocumentListener>(documentListeners)) {
				listener.titleChanged(event.title);
			}
		}
	}

	private final class StatusTextListenerImpl implements StatusTextListener {
		@Override
		public void changed(final StatusTextEvent event) {
			for (final IBrowserDocumentListener listener : new LinkedList<IBrowserDocumentListener>(documentListeners)) {
				listener.statusTextChanged(event.text);
			}
		}
	}

	private final class ProgressListenerImpl implements ProgressListener {

		@Override
		public void changed(final ProgressEvent event) {
			for (final IBrowserDocumentListener listener : new LinkedList<IBrowserDocumentListener>(documentListeners)) {
				listener.loadProgressChanged(event.current, event.total);
			}
		}

		@Override
		public void completed(final ProgressEvent event) {
			for (final IBrowserDocumentListener listener : new LinkedList<IBrowserDocumentListener>(documentListeners)) {
				listener.loadFinished();
			}
		}

	}
}
