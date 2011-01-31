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

package org.jowidgets.examples.common.helloworld;

import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.controler.impl.WindowAdapter;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.powo.JoButton;
import org.jowidgets.tools.powo.JoContainer;
import org.jowidgets.tools.powo.JoDialog;
import org.jowidgets.tools.powo.JoFrame;
import org.jowidgets.tools.powo.JoLabel;

public class HelloWorld2 implements IApplication {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

	@Override
	public void start(final IApplicationLifecycle lifecycle) {
		final IFrameBluePrint frameBp = JoFrame.bluePrint();
		frameBp.autoCenterOnce().setTitle("HelloWorld2").setIcon(IconsSmall.QUESTION);

		final JoFrame frame = new JoFrame(frameBp);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed() {
				lifecycle.finish();
			}
		});

		addContent(frame);

		final JoDialog dialog = new JoDialog(frame, "dialog");
		addContent(dialog);

		final JoButton button = new JoButton("click me");
		button.addActionListener(new IActionListener() {

			@Override
			public void actionPerformed() {
				dialog.setVisible(true);
			}
		});

		frame.add(button, "grow, span 2");

		frame.setSize(new Dimension(800, 600));
		frame.setVisible(true);
	}

	private void addContent(final IContainer icontainer) {

		final JoContainer container = JoContainer.toJoContainer(icontainer);
		container.setLayout(new MigLayoutDescriptor("[][grow, 200::]", "[][][][][]"));

		container.add(new JoLabel("String input"), "alignx right");
		final IInputControl<String> textField1 = container.add(BPF.inputFieldString().setMaxLength(10), "grow, wrap");
		addInputControlListener(textField1);

		container.add(new JoLabel("long"), "alignx right");
		final IInputControl<Long> textField2 = container.add(BPF.inputFieldLongNumber().setMaxLength(10), "grow, wrap");
		addInputControlListener(textField2);

		container.add(new JoLabel("combo"), "alignx right");
		final IInputControl<String> textField3 = container.add(
				BPF.comboBoxSelection("eins", "zwei", "drei").autoSelectionOn(),
				"grow, wrap");
		addInputControlListener(textField3);

		container.add(new JoLabel("check"), "alignx right");
		final IInputControl<Boolean> textField4 = container.add(BPF.checkBox(), "grow, wrap");
		addInputControlListener(textField4);
	}

	private void addInputControlListener(final IInputControl<?> inputControl) {
		inputControl.addInputListener(new IInputListener() {

			@Override
			public void inputChanged(final Object source) {
				System.out.println(inputControl.getValue());

			}
		});
	}

	public void start() {
		Toolkit.getApplicationRunner().run(this);
	}

}
