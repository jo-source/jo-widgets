/*
 * Copyright (c) 2016, grossmann
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

package org.jowidgets.logging.api;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import junit.framework.Assert;

import org.jowidgets.logging.tools.ILoggerFactory;
import org.jowidgets.logging.tools.LoggerMock;
import org.jowidgets.util.ValueHolder;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestLoggerProviderTest {

    @BeforeClass
    public static void setUpClass() {
        TestLoggerProvider.get();
    }

    @After
    public void tearDown() {
        TestLoggerProvider.clearLoggerProviderForCurrentThread();
    }

    @Test
    public void testProviderHasDifferentLoggerMocks() {
        final ILogger logger1a = LoggerProvider.get("Foo1");
        final ILogger logger1b = LoggerProvider.get("Foo1");
        final ILogger logger2a = LoggerProvider.get("Foo2");
        final ILogger logger2b = LoggerProvider.get("Foo2");

        Assert.assertSame(logger1a, logger1b);
        Assert.assertSame(logger2a, logger2b);

        Assert.assertNotSame(logger1a, logger2a);
    }

    @Test
    public void testSetLoggerForThread() {

        final LoggerMock logger = new LoggerMock();

        TestLoggerProvider.setLoggerForCurrentThread(logger);

        final ILogger logger1a = LoggerProvider.get("Foo1");
        final ILogger logger1b = LoggerProvider.get("Foo1");
        final ILogger logger2a = LoggerProvider.get("Foo2");
        final ILogger logger2b = LoggerProvider.get("Foo2");

        Assert.assertSame(logger, logger1a);
        Assert.assertSame(logger, logger1b);
        Assert.assertSame(logger, logger2a);
        Assert.assertSame(logger, logger2b);
    }

    @Test
    public void testSetLoggerProviderForThread() {

        final LoggerMock defaultLogger = new LoggerMock();
        final LoggerMock logger1 = new LoggerMock();
        final LoggerMock logger2 = new LoggerMock();

        TestLoggerProvider.setLoggerProviderForCurrentThread(new ILoggerFactory() {

            @Override
            public ILogger create(final String name, final String wrapperFQCN) {
                if ("Foo1".equals(name)) {
                    return logger1;
                }
                else if ("Foo2".equals(name)) {
                    return logger2;
                }
                else {
                    return defaultLogger;
                }
            }
        });

        final ILogger logger1a = LoggerProvider.get("Foo1");
        final ILogger logger1b = LoggerProvider.get("Foo1");
        final ILogger logger2a = LoggerProvider.get("Foo2");
        final ILogger logger2b = LoggerProvider.get("Foo2");
        final ILogger logger3a = LoggerProvider.get("Foo3");
        final ILogger logger3b = LoggerProvider.get("Foo3");

        Assert.assertSame(logger1, logger1a);
        Assert.assertSame(logger1, logger1b);
        Assert.assertSame(logger2, logger2a);
        Assert.assertSame(logger2, logger2b);
        Assert.assertSame(defaultLogger, logger3a);
        Assert.assertSame(defaultLogger, logger3b);
    }

    @Test
    public void testSetLoggerForDifferentThreads() {

        final LoggerMock logger1 = new LoggerMock();
        final LoggerMock logger2 = new LoggerMock();

        final ValueHolder<ILogger> retrievedLogger1 = new ValueHolder<ILogger>();
        final ValueHolder<ILogger> retrievedLogger2 = new ValueHolder<ILogger>();

        final ExecutorService executor1 = Executors.newSingleThreadExecutor();
        final ExecutorService executor2 = Executors.newSingleThreadExecutor();

        final CountDownLatch latch = new CountDownLatch(4);

        executor1.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    TestLoggerProvider.setLoggerForCurrentThread(logger1);
                }
                finally {
                    latch.countDown();
                }
            }
        });

        executor2.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    TestLoggerProvider.setLoggerForCurrentThread(logger2);
                }
                finally {
                    latch.countDown();
                }
            }
        });

        executor1.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    retrievedLogger1.set(LoggerProvider.get("Foo"));
                }
                finally {
                    latch.countDown();
                }
            }
        });

        executor2.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    retrievedLogger2.set(LoggerProvider.get("Foo"));
                }
                finally {
                    latch.countDown();
                }
            }
        });

        try {
            latch.await();
        }
        catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }

        Assert.assertSame(logger1, retrievedLogger1.get());
        Assert.assertSame(logger2, retrievedLogger2.get());
        Assert.assertNotSame(logger1, LoggerProvider.get("Foo"));
        Assert.assertNotSame(logger2, LoggerProvider.get("Foo"));
    }
}
