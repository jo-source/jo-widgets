package org.jowidgets.examples.rcp3.nattable;

import org.jowidgets.api.model.table.ITableColumn;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.controller.IMouseButtonEvent;
import org.jowidgets.common.widgets.controller.ITableCellMouseEvent;
import org.jowidgets.common.widgets.controller.ITableCellPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableCellPopupEvent;
import org.jowidgets.common.widgets.controller.ITableColumnMouseEvent;
import org.jowidgets.common.widgets.controller.ITableColumnPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableColumnPopupEvent;
import org.jowidgets.common.widgets.controller.ITableSelectionListener;
import org.jowidgets.tools.controller.MouseAdapter;
import org.jowidgets.tools.controller.TableCellAdapter;
import org.jowidgets.tools.controller.TableColumnAdapter;
import org.jowidgets.tools.controller.TableColumnModelObservable;
import org.jowidgets.tools.controller.TableDataModelAdapter;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.model.table.AbstractTableModel;
import org.jowidgets.tools.model.table.DefaultTableColumn;
import org.jowidgets.tools.model.table.TableCell;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.util.ObservableBoolean;
import org.jowidgets.util.ObservableValue;

public final class NatTableExample implements IApplication {

	@Override
	public void start(IApplicationLifecycle lifecycle) {
		IFrame frame = Toolkit.createRootFrame(BPF.frame("Nattable").setSize(new Dimension(800, 600)), lifecycle);
		frame.setLayout(MigLayoutFactory.growingInnerCellLayout());

		final ObservableBoolean option = new ObservableBoolean();

		final TableModel tableModel = new TableModel(option);
		final ITable table = frame.add(BPF.table(tableModel), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		table.addTableSelectionListener(new ITableSelectionListener() {
			@Override
			public void selectionChanged() {
				System.out.println("ViewSelection: " + table.getSelection());
			}
		});

		tableModel.addDataModelListener(new TableDataModelAdapter() {
			@Override
			public void selectionChanged() {
				System.out.println("ModelSelection: " + tableModel.getSelection());
			}
		});

		final MenuModel menu = new MenuModel();
		menu.addActionItem("ChangeData").addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				option.set(!option.getValue());
				tableModel.fireDataChanged();
			}
		});
		menu.addActionItem("Add Column").addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				tableModel.addColumn();
			}
		});
		menu.addActionItem("Remove Column").addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				tableModel.removeColumn();
			}
		});
		menu.addActionItem("Add Row").addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				tableModel.addRow();
			}
		});
		menu.addActionItem("Remove Row").addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				tableModel.removeRow();
			}
		});

		final IPopupMenu popupMenu = table.createPopupMenu();
		popupMenu.setModel(menu);

		table.addTableCellPopupDetectionListener(new ITableCellPopupDetectionListener() {
			@Override
			public void popupDetected(final ITableCellPopupEvent event) {
				popupMenu.show(event.getPosition());
				System.out.println("Cell popup: " + event);
			}
		});

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClicked(final IMouseButtonEvent event) {
				System.out.println("DoubleClick: " + event);
			}
		});

		table.addTableColumnListener(new TableColumnAdapter() {
			@Override
			public void mouseClicked(final ITableColumnMouseEvent event) {
				System.out.println("Clicked to header: " + event);
			}
		});

		table.addTableCellListener(new TableCellAdapter() {
			@Override
			public void mouseDoubleClicked(final ITableCellMouseEvent event) {
				System.out.println("DoubleClick: " + event);
			}
		});

		table.addTableColumnPopupDetectionListener(new ITableColumnPopupDetectionListener() {
			@Override
			public void popupDetected(final ITableColumnPopupEvent event) {
				System.out.println("Column popup: " + event);
			}
		});
		
		frame.setVisible(true);

	}

	private final class TableModel extends AbstractTableModel {

		private final TableColumnModelObservable columnModelObservable;;
		private final ObservableValue<Boolean> cellOption;

		private int columnCount;
		private int rowCount;

		TableModel(final ObservableValue<Boolean> cellOption) {
			this.columnModelObservable = new TableColumnModelObservable();
			this.cellOption = cellOption;
			this.rowCount = 20000;
			this.columnCount = 50;
		}

		void addRow() {
			rowCount++;
			fireRowsAdded(new int[] {rowCount - 1});
		}

		void removeRow() {
			rowCount--;
			fireRowsRemoved(new int[] {rowCount});
		}

		void addColumn() {
			columnCount++;
			columnModelObservable.fireColumnsAdded(new int[] {columnCount - 1});
		}

		void removeColumn() {
			columnCount--;
			columnModelObservable.fireColumnsRemoved(new int[] {columnCount});
		}

		@Override
		public ITableColumnModelObservable getTableColumnModelObservable() {
			return columnModelObservable;
		}

		@Override
		public int getColumnCount() {
			return columnCount;
		}

		@Override
		public int getRowCount() {
			return rowCount;
		}

		@Override
		public ITableColumn getColumn(final int columnIndex) {
			return new DefaultTableColumn("Column " + columnIndex, "columnTooltip: " + columnIndex);
		}

		@Override
		public ITableCell getCell(final int rowIndex, final int columnIndex) {
			if (Boolean.TRUE.equals(cellOption.getValue())) {
				return TableCell.builder(
					"Cell Opt(" + rowIndex + "/" + columnIndex + ")",
					"Tooltip (" + rowIndex + "/" + columnIndex + ")").build();
			}
			return TableCell.builder("Cell (" + rowIndex + "/" + columnIndex + ")", "Tooltip (" + rowIndex + "/" + columnIndex + ")").build();
		}

	}

}
