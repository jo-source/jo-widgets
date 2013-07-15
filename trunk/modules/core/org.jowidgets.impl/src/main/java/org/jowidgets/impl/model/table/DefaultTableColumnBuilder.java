/*
 * Copyright (c) 2011, grossmann, Nikolaus Moll
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

package org.jowidgets.impl.model.table;

import org.jowidgets.api.model.table.IDefaultTableColumn;
import org.jowidgets.api.model.table.IDefaultTableColumnBuilder;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.AlignmentHorizontal;

class DefaultTableColumnBuilder implements IDefaultTableColumnBuilder {

	private String text;
	private String toolTipText;
	private IImageConstant icon;
	private int width;
	private AlignmentHorizontal alignment;
	private boolean visible;

	DefaultTableColumnBuilder() {
		this.alignment = AlignmentHorizontal.LEFT;
		this.width = -1;
		this.visible = true;
	}

	@Override
	public IDefaultTableColumnBuilder setText(final String text) {
		this.text = text;
		return this;
	}

	@Override
	public IDefaultTableColumnBuilder setToolTipText(final String toolTipText) {
		this.toolTipText = toolTipText;
		return this;
	}

	@Override
	public IDefaultTableColumnBuilder setIcon(final IImageConstant icon) {
		this.icon = icon;
		return this;
	}

	@Override
	public IDefaultTableColumnBuilder setWidth(final int width) {
		this.width = width;
		return this;
	}

	@Override
	public IDefaultTableColumnBuilder setAlignment(final AlignmentHorizontal alignment) {
		this.alignment = alignment;
		return this;
	}

	@Override
	public IDefaultTableColumnBuilder setVisible(final boolean visible) {
		this.visible = visible;
		return this;
	}

	@Override
	public IDefaultTableColumn build() {
		return new DefaultTableColumn(text, toolTipText, icon, width, alignment, visible);
	}

}
