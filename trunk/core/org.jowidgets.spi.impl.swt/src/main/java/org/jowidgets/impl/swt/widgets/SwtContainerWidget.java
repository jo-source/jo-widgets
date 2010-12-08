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
package org.jowidgets.impl.swt.widgets;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.impl.swt.color.IColorCache;
import org.jowidgets.spi.widgets.ICompositeSpi;
import org.jowidgets.util.Assert;

public class SwtContainerWidget implements ICompositeSpi {

	private final IGenericWidgetFactory factory;
	private final Composite composite;
	private final SwtWidget swtWidgetDelegate;

	public SwtContainerWidget(final IGenericWidgetFactory factory, final IColorCache colorCache, final Composite composite) {

		Assert.paramNotNull(factory, "factory");
		Assert.paramNotNull(colorCache, "colorCache");
		Assert.paramNotNull(composite, "composite");

		this.factory = factory;
		this.composite = composite;
		this.swtWidgetDelegate = new SwtWidget(colorCache, composite);
	}

	@Override
	public final void setLayout(final ILayoutDescriptor layoutDescriptor) {
		Assert.paramNotNull(layoutDescriptor, "layoutDescriptor");
		if (layoutDescriptor instanceof MigLayoutDescriptor) {
			final MigLayoutDescriptor migLayoutManager = (MigLayoutDescriptor) layoutDescriptor;
			composite.setLayout(new MigLayout(
				migLayoutManager.getLayoutConstraints(),
				migLayoutManager.getColumnConstraints(),
				migLayoutManager.getRowConstraints()));
		}
		else {
			throw new IllegalArgumentException("LayoutDescriptor of type '"
				+ layoutDescriptor.getClass().getName()
				+ "' is not supported");
		}
	}

	@Override
	public Composite getUiReference() {
		return composite;
	}

	@Override
	public void redraw() {
		swtWidgetDelegate.redraw();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		swtWidgetDelegate.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		swtWidgetDelegate.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return swtWidgetDelegate.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return swtWidgetDelegate.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		swtWidgetDelegate.setCursor(cursor);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		swtWidgetDelegate.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return swtWidgetDelegate.isEnabled();
	}

	@Override
	public void setVisible(final boolean visible) {
		swtWidgetDelegate.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return swtWidgetDelegate.isVisible();
	}

	@Override
	public Dimension getSize() {
		return swtWidgetDelegate.getSize();
	}

	@Override
	public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object cellConstraints) {

		final WIDGET_TYPE result = factory.create(getUiReference(), descriptor);
		setLayoutConstraints(result, cellConstraints);
		return result;
	}

	@Override
	public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final ICustomWidgetFactory<WIDGET_TYPE> customFactory,
		final Object cellConstraints) {

		final IWidgetFactory<WIDGET_TYPE, IWidgetDescriptor<? extends WIDGET_TYPE>> widgetFactory = new IWidgetFactory<WIDGET_TYPE, IWidgetDescriptor<? extends WIDGET_TYPE>>() {
			@Override
			public WIDGET_TYPE create(final Object parentUiReference, final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
				return factory.create(parentUiReference, descriptor);
			}
		};

		final WIDGET_TYPE result = customFactory.create(getUiReference(), widgetFactory);
		setLayoutConstraints(result, cellConstraints);
		return result;
	}

	@Override
	public boolean remove(final IControlCommon control) {
		if (isChild(control)) {
			((Widget) control.getUiReference()).dispose();
			return true;
		}
		return false;
	}

	private boolean isChild(final IControlCommon control) {
		for (final Control child : getUiReference().getChildren()) {
			if (child == control.getUiReference()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void layoutBegin() {
		getParentShell(composite).setRedraw(false);
	}

	@Override
	public void layoutEnd() {
		getParentShell(composite).layout(true, true);
		getParentShell(composite).setRedraw(true);
	}

	protected void setLayoutConstraints(final IWidgetCommon widget, final Object layoutConstraints) {
		final Object object = widget.getUiReference();
		if (object instanceof Control) {
			final Control control = (Control) object;
			control.setLayoutData(layoutConstraints);
		}
		else {
			throw new IllegalArgumentException("'"
				+ Control.class
				+ "' excpected, but '"
				+ object.getClass().getName()
				+ "' found.");
		}
	}

	protected IGenericWidgetFactory getGenericWidgetFactory() {
		return factory;
	}

	@Override
	public void removeAll() {
		disposeChildren(composite);
		composite.layout(true, true);
	}

	private void disposeChildren(final Composite composite) {
		for (final Control childControl : composite.getChildren()) {
			if (childControl instanceof Composite) {
				disposeChildren((Composite) childControl);
			}
			childControl.dispose();
		}
	}

	private Shell getParentShell(final Control control) {
		if (control instanceof Shell) {
			return (Shell) control;
		}
		else if (control.getParent() != null) {
			return getParentShell(control.getParent());
		}
		else {
			return null;
		}
	}

}
