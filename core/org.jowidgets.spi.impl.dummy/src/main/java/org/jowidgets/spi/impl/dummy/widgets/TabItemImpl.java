/*
 * Copyright (c) 2011, Lukas Gross
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

package org.jowidgets.spi.impl.dummy.widgets;

import java.util.HashSet;
import java.util.Set;

import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.spi.impl.dummy.dummyui.UIDComponent;
import org.jowidgets.spi.impl.dummy.dummyui.UIDObservable;
import org.jowidgets.spi.impl.dummy.dummyui.UIDTabContainer;
import org.jowidgets.spi.impl.dummy.dummyui.UIDTabFolder;
import org.jowidgets.spi.impl.dummy.dummyui.UIDTabItem;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.ITabItemSpi;
import org.jowidgets.spi.widgets.controler.ITabItemListenerSpi;

public class TabItemImpl extends DummyContainer implements ITabItemSpi {

	private final UIDTabFolder parentfolder;
	private final UIDTabContainer tabContainer;
	private UIDTabItem tabItem;
	private final UIDObservable tabPopupObs;
	private final Set<PopupMenuImpl> tabPopupMenus;

	private String text;
	private String toolTipText;
	private IImageConstant icon;
	private boolean detached;

	public TabItemImpl(final IGenericWidgetFactory factory, final UIDTabFolder parentFolder, final boolean closeable) {
		this(factory, parentFolder, closeable, null);
	}

	public TabItemImpl(
		final IGenericWidgetFactory factory,
		final UIDTabFolder parentFolder,
		final boolean closeable,
		final Integer index) {

		super(factory, parentFolder);
		this.parentfolder = parentFolder;
		this.detached = false;

		this.tabPopupObs = new UIDObservable();
		this.tabContainer = new UIDTabContainer();
		this.tabPopupMenus = new HashSet<PopupMenuImpl>();

		tabItem = new UIDTabItem();

		parentFolder.add(tabContainer);
	}

	@Override
	public UIDTabItem getUiReference() {
		return (UIDTabItem) super.getUiReference();
	}

	@Override
	public void addTabPopupDetectionListener(final IPopupDetectionListener listener) {
		tabPopupObs.addPopupDetectionListener(listener);
	}

	@Override
	public void removeTabPopupDetectionListener(final IPopupDetectionListener listener) {
		tabPopupObs.removePopupDetectionListener(listener);
	}

	@Override
	public IPopupMenuSpi createTabPopupMenu() {
		final PopupMenuImpl menu = new PopupMenuImpl(parentfolder);
		tabPopupMenus.add(menu);
		return menu;
	}

	@Override
	public String getText() {
		return getUiReference().getText();
	}

	@Override
	public void setText(final String text) {
		this.text = text;
		getUiReference().setText(text);
	}

	@Override
	public boolean isReparentable() {
		return true;
	}

	@Override
	public String getToolTipText() {
		return getUiReference().getToolTipText();
	}

	@Override
	public void setToolTipText(final String text) {
		this.toolTipText = text;
		getUiReference().setToolTipText(text);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		this.icon = icon;
		getUiReference().setIcon(icon);

	}

	@Override
	public void setEnabled(final boolean enabled) {
		tabContainer.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return tabContainer.isEnabled();
	}

	@Override
	public boolean remove(final IControlCommon control) {
		return tabContainer.remove((UIDComponent) control);
	}

	@Override
	public IPopupMenuSpi createPopupMenu() {
		return new PopupMenuImpl(getUiReference());
	}

	@Override
	public void redraw() {
		tabContainer.redraw();
	}

	@Override
	public void setForegroundColor(final IColorConstant colorValue) {
		tabContainer.setForegroundColor(colorValue);
	}

	@Override
	public void setBackgroundColor(final IColorConstant colorValue) {
		tabContainer.setBackgroundColor(colorValue);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return tabContainer.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return tabContainer.getBackgroundColor();
	}

	@Override
	public void setCursor(final Cursor cursor) {
		tabContainer.setCursor(cursor);
	}

	@Override
	public void setVisible(final boolean visible) {
		tabContainer.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return tabContainer.isVisible();
	}

	@Override
	public Dimension getSize() {
		return tabContainer.getSize();
	}

	@Override
	public void addPopupDetectionListener(final IPopupDetectionListener listener) {
		tabContainer.addPopupDetectionListener(listener);
	}

	@Override
	public void removePopupDetectionListener(final IPopupDetectionListener listener) {
		tabContainer.removePopupDetectionListener(listener);
	}

	@Override
	public void setLayout(final ILayoutDescriptor layoutDescriptor) {
		tabContainer.setLayoutConstraints(layoutDescriptor);
	}

	@Override
	public void layoutBegin() {
		// do nothing
	}

	@Override
	public void layoutEnd() {
		redraw();
	}

	@Override
	public void removeAll() {
		tabContainer.removeAll();
	}

	@Override
	public void addTabItemListener(final ITabItemListenerSpi listener) {
		tabPopupObs.addTabItemListener(listener);
	}

	@Override
	public void removeTabItemListener(final ITabItemListenerSpi listener) {
		tabPopupObs.removeTabItemListener(listener);
	}

	public void attachItem(final boolean closeable, final Integer index) {
		tabItem = new UIDTabItem();
		tabItem.setText(this.text);
		tabItem.setToolTipText(this.toolTipText);
		tabItem.setIcon(this.icon);
		tabItem.setCloseable(closeable);

		detached = false;
		if (index != null) {
			tabContainer.add(tabItem, index.intValue());
		}
		else {
			tabContainer.add(tabItem);
		}
	}

	public void detach() {
		detached = true;
		tabItem = null;
	}

	public boolean isDetached() {
		return detached;
	}
}
