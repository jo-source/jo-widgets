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

import org.jowidgets.common.threads.IUiThreadAccessCommon;
import org.jowidgets.util.Assert;

/**
 * This implementation of ui thread access assumes that all
 * invocations will be done in the same thread this object was
 * created in. This fits for the most use cases in unit tests.
 * If more than one thread neccessary for your testcases use the
 * EventQueueUiThreadAccess instead
 * 
 * @author grossmann
 */
public class SingleThreadUiThreadAccess implements IUiThreadAccessCommon {

    private final Thread uiThread;

    public SingleThreadUiThreadAccess() {
        this.uiThread = Thread.currentThread();
    }

    @Override
    public boolean isUiThread() {
        return Thread.currentThread() == uiThread;
    }

    @Override
    public void invokeLater(final Runnable runnable) {
        checkThread();
        Assert.paramNotNull(runnable, "runnable");
        runnable.run();
    }

    @Override
    public void invokeAndWait(final Runnable runnable) throws InterruptedException {
        checkThread();
        Assert.paramNotNull(runnable, "runnable");
        runnable.run();
    }

    private void checkThread() {
        if (!isUiThread()) {
            throw new IllegalStateException("This implementation of the ui thread access assumes that all "
                + "invocations will be done in the same thread this object was "
                + "created in. This fits for the most use cases in unit tests. "
                + "If more than one thread neccessary for your testcases use the "
                + "EventQueueUiThreadAccess instead");
        }
    }

}
