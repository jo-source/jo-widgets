/*
 * Copyright (c) 2011, Lukas Gross
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

package org.jowidgets.impl.event;

import java.util.HashSet;
import java.util.Set;

import org.jowidgets.api.model.IListItemListener;
import org.jowidgets.api.model.IListItemObservable;
import org.jowidgets.api.widgets.IWidget;

public class ListItemObservable implements IListItemObservable {

	private final Set<IListItemListener> itemContainerListeners;

	public ListItemObservable() {
		super();
		this.itemContainerListeners = new HashSet<IListItemListener>();
	}

	@Override
	public final void addItemContainerListener(final IListItemListener listener) {
		itemContainerListeners.add(listener);
	}

	@Override
	public final void removeItemContainerListener(final IListItemListener listener) {
		itemContainerListeners.remove(listener);
	}

	public final void fireItemAdded(final IWidget item) {
		for (final IListItemListener listener : itemContainerListeners) {
			listener.itemAdded(item);
		}
	}

	public final void fireItemRemoved(final IWidget item) {
		for (final IListItemListener listener : itemContainerListeners) {
			listener.itemRemoved(item);
		}
	}
}
