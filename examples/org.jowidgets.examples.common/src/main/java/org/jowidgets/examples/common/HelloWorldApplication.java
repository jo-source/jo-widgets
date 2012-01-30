/*
 * Copyright (c) 2012, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.examples.common;

import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.ILabel;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.ILabelBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.tools.widgets.blueprint.BPF;

public class HelloWorldApplication implements IApplication {

	public void start() {
		Toolkit.getApplicationRunner().run(this);
	}

	@Override
	public void start(final IApplicationLifecycle lifecycle) {
		final IFrameBluePrint frameBp = BPF.frame().setTitle("Hello World");
		frameBp.setSize(new Dimension(800, 600));
		frameBp.setPosition(new Position(200, 200));
		final ILabelBluePrint labelBp = BPF.label().setText("     It works!").setToolTipText("Great").setFontSize(15).setIcon(
				IconsSmall.SETTINGS);
		final IButtonBluePrint buttonBp = BPF.button();
		final IButtonBluePrint buttonBp2 = BPF.button();
		final IFrame rootFrame = Toolkit.createRootFrame(frameBp, lifecycle);

		//		rootFrame.setLayout(Toolkit.getLayoutFactoryProvider().nullLayout());

		final ILabel label = rootFrame.add(labelBp);
		final IButton button = rootFrame.add(buttonBp);
		final IButton button2 = rootFrame.add(buttonBp2);
		rootFrame.setVisible(true);
		label.setSize(500, 100);
		button.setPosition(100, 200);
		button.setSize(100, 100);
		button.setMaxSize(new Dimension(100, 200));
		button.setMinSize(new Dimension(50, 100));
		button.setText("aaaaa");
		button.setToolTipText(button.getPreferredSize().toString()
			+ " \n Max: "
			+ button.getMaxSize().toString()
			+ " \n Min: "
			+ button.getMaxSize().toString());
		button2.setSize(100, 100);
		button2.setPosition(350, 10);
		button2.setToolTipText(button2.getPreferredSize().toString()
			+ " \n Max: "
			+ button2.getMaxSize().toString()
			+ " \n Min: "
			+ button2.getMaxSize().toString());

	}
}
