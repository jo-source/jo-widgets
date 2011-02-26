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

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.common.types.Orientation;
import org.jowidgets.common.types.SplitResizePolicy;
import org.jowidgets.examples.common.demo.DemoMenuProvider;
import org.jowidgets.examples.common.workbench.base.AbstractComponent;
import org.jowidgets.examples.common.workbench.base.FolderLayout;
import org.jowidgets.examples.common.workbench.base.Layout;
import org.jowidgets.examples.common.workbench.base.SplitLayout;
import org.jowidgets.examples.common.workbench.base.ViewLayout;
import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.api.IComponentContext;
import org.jowidgets.workbench.api.IFolderContext;
import org.jowidgets.workbench.api.IFolderLayout;
import org.jowidgets.workbench.api.ISplitLayout;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;
import org.jowidgets.workbench.api.IViewLayout;

public class ComponentDemo1 extends AbstractComponent implements IComponent {

	public static final String DEFAULT_LAYOUT_ID = "DEFAULT_LAYOUT_ID";
	public static final String MASTER_FOLDER_ID = "MASTER_FOLDER_ID";
	public static final String DETAIL1_FOLDER_ID = "DETAIL1_FOLDER_ID";
	public static final String DETAIL2_FOLDER_ID = "DETAIL2_FOLDER_ID";

	private final DemoMenuProvider menuProvider;

	public ComponentDemo1(final IComponentContext componentContext) {
		componentContext.setLayout(new Layout(DEFAULT_LAYOUT_ID, createMasterSplit()));
		menuProvider = new DemoMenuProvider(true);
	}

	@Override
	public IView createView(final String viewId, final IViewContext context) {
		if (ViewDemo1.ID.equals(viewId)) {
			return new ViewDemo1(context, menuProvider);
		}
		else if (ViewDemo2.ID.equals(viewId)) {
			return new ViewDemo2(context, menuProvider);
		}
		else if (ViewDemo3.ID.equals(viewId)) {
			return new ViewDemo3(context);
		}
		else if (ViewDemo4.ID.equals(viewId)) {
			return new ViewDemo4(context);
		}
		else if (ViewDemo5.ID.equals(viewId)) {
			return new ViewDemo5(context);
		}
		else if (ViewDemo6.ID.equals(viewId)) {
			return new ViewDemo6(context);
		}
		else {
			throw new IllegalArgumentException("View id '" + viewId + "' is not known.");
		}
	}

	private ISplitLayout createMasterSplit() {
		return new SplitLayout(
			Orientation.VERTICAL,
			0.45,
			SplitResizePolicy.RESIZE_SECOND,
			createMasterFolder(),
			createDetailSplit());
	}

	private IFolderLayout createMasterFolder() {
		final List<IViewLayout> resultViews = new LinkedList<IViewLayout>();
		resultViews.add(new ViewLayout(ViewDemo1.ID, ViewDemo1.DEFAULT_LABEL, ViewDemo1.DEFAULT_TOOLTIP, ViewDemo1.DEFAULT_ICON));
		return new FolderLayout(MASTER_FOLDER_ID, resultViews);
	}

	private ISplitLayout createDetailSplit() {
		return new SplitLayout(
			Orientation.HORIZONTAL,
			0.3,
			SplitResizePolicy.RESIZE_BOTH,
			createDetail1Folder(),
			createDetail2Folder());
	}

	private IFolderLayout createDetail1Folder() {
		final List<IViewLayout> resultViews = new LinkedList<IViewLayout>();
		resultViews.add(new ViewLayout(ViewDemo2.ID, ViewDemo2.DEFAULT_LABEL, ViewDemo2.DEFAULT_TOOLTIP, ViewDemo2.DEFAULT_ICON));
		resultViews.add(new ViewLayout(ViewDemo3.ID, ViewDemo3.DEFAULT_LABEL, ViewDemo3.DEFAULT_TOOLTIP, ViewDemo3.DEFAULT_ICON));
		return new FolderLayout(DETAIL1_FOLDER_ID, resultViews);
	}

	private IFolderLayout createDetail2Folder() {
		final List<IViewLayout> resultViews = new LinkedList<IViewLayout>();
		resultViews.add(new ViewLayout(ViewDemo4.ID, ViewDemo4.DEFAULT_LABEL, ViewDemo4.DEFAULT_TOOLTIP, ViewDemo4.DEFAULT_ICON));
		resultViews.add(new ViewLayout(ViewDemo5.ID, ViewDemo5.DEFAULT_LABEL, ViewDemo5.DEFAULT_TOOLTIP, ViewDemo5.DEFAULT_ICON));
		resultViews.add(new ViewLayout(ViewDemo6.ID, ViewDemo6.DEFAULT_LABEL, ViewDemo6.DEFAULT_TOOLTIP, ViewDemo6.DEFAULT_ICON));
		return new FolderLayout(DETAIL2_FOLDER_ID, resultViews);
	}

	@Override
	public void onFolderCreated(final IFolderContext folderContext) {
		if (DETAIL2_FOLDER_ID.equals(folderContext.getOriginalFolderId())) {
			final ActionFactory actionFactory = new ActionFactory();
			folderContext.getPopupMenu().addAction(actionFactory.createAddViewAction(folderContext));
		}
	}
}
