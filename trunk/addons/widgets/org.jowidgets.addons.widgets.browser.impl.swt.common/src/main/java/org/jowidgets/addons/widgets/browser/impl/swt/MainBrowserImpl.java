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

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.addons.widgets.browser.api.IBrowserDocumentListener;
import org.jowidgets.addons.widgets.browser.api.IMainBrowser;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.descriptor.setup.IComponentSetup;
import org.jowidgets.util.Assert;
import org.jowidgets.util.IAsyncCreationValue;

final class MainBrowserImpl extends BrowserImpl implements IMainBrowser {

	private final Set<IBrowserDocumentListener> documentListeners;

	MainBrowserImpl(final IControl control, final IAsyncCreationValue<Composite> swtComposite, final IComponentSetup setup) {
		super(control, swtComposite, setup);
		this.documentListeners = new LinkedHashSet<IBrowserDocumentListener>();
	}

	@Override
	Browser createSwtBrowser(final Composite composite) {
		final Browser result = super.createSwtBrowser(composite);
		result.addTitleListener(new TitleListenerImpl());
		result.addStatusTextListener(new StatusTextListenerImpl());
		return result;
	}

	@Override
	public String getUrl() {
		return getSwtBrowser().getUrl();
	}

	@Override
	public String getHtml() {
		return getSwtBrowser().getText();
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
	public void addDocumentListener(final IBrowserDocumentListener listener) {
		Assert.paramNotNull(listener, "listener");
		documentListeners.add(listener);
	}

	@Override
	public void removeDocumentListener(final IBrowserDocumentListener listener) {
		Assert.paramNotNull(listener, "listener");
		documentListeners.remove(listener);
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

}
