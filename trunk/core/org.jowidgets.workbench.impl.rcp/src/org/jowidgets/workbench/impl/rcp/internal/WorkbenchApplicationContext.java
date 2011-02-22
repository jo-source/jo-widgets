/*
 * Copyright (c) 2011, M. Grossmann, M. Woelker, H. Westphal
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of jo-widgets.org nor the
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

package org.jowidgets.workbench.impl.rcp.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.workbench.legacy.api.IComponentTreeNode;
import org.jowidgets.workbench.legacy.api.IUiPart;
import org.jowidgets.workbench.legacy.api.IWorkbenchApplication;
import org.jowidgets.workbench.legacy.api.IWorkbenchApplicationContext;
import org.jowidgets.workbench.legacy.api.IWorkbenchContext;

public final class WorkbenchApplicationContext implements IWorkbenchApplicationContext, IUiPart {

	private final IWorkbenchContext workbenchContext;
	private final IWorkbenchApplication application;
	private final WorkbenchApplicationTree tree;
	private final List<ComponentTreeNodeContext> childContexts = new ArrayList<ComponentTreeNodeContext>();
	private final Map<IComponentTreeNode, ComponentTreeNodeContext> nodeMap = new HashMap<IComponentTreeNode, ComponentTreeNodeContext>();

	public WorkbenchApplicationContext(
		final IWorkbenchContext workbenchContext,
		final IWorkbenchApplication application,
		final WorkbenchApplicationTree tree) {
		this.workbenchContext = workbenchContext;
		this.application = application;
		this.tree = tree;
		final List<IComponentTreeNode> treeNodes = application.createComponentTreeNodes();
		for (final IComponentTreeNode treeNode : treeNodes) {
			final ComponentTreeNodeContext treeNodeContext = new ComponentTreeNodeContext(this, null, treeNode, tree);
			childContexts.add(treeNodeContext);
			nodeMap.put(treeNode, treeNodeContext);
			treeNode.initialize(treeNodeContext);
		}
		tree.setInput(this);
	}

	@Override
	public void add(final IComponentTreeNode componentTreeNode) {
		add(childContexts.size(), componentTreeNode);
	}

	@Override
	public void add(final int index, final IComponentTreeNode componentTreeNode) {
		final ComponentTreeNodeContext treeNodeContext = new ComponentTreeNodeContext(this, null, componentTreeNode, tree);
		childContexts.add(index, treeNodeContext);
		nodeMap.put(componentTreeNode, treeNodeContext);
		componentTreeNode.initialize(treeNodeContext);
		tree.refresh(this);
	}

	@Override
	public void remove(final IComponentTreeNode componentTreeNode) {
		final ComponentTreeNodeContext treeNodeContext = nodeMap.get(componentTreeNode);
		if (treeNodeContext != null) {
			childContexts.remove(treeNodeContext);
			nodeMap.remove(componentTreeNode);
			tree.refresh(this);
		}
	}

	public ComponentTreeNodeContext[] getComponentTreeNodeContexts() {
		return childContexts.toArray(new ComponentTreeNodeContext[0]);
	}

	public String getId() {
		return application.getId();
	}

	@Override
	public IWorkbenchContext getWorkbenchContext() {
		return workbenchContext;
	}

	@Override
	public IMenu getMenu() {
		return tree.getJoMenu();
	}

	@Override
	public void setMenuTooltip(final String tooltip) {
		tree.setMenuTooltip(tooltip);
	}

	@Override
	public IToolBar getToolBar() {
		return tree.getToolBar();
	}

	@Override
	public String getLabel() {
		return application.getLabel();
	}

	@Override
	public String getTooltip() {
		return application.getTooltip();
	}

	@Override
	public IImageConstant getIcon() {
		return application.getIcon();
	}

}
