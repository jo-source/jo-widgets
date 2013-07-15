/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.examples.common.workbench.widgets.views;

import org.jowidgets.addons.widgets.browser.api.BrowserBPF;
import org.jowidgets.addons.widgets.browser.api.IBrowser;
import org.jowidgets.addons.widgets.browser.api.IBrowserLocationEvent;
import org.jowidgets.addons.widgets.browser.tools.BrowserLocationAdapter;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.ITextControl;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.common.widgets.controller.IKeyEvent;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controller.KeyAdapter;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;

public class BrowserView extends AbstractHowToView implements IView {

	public static final String ID = BrowserView.class.getName();
	public static final String DEFAULT_LABEL = "Browser";

	public BrowserView(final IViewContext context) {
		super(context);
	}

	@Override
	public void createViewContent(final IContainer container, final IBluePrintFactory bpFactory) {
		//set the layout
		container.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "0[][grow, 0::]0"));

		//add url field
		final ITextControl urlField = container.add(BPF.textField(), "gapleft 5, gapright 5,growx, h 0::, wrap");

		//add browser 
		final IBrowser browser = container.add(BrowserBPF.browser(), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		//add key listener to url field
		urlField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final IKeyEvent event) {
				if (event.getVirtualKey() == VirtualKey.ENTER) {
					browser.setUrl(urlField.getText());
				}
			}
		});

		//add location listener to browser
		browser.addLocationListener(new BrowserLocationAdapter() {
			@Override
			public void locationChanged(final IBrowserLocationEvent event) {
				if (event.isTopFrameLocation()) {
					urlField.setText(event.getLocation());
				}
			}
		});

		browser.setUrl("http://www.gmx.de");
	}
}
