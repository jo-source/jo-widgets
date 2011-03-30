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

package org.jowidgets.examples.common.workbench.demo2.workbench;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.ICommandAction;
import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.types.QuestionResult;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.examples.common.icons.DemoIconsInitializer;
import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.examples.common.workbench.demo2.application.Application1;
import org.jowidgets.examples.common.workbench.demo2.application.Application2;
import org.jowidgets.examples.common.workbench.demo2.workbench.command.ExitAction;
import org.jowidgets.examples.common.workbench.demo2.workbench.command.WorkbenchActions;
import org.jowidgets.tools.model.item.MenuBarModel;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.model.item.ToolBarModel;
import org.jowidgets.workbench.api.ICloseCallback;
import org.jowidgets.workbench.api.IWorkbench;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModel;
import org.jowidgets.workbench.toolkit.api.WorkbenchToolkit;
import org.jowidgets.workbench.tools.CloseCallbackAdapter;
import org.jowidgets.workbench.tools.WorkbenchModelBuilder;

public class WorkbenchDemo2 {

	private final IWorkbenchModel model;
	private final IAction exitAction;

	public WorkbenchDemo2() {
		DemoIconsInitializer.initialize();

		final WorkbenchModelBuilder builder = new WorkbenchModelBuilder();
		builder.setLabel("Workbench Demo2");
		builder.setIcon(SilkIcons.EMOTICON_SMILE);
		builder.setInitialDimension(new Dimension(1280, 900));
		builder.setInitialSplitWeight(0.18);
		builder.setCloseCallback(createCloseCallback());

		exitAction = createExitAction();

		model = builder.build();
		model.setToolBar(createToolBar());
		model.setMenuBar(createMenuBar());

		model.addApplication(new Application1().getModel());
		model.addApplication(new Application2().getModel());
	}

	public IWorkbenchModel getModel() {
		return model;
	}

	public IWorkbench createWorkbench() {
		return WorkbenchToolkit.getWorkbenchPartFactory().workbench(model);
	}

	private IToolBarModel createToolBar() {
		final IToolBarModel toolBar = new ToolBarModel();
		toolBar.addAction(WorkbenchActions.SAVE_ACTION);
		toolBar.addSeparator();
		toolBar.addAction(WorkbenchActions.CUT_ACTION);
		toolBar.addAction(WorkbenchActions.COPY_ACTION);
		toolBar.addAction(WorkbenchActions.PASTE_ACTION);
		toolBar.addSeparator();
		toolBar.addAction(WorkbenchActions.PRINT_ACTION);
		return toolBar;
	}

	private IMenuBarModel createMenuBar() {
		final IMenuBarModel menuBarModel = new MenuBarModel();

		final IMenuModel fileMenu = new MenuModel("File");
		fileMenu.addAction(WorkbenchActions.NEW_DOCUMENT_ACTION);
		fileMenu.addAction(WorkbenchActions.SAVE_ACTION);
		fileMenu.addAction(WorkbenchActions.SAVE_AS_ACTION);
		fileMenu.addAction(exitAction);

		final IMenuModel editMenu = new MenuModel("Edit");
		editMenu.setMnemonic('E');
		editMenu.addAction(WorkbenchActions.CUT_ACTION);
		editMenu.addAction(WorkbenchActions.COPY_ACTION);
		editMenu.addAction(WorkbenchActions.PASTE_ACTION);

		menuBarModel.addMenu(fileMenu);
		menuBarModel.addMenu(editMenu);

		return menuBarModel;
	}

	private ICloseCallback createCloseCallback() {
		return new CloseCallbackAdapter() {
			@Override
			public void onClose(final IVetoable vetoable) {
				if (!shouldWorkbenchFinished()) {
					vetoable.veto();
				}
			}
		};
	}

	private IAction createExitAction() {
		final ICommandAction result = new ExitAction();
		result.setCommand(new ICommandExecutor() {
			@Override
			public void execute(final IExecutionContext executionContext) throws Exception {
				if (shouldWorkbenchFinished()) {
					model.finish();
				}
			}
		});
		return result;
	}

	private boolean shouldWorkbenchFinished() {
		final QuestionResult result = Toolkit.getQuestionPane().askYesNoQuestion("Would you really like to quit the workbench?");
		if (result != QuestionResult.YES) {
			return false;
		}
		return true;
	}

}
