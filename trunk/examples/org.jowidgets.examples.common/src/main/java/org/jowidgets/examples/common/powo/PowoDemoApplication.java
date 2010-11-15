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

import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrameWidget;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.impl.WindowAdapter;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.powo.JoButton;
import org.jowidgets.tools.powo.JoComposite;
import org.jowidgets.tools.powo.JoDialog;
import org.jowidgets.tools.powo.JoFrame;
import org.jowidgets.tools.powo.JoIcon;
import org.jowidgets.tools.powo.JoProgressBar;
import org.jowidgets.tools.powo.JoSplitComposite;
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

		final JoFrame frame = new JoFrame(frameTitle);
		final JoDialog dialog = createDialog(frame);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed() {
				lifecycle.finish();
			}
		});

		frame.setLayout(new MigLayoutDescriptor("[grow]", "[][][][][][grow]"));
		frame.add(new JoTextLabel("Test1"), "wrap");
		frame.add(new JoTextLabel("Test2"), "wrap");
		frame.add(new JoTextLabel("Test3"), "wrap");
		frame.add(new JoTextLabel("Test4"), "wrap");
		frame.add(bpF.separator(), "grow, wrap");

		//headless creation of composite2
		final JoComposite composite2 = new JoComposite(bpF.composite("Test"));
		composite2.setLayout(new MigLayoutDescriptor("[][grow]", "[][][][]"));
		composite2.add(new JoIcon(IconsSmall.INFO), "");
		composite2.add(JoTextLabel.bluePrint("Test6"), "wrap");
		composite2.add(new JoIcon(IconsSmall.INFO), "");
		composite2.add(bpF.textLabel("Test8"), "wrap");

		composite2.add(new JoIcon(IconsSmall.ERROR), "");
		final JoProgressBar progressBar = new JoProgressBar(100);
		composite2.add(progressBar, "growx, wrap");
		progressBar.setProgress(35);

		final JoButton button = new JoButton("open dialog");
		composite2.add(button, "grow, span2");

		//here composite2 becomes initialized
		frame.add(composite2, "growx, growy");

		frame.setVisible(true);

		button.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				dialog.setVisible(true);
			}
		});
	}

	private JoDialog createDialog(final IFrameWidget parent) {

		final JoDialog result = new JoDialog(parent, JoDialog.bluePrint("test").autoPackOff());
		result.setLayout(new MigLayoutDescriptor("0[grow]0", "0[grow]0"));

		final JoSplitComposite split = new JoSplitComposite(JoSplitComposite.bluePrint().resizeSecondPolicy());
		split.getFirst().add(new JoTextLabel("Content1"));
		split.getSecond().add(new JoTextLabel("Content2"));

		result.add(split, "growx, growy");
		result.setSize(new Dimension(800, 600));
		return result;
	}
}
