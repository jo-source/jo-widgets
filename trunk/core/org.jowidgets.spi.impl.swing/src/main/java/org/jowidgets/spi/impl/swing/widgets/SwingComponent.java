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
package org.jowidgets.spi.impl.swing.widgets;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.spi.impl.controler.PopupDetectionObservable;
import org.jowidgets.spi.impl.swing.util.ColorConvert;
import org.jowidgets.spi.impl.swing.util.CursorConvert;
import org.jowidgets.spi.impl.swing.util.DimensionConvert;
import org.jowidgets.spi.widgets.IComponentSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;

public class SwingComponent extends SwingWidget implements IComponentSpi {

	private final PopupDetectionObservable popupDetectionObservable;
	private MouseListener mouseListener;

	public SwingComponent(final Component component) {
		super(component);
		this.popupDetectionObservable = new PopupDetectionObservable();

		this.mouseListener = new MouseAdapter() {
			@Override
			public void mouseReleased(final MouseEvent e) {
				if (e.isPopupTrigger()) {
					popupDetectionObservable.firePopupDetected(new Position(e.getX(), e.getY()));
				}
			}

			@Override
			public void mousePressed(final MouseEvent e) {
				if (e.isPopupTrigger()) {
					popupDetectionObservable.firePopupDetected(new Position(e.getX(), e.getY()));
				}
			}
		};

		component.addMouseListener(mouseListener);
	}

	protected PopupDetectionObservable getPopupDetectionObservable() {
		return popupDetectionObservable;
	}

	protected void setMouseListener(final MouseListener mouseListener) {
		getUiReference().removeMouseListener(this.mouseListener);
		this.mouseListener = mouseListener;
		getUiReference().addMouseListener(mouseListener);
	}

	@Override
	public void setComponent(final Component component) {
		getUiReference().removeMouseListener(mouseListener);
		super.setComponent(component);
		getUiReference().addMouseListener(mouseListener);
	}

	@Override
	public void redraw() {
		if (getUiReference() instanceof JComponent) {
			((JComponent) getUiReference()).revalidate();
		}
		else {
			getUiReference().validate();
		}
		getUiReference().repaint();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		getUiReference().setForeground(ColorConvert.convert(colorValue));
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		getUiReference().setBackground(ColorConvert.convert(colorValue));
	}

	@Override
	public IColorConstant getForegroundColor() {
		return ColorConvert.convert(getUiReference().getForeground());
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return ColorConvert.convert(getUiReference().getBackground());
	}

	@Override
	public void setCursor(final Cursor cursor) {
		getUiReference().setCursor(CursorConvert.convert(cursor));
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
	public Dimension getSize() {
		return DimensionConvert.convert(getUiReference().getSize());
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return new PopupMenuImpl(getUiReference());
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		popupDetectionObservable.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		popupDetectionObservable.removePopupDetectionListener(listener);
	}

}
