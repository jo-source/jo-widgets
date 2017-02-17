/*
 * Copyright (c) 2017, herrg
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

package org.jowidgets.util.concurrent;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jowidgets.util.Assert;

/**
 * Mock implementation of {@link ISingleThreadAccess} interface that can be used for junit tests e.g..
 * 
 * The thread that creates a instance becomes the single thread. All {@link Runnable}'s invoked by {@link #invoke(Runnable)}
 * method will be added to an internal queue and can be accessed, popped or run later.
 */
public final class EventBufferSingleThreadAccessMock implements ISingleThreadAccess {

    private final Thread creatorThread;
    private final BlockingQueue<Runnable> events;

    /**
     * Created a new instance. The thread that invoked this method becomes the ui thread.
     */
    public EventBufferSingleThreadAccessMock() {
        creatorThread = Thread.currentThread();
        events = new LinkedBlockingQueue<Runnable>();
    }

    @Override
    public boolean isSingleThread() {
        return Thread.currentThread() == creatorThread;
    }

    @Override
    public void invoke(final Runnable runnable) {
        Assert.paramNotNull(runnable, "runnable");
        events.add(runnable);
    }

    /**
     * Get's the number of enqueued events
     * 
     * This must be invoked in the defined single thread
     * 
     * @return The number of events
     */
    public int getEventsCount() {
        checkThread();
        return events.size();
    }

    /**
     * Get's a unmodifiable copy of all enqueued events.
     * 
     * This must be invoked in the defined single thread
     * 
     * @return A copy of the events queue, may be empty but never null
     */
    public List<Runnable> getEventsCopy() {
        checkThread();
        return Collections.unmodifiableList(new LinkedList<Runnable>(events));
    }

    /**
     * Get's a unmodifiable list of all enqueued events that will be popped from the internal queue.
     * 
     * This must be invoked in the defined single thread
     * 
     * @return The events list, may be empty but never null
     */
    public List<Runnable> popEvents() {
        checkThread();
        final List<Runnable> result = new LinkedList<Runnable>();
        while (!events.isEmpty()) {
            final Runnable event = events.poll();
            if (event != null) {
                result.add(event);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Runs all enqueued events that will be popped from the internal queue.
     * 
     * This must be invoked in the defined single thread
     */
    public void runEvents() {
        checkThread();
        while (!events.isEmpty()) {
            final Runnable event = events.poll();
            if (event != null) {
                event.run();
            }
        }

    }

    /**
     * Checks if the current thread is the single thread of this access
     */
    public void checkThread() {
        if (!isSingleThread()) {
            throw new IllegalStateException("Must be invoked in single thread");
        }
    }

}
