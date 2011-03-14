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

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.model.table.IDefaultTableColumn;
import org.jowidgets.api.model.table.IDefaultTableColumnModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.blueprint.ITableBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableModel;
import org.jowidgets.examples.common.demo.DemoMenuProvider;
import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.examples.common.workbench.base.AbstractView;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.model.table.AbstractTableModel;
import org.jowidgets.tools.model.table.DefaultTableColumnModel;
import org.jowidgets.tools.model.table.TableCell;
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

		final IComposite content = container.add(
				bpf.composite().setBackgroundColor(Colors.WHITE),
				MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		content.setLayout(MigLayoutFactory.growingInnerCellLayout());

		final int rowCount = 200;
		final int columnCount = 10;

		final IDefaultTableColumnModel columnModel = new DefaultTableColumnModel(columnCount);

		int columnIndex = 0;
		for (final IDefaultTableColumn column : columnModel.getColumns()) {
			column.setText("Column " + columnIndex);
			column.setToolTipText("Tooltip of column " + columnIndex);
			columnIndex++;
		}

		final ITableModel tableModel = new AbstractTableModel() {

			@Override
			public int getRowCount() {
				return rowCount;
			}

			@Override
			public ITableCell getCell(final int rowIndex, final int columnIndex) {
				return new TableCell() {

					@Override
					public String getText() {
						return "Cell (" + rowIndex + " / " + columnIndex + ")";
					}

					@Override
					public IColorConstant getBackgroundColor() {
						if (rowIndex % 2 == 0) {
							return new ColorValue(222, 235, 235);
						}
						return null;
					}

					@Override
					public boolean isEditable() {
						return true;
					}

				};
			}
		};

		final ITableBluePrint tableBp = bpf.table().setTableModel(tableModel).setColumnModel(columnModel);
		tableBp.setColumnModel(columnModel);
		content.add(tableBp, MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
	}
}
