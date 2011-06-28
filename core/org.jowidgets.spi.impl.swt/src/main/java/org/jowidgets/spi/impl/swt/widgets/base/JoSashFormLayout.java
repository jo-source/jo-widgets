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

package org.jowidgets.spi.impl.swt.widgets.base;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.jowidgets.common.types.SplitResizePolicy;

final class JoSashFormLayout extends Layout {

	private final JoSashForm parent;
	private final ISashOrientationUtil sashUtil;

	private int totalWidth;
	private int totalHeight;
	private boolean initialized;

	public JoSashFormLayout(final JoSashForm parent, final ISashOrientationUtil calculator) {
		this.parent = parent;
		this.sashUtil = calculator;
		initialized = false;
	}

	@Override
	protected Point computeSize(final Composite composite, final int wHint, final int hHint, final boolean flushCache) {
		if (flushCache) {
			calculatePreferredSize();
		}

		final Point result = new Point(SWT.DEFAULT, SWT.DEFAULT);
		if (wHint == SWT.DEFAULT) {
			result.x = totalWidth;
		}
		if (hHint == SWT.DEFAULT) {
			result.y = totalHeight;
		}
		return result;
	}

	private void calculatePreferredSize() {
		final Control first = parent.getFirst();
		final Control second = parent.getSecond();
		totalWidth = 0;
		totalHeight = 0;
		if (sashUtil.getOrientation() == SWT.HORIZONTAL) {
			totalWidth += parent.getSashSize();
			if (first != null) {
				final Point firstSize = first.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				totalWidth += firstSize.x;
				totalHeight = Math.max(totalHeight, firstSize.y);
			}
			if (second != null) {
				final Point secondSize = second.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				totalWidth += secondSize.x;
				totalHeight = Math.max(totalHeight, secondSize.y);
			}
		}
		else {
			totalHeight += parent.getSashSize();
			if (first != null) {
				final Point firstSize = first.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				totalHeight += firstSize.y;
				totalWidth = Math.max(totalWidth, firstSize.x);
			}
			if (second != null) {
				final Point secondSize = second.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				totalHeight += secondSize.y;
				totalWidth = Math.max(totalWidth, secondSize.x);
			}
		}
	}

	@Override
	protected void layout(final Composite composite, final boolean flushCache) {
		final Rectangle area = parent.getClientArea();

		int firstSize;
		int secondSize;
		final int firstMinSize = sashUtil.getSize(parent.getFirstMinSize());
		final int secondMinSize = sashUtil.getSize(parent.getSecondMinSize());

		final int effectiveSize = sashUtil.getSize(area) - parent.getSashSize();
		if (effectiveSize <= 0) {
			// no reasonable parent size set, so do not layout anything
			return;
		}

		if (!initialized || (parent.getResizePolicy() == SplitResizePolicy.RESIZE_BOTH)) {
			initialized = true;
			final double weight = parent.getWeight();

			firstSize = (int) (weight * effectiveSize);
			secondSize = effectiveSize - firstSize;
		}
		else if (parent.getResizePolicy() == SplitResizePolicy.RESIZE_FIRST) {
			secondSize = sashUtil.getSize(parent.getSecond().getBounds());
			firstSize = effectiveSize - secondSize;
		}
		else if (parent.getResizePolicy() == SplitResizePolicy.RESIZE_SECOND) {
			firstSize = sashUtil.getSize(parent.getFirst().getBounds());
			secondSize = effectiveSize - firstSize;
		}
		else {
			throw new IllegalStateException("Wrong Orientation is set");
		}

		if (firstSize < firstMinSize) {
			secondSize = secondSize - (firstMinSize - firstSize);
			firstSize = firstMinSize;
		}
		if (secondSize < secondMinSize) {
			firstSize = Math.max(firstMinSize, firstSize - (secondMinSize - secondSize));
			secondSize = secondMinSize;
		}

		final Rectangle firstBounds = parent.getFirst().getBounds();
		final Rectangle sashBounds = parent.getSash().getBounds();
		final Rectangle secondBounds = parent.getSecond().getBounds();

		sashUtil.updateBounds(firstBounds, area, sashUtil.getPosition(firstBounds), firstSize);
		sashUtil.updateBounds(sashBounds, area, sashUtil.getPosition(firstBounds) + firstSize, parent.getSashSize());
		sashUtil.updateBounds(secondBounds, area, sashUtil.getPosition(sashBounds) + parent.getSashSize(), secondSize);

		parent.getFirst().setBounds(firstBounds);
		parent.getSash().setBounds(sashBounds);
		parent.getSecond().setBounds(secondBounds);
	}
}
