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

package org.jowidgets.examples.common.workbench.demo2.component;

import org.jowidgets.api.command.ICommand;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.examples.common.workbench.base.AbstractComponent;
import org.jowidgets.examples.common.workbench.demo2.view.BigTableView;
import org.jowidgets.examples.common.workbench.demo2.view.EmptyView;
import org.jowidgets.examples.common.workbench.demo2.view.MediaView;
import org.jowidgets.examples.common.workbench.demo2.view.UserTableView;
import org.jowidgets.examples.common.workbench.demo2.view.ReportsView;
import org.jowidgets.examples.common.workbench.demo2.workbench.command.WorkbenchActions;
import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.api.IComponentContext;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;

public class ComponentDemo2 extends AbstractComponent implements IComponent {

	public static final String DEFAULT_LAYOUT_ID = "DEFAULT_LAYOUT_ID";
	public static final String MASTER_FOLDER_ID = "MASTER_FOLDER_ID";
	public static final String MEDIA_FOLDER_ID = "MEDIA_FOLDER_ID";
	public static final String DETAIL_FOLDER_ID = "DETAIL_FOLDER_ID";
	public static final String REPORTS_FOLDER_ID = "REPORTS_FOLDER_ID";

	private final ICommandExecutor saveCommand;

	public ComponentDemo2(final IComponentNodeModel nodeModel, final IComponentContext componentContext) {

		this.saveCommand = createSaveCommand(nodeModel);

		componentContext.setLayout(new ComponentDemo2Layout().getLayout());
	}

	@Override
	public IView createView(final String viewId, final IViewContext context) {
		if (BigTableView.ID.equals(viewId)) {
			return new BigTableView(context);
		}
		else if (MediaView.ID.equals(viewId)) {
			return new MediaView(context);
		}
		else if (UserTableView.ID.equals(viewId)) {
			return new UserTableView(context);
		}
		else if (EmptyView.ID.equals(viewId)) {
			return new EmptyView(context);
		}
		else if (ReportsView.ID.equals(viewId)) {
			return new ReportsView(context);
		}
		else {
			throw new IllegalArgumentException("View id '" + viewId + "' is not known.");
		}
	}

	@Override
	public void onActivation() {
		super.onActivation();
		WorkbenchActions.SAVE_ACTION.setCommand(saveCommand);
	}

	@Override
	public void onDeactivation(final IVetoable vetoable) {
		WorkbenchActions.SAVE_ACTION.setCommand((ICommand) null);
	}

	private ICommandExecutor createSaveCommand(final IComponentNodeModel nodeModel) {
		return new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				Toolkit.getMessagePane().showInfo(executionContext, "Saved data of component: " + nodeModel.getLabel());
			}
		};
	}
}
