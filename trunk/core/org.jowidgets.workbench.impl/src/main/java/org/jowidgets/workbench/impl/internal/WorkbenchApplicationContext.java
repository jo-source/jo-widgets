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

package org.jowidgets.workbench.impl.internal;

import java.util.HashMap;
import java.util.Map;

import org.jowidgets.api.controler.ITabItemListener;
import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.IToolBar;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.blueprint.ITreeBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.model.item.ToolBarModel;
import org.jowidgets.tools.types.VetoHolder;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IComponentTreeNode;
import org.jowidgets.workbench.api.IWorkbenchApplication;
import org.jowidgets.workbench.api.IWorkbenchApplicationContext;
import org.jowidgets.workbench.api.IWorkbenchContext;

public class WorkbenchApplicationContext implements IWorkbenchApplicationContext {

	private final WorkbenchContext workbenchContext;
	private final ITree tree;
	private final IContainer contentContainer;
	private final IListModelListener listModelListener;
	private final IMenuModel popupMenuModel;
	private final IToolBarModel toolBarModel;
	private final IMenuModel toolBarMenuModel;
	private final Map<IComponentTreeNode, ITreeNode> createdNodes;

	public WorkbenchApplicationContext(
		final WorkbenchContext workbenchContext,
		final ITabItem tabItem,
		final IContainer contentContainer,
		final IWorkbenchApplication application) {

		this.workbenchContext = workbenchContext;
		this.contentContainer = contentContainer;
		this.createdNodes = new HashMap<IComponentTreeNode, ITreeNode>();

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		tabItem.setText(application.getLabel());
		tabItem.setToolTipText(application.getTooltip());
		tabItem.setIcon(application.getIcon());

		tabItem.addTabItemListener(new ITabItemListener() {

			@Override
			public void selectionChanged(final boolean selected) {
				//TODO MG set the selection
			}

			@Override
			public void onClose(final IVetoable vetoable) {
				final VetoHolder vetoHolder = new VetoHolder();
				application.onClose(vetoHolder);
				if (vetoHolder.hasVeto()) {
					vetoable.veto();
				}
			}
		});

		final IToolBarModel internalToolBarModel = new ToolBarModel();
		toolBarModel = new ToolBarModel();

		toolBarModel.addListModelListener(new IListModelListener() {
			@Override
			public void childRemoved(final int index) {
				internalToolBarModel.removeItem(index);
			}

			@Override
			public void childAdded(final int index) {
				internalToolBarModel.addItem(index, toolBarModel.getItems().get(index));
			}
		});

		toolBarMenuModel = new MenuModel();
		internalToolBarModel.addItem(toolBarMenuModel);

		tabItem.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "0[]0[]0[grow, 0::]0"));
		final IToolBar toolBar = tabItem.add(bpf.toolBar(), "alignx right, w 0::, wrap");
		tabItem.add(bpf.separator(), "growx, wrap");
		toolBar.setModel(internalToolBarModel);

		final ITreeBluePrint treeBp = bpf.tree().singleSelection().setContentScrolled(true);
		this.tree = tabItem.add(treeBp, MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		this.listModelListener = new IListModelListener() {

			@Override
			public void childRemoved(final int index) {
				if (popupMenuModel.getChildren().size() == 0) {
					tree.setPopupMenu(null);
				}
			}

			@Override
			public void childAdded(final int index) {
				if (popupMenuModel.getChildren().size() == 1) {
					tree.setPopupMenu(popupMenuModel);
				}
			}
		};

		this.popupMenuModel = new MenuModel();
		this.popupMenuModel.addListModelListener(listModelListener);
		if (popupMenuModel.getChildren().size() > 0) {
			tree.setPopupMenu(popupMenuModel);
		}

		application.onContextInitialize(this);
	}

	@Override
	public void add(final IComponentTreeNode componentTreeNode) {
		add(tree.getChildren().size(), componentTreeNode);
	}

	@Override
	public void add(final int index, final IComponentTreeNode componentTreeNode) {
		Assert.paramNotNull(componentTreeNode, "componentTreeNode");
		final ITreeNode node = tree.addNode(index);
		createdNodes.put(componentTreeNode, node);
		new ComponentTreeNodeContext(componentTreeNode, node, null, this, workbenchContext);
	}

	@Override
	public void remove(final IComponentTreeNode componentTreeNode) {
		Assert.paramNotNull(componentTreeNode, "componentTreeNode");
		final ITreeNode node = createdNodes.get(componentTreeNode);
		if (node != null) {
			node.setSelected(false);
			tree.removeNode(node);
		}
	}

	@Override
	public IToolBarModel getToolBar() {
		return toolBarModel;
	}

	@Override
	public IMenuModel getToolBarMenu() {
		return toolBarMenuModel;
	}

	@Override
	public IMenuModel getPopupMenu() {
		return popupMenuModel;
	}

	@Override
	public IWorkbenchContext getWorkbenchContext() {
		return workbenchContext;
	}

	protected IContainer getContentContainer() {
		return contentContainer;
	}

}
