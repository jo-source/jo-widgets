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

package org.jowidgets.impl.threads;

import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.common.threads.IUiThreadAccessCommon;
import org.jowidgets.spi.IWidgetsServiceProvider;

public class UiThreadAccess implements IUiThreadAccess {

	private final IUiThreadAccessCommon uiThreadAccessCommon;
	private final IWidgetsServiceProvider widgetsServiceProvider;

	public UiThreadAccess(final IWidgetsServiceProvider widgetsServiceProvider) {
		super();
		this.uiThreadAccessCommon = widgetsServiceProvider.createUiThreadAccess();
		this.widgetsServiceProvider = widgetsServiceProvider;
	}

	@Override
	public boolean isUiThread() {
		return uiThreadAccessCommon.isUiThread();
	}

	@Override
	public void invokeLater(final Runnable runnable) {
		uiThreadAccessCommon.invokeLater(runnable);
	}

	@Override
	public void invokeAndWait(final Runnable runnable) throws InterruptedException {
		uiThreadAccessCommon.invokeAndWait(runnable);
	}

	@Override
	public void disableAllWindowsWhile(final Runnable runnable) {
		if (isUiThread()) {
			blockEvents(runnable);
		}
		else {
			try {
				invokeAndWait(new Runnable() {
					@Override
					public void run() {
						blockEvents(runnable);
					}
				});
			}
			catch (final InterruptedException e) {
				throw new RuntimeException("Error while blocking events");
			}
		}
	}

	private void blockEvents(final Runnable runnable) {
		widgetsServiceProvider.setAllWindowsEnabled(false);
		try {
			runnable.run();
		}
		catch (final RuntimeException e) {
			throw e;
		}
		catch (final Exception e) {
			throw new RuntimeException("Error while blocking events.", e);
		}
		finally {
			//do this later to allow accumulated events to be fired on
			//the disabled windows, because from know, accumulated events will 
			//be fired first
			invokeLater(new Runnable() {
				@Override
				public void run() {
					widgetsServiceProvider.setAllWindowsEnabled(true);
				}
			});

		}
	}

}
