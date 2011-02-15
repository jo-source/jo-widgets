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
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.IMenuBar;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ISubMenu;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.IToolBarButton;
import org.jowidgets.api.widgets.IToolBarContainerItem;
import org.jowidgets.api.widgets.IToolBarPopupButton;
import org.jowidgets.api.widgets.IToolBarToggleButton;
import org.jowidgets.api.widgets.blueprint.IToolBarToggleButtonBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.IItemStateListener;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.command.EnabledChecker;
import org.jowidgets.tools.model.item.ActionItemModel;
import org.jowidgets.tools.model.item.CheckedItemModel;
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
		final IPopupMenu toolBarPopupMenu = toolBar.createPopupMenu();
		toolBarPopupMenu.setModel(menuModel);
		toolBarPopupButton.addPopupDetectionListener(new IPopupDetectionListener() {
			@Override
			public void popupDetected(final Position position) {
				toolBarPopupMenu.show(position);
			}
		});

		final IToolBarButton toolBarButton = toolBar.addItem(BPF.toolBarButton());
		toolBarButton.setAction(action2);

		toolBar.addSeparator();

		final IToolBarToggleButtonBluePrint toggleButtonBp = BPF.toolBarToggleButton().setText("ToggleButton");
		toggleButtonBp.setToolTipText("Tooltip");
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
		final IPopupMenu popupMenu = composite.createPopupMenu();
		popupMenu.setModel(menuModel);
		composite.addPopupDetectionListener(new IPopupDetectionListener() {

			@Override
			public void popupDetected(final Position position) {
				popupMenu.show(position);
			}
		});

		menuModel.getChildren().get(5).setText("renamed in model");
		((IMenuModel) ((IMenuModel) (menuModel.getChildren().get(0))).getChildren().get(2)).removeItem(3);

		popupMenu.addSeparator();
		final ISubMenu subMenu = popupMenu.addItem(BPF.subMenu("menu added to view"));
		subMenu.addItem(BPF.checkedMenuItem("sub menu item 1 added to view"));
		subMenu.addItem(BPF.checkedMenuItem("sub menu item 2 added to view"));
	}

	private void createActions() {
		final IActionBuilderFactory actionBF = Toolkit.getActionBuilderFactory();

		final IActionBuilder action1Builder = actionBF.create();
		action1Builder.setText("Action1").setToolTipText("The tooltip of Action1").setIcon(IconsSmall.INFO);
		action1Builder.setMnemonic('c').setAccelerator(new Accelerator('A', Modifier.CTRL));
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
		action2Builder.setMnemonic('t').setAccelerator(new Accelerator('A', Modifier.ALT));
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
		menu1.setModel(menuModel);

		final IPopupMenu popupMenu = createPopupMenu();
		popupMenu.setModel(menuModel);
		addPopupDetectionListener(new IPopupDetectionListener() {

			@Override
			public void popupDetected(final Position position) {
				popupMenu.show(position);
			}
		});
	}

	private MenuModel createMenuModel() {

		final MenuModel menu = new MenuModel();
		menu.setText("Menu1");
		menu.setMnemonic('n');

		final MenuModel subMenu = new MenuModel(MenuModel.builder().setText("sub menu 1").setMnemonic('e'));
		menu.addItem(subMenu);

		subMenu.addItem(new ActionItemModel("sub item1"));
		subMenu.addItem(new ActionItemModel("sub item2"));

		final MenuModel subMenu2 = new MenuModel(MenuModel.builder().setText("sub menu 2").setMnemonic('n'));
		subMenu2.addItem(new ActionItemModel("sub item1"));
		subMenu2.addItem(new ActionItemModel("sub item2"));
		subMenu2.addItem(new ActionItemModel("sub item3"));
		subMenu2.addItem(new ActionItemModel("sub item4"));
		subMenu.addItem(subMenu2);

		subMenu.addItem(new ActionItemModel("sub item3"));
		subMenu.addSeparator();
		subMenu.addItem(new ActionItemModel("sub item4"));
		subMenu.addItem(new ActionItemModel("sub item5"));

		menu.addAction(action1);
		menu.addAction(action2);

		final IActionItemModelBuilder item2Builder = ActionItemModel.builder().setText("The Third Item");
		item2Builder.setToolTipText("This is the third item");
		item2Builder.setIcon(IconsSmall.WARNING).setAccelerator(new Accelerator('I', Modifier.SHIFT)).setMnemonic('e');
		final IActionItemModel item3 = new ActionItemModel(item2Builder);

		menu.addItem(1, item3);

		menu.addSeparator();

		final ICheckedItemModel item4 = new CheckedItemModel("item4");
		menu.addItem(item4);

		menu.addSeparator();

		final IRadioItemModel item5 = new RadioItemModel("item5");
		menu.addItem(item5);

		final IRadioItemModel item6 = new RadioItemModel("item6");
		item6.setSelected(true);
		menu.addItem(item6);

		final IRadioItemModel item7 = new RadioItemModel("item7");
		menu.addItem(item7);

		item3.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				// CHECKSTYLE:OFF
				System.out.println("Item3");
				// CHECKSTYLE:ON
			}
		});

		item4.addItemListener(new IItemStateListener() {
			@Override
			public void itemStateChanged() {
				// CHECKSTYLE:OFF
				System.out.println("Item4, selected=" + item4.isSelected());
				// CHECKSTYLE:ON
			}
		});

		item5.addItemListener(new IItemStateListener() {
			@Override
			public void itemStateChanged() {
				// CHECKSTYLE:OFF
				System.out.println("Item5, selected=" + item5.isSelected());
				// CHECKSTYLE:ON
			}
		});

		item6.addItemListener(new IItemStateListener() {
			@Override
			public void itemStateChanged() {
				// CHECKSTYLE:OFF
				System.out.println("Item6, selected=" + item6.isSelected());
				// CHECKSTYLE:ON
			}
		});

		item7.addItemListener(new IItemStateListener() {
			@Override
			public void itemStateChanged() {
				// CHECKSTYLE:OFF
				System.out.println("Item7, selected=" + item7.isSelected());
				// CHECKSTYLE:ON
			}
		});

		return menu;

	}
}
