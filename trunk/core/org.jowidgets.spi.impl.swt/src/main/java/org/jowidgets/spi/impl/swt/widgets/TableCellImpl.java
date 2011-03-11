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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.TableItem;
import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.ITableCell;
import org.jowidgets.spi.impl.swt.color.ColorCache;
import org.jowidgets.spi.impl.swt.image.SwtImageRegistry;
import org.jowidgets.spi.impl.swt.util.FontProvider;

public class TableCellImpl implements ITableCell {

	private final TableItem tableItem;
	private final int columnIndex;

	private String tooltipText;
	private IImageConstant icon;
	private boolean editable;

	public TableCellImpl(final TableItem tableItem, final int columnIndex) {
		super();
		this.tableItem = tableItem;
		this.columnIndex = columnIndex;
	}

	@Override
	public TableItem getUiReference() {
		return tableItem;
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
			tableItem.setText(columnIndex, text);
		}
		else {
			tableItem.setText(columnIndex, "");
		}
	}

	@Override
	public void setToolTipText(final String tooltipText) {
		this.tooltipText = tooltipText;
		// TODO BM Use Tooltip like in SwtMenu
	}

	@Override
	public String getToolTipText() {
		return tooltipText;
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		this.icon = icon;
		tableItem.setImage(columnIndex, SwtImageRegistry.getInstance().getImage(icon));
	}

	@Override
	public IImageConstant getIcon() {
		return icon;
	}

	@Override
	public void setEditable(final boolean editable) {
		this.editable = editable;
		// TODO MG implement editor
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public String getText() {
		return tableItem.getText(columnIndex);
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		tableItem.setForeground(columnIndex, ColorCache.getInstance().getColor(colorValue));
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		tableItem.setBackground(columnIndex, ColorCache.getInstance().getColor(colorValue));
	}

	@Override
	public IColorConstant getForegroundColor() {
		final Color color = tableItem.getForeground(columnIndex);
		return new ColorValue(color.getRed(), color.getGreen(), color.getBlue());
	}

	@Override
	public IColorConstant getBackgroundColor() {
		final Color color = tableItem.getBackground(columnIndex);
		return new ColorValue(color.getRed(), color.getGreen(), color.getBlue());
	}

	@Override
	public void setMarkup(final Markup markup) {
		final Font newFont = FontProvider.deriveFont(tableItem.getFont(columnIndex), markup);
		tableItem.setFont(columnIndex, newFont);

	}

	@Override
	public Position getPosition() {
		final Rectangle bounds = tableItem.getBounds();
		return new Position(bounds.x, bounds.y);
	}

	@Override
	public Dimension getSize() {
		final Rectangle bounds = tableItem.getBounds();
		return new Dimension(bounds.width, bounds.height);
	}

}
