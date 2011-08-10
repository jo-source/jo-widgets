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

import java.util.HashMap;
import java.util.List;

import org.jowidgets.api.layout.tablelayout.ITableLayout;
import org.jowidgets.api.layout.tablelayout.ITableLayoutBuilder.Alignment;
import org.jowidgets.api.layout.tablelayout.ITableRowLayout;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Rectangle;

/**
 * This layout manager would appreciate events on new children
 * 
 */
public final class TableRowLayout implements ITableRowLayout {
	private final IContainer container;
	private final ITableLayout rowLayoutCommon;
	private final HashMap<IControl, Dimension> preferredSizes;
	private final HashMap<Integer, IControl> controls;
	private final HashMap<IControl, Integer> spans;
	private final boolean ignoreInCalculations;
	private int layoutHashCode;
	private int nextIndex;

	public TableRowLayout(final IContainer container, final ITableLayout rowLayoutCommon, final boolean ignoreInCalculations) {
		this.container = container;
		this.rowLayoutCommon = rowLayoutCommon;
		this.preferredSizes = new HashMap<IControl, Dimension>();
		this.controls = new HashMap<Integer, IControl>();
		this.layoutHashCode = 0;
		this.spans = new HashMap<IControl, Integer>();
		this.ignoreInCalculations = ignoreInCalculations;
		rowLayoutCommon.addRowLayout(this);
		nextIndex = 0;
	}

	@Override
	public IContainer getContainer() {
		return container;
	}

	private IControl getChild(final int index) {
		if (controls.containsKey(Integer.valueOf(index))) {
			return controls.get(index);
		}

		final List<IControl> children = container.getChildren();
		for (final IControl control : children) {
			initControl(control);
		}

		return controls.get(index);
	}

	private Dimension getPreferredSize(final IControl control) {
		if (!preferredSizes.containsKey(control)) {
			final Dimension size = control.getPreferredSize();
			if (size.getHeight() > 0) {
				preferredSizes.put(control, size);
			}
			return size;
		}
		return preferredSizes.get(control);
	}

	@Override
	public Dimension getPreferredSize(final int index) {
		final IControl control = getChild(index);
		if (control == null) {
			return null;
		}
		return getPreferredSize(control);
	}

	@Override
	public void layout() {
		final Rectangle clientArea = container.getClientArea();
		if (clientArea.getHeight() == 0) {
			// no valid layout
			return;
		}
		rowLayoutCommon.validate();
		if (layoutHashCode == rowLayoutCommon.getLayoutHashCode()) {
			return;
		}
		layoutHashCode = rowLayoutCommon.getLayoutHashCode();

		final int[] gaps = rowLayoutCommon.getGaps();
		final int[] widths = rowLayoutCommon.getWidths();
		final Alignment[] alignments = rowLayoutCommon.getAlignments();

		int x = gaps[0];
		int index = 0;
		while (index < widths.length) {
			final IControl control = getChild(index);
			final int span = getSpan(control);
			final int width = getSpanWidth(index, span, widths, gaps);

			if (control != null) {
				int controlWidth = width;
				final Dimension size = getPreferredSize(control);
				final int y = clientArea.getY() + (clientArea.getHeight() - size.getHeight()) / 2;

				if (alignments[index] == Alignment.CENTER) {
					controlWidth = size.getWidth();
				}

				control.setSize(controlWidth, size.getHeight());
				control.setPosition(x + (width - controlWidth) / 2, y);
			}

			index = index + span;
			x = x + width + gaps[index];
		}
	}

	@Override
	public Dimension getMinSize() {
		return getPreferredSize();
	}

	@Override
	public Dimension getPreferredSize() {
		return rowLayoutCommon.getPreferredSize();
	}

	@Override
	public Dimension getMaxSize() {
		return getPreferredSize();
	}

	@Override
	public void invalidate() {
		//rowLayoutCommon.validate();
	}

	@Override
	public void remove() {
		rowLayoutCommon.removeRowLayout(this);
	}

	private int getValueFromConstraints(final String constraints, final String valueName, final int defaultValue) {
		if (constraints != null) {
			final String lowerValueName = valueName.toLowerCase();
			for (final String constraint : constraints.toLowerCase().split(",")) {
				final String c = constraint.trim();
				if (c.startsWith(lowerValueName)) {
					final String value = c.substring(lowerValueName.length()).trim();
					try {
						return Integer.parseInt(value);
					}
					catch (final Exception e) {
						return defaultValue;
					}
				}

			}
		}

		return defaultValue;
	}

	private int getSpan(final IControl control) {
		final Integer result = spans.get(control);
		if (result == null) {
			return 1;
		}
		else {
			return result.intValue();
		}
	}

	private void initControl(final IControl control) {
		if (spans.containsKey(control)) {
			return;
		}

		final String constraints = (String) control.getLayoutConstraints();
		final int span = getValueFromConstraints(constraints, "span", 1);
		spans.put(control, span);

		final int index = getValueFromConstraints(constraints, "index", nextIndex);
		controls.put(index, control);
		nextIndex = nextIndex + span;
	}

	private int getSpanWidth(final int index, final int span, final int[] widths, final int[] gaps) {
		int result = widths[index];
		for (int i = 1; i < span; i++) {
			result = result + gaps[index + i] + widths[index + i];
		}
		return result;
	}

	@Override
	public boolean isIgnoredInCalculations() {
		return ignoreInCalculations;
	}

	@Override
	public void invalidateControl(final IControl control) {
		preferredSizes.remove(control);
	}
}
