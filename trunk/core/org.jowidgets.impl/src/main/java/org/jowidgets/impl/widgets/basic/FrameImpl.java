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
import org.jowidgets.api.model.item.IMenuBarModel;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IDisplay;
import org.jowidgets.api.widgets.IMenuBar;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.api.widgets.descriptor.setup.IFrameSetup;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.impl.base.delegate.ContainerDelegate;
import org.jowidgets.impl.base.delegate.DisplayDelegate;
import org.jowidgets.impl.base.delegate.WindowDelegate;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.LayoutSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.impl.widgets.common.wrapper.AbstractFrameSpiWrapper;
import org.jowidgets.spi.widgets.IFrameSpi;
import org.jowidgets.test.api.widgets.IFrameUi;
import org.jowidgets.util.Assert;

public class FrameImpl extends AbstractFrameSpiWrapper implements IFrameUi {

	private final DisplayDelegate displayDelegate;
	private final WindowDelegate windowDelegate;
	private final ContainerDelegate containerDelegate;

	private IMenuBar menuBar;

	public FrameImpl(final IFrameSpi frameWidgetSpi, final IFrameSetup setup) {
		super(frameWidgetSpi);
		this.displayDelegate = new DisplayDelegate();
		this.windowDelegate = new WindowDelegate(frameWidgetSpi, this, setup);
		this.containerDelegate = new ContainerDelegate(frameWidgetSpi, this);
		ColorSettingsInvoker.setColors(setup, this);
		VisibiliySettingsInvoker.setVisibility(setup, frameWidgetSpi);
		LayoutSettingsInvoker.setLayout(setup, this);

		if (setup.getMinSize() != null) {
			setMinSize(setup.getMinSize());
		}
	}

	@Override
	public <LAYOUT_TYPE extends ILayouter> LAYOUT_TYPE setLayout(final ILayoutFactory<LAYOUT_TYPE> layoutFactory) {
		Assert.paramNotNull(layoutFactory, "layoutFactory");
		final LAYOUT_TYPE result = layoutFactory.create(this);
		setLayout(result);
		return result;
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final int index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		return containerDelegate.add(index, descriptor, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IControl> WIDGET_TYPE add(
		final int index,
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object layoutConstraints) {
		return containerDelegate.add(index, creator, layoutConstraints);
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
	public <WIDGET_TYPE extends IDisplay, DESCRIPTOR_TYPE extends IWidgetDescriptor<? extends WIDGET_TYPE>> WIDGET_TYPE createChildWindow(
		final DESCRIPTOR_TYPE descriptor) {
		return windowDelegate.createChildWindow(descriptor);
	}

	@Override
	public void centerLocation() {
		windowDelegate.centerLocation();
	}

	@Override
	public void setVisible(final boolean visible) {
		windowDelegate.setVisible(visible);
	}

	@Override
	public List<IDisplay> getChildWindows() {
		return windowDelegate.getChildWindows();
	}

	@Override
	public void setPosition(final Position position) {
		windowDelegate.setPosition(position);
	}

	@Override
	public void setSize(final Dimension size) {
		windowDelegate.setSize(size);
	}

	@Override
	public void setMinSize(final int width, final int height) {
		setMinSize(new Dimension(width, height));
	}

	@Override
	public void setMinSize(final Dimension minSize) {
		Assert.paramNotNull(minSize, "minSize");
		getWidget().setMinSize(minSize);
	}

	@Override
	public void setDefaultButton(final IButton button) {
		getWidget().setDefaultButton(button);
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return new PopupMenuImpl(getWidget().createPopupMenu(), this);
	}

	@Override
	public IMenuBar createMenuBar() {
		if (menuBar == null) {
			menuBar = new MenuBarImpl(getWidget().createMenuBar(), this);
		}
		return menuBar;
	}

	@Override
	public IMenuBarModel getMenuBarModel() {
		return createMenuBar().getModel();
	}

	@Override
	public void setMenuBar(final IMenuBarModel model) {
		Assert.paramNotNull(model, "model");
		createMenuBar().setModel(model);
	}

	@Override
	public boolean isTestable() {
		return true;
	}

}
