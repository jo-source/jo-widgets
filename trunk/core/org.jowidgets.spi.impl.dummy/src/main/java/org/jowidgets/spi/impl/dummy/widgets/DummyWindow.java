/*
 * Copyright (c) 2010, Michael Grossmann
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
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.spi.impl.dummy.widgets;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IButtonCommon;
import org.jowidgets.common.widgets.IDisplayCommon;
import org.jowidgets.common.widgets.controler.IWindowListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.impl.dummy.dummyui.UIDWindow;
import org.jowidgets.spi.impl.dummy.image.DummyImageRegistry;
import org.jowidgets.spi.widgets.IWindowSpi;

public class DummyWindow extends DummyContainer implements IWindowSpi {

	public DummyWindow(final IGenericWidgetFactory factory, final UIDWindow window) {
		super(factory, window);
	}

	@Override
	public UIDWindow getUiReference() {
		return (UIDWindow) super.getUiReference();
	}

	public void setDefaultButton(final IButtonCommon button) {
		// TODO LG default button must be simulated
	}

	@Override
	public void pack() {
		getUiReference().pack();
	}

	@Override
	public void setVisible(final boolean visible) {
		getUiReference().setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return getUiReference().isVisible();
	}

	@Override
	public final void setPosition(final Position position) {
		getUiReference().setPosition(position);
	}

	@Override
	public final Position getPosition() {
		return getUiReference().getPosition();
	}

	@Override
	public final void setSize(final Dimension size) {
		getUiReference().setSize(size);
	}

	public void setMinSize(final Dimension minSize) {
		getUiReference().setMinSize(minSize);
	}

	@Override
	public final Dimension getSize() {
		return getUiReference().getSize();
	}

	@Override
	public <WIDGET_TYPE extends IDisplayCommon, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE createChildWindow(
		final DESCRIPTOR_TYPE descriptor) {
		return getGenericWidgetFactory().create(getUiReference(), descriptor);
	}

	protected void setIcon(final IImageConstant icon, final DummyImageRegistry imageRegistry) {
		getUiReference().setIconImage(imageRegistry.getImage(icon));
	}

	@Override
	public Rectangle getParentBounds() {
		Dimension parentSize;
		Position parentPosition = new Position(0, 0);
		if (getUiReference().getParent() != null) {
			parentPosition = getUiReference().getParent().getPosition();
			parentSize = getUiReference().getParent().getSize();
		}
		else {
			parentSize = new Dimension(1920, 1200);
		}
		return new Rectangle(parentPosition, parentSize);
	}

	@Override
	public void addWindowListener(final IWindowListener listener) {
		getUiReference().addWindowListener(listener);
	}

	@Override
	public void removeWindowListener(final IWindowListener listener) {
		getUiReference().removeWindowListener(listener);
	}

	@Override
	public void dispose() {
		getUiReference().dispose();
	}

}
