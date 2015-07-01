/*
 * Copyright (c) 2014, grossmann
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

package org.jowidgets.tools.widgets.wrapper;

import java.util.Collection;
import java.util.List;

import org.jowidgets.api.controller.ITreeListener;
import org.jowidgets.api.controller.ITreePopupDetectionListener;
import org.jowidgets.api.controller.ITreeSelectionListener;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeContainer;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.ITreeNodeVisitor;
import org.jowidgets.api.widgets.descriptor.ITreeNodeDescriptor;
import org.jowidgets.common.types.Position;

public class TreeWrapper extends ControlWrapper implements ITree {

    public TreeWrapper(final ITree widget) {
        super(widget);
    }

    @Override
    protected ITree getWidget() {
        return (ITree) super.getWidget();
    }

    @Override
    public ITreeNode addNode() {
        return getWidget().addNode();
    }

    @Override
    public ITreeNode addNode(final int index) {
        return getWidget().addNode(index);
    }

    @Override
    public ITreeNode addNode(final ITreeNodeDescriptor descriptor) {
        return getWidget().addNode(descriptor);
    }

    @Override
    public ITreeNode addNode(final int index, final ITreeNodeDescriptor descriptor) {
        return getWidget().addNode(index, descriptor);
    }

    @Override
    public void removeNode(final ITreeNode node) {
        getWidget().removeNode(node);
    }

    @Override
    public void removeNode(final int index) {
        getWidget().removeNode(index);
    }

    @Override
    public void removeAllNodes() {
        getWidget().removeAllNodes();
    }

    @Override
    public List<ITreeNode> getChildren() {
        return getWidget().getChildren();
    }

    @Override
    public ITreeContainer getParentContainer() {
        return getWidget().getParentContainer();
    }

    @Override
    public void setAllChildrenExpanded(final boolean expanded) {
        getWidget().setAllChildrenExpanded(expanded);
    }

    @Override
    public void setAllChildrenExpanded(final Integer pivotLevel, final boolean expanded) {
        getWidget().setAllChildrenExpanded(pivotLevel, expanded);
    }

    @Override
    public void setAllChildrenBelowExpandedAboveCollapsed(final int pivotLevel) {
        getWidget().setAllChildrenBelowExpandedAboveCollapsed(pivotLevel);
    }

    @Override
    public void setAllChildrenChecked(final boolean checked) {
        getWidget().setAllChildrenChecked(checked);
    }

    @Override
    public int getLevel() {
        return getWidget().getLevel();
    }

    @Override
    public boolean accept(final ITreeNodeVisitor visitor) {
        return getWidget().accept(visitor);
    }

    @Override
    public void addTreeListener(final ITreeListener listener) {
        getWidget().addTreeListener(listener);
    }

    @Override
    public void removeTreeListener(final ITreeListener listener) {
        getWidget().removeTreeListener(listener);
    }

    @Override
    public void addTreeSelectionListener(final ITreeSelectionListener listener) {
        getWidget().addTreeSelectionListener(listener);
    }

    @Override
    public void removeTreeSelectionListener(final ITreeSelectionListener listener) {
        getWidget().removeTreeSelectionListener(listener);
    }

    @Override
    public void addTreePopupDetectionListener(final ITreePopupDetectionListener listener) {
        getWidget().addTreePopupDetectionListener(listener);
    }

    @Override
    public void removeTreePopupDetectionListener(final ITreePopupDetectionListener listener) {
        getWidget().removeTreePopupDetectionListener(listener);
    }

    @Override
    public ITreeNode getNodeAt(final Position position) {
        return getWidget().getNodeAt(position);
    }

    @Override
    public Collection<ITreeNode> getSelection() {
        return getWidget().getSelection();
    }

    @Override
    public void setSelection(final Collection<? extends ITreeNode> selection) {
        getWidget().setSelection(selection);
    }

    @Override
    public void setSelection(final ITreeNode... selection) {
        getWidget().setSelection(selection);
    }

    @Override
    public void clearSelection() {
        getWidget().clearSelection();
    }

}
