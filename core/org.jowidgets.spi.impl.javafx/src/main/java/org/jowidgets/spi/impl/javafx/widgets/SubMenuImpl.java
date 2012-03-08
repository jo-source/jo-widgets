/*
 * Copyright (c) 2012, David Bauknecht
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
package org.jowidgets.spi.impl.javafx.widgets;

import javafx.scene.control.Menu;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.controller.IMenuListener;
import org.jowidgets.spi.impl.controller.MenuObservable;
import org.jowidgets.spi.widgets.IActionMenuItemSpi;
import org.jowidgets.spi.widgets.IMenuItemSpi;
import org.jowidgets.spi.widgets.ISelectableMenuItemSpi;
import org.jowidgets.spi.widgets.ISubMenuSpi;

public class SubMenuImpl implements ISubMenuSpi {

	private final MenuItemImpl menuItemDelegate;
	private final Menu menu;
	private final MenuObservable menuObservable;

	public SubMenuImpl() {
		menu = new Menu("");
		this.menuItemDelegate = new MenuItemImpl(getUiReference());
		menuObservable = new MenuObservable();
	}

	@Override
	public Menu getUiReference() {
		return menu;
	}

	@Override
	public void setText(final String text) {
		menuItemDelegate.setText(text);
	}

	@Override
	public void setToolTipText(final String text) {
		menuItemDelegate.setToolTipText(text);
	}

	@Override
	public void setIcon(final IImageConstant icon) {
		menuItemDelegate.setIcon(icon);
	}

	@Override
	public void setMnemonic(final char mnemonic) {
		menuItemDelegate.setMnemonic(mnemonic);
	}

	@Override
	public void addMenuListener(final IMenuListener listener) {
		menuObservable.addMenuListener(listener);
	}

	@Override
	public void removeMenuListener(final IMenuListener listener) {
		menuObservable.removeMenuListener(listener);
	}

	@Override
	public void remove(final int index) {
		getUiReference().getItems().remove(index);
	}

	private void addItem(final Integer index, final MenuItemImpl item) {
		if (index != null) {
			getUiReference().getItems().add(index.intValue(), item.getUiReference());
		}
		else {
			getUiReference().getItems().add(item.getUiReference());
		}
	}

	@Override
	public IActionMenuItemSpi addActionItem(final Integer index) {
		final ActionMenuItemImpl result = new ActionMenuItemImpl();
		addItem(index, result);
		return result;
	}

	@Override
	public ISelectableMenuItemSpi addCheckedItem(final Integer index) {
		final CheckedMenuItimImpl result = new CheckedMenuItimImpl();
		addItem(index, result);
		return result;
	}

	@Override
	public ISelectableMenuItemSpi addRadioItem(final Integer index) {
		final RadioMenuItemImpl result = new RadioMenuItemImpl();
		addItem(index, result);
		return result;
	}

	@Override
	public ISubMenuSpi addSubMenu(final Integer index) {
		final SubMenuImpl result = new SubMenuImpl();
		getUiReference().getItems().add(index.intValue(), result.getUiReference());
		return result;
	}

	@Override
	public IMenuItemSpi addSeparator(final Integer index) {
		final SeparatorMenuItemImpl result = new SeparatorMenuItemImpl();
		addItem(index, result);
		return result;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
}
