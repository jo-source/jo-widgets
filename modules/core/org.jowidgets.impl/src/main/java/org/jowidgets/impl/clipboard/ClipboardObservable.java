/*
 * Copyright (c) 2014, Michael
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

package org.jowidgets.impl.clipboard;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jowidgets.api.clipboard.IClipboardListener;
import org.jowidgets.api.clipboard.IClipboardObservable;
import org.jowidgets.util.Assert;
import org.jowidgets.util.event.IObservableCallback;

class ClipboardObservable implements IClipboardObservable {

    private final Set<IClipboardListener> listeners;
    private final IObservableCallback observableCallback;

    ClipboardObservable(final IObservableCallback observableCallback) {
        Assert.paramNotNull(observableCallback, "observableCallback");
        this.observableCallback = observableCallback;
        this.listeners = new LinkedHashSet<IClipboardListener>();
    }

    @Override
    public final void addClipboardListener(final IClipboardListener listener) {
        Assert.paramNotNull(listener, "listener");
        final int lastSize = listeners.size();
        listeners.add(listener);
        if (lastSize == 0) {
            observableCallback.onFirstRegistered();
        }
    }

    @Override
    public final void removeClipboardListener(final IClipboardListener listener) {
        Assert.paramNotNull(listener, "listener");
        final int lastSize = listeners.size();
        listeners.remove(listener);
        if (lastSize > 0 && listeners.size() == 0) {
            observableCallback.onLastUnregistered();
        }
    }

    public final void fireClipboardChanged() {
        for (final IClipboardListener listener : new LinkedList<IClipboardListener>(listeners)) {
            listener.clipboardChanged();
        }
    }

}
