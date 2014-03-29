/*
 * Copyright (c) 2014, MGrossmann
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

package org.jowidgets.impl.command;

import org.jowidgets.api.command.EnabledState;
import org.jowidgets.api.command.IEnabledChecker;
import org.jowidgets.api.command.IEnabledState;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeContainer;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.tools.command.AbstractEnabledChecker;
import org.jowidgets.tools.controller.TreeAdapter;
import org.jowidgets.util.NullCompatibleEquivalence;

final class TreeExpansionEnabledChecker extends AbstractEnabledChecker implements IEnabledChecker {

	private static final IMessage NODES_ALREADY_EXPANDED = Messages.getMessage("TreeExpansionEnabledChecker.nodesAlreadyExpanded");
	private static final IMessage NODES_ALREADY_COLLAPSED = Messages.getMessage("TreeExpansionEnabledChecker.nodesAlreadyCollapsed");

	private final ITreeContainer treeContainer;
	private final boolean expanded;

	private Integer pivotLevel;

	TreeExpansionEnabledChecker(final ITreeContainer treeContainer, final boolean expanded, final Integer pivotLevel) {
		this.treeContainer = treeContainer;
		this.expanded = expanded;
		this.pivotLevel = pivotLevel;

		getParentTree(treeContainer).addTreeListener(new TreeAdapter() {

			@Override
			public void nodeExpanded(final ITreeNode node) {
				fireChangedEvent();
			}

			@Override
			public void nodeCollapsed(final ITreeNode node) {
				fireChangedEvent();
			}

		});
	}

	private static ITree getParentTree(final ITreeContainer treeContainer) {
		if (treeContainer instanceof ITree) {
			return (ITree) treeContainer;
		}
		else {
			return getParentTree(treeContainer.getParentContainer());
		}
	}

	@Override
	public IEnabledState getEnabledState() {
		if (hasNodeThatWillBeChanged(treeContainer, pivotLevel)) {
			return EnabledState.ENABLED;
		}
		else if (expanded) {
			return EnabledState.disabled(NODES_ALREADY_EXPANDED.get());
		}
		else {
			return EnabledState.disabled(NODES_ALREADY_COLLAPSED.get());
		}
	}

	private boolean hasNodeThatWillBeChanged(final ITreeContainer tree, final Integer currentLevel) {
		if (tree instanceof ITreeNode) {
			final ITreeNode node = (ITreeNode) tree;
			if (currentLevel == null) {
				if (node.isExpanded() != expanded) {
					return true;
				}
			}
			else {
				final int pivot = currentLevel.intValue();
				if (expanded) {
					if (pivot > 0) {
						if (node.isExpanded() != expanded) {
							return true;
						}
					}
				}
				else {
					if (pivot == 0) {
						if (node.isExpanded() != expanded) {
							return true;
						}
					}
				}
			}
		}

		//consider children
		if (currentLevel == null) {
			for (final ITreeNode childNode : tree.getChildren()) {
				if (hasNodeThatWillBeChanged(childNode, null)) {
					return true;
				}
			}
		}
		else {
			final int pivot = currentLevel.intValue();
			int newPivot = pivot;
			if (newPivot > 0) {
				newPivot = pivot - 1;
			}
			if (!expanded || pivot > 0) {
				for (final ITreeNode childNode : tree.getChildren()) {
					if (hasNodeThatWillBeChanged(childNode, Integer.valueOf(newPivot))) {
						return true;
					}
				}
			}
		}

		return false;
	}

	void setPivotLevel(final Integer pivotLevel) {
		if (!NullCompatibleEquivalence.equals(this.pivotLevel, pivotLevel)) {
			this.pivotLevel = pivotLevel;
			fireChangedEvent();
		}
	}

}
