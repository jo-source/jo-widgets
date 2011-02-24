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

import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.examples.common.icons.IconsInitializer;
import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.examples.common.workbench.base.AbstractWorkbench;
import org.jowidgets.tools.model.item.ActionItemModel;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.workbench.api.IWorkbenchContext;

public class WorkbenchDemo1 extends AbstractWorkbench {

	public WorkbenchDemo1() {
		IconsInitializer.initializeIcons();
	}

	@Override
	public void onContextInitialize(final IWorkbenchContext context) {
		context.add(new ApplicationDemo1());
		context.add(new ApplicationDemo2());
		createToolBar(context);
		createMenuBar(context);
	}

	@Override
	public String getLabel() {
		return "Hello Workbench";
	}

	@Override
	public IImageConstant getIcon() {
		return SilkIcons.EMOTICON_SMILE;
	}

	@Override
	public Dimension getInitialDimension() {
		return new Dimension(1024, 768);
	}

	@Override
	public boolean getApplicationsCloseable() {
		return true;
	}

	private void createToolBar(final IWorkbenchContext context) {
		final IToolBarModel toolBar = context.getToolBar();
		toolBar.addItem(ActionItemModel.builder().setIcon(SilkIcons.DISK).setToolTipText("Save"));
		toolBar.addSeparator();
		toolBar.addItem(ActionItemModel.builder().setIcon(SilkIcons.CUT).setToolTipText("Cut"));
		toolBar.addItem(ActionItemModel.builder().setIcon(SilkIcons.PAGE_COPY).setToolTipText("Copy"));
		toolBar.addItem(ActionItemModel.builder().setIcon(SilkIcons.PASTE_PLAIN).setToolTipText("Paste"));
		toolBar.addSeparator();
		toolBar.addItem(ActionItemModel.builder().setIcon(SilkIcons.PRINTER).setToolTipText("Print"));
	}

	private void createMenuBar(final IWorkbenchContext context) {
		final IMenuModel fileModel = new MenuModel("File");
		fileModel.setMnemonic('F');
		fileModel.addActionItem("New ");
		fileModel.addActionItem("Open file... ");
		fileModel.addSeparator();
		fileModel.addActionItem("Save", SilkIcons.DISK);
		fileModel.addActionItem("Save As...", SilkIcons.DISK);
		fileModel.addSeparator();
		final IActionItemModel exitAction = fileModel.addActionItem("Exit");
		exitAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				if (shouldWorkbenchFinished()) {
					context.finish();
				}
			}
		});

		final IMenuModel editModel = new MenuModel("Edit");
		editModel.setMnemonic('E');
		editModel.addActionItem("Cut", SilkIcons.CUT);
		editModel.addActionItem("Copy", SilkIcons.PAGE_COPY);
		editModel.addActionItem("Paste", SilkIcons.PASTE_PLAIN);

		final IMenuBarModel menuBarModel = context.getMenuBar();
		menuBarModel.addMenu(fileModel);
		menuBarModel.addMenu(editModel);
	}
}
