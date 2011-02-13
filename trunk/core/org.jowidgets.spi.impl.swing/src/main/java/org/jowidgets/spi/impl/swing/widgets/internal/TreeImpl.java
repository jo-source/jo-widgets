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

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.jowidgets.common.types.SelectionPolicy;
import org.jowidgets.spi.impl.swing.image.SwingImageRegistry;
import org.jowidgets.spi.impl.swing.util.ColorConvert;
import org.jowidgets.spi.impl.swing.util.FontProvider;
import org.jowidgets.spi.impl.swing.widgets.SwingControl;
import org.jowidgets.spi.impl.swing.widgets.internal.base.JoTreeNode;
import org.jowidgets.spi.widgets.ITreeNodeSpi;
import org.jowidgets.spi.widgets.ITreeSpi;
import org.jowidgets.spi.widgets.setup.ITreeSetupSpi;

public class TreeImpl extends SwingControl implements ITreeSpi {

	private final JTree tree;
	private final DefaultMutableTreeNode mutableRootNode;
	private final DefaultTreeModel treeModel;
	private final ITreeNodeSpi rootNode;

	public TreeImpl(final ITreeSetupSpi setup) {
		super(createComponent(setup));

		if (getUiReference() instanceof JScrollPane) {
			this.tree = (JTree) ((JScrollPane) getUiReference()).getViewport().getView();
		}
		else {
			this.tree = (JTree) getUiReference();
		}

		this.treeModel = (DefaultTreeModel) tree.getModel();
		this.mutableRootNode = (DefaultMutableTreeNode) treeModel.getRoot();

		tree.setCellRenderer(new DefaultTreeCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
			public Component getTreeCellRendererComponent(
				final JTree tree,
				final Object value,
				final boolean selected,
				final boolean expanded,
				final boolean leaf,
				final int row,
				final boolean hasFocus) {

				final Component result = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

				if (value instanceof JoTreeNode) {
					final JoTreeNode joTreeNode = ((JoTreeNode) value);
					setText(joTreeNode.getText());
					setToolTipText(joTreeNode.getToolTipText());
					setIcon(SwingImageRegistry.getInstance().getImageIcon(joTreeNode.getIcon()));

					if (joTreeNode.getMarkup() != null) {
						setFont(FontProvider.deriveFont(getFont(), joTreeNode.getMarkup()));
					}

					if (!selected && joTreeNode.getBackgroundColor() != null) {
						setBackgroundNonSelectionColor(ColorConvert.convert(joTreeNode.getBackgroundColor()));
					}
					else {
						setBackgroundNonSelectionColor(null);
					}

					if (!selected && joTreeNode.getForegroundColor() != null) {
						setForeground(ColorConvert.convert(joTreeNode.getForegroundColor()));
					}
					else {
						setForeground(null);
					}
				}

				return result;

			}
		});

		this.rootNode = new TreeNodeImpl(this, mutableRootNode);
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

	public void registerItem(final JoTreeNode uiReference, final TreeNodeImpl result) {
		// TODO Auto-generated method stub
	}

	public void unRegisterItem(final TreeNode child) {
		// TODO Auto-generated method stub
	}
}
