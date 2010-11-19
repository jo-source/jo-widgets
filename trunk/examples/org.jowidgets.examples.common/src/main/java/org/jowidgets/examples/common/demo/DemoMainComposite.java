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

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButtonWidget;
import org.jowidgets.api.widgets.IFrameWidget;
import org.jowidgets.api.widgets.IInputDialogWidget;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.IContainerWidgetCommon;
import org.jowidgets.common.widgets.IWindowWidgetCommon;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;

public class DemoMainComposite {

	public DemoMainComposite(final IContainerWidgetCommon parentContainer) {

		final IWindowWidgetCommon parentWindow = Toolkit.getWidgetUtils().getWindowAncestor(parentContainer);

		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();

		parentContainer.setLayout(new MigLayoutDescriptor("[300::, grow]", "[][][][]"));

		final IInputDialogWidget<String> inputDialog1 = new DemoInputDialog1(parentContainer).getInputDialog();
		final IButtonWidget inputDialog1Button = parentContainer.add(
				bpF.button("Input dialog demo", "Shows an simple input dialog"),
				"grow, sg bg, wrap");
		inputDialog1Button.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				inputDialog1.setVisible(true);
			}
		});

		final IButtonWidget splitDemoButton = parentContainer.add(
				bpF.button("Split demo", "Shows an frame with split composites"),
				"grow, sg bg, wrap");
		splitDemoButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrameWidget splitDemoFrame = parentWindow.createChildWindow(bpF.frame("Split demo").autoPackOff());
				splitDemoFrame.setSize(new Dimension(800, 600));
				new DemoSplitComposite(splitDemoFrame);
				splitDemoFrame.setVisible(true);
			}
		});

		final IButtonWidget progressBarDialogButton = parentContainer.add(
				bpF.button("Progress bar demo", "Opens the progress bar demo"),
				"grow, sg bg, wrap");
		progressBarDialogButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrameWidget progressBarDialog = parentWindow.createChildWindow(bpF.dialog("Progress bar demo"));
				new DemoProgressBarComposite(progressBarDialog, progressBarDialog);
				progressBarDialog.setVisible(true);
				progressBarDialog.close();
			}
		});

		final IFrameWidget messagesDemoDialog = parentWindow.createChildWindow(bpF.dialog("Messages demo"));
		new DemoMessagesComposite(messagesDemoDialog);
		final IButtonWidget messagesDialogButton = parentContainer.add(
				bpF.button("Messages demo", "Opens the messages demo"),
				"grow, sg bg, wrap");
		messagesDialogButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				messagesDemoDialog.setVisible(true);
			}
		});

	}

}
