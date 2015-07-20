/*
 * Copyright (c) 2015, grossmann
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

import java.util.concurrent.CountDownLatch;

import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.application.IApplicationRunner;
import org.jowidgets.common.threads.IUiThreadAccessCommon;
import org.jowidgets.util.Assert;

/**
 * This implementation of application runner assumes that all
 * invocations will be done in the same thread this object was
 * created in. This fits for the most use cases in unit tests.
 * If more than one thread neccessary for your testcases use the
 * EventQueueUiThreadAccess instead
 * 
 * @author grossmann
 */
public class SingleThreadApplicationRunner implements IApplicationRunner {

    private final IUiThreadAccessCommon uiThreadAccess;

    public SingleThreadApplicationRunner() {
        this(new SingleThreadUiThreadAccess());
    }

    public SingleThreadApplicationRunner(final IUiThreadAccessCommon uiThreadAccess) {
        Assert.paramNotNull(uiThreadAccess, "uiThreadAccess");
        this.uiThreadAccess = uiThreadAccess;
    }

    public IUiThreadAccessCommon getUiThreadAccess() {
        return uiThreadAccess;
    }

    @Override
    public void run(final IApplication application) {

        checkThread();

        final CountDownLatch latch = new CountDownLatch(1);

        final IApplicationLifecycle lifecycle = new IApplicationLifecycle() {
            @Override
            public synchronized void finish() {
                latch.countDown();
            }
        };

        application.start(lifecycle);

        try {
            latch.await();
        }
        catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void checkThread() {
        if (!uiThreadAccess.isUiThread()) {
            throw new IllegalStateException("This implementation of application runner assumes that all "
                + "invocations will be done in the same thread the application runner was "
                + "created in. This fits for the most use cases in unit tests. "
                + "If more than one thread neccessary for your testcases use the "
                + "EventQueueUiThreadAccess instead");
        }
    }

}
