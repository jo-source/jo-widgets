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

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.spi.impl.swing.util.BorderConvert;
import org.jowidgets.spi.impl.swing.util.ScrollBarSettingsConvert;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.IScrollCompositeSpi;
import org.jowidgets.spi.widgets.setup.IScrollCompositeSetupSpi;

public class ScrollCompositeImpl implements IScrollCompositeSpi {

	private final SwingComposite outerContainer;
	private final SwingContainer innerContainer;

	public ScrollCompositeImpl(final IGenericWidgetFactory factory, final IScrollCompositeSetupSpi setup) {

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderConvert.convert(setup.getBorder()));
		scrollPane.getViewport().setBackground(null);

		final int horizontalPolicy = ScrollBarSettingsConvert.convertHorizontal(setup);
		final int verticalPolicy = ScrollBarSettingsConvert.convertVertical(setup);

		scrollPane.setVerticalScrollBarPolicy(verticalPolicy);
		scrollPane.setHorizontalScrollBarPolicy(horizontalPolicy);

		this.outerContainer = new SwingComposite(factory, scrollPane);
		outerContainer.setBackgroundColor(null);

		this.innerContainer = new SwingContainer(factory, new JPanel());
		innerContainer.setLayout(setup.getLayout());
		innerContainer.setBackgroundColor(null);

		scrollPane.setViewportView(innerContainer.getUiReference());

	}

	@Override
	public final void setLayout(final ILayoutDescriptor layout) {
		innerContainer.setLayout(layout);
	}

	@Override
	public JScrollPane getUiReference() {
		return (JScrollPane) outerContainer.getUiReference();
	}

	@Override
	public void setLayoutConstraints(final Object layoutConstraints) {
		outerContainer.setLayoutConstraints(layoutConstraints);
	}

	@Override
	public Object getLayoutConstraints() {
		return outerContainer.getLayoutConstraints();
	}

	@Override
	public void redraw() {
		outerContainer.redraw();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		outerContainer.setRedrawEnabled(enabled);
	}

	@Override
	public void setVisible(final boolean visible) {
		outerContainer.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return outerContainer.isVisible();
	}

	@Override
	public Dimension getSize() {
		return outerContainer.getSize();
	}

	@Override
	public void setSize(final Dimension size) {
		outerContainer.setSize(size);
	}

	@Override
	public Position getPosition() {
		return outerContainer.getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		outerContainer.setPosition(position);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		outerContainer.setEnabled(enabled);
		innerContainer.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return outerContainer.isEnabled();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		outerContainer.setForegroundColor(colorValue);
		innerContainer.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		outerContainer.setBackgroundColor(colorValue);
		innerContainer.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return innerContainer.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return innerContainer.getBackgroundColor();
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return innerContainer.createPopupMenu();
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		innerContainer.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		innerContainer.removePopupDetectionListener(listener);
	}

	@Override
	public void setCursor(final Cursor cursor) {
		outerContainer.setCursor(cursor);
	}

	@Override
	public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object cellConstraints) {
		return innerContainer.add(descriptor, cellConstraints);
	}

	@Override
	public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final ICustomWidgetFactory<WIDGET_TYPE> factory,
		final Object cellConstraints) {
		return innerContainer.add(factory, cellConstraints);
	}

	@Override
	public boolean remove(final IControlCommon control) {
		return innerContainer.remove(control);
	}

	@Override
	public void layoutBegin() {
		outerContainer.layoutBegin();
	}

	@Override
	public void layoutEnd() {
		outerContainer.layoutEnd();
	}

	@Override
	public void removeAll() {
		innerContainer.removeAll();
	}

}
