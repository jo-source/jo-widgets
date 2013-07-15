/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.examples.common.demo;

import java.util.concurrent.atomic.AtomicBoolean;

import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IProgressBar;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.blueprint.IProgressBarBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controller.WindowAdapter;

public class DemoProgressBar {

	public DemoProgressBar(final IContainer parentContainer, final IWindow parentWindow) {

		final AtomicBoolean windowActive = new AtomicBoolean();
		final AtomicBoolean progressBarFinished = new AtomicBoolean();
		final IProgressBar progressBar;

		final int max = (int) (Math.random() * 200) + 100;
		final boolean indetermined = Math.random() > 0.7;

		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();

		parentContainer.setLayout(new MigLayoutDescriptor("0[grow][]0", "0[]0"));

		final IProgressBarBluePrint progressBarPb = bpF.progressBar().setIndeterminate(indetermined).setMaximum(max);

		progressBar = parentContainer.add(progressBarPb, "growx,w 300::, h 22:22:22");

		final IButton buttonWidget = parentContainer.add(bpF.button().setIcon(IconsSmall.ERROR), " h 25:25:25, w 25:25:25, wrap");

		buttonWidget.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				progressBarFinished.set(true);
				progressBar.setProgress(0);
			}
		});

		if (!indetermined) {

			parentWindow.addWindowListener(new WindowAdapter() {

				@Override
				public void windowActivated() {
					if (windowActive.getAndSet(true) || progressBarFinished.get()) {
						return;
					}
					progressBarFinished.set(false);
					final IUiThreadAccess uiThreadAccess = Toolkit.getUiThreadAccess();
					new Thread(new Runnable() {

						private int i = 0;

						@Override
						public void run() {
							for (i = 0; i <= max && windowActive.get() && !progressBarFinished.get(); i++) {
								uiThreadAccess.invokeLater(new Runnable() {

									@Override
									public void run() {
										if (windowActive.get() && !progressBarFinished.get()) {
											progressBar.setProgress(i);
										}
									}
								});

								try {
									Thread.sleep(200);
								}
								catch (final InterruptedException e) {
									throw new RuntimeException(e);
								}
							}
						}

					}).start();
				}

				@Override
				public void windowClosed() {
					windowActive.set(false);
				}

			});
		}

	}
}
