/*
 * Copyright (c) 2014, Michael Grossmann
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

package org.jowidgets.tools.osx;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import org.jowidgets.util.Assert;

/**
 * A workaround to deal with the problem that swt applications started with webstart
 * won't work on mac os x under some circumstances even if -XstartOnFirstThread was set
 * in the jnlp (for me java 7_51, swt-mac-osx-64-4.3).
 * 
 * Remark: This workaround only works for os x on a mac and it depends on
 * non public api that may be changed. But for now it works and may be the
 * problem described may be fixed in future releases :-)
 */
public final class OsxMainThreadExecutor {

	private OsxMainThreadExecutor() {}

	/**
	 * Runs a given application in the mac os x main thread and makes the vm argument
	 * -XstartOnFirstThread obsolete.
	 * 
	 * Remark: This is a workaround that depends on non public api
	 * 
	 * @param runnable The runnable that runs the application and the swt event loop.
	 *            This runnable has to block until the application was finished
	 */
	public static void runAppInOsxMainThread(final Runnable runnable) {
		Assert.paramNotNull(runnable, "runnable");

		try {
			//maybe the thread is already correct, so first try to start normally
			runnable.run();
		}
		catch (final Exception e) {
			//CHECKSTYLE:OFF
			System.out.println("*** Ignore warning about wrong thread by now and try it in the main thread");
			//CHECKSTYLE:ON
			final CountDownLatch latch = new CountDownLatch(1);
			getOsxNonBlockingMainExecutor().execute(new Runnable() {
				@Override
				public void run() {
					try {
						//this starts the swt event loop and blocks until main shell will closed
						runnable.run();
					}
					finally {
						//ensure that the execution thread not blocks on exceptions
						latch.countDown();
					}
				}
			});
			try {
				//wait until the application has been finished
				latch.await();
			}
			catch (final InterruptedException e2) {
				throw new RuntimeException(e2);
			}
		}

	}

	/**
	 * Gets the NonBlockingMainExecutor of os x.
	 * 
	 * This will be done by reflection do make it possible to compile this code on all platforms.
	 * 
	 * @return The executor or null, if no executor can be created (e.g. not a mac jvm or api changed)
	 */
	private static Executor getOsxNonBlockingMainExecutor() {
		try {
			final Class<?> dispatchClass = Class.forName("com.apple.concurrent.Dispatch");
			final Method getInstanceMethod = dispatchClass.getMethod("getInstance");
			final Object dispatchInstance = getInstanceMethod.invoke(null);
			final Method getExecutorMethod = dispatchClass.getMethod("getNonBlockingMainQueueExecutor");
			return (Executor) getExecutorMethod.invoke(dispatchInstance);
		}
		catch (final Exception e) {
			throw new RuntimeException("*** Cannot get os x main thread executor", e);
		}
	}

}
