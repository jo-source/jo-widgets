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
package org.jowidgets.examples.common.tutorial;

import org.jowidgets.api.command.CommandAction;
import org.jowidgets.api.command.EnabledState;
import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IEnabledChecker;
import org.jowidgets.api.command.IEnabledState;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.ICheckedItemModel;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IRadioItemModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.blueprint.IButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.controller.IItemStateListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.util.event.ChangeObservable;

public final class SaveActionExample implements IApplication {

	@Override
	public void start(final IApplicationLifecycle lifecycle) {

		final IFrameBluePrint frameBp = BPF.frame();
		frameBp.setSize(new Dimension(400, 300)).setTitle("Hello World");

		final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);

		frame.setLayout(new MigLayoutDescriptor("[grow]", "[][]"));

		final IToolBar toolBar = frame.add(BPF.toolBar(), "growx, w 0::, wrap");

		final IButtonBluePrint buttonBp = BPF.button().setText("Hello World");

		final IButton button = frame.add(buttonBp);

		button.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				//CHECKSTYLE:OFF
				System.out.println("Hello World");
				//CHECKSTYLE:ON
			}
		});

		//****************************************************************
		//MENU MODEL EXAMPLE
		//****************************************************************
		final IMenuModel mainMenu = new MenuModel("Main menu");

		final ICheckedItemModel checkedItem = mainMenu.addCheckedItem("CheckedItem");
		checkedItem.setSelected(true);
		checkedItem.addItemListener(new IItemStateListener() {
			@Override
			public void itemStateChanged() {
				//CHECKSTYLE:OFF
				System.out.println(checkedItem.isSelected());
				//CHECKSTYLE:ON
			}
		});

		final IAction saveAction = createSaveAction(checkedItem);
		final IActionItemModel actionItem = mainMenu.addAction(saveAction);
		actionItem.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				//CHECKSTYLE:OFF
				System.out.println("Action Performed");
				//CHECKSTYLE:ON
			}
		});

		mainMenu.addSeparator();

		final IMenuModel subMenu = mainMenu.addMenu("SubMenu");
		final IRadioItemModel radio1 = subMenu.addRadioItem("Radio1");
		subMenu.addRadioItem("Radio2").setSelected(true);
		subMenu.addRadioItem("Radio3");

		radio1.addItemListener(new IItemStateListener() {
			@Override
			public void itemStateChanged() {
				//CHECKSTYLE:OFF
				System.out.println(radio1.isSelected());
				//CHECKSTYLE:ON
			}
		});

		final IMenuBarModel menuBar = frame.getMenuBarModel();
		menuBar.addMenu(mainMenu);

		frame.setPopupMenu(mainMenu);

		final IToolBarModel toolBarModel = toolBar.getModel();
		toolBarModel.addAction(saveAction);
		toolBarModel.addItem(checkedItem);

		//set the root frame visible
		frame.setVisible(true);
	}

	private IAction createSaveAction(final ICheckedItemModel checkedItem) {
		final IActionBuilder builder = CommandAction.builder();
		builder.setText("Save");
		builder.setIcon(IconsSmall.DISK);
		builder.setCommand(new SaveExecutor(), new SaveEnabledChecker(checkedItem));
		return builder.build();
	}

	private final class SaveExecutor implements ICommandExecutor {
		@Override
		public void execute(final IExecutionContext executionContext) throws Exception {
			Toolkit.getMessagePane().showInfo(executionContext, "Data saved!");
		}
	}

	private final class SaveEnabledChecker extends ChangeObservable implements IEnabledChecker {

		private final ICheckedItemModel checkedItem;

		public SaveEnabledChecker(final ICheckedItemModel checkedItem) {
			this.checkedItem = checkedItem;
			checkedItem.addItemListener(new IItemStateListener() {
				@Override
				public void itemStateChanged() {
					fireChangedEvent();
				}
			});
		}

		@Override
		public IEnabledState getEnabledState() {
			if (checkedItem.isSelected()) {
				return EnabledState.ENABLED;
			}
			else {
				return EnabledState.disabled("Checked item must be checked");
			}
		}

	}
}
