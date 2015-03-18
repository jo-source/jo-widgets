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

package org.jowidgets.util.collection;

import java.util.Arrays;
import java.util.Iterator;

import org.jowidgets.util.Assert;
import org.jowidgets.util.CollectionUtils;

final class ObserverSetMemoryStrategyImpl<OBSERVER_TYPE> implements IObserverSet<OBSERVER_TYPE> {

    private OBSERVER_TYPE[] listeners;

    @Override
    public Iterator<OBSERVER_TYPE> iterator() {
        if (listeners == null) {
            return CollectionUtils.unmodifiableEmptyIterator();
        }
        else {
            return CollectionUtils.unmodifiableIterator(Arrays.asList(listeners).iterator());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void add(final OBSERVER_TYPE observer) {
        Assert.paramNotNull(observer, "observer");
        if (listeners == null) {
            listeners = (OBSERVER_TYPE[]) new Object[1];
            listeners[0] = observer;
        }
        else {
            if (indexOf(observer) == -1) {//only add if not already done
                final Object[] oldListeners = listeners;
                listeners = (OBSERVER_TYPE[]) new Object[oldListeners.length + 1];
                System.arraycopy(oldListeners, 0, listeners, 0, oldListeners.length);
                listeners[listeners.length - 1] = observer;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(final OBSERVER_TYPE observer) {
        Assert.paramNotNull(observer, "observer");
        if (listeners != null) {
            //listeners.length is at least 1
            if (listeners.length == 1) {
                if (listeners[0].equals(observer)) {
                    listeners = null;
                    return true;
                }
            }
            else {//listeners.length > 1
                final int removeIndex = indexOf(observer);
                if (removeIndex != -1) {
                    final Object[] oldListeners = listeners;
                    listeners = (OBSERVER_TYPE[]) new Object[oldListeners.length - 1];
                    if (removeIndex == 0) {//first removed
                        System.arraycopy(oldListeners, 1, listeners, 0, listeners.length);
                    }
                    else if (removeIndex == oldListeners.length - 1) {//last removed
                        System.arraycopy(oldListeners, 0, listeners, 0, listeners.length);
                    }
                    else {//removed in the middle
                        final int indexAfter = removeIndex + 1;
                        System.arraycopy(oldListeners, 0, listeners, 0, removeIndex);
                        System.arraycopy(oldListeners, indexAfter, listeners, removeIndex, listeners.length - removeIndex);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private int indexOf(final OBSERVER_TYPE observer) {
        if (listeners != null) {
            for (int index = 0; index < listeners.length; index++) {
                //by contract there are no null elements so this works
                if (listeners[index].equals(observer)) {
                    return index;
                }
            }
        }
        return -1;
    }

    @Override
    public void clear() {
        listeners = null;
    }

}
