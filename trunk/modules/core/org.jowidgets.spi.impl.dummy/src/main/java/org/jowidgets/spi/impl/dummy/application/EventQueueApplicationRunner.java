/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.spi.impl.dummy.application;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.application.IApplicationRunner;
import org.jowidgets.common.threads.IUiThreadAccessCommon;

public class EventQueueApplicationRunner implements IApplicationRunner, IUiThreadAccessCommon {

    private static final long SLEEP_TIME = 200;

    private final BlockingQueue<AbstractDummyEvent> events = new LinkedBlockingQueue<AbstractDummyEvent>();
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean onInvokeAndWait = new AtomicBoolean(false);

    private Thread eventDispatcherThread;

    @Override
    public void run(final IApplication application) {

        //Create a lifecycle 
        final IApplicationLifecycle lifecycle = new IApplicationLifecycle() {

            @Override
            public synchronized void finish() {
                running.set(false);
            }
        };

        //start the application
        running.set(true);

        eventDispatcherThread = Thread.currentThread();

        application.start(lifecycle);
        while (running.get()) {
            try {
                final Runnable event = events.poll(SLEEP_TIME, TimeUnit.MILLISECONDS);
                if (event != null) {
                    if (event instanceof InvokeAndWaitDummyEvent) {
                        final Object lock = ((InvokeAndWaitDummyEvent) event).getLock();
                        event.run();
                        synchronized (lock) {
                            onInvokeAndWait.set(false);
                            lock.notify();
                        }
                    }
                    else {
                        event.run();
                    }
                }
            }
            catch (final InterruptedException e) {
                throw new RuntimeException();
            }
        }

        eventDispatcherThread = null;

    }

    @Override
    public boolean isUiThread() {
        return isEventDispatcherThread(Thread.currentThread());
    }

    @Override
    public void invokeLater(final Runnable runnable) {
        checkEventDispatcherRunning();
        if (isEventDispatcherThread(Thread.currentThread())) {
            runnable.run();
        }
        else {
            events.add(new DummyEvent(runnable));
        }
    }

    @Override
    public void invokeAndWait(final Runnable runnable) throws InterruptedException {
        checkEventDispatcherRunning();
        if (isEventDispatcherThread(Thread.currentThread())) {
            runnable.run();
        }
        else {
            if (!onInvokeAndWait.getAndSet(true)) {
                events.add(new InvokeAndWaitDummyEvent(runnable));
            }
            else {
                throw new RuntimeException("Concurrent invocation of invoke and wait");
            }
        }
    }

    private boolean isEventDispatcherThread(final Thread thread) {
        return thread == eventDispatcherThread;
    }

    private void checkEventDispatcherRunning() {
        if (eventDispatcherThread == null) {
            throw new RuntimeException("No EventDispatcherThread is running");
        }
    }

    private abstract class AbstractDummyEvent implements Runnable {

    }

    private final class DummyEvent extends AbstractDummyEvent {

        private final Runnable runnable;

        private DummyEvent(final Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void run() {
            runnable.run();
        }

    }

    private final class InvokeAndWaitDummyEvent extends AbstractDummyEvent {

        private final Object lock;
        private final Runnable runnable;

        private InvokeAndWaitDummyEvent(final Runnable runnable) {
            this.lock = new Object();
            this.runnable = runnable;
        }

        private Object getLock() {
            return lock;
        }

        @Override
        public void run() {
            runnable.run();
            synchronized (lock) {
                try {
                    lock.wait();
                }
                catch (final InterruptedException e) {
                    throw new RuntimeException("Invoke and wait was interrupted.", e);
                }
            }
        }
    }

}
