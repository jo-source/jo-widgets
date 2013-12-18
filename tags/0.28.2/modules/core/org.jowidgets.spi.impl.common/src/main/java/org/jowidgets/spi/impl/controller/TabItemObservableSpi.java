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

import java.util.HashSet;
import java.util.Set;

import org.jowidgets.common.types.IVetoable;
import org.jowidgets.spi.widgets.controller.ITabItemListenerSpi;
import org.jowidgets.spi.widgets.controller.ITabItemObservableSpi;
import org.jowidgets.util.ValueHolder;

public class TabItemObservableSpi implements ITabItemObservableSpi {

	private final Set<ITabItemListenerSpi> listeners;

	public TabItemObservableSpi() {
		this.listeners = new HashSet<ITabItemListenerSpi>();
	}

	@Override
	public void addTabItemListener(final ITabItemListenerSpi listener) {
		listeners.add(listener);
	}

	@Override
	public void removeTabItemListener(final ITabItemListenerSpi listener) {
		listeners.remove(listener);
	}

	public void fireSelected() {
		for (final ITabItemListenerSpi listener : listeners) {
			listener.selected();
		}
	}

	public void fireOnClose(final IVetoable vetoable) {
		for (final ITabItemListenerSpi listener : listeners) {
			listener.onClose(vetoable);
		}
	}

	public boolean fireOnClose() {
		final ValueHolder<Boolean> veto = new ValueHolder<Boolean>(Boolean.FALSE);
		for (final ITabItemListenerSpi listener : listeners) {
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

	public void fireClosed() {
		for (final ITabItemListenerSpi listener : listeners) {
			listener.closed();
		}
	}

}
