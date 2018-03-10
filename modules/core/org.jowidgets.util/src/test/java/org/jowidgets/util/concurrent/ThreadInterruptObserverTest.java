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

import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.jowidgets.util.IExceptionHandler;
import org.jowidgets.util.mock.ScheduledExecutorServiceMock;
import org.jowidgets.util.mock.SystemTimeProviderMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import junit.framework.Assert;

public class ThreadInterruptObserverTest {

    private static final long DEFAULT_PERIOD = 1000;

    private SystemTimeProviderMock timeProvider;
    private ScheduledExecutorServiceMock executor;
    private IExceptionHandler exceptionHandler;
    private ThreadInterruptObserver observer;

    @Before
    public void setUp() {
        timeProvider = new SystemTimeProviderMock();
        executor = new ScheduledExecutorServiceMock(timeProvider);
        exceptionHandler = Mockito.mock(IExceptionHandler.class);
        observer = new ThreadInterruptObserver(executor, exceptionHandler, DEFAULT_PERIOD);
        observer.start();
        Assert.assertTrue(observer.isRunning());
    }

    @After
    public void tearDown() {
        observer.stop();
        executor.executeEvents();
        Assert.assertEquals(0, executor.getEvents().size());
        Assert.assertFalse(observer.isRunning());
    }

    @Test
    public void testAddAndRemoveInterruptListener() {
        Assert.assertFalse(observer.isCurrentThreadObserved());

        final IThreadInterruptListener listener1 = Mockito.mock(IThreadInterruptListener.class);
        final IThreadInterruptListener listener2 = Mockito.mock(IThreadInterruptListener.class);
        observer.addInterruptListener(listener1);
        Assert.assertTrue(observer.isCurrentThreadObserved());
        observer.addInterruptListener(listener2);
        Assert.assertTrue(observer.isCurrentThreadObserved());

        int interruptedCount = 0;

        for (int i = 0; i < 5; i++) {
            Thread.currentThread().interrupt();
            interruptedCount++;
            executor.executeEvents();
            Mockito.verify(listener1, Mockito.times(interruptedCount)).interrupted(Thread.currentThread());
            Mockito.verify(listener2, Mockito.times(interruptedCount)).interrupted(Thread.currentThread());
            Assert.assertTrue(Thread.interrupted());
            Assert.assertFalse(Thread.currentThread().isInterrupted());
            timeProvider.elapsedMillis(DEFAULT_PERIOD);
        }

        final int listener1InterruptedCount = interruptedCount;
        Assert.assertTrue(observer.removeInterruptListener(listener1));
        Assert.assertTrue(observer.isCurrentThreadObserved());
        Assert.assertFalse(observer.removeInterruptListener(listener1));

        for (int i = 0; i < 5; i++) {
            Thread.currentThread().interrupt();
            interruptedCount++;
            executor.executeEvents();
            Mockito.verify(listener1, Mockito.times(listener1InterruptedCount)).interrupted(Thread.currentThread());
            Mockito.verify(listener2, Mockito.times(interruptedCount)).interrupted(Thread.currentThread());
            Assert.assertTrue(Thread.interrupted());
            Assert.assertFalse(Thread.currentThread().isInterrupted());
            timeProvider.elapsedMillis(DEFAULT_PERIOD);
        }

        final int listener2InterruptedCount = interruptedCount;
        Assert.assertTrue(observer.removeInterruptListener(listener2));
        Assert.assertFalse(observer.isCurrentThreadObserved());
        Assert.assertFalse(observer.removeInterruptListener(listener2));

        for (int i = 0; i < 5; i++) {
            Thread.currentThread().interrupt();
            interruptedCount++;
            executor.executeEvents();
            Mockito.verify(listener1, Mockito.times(listener1InterruptedCount)).interrupted(Thread.currentThread());
            Mockito.verify(listener2, Mockito.times(listener2InterruptedCount)).interrupted(Thread.currentThread());
            Assert.assertTrue(Thread.interrupted());
            Assert.assertFalse(Thread.currentThread().isInterrupted());
            timeProvider.elapsedMillis(DEFAULT_PERIOD);
        }

        Mockito.verify(exceptionHandler, Mockito.never()).handleException(Mockito.any(Throwable.class));
    }

    @Test
    public void testHandleExceptions() {

        final RuntimeException expectedException = new RuntimeException("Foo has happended");

        final IThreadInterruptListener listener1 = new IThreadInterruptListener() {
            @Override
            public void interrupted(final Thread thread) {
                throw expectedException;
            }
        };
        final IThreadInterruptListener listener2 = Mockito.mock(IThreadInterruptListener.class);
        observer.addInterruptListener(listener1);
        observer.addInterruptListener(listener2);

        int interruptedCount = 0;
        for (int i = 0; i < 5; i++) {
            Thread.currentThread().interrupt();
            interruptedCount++;
            executor.executeEvents();
            Mockito.verify(exceptionHandler, Mockito.times(interruptedCount)).handleException(expectedException);
            Mockito.verify(listener2, Mockito.times(interruptedCount)).interrupted(Thread.currentThread());
            Assert.assertTrue(Thread.interrupted());
            Assert.assertFalse(Thread.currentThread().isInterrupted());
            timeProvider.elapsedMillis(DEFAULT_PERIOD);
        }
    }

    @Test
    public void testRemoveListenerOnInterrupt() {
        Assert.assertFalse(observer.isCurrentThreadObserved());

        final AtomicInteger listener1Invocations = new AtomicInteger(0);
        final IThreadInterruptListener listener1 = new IThreadInterruptListener() {
            @Override
            public void interrupted(final Thread thread) {
                listener1Invocations.incrementAndGet();
                observer.removeInterruptListener(thread, this);
            }
        };
        final IThreadInterruptListener listener2 = Mockito.mock(IThreadInterruptListener.class);
        observer.addInterruptListener(listener1);
        observer.addInterruptListener(listener2);

        int interruptedCount = 0;
        for (int i = 0; i < 5; i++) {
            Thread.currentThread().interrupt();
            interruptedCount++;
            executor.executeEvents();
            Assert.assertEquals(1, listener1Invocations.get());
            Mockito.verify(listener2, Mockito.times(interruptedCount)).interrupted(Thread.currentThread());
            Assert.assertTrue(Thread.interrupted());
            Assert.assertFalse(Thread.currentThread().isInterrupted());
            timeProvider.elapsedMillis(DEFAULT_PERIOD);
        }
    }

    @Test
    public void testRemoveListenersOnInterrupt() {
        Assert.assertFalse(observer.isCurrentThreadObserved());

        final IThreadInterruptListener listener1 = Mockito.mock(IThreadInterruptListener.class);

        final AtomicInteger listener2Invocations = new AtomicInteger(0);
        final IThreadInterruptListener listener2 = new IThreadInterruptListener() {
            @Override
            public void interrupted(final Thread thread) {
                listener2Invocations.incrementAndGet();
                observer.removeInterruptListener(thread, this);
                observer.removeInterruptListener(thread, listener1);
            }
        };

        observer.addInterruptListener(listener2);
        observer.addInterruptListener(listener1);

        for (int i = 0; i < 5; i++) {
            Thread.currentThread().interrupt();
            executor.executeEvents();
            Mockito.verify(listener1, Mockito.times(1)).interrupted(Thread.currentThread());
            Assert.assertEquals(1, listener2Invocations.get());
            Assert.assertTrue(Thread.interrupted());
            Assert.assertFalse(Thread.currentThread().isInterrupted());
            timeProvider.elapsedMillis(DEFAULT_PERIOD);
        }
    }

    @Test
    public void testCancelQueries() throws InterruptedException {

        final QueryInvoker queryInvoker1 = new QueryInvoker(observer);
        final QueryInvoker queryInvoker2 = new QueryInvoker(observer);
        final QueryInvoker queryInvoker3 = new QueryInvoker(observer);

        final Thread queryThread1 = new Thread(queryInvoker1, "Query thread 1");
        final Thread queryThread2 = new Thread(queryInvoker2, "Query thread 2");
        final Thread queryThread3 = new Thread(queryInvoker3, "Query thread 3");

        final Thread executorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    timeProvider.elapsedMillis(DEFAULT_PERIOD / 100);
                    executor.executeEvents();
                }
                observer.stop();
                executor.executeEvents();
            }
        }, "Executor thread");
        executorThread.start();

        queryThread1.start();
        queryThread2.start();
        queryThread3.start();

        queryInvoker1.awaitMinLoops();
        verifyQueryInvokerBeforeInterrupt(queryInvoker1);

        queryInvoker2.awaitMinLoops();
        verifyQueryInvokerBeforeInterrupt(queryInvoker2);

        queryInvoker3.awaitMinLoops();
        verifyQueryInvokerBeforeInterrupt(queryInvoker3);

        queryThread1.interrupt();
        queryThread2.interrupt();
        queryThread3.interrupt();

        queryThread1.join();
        verifyQueryInvokerAfterThreadFinished(queryInvoker1);

        queryThread2.join();
        verifyQueryInvokerAfterThreadFinished(queryInvoker1);

        queryThread3.join();
        verifyQueryInvokerAfterThreadFinished(queryInvoker1);

        executorThread.interrupt();
        executorThread.join();

        Mockito.verify(exceptionHandler, Mockito.never()).handleException(Mockito.any(Throwable.class));

    }

    private void verifyQueryInvokerBeforeInterrupt(final QueryInvoker queryInvoker) {
        final Thread queryThread = queryInvoker.getQueryThread();
        Assert.assertTrue(queryThread.isAlive());
        Assert.assertTrue(observer.isThreadObserved(queryThread));
        Assert.assertEquals(0, queryInvoker.getCanceledCount());
        Assert.assertEquals(1, queryInvoker.getInvocationCount());
        Mockito.verify(queryInvoker.getInterruptListener(), Mockito.never()).interrupted(queryThread);
        Assert.assertNull(queryInvoker.getInterruptedFlagAfterQuery());
    }

    private void verifyQueryInvokerAfterThreadFinished(final QueryInvoker queryInvoker) {
        final Thread queryThread = queryInvoker.getQueryThread();
        Assert.assertFalse(queryThread.isAlive());
        Assert.assertFalse(observer.isThreadObserved(queryThread));
        Assert.assertEquals(1, queryInvoker.getCanceledCount());
        Assert.assertEquals(1, queryInvoker.getInvocationCount());
        Mockito.verify(queryInvoker.getInterruptListener(), Mockito.times(1)).interrupted(queryThread);
        Assert.assertNotNull(queryInvoker.getInterruptedFlagAfterQuery());
        Assert.assertFalse(queryInvoker.getInterruptedFlagAfterQuery().booleanValue());
    }

    /**
     * A invoker for {@link CancelableQueryWithInfiniteRuntime} queries that should be canceled
     * when the executing thread was interrupted with help of the {@link ThreadInterruptObserver}.
     */
    private class QueryInvoker implements Runnable {

        private final ThreadInterruptObserver observer;
        private final IThreadInterruptListener listenerMock;
        private final CancelableQueryWithInfiniteRuntime query;
        private final AtomicInteger invocationCount;

        private Thread queryThread;
        private Boolean interruptedFlagAfterQuery;

        QueryInvoker(final ThreadInterruptObserver observer) {
            this.observer = observer;
            this.listenerMock = Mockito.mock(IThreadInterruptListener.class);
            this.invocationCount = new AtomicInteger(0);
            this.query = new CancelableQueryWithInfiniteRuntime();
        }

        @Override
        public void run() {
            invocationCount.incrementAndGet();
            queryThread = Thread.currentThread();
            final IThreadInterruptListener decoratedListener = new IThreadInterruptListener() {
                @Override
                public void interrupted(final Thread queryThread) {
                    query.cancel();
                    listenerMock.interrupted(queryThread);

                    //if listener will not be removed here, the test fails
                    //because cancel will be invoked more than once.
                    observer.removeInterruptListener(queryThread, this);
                }
            };
            observer.addInterruptListener(decoratedListener);
            try {
                query.getResult();
            }
            catch (final CancellationException e) {
                //expected because all queries will be canceled in this test
            }
            finally {
                //clear the interrupted flag for this thread
                Thread.interrupted();

                //save the interrupted flag before thread finishes
                interruptedFlagAfterQuery = Boolean.valueOf(Thread.currentThread().isInterrupted());

                //remove the interrupt listener
                //Remark: This is not necessary in this test but for queries that will
                //not be canceled
                observer.removeInterruptListener(decoratedListener);
            }
        }

        IThreadInterruptListener getInterruptListener() {
            return listenerMock;
        }

        Thread getQueryThread() {
            return queryThread;
        }

        public Boolean getInterruptedFlagAfterQuery() {
            return interruptedFlagAfterQuery;
        }

        int getInvocationCount() {
            return invocationCount.get();
        }

        int getCanceledCount() {
            return query.getCanceledCount();
        }

        void awaitMinLoops() throws InterruptedException {
            query.awaitMinLoops();
        }

    }

    /**
     * Simulate a query that does not handle the interrupted flag correctly but
     * allows to be canceled from a separate thread.
     * 
     * This is a similar behavior that hibernate queries have.
     */
    private class CancelableQueryWithInfiniteRuntime {

        private final CountDownLatch minLoopCount = new CountDownLatch(10);
        private final AtomicBoolean canceled = new AtomicBoolean(false);
        private final AtomicInteger canceledCount = new AtomicInteger(0);

        String getResult() {
            while (!canceled.get()) {
                minLoopCount.countDown();
                //do nothing
            }

            //simulate cancel by doing some work without interrupted exception will be thrown.
            //If interrupt listener will not be removed on interrupt (observer.removeInterruptListener(queryThread, this))
            //this work is necessary to fail the test because cancel will be invoked more than once while this
            //loop runs
            for (int i = 0; i < 50000; i++) {
                Math.pow(2.42, 1.4711);
            }
            throw new CancellationException();
        }

        void cancel() {
            canceledCount.incrementAndGet();
            canceled.compareAndSet(false, true);
        }

        int getCanceledCount() {
            return canceledCount.get();
        }

        void awaitMinLoops() throws InterruptedException {
            minLoopCount.await();
        }

    }

}
