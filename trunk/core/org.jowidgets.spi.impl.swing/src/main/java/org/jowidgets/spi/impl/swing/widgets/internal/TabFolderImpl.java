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

package org.jowidgets.spi.impl.swing.widgets.internal;

import javax.swing.JTabbedPane;

import org.jowidgets.common.types.TabPlacement;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.impl.swing.widgets.SwingControl;
import org.jowidgets.spi.widgets.ITabFolderSpi;
import org.jowidgets.spi.widgets.ITabItemSpi;
import org.jowidgets.spi.widgets.setup.ITabFolderSetupSpi;
import org.jowidgets.spi.widgets.setup.ITabItemSetupSpi;

public class TabFolderImpl extends SwingControl implements ITabFolderSpi {

	private final IGenericWidgetFactory widgetFactory;
	private final boolean tabsCloseable;

	public TabFolderImpl(final IGenericWidgetFactory widgetFactory, final ITabFolderSetupSpi setup) {
		super(new JTabbedPane());

		this.tabsCloseable = setup.isTabsCloseable();
		this.widgetFactory = widgetFactory;

		getUiReference().setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

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
	}

	@Override
	public ITabItemSpi addItem(final ITabItemSetupSpi setup) {
		return new TabItemImpl(widgetFactory, getUiReference(), tabsCloseable);
	}

	@Override
	public ITabItemSpi addItem(final int index, final ITabItemSetupSpi setup) {
		return new TabItemImpl(widgetFactory, getUiReference(), tabsCloseable, Integer.valueOf(index));
	}

	@Override
	public void setSelectedItem(final int index) {
		getUiReference().setSelectedIndex(index);
	}

	@Override
	public int getSelectedIndex() {
		return getUiReference().getSelectedIndex();
	}

}
