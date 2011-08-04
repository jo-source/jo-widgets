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

package org.jowidgets.workbench.impl;

import java.util.HashMap;
import java.util.Map;

import org.jowidgets.api.controler.ITabItemListener;
import org.jowidgets.api.controler.ITreeSelectionEvent;
import org.jowidgets.api.controler.ITreeSelectionListener;
import org.jowidgets.api.model.IListModelListener;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.model.item.IToolBarModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.blueprint.ITreeBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.tools.controller.ListModelAdapter;
import org.jowidgets.tools.controller.TabItemAdapter;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.model.item.MenuModel;
import org.jowidgets.tools.types.VetoHolder;
import org.jowidgets.workbench.api.IComponentNode;
import org.jowidgets.workbench.api.IWorkbenchApplication;
import org.jowidgets.workbench.api.IWorkbenchApplicationContext;

public class WorkbenchApplicationContext implements IWorkbenchApplicationContext {

	private final WorkbenchContext workbenchContext;
	private final IWorkbenchApplication application;

	private final IMenuModel popupMenuModel;
	private final IToolBarModel toolBarModel;
	private final IMenuModel toolBarMenuModel;

	private final ComponentNodeContainerContext componentNodeContainerContext;

	private final Map<ITreeNode, ComponentNodeContext> registeredNodes;

	private final ITabItem tabItem;
	private final ITree tree;
	private final ITreeSelectionListener treeSelectionListener;
	private final IListModelListener popupMenuModelListener;
	private final ITabItemListener tabItemListener;

	private ITreeNode selectedNode;

	public WorkbenchApplicationContext(
		final WorkbenchContext workbenchContext,
		final ITabItem tabItem,
		final IWorkbenchApplication application) {

		this.workbenchContext = workbenchContext;
		this.tabItem = tabItem;
		this.application = application;

		this.registeredNodes = new HashMap<ITreeNode, ComponentNodeContext>();

		this.popupMenuModel = new MenuModel();

		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		final ViewWithToolBar toolBarHelper = new ViewWithToolBar(tabItem);
		toolBarModel = toolBarHelper.getToolBarModel();
		toolBarMenuModel = toolBarHelper.getToolBarMenuModel();

		final IComposite content = toolBarHelper.getViewContent();
		content.setLayout(MigLayoutFactory.growingInnerCellLayout());

		final ITreeBluePrint treeBp = bpf.tree().singleSelection().setContentScrolled(true);
		this.tree = content.add(treeBp, MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		this.treeSelectionListener = createTreeSelectionListener();
		tree.addTreeSelectionListener(treeSelectionListener);

		this.popupMenuModelListener = createPopupMenuModelListener();
		popupMenuModel.addListModelListener(popupMenuModelListener);

		this.tabItemListener = createTabItemListener();
		tabItem.addTabItemListener(tabItemListener);

		this.componentNodeContainerContext = new ComponentNodeContainerContext(tree, this, workbenchContext);
	}

	@Override
	public void add(final IComponentNode componentNode) {
		componentNodeContainerContext.add(componentNode);
	}

	@Override
	public void add(final int index, final IComponentNode componentNode) {
		componentNodeContainerContext.add(index, componentNode);
	}

	@Override
	public void remove(final IComponentNode componentNode) {
		componentNodeContainerContext.remove(componentNode);
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
	public WorkbenchContext getWorkbenchContext() {
		return workbenchContext;
	}

	protected void dispose() {
		application.onDispose();

		popupMenuModel.removeListModelListener(popupMenuModelListener);
		tree.removeTreeSelectionListener(treeSelectionListener);
		tabItem.removeTabItemListener(tabItemListener);

		for (final ITreeNode childNode : tree.getChildren()) {
			unRegisterNodeContext(childNode);
		}
	}

	protected String getId() {
		return application.getId();
	}

	protected IWorkbenchApplication getApplication() {
		return application;
	}

	protected void registerNodeContext(final ComponentNodeContext nodeContext) {
		registeredNodes.put(nodeContext.getTreeNode(), nodeContext);
	}

	protected void unRegisterNodeContext(final ITreeNode node) {
		final ComponentNodeContext nodeContext = registeredNodes.remove(node);
		if (nodeContext != null) {
			nodeContext.dispose();
		}
		if (selectedNode == node) {
			selectedNode = null;
		}
		for (final ITreeNode childNode : node.getChildren()) {
			unRegisterNodeContext(childNode);
		}
	}

	protected ComponentNodeContext getSelectedNodeContext() {
		if (selectedNode != null) {
			return registeredNodes.get(selectedNode);
		}
		return null;
	}

	private ITreeSelectionListener createTreeSelectionListener() {
		return new ITreeSelectionListener() {
			@Override
			public void selectionChanged(final ITreeSelectionEvent event) {

				final ITreeNode newSelectedNode = event.getFirstSelected();

				ComponentNodeContext wasSelectedContext = null;
				ComponentNodeContext isSelectedContext = null;

				if (newSelectedNode != selectedNode) {
					if (selectedNode != null) {
						wasSelectedContext = registeredNodes.get(selectedNode);
						if (wasSelectedContext != null && wasSelectedContext.isActive()) {
							workbenchContext.beforeComponentChange();
							final VetoHolder veto = wasSelectedContext.tryDeactivate();
							if (veto.hasVeto()) {
								tree.removeTreeSelectionListener(treeSelectionListener);
								selectedNode.setSelected(true);
								tree.addTreeSelectionListener(treeSelectionListener);
								workbenchContext.afterComponentChange();
								return;
							}
						}
					}

					if (newSelectedNode != null) {
						isSelectedContext = registeredNodes.get(newSelectedNode);
					}

					selectedNode = newSelectedNode;
					workbenchContext.selectComponentNode(isSelectedContext);
				}
			}
		};
	}

	private ITabItemListener createTabItemListener() {
		return new TabItemAdapter() {
			@Override
			public void onClose(final IVetoable vetoable) {
				final VetoHolder vetoHolder = new VetoHolder();
				application.onClose(vetoHolder);
				if (vetoHolder.hasVeto()) {
					vetoable.veto();
				}
				else {
					dispose();
					workbenchContext.unregsiterApplication(tabItem);
				}
			}

			@Override
			public void closed() {

			}

		};
	}

	private IListModelListener createPopupMenuModelListener() {
		return new ListModelAdapter() {

			@Override
			public void afterChildRemoved(final int index) {
				if (popupMenuModel.getChildren().size() == 0) {
					tree.setPopupMenu(null);
				}
			}

			@Override
			public void afterChildAdded(final int index) {
				if (popupMenuModel.getChildren().size() == 1) {
					tree.setPopupMenu(popupMenuModel);
				}
			}
		};
	}

}
