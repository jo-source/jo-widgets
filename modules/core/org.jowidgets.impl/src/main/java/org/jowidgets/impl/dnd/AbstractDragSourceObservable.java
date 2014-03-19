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

package org.jowidgets.impl.dnd;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jowidgets.api.clipboard.TransferType;
import org.jowidgets.api.dnd.IDragDataResponse;
import org.jowidgets.api.dnd.IDragEvent;
import org.jowidgets.api.dnd.IDragSourceListener;
import org.jowidgets.api.dnd.IDragSourceObservable;
import org.jowidgets.common.dnd.DropAction;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.util.Assert;

abstract class AbstractDragSourceObservable implements IDragSourceObservable {

	private final Set<IDragSourceListener> listeners;

	public AbstractDragSourceObservable() {
		this.listeners = new LinkedHashSet<IDragSourceListener>();
	}

	@Override
	public final void addDragSourceListener(final IDragSourceListener listener) {
		Assert.paramNotNull(listener, "listener");
		final int lastSize = listeners.size();
		listeners.add(listener);
		if (lastSize == 0) {
			setActive(true);
		}
	}

	@Override
	public final void removeDragSourceListener(final IDragSourceListener listener) {
		Assert.paramNotNull(listener, "listener");
		final int lastSize = listeners.size();
		listeners.remove(listener);
		if (lastSize == 1 && listeners.size() == 0) {
			setActive(false);
		}
	}

	final void fireDragStart(final IDragEvent event, final IVetoable veto) {
		for (final IDragSourceListener listener : new LinkedList<IDragSourceListener>(listeners)) {
			listener.dragStart(event, veto);
		}
	}

	final void fireDragSetData(
		final IDragEvent event,
		final IVetoable veto,
		final TransferType<?> transferType,
		final IDragDataResponse dragData) {
		for (final IDragSourceListener listener : new LinkedList<IDragSourceListener>(listeners)) {
			listener.dragSetData(event, veto, transferType, dragData);
		}
	}

	final void fireDragFinished(final IDragEvent event, final DropAction dropAction) {
		for (final IDragSourceListener listener : new LinkedList<IDragSourceListener>(listeners)) {
			listener.dragFinished(event, dropAction);
		}
	}

	protected abstract void setActive(boolean active);

}
