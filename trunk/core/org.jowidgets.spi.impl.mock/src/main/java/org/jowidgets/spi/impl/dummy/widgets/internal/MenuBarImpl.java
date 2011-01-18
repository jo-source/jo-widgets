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

package org.jowidgets.spi.impl.dummy.widgets.internal;

import org.jowidgets.spi.impl.dummy.dummyui.UIDMenuItem;
import org.jowidgets.spi.impl.dummy.widgets.DummyWidget;
import org.jowidgets.spi.widgets.IMainMenuSpi;
import org.jowidgets.spi.widgets.IMenuBarSpi;

public class MenuBarImpl extends DummyWidget implements IMenuBarSpi {

	public MenuBarImpl(final UIDMenuItem menuBar) {
		super(menuBar);
	}

	@Override
	public UIDMenuItem getUiReference() {
		return (UIDMenuItem) super.getUiReference();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		getUiReference().setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return getUiReference().isEnabled();
	}

	@Override
	public void remove(final int index) {
		getUiReference().remove(index);
	}

	@Override
	public IMainMenuSpi addMenu(final Integer index) {
		final MainMenuImpl result = new MainMenuImpl();
		addItem(index, result);
		return result;
	}

	private void addItem(final Integer index, final DummyWidget item) {
		if (index != null) {
			getUiReference().add(item.getUiReference(), index.intValue());
		}
		else {
			getUiReference().add(item.getUiReference());
		}
	}

}
