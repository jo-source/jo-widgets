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

import org.jowidgets.common.widgets.IMainMenuCommon;
import org.jowidgets.common.widgets.controler.IMenuListener;
import org.jowidgets.impl.base.delegate.ItemDelegate;
import org.jowidgets.spi.widgets.IMainMenuSpi;

public class MainMenuSpiWrapper extends WidgetSpiWrapper implements IMainMenuCommon {

	private final ItemDelegate itemDelegate;

	public MainMenuSpiWrapper(final IMainMenuSpi widget, final ItemDelegate itemDelegate) {
		super(widget);
		this.itemDelegate = itemDelegate;
	}

	@Override
	public IMainMenuSpi getWidget() {
		return (IMainMenuSpi) super.getWidget();
	}

	protected ItemDelegate getItemDelegate() {
		return itemDelegate;
	}

	@Override
	public void setText(final String text) {
		itemDelegate.setText(text);
	}

	@Override
	public void setMnemonic(final char mnemonic) {
		itemDelegate.setMnemonic(mnemonic);
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
