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
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.jowidgets.common.dnd.DropAction;
import org.jowidgets.common.dnd.DropMode;
import org.jowidgets.common.types.Position;
import org.jowidgets.spi.clipboard.TransferContainer;
import org.jowidgets.spi.clipboard.TransferObject;
import org.jowidgets.spi.clipboard.TransferTypeSpi;
import org.jowidgets.spi.dnd.IDropEventSpi;
import org.jowidgets.spi.dnd.IDropResponseSpi;
import org.jowidgets.spi.dnd.IDropTargetSpi;
import org.jowidgets.spi.impl.dnd.AbstractDropTargetObservableSpi;
import org.jowidgets.spi.impl.dnd.DropEventSpiImpl;
import org.jowidgets.spi.impl.dnd.DropResponseSpiImpl;
import org.jowidgets.spi.impl.swt.common.clipboard.ObjectTransfer;
import org.jowidgets.spi.impl.swt.common.util.PositionConvert;
import org.jowidgets.util.Assert;
import org.jowidgets.util.EmptyCheck;

public final class SwtDropTarget extends AbstractDropTargetObservableSpi implements IDropTargetSpi {

	private static final Transfer TEXT_TRANSFER = TextTransfer.getInstance();
	private static final Transfer OBJECT_TRANSFER = ObjectTransfer.getInstance();

	private final Control control;
	private final IDropSelectionProvider dropSelectionProvider;

	private Set<TransferTypeSpi> supportedTypes;
	private Set<DropAction> actions;
	private DropTarget dropTarget;
	private DropMode defaultDropMode;

	public SwtDropTarget(final Control control, final IDropSelectionProvider dropSelectionProvider) {
		Assert.paramNotNull(control, "control");
		Assert.paramNotNull(dropSelectionProvider, "dropSelectionProvider");
		this.control = control;
		this.dropSelectionProvider = dropSelectionProvider;
	}

	@Override
	public void setTransferTypes(final Collection<TransferTypeSpi> supportedTypes) {
		this.supportedTypes = new LinkedHashSet<TransferTypeSpi>(supportedTypes);
		if (dropTarget != null) {
			dropTarget.setTransfer(DragDropUtil.createTransfers(supportedTypes));
		}
		createDropTargetIfNecessary();
	}

	@Override
	public void setActions(final Set<DropAction> actions) {
		if (dropTarget != null) {
			dropTarget.dispose();
			dropTarget = null;
		}
		this.actions = new HashSet<DropAction>(actions);
		createDropTargetIfNecessary();
	}

	@Override
	public void setDefaultDropMode(final DropMode dropMode) {
		Assert.paramNotNull(dropMode, "dropMode");
		this.defaultDropMode = dropMode;
	}

	@Override
	protected void setActive(final boolean active) {
		if (active) {
			createDropTargetIfNecessary();
		}
		else {
			if (dropTarget != null) {
				dropTarget.dispose();
				dropTarget = null;
			}
		}
	}

	private void createDropTargetIfNecessary() {
		if (dropTarget == null && isActive() && !EmptyCheck.isEmpty(actions) && !EmptyCheck.isEmpty(supportedTypes)) {
			this.dropTarget = new DropTarget(control, DragDropUtil.createOperations(actions));
			dropTarget.setTransfer(DragDropUtil.createTransfers(supportedTypes));
			dropTarget.addDropListener(new DropTargetListenerImpl());
		}
	}

	private final class DropTargetListenerImpl implements DropTargetListener {

		@Override
		public void dragEnter(final DropTargetEvent event) {
			handleDropTargetEvent(event, new IEventPublisher() {
				@Override
				public void fireEvent(final IDropEventSpi dropEvent, final IDropResponseSpi dropResponse) {
					fireDragEnter(dropEvent, dropResponse);
				}
			});
		}

		@Override
		public void dragLeave(final DropTargetEvent event) {
			fireDragExit();
		}

		@Override
		public void dragOperationChanged(final DropTargetEvent event) {
			handleDropTargetEvent(event, new IEventPublisher() {
				@Override
				public void fireEvent(final IDropEventSpi dropEvent, final IDropResponseSpi dropResponse) {
					fireDragOperationChanged(dropEvent, dropResponse);
				}
			});
		}

		@Override
		public void dragOver(final DropTargetEvent event) {
			handleDropTargetEvent(event, new IEventPublisher() {
				@Override
				public void fireEvent(final IDropEventSpi dropEvent, final IDropResponseSpi dropResponse) {
					fireDragOver(dropEvent, dropResponse);
				}
			});
		}

		@Override
		public void drop(final DropTargetEvent event) {
			handleDropTargetEvent(event, new IEventPublisher() {
				@Override
				public void fireEvent(final IDropEventSpi dropEvent, final IDropResponseSpi dropResponse) {
					fireDrop(dropEvent);
				}
			});
		}

		@Override
		public void dropAccept(final DropTargetEvent event) {
			handleDropTargetEvent(event, new IEventPublisher() {
				@Override
				public void fireEvent(final IDropEventSpi dropEvent, final IDropResponseSpi dropResponse) {
					fireDropAccept(dropEvent, dropResponse);
				}
			});
		}

	}

	private void handleDropTargetEvent(final DropTargetEvent event, final IEventPublisher eventPublisher) {
		final Set<DropMode> dropModes = new HashSet<DropMode>();
		final Set<DropAction> dropActions = new HashSet<DropAction>();
		boolean rejected = false;
		for (final IDropEventSpi dropEvent : createDropEvents(event)) {
			final DropResponseSpiImpl dropResponse = new DropResponseSpiImpl();
			eventPublisher.fireEvent(dropEvent, dropResponse);
			final DropAction accepted = dropResponse.getAccepted();
			if (accepted != null) {
				dropActions.add(accepted);
			}
			final DropMode dropMode = dropResponse.getDropMode();
			if (dropMode != null) {
				dropModes.add(dropMode);
			}
			if (dropResponse.isRejected()) {
				rejected = true;
			}
		}
		handleDropActions(dropActions, rejected, event);
		setFeedback(dropModes, event);
	}

	private void handleDropActions(final Set<DropAction> dropActions, final boolean rejected, final DropTargetEvent event) {
		if (dropActions.size() > 1) {
			throw new IllegalStateException("It is not possible to accept more than one drop action for the same event, "
				+ dropActions);
		}
		else if (rejected && dropActions.isEmpty()) {
			event.detail = DND.DROP_NONE;
		}
		else if (dropActions.size() == 1) {
			event.detail = DragDropUtil.createOperations(dropActions);
		}
	}

	private void setFeedback(final Set<DropMode> dropModes, final DropTargetEvent event) {
		if (dropModes.size() > 1) {
			throw new IllegalStateException("It is not possible to set more than one drop mode for the same event, " + dropModes);
		}
		else if (dropModes.size() == 1) {
			setFeedback(dropModes.iterator().next(), event);
		}
		else if (defaultDropMode != null) {
			setFeedback(defaultDropMode, event);
		}
	}

	private void setFeedback(final DropMode dropMode, final DropTargetEvent event) {
		final Integer feedback = dropSelectionProvider.getFeedback(event.item, getPosition(event), dropMode);
		if (feedback != null) {
			event.feedback = feedback.intValue();
		}
	}

	private List<IDropEventSpi> createDropEvents(final DropTargetEvent event) {
		final List<IDropEventSpi> result = new LinkedList<IDropEventSpi>();
		final TransferData transferData = event.currentDataType;
		if (TEXT_TRANSFER.isSupportedType(transferData)) {
			final TransferTypeSpi transferType = DragDropUtil.getStringTransferType(supportedTypes);
			//Only create a event, if this drop target supports the transfer type
			if (transferType != null) {
				result.add(createDropEvent(event, event.data, transferType));
			}
		}
		else if (OBJECT_TRANSFER.isSupportedType(transferData)) {
			final Object data = event.data;
			if (data instanceof TransferContainer) {
				for (final TransferObject transferObject : ((TransferContainer) data).getTransferObjetcs()) {
					final TransferTypeSpi transferType = transferObject.getTransferType();
					//Only create a event, if this drop target supports the transfer type
					if (supportedTypes.contains(transferType)) {
						result.add(createDropEvent(event, transferObject.getData(), transferType));
					}
				}
			}
		}
		return result;
	}

	private IDropEventSpi createDropEvent(final DropTargetEvent event, final Object data, final TransferTypeSpi transferType) {
		return new DropEventSpiImpl(
			getPosition(event),
			getDropSelection(event),
			getSupportedActions(event),
			getDropAction(event),
			data,
			transferType);
	}

	private Position getPosition(final DropTargetEvent event) {
		return PositionConvert.convert(control.toControl(new Point(event.x, event.y)));
	}

	private Object getDropSelection(final DropTargetEvent event) {
		return dropSelectionProvider.getDropSelection(event.item);
	}

	private Set<DropAction> getSupportedActions(final DropTargetEvent event) {
		return DragDropUtil.createActions(event.operations);
	}

	private DropAction getDropAction(final DropTargetEvent event) {
		return DragDropUtil.getDropAction(event.detail);
	}

	private interface IEventPublisher {
		void fireEvent(IDropEventSpi dropEvent, IDropResponseSpi dropResponse);
	}
}
