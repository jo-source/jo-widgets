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
import java.util.Set;

import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.widgets.Control;
import org.jowidgets.common.dnd.DropAction;
import org.jowidgets.spi.clipboard.TransferTypeSpi;
import org.jowidgets.spi.dnd.IDropTargetSpi;
import org.jowidgets.spi.impl.dnd.AbstractDropTargetObservableSpi;
import org.jowidgets.util.Assert;
import org.jowidgets.util.EmptyCheck;

public final class SwtDropTarget extends AbstractDropTargetObservableSpi implements IDropTargetSpi {

	private final Control control;
	@SuppressWarnings("unused")
	private final IDropSelectionProvider dropSelectionProvider;

	private Collection<TransferTypeSpi> supportedTypes;
	private Set<DropAction> actions;
	private DropTarget dropTarget;

	public SwtDropTarget(final Control control, final IDropSelectionProvider dropSelectionProvider) {
		Assert.paramNotNull(control, "control");
		Assert.paramNotNull(dropSelectionProvider, "dropSelectionProvider");
		this.control = control;
		this.dropSelectionProvider = dropSelectionProvider;
	}

	@Override
	public void setTransferTypes(final Collection<TransferTypeSpi> supportedTypes) {
		this.supportedTypes = new LinkedList<TransferTypeSpi>(supportedTypes);
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
		public void dragEnter(final DropTargetEvent event) {}

		@Override
		public void dragLeave(final DropTargetEvent event) {}

		@Override
		public void dragOperationChanged(final DropTargetEvent event) {}

		@Override
		public void dragOver(final DropTargetEvent event) {}

		@Override
		public void drop(final DropTargetEvent event) {}

		@Override
		public void dropAccept(final DropTargetEvent event) {}

	}

}
