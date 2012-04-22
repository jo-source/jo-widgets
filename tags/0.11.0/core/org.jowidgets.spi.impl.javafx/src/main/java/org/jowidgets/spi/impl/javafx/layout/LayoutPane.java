/*
 * Copyright (c) 2012, David Bauknecht
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

package org.jowidgets.spi.impl.javafx.layout;

import javafx.scene.layout.Pane;

import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.util.Assert;

public class LayoutPane extends Pane {

	private ILayouter layouter;

	public LayoutPane(final ILayouter layouter) {
		Assert.paramNotNull(layouter, "layouter");
		this.layouter = layouter;
	}

	public LayoutPane() {
		this.layouter = null;
	}

	public ILayouter getLayouter() {
		return layouter;
	}

	public void setLayouter(final ILayouter layouter) {
		Assert.paramNotNull(layouter, "layouter");
		this.layouter = layouter;
	}

	@Override
	protected void layoutChildren() {
		layouter.layout();
	}

	@Override
	protected double computeMinWidth(final double paramDouble) {
		return layouter.getMinSize().getWidth();
	}

	@Override
	protected double computeMinHeight(final double paramDouble) {
		return layouter.getMinSize().getHeight();
	}

	@Override
	protected double computePrefHeight(final double paramDouble) {
		return layouter.getPreferredSize().getHeight();
	}

	@Override
	protected double computePrefWidth(final double paramDouble) {
		return layouter.getPreferredSize().getWidth();
	}

	@Override
	protected double computeMaxHeight(final double paramDouble) {
		return layouter.getMaxSize().getHeight();
	}

	@Override
	protected double computeMaxWidth(final double paramDouble) {
		return layouter.getMaxSize().getWidth();
	}
}
