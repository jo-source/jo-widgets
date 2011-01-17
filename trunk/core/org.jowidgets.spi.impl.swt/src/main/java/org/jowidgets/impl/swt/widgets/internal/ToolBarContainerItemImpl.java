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

package org.jowidgets.impl.swt.widgets.internal;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.impl.swt.widgets.SwtComposite;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.IToolBarContainerItemSpi;

public class ToolBarContainerItemImpl extends ToolBarItemImpl implements IToolBarContainerItemSpi {

	private final SwtComposite swtComposite;
	private final Composite composite;

	public ToolBarContainerItemImpl(final ToolItem item, final ToolBar toolBar, final IGenericWidgetFactory factory) {
		super(item);

		this.composite = new Composite(toolBar, SWT.NONE);
		composite.setLayout(new MigLayout("", "0[grow]0", "0[grow]0"));

		item.setControl(composite);

		this.swtComposite = new SwtComposite(factory, composite);
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		final WIDGET_TYPE result = swtComposite.add(descriptor, layoutConstraints);

		return result;
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final ICustomWidgetFactory<WIDGET_TYPE> factory,
		final Object layoutConstraints) {
		return swtComposite.add(factory, layoutConstraints);
	}

	@Override
	public boolean remove(final IControlCommon control) {
		return swtComposite.remove(control);
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return swtComposite.createPopupMenu();
	}

	@Override
	public void redraw() {
		swtComposite.redraw();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		swtComposite.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		swtComposite.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return swtComposite.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return swtComposite.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		swtComposite.setCursor(cursor);
	}

	@Override
	public void setVisible(final boolean visible) {
		swtComposite.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return swtComposite.isVisible();
	}

	@Override
	public Dimension getSize() {
		return swtComposite.getSize();
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		swtComposite.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		swtComposite.removePopupDetectionListener(listener);
	}

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {
		swtComposite.setLayout(layoutDescriptor);
	}

	@Override
	public void layoutBegin() {
		swtComposite.layoutBegin();
	}

	@Override
	public void layoutEnd() {
		swtComposite.layoutEnd();
	}

	@Override
	public void removeAll() {
		swtComposite.removeAll();
	}

	public void pack() {
		composite.pack(true);
		getUiReference().setWidth(composite.getSize().x);
	}

}
