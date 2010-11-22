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
package org.jowidgets.impl.swing.widgets.internal;

import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.impl.swing.util.ScrollBarSettingsConvert;
import org.jowidgets.impl.swing.widgets.SwingContainerWidget;
import org.jowidgets.spi.widgets.IScrollCompositeSpi;
import org.jowidgets.spi.widgets.setup.IScrollCompositeSetupSpi;

public class ScrollCompositeWidget implements IScrollCompositeSpi {

	private final SwingContainerWidget outerCompositeWidget;
	private final CompositeWidget innerCompositeWidget;

	public ScrollCompositeWidget(final IGenericWidgetFactory factory, final IScrollCompositeSetupSpi setup) {

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderFactory.createEmptyBorder());

		final int horizontalPolicy = ScrollBarSettingsConvert.convertHorizontal(setup);
		final int verticalPolicy = ScrollBarSettingsConvert.convertVertical(setup);

		scrollPane.setVerticalScrollBarPolicy(verticalPolicy);
		scrollPane.setHorizontalScrollBarPolicy(horizontalPolicy);

		this.outerCompositeWidget = new SwingContainerWidget(factory, scrollPane);
		this.innerCompositeWidget = new CompositeWidget(factory, setup);
		scrollPane.setViewportView(innerCompositeWidget.getUiReference());

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
	public void setVisible(final boolean visible) {
		outerCompositeWidget.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return outerCompositeWidget.isVisible();
	}

	@Override
	public Dimension getSize() {
		return outerCompositeWidget.getSize();
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
	public IColorConstant getForegroundColor() {
		return innerCompositeWidget.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return innerCompositeWidget.getBackgroundColor();
	}

	@Override
	public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object cellConstraints) {
		return innerCompositeWidget.add(descriptor, cellConstraints);
	}

	@Override
	public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final ICustomWidgetFactory<WIDGET_TYPE> factory,
		final Object cellConstraints) {
		return innerCompositeWidget.add(factory, cellConstraints);
	}

	@Override
	public void layoutBegin() {
		outerCompositeWidget.layoutBegin();
	}

	@Override
	public void layoutEnd() {
		outerCompositeWidget.layoutEnd();
	}

	@Override
	public void removeAll() {
		innerCompositeWidget.removeAll();
	}

}
