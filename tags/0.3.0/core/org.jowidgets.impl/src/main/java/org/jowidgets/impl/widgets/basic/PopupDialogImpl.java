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

import java.util.List;

import org.jowidgets.api.layout.ILayoutFactory;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IPopupDialog;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.descriptor.setup.IPopupDialogSetup;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.controler.IWindowListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.impl.base.delegate.ContainerDelegate;
import org.jowidgets.impl.base.delegate.DisplayDelegate;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.LayoutSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.impl.widgets.common.wrapper.ComponentSpiWrapper;
import org.jowidgets.spi.widgets.IPopupDialogSpi;
import org.jowidgets.tools.controler.WindowAdapter;
import org.jowidgets.util.Assert;

public class PopupDialogImpl extends ComponentSpiWrapper implements IPopupDialog {

	private final DisplayDelegate displayDelegate;
	private final ContainerDelegate containerDelegate;

	private ILayouter layouter;

	public PopupDialogImpl(final IPopupDialogSpi widget, final IPopupDialogSetup setup) {
		super(widget);
		this.displayDelegate = new DisplayDelegate();
		this.containerDelegate = new ContainerDelegate(widget, this);
		ColorSettingsInvoker.setColors(setup, this);
		VisibiliySettingsInvoker.setVisibility(setup, widget);
		LayoutSettingsInvoker.setLayout(setup, this);

		if (setup.isAutoDispose()) {
			final IWindowListener windowListener = new WindowAdapter() {
				@Override
				public void windowDeactivated() {
					removeWindowListener(this);
					dispose();
				}
			};

			getWidget().addWindowListener(windowListener);
		}
	}

	@Override
	public IPopupDialogSpi getWidget() {
		return (IPopupDialogSpi) super.getWidget();
	}

	@Override
	public void dispose() {
		getWidget().dispose();
	}

	@Override
	public <LAYOUT_TYPE extends ILayouter> LAYOUT_TYPE setLayout(final ILayoutFactory<LAYOUT_TYPE> layoutFactory) {
		Assert.paramNotNull(layoutFactory, "layoutFactory");
		final LAYOUT_TYPE result = layoutFactory.create(this);
		setLayout(result);
		return result;
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
	public void pack() {
		if (layouter != null) {
			setSize(layouter.getPreferredSize());
		}
		else {
			getWidget().pack();
		}
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

	@Override
	public void addWindowListener(final IWindowListener listener) {
		getWidget().addWindowListener(listener);
	}

	@Override
	public void removeWindowListener(final IWindowListener listener) {
		getWidget().removeWindowListener(listener);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		return containerDelegate.add(descriptor, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		return containerDelegate.add(creator, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
		return containerDelegate.add(descriptor);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(final ICustomWidgetCreator<WIDGET_TYPE> creator) {
		return containerDelegate.add(creator);
	}

	@Override
	public List<IControl> getChildren() {
		return containerDelegate.getChildren();
	}

	@Override
	public void removeAll() {
		containerDelegate.removeAll();
	}

	@Override
	public boolean remove(final IControl control) {
		return containerDelegate.remove(control);
	}

	@Override
	public IWindow getParent() {
		return displayDelegate.getParent();
	}

	@Override
	public void setParent(final IWindow parent) {
		displayDelegate.setParent(parent);
	}

	@Override
	public boolean isReparentable() {
		return displayDelegate.isReparentable();
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return new PopupMenuImpl(getWidget().createPopupMenu(), this);
	}

}
