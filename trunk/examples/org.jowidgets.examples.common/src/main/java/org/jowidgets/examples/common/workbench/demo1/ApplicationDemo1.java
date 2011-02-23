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

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.types.QuestionResult;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.model.item.ToolBarModel;
import org.jowidgets.workbench.api.IComponentTreeNode;
import org.jowidgets.workbench.api.IWorkbenchApplication;
import org.jowidgets.workbench.api.IWorkbenchApplicationContext;

public class ApplicationDemo1 implements IWorkbenchApplication {

	private static final String ID = ApplicationDemo1.class.getName();

	private final IMenuModel popupMenu;
	private final IToolBarModel toolBar;
	private final IMenuModel toolBarMenu;

	public ApplicationDemo1() {
		this.toolBar = new ToolBarModel();
		this.popupMenu = new MenuModel();
		this.toolBarMenu = new MenuModel();
	}

	@Override
	public void onContextInitialize(final IWorkbenchApplicationContext context) {
		final ActionFactory actionFactory = new ActionFactory();
		final IAction addFolderAction = actionFactory.createAddFolderAction(context);

		popupMenu.addAction(addFolderAction);
		toolBarMenu.addAction(addFolderAction);
		toolBar.addAction(addFolderAction);
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public String getLabel() {
		return "Application Demo 1";
	}

	@Override
	public String getTooltip() {
		return "Application Demo 1 tooltip";
	}

	@Override
	public IImageConstant getIcon() {
		return SilkIcons.USER_GREEN;
	}

	@Override
	public void onActiveStateChanged(final boolean active) {
		// CHECKSTYLE:OFF
		System.out.println("activated= " + active + ", " + ID);
		// CHECKSTYLE:ON
	}

	@Override
	public void onVisibleStateChanged(final boolean visible) {
		// CHECKSTYLE:OFF
		System.out.println("visibility= " + visible + ", " + ID);
		// CHECKSTYLE:ON
	}

	@Override
	public void onClose(final IVetoable vetoable) {
		final QuestionResult result = Toolkit.getQuestionPane().askYesNoQuestion("Would you really like to quit the application?");
		if (result != QuestionResult.YES) {
			vetoable.veto();
		}
	}

	@Override
	public List<IComponentTreeNode> createComponentTreeNodes() {
		final List<IComponentTreeNode> result = new LinkedList<IComponentTreeNode>();

		final List<IComponentTreeNode> componentList1 = new LinkedList<IComponentTreeNode>();
		componentList1.add(new ComponentTreeNodeDemo1("COMPONENT1", "Component1"));
		componentList1.add(new ComponentTreeNodeDemo1("COMPONENT2", "Component2"));
		componentList1.add(new ComponentTreeNodeDemo1("COMPONENT3", "Component3"));
		final FolderTreeNodeDemo folderNode1 = new FolderTreeNodeDemo("FOLDER1", "Folder1", componentList1);
		result.add(folderNode1);

		final List<IComponentTreeNode> componentList2 = new LinkedList<IComponentTreeNode>();
		componentList2.add(new ComponentTreeNodeDemo1("COMPONENT1", "Component1"));
		componentList2.add(new ComponentTreeNodeDemo1("COMPONENT2", "Component2"));
		componentList2.add(new ComponentTreeNodeDemo1("COMPONENT3", "Component3"));
		final FolderTreeNodeDemo folderNode2 = new FolderTreeNodeDemo("FOLDER2", "Folder2", componentList2);
		result.add(folderNode2);

		return result;
	}

	@Override
	public IMenuModel createPopupMenu() {
		return popupMenu;
	}

	@Override
	public IToolBarModel createToolBar() {
		return toolBar;
	}

	@Override
	public IMenuModel createToolBarMenu() {
		return toolBarMenu;
	}

}
