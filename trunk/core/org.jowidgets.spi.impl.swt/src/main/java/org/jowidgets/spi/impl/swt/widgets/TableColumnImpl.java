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

package org.jowidgets.spi.impl.swt.widgets;

import org.eclipse.swt.widgets.TableColumn;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.common.types.TableColumnPackPolicy;
import org.jowidgets.spi.impl.swt.image.SwtImageRegistry;
import org.jowidgets.spi.impl.swt.util.AlignmentConvert;
import org.jowidgets.spi.widgets.ITableColumnSpi;

public class TableColumnImpl implements ITableColumnSpi {

	private final TableColumn tableColumn;

	public TableColumnImpl(final TableColumn tableColumn, final boolean columnsMoveable, final boolean columnsResizeable) {
		super();
		this.tableColumn = tableColumn;
		this.tableColumn.setMoveable(columnsMoveable);
		this.tableColumn.setResizable(columnsResizeable);
	}

	@Override
	public TableColumn getUiReference() {
		return tableColumn;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		if (!enabled) {
			throw new IllegalArgumentException("Table cell could not be disabled.");
		}
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void setText(final String text) {
		if (text != null) {
			tableColumn.setText(text);
		}
		else {
			tableColumn.setText("");
		}
	}

	@Override
	public void setToolTipText(final String text) {
		tableColumn.setToolTipText(text);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		tableColumn.setImage(SwtImageRegistry.getInstance().getImage(icon));
	}

	@Override
	public void setAlignment(final AlignmentHorizontal alignment) {
		tableColumn.setAlignment(AlignmentConvert.convert(alignment));
	}

	@Override
	public void setWidth(final int width) {
		tableColumn.setWidth(width);
	}

	@Override
	public int getWidth(final int width) {
		return tableColumn.getWidth();
	}

	@Override
	public void pack(final TableColumnPackPolicy policy) {
		//TODO MG consider pack policy
		tableColumn.pack();
	}

}
