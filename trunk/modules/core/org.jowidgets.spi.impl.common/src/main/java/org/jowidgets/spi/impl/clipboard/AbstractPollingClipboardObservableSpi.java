/*
 * Copyright (c) 2014, grossmann
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

package org.jowidgets.spi.impl.clipboard;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.jowidgets.spi.clipboard.IClipboardListenerSpi;
import org.jowidgets.util.concurrent.DaemonThreadFactory;

public abstract class AbstractPollingClipboardObservableSpi extends ClipboardObservableSpi {

    private final ScheduledExecutorService scheduledExecutor;
    private final Runnable pollingExecutor;
    private final Long delay;

    private ScheduledFuture<?> scheduledFuture;

    protected AbstractPollingClipboardObservableSpi(final Long delay) {
        this.delay = delay;
        if (delay != null) {
            this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
            this.pollingExecutor = new PollingExecutor();
        }
        else {
            this.scheduledExecutor = null;
            this.pollingExecutor = null;
        }
    }

    protected abstract void checkContentChanged();

    @Override
    public void addClipboardListener(final IClipboardListenerSpi listener) {
        final int lastSize = getSize();
        super.addClipboardListener(listener);
        if (lastSize == 0) {
            startPolling();
        }
    }

    @Override
    public void removeClipboardListener(final IClipboardListenerSpi listener) {
        final int lastSize = getSize();
        super.removeClipboardListener(listener);
        if (lastSize > 0 && getSize() == 1) {
            stopPolling();
        }
    }

    private synchronized void startPolling() {
        if (scheduledExecutor != null && scheduledFuture == null) {
            scheduledFuture = scheduledExecutor.scheduleAtFixedRate(pollingExecutor, delay, delay, TimeUnit.MILLISECONDS);
        }
    }

    private synchronized void stopPolling() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            scheduledFuture = null;
        }
    }

    protected void dispose() {
        stopPolling();
    }

    private final class PollingExecutor implements Runnable {
        @Override
        public void run() {
            try {
                checkContentChanged();
            }
            catch (final Exception e) {
                //CHECKSTYLE:OFF
                //TODO Use uncaught exception handler
                e.printStackTrace();
                //CHECKSTYLE:ON
            }
        }
    }
}
