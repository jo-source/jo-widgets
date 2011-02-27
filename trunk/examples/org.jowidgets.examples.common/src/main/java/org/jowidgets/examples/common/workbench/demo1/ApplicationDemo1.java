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
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.examples.common.workbench.base.AbstractApplication;
import org.jowidgets.examples.common.workbench.widgets.WidgetsDemoTreeNode;
import org.jowidgets.workbench.api.IComponentTreeNode;
import org.jowidgets.workbench.api.IWorkbenchApplicationContext;

public class ApplicationDemo1 extends AbstractApplication {

	private static final String ID = ApplicationDemo1.class.getName();

	public ApplicationDemo1() {
		super(ID);
	}

	@Override
	public void onContextInitialize(final IWorkbenchApplicationContext context) {
		final ActionFactory actionFactory = new ActionFactory();
		final IAction addFolderAction = actionFactory.createAddFolderAction(context);

		//create menus
		context.getPopupMenu().addAction(addFolderAction);
		context.getToolBarMenu().addAction(addFolderAction);
		context.getToolBar().addAction(addFolderAction);

		//create tree
		//create first folder
		final List<IComponentTreeNode> componentList1 = new LinkedList<IComponentTreeNode>();
		componentList1.add(new ComponentTreeNodeDemo1("COMPONENT1", "Component1"));
		componentList1.add(new ComponentTreeNodeDemo1("COMPONENT2", "Component2"));
		componentList1.add(new ComponentTreeNodeDemo1("COMPONENT3", "Component3"));
		context.add(new FolderTreeNodeDemo("COMPONENTS", "Components", componentList1));
		//create second node
		context.add(new WidgetsDemoTreeNode());
		//create third node
		final List<IComponentTreeNode> componentList3 = new LinkedList<IComponentTreeNode>();
		componentList3.add(new ImportantComponentTreeNodeDemo1("IMPORTANT1", "Important"));
		final FolderTreeNodeDemo folderNode2 = new FolderTreeNodeDemo("MISC", "Misc", componentList3);
		context.add(folderNode2);
	}

	@Override
	public String getLabel() {
		return "App 1";
	}

	@Override
	public String getTooltip() {
		return "Application Demo 1";
	}

	@Override
	public IImageConstant getIcon() {
		return SilkIcons.USER_GREEN;
	}

}
