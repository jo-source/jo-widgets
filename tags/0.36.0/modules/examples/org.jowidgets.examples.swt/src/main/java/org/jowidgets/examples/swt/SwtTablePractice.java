/*
 * Copyright (c) 2014, grossmann
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

package org.jowidgets.examples.swt;

import java.util.Random;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jowidgets.api.color.Colors;
import org.jowidgets.api.model.table.ITableCellBuilder;
import org.jowidgets.api.model.table.ITableColumn;
import org.jowidgets.api.model.table.ITableModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.model.ITableColumnModelObservable;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.model.table.AbstractTableDataModel;
import org.jowidgets.tools.model.table.DefaultTableColumn;
import org.jowidgets.tools.model.table.TableCell;
import org.jowidgets.tools.widgets.blueprint.BPF;

public class SwtTablePractice implements IApplication {

	private static final Random RANDOM = new Random();

	@Override
	public void start(final IApplicationLifecycle lifecycle) {
		//SwtOptions.setClassicTableSelectionColors();

		final IFrame rootFrame = Toolkit.createRootFrame(BPF.frame().setTitle("Table with controls"), lifecycle);
		rootFrame.setLayout(MigLayoutFactory.growingInnerCellLayout());

		final TableModel model = new TableModel();

		final ITable table = rootFrame.add(BPF.table(model), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		final Table swtTable = (Table) table.getUiReference();

		final Color evenColor = new Color(Display.getCurrent(), new RGB(222, 235, 235));
		final Color oddColor = new Color(Display.getCurrent(), new RGB(255, 255, 255));
		final Color inColor = new Color(Display.getCurrent(), new RGB(20, 95, 25));
		final Color outColor = new Color(Display.getCurrent(), new RGB(150, 0, 0));

		final TableItem[] items = swtTable.getItems();
		for (int i = 0; i < items.length; i++) {
			final TableEditor editor = new TableEditor(swtTable);

			final Color background;
			if (i % 2 != 0) {
				background = evenColor;
			}
			else {
				background = oddColor;
			}

			final Composite composite = new Composite(swtTable, SWT.NONE);
			composite.setBackground(background);
			composite.setLayout(new MigLayout("", "[]12[]12[]12[]12[]", "1[]1"));

			createLabel(composite, "A", background, inColor, outColor);
			createLabel(composite, "B", background, inColor, outColor);
			createLabel(composite, "C", background, inColor, outColor);
			createLabel(composite, "D", background, inColor, outColor);
			createLabel(composite, "E", background, inColor, outColor);

			editor.grabHorizontal = true;
			editor.setEditor(composite, items[i], 5);

			//			editor = new TableEditor(swtTable);
			//			final Text text = new Text(swtTable, SWT.NONE);
			//			editor.grabHorizontal = true;
			//			editor.setEditor(text, items[i], 2);
			//			editor = new TableEditor(swtTable);
			//			final Button button = new Button(swtTable, SWT.CHECK);
			//			button.pack();
			//			editor.minimumWidth = button.getSize().x;
			//			editor.horizontalAlignment = SWT.LEFT;
			//			editor.setEditor(button, items[i], 3);
		}

		rootFrame.setSize(800, 800);
		rootFrame.setVisible(true);
	}

	private Label createLabel(final Composite parent, final String text, final Color background, final Color in, final Color out) {
		final Label label = new Label(parent, SWT.NONE);
		label.setText(text);
		label.setBackground(background);
		if (RANDOM.nextBoolean()) {
			label.setForeground(in);
		}
		else {
			label.setForeground(out);
		}
		return label;
	}

	public static void main(final String[] args) {
		Toolkit.getInstance().getApplicationRunner().run(new SwtTablePractice());
	}

	private final class TableModel extends AbstractTableDataModel implements ITableModel {

		@Override
		public int getColumnCount() {
			return 5;
		}

		@Override
		public ITableColumn getColumn(final int columnIndex) {
			return new DefaultTableColumn("Column " + columnIndex);
		}

		@Override
		public ITableColumnModelObservable getTableColumnModelObservable() {
			return null;
		}

		@Override
		public int getRowCount() {
			return 200;
		}

		@Override
		public ITableCell getCell(final int rowIndex, final int columnIndex) {
			final ITableCellBuilder builder = TableCell.builder();
			builder.setText("Cell " + rowIndex + " / " + columnIndex);
			if (rowIndex % 2 != 0) {
				builder.setBackgroundColor(Colors.DEFAULT_TABLE_EVEN_BACKGROUND_COLOR);
			}
			return builder.build();
		}

	}
}
