/*
 * Copyright (c) 2011, Nikolaus Moll
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

package org.jowidgets.impl.widgets.basic;

import java.util.HashSet;
import java.util.Set;

import org.jowidgets.common.widgets.controller.ITableColumnPopupDetectionListener;
import org.jowidgets.common.widgets.controller.ITableColumnPopupDetectionObservable;
import org.jowidgets.common.widgets.controller.ITableColumnPopupEvent;
import org.jowidgets.impl.event.TableColumnPopupEvent;

class TableColumnPopupDetectionObservableSpiAdapter implements ITableColumnPopupDetectionObservable {

	private final Set<ITableColumnPopupDetectionListener> listeners;

	TableColumnPopupDetectionObservableSpiAdapter() {
		this.listeners = new HashSet<ITableColumnPopupDetectionListener>();
	}

	@Override
	public void addTableColumnPopupDetectionListener(final ITableColumnPopupDetectionListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeTableColumnPopupDetectionListener(final ITableColumnPopupDetectionListener listener) {
		listeners.remove(listener);
	}

	public void firePopupDetected(final ITableColumnPopupEvent event, final TableModelSpiAdapter modelSpiAdapter) {
		if (!listeners.isEmpty()) {
			final int convertedIndex = modelSpiAdapter.convertViewToModel(event.getColumnIndex());
			final ITableColumnPopupEvent decoratedEvent = new TableColumnPopupEvent(convertedIndex, event.getPosition());
			for (final ITableColumnPopupDetectionListener listener : listeners) {
				listener.popupDetected(decoratedEvent);
			}
		}
	}
}
