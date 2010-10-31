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
package org.jowidgets.impl.swing.widgets;

import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IWidget;
import org.jowidgets.common.widgets.IWindowWidgetCommon;
import org.jowidgets.common.widgets.controler.IWindowListener;
import org.jowidgets.common.widgets.controler.impl.WindowObservable;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.impl.swing.image.SwingImageRegistry;
import org.jowidgets.impl.swing.util.DimensionConvert;
import org.jowidgets.impl.swing.util.PositionConvert;

public class SwingWindowWidget extends SwingContainerWidget implements IWindowWidgetCommon {

	private final WindowObservable windowObservableDelegate;

	public SwingWindowWidget(final IGenericWidgetFactory factory, final Window window) {
		super(factory, window);

		this.windowObservableDelegate = new WindowObservable();

		getUiReference().addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(final WindowEvent e) {
				windowObservableDelegate.fireWindowActivated();
			}

			@Override
			public void windowDeactivated(final WindowEvent e) {
				windowObservableDelegate.fireWindowDeactivated();
			}

			@Override
			public void windowIconified(final WindowEvent e) {
				windowObservableDelegate.fireWindowIconified();
			}

			@Override
			public void windowDeiconified(final WindowEvent e) {
				windowObservableDelegate.fireWindowDeiconified();
			}

			@Override
			public void windowClosed(final WindowEvent e) {
				windowObservableDelegate.fireWindowClosed();
			}

			@Override
			public void windowClosing(final WindowEvent e) {}

			@Override
			public void windowOpened(final WindowEvent e) {}

		});

	}

	@Override
	public Window getUiReference() {
		return (Window) super.getUiReference();
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
	public final void setPosition(final Position position) {
		getUiReference().setLocation(PositionConvert.convert(position));
	}

	@Override
	public final Position getPosition() {
		return PositionConvert.convert(getUiReference().getLocation());
	}

	@Override
	public final void setSize(final Dimension size) {
		getUiReference().setSize(DimensionConvert.convert(size));
	}

	@Override
	public final Dimension getSize() {
		return DimensionConvert.convert(getUiReference().getSize());
	}

	@Override
	public <WIDGET_TYPE extends IWidget, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE createChildWindow(
		final DESCRIPTOR_TYPE descriptor) {
		return getGenericWidgetFactory().create(this, descriptor);
	}

	protected void setIcon(final IImageConstant icon, final SwingImageRegistry imageRegistry) {
		getUiReference().setIconImage(imageRegistry.getImage(icon));
	}

	@Override
	public Rectangle getParentBounds() {
		Dimension parentSize;
		Position parentPosition = new Position(0, 0);
		if (getUiReference().getParent() != null) {
			parentPosition = PositionConvert.convert(getUiReference().getParent().getLocation());
			parentSize = DimensionConvert.convert(getUiReference().getParent().getSize());
		}
		else {
			parentSize = DimensionConvert.convert(Toolkit.getDefaultToolkit().getScreenSize());
		}
		return new Rectangle(parentPosition, parentSize);
	}

	@Override
	public void addWindowListener(final IWindowListener listener) {
		windowObservableDelegate.addWindowListener(listener);
	}

	@Override
	public void removeWindowListener(final IWindowListener listener) {
		windowObservableDelegate.removeWindowListener(listener);
	}

	@Override
	public void close() {
		getUiReference().dispose();
	}

}
