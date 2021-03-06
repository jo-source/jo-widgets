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

import java.util.Collection;
import java.util.LinkedList;

import org.jowidgets.api.command.IActionBuilder;
import org.jowidgets.api.command.ITreeExpansionAction;
import org.jowidgets.api.command.ITreeExpansionActionBuilder;
import org.jowidgets.api.widgets.ITreeContainer;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.util.Assert;
import org.jowidgets.util.FilterComposite;
import org.jowidgets.util.IFilter;

class TreeExpansionActionBuilder extends AbstractDefaultActionBuilder implements ITreeExpansionActionBuilder {

    private final ITreeContainer tree;
    private final ExpansionMode expansionMode;
    private final Collection<IFilter<ITreeNode>> filters;

    private Integer level;
    private boolean enabledChecking;
    private String boundPivotLevelText;

    TreeExpansionActionBuilder(final ITreeContainer tree, final ExpansionMode expansionMode) {
        Assert.paramNotNull(tree, "tree");
        this.tree = tree;
        this.enabledChecking = true;
        this.expansionMode = expansionMode;
        this.filters = new LinkedList<IFilter<ITreeNode>>();
    }

    @Override
    public ITreeExpansionActionBuilder setPivotLevel(final int level) {
        return setPivotLevel(Integer.valueOf(level));
    }

    @Override
    public ITreeExpansionActionBuilder setPivotLevel(final Integer level) {
        this.level = level;
        return this;
    }

    @Override
    public ITreeExpansionActionBuilder setBoundPivotLevelText(final String text) {
        this.boundPivotLevelText = text;
        return this;
    }

    @Override
    public ITreeExpansionActionBuilder setEnabledChecking(final boolean enabledChecking) {
        this.enabledChecking = enabledChecking;
        return this;
    }

    @Override
    public ITreeExpansionActionBuilder addFilter(final IFilter<ITreeNode> filter) {
        Assert.paramNotNull(filter, "filter");
        filters.add(filter);
        return this;
    }

    @Override
    public ITreeExpansionActionBuilder setFilter(final IFilter<ITreeNode> filter) {
        Assert.paramNotNull(filter, "filter");
        filters.clear();
        return addFilter(filter);
    }

    @Override
    public ITreeExpansionAction build() {
        return (ITreeExpansionAction) super.build();
    }

    @Override
    protected ITreeExpansionAction doBuild(final IActionBuilder original) {
        return new TreeExpansionAction(
            original,
            tree,
            expansionMode,
            FilterComposite.create(filters),
            enabledChecking,
            level,
            getText(),
            boundPivotLevelText);
    }

}
