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

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IPopupDialog;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controler.IMouseButtonEvent;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.controler.MouseAdapter;
import org.jowidgets.tools.powo.JoFrame;

public class PopupDialogDemoFrame extends JoFrame {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

	private IPopupDialog popupDialog;

	public PopupDialogDemoFrame() {
		super("Popup dialog demo");

		setLayout(new MigLayoutDescriptor("[grow]", "[][]"));

		final IButton button1 = add(BPF.button("Open popup window..."), "wrap, sg bg");
		button1.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final IMouseButtonEvent event) {
				popupDialog = createChildWindow(BPF.popupDialog());
				final Dimension buttonSize = button1.getSize();
				popupDialog.setBackgroundColor(Colors.WHITE);
				popupDialog.setSize(new Dimension(buttonSize.getWidth(), 200));
				popupDialog.setPosition(Toolkit.toScreen(button1.getPosition(), PopupDialogDemoFrame.this));
				popupDialog.setVisible(true);
			}

		});

		final IButton button2 = add(BPF.button("Open / Close popup window..."), "sg bg");
		button2.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final IMouseButtonEvent event) {
				popupDialog = createChildWindow(BPF.popupDialog().setAutoDispose(false));
				final Dimension buttonSize = button2.getSize();
				final Position buttonPos = button2.getPosition();
				popupDialog.setBackgroundColor(Colors.WHITE);
				popupDialog.setSize(new Dimension(buttonSize.getWidth(), 150));
				popupDialog.setPosition(Toolkit.toScreen(
						new Position(buttonPos.getX(), buttonPos.getY() + buttonSize.getHeight()),
						PopupDialogDemoFrame.this));
				popupDialog.setVisible(true);
			}

			@Override
			public void mouseReleased(final IMouseButtonEvent mouseEvent) {
				if (popupDialog != null) {
					popupDialog.dispose();
				}
			}

		});

	}
}
