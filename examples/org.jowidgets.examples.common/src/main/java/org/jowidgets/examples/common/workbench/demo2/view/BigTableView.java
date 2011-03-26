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

package org.jowidgets.examples.common.workbench.demo2.view;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.table.IDefaultTableColumnBuilder;
import org.jowidgets.api.model.table.IDefaultTableColumnModel;
import org.jowidgets.api.model.table.ITableCellBuilder;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModel;
import org.jowidgets.common.model.ITableDataModel;
import org.jowidgets.common.widgets.controler.ITableCellPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableCellPopupEvent;
import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.examples.common.workbench.base.AbstractDemoView;
import org.jowidgets.tools.command.ActionBuilder;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.model.table.AbstractTableDataModel;
import org.jowidgets.tools.model.table.DefaultTableColumnBuilder;
import org.jowidgets.tools.model.table.DefaultTableColumnModel;
import org.jowidgets.tools.model.table.TableCellBuilder;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;

public class BigTableView extends AbstractDemoView implements IView {

	public static final String ID = BigTableView.class.getName();
	public static final String DEFAULT_LABEL = "Big table";
	public static final String DEFAULT_TOOLTIP = "Table with a lot of data";
	public static final IImageConstant DEFAULT_ICON = SilkIcons.TABLE;

	private static final int ROW_COUNT = 2000000;
	private static final int COLUMN_COUNT = 50;

	private final ITable table;
	private final IAction fitColumnsAction;
	private final IAction resetPermutationAction;

	public BigTableView(final IViewContext context) {
		super(ID);

		this.fitColumnsAction = createFitColumnsAction();
		this.resetPermutationAction = createResetPermutationAction();

		context.getToolBar().addAction(resetPermutationAction);
		context.getToolBar().addAction(fitColumnsAction);

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
		final IContainer container = context.getContainer();
		container.setLayout(MigLayoutFactory.growingInnerCellLayout());

		final ITableColumnModel columnModel = createColumnModel();
		final ITableDataModel dataModel = createDataModel();

		this.table = container.add(bpf.table(columnModel, dataModel), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
		addPopupMenu(table);
	}

	private ITableColumnModel createColumnModel() {
		final IDefaultTableColumnModel columnModel = new DefaultTableColumnModel(COLUMN_COUNT);
		for (int columnIndex = 0; columnIndex < columnModel.getColumnCount(); columnIndex++) {
			final IDefaultTableColumnBuilder columnBuilder = new DefaultTableColumnBuilder();
			columnBuilder.setText("Column " + columnIndex);
			columnBuilder.setToolTipText("Tooltip of column " + columnIndex);
			columnBuilder.setWidth(100);
			columnModel.setColumn(columnIndex, columnBuilder);
		}
		return columnModel;
	}

	private ITableDataModel createDataModel() {
		return new AbstractTableDataModel() {

			@Override
			public int getRowCount() {
				return ROW_COUNT;
			}

			@Override
			public ITableCell getCell(final int rowIndex, final int columnIndex) {
				final ITableCellBuilder cellBuilder = new TableCellBuilder();
				cellBuilder.setText("Cell (" + rowIndex + " / " + columnIndex + ")");
				if (rowIndex % 2 == 0) {
					cellBuilder.setBackgroundColor(Colors.DEFAULT_TABLE_EVEN_BACKGROUND_COLOR);
				}
				return cellBuilder.build();
			}
		};
	}

	private void addPopupMenu(final ITable table) {
		final IMenuModel menuModel = new MenuModel();
		menuModel.addAction(fitColumnsAction);
		menuModel.addAction(resetPermutationAction);

		final IPopupMenu popupMenu = table.createPopupMenu();
		popupMenu.setModel(menuModel);

		table.addTableCellPopupDetectionListener(new ITableCellPopupDetectionListener() {
			@Override
			public void popupDetected(final ITableCellPopupEvent event) {
				popupMenu.show(event.getPosition());
			}
		});
	}

	private IAction createFitColumnsAction() {
		final IActionBuilder builder = new ActionBuilder();
		builder.setText("Fit columns");
		builder.setToolTipText("Fits all columns of the table");
		builder.setIcon(SilkIcons.ARROW_INOUT);

		builder.setCommand(new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				table.pack();
			}
		});
		return builder.build();
	}

	private IAction createResetPermutationAction() {
		final IActionBuilder builder = new ActionBuilder();
		builder.setText("Reset column order");
		builder.setToolTipText("Sets all columns to its origin position");
		builder.setIcon(SilkIcons.ARROW_UNDO);

		builder.setCommand(new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				table.resetColumnPermutation();
			}
		});
		return builder.build();
	}
}
