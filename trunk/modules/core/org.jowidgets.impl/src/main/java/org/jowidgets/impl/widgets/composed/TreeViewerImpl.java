/*
 * Copyright (c) 2014, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.impl.widgets.composed;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.model.tree.ITreeNodeModel;
import org.jowidgets.api.model.tree.ITreeNodeModelListener;
import org.jowidgets.api.types.CheckedState;
import org.jowidgets.api.types.TreeViewerCreationPolicy;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeContainer;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.ITreeViewer;
import org.jowidgets.api.widgets.descriptor.setup.ITreeViewerSetup;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.controller.ITreeNodeListener;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.tools.controller.TreeNodeAdapter;
import org.jowidgets.tools.model.tree.TreeNodeModelAdapter;
import org.jowidgets.tools.widgets.wrapper.TreeWrapper;
import org.jowidgets.util.Assert;
import org.jowidgets.util.NullCompatibleEquivalence;

public final class TreeViewerImpl<ROOT_NODE_VALUE_TYPE> extends TreeWrapper implements ITreeViewer<ROOT_NODE_VALUE_TYPE> {

	private static final String DUMMY_NODE_NAME = UUID.randomUUID().toString();

	private static final IMessage MORE = Messages.getMessage("TreeViewerImpl.more");

	private final ITreeNodeModel<ROOT_NODE_VALUE_TYPE> rootNodeModel;
	private final TreeViewerCreationPolicy creationPolicy;
	private final Integer pageSize;
	private final boolean autoCheckMode;

	private final Map<ITreeContainer, ModelNodeBinding> bindings;

	public TreeViewerImpl(final ITree tree, final ITreeViewerSetup<ROOT_NODE_VALUE_TYPE> setup) {
		super(tree);
		Assert.paramNotNull(setup.getRootNodeModel(), "setup.getRootNodeModel()");
		Assert.paramNotNull(setup.getCreationPolicy(), "setup.getCreationPolicy()");
		this.rootNodeModel = setup.getRootNodeModel();
		this.creationPolicy = setup.getCreationPolicy();
		this.pageSize = setup.getPageSize();
		this.autoCheckMode = setup.getAutoCheckMode();
		this.bindings = new HashMap<ITreeContainer, TreeViewerImpl<ROOT_NODE_VALUE_TYPE>.ModelNodeBinding>();

		final ModelNodeBinding rootBinding = new ModelNodeBinding(tree, rootNodeModel);
		bindings.put(tree, rootBinding);

		rootNodeModel.addTreeNodeModelListener(new TreeNodeModelAdapter() {
			@Override
			public void dispose() {
				disposeBindings();
			}
		});

		tree.addDisposeListener(new IDisposeListener() {
			@Override
			public void onDispose() {
				disposeBindings();
			}
		});
	}

	private void disposeBindings() {
		for (final ModelNodeBinding binding : bindings.values()) {
			binding.dispose();
		}
	}

	@Override
	public ITreeNodeModel<ROOT_NODE_VALUE_TYPE> getRootNodeModel() {
		return rootNodeModel;
	}

	private final class ModelNodeBinding {

		private final ITreeContainer parentNode;
		private final ITreeNodeModel<?> parentNodeModel;

		private final ITreeNodeModelListener dataListener;
		private final ITreeNodeModelListener childrenListener;
		private final ITreeNodeListener treeNodeListener;

		private ITreeNode pagingNode;
		private int currentPage;

		private ModelNodeBinding(final ITreeContainer parentNode, final ITreeNodeModel<?> parentNodeModel) {
			Assert.paramNotNull(parentNode, "parentNode");
			Assert.paramNotNull(parentNodeModel, "parentNodeModel");
			this.parentNode = parentNode;
			this.parentNodeModel = parentNodeModel;
			this.currentPage = 0;

			this.dataListener = new DataListener();
			this.childrenListener = new ChildrenListener();
			this.treeNodeListener = new TreeNodeListener();

			if (parentNode instanceof ITreeNode) {
				final ITreeNode treeNode = (ITreeNode) parentNode;
				renderDataChanged(parentNodeModel, treeNode);
				renderSelectionChanged(parentNodeModel, treeNode);
				renderCheckedChanged(parentNodeModel, treeNode);

				if (TreeViewerCreationPolicy.CREATE_COMPLETE.equals(creationPolicy)) {
					onChildrenChanged();
				}
				else if (parentNodeModel.getChildrenCount() > 0) {
					if (parentNodeModel.isExpanded()) {
						onChildrenChanged();
					}
					else {
						final ITreeNode dummyNode = parentNode.addNode();
						dummyNode.setText(DUMMY_NODE_NAME);
					}
				}
			}
			else {
				onChildrenChanged();
			}

			if (parentNode instanceof ITreeNode) {
				final ITreeNode treeNode = (ITreeNode) parentNode;
				renderExpansionChanged(parentNodeModel, treeNode);
				parentNodeModel.addTreeNodeModelListener(dataListener);
				treeNode.addTreeNodeListener(treeNodeListener);
			}

			parentNodeModel.addTreeNodeModelListener(childrenListener);
		}

		private ITreeNodeModel<?> getParentNodeModel() {
			return parentNodeModel;
		}

		private void onChildrenChanged() {
			final boolean wasExpanded;
			final CheckedState lastCheckedState;
			if (parentNode instanceof ITreeNode) {
				final ITreeNode treeNode = (ITreeNode) parentNode;
				wasExpanded = treeNode.isExpanded();
				lastCheckedState = treeNode.getCheckedState();
			}
			else {
				wasExpanded = false;
				lastCheckedState = null;
			}

			//Brute force, remove all nodes and add new ones
			removeChildren();

			//than add the new nodes
			addChildren(null);

			if (parentNode instanceof ITreeNode) {
				final ITreeNode treeNode = (ITreeNode) parentNode;
				if (wasExpanded && !treeNode.isExpanded()) {
					treeNode.removeTreeNodeListener(treeNodeListener);
					treeNode.setExpanded(true);
					treeNode.addTreeNodeListener(treeNodeListener);
				}
				if (!NullCompatibleEquivalence.equals(lastCheckedState, treeNode.getCheckedState())) {
					treeNode.removeTreeNodeListener(treeNodeListener);
					treeNode.setCheckedState(lastCheckedState);
					treeNode.addTreeNodeListener(treeNodeListener);
				}
			}
		}

		private void eagerDisposeChildren() {
			final boolean wasExpanded;
			final CheckedState lastCheckedState;
			if (parentNode instanceof ITreeNode) {
				final ITreeNode treeNode = (ITreeNode) parentNode;
				wasExpanded = treeNode.isExpanded();
				lastCheckedState = treeNode.getCheckedState();
			}
			else {
				wasExpanded = false;
				lastCheckedState = null;
			}

			removeChildren();

			if (parentNodeModel.getChildrenCount() > 0) {
				final ITreeNode dummyNode = parentNode.addNode();
				dummyNode.setText(DUMMY_NODE_NAME);
			}

			if (parentNode instanceof ITreeNode) {
				final ITreeNode treeNode = (ITreeNode) parentNode;
				if (wasExpanded && !treeNode.isExpanded()) {
					treeNode.removeTreeNodeListener(treeNodeListener);
					treeNode.setExpanded(true);
					treeNode.addTreeNodeListener(treeNodeListener);
				}
				if (!NullCompatibleEquivalence.equals(lastCheckedState, treeNode.getCheckedState())) {
					treeNode.removeTreeNodeListener(treeNodeListener);
					treeNode.setCheckedState(lastCheckedState);
					treeNode.addTreeNodeListener(treeNodeListener);
				}
			}
		}

		private void removeChildren() {
			for (final ITreeNode childNode : parentNode.getChildren()) {
				final ModelNodeBinding binding = bindings.remove(childNode);
				if (binding != null) {
					final ITreeNodeModel<?> childNodeModel = binding.getParentNodeModel();
					renderDisposeNode(childNodeModel, childNode);
					binding.dispose();
				}
			}
			parentNode.removeAllNodes();
			currentPage = 0;
			pagingNode = null;
		}

		private void addChildren(final CheckedState checkedState) {
			final int pageSizeInt = pageSize != null ? pageSize.intValue() : Integer.MAX_VALUE;
			final int pageStart = currentPage * pageSizeInt;
			final int residualNodes = parentNodeModel.getChildrenCount() - pageStart;
			if (residualNodes < 0) {
				//no nodes to add
				return;
			}
			final int pageSizeTrunc = Math.min(residualNodes, pageSizeInt);

			final int pageEnd = pageStart + pageSizeTrunc;

			for (int i = pageStart; i < pageEnd; i++) {
				final ITreeNodeModel<?> childNodeModel = parentNodeModel.getChildNode(i);

				if (checkedState != null) {
					childNodeModel.setCheckedState(checkedState);
				}

				final ITreeNode childNode = parentNode.addNode();
				renderNodeCreated(childNodeModel, childNode);
				final ModelNodeBinding childBinding = new ModelNodeBinding(childNode, childNodeModel);
				bindings.put(childNode, childBinding);

				if (childNodeModel.isExpanded()) {
					childNode.setExpanded(true);
				}

				childNode.setCheckedState(childNodeModel.getCheckedState());

				if (childNodeModel.isSelected()) {
					childNode.setSelected(true);
				}
			}

			currentPage++;
			if (residualNodes > pageSizeInt) {
				this.pagingNode = createPagingNode(checkedState);
			}
		}

		private ITreeNode createPagingNode(final CheckedState checkedState) {
			final ITreeNode result = parentNode.addNode();
			result.setMarkup(Markup.EMPHASIZED);
			result.setForegroundColor(Colors.STRONG);
			result.setText(MORE.get());
			if (checkedState != null) {
				result.setCheckedState(checkedState);
			}
			result.addTreeNodeListener(new TreeNodeAdapter() {
				@Override
				public void selectionChanged(final boolean selected) {
					loadNextPage();
				}

				private void loadNextPage() {
					final CheckedState currentCheckedState = autoCheckMode ? result.getCheckedState() : null;
					parentNode.removeNode(pagingNode);
					addChildren(currentCheckedState);
				}
			});
			return result;
		}

		private boolean hasDummyChildNode() {
			final List<ITreeNode> children = parentNode.getChildren();
			return children.size() == 1 && DUMMY_NODE_NAME.equals(children.iterator().next().getText());
		}

		@SuppressWarnings({"rawtypes", "unchecked"})
		private void renderNodeCreated(final ITreeNodeModel model, final ITreeNode node) {
			model.getRenderer().nodeCreated(model.getData(), node);
		}

		@SuppressWarnings({"rawtypes", "unchecked"})
		private void renderDataChanged(final ITreeNodeModel model, final ITreeNode node) {
			model.getRenderer().dataChanged(model.getData(), node);
		}

		@SuppressWarnings({"rawtypes", "unchecked"})
		private void renderDisposeNode(final ITreeNodeModel model, final ITreeNode node) {
			model.getRenderer().disposeNode(model.getData(), node);
		}

		@SuppressWarnings({"rawtypes", "unchecked"})
		private void renderSelectionChanged(final ITreeNodeModel model, final ITreeNode node) {
			model.getRenderer().selectionChanged(model.getData(), node);
		}

		@SuppressWarnings({"rawtypes", "unchecked"})
		private void renderCheckedChanged(final ITreeNodeModel model, final ITreeNode node) {
			model.getRenderer().checkedChanged(model.getData(), node);
		}

		@SuppressWarnings({"rawtypes", "unchecked"})
		private void renderExpansionChanged(final ITreeNodeModel model, final ITreeNode node) {
			model.getRenderer().expansionChanged(model.getData(), node);
		}

		private void dispose() {
			if (parentNode instanceof ITreeNode) {
				parentNodeModel.removeTreeNodeModelListener(dataListener);
				((ITreeNode) parentNode).removeTreeNodeListener(treeNodeListener);
			}
			parentNodeModel.removeTreeNodeModelListener(childrenListener);
		}

		private final class DataListener extends TreeNodeModelAdapter {
			@Override
			public void dataChanged() {
				renderDataChanged(parentNodeModel, (ITreeNode) parentNode);
			}
		}

		private final class ChildrenListener extends TreeNodeModelAdapter {
			@Override
			public void childrenChanged() {
				onChildrenChanged();
			}
		}

		private final class TreeNodeListener implements ITreeNodeListener {

			@Override
			public void selectionChanged(final boolean selected) {
				parentNodeModel.setSelected(selected);
				renderSelectionChanged(parentNodeModel, (ITreeNode) parentNode);
			}

			@Override
			public void expandedChanged(final boolean expanded) {
				parentNodeModel.setExpanded(expanded);
				renderExpansionChanged(parentNodeModel, (ITreeNode) parentNode);
				if (expanded && hasDummyChildNode()) {
					onChildrenChanged();
				}
				else if (!expanded
					&& !hasDummyChildNode()
					&& TreeViewerCreationPolicy.CREATE_ON_EXPAND_DISPOSE_ON_COLLAPSE.equals(creationPolicy)) {
					eagerDisposeChildren();
				}
			}

			@Override
			public void checkedChanged(final boolean checked) {
				final ITreeNode treeNode = (ITreeNode) parentNode;
				parentNodeModel.setCheckedState(treeNode.getCheckedState());
				renderCheckedChanged(parentNodeModel, treeNode);
			}
		}

	}
}
