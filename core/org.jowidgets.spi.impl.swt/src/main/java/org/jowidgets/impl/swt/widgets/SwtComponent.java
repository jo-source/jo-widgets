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
package org.jowidgets.impl.swt.widgets;

import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.controler.impl.PopupDetectionObservable;
import org.jowidgets.impl.swt.color.ColorCache;
import org.jowidgets.impl.swt.cursor.CursorCache;
import org.jowidgets.impl.swt.util.DimensionConvert;
import org.jowidgets.impl.swt.widgets.internal.PopupMenuImpl;
import org.jowidgets.spi.widgets.IComponentSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;

public class SwtComponent extends SwtWidget implements IComponentSpi {

	private final PopupDetectionObservable popupDetectionObservable;

	public SwtComponent(final Control control) {
		super(control);
		popupDetectionObservable = new PopupDetectionObservable();

		getUiReference().addMenuDetectListener(new MenuDetectListener() {

			@Override
			public void menuDetected(final MenuDetectEvent e) {
				final Point position = getUiReference().toControl(e.x, e.y);
				popupDetectionObservable.firePopupDetected(new Position(position.x, position.y));
			}
		});
	}

	@Override
	public void redraw() {
		if (getUiReference().getParent() != null) {
			getUiReference().getParent().layout(new Control[] {getUiReference()});
			getUiReference().getParent().redraw();
		}
		else {
			getUiReference().redraw();
		}
	}

	@Override
	public void setCursor(final Cursor cursor) {
		getUiReference().setCursor(CursorCache.getCursor(cursor));
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		getUiReference().setForeground(ColorCache.getInstance().getColor(colorValue));
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		getUiReference().setBackground(ColorCache.getInstance().getColor(colorValue));
	}

	@Override
	public IColorConstant getForegroundColor() {
		return toColorConstant(getUiReference().getForeground());
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return toColorConstant(getUiReference().getBackground());
	}

	private IColorConstant toColorConstant(final Color color) {
		return new ColorValue(color.getRed(), color.getGreen(), color.getBlue());
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
