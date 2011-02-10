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

package org.jowidgets.spi.impl.swt.widgets.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.TabPlacement;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.impl.swt.widgets.SwtControl;
import org.jowidgets.spi.widgets.ITabFolderSpi;
import org.jowidgets.spi.widgets.ITabItemSpi;
import org.jowidgets.spi.widgets.setup.ITabFolderSetupSpi;
import org.jowidgets.spi.widgets.setup.ITabItemSetupSpi;

public class TabFolderImpl extends SwtControl implements ITabFolderSpi {

	private final IGenericWidgetFactory widgetFactory;
	private final boolean tabsCloseable;
	private final Map<CTabItem, TabItemImpl> items;

	public TabFolderImpl(final IGenericWidgetFactory widgetFactory, final Object parentUiReference, final ITabFolderSetupSpi setup) {
		super(new CTabFolder((Composite) parentUiReference, getStyle(setup)));

		this.tabsCloseable = setup.isTabsCloseable();
		this.widgetFactory = widgetFactory;

		this.items = new HashMap<CTabItem, TabItemImpl>();

		getUiReference().setUnselectedImageVisible(true);
		getUiReference().setUnselectedCloseVisible(true);

		setMenuDetectListener(new MenuDetectListener() {

			@Override
			public void menuDetected(final MenuDetectEvent e) {
				final Point position = getUiReference().toControl(e.x, e.y);
				final CTabItem item = getUiReference().getItem(position);
				if (item == null) {
					getPopupDetectionObservable().firePopupDetected(new Position(position.x, position.y));
				}
				else {
					final TabItemImpl itemImpl = items.get(item);
					getUiReference().setSelection(item);
					itemImpl.fireSelected();
					itemImpl.firePopopDetected(new Position(position.x, position.y));
				}
			}
		});
	}

	@Override
	public CTabFolder getUiReference() {
		return (CTabFolder) super.getUiReference();
	}

	@Override
	public void removeItem(final int index) {
		final CTabItem item = getUiReference().getItem(index);
		items.remove(item);
		final Control control = item.getControl();
		if (control != null && !control.isDisposed()) {
			control.dispose();
		}
		item.dispose();
	}

	@Override
	public ITabItemSpi addItem(final ITabItemSetupSpi setup) {
		final TabItemImpl result = new TabItemImpl(widgetFactory, getUiReference(), tabsCloseable);
		items.put(result.getUiReference(), result);
		return result;
	}

	@Override
	public ITabItemSpi addItem(final int index, final ITabItemSetupSpi setup) {
		final TabItemImpl result = new TabItemImpl(widgetFactory, getUiReference(), tabsCloseable, Integer.valueOf(index));
		items.put(result.getUiReference(), result);
		return result;
	}

	@Override
	public void setSelectedItem(final int index) {
		getUiReference().setSelection(index);
	}

	@Override
	public int getSelectedIndex() {
		return getUiReference().getSelectionIndex();
	}

	private static int getStyle(final ITabFolderSetupSpi setup) {
		int result = SWT.BORDER | SWT.MULTI;
		if (TabPlacement.BOTTOM == setup.getTabPlacement()) {
			result = result | SWT.BOTTOM;
		}
		else if (TabPlacement.TOP == setup.getTabPlacement()) {
			result = result | SWT.TOP;
		}
		else {
			throw new IllegalArgumentException("TabPlacement '" + setup.getTabPlacement() + "' is not known");
		}
		return result;
	}
}
