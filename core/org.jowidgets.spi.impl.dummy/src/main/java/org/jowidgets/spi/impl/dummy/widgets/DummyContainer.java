/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.spi.impl.dummy.widgets;

import java.util.List;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.spi.impl.dummy.dummyui.UIDComponent;
import org.jowidgets.spi.impl.dummy.dummyui.UIDContainer;
import org.jowidgets.spi.widgets.IContainerSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.util.Assert;

public class DummyContainer implements IContainerSpi {

	private final IGenericWidgetFactory factory;
	private final UIDContainer container;
	private final DummyComponent dummyComponentDelegate;

	public DummyContainer(final IGenericWidgetFactory factory, final UIDContainer container) {
		Assert.paramNotNull(factory, "factory");
		Assert.paramNotNull(container, "container");

		this.factory = factory;
		this.container = container;
		this.dummyComponentDelegate = new DummyComponent(container);
	}

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {
		Assert.paramNotNull(layoutDescriptor, "layoutManager");
		if (layoutDescriptor instanceof MigLayoutDescriptor) {
			container.setLayout(layoutDescriptor);
		}
		else if (layoutDescriptor instanceof ILayouter) {
			container.setLayout(layoutDescriptor);
		}
		else {
			throw new IllegalArgumentException("Layout Manager of type '"
				+ layoutDescriptor.getClass().getName()
				+ "' is not supported");
		}
	}

	@Override
	public UIDContainer getUiReference() {
		return container;
	}

	@Override
	public void redraw() {
		dummyComponentDelegate.redraw();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		dummyComponentDelegate.setRedrawEnabled(enabled);
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		dummyComponentDelegate.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		dummyComponentDelegate.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return dummyComponentDelegate.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return dummyComponentDelegate.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		dummyComponentDelegate.setCursor(cursor);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		dummyComponentDelegate.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return dummyComponentDelegate.isEnabled();
	}

	@Override
	public void setVisible(final boolean visible) {
		dummyComponentDelegate.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return dummyComponentDelegate.isVisible();
	}

	@Override
	public Rectangle getClientArea() {
		return dummyComponentDelegate.getClientArea();
	}

	@Override
	public Dimension computeDecoratedSize(final Dimension clientAreaSize) {
		return dummyComponentDelegate.computeDecoratedSize(clientAreaSize);
	}

	@Override
	public Dimension getSize() {
		return dummyComponentDelegate.getSize();
	}

	@Override
	public void setSize(final Dimension size) {
		dummyComponentDelegate.setSize(size);
	}

	@Override
	public Position getPosition() {
		return dummyComponentDelegate.getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		dummyComponentDelegate.setPosition(position);
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return dummyComponentDelegate.createPopupMenu();
	}

	@Override
	public boolean requestFocus() {
		return dummyComponentDelegate.requestFocus();
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		dummyComponentDelegate.addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		dummyComponentDelegate.removeFocusListener(listener);
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		dummyComponentDelegate.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		dummyComponentDelegate.removeKeyListener(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener mouseListener) {
		dummyComponentDelegate.addMouseListener(mouseListener);
	}

	@Override
	public void removeMouseListener(final IMouseListener mouseListener) {
		dummyComponentDelegate.removeMouseListener(mouseListener);
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		dummyComponentDelegate.addComponentListener(componentListener);
	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		dummyComponentDelegate.removeComponentListener(componentListener);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		dummyComponentDelegate.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		dummyComponentDelegate.removePopupDetectionListener(listener);
	}

	@Override
	public void setTabOrder(final List<? extends IControlCommon> tabOrder) {

	}

	@Override
	public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object cellConstraints) {

		//TODO MG consider index
		final WIDGET_TYPE result = factory.create(getUiReference(), descriptor);
		addToContainer(result, cellConstraints);
		return result;
	}

	@Override
	public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final ICustomWidgetCreator<WIDGET_TYPE> creator,
		final Object cellConstraints) {

		final ICustomWidgetFactory customWidgetFactory = createCustomWidgetFactory();

		//TODO MG consider index
		final WIDGET_TYPE result = creator.create(customWidgetFactory);
		addToContainer(result, cellConstraints);
		return result;
	}

	private ICustomWidgetFactory createCustomWidgetFactory() {
		return new ICustomWidgetFactory() {
			@Override
			public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE create(
				final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
				return factory.create(getUiReference(), descriptor);
			}
		};
	}

	@Override
	public boolean remove(final IControlCommon control) {
		return getUiReference().remove((UIDComponent) control.getUiReference());
	}

	@Override
	public void layoutBegin() {
		//do nothing here
	}

	@Override
	public void layoutEnd() {
		redraw();
	}

	@Override
	public void removeAll() {
		container.removeAll();
	}

	protected IGenericWidgetFactory getGenericWidgetFactory() {
		return factory;
	}

	private void addToContainer(final IWidgetCommon widget, final Object cellConstraints) {
		if (cellConstraints != null) {
			container.add((UIDComponent) (widget.getUiReference()), cellConstraints);
		}
		else {
			container.add((UIDComponent) (widget.getUiReference()));
		}
	}

}
