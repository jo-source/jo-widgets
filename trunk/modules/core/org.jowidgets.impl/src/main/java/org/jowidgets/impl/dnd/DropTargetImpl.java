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

import java.util.Collection;
import java.util.Set;

import org.jowidgets.api.clipboard.TransferType;
import org.jowidgets.api.dnd.IDropEvent;
import org.jowidgets.api.dnd.IDropTarget;
import org.jowidgets.common.dnd.DropAction;
import org.jowidgets.common.dnd.DropMode;
import org.jowidgets.spi.clipboard.TransferTypeSpi;
import org.jowidgets.spi.dnd.IDropEventSpi;
import org.jowidgets.spi.dnd.IDropResponseSpi;
import org.jowidgets.spi.dnd.IDropTargetListenerSpi;
import org.jowidgets.spi.dnd.IDropTargetSpi;
import org.jowidgets.util.Assert;

public final class DropTargetImpl extends AbstractDropTargetObservable implements IDropTarget {

	private final IDropTargetSpi dropTargetSpi;
	private final IDropSelectionProvider dropSelectionProvider;
	private final IDropTargetListenerSpi dropTargetListenerSpi;
	private final DragDropDelegate dragDropDelegate;

	public DropTargetImpl(final IDropTargetSpi dropTargetSpi, final IDropSelectionProvider dropSelectionProvider) {

		Assert.paramNotNull(dropTargetSpi, "dropTargetSpi");
		Assert.paramNotNull(dropSelectionProvider, "dropSelectionProvider");

		this.dropTargetSpi = dropTargetSpi;
		this.dropSelectionProvider = dropSelectionProvider;

		this.dropTargetListenerSpi = new DropTargetListenerSpi();
		this.dragDropDelegate = new DragDropDelegate(new IDragDropSpiSupport() {

			@Override
			public void setTransferTypesSpi(final Collection<TransferTypeSpi> supportedTypes) {
				dropTargetSpi.setTransferTypes(supportedTypes);
			}

			@Override
			public void setActionsSpi(final Set<DropAction> actions) {
				dropTargetSpi.setActions(actions);
			}
		});
	}

	@Override
	public void setTransferTypes(final Collection<TransferType<?>> types) {
		dragDropDelegate.setTransferTypes(types);
	}

	@Override
	public void setTransferTypes(final TransferType<?>... supportedTypes) {
		dragDropDelegate.setTransferTypes(supportedTypes);
	}

	@Override
	public void setActions(final Set<DropAction> actions) {
		dragDropDelegate.setActions(actions);
	}

	@Override
	public void setActions(final DropAction... actions) {
		dragDropDelegate.setActions(actions);
	}

	@Override
	public void setDefaultDropMode(final DropMode dropMode) {
		dropTargetSpi.setDefaultDropMode(dropMode);
	}

	@Override
	protected void setActive(final boolean active) {
		if (active) {
			dropTargetSpi.addDropTargetListenerSpi(dropTargetListenerSpi);
		}
		else {
			dropTargetSpi.removeDropTargetListenerSpi(dropTargetListenerSpi);
		}
	}

	private final class DropTargetListenerSpi implements IDropTargetListenerSpi {

		@Override
		public void dragEnter(final IDropEventSpi event, final IDropResponseSpi response) {
			final TransferType<?> transferType = dragDropDelegate.getTransferType(event.getTransferType());
			if (transferType != null) {
				fireDragEnter(createDropEvent(event, transferType), new DropResponseImpl(response));
			}
		}

		@Override
		public void dragOver(final IDropEventSpi event, final IDropResponseSpi response) {
			final TransferType<?> transferType = dragDropDelegate.getTransferType(event.getTransferType());
			if (transferType != null) {
				fireDragOver(createDropEvent(event, transferType), new DropResponseImpl(response));
			}
		}

		@Override
		public void dragOperationChanged(final IDropEventSpi event, final IDropResponseSpi response) {
			final TransferType<?> transferType = dragDropDelegate.getTransferType(event.getTransferType());
			if (transferType != null) {
				fireDragOperationChanged(createDropEvent(event, transferType), new DropResponseImpl(response));
			}
		}

		@Override
		public void dragExit() {
			fireDragExit();
		}

		@Override
		public void dropAccept(final IDropEventSpi event, final IDropResponseSpi response) {
			final TransferType<?> transferType = dragDropDelegate.getTransferType(event.getTransferType());
			if (transferType != null) {
				fireDropAccept(createDropEvent(event, transferType), new DropResponseImpl(response));
			}
		}

		@Override
		public void drop(final IDropEventSpi event) {
			final TransferType<?> transferType = dragDropDelegate.getTransferType(event.getTransferType());
			if (transferType != null) {
				fireDrop(createDropEvent(event, transferType));
			}
		}

		private IDropEvent createDropEvent(final IDropEventSpi dropEventSpi, final TransferType<?> transferType) {
			return new DropEventImpl(
				dropEventSpi,
				dropSelectionProvider.getDropSelection(dropEventSpi.getDropSelection()),
				transferType);
		}

	}

}
