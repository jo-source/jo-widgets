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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.mockito.Mockito;

import junit.framework.Assert;

public class ScheduledExecutorServiceMockTest {

    //Use this timeout to ensure tests terminate on the one hand but on the other hand give enough time for
    //termination when thinking very pessimistic with respect to hardware performance. 
    private static final long TIMEOUT = 30 * 1000;

    @Test
    public void testExecute() {
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final Runnable runnable = Mockito.mock(Runnable.class);
        executor.execute(runnable);

        Mockito.verify(runnable, Mockito.never()).run();
        Assert.assertEquals(1, executor.getEvents().size());

        executor.executeEvents();
        Mockito.verify(runnable, Mockito.times(1)).run();
        Assert.assertEquals(0, executor.getEvents().size());

        executor.executeEvents();
        Mockito.verify(runnable, Mockito.times(1)).run();
        Assert.assertEquals(0, executor.getEvents().size());
    }

    @Test
    public void testExecuteInThread() {
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final CountDownLatch latch = new CountDownLatch(1);
        final Runnable runnable = Mockito.mock(Runnable.class);
        executor.execute(runnable);

        Mockito.verify(runnable, Mockito.never()).run();
        Assert.assertEquals(1, executor.getEvents().size());

        new Thread(new Runnable() {
            @Override
            public void run() {
                executor.executeEvents();
                latch.countDown();
            }
        }).start();

        try {
            final boolean terminated = latch.await(TIMEOUT, TimeUnit.MILLISECONDS);
            Assert.assertTrue(terminated);
            Mockito.verify(runnable, Mockito.times(1)).run();
            Assert.assertEquals(0, executor.getEvents().size());
        }
        catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSubmitRunnable() {
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final Runnable runnable = Mockito.mock(Runnable.class);
        final ScheduledFutureMock<Void> futureResult = executor.submit(runnable);

        Mockito.verify(runnable, Mockito.never()).run();
        Assert.assertEquals(1, executor.getEvents().size());

        executor.executeEvents();

        try {
            Assert.assertNull(futureResult.get(TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertTrue(futureResult.isDone());
            Assert.assertFalse(futureResult.isCancelled());
            Mockito.verify(runnable, Mockito.times(1)).run();
            Assert.assertEquals(0, executor.getEvents().size());
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSubmitRunnableInThread() {
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final Runnable runnable = Mockito.mock(Runnable.class);
        final ScheduledFutureMock<Void> futureResult = executor.submit(runnable);

        Mockito.verify(runnable, Mockito.never()).run();
        Assert.assertEquals(1, executor.getEvents().size());

        new Thread(new Runnable() {
            @Override
            public void run() {
                executor.executeEvents();
            }
        }).start();

        try {
            Assert.assertNull(futureResult.get(TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertTrue(futureResult.isDone());
            Assert.assertFalse(futureResult.isCancelled());
            Mockito.verify(runnable, Mockito.times(1)).run();
            Assert.assertEquals(0, executor.getEvents().size());
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSubmitCallable() {
        final String result = "FOO_RESULT";

        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final CallableMock<String> callable = new CallableMock<String>(result);
        final ScheduledFutureMock<String> futureResult = executor.submit(callable);
        Assert.assertEquals(0, callable.getInvocationCount());
        Assert.assertEquals(1, executor.getEvents().size());

        executor.executeEvents();

        try {
            Assert.assertEquals(result, futureResult.get(TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertTrue(futureResult.isDone());
            Assert.assertFalse(futureResult.isCancelled());
            Assert.assertEquals(1, callable.getInvocationCount());
            Assert.assertEquals(0, executor.getEvents().size());
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSubmitCallableInThread() {
        final String result = "FOO_RESULT";

        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final CallableMock<String> callable = new CallableMock<String>(result);
        final ScheduledFutureMock<String> futureResult = executor.submit(callable);
        Assert.assertEquals(0, callable.getInvocationCount());
        Assert.assertEquals(1, executor.getEvents().size());

        new Thread(new Runnable() {
            @Override
            public void run() {
                executor.executeEvents();
            }
        }).start();

        try {
            Assert.assertEquals(result, futureResult.get(TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertTrue(futureResult.isDone());
            Assert.assertFalse(futureResult.isCancelled());
            Assert.assertEquals(1, callable.getInvocationCount());
            Assert.assertEquals(0, executor.getEvents().size());
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSubmitRunnableWithResult() {
        final String result = "FOO_RESULT";

        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final Runnable runnable = Mockito.mock(Runnable.class);

        final ScheduledFutureMock<String> futureResult = executor.submit(runnable, result);
        Assert.assertEquals(1, executor.getEvents().size());

        executor.executeEvents();

        try {
            Assert.assertEquals(result, futureResult.get(TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertTrue(futureResult.isDone());
            Assert.assertFalse(futureResult.isCancelled());
            Assert.assertEquals(0, executor.getEvents().size());
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSubmitRunnableWithResultInThread() {
        final String result = "FOO_RESULT";

        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final Runnable runnable = Mockito.mock(Runnable.class);

        final ScheduledFutureMock<String> futureResult = executor.submit(runnable, result);
        Assert.assertEquals(1, executor.getEvents().size());

        new Thread(new Runnable() {
            @Override
            public void run() {
                executor.executeEvents();
            }
        }).start();

        try {
            Assert.assertEquals(result, futureResult.get(TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertTrue(futureResult.isDone());
            Assert.assertFalse(futureResult.isCancelled());
            Assert.assertEquals(0, executor.getEvents().size());
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testScheduleRunnable() {
        final long delay = 1000;

        final SystemTimeProviderMock timeProvider = new SystemTimeProviderMock();
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock(timeProvider);

        final Runnable runnable = Mockito.mock(Runnable.class);
        final ScheduledFutureMock<Void> futureResult = executor.schedule(runnable, delay, TimeUnit.MILLISECONDS);

        Mockito.verify(runnable, Mockito.never()).run();
        Assert.assertEquals(1, executor.getEvents().size());

        executor.executeEvents();
        Mockito.verify(runnable, Mockito.never()).run();
        Assert.assertEquals(1, executor.getEvents().size());

        timeProvider.elapsedMillis(delay / 2);
        executor.executeEvents();
        Mockito.verify(runnable, Mockito.never()).run();
        Assert.assertEquals(1, executor.getEvents().size());

        timeProvider.elapsedMillis(delay / 2);
        executor.executeEvents();
        try {
            Assert.assertNull(futureResult.get(TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertTrue(futureResult.isDone());
            Assert.assertFalse(futureResult.isCancelled());
            Mockito.verify(runnable, Mockito.times(1)).run();
            Assert.assertEquals(0, executor.getEvents().size());
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testScheduleRunnableInThread() {
        final long delay = 1000;

        final SystemTimeProviderMock timeProvider = new SystemTimeProviderMock();
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock(timeProvider);

        final Runnable runnable = Mockito.mock(Runnable.class);
        final ScheduledFutureMock<Void> futureResult = executor.schedule(runnable, delay, TimeUnit.MILLISECONDS);

        Mockito.verify(runnable, Mockito.never()).run();
        Assert.assertEquals(1, executor.getEvents().size());

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < delay; i++) {
                    timeProvider.elapsedMillis(1);
                    executor.executeEvents();
                }
            }
        }).start();

        try {
            Assert.assertNull(futureResult.get(TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertTrue(futureResult.isDone());
            Assert.assertFalse(futureResult.isCancelled());
            Mockito.verify(runnable, Mockito.times(1)).run();
            Assert.assertEquals(0, executor.getEvents().size());
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testScheduleCallable() {
        final long delay = 1000;

        final String result = "FOO_RESULT";

        final SystemTimeProviderMock timeProvider = new SystemTimeProviderMock();
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock(timeProvider);

        final CallableMock<String> callable = new CallableMock<String>(result);
        final ScheduledFutureMock<String> futureResult = executor.schedule(callable, delay, TimeUnit.MILLISECONDS);
        Assert.assertEquals(0, callable.getInvocationCount());
        Assert.assertEquals(1, executor.getEvents().size());

        timeProvider.elapsedMillis(delay);
        executor.executeEvents();
        try {
            Assert.assertEquals(result, futureResult.get(TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertTrue(futureResult.isDone());
            Assert.assertFalse(futureResult.isCancelled());
            Assert.assertEquals(1, callable.getInvocationCount());
            Assert.assertEquals(0, executor.getEvents().size());
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testScheduleCallableInThread() {
        final long delay = 1000;

        final String result = "FOO_RESULT";

        final SystemTimeProviderMock timeProvider = new SystemTimeProviderMock();
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock(timeProvider);

        final CallableMock<String> callable = new CallableMock<String>(result);
        final ScheduledFutureMock<String> futureResult = executor.schedule(callable, delay, TimeUnit.MILLISECONDS);
        Assert.assertEquals(0, callable.getInvocationCount());
        Assert.assertEquals(1, executor.getEvents().size());

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < delay; i++) {
                    timeProvider.elapsedMillis(1);
                    executor.executeEvents();
                }
            }
        }).start();

        try {
            Assert.assertEquals(result, futureResult.get(TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertTrue(futureResult.isDone());
            Assert.assertFalse(futureResult.isCancelled());
            Assert.assertEquals(1, callable.getInvocationCount());
            Assert.assertEquals(0, executor.getEvents().size());
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testScheduleMultipleCallables() throws TimeoutException, ExecutionException, InterruptedException {

        final long period = 200;

        final long delay1 = 600;
        final long delay2 = 200;
        final long delay3 = 400;

        final String result1 = "FOO_RESULT_1";
        final String result2 = "FOO_RESULT_2";
        final String result3 = "FOO_RESULT_3";

        final SystemTimeProviderMock timeProvider = new SystemTimeProviderMock();
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock(timeProvider);

        final CallableMock<String> callable1 = new CallableMock<String>(result1);
        final CallableMock<String> callable2 = new CallableMock<String>(result2);
        final CallableMock<String> callable3 = new CallableMock<String>(result3);

        final ScheduledFutureMock<String> futureResult1 = executor.schedule(callable1, delay1, TimeUnit.MILLISECONDS);
        final ScheduledFutureMock<String> futureResult2 = executor.schedule(callable2, delay2, TimeUnit.MILLISECONDS);
        final ScheduledFutureMock<String> futureResult3 = executor.schedule(callable3, delay3, TimeUnit.MILLISECONDS);

        executor.executeEvents();
        Assert.assertEquals(0, callable1.getInvocationCount());
        Assert.assertEquals(0, callable2.getInvocationCount());
        Assert.assertEquals(0, callable3.getInvocationCount());
        Assert.assertEquals(3, executor.getEvents().size());
        Assert.assertFalse(futureResult1.isDone());
        Assert.assertFalse(futureResult1.isCancelled());
        Assert.assertFalse(futureResult2.isDone());
        Assert.assertFalse(futureResult2.isCancelled());
        Assert.assertFalse(futureResult3.isDone());
        Assert.assertFalse(futureResult3.isCancelled());

        timeProvider.elapsedMillis(period);
        executor.executeEvents();
        Assert.assertEquals(result2, futureResult2.get(TIMEOUT, TimeUnit.MILLISECONDS));
        Assert.assertEquals(0, callable1.getInvocationCount());
        Assert.assertEquals(1, callable2.getInvocationCount());
        Assert.assertEquals(0, callable3.getInvocationCount());
        Assert.assertEquals(2, executor.getEvents().size());
        Assert.assertFalse(futureResult1.isDone());
        Assert.assertFalse(futureResult1.isCancelled());
        Assert.assertTrue(futureResult2.isDone());
        Assert.assertFalse(futureResult2.isCancelled());
        Assert.assertFalse(futureResult3.isDone());
        Assert.assertFalse(futureResult3.isCancelled());

        timeProvider.elapsedMillis(period);
        executor.executeEvents();
        Assert.assertEquals(result3, futureResult3.get(TIMEOUT, TimeUnit.MILLISECONDS));
        Assert.assertEquals(0, callable1.getInvocationCount());
        Assert.assertEquals(1, callable2.getInvocationCount());
        Assert.assertEquals(1, callable3.getInvocationCount());
        Assert.assertEquals(1, executor.getEvents().size());
        Assert.assertFalse(futureResult1.isDone());
        Assert.assertFalse(futureResult1.isCancelled());
        Assert.assertTrue(futureResult2.isDone());
        Assert.assertFalse(futureResult2.isCancelled());
        Assert.assertTrue(futureResult3.isDone());
        Assert.assertFalse(futureResult3.isCancelled());

        timeProvider.elapsedMillis(period);
        executor.executeEvents();
        Assert.assertEquals(result1, futureResult1.get(TIMEOUT, TimeUnit.MILLISECONDS));
        Assert.assertEquals(1, callable1.getInvocationCount());
        Assert.assertEquals(1, callable2.getInvocationCount());
        Assert.assertEquals(1, callable3.getInvocationCount());
        Assert.assertEquals(0, executor.getEvents().size());
        Assert.assertTrue(futureResult1.isDone());
        Assert.assertFalse(futureResult1.isCancelled());
        Assert.assertTrue(futureResult2.isDone());
        Assert.assertFalse(futureResult2.isCancelled());
        Assert.assertTrue(futureResult3.isDone());
        Assert.assertFalse(futureResult3.isCancelled());
    }

    @Test
    public void testScheduleMultipleCallablesInThread() {

        final long delay1 = 600;
        final long delay2 = 200;
        final long delay3 = 400;

        final String result1 = "FOO_RESULT_1";
        final String result2 = "FOO_RESULT_2";
        final String result3 = "FOO_RESULT_3";

        final SystemTimeProviderMock timeProvider = new SystemTimeProviderMock();
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock(timeProvider);

        final CallableMock<String> callable1 = new CallableMock<String>(result1);
        final CallableMock<String> callable2 = new CallableMock<String>(result2);
        final CallableMock<String> callable3 = new CallableMock<String>(result3);

        final ScheduledFutureMock<String> futureResult1 = executor.schedule(callable1, delay1, TimeUnit.MILLISECONDS);
        final ScheduledFutureMock<String> futureResult2 = executor.schedule(callable2, delay2, TimeUnit.MILLISECONDS);
        final ScheduledFutureMock<String> futureResult3 = executor.schedule(callable3, delay3, TimeUnit.MILLISECONDS);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < delay1; i++) {
                    timeProvider.elapsedMillis(1);
                    executor.executeEvents();
                }
            }
        }).start();

        try {
            Assert.assertEquals(result1, futureResult1.get(TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertTrue(futureResult1.isDone());
            Assert.assertFalse(futureResult1.isCancelled());

            Assert.assertEquals(result2, futureResult2.get(TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertTrue(futureResult2.isDone());
            Assert.assertFalse(futureResult2.isCancelled());

            Assert.assertEquals(result3, futureResult3.get(TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertTrue(futureResult3.isDone());
            Assert.assertFalse(futureResult3.isCancelled());

            Assert.assertEquals(0, executor.getEvents().size());
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test(expected = ExecutionException.class)
    public void testScheduleCallableWithExecutionException() throws ExecutionException, InterruptedException, TimeoutException {
        final long delay = 1000;

        final Exception expectedCause = new RuntimeException("Foo has happened");

        final SystemTimeProviderMock timeProvider = new SystemTimeProviderMock();
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock(timeProvider);

        final Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                throw expectedCause;
            }
        };
        final ScheduledFutureMock<String> futureResult = executor.schedule(callable, delay, TimeUnit.MILLISECONDS);
        timeProvider.elapsedMillis(delay);
        executor.executeEvents();

        try {
            futureResult.get(TIMEOUT, TimeUnit.MILLISECONDS);
        }
        catch (final ExecutionException e) {
            Assert.assertEquals(expectedCause, e.getCause());
            Assert.assertTrue(futureResult.isDone());
            Assert.assertFalse(futureResult.isCancelled());
            throw e;
        }
    }

    @Test(expected = ExecutionException.class)
    public void testScheduleCallableWithExecutionExceptionInThread()
            throws ExecutionException, InterruptedException, TimeoutException {
        final long delay = 1000;

        final Exception expectedCause = new RuntimeException("Foo has happened");

        final SystemTimeProviderMock timeProvider = new SystemTimeProviderMock();
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock(timeProvider);

        final Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                throw expectedCause;
            }
        };
        final ScheduledFutureMock<String> futureResult = executor.schedule(callable, delay, TimeUnit.MILLISECONDS);

        new Thread(new Runnable() {
            @Override
            public void run() {
                timeProvider.elapsedMillis(delay);
                executor.executeEvents();
            }
        }).start();

        try {
            futureResult.get(TIMEOUT, TimeUnit.MILLISECONDS);
        }
        catch (final ExecutionException e) {
            Assert.assertEquals(expectedCause, e.getCause());
            Assert.assertTrue(futureResult.isDone());
            Assert.assertFalse(futureResult.isCancelled());
            throw e;
        }
    }

    @Test(expected = TimeoutException.class)
    public void testScheduleCallableWithTimeoutExceptionInThread()
            throws ExecutionException, InterruptedException, TimeoutException {
        final long delay = 1000;

        final String result = "RESULT";

        final SystemTimeProviderMock timeProvider = new SystemTimeProviderMock();
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock(timeProvider);

        final AtomicBoolean running = new AtomicBoolean(true);
        final Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                while (running.get()) {
                    Thread.sleep(100);
                }
                return result;
            }
        };
        final ScheduledFutureMock<String> futureResult = executor.schedule(callable, delay, TimeUnit.MILLISECONDS);

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                timeProvider.elapsedMillis(delay);
                executor.executeEvents();
            }
        });
        thread.start();

        try {
            futureResult.get(1, TimeUnit.MILLISECONDS);
        }
        catch (final TimeoutException e) {
            running.set(false);
            throw e;
        }
        finally {
            thread.join();
            Assert.assertEquals(result, futureResult.get(TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertTrue(futureResult.isDone());
            Assert.assertFalse(futureResult.isCancelled());
        }
    }

    @Test(expected = CancellationException.class)
    public void testScheduleCallableWithCancelInThread() throws ExecutionException, InterruptedException, TimeoutException {
        final long delay = 1000;

        final SystemTimeProviderMock timeProvider = new SystemTimeProviderMock();
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock(timeProvider);

        final CountDownLatch startedLatch = new CountDownLatch(1);
        final Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                while (true) {
                    startedLatch.countDown();
                    try {
                        Thread.sleep(100);
                    }
                    catch (final InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                return null;

            }
        };
        final ScheduledFutureMock<String> futureResult = executor.schedule(callable, delay, TimeUnit.MILLISECONDS);

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                timeProvider.elapsedMillis(delay);
                executor.executeEvents();
            }
        });
        thread.start();

        startedLatch.await();

        futureResult.cancel(true);
        thread.join();
        Assert.assertFalse(thread.isAlive());

        //this throws cancellation exception
        try {
            futureResult.get(TIMEOUT, TimeUnit.MILLISECONDS);
        }
        finally {
            Assert.assertTrue(futureResult.isDone());
            Assert.assertTrue(futureResult.isCancelled());
        }
    }

    @Test(expected = CancellationException.class)
    public void testScheduleAtFixedRate() throws TimeoutException, InterruptedException, ExecutionException {

        final long initialDelay = 1000;
        final long period = 200;
        final long runDuration = 20;

        final SystemTimeProviderMock timeProvider = new SystemTimeProviderMock();
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock(timeProvider);

        final Runnable runnable = Mockito.mock(Runnable.class);
        final Runnable timeConsumingRunnable = new Runnable() {
            @Override
            public void run() {
                timeProvider.elapsedMillis(runDuration);
                runnable.run();
            }
        };
        final ScheduledFutureMock<Void> futureResult = executor
                .scheduleAtFixedRate(timeConsumingRunnable, initialDelay, period, TimeUnit.MILLISECONDS);

        Mockito.verify(runnable, Mockito.never()).run();
        Assert.assertEquals(1, executor.getEvents().size());

        executor.executeEvents();
        Mockito.verify(runnable, Mockito.never()).run();
        Assert.assertEquals(1, executor.getEvents().size());

        int expectedInvocations = 0;

        timeProvider.elapsedMillis(initialDelay / 2);
        executor.executeEvents();
        Mockito.verify(runnable, Mockito.times(expectedInvocations)).run();
        Assert.assertEquals(1, executor.getEvents().size());

        timeProvider.elapsedMillis(initialDelay / 2);
        executor.executeEvents();
        Mockito.verify(runnable, Mockito.times(++expectedInvocations)).run();
        Assert.assertEquals(1, executor.getEvents().size());

        for (int i = 0; i < 5; i++) {
            timeProvider.elapsedMillis((period - runDuration) / 2);
            executor.executeEvents();
            Mockito.verify(runnable, Mockito.times(expectedInvocations)).run();
            Assert.assertEquals(1, executor.getEvents().size());

            timeProvider.elapsedMillis((period - runDuration) / 2);
            executor.executeEvents();
            Mockito.verify(runnable, Mockito.times(++expectedInvocations)).run();
            Assert.assertEquals(1, executor.getEvents().size());
        }

        futureResult.cancel(false);

        try {
            futureResult.get(TIMEOUT, TimeUnit.MILLISECONDS);
        }
        finally {
            Assert.assertEquals(0, executor.getEvents().size());
        }
    }

    @Test(expected = CancellationException.class)
    public void testScheduleAtFixedRateInThread() throws TimeoutException, InterruptedException, ExecutionException {

        final long initialDelay = 1000;
        final long period = 200;
        final long runDuration = 20;

        final long duration = 10000;

        final SystemTimeProviderMock timeProvider = new SystemTimeProviderMock();
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock(timeProvider);

        final Runnable runnable = Mockito.mock(Runnable.class);
        final Runnable timeConsumingRunnable = new Runnable() {
            @Override
            public void run() {
                timeProvider.elapsedMillis(runDuration);
                runnable.run();
            }
        };
        final ScheduledFutureMock<Void> futureResult = executor
                .scheduleAtFixedRate(timeConsumingRunnable, initialDelay, period, TimeUnit.MILLISECONDS);

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < duration; i = i + 20) {
                    timeProvider.elapsedMillis(20);
                    executor.executeEvents();
                }
            }
        });
        thread.start();
        thread.join();

        final int expectedInvocations = (int) ((duration - initialDelay) / (period - runDuration)) + 1;
        Mockito.verify(runnable, Mockito.times(expectedInvocations)).run();

        futureResult.cancel(false);

        try {
            futureResult.get(TIMEOUT, TimeUnit.MILLISECONDS);
        }
        finally {
            Assert.assertEquals(0, executor.getEvents().size());
        }
    }

    @Test(expected = CancellationException.class)
    public void testScheduleWithFixedDelay() throws TimeoutException, InterruptedException, ExecutionException {

        final long initialDelay = 1000;
        final long delay = 200;
        final long runDuration = 237;

        final SystemTimeProviderMock timeProvider = new SystemTimeProviderMock();
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock(timeProvider);

        final Runnable runnable = Mockito.mock(Runnable.class);
        final Runnable timeConsumingRunnable = new Runnable() {
            @Override
            public void run() {
                timeProvider.elapsedMillis(runDuration);
                runnable.run();
            }
        };
        final ScheduledFutureMock<Void> futureResult = executor
                .scheduleWithFixedDelay(timeConsumingRunnable, initialDelay, delay, TimeUnit.MILLISECONDS);

        Mockito.verify(runnable, Mockito.never()).run();
        Assert.assertEquals(1, executor.getEvents().size());

        executor.executeEvents();
        Mockito.verify(runnable, Mockito.never()).run();
        Assert.assertEquals(1, executor.getEvents().size());

        int expectedInvocations = 0;

        timeProvider.elapsedMillis(initialDelay / 2);
        executor.executeEvents();
        Mockito.verify(runnable, Mockito.times(expectedInvocations)).run();
        Assert.assertEquals(1, executor.getEvents().size());

        timeProvider.elapsedMillis(initialDelay / 2);
        executor.executeEvents();
        Mockito.verify(runnable, Mockito.times(++expectedInvocations)).run();
        Assert.assertEquals(1, executor.getEvents().size());

        for (int i = 0; i < 5; i++) {
            timeProvider.elapsedMillis(delay / 2);
            executor.executeEvents();
            Mockito.verify(runnable, Mockito.times(expectedInvocations)).run();
            Assert.assertEquals(1, executor.getEvents().size());

            timeProvider.elapsedMillis(delay / 2);
            executor.executeEvents();
            Mockito.verify(runnable, Mockito.times(++expectedInvocations)).run();
            Assert.assertEquals(1, executor.getEvents().size());
        }

        futureResult.cancel(false);

        try {
            futureResult.get(TIMEOUT, TimeUnit.MILLISECONDS);
        }
        finally {
            Assert.assertEquals(0, executor.getEvents().size());
        }
    }

    @Test(expected = ExecutionException.class)
    public void testStopScheduleAtFixedRateOnExceptionInThread()
            throws TimeoutException, InterruptedException, ExecutionException {

        final long initialDelay = 1000;
        final long period = 200;

        final long durationToFail = 10000;

        final RuntimeException expectedCause = new RuntimeException("Foo goes wrong");

        final SystemTimeProviderMock timeProvider = new SystemTimeProviderMock();
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock(timeProvider);

        final Runnable runnable = Mockito.mock(Runnable.class);
        final Runnable decoratedRunnable = new Runnable() {
            @Override
            public void run() {
                if (timeProvider.currentTimeMillis() <= durationToFail) {
                    runnable.run();
                }
                else {
                    throw expectedCause;
                }
            }
        };
        final ScheduledFutureMock<Void> futureResult = executor
                .scheduleAtFixedRate(decoratedRunnable, initialDelay, period, TimeUnit.MILLISECONDS);

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < (durationToFail * 2); i = i + 20) {
                    timeProvider.elapsedMillis(20);
                    executor.executeEvents();
                }
            }
        });
        thread.start();
        thread.join();

        final int expectedInvocations = (int) ((durationToFail - initialDelay) / period) + 1;
        Mockito.verify(runnable, Mockito.times(expectedInvocations)).run();

        try {
            futureResult.get(TIMEOUT, TimeUnit.MILLISECONDS);
        }
        catch (final ExecutionException e) {
            Assert.assertEquals(expectedCause, e.getCause());
            throw e;
        }
        finally {
            Assert.assertEquals(0, executor.getEvents().size());
            Assert.assertTrue(futureResult.isDone());
            Assert.assertFalse(futureResult.isCancelled());
        }
    }

    @Test
    public void testCancelEventsAlreadyOffered() throws InterruptedException {
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final Runnable runnable1 = Mockito.mock(Runnable.class);
        final Runnable runnable2 = Mockito.mock(Runnable.class);

        final ScheduledFutureMock<Void> futureResult1 = executor.submit(runnable1);
        final ScheduledFutureMock<Void> futureResult2 = executor.submit(runnable2);

        Assert.assertEquals(2, executor.getEvents().size());

        final ScheduledFutureMock<?> offeredEvent1 = executor.offerNextEventForExecution();
        final ScheduledFutureMock<?> offeredEvent2 = executor.offerNextEventForExecution();
        Assert.assertEquals(2, executor.getEvents().size());

        Assert.assertEquals(futureResult1, offeredEvent1);
        Assert.assertEquals(futureResult2, offeredEvent2);

        futureResult1.cancel(true);
        Assert.assertTrue(futureResult1.isCancelled());
        Assert.assertTrue(futureResult1.isDone());
        Assert.assertEquals(2, executor.getEvents().size());
        Assert.assertFalse(Thread.currentThread().isInterrupted());

        offeredEvent1.execute();
        Assert.assertEquals(1, executor.getEvents().size());
        Assert.assertFalse(Thread.currentThread().isInterrupted());

        futureResult2.cancel(true);
        Assert.assertTrue(futureResult2.isCancelled());
        Assert.assertTrue(futureResult2.isDone());
        Assert.assertEquals(1, executor.getEvents().size());
        Assert.assertFalse(Thread.currentThread().isInterrupted());

        offeredEvent2.execute();
        Assert.assertEquals(0, executor.getEvents().size());
        Assert.assertFalse(Thread.currentThread().isInterrupted());

        Mockito.verify(runnable1, Mockito.never()).run();
        Mockito.verify(runnable2, Mockito.never()).run();

        executor.shutdown();
        Assert.assertTrue(executor.isShutdown());
        Assert.assertTrue(executor.isTerminated());

        Assert.assertTrue(executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testCancelEventsNotYetOffered() throws InterruptedException {
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final Runnable runnable1 = Mockito.mock(Runnable.class);
        final Runnable runnable2 = Mockito.mock(Runnable.class);

        final ScheduledFutureMock<Void> futureResult1 = executor.submit(runnable1);
        final ScheduledFutureMock<Void> futureResult2 = executor.submit(runnable2);

        Assert.assertEquals(2, executor.getEvents().size());

        futureResult1.cancel(true);
        Assert.assertTrue(futureResult1.isCancelled());
        Assert.assertTrue(futureResult1.isDone());
        Assert.assertEquals(1, executor.getEvents().size());
        Assert.assertFalse(Thread.currentThread().isInterrupted());

        futureResult2.cancel(true);
        Assert.assertTrue(futureResult2.isCancelled());
        Assert.assertTrue(futureResult2.isDone());
        Assert.assertEquals(0, executor.getEvents().size());
        Assert.assertFalse(Thread.currentThread().isInterrupted());

        Mockito.verify(runnable1, Mockito.never()).run();
        Mockito.verify(runnable2, Mockito.never()).run();

        executor.shutdown();
        Assert.assertTrue(executor.isShutdown());
        Assert.assertTrue(executor.isTerminated());
        Assert.assertTrue(executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testShutdownWithNoEvents() throws InterruptedException {
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        Assert.assertFalse(executor.isShutdown());
        Assert.assertFalse(executor.isTerminated());

        executor.shutdown();

        Assert.assertTrue(executor.isShutdown());
        Assert.assertTrue(executor.isTerminated());

        Assert.assertTrue(executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testShutdownWithEvents() throws InterruptedException {
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final Runnable runnable1 = Mockito.mock(Runnable.class);
        final Runnable runnable2 = Mockito.mock(Runnable.class);
        executor.execute(runnable1);
        executor.execute(runnable2);

        Assert.assertEquals(2, executor.getEvents().size());

        executor.shutdown();
        final Runnable runnable3 = Mockito.mock(Runnable.class);
        executor.execute(runnable3);

        Assert.assertEquals(2, executor.getEvents().size());
        Assert.assertTrue(executor.isShutdown());
        Assert.assertFalse(executor.isTerminated());

        executor.executeEvents();
        Mockito.verify(runnable1, Mockito.times(1)).run();
        Mockito.verify(runnable2, Mockito.times(1)).run();
        Mockito.verify(runnable3, Mockito.never()).run();
        Assert.assertEquals(0, executor.getEvents().size());
        Assert.assertTrue(executor.isShutdown());
        Assert.assertTrue(executor.isTerminated());
        Assert.assertTrue(executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testShutdownNowWithNoEvents() throws InterruptedException {
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        Assert.assertFalse(executor.isShutdown());
        Assert.assertFalse(executor.isTerminated());

        executor.shutdownNow();

        Assert.assertTrue(executor.isShutdown());
        Assert.assertTrue(executor.isTerminated());
        Assert.assertTrue(executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testShutdownNowWithEvents() throws InterruptedException {
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final Runnable runnable1 = Mockito.mock(Runnable.class);
        final Runnable runnable2 = Mockito.mock(Runnable.class);
        final ScheduledFutureMock<Void> futureResult1 = executor.submit(runnable1);
        final ScheduledFutureMock<Void> futureResult2 = executor.submit(runnable2);

        Assert.assertEquals(2, executor.getEvents().size());

        executor.shutdownNow();
        final Runnable runnable3 = Mockito.mock(Runnable.class);
        final ScheduledFutureMock<Void> futureResult3 = executor.submit(runnable3);

        Assert.assertEquals(0, executor.getEvents().size());
        Assert.assertTrue(executor.isShutdown());
        Assert.assertTrue(executor.isTerminated());
        Assert.assertTrue(futureResult1.isCancelled());
        Assert.assertTrue(futureResult2.isCancelled());
        Assert.assertFalse(futureResult3.isCancelled());

        executor.executeEvents();
        Mockito.verify(runnable1, Mockito.never()).run();
        Mockito.verify(runnable2, Mockito.never()).run();
        Mockito.verify(runnable3, Mockito.never()).run();
        Assert.assertTrue(executor.isShutdown());
        Assert.assertTrue(executor.isTerminated());
        Assert.assertTrue(executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testShutdownNowWithOfferedEvents() throws InterruptedException {
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final Runnable runnable1 = Mockito.mock(Runnable.class);
        final Runnable runnable2 = Mockito.mock(Runnable.class);
        final ScheduledFutureMock<Void> futureResult1 = executor.submit(runnable1);
        final ScheduledFutureMock<Void> futureResult2 = executor.submit(runnable2);

        Assert.assertEquals(2, executor.getEvents().size());

        final ScheduledFutureMock<?> offeredEvent = executor.offerNextEventForExecution();
        Assert.assertEquals(futureResult1, offeredEvent);
        Assert.assertEquals(2, executor.getEvents().size());

        executor.shutdownNow();

        Assert.assertEquals(1, executor.getEvents().size());
        Assert.assertTrue(executor.isShutdown());
        Assert.assertFalse(executor.isTerminated());
        Assert.assertTrue(futureResult1.isCancelled());
        Assert.assertTrue(futureResult2.isCancelled());

        offeredEvent.execute();
        Assert.assertEquals(0, executor.getEvents().size());
        Mockito.verify(runnable1, Mockito.never()).run();
        Mockito.verify(runnable2, Mockito.never()).run();
        Assert.assertTrue(executor.isShutdown());
        Assert.assertTrue(executor.isTerminated());
        Assert.assertTrue(executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testOfferEvents() throws InterruptedException {
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final Runnable runnable1 = Mockito.mock(Runnable.class);
        final Runnable runnable2 = Mockito.mock(Runnable.class);
        final ScheduledFutureMock<Void> futureResult1 = executor.submit(runnable1);
        final ScheduledFutureMock<Void> futureResult2 = executor.submit(runnable2);

        Assert.assertEquals(2, executor.getEvents().size());

        final ScheduledFutureMock<?> offeredEvent1 = executor.offerNextEventForExecution();
        final ScheduledFutureMock<?> offeredEvent2 = executor.offerNextEventForExecution();

        Assert.assertEquals(futureResult1, offeredEvent1);
        Assert.assertEquals(futureResult2, offeredEvent2);
        Assert.assertEquals(2, executor.getEvents().size());
        Assert.assertNull(executor.offerNextEventForExecution());

        offeredEvent2.execute();
        Assert.assertEquals(1, executor.getEvents().size());
        Assert.assertNull(executor.offerNextEventForExecution());
        Mockito.verify(runnable1, Mockito.never()).run();
        Mockito.verify(runnable2, Mockito.times(1)).run();

        offeredEvent1.execute();
        Assert.assertEquals(0, executor.getEvents().size());
        Assert.assertNull(executor.offerNextEventForExecution());
        Mockito.verify(runnable1, Mockito.times(1)).run();
        Mockito.verify(runnable2, Mockito.times(1)).run();

        offeredEvent2.execute();
        Mockito.verify(runnable2, Mockito.times(1)).run();

        offeredEvent1.execute();
        Mockito.verify(runnable1, Mockito.times(1)).run();
    }

    @Test
    public void testOfferEventsWithFixedRate() throws InterruptedException {
        final long initialDelay = 100;
        final long period1 = 200;
        final long period2 = 400;

        final SystemTimeProviderMock timeProvider = new SystemTimeProviderMock();
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock(timeProvider);

        final Runnable runnable1 = Mockito.mock(Runnable.class);
        final Runnable runnable2 = Mockito.mock(Runnable.class);

        final ScheduledFutureMock<Void> futureResult1 = executor
                .scheduleAtFixedRate(runnable1, initialDelay, period1, TimeUnit.MILLISECONDS);

        final ScheduledFutureMock<Void> futureResult2 = executor
                .scheduleAtFixedRate(runnable2, initialDelay, period2, TimeUnit.MILLISECONDS);

        Assert.assertEquals(2, executor.getEvents().size());
        Assert.assertNull(executor.offerNextEventForExecution());

        timeProvider.elapsedMillis(initialDelay);
        ScheduledFutureMock<?> offeredEvent1 = executor.offerNextEventForExecution();
        ScheduledFutureMock<?> offeredEvent2 = executor.offerNextEventForExecution();
        Assert.assertNull(executor.offerNextEventForExecution());

        Assert.assertEquals(futureResult1, offeredEvent1);
        Assert.assertEquals(futureResult2, offeredEvent2);

        offeredEvent2.execute();
        Assert.assertNull(executor.offerNextEventForExecution());

        timeProvider.elapsedMillis(period1);
        Assert.assertNull(executor.offerNextEventForExecution());

        timeProvider.elapsedMillis(period1);
        offeredEvent2 = executor.offerNextEventForExecution();
        Assert.assertEquals(futureResult2, offeredEvent2);
        offeredEvent2.execute();

        offeredEvent1.execute();
        Assert.assertNull(executor.offerNextEventForExecution());

        timeProvider.elapsedMillis(period1);
        offeredEvent1 = executor.offerNextEventForExecution();
        Assert.assertEquals(futureResult1, offeredEvent1);
        Assert.assertNull(executor.offerNextEventForExecution());

        timeProvider.elapsedMillis(period1);
        offeredEvent2 = executor.offerNextEventForExecution();
        Assert.assertEquals(futureResult2, offeredEvent2);
    }

    @Test
    public void testExecuteInMultiThreads() throws InterruptedException {
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final int messageCount = 500;

        final Runnable message = Mockito.mock(Runnable.class);

        final Thread messageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < messageCount; i++) {
                    executor.execute(message);
                }
            }
        }, "Message executor thread");

        final CountDownLatch dispatcherSync = new CountDownLatch(1);
        final MessageDispatcher messageDispatcher1 = new MessageDispatcher(executor, dispatcherSync);
        final MessageDispatcher messageDispatcher2 = new MessageDispatcher(executor, dispatcherSync);
        final MessageDispatcher messageDispatcher3 = new MessageDispatcher(executor, dispatcherSync);

        final Thread dispatcherThread1 = new Thread(messageDispatcher1, "Dispatcher thread1");
        final Thread dispatcherThread2 = new Thread(messageDispatcher2, "Dispatcher thread2");
        final Thread dispatcherThread3 = new Thread(messageDispatcher3, "Dispatcher thread3");

        dispatcherThread1.start();
        dispatcherThread2.start();
        dispatcherThread3.start();
        dispatcherSync.countDown();
        messageThread.start();

        messageThread.join(TIMEOUT);
        Assert.assertFalse(messageThread.isAlive());
        Assert.assertTrue(dispatcherThread1.isAlive());
        Assert.assertTrue(dispatcherThread2.isAlive());
        Assert.assertTrue(dispatcherThread3.isAlive());

        executor.shutdown();
        Assert.assertTrue(executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS));

        dispatcherThread1.join(TIMEOUT);
        dispatcherThread2.join(TIMEOUT);
        dispatcherThread3.join(TIMEOUT);

        Assert.assertFalse(dispatcherThread1.isAlive());
        Assert.assertFalse(dispatcherThread2.isAlive());
        Assert.assertFalse(dispatcherThread3.isAlive());
        Assert.assertEquals(
                messageCount,
                messageDispatcher1.getCount() + messageDispatcher2.getCount() + messageDispatcher3.getCount());
        Mockito.verify(message, Mockito.times(messageCount));
    }

    @Test
    public void testInvokeAll() throws InterruptedException, ExecutionException {
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final int taskCount = 500;
        final int dispatcherCount = 50;

        final ArrayList<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>(taskCount);

        for (int i = 0; i < taskCount; i++) {
            final Integer result = Integer.valueOf(i);
            tasks.add(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return result;
                }
            });
        }
        final CountDownLatch dispatcherSync = new CountDownLatch(1);
        final ArrayList<MessageDispatcher> messageDispatchers = new ArrayList<MessageDispatcher>();
        final ArrayList<Thread> dispatcherThreads = new ArrayList<Thread>();
        for (int i = 0; i < dispatcherCount; i++) {
            final MessageDispatcher messageDispatcher = new MessageDispatcher(executor, dispatcherSync);
            messageDispatchers.add(messageDispatcher);
            final Thread dispatcherThread = new Thread(messageDispatcher, "Dispatcher thread" + i);
            dispatcherThreads.add(dispatcherThread);
            dispatcherThread.start();
        }
        dispatcherSync.countDown();

        try {
            final List<Future<Integer>> invocationResults = executor.invokeAll(tasks);

            Assert.assertEquals(taskCount, invocationResults.size());
            for (int i = 0; i < taskCount; i++) {
                final Future<Integer> invocationResult = invocationResults.get(i);
                Assert.assertTrue(invocationResult.isDone());
                Assert.assertFalse(invocationResult.isCancelled());
                Assert.assertEquals(Integer.valueOf(i), invocationResult.get());
            }
        }
        finally {
            executor.shutdown();
            Assert.assertTrue(executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertEquals(0, executor.getEvents().size());

            for (final Thread dispatcherThread : dispatcherThreads) {
                dispatcherThread.join(TIMEOUT);
                Assert.assertFalse(dispatcherThread.isAlive());
            }
        }
    }

    @Test(expected = InterruptedException.class)
    public void testInvokeAllWithInterrupt() throws InterruptedException, ExecutionException {
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final int taskCount = 50;

        final ArrayList<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>(taskCount);

        final Thread mainThread = Thread.currentThread();
        for (int i = 0; i < taskCount; i++) {
            final Integer result = Integer.valueOf(i);
            tasks.add(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    if (result.intValue() == 25) {
                        mainThread.interrupt();
                    }
                    Thread.sleep(Long.MAX_VALUE);
                    return result;
                }
            });
        }
        final CountDownLatch dispatcherSync = new CountDownLatch(1);
        final ArrayList<MessageDispatcher> messageDispatchers = new ArrayList<MessageDispatcher>();
        final ArrayList<Thread> dispatcherThreads = new ArrayList<Thread>();
        for (int i = 0; i < taskCount; i++) {
            final MessageDispatcher messageDispatcher = new MessageDispatcher(executor, dispatcherSync);
            messageDispatchers.add(messageDispatcher);
            final Thread dispatcherThread = new Thread(messageDispatcher, "Dispatcher thread" + i);
            dispatcherThreads.add(dispatcherThread);
            dispatcherThread.start();
        }
        dispatcherSync.countDown();

        try {
            executor.invokeAll(tasks);
        }
        finally {
            executor.shutdown();
            Assert.assertTrue(executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertEquals(0, executor.getEvents().size());
            for (final Thread dispatcherThread : dispatcherThreads) {
                dispatcherThread.join(TIMEOUT);
                Assert.assertFalse(dispatcherThread.isAlive());
            }
        }
    }

    @Test
    public void testInvokeAllWithTimeout() throws InterruptedException, ExecutionException {
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final int taskCount = 50;
        final int dispatcherCount = 30;

        final ArrayList<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>(taskCount);

        for (int i = 0; i < taskCount; i++) {
            final Integer result = Integer.valueOf(i);
            tasks.add(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    Thread.sleep(Long.MAX_VALUE);
                    return result;
                }
            });
        }
        final CountDownLatch dispatcherSync = new CountDownLatch(1);
        final ArrayList<MessageDispatcher> messageDispatchers = new ArrayList<MessageDispatcher>();
        final ArrayList<Thread> dispatcherThreads = new ArrayList<Thread>();
        for (int i = 0; i < dispatcherCount; i++) {
            final MessageDispatcher messageDispatcher = new MessageDispatcher(executor, dispatcherSync);
            messageDispatchers.add(messageDispatcher);
            final Thread dispatcherThread = new Thread(messageDispatcher, "Dispatcher thread" + i);
            dispatcherThreads.add(dispatcherThread);
            dispatcherThread.start();
        }
        dispatcherSync.countDown();

        try {
            final List<Future<Integer>> invocationResults = executor.invokeAll(tasks, 1, TimeUnit.MILLISECONDS);

            Assert.assertEquals(taskCount, invocationResults.size());
            for (int i = 0; i < taskCount; i++) {
                final Future<Integer> invocationResult = invocationResults.get(i);
                Assert.assertTrue(invocationResult.isDone());
                Assert.assertTrue(invocationResult.isCancelled());
                assertFutureCanceled(invocationResult);
            }
        }
        finally {
            executor.shutdown();
            Assert.assertTrue(executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertEquals(0, executor.getEvents().size());

            for (final Thread dispatcherThread : dispatcherThreads) {
                dispatcherThread.join(TIMEOUT);
                Assert.assertFalse(dispatcherThread.isAlive());
            }
        }
    }

    @Test
    public void testInvokeAny() throws InterruptedException, ExecutionException {
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final int taskCount = 100;
        final int dispatcherCount = 50;

        final ArrayList<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>(taskCount);

        for (int i = 0; i < taskCount; i++) {
            final Integer result = Integer.valueOf(i);
            tasks.add(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    //produces interrupted exception for half of the dispatched tasks
                    if (result.intValue() > dispatcherCount / 2) {
                        Thread.sleep(50000);
                    }
                    return result;
                }
            });
        }

        final CountDownLatch dispatcherSync = new CountDownLatch(1);
        final ArrayList<MessageDispatcher> messageDispatchers = new ArrayList<MessageDispatcher>();
        final ArrayList<Thread> dispatcherThreads = new ArrayList<Thread>();
        for (int i = 0; i < dispatcherCount; i++) {
            final MessageDispatcher messageDispatcher = new MessageDispatcher(executor, dispatcherSync);
            messageDispatchers.add(messageDispatcher);
            final Thread dispatcherThread = new Thread(messageDispatcher, "Dispatcher thread" + i);
            dispatcherThreads.add(dispatcherThread);
            dispatcherThread.start();
        }

        final List<ScheduledFutureMock<?>> scheduledTasks = new LinkedList<ScheduledFutureMock<?>>();
        final Thread tasksAdderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<ScheduledFutureMock<?>> events = executor.getEvents();
                while (events.size() != taskCount) {
                    events = executor.getEvents();
                }
                scheduledTasks.addAll(events);
                //after all events has been received, start dispatching
                dispatcherSync.countDown();
            }
        }, "Events adder thread");
        tasksAdderThread.start();

        try {
            final Integer invocationResult = executor.invokeAny(tasks);
            tasksAdderThread.join();
            Assert.assertEquals(taskCount, scheduledTasks.size());
            for (final ScheduledFutureMock<?> future : scheduledTasks) {
                Assert.assertTrue(future.isDone());
            }
            Assert.assertNotNull(invocationResult);
            Assert.assertTrue(invocationResult.intValue() >= 0);
            Assert.assertTrue(invocationResult.intValue() < taskCount);
        }
        finally {
            executor.shutdown();
            Assert.assertTrue(executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertEquals(0, executor.getEvents().size());
            for (final Thread dispatcherThread : dispatcherThreads) {
                dispatcherThread.join(TIMEOUT);
                Assert.assertFalse(dispatcherThread.isAlive());
            }
        }
    }

    @Test(expected = TimeoutException.class)
    public void testInvokeAnyWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final int taskCount = 100;
        final int dispatcherCount = 50;

        final ArrayList<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>(taskCount);

        for (int i = 0; i < taskCount; i++) {
            final Integer result = Integer.valueOf(i);
            tasks.add(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    Thread.sleep(Long.MAX_VALUE);
                    return result;
                }
            });
        }

        final CountDownLatch dispatcherSync = new CountDownLatch(1);
        final ArrayList<MessageDispatcher> messageDispatchers = new ArrayList<MessageDispatcher>();
        final ArrayList<Thread> dispatcherThreads = new ArrayList<Thread>();
        for (int i = 0; i < dispatcherCount; i++) {
            final MessageDispatcher messageDispatcher = new MessageDispatcher(executor, dispatcherSync);
            messageDispatchers.add(messageDispatcher);
            final Thread dispatcherThread = new Thread(messageDispatcher, "Dispatcher thread" + i);
            dispatcherThreads.add(dispatcherThread);
            dispatcherThread.start();
        }

        final List<ScheduledFutureMock<?>> scheduledTasks = new LinkedList<ScheduledFutureMock<?>>();
        final Thread tasksAdderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<ScheduledFutureMock<?>> events = executor.getEvents();
                while (events.size() != taskCount) {
                    events = executor.getEvents();
                }
                scheduledTasks.addAll(events);
                //after all events has been received, start dispatching
                dispatcherSync.countDown();
            }
        }, "Events adder thread");
        tasksAdderThread.start();

        Integer invocationResult = null;
        try {
            invocationResult = executor.invokeAny(tasks, 1, TimeUnit.MILLISECONDS);
        }
        finally {
            tasksAdderThread.join();
            Assert.assertEquals(taskCount, scheduledTasks.size());
            for (final ScheduledFutureMock<?> future : scheduledTasks) {
                Assert.assertTrue(future.isDone());
                Assert.assertTrue(future.isCancelled());
            }
            Assert.assertNull(invocationResult);
            executor.shutdown();
            Assert.assertTrue(executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertEquals(0, executor.getEvents().size());
            for (final Thread dispatcherThread : dispatcherThreads) {
                dispatcherThread.join(TIMEOUT);
                Assert.assertFalse(dispatcherThread.isAlive());
            }
        }
    }

    @Test(expected = InterruptedException.class)
    public void testInvokeAnyWithInterrupt() throws InterruptedException, ExecutionException, TimeoutException {
        final ScheduledExecutorServiceMock executor = new ScheduledExecutorServiceMock();

        final int taskCount = 50;

        final ArrayList<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>(taskCount);

        for (int i = 0; i < taskCount; i++) {
            final Integer result = Integer.valueOf(i);
            tasks.add(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    Thread.sleep(Long.MAX_VALUE);
                    return result;
                }
            });
        }

        final CountDownLatch dispatcherSync = new CountDownLatch(1);
        final ArrayList<MessageDispatcher> messageDispatchers = new ArrayList<MessageDispatcher>();
        final ArrayList<Thread> dispatcherThreads = new ArrayList<Thread>();
        for (int i = 0; i < taskCount; i++) {
            final MessageDispatcher messageDispatcher = new MessageDispatcher(executor, dispatcherSync);
            messageDispatchers.add(messageDispatcher);
            final Thread dispatcherThread = new Thread(messageDispatcher, "Dispatcher thread" + i);
            dispatcherThreads.add(dispatcherThread);
            dispatcherThread.start();
        }

        final Thread mainThread = Thread.currentThread();
        final Thread mainThreadInterrupterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (getOfferedEventCount() < taskCount) {
                    //wait until all events are offered to dispatchers
                }
                mainThread.interrupt();
            }

            private int getOfferedEventCount() {
                int result = 0;
                for (final ScheduledFutureMock<?> future : executor.getEvents()) {
                    if (future.isOffered()) {
                        result++;
                    }
                }
                return result;
            }
        }, "Events adder thread");

        final List<ScheduledFutureMock<?>> scheduledTasks = new LinkedList<ScheduledFutureMock<?>>();
        final Thread tasksAdderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<ScheduledFutureMock<?>> events = executor.getEvents();
                while (events.size() != taskCount) {
                    events = executor.getEvents();
                }
                scheduledTasks.addAll(events);

                //then wait until all events are offered to dispatcher and
                //interrupt the main thread
                mainThreadInterrupterThread.start();

                //start dispatching
                dispatcherSync.countDown();

            }
        }, "Events adder thread");
        tasksAdderThread.start();

        Integer invocationResult = null;
        try {
            invocationResult = executor.invokeAny(tasks);
        }
        finally {
            tasksAdderThread.join(TIMEOUT);
            mainThreadInterrupterThread.join(TIMEOUT);
            Assert.assertEquals(taskCount, scheduledTasks.size());
            for (final ScheduledFutureMock<?> future : scheduledTasks) {
                Assert.assertTrue(future.isDone());
                Assert.assertTrue(future.isCancelled());
            }
            Assert.assertNull(invocationResult);
            executor.shutdown();
            Assert.assertTrue(executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS));
            Assert.assertEquals(0, executor.getEvents().size());
            for (final Thread dispatcherThread : dispatcherThreads) {
                dispatcherThread.join(TIMEOUT);
                Assert.assertFalse(dispatcherThread.isAlive());
            }
        }
    }

    private void assertFutureCanceled(final Future<?> future) {
        try {
            future.get();
            Assert.fail("Cancelation excpetion expected");
        }
        catch (final CancellationException e) {
            //expected, do nothing
        }
        catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
        catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private class MessageDispatcher implements Runnable {

        private final ScheduledExecutorServiceMock executor;
        private final CountDownLatch syncer;
        private final AtomicInteger count;

        MessageDispatcher(final ScheduledExecutorServiceMock executor, final CountDownLatch syncer) {
            this.executor = executor;
            this.syncer = syncer;
            this.count = new AtomicInteger(0);
        }

        @Override
        public void run() {
            try {
                syncer.await();
            }
            catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Dispatcher interrupted");
                return;
            }
            while (!executor.isTerminated()) {
                final ScheduledFutureMock<?> executedEvent = executor.executeNextEvent();
                if (executedEvent != null) {
                    count.incrementAndGet();
                }
            }
        }

        int getCount() {
            return count.get();
        }

    }

    private class CallableMock<T> implements Callable<T> {

        private final T result;

        private final AtomicLong invocationCount;

        CallableMock(final T result) {
            this.result = result;
            this.invocationCount = new AtomicLong(0);
        }

        @Override
        public T call() throws Exception {
            invocationCount.incrementAndGet();
            return result;
        }

        long getInvocationCount() {
            return invocationCount.get();
        }

    }

}
