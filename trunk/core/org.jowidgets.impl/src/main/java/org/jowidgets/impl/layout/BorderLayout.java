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

final class BorderLayout implements ILayouter {

	private static final Dimension MAX_SIZE = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);

	private final IContainer container;

	private final int gapX;
	private final int gapY;
	private final int marginTop;
	private final int marginBottom;
	private final int marginLeft;
	private final int marginRight;

	private final Map<IControl, Dimension> prefSizes;
	private final Map<IControl, Dimension> minSizes;

	private Map<BorderLayoutConstraints, IControl> controlsMap;
	private Dimension minSize;
	private Dimension preferredSize;

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

		this.prefSizes = new HashMap<IControl, Dimension>();
		this.minSizes = new HashMap<IControl, Dimension>();
	}

	@Override
	public void layout() {
		final Rectangle clientArea = container.getClientArea();

		int x = clientArea.getX() + marginLeft;
		int y = clientArea.getY() + marginTop;

		int centerHeight = clientArea.getHeight() - marginTop - marginBottom;
		int centerWidth = clientArea.getWidth() - marginLeft - marginRight;

		final Map<BorderLayoutConstraints, IControl> controls = getControls();

		final IControl topControl = controls.get(BorderLayoutConstraints.TOP);
		if (topControl != null) {
			final Dimension size = getControlPrefSize(topControl);
			topControl.setPosition(x, y);
			topControl.setSize(clientArea.getWidth() - marginLeft - marginRight, size.getHeight());

			y = y + gapY + size.getHeight();
			centerHeight = centerHeight - size.getHeight() - gapY;
		}

		final IControl bottomControl = controls.get(BorderLayoutConstraints.BOTTOM);
		if (bottomControl != null) {
			final Dimension size = getControlPrefSize(bottomControl);
			bottomControl.setPosition(x, clientArea.getY() + clientArea.getHeight() - marginBottom - size.getHeight());
			bottomControl.setSize(clientArea.getWidth() - marginLeft - marginRight, size.getHeight());

			centerHeight = centerHeight - size.getHeight() - gapY;
		}

		final IControl leftControl = controls.get(BorderLayoutConstraints.LEFT);
		if (leftControl != null) {
			final Dimension size = getControlPrefSize(leftControl);
			leftControl.setPosition(x, y);
			leftControl.setSize(size.getWidth(), centerHeight);

			x = x + gapX + size.getWidth();
			centerWidth = centerWidth - size.getWidth() - gapX;
		}

		final IControl rigthControl = controls.get(BorderLayoutConstraints.RIGHT);
		if (rigthControl != null) {
			final Dimension size = getControlPrefSize(rigthControl);
			rigthControl.setPosition(clientArea.getX() + clientArea.getWidth() - marginRight - size.getWidth(), y);
			rigthControl.setSize(size.getWidth(), centerHeight);

			centerWidth = centerWidth - size.getWidth() - gapX;
		}

		final IControl centerControl = controls.get(BorderLayoutConstraints.CENTER);
		if (centerControl != null) {
			centerControl.setPosition(x, y);
			centerControl.setSize(centerWidth, centerHeight);
		}
	}

	@Override
	public Dimension getMinSize() {
		if (minSize == null) {
			this.minSize = calcMinSize();
		}
		return minSize;
	}

	@Override
	public Dimension getPreferredSize() {
		if (preferredSize == null) {
			this.preferredSize = calcPreferredSize();
		}
		return preferredSize;
	}

	@Override
	public Dimension getMaxSize() {
		return MAX_SIZE;
	}

	@Override
	public void invalidate() {
		minSize = null;
		preferredSize = null;
		controlsMap = null;
		prefSizes.clear();
		minSizes.clear();
	}

	private Dimension calcMinSize() {
		final Map<BorderLayoutConstraints, IControl> controls = getControls();

		int width = 0;
		int height = 0;

		final IControl leftControl = controls.get(BorderLayoutConstraints.LEFT);
		if (leftControl != null) {
			final Dimension size = getControlMinSize(leftControl);
			width = width + size.getWidth() + gapX;
			height = Math.max(height, size.getHeight());
		}

		final IControl centerControl = controls.get(BorderLayoutConstraints.CENTER);
		if (centerControl != null) {
			final Dimension size = getControlMinSize(centerControl);
			width = width + size.getWidth();
			height = height + size.getHeight();
		}

		final IControl rightControl = controls.get(BorderLayoutConstraints.RIGHT);
		if (rightControl != null) {
			final Dimension size = getControlMinSize(rightControl);
			width = width + size.getWidth() + gapX;
			height = Math.max(height, size.getHeight());
		}

		final IControl topControl = controls.get(BorderLayoutConstraints.TOP);
		if (topControl != null) {
			final Dimension size = getControlMinSize(topControl);
			height = height + size.getHeight() + gapY;
			width = Math.max(width, size.getWidth());
		}

		final IControl bottomControl = controls.get(BorderLayoutConstraints.BOTTOM);
		if (bottomControl != null) {
			final Dimension size = getControlMinSize(bottomControl);
			height = height + size.getHeight() + gapY;
			width = Math.max(width, size.getWidth());
		}

		width = width + marginLeft + marginRight;
		height = height + marginTop + marginBottom;

		return container.computeDecoratedSize(new Dimension(width, height));
	}

	private Dimension calcPreferredSize() {
		final Map<BorderLayoutConstraints, IControl> controls = getControls();

		int width = 0;
		int height = 0;

		final IControl leftControl = controls.get(BorderLayoutConstraints.LEFT);
		if (leftControl != null) {
			final Dimension size = getControlPrefSize(leftControl);
			width = width + size.getWidth() + gapX;
			height = Math.max(height, size.getHeight());
		}

		final IControl centerControl = controls.get(BorderLayoutConstraints.CENTER);
		if (centerControl != null) {
			final Dimension size = getControlPrefSize(centerControl);
			width = width + size.getWidth();
			height = height + size.getHeight();
		}

		final IControl rightControl = controls.get(BorderLayoutConstraints.RIGHT);
		if (rightControl != null) {
			final Dimension size = getControlPrefSize(rightControl);
			width = width + size.getWidth() + gapX;
			height = Math.max(height, size.getHeight());
		}

		final IControl topControl = controls.get(BorderLayoutConstraints.TOP);
		if (topControl != null) {
			final Dimension size = getControlPrefSize(topControl);
			height = height + size.getHeight() + gapY;
			width = Math.max(width, size.getWidth());
		}

		final IControl bottomControl = controls.get(BorderLayoutConstraints.BOTTOM);
		if (bottomControl != null) {
			final Dimension size = getControlPrefSize(bottomControl);
			height = height + size.getHeight() + gapY;
			width = Math.max(width, size.getWidth());
		}

		width = width + marginLeft + marginRight;
		height = height + marginTop + marginBottom;

		return container.computeDecoratedSize(new Dimension(width, height));
	}

	private Dimension getControlMinSize(final IControl control) {
		Dimension result = minSizes.get(control);
		if (result == null) {
			result = control.getMinSize();
			minSizes.put(control, result);
		}
		return result;
	}

	private Dimension getControlPrefSize(final IControl control) {
		Dimension result = prefSizes.get(control);
		if (result == null) {
			result = control.getPreferredSize();
			prefSizes.put(control, result);
		}
		return result;
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

}
