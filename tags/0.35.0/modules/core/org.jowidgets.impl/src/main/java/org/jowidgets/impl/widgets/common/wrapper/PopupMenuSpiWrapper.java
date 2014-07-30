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

import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IPopupMenuCommon;
import org.jowidgets.common.widgets.controller.IMenuListener;
import org.jowidgets.impl.base.delegate.ItemModelBindingDelegate;
import org.jowidgets.spi.widgets.IPopupMenuSpi;

public class PopupMenuSpiWrapper extends WidgetSpiWrapper implements IPopupMenuCommon {

	private final ItemModelBindingDelegate itemModelBindingDelegate;

	public PopupMenuSpiWrapper(final IPopupMenuSpi component, final ItemModelBindingDelegate itemDelegate) {
		super(component);
		this.itemModelBindingDelegate = itemDelegate;
	}

	@Override
	public IPopupMenuSpi getWidget() {
		return (IPopupMenuSpi) super.getWidget();
	}

	protected ItemModelBindingDelegate getItemModelBindingDelegate() {
		return itemModelBindingDelegate;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		itemModelBindingDelegate.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return itemModelBindingDelegate.isEnabled();
	}

	@Override
	public void show(final Position position) {
		getWidget().show(position);
	}

	@Override
	public void addMenuListener(final IMenuListener listener) {
		getWidget().addMenuListener(listener);
	}

	@Override
	public void removeMenuListener(final IMenuListener listener) {
		getWidget().removeMenuListener(listener);
	}

}
