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
import java.util.HashMap;
import java.util.Map;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.blueprint.ITableBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.types.TableColumnPackPolicy;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.ITableCellEditEvent;
import org.jowidgets.common.widgets.controler.ITableCellEditorListener;
import org.jowidgets.common.widgets.controler.ITableCellEvent;
import org.jowidgets.common.widgets.controler.ITableCellListener;
import org.jowidgets.common.widgets.controler.ITableCellMouseEvent;
import org.jowidgets.common.widgets.controler.ITableCellPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableCellPopupEvent;
import org.jowidgets.common.widgets.controler.ITableColumnListener;
import org.jowidgets.common.widgets.controler.ITableColumnMouseEvent;
import org.jowidgets.common.widgets.controler.ITableColumnPopupDetectionListener;
import org.jowidgets.common.widgets.controler.ITableColumnPopupEvent;
import org.jowidgets.common.widgets.controler.ITableColumnResizeEvent;
import org.jowidgets.common.widgets.controler.ITableSelectionListener;
import org.jowidgets.common.widgets.model.ITableCell;
import org.jowidgets.common.widgets.model.ITableColumn;
import org.jowidgets.common.widgets.model.ITableColumnModel;
import org.jowidgets.common.widgets.model.ITableColumnModelObservable;
import org.jowidgets.common.widgets.model.ITableModel;
import org.jowidgets.common.widgets.model.ITableModelObservable;
import org.jowidgets.examples.common.demo.DemoMenuProvider;
import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.examples.common.workbench.base.AbstractView;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.util.ValueHolder;
import org.jowidgets.workbench.api.IViewContext;

public class ViewDemo1 extends AbstractView {

	public static final String ID = ViewDemo1.class.getName();
	public static final String DEFAULT_LABEL = "View1";
	public static final String DEFAULT_TOOLTIP = "View1 tooltip";
	public static final IImageConstant DEFAULT_ICON = SilkIcons.STATUS_ONLINE;

	public ViewDemo1(final IViewContext context, final DemoMenuProvider menuProvider) {
		super(ID);

		context.getToolBar().addItemsOfModel(menuProvider.getToolBarModel());
		context.getToolBarMenu().addItemsOfModel(menuProvider.getMenuModel());

		createContent(context.getContainer());
	}

	private void createContent(final IContainer container) {

		container.setLayout(MigLayoutFactory.growingInnerCellLayout());
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		final IComposite content = container.add(
				bpf.composite().setBackgroundColor(Colors.WHITE),
				MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		content.setLayout(MigLayoutFactory.growingInnerCellLayout());

		final int rowCount = 2000;
		final int columnCount = 13;

		final ITableBluePrint tableBp = bpf.table();

		tableBp.setColumnModel(new ITableColumnModel() {

			private final Map<Integer, ITableColumn> columns = new HashMap<Integer, ITableColumn>();

			@Override
			public int getColumnCount() {
				return columnCount;
			}

			@Override
			public ITableColumn getColumn(final int columnIndex) {
				ITableColumn result = columns.get(Integer.valueOf(columnIndex));
				if (result == null) {
					result = new ITableColumn() {

						private int width = 100;

						@Override
						public void setWidth(final int width) {
							this.width = width;
						}

						@Override
						public int getWidth() {
							return width;
						}

						@Override
						public String getToolTipText() {
							return "Tooltip of column " + columnIndex;
						}

						@Override
						public String getText() {
							return "Column " + columnIndex;
						}

						@Override
						public IImageConstant getIcon() {
							if (columnIndex == 2) {
								return SilkIcons.ARROW_DOWN;
							}
							return null;
						}

						@Override
						public AlignmentHorizontal getAlignment() {
							return null;
						}
					};
				}
				columns.put(Integer.valueOf(columnIndex), result);
				return result;
			}

			@Override
			public ITableColumnModelObservable getTableColumnModelObservable() {
				return null;
			}
		});

		tableBp.setTableModel(new ITableModel() {

			private ArrayList<Integer> selection;

			@Override
			public int getRowCount() {
				return rowCount;
			}

			@Override
			public ITableCell getCell(final int rowIndex, final int columnIndex) {
				//CHECKSTYLE:OFF
				//System.out.println(rowIndex);
				//CHECKSTYLE:ON

				return new ITableCell() {

					@Override
					public boolean isEditable() {
						return true;
					}

					@Override
					public String getToolTipText() {
						return null;
					}

					@Override
					public String getText() {
						return "Cell (" + rowIndex + " / " + columnIndex + ")";
					}

					@Override
					public Markup getMarkup() {
						return null;
					}

					@Override
					public IImageConstant getIcon() {
						return null;
					}

					@Override
					public IColorConstant getForegroundColor() {
						return null;
					}

					@Override
					public IColorConstant getBackgroundColor() {
						if (rowIndex % 2 == 0) {
							return new ColorValue(222, 235, 235);
						}
						return null;
					}

				};
			}

			@Override
			public ArrayList<Integer> getSelection() {
				if (selection == null) {
					selection = new ArrayList<Integer>(2);
					selection.add(Integer.valueOf(0));
					selection.add(Integer.valueOf(2));
				}
				return selection;
			}

			@Override
			public void setSelection(final ArrayList<Integer> selection) {
				this.selection = selection;
			}

			@Override
			public ITableModelObservable getTableModelObservable() {
				return null;
			}

		});

		final ITable table = content.add(tableBp, MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		table.addTableSelectionListener(new ITableSelectionListener() {
			@Override
			public void selectionChanged() {
				//CHECKSTYLE:OFF
				System.out.println("New selection: " + table.getSelection());
				//CHECKSTYLE:ON
			}
		});

		table.addTableCellListener(new ITableCellListener() {

			@Override
			public void mouseReleased(final ITableCellMouseEvent event) {
				//CHECKSTYLE:OFF
				System.out.println("mouseReleased: " + event);
				//CHECKSTYLE:ON
			}

			@Override
			public void mousePressed(final ITableCellMouseEvent event) {
				//CHECKSTYLE:OFF
				System.out.println("mousePressed: " + event);
				//CHECKSTYLE:ON
			}

			@Override
			public void mouseDoubleClicked(final ITableCellMouseEvent event) {
				//CHECKSTYLE:OFF
				System.out.println("mouseDoubleClicked: " + event);
				//CHECKSTYLE:ON
			}
		});

		table.addTableColumnListener(new ITableColumnListener() {

			@Override
			public void mouseClicked(final ITableColumnMouseEvent event) {
				//CHECKSTYLE:OFF
				System.out.println("mouseClicked: " + event);
				//CHECKSTYLE:ON
			}

			@Override
			public void columnResized(final ITableColumnResizeEvent event) {
				//CHECKSTYLE:OFF
				System.out.println("columnResized: " + event);
				//CHECKSTYLE:ON
			}

			@Override
			public void columnPermutationChanged() {
				//CHECKSTYLE:OFF
				System.out.println("columnPermutationChanged: " + table.getColumnPermutation());
				//CHECKSTYLE:ON
			}
		});

		table.addTableCellEditorListener(new ITableCellEditorListener() {

			@Override
			public void onEdit(final IVetoable veto, final ITableCellEditEvent event) {
				//CHECKSTYLE:OFF
				System.out.println("onEdit: " + event);
				//CHECKSTYLE:ON
			}

			@Override
			public void editFinished(final ITableCellEditEvent event) {
				//CHECKSTYLE:OFF
				System.out.println("editFinished: " + event);
				//CHECKSTYLE:ON
			}

			@Override
			public void editCanceled(final ITableCellEvent event) {
				//CHECKSTYLE:OFF
				System.out.println("editCanceled: " + event);
				//CHECKSTYLE:ON
			}
		});

		final ValueHolder<Integer> selectedColumn = new ValueHolder<Integer>();
		final ValueHolder<Integer> selectedRow = new ValueHolder<Integer>();

		final IPopupMenu popupMenu = table.createPopupMenu();
		final IActionItemModel item1 = popupMenu.getModel().addActionItem();
		final IActionItemModel reloadAction = popupMenu.getModel().addActionItem("Reload", SilkIcons.ARROW_REFRESH_SMALL);
		final IActionItemModel packTableAction = popupMenu.getModel().addActionItem("Fit all columns", SilkIcons.ARROW_INOUT);
		final IActionItemModel packColumnAction = popupMenu.getModel().addActionItem("Fit column", SilkIcons.ARROW_INOUT);

		reloadAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				table.setCursor(Cursor.WAIT);
				table.initialize();
				table.setCursor(Cursor.DEFAULT);
			}
		});

		packTableAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				table.setCursor(Cursor.WAIT);
				table.pack(TableColumnPackPolicy.HEADER_AND_CONTENT);
				table.setCursor(Cursor.DEFAULT);
			}
		});

		packColumnAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				table.setCursor(Cursor.WAIT);
				table.pack(selectedColumn.get().intValue(), TableColumnPackPolicy.HEADER_AND_CONTENT);
				table.setCursor(Cursor.DEFAULT);
			}
		});

		table.addTableCellPopupDetectionListener(new ITableCellPopupDetectionListener() {
			@Override
			public void popupDetected(final ITableCellPopupEvent event) {
				selectedColumn.set(Integer.valueOf(event.getColumnIndex()));
				selectedRow.set(Integer.valueOf(event.getRowIndex()));
				item1.setText("Item1 (" + event.getRowIndex() + " / " + event.getColumnIndex() + ")");
				popupMenu.show(event.getPosition());
			}
		});

		table.addTableColumnPopupDetectionListener(new ITableColumnPopupDetectionListener() {
			@Override
			public void popupDetected(final ITableColumnPopupEvent event) {
				selectedColumn.set(Integer.valueOf(event.getColumnIndex()));
				item1.setText("Item1 (" + event.getColumnIndex() + ")");
				popupMenu.show(event.getPosition());
			}
		});
	}
}
