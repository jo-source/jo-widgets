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

package org.jowidgets.examples.common.powo;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.widgets.controler.impl.WindowAdapter;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.powo.JoComposite;
import org.jowidgets.tools.powo.JoFrame;
import org.jowidgets.tools.powo.JoRootFrame;
import org.jowidgets.tools.powo.JoTextLabel;

public class PowoDemoApplication implements IApplication {

	private final String frameTitle;

	public PowoDemoApplication(final String frameTitle) {
		super();
		this.frameTitle = frameTitle;
	}

	public void start() {
		Toolkit.getInstance().getApplicationRunner().run(this);
	}

	@Override
	public void start(final IApplicationLifecycle lifecycle) {
		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();

		final JoFrame frame = new JoRootFrame(frameTitle);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed() {
				lifecycle.finish();
			}
		});

		//demonstrates convenient powo use from here
		frame.setLayout(new MigLayoutDescriptor("[grow]", "[][][][][][grow]"));
		frame.add(new JoTextLabel("Test1"), "wrap");
		frame.add(new JoTextLabel("Test2"), "wrap");
		frame.add(new JoTextLabel("Test3"), "wrap");
		frame.add(new JoTextLabel("Test4"), "wrap");
		frame.add(bpF.separator(), "grow, wrap");

		//headless creation of composite2
		final JoComposite composite2 = new JoComposite(bpF.composite("Test"));
		composite2.setLayout(new MigLayoutDescriptor("[][]", "[][][][]"));
		composite2.add(bpF.textLabel("Test5"), "");
		composite2.add(bpF.textLabel("Test6"), "wrap");
		composite2.add(bpF.textLabel("Test7"), "");
		composite2.add(bpF.textLabel("Test8"), "wrap");

		//here composite2 becomes initialized
		frame.add(composite2, "growx, growy");

		frame.setVisible(true);

	}
}
