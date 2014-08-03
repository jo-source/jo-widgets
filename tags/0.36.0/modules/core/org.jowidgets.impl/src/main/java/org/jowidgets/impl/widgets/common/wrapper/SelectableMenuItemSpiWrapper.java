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

package org.jowidgets.impl.widgets.common.wrapper;

import org.jowidgets.api.model.item.ISelectableItemModel;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.common.widgets.ISelectableMenuItemCommon;
import org.jowidgets.common.widgets.controller.IItemStateListener;
import org.jowidgets.impl.base.delegate.SelectableItemModelBindingDelegate;
import org.jowidgets.spi.widgets.ISelectableMenuItemSpi;
import org.jowidgets.tools.controller.ItemStateObservable;

public class SelectableMenuItemSpiWrapper extends MenuItemSpiWrapper implements ISelectableMenuItemCommon {

	private final ItemStateObservable itemStateObservable;

	public SelectableMenuItemSpiWrapper(final ISelectableMenuItemSpi widget, final SelectableItemModelBindingDelegate itemDelegate) {
		super(widget, itemDelegate);
		this.itemStateObservable = new ItemStateObservable();
		widget.addItemListener(new IItemStateListener() {
			@Override
			public void itemStateChanged() {
				getModel().setSelected(widget.isSelected());
				itemStateObservable.fireItemStateChanged();
			}
		});
	}

	@Override
	public ISelectableMenuItemSpi getWidget() {
		return (ISelectableMenuItemSpi) super.getWidget();
	}

	@Override
	protected SelectableItemModelBindingDelegate getItemModelBindingDelegate() {
		return (SelectableItemModelBindingDelegate) super.getItemModelBindingDelegate();
	}

	public ISelectableItemModel getModel() {
		return getItemModelBindingDelegate().getModel();
	}

	@Override
	public void addItemListener(final IItemStateListener listener) {
		itemStateObservable.addItemListener(listener);
	}

	@Override
	public void removeItemListener(final IItemStateListener listener) {
		itemStateObservable.removeItemListener(listener);
	}

	@Override
	public void setAccelerator(final Accelerator accelerator) {
		getItemModelBindingDelegate().setAccelerator(accelerator);
	}

	@Override
	public void setSelected(final boolean selected) {
		getItemModelBindingDelegate().setSelected(selected);
	}

	@Override
	public boolean isSelected() {
		return getItemModelBindingDelegate().isSelected();
	}

}
