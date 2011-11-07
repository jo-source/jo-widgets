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

import org.jowidgets.api.model.table.ISimpleTableModel;
import org.jowidgets.api.model.table.ITableModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.TablePackPolicy;
import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.model.table.SimpleTableModelBuilder;
import org.jowidgets.workbench.api.IViewContext;
import org.jowidgets.workbench.tools.AbstractView;

public class UserTableView extends AbstractView {

	public static final String ID = UserTableView.class.getName();
	public static final String DEFAULT_LABEL = "Persons";
	public static final String DEFAULT_TOOLTIP = "Shows all person";
	public static final IImageConstant DEFAULT_ICON = SilkIcons.USER;

	public UserTableView(final IViewContext context) {
		final IContainer container = context.getContainer();

		container.setLayout(MigLayoutFactory.growingInnerCellLayout());
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		final ITableModel tableModel = createTableModel();
		final ITable table = container.add(bpf.table(tableModel), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
		table.pack(TablePackPolicy.HEADER_AND_DATA_ALL);

	}

	private ISimpleTableModel createTableModel() {

		final ISimpleTableModel result = new SimpleTableModelBuilder().setEditableDefault(true).build();

		result.addColumn("Gender");
		result.addColumn("Firstname");
		result.addColumn("Lastname");
		result.addColumn("Street");
		result.addColumn("Postal code");
		result.addColumn("City");
		result.addColumn("Country");
		result.addColumn("Phone number");
		result.addColumn("Email");

		result.addRow(
				"Male",
				"Pete",
				"Brown",
				"Audubon Ave 34",
				"76453",
				"New York",
				"USA",
				"47634826",
				"hans.maier@gtzservice.com");

		result.addRow(
				"Male",
				"Steve",
				"Miller",
				"Convent Ave 25",
				"53453",
				"New York",
				"USA",
				"4354354",
				"mr.steve.miller@gjk.com");

		result.addRow(
				"Female",
				"Laura",
				"Brixton",
				"West End Ave 2",
				"53453",
				"New York",
				"USA",
				"435345345",
				"laura.brixton@gjk.com");

		return result;
	}
}
