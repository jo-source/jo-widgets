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

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IScrollComposite;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controler.WindowAdapter;
import org.jowidgets.tools.powo.JoFrame;
import org.jowidgets.util.ValueHolder;

public class ScrollCompositeDemoFrame extends JoFrame {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

	public ScrollCompositeDemoFrame() {
		super("Scroll composite demo");

		final ValueHolder<Boolean> finish = new ValueHolder<Boolean>(Boolean.FALSE);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed() {
				finish.set(Boolean.TRUE);
			};
		});

		setLayout(new MigLayoutDescriptor("[grow]", "grow"));
		final IScrollComposite scrollComposite = add(BPF.scrollComposite(), "growx, growy, w 0::, h 0::");

		final Runnable updater = new Runnable() {

			@Override
			public void run() {
				final ValueHolder<Integer> index = new ValueHolder<Integer>(Integer.valueOf(0));

				while (!finish.get().booleanValue()) {

					Toolkit.getUiThreadAccess().invokeLater(new Runnable() {
						@Override
						public void run() {
							scrollComposite.layoutBegin();
							scrollComposite.add(BPF.textLabel("Label: " + index.get().intValue()), "wrap");
							scrollComposite.layoutEnd();
						}
					});

					index.set(Integer.valueOf(index.get().intValue() + 1));
					try {
						Thread.sleep(1000);
					}
					catch (final InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}

		};

		new Thread(updater).start();
	}

}
