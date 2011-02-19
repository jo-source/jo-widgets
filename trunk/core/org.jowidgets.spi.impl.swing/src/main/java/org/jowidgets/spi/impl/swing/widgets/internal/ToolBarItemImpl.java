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

package org.jowidgets.spi.impl.swing.widgets.internal;

import javax.swing.AbstractButton;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.spi.impl.swing.image.SwingImageRegistry;
import org.jowidgets.spi.impl.swing.util.DimensionConvert;
import org.jowidgets.spi.impl.swing.util.PositionConvert;
import org.jowidgets.spi.widgets.IToolBarItemSpi;

public class ToolBarItemImpl implements IToolBarItemSpi {

	private final AbstractButton button;

	public ToolBarItemImpl(final AbstractButton button) {
		super();
		this.button = button;

		this.button.setFocusable(false);
	}

	@Override
	public AbstractButton getUiReference() {
		return button;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		button.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return button.isEnabled();
	}

	@Override
	public void setText(final String text) {
		if (text != null) {
			button.setText(text);
		}
		else {
			button.setText("");
		}
	}

	@Override
	public void setToolTipText(final String text) {
		button.setToolTipText(text);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		button.setIcon(SwingImageRegistry.getInstance().getImageIcon(icon));
	}

	@Override
	public Position getPosition() {
		return PositionConvert.convert(button.getLocation());
	}

	@Override
	public Dimension getSize() {
		return DimensionConvert.convert(button.getSize());
	}

}
