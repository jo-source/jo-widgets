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

package org.jowidgets.util.collection;

import org.jowidgets.util.EmptyCheck;
import org.jowidgets.util.collection.IObserverSetFactory.Strategy;
import org.junit.Assert;
import org.junit.Test;

public class ObserverSetTest {

    @Test(expected = IllegalArgumentException.class)
    public void testAddNull() {
        testAddNull(create(Strategy.HIGH_PERFORMANCE));
        testAddNull(create(Strategy.LOW_MEMORY));
    }

    private void testAddNull(final IObserverSet<ListenerMock> set) {
        set.add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNull() {
        testRemoveNull(create(Strategy.HIGH_PERFORMANCE));
        testRemoveNull(create(Strategy.LOW_MEMORY));
    }

    private void testRemoveNull(final IObserverSet<ListenerMock> set) {
        set.remove(null);
    }

    @Test
    public void testAddAndRemove() {
        testAddAndRemove(create(Strategy.HIGH_PERFORMANCE));
        testAddAndRemove(create(Strategy.LOW_MEMORY));
    }

    private void testAddAndRemove(final IObserverSet<ListenerMock> set) {

        final ListenerMock a = new ListenerMock("A");
        final ListenerMock b = new ListenerMock("B");
        final ListenerMock c = new ListenerMock("C");
        final ListenerMock d = new ListenerMock("D");
        final ListenerMock e = new ListenerMock("E");

        Assert.assertTrue(EmptyCheck.isEmpty(invokeListeners(set)));

        set.add(a);
        Assert.assertEquals("A", invokeListeners(set));

        set.add(b);
        Assert.assertEquals("AB", invokeListeners(set));

        set.add(c);
        Assert.assertEquals("ABC", invokeListeners(set));

        set.add(d);
        Assert.assertEquals("ABCD", invokeListeners(set));

        set.add(e);
        Assert.assertEquals("ABCDE", invokeListeners(set));

        //remove first
        set.remove(a);
        Assert.assertEquals("BCDE", invokeListeners(set));

        //remove last
        set.remove(e);
        Assert.assertEquals("BCD", invokeListeners(set));

        //remove in the middle
        set.remove(c);
        Assert.assertEquals("BD", invokeListeners(set));

        //remove first of two elements
        set.remove(b);
        Assert.assertEquals("D", invokeListeners(set));

        //add b again
        set.add(b);
        Assert.assertEquals("DB", invokeListeners(set));

        //remove last of two elements
        set.remove(b);
        Assert.assertEquals("D", invokeListeners(set));

        //remove last
        set.remove(d);
        Assert.assertTrue(EmptyCheck.isEmpty(invokeListeners(set)));
    }

    @Test
    public void testAddAndRemoveMoreThanOnce() {
        testAddAndRemoveMoreThanOnce(create(Strategy.HIGH_PERFORMANCE));
        testAddAndRemoveMoreThanOnce(create(Strategy.LOW_MEMORY));
    }

    private void testAddAndRemoveMoreThanOnce(final IObserverSet<ListenerMock> set) {

        final ListenerMock a = new ListenerMock("A");
        final ListenerMock b = new ListenerMock("B");
        final ListenerMock c = new ListenerMock("C");
        final ListenerMock d = new ListenerMock("D");
        final ListenerMock e = new ListenerMock("E");

        Assert.assertTrue(EmptyCheck.isEmpty(invokeListeners(set)));

        set.add(a);
        set.add(a);
        Assert.assertEquals("A", invokeListeners(set));

        set.add(a);
        set.add(b);
        set.add(b);
        Assert.assertEquals("AB", invokeListeners(set));

        set.add(a);
        set.add(b);
        set.add(c);
        set.add(c);
        Assert.assertEquals("ABC", invokeListeners(set));

        set.add(a);
        set.add(b);
        set.add(c);
        set.add(d);
        set.add(d);
        Assert.assertEquals("ABCD", invokeListeners(set));

        set.add(a);
        set.add(b);
        set.add(c);
        set.add(d);
        set.add(e);
        set.add(e);
        Assert.assertEquals("ABCDE", invokeListeners(set));

        //remove first
        set.remove(a);
        set.remove(a);
        Assert.assertEquals("BCDE", invokeListeners(set));

        //remove last
        Assert.assertTrue(set.remove(e));
        Assert.assertFalse(set.remove(e));
        Assert.assertEquals("BCD", invokeListeners(set));

        //remove in the middle
        Assert.assertTrue(set.remove(c));
        Assert.assertFalse(set.remove(c));
        Assert.assertEquals("BD", invokeListeners(set));

        //remove first of two elements
        Assert.assertTrue(set.remove(b));
        Assert.assertFalse(set.remove(b));
        Assert.assertEquals("D", invokeListeners(set));

        //add b again
        set.add(b);
        set.add(b);
        Assert.assertEquals("DB", invokeListeners(set));

        //remove last of two elements
        Assert.assertTrue(set.remove(b));
        Assert.assertFalse(set.remove(b));
        Assert.assertEquals("D", invokeListeners(set));

        //remove last
        Assert.assertTrue(set.remove(d));
        Assert.assertFalse(set.remove(d));
        Assert.assertTrue(EmptyCheck.isEmpty(invokeListeners(set)));
    }

    @Test
    public void testRemoveWhileInvoke() {
        testRemoveWhileInvoke(create(Strategy.HIGH_PERFORMANCE));
        testRemoveWhileInvoke(create(Strategy.LOW_MEMORY));
    }

    private void testRemoveWhileInvoke(final IObserverSet<ListenerMock> set) {

        final RemoveListenerMock a = new RemoveListenerMock("A", set);
        final ListenerMock b = new RemoveListenerMock("B", set);
        final ListenerMock c = new RemoveListenerMock("C", set);
        final ListenerMock d = new RemoveListenerMock("D", set);
        final ListenerMock e = new RemoveListenerMock("E", set);

        Assert.assertTrue(EmptyCheck.isEmpty(invokeListeners(set)));

        set.add(a);
        set.add(b);
        set.add(c);
        set.add(d);
        set.add(e);

        Assert.assertEquals("ABCDE", invokeListeners(set));

        //after first invocation all listners was removed by the listener itself
        Assert.assertTrue(EmptyCheck.isEmpty(invokeListeners(set)));

    }

    private static String invokeListeners(final IObserverSet<? extends ListenerMock> listeners) {
        final StringBuilder result = new StringBuilder();
        for (final ListenerMock listener : listeners) {
            listener.invoke(result);
        }
        return result.toString();
    }

    private static IObserverSet<ListenerMock> create(final Strategy strategy) {
        return ObserverSetFactory.create(strategy);
    }

    private static class ListenerMock {

        private final String tanga;

        private ListenerMock(final String tanga) {
            this.tanga = tanga;
        }

        void invoke(final StringBuilder builder) {
            builder.append(tanga);
        }

        @Override
        public String toString() {
            return tanga;
        }

    }

    private static class RemoveListenerMock extends ListenerMock {

        private final IObserverSet<ListenerMock> set;

        private RemoveListenerMock(final String tanga, final IObserverSet<ListenerMock> set) {
            super(tanga);
            this.set = set;
        }

        @Override
        void invoke(final StringBuilder builder) {
            super.invoke(builder);
            set.remove(this);
        }

    }
}
