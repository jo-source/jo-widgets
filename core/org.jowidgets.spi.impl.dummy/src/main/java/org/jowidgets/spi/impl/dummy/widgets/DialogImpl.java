/*
 * Copyright (c) 2010, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.spi.impl.dummy.widgets;

import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.impl.dummy.dummyui.UIDDialog;
import org.jowidgets.spi.impl.dummy.dummyui.UIDMenuItem;
import org.jowidgets.spi.impl.dummy.dummyui.UIDWindow;
import org.jowidgets.spi.impl.dummy.image.DummyImageRegistry;
import org.jowidgets.spi.widgets.IFrameSpi;
import org.jowidgets.spi.widgets.IMenuBarSpi;
import org.jowidgets.spi.widgets.setup.IDialogSetupSpi;

public class DialogImpl extends DummyWindow implements IFrameSpi {

	public DialogImpl(
		final IGenericWidgetFactory factory,
		final DummyImageRegistry imageRegistry,
		final Object parentUiReference,
		final IDialogSetupSpi setup) {
		super(factory, new UIDDialog((UIDWindow) parentUiReference));

		getUiReference().setTitle(setup.getTitle());
		getUiReference().setResizable(setup.isResizable());
		getUiReference().setModal(true);

		setIcon(setup.getIcon(), imageRegistry);
	}

	@Override
	public UIDDialog getUiReference() {
		return (UIDDialog) super.getUiReference();
	}

	@Override
	public void setVisible(final boolean visible) {
		getUiReference().setVisible(visible);
	}

	@Override
	public IMenuBarSpi createMenuBar() {
		return new MenuBarImpl(new UIDMenuItem());
	}

}
