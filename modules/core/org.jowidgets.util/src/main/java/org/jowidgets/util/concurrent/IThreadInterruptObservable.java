/*
 * Copyright (c) 2018, grossmann
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

/**
 * Can be used to observe interrupts on threads.
 * 
 * Remark: Because java threads does not support listeners for thread interrupts, the interrupted state
 * must be polled by implementations. Due to this fact, the interrupt events invoked on the {@link IThreadInterruptListener}
 * may be invoked delayed. Until the interrupt state of the thread will not be cleared, the listener fires continual with the
 * delay that can be requested from the method {@link #getDelayMillis()} until the listener will be removed or the interrupted
 * state will be cleared.
 */
public interface IThreadInterruptObservable {

    /**
     * Adds a interrupt listener to the current thread
     * 
     * @param listener The listener to add
     */
    void addInterruptListener(IThreadInterruptListener listener);

    /**
     * Adds a interrupt listener to the given thread
     * 
     * @param thread The thread to add the listener for
     * @param listener The listener to add
     */
    void addInterruptListener(Thread thread, IThreadInterruptListener listener);

    /**
     * Removes a interrupt listener from the current thread
     * 
     * @param listener The listener to remove
     * 
     * @return True if the listener was removed, false if the listener was not registered
     */
    boolean removeInterruptListener(IThreadInterruptListener listener);

    /**
     * Removes a interrupt listener from the given thread
     * 
     * @param thread The thread to remove the listener from
     * @param listener The listener to remove
     * 
     * @return True if the listener was removed, false if the listener was not registered
     */
    boolean removeInterruptListener(Thread thread, IThreadInterruptListener listener);

    /**
     * Get's the possible delay from interrupt happened to interrupted event.
     * 
     * Because java threads does not support listeners for thread interrupts, the interrupted state must be polled by
     * implementations. Due to this fact, the interrupt events invoked on the {@link IThreadInterruptListener} will be invoked
     * delayed.
     * 
     * Remark: If listeners do time consuming operations, the resulting delay may be higher then the value returned by this
     * method.
     * 
     * @return The delay in milliseconds
     */
    long getDelayMillis();

}
