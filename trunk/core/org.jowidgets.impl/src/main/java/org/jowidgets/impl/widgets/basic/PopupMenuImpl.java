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

import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IMenuItem;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.impl.base.delegate.MenuDelegate;
import org.jowidgets.impl.widgets.common.wrapper.PopupMenuSpiWrapper;
import org.jowidgets.spi.widgets.IPopupMenuSpi;

public class PopupMenuImpl extends PopupMenuSpiWrapper implements IPopupMenu {

	private final MenuDelegate menuDelegate;

	public PopupMenuImpl(final IPopupMenuSpi popupMenuSpi, final IComponent parent) {
		super(popupMenuSpi);
		this.menuDelegate = new MenuDelegate(popupMenuSpi, parent);
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
	public <WIDGET_TYPE extends IMenuItem> WIDGET_TYPE addMenuItem(final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
		return menuDelegate.addMenuItem(descriptor);
	}

	@Override
	public IComponent getParent() {
		return menuDelegate.getParent();
	}

	@Override
	public <WIDGET_TYPE extends IMenuItem> WIDGET_TYPE addMenuItem(
		final int index,
		final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {

		return menuDelegate.addMenuItem(index, descriptor);
	}
}
