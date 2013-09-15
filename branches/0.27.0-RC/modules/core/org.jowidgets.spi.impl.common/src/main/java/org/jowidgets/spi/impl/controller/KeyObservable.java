/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.spi.impl.controller;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jowidgets.common.widgets.controller.IKeyEvent;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IKeyObservable;
import org.jowidgets.util.Assert;

public class KeyObservable implements IKeyObservable {

	private final Set<IKeyListener> listeners;
	private final IObservableCallback observableCallback;

	public KeyObservable(final IObservableCallback observableCallback) {
		Assert.paramNotNull(observableCallback, "observableCallback");
		this.listeners = new LinkedHashSet<IKeyListener>();
		this.observableCallback = observableCallback;
	}

	@Override
	public final void addKeyListener(final IKeyListener listener) {
		Assert.paramNotNull(listener, "listener");
		listeners.add(listener);
		if (listeners.size() == 1) {
			observableCallback.onFirstRegistered();
		}
	}

	@Override
	public final void removeKeyListener(final IKeyListener listener) {
		Assert.paramNotNull(listener, "listener");
		listeners.remove(listener);
		if (listeners.size() == 0) {
			observableCallback.onLastUnregistered();
		}
	}

	public final void fireKeyPressed(final ILazyKeyEventContentFactory contentFactory) {
		if (listeners.size() > 0) {
			final IKeyEvent event = new KeyEvent(contentFactory);
			for (final IKeyListener listener : new LinkedList<IKeyListener>(listeners)) {
				listener.keyPressed(event);
			}
		}
	}

	public final void fireKeyReleased(final ILazyKeyEventContentFactory contentFactory) {
		if (listeners.size() > 0) {
			final IKeyEvent event = new KeyEvent(contentFactory);
			for (final IKeyListener listener : new LinkedList<IKeyListener>(listeners)) {
				listener.keyReleased(event);
			}
		}
	}

}
