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

package org.jowidgets.impl.mock.mockui;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;

public class UIMComponent extends UIMObservable {

	private Position position = new Position(0, 0);
	private Dimension size = new Dimension(400, 300);
	private IColorConstant foregroundColor;
	private IColorConstant backgroundColor;
	private String tooltipText;
	private boolean visible = false;
	private boolean enabled = true;

	public void redraw() {}

	public void setPosition(final Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}

	public void setSize(final Dimension size) {
		this.size = size;
	}

	public Dimension getSize() {
		return size;
	}

	public void setForegroundColor(final IColorConstant colorValue) {
		this.foregroundColor = colorValue;
	}

	public void setBackgroundColor(final IColorConstant colorValue) {
		this.backgroundColor = colorValue;
	}

	public IColorConstant getForegroundColor() {
		return foregroundColor;
	}

	public IColorConstant getBackgroundColor() {
		return backgroundColor;
	}

	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public String getToolTipText() {
		return tooltipText;
	}

	public void setToolTipText(final String tooltipText) {
		this.tooltipText = tooltipText;
	}

}
