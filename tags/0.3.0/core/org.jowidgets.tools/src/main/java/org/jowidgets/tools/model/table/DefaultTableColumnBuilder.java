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

import org.jowidgets.api.model.table.IDefaultTableColumn;
import org.jowidgets.api.model.table.IDefaultTableColumnBuilder;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.AlignmentHorizontal;

public class DefaultTableColumnBuilder implements IDefaultTableColumnBuilder {

	private final IDefaultTableColumnBuilder builder;

	public DefaultTableColumnBuilder() {
		this(Toolkit.getModelFactoryProvider().getTableModelFactory().columnBuilder());
	}

	public DefaultTableColumnBuilder(final IDefaultTableColumnBuilder builder) {
		super();
		this.builder = builder;
	}

	@Override
	public final IDefaultTableColumnBuilder setText(final String text) {
		builder.setText(text);
		return this;
	}

	@Override
	public final IDefaultTableColumnBuilder setToolTipText(final String tooltipText) {
		builder.setToolTipText(tooltipText);
		return this;
	}

	@Override
	public final IDefaultTableColumnBuilder setIcon(final IImageConstant icon) {
		builder.setIcon(icon);
		return this;
	}

	@Override
	public final IDefaultTableColumnBuilder setWidth(final int width) {
		builder.setWidth(width);
		return this;
	}

	@Override
	public final IDefaultTableColumnBuilder setAlignment(final AlignmentHorizontal alignment) {
		builder.setAlignment(alignment);
		return this;
	}

	@Override
	public final IDefaultTableColumn build() {
		return builder.build();
	}

}
