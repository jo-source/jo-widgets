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

import java.awt.Component;

import javax.swing.JComponent;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.impl.swing.util.ColorConvert;
import org.jowidgets.impl.swing.util.DimensionConvert;

public class SwingWidget implements IWidgetCommon {

	private final Component component;

	public SwingWidget(final Component component) {
		super();
		this.component = component;
	}

	@Override
	public Component getUiReference() {
		return component;
	}

	@Override
	public void redraw() {
		if (component instanceof JComponent) {
			((JComponent) component).revalidate();
		}
		component.repaint();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		component.setForeground(ColorConvert.convert(colorValue));
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		component.setBackground(ColorConvert.convert(colorValue));
	}

	@Override
	public IColorConstant getForegroundColor() {
		return ColorConvert.convert(component.getForeground());
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return ColorConvert.convert(component.getBackground());
	}

	@Override
	public void setVisible(final boolean visible) {
		component.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return component.isVisible();
	}

	@Override
	public Dimension getSize() {
		return DimensionConvert.convert(component.getSize());
	}

}
