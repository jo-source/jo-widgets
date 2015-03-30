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
import org.jowidgets.api.widgets.descriptor.IFrameDescriptor;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.tools.widgets.wrapper.FrameWrapper;

/**
 * A Frame can be used to encapsulate a IFrame implementation
 */
public class Frame extends FrameWrapper implements IFrame {

    /**
     * Creates a new instance
     * 
     * @param title The frames title
     * @param icon The frame icon
     */
    public Frame(final String title, final IImageConstant icon) {
        this(BPF.frame(title).setIcon(icon));
    }

    /**
     * Creates a new instance
     * 
     * @param title The frames title
     */
    public Frame(final String title) {
        this(BPF.frame(title));
    }

    /**
     * Creates a new instance
     * 
     * @param descriptor The frame descriptor to use
     */
    public Frame(final IFrameDescriptor descriptor) {
        super(Toolkit.createRootFrame(descriptor));
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
