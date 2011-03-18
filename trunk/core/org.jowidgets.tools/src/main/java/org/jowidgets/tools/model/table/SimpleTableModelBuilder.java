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

import org.jowidgets.api.model.table.ISimpleTableModel;
import org.jowidgets.api.model.table.ISimpleTableModelBuilder;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.color.IColorConstant;

public class SimpleTableModelBuilder implements ISimpleTableModelBuilder {

	private final ISimpleTableModelBuilder builder;

	public SimpleTableModelBuilder() {
		super();
		this.builder = Toolkit.getModelFactoryProvider().getTableModelFactory().simpleTableModelBuilder();
	}

	public SimpleTableModelBuilder(final ISimpleTableModelBuilder builder) {
		super();
		this.builder = builder;
	}

	@Override
	public ISimpleTableModelBuilder setRowCount(final int rowCount) {
		builder.setRowCount(rowCount);
		return this;
	}

	@Override
	public ISimpleTableModelBuilder setColumnCount(final int columnCount) {
		builder.setColumnCount(columnCount);
		return this;
	}

	@Override
	public ISimpleTableModelBuilder setEditableDefault(final boolean editable) {
		builder.setEditableDefault(editable);
		return this;
	}

	@Override
	public ISimpleTableModelBuilder setDefaultStripedRenderingColors() {
		builder.setDefaultStripedRenderingColors();
		return this;
	}

	@Override
	public ISimpleTableModelBuilder setEvenRowsBackgroundColor(final IColorConstant color) {
		builder.setEvenRowsBackgroundColor(color);
		return this;
	}

	@Override
	public ISimpleTableModelBuilder setOddRowsBackgroundColor(final IColorConstant color) {
		builder.setOddRowsBackgroundColor(color);
		return this;
	}

	@Override
	public ISimpleTableModel build() {
		return builder.build();
	}

}
