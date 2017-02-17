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

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class EventBufferSingleThreadAccessMockTest {

    private EventBufferSingleThreadAccessMock threadAccess;
    private Runnable runnable0;
    private Runnable runnable1;
    private Runnable runnable2;
    private Runnable runnable3;

    @Before
    public void setUp() {
        threadAccess = new EventBufferSingleThreadAccessMock();
        runnable0 = Mockito.mock(Runnable.class);
        runnable1 = Mockito.mock(Runnable.class);
        runnable2 = Mockito.mock(Runnable.class);
        runnable3 = Mockito.mock(Runnable.class);
    }

    @Test
    public void testCheckThreadPositive() {
        threadAccess.checkThread();
    }

    @Test(expected = IllegalStateException.class)
    public void testCheckThreadNegative() {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<EventBufferSingleThreadAccessMock> threadAcccessReference = new AtomicReference<EventBufferSingleThreadAccessMock>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                threadAcccessReference.set(new EventBufferSingleThreadAccessMock());
                latch.countDown();
            }
        }).start();

        try {
            latch.await();
        }
        catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }

        threadAcccessReference.get().checkThread();
    }

    @Test
    public void testGetEventsCopy() {
        invokeRunnablesOnce();
        verifyNothingInvoked();

        final List<Runnable> eventsList = threadAccess.getEventsCopy();

        Assert.assertEquals(4, eventsList.size());
        final Runnable[] eventsArray = eventsList.toArray(new Runnable[4]);
        Assert.assertEquals(runnable0, eventsArray[0]);
        Assert.assertEquals(runnable1, eventsArray[1]);
        Assert.assertEquals(runnable2, eventsArray[2]);
        Assert.assertEquals(runnable3, eventsArray[3]);

        Assert.assertEquals(4, threadAccess.getEventsCount());
    }

    @Test
    public void testPopEvents() {
        invokeRunnablesOnce();
        verifyNothingInvoked();

        final List<Runnable> eventsList = threadAccess.popEvents();

        Assert.assertEquals(4, eventsList.size());
        final Runnable[] eventsArray = eventsList.toArray(new Runnable[4]);
        Assert.assertEquals(runnable0, eventsArray[0]);
        Assert.assertEquals(runnable1, eventsArray[1]);
        Assert.assertEquals(runnable2, eventsArray[2]);
        Assert.assertEquals(runnable3, eventsArray[3]);

        Assert.assertEquals(0, threadAccess.getEventsCount());
    }

    @Test
    public void testRunEvents() {
        invokeRunnablesOnce();
        verifyNothingInvoked();
        threadAccess.runEvents();
        verifyAllInvokedoOnce();
        Assert.assertEquals(0, threadAccess.getEventsCount());
    }

    private void invokeRunnablesOnce() {
        threadAccess.invoke(runnable0);
        threadAccess.invoke(runnable1);
        threadAccess.invoke(runnable2);
        threadAccess.invoke(runnable3);
    }

    private void verifyNothingInvoked() {
        Mockito.verify(runnable0, Mockito.never()).run();
        Mockito.verify(runnable1, Mockito.never()).run();
        Mockito.verify(runnable2, Mockito.never()).run();
        Mockito.verify(runnable3, Mockito.never()).run();
    }

    private void verifyAllInvokedoOnce() {
        Mockito.verify(runnable0, Mockito.times(1)).run();
        Mockito.verify(runnable1, Mockito.times(1)).run();
        Mockito.verify(runnable2, Mockito.times(1)).run();
        Mockito.verify(runnable3, Mockito.times(1)).run();
    }

}
