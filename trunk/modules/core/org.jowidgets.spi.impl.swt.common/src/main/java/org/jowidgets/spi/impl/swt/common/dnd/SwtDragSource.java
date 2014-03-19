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

package org.jowidgets.spi.impl.swt.common.dnd;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Control;
import org.jowidgets.common.dnd.DropAction;
import org.jowidgets.spi.clipboard.TransferContainer;
import org.jowidgets.spi.clipboard.TransferObject;
import org.jowidgets.spi.clipboard.TransferTypeSpi;
import org.jowidgets.spi.dnd.IDragSourceSpi;
import org.jowidgets.spi.impl.dnd.AbstractDragSourceObservableSpi;
import org.jowidgets.spi.impl.dnd.DragDataResponseSpiImpl;
import org.jowidgets.spi.impl.swt.common.clipboard.ObjectTransfer;
import org.jowidgets.spi.impl.types.VetoHolder;
import org.jowidgets.util.Assert;
import org.jowidgets.util.EmptyCheck;

public final class SwtDragSource extends AbstractDragSourceObservableSpi implements IDragSourceSpi {

	private static final Transfer TEXT_TRANSFER = TextTransfer.getInstance();
	private static final Transfer OBJECT_TRANSFER = ObjectTransfer.getInstance();

	private final Control control;

	private Collection<TransferTypeSpi> supportedTypes;
	private Set<DropAction> actions;
	private DragSource dragSource;

	public SwtDragSource(final Control control) {
		Assert.paramNotNull(control, "control");
		this.control = control;
	}

	@Override
	public void setTransferTypes(final Collection<TransferTypeSpi> supportedTypes) {
		this.supportedTypes = new LinkedList<TransferTypeSpi>(supportedTypes);
		if (dragSource != null) {
			dragSource.setTransfer(DragDropUtil.createTransfers(supportedTypes));
		}
		createDragSourceIfNecessary();
	}

	@Override
	public void setActions(final Set<DropAction> actions) {
		if (dragSource != null) {
			dragSource.dispose();
			dragSource = null;
		}
		this.actions = new HashSet<DropAction>(actions);
		createDragSourceIfNecessary();
	}

	@Override
	protected void setActive(final boolean active) {
		if (active) {
			createDragSourceIfNecessary();
		}
		else {
			if (dragSource != null) {
				dragSource.dispose();
				dragSource = null;
			}
		}
	}

	private void createDragSourceIfNecessary() {
		if (dragSource == null && isActive() && !EmptyCheck.isEmpty(actions) && !EmptyCheck.isEmpty(supportedTypes)) {
			this.dragSource = new DragSource(control, DragDropUtil.createOperations(actions));
			dragSource.setTransfer(DragDropUtil.createTransfers(supportedTypes));
			dragSource.addDragListener(new DragSourceListenerImpl());
		}
	}

	private final class DragSourceListenerImpl implements DragSourceListener {

		@Override
		public void dragStart(final DragSourceEvent event) {
			final VetoHolder veto = new VetoHolder();
			fireDragStart(event.x, event.y, veto);
			if (veto.hasVeto()) {
				event.doit = false;
			}
		}

		@Override
		public void dragSetData(final DragSourceEvent event) {
			if (TEXT_TRANSFER.isSupportedType(event.dataType)) {
				final TransferTypeSpi transferType = DragDropUtil.getStringTransferType(supportedTypes);
				if (transferType != null) {
					final DragDataResponseSpiImpl dragData = new DragDataResponseSpiImpl();
					final VetoHolder veto = new VetoHolder();
					fireDragSetData(event.x, event.y, veto, transferType, dragData);
					if (veto.hasVeto()) {
						event.doit = false;
					}
					else {
						event.data = dragData.getData();
					}
				}
				else {
					event.doit = false;
				}
			}
			else if (OBJECT_TRANSFER.isSupportedType(event.dataType)) {
				final List<TransferObject> transferObjectsList = new LinkedList<TransferObject>();
				for (final TransferTypeSpi transferType : DragDropUtil.getTransferObjectTypes(supportedTypes)) {
					final DragDataResponseSpiImpl dragData = new DragDataResponseSpiImpl();
					final VetoHolder veto = new VetoHolder();
					fireDragSetData(event.x, event.y, veto, transferType, dragData);
					if (!veto.hasVeto()) {
						transferObjectsList.add(new TransferObject(transferType, dragData.getData()));
					}
				}
				if (!transferObjectsList.isEmpty()) {
					event.data = new TransferContainer(transferObjectsList);
				}
				else {
					event.doit = false;
				}
			}
			else {
				event.doit = false;
			}
		}

		@Override
		public void dragFinished(final DragSourceEvent event) {
			fireDragFinished(event.x, event.y, DragDropUtil.getDropAction(event.detail));
		}
	}

}
