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

import java.util.ArrayList;
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
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.TablePackPolicy;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableCellEditEvent;
import org.jowidgets.common.widgets.controller.ITableCellEditorListener;
import org.jowidgets.common.widgets.controller.ITableCellEvent;
import org.jowidgets.common.widgets.controller.ITableCellPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableCellPopupEvent;
import org.jowidgets.common.widgets.controller.ITableColumnPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableColumnPopupEvent;
import org.jowidgets.examples.common.demo.DemoInputDialog1;
import org.jowidgets.examples.common.demo.DemoMenuProvider;
import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.examples.common.workbench.base.AbstractDemoView;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.model.table.DefaultTableColumn;
import org.jowidgets.util.ValueHolder;
import org.jowidgets.workbench.api.IViewContext;

public class ViewDemo1 extends AbstractDemoView {

	public static final String ID = ViewDemo1.class.getName();
	public static final String DEFAULT_LABEL = "Persons";
	public static final String DEFAULT_TOOLTIP = "Shows all person";
	public static final IImageConstant DEFAULT_ICON = SilkIcons.USER;

	private final InputDialogFactory inputDialogFactory;
	private ITable table;

	public ViewDemo1(final IViewContext context, final DemoMenuProvider menuProvider, final ISimpleTableModel tableModel) {
		super(ID);
		this.inputDialogFactory = new InputDialogFactory();

		context.getToolBar().addItemsOfModel(menuProvider.getToolBarModel());
		context.getToolBarMenu().addItemsOfModel(menuProvider.getMenuModel());

		createContent(context.getContainer(), tableModel);
	}

	private void createContent(final IContainer container, final ISimpleTableModel tableModel) {

		container.setLayout(MigLayoutFactory.growingInnerCellLayout());
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		table = container.add(bpf.table(tableModel), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
		table.pack(TablePackPolicy.HEADER_AND_DATA_ALL);

		final ValueHolder<Integer> currentRow = new ValueHolder<Integer>();
		final ValueHolder<Integer> currentColumn = new ValueHolder<Integer>();

		final IPopupMenu cellPopupMenu = table.createPopupMenu();
		final IMenuModel cellPopupMenuModel = cellPopupMenu.getModel();

		final IPopupMenu popupMenu = table.createPopupMenu();
		final IMenuModel popupMenuModel = popupMenu.getModel();

		table.addPopupDetectionListener(new IPopupDetectionListener() {
			@Override
			public void popupDetected(final Position position) {
				currentColumn.set(null);
				currentRow.set(null);
				popupMenu.show(position);
			}
		});

		table.addTableCellPopupDetectionListener(new ITableCellPopupDetectionListener() {
			@Override
			public void popupDetected(final ITableCellPopupEvent event) {
				currentColumn.set(Integer.valueOf(event.getColumnIndex()));
				currentRow.set(Integer.valueOf(event.getRowIndex()));
				cellPopupMenu.show(event.getPosition());
			}
		});

		final IActionItemModel addRow = cellPopupMenuModel.addActionItem("Add person", SilkIcons.USER_ADD);
		addRow.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final List<String> row = createRow();
				if (row != null) {
					if (currentRow.get() != null) {
						tableModel.addRow(currentRow.get().intValue() + 1, row);
					}
					else {
						tableModel.addRow(table.getRowCount(), row);
					}
				}
			}
		});

		popupMenuModel.addItem(addRow);

		final IActionItemModel editRow = cellPopupMenuModel.addActionItem("Edit person", SilkIcons.USER_EDIT);
		editRow.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final int rowIndex = currentRow.get().intValue();
				final List<String> row = editRow(tableModel.getRowTexts(rowIndex));
				if (row != null) {
					tableModel.setRowTexts(rowIndex, row);
				}
			}
		});

		final IActionItemModel deleteRowAction = cellPopupMenuModel.addActionItem("Delete person", SilkIcons.USER_DELETE);
		deleteRowAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				tableModel.removeRow(currentRow.get().intValue());
			}
		});

		final IActionItemModel deleteSelectedRows = cellPopupMenuModel.addActionItem(
				"Delete selected persons",
				SilkIcons.USER_DELETE);
		deleteSelectedRows.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				tableModel.removeRows(table.getSelection());
			}
		});

		final IPopupMenu columnPopupMenu = table.createPopupMenu();
		final IMenuModel columnPopupMenuModel = columnPopupMenu.getModel();

		table.addTableColumnPopupDetectionListener(new ITableColumnPopupDetectionListener() {
			@Override
			public void popupDetected(final ITableColumnPopupEvent event) {
				currentColumn.set(Integer.valueOf(event.getColumnIndex()));
				currentRow.set(null);
				columnPopupMenu.show(event.getPosition());
			}
		});

		final IActionItemModel renameColumnAction = columnPopupMenuModel.addActionItem("Rename column");
		renameColumnAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IInputDialog<String> inputDialog = inputDialogFactory.createInputDialog("rename column", "Column name");
				inputDialog.setValue(tableModel.getColumn(currentColumn.get()).getText());
				inputDialog.setVisible(true);
				if (inputDialog.isOkPressed()) {
					tableModel.setColumnText(currentColumn.get().intValue(), inputDialog.getValue());
				}
			}
		});

		final IMenuModel alignmentMenu = columnPopupMenuModel.addMenu("Alignment");
		final IActionItemModel leftItem = alignmentMenu.addActionItem("Left");
		final IActionItemModel centerItem = alignmentMenu.addActionItem("Center");
		final IActionItemModel rightItem = alignmentMenu.addActionItem("Right");

		leftItem.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				tableModel.setColumnAlignment(currentColumn.get().intValue(), AlignmentHorizontal.LEFT);
			}
		});

		centerItem.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				tableModel.setColumnAlignment(currentColumn.get().intValue(), AlignmentHorizontal.CENTER);
			}
		});

		rightItem.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				tableModel.setColumnAlignment(currentColumn.get().intValue(), AlignmentHorizontal.RIGHT);
			}
		});

		final IActionItemModel addColumnAction = columnPopupMenuModel.addActionItem("Add column");
		addColumnAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final IInputDialog<String> inputDialog = inputDialogFactory.createInputDialog("add column", "Column name");
				inputDialog.setVisible(true);
				if (inputDialog.isOkPressed()) {
					final int newModelIndex = currentColumn.get() + 1;
					final int newViewIndex = table.convertColumnIndexToView(currentColumn.get()) + 1;
					final DefaultTableColumn column = new DefaultTableColumn(inputDialog.getValue());
					column.setWidth(100);
					tableModel.addColumn(newModelIndex, column);
					table.moveColumn(newModelIndex, newViewIndex);
				}
			}
		});

		final IActionItemModel invertColumnPermutationAction = columnPopupMenuModel.addActionItem("Invert column order");
		invertColumnPermutationAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final ArrayList<Integer> permutation = table.getColumnPermutation();
				final ArrayList<Integer> newPermutation = new ArrayList<Integer>(permutation.size());

				for (int i = 0; i < permutation.size(); i++) {
					newPermutation.add(permutation.get(permutation.size() - 1 - i));
				}
				table.setColumnPermutation(newPermutation);
			}
		});

		final IActionItemModel resetColumnPermutationAction = columnPopupMenuModel.addActionItem("Reset column order");
		resetColumnPermutationAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final ArrayList<Integer> newPermutation = new ArrayList<Integer>(table.getColumnCount());
				for (int i = 0; i < table.getColumnCount(); i++) {
					newPermutation.add(Integer.valueOf(i));
				}
				table.setColumnPermutation(newPermutation);
			}
		});

		final IActionItemModel resizeColumnAction = columnPopupMenuModel.addActionItem("Resize column");
		resizeColumnAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				tableModel.getColumn(currentColumn.get().intValue()).setWidth(200);
			}
		});

		final IActionItemModel deleteColumnAction = columnPopupMenuModel.addActionItem("Delete column");
		deleteColumnAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				tableModel.removeColumn(currentColumn.get().intValue());
			}
		});

		final IActionItemModel deleteColumnsAction = columnPopupMenuModel.addActionItem("Delete columns 0, 2, 4");
		deleteColumnsAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				final int[] columnsToRemove = getColumnsToRemove();
				if (columnsToRemove.length > 0) {
					tableModel.removeColumns(columnsToRemove);
				}
			}
		});

		final IActionItemModel fitColumnsAction = columnPopupMenuModel.addActionItem("Fit all columns", SilkIcons.ARROW_INOUT);
		fitColumnsAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				table.pack();
			}
		});

		popupMenuModel.addItem(fitColumnsAction);
		cellPopupMenuModel.addItem(fitColumnsAction);

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
		final IInputDialog<List<String>> inputDialog = new DemoInputDialog1("Add person", SilkIcons.USER_ADD).getInputDialog();
		inputDialog.setVisible(true);
		if (inputDialog.isOkPressed()) {
			return inputDialog.getValue();
		}
		return null;
	}

	private List<String> editRow(final List<String> row) {
		final IInputDialog<List<String>> inputDialog = new DemoInputDialog1("Edit person", SilkIcons.USER_EDIT).getInputDialog();
		inputDialog.setValue(row);
		inputDialog.setVisible(true);
		if (inputDialog.isOkPressed()) {
			return inputDialog.getValue();
		}
		return null;
	}

	private int[] getColumnsToRemove() {
		return new int[] {4, 2, 0};
	}
}
