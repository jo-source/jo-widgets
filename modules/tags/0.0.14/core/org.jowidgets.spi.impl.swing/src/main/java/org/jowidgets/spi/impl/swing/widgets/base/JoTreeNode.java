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

package org.jowidgets.spi.impl.swing.widgets.base;

import javax.swing.tree.DefaultMutableTreeNode;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Markup;

public class JoTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 1L;

	private String text;
	private String toolTipText;
	private IImageConstant icon;
	private Markup markup;
	private IColorConstant backgroundColor;
	private IColorConstant foregroundColor;

	public void setText(final String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setToolTipText(final String toolTipText) {
		this.toolTipText = toolTipText;
	}

	public String getToolTipText() {
		return toolTipText;
	}

	public void setIcon(final IImageConstant icon) {
		this.icon = icon;
	}

	public IImageConstant getIcon() {
		return icon;
	}

	public void setMarkup(final Markup markup) {
		this.markup = markup;
	}

	public Markup getMarkup() {
		return markup;
	}

	public void setBackgroundColor(final IColorConstant backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public IColorConstant getBackgroundColor() {
		return backgroundColor;
	}

	public void setForegroundColor(final IColorConstant foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public IColorConstant getForegroundColor() {
		return foregroundColor;
	}

}
