/*
 * Copyright (c) 2011, M. Woelker, H. Westphal
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.api.IComponentNode;
import org.jowidgets.workbench.api.IComponentNodeContext;
import org.jowidgets.workbench.api.IWorkbenchApplicationContext;
import org.jowidgets.workbench.api.IWorkbenchContext;
import org.jowidgets.workbench.api.IWorkbenchPart;

public final class ComponentNodeContext implements IComponentNodeContext, IWorkbenchPart {

	private final WorkbenchApplicationContext applicationContext;
	private final ComponentNodeContext parentContext;
	private final IComponentNode treeNode;
	private final WorkbenchApplicationTree tree;
	private final IComposite joTree;
	private IPopupMenu popupMenu;
	private IMenuModel popupMenuModel;
	private final List<ComponentNodeContext> childContexts = new ArrayList<ComponentNodeContext>();
	private final Map<IComponentNode, ComponentNodeContext> nodeMap = new HashMap<IComponentNode, ComponentNodeContext>();
	private AtomicReference<ComponentContext> componentContextReference;
	private String label;
	private String tooltip;
	private IImageConstant icon;

	public ComponentNodeContext(
		final WorkbenchApplicationContext applicationContext,
		final ComponentNodeContext parentContext,
		final IComponentNode treeNode,
		final WorkbenchApplicationTree tree) {
		this.applicationContext = applicationContext;
		this.parentContext = parentContext;
		this.treeNode = treeNode;
		this.tree = tree;
		joTree = Toolkit.getWidgetWrapperFactory().createComposite(tree);
		label = treeNode.getLabel();
		tooltip = treeNode.getTooltip();
		icon = treeNode.getIcon();
	}

	@Override
	public void add(final IComponentNode componentTreeNode) {
		add(childContexts.size(), componentTreeNode);
	}

	@Override
	public void add(final int index, final IComponentNode componentTreeNode) {
		final ComponentNodeContext treeNodeContext = new ComponentNodeContext(applicationContext, this, componentTreeNode, tree);
		childContexts.add(index, treeNodeContext);
		nodeMap.put(componentTreeNode, treeNodeContext);
		componentTreeNode.onContextInitialize(treeNodeContext);
		tree.refresh(this);
	}

	@Override
	public void remove(final IComponentNode componentTreeNode) {
		final ComponentNodeContext treeNodeContext = nodeMap.get(componentTreeNode);
		if (treeNodeContext != null) {
			childContexts.remove(treeNodeContext);
			nodeMap.remove(componentTreeNode);
			tree.refresh(this);
		}
	}

	public ComponentNodeContext[] getComponentTreeNodeContexts() {
		return childContexts.toArray(new ComponentNodeContext[0]);
	}

	public ComponentContext getComponentContext() {
		if (componentContextReference == null) {
			final ComponentContext componentContext = new ComponentContext(this);
			final IComponent component = treeNode.createComponent(componentContext);
			if (component != null) {
				componentContextReference = new AtomicReference<ComponentContext>(componentContext);
				componentContext.setComponent(component);
			}
			else {
				componentContextReference = new AtomicReference<ComponentContext>(null);
			}
		}
		return componentContextReference.get();
	}

	public String getId() {
		return treeNode.getId();
	}

	@Override
	public IComponentNodeContext getParent() {
		return parentContext;
	}

	@Override
	public IWorkbenchApplicationContext getWorkbenchApplicationContext() {
		return applicationContext;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getTooltip() {
		return tooltip;
	}

	@Override
	public IImageConstant getIcon() {
		return icon;
	}

	@Override
	public void setSelected(final boolean selected) {
		if (selected) {
			final List<String> result = new LinkedList<String>();
			ComponentNodeContext context = this;
			while (context != null) {
				result.add(0, context.getId());
				context = (ComponentNodeContext) context.getParent();
			}
			result.add(0, applicationContext.getId());
			((WorkbenchContext) getWorkbenchContext()).selectTreeNode(result.toArray(new String[0]));
		}
		//		else {
		//TODO HW changed from select() to setSelected()
		//		}
	}

	@Override
	public void setExpanded(final boolean expanded) {
		if (expanded) {
			tree.expand(this);
		}
		else {
			tree.collapse(this);
		}
	}

	@Override
	public void setLabel(final String label) {
		this.label = label;
		tree.refresh(this);
	}

	@Override
	public void setTooltip(final String tooltip) {
		this.tooltip = tooltip;
		tree.refresh(this);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		this.icon = icon;
		tree.refresh(this);
	}

	@Override
	public IMenuModel getPopupMenu() {
		if (popupMenuModel == null) {
			popupMenuModel = Toolkit.getModelFactoryProvider().getItemModelFactory().menu();
			popupMenu = joTree.createPopupMenu();
			popupMenu.setModel(popupMenuModel);
		}
		return popupMenuModel;
	}

	public IPopupMenu getJoPopupMenu() {
		return popupMenu;
	}

	@Override
	public IWorkbenchContext getWorkbenchContext() {
		return applicationContext.getWorkbenchContext();
	}

}
