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

package org.jowidgets.tools.powo;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jowidgets.api.clipboard.TransferType;
import org.jowidgets.api.dnd.IDragSource;
import org.jowidgets.api.dnd.IDragSourceListener;
import org.jowidgets.common.dnd.DnD;
import org.jowidgets.util.Assert;

final class DragSourcePowo implements IDragSource {

	private final Set<IDragSourceListener> listeners;

	private IDragSource original;

	private Collection<TransferType<?>> supportedTypes;
	private Set<DnD> actions;

	DragSourcePowo() {
		this.listeners = new LinkedHashSet<IDragSourceListener>();
	}

	void setOriginal(final IDragSource original) {
		this.original = original;
		for (final IDragSourceListener listener : listeners) {
			original.addDragSourceListener(listener);
		}
		if (supportedTypes != null) {
			original.setTransferTypes(supportedTypes);
		}
		if (actions != null) {
			original.setActions(actions);
		}
		listeners.clear();
		supportedTypes = null;
		actions = null;
	}

	@Override
	public void addDragSourceListener(final IDragSourceListener listener) {
		Assert.paramNotNull(listener, "listener");
		if (original != null) {
			original.addDragSourceListener(listener);
		}
		else {
			listeners.add(listener);
		}
	}

	@Override
	public void removeDragSourceListener(final IDragSourceListener listener) {
		Assert.paramNotNull(listener, "listener");
		if (original != null) {
			original.removeDragSourceListener(listener);
		}
		else {
			listeners.remove(listener);
		}
	}

	@Override
	public void setTransferTypes(final Collection<TransferType<?>> supportedTypes) {
		Assert.paramNotNull(supportedTypes, "supportedTypes");
		if (original != null) {
			original.setTransferTypes(supportedTypes);
		}
		else {
			this.supportedTypes = new LinkedList<TransferType<?>>(supportedTypes);
		}
	}

	@Override
	public void setActions(final Set<DnD> actions) {
		Assert.paramNotNull(actions, "actions");
		if (original != null) {
			original.setActions(actions);
		}
		else {
			this.actions = new HashSet<DnD>(actions);
		}
	}

}
