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
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jo.widgets.impl.swing.widgets;

import java.awt.Component;
import java.awt.Container;

import net.miginfocom.swing.MigLayout;

import org.jo.widgets.api.color.IColorConstant;
import org.jo.widgets.api.widgets.IChildWidget;
import org.jo.widgets.api.widgets.IContainerWidget;
import org.jo.widgets.api.widgets.IWidget;
import org.jo.widgets.api.widgets.descriptor.base.IBaseWidgetDescriptor;
import org.jo.widgets.api.widgets.factory.ICustomWidgetFactory;
import org.jo.widgets.api.widgets.factory.IGenericWidgetFactory;
import org.jo.widgets.api.widgets.factory.IWidgetFactory;
import org.jo.widgets.api.widgets.layout.ILayoutDescriptor;
import org.jo.widgets.api.widgets.layout.MigLayoutDescriptor;
import org.jo.widgets.util.Assert;

public class SwingContainerWidget implements IContainerWidget {

	private final IGenericWidgetFactory factory;
	private final Container container;
	private final SwingWidget swingWidgetDelegate;

	public SwingContainerWidget(final IGenericWidgetFactory factory, final Container container) {

		Assert.paramNotNull(factory, "factory");
		Assert.paramNotNull(container, "container");

		this.factory = factory;
		this.container = container;
		this.swingWidgetDelegate = new SwingWidget(container);
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
		} else {
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
		swingWidgetDelegate.redraw();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		swingWidgetDelegate.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		swingWidgetDelegate.setBackgroundColor(colorValue);
	}

	@Override
	public final <WIDGET_TYPE extends IChildWidget> WIDGET_TYPE add(
		final IBaseWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object cellConstraints) {

		final WIDGET_TYPE result = factory.create(this, descriptor);
		addToContainer(result, cellConstraints);
		return result;
	}

	@Override
	public final <WIDGET_TYPE extends IChildWidget> WIDGET_TYPE add(
		final ICustomWidgetFactory<WIDGET_TYPE> customFactory,
		final Object cellConstraints) {

		final IWidgetFactory<WIDGET_TYPE, IBaseWidgetDescriptor<? extends WIDGET_TYPE>> widgetFactory = new IWidgetFactory<WIDGET_TYPE, IBaseWidgetDescriptor<? extends WIDGET_TYPE>>() {
			@Override
			public WIDGET_TYPE create(final IWidget parent, final IBaseWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
				return factory.create(parent, descriptor);
			}
		};

		final WIDGET_TYPE result = customFactory.create(this, widgetFactory);
		addToContainer(result, cellConstraints);
		return result;
	}

	@Override
	public void setRedraw(final boolean redraw) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeAll() {
		container.removeAll();
	}

	protected IGenericWidgetFactory getGenericWidgetFactory() {
		return factory;
	}

	private void addToContainer(final IChildWidget widget, final Object cellConstraints) {
		if (cellConstraints != null) {
			container.add((Component) (widget.getUiReference()), cellConstraints);
		} else {
			container.add((Component) (widget.getUiReference()));
		}
	}

}
