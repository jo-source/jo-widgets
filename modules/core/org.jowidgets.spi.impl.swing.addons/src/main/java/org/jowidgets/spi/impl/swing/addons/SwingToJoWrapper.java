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

package org.jowidgets.spi.impl.swing.addons;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.util.Assert;

public final class SwingToJoWrapper {

    private SwingToJoWrapper() {}

    /**
     * Creates a wrapped composite from the given JPanel with autoDispose = false
     * 
     * Remark: autoDispose is set to false by default due to backward compatibility and due to the fact
     * that in swing panels can be reused after disposed (lost peer) and this is even a best practice.
     * This implies that the returned IComposite will never be disposed until it will be done programmatically.
     * 
     * @param panel The panel to create the wrapper for.
     * 
     * @return The composite that wrappes the given panel
     */
    public static IComposite create(final JPanel panel) {
        return create(panel, false);
    }

    /**
     * Creates a wrapped composite from the given JPanel
     * 
     * @param panel The panel to create the wrapper for.
     * @param autoDispose If set to true, the returned composite will be disposed automatically after the given panel has lost its
     *            peer. Do not set to true if you intend to reuse the composite with a different parent.
     * 
     * @return The composite that wrappes the given panel
     */
    public static IComposite create(final JPanel panel, final boolean autoDispose) {
        Assert.paramNotNull(panel, "panel");
        return Toolkit.getWidgetWrapperFactory().createComposite(panel, autoDispose);
    }

    /**
     * Creates a wrapped IFrame from the given JFrame with autoDispose = false
     * 
     * Remark: autoDispose is set to false by default due to backward compatibility and due to the fact
     * that in swing frames can be reused after disposed (lost peer) and this is even a best practice.
     * This implies that the returned IFrame will never be disposed until it will be done programmatically.
     * 
     * @param frame The frame to create the wrapper for.
     * 
     * @return The IFrame that wrappes the given frame
     */
    public static IFrame create(final JFrame frame) {
        return create(frame, false);
    }

    /**
     * Creates a wrapped IFrame from the given JDialog with autoDispose = false
     * 
     * Remark: autoDispose is set to false by default due to backward compatibility and due to the fact
     * that in swing frames can be reused after disposed (lost peer) and this is even a best practice.
     * This implies that the returned IFrame will never be disposed until it will be done programmatically.
     * 
     * @param dialog The dialog to create the wrapper for.
     * 
     * @return The IFrame that wrappes the given dialog
     */
    public static IFrame create(final JDialog dialog) {
        return create(dialog, false);
    }

    /**
     * Creates a wrapped IFrame from the given JFrame
     * 
     * @param frame The frame to create the wrapper for.
     * @param autoDispose If set to true, the returned IFrame will be disposed automatically after the given JFrame has lost its
     *            peer. Do not set to true if you intend to reuse (open / pack) the JFrame with after dispose.
     * 
     * @return The IFrame that wrappes the given frame
     */
    public static IFrame create(final JFrame frame, final boolean autoDispose) {
        Assert.paramNotNull(frame, "frame");
        return Toolkit.getWidgetWrapperFactory().createFrame(frame, autoDispose);
    }

    /**
     * Creates a wrapped IFrame from the given JDialog
     * 
     * @param dialog The dialog to create the wrapper for.
     * @param autoDispose If set to true, the returned IFrame will be disposed automatically after the given JDialog has lost its
     *            peer. Do not set to true if you intend to reuse (open / pack) the JDialog with after dispose.
     * 
     * @return The IFrame that wrappes the given dialog
     */
    public static IFrame create(final JDialog dialog, final boolean autoDispose) {
        Assert.paramNotNull(dialog, "dialog");
        return Toolkit.getWidgetWrapperFactory().createFrame(dialog, autoDispose);
    }

}
