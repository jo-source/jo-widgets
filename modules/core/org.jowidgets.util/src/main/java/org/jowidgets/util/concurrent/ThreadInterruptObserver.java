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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.jowidgets.util.Assert;
import org.jowidgets.util.IExceptionHandler;
import org.jowidgets.util.collection.IObserverSet;
import org.jowidgets.util.collection.IObserverSetFactory.Strategy;
import org.jowidgets.util.collection.ObserverSetFactory;

/**
 * This utility allows to observe the interrupted state for different threads.
 * 
 * The state of each observed thread is polled with a given period in a separate thread that
 * calls the {@link IThreadInterruptListener#interrupted(Thread)} method if the interrupt state
 * is set.
 * 
 * This can be useful e.g. if a blocking method does not support thread interruption but other
 * possibilities like cancel. Hibernate e.g. does not support interruption but allows to cancel a session.
 */
public final class ThreadInterruptObserver implements IThreadInterruptObservable {

    private static final long DEFAULT_PERIOD = 1000;

    private final ScheduledExecutorService executor;
    private final IExceptionHandler exceptionHandler;
    private final long period;
    private final AtomicReference<Future<?>> scheduledFutureReference;
    private final ConcurrentHashMap<Thread, IObserverSet<IThreadInterruptListener>> listenerForThreads;

    /**
     * Creates a new instance with default period of 1000 millis
     * 
     * @param threadNamePrefix The prefix to use for the name of the observation thread
     * @param exceptionHandler The exception used when exception occurs inside the onException runnables
     */
    public ThreadInterruptObserver(final String threadNamePrefix, final IExceptionHandler exceptionHandler) {
        this(threadNamePrefix, exceptionHandler, DEFAULT_PERIOD);
    }

    /**
     * Creates a new instance
     * 
     * @param threadNamePrefix The prefix to use for the name of the observation thread
     * @param exceptionHandler The exception used when exception occurs inside the onException runnables
     * @param period The period threads should be watched in millis
     */
    public ThreadInterruptObserver(final String threadNamePrefix, final IExceptionHandler exceptionHandler, final long period) {
        this(Executors.newScheduledThreadPool(1, new DaemonThreadFactory(threadNamePrefix)), exceptionHandler, period);
    }

    /**
     * Creates a new instance
     * 
     * @param executor The executor to use for thread observation
     * @param exceptionHandler The exception used when exception occurs inside the onException runnables
     * @param period The period threads should be watched in millis
     */
    public ThreadInterruptObserver(
        final ScheduledExecutorService executor,
        final IExceptionHandler exceptionHandler,
        final long period) {

        Assert.paramNotNull(executor, "executor");
        Assert.paramNotNull(exceptionHandler, "exceptionHandler");
        Assert.paramNotNull(period, "period");

        this.executor = executor;
        this.exceptionHandler = exceptionHandler;
        this.period = period;

        this.scheduledFutureReference = new AtomicReference<Future<?>>(null);
        this.listenerForThreads = new ConcurrentHashMap<Thread, IObserverSet<IThreadInterruptListener>>();
    }

    /**
     * Starts the observer if not yet running
     */
    public synchronized void start() {
        scheduledFutureReference
                .compareAndSet(null, executor.scheduleAtFixedRate(new ThreadChecker(), 0, period, TimeUnit.MILLISECONDS));
    }

    /**
     * Stops the observer if running
     */
    public synchronized void stop() {
        final Future<?> scheduledFuture = scheduledFutureReference.get();
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            scheduledFutureReference.set(null);
        }
    }

    /**
     * Stops the observer if running and shutdown the executor
     */
    public synchronized void shutdown() {
        stop();
        executor.shutdown();
    }

    /**
     * Checks if the observer is running
     * 
     * @return True if running, false otherwise
     */
    public boolean isRunning() {
        return scheduledFutureReference.get() != null;
    }

    @Override
    public void addInterruptListener(final IThreadInterruptListener listener) {
        addInterruptListener(Thread.currentThread(), listener);
    }

    @Override
    public void addInterruptListener(final Thread thread, final IThreadInterruptListener listener) {
        synchronized (thread) {
            IObserverSet<IThreadInterruptListener> listeners = listenerForThreads.get(thread);
            if (listeners == null) {
                listeners = ObserverSetFactory.create(Strategy.HIGH_PERFORMANCE);
                listenerForThreads.put(thread, listeners);
            }
            listeners.add(listener);
        }
    }

    @Override
    public boolean removeInterruptListener(final IThreadInterruptListener listener) {
        return removeInterruptListener(Thread.currentThread(), listener);
    }

    @Override
    public boolean removeInterruptListener(final Thread thread, final IThreadInterruptListener listener) {
        boolean result = false;
        synchronized (thread) {
            final IObserverSet<IThreadInterruptListener> listeners = listenerForThreads.get(thread);
            if (listeners != null) {
                result = listeners.remove(listener);
                if (!listeners.iterator().hasNext()) {
                    listenerForThreads.remove(thread);
                }
            }
        }
        return result;
    }

    /**
     * Checks if the current thread is observed
     * 
     * A thread is observed if there is at least one listener registered to the thread
     * 
     * @return True if the current thread is observed, false otherwise
     */
    public boolean isCurrentThreadObserved() {
        return isThreadObserved(Thread.currentThread());
    }

    /**
     * Checks if the given thread is observed.
     * 
     * A thread is observed if there is at least one listener registered to the thread
     * 
     * @param thread The thread to check
     * @return True if the thread is observed, false otherwise
     */
    public boolean isThreadObserved(final Thread thread) {
        Assert.paramNotNull(thread, "thread");
        synchronized (thread) {
            final IObserverSet<IThreadInterruptListener> listeners = listenerForThreads.get(thread);
            return listeners != null && listeners.iterator().hasNext();
        }
    }

    private class ThreadChecker implements Runnable {

        @Override
        public void run() {
            for (final Thread thread : listenerForThreads.keySet()) {
                checkThread(thread);
            }
        }

        private void checkThread(final Thread thread) {
            synchronized (thread) {
                if (thread.isInterrupted()) {
                    fireInterrupted(thread);
                }
            }
        }

        private void fireInterrupted(final Thread thread) {
            final IObserverSet<IThreadInterruptListener> listeners = listenerForThreads.get(thread);
            if (listeners != null) {
                for (final IThreadInterruptListener listener : listeners) {
                    try {
                        listener.interrupted(thread);
                    }
                    catch (final Throwable e) {
                        exceptionHandler.handleException(e);
                    }
                }
            }
        }
    }

}
