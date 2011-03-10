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

package org.jowidgets.impl.widgets.basic;

import org.jowidgets.api.widgets.ITable;
import org.jowidgets.api.widgets.ITableColumn;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.common.types.TableColumnPackPolicy;
import org.jowidgets.impl.widgets.common.wrapper.ItemSpiWrapper;
import org.jowidgets.spi.widgets.ITableColumnSpi;
import org.jowidgets.util.Assert;

public class TableColumnImpl extends ItemSpiWrapper implements ITableColumn {

	private final ITable parent;

	public TableColumnImpl(final ITableColumnSpi component, final ITable parent) {
		super(component);
		this.parent = parent;
	}

	@Override
	public ITableColumnSpi getWidget() {
		return (ITableColumnSpi) super.getWidget();
	}

	@Override
	public IWidget getParent() {
		return parent;
	}

	@Override
	public void setAlignment(final AlignmentHorizontal alignment) {
		Assert.paramNotNull(alignment, "alignment");
		getWidget().setAlignment(alignment);
	}

	@Override
	public void setWidth(final int width) {
		getWidget().setWidth(width);
	}

	@Override
	public int getWidth(final int width) {
		return getWidget().getWidth(width);
	}

	@Override
	public void pack(final TableColumnPackPolicy policy) {
		Assert.paramNotNull(policy, "policy");
		getWidget().pack(policy);
	}

}
