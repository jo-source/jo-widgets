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

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.ICommand;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.examples.common.workbench.base.AbstractDemoComponent;
import org.jowidgets.examples.common.workbench.demo2.view.BigTableView;
import org.jowidgets.examples.common.workbench.demo2.view.EmptyView;
import org.jowidgets.examples.common.workbench.demo2.view.MediaView;
import org.jowidgets.examples.common.workbench.demo2.view.ReportsView;
import org.jowidgets.examples.common.workbench.demo2.view.UserTableView;
import org.jowidgets.examples.common.workbench.demo2.workbench.command.WorkbenchActions;
import org.jowidgets.tools.command.ActionBuilder;
import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.api.IComponentContext;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;
import org.jowidgets.workbench.api.LayoutScope;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;

public class ComponentDemo2 extends AbstractDemoComponent implements IComponent {

	private final IComponentNodeModel componentNodeModel;

	private final ICommandExecutor saveCommand;
	private final IAction addUserAction;

	public ComponentDemo2(final IComponentNodeModel componentNodeModel, final IComponentContext componentContext) {
		this.componentNodeModel = componentNodeModel;

		this.addUserAction = createAddUserAction(componentNodeModel);
		this.saveCommand = createSaveCommand(componentNodeModel);
		componentContext.setLayout(new ComponentDemo2Layout(LayoutScope.COMPONENT).getLayout());
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
		componentNodeModel.getWorkbench().getToolBar().addAction(addUserAction);
	}

	@Override
	public void onDeactivation(final IVetoable vetoable) {
		WorkbenchActions.SAVE_ACTION.setCommand((ICommand) null);
		componentNodeModel.getWorkbench().getToolBar().removeAction(addUserAction);
	}

	private ICommandExecutor createSaveCommand(final IComponentNodeModel nodeModel) {
		return new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				Toolkit.getMessagePane().showInfo(executionContext, "Saved data of component: " + nodeModel.getLabel());
			}
		};
	}

	private IAction createAddUserAction(final IComponentNodeModel nodeModel) {
		final IActionBuilder builder = new ActionBuilder();
		builder.setText("Add user");
		builder.setIcon(SilkIcons.USER_ADD);

		builder.setCommand(new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				Toolkit.getMessagePane().showInfo(executionContext, "Add user action of component: " + nodeModel.getLabel());
			}
		});
		return builder.build();
	}
}
