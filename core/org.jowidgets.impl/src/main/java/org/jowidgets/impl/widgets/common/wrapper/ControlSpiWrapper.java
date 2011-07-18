/*
 * Copyright (c) 2010, grossmann, Nikolaus Moll
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

package org.jowidgets.impl.widgets.common.wrapper;

import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.spi.widgets.IControlSpi;

public class ControlSpiWrapper extends ComponentSpiWrapper implements IControlCommon {
	private static final Dimension INFINITE_SIZE = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);

	private Dimension minSize;
	private Dimension prefferedSize;
	private Dimension maxSize;

	public ControlSpiWrapper(final IControlSpi control) {
		super(control);
	}

	@Override
	public IControlSpi getWidget() {
		return (IControlSpi) super.getWidget();
	}

	@Override
	public void setLayoutConstraints(final Object layoutConstraints) {
		getWidget().setLayoutConstraints(layoutConstraints);
	}

	@Override
	public Object getLayoutConstraints() {
		return getWidget().getLayoutConstraints();
	}

	public void setMinSize(final Dimension minSize) {
		this.minSize = minSize;
	}

	public void setPreferredSize(final Dimension preferredSize) {
		this.prefferedSize = preferredSize;
	}

	public void setMaxSize(final Dimension maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	public Dimension getMinSize() {
		if (minSize != null) {
			return minSize;
		}
		else {
			return getWidget().getMinSize();
		}
	}

	@Override
	public Dimension getPreferredSize() {
		if (prefferedSize != null) {
			return prefferedSize;
		}
		else {
			return getWidget().getPreferredSize();
		}
	}

	@Override
	public Dimension getMaxSize() {
		if (maxSize != null) {
			return maxSize;
		}
		else {
			return INFINITE_SIZE;
		}
	}
}
