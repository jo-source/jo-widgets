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
import java.util.Map;

import org.jowidgets.api.widgets.IControl;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.layout.ILayouter;

abstract class AbstractCachingLayout implements ILayouter {
	private static final Dimension MAX_SIZE = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
	private static final Dimension EMPTY_SIZE = new Dimension(0, 0);

	private final Map<IControl, Dimension> prefSizes;
	private final Map<IControl, Dimension> minSizes;

	private final ISizeProvider minimum;
	private final ISizeProvider preferred;

	private Dimension preferredSize;
	private Dimension minSize;

	AbstractCachingLayout() {
		this.prefSizes = new HashMap<IControl, Dimension>();
		this.minSizes = new HashMap<IControl, Dimension>();

		this.minimum = new ISizeProvider() {
			@Override
			public Dimension getSize(final IControl control) {
				return getControlMinSize(control);
			}
		};
		this.preferred = new ISizeProvider() {
			@Override
			public Dimension getSize(final IControl control) {
				return getControlPrefSize(control);
			}
		};
	}

	protected ISizeProvider minimumPolicy() {
		return minimum;
	}

	protected ISizeProvider preferredPolicy() {
		return preferred;
	}

	protected abstract Dimension calculateMinSize();

	protected abstract Dimension calculatePreferredSize();

	protected Dimension getControlMinSize(final IControl control) {
		if (control == null) {
			return EMPTY_SIZE;
		}

		Dimension result = minSizes.get(control);
		if (result == null) {
			result = control.getMinSize();
			minSizes.put(control, result);
		}
		return result;
	}

	protected Dimension getControlPrefSize(final IControl control) {
		if (control == null) {
			return EMPTY_SIZE;
		}

		Dimension result = prefSizes.get(control);
		if (result == null) {
			result = control.getPreferredSize();
			prefSizes.put(control, result);
		}
		return result;
	}

	@Override
	public void invalidate() {
		prefSizes.clear();
		minSizes.clear();
		minSize = null;
		preferredSize = null;
	}

	@Override
	public Dimension getMinSize() {
		if (minSize == null) {
			this.minSize = calculateMinSize();
		}
		return minSize;
	}

	@Override
	public Dimension getPreferredSize() {
		if (preferredSize == null) {
			this.preferredSize = calculatePreferredSize();
		}
		return preferredSize;
	}

	@Override
	public Dimension getMaxSize() {
		return MAX_SIZE;
	}

	interface ISizeProvider {
		Dimension getSize(IControl control);
	}
}
