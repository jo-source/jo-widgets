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
import org.jowidgets.api.layout.IDynamicFlowLayoutConstraintsBuilder;

final class DynamicFlowLayoutConstraintsBuilderImpl implements IDynamicFlowLayoutConstraintsBuilder {

    private Integer minWidth;
    private Integer minHeight;

    private Integer prefWidth;
    private Integer prefHeight;

    private Integer maxWidth;
    private Integer maxHeight;

    private boolean growWidth;
    private boolean growHeight;

    private Integer useWidthOfElementAt;
    private Integer useHeightOfElementAt;

    @Override
    public IDynamicFlowLayoutConstraintsBuilder minWidth(final Integer width) {
        this.minWidth = width;
        return this;
    }

    @Override
    public IDynamicFlowLayoutConstraintsBuilder minHeight(final Integer height) {
        this.minHeight = height;
        return this;
    }

    @Override
    public IDynamicFlowLayoutConstraintsBuilder preferredWidth(final Integer width) {
        this.prefWidth = width;
        return this;
    }

    @Override
    public IDynamicFlowLayoutConstraintsBuilder preferredHeight(final Integer height) {
        this.prefHeight = height;
        return this;
    }

    @Override
    public IDynamicFlowLayoutConstraintsBuilder maxWidth(final Integer width) {
        this.minWidth = width;
        return this;
    }

    @Override
    public IDynamicFlowLayoutConstraintsBuilder maxHeight(final Integer height) {
        this.minHeight = height;
        return this;
    }

    @Override
    public IDynamicFlowLayoutConstraintsBuilder growWidth(final boolean grow) {
        this.growWidth = grow;
        return this;
    }

    @Override
    public IDynamicFlowLayoutConstraintsBuilder growHeight(final boolean grow) {
        this.growHeight = grow;
        return this;
    }

    @Override
    public IDynamicFlowLayoutConstraintsBuilder growWidth() {
        return growWidth(true);
    }

    @Override
    public IDynamicFlowLayoutConstraintsBuilder growHeight() {
        return growHeight(true);
    }

    @Override
    public IDynamicFlowLayoutConstraintsBuilder useWidthOfElementAt(final Integer index) {
        this.useWidthOfElementAt = index;
        return this;
    }

    @Override
    public IDynamicFlowLayoutConstraintsBuilder useHeightOfElementAt(final Integer index) {
        this.useHeightOfElementAt = index;
        return this;
    }

    Integer getMinWidth() {
        return minWidth;
    }

    Integer getMinHeight() {
        return minHeight;
    }

    Integer getPrefWidth() {
        return prefWidth;
    }

    Integer getPrefHeight() {
        return prefHeight;
    }

    Integer getMaxWidth() {
        return maxWidth;
    }

    Integer getMaxHeight() {
        return maxHeight;
    }

    boolean isGrowWidth() {
        return growWidth;
    }

    boolean isGrowHeight() {
        return growHeight;
    }

    Integer getUseWidthOfElementAt() {
        return useWidthOfElementAt;
    }

    Integer getUseHeightOfElementAt() {
        return useHeightOfElementAt;
    }

    @Override
    public IDynamicFlowLayoutConstraints build() {
        return new DynamicFlowLayoutConstraintsImpl(this);
    }

}
