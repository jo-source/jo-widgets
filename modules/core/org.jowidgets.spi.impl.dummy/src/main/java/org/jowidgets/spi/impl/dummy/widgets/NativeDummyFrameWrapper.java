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
import org.jowidgets.spi.impl.controller.DisposeObservableSpi;
import org.jowidgets.spi.impl.dummy.ui.UIDMenuItem;
import org.jowidgets.spi.impl.dummy.ui.UIDWindow;
import org.jowidgets.spi.widgets.IFrameWrapperSpi;
import org.jowidgets.spi.widgets.IMenuBarSpi;
import org.jowidgets.spi.widgets.controller.IDisposeListenerSpi;

public class NativeDummyFrameWrapper extends DummyWindow implements IFrameWrapperSpi {

    private final DisposeObservableSpi disposeObservable;

    public NativeDummyFrameWrapper(final IGenericWidgetFactory factory, final UIDWindow uiReference) {
        super(factory, uiReference);
        this.disposeObservable = new DisposeObservableSpi();
    }

    @Override
    public UIDWindow getUiReference() {
        return super.getUiReference();
    }

    @Override
    public IMenuBarSpi createMenuBar() {
        return new MenuBarImpl(new UIDMenuItem());
    }

    @Override
    public void setTitle(final String title) {
        getUiReference().setTitle(title);
    }

    @Override
    public void addDisposeListener(final IDisposeListenerSpi listener) {
        disposeObservable.addDisposeListener(listener);
    }

    @Override
    public void removeDisposeListener(final IDisposeListenerSpi listener) {
        disposeObservable.removeDisposeListener(listener);
    }

    @Override
    public void dispose() {
        super.dispose();
        disposeObservable.fireAfterDispose();
    }

}
