/*
 * Copyright (c) 2013, grossmann
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
package org.jowidgets.examples.common.snipped;

import org.jowidgets.api.command.Action;
import org.jowidgets.api.command.EnabledState;
import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IEnabledChecker;
import org.jowidgets.api.command.IEnabledState;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.layout.BorderLayout;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.MessagePane;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IInputComponent;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.command.AbstractEnabledChecker;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class CommandActionSnipped implements IApplication {

	@Override
	public void start(final IApplicationLifecycle lifecycle) {

		//create a root frame
		final IFrameBluePrint frameBp = BPF.frame();
		frameBp.setSize(new Dimension(400, 300)).setTitle("Command actions");
		final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);

		//Create the menu bar
		final IMenuBarModel menuBar = frame.getMenuBarModel();

		//Use a border layout
		frame.setLayout(BorderLayout.builder().gap(0).build());

		//add a toolbar to the top
		final IToolBarModel toolBar = frame.add(BPF.toolBar(), BorderLayout.TOP).getModel();

		//add a composite to the center
		final IComposite composite = frame.add(BPF.composite().setBorder(), BorderLayout.CENTER);
		composite.setLayout(new MigLayoutDescriptor("[grow][]", "[]"));

		//add a input field and save button to the composite
		final IInputField<String> inputField = composite.add(BPF.inputFieldString(), "growx");
		final IButton saveButton = composite.add(BPF.button());

		//create save action 
		final IAction saveAction = createSaveAction(inputField);

		//create a menu and add save action
		final MenuModel menu = new MenuModel("File");
		menu.addAction(saveAction);

		//add the menu to the menu bar
		menuBar.addMenu(menu);

		//add the action to the toolbar
		toolBar.addAction(saveAction);

		//bind the action to the save button
		saveButton.setAction(saveAction);

		//set the root frame visible
		frame.setVisible(true);
	}

	private static IAction createSaveAction(final IInputComponent<String> inputComponent) {
		final IActionBuilder builder = Action.builder();
		builder.setText("Save");
		builder.setToolTipText("Saves the text");
		builder.setAccelerator(VirtualKey.S, Modifier.CTRL);
		builder.setIcon(IconsSmall.DISK);

		//save command implements ICommandExecutor and IEnabledChecker,
		//so set them both
		final SaveCommand saveCommand = new SaveCommand(inputComponent);
		builder.setCommand(saveCommand, saveCommand);

		return builder.build();
	}

	private static final class SaveCommand extends AbstractEnabledChecker implements ICommandExecutor, IEnabledChecker {

		private final IInputComponent<?> inputComponent;

		private SaveCommand(final IInputComponent<?> inputComponent) {
			this.inputComponent = inputComponent;

			inputComponent.addInputListener(new IInputListener() {
				@Override
				public void inputChanged() {
					fireEnabledStateChanged();
				}
			});
		}

		@Override
		public void execute(final IExecutionContext executionContext) throws Exception {
			inputComponent.resetModificationState();
			fireEnabledStateChanged();
			final String message = "'" + inputComponent.getValue() + "' saved!";
			MessagePane.showInfo(executionContext, message);
		}

		@Override
		public IEnabledState getEnabledState() {
			if (!inputComponent.hasModifications()) {
				return EnabledState.disabled("No changes to save");
			}
			else {
				return EnabledState.ENABLED;
			}
		}

	}

}
