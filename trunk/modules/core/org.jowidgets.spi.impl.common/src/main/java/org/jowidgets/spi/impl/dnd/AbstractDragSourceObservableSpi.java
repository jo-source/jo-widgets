/*
 * Copyright (c) 2014, Michael
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

package org.jowidgets.spi.impl.dnd;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jowidgets.common.dnd.DropAction;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.types.Position;
import org.jowidgets.spi.clipboard.TransferTypeSpi;
import org.jowidgets.spi.dnd.IDragDataResponseSpi;
import org.jowidgets.spi.dnd.IDragEventSpi;
import org.jowidgets.spi.dnd.IDragSourceListenerSpi;
import org.jowidgets.spi.dnd.IDragSourceObservableSpi;
import org.jowidgets.util.Assert;

public abstract class AbstractDragSourceObservableSpi implements IDragSourceObservableSpi {

	private final Set<IDragSourceListenerSpi> listeners;

	private boolean active;

	public AbstractDragSourceObservableSpi() {
		this.listeners = new LinkedHashSet<IDragSourceListenerSpi>();
		this.active = false;
	}

	@Override
	public final void addDragSourceListenerSpi(final IDragSourceListenerSpi listener) {
		Assert.paramNotNull(listener, "listener");
		final int lastSize = listeners.size();
		listeners.add(listener);
		if (lastSize == 0) {
			this.active = true;
			setActive(true);
		}
	}

	@Override
	public final void removeDragSourceListenerSpi(final IDragSourceListenerSpi listener) {
		Assert.paramNotNull(listener, "listener");
		final int lastSize = listeners.size();
		listeners.remove(listener);
		if (lastSize == 1 && listeners.size() == 0) {
			this.active = false;
			setActive(false);
		}
	}

	public final void fireDragStart(final int x, final int y, final IVetoable veto) {
		fireDragStart(new DragEventSpiImpl(new Position(x, y)), veto);
	}

	public final void fireDragStart(final IDragEventSpi event, final IVetoable veto) {
		for (final IDragSourceListenerSpi listener : new LinkedList<IDragSourceListenerSpi>(listeners)) {
			listener.dragStart(event, veto);
		}
	}

	public final void fireDragSetData(
		final int x,
		final int y,
		final IVetoable veto,
		final TransferTypeSpi transferType,
		final IDragDataResponseSpi dragData) {
		fireDragSetData(new DragEventSpiImpl(new Position(x, y)), veto, transferType, dragData);
	}

	public final void fireDragSetData(
		final IDragEventSpi event,
		final IVetoable veto,
		final TransferTypeSpi transferType,
		final IDragDataResponseSpi dragData) {
		for (final IDragSourceListenerSpi listener : new LinkedList<IDragSourceListenerSpi>(listeners)) {
			listener.dragSetData(event, veto, transferType, dragData);
		}
	}

	public final void fireDragFinished(final int x, final int y, final DropAction dropAction) {
		fireDragFinished(new DragEventSpiImpl(new Position(x, y)), dropAction);
	}

	public final void fireDragFinished(final IDragEventSpi event, final DropAction dropAction) {
		for (final IDragSourceListenerSpi listener : new LinkedList<IDragSourceListenerSpi>(listeners)) {
			listener.dragFinished(event, dropAction);
		}
	}

	protected boolean isActive() {
		return active;
	}

	protected abstract void setActive(boolean active);

}
