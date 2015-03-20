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

package org.jowidgets.tools.powo;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jowidgets.api.clipboard.TransferType;
import org.jowidgets.api.dnd.IDropTarget;
import org.jowidgets.api.dnd.IDropTargetListener;
import org.jowidgets.common.dnd.DropAction;
import org.jowidgets.common.dnd.DropMode;
import org.jowidgets.util.Assert;

/**
 * @deprecated The idea of POWO's (Plain Old Widget Object's) has not been established.
 *             For that, POWO's will no longer be supported and may removed completely in middle term.
 *             Feel free to move them to your own open source project.
 */
@Deprecated
final class DropTargetPowo implements IDropTarget {

    private final Set<IDropTargetListener> listeners;

    private IDropTarget original;

    private Collection<TransferType<?>> supportedTypes;
    private Set<DropAction> actions;
    private DropMode defaultDropMode;

    DropTargetPowo() {
        this.listeners = new LinkedHashSet<IDropTargetListener>();
    }

    void setOriginal(final IDropTarget original) {
        this.original = original;
        for (final IDropTargetListener listener : listeners) {
            original.addDropTargetListener(listener);
        }
        if (supportedTypes != null) {
            original.setTransferTypes(supportedTypes);
        }
        if (actions != null) {
            original.setActions(actions);
        }
        if (defaultDropMode != null) {
            original.setDefaultDropMode(defaultDropMode);
        }
        listeners.clear();
        supportedTypes = null;
        actions = null;
        defaultDropMode = null;
    }

    @Override
    public void addDropTargetListener(final IDropTargetListener listener) {
        Assert.paramNotNull(listener, "listener");
        if (original != null) {
            original.addDropTargetListener(listener);
        }
        else {
            listeners.add(listener);
        }
    }

    @Override
    public void removeDropTargetListener(final IDropTargetListener listener) {
        Assert.paramNotNull(listener, "listener");
        if (original != null) {
            original.removeDropTargetListener(listener);
        }
        else {
            listeners.remove(listener);
        }
    }

    @Override
    public void setTransferTypes(final Collection<TransferType<?>> supportedTypes) {
        Assert.paramNotNull(supportedTypes, "supportedTypes");
        if (original != null) {
            original.setTransferTypes(supportedTypes);
        }
        else {
            this.supportedTypes = new LinkedList<TransferType<?>>(supportedTypes);
        }
    }

    @Override
    public void setTransferTypes(final TransferType<?>... supportedTypes) {
        Assert.paramNotNull(supportedTypes, "supportedTypes");
        setTransferTypes(Arrays.asList(supportedTypes));
    }

    @Override
    public void setActions(final Set<DropAction> actions) {
        Assert.paramNotNull(actions, "actions");
        if (original != null) {
            original.setActions(actions);
        }
        else {
            this.actions = new HashSet<DropAction>(actions);
        }
    }

    @Override
    public void setActions(final DropAction... actions) {
        Assert.paramNotNull(actions, "actions");
        final Set<DropAction> actionsSet = new HashSet<DropAction>();
        for (int i = 0; i < actions.length; i++) {
            actionsSet.add(actions[i]);
        }
        setActions(actionsSet);
    }

    @Override
    public void setDefaultDropMode(final DropMode dropMode) {
        Assert.paramNotNull(actions, "actions");
        this.defaultDropMode = dropMode;
    }

}
