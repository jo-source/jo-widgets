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
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.IMenuBar;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.IToolBarButton;
import org.jowidgets.api.widgets.IToolBarContainerItem;
import org.jowidgets.api.widgets.IToolBarPopupButton;
import org.jowidgets.api.widgets.IToolBarToggleButton;
import org.jowidgets.api.widgets.blueprint.IToolBarToggleButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.IItemStateListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.command.EnabledChecker;
import org.jowidgets.tools.model.item.ActionItemModel;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.model.item.RadioItemModel;
import org.jowidgets.tools.powo.JoFrame;

public class DemoMenuFrame extends JoFrame {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();

	private ICommandAction action1;
	private ICommandAction action2;
	private final IMenuModel menuModel;

	public DemoMenuFrame() {
		super(bluePrint("Menu demo").autoPackOff());
		createActions();
		this.menuModel = createMenuModel();
		createMainMenus();

		setLayout(new MigLayoutDescriptor("0[grow]0", "0[][grow]0"));

		final IToolBar toolBar = add(BPF.toolBar(), "wrap");

		final IToolBarPopupButton toolBarPopupButton = toolBar.addItem(BPF.toolBarPopupButton());
		toolBarPopupButton.setAction(action1);
		toolBarPopupButton.setPopupMenu(getMenuModel());

		final IToolBarButton toolBarButton = toolBar.addItem(BPF.toolBarButton());
		toolBarButton.setAction(action2);

		toolBar.addSeparator();

		final IToolBarToggleButtonBluePrint toggleButtonBp = BPF.toolBarToggleButton();
		toggleButtonBp.setText("ToggleButton").setToolTipText("Tooltip");
		final IToolBarToggleButton toggleButton = toolBar.addItem(toggleButtonBp);
		toggleButton.addItemListener(new IItemStateListener() {

			@Override
			public void itemStateChanged() {
				// CHECKSTYLE:OFF
				System.out.println("ToggleButton selected: " + toggleButton.isSelected());
				// CHECKSTYLE:ON
			}
		});

		final IToolBarContainerItem toolBarContainer = toolBar.addItem(BPF.toolBarContainerItem());
		final IInputControl<String> textField = toolBarContainer.add(BPF.textField(), "w 200");
		textField.setValue("Test");

		toolBar.addItem(BPF.toolBarButton().setText("Button").setToolTipText("Tooltip"));

		toolBar.pack();

		final IComposite composite = add(BPF.composite().setBackgroundColor(Colors.WHITE), "growx, growy");
		composite.setPopupMenu(getMenuModel());

		menuModel.getChildren().get(5).setText("renamed in model");
		((IMenuModel) ((IMenuModel) (menuModel.getChildren().get(0))).getChildren().get(2)).removeItem(3);
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

	private void createMainMenus() {
		final IMenuBar menuBar = createMenuBar();
		final IMenu menu1 = menuBar.addMenu(Toolkit.getBluePrintFactory().mainMenu());
		menu1.setModel(getMenuModel());

		setPopupMenu(getMenuModel());
	}

	private IMenuModel getMenuModel() {
		return this.menuModel;
	}

	private IMenuModel createMenuModel() {

		final IMenuModel menu = new MenuModel(MenuModel.builder("Menu1").setMnemonic('n'));

		final IMenuModel subMenu = menu.addItem(MenuModel.builder("sub menu 1").setMnemonic('e'));
		subMenu.addActionItem("sub item1");
		subMenu.addActionItem("sub item2");

		final IMenuModel subMenu2 = subMenu.addItem(MenuModel.builder("sub menu 1").setMnemonic('n'));
		subMenu2.addActionItem("sub item1");
		subMenu2.addActionItem("sub item2");
		subMenu2.addActionItem("sub item3");
		subMenu2.addActionItem("sub item4");

		subMenu.addActionItem("sub item3");
		subMenu.addSeparator();
		subMenu.addActionItem("sub item4");
		subMenu.addActionItem("sub item5");

		menu.addAction(action1);
		menu.addAction(action2);

		final IActionItemModelBuilder item3Bd = ActionItemModel.builder();
		item3Bd.setText("The Third Item").setToolTipText("This is the third item");
		item3Bd.setIcon(IconsSmall.WARNING);
		item3Bd.setAccelerator('I', Modifier.SHIFT).setMnemonic('e');
		final IActionItemModel item3 = menu.addItem(1, item3Bd);
		addActionListener(item3);

		menu.addSeparator();

		final ICheckedItemModel item4 = menu.addCheckedItem("item4");

		menu.addSeparator();

		final IRadioItemModel item5 = menu.addRadioItem("item5");
		final IRadioItemModel item6 = menu.addItem(RadioItemModel.builder("item6").setSelected(true));
		final IRadioItemModel item7 = menu.addRadioItem("item7");

		addItemListener(item4);
		addItemListener(item5);
		addItemListener(item6);
		addItemListener(item7);

		return menu;
	}

	private void addActionListener(final IActionItemModel item) {
		item.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				// CHECKSTYLE:OFF
				System.out.println("Action performed" + item.getText());
				// CHECKSTYLE:ON
			}
		});
	}

	private void addItemListener(final ISelectableItemModel item) {
		item.addItemListener(new IItemStateListener() {
			@Override
			public void itemStateChanged() {
				// CHECKSTYLE:OFF
				System.out.println(item.getText() + ", selected=" + item.isSelected());
				// CHECKSTYLE:ON
			}
		});
	}
}
