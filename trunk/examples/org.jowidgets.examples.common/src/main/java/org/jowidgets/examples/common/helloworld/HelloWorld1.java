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

import org.jowidgets.api.command.EnabledState;
import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.IActionBuilderFactory;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IEnabledState;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.IMainMenu;
import org.jowidgets.api.widgets.IMenuBar;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.blueprint.ITextLabelBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.command.EnabledChecker;

public class HelloWorld1 implements IApplication {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();
	private static final IActionBuilderFactory ABF = Toolkit.getActionBuilderFactory();

	private boolean action1Enabled = true;
	private IAction action1;
	private IAction action2;

	@Override
	public void start(final IApplicationLifecycle lifecycle) {
		final IFrameBluePrint frameBp = BPF.frame();
		frameBp.autoCenterOnce().setTitle("HelloWorld").setIcon(IconsSmall.QUESTION);

		final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);

		final IMenuBar menuBar = frame.createMenuBar();
		final IMainMenu menu = menuBar.addMenu(BPF.mainMenu("File").setMnemonic('F'));

		initActions();

		menu.addAction(action1);
		menu.addAction(action2);

		frame.setLayout(new MigLayoutDescriptor("[grow]", "[][grow]"));
		final IToolBar toolBar = frame.add(BPF.toolBar(), "wrap");
		toolBar.addAction(action1);
		toolBar.addAction(action2);

		//addContent(frame);

		final IFrame dialog = frame.createChildWindow(BPF.dialog("dialog"));
		addContent(dialog);

		//		final IButton button = frame.add(BPF.button("click me"), "grow, span 2");
		//		button.addActionListener(new IActionListener() {
		//
		//			@Override
		//			public void actionPerformed() {
		//				dialog.setVisible(true);
		//			}
		//		});

		frame.setSize(new Dimension(800, 600));
		frame.setVisible(true);
	}

	private void initActions() {

		final EnabledChecker enabledChecker1 = new EnabledChecker() {
			@Override
			public IEnabledState getEnabledState() {
				if (!action1Enabled) {
					return EnabledState.disabled("action2 must be invoked first");
				}
				return EnabledState.ENABLED;
			}
		};

		final EnabledChecker enabledChecker2 = new EnabledChecker() {
			@Override
			public IEnabledState getEnabledState() {
				if (action1Enabled) {
					return EnabledState.disabled("action1 must be invoked first");
				}
				return EnabledState.ENABLED;
			}
		};

		final ICommandExecutor executor1 = new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				Toolkit.getMessagePane().showInfo("Action1");
				action1Enabled = !action1Enabled;
				enabledChecker1.fireEnabledStateChanged();
				enabledChecker2.fireEnabledStateChanged();

			}
		};

		final ICommandExecutor executor2 = new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				Toolkit.getMessagePane().showInfo("Action2");
				action1Enabled = !action1Enabled;
				enabledChecker1.fireEnabledStateChanged();
				enabledChecker2.fireEnabledStateChanged();
			}
		};

		final IActionBuilder actionBuilder = ABF.create();
		actionBuilder.setText("action1").setToolTipText("tooltip of action1").setAccelerator(new Accelerator('A', Modifier.ALT));
		actionBuilder.setIcon(IconsSmall.INFO);
		actionBuilder.setCommand(executor1, enabledChecker1);
		action1 = actionBuilder.build();

		actionBuilder.setText("action2").setToolTipText("tooltip of action2").setAccelerator(new Accelerator('A', Modifier.SHIFT));
		actionBuilder.setCommand(executor2, enabledChecker2);
		actionBuilder.setIcon(IconsSmall.QUESTION);
		action2 = actionBuilder.build();

	}

	private void addContent(final IContainer container) {
		container.setLayout(new MigLayoutDescriptor("[][grow, 200::]", "[][][][][]"));

		final ITextLabelBluePrint labelBp = BPF.textLabel().alignRight();

		container.add(labelBp.setText("String input"), "alignx right");
		final IInputControl<String> textField1 = container.add(BPF.inputFieldString().setMaxLength(10), "grow, wrap");
		addInputControlListener(textField1);

		container.add(labelBp.setText("long"), "alignx right");
		final IInputControl<Long> textField2 = container.add(BPF.inputFieldLongNumber().setMaxLength(10), "grow, wrap");
		addInputControlListener(textField2);

		container.add(labelBp.setText("combo"), "alignx right");
		final IInputControl<String> textField3 = container.add(
				BPF.comboBoxSelection("eins", "zwei", "drei").autoSelectionOn(),
				"grow, wrap");
		addInputControlListener(textField3);

		container.add(labelBp.setText("check"), "alignx right");
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
