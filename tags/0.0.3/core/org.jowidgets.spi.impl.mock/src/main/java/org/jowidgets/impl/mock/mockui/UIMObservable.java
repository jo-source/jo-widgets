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

package org.jowidgets.impl.mock.mockui;

import java.util.HashSet;
import java.util.Set;

import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.common.widgets.controler.IInputListener;
import org.jowidgets.common.widgets.controler.IWindowListener;

public class UIMObservable {

	private final Set<IInputListener> inputListeners;
	private final Set<IActionListener> actionListeners;
	private final Set<IWindowListener> windowListeners;

	public UIMObservable() {
		super();
		this.inputListeners = new HashSet<IInputListener>();
		this.actionListeners = new HashSet<IActionListener>();
		this.windowListeners = new HashSet<IWindowListener>();
	}

	public void addActionListener(final IActionListener listener) {
		actionListeners.add(listener);
	}

	public void removeActionListener(final IActionListener listener) {
		actionListeners.remove(listener);
	}

	public void addInputListener(final IInputListener listener) {
		inputListeners.add(listener);
	}

	public void removeInputListener(final IInputListener listener) {
		inputListeners.remove(listener);
	}

	public void addWindowListener(final IWindowListener listener) {
		windowListeners.add(listener);
	}

	public void removeWindowListener(final IWindowListener listener) {
		windowListeners.remove(listener);
	}

	public void fireActionPerformed() {
		for (final IActionListener listener : actionListeners) {
			listener.actionPerformed();
		}
	}

	public void fireInputChanged(final Object source) {
		for (final IInputListener listener : inputListeners) {
			listener.inputChanged(source);
		}
	}

	void fireWindowActivated() {
		for (final IWindowListener listener : windowListeners) {
			listener.windowActivated();
		}
	}

	void fireWindowDeactivated() {
		for (final IWindowListener listener : windowListeners) {
			listener.windowDeactivated();
		}
	}

	void fireWindowIconified() {
		for (final IWindowListener listener : windowListeners) {
			listener.windowIconified();
		}
	}

	void fireWindowDeiconified() {
		for (final IWindowListener listener : windowListeners) {
			listener.windowDeiconified();
		}
	}

	void fireWindowClosed() {
		for (final IWindowListener listener : windowListeners) {
			listener.windowClosed();
		}
	}

}
