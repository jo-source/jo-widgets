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

package org.jowidgets.util.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.jowidgets.util.Assert;
import org.jowidgets.util.DefaultSystemTimeProvider;
import org.jowidgets.util.ISystemTimeProvider;
import org.jowidgets.util.mock.ScheduledFutureMock.ExcecutionType;

/**
 * Mock implementation for {@link ScheduledExecutorService}.
 *
 * This implementation queues the scheduled runnable's an allows to execute them controlled,
 * e.g. in a JUnit test.
 * 
 * A {@link ISystemTimeProvider} can be used to control the execution delay exactly
 * 
 * Remark: The given {@link ISystemTimeProvider} is only used for execution delays and not for the timeouts of blocking
 * methods like {@link #awaitTermination(long, TimeUnit)} or {@link #invokeAll(Collection, long, TimeUnit)}
 * or {@link #invokeAny(Collection, long, TimeUnit)} or {@link Future#get(long, TimeUnit)}
 * 
 * This executor service was designed thread save, so all methods of the interface {@link ScheduledExecutorService} and all
 * execution trigger methods like {@link #executeEvents()} or {@link #executeNextEvent()} or {@link #offerNextEventForExecution()}
 * can be invoked simultaneously from different threads.
 */
public final class ScheduledExecutorServiceMock implements ScheduledExecutorService {

    private final LinkedList<ScheduledFutureMock<?>> events;
    private final ISystemTimeProvider systemTimeProvider;
    private final AtomicBoolean shutdown;
    private final AtomicBoolean terminated;
    private final CountDownLatch terminationLatch;

    /**
     * Creates a new instance using the {@link DefaultSystemTimeProvider}
     */
    public ScheduledExecutorServiceMock() {
        this(DefaultSystemTimeProvider.getInstance());
    }

    /**
     * Creates a new instance using the given {@link ISystemTimeProvider}
     * 
     * @param systemTimeProvider The system time provider to use, never null
     */
    public ScheduledExecutorServiceMock(final ISystemTimeProvider systemTimeProvider) {
        Assert.paramNotNull(systemTimeProvider, "systemTimeProvider");

        this.systemTimeProvider = systemTimeProvider;
        this.events = new LinkedList<ScheduledFutureMock<?>>();
        this.shutdown = new AtomicBoolean();
        this.terminated = new AtomicBoolean();
        this.terminationLatch = new CountDownLatch(1);
    }

    /**
     * Gets a unmodifiable copy of the events to be executed
     *
     * @return The events, may be empty but never null
     */
    public List<ScheduledFutureMock<?>> getEvents() {
        synchronized (events) {
            return Collections.unmodifiableList(new ArrayList<ScheduledFutureMock<?>>(events));
        }
    }

    /**
     * Executes all available events.
     * 
     * A event is available if it's delay has been expired (delay <= 0) and if was not canceled or done and if it's not running.
     * 
     * The execution will be made in the current thread so this method blocks until all executions has been finished.
     * If no events are available, the method returns immediately with a empty result list.
     * 
     * @return The events that has been executed
     */
    public List<ScheduledFutureMock<?>> executeEvents() {
        final List<ScheduledFutureMock<?>> result = new LinkedList<ScheduledFutureMock<?>>();
        ScheduledFutureMock<?> scheduledFuture = executeNextEvent();
        while (scheduledFuture != null) {
            result.add(scheduledFuture);
            scheduledFuture = executeNextEvent();
        }
        return result;
    }

    /**
     * Executes the next available event.
     * 
     * A event is available if it's delay has been expired (delay <= 0) and if it was not canceled or done and if it's not
     * running.
     * 
     * The execution will be made in the current thread so this method blocks until execution is finished.
     * If no event is available, the method return immediately with a result of null.
     *
     * @return The event that was executed or null
     */
    public ScheduledFutureMock<?> executeNextEvent() {
        final ScheduledFutureMock<?> nextEventToExecute = offerNextEventForExecution();
        if (nextEventToExecute != null) {
            nextEventToExecute.execute();
        }
        return nextEventToExecute;
    }

    /**
     * Offers the next event that is available for execution.
     * 
     * A event is available if it's delay has been expired (delay <= 0) and if was canceled or done and if it's not running.
     * 
     * The invoker of this method is responsible to execute the offered event because events can only be offered once until
     * they are executed.
     * 
     * @return The next available event or null if no event is available
     */
    public ScheduledFutureMock<?> offerNextEventForExecution() {
        synchronized (events) {
            for (final ScheduledFutureMock<?> scheduledFuture : events) {
                if (scheduledFuture.isDelayExpired() && scheduledFuture.tryToOfferEvent()) {
                    return scheduledFuture;
                }
            }
            return null;
        }
    }

    @Override
    public void execute(final Runnable runnable) {
        submit(runnable);
    }

    @Override
    public ScheduledFutureMock<Void> submit(final Runnable runnable) {
        return schedule(runnable, 0, TimeUnit.MILLISECONDS);
    }

    @Override
    public <T> ScheduledFutureMock<T> submit(final Runnable runnable, final T result) {
        return submit(new CallableAdapter<T>(runnable, result));
    }

    @Override
    public <T> ScheduledFutureMock<T> submit(final Callable<T> callable) {
        return schedule(callable, 0, TimeUnit.MILLISECONDS);
    }

    @Override
    public ScheduledFutureMock<Void> schedule(final Runnable runnable, final long delay, final TimeUnit unit) {
        return scheduleRunnable(runnable, delay, 0, unit, ExcecutionType.SINGLE);
    }

    @Override
    public <T> ScheduledFutureMock<T> schedule(final Callable<T> callable, final long delay, final TimeUnit unit) {
        final ScheduledFutureMock<T> result = new ScheduledFutureMock<T>(
            this,
            ExcecutionType.SINGLE,
            callable,
            delay,
            0,
            unit,
            systemTimeProvider);
        scheduleEvent(result);
        return result;
    }

    @Override
    public ScheduledFutureMock<Void> scheduleAtFixedRate(
        final Runnable runnable,
        final long initialDelay,
        final long period,
        final TimeUnit unit) {
        return scheduleRunnable(runnable, initialDelay, period, unit, ExcecutionType.FIXED_RATE);
    }

    @Override
    public ScheduledFutureMock<Void> scheduleWithFixedDelay(
        final Runnable runnable,
        final long initialDelay,
        final long delay,
        final TimeUnit unit) {
        return scheduleRunnable(runnable, initialDelay, delay, unit, ExcecutionType.FIXED_DELAY);
    }

    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        try {
            return doInvokeAny(tasks, null, null);
        }
        catch (final TimeoutException e) {
            throw new RuntimeException("Unexpected timeout exception", e);
        }
    }

    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return doInvokeAny(tasks, Long.valueOf(timeout), unit);
    }

    private <T> T doInvokeAny(final Collection<? extends Callable<T>> tasks, final Long timeout, final TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        Assert.paramNotNull(tasks, "tasks");

        //first schedule all given tasks
        final List<Future<T>> allFutures = new ArrayList<Future<T>>(tasks.size());
        final LinkedBlockingQueue<AtomicReference<Future<T>>> doneTasks = new LinkedBlockingQueue<AtomicReference<Future<T>>>();
        for (final Callable<T> task : tasks) {
            final AtomicReference<Future<T>> futureReference = new AtomicReference<Future<T>>();
            final ScheduledFutureMock<T> future = submit(new Callable<T>() {
                @Override
                public T call() throws Exception {
                    try {
                        return task.call();
                    }
                    finally {
                        doneTasks.put(futureReference);
                    }
                }
            });
            futureReference.set(future);
            allFutures.add(future);
        }

        //wait until first done task becomes available
        AtomicReference<Future<T>> firstDoneTaskReference = null;
        try {
            if (timeout == null) {
                firstDoneTaskReference = doneTasks.take();
            }
            else {
                firstDoneTaskReference = doneTasks.poll(timeout.longValue(), unit);
                if (firstDoneTaskReference == null) {
                    throw new TimeoutException("Timeout of '" + timeout.longValue() + " " + unit + "' reached.");
                }
            }
        }
        finally {
            final Future<T> doneTask = firstDoneTaskReference != null ? firstDoneTaskReference.get() : null;
            //cancel all unfinished tasks
            for (final Future<T> future : allFutures) {
                if (future != doneTask && !future.isDone()) {
                    future.cancel(true);
                }
            }

        }

        //return the first done task
        return firstDoneTaskReference != null ? firstDoneTaskReference.get().get() : null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit)
            throws InterruptedException {
        return doInvokeAll(tasks, Long.valueOf(timeout), unit);
    }

    @Override
    public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return doInvokeAll(tasks, null, null);
    }

    private <T> List<Future<T>> doInvokeAll(
        final Collection<? extends Callable<T>> tasks,
        final Long timeout,
        final TimeUnit unit) throws InterruptedException {
        Assert.paramNotNull(tasks, "tasks");

        //first schedule all given tasks
        final List<Future<T>> result = new ArrayList<Future<T>>(tasks.size());
        final CopyOnWriteArraySet<AtomicReference<Future<T>>> calledTaskRefs = new CopyOnWriteArraySet<AtomicReference<Future<T>>>();

        final CountDownLatch latch = new CountDownLatch(tasks.size());
        final AtomicBoolean stopInvocation = new AtomicBoolean(false);

        for (final Callable<T> task : tasks) {
            final AtomicReference<Future<T>> futureReference = new AtomicReference<Future<T>>();
            final ScheduledFutureMock<T> future = submit(new Callable<T>() {
                @Override
                public T call() throws Exception {
                    try {
                        if (!stopInvocation.get()) {
                            return task.call();
                        }
                        else {
                            return null;
                        }
                    }
                    finally {
                        if (!stopInvocation.get()) {
                            calledTaskRefs.add(futureReference);
                        }
                        else {
                            //in this case reference has already been set
                            futureReference.get().cancel(true);
                        }
                        latch.countDown();
                    }
                }
            });
            futureReference.set(future);
            result.add(future);
        }

        try {
            //wait until all tasks has been called
            if (timeout == null) {
                latch.await();
            }
            else {
                final boolean awaitResult = latch.await(timeout.longValue(), unit);
                if (!awaitResult) {
                    stopInvocation.set(true);
                }
            }
        }
        catch (final InterruptedException e) {
            stopInvocation.set(true);
            Thread.currentThread().interrupt();
        }
        finally {
            if (stopInvocation.get()) {
                //cancel the uncalled tasks
                final Set<Future<T>> calledTasks = new HashSet<Future<T>>();
                for (final AtomicReference<Future<T>> calledTaskRef : calledTaskRefs) {
                    calledTasks.add(calledTaskRef.get());
                }
                for (final Future<T> task : result) {
                    if (!task.isDone()) {
                        if (!calledTasks.contains(task)) {
                            //from now the task is done
                            task.cancel(true);
                        }
                    }
                }
            }

            //wait for all tasks done
            for (final Future<T> task : result) {
                try {
                    task.get();
                }
                catch (final InterruptedException e) {
                    //set interrupt flag again
                    Thread.currentThread().interrupt();
                }
                catch (final ExecutionException e) {
                    //ignore here
                }
                catch (final CancellationException e) {
                    //ignore here
                }
            }

            if (Thread.interrupted()) {
                throw new InterruptedException("InvokeAll was interrupted");
            }
        }
        return result;
    }

    private ScheduledFutureMock<Void> scheduleRunnable(
        final Runnable runnable,
        final long initialDelay,
        final long delay,
        final TimeUnit unit,
        final ExcecutionType exceutionType) {
        final ScheduledFutureMock<Void> result = new ScheduledFutureMock<Void>(
            this,
            exceutionType,
            new CallableAdapter<Void>(runnable, null),
            initialDelay,
            delay,
            unit,
            systemTimeProvider);
        scheduleEvent(result);
        return result;
    }

    private void scheduleEvent(final ScheduledFutureMock<?> event) {
        if (!shutdown.get()) {
            synchronized (events) {
                events.add(event);
            }
        }
    }

    Object getEventsLock() {
        return events;
    }

    void removeEvent(final ScheduledFutureMock<?> event) {
        synchronized (events) {
            events.remove(event);
            setTerminatedIfNecessary();
        }
    }

    private void setTerminatedIfNecessary() {
        synchronized (events) {
            if (shutdown.get() && events.isEmpty() && !terminated.get()) {
                terminated.set(true);
                terminationLatch.countDown();
            }
        }
    }

    @Override
    public void shutdown() {
        if (shutdown.compareAndSet(false, true)) {
            setTerminatedIfNecessary();
        }
    }

    @Override
    public List<Runnable> shutdownNow() {
        final List<Runnable> result = new LinkedList<Runnable>();
        if (shutdown.compareAndSet(false, true)) {
            synchronized (events) {
                //copy events because cancel may remove events if not yet offered
                for (final ScheduledFutureMock<?> scheduledFuture : new ArrayList<ScheduledFutureMock<?>>(events)) {
                    result.add(scheduledFuture.getCommand());
                    scheduledFuture.cancel(true);
                }
                setTerminatedIfNecessary();
            }
        }
        return result;
    }

    @Override
    public boolean isShutdown() {
        return shutdown.get();
    }

    @Override
    public boolean isTerminated() {
        return terminated.get();
    }

    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        return terminationLatch.await(timeout, unit);
    }

    private class CallableAdapter<V> implements Callable<V> {

        private final Runnable runnable;
        private final V result;

        CallableAdapter(final Runnable runnable, final V result) {
            Assert.paramNotNull(runnable, "runnable");
            this.runnable = runnable;
            this.result = result;
        }

        @Override
        public V call() throws Exception {
            runnable.run();
            return result;
        }

    }
}
