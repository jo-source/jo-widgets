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
import org.jowidgets.api.color.IColorConstant;
import org.jowidgets.api.widgets.IChildWidget;
import org.jowidgets.api.widgets.IContainerWidget;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.descriptor.base.IBaseWidgetDescriptor;
import org.jowidgets.api.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.api.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.api.widgets.factory.IWidgetFactory;
import org.jowidgets.api.widgets.layout.ILayoutDescriptor;
import org.jowidgets.api.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.impl.swt.internal.color.IColorCache;
import org.jowidgets.util.Assert;

public class SwtContainerWidget implements IContainerWidget {

	private final IGenericWidgetFactory factory;
	private final Composite composite;
	private final SwtWidget swtWidgetDelegate;

	public SwtContainerWidget(final IGenericWidgetFactory factory, final IColorCache colorCache, final Composite composite) {

		Assert.paramNotNull(factory, "factory");
		Assert.paramNotNull(colorCache, "colorCache");
		//Assert.paramNotNull(composite, "composite");

		this.factory = factory;
		this.composite = composite;
		this.swtWidgetDelegate = new SwtWidget(colorCache, composite);
	}

	@Override
	public final void setLayout(final ILayoutDescriptor layoutManager) {
		Assert.paramNotNull(layoutManager, "layoutManager");
		if (layoutManager instanceof MigLayoutDescriptor) {
			final MigLayoutDescriptor migLayoutManager = (MigLayoutDescriptor) layoutManager;
			composite.setLayout(new MigLayout(
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
	public final <WIDGET_TYPE extends IChildWidget> WIDGET_TYPE add(
		final IBaseWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object cellConstraints) {

		final WIDGET_TYPE result = factory.create(this, descriptor);
		setLayoutConstraints(result, cellConstraints);
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
		setLayoutConstraints(result, cellConstraints);
		return result;
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

	protected void setLayoutConstraints(final IWidget widget, final Object layoutConstraints) {
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
