/*
 * Copyright (c) 2011, grossmann, Nikolaus Moll
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

package org.jowidgets.impl.layout;

import java.util.List;

import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Orientation;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.util.Assert;

final class FlowLayout extends AbstractCachingLayout implements ILayouter {
	private final IContainer container;
	private final int gap;
	private final Orientation orientation;

	FlowLayout(final IContainer container, final int gap, final Orientation orientation) {
		Assert.paramNotNull(container, "container");
		this.container = container;
		this.gap = gap;
		this.orientation = orientation;
	}

	@Override
	public void layout() {
		final Rectangle clientArea = container.getClientArea();
		final int availableHeight = clientArea.getHeight();
		final int availableWidth = clientArea.getWidth();

		int x = clientArea.getX();
		int y = clientArea.getY();

		final List<IControl> children = container.getChildren();
		final int[] widths;
		final int[] heights;

		if (Orientation.HORIZONTAL == orientation) {
			heights = new int[children.size()];
			final int[] minWidths = new int[children.size()];
			final int[] prefWidths = new int[children.size()];

			for (int i = 0; i < children.size(); i++) {
				final IControl control = children.get(i);

				final Dimension minSize = getControlMinSize(control);
				final Dimension prefSize = getControlPrefSize(control);
				minWidths[i] = minSize.getWidth();
				prefWidths[i] = prefSize.getWidth();

				if (availableHeight > prefSize.getHeight()) {
					heights[i] = prefSize.getHeight();
				}
				else if (availableHeight > minSize.getHeight()) {
					heights[i] = availableHeight;
				}
				else {
					heights[i] = minSize.getHeight();
				}
			}

			widths = calcRatio(minWidths, prefWidths, availableWidth - (minWidths.length - 1) * gap);

			for (int i = 0; i < children.size(); i++) {
				final IControl control = children.get(i);
				control.setSize(widths[i], heights[i]);
				control.setPosition(x, y);
				x = x + gap + widths[i];
			}
		}
		else {
			widths = new int[children.size()];
			final int[] minHeights = new int[children.size()];
			final int[] prefHeights = new int[children.size()];

			for (int i = 0; i < children.size(); i++) {
				final IControl control = children.get(i);
				final Dimension minSize = getControlMinSize(control);
				final Dimension prefSize = getControlPrefSize(control);
				minHeights[i] = minSize.getHeight();
				prefHeights[i] = prefSize.getHeight();

				if (availableWidth > prefSize.getWidth()) {
					widths[i] = prefSize.getHeight();
				}
				else if (availableWidth > minSize.getWidth()) {
					widths[i] = availableWidth;
				}
				else {
					widths[i] = minSize.getWidth();
				}
			}
			heights = calcRatio(minHeights, prefHeights, availableHeight - (minHeights.length - 1) * gap);

			for (int i = 0; i < children.size(); i++) {
				final IControl control = children.get(i);
				control.setSize(widths[i], heights[i]);
				control.setPosition(x, y);
				y = y + gap + heights[i];
			}
		}
	}

	private int[] calcRatio(final int[] mins, final int[] prefs, final int availableSize) {
		if (mins.length == 0) {
			return mins;
		}

		int minSize = 0;
		int prefSize = 0;
		int delta = 0;
		final int[] deltas = new int[mins.length];
		for (int i = 0; i < mins.length; i++) {
			minSize = minSize + mins[i];
			prefSize = prefSize + prefs[i];
			deltas[i] = (prefs[i] - mins[i]);
			delta = delta + deltas[i];
		}

		if (availableSize <= minSize) {
			return mins;
		}
		if (availableSize >= prefSize) {
			return prefs;
		}

		final int ratioSize = availableSize - minSize;
		if (ratioSize < 0) {
			throw new IllegalStateException("ratio size < 0");
		}

		final int[] result = new int[mins.length];
		int usedSize = 0;
		for (int i = 0; i < result.length - 1; i++) {
			result[i] = Math.min(prefs[i], mins[i] + (int) (ratioSize * (double) deltas[i] / delta));
			usedSize = usedSize + result[i];
		}
		result[result.length - 1] = Math.min(prefs[result.length - 1], availableSize - usedSize);
		return result;
	}

	@Override
	protected Dimension calculateMinSize() {
		return calcDecoratedSize(minimumPolicy());
	}

	@Override
	public Dimension calculatePreferredSize() {
		return calcDecoratedSize(preferredPolicy());
	}

	private Dimension calcDecoratedSize(final ISizeProvider policy) {
		return container.computeDecoratedSize(calcControlsSize(policy));
	}

	private Dimension calcControlsSize(final ISizeProvider policy) {
		int width = 0;
		int height = 0;
		final List<IControl> children = container.getChildren();

		if (Orientation.HORIZONTAL == orientation) {
			for (final IControl control : children) {
				final Dimension size = policy.getSize(control);
				width = width + gap + size.getWidth();
				height = Math.max(height, size.getHeight());
			}
			if (children.size() > 0) {
				width = width - gap;
			}
		}
		else {
			for (final IControl control : children) {
				final Dimension size = policy.getSize(control);
				height = height + gap + size.getHeight();
				width = Math.max(width, size.getWidth());
			}
			if (children.size() > 0) {
				height = height - gap;
			}
		}

		return new Dimension(width, height);
	}

}
