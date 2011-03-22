/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.workbench.toolkit.impl;

import org.jowidgets.common.types.Orientation;
import org.jowidgets.common.types.SplitResizePolicy;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.ILayoutContainer;
import org.jowidgets.workbench.api.ISplitLayout;

class SplitLayout implements ISplitLayout {

	private final Orientation orientation;
	private final double weight;
	private final SplitResizePolicy resizePolicy;
	private final ILayoutContainer firstLayoutContainer;
	private final ILayoutContainer secondLayoutContainer;

	SplitLayout(
		final Orientation orientation,
		final double weight,
		final SplitResizePolicy resizePolicy,
		final ILayoutContainer firstLayoutContainer,
		final ILayoutContainer secondLayoutContainer) {

		Assert.paramNotNull(orientation, "orientation");
		Assert.paramNotNull(firstLayoutContainer, "firstLayoutContainer");
		Assert.paramNotNull(secondLayoutContainer, "secondLayoutContainer");

		this.orientation = orientation;
		this.weight = weight;
		this.resizePolicy = resizePolicy;
		this.firstLayoutContainer = firstLayoutContainer;
		this.secondLayoutContainer = secondLayoutContainer;
	}

	@Override
	public Orientation getOrientation() {
		return orientation;
	}

	@Override
	public double getWeight() {
		return weight;
	}

	@Override
	public SplitResizePolicy getResizePolicy() {
		return resizePolicy;
	}

	@Override
	public ILayoutContainer getFirstContainer() {
		return firstLayoutContainer;
	}

	@Override
	public ILayoutContainer getSecondContainer() {
		return secondLayoutContainer;
	}

}
