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

package org.jowidgets.spi.impl.swing.widgets;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;

import org.jowidgets.common.types.TabPlacement;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.impl.swing.options.SwingOptions;
import org.jowidgets.spi.impl.swing.widgets.base.JoWidgetsTabLookAndFeel;
import org.jowidgets.spi.widgets.ITabFolderSpi;
import org.jowidgets.spi.widgets.ITabItemSpi;
import org.jowidgets.spi.widgets.setup.ITabFolderSetupSpi;
import org.jowidgets.spi.widgets.setup.ITabItemSetupSpi;
import org.jowidgets.util.Assert;
import org.jowidgets.util.TypeCast;

public class TabFolderImpl extends SwingControl implements ITabFolderSpi {

	private final IGenericWidgetFactory widgetFactory;
	private final boolean tabsCloseable;
	private final List<TabItemImpl> items;

	public TabFolderImpl(final IGenericWidgetFactory widgetFactory, final ITabFolderSetupSpi setup) {
		super(new JTabbedPane());
		if (SwingOptions.isJoWidgetsTabLayout()) {
			getUiReference().setUI(new JoWidgetsTabLookAndFeel());
		}

		this.tabsCloseable = setup.isTabsCloseable();
		this.widgetFactory = widgetFactory;
		this.items = new LinkedList<TabItemImpl>();

		getUiReference().setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		getUiReference().setBorder(BorderFactory.createMatteBorder(3, 1, 1, 1, (Color) null));

		if (setup.getTabPlacement() == TabPlacement.TOP) {
			getUiReference().setTabPlacement(JTabbedPane.TOP);
		}
		else if (setup.getTabPlacement() == TabPlacement.BOTTOM) {
			getUiReference().setTabPlacement(JTabbedPane.BOTTOM);
		}
		else {
			throw new IllegalArgumentException("TabPlacement '" + setup.getTabPlacement() + "' is not known");
		}
	}

	@Override
	public JTabbedPane getUiReference() {
		return (JTabbedPane) super.getUiReference();
	}

	@Override
	public void removeItem(final int index) {
		getUiReference().remove(index);
		items.remove(index);
	}

	@Override
	public ITabItemSpi addItem(final ITabItemSetupSpi setup) {
		final TabItemImpl result = new TabItemImpl(widgetFactory, getUiReference(), tabsCloseable);
		items.add(result);
		return result;
	}

	@Override
	public ITabItemSpi addItem(final int index, final ITabItemSetupSpi setup) {
		final TabItemImpl result = new TabItemImpl(widgetFactory, getUiReference(), tabsCloseable, Integer.valueOf(index));
		items.add(index, result);
		return result;
	}

	@Override
	public void setSelectedItem(final int index) {
		getUiReference().setSelectedIndex(index);
	}

	@Override
	public int getSelectedIndex() {
		return getUiReference().getSelectedIndex();
	}

	@Override
	public void detachItem(final ITabItemSpi item) {
		Assert.paramNotNull(item, "item");
		final TabItemImpl itemImpl = TypeCast.toType(item, TabItemImpl.class);
		final int itemIndex = items.indexOf(itemImpl);
		if (itemIndex == -1) {
			throw new IllegalArgumentException("Item is not attached to this folder");
		}
		if (itemImpl.isDetached()) {
			throw new IllegalArgumentException("Item is already detached");
		}

		items.remove(itemIndex);
		itemImpl.detach();
	}

	@Override
	public void attachItem(final ITabItemSpi item) {
		Assert.paramNotNull(item, "item");
		final TabItemImpl itemImpl = TypeCast.toType(item, TabItemImpl.class);
		if (!itemImpl.isDetached()) {
			throw new IllegalArgumentException("Item is not detached");
		}
		itemImpl.attach(getUiReference(), tabsCloseable, null);
		items.add(itemImpl);
	}

	@Override
	public void attachItem(final int index, final ITabItemSpi item) {
		Assert.paramNotNull(item, "item");
		final TabItemImpl itemImpl = TypeCast.toType(item, TabItemImpl.class);
		if (!itemImpl.isDetached()) {
			throw new IllegalArgumentException("Item is not detached");
		}
		itemImpl.attach(getUiReference(), tabsCloseable, Integer.valueOf(index));
		items.add(index, itemImpl);
	}

}
