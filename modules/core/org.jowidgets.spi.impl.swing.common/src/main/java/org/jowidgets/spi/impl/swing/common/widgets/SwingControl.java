/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.spi.impl.swing.common.widgets;

import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;

import net.miginfocom.swing.MigLayout;

import org.jowidgets.common.types.Dimension;
import org.jowidgets.spi.dnd.IDragSourceSpi;
import org.jowidgets.spi.dnd.IDropTargetSpi;
import org.jowidgets.spi.impl.swing.common.dnd.IDropSelectionProvider;
import org.jowidgets.spi.impl.swing.common.dnd.ImmutableDropSelection;
import org.jowidgets.spi.impl.swing.common.dnd.SwingDragSource;
import org.jowidgets.spi.impl.swing.common.dnd.SwingDropTarget;
import org.jowidgets.spi.impl.swing.common.layout.LayoutManagerImpl;
import org.jowidgets.spi.impl.swing.common.util.DimensionConvert;
import org.jowidgets.spi.widgets.IControlSpi;
import org.jowidgets.util.Tuple;

public class SwingControl extends SwingComponent implements IControlSpi {

    private final SwingDragSource dragSource;
    private final SwingDropTarget dropTarget;

    public SwingControl(final Tuple<Component, Component> component) {
        this(component, null);
    }

    public SwingControl(final Tuple<Component, Component> component, final IDropSelectionProvider dropSelectionProvider) {
        this(component.getFirst(), component.getSecond(), dropSelectionProvider);
    }

    public SwingControl(final Component component) {
        this(component, null);
    }

    public SwingControl(final Component component, final IDropSelectionProvider dropSelectionProvider) {
        this(component, component, dropSelectionProvider);
    }

    public SwingControl(final Component component, final Component innerComponent, IDropSelectionProvider dropSelectionProvider) {
        super(component, innerComponent);
        dragSource = new SwingDragSource(innerComponent);
        if (dropSelectionProvider == null) {
            if (this instanceof IDropSelectionProvider) {
                dropSelectionProvider = (IDropSelectionProvider) this;
            }
            else {
                dropSelectionProvider = new ImmutableDropSelection(this);
            }
        }
        dropTarget = new SwingDropTarget(innerComponent, dropSelectionProvider);
    }

    @Override
    public void setLayoutConstraints(final Object layoutConstraints) {
        final LayoutManager layoutManager = getParentLayout();
        if (layoutManager instanceof MigLayout) {
            ((MigLayout) layoutManager).setComponentConstraints(getUiReference(), layoutConstraints);
        }
        else if (layoutManager instanceof LayoutManagerImpl) {
            ((LayoutManagerImpl) layoutManager).addLayoutComponent(getUiReference(), layoutConstraints);
        }
        else {
            throw new IllegalStateException("MigLayout expected");
        }
    }

    @Override
    public Object getLayoutConstraints() {
        final LayoutManager layoutManager = getParentLayout();
        if (layoutManager instanceof MigLayout) {
            return ((MigLayout) layoutManager).getComponentConstraints(getUiReference());
        }
        if (layoutManager instanceof LayoutManager) {
            return ((LayoutManagerImpl) layoutManager).getLayoutConstraints(getUiReference());
        }
        else {
            throw new IllegalStateException("MigLayout expected");
        }
    }

    @Override
    public Dimension getMinSize() {
        return DimensionConvert.convert(getUiReference().getMinimumSize());
    }

    @Override
    public Dimension getPreferredSize() {
        return DimensionConvert.convert(getUiReference().getPreferredSize());
    }

    @Override
    public Dimension getMaxSize() {
        return DimensionConvert.convert(getUiReference().getMaximumSize());
    }

    @Override
    public IDragSourceSpi getDragSource() {
        return dragSource;
    }

    @Override
    public IDropTargetSpi getDropTarget() {
        return dropTarget;
    }

    private LayoutManager getParentLayout() {
        final Container container = getUiReference().getParent();
        if (container != null) {
            return container.getLayout();
        }
        return null;
    }

}
