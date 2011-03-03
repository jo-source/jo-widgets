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

import org.jowidgets.api.image.Icons;
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.types.QuestionResult;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.blueprint.IComboBoxBluePrint;
import org.jowidgets.api.widgets.blueprint.IComboBoxSelectionBluePrint;
import org.jowidgets.api.widgets.blueprint.IMessageDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.IQuestionDialogBluePrint;
import org.jowidgets.api.widgets.blueprint.ISplitCompositeBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.examples.common.icons.DemoIconsInitializer;
import org.jowidgets.tools.controler.WindowAdapter;
import org.jowidgets.tools.powo.IJoMenu;
import org.jowidgets.tools.powo.JoActionMenuItem;
import org.jowidgets.tools.powo.JoButton;
import org.jowidgets.tools.powo.JoCheckBox;
import org.jowidgets.tools.powo.JoCheckedMenuItem;
import org.jowidgets.tools.powo.JoComboBox;
import org.jowidgets.tools.powo.JoComboBoxSelection;
import org.jowidgets.tools.powo.JoComposite;
import org.jowidgets.tools.powo.JoDialog;
import org.jowidgets.tools.powo.JoFrame;
import org.jowidgets.tools.powo.JoIcon;
import org.jowidgets.tools.powo.JoInputField;
import org.jowidgets.tools.powo.JoMainMenu;
import org.jowidgets.tools.powo.JoMenuBar;
import org.jowidgets.tools.powo.JoMessageDialog;
import org.jowidgets.tools.powo.JoPopupMenu;
import org.jowidgets.tools.powo.JoProgressBar;
import org.jowidgets.tools.powo.JoQuestionDialog;
import org.jowidgets.tools.powo.JoRadioMenuItem;
import org.jowidgets.tools.powo.JoScrollComposite;
import org.jowidgets.tools.powo.JoSeparatorMenuItem;
import org.jowidgets.tools.powo.JoSplitComposite;
import org.jowidgets.tools.powo.JoSubMenu;
import org.jowidgets.tools.powo.JoTextLabel;
import org.jowidgets.tools.powo.JoToggleButton;

public class PowoDemoApplication implements IApplication {

	private final String frameTitle;
	private JoFrame frame;

	public PowoDemoApplication(final String frameTitle) {
		super();
		this.frameTitle = frameTitle;
	}

	public void start() {
		DemoIconsInitializer.initialize();
		Toolkit.getInstance().getApplicationRunner().run(this);
	}

	@Override
	public void start(final IApplicationLifecycle lifecycle) {
		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();

		frame = new JoFrame(frameTitle);
		final JoDialog dialog = createDialog(frame);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed() {
				lifecycle.finish();
			}
		});

		frame.setMenuBar(createMenuBar());

		final JoPopupMenu popupMenu = createPopUpMenu();
		frame.addPopupMenu(popupMenu);
		frame.addPopupDetectionListener(new IPopupDetectionListener() {
			@Override
			public void popupDetected(final Position position) {
				popupMenu.show(position);
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
		composite2.setLayout(new MigLayoutDescriptor("[][grow]", "[][][][][][]"));
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

		final JoButton button3 = new JoButton("ask question", "shows an question dialog");
		composite2.add(button3, "grow, span2, wrap");

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

		button3.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IQuestionDialogBluePrint bp = JoQuestionDialog.bluePrintYesNoCancel();
				bp.setTitle("Title for my message");
				bp.setText("This is my message!\nI hope you like it very much!");
				final QuestionResult result = new JoQuestionDialog(frame, bp).question();

				new JoMessageDialog(frame, Icons.INFO, "Answer was:\n" + result).showMessage();
			}
		});

		//here composite2 becomes initialized
		frame.add(composite2, "growx, growy");

		frame.setVisible(true);
	}

	private JoPopupMenu createPopUpMenu() {
		final JoPopupMenu result = new JoPopupMenu();
		fillMenu(result);
		return result;
	}

	private JoMenuBar createMenuBar() {
		final JoMenuBar menuBar = new JoMenuBar();
		menuBar.addMenu(createMainMenu1());
		menuBar.addMenu(createMainMenu2());
		return menuBar;
	}

	private JoMainMenu createMainMenu1() {
		final JoMainMenu result = new JoMainMenu("File", 'F');
		fillMenu(result);
		return result;
	}

	private JoMainMenu createMainMenu2() {
		final JoMainMenu result = new JoMainMenu("Edit", 'E');
		fillMenu(result);
		return result;
	}

	private void fillMenu(final IJoMenu menu) {
		final JoCheckedMenuItem checkedItem = new JoCheckedMenuItem("Checked1");
		checkedItem.setSelected(true);
		menu.addItem(checkedItem);

		menu.addItem(new JoSeparatorMenuItem());

		menu.addItem(new JoActionMenuItem("Item1", "Tooltip of item1"));
		menu.addItem(new JoActionMenuItem("Item2", "Tooltip of item2"));
		menu.addItem(new JoActionMenuItem("Item3", "Tooltip of item3"));

		menu.addItem(new JoSeparatorMenuItem());

		final JoSubMenu subMenu = new JoSubMenu("Submenu", "Tooltip of submenu");
		subMenu.addItem(new JoActionMenuItem("Sub item1", "Tooltip of sub item1"));
		subMenu.addItem(new JoActionMenuItem("Sub item2", "Tooltip of sub item2"));
		subMenu.addItem(new JoActionMenuItem("Sub item3", "Tooltip of sub item3"));
		menu.addItem(subMenu);

		menu.addItem(new JoSeparatorMenuItem());

		final JoRadioMenuItem radioItem = new JoRadioMenuItem("Radio1", "Tooltip of radio1");
		radioItem.setSelected(true);
		menu.addItem(radioItem);
		menu.addItem(new JoRadioMenuItem("Radio2", "Tooltip of radio2"));
		menu.addItem(new JoRadioMenuItem("Radio3", "Tooltip of radio3"));

	}

	private JoDialog createDialog(final IFrame parent) {

		final JoDialog result = new JoDialog(parent, JoDialog.bluePrint("test").autoPackOff());
		result.setLayout(new MigLayoutDescriptor("0[grow]0", "0[grow]0"));

		final ISplitCompositeBluePrint splitBp = JoSplitComposite.bluePrint().setWeight(0.3).resizeSecondPolicy();
		final JoSplitComposite split = new JoSplitComposite(splitBp);
		split.getFirst().add(createFirstSplit(), "growx, growy");
		split.getSecond().add(createSecondSplit(), "growx, growy");

		result.add(split, "growx, growy");
		result.setSize(new Dimension(800, 600));
		return result;
	}

	private JoScrollComposite createFirstSplit() {
		final JoScrollComposite result = new JoScrollComposite(new MigLayoutDescriptor("[][grow]", "[][][]"));
		result.addLabel("String input");
		result.add(JoInputField.inputFieldString(), "growx, wrap");

		result.addLabel("Long input");
		result.add(JoInputField.inputFieldLong(), "growx, wrap");

		result.addLabel("Integer input");
		result.add(JoInputField.inputFieldInteger(), "growx, wrap");

		return result;
	}

	@SuppressWarnings("deprecation")
	private JoScrollComposite createSecondSplit() {
		final IBluePrintFactory bpF = Toolkit.getBluePrintFactory();

		final JoScrollComposite result = new JoScrollComposite(new MigLayoutDescriptor("[grow]", "[][][][][][][]"));

		final JoComboBoxSelection<String> comboBox1 = new JoComboBoxSelection<String>(new String[] {"hallo", "test", "more"});
		comboBox1.setValue("test");
		result.add(comboBox1, "growx, wrap");

		final JoComboBoxSelection<Integer> comboBox2 = new JoComboBoxSelection<Integer>(new Integer[] {1, 2, 3, 4, 5});
		result.add(comboBox2, "growx, wrap");

		final JoCheckBox checkBox = new JoCheckBox("test");
		result.add(checkBox, "growx, wrap");

		final IComboBoxSelectionBluePrint<Date> cbBp = JoComboBoxSelection.bluePrint();
		cbBp.autoSelectionOn();
		cbBp.setElements(new Date[] {new Date(), new Date(110, 11, 9), new Date(110, 11, 11)});
		final JoComboBoxSelection<Date> comboBox3 = new JoComboBoxSelection<Date>(cbBp);
		result.add(comboBox3, "growx, wrap");

		result.add(bpF.comboBoxSelection(new String[] {"some", "more", "testing"}), "growx, wrap");

		final IComboBoxBluePrint<Long> comboBoxBp4 = JoComboBox.bluePrintLong().autoSelectionOn();
		comboBoxBp4.setElements(new Long[] {14324L, 4235345L, 324234L, 32423L, 32432432L, 2343234L});
		result.add(new JoComboBox<Long>(comboBoxBp4), "growx, wrap");

		result.add(JoComboBox.comboBoxString(new String[] {"put your own", "values", "in here"}), "growx, wrap");

		result.add(new JoToggleButton("Toggle me"), "growx, wrap");
		return result;
	}

	public JoFrame getRootFrame() {
		return frame;
	}

}
