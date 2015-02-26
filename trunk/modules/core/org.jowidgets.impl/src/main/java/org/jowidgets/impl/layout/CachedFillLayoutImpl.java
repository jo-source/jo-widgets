/*
 * Copyright (c) 2013, grossmann
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

import org.jowidgets.api.layout.ICachedFillLayout;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.util.Assert;

final class CachedFillLayoutImpl implements ICachedFillLayout {

	private static final Dimension MAX_SIZE = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);

	private final IContainer container;

	private Dimension preferredSize;
	private Dimension minSize;
	private Dimension maxSize;

	CachedFillLayoutImpl(final IContainer container) {
		Assert.paramNotNull(container, "container");
		this.container = container;
	}

	@Override
	public void layout() {
		final IControl control = getFirstControl();
		if (control != null) {
			control.setBounds(container.getClientArea());
		}
	}

	@Override
	public void invalidate() {}

	@Override
	public Dimension getPreferredSize() {
		final IControl control = getFirstControl();
		if (control != null) {
			if (preferredSize == null) {
				preferredSize = control.getPreferredSize();
			}
			return container.computeDecoratedSize(preferredSize);
		}
		else {
			return new Dimension(0, 0);
		}
	}

	@Override
	public Dimension getMinSize() {
		final IControl control = getFirstControl();
		if (control != null) {
			if (minSize == null) {
				minSize = control.getMinSize();
			}
			return container.computeDecoratedSize(minSize);
		}
		else {
			return new Dimension(0, 0);
		}
	}

	@Override
	public Dimension getMaxSize() {
		final IControl control = getFirstControl();
		if (control != null) {
			if (maxSize == null) {
				maxSize = control.getMaxSize();
			}
			return container.computeDecoratedSize(maxSize);
		}
		else {
			return MAX_SIZE;
		}
	}

	@Override
	public void clearCache() {
		minSize = null;
		maxSize = null;
		preferredSize = null;
	}

	private IControl getFirstControl() {
		for (final IControl control : container.getChildren()) {
			if (control.isVisible()) {
				return control;
			}
		}
		return null;
	}
}
