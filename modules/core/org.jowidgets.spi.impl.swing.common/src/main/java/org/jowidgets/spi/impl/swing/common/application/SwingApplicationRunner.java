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

package org.jowidgets.spi.impl.swing.common.application;

import java.awt.Window;
import java.lang.reflect.Method;

import javax.swing.SwingUtilities;

import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.application.IApplicationRunner;

public class SwingApplicationRunner implements IApplicationRunner {

    private Thread eventDispatcherThread;

    @Override
    public void run(final IApplication application) {

        if (SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException(
                "The current thread is the EventDispatcherThread. A ApplicationRunner must not be used from the EventDispatcherThread");
        }

        //Create a lifecycle that disposes all windows when finished
        final IApplicationLifecycle lifecycle = new IApplicationLifecycle() {

            private boolean isRunning = true;

            @Override
            public synchronized void finish() {
                if (isRunning) {
                    isRunning = false;

                    //dispose all windows
                    for (final Window window : Window.getWindows()) {
                        window.dispose();
                    }

                    //stop event dispatching
                    try {
                        final Class<?> edtClass = Class.forName("java.awt.EventDispatchThread");
                        final Method method = edtClass.getDeclaredMethod("stopDispatching");
                        method.setAccessible(true);
                        method.invoke(eventDispatcherThread);
                    }
                    catch (final Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        //start the application
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    eventDispatcherThread = Thread.currentThread();
                    application.start(lifecycle);
                }
            });
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }

        //wait until the event dispatcher has finished
        try {
            eventDispatcherThread.join();
        }
        catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
