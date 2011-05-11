/*
 * Copyright (c) 2010, grossmann
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
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IContainerCommon;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.spi.widgets.IContainerSpi;
import org.jowidgets.spi.widgets.IControlSpi;
import org.jowidgets.util.Assert;

public abstract class AbstractContainerSpiWrapper extends ComponentSpiWrapper implements IContainerCommon {

	private ILayouter layouter;

	private Dimension minSize;
	private Dimension prefferedSize;
	private Dimension maxSize;

	public AbstractContainerSpiWrapper(final IContainerSpi widget) {
		super(widget);
	}

	@Override
	public IContainerSpi getWidget() {
		return (IContainerSpi) super.getWidget();
	}

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {
		Assert.paramNotNull(layoutDescriptor, "layoutDescriptor");
		if (layoutDescriptor instanceof ILayouter) {
			this.layouter = (ILayouter) layoutDescriptor;
		}
		getWidget().setLayout(layoutDescriptor);
	}

	@Override
	public void layoutBegin() {
		getWidget().layoutBegin();
	}

	@Override
	public void layoutEnd() {
		getWidget().layoutEnd();
	}

	@Override
	public Rectangle getClientArea() {
		return getWidget().getClientArea();
	}

	@Override
	public Dimension computeDecoratedSize(final Dimension clientAreaSize) {
		return getWidget().computeDecoratedSize(clientAreaSize);
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

	public Dimension getMinSize() {
		if (minSize != null) {
			return minSize;
		}
		else if (layouter != null) {
			return layouter.getMinSize();
		}
		else if (getWidget() instanceof IControlSpi) {
			return ((IControlSpi) getWidget()).getMinSize();
		}
		else {
			return null;
		}
	}

	public Dimension getPreferredSize() {
		if (prefferedSize != null) {
			return prefferedSize;
		}
		else if (layouter != null) {
			return layouter.getPreferredSize();
		}
		else if (getWidget() instanceof IControlSpi) {
			return ((IControlSpi) getWidget()).getPreferredSize();
		}
		else {
			return null;
		}
	}

	public Dimension getMaxSize() {
		if (maxSize != null) {
			return maxSize;
		}
		else if (layouter != null) {
			return layouter.getMaxSize();
		}
		else if (getWidget() instanceof IControlSpi) {
			return ((IControlSpi) getWidget()).getMaxSize();
		}
		else {
			return null;
		}
	}

}
