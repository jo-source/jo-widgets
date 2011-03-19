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

package org.jowidgets.examples.common.workbench.demo1;

import java.util.List;

import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.table.ISimpleTableModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IInputDialog;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.ITableCellEditEvent;
import org.jowidgets.common.widgets.controler.ITableCellEditorListener;
import org.jowidgets.common.widgets.controler.ITableCellEvent;
import org.jowidgets.common.widgets.controler.ITableCellPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableCellPopupEvent;
import org.jowidgets.common.widgets.controler.ITableColumnPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableColumnPopupEvent;
import org.jowidgets.examples.common.demo.DemoInputDialog1;
import org.jowidgets.examples.common.demo.DemoMenuProvider;
import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.examples.common.workbench.base.AbstractView;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.model.table.SimpleTableModelBuilder;
import org.jowidgets.util.ValueHolder;
import org.jowidgets.workbench.api.IComponentTreeNodeContext;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;

public class ViewDemo6 extends AbstractView implements IView {

	public static final String ID = ViewDemo6.class.getName();
	public static final String DEFAULT_LABEL = "View6";
	public static final String DEFAULT_TOOLTIP = "View6 tooltip";
	public static final IImageConstant DEFAULT_ICON = SilkIcons.ROSETTE;

	public ViewDemo6(final IViewContext context, final DemoMenuProvider menuProvider) {
		super(ID);
		context.getToolBar().addItemsOfModel(menuProvider.getToolBarModel());
		context.getToolBarMenu().addItemsOfModel(menuProvider.getMenuModel());

		final ActionFactory actionFactory = new ActionFactory();
		final IComponentTreeNodeContext treeNodeContent = context.getComponentTreeNodeContext();
		treeNodeContent.getPopupMenu().addSeparator();
		treeNodeContent.getPopupMenu().addAction(actionFactory.createActivateViewAction(context, DEFAULT_LABEL));
		treeNodeContent.getPopupMenu().addAction(actionFactory.createUnHideViewAction(context, DEFAULT_LABEL));
		context.getToolBarMenu().addSeparator();
		context.getToolBarMenu().addAction(actionFactory.createHideViewAction(context));

		createContent(context.getContainer());
	}

	private void createContent(final IContainer container) {

		container.setLayout(MigLayoutFactory.growingInnerCellLayout());
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		final ISimpleTableModel tableModel = new SimpleTableModelBuilder().setEditableDefault(true).build();
		tableModel.addColumn("Gender");
		tableModel.addColumn("Firstname");
		tableModel.addColumn("Lastname");
		tableModel.addColumn("Street");
		tableModel.addColumn("Postal code");
		tableModel.addColumn("City");
		tableModel.addColumn("Country");
		tableModel.addColumn("Phone number");
		tableModel.addColumn("Email");

		tableModel.addRow(
				"Male",
				"Pete",
				"Brown",
				"Audubon Ave 34",
				"76453",
				"New York",
				"USA",
				"47634826",
				"hans.maier@gtzservice.com");

		tableModel.addRow(
				"Male",
				"Steve",
				"Miller",
				"Convent Ave 34",
				"534534",
				"New York",
				"USA",
				"4354354",
				"steve.miller@gjk.com");

		tableModel.addRow(
				"Female",
				"Laura",
				"Brixton",
				"West End Ave 34",
				"534534",
				"New York",
				"USA",
				"435345345",
				"laura.brixton@gjk.com");

		final ITable table = container.add(bpf.table(tableModel), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
		table.pack();

		final ValueHolder<Integer> currentRow = new ValueHolder<Integer>();
		final ValueHolder<Integer> currentColumn = new ValueHolder<Integer>();

		final IPopupMenu cellPopupMenu = table.createPopupMenu();
		final IMenuModel cellPopupMenuModel = cellPopupMenu.getModel();

		table.addTableCellPopupDetectionListener(new ITableCellPopupDetectionListener() {
			@Override
			public void popupDetected(final ITableCellPopupEvent event) {
				currentColumn.set(Integer.valueOf(event.getColumnIndex()));
				currentRow.set(Integer.valueOf(event.getRowIndex()));
				cellPopupMenu.show(event.getPosition());
			}
		});

		final IActionItemModel deleteRowAction = cellPopupMenuModel.addActionItem("Delete row");
		deleteRowAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				tableModel.removeRow(currentRow.get().intValue());
			}
		});

		final IActionItemModel deleteSelectedRows = cellPopupMenuModel.addActionItem("Delete selected rows");
		deleteSelectedRows.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				tableModel.removeRows(table.getSelection());
			}
		});

		final IActionItemModel addRow = cellPopupMenuModel.addActionItem("Add person");
		addRow.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final List<String> row = createRow();
				if (row != null) {
					tableModel.addRow(currentRow.get().intValue() + 1, row);
				}
			}
		});

		final IPopupMenu columnPopupMenu = table.createPopupMenu();
		final IMenuModel columnPopupMenuModel = columnPopupMenu.getModel();

		table.addTableColumnPopupDetectionListener(new ITableColumnPopupDetectionListener() {
			@Override
			public void popupDetected(final ITableColumnPopupEvent event) {
				currentColumn.set(Integer.valueOf(event.getColumnIndex()));
				columnPopupMenu.show(event.getPosition());
			}
		});

		final IActionItemModel deleteColumnAction = columnPopupMenuModel.addActionItem("Delete column");
		deleteColumnAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				tableModel.removeColumn(currentColumn.get().intValue());
			}
		});

		table.addTableCellEditorListener(new ITableCellEditorListener() {

			@Override
			public void onEdit(final IVetoable veto, final ITableCellEditEvent event) {}

			@Override
			public void editFinished(final ITableCellEditEvent event) {
				tableModel.setCell(event.getRowIndex(), event.getColumnIndex(), event.getCurrentText());
			}

			@Override
			public void editCanceled(final ITableCellEvent event) {}
		});
	}

	private List<String> createRow() {
		final IInputDialog<List<String>> inputDialog = new DemoInputDialog1().getInputDialog();
		inputDialog.setVisible(true);
		if (inputDialog.isOkPressed()) {
			return inputDialog.getValue();
		}
		return null;
	}
}
