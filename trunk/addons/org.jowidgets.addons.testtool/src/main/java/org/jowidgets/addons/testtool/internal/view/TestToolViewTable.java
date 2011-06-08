/*
 * Copyright (c) 2011, Lukas Gross
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

package org.jowidgets.addons.testtool.internal.view;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.model.table.ISimpleTableModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.ITabFolder;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.blueprint.ITableBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.model.table.DefaultTableColumnBuilder;

// CHECKSTYLE:OFF
public class TestToolViewTable {

	private static final IBluePrintFactory BPF = Toolkit.getBluePrintFactory();
	private static ISimpleTableModel tableDataModel;

	public TestToolViewTable(final IFrame frame) {
		final ITabFolder folder = frame.add(BPF.tabFolder(), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
		folder.setSize(new Dimension(200, 300));
		final ITabItem item = folder.addItem(BPF.tabItem().setText("Test Data"));
		tableDataModel = Toolkit.getModelFactoryProvider().getTableModelFactory().simpleTableModel();
		final DefaultTableColumnBuilder colBuilder = new DefaultTableColumnBuilder();
		tableDataModel.addColumn(colBuilder.setText("Step").build());
		tableDataModel.addColumn(colBuilder.setText("Widget").build());
		tableDataModel.addColumn(colBuilder.setText("User Action").build());
		final ITableBluePrint tableBluePrint = BPF.table(tableDataModel);
		item.setLayout(MigLayoutFactory.growingInnerCellLayout());
		final ITable table = item.add(tableBluePrint, "grow");
		item.setBackgroundColor(Colors.WHITE);
		table.pack();
		tableDataModel.addColumn(colBuilder.setText("Value").setWidth(100).build());
		tableDataModel.addColumn(colBuilder.setText("Property").setWidth(100).build());
		tableDataModel.addColumn(colBuilder.setText("ID").setWidth(200).build());
	}

	public static ISimpleTableModel getTableModel() {
		return tableDataModel;
	}
}
