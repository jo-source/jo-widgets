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

package org.jowidgets.impl.widgets.basic;

import java.util.List;

import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.descriptor.ITabItemDescriptor;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.controler.ITabItemListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.impl.base.delegate.ComponentDelegate;
import org.jowidgets.impl.base.delegate.ContainerDelegate;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.impl.widgets.common.wrapper.AbstractContainerSpiWrapper;
import org.jowidgets.spi.widgets.ITabItemSpi;

public class TabItemImpl extends AbstractContainerSpiWrapper implements ITabItem {

	private final ContainerDelegate containerDelegate;
	private final ComponentDelegate componentDelegate;

	public TabItemImpl(final ITabItemSpi widget, final ITabItemDescriptor descriptor) {
		super(widget);
		this.componentDelegate = new ComponentDelegate();

		VisibiliySettingsInvoker.setVisibility(descriptor, this);
		ColorSettingsInvoker.setColors(descriptor, this);

		getWidget().setText(descriptor.getText());
		getWidget().setToolTipText(descriptor.getToolTipText());
		getWidget().setIcon(descriptor.getIcon());

		this.containerDelegate = new ContainerDelegate(widget, this);
	}

	@Override
	public ITabItemSpi getWidget() {
		return (ITabItemSpi) super.getWidget();
	}

	@Override
	public void addTabItemListener(final ITabItemListener listener) {
		getWidget().addTabItemListener(listener);
	}

	@Override
	public void removeTabItemListener(final ITabItemListener listener) {
		getWidget().removeTabItemListener(listener);
	}

	@Override
	public void setText(final String text) {
		getWidget().setText(text);
	}

	@Override
	public void setToolTipText(final String text) {
		getWidget().setToolTipText(text);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		getWidget().setIcon(icon);
	}

	@Override
	public IComponent getParent() {
		return componentDelegate.getParent();
	}

	@Override
	public void setParent(final IComponent parent) {
		componentDelegate.setParent(parent);
	}

	@Override
	public boolean isReparentable() {
		return componentDelegate.isReparentable();
	}

	@Override
	public List<IControl> getChildren() {
		return containerDelegate.getChildren();
	}

	@Override
	public boolean remove(final IControl control) {
		return containerDelegate.remove(control);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		return containerDelegate.add(descriptor, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final ICustomWidgetFactory<WIDGET_TYPE> factory,
		final Object layoutConstraints) {
		return containerDelegate.add(factory, layoutConstraints);
	}

	@Override
	public void removeAll() {
		containerDelegate.removeAll();
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return new PopupMenuImpl(getWidget().createPopupMenu(), this);
	}

}
