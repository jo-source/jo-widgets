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

import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.util.Assert;

final class FillLayout implements ILayouter {

	private final IContainer container;

	private Dimension minSize;
	private Dimension preferredSize;
	private Dimension maxSize;

	FillLayout(final IContainer container) {
		Assert.paramNotNull(container, "container");
		this.container = container;
	}

	@Override
	public void layout() {
		final IControl control = getFirstControl();
		if (control != null) {
			control.setSize(container.getClientAreaSize());
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
		if (maxSize == null) {
			this.maxSize = calcMaxSize();
		}
		return maxSize;
	}

	@Override
	public void invalidate() {
		maxSize = null;
		minSize = null;
		preferredSize = null;
	}

	private Dimension calcMinSize() {
		final IControl control = getFirstControl();
		if (control != null) {
			return control.getMinSize();
		}
		else {
			return new Dimension(0, 0);
		}
	}

	private Dimension calcPreferredSize() {
		final IControl control = getFirstControl();
		if (control != null) {
			return control.getPreferredSize();
		}
		else {
			return new Dimension(0, 0);
		}
	}

	private Dimension calcMaxSize() {
		final IControl control = getFirstControl();
		if (control != null) {
			return control.getMaxSize();
		}
		else {
			return new Dimension(0, 0);
		}
	}

	private IControl getFirstControl() {
		for (final IControl control : container.getChildren()) {
			return control;
		}
		return null;
	}
}
