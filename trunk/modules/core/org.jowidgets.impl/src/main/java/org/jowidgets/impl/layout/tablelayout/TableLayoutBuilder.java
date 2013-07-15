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

import org.jowidgets.api.layout.tablelayout.ITableLayoutBuilder;
import org.jowidgets.util.Assert;

public final class TableLayoutBuilder implements ITableLayoutBuilder {

	private int columnCount;
	private int[] widths;
	private int[] gaps;
	private ColumnMode[] modes;
	private Alignment[] alignments;
	private int verticalGap;
	private int layoutMinRows;

	public TableLayoutBuilder() {
		layoutMinRows = 1;
	}

	@Override
	public TableLayoutBuilder verticalGap(final int verticalGap) {
		this.verticalGap = verticalGap;
		return this;
	}

	@Override
	public TableLayoutBuilder layoutMinRows(final int layoutMinRows) {
		this.layoutMinRows = layoutMinRows;
		return this;
	}

	@Override
	public TableLayoutBuilder columnCount(final int columnCount) {
		this.columnCount = columnCount;
		initializeArrays();
		return this;
	}

	private void initializeArrays() {
		if (widths == null) {
			widths = new int[columnCount];
		}
		if (gaps == null) {
			gaps = new int[columnCount + 1];
		}
		if (modes == null) {
			modes = new ColumnMode[columnCount];
			for (int i = 0; i < modes.length; i++) {
				modes[i] = ColumnMode.PREFERRED;
			}
		}
		if (alignments == null) {
			alignments = new Alignment[columnCount];
			for (int i = 0; i < alignments.length; i++) {
				alignments[i] = Alignment.LEFT;
			}
		}
	}

	@Override
	public TableLayoutBuilder widths(final int[] widths) {
		Assert.paramNotNull(widths, "widths");
		if ((columnCount > 0) && (widths.length != columnCount)) {
			throw new IllegalArgumentException("Column count does not match.");
		}

		if (columnCount == 0) {
			columnCount = widths.length;
			initializeArrays();
		}
		this.widths = widths;
		return this;
	}

	@Override
	public TableLayoutBuilder gaps(final int[] gaps) {
		Assert.paramNotNull(gaps, "gaps");
		if ((columnCount > 0) && (gaps.length != columnCount + 1)) {
			throw new IllegalArgumentException("Column count does not match.");
		}

		if (columnCount == 0) {
			columnCount = gaps.length - 1;
			initializeArrays();
		}
		this.gaps = gaps;
		return this;
	}

	@Override
	public TableLayoutBuilder modes(final ColumnMode[] modes) {
		Assert.paramNotNull(modes, "modes");
		if ((columnCount > 0) && (modes.length != columnCount)) {
			throw new IllegalArgumentException("Column count does not match.");
		}

		if (columnCount == 0) {
			columnCount = modes.length;
			initializeArrays();
		}
		this.modes = modes;
		return this;
	}

	@Override
	public TableLayoutBuilder alignments(final Alignment[] alignments) {
		Assert.paramNotNull(alignments, "alignments");
		if ((columnCount > 0) && (alignments.length != columnCount)) {
			throw new IllegalArgumentException("Column count does not match.");
		}

		if (columnCount == 0) {
			columnCount = alignments.length;
			initializeArrays();
		}
		this.alignments = alignments;
		return this;
	}

	@Override
	public TableLayoutBuilder fixedColumnWidth(final int column, final int width) {
		widths[column] = width;
		modes[column] = ColumnMode.FIXED;
		return this;
	}

	@Override
	public TableLayoutBuilder columnMode(final int column, final ColumnMode mode) {
		modes[column] = mode;
		return this;
	}

	@Override
	public TableLayoutBuilder columnAlignment(final int column, final Alignment alignment) {
		alignments[column] = alignment;
		return this;
	}

	@Override
	public TableLayoutBuilder gap(final int gap) {
		for (int i = 0; i < gaps.length; i++) {
			gaps[i] = gap;
		}
		return this;
	}

	@Override
	public TableLayoutBuilder gapBeforeColumn(final int column, final int gap) {
		gaps[column] = gap;
		return this;
	}

	@Override
	public TableLayoutBuilder gapAfterColumn(final int column, final int gap) {
		gaps[column + 1] = gap;
		return this;
	}

	@Override
	public TableLayoutBuilder alignment(final int index, final Alignment alignment) {
		alignments[index] = alignment;
		return this;
	}

	@Override
	public TableLayoutImpl build() {
		return new TableLayoutImpl(widths, gaps, modes, alignments, verticalGap, layoutMinRows);
	}

}
