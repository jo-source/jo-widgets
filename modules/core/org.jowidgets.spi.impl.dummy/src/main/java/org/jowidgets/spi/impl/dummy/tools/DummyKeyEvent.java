/*
 * Copyright (c) 2017, herrg
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

package org.jowidgets.spi.impl.dummy.tools;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.common.widgets.controller.IKeyEvent;
import org.jowidgets.util.Assert;

public final class DummyKeyEvent implements IKeyEvent {

    private final Set<Modifier> modifier;
    private final VirtualKey virtualKey;

    public DummyKeyEvent(final VirtualKey virtualKey, final Modifier... modifier) {
        Assert.paramNotNull(virtualKey, "virtualKey");

        this.virtualKey = virtualKey;
        if (modifier != null) {
            this.modifier = Collections.unmodifiableSet(new HashSet<Modifier>(Arrays.asList(modifier)));
        }
        else {
            this.modifier = Collections.emptySet();
        }
    }

    @Override
    public VirtualKey getVirtualKey() {
        return virtualKey;
    }

    @Override
    public Character getCharacter() {
        return virtualKey.getCharacter();
    }

    @Override
    public Character getResultingCharacter() {
        return virtualKey.getCharacter();
    }

    @Override
    public Set<Modifier> getModifier() {
        return modifier;
    }

}
