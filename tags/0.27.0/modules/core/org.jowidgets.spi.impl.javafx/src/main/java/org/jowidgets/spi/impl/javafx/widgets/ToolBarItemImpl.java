/*
 * Copyright (c) 2012, David Bauknecht
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

package org.jowidgets.spi.impl.javafx.widgets;

import javafx.geometry.Bounds;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Tooltip;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.spi.impl.javafx.image.JavafxImageRegistry;
import org.jowidgets.spi.widgets.IToolBarItemSpi;

public class ToolBarItemImpl implements IToolBarItemSpi {

	private final ButtonBase button;

	public ToolBarItemImpl(final ButtonBase button) {
		super();
		this.button = button;
		//TODO DB there is a bug in javafx  https://forums.oracle.com/forums/thread.jspa?threadID=2359882&tstart=0
		this.button.setFocusTraversable(false);
	}

	@Override
	public ButtonBase getUiReference() {
		return button;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		button.setDisable(!enabled);
	}

	@Override
	public boolean isEnabled() {
		return !button.isDisabled();
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
		final Tooltip tool = new Tooltip(text);
		if (text == null || text.isEmpty()) {
			Tooltip.uninstall(getUiReference(), tool);
		}
		else {
			Tooltip.install(getUiReference(), tool);
		}
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		button.setGraphic(JavafxImageRegistry.getInstance().getImage(icon));
	}

	@Override
	public Position getPosition() {
		//TODO DB works only for PopupMenu because it will be shown under the button
		final Bounds bounds = button.localToScene(button.getBoundsInLocal());
		final double posX = bounds.getMinX() + button.getScene().getX() + button.getScene().getWindow().getX();
		final double posY = bounds.getMinY() + button.getScene().getY() + button.getScene().getWindow().getY();
		return new Position((int) posX, (int) posY);
	}

	@Override
	public Dimension getSize() {
		return new Dimension((int) button.getWidth(), (int) button.getHeight());
	}

}
