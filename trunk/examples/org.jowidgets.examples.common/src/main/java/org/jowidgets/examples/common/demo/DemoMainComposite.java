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

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IInputDialog;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.blueprint.ICollectionInputDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IInputFieldBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.IValidator;
import org.jowidgets.validation.ValidationResult;

public final class DemoMainComposite {

	private final IContainer parentContainer;

	public DemoMainComposite(final IContainer parentContainer) {
		this.parentContainer = parentContainer;

		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();

		parentContainer.setLayout(new MigLayoutDescriptor("[300::, grow]", "[][][][]"));

		final IInputDialog<List<String>> inputDialog1 = new DemoInputDialog1(getParentWindow()).getInputDialog();
		final IButton inputDialog1Button = parentContainer.add(
				bpF.button("Input dialog demo", "Shows an simple input dialog"),
				"grow, sg bg, wrap");
		inputDialog1Button.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				inputDialog1.setValue(null);
				inputDialog1.setVisible(true);
			}
		});

		final IButton listInputDemoButton = parentContainer.add(bpF.button("List input demo"), "grow, sg bg, wrap");
		listInputDemoButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {

				final IInputFieldBluePrint<Integer> inputFieldBp = bpF.inputFieldIntegerNumber().setValidator(
						new IValidator<Integer>() {
							@Override
							public IValidationResult validate(final Integer value) {
								if (value == null) {
									return ValidationResult.create().withError("Must not be empty");
								}
								if (value != null && value == 303) {
									return ValidationResult.create().withError("Cool numbers are not supported");
								}
								else {
									return ValidationResult.ok();
								}
							}
						});

				final ICollectionInputDialogBluePrint<Integer> dialogBp = bpF.collectionInputDialog(inputFieldBp);
				dialogBp.setValidator(new IValidator<Collection<Integer>>() {

					@Override
					public IValidationResult validate(final Collection<Integer> value) {
						if (value == null || value.size() == 0) {
							return ValidationResult.error("Input must not be empty");
						}
						else {
							if (value.size() != new HashSet<Integer>(value).size()) {
								return ValidationResult.error("Input must not contain dublicates");
							}
						}
						return null;
					}
				});

				final List<Integer> values = new LinkedList<Integer>();
				values.add(null);

				dialogBp.setValue(values);
				dialogBp.setMissingInputHint("Please edit the list");

				final IInputDialog<Collection<Integer>> inputDialog = getParentWindow().createChildWindow(dialogBp);

				inputDialog.setSize(new Dimension(300, 300));
				inputDialog.setVisible(true);
			}
		});

		final IButton expandCompositeButton = parentContainer.add(
				bpF.button("Expand composite demo", "Shows an expand composite"),
				"grow, sg bg, wrap");
		expandCompositeButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrame frame = new DemoExpandComposite();
				frame.setSize(new Dimension(400, 600));
				frame.setVisible(true);
			}
		});

		final IButton textAreaDemoButton = parentContainer.add(
				bpF.button("Text area demo", "Shows a text area with line wrapping"),
				"grow, sg bg, wrap");
		textAreaDemoButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrame menuDemoFrame = new DemoTextAreaFrame();
				menuDemoFrame.setSize(new Dimension(300, 300));
				menuDemoFrame.setVisible(true);
			}
		});

		final IButton scrollCompositeDemoButton = parentContainer.add(
				bpF.button("Scroll composite demo", "Shows a simple scroll composite with growing content"),
				"grow, sg bg, wrap");
		scrollCompositeDemoButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrame menuDemoFrame = new DemoScrollCompositeFrame();
				menuDemoFrame.setSize(new Dimension(300, 300));
				menuDemoFrame.setVisible(true);
			}
		});

		final IButton menuDemoButton = parentContainer.add(
				bpF.button("Menu demo", "Shows an frame with menus and popup menus"),
				"grow, sg bg, wrap");
		menuDemoButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrame menuDemoFrame = new DemoMenuFrame();
				menuDemoFrame.setSize(new Dimension(800, 600));
				menuDemoFrame.setVisible(true);
			}
		});

		final IButton splitDemoButton = parentContainer.add(
				bpF.button("Split demo", "Shows an frame with split composites"),
				"grow, sg bg, wrap");
		splitDemoButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrame splitDemoFrame = getParentWindow().createChildWindow(bpF.frame("Split demo").autoPackOff());
				splitDemoFrame.setSize(new Dimension(800, 600));
				new DemoSplitComposite(splitDemoFrame);
				splitDemoFrame.setVisible(true);
			}
		});

		final IButton tabDemoButton = parentContainer.add(
				bpF.button("Tab folder demo", "Shows an frame with a tab folder"),
				"grow, sg bg, wrap");
		tabDemoButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrame tabDemoFrame = getParentWindow().createChildWindow(bpF.frame("Tab folder demo").autoPackOff());
				tabDemoFrame.setSize(new Dimension(1024, 768));
				new DemoTabFolderComposite(tabDemoFrame);
				tabDemoFrame.setVisible(true);
			}
		});

		final IButton treeDemoButton = parentContainer.add(
				bpF.button("Tree demo", "Shows an frame with a tree"),
				"grow, sg bg, wrap");
		treeDemoButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrame treeDemoFrame = getParentWindow().createChildWindow(bpF.frame("Tree demo").autoPackOff());
				treeDemoFrame.setSize(new Dimension(800, 600));
				new DemoTreeComposite(treeDemoFrame);
				treeDemoFrame.setVisible(true);
			}
		});

		final IButton tableDemoButton = parentContainer.add(
				bpF.button("Table demo", "Shows an frame with a table"),
				"grow, sg bg, wrap");
		tableDemoButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrame tableDemoFrame = getParentWindow().createChildWindow(bpF.frame("Table demo").autoPackOff());
				tableDemoFrame.setSize(new Dimension(800, 600));
				new DemoTableComposite(tableDemoFrame);
				tableDemoFrame.setVisible(true);
			}
		});

		final IButton progressBarDialogButton = parentContainer.add(
				bpF.button("Progress bar demo", "Opens the progress bar demo"),
				"grow, sg bg, wrap");
		progressBarDialogButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrame progressBarDialog = getParentWindow().createChildWindow(bpF.dialog("Progress bar demo"));
				new DemoProgressBarComposite(progressBarDialog, progressBarDialog);
				progressBarDialog.setVisible(true);
				progressBarDialog.dispose();
			}
		});

		final IButton sliderDialogButton = parentContainer.add(
				bpF.button("Slider demo", "Opens the slider demo"),
				"grow, sg bg, wrap");
		sliderDialogButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrame sliderDemoFrame = new DemoSliderFrame();
				sliderDemoFrame.setSize(new Dimension(300, 300));
				sliderDemoFrame.setVisible(true);
			}
		});

		final IButton chooserDemoButton = parentContainer.add(
				bpF.button("Chooser demo", "Demonstrates choosers"),
				"grow, sg bg, wrap");
		chooserDemoButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IFrame popupDialogDemoFrame = new DemoChooserFrame();
				popupDialogDemoFrame.setSize(new Dimension(400, 300));
				popupDialogDemoFrame.setVisible(true);
			}
		});

		final IFrame fileChooserDialog = getParentWindow().createChildWindow(bpF.dialog("File chooser demo"));
		new DemoFileChooserComposite(fileChooserDialog);
		final IButton fileChooserDialogButton = parentContainer.add(
				bpF.button("File / Directory chooser demo", "Opens the file / directory chooser demo"),
				"grow, sg bg, wrap");
		fileChooserDialogButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				fileChooserDialog.setVisible(true);
			}
		});

		final IButton downloadDialogButton = parentContainer.add(bpF.button("Download button demo"), "grow, sg bg, wrap");
		downloadDialogButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IDialogBluePrint downloadDemoDialogBp = bpF.dialog("Download button demo");
				downloadDemoDialogBp.setMinPackSize(new Dimension(800, 0));
				final IFrame dowloadDemoDiaolg = getParentWindow().createChildWindow(downloadDemoDialogBp);
				new DemoDownloadComposite(dowloadDemoDiaolg);
				dowloadDemoDiaolg.setVisible(true);
				dowloadDemoDiaolg.dispose();
			}
		});

		final IFrame messagesDemoDialog = getParentWindow().createChildWindow(bpF.dialog("Messages demo"));
		new DemoMessagesComposite(messagesDemoDialog);
		final IButton messagesDialogButton = parentContainer.add(
				bpF.button("Messages demo", "Opens the messages demo"),
				"grow, sg bg, wrap");
		messagesDialogButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				messagesDemoDialog.setVisible(true);
			}
		});

		final IFrame layoutDemoDialog = getParentWindow().createChildWindow(bpF.frame("Layout demo"));
		new DemoLayoutComposite(layoutDemoDialog);
		final IButton layoutTestButton = parentContainer.add(bpF.button("Layout demo"), "grow, sg bg, wrap");
		layoutTestButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				layoutDemoDialog.setVisible(true);
			}
		});

		final IButton maximizeButton = parentContainer.add(bpF.button("Toggle maximized"), "grow, sg bg, wrap");
		maximizeButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IWindow parentWindow = getParentWindow();
				if (parentWindow instanceof IFrame) {
					final IFrame frame = (IFrame) parentWindow;
					frame.setMaximized(!frame.isMaximized());
				}
			}
		});

		final IButton iconfyButton = parentContainer.add(bpF.button("Iconfy"), "grow, sg bg, wrap");
		iconfyButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IWindow parentWindow = getParentWindow();
				if (parentWindow instanceof IFrame) {
					final IFrame frame = (IFrame) parentWindow;
					frame.setIconfied(true);
				}
			}
		});

	}

	public void foo() {}

	private IWindow getParentWindow() {
		IWindow parentWindow = Toolkit.getWidgetUtils().getWindowAncestor(parentContainer);
		if (parentWindow == null) {
			parentWindow = Toolkit.getActiveWindow();
		}
		return parentWindow;
	}

}
