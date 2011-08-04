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

import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.widgets.controller.ITableCellEditEvent;
import org.jowidgets.common.widgets.controller.ITableCellEditorListener;
import org.jowidgets.common.widgets.controller.ITableCellEditorObservable;
import org.jowidgets.common.widgets.controller.ITableCellEvent;
import org.jowidgets.impl.event.TableCellEditEvent;
import org.jowidgets.impl.event.TableCellEvent;

class TableCellEditorObservableSpiAdapter implements ITableCellEditorObservable {

	private final Set<ITableCellEditorListener> listeners;

	TableCellEditorObservableSpiAdapter() {
		this.listeners = new HashSet<ITableCellEditorListener>();
	}

	@Override
	public void addTableCellEditorListener(final ITableCellEditorListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeTableCellEditorListener(final ITableCellEditorListener listener) {
		listeners.remove(listener);
	}

	public void fireEditCanceled(final ITableCellEvent event, final TableModelSpiAdapter modelSpiAdapter) {
		if (!listeners.isEmpty()) {
			final ITableCellEvent decoratedEvent = new TableCellEvent(
				event.getRowIndex(),
				modelSpiAdapter.convertViewToModel(event.getColumnIndex()));
			for (final ITableCellEditorListener listener : listeners) {
				listener.editCanceled(decoratedEvent);
			}
		}
	}

	public void fireEditFinished(final ITableCellEditEvent event, final TableModelSpiAdapter modelSpiAdapter) {
		if (!listeners.isEmpty()) {
			final ITableCellEditEvent decoratedEvent = createDecoratedEvent(event, modelSpiAdapter);
			for (final ITableCellEditorListener listener : listeners) {
				listener.editFinished(decoratedEvent);
			}
		}
	}

	public void fireOnEdit(final IVetoable veto, final ITableCellEditEvent event, final TableModelSpiAdapter modelSpiAdapter) {
		if (!listeners.isEmpty()) {
			final ITableCellEditEvent decoratedEvent = createDecoratedEvent(event, modelSpiAdapter);
			for (final ITableCellEditorListener listener : listeners) {
				listener.onEdit(veto, decoratedEvent);
			}
		}
	}

	private ITableCellEditEvent createDecoratedEvent(final ITableCellEditEvent event, final TableModelSpiAdapter modelSpiAdapter) {
		return new TableCellEditEvent(
			event.getRowIndex(),
			modelSpiAdapter.convertViewToModel(event.getColumnIndex()),
			event.getCurrentText());
	}
}
