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

package org.jowidgets.examples.common.powo;

import java.util.Date;

import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrameWidget;
import org.jowidgets.api.widgets.blueprint.IComboBoxBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxSelectionBluePrint;
import org.jowidgets.api.widgets.blueprint.IMessageDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.ISplitCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.impl.WindowAdapter;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.powo.JoButton;
import org.jowidgets.tools.powo.JoCheckBox;
import org.jowidgets.tools.powo.JoComboBox;
import org.jowidgets.tools.powo.JoComboBoxSelection;
import org.jowidgets.tools.powo.JoComposite;
import org.jowidgets.tools.powo.JoDialog;
import org.jowidgets.tools.powo.JoFrame;
import org.jowidgets.tools.powo.JoIcon;
import org.jowidgets.tools.powo.JoMessageDialog;
import org.jowidgets.tools.powo.JoProgressBar;
import org.jowidgets.tools.powo.JoScrollComposite;
import org.jowidgets.tools.powo.JoSplitComposite;
import org.jowidgets.tools.powo.JoTextLabel;
import org.jowidgets.tools.powo.JoToggleButton;

public class PowoDemoApplication implements IApplication {

	private final String frameTitle;

	public PowoDemoApplication(final String frameTitle) {
		super();
		this.frameTitle = frameTitle;
	}

	public void start() {
		Toolkit.getInstance().getApplicationRunner().run(this);
	}

	@Override
	public void start(final IApplicationLifecycle lifecycle) {
		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();

		final JoFrame frame = new JoFrame(frameTitle);
		final JoDialog dialog = createDialog(frame);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed() {
				lifecycle.finish();
			}
		});

		frame.setLayout(new MigLayoutDescriptor("[grow]", "[][][][][][grow]"));
		frame.add(new JoTextLabel("Test1"), "wrap");
		frame.add(new JoTextLabel("Test2"), "wrap");
		frame.add(new JoTextLabel("Test3"), "wrap");
		frame.add(new JoTextLabel("Test4"), "wrap");
		frame.add(bpF.separator(), "grow, wrap");

		//headless creation of composite2
		final JoComposite composite2 = new JoComposite(bpF.composite("Test"));
		composite2.setLayout(new MigLayoutDescriptor("[][grow]", "[][][][][]"));
		composite2.add(new JoIcon(IconsSmall.INFO), "");
		composite2.add(JoTextLabel.bluePrint("Test6"), "wrap");
		composite2.add(new JoIcon(IconsSmall.INFO), "");
		composite2.add(bpF.textLabel("Test8"), "wrap");

		composite2.add(new JoIcon(IconsSmall.ERROR), "");
		final JoProgressBar progressBar = new JoProgressBar(100);
		composite2.add(progressBar, "growx, wrap");
		progressBar.setProgress(35);

		final JoButton button1 = new JoButton("open dialog");
		composite2.add(button1, "grow, span2, wrap");

		final JoButton button2 = new JoButton("show message", "shows an message dialog");
		composite2.add(button2, "grow, span2, wrap");

		//here composite2 becomes initialized
		frame.add(composite2, "growx, growy");

		frame.setVisible(true);

		button1.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				dialog.setVisible(true);
			}
		});

		button2.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IMessageDialogBluePrint bp = JoMessageDialog.bluePrintInfo();
				bp.setTitle("Title for my message");
				bp.setText("This is my message!\nI hope you like it very much!");
				new JoMessageDialog(frame, bp).showMessage();
			}
		});
	}

	@SuppressWarnings("deprecation")
	private JoDialog createDialog(final IFrameWidget parent) {

		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();

		final JoDialog result = new JoDialog(parent, JoDialog.bluePrint("test").autoPackOff());
		result.setLayout(new MigLayoutDescriptor("0[grow]0", "0[grow]0"));

		final ISplitCompositeBluePrint splitBp = JoSplitComposite.bluePrint().setWeight(0.2).resizeSecondPolicy();
		final JoSplitComposite split = new JoSplitComposite(splitBp);

		final JoComposite first = split.getFirst();
		first.setLayout(new MigLayoutDescriptor("[grow]", "[]"));
		first.add(new JoTextLabel("Content1"));

		final JoComposite second = split.getSecond();
		final JoScrollComposite secondS = new JoScrollComposite(new MigLayoutDescriptor("[grow]", "[][][][][][][]"));

		final JoComboBoxSelection<String> comboBox1 = new JoComboBoxSelection<String>(new String[] {"hallo", "test", "more"});
		comboBox1.setValue("test");
		secondS.add(comboBox1, "growx, wrap");

		final JoComboBoxSelection<Integer> comboBox2 = new JoComboBoxSelection<Integer>(new Integer[] {1, 2, 3, 4, 5});
		secondS.add(comboBox2, "growx, wrap");

		final JoCheckBox checkBox = new JoCheckBox("test");
		secondS.add(checkBox, "growx, wrap");

		final IComboBoxSelectionBluePrint<Date> cbBp = JoComboBoxSelection.bluePrint();
		cbBp.autoSelectionOn();
		cbBp.setElements(new Date[] {new Date(), new Date(110, 11, 9), new Date(110, 11, 11)});
		final JoComboBoxSelection<Date> comboBox3 = new JoComboBoxSelection<Date>(cbBp);
		secondS.add(comboBox3, "growx, wrap");

		secondS.add(bpF.comboBoxSelection(new String[] {"some", "more", "testing"}), "growx, wrap");

		final IComboBoxBluePrint<Long> comboBoxBp4 = JoComboBox.bluePrintLong().autoSelectionOn();
		comboBoxBp4.setElements(new Long[] {14324L, 4235345L, 324234L, 32423L, 32432432L, 2343234L});
		secondS.add(new JoComboBox<Long>(comboBoxBp4), "growx, wrap");

		secondS.add(JoComboBox.comboBoxString(new String[] {"put your own", "values", "in here"}), "growx, wrap");

		secondS.add(new JoToggleButton("Toggle me"), "growx, wrap");

		second.add(secondS, "growx, growy");
		result.add(split, "growx, growy");

		result.setSize(new Dimension(800, 600));
		return result;
	}
}
