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

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.command.EnabledState;
import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.IActionBuilderFactory;
import org.jowidgets.api.command.ICommandAction;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.IActionItemModelBuilder;
import org.jowidgets.api.model.item.ICheckedItemModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IRadioItemModel;
import org.jowidgets.api.model.item.ISelectableItemModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.controler.IItemStateListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.command.EnabledChecker;
import org.jowidgets.tools.model.item.ActionItemModel;
import org.jowidgets.tools.model.item.InputControlItemModel;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.model.item.RadioItemModel;
import org.jowidgets.tools.model.item.ToolBarModel;
import org.jowidgets.tools.powo.JoFrame;

public class DemoMenuFrame extends JoFrame {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

	private ICommandAction action1;
	private ICommandAction action2;
	private IMenuModel menuModel;
	private ICheckedItemModel checkedItem;
	private IActionItemModel actionItem;
	private IToolBarModel toolBarModel;

	public DemoMenuFrame() {
		super(bluePrint("Menu demo").autoPackOff());

		createActions();
		createMenuModel();
		createToolBarModel();

		createMainMenus();

		setLayout(new MigLayoutDescriptor("0[grow]0", "0[][]0[]0[grow]0"));

		add(BPF.toolBar(), "wrap").setModel(toolBarModel);
		add(BPF.toolBar(), "wrap").setModel(toolBarModel);

		add(BPF.separator(), "growx, wrap");

		add(BPF.composite().setBackgroundColor(Colors.WHITE), "growx, growy").setPopupMenu(getMenuModel());

		menuModel.getChildren().get(5).setText("renamed text in menu model");
	}

	private void createActions() {
		final IActionBuilderFactory actionBF = Toolkit.getActionBuilderFactory();

		final IActionBuilder action1Builder = actionBF.create();
		action1Builder.setText("Action1").setToolTipText("The tooltip of Action1").setIcon(IconsSmall.INFO);
		action1Builder.setMnemonic('c').setAccelerator('A', Modifier.CTRL);
		action1 = action1Builder.build();

		final EnabledChecker enabledChecker1 = new EnabledChecker();
		final EnabledChecker enabledChecker2 = new EnabledChecker();
		enabledChecker2.setEnabledState(EnabledState.disabled("Action 1 must be invoked first"));

		final ICommandExecutor command1 = new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext event) {
				final IAction action = event.getAction();
				Toolkit.getMessagePane().showInfo(action.getText(), action.getIcon(), "Hello action1");
				enabledChecker1.setEnabledState(EnabledState.disabled("Action 2 must be invoked first"));
				enabledChecker2.setEnabledState(EnabledState.ENABLED);
			}
		};

		action1.setCommand(command1, enabledChecker1);

		final IActionBuilder action2Builder = actionBF.create();
		action2Builder.setText("Action2").setToolTipText("The tooltip of Action2");
		action2Builder.setMnemonic('t').setAccelerator('A', Modifier.ALT);
		action2 = action2Builder.build();

		final ICommandExecutor command2 = new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext event) {
				final IAction action = event.getAction();
				Toolkit.getMessagePane().showInfo(action.getText(), action.getIcon(), "Hello action2");
				enabledChecker1.setEnabledState(EnabledState.ENABLED);
				enabledChecker2.setEnabledState(EnabledState.disabled("Action 1 must be invoked first"));
			}
		};

		action2.setCommand(command2, enabledChecker2);
	}

	private IMenuModel createMenuModel() {

		//first create the menu
		menuModel = new MenuModel(MenuModel.builder("Menu1").setMnemonic('n'));

		final IMenuModel subMenu = menuModel.addItem(MenuModel.builder("sub menu 1").setMnemonic('e'));
		actionItem = subMenu.addActionItem("sub item1");
		final IActionItemModel subItem2 = subMenu.addActionItem("sub item2");

		final IMenuModel subMenu2 = subMenu.addItem(MenuModel.builder("sub menu 2").setMnemonic('n'));
		final IActionItemModel sub2Item1 = subMenu2.addActionItem("sub2 item1");
		final IActionItemModel sub2Item2 = subMenu2.addActionItem("sub2 item2");
		final IActionItemModel sub2Item3 = subMenu2.addActionItem("sub2 item3");
		final IActionItemModel sub2Item4 = subMenu2.addActionItem("sub2 item4");

		final IActionItemModel subItem3 = subMenu.addActionItem("sub item3");
		subMenu.addSeparator();
		final IActionItemModel subItem4 = subMenu.addActionItem("sub item4");
		final IActionItemModel subItem5 = subMenu.addActionItem("sub item5");

		menuModel.addAction(action1);
		menuModel.addAction(action2);

		final IActionItemModelBuilder item3Builder = ActionItemModel.builder();
		item3Builder.setText("The Third Item").setToolTipText("This is the third item");
		item3Builder.setIcon(IconsSmall.WARNING);
		item3Builder.setAccelerator('I', Modifier.SHIFT).setMnemonic('i');
		final IActionItemModel item3 = menuModel.addItem(1, item3Builder);

		menuModel.addSeparator();

		checkedItem = menuModel.addCheckedItem("item4");

		menuModel.addSeparator();

		final IRadioItemModel item5 = menuModel.addRadioItem("item5");
		final IRadioItemModel item6 = menuModel.addItem(RadioItemModel.builder("item6").setSelected(true));
		final IRadioItemModel item7 = menuModel.addRadioItem("item7");

		//then add listeners to the created items
		addListener(actionItem);
		addListener(subItem2);
		addListener(subItem3);
		addListener(subItem4);
		addListener(subItem5);
		addListener(sub2Item1);
		addListener(sub2Item2);
		addListener(sub2Item3);
		addListener(sub2Item4);
		addListener(item3);
		addListener(checkedItem);
		addListener(item5);
		addListener(item6);
		addListener(item7);

		return menuModel;
	}

	private void createToolBarModel() {
		toolBarModel = new ToolBarModel();

		toolBarModel.addPopupAction(action1, getMenuModel());
		toolBarModel.addAction(action2);
		toolBarModel.addSeparator();
		toolBarModel.addItem(checkedItem);

		final InputControlItemModel<String> textField = new InputControlItemModel<String>(BPF.textField().setValue("Test"));
		toolBarModel.addItem(textField);
		addInputListener(textField);

		toolBarModel.addItem(actionItem);
	}

	private void createMainMenus() {
		getMenuBarModel().addMenu(getMenuModel());
		setPopupMenu(getMenuModel());
	}

	private IMenuModel getMenuModel() {
		return this.menuModel;
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
