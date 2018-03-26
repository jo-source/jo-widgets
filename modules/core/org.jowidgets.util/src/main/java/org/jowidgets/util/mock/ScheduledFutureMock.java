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

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.jowidgets.util.Assert;
import org.jowidgets.util.ISystemTimeProvider;

/**
 * A mock implementation for the {@link ScheduledFuture} interface.
 * 
 * The implementation has an underlying command that can be executed controlled inside a junit
 * test.
 *
 * @param <V> The type for callable parameters
 */
public class ScheduledFutureMock<V> implements ScheduledFuture<V> {

    public enum ExcecutionType {
        SINGLE,
        FIXED_RATE,
        FIXED_DELAY;
    }

    private final ScheduledExecutorServiceMock executorService;
    private final ExcecutionType executionType;
    private final ScheduledCommand command;
    private final long period;
    private final ISystemTimeProvider systemTimeProvider;

    private final AtomicReference<Thread> executingThreadReference;
    private final AtomicBoolean offered;
    private final AtomicBoolean canceled;
    private final AtomicBoolean done;

    private long executionTime;

    ScheduledFutureMock(
        final ScheduledExecutorServiceMock executorService,
        final ExcecutionType executionType,
        final Callable<V> callable,
        final long initialDelay,
        final long period,
        final TimeUnit timeUnit,
        final ISystemTimeProvider systemTimeProvider) {

        Assert.paramNotNull(executorService, "executorService");
        Assert.paramNotNull(executionType, "executionType");
        Assert.paramNotNull(callable, "callable");
        Assert.paramNotNull(timeUnit, "timeUnit");
        Assert.paramNotNull(systemTimeProvider, "systemTimeProvider");

        this.executorService = executorService;
        this.executionType = executionType;
        this.command = new ScheduledCommand(callable, ExcecutionType.SINGLE.equals(executionType));

        this.period = TimeUnit.MILLISECONDS.convert(period, timeUnit);
        this.systemTimeProvider = systemTimeProvider;
        this.executionTime = systemTimeProvider.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(initialDelay, timeUnit);

        this.executingThreadReference = new AtomicReference<Thread>();
        this.offered = new AtomicBoolean(false);
        this.canceled = new AtomicBoolean(false);
        this.done = new AtomicBoolean(false);
    }

    /**
     * Checks if the delay of the future has been expired.
     * 
     * This means that the underlying command is ready to execute or has already been executed
     * 
     * @return True if the delay has been expired, false otherwise
     */
    public boolean isDelayExpired() {
        return getDelay(TimeUnit.MILLISECONDS) <= 0;
    }

    /**
     * Get's the execution type of the underlying command
     * 
     * @return The execution time, never null
     */
    public ExcecutionType getExecutionType() {
        return executionType;
    }

    /**
     * Checks if the execution type is a single execution.
     * 
     * Single execution will only be executed once an will be removed after that
     * 
     * @return True if the execution type is a single execution
     */
    public boolean isSingleExecution() {
        return getExecutionType() == ExcecutionType.SINGLE;
    }

    /**
     * Checks if this future is offered to someone for execution but execution has not yet been finished
     * 
     * @return True if offered, false otherwise
     */
    public boolean isOffered() {
        return offered.get();
    }

    /**
     * Executes the underlying command in the current thread
     */
    public void execute() {
        command.run();
    }

    Runnable getCommand() {
        return command;
    }

    /**
     * Ensure that events will not be executed with different thread at the same time.
     * 
     * @return True if event can be offered
     */
    boolean tryToOfferEvent() {
        return offered.compareAndSet(false, true);
    }

    private void prepareNextExecutionTime() {
        executionTime = getNextExecutionTime();
    }

    private long getNextExecutionTime() {
        if (executionType == ExcecutionType.FIXED_RATE) {
            return getNextExecutionTimeForFixedRate();
        }
        else if (executionType == ExcecutionType.FIXED_DELAY) {
            return systemTimeProvider.currentTimeMillis() + period;
        }
        else {
            return executionTime;
        }
    }

    private long getNextExecutionTimeForFixedRate() {
        final long currentTime = systemTimeProvider.currentTimeMillis();
        long result = executionTime + period;
        while (result <= currentTime) {
            result += period;
        }
        return result;
    }

    @Override
    public long getDelay(final TimeUnit unit) {
        Assert.paramNotNull(unit, "unit");
        return unit.convert(getDelayMillis(), TimeUnit.MILLISECONDS);
    }

    private long getDelayMillis() {
        if (isDone()) {
            return 0;
        }
        else {
            return executionTime - systemTimeProvider.currentTimeMillis();
        }
    }

    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        if (!canceled.getAndSet(true) && !done.getAndSet(true)) {
            if (mayInterruptIfRunning) {
                interruptExecutingThread();
            }
            command.cancel();
            synchronized (executorService.getEventsLock()) {
                if (!offered.get()) {
                    executorService.removeEvent(ScheduledFutureMock.this);
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean isCancelled() {
        return canceled.get();
    }

    @Override
    public boolean isDone() {
        return done.get() || isCancelled();
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return command.get();
    }

    @Override
    public V get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return command.get(timeout, unit);
    }

    @Override
    public int compareTo(final Delayed o) {
        if (o == null) {
            return -1;
        }
        return (int) (getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    private void interruptExecutingThread() {
        synchronized (executingThreadReference) {
            final Thread executingThread = executingThreadReference.get();
            if (executingThread != null) {
                executingThread.interrupt();
            }
        }
    }

    private class ScheduledCommand implements Runnable {

        private final Callable<V> callable;
        private final CountDownLatch latch;
        private final boolean oneShot;

        private V result;
        private Exception exception;

        ScheduledCommand(final Callable<V> callable, final boolean oneShot) {
            Assert.paramNotNull(callable, "callable");
            this.callable = callable;
            this.oneShot = oneShot;

            this.latch = new CountDownLatch(1);
        }

        @Override
        public void run() {
            if (!canceled.get() && !done.get()) {
                try {
                    synchronized (executingThreadReference) {
                        executingThreadReference.set(Thread.currentThread());
                    }
                    if (!canceled.get()) {
                        result = callable.call();
                    }
                }
                catch (final Exception e) {
                    exception = e;
                }
                finally {
                    synchronized (executingThreadReference) {
                        executingThreadReference.set(null);
                    }
                    if (oneShot || exception != null) {
                        executorService.removeEvent(ScheduledFutureMock.this);
                        done.set(true);
                        offered.set(false);
                        latch.countDown();
                    }
                    else {
                        prepareNextExecutionTime();
                        offered.set(false);
                    }
                }
            }
            else {
                executorService.removeEvent(ScheduledFutureMock.this);
                done.set(true);
                offered.set(false);
                latch.countDown();
            }
        }

        private void cancel() {
            latch.countDown();
        }

        private V get() throws InterruptedException, ExecutionException {
            try {
                return getResult(null, null);
            }
            catch (final TimeoutException e) {
                throw new RuntimeException("If this exception was throws there seems to be a bug", e);
            }
        }

        private V get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return getResult(Long.valueOf(timeout), unit);
        }

        private V getResult(final Long timeout, final TimeUnit unit)
                throws InterruptedException, ExecutionException, TimeoutException {

            throwExceptionIfNecessary();
            awaitTermination(timeout, unit);
            throwExceptionIfNecessary();

            return result;
        }

        private void awaitTermination(final Long timeout, final TimeUnit unit) throws InterruptedException, TimeoutException {
            if (timeout != null) {
                if (!latch.await(timeout.longValue(), unit)) {
                    throw new TimeoutException();
                }
            }
            else {
                latch.await();
            }
        }

        private void throwExceptionIfNecessary() throws CancellationException, InterruptedException, ExecutionException {
            if (canceled.get()) {
                throw new CancellationException();
            }
            else if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            else if (exception != null) {
                throw new ExecutionException(exception);
            }
        }

    }

}
