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

package org.jowidgets.tools.controller;

import java.util.HashSet;
import java.util.Set;

import org.jowidgets.api.controller.ITabItemListener;
import org.jowidgets.api.controller.ITabItemObservable;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.util.ValueHolder;

public class TabItemObservable implements ITabItemObservable {

	private final Set<ITabItemListener> listeners;

	public TabItemObservable() {
		this.listeners = new HashSet<ITabItemListener>();
	}

	@Override
	public void addTabItemListener(final ITabItemListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeTabItemListener(final ITabItemListener listener) {
		listeners.remove(listener);
	}

	public void fireSelectionChanged(final boolean selected) {
		for (final ITabItemListener listener : listeners) {
			listener.selectionChanged(selected);
		}
	}

	public void fireOnClose(final IVetoable vetoable) {
		for (final ITabItemListener listener : listeners) {
			listener.onClose(vetoable);
		}
	}

	public boolean fireOnClose() {
		final ValueHolder<Boolean> veto = new ValueHolder<Boolean>(Boolean.FALSE);
		for (final ITabItemListener listener : listeners) {
			listener.onClose(new IVetoable() {
				@Override
				public void veto() {
					veto.set(Boolean.TRUE);
				}
			});
			if (veto.get().booleanValue()) {
				break;
			}
		}
		return veto.get().booleanValue();
	}

}
