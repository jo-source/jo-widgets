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

package org.jowidgets.tools.powo;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.widgets.IMainMenu;
import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.IMenuBar;
import org.jowidgets.api.widgets.descriptor.IMainMenuDescriptor;
import org.jowidgets.util.Assert;

class MenuBar extends Widget<IMenuBar, DummyBluePrint<IMenuBar>> implements IMenuBar {

	private final List<JoMainMenu> menus;

	MenuBar() {
		super(new DummyBluePrint<IMenuBar>());
		this.menus = new LinkedList<JoMainMenu>();
	}

	@Override
	void initialize(final IMenuBar widget) {
		super.initialize(widget);
		for (final JoMainMenu menu : menus) {
			final IMainMenu newMenu = widget.addMenu(menu.getBluePrint());
			menu.initialize(newMenu);
		}
	}

	public final void addMenu(final JoMainMenu mainMenu) {
		Assert.paramNotNull(mainMenu, "mainMenu");
		if (isInitialized()) {
			final IMainMenu result = getWidget().addMenu(mainMenu.getBluePrint());
			mainMenu.initialize(result);
		}
		else {
			menus.add(mainMenu);
		}
	}

	public final void addMenu(final int index, final JoMainMenu mainMenu) {
		Assert.paramNotNull(mainMenu, "mainMenu");
		if (isInitialized()) {
			final IMainMenu result = getWidget().addMenu(index, mainMenu.getBluePrint());
			mainMenu.initialize(result);
		}
		else {
			menus.add(index, mainMenu);
		}
	}

	@Override
	public List<IMenu> getMenus() {
		if (isInitialized()) {
			return getWidget().getMenus();
		}
		else {
			return new LinkedList<IMenu>(menus);
		}
	}

	@Override
	public boolean remove(final IMenu menu) {
		if (isInitialized()) {
			return getWidget().remove(menu);
		}
		else {
			return menus.remove(menu);
		}
	}

	@Override
	public IMainMenu addMenu(final String name) {
		if (isInitialized()) {
			return getWidget().addMenu(name);
		}
		else {
			final JoMainMenu result = new JoMainMenu(name);
			menus.add(result);
			return result;
		}
	}

	@Override
	public IMainMenu addMenu(final String name, final char mnemonic) {
		if (isInitialized()) {
			return getWidget().addMenu(name, mnemonic);
		}
		else {
			final JoMainMenu result = new JoMainMenu(name, mnemonic);
			menus.add(result);
			return result;
		}
	}

	@Override
	public IMainMenu addMenu(final int index, final String name) {
		if (isInitialized()) {
			return getWidget().addMenu(index, name);
		}
		else {
			final JoMainMenu result = new JoMainMenu(name);
			menus.add(index, result);
			return result;
		}
	}

	@Override
	public IMainMenu addMenu(final IMainMenuDescriptor descriptor) {
		if (isInitialized()) {
			return getWidget().addMenu(descriptor);
		}
		else {
			final JoMainMenu result = new JoMainMenu(descriptor);
			menus.add(result);
			return result;
		}
	}

	@Override
	public IMainMenu addMenu(final int index, final IMainMenuDescriptor descriptor) {
		if (isInitialized()) {
			return getWidget().addMenu(index, descriptor);
		}
		else {
			final JoMainMenu result = new JoMainMenu(descriptor);
			menus.add(index, result);
			return result;
		}
	}

}
