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

import org.jowidgets.api.layout.ILayoutFactoryProvider;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.ITextArea;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.tools.model.item.MenuBarModel;
import org.jowidgets.tools.powo.JoFrame;

public class DemoFillLayoutFrame extends JoFrame {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

	public DemoFillLayoutFrame() {
		super("Fill layout demo");

		final IMenuBarModel menuBarModel = new MenuBarModel();
		menuBarModel.addMenu("File");
		setMenuBar(menuBarModel);

		final ILayoutFactoryProvider lfp = Toolkit.getLayoutFactoryProvider();

		final ILayouter layouter = setLayout(lfp.fillLayoutBuilder().build());
		final ITextArea textArea = add(BPF.textArea().setBorder(false));

		final StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < 50; i++) {
			stringBuilder.append("Text area in a border layout. ");
		}
		textArea.setText(stringBuilder.toString());

		setSize(500, 400);
		setMinSize(layouter.getMinSize());
	}
}
