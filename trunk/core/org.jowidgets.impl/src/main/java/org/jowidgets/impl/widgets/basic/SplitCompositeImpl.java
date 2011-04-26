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

package org.jowidgets.impl.widgets.basic;

import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ISplitComposite;
import org.jowidgets.api.widgets.descriptor.setup.ISplitCompositeSetup;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Orientation;
import org.jowidgets.impl.base.delegate.ControlDelegate;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.impl.widgets.common.wrapper.AbstractSplitCompositeSpiWrapper;
import org.jowidgets.spi.widgets.ISplitCompositeSpi;

public class SplitCompositeImpl extends AbstractSplitCompositeSpiWrapper implements ISplitComposite {

	private final ControlDelegate controlDelegate;
	private final ContainerImpl first;
	private final ContainerImpl second;
	private final int dividerSize;
	private final Orientation orientation;

	private Dimension firstClientAreaMinSize;
	private Dimension secondClientAreaMinSize;

	public SplitCompositeImpl(final ISplitCompositeSpi containerWidgetSpi, final ISplitCompositeSetup setup) {
		super(containerWidgetSpi);
		this.controlDelegate = new ControlDelegate();
		this.first = new ContainerImpl(getWidget().getFirst());
		this.second = new ContainerImpl(getWidget().getSecond());
		this.dividerSize = setup.getDividerSize();
		this.orientation = setup.getOrientation();
		this.first.setParent(this);
		this.second.setParent(this);
		VisibiliySettingsInvoker.setVisibility(setup, this);
		ColorSettingsInvoker.setColors(setup, this);
	}

	@Override
	public IContainer getParent() {
		return controlDelegate.getParent();
	}

	@Override
	public void setParent(final IContainer parent) {
		controlDelegate.setParent(parent);
	}

	@Override
	public boolean isReparentable() {
		return controlDelegate.isReparentable();
	}

	@Override
	public IContainer getFirst() {
		return first;
	}

	@Override
	public IContainer getSecond() {
		return second;
	}

	@Override
	public void setMinSizes(final Dimension firstMinSize, final Dimension secondMinSize) {
		this.firstClientAreaMinSize = firstMinSize;
		this.secondClientAreaMinSize = secondMinSize;
		getWidget().setMinSizes(firstMinSize, secondMinSize);
	}

	@Override
	public Dimension getFirstMinSize() {
		return firstClientAreaMinSize;
	}

	@Override
	public Dimension getSecondMinSize() {
		return secondClientAreaMinSize;
	}

	@Override
	public Dimension getMinSize() {
		final Dimension firstMinSize = first.getMinSize();
		final Dimension secondMinSize = second.getMinSize();

		int width = firstMinSize != null ? firstMinSize.getWidth() : 0;
		int height = firstMinSize != null ? firstMinSize.getHeight() : 0;

		width = width + (secondMinSize != null ? secondMinSize.getWidth() : 0);
		height = height + (secondMinSize != null ? secondMinSize.getHeight() : 0);

		if (Orientation.VERTICAL == orientation) {
			height = height + dividerSize;
		}
		else {
			width = width + dividerSize;
		}

		return new Dimension(width, height);
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return new PopupMenuImpl(getWidget().createPopupMenu(), this);
	}

}
