/*
 * Copyright (c) 2013, MGrossmann
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

/**
 * Implements an observable value that never has null values.
 * 
 * If set to null, a given default value will be used
 * 
 * @author MGrossmann
 * 
 * @param <VALUE_TYPE>
 */
public class MandatoryObservableValue<VALUE_TYPE> extends ObservableValue<VALUE_TYPE> {

    private final VALUE_TYPE defaultValue;

    public MandatoryObservableValue(final VALUE_TYPE defaultValue) {
        Assert.paramNotNull(defaultValue, "defaultValue");
        this.defaultValue = defaultValue;
    }

    @Override
    public void setValue(final VALUE_TYPE value) {
        if (value != null) {
            super.setValue(value);
        }
        else {
            super.setValue(defaultValue);
        }
    }

    @Override
    public VALUE_TYPE getValue() {
        final VALUE_TYPE superResult = super.getValue();
        if (superResult != null) {
            return superResult;
        }
        else {
            return defaultValue;
        }
    }

}
