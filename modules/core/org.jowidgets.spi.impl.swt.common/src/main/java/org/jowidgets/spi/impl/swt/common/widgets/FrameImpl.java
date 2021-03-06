/*
 * Copyright (c) 2010, Michael Grossmann, Lukas Gross
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
package org.jowidgets.spi.impl.swt.common.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.impl.swt.common.image.SwtImageRegistry;
import org.jowidgets.spi.widgets.IFrameSpi;
import org.jowidgets.spi.widgets.setup.IFrameSetupSpi;

public class FrameImpl extends WindowImpl implements IFrameSpi {

    public FrameImpl(final IGenericWidgetFactory factory, final IFrameSetupSpi setup, final SwtImageRegistry imageRegistry) {
        super(factory, new Shell(getStyle(setup)), setup.isCloseable(), imageRegistry);

        if (setup.getTitle() != null) {
            getUiReference().setText(setup.getTitle());
        }
        setIcon(setup.getIcon());
    }

    private static int getStyle(final IFrameSetupSpi setup) {
        int result;
        if (setup.isDecorated()) {
            result = SWT.TITLE | SWT.MIN | SWT.MAX;
        }
        else {
            result = SWT.NONE;
        }
        if (setup.isResizable() && setup.isDecorated()) {
            result = result | SWT.RESIZE;
        }
        if (setup.isCloseable() && setup.isDecorated()) {
            result = result | SWT.CLOSE;
        }
        return result;
    }

}
