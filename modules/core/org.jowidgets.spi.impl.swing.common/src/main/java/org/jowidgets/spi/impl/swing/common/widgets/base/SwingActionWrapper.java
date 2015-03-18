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

package org.jowidgets.spi.impl.swing.common.widgets.base;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import javax.swing.Action;

import org.jowidgets.util.Assert;

public class SwingActionWrapper implements Action, Serializable {

    private static final long serialVersionUID = -4929518877569965000L;

    private final Action original;

    public SwingActionWrapper(final Action original) {
        Assert.paramNotNull(original, "original");
        this.original = original;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        original.actionPerformed(e);
    }

    @Override
    public Object getValue(final String key) {
        return original.getValue(key);
    }

    @Override
    public void putValue(final String key, final Object value) {
        original.putValue(key, value);
    }

    @Override
    public void setEnabled(final boolean b) {
        original.setEnabled(b);
    }

    @Override
    public boolean isEnabled() {
        return original.isEnabled();
    }

    @Override
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        original.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        original.removePropertyChangeListener(listener);
    }

}
