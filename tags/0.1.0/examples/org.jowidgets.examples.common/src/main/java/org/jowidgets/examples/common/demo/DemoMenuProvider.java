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

import org.jowidgets.api.command.EnabledState;
import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.IActionBuilderFactory;
import org.jowidgets.api.command.ICommandAction;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.ICheckedItemModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IRadioItemModel;
import org.jowidgets.api.model.item.ISelectableItemModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.blueprint.IComboBoxSelectionBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.controler.IItemStateListener;
import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.tools.command.EnabledChecker;
import org.jowidgets.tools.model.item.InputControlItemModel;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.model.item.RadioItemModel;
import org.jowidgets.tools.model.item.ToolBarModel;

public class DemoMenuProvider {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

	private ICommandAction action1;
	private ICommandAction action2;
	private ICommandAction refreshAction;
	private ICommandAction imageEditAction;
	private IMenuModel menuModel;
	private ICheckedItemModel editItem;
	private IToolBarModel toolBarModel;

	public DemoMenuProvider(final boolean lessToolBarItems) {
		createActions();
		createMenuModel();
		createToolBarModel(lessToolBarItems);
	}

	private void createActions() {
		final IActionBuilderFactory actionBF = Toolkit.getActionBuilderFactory();

		final IActionBuilder action1Builder = actionBF.create();
		action1Builder.setText("Step First").setToolTipText("Step to the first").setIcon(SilkIcons.RESULTSET_FIRST);
		action1Builder.setMnemonic('f').setAccelerator('F', Modifier.CTRL);
		action1 = action1Builder.build();

		final EnabledChecker enabledChecker1 = new EnabledChecker();
		final EnabledChecker enabledChecker2 = new EnabledChecker();
		enabledChecker2.setEnabledState(EnabledState.disabled("Step First must be invoked first"));

		final ICommandExecutor command1 = new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext event) {
				final IAction action = event.getAction();
				Toolkit.getMessagePane().showMessage(action.getText(), action.getIcon(), "Step First", SilkIcons.RESULTSET_FIRST);
				enabledChecker1.setEnabledState(EnabledState.disabled("Step Last Action must be invoked first"));
				enabledChecker2.setEnabledState(EnabledState.ENABLED);
			}
		};

		action1.setCommand(command1, enabledChecker1);

		final IActionBuilder action2Builder = actionBF.create();
		action2Builder.setText("Step Last").setToolTipText("Step to the last").setIcon(SilkIcons.RESULTSET_LAST);
		action2Builder.setMnemonic('l').setAccelerator('L', Modifier.CTRL);
		action2 = action2Builder.build();

		final ICommandExecutor command2 = new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext event) {
				final IAction action = event.getAction();
				Toolkit.getMessagePane().showMessage(action.getText(), action.getIcon(), "Step Last", SilkIcons.RESULTSET_LAST);
				enabledChecker1.setEnabledState(EnabledState.ENABLED);
				enabledChecker2.setEnabledState(EnabledState.disabled("Step First Action must be invoked first"));
			}
		};

		action2.setCommand(command2, enabledChecker2);

		final IActionBuilder refreshActionBuilder = actionBF.create();
		refreshActionBuilder.setText("Refresh");
		refreshActionBuilder.setIcon(SilkIcons.ARROW_REFRESH_SMALL);
		refreshActionBuilder.setCommand(new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				// CHECKSTYLE:OFF
				System.out.println("Refreshed :-)");
				// CHECKSTYLE:ON
			}
		});
		refreshAction = refreshActionBuilder.build();

		final IActionBuilder imageEditActionBuilder = actionBF.create();
		imageEditActionBuilder.setText("Edit Image...");
		imageEditActionBuilder.setIcon(SilkIcons.IMAGE_EDIT);
		imageEditActionBuilder.setAccelerator('I', Modifier.SHIFT).setMnemonic('i');
		imageEditActionBuilder.setCommand(new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				final IAction action = executionContext.getAction();
				Toolkit.getMessagePane().showMessage(action.getText(), action.getIcon(), "Edit the image", action.getIcon());
			}
		});
		imageEditAction = imageEditActionBuilder.build();

	}

	private IMenuModel createMenuModel() {

		//first create the menu
		menuModel = new MenuModel(MenuModel.builder("Menu1").setMnemonic('n'));

		menuModel.addAction(refreshAction);
		menuModel.addAction(imageEditAction);
		menuModel.addAction(action1);
		menuModel.addAction(action2);

		menuModel.addSeparator();
		final IMenuModel subMenu = menuModel.addItem(MenuModel.builder("Sub menu 1").setMnemonic('e'));
		final IActionItemModel subItem1 = subMenu.addActionItem("Sub item1");
		final IActionItemModel subItem2 = subMenu.addActionItem("Sub item2");

		final IMenuModel subMenu2 = subMenu.addItem(MenuModel.builder("Sub menu 2").setMnemonic('n'));
		final IActionItemModel sub2Item1 = subMenu2.addActionItem("Sub2 item1");
		final IActionItemModel sub2Item2 = subMenu2.addActionItem("Sub2 item2");
		final IActionItemModel sub2Item3 = subMenu2.addActionItem("Sub2 item3");
		final IActionItemModel sub2Item4 = subMenu2.addActionItem("Sub2 item4");

		subMenu.addSeparator();

		final IActionItemModel subItem3 = subMenu.addActionItem("Sub item3");

		subMenu.addSeparator();

		subMenu.addRadioItem("Group 1: 1.");
		subMenu.addRadioItem("Group 1: 2.");

		final IActionItemModel subItem4 = subMenu.addActionItem("Sub item4");
		final IActionItemModel subItem5 = subMenu.addActionItem("Sub item5");

		subMenu.addSeparator();

		subMenu.addRadioItem("Group 2: 1.");
		subMenu.addRadioItem("Group 2: 2.");
		subMenu.addRadioItem("Group 2: 3.");

		editItem = menuModel.addCheckedItem("Readonly");
		editItem.setIcon(SilkIcons.EYE);

		subMenu.addSeparator();

		final IRadioItemModel item5 = menuModel.addRadioItem("Low");
		final IRadioItemModel item6 = menuModel.addItem(RadioItemModel.builder("Med").setSelected(true));
		final IRadioItemModel item7 = menuModel.addRadioItem("Fast");

		//then add listeners to the created items
		addListener(subItem1);
		addListener(subItem2);
		addListener(subItem3);
		addListener(subItem4);
		addListener(subItem5);
		addListener(sub2Item1);
		addListener(sub2Item2);
		addListener(sub2Item3);
		addListener(sub2Item4);
		addListener(editItem);
		addListener(item5);
		addListener(item6);
		addListener(item7);

		return menuModel;
	}

	private void createToolBarModel(final boolean lessToolBarItems) {
		toolBarModel = new ToolBarModel();

		final IMenuModel radioSubMenu = new MenuModel();
		radioSubMenu.addRadioItem("All").setSelected(true);
		radioSubMenu.addRadioItem("Current");

		toolBarModel.addPopupAction(refreshAction, radioSubMenu);
		toolBarModel.addPopupAction(imageEditAction, radioSubMenu);
		toolBarModel.addAction(action1);
		toolBarModel.addAction(action2);
		toolBarModel.addItem(editItem);

		if (!lessToolBarItems) {
			toolBarModel.addSeparator();
			final IComboBoxSelectionBluePrint<String> comboBoxBp = BPF.comboBoxSelection(" ", "Spain", "Italy", "France");
			final InputControlItemModel<String> comboBox = new InputControlItemModel<String>(comboBoxBp, 80);
			toolBarModel.addItem(comboBox);

			final InputControlItemModel<String> textField = new InputControlItemModel<String>(BPF.textField(), 150);
			toolBarModel.addItem(textField);

			toolBarModel.addItem(getMenuModel());

			addInputListener(textField);
			addInputListener(comboBox);
		}

	}

	public IMenuModel getMenuModel() {
		return this.menuModel;
	}

	public IToolBarModel getToolBarModel() {
		return this.toolBarModel;
	}

	private void addListener(final IActionItemModel item) {
		item.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				// CHECKSTYLE:OFF
				System.out.println("Action performed: " + item.getText());
				// CHECKSTYLE:ON
			}
		});
	}

	private void addListener(final ISelectableItemModel item) {
		item.addItemListener(new IItemStateListener() {
			@Override
			public void itemStateChanged() {
				// CHECKSTYLE:OFF
				System.out.println(item.getText() + ", selected=" + item.isSelected());
				// CHECKSTYLE:ON
			}
		});
	}

	private void addInputListener(final InputControlItemModel<?> inputComponent) {
		inputComponent.addInputListener(new IInputListener() {
			@Override
			public void inputChanged(final Object source) {
				// CHECKSTYLE:OFF
				System.out.println("Input changed: " + inputComponent.getValue());
				// CHECKSTYLE:ON
			}
		});
	}
}
