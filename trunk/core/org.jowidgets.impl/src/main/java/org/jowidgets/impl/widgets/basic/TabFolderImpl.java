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

package org.jowidgets.impl.widgets.basic;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jowidgets.api.controler.ITabFolderListener;
import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.api.widgets.ITabFolder;
import org.jowidgets.api.widgets.ITabItem;
import org.jowidgets.api.widgets.descriptor.ITabFolderDescriptor;
import org.jowidgets.api.widgets.descriptor.ITabItemDescriptor;
import org.jowidgets.impl.base.delegate.ControlDelegate;
import org.jowidgets.impl.spi.SpiBluePrintFactory;
import org.jowidgets.impl.spi.blueprint.ITabItemBluePrintSpi;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.impl.widgets.basic.factory.internal.util.VisibiliySettingsInvoker;
import org.jowidgets.impl.widgets.common.wrapper.TabFolderSpiWrapper;
import org.jowidgets.spi.widgets.ITabFolderSpi;
import org.jowidgets.spi.widgets.ITabItemSpi;
import org.jowidgets.util.Assert;
import org.jowidgets.util.TypeCast;

public class TabFolderImpl extends TabFolderSpiWrapper implements ITabFolder {

	private static final SpiBluePrintFactory SPI_BPF = new SpiBluePrintFactory();
	private final ControlDelegate controlDelegate;
	private final Set<ITabFolderListener> tabFolderListeners;
	private final List<TabItemImpl> items;

	public TabFolderImpl(final ITabFolderSpi widget, final ITabFolderDescriptor descriptor) {
		super(widget);
		this.controlDelegate = new ControlDelegate();
		this.tabFolderListeners = new HashSet<ITabFolderListener>();
		this.items = new LinkedList<TabItemImpl>();

		VisibiliySettingsInvoker.setVisibility(descriptor, this);
		ColorSettingsInvoker.setColors(descriptor, this);
	}

	@Override
	public IContainer getParent() {
		return controlDelegate.getParent();
	}

	@Override
	public void setParent(final IComponent parent) {
		controlDelegate.setParent(parent);
	}

	@Override
	public boolean isReparentable() {
		return controlDelegate.isReparentable();
	}

	@Override
	public IPopupMenu createPopupMenu() {
		return new PopupMenuImpl(getWidget().createPopupMenu(), this);
	}

	@Override
	public ITabItem addItem(final ITabItemDescriptor descriptor) {
		final ITabItemBluePrintSpi tabItemBluePrintSpi = SPI_BPF.tabItem();
		tabItemBluePrintSpi.setSetup(descriptor);
		final ITabItemSpi tabItemSpi = getWidget().addItem(tabItemBluePrintSpi);
		final TabItemImpl result = new TabItemImpl(tabItemSpi, descriptor, this);
		items.add(result);
		if (items.size() == 1) {
			setSelectedItem(0);
			result.setSelected(true);
		}
		return result;
	}

	@Override
	public ITabItem addItem(final int index, final ITabItemDescriptor descriptor) {
		final ITabItemBluePrintSpi tabItemBluePrintSpi = SPI_BPF.tabItem();
		tabItemBluePrintSpi.setSetup(descriptor);
		final ITabItemSpi tabItemSpi = getWidget().addItem(index, tabItemBluePrintSpi);
		final TabItemImpl result = new TabItemImpl(tabItemSpi, descriptor, this);
		items.add(index, result);
		if (items.size() == 1) {
			setSelectedItem(0);
			result.setSelected(true);
		}
		return result;
	}

	@Override
	public void removeItem(final ITabItem item) {
		Assert.paramNotNull(item, "item");
		final int index = items.indexOf(item);
		if (index != -1) {
			getWidget().removeItem(index);
			final TabItemImpl itemImpl = items.remove(index);
			if (itemImpl.isSelected()) {
				itemImpl.setSelected(false);
			}
		}
	}

	@Override
	public void removeAllItems() {
		for (final ITabItem item : new LinkedList<ITabItem>(items)) {
			removeItem(item);
		}
	}

	@Override
	public void setSelectedItem(final ITabItem item) {
		final int itemIndex = getItemIndex(item);
		getWidget().setSelectedItem(itemIndex);
	}

	@Override
	public ITabItem getSelectedItem() {
		final int selectedIndex = getWidget().getSelectedIndex();
		if (selectedIndex != -1) {
			items.get(selectedIndex);
		}
		return null;
	}

	@Override
	public void detachItem(final ITabItem item) {
		Assert.paramNotNull(item, "item");
		final int itemIndex = items.indexOf(item);
		if (itemIndex == -1) {
			throw new IllegalArgumentException("Item '" + item + "' is not attached to this folder.");
		}
		if (item.isDetached()) {
			throw new IllegalArgumentException("The item is already detached.");
		}
		final TabItemImpl itemImpl = TypeCast.toType(item, TabItemImpl.class);
		getWidget().detachItem(itemImpl.getWidget());
		items.remove(itemIndex);
		itemImpl.setDetached(true);
	}

	@Override
	public void attachItem(final ITabItem item) {
		attachItem(null, item);
	}

	@Override
	public void attachItem(final int index, final ITabItem item) {
		attachItem(Integer.valueOf(index), item);
	}

	private void attachItem(final Integer index, final ITabItem item) {
		Assert.paramNotNull(item, "item");
		if (!item.isDetached()) {
			throw new IllegalArgumentException("The item is not detached.");
		}
		final TabItemImpl itemImpl = TypeCast.toType(item, TabItemImpl.class);
		if (index != null) {
			getWidget().attachItem(index.intValue(), itemImpl.getWidget());
			items.add(index.intValue(), itemImpl);
		}
		else {
			getWidget().attachItem(itemImpl.getWidget());
			items.add(itemImpl);
		}

		itemImpl.setDetached(false);
		itemImpl.setParent(this);
	}

	@Override
	public void changeItemIndex(final ITabItem item, final int newIndex) {
		final int itemIndex = getItemIndex(item);
		if (item.isDetached()) {
			throw new IllegalArgumentException("The item is detached.");
		}
		if (itemIndex != newIndex) {
			final TabItemImpl itemImpl = TypeCast.toType(item, TabItemImpl.class);
			final boolean selected = itemImpl.isSelected();
			detachItem(item);
			attachItem(Integer.valueOf(newIndex), item);
			if (selected) {
				setSelectedItem(newIndex);
				itemImpl.setSelected(true);
			}
		}
	}

	private int getItemIndex(final ITabItem item) {
		Assert.paramNotNull(item, "item");
		final int itemIndex = items.indexOf(item);
		if (itemIndex == -1) {
			throw new IllegalArgumentException("Item '" + item + "' is not attached to this folder.");
		}
		return itemIndex;
	}

	@Override
	public ITabItem getItem(final int index) {
		return items.get(index);
	}

	@Override
	public int getIndex(final ITabItem item) {
		Assert.paramNotNull(item, "item");
		return items.indexOf(item);
	}

	@Override
	public List<ITabItem> getItems() {
		return new LinkedList<ITabItem>(items);
	}

	@Override
	public void addTabFolderListener(final ITabFolderListener listener) {
		tabFolderListeners.add(listener);
	}

	@Override
	public void removeTabFolderListener(final ITabFolderListener listener) {
		tabFolderListeners.remove(listener);
	}

	protected void fireItemSelected(final ITabItem item) {
		for (final ITabFolderListener listener : tabFolderListeners) {
			listener.itemSelected(item);
		}
	}

	protected List<TabItemImpl> getItemsImpl() {
		return new LinkedList<TabItemImpl>(items);
	}

}
