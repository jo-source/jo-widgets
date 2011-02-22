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

import java.util.Collections;
import java.util.List;

import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.types.QuestionResult;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.tools.model.item.ActionItemModel;
import org.jowidgets.tools.model.item.MenuBarModel;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.model.item.ToolBarModel;
import org.jowidgets.workbench.api.IWorkbench;
import org.jowidgets.workbench.api.IWorkbenchApplication;
import org.jowidgets.workbench.api.IWorkbenchContext;

public class WorkbenchDemo1 implements IWorkbench {

	private IWorkbenchContext context;

	@Override
	public String getLabel() {
		return "Hello Workbench";
	}

	@Override
	public String getTooltip() {
		return null;
	}

	@Override
	public IImageConstant getIcon() {
		return null;
	}

	@Override
	public void initialize(final IWorkbenchContext context) {
		this.context = context;
	}

	@Override
	public void onWindowClose(final IVetoable vetoable) {
		if (!askFinishWorkbench()) {
			vetoable.veto();
		}
	}

	@Override
	public List<? extends IWorkbenchApplication> createWorkbenchApplications() {
		return Collections.singletonList(new ApplicationDemo1());
	}

	@Override
	public IToolBarModel createToolBar() {
		final IToolBarModel result = new ToolBarModel();
		result.addItem(ActionItemModel.builder().setIcon(IconsSmall.INFO).setToolTipText("info action tooltip"));
		result.addItem(ActionItemModel.builder().setIcon(IconsSmall.QUESTION).setToolTipText("question action tooltip"));
		return result;
	}

	@Override
	public IMenuBarModel createMenuBar() {
		final IMenuModel fileModel = new MenuModel("File");
		fileModel.setMnemonic('F');
		fileModel.addActionItem("New ");
		fileModel.addActionItem("Open file... ");
		fileModel.addSeparator();
		fileModel.addActionItem("Save");
		fileModel.addActionItem("Save As...");
		fileModel.addSeparator();
		final IActionItemModel exitAction = fileModel.addActionItem("Exit");
		exitAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				if (askFinishWorkbench()) {
					context.finish();
				}
			}
		});

		final IMenuModel editModel = new MenuModel("Edit");
		editModel.setMnemonic('E');
		editModel.addActionItem("Cut");
		editModel.addActionItem("Copy");
		editModel.addActionItem("Paste");

		final IMenuBarModel menuBarModel = new MenuBarModel();
		menuBarModel.addMenu(fileModel);
		menuBarModel.addMenu(editModel);

		return menuBarModel;
	}

	@Override
	public Dimension getInitialDimension() {
		return new Dimension(800, 600);
	}

	@Override
	public Position getInitialPosition() {
		return null;
	}

	@Override
	public boolean getApplicationsCloseable() {
		return false;
	}

	@Override
	public boolean hasStatusBar() {
		return true;
	}

	@Override
	public boolean hasTrayItem() {
		return false;
	}

	private boolean askFinishWorkbench() {
		final QuestionResult result = Toolkit.getQuestionPane().askYesNoQuestion("Would you really like to quit the workbench?");
		if (result != QuestionResult.YES) {
			return false;
		}
		return true;
	}

}
