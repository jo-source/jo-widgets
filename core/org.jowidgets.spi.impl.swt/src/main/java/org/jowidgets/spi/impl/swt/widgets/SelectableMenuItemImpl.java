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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.jowidgets.common.widgets.controller.IItemStateListener;
import org.jowidgets.spi.impl.controller.ItemStateObservable;
import org.jowidgets.spi.widgets.ISelectableMenuItemSpi;

public class SelectableMenuItemImpl extends MenuItemImpl implements ISelectableMenuItemSpi {

	private final ItemStateObservable itemStateObservable;

	public SelectableMenuItemImpl(final MenuItem menuItem) {
		super(menuItem);

		this.itemStateObservable = new ItemStateObservable();
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				itemStateObservable.fireItemStateChanged();
			}
		});
	}

	@Override
	public boolean isSelected() {
		return getUiReference().getSelection();
	}

	private static boolean isRadio(final MenuItem menuItem) {
		return (menuItem.getStyle() & SWT.RADIO) == SWT.RADIO;
	}

	private static void unselectItem(final MenuItem menuItem) {
		if (menuItem.getSelection()) {
			menuItem.setSelection(false);
			menuItem.notifyListeners(SWT.Selection, new Event());
		}
	}

	@Override
	public void setSelected(final boolean selected) {
		// TODO MG please check if code is ok
		if (selected && isRadio(getUiReference())) {
			final Menu menu = getUiReference().getParent();
			final MenuItem[] items = menu.getItems();
			final int index = menu.indexOf(getUiReference());

			int i = index - 1;
			while (i >= 0 && isRadio(items[i])) {
				unselectItem(items[i]);
				i--;
			}

			i = index + 1;
			while (i < menu.getItemCount() && isRadio(items[i])) {
				unselectItem(items[i]);
				i++;
			}
		}
		getUiReference().setSelection(selected);
		itemStateObservable.fireItemStateChanged();
	}

	@Override
	public void addItemListener(final IItemStateListener listener) {
		itemStateObservable.addItemListener(listener);
	}

	@Override
	public void removeItemListener(final IItemStateListener listener) {
		itemStateObservable.removeItemListener(listener);
	}

}
