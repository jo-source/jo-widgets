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

import java.util.Set;

import org.jowidgets.api.clipboard.TransferType;
import org.jowidgets.api.dnd.IDropEvent;
import org.jowidgets.common.dnd.DropAction;
import org.jowidgets.common.types.Position;
import org.jowidgets.spi.dnd.IDropEventSpi;
import org.jowidgets.util.Assert;

final class DropEventImpl implements IDropEvent {

    private final Position position;
    private final Set<DropAction> supportedActions;
    private final DropAction dropAction;
    private final Object dropSelection;
    private final Object data;
    private final TransferType<?> transferType;

    DropEventImpl(final IDropEventSpi dropEventSpi, final Object dropSelection, final TransferType<?> transferType) {
        this(
            dropEventSpi.getPosition(),
            dropEventSpi.getSupportedActions(),
            dropEventSpi.getDropAction(),
            dropEventSpi.getData(),
            dropSelection,
            transferType);
    }

    DropEventImpl(
        final Position position,
        final Set<DropAction> supportedActions,
        final DropAction dropAction,
        final Object data,
        final Object dropSelection,
        final TransferType<?> transferType) {

        Assert.paramNotNull(position, "position");
        Assert.paramNotNull(supportedActions, "supportedActions");
        Assert.paramNotNull(dropAction, "dropAction");
        Assert.paramNotNull(transferType, "transferType");

        this.position = position;
        this.supportedActions = supportedActions;
        this.dropAction = dropAction;
        this.data = data;
        this.dropSelection = dropSelection;
        this.transferType = transferType;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Set<DropAction> getSupportedActions() {
        return supportedActions;
    }

    @Override
    public DropAction getDropAction() {
        return dropAction;
    }

    @Override
    public Object getDropSelection() {
        return dropSelection;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public TransferType<?> getTransferType() {
        return transferType;
    }

    @Override
    public String toString() {
        return "DropEventImpl [position="
            + position
            + ", supportedActions="
            + supportedActions
            + ", dropAction="
            + dropAction
            + ", dropSelection="
            + dropSelection
            + ", data="
            + data
            + ", transferType="
            + transferType
            + "]";
    }

}
