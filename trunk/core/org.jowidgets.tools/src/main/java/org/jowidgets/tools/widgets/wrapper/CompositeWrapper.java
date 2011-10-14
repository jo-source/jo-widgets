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

package org.jowidgets.tools.widgets.wrapper;

import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.common.types.Dimension;

public class CompositeWrapper extends ContainerWrapper implements IComposite {

	public CompositeWrapper(final IComposite widget) {
		super(widget);
	}

	@Override
	protected IComposite getWidget() {
		return (IComposite) super.getWidget();
	}

	@Override
	public void setParent(final IContainer parent) {
		getWidget().setParent(parent);
	}

	@Override
	public IContainer getParent() {
		return getWidget().getParent();
	}

	@Override
	public IControl getRoot() {
		return getWidget().getRoot();
	}

	@Override
	public void setMinSize(final Dimension minSize) {
		getWidget().setMinSize(minSize);
	}

	@Override
	public void setPreferredSize(final Dimension preferredSize) {
		getWidget().setPreferredSize(preferredSize);
	}

	@Override
	public void setMaxSize(final Dimension maxSize) {
		getWidget().setMaxSize(maxSize);
	}

	@Override
	public void setLayoutConstraints(final Object layoutConstraints) {
		getWidget().setLayoutConstraints(layoutConstraints);
	}

	@Override
	public Object getLayoutConstraints() {
		return getWidget().getLayoutConstraints();
	}

	@Override
	public Dimension getMinSize() {
		return getWidget().getMinSize();
	}

	@Override
	public Dimension getPreferredSize() {
		return getWidget().getPreferredSize();
	}

	@Override
	public Dimension getMaxSize() {
		return getWidget().getMaxSize();
	}

	@Override
	public void setToolTipText(final String toolTip) {
		getWidget().setToolTipText(toolTip);
	}

}
