/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.spi.impl.dummy.ui;

import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.util.Assert;

public class UIDScrollPane extends UIDContainer {

    private final UIDScrollBar horizontalScrollBar;
    private final UIDScrollBar verticalScrollBar;

    private boolean verticalBarVisible;
    private boolean horizontalBarVisible;
    private boolean alwaysShowBars;
    private Position viewPosition;
    private Dimension viewSize;

    public UIDScrollPane() {
        this.viewPosition = new Position(0, 0);
        this.viewSize = new Dimension(0, 0);
        this.horizontalScrollBar = new UIDScrollBar();
        this.verticalScrollBar = new UIDScrollBar();
    }

    public boolean isVerticalBar() {
        return verticalBarVisible;
    }

    public void setVerticalBar(final boolean verticalBar) {
        this.verticalBarVisible = verticalBar;
    }

    public boolean isHorizontalBar() {
        return horizontalBarVisible;
    }

    public void setHorizontalBar(final boolean horizontalBar) {
        this.horizontalBarVisible = horizontalBar;
    }

    public UIDScrollBar getHorizontalScrollBar() {
        return horizontalScrollBar;
    }

    public UIDScrollBar getVerticalScrollBar() {
        return verticalScrollBar;
    }

    public boolean isAlwaysShowBars() {
        return alwaysShowBars;
    }

    public void setAlwaysShowBars(final boolean alwaysShowBars) {
        this.alwaysShowBars = alwaysShowBars;
    }

    public void setViewPosition(final Position position) {
        Assert.paramNotNull(position, "position");
        this.viewPosition = position;
    }

    public Position getViewPosition() {
        return viewPosition;
    }

    public Dimension getViewSize() {
        return viewSize;
    }

    public void setViewSize(final Dimension size) {
        Assert.paramNotNull(size, "size");
        this.viewSize = size;
    }

}
