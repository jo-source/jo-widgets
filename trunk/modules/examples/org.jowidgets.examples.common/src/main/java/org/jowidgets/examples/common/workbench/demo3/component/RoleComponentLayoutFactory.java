/*
 * Copyright (c) 2013, grossmann
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

package org.jowidgets.examples.common.workbench.demo3.component;

import org.jowidgets.workbench.api.ILayout;
import org.jowidgets.workbench.toolkit.api.IFolderLayoutBuilder;
import org.jowidgets.workbench.toolkit.api.ILayoutBuilder;
import org.jowidgets.workbench.toolkit.api.ISplitLayoutBuilder;
import org.jowidgets.workbench.tools.FolderLayoutBuilder;
import org.jowidgets.workbench.tools.LayoutBuilder;
import org.jowidgets.workbench.tools.SplitLayoutBuilder;

public final class RoleComponentLayoutFactory {

	private static final String DEFAULT_LAYOUT_ID = "DEFAULT_LAYOUT_ID";
	private static final String MASTER_FOLDER_ID = "MASTER_FOLDER_ID";
	private static final String DETAIL_FOLDER_ID = "DETAIL_FOLDER_ID";

	private RoleComponentLayoutFactory() {}

	public static ILayout create() {
		final ILayoutBuilder builder = new LayoutBuilder();
		builder.setId(DEFAULT_LAYOUT_ID).setLayoutContainer(createMasterDetailSplit());
		return builder.build();
	}

	private static ISplitLayoutBuilder createMasterDetailSplit() {
		final ISplitLayoutBuilder result = new SplitLayoutBuilder();
		result.setVertical().setWeight(0.5).setResizeSecond();
		result.setFirstContainer(createMasterFolder());
		result.setSecondContainer(createDetailFolder());
		return result;
	}

	private static IFolderLayoutBuilder createMasterFolder() {
		final IFolderLayoutBuilder result = new FolderLayoutBuilder(MASTER_FOLDER_ID);
		result.setViewsCloseable(false);
		result.addView(RoleTableView.ID, RoleTableView.DEFAULT_LABEL, RoleTableView.DEFAULT_TOOLTIP, RoleTableView.DEFAULT_ICON);
		return result;
	}

	private static IFolderLayoutBuilder createDetailFolder() {
		final IFolderLayoutBuilder result = new FolderLayoutBuilder(DETAIL_FOLDER_ID);
		result.setViewsCloseable(false);
		result.addView(
				RoleDetailView.ID,
				RoleDetailView.DEFAULT_LABEL,
				RoleDetailView.DEFAULT_TOOLTIP,
				RoleDetailView.DEFAULT_ICON);
		return result;
	}

}
