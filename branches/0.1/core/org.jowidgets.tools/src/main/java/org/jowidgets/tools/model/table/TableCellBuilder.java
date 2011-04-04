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

package org.jowidgets.tools.model.table;

import org.jowidgets.api.model.table.ITableCellBuilder;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.model.ITableCell;
import org.jowidgets.common.types.Markup;

public class TableCellBuilder implements ITableCellBuilder {

	private final ITableCellBuilder builder;

	public TableCellBuilder() {
		this(Toolkit.getModelFactoryProvider().getTableModelFactory().cellBuilder());
	}

	public TableCellBuilder(final ITableCellBuilder builder) {
		super();
		this.builder = builder;
	}

	@Override
	public final ITableCellBuilder setText(final String text) {
		builder.setText(text);
		return this;
	}

	@Override
	public final ITableCellBuilder setToolTipText(final String toolTipText) {
		builder.setToolTipText(toolTipText);
		return this;
	}

	@Override
	public final ITableCellBuilder setIcon(final IImageConstant icon) {
		builder.setIcon(icon);
		return this;
	}

	@Override
	public final ITableCellBuilder setForegroundColor(final IColorConstant foregroundColor) {
		builder.setForegroundColor(foregroundColor);
		return this;
	}

	@Override
	public final ITableCellBuilder setBackgroundColor(final IColorConstant backgroundColor) {
		builder.setBackgroundColor(backgroundColor);
		return this;
	}

	@Override
	public final ITableCellBuilder setMarkup(final Markup markup) {
		builder.setMarkup(markup);
		return this;
	}

	@Override
	public final ITableCellBuilder setEditable(final boolean editable) {
		builder.setEditable(editable);
		return this;
	}

	@Override
	public final ITableCell build() {
		return builder.build();
	}

}
