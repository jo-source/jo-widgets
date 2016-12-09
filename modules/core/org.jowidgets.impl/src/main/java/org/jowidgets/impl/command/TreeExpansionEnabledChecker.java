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
import org.jowidgets.api.widgets.ITreeNodeVisitor;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.tools.command.AbstractEnabledChecker;
import org.jowidgets.tools.controller.TreeAdapter;
import org.jowidgets.util.IFilter;
import org.jowidgets.util.NullCompatibleEquivalence;
import org.jowidgets.util.ValueHolder;

final class TreeExpansionEnabledChecker extends AbstractEnabledChecker implements IEnabledChecker {

    private static final IMessage NODES_ALREADY_EXPANDED = Messages
            .getMessage("TreeExpansionEnabledChecker.nodesAlreadyExpanded");
    private static final IMessage NODES_ALREADY_COLLAPSED = Messages
            .getMessage("TreeExpansionEnabledChecker.nodesAlreadyCollapsed");

    private final ITreeContainer treeContainer;
    private final ExpansionMode expansionMode;
    private final IFilter<ITreeNode> filter;

    private Integer pivotLevel;

    TreeExpansionEnabledChecker(
        final ITreeContainer treeContainer,
        final ExpansionMode expansionMode,
        final IFilter<ITreeNode> filter,
        final Integer pivotLevel) {

        this.treeContainer = treeContainer;
        this.expansionMode = expansionMode;
        this.filter = filter;
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

            @Override
            public void nodeChecked(final ITreeNode node) {
                fireChangedEvent();
            }

            @Override
            public void nodeUnchecked(final ITreeNode node) {
                fireChangedEvent();
            }

            @Override
            public void afterNodeAdded(final ITreeNode node) {
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
        if (ExpansionMode.EXPAND.equals(expansionMode)
            || (ExpansionMode.EXPAND_COLLAPSE.equals(expansionMode) && pivotLevel == null)) {
            if (hasChildNodeThatWillBeChanged(treeContainer, pivotLevel, true)) {
                return EnabledState.ENABLED;
            }
            else {
                return EnabledState.disabled(NODES_ALREADY_EXPANDED.get());
            }
        }
        else if (ExpansionMode.COLLAPSE.equals(expansionMode)) {
            if (hasChildNodeThatWillBeChanged(treeContainer, pivotLevel, false)) {
                return EnabledState.ENABLED;
            }
            else {
                return EnabledState.disabled(NODES_ALREADY_COLLAPSED.get());
            }
        }
        else if (ExpansionMode.EXPAND_COLLAPSE.equals(expansionMode)) {
            if (((pivotLevel > 0) && hasChildNodeThatWillBeChanged(treeContainer, pivotLevel - 1, true))
                || hasChildNodeThatWillBeChanged(treeContainer, pivotLevel, false)) {
                return EnabledState.ENABLED;
            }
            return EnabledState.disabled(NODES_ALREADY_EXPANDED.get());
        }
        else if (ExpansionMode.EXPAND_FILTER_ACCEPT_COLLAPSE_OTHERS.equals(expansionMode)) {
            final ValueHolder<IEnabledState> result = new ValueHolder<IEnabledState>(
                EnabledState.disabled(NODES_ALREADY_EXPANDED.get()));

            treeContainer.accept(new ITreeNodeVisitor() {

                private boolean visitResult = true;

                @Override
                public boolean visitEnter(final ITreeNode node) {
                    boolean expanded = filter.accept(node);
                    if (pivotLevel != null) {
                        expanded = expanded && node.getLevel() <= pivotLevel.intValue();
                    }
                    if (!node.isLeaf() && node.isExpanded() != expanded) {
                        result.set(EnabledState.ENABLED);
                        visitResult = false;
                    }
                    return visitResult;
                }

                @Override
                public boolean visitLeave(final ITreeNode node) {
                    return visitResult;
                }
            });
            return result.get();
        }
        else {
            throw new IllegalStateException("The expansion mode '" + expansionMode + "' is not supported");
        }
    }

    private boolean hasChildNodeThatWillBeChanged(final ITreeContainer tree, final Integer currentLevel, final boolean expanded) {

        if (tree.getChildren().size() == 0) {
            return false;
        }

        //consider children
        if (currentLevel == null) {
            for (final ITreeNode childNode : tree.getChildren()) {
                if (willNodeBeChanged(childNode, null, expanded)) {
                    return true;
                }
                else if (hasChildNodeThatWillBeChanged(childNode, null, expanded)) {
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
            final Integer newPivotlevel = Integer.valueOf(newPivot);

            if (expanded) {
                for (final ITreeNode childNode : tree.getChildren()) {
                    if (willNodeBeChanged(childNode, pivot, expanded)) {
                        return true;
                    }
                    else if (pivot > 0 && hasChildNodeThatWillBeChanged(childNode, newPivotlevel, expanded)) {
                        return true;
                    }
                }
            }
            else {
                for (final ITreeNode childNode : tree.getChildren()) {
                    if (willNodeBeChanged(childNode, pivot, expanded)) {
                        return true;
                    }
                    else if (hasChildNodeThatWillBeChanged(childNode, newPivotlevel, expanded)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean willNodeBeChanged(final ITreeNode node, final Integer currentLevel, final boolean expanded) {

        if (currentLevel == null) {
            if (node.isExpanded() != expanded) {
                return node.getChildren().size() > 0;
            }
        }
        else {
            final int pivot = currentLevel.intValue();
            if (expanded) {
                if (node.isExpanded() != expanded) {
                    return node.getChildren().size() > 0;
                }
            }
            else {
                if (pivot == 0) {
                    if (node.isExpanded() != expanded) {
                        return node.getChildren().size() > 0;
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
