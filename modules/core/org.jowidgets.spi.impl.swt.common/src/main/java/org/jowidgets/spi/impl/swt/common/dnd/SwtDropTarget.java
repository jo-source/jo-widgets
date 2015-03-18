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
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
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

    private static final TextTransfer TEXT_TRANSFER = TextTransfer.getInstance();
    private static final ObjectTransfer OBJECT_TRANSFER = ObjectTransfer.getInstance();

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
        final IDropEventSpi dropEvent = createDropEvent(event);
        final DropResponseSpiImpl dropResponse = new DropResponseSpiImpl();
        eventPublisher.fireEvent(dropEvent, dropResponse);
        handleDropAction(dropResponse.getAccepted(), dropResponse.isRejected(), event);
        setFeedback(dropResponse.getDropMode(), event);
    }

    private void handleDropAction(final DropAction dropAction, final boolean rejected, final DropTargetEvent event) {
        if (rejected && dropAction == null) {
            event.detail = DND.DROP_NONE;
        }
        else if (dropAction != null) {
            event.detail = DragDropUtil.createOperations(Collections.singleton(dropAction));
        }
    }

    private void setFeedback(final DropMode dropMode, final DropTargetEvent event) {
        if (dropMode != null) {
            setFeedbackImpl(dropMode, event);
        }
        else if (defaultDropMode != null) {
            setFeedbackImpl(defaultDropMode, event);
        }
    }

    private void setFeedbackImpl(final DropMode dropMode, final DropTargetEvent event) {
        final Integer feedback = dropSelectionProvider.getFeedback(event.item, getPosition(event), dropMode);
        if (feedback != null) {
            event.feedback = feedback.intValue();
        }
    }

    private IDropEventSpi createDropEvent(final DropTargetEvent event) {
        final TransferData transferData = event.currentDataType;
        if (TEXT_TRANSFER.isSupportedType(transferData)) {
            final TransferTypeSpi transferType = DragDropUtil.getStringTransferType(supportedTypes);
            //Only create a event, if this drop target supports the transfer type
            if (transferType != null) {
                Object data = event.data;
                if (data == null) {
                    data = TEXT_TRANSFER.nativeToJava(transferData);
                }
                return createDropEvent(event, data, transferType);
            }
        }
        else if (OBJECT_TRANSFER.isSupportedType(transferData)) {
            Object data = event.data;
            if (data == null) {
                data = OBJECT_TRANSFER.nativeToJava(transferData);
            }
            if (data instanceof TransferContainer) {
                for (final TransferObject transferObject : ((TransferContainer) data).getTransferObjetcs()) {
                    final TransferTypeSpi transferType = transferObject.getTransferType();
                    //Only create a event, if this drop target supports the transfer type
                    if (supportedTypes.contains(transferType)) {
                        return createDropEvent(event, transferObject.getData(), transferType);
                    }
                }
            }
        }
        return createDropEvent(event, null, TransferTypeSpi.UNKNOWN_TYPE);
    }

    private IDropEventSpi createDropEvent(final DropTargetEvent event, final Object data, final TransferTypeSpi transferType) {
        final Position position = getPosition(event);
        return new DropEventSpiImpl(
            position,
            getDropSelection(event, position),
            getSupportedActions(event),
            getDropAction(event),
            data,
            transferType);
    }

    private Position getPosition(final DropTargetEvent event) {
        return PositionConvert.convert(control.toControl(new Point(event.x, event.y)));
    }

    private Object getDropSelection(final DropTargetEvent event, final Position position) {
        return dropSelectionProvider.getDropSelection(event.item, position, event.feedback);
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
