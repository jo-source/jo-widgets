/*
 * Copyright (c) 2011, grossmann
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
import org.jowidgets.workbench.toolkit.api.ILayoutContainerBuilder;
import org.jowidgets.workbench.toolkit.api.ISplitLayoutBuilder;

class SplitLayoutBuilder implements ISplitLayoutBuilder {

	private Orientation orientation;
	private double weight;
	private SplitResizePolicy splitResizePolicy;
	private ILayoutContainer firstContainer;
	private ILayoutContainer secondContainer;

	SplitLayoutBuilder() {
		super();
	}

	@Override
	public ISplitLayoutBuilder setOrientation(final Orientation orientation) {
		Assert.paramNotNull(orientation, "orientation");
		this.orientation = orientation;
		return this;
	}

	@Override
	public ISplitLayoutBuilder setHorizontal() {
		return setOrientation(Orientation.HORIZONTAL);
	}

	@Override
	public ISplitLayoutBuilder setVertical() {
		return setOrientation(Orientation.VERTICAL);
	}

	@Override
	public ISplitLayoutBuilder setWeight(final double weigth) {
		this.weight = weigth;
		return this;
	}

	@Override
	public ISplitLayoutBuilder setResizePolicy(final SplitResizePolicy splitResizePolicy) {
		Assert.paramNotNull(splitResizePolicy, "splitResizePolicy");
		this.splitResizePolicy = splitResizePolicy;
		return this;
	}

	@Override
	public ISplitLayoutBuilder setResizeFirst() {
		return setResizePolicy(SplitResizePolicy.RESIZE_FIRST);
	}

	@Override
	public ISplitLayoutBuilder setResizeSecond() {
		return setResizePolicy(SplitResizePolicy.RESIZE_SECOND);
	}

	@Override
	public ISplitLayoutBuilder setResizeBoth() {
		return setResizePolicy(SplitResizePolicy.RESIZE_BOTH);
	}

	@Override
	public ISplitLayoutBuilder setFirstContainer(final ILayoutContainer firstContainer) {
		Assert.paramNotNull(firstContainer, "firstContainer");
		this.firstContainer = firstContainer;
		return this;
	}

	@Override
	public ISplitLayoutBuilder setFirstContainer(final ILayoutContainerBuilder firstContainerBuilder) {
		Assert.paramNotNull(firstContainerBuilder, "firstContainerBuilder");
		this.firstContainer = firstContainerBuilder.build();
		return this;
	}

	@Override
	public ISplitLayoutBuilder setSecondContainer(final ILayoutContainer secondContainer) {
		Assert.paramNotNull(secondContainer, "secondContainer");
		this.secondContainer = secondContainer;
		return this;
	}

	@Override
	public ISplitLayoutBuilder setSecondContainer(final ILayoutContainerBuilder secondContainerBuilder) {
		Assert.paramNotNull(secondContainerBuilder, "secondContainerBuilder");
		this.secondContainer = secondContainerBuilder.build();
		return this;
	}

	@Override
	public ISplitLayout build() {
		return new SplitLayout(orientation, weight, splitResizePolicy, firstContainer, secondContainer);
	}

}
