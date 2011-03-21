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

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.table.ISimpleTableModel;
import org.jowidgets.examples.common.demo.DemoMenuProvider;
import org.jowidgets.examples.common.workbench.base.AbstractComponent;
import org.jowidgets.tools.model.table.SimpleTableModelBuilder;
import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.api.IComponentContext;
import org.jowidgets.workbench.api.IFolderContext;
import org.jowidgets.workbench.api.ILayout;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;
import org.jowidgets.workbench.toolkit.api.IFolderLayoutBuilder;
import org.jowidgets.workbench.toolkit.api.ISplitLayoutBuilder;
import org.jowidgets.workbench.tools.FolderLayoutBuilder;
import org.jowidgets.workbench.tools.Layout;
import org.jowidgets.workbench.tools.SplitLayoutBuilder;

public class ComponentDemo1 extends AbstractComponent implements IComponent {

	public static final String DEFAULT_LAYOUT_ID = "DEFAULT_LAYOUT_ID";
	public static final String MASTER_FOLDER_ID = "MASTER_FOLDER_ID";
	public static final String DETAIL1_FOLDER_ID = "DETAIL1_FOLDER_ID";
	public static final String DETAIL2_FOLDER_ID = "DETAIL2_FOLDER_ID";
	public static final String MAIL_FOLDER_ID = "DETAIL3_FOLDER_ID";

	private final DemoMenuProvider menuProvider;
	private ISimpleTableModel tableModel;

	public ComponentDemo1(final IComponentContext componentContext) {
		this.menuProvider = new DemoMenuProvider(true);

		final ILayout defaultLayout = new Layout(DEFAULT_LAYOUT_ID, createMainSplit());
		componentContext.setLayout(defaultLayout);

		final IMenuModel popupMenu = componentContext.getComponentTreeNodeContext().getPopupMenu();
		popupMenu.addSeparator();
		popupMenu.addAction(new ActionFactory().createResetLayoutAction(componentContext, defaultLayout));
	}

	@Override
	public IView createView(final String viewId, final IViewContext context) {
		if (ViewDemo6.ID.equals(viewId)) {
			return new ViewDemo1(context, menuProvider, getTableModelLazy());
		}
		else if (ViewDemo2.ID.equals(viewId)) {
			return new ViewDemo2(context, menuProvider);
		}
		else if (ViewDemo3.ID.equals(viewId)) {
			return new ViewDemo3(context);
		}
		else if (ViewDemo4.ID.equals(viewId)) {
			return new ViewDemo4(context, getTableModelLazy());
		}
		else if (ViewDemo5.ID.equals(viewId)) {
			return new ViewDemo5(context);
		}
		else if (ViewDemo1.ID.equals(viewId)) {
			return new ViewDemo6(context, menuProvider);
		}
		else if (ViewDemo7.ID.equals(viewId)) {
			return new ViewDemo7(context);
		}
		else if (viewId.startsWith(DynamicViewDemo.ID_PREFIX)) {
			return new DynamicViewDemo(viewId, context);
		}
		else {
			throw new IllegalArgumentException("View id '" + viewId + "' is not known.");
		}
	}

	@Override
	public void onFolderCreated(final IFolderContext folderContext) {
		if (DETAIL2_FOLDER_ID.equals(folderContext.getOriginalFolderId())) {
			final ActionFactory actionFactory = new ActionFactory();
			folderContext.getPopupMenu().addAction(actionFactory.createAddViewAction(folderContext));
		}
	}

	private ISplitLayoutBuilder createMainSplit() {
		final ISplitLayoutBuilder result = new SplitLayoutBuilder();
		result.setHorizontal().setWeight(0.78).setResizeFirst();
		result.setFirstContainer(createMasterDetailSplit());
		result.setSecondContainer(createMailFolder());
		return result;
	}

	private ISplitLayoutBuilder createMasterDetailSplit() {
		final ISplitLayoutBuilder result = new SplitLayoutBuilder();
		result.setVertical().setWeight(0.55).setResizeFirst();
		result.setFirstContainer(createMasterFolder());
		result.setSecondContainer(createDetailSplit());
		return result;
	}

	private IFolderLayoutBuilder createMasterFolder() {
		final IFolderLayoutBuilder result = new FolderLayoutBuilder(MASTER_FOLDER_ID);
		result.addView(ViewDemo1.ID, ViewDemo1.DEFAULT_LABEL, ViewDemo1.DEFAULT_TOOLTIP, ViewDemo1.DEFAULT_ICON);
		return result;
	}

	private ISplitLayoutBuilder createDetailSplit() {
		final ISplitLayoutBuilder result = new SplitLayoutBuilder();
		result.setHorizontal().setWeight(0.28).setResizeSecond();
		result.setFirstContainer(createDetail1Folder());
		result.setSecondContainer(createDetail2Folder());
		return result;
	}

	private IFolderLayoutBuilder createDetail1Folder() {
		final IFolderLayoutBuilder result = new FolderLayoutBuilder(DETAIL1_FOLDER_ID);
		result.addView(ViewDemo2.ID, ViewDemo2.DEFAULT_LABEL, ViewDemo2.DEFAULT_TOOLTIP, ViewDemo2.DEFAULT_ICON);
		result.addView(ViewDemo3.ID, ViewDemo3.DEFAULT_LABEL, ViewDemo3.DEFAULT_TOOLTIP, ViewDemo3.DEFAULT_ICON);
		return result;
	}

	private IFolderLayoutBuilder createDetail2Folder() {
		final IFolderLayoutBuilder result = new FolderLayoutBuilder(DETAIL2_FOLDER_ID);
		result.addView(ViewDemo4.ID, ViewDemo4.DEFAULT_LABEL, ViewDemo4.DEFAULT_TOOLTIP, ViewDemo4.DEFAULT_ICON);
		result.addView(ViewDemo5.ID, ViewDemo5.DEFAULT_LABEL, ViewDemo5.DEFAULT_TOOLTIP, ViewDemo5.DEFAULT_ICON);
		result.addView(ViewDemo6.ID, ViewDemo6.DEFAULT_LABEL, ViewDemo6.DEFAULT_TOOLTIP, ViewDemo6.DEFAULT_ICON);
		return result;
	}

	private IFolderLayoutBuilder createMailFolder() {
		final IFolderLayoutBuilder result = new FolderLayoutBuilder(MAIL_FOLDER_ID);
		result.addView(ViewDemo7.ID, ViewDemo7.DEFAULT_LABEL, ViewDemo7.DEFAULT_TOOLTIP, ViewDemo7.DEFAULT_ICON);
		return result;
	}

	private ISimpleTableModel getTableModelLazy() {
		if (tableModel == null) {
			tableModel = createTableModel();
		}
		return tableModel;
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

		result.addRow("Male", "Steve", "Miller", "Convent Ave 25", "53453", "New York", "USA", "4354354", "steve.miller@gjk.com");

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
