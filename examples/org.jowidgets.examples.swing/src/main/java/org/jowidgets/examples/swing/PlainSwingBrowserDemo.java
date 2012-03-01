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

package org.jowidgets.examples.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jowidgets.addons.widgets.browser.api.BrowserBPF;
import org.jowidgets.addons.widgets.browser.api.IBrowser;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.examples.common.icons.DemoIconsInitializer;
import org.jowidgets.spi.impl.bridge.swt.awt.common.application.BridgedSwtEventLoop;
import org.jowidgets.spi.impl.swing.addons.SwingToJo;
import org.jowidgets.tools.layout.MigLayoutFactory;

public final class PlainSwingBrowserDemo {

	private PlainSwingBrowserDemo() {}

	public static void main(final String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		DemoIconsInitializer.initialize();

		//the swt event loop must be initialized before the swing context will be created
		final BridgedSwtEventLoop swtEventLoop = new BridgedSwtEventLoop();

		//The swing context must be created before the swt event loops starts
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				runInUiThread(swtEventLoop);
			}
		});

		//The swt event loop will be started after the swing context was created
		swtEventLoop.start();

	}

	private static void runInUiThread(final BridgedSwtEventLoop swtEventLoop) {
		final JFrame frame = new JFrame();
		frame.setSize(1024, 768);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				//the swt event loop must be stopped before the root frame will be disposed
				swtEventLoop.stop();
				frame.dispose();
				System.exit(0);
			}
		});

		final Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());

		final JPanel contentPanel = new JPanel();
		contentPane.add(BorderLayout.CENTER, contentPanel);

		final IComposite contentComposite = SwingToJo.create(contentPanel);
		contentComposite.setLayout(MigLayoutFactory.growingInnerCellLayout());

		final IBrowser browser = contentComposite.add(BrowserBPF.browser(), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
		browser.setUrl("http://www.google.de/");

		//show the frame
		frame.setVisible(true);

	}

}
