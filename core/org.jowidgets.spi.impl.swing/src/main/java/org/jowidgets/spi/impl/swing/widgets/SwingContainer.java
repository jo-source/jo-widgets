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
package org.jowidgets.spi.impl.swing.widgets;

import java.awt.Component;
import java.awt.Container;

import net.miginfocom.swing.MigLayout;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.spi.impl.swing.widgets.util.ChildRemover;
import org.jowidgets.spi.widgets.IContainerSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.util.Assert;

public class SwingContainer implements IContainerSpi {

	private final IGenericWidgetFactory factory;
	private final SwingComponent swingComponentDelegate;

	private Container container;

	public SwingContainer(final IGenericWidgetFactory factory, final Container container) {
		Assert.paramNotNull(factory, "factory");
		Assert.paramNotNull(container, "container");

		this.factory = factory;
		this.container = container;
		this.swingComponentDelegate = new SwingComponent(container);
	}

	public void setContainer(final Container container) {
		this.container = container;
		swingComponentDelegate.setComponent(container);
	}

	@Override
	public final void setLayout(final ILayoutDescriptor layoutManager) {
		Assert.paramNotNull(layoutManager, "layoutManager");
		if (layoutManager instanceof MigLayoutDescriptor) {
			final MigLayoutDescriptor migLayoutManager = (MigLayoutDescriptor) layoutManager;
			container.setLayout(new MigLayout(
				migLayoutManager.getLayoutConstraints(),
				migLayoutManager.getColumnConstraints(),
				migLayoutManager.getRowConstraints()));
		}
		else {
			throw new IllegalArgumentException("Layout Manager of type '"
				+ layoutManager.getClass().getName()
				+ "' is not supported");
		}
	}

	@Override
	public Container getUiReference() {
		return container;
	}

	@Override
	public void redraw() {
		swingComponentDelegate.redraw();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		swingComponentDelegate.setRedrawEnabled(enabled);
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		swingComponentDelegate.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		swingComponentDelegate.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return swingComponentDelegate.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return swingComponentDelegate.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		swingComponentDelegate.setCursor(cursor);
	}

	@Override
	public void setVisible(final boolean visible) {
		swingComponentDelegate.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return swingComponentDelegate.isVisible();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		swingComponentDelegate.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return swingComponentDelegate.isEnabled();
	}

	@Override
	public Dimension getSize() {
		return swingComponentDelegate.getSize();
	}

	@Override
	public void setSize(final Dimension size) {
		swingComponentDelegate.setSize(size);
	}

	@Override
	public Position getPosition() {
		return swingComponentDelegate.getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		swingComponentDelegate.setPosition(position);
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return swingComponentDelegate.createPopupMenu();
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		swingComponentDelegate.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		swingComponentDelegate.removePopupDetectionListener(listener);
	}

	@Override
	public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object cellConstraints) {

		final WIDGET_TYPE result = factory.create(getUiReference(), descriptor);
		addToContainer(result, cellConstraints);
		return result;
	}

	@Override
	public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final ICustomWidgetFactory<WIDGET_TYPE> customFactory,
		final Object cellConstraints) {

		final IWidgetFactory<WIDGET_TYPE, IWidgetDescriptor<? extends WIDGET_TYPE>> widgetFactory = new IWidgetFactory<WIDGET_TYPE, IWidgetDescriptor<? extends WIDGET_TYPE>>() {
			@Override
			public WIDGET_TYPE create(final Object parent, final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
				return factory.create(parent, descriptor);
			}
		};

		final WIDGET_TYPE result = customFactory.create(getUiReference(), widgetFactory);
		addToContainer(result, cellConstraints);
		return result;
	}

	@Override
	public boolean remove(final IControlCommon control) {
		return ChildRemover.removeChild(container, (Component) control.getUiReference());
	}

	@Override
	public void removeAll() {
		container.removeAll();
	}

	@Override
	public void layoutBegin() {
		//do nothing here (swing does not flicker like swt)
	}

	@Override
	public void layoutEnd() {
		redraw();
	}

	protected IGenericWidgetFactory getGenericWidgetFactory() {
		return factory;
	}

	private void addToContainer(final IWidgetCommon widget, final Object cellConstraints) {
		if (cellConstraints != null) {
			container.add((Component) (widget.getUiReference()), cellConstraints);
		}
		else {
			container.add((Component) (widget.getUiReference()));
		}
	}

}
