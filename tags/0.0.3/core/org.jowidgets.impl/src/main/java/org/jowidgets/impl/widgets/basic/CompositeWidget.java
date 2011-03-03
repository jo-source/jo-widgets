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

package org.jowidgets.impl.widgets.basic;

import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.descriptor.setup.ICompositeSetup;
import org.jowidgets.common.widgets.IContainerCommon;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.impl.base.delegate.ChildWidgetDelegate;
import org.jowidgets.impl.base.delegate.CompositeWidgetDelegate;
import org.jowidgets.impl.widgets.common.wrapper.AbstractContainerWidgetCommonWrapper;

public class CompositeWidget extends AbstractContainerWidgetCommonWrapper implements IComposite {

	private final ChildWidgetDelegate childWidgetDelegate;
	private final CompositeWidgetDelegate compositeWidgetDelegate;

	public CompositeWidget(final IContainerCommon containerWidgetCommon) {
		this(containerWidgetCommon, (Boolean) null);
	}

	public CompositeWidget(final IContainerCommon containerWidgetCommon, final ICompositeSetup setup) {
		this(containerWidgetCommon, setup.isVisible());
	}

	public CompositeWidget(final IContainerCommon containerWidgetCommon, final Boolean visible) {
		super(containerWidgetCommon);
		this.childWidgetDelegate = new ChildWidgetDelegate();

		if (visible != null) {
			setVisible(visible.booleanValue());
		}
		this.compositeWidgetDelegate = new CompositeWidgetDelegate(containerWidgetCommon, this);
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		return compositeWidgetDelegate.add(descriptor, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final ICustomWidgetFactory<WIDGET_TYPE> factory,
		final Object layoutConstraints) {
		return compositeWidgetDelegate.add(factory, layoutConstraints);
	}

	@Override
	public IWidget getParent() {
		return childWidgetDelegate.getParent();
	}

	@Override
	public void setParent(final IWidget parent) {
		childWidgetDelegate.setParent(parent);
	}

	@Override
	public boolean isReparentable() {
		return childWidgetDelegate.isReparentable();
	}
}
