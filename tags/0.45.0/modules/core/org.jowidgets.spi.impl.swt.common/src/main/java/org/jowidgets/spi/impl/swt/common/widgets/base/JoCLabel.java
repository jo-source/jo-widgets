/*
 * Copyright (c) 2015, MGrossmann
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.common.types.EllipsisMode;
import org.jowidgets.util.Assert;

public final class JoCLabel extends CLabel {

    private static final String ELLIPSIS = "...";
    private static final int DRAW_FLAGS = SWT.DRAW_MNEMONIC | SWT.DRAW_TAB | SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER;

    private final EllipsisMode ellipsisMode;

    public JoCLabel(final Composite parent, final int style, final EllipsisMode ellipsis) {
        super(parent, style);
        Assert.paramNotNull(ellipsis, "ellipseMode");
        this.ellipsisMode = ellipsis;
    }

    @Override
    protected String shortenText(final GC gc, final String t, final int width) {
        if (EllipsisMode.LEFT.equals(ellipsisMode)) {
            return shortenTextLeft(gc, t, width);

        }
        else if (EllipsisMode.RIGHT.equals(ellipsisMode)) {
            return shortenTextRight(gc, t, width);

        }
        else if (EllipsisMode.CENTER.equals(ellipsisMode)) {
            return shortenTextCenter(gc, t, width);

        }
        else {
            throw new IllegalStateException("The ellipse mode '" + ellipsisMode + "' is not supported");
        }
    }

    private String shortenTextLeft(final GC gc, final String text, final int width) {
        if (text == null) {
            return null;
        }
        final int ellipseWidth = gc.textExtent(ELLIPSIS, DRAW_FLAGS).x;
        if (width <= ellipseWidth) {
            return ELLIPSIS;
        }
        else if ((gc.textExtent(text, DRAW_FLAGS)).x <= width) {
            return text;
        }

        final int length = text.length();
        for (int index = 0; index < text.length(); index++) {
            final String substring = text.substring(index, length);
            final int subWidth = gc.textExtent(substring, DRAW_FLAGS).x;
            if (ellipseWidth + subWidth <= width) {
                return ELLIPSIS + substring;
            }
        }
        return ELLIPSIS;
    }

    private String shortenTextRight(final GC gc, final String text, final int width) {
        if (text == null) {
            return null;
        }
        final int ellipseWidth = gc.textExtent(ELLIPSIS, DRAW_FLAGS).x;
        if (width <= ellipseWidth) {
            return ELLIPSIS;
        }
        else if ((gc.textExtent(text, DRAW_FLAGS)).x <= width) {
            return text;
        }

        final int length = text.length();
        for (int index = 0; index < text.length(); index++) {
            final String substring = text.substring(0, length - index);
            final int subWidth = gc.textExtent(substring, DRAW_FLAGS).x;
            if (subWidth + ellipseWidth <= width) {
                return substring + ELLIPSIS;
            }
        }
        return ELLIPSIS;
    }

    private String shortenTextCenter(final GC gc, final String text, final int width) {
        if (text == null) {
            return null;
        }
        final int ellipseWidth = gc.textExtent(ELLIPSIS, DRAW_FLAGS).x;
        if (width <= ellipseWidth) {
            return ELLIPSIS;
        }
        else if ((gc.textExtent(text, DRAW_FLAGS)).x <= width) {
            return text;
        }
        final int l = text.length();
        int max = l / 2;
        int min = 0;
        int mid = (max + min) / 2 - 1;
        if (mid <= 0) {
            return ELLIPSIS;
        }
        final TextLayout layout = new TextLayout(getDisplay());
        layout.setText(text);
        mid = calculateOffset(layout, mid);
        while (min < mid && mid < max) {
            final String s1 = text.substring(0, mid);
            final String s2 = text.substring(calculateOffset(layout, l - mid), l);
            final int l1 = gc.textExtent(s1, DRAW_FLAGS).x;
            final int l2 = gc.textExtent(s2, DRAW_FLAGS).x;
            if (l1 + ellipseWidth + l2 > width) {
                max = mid;
                mid = calculateOffset(layout, (max + min) / 2);
            }
            else if (l1 + ellipseWidth + l2 < width) {
                min = mid;
                mid = calculateOffset(layout, (max + min) / 2);
            }
            else {
                min = max;
            }
        }
        final String result = mid == 0 ? ELLIPSIS : text.substring(0, mid)
            + ELLIPSIS
            + text.substring(calculateOffset(layout, l - mid), l);
        layout.dispose();
        return result;
    }

    int calculateOffset(final TextLayout layout, final int offset) {
        final int nextOffset = layout.getNextOffset(offset, SWT.MOVEMENT_CLUSTER);
        if (nextOffset != offset) {
            return layout.getPreviousOffset(nextOffset, SWT.MOVEMENT_CLUSTER);
        }
        return offset;
    }
}
