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

package org.jowidgets.spi.impl.swing.common.dnd;

import java.awt.Component;
import java.util.Collection;
import java.util.Set;

import org.jowidgets.common.dnd.DropAction;
import org.jowidgets.common.dnd.DropMode;
import org.jowidgets.spi.clipboard.TransferTypeSpi;
import org.jowidgets.spi.dnd.IDropTargetSpi;
import org.jowidgets.spi.impl.dnd.AbstractDropTargetObservableSpi;
import org.jowidgets.util.Assert;

public final class SwingDropTarget extends AbstractDropTargetObservableSpi implements IDropTargetSpi {

    @SuppressWarnings("unused")
    private final Component component;
    @SuppressWarnings("unused")
    private final IDropSelectionProvider dropSelectionProvider;

    public SwingDropTarget(final Component component, final IDropSelectionProvider dropSelectionProvider) {
        Assert.paramNotNull(component, "component");
        Assert.paramNotNull(dropSelectionProvider, "dropSelectionProvider");
        this.component = component;
        this.dropSelectionProvider = dropSelectionProvider;
    }

    @Override
    public void setTransferTypes(final Collection<TransferTypeSpi> supportedTypes) {
        // TODO MG must be implemented
    }

    @Override
    public void setActions(final Set<DropAction> actions) {
        // TODO MG must be implemented
    }

    @Override
    protected void setActive(final boolean active) {
        // TODO MG must be implemented
    }

    @Override
    public void setDefaultDropMode(final DropMode dropMode) {
        // TODO Auto-generated method stub
    }

}
