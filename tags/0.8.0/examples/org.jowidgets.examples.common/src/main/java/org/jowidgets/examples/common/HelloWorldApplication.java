/*
 * Copyright (c) 2012, David Bauknecht
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

import java.util.Date;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.ICheckBox;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.ILabel;
import org.jowidgets.api.widgets.IProgressBar;
import org.jowidgets.api.widgets.ITextArea;
import org.jowidgets.api.widgets.ITextControl;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.ICheckBoxBluePrint;
import org.jowidgets.api.widgets.blueprint.IDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.ILabelBluePrint;
import org.jowidgets.api.widgets.blueprint.IProgressBarBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextAreaBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.IToggleButtonBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.InputChangeEventPolicy;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.tools.widgets.blueprint.BPF;

public class HelloWorldApplication implements IApplication {

	public void start() {
		Toolkit.getApplicationRunner().run(this);
	}

	@Override
	public void start(final IApplicationLifecycle lifecycle) {
		final IFrameBluePrint frameBp = BPF.frame().setTitle("Hello World").setBackgroundColor(Colors.WHITE);
		frameBp.setSize(new Dimension(800, 600));
		frameBp.setPosition(new Position(500, 100));
		final ILabelBluePrint labelBp = BPF.label().setText("Vorname: ").setIcon(IconsSmall.SETTINGS);
		final ILabelBluePrint label2Bp = BPF.label().setText("Name: ");
		final ILabelBluePrint label3Bp = BPF.label().setText("Sonstiges: ");
		final IButtonBluePrint buttonSubmitBp = BPF.button().setText("Submit").setEnabled(false).setToolTipText("Submit");
		final IButtonBluePrint buttonClearBp = BPF.button().setText("Clear").setToolTipText("Clear");
		final IFrame rootFrame = Toolkit.createRootFrame(frameBp, lifecycle);
		final ITextFieldBluePrint textfieldBp = BPF.textField().setMaxLength(20).setBorder(false);
		final ITextFieldBluePrint textfield2Bp = BPF.textField();
		textfield2Bp.setMaxLength(20).setBorder(false).setInputChangeEventPolicy(InputChangeEventPolicy.EDIT_FINISHED);
		final ITextFieldBluePrint textfield3Bp = BPF.textField().setEditable(false).setPasswordPresentation(true);
		final ITextAreaBluePrint textareaBp = BPF.textArea().setFontName("Century Gothic");
		final ICheckBoxBluePrint checkboxBp = BPF.checkBox().setText("Test");
		final IToggleButtonBluePrint toogleButtonBp = BPF.toggleButton().setText("Toggle me");
		final IInputFieldBluePrint<Date> inputFieldDateBp = BPF.inputFieldDate();
		final IInputFieldBluePrint<Integer> inputFieldIntegerNumberBp = BPF.inputFieldIntegerNumber();
		final IProgressBarBluePrint progressBarBp = BPF.progressBar(1, 100);
		final IDialogBluePrint dialogBP = BPF.dialog().setSize(new Dimension(100, 100)).setDecorated(false);

		final IFrame childWindow = rootFrame.createChildWindow(dialogBP);
		rootFrame.setLayout(Toolkit.getLayoutFactoryProvider().migLayoutBuilder().constraints("insets 10 10 10 10").columnConstraints(
				"[grow][grow]").rowConstraints("[]20[]20[]20[]20[]20[]").build());
		final ILabel label1 = rootFrame.add(labelBp, "cell 0 0");
		final ILabel label2 = rootFrame.add(label2Bp, "cell 0 1");
		final ILabel label3 = rootFrame.add(label3Bp, "cell 0 2");
		final ITextControl textfield = rootFrame.add(textfieldBp, "cell 1 0, growx, h 0::");
		final ITextControl textfield2 = rootFrame.add(textfield2Bp, "cell 1 1, growx, h 0::");
		final ITextControl textfield3 = rootFrame.add(textfield3Bp, "cell 1 2, growx, h 0::");
		final IButton buttonsub = rootFrame.add(buttonSubmitBp, "cell 0 3");
		final IButton buttonclear = rootFrame.add(buttonClearBp, "cell 1 3");
		final ITextArea textarea = rootFrame.add(textareaBp, "cell 0 4, span, growx, h 0::");
		final ICheckBox checkbox = rootFrame.add(checkboxBp, "cell 0 5");
		rootFrame.add(inputFieldDateBp, "cell 0 6, growx, h 0::");
		rootFrame.add(toogleButtonBp, "cell 1 5");
		rootFrame.add(inputFieldIntegerNumberBp, "cell 1 6, growx, h 0::");
		final IProgressBar progressBar = rootFrame.add(progressBarBp, "cell 0 7");
		progressBar.setPreferredSize(new Dimension(300, 10));

		checkbox.addInputListener(new IInputListener() {

			@Override
			public void inputChanged() {
				if (checkbox.isSelected()) {
					label1.setVisible(false);
					label2.setVisible(false);
					label3.setVisible(false);
					textfield.setVisible(false);
					textfield2.setVisible(false);
					textfield3.setVisible(false);
					buttonsub.setVisible(false);
					buttonclear.setVisible(false);
					textarea.setVisible(false);
				}
				else {
					label1.setVisible(true);
					label2.setVisible(true);
					label3.setVisible(true);
					textfield.setVisible(true);
					textfield2.setVisible(true);
					textfield3.setVisible(true);
					buttonsub.setVisible(true);
					buttonclear.setVisible(true);
					textarea.setVisible(true);
				}
			}
		});

		final IFocusListener listener = new IFocusListener() {
			@Override
			public void focusLost() {
				if ((!textfield.getText().isEmpty()) && (!textfield2.getText().isEmpty())) {
					textfield3.setEditable(true);
					buttonsub.setEnabled(true);
				}
				else {
					textfield3.setEditable(false);
					buttonsub.setEnabled(false);
				}
				textfield.setBackgroundColor(Colors.ERROR);
			}

			@Override
			public void focusGained() {
				textfield.setBackgroundColor(Colors.GREEN);
			}
		};

		buttonclear.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {

				textfield.setText("");
				textfield2.setText("");
				textfield3.setText("");
				textarea.setText("");
			}
		});

		buttonsub.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				final String tmp = textarea.getText()
					+ "\n"
					+ textfield.getText()
					+ "\n"
					+ textfield2.getText()
					+ "\n"
					+ textfield3.getText();
				textarea.setText(tmp);
				childWindow.setVisible(true);
			}
		});

		textfield2.addInputListener(new IInputListener() {

			@Override
			public void inputChanged() {
				if (textfield2.getText().isEmpty()) {
					buttonsub.setEnabled(false);
				}
				else {
					buttonsub.setEnabled(true);
				}
				textarea.setText("" + textfield2.getText().length());

			}
		});

		textfield.addFocusListener(listener);
		textfield2.addFocusListener(listener);
		rootFrame.setVisible(true);
	}
}
