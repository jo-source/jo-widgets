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

package org.jowidgets.spi.impl.swing.widgets.internal;

import java.awt.Component;

import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controler.IMenuListener;
import org.jowidgets.common.widgets.controler.impl.MenuObservable;
import org.jowidgets.spi.impl.swing.widgets.internal.base.JoPopupMenu;
import org.jowidgets.spi.widgets.IPopupMenuSpi;

public class PopupMenuImpl extends AbstractSwingMenu implements IPopupMenuSpi {

	private Component parent;

	public PopupMenuImpl(final Component parent) {
		super(new JoPopupMenu() {

			private static final long serialVersionUID = -3625772723915271319L;

			private final MenuObservable menuObservable = new MenuObservable();

			@Override
			public void setVisible(final boolean visible) {
				super.setVisible(visible);
				if (visible) {
					menuObservable.fireMenuActivated();
				}
				else {
					menuObservable.fireMenuDeactivated();
				}
			}

		});
		this.parent = parent;
	}

	public void setParent(final Component parent) {
		this.parent = parent;
	}

	@Override
	public JoPopupMenu getUiReference() {
		return (JoPopupMenu) super.getUiReference();
	}

	@Override
	public void show(final Position position) {
		getUiReference().show(parent, position.getX(), position.getY());
	}

	@Override
	public void addMenuListener(final IMenuListener listener) {
		getUiReference().addMenuListener(listener);
	}

	@Override
	public void removeMenuListener(final IMenuListener listener) {
		getUiReference().removeMenuListener(listener);
	}

}
