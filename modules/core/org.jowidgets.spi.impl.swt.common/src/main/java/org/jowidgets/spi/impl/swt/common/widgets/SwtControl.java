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
package org.jowidgets.spi.impl.swt.common.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.spi.dnd.IDragSourceSpi;
import org.jowidgets.spi.dnd.IDropTargetSpi;
import org.jowidgets.spi.impl.swt.common.dnd.IDropSelectionProvider;
import org.jowidgets.spi.impl.swt.common.dnd.ImmutableDropSelection;
import org.jowidgets.spi.impl.swt.common.dnd.SwtDragSource;
import org.jowidgets.spi.impl.swt.common.dnd.SwtDropTarget;
import org.jowidgets.spi.impl.swt.common.util.DimensionConvert;
import org.jowidgets.spi.impl.swt.common.widgets.base.IEnhancedLayoutable;
import org.jowidgets.spi.impl.swt.common.widgets.base.IEnhancedLayoutable.LayoutMode;
import org.jowidgets.spi.widgets.IControlSpi;

public class SwtControl extends SwtComponent implements IControlSpi {

    private static final Dimension MAX_SIZE = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);

    private final SwtDragSource dragSource;
    private final SwtDropTarget dropTarget;

    private boolean visible;

    public SwtControl(final Control control) {
        this(control, null);
    }

    public SwtControl(final Control control, IDropSelectionProvider dropSelectionProvider) {
        super(control);
        this.visible = true;
        this.dragSource = new SwtDragSource(control);
        if (dropSelectionProvider == null) {
            if (this instanceof IDropSelectionProvider) {
                dropSelectionProvider = (IDropSelectionProvider) this;
            }
            else {
                dropSelectionProvider = new ImmutableDropSelection(this);
            }
        }
        this.dropTarget = new SwtDropTarget(control, dropSelectionProvider);
    }

    @Override
    public void setLayoutConstraints(final Object layoutConstraints) {
        getUiReference().setLayoutData(layoutConstraints);
    }

    @Override
    public Object getLayoutConstraints() {
        return getUiReference().getLayoutData();
    }

    @Override
    public Dimension getMinSize() {
        final Control uiReference = getUiReference();
        return DimensionConvert.convert(uiReference.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    @Override
    public Dimension getPreferredSize() {
        final Point result;
        final Control uiReference = getUiReference();
        if (uiReference instanceof Composite) {
            setLayoutMode((Composite) uiReference, LayoutMode.PREFFERED_SIZE);
            result = uiReference.computeSize(SWT.DEFAULT, SWT.DEFAULT);
            setLayoutMode((Composite) uiReference, LayoutMode.MIN_SIZE);
        }
        else {
            result = uiReference.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        }
        return DimensionConvert.convert(result);
    }

    private void setLayoutMode(final Composite composite, final LayoutMode layoutMode) {
        if (composite instanceof IEnhancedLayoutable) {
            ((IEnhancedLayoutable) composite).setLayoutMode(layoutMode);
        }
        for (final Control control : composite.getChildren()) {
            if (control instanceof Composite) {
                setLayoutMode((Composite) control, layoutMode);
            }
        }
    }

    @Override
    public Dimension getMaxSize() {
        return MAX_SIZE;
    }

    @Override
    public IDragSourceSpi getDragSource() {
        return dragSource;
    }

    @Override
    public IDropTargetSpi getDropTarget() {
        return dropTarget;
    }

    @Override
    public void setVisible(final boolean visible) {
        this.visible = visible;
        super.setVisible(visible);
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

}
