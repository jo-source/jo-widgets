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

import java.util.List;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.model.item.IItemModel;
import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.api.widgets.IActionMenuItem;
import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.IMenuItem;
import org.jowidgets.api.widgets.ISubMenu;
import org.jowidgets.api.widgets.descriptor.setup.IMenuItemSetup;
import org.jowidgets.common.widgets.controler.IMenuListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.impl.base.delegate.ItemDelegate;
import org.jowidgets.impl.base.delegate.MenuDelegate;
import org.jowidgets.impl.model.item.MenuModelBuilder;
import org.jowidgets.impl.widgets.common.wrapper.MenuItemSpiWrapper;
import org.jowidgets.impl.widgets.common.wrapper.invoker.MenuItemSpiInvoker;
import org.jowidgets.spi.widgets.ISubMenuSpi;

public class SubMenuImpl extends MenuItemSpiWrapper implements ISubMenu {

	private final MenuDelegate menuDelegate;
	private final IMenu parent;

	public SubMenuImpl(final ISubMenuSpi subMenuSpi, final IMenu parent, final IMenuItemSetup setup) {
		super(subMenuSpi, new ItemDelegate(new MenuItemSpiInvoker(subMenuSpi), new MenuModelBuilder().build()));

		this.menuDelegate = new MenuDelegate(this, subMenuSpi, getModel());
		this.parent = parent;

		setText(setup.getText());
		setToolTipText(setup.getToolTipText());
		setIcon(setup.getIcon());

		if (setup.getMnemonic() != null) {
			setMnemonic(setup.getMnemonic().charValue());
		}

	}

	@Override
	public ISubMenuSpi getWidget() {
		return (ISubMenuSpi) super.getWidget();
	}

	@Override
	public IMenuModel getModel() {
		return (IMenuModel) getItemDelegate().getModel();
	}

	@Override
	public void addMenuListener(final IMenuListener listener) {
		getWidget().addMenuListener(listener);
	}

	@Override
	public void removeMenuListener(final IMenuListener listener) {
		getWidget().removeMenuListener(listener);
	}

	@Override
	public List<IMenuItem> getChildren() {
		return menuDelegate.getChildren();
	}

	@Override
	public boolean remove(final IMenuItem item) {
		return menuDelegate.remove(item);
	}

	@Override
	public void removeAll() {
		menuDelegate.removeAll();
	}

	@Override
	public IMenuItem addSeparator() {
		return menuDelegate.addSeparator();
	}

	@Override
	public IMenuItem addSeparator(final int index) {
		return menuDelegate.addSeparator(index);
	}

	@Override
	public IActionMenuItem addAction(final IAction action) {
		return menuDelegate.addAction(action);
	}

	@Override
	public IActionMenuItem addAction(final int index, final IAction action) {
		return menuDelegate.addAction(index, action);
	}

	@Override
	public <WIDGET_TYPE extends IMenuItem> WIDGET_TYPE addItem(final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
		return menuDelegate.addMenuItem(descriptor);
	}

	@Override
	public <WIDGET_TYPE extends IMenuItem> WIDGET_TYPE addItem(
		final int index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {

		return menuDelegate.addMenuItem(index, descriptor);
	}

	@Override
	public void setModel(final IMenuModel model) {
		getItemDelegate().setModel(model);
		menuDelegate.setModel(model);
	}

	@Override
	public void setModel(final IItemModel model) {
		if (model instanceof IMenuModel) {
			setModel((IMenuModel) model);
		}
		else {
			throw new IllegalArgumentException("Model must be instance of '" + IMenuModel.class.getName() + "'");
		}
	}

	@Override
	public IMenu getParent() {
		return parent;
	}

}
