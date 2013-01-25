/*
 * Copyright (c) 2013, grossmann
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

import org.jowidgets.addons.icons.silkicons.SilkIcons;
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.IToolBarButton;
import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.powo.JoFrame;
import org.jowidgets.tools.widgets.blueprint.BPF;

public class DemoExpandComposite extends JoFrame {

	public DemoExpandComposite() {
		super("Expand composite demo");

		setLayout(new MigLayoutDescriptor("wrap", "[grow, 0::]", "[]0[grow, 0::]"));

		final ColorValue grey = new ColorValue(220, 220, 220);
		final ColorValue blue = new ColorValue(0, 0, 170);

		final IComposite top = add(BPF.composite().setBackgroundColor(grey), "growx, w 0::");
		top.setLayout(new MigLayoutDescriptor("[][grow, 0::][]", "[]"));

		top.add(BPF.icon(SilkIcons.HELP));

		top.add(BPF.textLabel("Help").setMarkup(Markup.STRONG).setColor(blue).alignLeft());

		final IToolBar toolBar = top.add(BPF.toolBar().setBackgroundColor(grey));
		final IToolBarButton button = toolBar.addItem(BPF.toolBarButton().setIcon(IconsSmall.EXPAND_DOWN));

		final IComposite composite = add(BPF.composite().setBorder(), "aligny t, growx, w 0::");
		composite.setLayout(new MigLayoutDescriptor("[]", "[]"));
		final StringBuilder helpText = new StringBuilder();
		helpText.append("Important for propper setup:\n\n");
		helpText.append("- Connect your MIDI device before power on\n");
		helpText.append("- Lower the volume before power on the amplifter\n");
		helpText.append("- Use only short cables < 1,5 m\n");
		helpText.append("- In case of emergency cool down\n");

		composite.add(BPF.textLabel(helpText.toString()).setColor(blue));
		composite.setVisible(false);

		button.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				composite.setVisible(!composite.isVisible());
				if (composite.isVisible()) {
					button.setIcon(IconsSmall.EXPAND_UP);
				}
				else {
					button.setIcon(IconsSmall.EXPAND_DOWN);
				}
			}
		});
	}

}
