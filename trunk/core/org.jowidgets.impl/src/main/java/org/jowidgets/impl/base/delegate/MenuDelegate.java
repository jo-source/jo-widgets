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

package org.jowidgets.impl.base.delegate;

import java.util.LinkedList;
import java.util.List;

import org.jowidgets.api.widgets.IActionMenuItem;
import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.IMenuItem;
import org.jowidgets.api.widgets.descriptor.IActionMenuItemDescriptor;
import org.jowidgets.api.widgets.descriptor.setup.IActionItemSetup;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.impl.widgets.basic.ActionMenuItemImpl;
import org.jowidgets.spi.widgets.IActionMenuItemSpi;
import org.jowidgets.spi.widgets.IMenuSpi;
import org.jowidgets.util.Assert;

public class MenuDelegate {

	private final IMenuSpi menuSpi;
	private final IMenu menu;
	private final List<IMenuItem> children;
	private final IComponent parent;

	public MenuDelegate(final IMenu menu, final IMenuSpi menuSpi, final IComponent parent) {
		Assert.paramNotNull(menu, "menu");
		Assert.paramNotNull(menuSpi, "menuSpi");
		Assert.paramNotNull(parent, "parent");
		this.children = new LinkedList<IMenuItem>();
		this.menu = menu;
		this.menuSpi = menuSpi;
		this.parent = parent;
	}

	public <WIDGET_TYPE extends IMenuItem> WIDGET_TYPE addMenuItem(final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
		return addMenuItem(null, descriptor);
	}

	public <WIDGET_TYPE extends IMenuItem> WIDGET_TYPE addMenuItem(
		final int index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {

		if (index < 0 || index > children.size()) {
			throw new IllegalArgumentException("Index must be between '0' and '" + children.size() + "'.");
		}

		return addMenuItem(Integer.valueOf(index), descriptor);
	}

	@SuppressWarnings("unchecked")
	private <WIDGET_TYPE extends IMenuItem> WIDGET_TYPE addMenuItem(
		final Integer index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {

		WIDGET_TYPE result = null;

		if (IActionMenuItemDescriptor.class.isAssignableFrom(descriptor.getDescriptorInterface())) {
			final IActionMenuItemSpi actionMenuItemSpi = menuSpi.addActionItem(index);
			final IActionMenuItem actionMenuItem = new ActionMenuItemImpl(menu, actionMenuItemSpi, (IActionItemSetup) descriptor);
			result = (WIDGET_TYPE) actionMenuItem;
		}

		if (index != null) {
			children.add(index.intValue(), result);
		}
		else {
			children.add(result);
		}

		return result;
	}

	public List<IMenuItem> getChildren() {
		return new LinkedList<IMenuItem>(children);
	}

	public boolean remove(final IMenuItem item) {
		Assert.paramNotNull(item, "item");

		final int index = children.indexOf(item);
		if (index != -1) {
			children.remove(index);
			menuSpi.remove(index);
			return true;
		}
		else {
			return false;
		}

	}

	public IComponent getParent() {
		return parent;
	}

}
