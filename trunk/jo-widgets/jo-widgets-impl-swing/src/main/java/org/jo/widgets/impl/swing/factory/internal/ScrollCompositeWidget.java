/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of the jo-widgets.org nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
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
package org.jo.widgets.impl.swing.factory.internal;

import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import org.jo.widgets.api.color.IColorConstant;
import org.jo.widgets.api.util.ColorSettingsInvoker;
import org.jo.widgets.api.widgets.IChildWidget;
import org.jo.widgets.api.widgets.IScrollCompositeWidget;
import org.jo.widgets.api.widgets.IWidget;
import org.jo.widgets.api.widgets.descriptor.IScrollCompositeDescriptor;
import org.jo.widgets.api.widgets.descriptor.base.IBaseWidgetDescriptor;
import org.jo.widgets.api.widgets.factory.ICustomWidgetFactory;
import org.jo.widgets.api.widgets.factory.IGenericWidgetFactory;
import org.jo.widgets.api.widgets.layout.ILayoutDescriptor;
import org.jo.widgets.impl.swing.util.ScrollBarSettingsConvert;

public class ScrollCompositeWidget implements IScrollCompositeWidget {

	private final IWidget parent;
	private final SwingWidgetContainer outerCompositeWidget;
	private final CompositeWidget innerCompositeWidget;

	public ScrollCompositeWidget(final IGenericWidgetFactory factory,
			final IWidget parent, final IScrollCompositeDescriptor descriptor) {

		this.parent = parent;

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderFactory.createEmptyBorder());

		final int horizontalPolicy = ScrollBarSettingsConvert
				.convertHorizontal(descriptor);
		final int verticalPolicy = ScrollBarSettingsConvert
				.convertVertical(descriptor);

		scrollPane.setVerticalScrollBarPolicy(verticalPolicy);
		scrollPane.setHorizontalScrollBarPolicy(horizontalPolicy);

		this.outerCompositeWidget = new SwingWidgetContainer(factory,
				scrollPane);
		this.innerCompositeWidget = new CompositeWidget(factory,
				outerCompositeWidget, descriptor);
		scrollPane.setViewportView(innerCompositeWidget.getUiReference());

		ColorSettingsInvoker.setColors(descriptor, this);

	}

	@Override
	public IWidget getParent() {
		return parent;
	}

	@Override
	public final void setLayout(final ILayoutDescriptor layout) {
		innerCompositeWidget.setLayout(layout);
	}

	@Override
	public Container getUiReference() {
		return outerCompositeWidget.getUiReference();
	}

	@Override
	public void redraw() {
		outerCompositeWidget.redraw();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		innerCompositeWidget.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		innerCompositeWidget.setBackgroundColor(colorValue);
	}

	@Override
	public final <WIDGET_TYPE extends IChildWidget> WIDGET_TYPE add(
			final IBaseWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
			final Object cellConstraints) {
		return innerCompositeWidget.add(descriptor, cellConstraints);
	}

	@Override
	public final <WIDGET_TYPE extends IChildWidget> WIDGET_TYPE add(
			final ICustomWidgetFactory<WIDGET_TYPE> factory,
			final Object cellConstraints) {
		return innerCompositeWidget.add(factory, cellConstraints);
	}

	@Override
	public void setRedraw(final boolean redraw) {
		outerCompositeWidget.redraw();
	}

	@Override
	public void removeAll() {
		innerCompositeWidget.removeAll();
	}

}
