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

package org.jowidgets.util;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

public class ObservableValueTest {

    private static String STRING_1 = "STRING_1";
    private static String STRING_2 = "STRING_2";
    private static String STRING_3 = "STRING_3";

    @Test
    public void testObservableValue() {
        //Create two observable values
        final ObservableValue<String> value = new ObservableValue<String>(STRING_1);

        final ObservableValueListener listener = new ObservableValueListener();
        value.addValueListener(listener);

        //set the intial value and assume that listener not fires
        value.setValue(STRING_1);
        Assert.assertEquals(0, listener.getInvocations());
        Assert.assertNull(listener.getLastValue());
        Assert.assertNull(listener.getLastObservableValue());

        //set value and assume that value changes and listener fires
        value.setValue(STRING_2);
        Assert.assertEquals(STRING_2, value.getValue());
        Assert.assertEquals(1, listener.getInvocations());
        Assert.assertEquals(STRING_2, listener.getLastValue());
        Assert.assertSame(value, listener.getLastObservableValue());

        //set value again and assume that listener not fires again
        value.setValue(STRING_2);
        Assert.assertEquals(1, listener.getInvocations());
        Assert.assertEquals(STRING_2, listener.getLastValue());
        Assert.assertSame(value, listener.getLastObservableValue());

        //set value and assume that value changes and listener fires
        value.setValue(STRING_3);
        Assert.assertEquals(STRING_3, value.getValue());
        Assert.assertEquals(2, listener.getInvocations());
        Assert.assertEquals(STRING_3, listener.getLastValue());
        Assert.assertSame(value, listener.getLastObservableValue());
    }

    @Test
    public void testEqualsHashCodeNotImplementedMissleading() {
        final ObservableValue<String> value = new ObservableValue<String>();
        value.setValue(STRING_1);

        final Set<ObservableValue<String>> set = new HashSet<ObservableValue<String>>();
        set.add(value);

        value.setValue(STRING_2);

        Assert.assertTrue(set.remove(value));
    }

    private final class ObservableValueListener implements IObservableValueListener<String> {

        private int invocations = 0;
        private IObservableValue<String> lastObservableValue;
        private String lastValue;

        @Override
        public void changed(final IObservableValue<String> observableValue, final String value) {
            invocations++;
            lastObservableValue = observableValue;
            lastValue = value;
        }

        private int getInvocations() {
            return invocations;
        }

        private IObservableValue<String> getLastObservableValue() {
            return lastObservableValue;
        }

        private String getLastValue() {
            return lastValue;
        }

    }
}
