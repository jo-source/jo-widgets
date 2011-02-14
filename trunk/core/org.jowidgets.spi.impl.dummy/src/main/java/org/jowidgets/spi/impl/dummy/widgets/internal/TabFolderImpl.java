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

package org.jowidgets.spi.impl.dummy.widgets.internal;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.impl.dummy.dummyui.UIDComponent;
import org.jowidgets.spi.impl.dummy.dummyui.UIDTabFolder;
import org.jowidgets.spi.impl.dummy.dummyui.UIDTabItem;
import org.jowidgets.spi.impl.dummy.widgets.DummyControl;
import org.jowidgets.spi.widgets.ITabFolderSpi;
import org.jowidgets.spi.widgets.ITabItemSpi;
import org.jowidgets.spi.widgets.setup.ITabFolderSetupSpi;
import org.jowidgets.spi.widgets.setup.ITabItemSetupSpi;
import org.jowidgets.util.Assert;
import org.jowidgets.util.TypeCast;

public class TabFolderImpl extends DummyControl implements ITabFolderSpi {

	private final IGenericWidgetFactory factory;
	private final List<TabItemImpl> items;

	public TabFolderImpl(final IGenericWidgetFactory factory, final ITabFolderSetupSpi setup) {
		super(new UIDTabFolder());
		this.factory = factory;
		getUiReference().setTabsCloseable(setup.isTabsCloseable());
		getUiReference().setTabPlacement(setup.getTabPlacement());
		items = new LinkedList<TabItemImpl>();
	}

	@Override
	public UIDTabFolder getUiReference() {
		return (UIDTabFolder) super.getUiReference();
	}

	@Override
	public void removeItem(final int index) {
		getUiReference().remove(index);
		items.remove(index);
	}

	@Override
	public void setSelectedItem(final int index) {
		UIDTabItem tmp;
		for (final UIDComponent comp : getUiReference().getComponents()) {
			tmp = (UIDTabItem) comp;
			tmp.setSelected(false);
		}
		tmp = (UIDTabItem) getUiReference().getComponents().get(index);
		tmp.setSelected(true);
	}

	@Override
	public int getSelectedIndex() {
		for (int i = 0; i < getUiReference().getComponents().size(); i++) {
			final UIDTabItem comp = (UIDTabItem) getUiReference().getComponents().get(i);
			if (comp.isSelected()) {
				return i;
			}
		}
		// no item selected
		return -1;
	}

	@Override
	public ITabItemSpi addItem(final ITabItemSetupSpi setup) {
		final TabItemImpl tabItemImpl = new TabItemImpl(factory, getUiReference(), getUiReference().isTabsCloseable());
		items.add(tabItemImpl);
		return tabItemImpl;
	}

	@Override
	public ITabItemSpi addItem(final int index, final ITabItemSetupSpi setup) {
		final TabItemImpl tabItemImpl = new TabItemImpl(factory, getUiReference(), getUiReference().isTabsCloseable());
		getUiReference().add(tabItemImpl.getUiReference(), index);
		items.add(index, tabItemImpl);
		return tabItemImpl;
	}

	@Override
	public void detachItem(final ITabItemSpi item) {
		Assert.paramNotNull(item, "item");
		final TabItemImpl result = TypeCast.toType(item, TabItemImpl.class);
		if (items.contains(result.getUiReference())) {
			throw new IllegalArgumentException("Item is not attached to this folder");
		}
		if (result.isDetached()) {
			throw new IllegalArgumentException("Item is not detached");
		}
		result.detach();
		items.remove(result);
	}

	@Override
	public void attachItem(final ITabItemSpi item) {
		Assert.paramNotNull(item, "item");
		final TabItemImpl result = TypeCast.toType(item, TabItemImpl.class);
		if (!result.isDetached()) {
			throw new IllegalArgumentException("Item is not detached");
		}
		result.attachItem(getUiReference().isTabsCloseable(), null);
		items.add(result);
	}

	@Override
	public void attachItem(final int index, final ITabItemSpi item) {
		Assert.paramNotNull(item, "item");
		final TabItemImpl result = TypeCast.toType(item, TabItemImpl.class);
		if (!result.isDetached()) {
			throw new IllegalArgumentException("Item is not detached");
		}
		result.attachItem(getUiReference().isTabsCloseable(), Integer.valueOf(index));
		items.add(index, result);
	}
}
