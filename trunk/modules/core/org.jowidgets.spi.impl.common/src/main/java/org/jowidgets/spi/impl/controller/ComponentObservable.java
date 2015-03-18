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

package org.jowidgets.spi.impl.controller;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IComponentObservable;
import org.jowidgets.util.Assert;
import org.jowidgets.util.NullCompatibleEquivalence;

public class ComponentObservable implements IComponentObservable {

    private final Set<IComponentListener> listeners;

    private Dimension lastSize;
    private Position lastPosition;

    public ComponentObservable() {
        this.listeners = new LinkedHashSet<IComponentListener>();
    }

    @Override
    public final void addComponentListener(final IComponentListener componentListener) {
        Assert.paramNotNull(componentListener, "componentListener");
        listeners.add(componentListener);
    }

    @Override
    public final void removeComponentListener(final IComponentListener componentListener) {
        Assert.paramNotNull(componentListener, "componentListener");
        listeners.remove(componentListener);
    }

    public final void fireSizeChanged(final Dimension size) {
        if (!NullCompatibleEquivalence.equals(size, lastSize)) {
            lastSize = size;
            for (final IComponentListener listener : new LinkedList<IComponentListener>(listeners)) {
                listener.sizeChanged();
            }
        }
    }

    public final void firePositionChanged(final Position position) {
        if (!NullCompatibleEquivalence.equals(position, lastPosition)) {
            lastPosition = position;
            for (final IComponentListener listener : new LinkedList<IComponentListener>(listeners)) {
                listener.positionChanged();
            }
        }
    }

}
