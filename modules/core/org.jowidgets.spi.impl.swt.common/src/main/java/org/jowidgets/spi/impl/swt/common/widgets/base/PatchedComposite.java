/*
 * Copyright (c) 2016, grossmann
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

package org.jowidgets.spi.impl.swt.common.widgets.base;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.jowidgets.logging.api.ILogger;
import org.jowidgets.logging.api.LoggerProvider;
import org.jowidgets.util.Assert;

/**
 * Fixes https://bugs.eclipse.org/bugs/show_bug.cgi?id=118659
 * fixed since swt 3.7 M4 (tested with win)
 * 
 * @author MGrossmann
 */
final class PatchedComposite extends Composite {

    private static final ILogger LOGGER = LoggerProvider.get(PatchedComposite.class);

    /* The preferred size of a child has changed */
    private static final int LAYOUT_CHANGED = 1 << 6;

    /* Default size for widgets */
    private static final int DEFAULT_WIDTH = 64;
    private static final int DEFAULT_HEIGHT = 64;

    private final Method runSkinMethod;
    private final Method computeSizeMethod;
    private final Method minimumSizeMethod;
    private final Field stateField;

    private boolean failed = false;

    PatchedComposite(final Composite parent, final int style, final PatchedCompositeReflectionAccessor accessor) {
        super(parent, style);
        Assert.paramNotNull(accessor, "accessor");
        this.runSkinMethod = accessor.getRunSkinMethod();
        this.computeSizeMethod = accessor.getComputeSizeMethod();
        this.minimumSizeMethod = accessor.getMinimumSizeMethod();
        this.stateField = accessor.getStateField();
    }

    @Override
    public Point computeSize(final int wHint, final int hHint, final boolean changed) {
        try {
            if (!failed) {
                return patchedComputeSizeImpl(wHint, hHint, changed);
            }
        }
        catch (final Exception e) {
            failed = true;
            LOGGER.error("Composite min size patch can not be applied", e);
        }
        return super.computeSize(wHint, hHint, changed);
    }

    private Point patchedComputeSizeImpl(final int wHint, final int hHint, boolean changed)
            throws IllegalAccessException, InvocationTargetException {
        checkWidget();
        runSkinMethod.invoke(getDisplay());
        final Point size;
        final Layout layout = getLayout();
        if (layout != null) {
            int state = stateField.getInt(this);
            if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
                changed |= (state & LAYOUT_CHANGED) != 0;
                state &= ~LAYOUT_CHANGED;
                stateField.setInt(this, state);
                size = (Point) computeSizeMethod.invoke(layout, this, wHint, hHint, changed);
            }
            else {
                size = new Point(wHint, hHint);
            }
        }
        else {
            size = (Point) minimumSizeMethod.invoke(this, wHint, hHint, changed);
            if (size.x == 0) {
                size.x = DEFAULT_WIDTH; // Do this only when layout is null
            }
            if (size.y == 0) {
                size.y = DEFAULT_HEIGHT;
            }
        }
        if (wHint != SWT.DEFAULT) {
            size.x = wHint;
        }
        if (hHint != SWT.DEFAULT) {
            size.y = hHint;
        }
        final Rectangle trim = computeTrim(0, 0, size.x, size.y);
        return new Point(trim.width, trim.height);
    }

}
