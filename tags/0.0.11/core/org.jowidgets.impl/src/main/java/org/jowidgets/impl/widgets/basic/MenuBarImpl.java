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

package org.jowidgets.impl.widgets.basic;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IMainMenu;
import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.IMenuBar;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.descriptor.IMainMenuDescriptor;
import org.jowidgets.impl.widgets.common.wrapper.WidgetSpiWrapper;
import org.jowidgets.spi.widgets.IMenuBarSpi;
import org.jowidgets.util.Assert;

public class MenuBarImpl extends WidgetSpiWrapper implements IMenuBar {

	private final List<IMenu> menus;
	private final IWidget parent;

	public MenuBarImpl(final IMenuBarSpi widget, final IWidget parent) {
		super(widget);
		this.parent = parent;
		this.menus = new LinkedList<IMenu>();
	}

	@Override
	public IMenuBarSpi getWidget() {
		return (IMenuBarSpi) super.getWidget();
	}

	@Override
	public IWidget getParent() {
		return parent;
	}

	@Override
	public List<IMenu> getMenus() {
		return new LinkedList<IMenu>(menus);
	}

	@Override
	public boolean remove(final IMenu menu) {
		Assert.paramNotNull(menu, "menu");
		if (menus.contains(menu)) {
			menus.remove(menu);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public IMainMenu addMenu(final IMainMenuDescriptor descriptor) {
		final IMainMenu result = new MainMenuImpl(getWidget().addMenu(null), this, descriptor);
		menus.add(result);
		return result;
	}

	@Override
	public IMainMenu addMenu(final int index, final IMainMenuDescriptor descriptor) {
		final IMainMenu result = new MainMenuImpl(getWidget().addMenu(Integer.valueOf(index)), this, descriptor);
		menus.add(index, result);
		return result;
	}

	@Override
	public IMainMenu addMenu(final String name) {
		return addMenu(Toolkit.getBluePrintFactory().mainMenu(name));
	}

	@Override
	public IMainMenu addMenu(final String name, final char mnemonic) {
		return addMenu(Toolkit.getBluePrintFactory().mainMenu(name).setMnemonic(Character.valueOf(mnemonic)));
	}

	@Override
	public IMainMenu addMenu(final int index, final String name) {
		return addMenu(index, Toolkit.getBluePrintFactory().mainMenu(name));
	}

}
