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
import org.jowidgets.api.layout.BorderLayoutConstraints;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.ILabel;
import org.jowidgets.api.widgets.ITextControl;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.ILabelBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextFieldBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.tools.widgets.blueprint.BPF;

public class HelloWorldApplication implements IApplication {

	public void start() {
		Toolkit.getApplicationRunner().run(this);
	}

	@Override
	public void start(final IApplicationLifecycle lifecycle) {
		final IFrameBluePrint frameBp = BPF.frame().setTitle("Hello World");
		frameBp.setSize(new Dimension(300, 300));
		frameBp.setPosition(new Position(200, 200));
		final ILabelBluePrint labelBp = BPF.label().setText("     It works!").setToolTipText("Great").setFontSize(15).setIcon(
				IconsSmall.SETTINGS);
		final IButtonBluePrint buttonBp = BPF.button();
		final IButtonBluePrint buttonBp2 = BPF.button();
		final IFrame rootFrame = Toolkit.createRootFrame(frameBp, lifecycle);
		final ITextFieldBluePrint textfieldBp = BPF.textField();
		rootFrame.setLayout(Toolkit.getLayoutFactoryProvider().borderLayout());
		final IButton button2 = rootFrame.add(buttonBp2, BorderLayoutConstraints.TOP);
		final IButton button = rootFrame.add(buttonBp, BorderLayoutConstraints.LEFT);
		final ILabel label = rootFrame.add(labelBp, BorderLayoutConstraints.RIGHT);
		final ITextControl textfield = rootFrame.add(textfieldBp, BorderLayoutConstraints.BOTTOM);
		button2.setText("On/Off");
		label.setPosition(100, 300);
		label.setCursor(Cursor.WAIT);
		//		button.setPosition(100, 200);
		//		button.setPreferredSize(new Dimension(100, 100));
		button.setMaxSize(new Dimension(100, 200));
		button.setMinSize(new Dimension(50, 100));
		final IActionListener listener = new IActionListener() {

			@Override
			public void actionPerformed() {
				button2.setSize(button2.getSize().getWidth() + 3, button2.getSize().getHeight() + 3);
				button2.setToolTipText("Pref: "
					+ button2.getPreferredSize().toString()
					+ " \nMax: "
					+ button2.getMaxSize().toString()
					+ " \nMin: "
					+ button2.getMaxSize().toString()
					+ " \nSize: "
					+ button2.getSize().toString());

			}
		};
		button.addActionListener(listener);

		button2.addActionListener(new IActionListener() {

			private boolean flag = true;

			@Override
			public void actionPerformed() {

				if (flag) {
					button.removeActionListener(listener);
					flag = false;
					textfield.setText("" + textfield.requestFocus());
				}
				else {
					button.addActionListener(listener);
					flag = true;
					textfield.setText("" + textfield.hasFocus());
				}

			}
		});
		//		textfield.setPosition(500, 500);

		rootFrame.setVisible(true);
		//		button.setSize(100, 100);

		button.setToolTipText("Pref: "
			+ button.getPreferredSize().toString()
			+ " \nMax: "
			+ button.getMaxSize().toString()
			+ " \nMin: "
			+ button.getMaxSize().toString()
			+ " \nSize: "
			+ button.getSize().toString());

	}
}
