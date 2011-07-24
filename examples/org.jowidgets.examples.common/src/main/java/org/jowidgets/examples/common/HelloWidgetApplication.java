/*
 * Copyright (c) 2010, Michael Grossmann
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

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.types.AutoCenterPolicy;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IInputComponent;
import org.jowidgets.api.widgets.IInputDialog;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputComponentValidationLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IScrollCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.blueprint.factory.ISimpleBluePrintFactory;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.widgets.IActionWidgetCommon;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.examples.common.icons.DemoIconsInitializer;

public class HelloWidgetApplication implements IApplication {

	private final String rootFrameTitle;
	private IFrame rootFrame;

	public HelloWidgetApplication(final String title) {
		this.rootFrameTitle = title;
	}

	public void start() {
		DemoIconsInitializer.initialize();
		Toolkit.getApplicationRunner().run(this);
	}

	@Override
	public void start(final IApplicationLifecycle lifecycle) {

		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();

		// create the root window
		final IFrameBluePrint frameBp = bpF.frame().setTitle(rootFrameTitle);
		frameBp.setMigLayout("[left, grow]", "[top, grow]");
		rootFrame = Toolkit.createRootFrame(frameBp, lifecycle);

		// create dialog
		final IInputDialog<String> dialog = createDialogWidget(bpF);

		// create the scrolled content
		final IScrollCompositeBluePrint scrollCompositeBp = bpF.scrollComposite("Titled border");
		scrollCompositeBp.setMigLayout("[][grow, 250:250:800][260::]", "[]5[]5[]5[][][][][]");
		final IComposite group = rootFrame.add(scrollCompositeBp, "grow");

		// base descriptor for left labels
		final ITextLabelBluePrint labelBp = bpF.textLabel().alignRight();

		final IInputComponentValidationLabelBluePrint validationLabelBp = bpF.inputComponentValidationLabel();

		// row1
		group.add(labelBp.setText("Number 1").setToolTipText("Very useful numbers here"), "sg lg");
		final IInputComponent<Long> widget1 = group.add(bpF.inputFieldLongNumber(), "growx");
		group.add(validationLabelBp.setInputComponent(widget1), "wrap");

		// row2
		group.add(labelBp.setText("Number 2").setToolTipText("Very very useful numbers here"), "sg lg");
		final IInputComponent<Integer> widget2 = group.add(bpF.inputFieldIntegerNumber(), "growx");
		group.add(validationLabelBp.setInputComponent(widget2), "wrap");

		// row3
		group.add(labelBp.setText("String").setToolTipText("Very special input here"), "sg lg");
		final IInputComponent<String> widget3 = group.add(bpF.inputFieldString(), "growx");
		group.add(validationLabelBp.setInputComponent(widget3), "wrap");

		// row4
		group.add(
				bpF.textSeparator("Integrated input compsosite", "The tooltip text").alignCenter().setStrong(),
				"grow, span, wrap");

		// row5
		final IInputCompositeBluePrint<String> inputCompositeBluePrint = bpF.inputComposite(new HelloContentCreator());
		inputCompositeBluePrint.setContentScrolled(false).setContentBorder();
		inputCompositeBluePrint.setMissingInputHint("Do input all mandatory(*) fields!");
		group.add(inputCompositeBluePrint, "grow, span, wrap");

		// row6
		group.add(bpF.textSeparator("Button follows", "The tooltip text").alignCenter(), "grow, span, wrap");

		// row7
		group.add(labelBp.setText("Open dialog").setToolTipText("This opens an dialog"), "sg lg");
		final IActionWidgetCommon button = group.add(bpF.button("Open"), "growx");

		// open dialog on button
		button.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				dialog.setVisible(true);
			}
		});

		rootFrame.setVisible(true);
	}

	private IInputDialog<String> createDialogWidget(final ISimpleBluePrintFactory bpF) {
		final IInputDialogBluePrint<String> inputDialogBp = bpF.inputDialog(new HelloContentCreator());
		inputDialogBp.setTitle("Test dialog").setAutoCenterPolicy(AutoCenterPolicy.ONCE);
		inputDialogBp.setOkButton("very ok", "This is very ok");
		inputDialogBp.setCancelButton("cancel user input", "cancel the current user input");
		inputDialogBp.setMissingInputHint("Do input all mandatory(*) fields!");
		inputDialogBp.setContentBorder().setContentScrolled(true);
		final IInputDialog<String> dialog = rootFrame.createChildWindow(inputDialogBp);

		return dialog;
	}

	public IFrame getRootFrame() {
		return rootFrame;
	}

}
