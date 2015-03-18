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

import org.jowidgets.api.command.ICommandExecutor;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.widgets.ITreeContainer;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.ITreeNodeVisitor;
import org.jowidgets.util.IFilter;

final class TreeExpansionExecutor implements ICommandExecutor {

    private final ITreeContainer tree;
    private final ExpansionMode expansionMode;
    private final IFilter<ITreeNode> filter;

    private Integer pivotLevel;

    TreeExpansionExecutor(
        final ITreeContainer tree,
        final ExpansionMode expansionMode,
        final IFilter<ITreeNode> filter,
        final Integer pivotLevel) {
        this.tree = tree;
        this.expansionMode = expansionMode;
        this.filter = filter;
        this.pivotLevel = pivotLevel;
    }

    @Override
    public void execute(final IExecutionContext executionContext) throws Exception {
        if (ExpansionMode.EXPAND.equals(expansionMode)
            || (ExpansionMode.EXPAND_COLLAPSE.equals(expansionMode) && pivotLevel == null)) {
            tree.setAllChildrenExpanded(pivotLevel, true);
        }
        else if (ExpansionMode.COLLAPSE.equals(expansionMode)) {
            tree.setAllChildrenExpanded(pivotLevel, false);
        }
        else if (ExpansionMode.EXPAND_COLLAPSE.equals(expansionMode)) {
            tree.setAllChildrenBelowExpandedAboveCollapsed(pivotLevel.intValue());
        }
        else if (ExpansionMode.EXPAND_FILTER_ACCEPT_COLLAPSE_OTHERS.equals(expansionMode)) {
            tree.accept(new ITreeNodeVisitor() {
                @Override
                public boolean visitEnter(final ITreeNode node) {
                    boolean expanded = filter.accept(node) && !node.isLeaf();
                    if (pivotLevel != null) {
                        expanded = expanded && node.getLevel() <= pivotLevel.intValue();
                    }
                    node.setExpanded(expanded);
                    return true;
                }

                @Override
                public boolean visitLeave(final ITreeNode node) {
                    return true;
                }
            });
        }
    }

    void setPivotLevel(final Integer pivotLevel) {
        this.pivotLevel = pivotLevel;
    }

}
