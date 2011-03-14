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

import org.jowidgets.api.controler.IChangeListener;
import org.jowidgets.api.model.table.IDefaultTableColumn;
import org.jowidgets.api.model.table.IDefaultTableColumnBuilder;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.AlignmentHorizontal;
import org.jowidgets.util.Assert;

public class DefaultTableColumn implements IDefaultTableColumn {

	private final IDefaultTableColumn column;

	public DefaultTableColumn() {
		this(builder());
	}

	public DefaultTableColumn(final String text) {
		this(builder(text));
	}

	public DefaultTableColumn(final String text, final String toolTipText) {
		this(builder(text, toolTipText));
	}

	public DefaultTableColumn(final String text, final AlignmentHorizontal alignment) {
		this(builder(text, alignment));
	}

	public DefaultTableColumn(final String text, final String toolTipText, final AlignmentHorizontal alignment) {
		this(builder(text, toolTipText, alignment));
	}

	public DefaultTableColumn(final IDefaultTableColumnBuilder columnBuilder) {
		Assert.paramNotNull(columnBuilder, "columnBuilder");
		this.column = columnBuilder.build();
	}

	@Override
	public String getText() {
		return column.getText();
	}

	@Override
	public String getToolTipText() {
		return column.getToolTipText();
	}

	@Override
	public IImageConstant getIcon() {
		return column.getIcon();
	}

	@Override
	public void setWidth(final int width) {
		column.setWidth(width);
	}

	@Override
	public int getWidth() {
		return column.getWidth();
	}

	@Override
	public AlignmentHorizontal getAlignment() {
		return column.getAlignment();
	}

	@Override
	public void setText(final String text) {
		column.setText(text);
	}

	@Override
	public void setToolTipText(final String tooltipText) {
		column.setToolTipText(tooltipText);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		column.setIcon(icon);
	}

	@Override
	public void setAlignment(final AlignmentHorizontal alignment) {
		column.setAlignment(alignment);
	}

	@Override
	public void addChangeListener(final IChangeListener listener) {
		column.addChangeListener(listener);
	}

	@Override
	public void removeChangeListener(final IChangeListener listener) {
		column.removeChangeListener(listener);
	}

	public static IDefaultTableColumnBuilder builder() {
		return Toolkit.getModelFactoryProvider().getTableModelFactory().columnBuilder();
	}

	public static IDefaultTableColumnBuilder builder(final String text) {
		return builder().setText(text);
	}

	public static IDefaultTableColumnBuilder builder(final String text, final String toolTipText) {
		return builder(text).setToolTipText(toolTipText);
	}

	public static IDefaultTableColumnBuilder builder(final String text, final AlignmentHorizontal alignment) {
		return builder(text).setAlignment(alignment);
	}

	public static IDefaultTableColumnBuilder builder(
		final String text,
		final String toolTipText,
		final AlignmentHorizontal alignment) {
		return builder(text, toolTipText).setAlignment(alignment);
	}

}
