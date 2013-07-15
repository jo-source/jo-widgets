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

import java.util.HashMap;
import java.util.Map;

import org.jowidgets.api.layout.BorderLayoutConstraints;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.util.Assert;

final class BorderLayout extends AbstractCachingLayout implements ILayouter {
	private final IContainer container;

	private final int gapX;
	private final int gapY;
	private final int marginTop;
	private final int marginBottom;
	private final int marginLeft;
	private final int marginRight;

	private Map<BorderLayoutConstraints, IControl> controlsMap;

	BorderLayout(
		final IContainer container,
		final int marginLeft,
		final int marginRight,
		final int marginTop,
		final int marginBottom,
		final int gapX,
		final int gapY) {
		Assert.paramNotNull(container, "container");
		this.container = container;
		this.gapX = gapX;
		this.gapY = gapY;
		this.marginLeft = marginLeft;
		this.marginRight = marginRight;
		this.marginTop = marginTop;
		this.marginBottom = marginBottom;

	}

	@Override
	public void layout() {
		final Rectangle clientArea = container.getClientArea();
		final Map<BorderLayoutConstraints, IControl> controls = getControls();

		final int rowCount = getRowCount(controls);
		final int centerColumnCount = getCenterColumnCount(controls);
		final Dimension minimumSize = calcControlsSize(minimumPolicy());

		final int x = clientArea.getX() + marginLeft;
		int y = clientArea.getY() + marginTop;

		// do not allow sizes less than minimum size
		final int totalHeight = Math.max(clientArea.getHeight() - marginTop - marginBottom, minimumSize.getHeight());
		final int totalWidth = Math.max(clientArea.getWidth() - marginLeft - marginRight, minimumSize.getWidth());

		final IControl topControl = controls.get(BorderLayoutConstraints.TOP);
		final IControl bottomControl = controls.get(BorderLayoutConstraints.BOTTOM);
		final IControl leftControl = controls.get(BorderLayoutConstraints.LEFT);
		final IControl centerControl = controls.get(BorderLayoutConstraints.CENTER);
		final IControl rightControl = controls.get(BorderLayoutConstraints.RIGHT);

		final int[] minimumHeights = new int[] {
				minimumPolicy().getSize(topControl).getHeight(), calcCenterHeight(controls, minimumPolicy()),
				minimumPolicy().getSize(bottomControl).getHeight()};
		final int[] preferredHeights = new int[] {
				preferredPolicy().getSize(topControl).getHeight(), calcCenterHeight(controls, preferredPolicy()),
				preferredPolicy().getSize(bottomControl).getHeight()};

		final int[] minimumWidths = new int[] {
				minimumPolicy().getSize(leftControl).getWidth(),
				minimumPolicy().getSize(leftControl).getWidth()
					+ minimumPolicy().getSize(centerControl).getWidth()
					+ minimumPolicy().getSize(rightControl).getWidth(), minimumPolicy().getSize(rightControl).getWidth()};
		final int[] preferredWidths = new int[] {
				preferredPolicy().getSize(leftControl).getWidth(),
				preferredPolicy().getSize(leftControl).getWidth()
					+ preferredPolicy().getSize(centerControl).getWidth()
					+ preferredPolicy().getSize(rightControl).getWidth(), preferredPolicy().getSize(rightControl).getWidth()};

		final int[] usedHeights = calcRatio(minimumHeights, preferredHeights, totalHeight - (rowCount - 1) * gapY);
		final int[] usedWidths = calcRatio(minimumWidths, preferredWidths, totalWidth - (centerColumnCount - 1) * gapX);

		// TODO MG,NM BorderLayout - calculate gaps after top, before bottom, after left and before right
		final int gapYAfterTop = gapY;
		final int gapYBeforeBottom = gapY;
		final int gapXAfterLeft = gapX;
		final int gapXBeforeRight = gapX;

		if (topControl != null) {
			topControl.setPosition(x, y);
			topControl.setSize(totalWidth, usedHeights[0]);
			y = y + usedHeights[0] + gapYAfterTop;
		}

		int centerX = x;
		if (leftControl != null) {
			leftControl.setPosition(centerX, y);
			leftControl.setSize(usedWidths[0], usedHeights[1]);
			centerX = centerX + usedWidths[0] + gapXAfterLeft;
		}
		if (centerControl != null) {
			centerControl.setPosition(centerX, y);
			centerControl.setSize(usedWidths[1], usedHeights[1]);
			centerX = centerX + usedWidths[1];
		}
		if (rightControl != null) {
			centerX = centerX + gapXBeforeRight;
			rightControl.setPosition(centerX, y);
			rightControl.setSize(usedWidths[2], usedHeights[1]);
		}

		y = y + usedHeights[1];

		if (bottomControl != null) {
			y = y + gapYBeforeBottom;
			bottomControl.setPosition(x, y);
			bottomControl.setSize(totalWidth, usedHeights[2]);
		}
	}

	private int[] calcRatio(final int[] minSizes, final int[] prefSizes, final int totalSize) {
		final int totalMin = minSizes[0] + minSizes[1] + minSizes[2];
		final int totalPref = prefSizes[0] + prefSizes[1] + prefSizes[2];
		if (totalSize < totalMin) {
			return minSizes;
		}

		final int[] result = new int[3];
		if (totalSize >= totalPref) {
			result[0] = prefSizes[0];
			result[2] = prefSizes[2];
		}
		else {
			final int availableSize = totalSize - totalMin;
			final double ratio = (double) availableSize / (totalPref - totalMin);
			result[0] = (int) (ratio * (prefSizes[0] - minSizes[0]) + minSizes[0]);
			result[2] = (int) (ratio * (prefSizes[2] - minSizes[2]) + minSizes[2]);
		}
		result[1] = totalSize - result[0] - result[2];
		return result;
	}

	private int calcCenterHeight(final Map<BorderLayoutConstraints, IControl> controls, final ISizeProvider policy) {
		int height = 0;

		height = Math.max(policy.getSize(controls.get(BorderLayoutConstraints.LEFT)).getHeight(), height);
		height = Math.max(policy.getSize(controls.get(BorderLayoutConstraints.CENTER)).getHeight(), height);
		height = Math.max(policy.getSize(controls.get(BorderLayoutConstraints.RIGHT)).getHeight(), height);

		return height;
	}

	private int getRowCount(final Map<BorderLayoutConstraints, IControl> controls) {
		int result = 0;
		if (controls.get(BorderLayoutConstraints.TOP) != null) {
			result++;
		}
		if (controls.get(BorderLayoutConstraints.BOTTOM) != null) {
			result++;
		}
		if (controls.get(BorderLayoutConstraints.CENTER) != null
			|| controls.get(BorderLayoutConstraints.LEFT) != null
			|| controls.get(BorderLayoutConstraints.RIGHT) != null) {
			result++;
		}

		return result;
	}

	private int getCenterColumnCount(final Map<BorderLayoutConstraints, IControl> controls) {
		int result = 0;
		if (controls.get(BorderLayoutConstraints.LEFT) != null) {
			result++;
		}
		if (controls.get(BorderLayoutConstraints.RIGHT) != null) {
			result++;
		}
		if (controls.get(BorderLayoutConstraints.CENTER) != null) {
			result++;
		}

		return result;
	}

	@Override
	public void invalidate() {
		super.invalidate();
		controlsMap = null;
	}

	private Dimension calcControlsSize(final ISizeProvider policy) {
		final Map<BorderLayoutConstraints, IControl> controls = getControls();

		int width = 0;
		int height = 0;
		int horizontalCount = 0;
		int verticalCount = 0;

		final IControl centerControl = controls.get(BorderLayoutConstraints.CENTER);
		if (centerControl != null) {
			final Dimension size = policy.getSize(centerControl);
			width = size.getWidth();
			height = size.getHeight();
			horizontalCount++;
			verticalCount++;
		}

		final IControl leftControl = controls.get(BorderLayoutConstraints.LEFT);
		if (leftControl != null) {
			final Dimension size = policy.getSize(leftControl);
			width = width + size.getWidth();
			height = Math.max(height, size.getHeight());
			horizontalCount++;
		}

		final IControl rightControl = controls.get(BorderLayoutConstraints.RIGHT);
		if (rightControl != null) {
			final Dimension size = policy.getSize(rightControl);
			width = width + size.getWidth();
			height = Math.max(height, size.getHeight());
			horizontalCount++;
		}

		width = width + Math.max(0, (horizontalCount - 1)) * gapX;

		final IControl topControl = controls.get(BorderLayoutConstraints.TOP);
		if (topControl != null) {
			final Dimension size = policy.getSize(topControl);
			height = height + size.getHeight();
			width = Math.max(width, size.getWidth());
			verticalCount++;
		}

		final IControl bottomControl = controls.get(BorderLayoutConstraints.BOTTOM);
		if (bottomControl != null) {
			final Dimension size = policy.getSize(bottomControl);
			height = height + size.getHeight();
			width = Math.max(width, size.getWidth());
			verticalCount++;
		}

		height = height + Math.max(0, (verticalCount - 1)) * gapY;
		return new Dimension(width, height);
	}

	private Dimension calcSize(final ISizeProvider policy) {
		final Dimension controlsSize = calcControlsSize(policy);
		final int width = controlsSize.getWidth() + marginLeft + marginRight;
		final int height = controlsSize.getHeight() + marginTop + marginBottom;
		return container.computeDecoratedSize(new Dimension(width, height));
	}

	private Map<BorderLayoutConstraints, IControl> getControls() {
		if (controlsMap == null) {
			controlsMap = new HashMap<BorderLayoutConstraints, IControl>();
			for (final IControl control : container.getChildren()) {
				Object constraints = control.getLayoutConstraints();
				if (constraints != null && !(constraints instanceof BorderLayoutConstraints)) {
					throw new IllegalStateException("Constraints muts be instance of '"
						+ BorderLayoutConstraints.class.getName()
						+ "'");
				}
				else if (constraints == null) {
					constraints = BorderLayoutConstraints.CENTER;
				}

				final BorderLayoutConstraints blConstraints = (BorderLayoutConstraints) constraints;

				if (controlsMap.get(blConstraints) != null) {
					throw new IllegalStateException("Container has more than one control with the constraint '"
						+ blConstraints
						+ "'");
				}
				else {
					controlsMap.put(blConstraints, control);
				}
			}
		}
		return controlsMap;
	}

	@Override
	protected Dimension calculateMinSize() {
		return calcSize(minimumPolicy());
	}

	@Override
	protected Dimension calculatePreferredSize() {
		return calcSize(preferredPolicy());
	}

}
