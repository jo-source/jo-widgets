/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.impl.mock.widgets;

import org.jowidgets.impl.mock.mockui.UIMCheckedMenuItem;
import org.jowidgets.impl.mock.mockui.UIMMenuItem;
import org.jowidgets.impl.mock.mockui.UIMRadioMenuItem;
import org.jowidgets.impl.mock.widgets.internal.ActionMenuItemImpl;
import org.jowidgets.impl.mock.widgets.internal.MenuItemImpl;
import org.jowidgets.impl.mock.widgets.internal.SelectableMenuItemImpl;
import org.jowidgets.impl.mock.widgets.internal.SubMenuImpl;
import org.jowidgets.spi.widgets.IActionMenuItemSpi;
import org.jowidgets.spi.widgets.IMenuItemSpi;
import org.jowidgets.spi.widgets.IMenuSpi;
import org.jowidgets.spi.widgets.ISelectableMenuItemSpi;
import org.jowidgets.spi.widgets.ISubMenuSpi;

public class MockMenu extends MockWidget implements IMenuSpi {

	public MockMenu(final UIMMenuItem container) {
		super(container);
	}

	@Override
	public UIMMenuItem getUiReference() {
		return (UIMMenuItem) super.getUiReference();
	}

	@Override
	public void remove(final int index) {
		getUiReference().remove(index);
	}

	@Override
	public IActionMenuItemSpi addActionItem(final Integer index) {
		final ActionMenuItemImpl result = new ActionMenuItemImpl();
		addItem(index, result);
		return result;
	}

	@Override
	public ISelectableMenuItemSpi addCheckedItem(final Integer index) {
		final SelectableMenuItemImpl result = new SelectableMenuItemImpl(new UIMCheckedMenuItem());
		addItem(index, result);
		return result;
	}

	@Override
	public ISelectableMenuItemSpi addRadioItem(final Integer index) {
		final SelectableMenuItemImpl result = new SelectableMenuItemImpl(new UIMRadioMenuItem());
		addItem(index, result);
		return result;
	}

	@Override
	public IMenuItemSpi addSeparator(final Integer index) {
		final MenuItemImpl result = new MenuItemImpl(new UIMMenuItem());
		addItem(index, result);
		return result;
	}

	@Override
	public ISubMenuSpi addSubMenu(final Integer index) {
		final SubMenuImpl result = new SubMenuImpl();
		addItem(index, result);
		return result;
	}

	private void addItem(final Integer index, final MockWidget item) {
		if (index != null) {
			getUiReference().add(item.getUiReference(), index.intValue());
		}
		else {
			getUiReference().add(item.getUiReference());
		}
	}

}
