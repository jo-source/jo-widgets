/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.examples.common.workbench.widgets.views;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.blueprint.ILabelBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;

public class LabelKitchensinkView extends AbstractHowToView implements IView {

	public static final String ID = LabelKitchensinkView.class.getName();
	public static final String DEFAULT_LABEL = "Labels Kitchensink";

	public LabelKitchensinkView(final IViewContext context) {
		super(context);
	}

	@Override
	public void createViewContent(final IContainer container, final IBluePrintFactory bpFactory) {
		//set the layout
		container.setLayout(new MigLayoutDescriptor("[]", ""));

		//create the labels blue prints
		final ILabelBluePrint labelBp = bpFactory.label();
		final ILabelBluePrint strongLabelBp = bpFactory.label().setMarkup(Markup.STRONG);
		final ILabelBluePrint emphasizedLabelBp = bpFactory.label().setMarkup(Markup.EMPHASIZED);
		final ILabelBluePrint redLabelBp = bpFactory.label().setColor(new ColorValue(255, 0, 0));
		final ILabelBluePrint strongColorLabelBp = bpFactory.label().setColor(Colors.STRONG);

		//add the label blue prints to the container
		container.add(strongLabelBp.setText("Strong markup"), "wrap");
		container.add(emphasizedLabelBp.setText("Emphasized markup"), "wrap");
		container.add(redLabelBp.setText("Red label"), "wrap");
		container.add(strongColorLabelBp.setText("Label with logical color 'Strong'"), "wrap");
		container.add(labelBp.setText("Next label is empty"), "wrap");
		container.add(labelBp.setText(""), "wrap");
		container.add(bpFactory.label().setText("Label with tooltip").setToolTipText("The tooltip of the label"), "wrap");
		container.add(
				labelBp.setText("First line of a multi line label\nSecond line of a multi line label\nThird line of a multi line label"),
				"wrap");
	}
}
