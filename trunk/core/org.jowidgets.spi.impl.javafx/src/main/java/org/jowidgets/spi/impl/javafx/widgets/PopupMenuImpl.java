/*
 * Copyright (c) 2012, David Bauknecht
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

package org.jowidgets.spi.impl.javafx.widgets;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;

import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IMenuListener;
import org.jowidgets.spi.impl.controller.MenuObservable;
import org.jowidgets.spi.widgets.IActionMenuItemSpi;
import org.jowidgets.spi.widgets.IMenuItemSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.ISelectableMenuItemSpi;
import org.jowidgets.spi.widgets.ISubMenuSpi;

public class PopupMenuImpl implements IPopupMenuSpi {
	private final ContextMenu contextMenu;
	private final Object parent;
	private final MenuObservable menuObservable;

	public PopupMenuImpl(final Object parent) {
		contextMenu = new ContextMenu();
		this.parent = parent;
		menuObservable = new MenuObservable();
	}

	@Override
	public ContextMenu getUiReference() {
		return contextMenu;
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
		// TODO DB Auto-generated method stub
	}

	@Override
	public boolean isEnabled() {
		// TODO DB Auto-generated method stub
		return false;
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
	public void show(final Position position) {
		if (parent instanceof ToolBar) {
			getUiReference().show((Control) parent, position.getX(), position.getY());
		}
		else if (parent instanceof Control) {
			((Control) parent).setContextMenu(getUiReference());
		}
		else if (parent instanceof Stage) {
			getUiReference().show((Stage) parent, position.getX(), position.getY());
		}
	}
}
