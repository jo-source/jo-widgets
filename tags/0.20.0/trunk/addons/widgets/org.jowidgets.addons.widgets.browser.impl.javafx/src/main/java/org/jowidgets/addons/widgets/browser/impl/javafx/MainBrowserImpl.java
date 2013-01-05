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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebHistory;

import org.jowidgets.addons.widgets.browser.api.IBrowserDocumentListener;
import org.jowidgets.addons.widgets.browser.api.IBrowserSetupBuilder;
import org.jowidgets.addons.widgets.browser.api.IMainBrowser;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.util.Assert;

public class MainBrowserImpl extends BrowserImpl implements IMainBrowser {

	private final WebHistory webHistory;
	private final LinkedHashSet<IBrowserDocumentListener> documentListeners;

	public MainBrowserImpl(final IControl control, final IBrowserSetupBuilder<?> setup) {
		super(control, setup);
		webHistory = getEngine().getHistory();
		this.documentListeners = new LinkedHashSet<IBrowserDocumentListener>();

		getEngine().setOnStatusChanged(new StatusChangedHandler());
		getEngine().titleProperty().addListener(new TitleChangedListener());
	}

	@Override
	public String getUrl() {
		return getEngine().getLocation();
	}

	@Override
	public String getHtml() {
		return getEngine().getDocument().getDocumentURI();
	}

	@Override
	public void setJavascriptEnabled(final boolean enabled) {
		getEngine().setJavaScriptEnabled(enabled);

	}

	@Override
	public boolean isJavascriptEnabled() {
		return getEngine().isJavaScriptEnabled();
	}

	@Override
	public boolean setCookie(final String url, final String cookieValue) {
		// TODO DB Auto-generated method stub
		return false;
	}

	@Override
	public String getCookie(final String url, final String cookieName) {
		// TODO DB Auto-generated method stub
		return null;
	}

	@Override
	public void clearAllCookies() {
		// TODO DB Auto-generated method stub

	}

	@Override
	public void reload() {
		getEngine().reload();

	}

	@Override
	public void cancel() {
		getEngine().getLoadWorker().cancel();
	}

	@Override
	public void back() {
		webHistory.go(-1);
	}

	@Override
	public boolean isBackEnabled() {
		return webHistory.getCurrentIndex() >= 1 ? true : false;
	}

	@Override
	public void forward() {
		webHistory.go(1);
	}

	@Override
	public boolean isForwardEnabled() {
		return webHistory.getCurrentIndex() < webHistory.getEntries().size() - 1 ? true : false;
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

	private final class StatusChangedHandler implements EventHandler<WebEvent<String>> {

		@Override
		public void handle(final WebEvent<String> handle) {
			if (handle.getEventType() == WebEvent.STATUS_CHANGED) {
				for (final IBrowserDocumentListener listener : new LinkedList<IBrowserDocumentListener>(documentListeners)) {
					listener.statusTextChanged(handle.getData());
				}
			}

		}

	}

	private final class TitleChangedListener implements ChangeListener<String> {

		@Override
		public void changed(final ObservableValue<? extends String> ov, final String oldString, final String newString) {
			for (final IBrowserDocumentListener listener : new LinkedList<IBrowserDocumentListener>(documentListeners)) {
				listener.titleChanged(newString);
			}
		}

	}

}
