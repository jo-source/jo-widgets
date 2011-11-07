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

package org.jowidgets.spi.impl.swing.widgets;

import javax.swing.JMenuBar;

import org.jowidgets.spi.widgets.IMainMenuSpi;
import org.jowidgets.spi.widgets.IMenuBarSpi;

public class MenuBarImpl implements IMenuBarSpi {

	private final JMenuBar menuBar;

	public MenuBarImpl(final JMenuBar menuBar) {
		this.menuBar = menuBar;
	}

	@Override
	public JMenuBar getUiReference() {
		return menuBar;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		menuBar.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return menuBar.isEnabled();
	}

	@Override
	public void remove(final int index) {
		menuBar.remove(index);
	}

	@Override
	public IMainMenuSpi addMenu(final Integer index) {
		final MainMenuImpl result = new MainMenuImpl();
		addItem(index, result);
		return result;
	}

	private void addItem(final Integer index, final SwingWidget item) {
		if (index != null) {
			getUiReference().add(item.getUiReference(), index.intValue());
		}
		else {
			getUiReference().add(item.getUiReference());
		}
	}

}
