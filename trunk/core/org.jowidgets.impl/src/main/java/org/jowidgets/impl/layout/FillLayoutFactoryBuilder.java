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

import org.jowidgets.api.layout.IFillLayoutFactoryBuilder;
import org.jowidgets.api.layout.ILayoutFactory;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.common.widgets.layout.ILayouter;

final class FillLayoutFactoryBuilder implements IFillLayoutFactoryBuilder {

	private int marginTop;
	private int marginBottom;
	private int marginLeft;
	private int marginRight;

	@Override
	public IFillLayoutFactoryBuilder margin(final int margin) {
		this.marginTop = margin;
		this.marginBottom = margin;
		this.marginLeft = margin;
		this.marginRight = margin;
		return this;
	}

	@Override
	public IFillLayoutFactoryBuilder marginLeft(final int marginLeft) {
		this.marginLeft = marginLeft;
		return this;
	}

	@Override
	public IFillLayoutFactoryBuilder marginRight(final int marginRight) {
		this.marginRight = marginRight;
		return this;
	}

	@Override
	public IFillLayoutFactoryBuilder marginTop(final int marginTop) {
		this.marginTop = marginTop;
		return this;
	}

	@Override
	public IFillLayoutFactoryBuilder marginBottom(final int marginBottom) {
		this.marginBottom = marginBottom;
		return this;
	}

	@Override
	public ILayoutFactory<ILayouter> build() {
		return new ILayoutFactory<ILayouter>() {
			@Override
			public ILayouter create(final IContainer container) {
				return new FillLayout(container, marginLeft, marginRight, marginTop, marginBottom);
			}

		};
	}

}
