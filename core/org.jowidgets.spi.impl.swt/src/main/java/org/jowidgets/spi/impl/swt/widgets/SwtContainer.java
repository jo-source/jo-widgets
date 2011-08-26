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
package org.jowidgets.spi.impl.swt.widgets;

import java.util.List;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;
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
import org.jowidgets.spi.impl.swt.layout.LayoutImpl;
import org.jowidgets.spi.impl.swt.util.RectangleConvert;
import org.jowidgets.spi.widgets.IContainerSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.util.Assert;

public class SwtContainer implements IContainerSpi {

	private final IGenericWidgetFactory factory;
	private final SwtComponent swtComponentDelegate;

	private Composite composite;

	public SwtContainer(final IGenericWidgetFactory factory, final Composite composite) {

		Assert.paramNotNull(factory, "factory");
		Assert.paramNotNull(composite, "composite");

		this.factory = factory;
		this.composite = composite;
		this.swtComponentDelegate = new SwtComponent(composite);

	}

	public void setComposite(final Composite composite) {
		this.composite = composite;
		swtComponentDelegate.setControl(composite);
	}

	@Override
	public final void setLayout(final ILayoutDescriptor layout) {
		Assert.paramNotNull(layout, "layoutDescriptor");

		if (layout instanceof MigLayoutDescriptor) {
			final MigLayoutDescriptor migLayoutManager = (MigLayoutDescriptor) layout;
			composite.setLayout(new MigLayout(
				migLayoutManager.getLayoutConstraints(),
				migLayoutManager.getColumnConstraints(),
				migLayoutManager.getRowConstraints()));
		}
		else if (layout instanceof ILayouter) {
			composite.setLayout(new LayoutImpl((ILayouter) layout));
		}
		else {
			throw new IllegalArgumentException("LayoutDescriptor of type '" + layout.getClass().getName() + "' is not supported");
		}
	}

	@Override
	public Composite getUiReference() {
		return composite;
	}

	@Override
	public void redraw() {
		swtComponentDelegate.redraw();
	}

	@Override
	public void setRedrawEnabled(final boolean enabled) {
		swtComponentDelegate.setRedrawEnabled(enabled);
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		swtComponentDelegate.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		swtComponentDelegate.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return swtComponentDelegate.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return swtComponentDelegate.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		swtComponentDelegate.setCursor(cursor);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		swtComponentDelegate.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return swtComponentDelegate.isEnabled();
	}

	@Override
	public void setVisible(final boolean visible) {
		swtComponentDelegate.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return swtComponentDelegate.isVisible();
	}

	@Override
	public Rectangle getClientArea() {
		return RectangleConvert.convert(getUiReference().getClientArea());
	}

	@Override
	public Dimension computeDecoratedSize(final Dimension clientAreaSize) {
		Assert.paramNotNull(clientAreaSize, "clientAreaSize");
		final org.eclipse.swt.graphics.Rectangle trim = getUiReference().computeTrim(
				0,
				0,
				clientAreaSize.getWidth(),
				clientAreaSize.getHeight());
		return new Dimension(trim.width, trim.height);
	}

	@Override
	public Dimension getSize() {
		return swtComponentDelegate.getSize();
	}

	@Override
	public void setSize(final Dimension size) {
		swtComponentDelegate.setSize(size);
	}

	@Override
	public Position getPosition() {
		return swtComponentDelegate.getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		swtComponentDelegate.setPosition(position);
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return swtComponentDelegate.createPopupMenu();
	}

	@Override
	public boolean requestFocus() {
		return swtComponentDelegate.requestFocus();
	}

	@Override
	public void addFocusListener(final IFocusListener listener) {
		swtComponentDelegate.addFocusListener(listener);
	}

	@Override
	public void removeFocusListener(final IFocusListener listener) {
		swtComponentDelegate.removeFocusListener(listener);
	}

	@Override
	public void addKeyListener(final IKeyListener listener) {
		swtComponentDelegate.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(final IKeyListener listener) {
		swtComponentDelegate.removeKeyListener(listener);
	}

	@Override
	public void addMouseListener(final IMouseListener mouseListener) {
		swtComponentDelegate.addMouseListener(mouseListener);
	}

	@Override
	public void removeMouseListener(final IMouseListener mouseListener) {
		swtComponentDelegate.removeMouseListener(mouseListener);
	}

	@Override
	public void addComponentListener(final IComponentListener componentListener) {
		swtComponentDelegate.addComponentListener(componentListener);
	}

	@Override
	public void removeComponentListener(final IComponentListener componentListener) {
		swtComponentDelegate.removeComponentListener(componentListener);
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		swtComponentDelegate.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		swtComponentDelegate.removePopupDetectionListener(listener);
	}

	@Override
	public void setTabOrder(final List<? extends IControlCommon> tabOrder) {
		if (tabOrder == null) {
			getUiReference().setTabList(null);
		}
		else {
			final Control[] tabList = new Control[tabOrder.size()];
			int index = 0;
			for (final IControlCommon control : tabOrder) {
				tabList[index] = (Control) control.getUiReference();
				index++;
			}
			getUiReference().setTabList(tabList);
		}
	}

	@Override
	public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object cellConstraints) {

		final WIDGET_TYPE result = factory.create(getUiReference(), descriptor);
		afterChildCreation(index, result, cellConstraints);
		return result;
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final Integer index,
		final ICustomWidgetCreator<WIDGET_TYPE> widgetCreator,
		final Object cellConstraints) {

		final ICustomWidgetFactory customWidgetFactory = createCustomWidgetFactory();

		final WIDGET_TYPE result = widgetCreator.create(customWidgetFactory);
		afterChildCreation(index, result, cellConstraints);
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
		composite.setRedraw(false);
	}

	@Override
	public void layoutEnd() {
		composite.layout(true, true);
		composite.setRedraw(true);
	}

	private void afterChildCreation(final Integer index, final IWidgetCommon child, final Object layoutConstraints) {
		setLayoutConstraints(child, layoutConstraints);
		correctIndex(index, child);
	}

	private void setLayoutConstraints(final IWidgetCommon widget, final Object layoutConstraints) {
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

	private void correctIndex(final Integer index, final IWidgetCommon widget) {
		if (index != null) {
			final int indexInt = index.intValue();
			if (indexInt >= 0 && indexInt < composite.getChildren().length - 1) {
				((Control) widget.getUiReference()).moveAbove(composite.getChildren()[indexInt]);
			}
			else if (indexInt < 0 || indexInt > composite.getChildren().length - 1) {
				throw new IndexOutOfBoundsException("Index '" + indexInt + "' is out of range");
			}
			//else {control is already at the last position}
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

}
