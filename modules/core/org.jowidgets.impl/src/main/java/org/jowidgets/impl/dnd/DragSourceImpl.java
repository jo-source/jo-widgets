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

package org.jowidgets.impl.dnd;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jowidgets.api.clipboard.TransferType;
import org.jowidgets.api.dnd.IDragSource;
import org.jowidgets.common.dnd.DropAction;
import org.jowidgets.common.types.IVetoable;
import org.jowidgets.spi.clipboard.TransferTypeSpi;
import org.jowidgets.spi.dnd.IDragDataResponseSpi;
import org.jowidgets.spi.dnd.IDragEventSpi;
import org.jowidgets.spi.dnd.IDragSourceListenerSpi;
import org.jowidgets.spi.dnd.IDragSourceSpi;
import org.jowidgets.util.Assert;

public final class DragSourceImpl extends AbstractDragSourceObservable implements IDragSource {

	private final IDragSourceSpi dragSourceSpi;
	private final IDragSourceListenerSpi dragSourceListenerSpi;

	private Map<TransferTypeSpi, TransferType<?>> supportedTypes;

	public DragSourceImpl(final IDragSourceSpi dragSourceSpi) {
		Assert.paramNotNull(dragSourceSpi, "dragSourceSpi");
		this.dragSourceSpi = dragSourceSpi;
		this.dragSourceListenerSpi = new DragSourceListenerSpi();
	}

	@Override
	public void setTransferTypes(final Collection<TransferType<?>> types) {
		Assert.paramNotNull(types, "types");
		this.supportedTypes = new LinkedHashMap<TransferTypeSpi, TransferType<?>>();
		final List<TransferTypeSpi> transferTypesSpi = new LinkedList<TransferTypeSpi>();
		for (final TransferType<?> transferType : types) {
			final TransferTypeSpi transferTypeSpi = new TransferTypeSpi(transferType.getJavaType());
			transferTypesSpi.add(transferTypeSpi);
			supportedTypes.put(transferTypeSpi, transferType);
		}
		dragSourceSpi.setTransferTypes(transferTypesSpi);
	}

	@Override
	public void setTransferTypes(final TransferType<?>... supportedTypes) {
		Assert.paramNotNull(supportedTypes, "supportedTypes");
		setTransferTypes(Arrays.asList(supportedTypes));
	}

	@Override
	public void setActions(final Set<DropAction> actions) {
		Assert.paramNotNull(actions, "actions");
		dragSourceSpi.setActions(actions);
	}

	@Override
	public void setActions(final DropAction... actions) {
		Assert.paramNotNull(actions, "actions");
		final Set<DropAction> actionsSet = new HashSet<DropAction>();
		for (int i = 0; i < actions.length; i++) {
			actionsSet.add(actions[i]);
		}
		setActions(actionsSet);
	}

	@Override
	protected void setActive(final boolean active) {
		if (active) {
			dragSourceSpi.addDragSourceListenerSpi(dragSourceListenerSpi);
		}
		else {
			dragSourceSpi.removeDragSourceListenerSpi(dragSourceListenerSpi);
		}
	}

	private final class DragSourceListenerSpi implements IDragSourceListenerSpi {

		@Override
		public void dragStart(final IDragEventSpi event, final IVetoable veto) {
			fireDragStart(new DragEventImpl(event), veto);
		}

		@Override
		public void dragSetData(
			final IDragEventSpi event,
			final IVetoable veto,
			final TransferTypeSpi transferTypeSpi,
			final IDragDataResponseSpi dragData) {
			final TransferType<?> transferType = supportedTypes.get(transferTypeSpi);
			fireDragSetData(new DragEventImpl(event), veto, transferType, new DragDataResponseImpl(dragData));
		}

		@Override
		public void dragFinished(final IDragEventSpi event, final DropAction dropAction) {
			fireDragFinished(new DragEventImpl(event), dropAction);
		}

	}

}
