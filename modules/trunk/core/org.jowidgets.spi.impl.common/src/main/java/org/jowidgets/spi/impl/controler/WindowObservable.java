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

package org.jowidgets.spi.impl.controler;

import java.util.HashSet;
import java.util.Set;

import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.widgets.controler.IWindowListener;
import org.jowidgets.common.widgets.controler.IWindowObservable;
import org.jowidgets.util.ValueHolder;

public class WindowObservable implements IWindowObservable {

	private final Set<IWindowListener> windowListeners;

	public WindowObservable() {
		super();
		this.windowListeners = new HashSet<IWindowListener>();
	}

	@Override
	public final void addWindowListener(final IWindowListener listener) {
		windowListeners.add(listener);
	}

	@Override
	public final void removeWindowListener(final IWindowListener listener) {
		windowListeners.remove(listener);
	}

	public final void fireWindowActivated() {
		for (final IWindowListener windowListener : windowListeners) {
			windowListener.windowActivated();
		}
	}

	public final void fireWindowDeactivated() {
		for (final IWindowListener windowListener : windowListeners) {
			windowListener.windowDeactivated();
		}
	}

	public final void fireWindowIconified() {
		for (final IWindowListener windowListener : windowListeners) {
			windowListener.windowIconified();
		}
	}

	public final void fireWindowDeiconified() {
		for (final IWindowListener windowListener : windowListeners) {
			windowListener.windowDeiconified();
		}
	}

	public final void fireWindowClosed() {
		for (final IWindowListener windowListener : windowListeners) {
			windowListener.windowClosed();
		}
	}

	public final void fireWindowClosing(final IVetoable vetoable) {
		final ValueHolder<Boolean> veto = new ValueHolder<Boolean>(Boolean.FALSE);
		final IVetoable innerVetoable = new IVetoable() {
			@Override
			public void veto() {
				veto.set(Boolean.TRUE);
			}
		};
		for (final IWindowListener windowListener : windowListeners) {
			windowListener.windowClosing(innerVetoable);
			if (veto.get().booleanValue()) {
				vetoable.veto();
				break;
			}
		}
	}

	public final boolean fireWindowClosing() {
		final ValueHolder<Boolean> veto = new ValueHolder<Boolean>(Boolean.FALSE);
		final IVetoable vetoable = new IVetoable() {
			@Override
			public void veto() {
				veto.set(Boolean.TRUE);
			}
		};
		for (final IWindowListener windowListener : windowListeners) {
			windowListener.windowClosing(vetoable);
			if (veto.get().booleanValue()) {
				return true;
			}
		}
		return false;
	}

}
