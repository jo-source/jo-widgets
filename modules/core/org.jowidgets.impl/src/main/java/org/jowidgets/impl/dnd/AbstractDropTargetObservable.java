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

package org.jowidgets.impl.dnd;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jowidgets.api.dnd.IDropEvent;
import org.jowidgets.api.dnd.IDropResponse;
import org.jowidgets.api.dnd.IDropTargetListener;
import org.jowidgets.api.dnd.IDropTargetObservable;
import org.jowidgets.util.Assert;

abstract class AbstractDropTargetObservable implements IDropTargetObservable {

    private final Set<IDropTargetListener> listeners;

    AbstractDropTargetObservable() {
        this.listeners = new LinkedHashSet<IDropTargetListener>();
    }

    @Override
    public final void addDropTargetListener(final IDropTargetListener listener) {
        Assert.paramNotNull(listener, "listener");
        final int lastSize = listeners.size();
        listeners.add(listener);
        if (lastSize == 0) {
            setActive(true);
        }
    }

    @Override
    public final void removeDropTargetListener(final IDropTargetListener listener) {
        Assert.paramNotNull(listener, "listener");
        final int lastSize = listeners.size();
        listeners.remove(listener);
        if (lastSize == 1 && listeners.size() == 0) {
            setActive(false);
        }
    }

    final void fireDragEnter(final IDropEvent event, final IDropResponse response) {
        for (final IDropTargetListener listener : new LinkedList<IDropTargetListener>(listeners)) {
            listener.dragEnter(event, response);
        }
    }

    final void fireDragOver(final IDropEvent event, final IDropResponse response) {
        for (final IDropTargetListener listener : new LinkedList<IDropTargetListener>(listeners)) {
            listener.dragOver(event, response);
        }
    }

    final void fireDragOperationChanged(final IDropEvent event, final IDropResponse response) {
        for (final IDropTargetListener listener : new LinkedList<IDropTargetListener>(listeners)) {
            listener.dragOperationChanged(event, response);
        }
    }

    final void fireDragExit() {
        for (final IDropTargetListener listener : new LinkedList<IDropTargetListener>(listeners)) {
            listener.dragExit();
        }
    }

    final void fireDropAccept(final IDropEvent event, final IDropResponse response) {
        for (final IDropTargetListener listener : new LinkedList<IDropTargetListener>(listeners)) {
            listener.dropAccept(event, response);
        }
    }

    final void fireDrop(final IDropEvent event) {
        for (final IDropTargetListener listener : new LinkedList<IDropTargetListener>(listeners)) {
            listener.drop(event);
        }
    }

    protected abstract void setActive(boolean active);

}
