/*
 * Copyright (c) 2011, grossmann
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

import org.jowidgets.api.widgets.IComponent;
import org.jowidgets.api.widgets.IPopupMenu;
import org.jowidgets.impl.widgets.basic.PopupMenuImpl;
import org.jowidgets.spi.widgets.IComponentSpi;
import org.jowidgets.util.Assert;

public class PopupMenuCreationDelegate {

	private final IPopupFactory popupFactory;
	private final List<IPopupMenu> popupMenus;

	public PopupMenuCreationDelegate(final IComponentSpi componentSpi, final IComponent component) {
		this(new IPopupFactory() {
			@Override
			public IPopupMenu create() {
				return new PopupMenuImpl(componentSpi.createPopupMenu(), component);
			}
		});
	}

	public PopupMenuCreationDelegate(final IPopupFactory popupFactory) {
		Assert.paramNotNull(popupFactory, "popupFactory");
		this.popupFactory = popupFactory;
		this.popupMenus = new LinkedList<IPopupMenu>();
	}

	public IPopupMenu createPopupMenu() {
		final IPopupMenu result = popupFactory.create();
		popupMenus.add(result);
		return result;
	}

	public void dispose() {
		for (final IPopupMenu popupMenu : popupMenus) {
			popupMenu.dispose();
		}
	}

	public interface IPopupFactory {
		IPopupMenu create();
	}
}
