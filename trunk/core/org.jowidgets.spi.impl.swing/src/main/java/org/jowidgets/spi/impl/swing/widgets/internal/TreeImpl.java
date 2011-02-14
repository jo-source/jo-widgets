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

package org.jowidgets.spi.impl.swing.widgets.internal;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.SelectionPolicy;
import org.jowidgets.spi.impl.swing.widgets.SwingControl;
import org.jowidgets.spi.impl.swing.widgets.internal.base.JoTreeNode;
import org.jowidgets.spi.impl.swing.widgets.internal.base.JoTreeNodeRenderer;
import org.jowidgets.spi.widgets.ITreeNodeSpi;
import org.jowidgets.spi.widgets.ITreeSpi;
import org.jowidgets.spi.widgets.setup.ITreeSetupSpi;

public class TreeImpl extends SwingControl implements ITreeSpi {

	private final Map<JoTreeNode, TreeNodeImpl> nodes;

	private final JTree tree;
	private final DefaultMutableTreeNode mutableRootNode;
	private final DefaultTreeModel treeModel;
	private final ITreeNodeSpi rootNode;

	private List<JoTreeNode> lastSelection;

	public TreeImpl(final ITreeSetupSpi setup) {
		super(createComponent(setup));

		this.nodes = new HashMap<JoTreeNode, TreeNodeImpl>();
		this.lastSelection = new LinkedList<JoTreeNode>();

		if (getUiReference() instanceof JScrollPane) {
			this.tree = (JTree) ((JScrollPane) getUiReference()).getViewport().getView();
		}
		else {
			this.tree = (JTree) getUiReference();
		}

		this.treeModel = (DefaultTreeModel) tree.getModel();
		this.mutableRootNode = (DefaultMutableTreeNode) treeModel.getRoot();

		tree.setCellRenderer(new JoTreeNodeRenderer());

		tree.addTreeExpansionListener(new TreeExpansionListener() {

			@Override
			public void treeExpanded(final TreeExpansionEvent event) {
				final TreePath path = event.getPath();
				if (path.getLastPathComponent() != mutableRootNode) {
					final JoTreeNode node = (JoTreeNode) path.getLastPathComponent();
					nodes.get(node).fireExpandedChanged(true);
				}
			}

			@Override
			public void treeCollapsed(final TreeExpansionEvent event) {
				final TreePath path = event.getPath();
				if (path.getLastPathComponent() != mutableRootNode) {
					final JoTreeNode node = (JoTreeNode) path.getLastPathComponent();
					nodes.get(node).fireExpandedChanged(false);
				}
			}
		});

		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(final TreeSelectionEvent e) {
				fireSelectionChange();
			}
		});

		setMouseListener(new MouseAdapter() {});
		tree.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(final MouseEvent e) {
				trigger(e);
			}

			@Override
			public void mousePressed(final MouseEvent e) {
				trigger(e);
			}

			private void trigger(final MouseEvent e) {
				if (e.isPopupTrigger()) {
					final TreePath path = tree.getPathForLocation(e.getX(), e.getY());
					final Position position = new Position(e.getX(), e.getY());
					if (path != null) {
						final JoTreeNode node = (JoTreeNode) path.getLastPathComponent();
						if (!e.isShiftDown() && !e.isControlDown()) {
							tree.setSelectionPath(new TreePath(node.getPath()));
						}
						nodes.get(node).firePopupDetected(position);
					}
					else {
						getPopupDetectionObservable().firePopupDetected(position);
					}
				}
			}

		});

		this.rootNode = new TreeNodeImpl(this, mutableRootNode);
	}

	private void fireSelectionChange() {

		final List<JoTreeNode> newSelection = new LinkedList<JoTreeNode>();
		final TreePath[] selectionPaths = tree.getSelectionPaths();
		if (selectionPaths != null) {
			for (final TreePath selectionPath : selectionPaths) {
				final Object selectedNode = selectionPath.getLastPathComponent();
				if (selectedNode instanceof JoTreeNode) {
					newSelection.add((JoTreeNode) selectedNode);
				}
			}
		}

		for (final JoTreeNode wasSelected : lastSelection) {
			if (!newSelection.contains(wasSelected)) {
				nodes.get(wasSelected).fireSelectionChanged(false);
			}
		}

		for (final JoTreeNode isSelected : newSelection) {
			if (!lastSelection.contains(isSelected)) {
				nodes.get(isSelected).fireSelectionChanged(true);
			}
		}

		lastSelection = newSelection;
	}

	@Override
	public ITreeNodeSpi getRootNode() {
		return rootNode;
	}

	protected DefaultMutableTreeNode getMutableRootNode() {
		return mutableRootNode;
	}

	protected JTree getTree() {
		return tree;
	}

	protected DefaultTreeModel getTreeModel() {
		return treeModel;
	}

	private static Component createComponent(final ITreeSetupSpi setup) {

		final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
		final DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);

		final JTree tree = new JTree(rootNode);
		tree.setModel(treeModel);

		rootNode.setAllowsChildren(true);
		tree.setShowsRootHandles(true);
		tree.setDoubleBuffered(true);
		tree.setRootVisible(false);

		ToolTipManager.sharedInstance().registerComponent(tree);

		if (SelectionPolicy.MULTI_SELECTION == setup.getSelectionPolicy()) {
			tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		}
		else if (SelectionPolicy.SINGLE_SELECTION == setup.getSelectionPolicy()) {
			tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		}
		else {
			throw new IllegalArgumentException("SelectionPolicy '" + setup.getSelectionPolicy() + "' is not known");
		}

		if (setup.isContentScrolled()) {
			return new JScrollPane(tree);
		}
		else {
			return tree;
		}
	}

	public void registerItem(final JoTreeNode joTreeNode, final TreeNodeImpl nodeImpl) {
		nodes.put(joTreeNode, nodeImpl);
	}

	public void unRegisterItem(final JoTreeNode joTreeNode) {
		nodes.remove(joTreeNode);
	}
}
