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

package org.jowidgets.examples.common.browser;

import org.jowidgets.addons.icons.silkicons.SilkIcons;
import org.jowidgets.addons.widgets.browser.api.BrowserBPF;
import org.jowidgets.addons.widgets.browser.api.IBrowser;
import org.jowidgets.addons.widgets.browser.api.IBrowserDocumentListener;
import org.jowidgets.addons.widgets.browser.api.IBrowserLocationEvent;
import org.jowidgets.addons.widgets.browser.api.IBrowserLocationListener;
import org.jowidgets.api.color.Colors;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.ITextControl;
import org.jowidgets.api.widgets.ITextLabel;
import org.jowidgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.controller.IKeyEvent;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.examples.common.icons.DemoIconsInitializer;
import org.jowidgets.tools.controller.KeyAdapter;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.util.EmptyCheck;

public final class BrowserDemoApplication implements IApplication {

	private static final String HOME_URL = "http://www.google.de";

	private final String titlePrefix;

	public BrowserDemoApplication(final String title) {
		this.titlePrefix = title;
	}

	public void start() {
		DemoIconsInitializer.initialize();
		Toolkit.getInstance().getApplicationRunner().run(this);
	}

	@Override
	public void start(final IApplicationLifecycle lifecycle) {
		final IFrame frame = Toolkit.createRootFrame(BPF.frame().setTitle("Browser Demo").autoPackOff(), lifecycle);
		frame.setBackgroundColor(Colors.WHITE);
		frame.setSize(1024, 768);
		frame.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "[30!][]0[grow, 0::]2[]2[18!]"));

		//add navigation bar
		final IComposite navigation = frame.add(BPF.composite(), "growx, h 0::, wrap");
		navigation.setLayout(new MigLayoutDescriptor("[][][][][grow, 0::]", "0[]0"));
		final IButton backButton = navigation.add(BPF.button().setEnabled(false).setIcon(SilkIcons.ARROW_LEFT), "sg bg");
		final IButton forwardButton = navigation.add(BPF.button().setEnabled(false).setIcon(SilkIcons.ARROW_RIGHT), "sg bg");
		final IButton reloadButton = navigation.add(BPF.button().setIcon(SilkIcons.ARROW_REFRESH), "sg bg");
		final IButton cancelButton = navigation.add(BPF.button().setEnabled(false).setIcon(SilkIcons.CANCEL), "sg bg");
		final ITextControl urlField = navigation.add(BPF.textField(), "growx, h 0::");
		frame.add(BPF.separator(), "growx, h 0::, wrap");

		//add browser content
		final IBrowser browser = frame.add(BrowserBPF.browser(), MigLayoutFactory.GROWING_CELL_CONSTRAINTS + ",wrap");
		frame.add(BPF.separator(), "growx, h 0::, wrap");

		//add status bar
		final ITextLabelBluePrint statusLabelBp = BPF.textLabel();
		statusLabelBp.setForegroundColor(Colors.DISABLED);
		final ITextLabel statusLabel = frame.add(statusLabelBp, "growx, h 0::, gapleft 5");

		backButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				browser.back();
			}
		});

		forwardButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				browser.forward();
			}
		});

		reloadButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				browser.reload();
			}
		});

		cancelButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				browser.cancel();
			}
		});

		urlField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final IKeyEvent event) {
				if (event.getVirtualKey() == VirtualKey.ENTER) {
					browser.setUrl(urlField.getText());
				}
			}
		});

		browser.addLocationListener(new IBrowserLocationListener() {

			@Override
			public void onLocationChange(final IBrowserLocationEvent event, final IVetoable veto) {
				if (event.getLocation().equals("http://www.gmx.de/")) {
					Toolkit.getMessagePane().showInfo("GMX will be blocked! \n(Just for test purpose :-)");
					veto.veto();
				}
			}

			@Override
			public void locationChanged(final IBrowserLocationEvent event) {
				if (event.isTopFrameLocation()) {
					urlField.setText(event.getLocation());
					checkNavButtons();
				}
			}

			private void checkNavButtons() {
				forwardButton.setEnabled(browser.isForwardEnabled());
				backButton.setEnabled(browser.isBackEnabled());
			}
		});

		browser.addDocumentListener(new IBrowserDocumentListener() {

			@Override
			public void titleChanged(final String title) {
				if (EmptyCheck.isEmpty(title)) {
					frame.setTitle(titlePrefix);
				}
				else {
					frame.setTitle(titlePrefix + " - " + title);
				}
			}

			@Override
			public void statusTextChanged(final String statusText) {
				statusLabel.setText(" " + statusText);
			}

			@Override
			public void loadProgressChanged(final int progress, final int totalAmount) {
				//CHECKSTYLE:OFF
				System.out.println("LOAD PROGRESS CHANGED: " + progress + " / " + totalAmount);
				//CHECKSTYLE:ON
				cancelButton.setEnabled(progress != totalAmount);
			}

			@Override
			public void loadFinished() {
				//CHECKSTYLE:OFF
				System.out.println("LOAD FINISHED");
				//CHECKSTYLE:ON
				cancelButton.setEnabled(false);
			}
		});

		browser.setUrl(HOME_URL);

		frame.setVisible(true);
	}
}
