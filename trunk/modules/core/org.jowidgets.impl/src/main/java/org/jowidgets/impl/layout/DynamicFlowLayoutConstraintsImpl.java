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

package org.jowidgets.impl.layout;

import org.jowidgets.api.layout.IDynamicFlowLayoutConstraints;

final class DynamicFlowLayoutConstraintsImpl implements IDynamicFlowLayoutConstraints {

    private final Integer minWidth;
    private final Integer minHeight;

    private final Integer prefWidth;
    private final Integer prefHeight;

    private final Integer maxWidth;
    private final Integer maxHeight;

    private final boolean growWidth;
    private final boolean growHeight;

    private final Integer useWidthOfElementAt;
    private final Integer useHeightOfElementAt;

    DynamicFlowLayoutConstraintsImpl(final DynamicFlowLayoutConstraintsBuilderImpl builder) {
        this.minWidth = builder.getMinWidth();
        this.minHeight = builder.getMinHeight();
        this.prefWidth = builder.getPrefWidth();
        this.prefHeight = builder.getPrefHeight();
        this.maxWidth = builder.getMaxWidth();
        this.maxHeight = builder.getMaxHeight();
        this.growWidth = builder.isGrowWidth();
        this.growHeight = builder.isGrowHeight();
        this.useWidthOfElementAt = builder.getUseWidthOfElementAt();
        this.useHeightOfElementAt = builder.getUseHeightOfElementAt();
    }

    @Override
    public Integer getMinWidth() {
        return minWidth;
    }

    @Override
    public Integer getMinHeight() {
        return minHeight;
    }

    @Override
    public Integer getPreferredWidth() {
        return prefWidth;
    }

    @Override
    public Integer getPreferredHeight() {
        return prefHeight;
    }

    @Override
    public Integer getMaxWidth() {
        return maxWidth;
    }

    @Override
    public Integer getMaxHeight() {
        return maxHeight;
    }

    @Override
    public boolean isGrowWidth() {
        return growWidth;
    }

    @Override
    public boolean isGrowHeight() {
        return growHeight;
    }

    @Override
    public Integer useWidthOfElementAt() {
        return useWidthOfElementAt;
    }

    @Override
    public Integer useHeightOfElementAt() {
        return useHeightOfElementAt;
    }

}
