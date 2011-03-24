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
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.types.QuestionResult;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.examples.common.workbench.base.AbstractComponent;
import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.api.IComponentContext;
import org.jowidgets.workbench.api.IFolderLayout;
import org.jowidgets.workbench.api.ILayout;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;
import org.jowidgets.workbench.toolkit.api.IFolderLayoutBuilder;
import org.jowidgets.workbench.tools.FolderLayoutBuilder;
import org.jowidgets.workbench.tools.Layout;

public class ImportantComponentDemo1 extends AbstractComponent implements IComponent {

	public static final String DEFAULT_LAYOUT_ID = "DEFAULT_LAYOUT_ID";
	public static final String MASTER_FOLDER_ID = "MASTER_FOLDER_ID";

	public ImportantComponentDemo1(final IComponentContext context) {
		final ILayout defaultLayout = new Layout(DEFAULT_LAYOUT_ID, createMasterFolder());
		context.setLayout(defaultLayout);
		final IMenuModel popupMenu = context.getComponentNodeContext().getPopupMenu();
		popupMenu.addSeparator();
		popupMenu.addAction(new ActionFactory().createResetLayoutAction(context, defaultLayout));
	}

	@Override
	public IView createView(final String viewId, final IViewContext context) {
		if (ImportantViewDemo1.ID.equals(viewId)) {
			return new ImportantViewDemo1(context);
		}
		else {
			throw new IllegalArgumentException("View id '" + viewId + "' is not known.");
		}
	}

	@Override
	public void onDeactivation(final IVetoable vetoable) {
		final QuestionResult result = Toolkit.getQuestionPane().askYesNoQuestion("Would you really like to quit the component?");
		if (result != QuestionResult.YES) {
			vetoable.veto();
		}
	}

	private IFolderLayout createMasterFolder() {
		final IFolderLayoutBuilder folderLayoutBuilder = new FolderLayoutBuilder(MASTER_FOLDER_ID);

		folderLayoutBuilder.addView(
				ImportantViewDemo1.ID,
				ImportantViewDemo1.DEFAULT_LABEL,
				ImportantViewDemo1.DEFAULT_TOOLTIP,
				ImportantViewDemo1.DEFAULT_ICON);

		return folderLayoutBuilder.build();
	}
}
