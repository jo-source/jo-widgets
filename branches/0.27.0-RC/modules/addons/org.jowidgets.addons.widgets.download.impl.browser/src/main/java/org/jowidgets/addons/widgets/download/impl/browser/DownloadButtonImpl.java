/*
 * Copyright (c) 2013, grossmann
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

package org.jowidgets.addons.widgets.download.impl.browser;

import java.net.MalformedURLException;
import java.net.URL;

import org.jowidgets.addons.widgets.browser.api.BrowserBPF;
import org.jowidgets.addons.widgets.browser.api.IBrowser;
import org.jowidgets.addons.widgets.download.api.IDownloadButton;
import org.jowidgets.addons.widgets.download.api.IDownloadButtonBluePrint;
import org.jowidgets.api.command.IAction;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.tools.widgets.wrapper.CompositeWrapper;
import org.jowidgets.util.EmptyCheck;

class DownloadButtonImpl extends CompositeWrapper implements IDownloadButton {

	private final IButton button;

	private IBrowser browser;
	private String url;

	public DownloadButtonImpl(final IComposite composite, final IDownloadButtonBluePrint bluePrint) {
		super(composite);
		composite.setLayout(new MigLayoutDescriptor("hidemode 3", "0[grow, 0::]0", "0[grow, 0::]0"));
		this.url = bluePrint.getUrl();

		final IButtonBluePrint buttonBp = BPF.button();
		buttonBp.setSetup(bluePrint);

		this.button = composite.add(buttonBp, MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
		button.addActionListener(new DownloadActionListener());
		button.setEnabled(false);

		if (!EmptyCheck.isEmpty(bluePrint.getUrl())) {
			setUrl(bluePrint.getUrl());
		}
	}

	@Override
	protected IComposite getWidget() {
		return super.getWidget();
	}

	@Override
	public void setUrl(final String url) {
		try {
			new URL(url);
			this.url = url;
		}
		catch (final MalformedURLException e) {
			this.url = null;
		}
		button.setEnabled(!EmptyCheck.isEmpty(this.url));
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public void download() {
		//Create a new browser for every download to allow that subsequent
		//downloads for the same url will work. If you change this code, try
		//to press the download button more than once in rwt for the same url!
		if (browser != null) {
			browser.dispose();
			browser = null;
		}
		if (!EmptyCheck.isEmpty(url)) {
			browser = getWidget().add(BrowserBPF.browser().setVisible(false));
			browser.setUrl(url);
		}
	}

	@Override
	public void setAction(final IAction action) {
		throw new UnsupportedOperationException("setAction() is not possible for a DownloadButton");
	}

	@Override
	public String getText() {
		return button.getText();
	}

	@Override
	public IImageConstant getIcon() {
		return button.getIcon();
	}

	@Override
	public void setFontSize(final int size) {
		button.setFontSize(size);
	}

	@Override
	public void setFontName(final String fontName) {
		button.setFontName(fontName);
	}

	@Override
	public void setMarkup(final Markup markup) {
		button.setMarkup(markup);
	}

	@Override
	public void setText(final String text) {
		button.setText(text);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		button.setIcon(icon);
	}

	@Override
	public void addActionListener(final IActionListener actionListener) {
		button.addActionListener(actionListener);
	}

	@Override
	public void removeActionListener(final IActionListener actionListener) {
		button.removeActionListener(actionListener);
	}

	private class DownloadActionListener implements IActionListener {
		@Override
		public void actionPerformed() {
			download();
		}
	}

}
