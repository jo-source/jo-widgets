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
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;

public final class DemoLayoutComposite {

	public DemoLayoutComposite(final IContainer parentContainer) {

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		parentContainer.setLayout(new MigLayoutDescriptor("[300::, grow]", ""));

		final IButton nullLayoutButton = parentContainer.add(bpf.button("Null layout"), "grow, sg bg, wrap");
		nullLayoutButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrame frame = new DemoNullLayoutFrame();
				frame.setVisible(true);
			}
		});

		final IButton preferredSizeLayoutButton = parentContainer.add(bpf.button("Preffered size layout"), "grow, sg bg, wrap");
		preferredSizeLayoutButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrame frame = new DemoPreferredSizeLayoutFrame();
				frame.setVisible(true);
			}
		});

		final IButton flowLayoutButton = parentContainer.add(bpf.button("Flow layout"), "grow, sg bg, wrap");
		flowLayoutButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrame frame = new DemoFlowLayoutFrame();
				frame.setVisible(true);
			}
		});

		final IButton fillLayoutButton = parentContainer.add(bpf.button("Fill layout"), "grow, sg bg, wrap");
		fillLayoutButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrame frame = new DemoFillLayoutFrame();
				frame.setVisible(true);
			}
		});

		final IButton fillLayoutMarginButton = parentContainer.add(bpf.button("Fill layout (margin)"), "grow, sg bg, wrap");
		fillLayoutMarginButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrame frame = new DemoFillLayoutMarginFrame();
				frame.setVisible(true);
			}
		});

		final IButton borderLayoutButton = parentContainer.add(bpf.button("Border layout"), "grow, sg bg, wrap");
		borderLayoutButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrame frame = new DemoBorderLayoutFrame();
				frame.setVisible(true);
			}
		});

		final IButton borderLayoutButton2 = parentContainer.add(bpf.button("Border layout 2"), "grow, sg bg, wrap");
		borderLayoutButton2.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrame frame = new DemoBorderLayoutFrame2();
				frame.setVisible(true);
			}
		});

		final IButton migLayoutButton = parentContainer.add(bpf.button("MigLayout"), "grow, sg bg, wrap");
		migLayoutButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrame frame = new DemoMigLayoutFrame();
				frame.setVisible(true);
			}
		});
	}

	public void foo() {}
}
