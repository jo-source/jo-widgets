/*
 * Copyright (c) 2010, grossmann, Nikolaus Moll
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

package org.jowidgets.impl.base.delegate;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.types.AutoCenterPolicy;
import org.jowidgets.api.types.AutoPackPolicy;
import org.jowidgets.api.types.AutoPositionCorrectionPolicy;
import org.jowidgets.api.widgets.IDisplay;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.descriptor.setup.IWindowSetup;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.spi.widgets.IWindowSpi;
import org.jowidgets.tools.controller.WindowAdapter;

public class WindowDelegate {

	private final List<IDisplay> childWindows;
	private final AutoCenterPolicy autoCenterPolicy;
	private final AutoPackPolicy autoPackPolicy;
	private Dimension minPackSize;
	private Dimension maxPackSize;
	private final AutoPositionCorrectionPolicy autoPositionCorrectionPolicy;
	private final IWindowSpi windowSpi;
	private final IWindow window;
	private boolean wasVisible;
	private boolean positionSet;
	private boolean positionCorrected;
	private boolean sizeSet;

	public WindowDelegate(final IWindowSpi windowSpi, final IWindow window, final IWindowSetup setup) {
		this.childWindows = new LinkedList<IDisplay>();
		this.autoCenterPolicy = setup.getAutoCenterPolicy();
		this.autoPackPolicy = setup.getAutoPackPolicy();
		this.minPackSize = setup.getMinPackSize();
		this.maxPackSize = setup.getMaxPackSize();
		this.autoPositionCorrectionPolicy = setup.getAutoPositionCorrectionPolicy();
		this.windowSpi = windowSpi;
		this.window = window;
		this.wasVisible = false;
		this.positionSet = false;
		this.positionCorrected = false;
		this.sizeSet = false;

		if (setup.getSize() != null) {
			setSize(setup.getSize());
		}

		if (setup.getPosition() != null) {
			setPosition(setup.getPosition());
		}

		if (setup.getAutoDispose()) {
			windowSpi.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed() {
					if (!window.isDisposed()) {
						window.dispose();
					}
				}
			});
		}
	}

	public void dispose() {
		for (final IDisplay childWindow : new LinkedList<IDisplay>(childWindows)) {
			childWindow.dispose();
		}
		childWindows.clear();
	}

	public void centerLocation() {
		final Rectangle parentBounds = windowSpi.getParentBounds();
		final Dimension parentSize = parentBounds.getSize();
		final Position parentPosition = parentBounds.getPosition();

		final Dimension size = windowSpi.getSize();

		final int posX = parentPosition.getX() + ((parentSize.getWidth() - size.getWidth()) / 2);
		final int posY = parentPosition.getY() + ((parentSize.getHeight() - size.getHeight()) / 2);

		windowSpi.setPosition(new Position(posX, posY));
	}

	public void setVisible(final boolean visible) {
		if (visible) {
			if (AutoPackPolicy.ALWAYS == autoPackPolicy) {
				pack();
			}
			else if (!sizeSet && !wasVisible && AutoPackPolicy.ONCE == autoPackPolicy) {
				pack();
			}
			if (AutoCenterPolicy.ALWAYS == autoCenterPolicy) {
				centerLocation();
			}
			else if (!positionSet && !wasVisible && AutoCenterPolicy.ONCE == autoCenterPolicy) {
				centerLocation();
			}
			if (AutoPositionCorrectionPolicy.ALWAYS == autoPositionCorrectionPolicy) {
				correctPosition();
			}
			else if (!positionCorrected && !wasVisible && AutoPositionCorrectionPolicy.ONCE == autoPositionCorrectionPolicy) {
				correctPosition();
			}
			wasVisible = true;
		}
		windowSpi.setVisible(visible);
	}

	public void pack() {
		window.pack();
		final Dimension oldSize = window.getSize();
		Dimension size = oldSize;
		if (minPackSize != null) {
			size = new Dimension(Math.max(size.getWidth(), minPackSize.getWidth()), Math.max(
					size.getHeight(),
					minPackSize.getHeight()));
		}
		if (maxPackSize != null) {
			size = new Dimension(Math.min(size.getWidth(), maxPackSize.getWidth()), Math.min(
					size.getHeight(),
					maxPackSize.getHeight()));
		}
		if (!oldSize.equals(size)) {
			window.setSize(size);
		}
	}

	public void setMinPackSize(final Dimension size) {
		this.minPackSize = size;
	}

	public void setMaxPackSize(final Dimension size) {
		this.maxPackSize = size;
	}

	public void setPosition(final Position position) {
		positionSet = true;
		windowSpi.setPosition(position);
	}

	public void setSize(final Dimension size) {
		sizeSet = true;
		windowSpi.setSize(size);
	}

	public <WIDGET_TYPE extends IDisplay, DESCRIPTOR_TYPE extends IWidgetDescriptor<WIDGET_TYPE>> WIDGET_TYPE createChildWindow(
		final DESCRIPTOR_TYPE descriptor) {
		final WIDGET_TYPE result = windowSpi.createChildWindow(descriptor);
		result.setParent(window);
		childWindows.add(result);
		return result;
	}

	public List<IDisplay> getChildWindows() {
		return new LinkedList<IDisplay>(childWindows);
	}

	public void correctPosition() {
		IWindow parent = window;
		while (parent.getParent() != null) {
			parent = parent.getParent();
		}

		final Rectangle screenBounds = parent.getParentBounds();

		final Dimension size = windowSpi.getSize();
		final Position position = windowSpi.getPosition();
		int x = position.getX();
		int y = position.getY();

		if (x + size.getWidth() > screenBounds.getX() + screenBounds.getWidth()) {
			x = screenBounds.getX() + screenBounds.getWidth() - size.getWidth();
		}
		if (y + size.getHeight() > screenBounds.getY() + screenBounds.getHeight()) {
			y = screenBounds.getY() + screenBounds.getHeight() - size.getHeight();
		}

		x = Math.max(x, screenBounds.getX());
		y = Math.max(y, screenBounds.getY());

		windowSpi.setPosition(new Position(x, y));
		positionCorrected = true;
	}
}
