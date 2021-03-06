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

package org.jowidgets.tools.command;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jowidgets.api.command.EnabledState;
import org.jowidgets.api.command.IEnabledChecker;
import org.jowidgets.api.command.IEnabledState;
import org.jowidgets.util.Assert;
import org.jowidgets.util.EmptyCheck;
import org.jowidgets.util.event.IChangeListener;

public class EnabledChecker implements IEnabledChecker {

    private final Set<IChangeListener> changeListeners;

    private IEnabledState enabledState;

    /**
     * Creates a new enabled checker
     */
    public EnabledChecker() {
        this.changeListeners = new LinkedHashSet<IChangeListener>();
        this.enabledState = EnabledState.ENABLED;
    }

    /**
     * Sets the enabled state
     * 
     * @param enabledState The enabled state to set, must not be null
     */
    public final void setEnabledState(final IEnabledState enabledState) {
        Assert.paramNotNull(enabledState, "enabledState");

        final boolean stateChanged = !this.enabledState.equals(enabledState);
        this.enabledState = enabledState;

        if (stateChanged) {
            fireEnabledStateChanged();
        }
    }

    /**
     * Sets the enables state EnabledState.ENABLED
     */
    public final void setEnabled() {
        setEnabledState(EnabledState.ENABLED);
    }

    /**
     * Sets a disabled EnabledState with a given reason
     * 
     * @param reason The reason, may be empty and null
     */
    public final void setDisabled(final String reason) {
        if (EmptyCheck.isEmpty(reason)) {
            setEnabledState(EnabledState.DISABLED);
        }
        else {
            setEnabledState(EnabledState.disabled(reason));
        }
    }

    @Override
    public final IEnabledState getEnabledState() {
        return enabledState;
    }

    @Override
    public final void addChangeListener(final IChangeListener listener) {
        changeListeners.add(listener);
    }

    @Override
    public final void removeChangeListener(final IChangeListener listener) {
        changeListeners.remove(listener);
    }

    /**
     * Fires a changed event on all registered change listeners
     */
    public final void fireEnabledStateChanged() {
        for (final IChangeListener listener : new LinkedList<IChangeListener>(changeListeners)) {
            listener.changed();
        }
    }

}
