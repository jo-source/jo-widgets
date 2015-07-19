/*
 * Copyright (c) 2015, grossmann
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

package org.jowidgets.tools.widgets.base;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IWindow;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.tools.widgets.wrapper.FrameWrapper;
import org.jowidgets.util.Assert;

/**
 * A Dialog can be used to encapsulate a IFrame implementation that represents a dialog
 */
public class Dialog extends FrameWrapper implements IFrame {

    /**
     * Creates a new dialog
     * 
     * @param parent The parent window
     * @param title The dialog title
     * @param icon The dialog icon
     */
    public Dialog(final IWindow parent, final String title, final IImageConstant icon) {
        this(parent, BPF.dialog(title).setIcon(icon));
    }

    /**
     * Creates a new dialog
     * 
     * @param parent The parent window
     * @param title The dialog title
     */
    public Dialog(final IWindow parent, final String title) {
        this(parent, BPF.dialog(title));
    }

    /**
     * Creates a new dialog
     * 
     * @param parent The parent window
     * @param descriptor The dialog descriptor
     */
    public Dialog(final IWindow parent, final IWidgetDescriptor<? extends IFrame> descriptor) {
        super(Toolkit.getWidgetFactory().create(Assert.getParamNotNull(parent, "parent").getUiReference(), descriptor));
    }

    /**
     * Gets the wrapped frame
     * 
     * @return The wrapped frame
     */
    protected IFrame getFrame() {
        return (IFrame) super.getWidget();
    }
}
