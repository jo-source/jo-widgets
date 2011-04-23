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
import org.jowidgets.api.model.table.ISimpleTableModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.ITextArea;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.tools.model.table.SimpleTableModel;
import org.jowidgets.tools.powo.JoFrame;

public class DemoFillLayoutMarginFrame extends JoFrame {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

	public DemoFillLayoutMarginFrame() {
		super("Fill layout (margin) demo");

		final ILayoutFactoryProvider lfp = Toolkit.getLayoutFactoryProvider();

		final ILayouter layouter = setLayout(lfp.fillLayoutBuilder().margin(100).build());

		//addTextArea(this);
		addTable(this);

		setClientAreaMinSize(layouter.getMinSize());
		pack();
		//setSize(500, 400);
	}

	@SuppressWarnings("unused")
	private void addTextArea(final IContainer container) {
		final ITextArea textArea = container.add(BPF.textArea());

		final StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < 50; i++) {
			stringBuilder.append("Text area in a fill layout. ");
		}
		textArea.setText(stringBuilder.toString());
	}

	private void addTable(final IContainer container) {
		final ISimpleTableModel tableModel = new SimpleTableModel();
		for (int i = 0; i < 10; i++) {
			tableModel.addColumn("Column " + i);
			tableModel.getColumn(i).setWidth(100);
		}
		for (int i = 0; i < 20; i++) {
			tableModel.addRow();
			for (int j = 0; j < 10; j++) {
				tableModel.setCellText(i, j, "Cell (" + i + " / " + j + ")");
			}
		}

		container.add(BPF.table(tableModel).setBorder(true));

	}
}
