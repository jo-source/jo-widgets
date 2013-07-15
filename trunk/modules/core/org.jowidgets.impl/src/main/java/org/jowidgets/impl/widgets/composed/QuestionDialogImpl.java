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
package org.jowidgets.impl.widgets.composed;

import org.jowidgets.api.types.QuestionResult;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IQuestionDialog;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.api.widgets.descriptor.setup.IQuestionDialogSetup;
import org.jowidgets.common.widgets.IButtonCommon;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.composed.blueprint.BluePrintFactory;
import org.jowidgets.tools.controller.WindowAdapter;
import org.jowidgets.tools.widgets.wrapper.ComponentWrapper;

public class QuestionDialogImpl extends ComponentWrapper implements IQuestionDialog {

	private final IFrame dialog;
	private IButtonCommon defaultButton;
	private boolean wasVisible;
	private QuestionResult result;

	public QuestionDialogImpl(final IFrame dialog, final IQuestionDialogSetup setup) {
		super(dialog);

		this.wasVisible = false;
		this.dialog = dialog;

		final IBluePrintFactory bpF = new BluePrintFactory();

		if (setup.getIcon() != null) {
			this.dialog.setLayout(new MigLayoutDescriptor("[]20[grow]", "15[][]"));
			this.dialog.add(bpF.icon(setup.getIcon()), "");
		}
		else {
			this.dialog.setLayout(new MigLayoutDescriptor("[grow]", "15[][]"));
		}

		this.dialog.add(bpF.textLabel(setup.getText(), setup.getToolTipText()), "wrap");

		// buttons
		final IComposite buttonBar = dialog.add(bpF.composite(), "span, align center");
		buttonBar.setLayout(new MigLayoutDescriptor("[][][]", "[]"));

		final String buttonCellConstraints = "w 80::, sg bg";

		result = setup.getDefaultResult();

		final IButtonCommon yesButton = buttonBar.add(setup.getYesButton(), buttonCellConstraints);
		yesButton.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				result = QuestionResult.YES;
				dialog.setVisible(false);
			}
		});
		if (setup.getDefaultResult() == null) {
			result = QuestionResult.YES;
		}
		if (QuestionResult.YES == result) {
			defaultButton = yesButton;
		}

		if (setup.getNoButton() != null) {
			final IButtonCommon noButton = buttonBar.add(setup.getNoButton(), buttonCellConstraints);
			noButton.addActionListener(new IActionListener() {

				@Override
				public void actionPerformed() {
					result = QuestionResult.NO;
					dialog.setVisible(false);
				}
			});

			if (setup.getDefaultResult() == null) {
				result = QuestionResult.NO;
			}
			if (QuestionResult.NO == result) {
				defaultButton = noButton;
			}
		}

		if (setup.getCancelButton() != null) {
			final IButtonCommon cancelButton = buttonBar.add(setup.getCancelButton(), buttonCellConstraints);
			cancelButton.addActionListener(new IActionListener() {

				@Override
				public void actionPerformed() {
					result = QuestionResult.CANCEL;
					dialog.setVisible(false);
				}
			});

			if (setup.getDefaultResult() == null) {
				result = QuestionResult.CANCEL;
			}
			if (QuestionResult.CANCEL == result) {
				defaultButton = cancelButton;
			}

		}

		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated() {
				defaultButton.requestFocus();
			}
		});

		ColorSettingsInvoker.setColors(setup, this);
	}

	@Override
	public IWindow getParent() {
		return dialog.getParent();
	}

	@Override
	public void setParent(final IWindow parent) {
		dialog.setParent(parent);
	}

	@Override
	public QuestionResult question() {
		if (!wasVisible) {
			wasVisible = true;
			dialog.setVisible(true);
			//ui block until user closes the dialog

			//after that dispose the message dialog
			dialog.dispose();
		}
		else {
			throw new IllegalStateException("A message dialog can only be shown once!");
		}
		return result;
	}

	@Override
	public void setVisible(final boolean visible) {
		if (visible) {
			question();
		}
		else {
			dialog.dispose();
		}
	}

}
