/*
 * Copyright (c) 2011, Nikolaus Moll
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

package org.jowidgets.impl.layout.tablelayout;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.layout.tablelayout.ITableLayout;
import org.jowidgets.api.layout.tablelayout.ITableLayoutBuilder.Alignment;
import org.jowidgets.api.layout.tablelayout.ITableLayoutBuilder.ColumnMode;
import org.jowidgets.api.layout.tablelayout.ITableRowLayout;
import org.jowidgets.api.layout.tablelayout.ITableRowLayoutFactoryBuilder;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Rectangle;

public final class TableLayoutImpl implements ITableLayout {
	private final int columnCount;
	private final int layoutMinRows;
	private final int[] widths;
	private final int[] gaps;
	private final Alignment[] alignments;
	private final int verticalGap;
	private final ColumnMode[] modes;
	private int layoutHashCode;
	private int availableWidth;
	private final LinkedList<ITableRowLayout> layouters;
	private Dimension preferredSize;
	private boolean layouting;
	private final List<Integer> growingColumns;
	private int usedIndex;

	public TableLayoutImpl(
		final int[] widths,
		final int[] gaps,
		final ColumnMode[] modes,
		final Alignment[] alignments,
		final int verticalGap,
		final int layoutMinRows) {
		this.columnCount = widths.length;
		this.modes = modes;
		this.widths = widths;
		this.gaps = gaps;
		this.verticalGap = verticalGap;
		this.layoutMinRows = layoutMinRows;
		this.alignments = alignments;
		layouters = new LinkedList<ITableRowLayout>();
		layouting = true;

		growingColumns = new LinkedList<Integer>();
		for (int i = 0; i < widths.length; i++) {
			if (modes[i] == ColumnMode.GROWING) {
				growingColumns.add(Integer.valueOf(i));
			}
		}

		usedIndex = -1;
		invalidate();
	}

	@Override
	public void addRowLayout(final ITableRowLayout rowLayout) {
		layouters.add(rowLayout);
		if (usedIndex < 0) {
			getUsedIndex();
		}
	}

	private void getUsedIndex() {
		for (int i = 0; i < layouters.size(); i++) {
			if (!layouters.get(i).isIgnoredInCalculations()) {
				usedIndex = i;
				return;
			}
		}
		usedIndex = -1;
	}

	@Override
	public void removeRowLayout(final ITableRowLayout rowLayout) {
		layouters.remove(rowLayout);
		getUsedIndex();
	}

	@Override
	public int[] getWidths() {
		return widths;
	}

	@Override
	public int[] getGaps() {
		return gaps;
	}

	ColumnMode[] getModes() {
		return modes;
	}

	@Override
	public Alignment[] getAlignments() {
		return alignments;
	}

	@Override
	public void invalidate() {
		if (!layouting) {
			return;
		}

		calculateLayout();
	}

	public int getAvailableWidth() {
		return availableWidth;
	}

	@Override
	public int getLayoutHashCode() {
		return layoutHashCode;
	}

	private int calculateHashCode() {
		int result = 17;
		result = 31 * result + layouters.size();
		result = 31 * result + availableWidth;
		for (int i = 0; i < widths.length; i++) {
			result = 31 * result + widths[i];
		}
		for (int i = 0; i < gaps.length; i++) {
			result = 31 * result + gaps[i];
		}
		return result;
	}

	public void calculateLayout() {
		if (layouters.size() < layoutMinRows) {
			preferredSize = new Dimension(0, 0);
			return;
		}

		final ITableRowLayout rowLayout = layouters.get(usedIndex);
		final Rectangle clientArea = rowLayout.getContainer().getClientArea();
		availableWidth = clientArea.getWidth();

		for (int i = 0; i < widths.length; i++) {
			if (modes[i] != ColumnMode.FIXED) {
				widths[i] = 0;
			}
		}

		int minHeight = 0;
		for (final ITableRowLayout layouter : layouters) {
			if (layouter.isIgnoredInCalculations()) {
				continue;
			}

			for (int i = 0; i < columnCount; i++) {
				if (modes[i] == ColumnMode.HIDDEN) {
					continue;
				}

				final Dimension size = layouter.getPreferredSize(i);
				if (size == null) {
					continue;
				}

				widths[i] = Math.max(widths[i], size.getWidth());
				minHeight = Math.max(minHeight, size.getHeight());
			}
		}

		int currentWidth = 0;
		for (final int width : widths) {
			currentWidth = currentWidth + width;
		}
		for (final int gap : gaps) {
			currentWidth = currentWidth + gap;
		}

		final int usedWidth = currentWidth;
		if (availableWidth > currentWidth) {
			final int additionalWidth = availableWidth - currentWidth;
			if (growingColumns.size() > 0) {
				int columnAdditional = additionalWidth / growingColumns.size();
				for (final Integer index : growingColumns) {
					widths[index] = widths[index] + columnAdditional;
					currentWidth = currentWidth + columnAdditional;
					columnAdditional = Math.min(columnAdditional, availableWidth - currentWidth);
				}
			}
		}

		preferredSize = new Dimension(usedWidth, minHeight + 2 * verticalGap);
		layoutHashCode = calculateHashCode();
	}

	@Override
	public Dimension getPreferredSize() {
		if (preferredSize == null) {
			calculateLayout();
		}
		return preferredSize;
	}

	public int getColumnCount() {
		return columnCount;
	}

	@Override
	public void beginLayout() {
		layouting = false;
	}

	@Override
	public void endLayout() {
		if (!layouting) {
			layoutHashCode = 0;
			layouting = true;
			calculateLayout();
		}
	}

	@Override
	public void validate() {
		if (availableWidth > 0) {
			if (growingColumns.isEmpty() || layouters.size() < layoutMinRows) {
				return;
			}
		}

		if (usedIndex < 0) {
			return;
		}

		if (layoutHashCode != calculateHashCode()) {
			invalidate();
			return;
		}

		final ITableRowLayout rowLayout = layouters.get(usedIndex);
		if (rowLayout != null) {
			final Rectangle clientArea = rowLayout.getContainer().getClientArea();
			if (availableWidth != clientArea.getWidth()) {
				invalidate();
			}
		}
		else {
			invalidate();
		}
	}

	@Override
	public ITableRowLayoutFactoryBuilder rowBuilder() {
		return new TableRowLayoutFactoryBuilder(this);
	}

}
