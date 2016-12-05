/*
 * Copyright (c) 2014, grossmann
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
package org.jowidgets.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

abstract class AbstractObservableBooleanComposite extends AbstractObservableValue<Boolean> implements IObservableValue<Boolean> {

    private final Set<IObservableValue<Boolean>> originals;

    AbstractObservableBooleanComposite(final IObservableValue<Boolean>... originals) {
        this(Arrays.asList(originals));
    }

    AbstractObservableBooleanComposite(final Collection<IObservableValue<Boolean>> originals) {
        Assert.paramNotNull(originals, "originals");
        this.originals = new HashSet<IObservableValue<Boolean>>(originals);

        for (final IObservableValue<Boolean> original : originals) {
            original.addValueListener(new IObservableValueListener<Boolean>() {
                @Override
                public void changed(final IObservableValue<Boolean> observableValue, final Boolean value) {
                    fireValueChanged();
                }
            });
        }
    }

    abstract boolean getValue(Collection<IObservableValue<Boolean>> originals);

    @Override
    public final void setValue(final Boolean value) {
        throw new UnsupportedOperationException("The value is readonly");
    }

    @Override
    public final Boolean getValue() {
        if (originals.isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.valueOf(getValue(originals));
    }

    /**
     * @return The value as a elemental boolean type
     */
    public final boolean get() {
        return getValue().booleanValue();
    }

}
