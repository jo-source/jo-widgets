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

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.addons.widgets.browser.api.BrowserBPF;
import org.jowidgets.addons.widgets.browser.api.IBrowser;
import org.jowidgets.addons.widgets.browser.api.IBrowserBluePrint;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.spi.impl.swt.common.color.ColorCache;
import org.jowidgets.tools.widgets.wrapper.ControlWrapper;
import org.jowidgets.util.IProvider;

final class BrowserImpl extends ControlWrapper implements IBrowser {

	private final IBrowserBluePrint bluePrint;
	private final IProvider<Composite> swtCompositeProvider;

	private Browser swtBrowser;

	BrowserImpl(final IControl control, final IProvider<Composite> swtCompositeProvider, final IBrowserBluePrint bluePrint) {
		super(control);
		this.swtCompositeProvider = swtCompositeProvider;
		this.bluePrint = BrowserBPF.browser();
		this.bluePrint.setSetup(bluePrint);
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
	public void back() {
		getSwtBrowser().back();
	}

	@Override
	public void forward() {
		getSwtBrowser().forward();
	}

	private Browser getSwtBrowser() {
		if (swtBrowser == null) {
			final Composite swtComposite = swtCompositeProvider.get();
			swtComposite.setLayout(new FillLayout());
			this.swtBrowser = new Browser(swtComposite, SWT.NONE & SWT.MOZILLA);

			if (bluePrint.isVisible() != null) {
				setVisible(bluePrint.isVisible());
			}

			if (bluePrint.getBackgroundColor() != null) {
				swtBrowser.setBackground(ColorCache.getInstance().getColor(bluePrint.getBackgroundColor()));
			}

			if (bluePrint.getForegroundColor() != null) {
				swtBrowser.setForeground(ColorCache.getInstance().getColor(bluePrint.getBackgroundColor()));
			}
		}
		return swtBrowser;
	}

}
