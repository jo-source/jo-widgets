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
import java.util.List;
import java.util.Map;

import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Orientation;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.util.Assert;

final class FlowLayout implements ILayouter {

	private static final Dimension MAX_SIZE = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);

	private final IContainer container;
	private final int gap;
	private final Orientation orientation;
	private final Map<IControl, Dimension> preferredSizes;

	private Dimension preferredSize;

	FlowLayout(final IContainer container, final int gap, final Orientation orientation) {
		Assert.paramNotNull(container, "container");
		this.container = container;
		this.gap = gap;
		this.orientation = orientation;

		this.preferredSizes = new HashMap<IControl, Dimension>();
	}

	@Override
	public void layout() {
		final Rectangle clientArea = container.getClientArea();

		int x = clientArea.getX();
		int y = clientArea.getY();

		final List<IControl> children = container.getChildren();
		if (Orientation.HORIZONTAL == orientation) {
			for (final IControl control : children) {
				final Dimension prefSize = getPreferredSize(control);
				control.setSize(prefSize);
				control.setPosition(x, y);
				x = x + gap + prefSize.getWidth();
			}
		}
		else {
			for (final IControl control : children) {
				final Dimension prefSize = getPreferredSize(control);
				control.setSize(prefSize);
				control.setPosition(x, y);
				y = y + gap + prefSize.getHeight();
			}
		}

	}

	@Override
	public Dimension getMinSize() {
		return getPreferredSize();
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
		preferredSize = null;
		preferredSizes.clear();
	}

	private Dimension calcPreferredSize() {

		int width = 0;
		int height = 0;

		final List<IControl> children = container.getChildren();
		if (Orientation.HORIZONTAL == orientation) {
			for (final IControl control : children) {
				final Dimension prefSize = getPreferredSize(control);
				width = width + gap + prefSize.getWidth();
				height = Math.max(height, prefSize.getHeight());
			}
			if (children.size() > 0) {
				width = width - gap;
			}
		}
		else {
			for (final IControl control : children) {
				final Dimension prefSize = getPreferredSize(control);
				height = height + gap + prefSize.getHeight();
				width = Math.max(width, prefSize.getWidth());
			}
			if (children.size() > 0) {
				height = height - gap;
			}
		}

		return container.computeDecoratedSize(new Dimension(width, height));
	}

	private Dimension getPreferredSize(final IControl control) {
		Dimension result = preferredSizes.get(control);
		if (result == null) {
			result = control.getPreferredSize();
			preferredSizes.put(control, result);
		}
		return result;
	}
}
