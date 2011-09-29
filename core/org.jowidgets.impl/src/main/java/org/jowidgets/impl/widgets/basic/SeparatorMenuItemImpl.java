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

import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.model.item.IMenuItemModel;
import org.jowidgets.api.model.item.ISeparatorItemModel;
import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.IMenuItem;
import org.jowidgets.impl.base.delegate.ItemModelBindingDelegate;
import org.jowidgets.impl.base.delegate.MenuItemDisposableDelegate;
import org.jowidgets.impl.model.item.SeparatorItemModelBuilder;
import org.jowidgets.impl.widgets.common.wrapper.MenuItemSpiWrapper;
import org.jowidgets.impl.widgets.common.wrapper.invoker.MenuItemSpiInvoker;
import org.jowidgets.spi.widgets.IMenuItemSpi;

public class SeparatorMenuItemImpl extends MenuItemSpiWrapper implements IMenuItem {

	private final IMenu parent;
	private final MenuItemDisposableDelegate disposableDelegate;

	public SeparatorMenuItemImpl(final IMenu parent, final IMenuItemSpi menuItemSpi) {
		super(menuItemSpi, new ItemModelBindingDelegate(new MenuItemSpiInvoker(menuItemSpi), new SeparatorItemModelBuilder().build()));

		this.parent = parent;
		this.disposableDelegate = new MenuItemDisposableDelegate(this, getItemModelBindingDelegate());
	}

	@Override
	public IMenu getParent() {
		return parent;
	}

	@Override
	public void dispose() {
		disposableDelegate.dispose();
	}

	@Override
	public boolean isDisposed() {
		return disposableDelegate.isDisposed();
	}

	@Override
	public void addDisposeListener(final IDisposeListener listener) {
		disposableDelegate.addDisposeListener(listener);
	}

	@Override
	public void removeDisposeListener(final IDisposeListener listener) {
		disposableDelegate.removeDisposeListener(listener);
	}

	@Override
	public ISeparatorItemModel getModel() {
		return (ISeparatorItemModel) getItemModelBindingDelegate().getModel();
	}

	@Override
	public void setModel(final IMenuItemModel model) {
		getItemModelBindingDelegate().setModel(model);
	}

}
