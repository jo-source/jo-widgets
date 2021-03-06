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

package org.jowidgets.spi.impl.swt.addons;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.util.Assert;

/**
 * Creates wrapper for native swt widgets
 */
public final class SwtToJoWrapper {

    private SwtToJoWrapper() {}

    /**
     * Creates a IComposite Wrapper with help of a swt composite.
     * 
     * The IComposite will be disposed if the swt composite will be disposed
     * 
     * @param composite The swt composite to create the IComposite for, must not be null
     * 
     * @return A IComposite Wrapper
     */
    public static IComposite create(final Composite composite) {
        Assert.paramNotNull(composite, "composite");
        return Toolkit.getWidgetWrapperFactory().createComposite(composite);
    }

    /**
     * Creates a IFrame Wrapper with help of a swt shell.
     * 
     * The IFrame will be disposed if the swt shell will be disposed
     * 
     * @param shell The swt shell to create the IComposite for, must not be null
     * 
     * @return A IFrame Wrapper
     */
    public static IFrame create(final Shell shell) {
        Assert.paramNotNull(shell, "shell");
        return Toolkit.getWidgetWrapperFactory().createFrame(shell);
    }

}
