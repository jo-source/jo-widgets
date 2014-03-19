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

package org.jowidgets.spi.impl.dnd;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jowidgets.spi.dnd.IDropEventSpi;
import org.jowidgets.spi.dnd.IDropResponseSpi;
import org.jowidgets.spi.dnd.IDropTargetListenerSpi;
import org.jowidgets.spi.dnd.IDropTargetObservableSpi;
import org.jowidgets.util.Assert;

public abstract class AbstractDropTargetObservableSpi implements IDropTargetObservableSpi {

	private final Set<IDropTargetListenerSpi> listeners;

	private boolean active;

	public AbstractDropTargetObservableSpi() {
		this.listeners = new LinkedHashSet<IDropTargetListenerSpi>();
		this.active = false;
	}

	@Override
	public final void addDropTargetListenerSpi(final IDropTargetListenerSpi listener) {
		Assert.paramNotNull(listener, "listener");
		final int lastSize = listeners.size();
		listeners.add(listener);
		if (lastSize == 0) {
			this.active = true;
			setActive(true);
		}
	}

	@Override
	public final void removeDropTargetListenerSpi(final IDropTargetListenerSpi listener) {
		Assert.paramNotNull(listener, "listener");
		final int lastSize = listeners.size();
		listeners.remove(listener);
		if (lastSize == 1 && listeners.size() == 0) {
			this.active = false;
			setActive(false);
		}
	}

	public final void fireDragEnter(final IDropEventSpi event, final IDropResponseSpi response) {
		for (final IDropTargetListenerSpi listener : new LinkedList<IDropTargetListenerSpi>(listeners)) {
			listener.dragEnter(event, response);
		}
	}

	public final void fireDragOver(final IDropEventSpi event, final IDropResponseSpi response) {
		for (final IDropTargetListenerSpi listener : new LinkedList<IDropTargetListenerSpi>(listeners)) {
			listener.dragOver(event, response);
		}
	}

	public final void fireDragOperationChanged(final IDropEventSpi event, final IDropResponseSpi response) {
		for (final IDropTargetListenerSpi listener : new LinkedList<IDropTargetListenerSpi>(listeners)) {
			listener.dragOperationChanged(event, response);
		}
	}

	public final void fireDragExit() {
		for (final IDropTargetListenerSpi listener : new LinkedList<IDropTargetListenerSpi>(listeners)) {
			listener.dragExit();
		}
	}

	public final void fireDropAccept(final IDropEventSpi event, final IDropResponseSpi response) {
		for (final IDropTargetListenerSpi listener : new LinkedList<IDropTargetListenerSpi>(listeners)) {
			listener.dropAccept(event, response);
		}
	}

	public final void fireDrop(final IDropEventSpi event) {
		for (final IDropTargetListenerSpi listener : new LinkedList<IDropTargetListenerSpi>(listeners)) {
			listener.drop(event);
		}
	}

	protected boolean isActive() {
		return active;
	}

	protected abstract void setActive(boolean active);

}
