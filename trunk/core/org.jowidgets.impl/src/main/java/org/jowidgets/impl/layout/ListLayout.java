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

package org.jowidgets.impl.layout;

import java.util.HashMap;

import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.layout.ILayouter;

public final class ListLayout implements ILayouter {

	private final IContainer container;
	private final HashMap<IControl, Dimension> preferredSizes;
	private Dimension preferredSize;
	private final IColorConstant[] colors;
	private int lastWidth;

	public ListLayout(final IContainer container, final IColorConstant[] backgroundColors) {
		this.container = container;
		this.colors = backgroundColors;
		this.preferredSizes = new HashMap<IControl, Dimension>();
		this.lastWidth = -1;
	}

	@Override
	public void layout() {
		final Rectangle clientArea = container.getClientArea();
		final int x = clientArea.getX();
		int y = clientArea.getY();
		final int width = clientArea.getWidth();

		int groupIndex = 0;

		for (final IControl control : container.getChildren()) {
			final boolean controlVisible = control.isVisible();
			if (!shouldControlBeVisible(control)) {
				if (controlVisible) {
					control.setVisible(false);
				}
				continue;
			}

			if (!controlVisible) {
				control.setVisible(true);
				if (control instanceof IContainer) {
					final IContainer c = (IContainer) control;
					c.layoutBegin();
					c.layoutEnd();
				}
			}

			final Dimension size = getPreferredSize(control);

			control.setPosition(x, y);
			control.setSize(width, size.getHeight());
			if (lastWidth != width && control instanceof IContainer) {
				final IContainer c = ((IContainer) control);
				c.layoutBegin();
				c.layoutEnd();
			}

			if (size.getHeight() > 0 && colors.length > 0) {
				control.setBackgroundColor(getAttributeColor(groupIndex));
				groupIndex++;
			}

			y = y + size.getHeight();
		}

		lastWidth = width;
	}

	private IColorConstant getAttributeColor(final int index) {
		return colors[index % colors.length];
	}

	@Override
	public Dimension getMinSize() {
		return getPreferredSize();
	}

	@Override
	public Dimension getPreferredSize() {
		if (preferredSize == null) {
			calculateSizes();
		}
		return preferredSize;
	}

	@Override
	public Dimension getMaxSize() {
		return getPreferredSize();
	}

	@Override
	public void invalidate() {
		preferredSizes.clear();
		preferredSize = null;
	}

	private void calculateSizes() {
		//final Rectangle clientArea = container.getClientArea();
		int preferredWidth = 0;
		int preferredHeight = 0;
		for (final IControl control : container.getChildren()) {
			final boolean controlVisible = control.isVisible();
			if (!shouldControlBeVisible(control)) {
				if (controlVisible) {
					control.setVisible(false);
				}
				continue;
			}

			if (!controlVisible) {
				control.setVisible(true);
			}

			final Dimension size = getPreferredSize(control);
			preferredWidth = Math.max(preferredWidth, size.getWidth());
			preferredHeight = preferredHeight + size.getHeight();
		}
		preferredSize = container.computeDecoratedSize(new Dimension(preferredWidth, preferredHeight));
	}

	private Dimension getPreferredSize(final IControl control) {
		if (!preferredSizes.containsKey(control)) {
			final Dimension size = control.getPreferredSize();
			if (size == null) {
				return new Dimension(0, 0);
			}
			if (size.getHeight() > 0) {
				preferredSizes.put(control, size);
			}
			return size;
		}
		return preferredSizes.get(control);
	}

	private boolean shouldControlBeVisible(final IControl control) {
		if (control.getLayoutConstraints() == null) {
			return true;
		}
		if (control.getLayoutConstraints() instanceof Boolean) {
			final Boolean visible = (Boolean) control.getLayoutConstraints();
			return visible.booleanValue();
		}

		return true;
	}

}
