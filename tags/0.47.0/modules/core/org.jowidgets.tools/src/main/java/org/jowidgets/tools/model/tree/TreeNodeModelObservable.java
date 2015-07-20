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

package org.jowidgets.tools.model.tree;

import org.jowidgets.api.model.tree.ITreeNodeModelListener;
import org.jowidgets.api.model.tree.ITreeNodeModelObservable;
import org.jowidgets.util.collection.IObserverSet;
import org.jowidgets.util.collection.IObserverSetFactory.Strategy;
import org.jowidgets.util.collection.ObserverSetFactory;

public class TreeNodeModelObservable implements ITreeNodeModelObservable {

    private final IObserverSet<ITreeNodeModelListener> listeners;

    public TreeNodeModelObservable() {
        this.listeners = ObserverSetFactory.create(Strategy.HIGH_PERFORMANCE);
    }

    @Override
    public final void addTreeNodeModelListener(final ITreeNodeModelListener listener) {
        listeners.add(listener);
    }

    @Override
    public final void removeTreeNodeModelListener(final ITreeNodeModelListener listener) {
        listeners.remove(listener);
    }

    public final void fireDataChanged() {
        for (final ITreeNodeModelListener listener : listeners) {
            listener.dataChanged();
        }
    }

    public final void fireChildrenChanged() {
        for (final ITreeNodeModelListener listener : listeners) {
            listener.childrenChanged();
        }
    }

    public final void fireSelectionChanged() {
        for (final ITreeNodeModelListener listener : listeners) {
            listener.selectionChanged();
        }
    }

    public final void fireCheckedChanged() {
        for (final ITreeNodeModelListener listener : listeners) {
            listener.checkedChanged();
        }
    }

    public final void fireCheckableChanged() {
        for (final ITreeNodeModelListener listener : listeners) {
            listener.checkableChanged();
        }
    }

    public final void fireExpansionChanged() {
        for (final ITreeNodeModelListener listener : listeners) {
            listener.expansionChanged();
        }
    }

    public final void fireVisibilityChanged() {
        for (final ITreeNodeModelListener listener : listeners) {
            listener.visibilityChanged();
        }
    }

    public final void fireDisposed() {
        for (final ITreeNodeModelListener listener : listeners) {
            listener.dataChanged();
        }
    }

}
