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

package org.jowidgets.spi.impl.swing.common.widgets;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JScrollBar;

import org.jowidgets.common.types.Dimension;
import org.jowidgets.spi.impl.controller.VisibilityStateObservable;
import org.jowidgets.spi.impl.swing.common.util.DimensionConvert;
import org.jowidgets.spi.widgets.IScrollBarSpi;
import org.jowidgets.util.Assert;

final class ScrollBarImpl extends VisibilityStateObservable implements IScrollBarSpi {

    private final JScrollBar scrollBar;

    ScrollBarImpl(final JScrollBar scrollBar) {
        super(scrollBar.isVisible());
        Assert.paramNotNull(scrollBar, "scrollBar");

        this.scrollBar = scrollBar;

        scrollBar.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentShown(final ComponentEvent e) {
                fireVisibilityStateChanged(isVisible());
            }

            @Override
            public void componentHidden(final ComponentEvent e) {
                fireVisibilityStateChanged(isVisible());
            }
        });
    }

    @Override
    public JScrollBar getUiReference() {
        return scrollBar;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        scrollBar.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return scrollBar.isEnabled();
    }

    @Override
    public boolean isVisible() {
        return scrollBar.isVisible();
    }

    @Override
    public Dimension getSize() {
        return DimensionConvert.convert(getUiReference().getSize());
    }

}
