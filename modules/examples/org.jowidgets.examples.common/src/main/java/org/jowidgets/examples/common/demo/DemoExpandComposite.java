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
import org.jowidgets.api.controller.IExpandListener;
import org.jowidgets.api.widgets.IExpandComposite;
import org.jowidgets.api.widgets.blueprint.IExpandCompositeBluePrint;
import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.powo.JoFrame;
import org.jowidgets.tools.widgets.blueprint.BPF;

public class DemoExpandComposite extends JoFrame {

	private static final ColorValue BLUE = new ColorValue(0, 0, 170);

	public DemoExpandComposite() {
		super("Expand composite demo");
		setLayout(new MigLayoutDescriptor("wrap", "[grow, 0::]", "[][]"));

		final StringBuilder helpText = new StringBuilder();
		helpText.append("Programmer: Joe Estrada\n");
		helpText.append("Mood: Well\n");
		helpText.append("Lines of code: Unkown\n");
		helpText.append("Clean: Hope so!\n");
		addExpandComposite(SilkIcons.HELP, "About", helpText.toString());

		final StringBuilder infoText = new StringBuilder();
		infoText.append("Important for proper setup:\n\n");
		infoText.append("- Connect your MIDI device before power on\n");
		infoText.append("- Lower the volume before power on the amplifter\n");
		infoText.append("- Use only short cables < 1,5 m\n");
		infoText.append("- In case of emergency cool down\n");
		addExpandComposite(SilkIcons.INFORMATION, "Information", infoText.toString());

		addExpandComposite(SilkIcons.INFORMATION, "Same information", infoText.toString());
	}

	private void addExpandComposite(final IImageConstant icon, final String header, final String text) {
		final IExpandCompositeBluePrint expandCompositeBp = BPF.expandComposite();
		expandCompositeBp.setText(header).setIcon(icon).setTextColor(BLUE).setTextMarkup(Markup.STRONG);
		final IExpandComposite expandComposite = add(expandCompositeBp, "growx, w 0::");
		expandComposite.setLayout(new MigLayoutDescriptor("[]", "[]"));
		expandComposite.add(BPF.textLabel(text).setColor(BLUE));

		expandComposite.addExpandListener(new IExpandListener() {
			@Override
			public void expandedChanged(final boolean expanded) {
				layout();
				//CHECKSTYLE:OFF
				System.out.println("Expanded " + header + ": " + expanded);
				//CHECKSTYLE:ON
			}
		});
	}

}
