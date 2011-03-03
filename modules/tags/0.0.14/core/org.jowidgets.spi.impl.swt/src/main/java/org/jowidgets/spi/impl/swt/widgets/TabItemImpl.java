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

package org.jowidgets.spi.impl.swt.widgets;

import java.util.HashSet;
import java.util.Set;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.spi.impl.controler.TabItemObservableSpi;
import org.jowidgets.spi.impl.swt.image.SwtImageRegistry;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.ITabItemSpi;

public class TabItemImpl extends TabItemObservableSpi implements ITabItemSpi {

	private CTabItem cTabItem;
	private boolean detached;
	private Control detachedControl;
	private String text;
	private String toolTipText;
	private IImageConstant icon;

	private final CTabFolder parentFolder;
	private final SwtContainer swtContainer;
	private final Set<IPopupDetectionListener> tabPopupDetectionListeners;
	private final Set<PopupMenuImpl> tabPopupMenus;

	public TabItemImpl(final IGenericWidgetFactory genericWidgetFactory, final CTabFolder parentFolder, final boolean closeable) {
		this(genericWidgetFactory, parentFolder, closeable, null);
	}

	public TabItemImpl(
		final IGenericWidgetFactory genericWidgetFactory,
		final CTabFolder parentFolder,
		final boolean closeable,
		final Integer index) {

		this.detached = false;
		this.parentFolder = parentFolder;

		this.tabPopupDetectionListeners = new HashSet<IPopupDetectionListener>();
		this.tabPopupMenus = new HashSet<PopupMenuImpl>();

		cTabItem = createItem(parentFolder, closeable, index);

		final Composite composite = new Composite(parentFolder, SWT.NONE);
		composite.setLayout(new MigLayout("", "[]", "[]"));

		swtContainer = new SwtComposite(genericWidgetFactory, composite);

		cTabItem.setControl(swtContainer.getUiReference());
	}

	@Override
	public CTabItem getUiReference() {
		return cTabItem;
	}

	public void detach() {
		detached = true;
		detachedControl = cTabItem.getControl();
		cTabItem.setControl(null);
		cTabItem.dispose();
		cTabItem = null;
	}

	public boolean isDetached() {
		return detached;
	}

	public void attach(final CTabFolder parentFolder, final boolean closeable, final Integer index) {
		cTabItem = createItem(parentFolder, closeable, index);
		setText(text);
		setToolTipText(toolTipText);
		setIcon(icon);

		final Composite composite = (Composite) detachedControl;
		if (composite.getParent() != parentFolder) {
			if (composite.isReparentable()) {
				composite.setParent(parentFolder);
				for (final PopupMenuImpl popupMenu : tabPopupMenus) {
					popupMenu.setParent(parentFolder);
				}
			}
			else {
				throw new IllegalArgumentException("Content is not reparentable");
			}
		}
		detached = false;
		cTabItem.setControl(composite);
		swtContainer.setComposite(composite);
	}

	@Override
	public boolean isReparentable() {
		final Control control = cTabItem.getControl();
		if (control != null) {
			return control.isReparentable();
		}
		else {
			//workaround to test if potential items could be reparented
			final Composite testComposite = new Composite(parentFolder, SWT.NONE);
			final boolean result = testComposite.isReparentable();
			testComposite.dispose();
			return result;
		}
	}

	@Override
	public void setText(final String text) {
		this.text = text;
		if (text != null) {
			getUiReference().setText(text);
		}
		else {
			getUiReference().setText(String.valueOf(""));
		}
	}

	@Override
	public void setToolTipText(final String toolTipText) {
		this.toolTipText = toolTipText;
		getUiReference().setToolTipText(toolTipText);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		this.icon = icon;
		final Image oldImage = getUiReference().getImage();
		final Image newImage = SwtImageRegistry.getInstance().getImage(icon);
		if (oldImage != newImage) {
			getUiReference().setImage(newImage);
		}
	}

	@Override
	public void setVisible(final boolean visible) {
		swtContainer.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return swtContainer.isVisible();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		swtContainer.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return swtContainer.isEnabled();
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor,
		final Object layoutConstraints) {
		return swtContainer.add(descriptor, layoutConstraints);
	}

	@Override
	public <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(
		final ICustomWidgetFactory<WIDGET_TYPE> factory,
		final Object layoutConstraints) {
		return swtContainer.add(factory, layoutConstraints);
	}

	@Override
	public boolean remove(final IControlCommon control) {
		return swtContainer.remove(control);
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return swtContainer.createPopupMenu();
	}

	@Override
	public IPopupMenuSpi createTabPopupMenu() {
		final PopupMenuImpl result = new PopupMenuImpl(parentFolder);
		tabPopupMenus.add(result);
		return result;
	}

	@Override
	public void redraw() {
		swtContainer.redraw();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		swtContainer.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		swtContainer.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return swtContainer.getBackgroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return swtContainer.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		swtContainer.setCursor(cursor);
	}

	@Override
	public Dimension getSize() {
		return swtContainer.getSize();
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		swtContainer.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		swtContainer.removePopupDetectionListener(listener);
	}

	@Override
	public void addTabPopupDetectionListener(final IPopupDetectionListener listener) {
		tabPopupDetectionListeners.add(listener);
	}

	@Override
	public void removeTabPopupDetectionListener(final IPopupDetectionListener listener) {
		tabPopupDetectionListeners.remove(listener);
	}

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {
		swtContainer.setLayout(layoutDescriptor);
	}

	@Override
	public void layoutBegin() {
		swtContainer.layoutBegin();
	}

	@Override
	public void layoutEnd() {
		swtContainer.layoutEnd();
	}

	@Override
	public void removeAll() {
		swtContainer.removeAll();
	}

	protected void firePopopDetected(final Position position) {
		for (final IPopupDetectionListener listener : tabPopupDetectionListeners) {
			listener.popupDetected(position);
		}
	}

	private static CTabItem createItem(final CTabFolder parentFolder, final boolean closeable, final Integer index) {
		CTabItem result;
		if (index != null) {
			result = new CTabItem(parentFolder, closeable ? SWT.CLOSE : SWT.NONE, index.intValue());
		}
		else {
			result = new CTabItem(parentFolder, closeable ? SWT.CLOSE : SWT.NONE);
		}
		return result;
	}

}
