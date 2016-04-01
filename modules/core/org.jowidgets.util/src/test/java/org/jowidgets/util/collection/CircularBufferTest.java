/*
 * Copyright (c) 2016, grossmann
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

import junit.framework.Assert;

import org.jowidgets.util.NullCompatibleEquivalence;
import org.junit.Test;

public class CircularBufferTest {

    @Test
    public void testCreateBuffer() {
        final int capacity = 100;
        final ICircularBuffer<Object> buffer = CircularBuffer.create(capacity);

        Assert.assertEquals(capacity, buffer.capacity());
        assertBufferIsEmpty(buffer);
    }

    @Test
    public void testAdd() {
        final int capacity = 100;
        final ICircularBuffer<Integer> buffer = CircularBuffer.create(capacity);

        //add the elements from 0 to 99
        for (int i = 0; i < capacity; i++) {
            final Integer element = Integer.valueOf(i);
            buffer.add(element);
            Assert.assertEquals(i + 1, buffer.size());
            Assert.assertEquals(element, buffer.get(i));
        }

        //now the buffer is full, so the size is equal with the capacity
        Assert.assertEquals(capacity, buffer.size());

        //now add the some integers to the buffer with shifting
        for (int i = 0; i < 2 * capacity; i++) {
            final Integer previousElement = Integer.valueOf(capacity + (i - 1));
            final Integer element = Integer.valueOf(capacity + i);

            //previous element is the last element of the buffer
            Assert.assertEquals(previousElement, buffer.get(buffer.capacity() - 1));

            //add the element to the end
            buffer.add(element);

            //buffer size must not change from now
            Assert.assertEquals(capacity, buffer.size());

            //assert that the last element is the added element
            Assert.assertEquals(element, buffer.get(buffer.capacity() - 1));

            //previous element was shifted left
            Assert.assertEquals(previousElement, buffer.get(buffer.capacity() - 2));
        }

    }

    @Test
    public void testRemove() {
        final int capacity = 5;
        final ICircularBuffer<String> buffer = CircularBuffer.create(capacity);

        buffer.add("1");
        buffer.add("2");
        buffer.add("3");
        buffer.add("4");
        buffer.add("5");
        buffer.add("6");
        assertBufferHasStringValues(buffer, "2", "3", "4", "5", "6");
        Assert.assertEquals(5, buffer.size());

        Assert.assertTrue(buffer.removeLast());
        Assert.assertEquals(4, buffer.size());
        assertBufferHasStringValues(buffer, "2", "3", "4", "5", null);

        Assert.assertTrue(buffer.removeLast());
        Assert.assertEquals(3, buffer.size());
        assertBufferHasStringValues(buffer, "2", "3", "4", null, null);

        Assert.assertTrue(buffer.removeLast());
        Assert.assertEquals(2, buffer.size());
        assertBufferHasStringValues(buffer, "2", "3", null, null, null);

        Assert.assertTrue(buffer.removeLast());
        Assert.assertEquals(1, buffer.size());
        assertBufferHasStringValues(buffer, "2", null, null, null, null);

        Assert.assertTrue(buffer.removeLast());
        Assert.assertEquals(0, buffer.size());
        assertBufferHasStringValues(buffer, null, null, null, null, null);

        Assert.assertFalse(buffer.removeLast());
        Assert.assertEquals(0, buffer.size());
        assertBufferHasStringValues(buffer, null, null, null, null, null);

        buffer.add("A");
        buffer.add("B");
        buffer.add("C");
        assertBufferHasStringValues(buffer, "A", "B", "C", null, null);
        Assert.assertEquals(3, buffer.size());

        Assert.assertTrue(buffer.removeLast());
        Assert.assertEquals(2, buffer.size());
        assertBufferHasStringValues(buffer, "A", "B", null, null, null);

        Assert.assertTrue(buffer.removeLast());
        Assert.assertEquals(1, buffer.size());
        assertBufferHasStringValues(buffer, "A", null, null, null, null);

        Assert.assertTrue(buffer.removeLast());
        Assert.assertEquals(0, buffer.size());
        assertBufferHasStringValues(buffer, null, null, null, null, null);

        Assert.assertFalse(buffer.removeLast());
        Assert.assertEquals(0, buffer.size());
        assertBufferHasStringValues(buffer, null, null, null, null, null);

        buffer.add("A1");
        buffer.add("B1");
        buffer.add("C1");
        buffer.add("D1");
        buffer.add("E1");
        assertBufferHasStringValues(buffer, "A1", "B1", "C1", "D1", "E1");
        Assert.assertEquals(5, buffer.size());

        Assert.assertTrue(buffer.removeLast());
        Assert.assertEquals(4, buffer.size());
        assertBufferHasStringValues(buffer, "A1", "B1", "C1", "D1", null);

        buffer.add("E2");
        Assert.assertEquals(5, buffer.size());
        assertBufferHasStringValues(buffer, "A1", "B1", "C1", "D1", "E2");

        buffer.add("E2");
        buffer.add("F2");
        buffer.add("G2");
        buffer.add("H2");
        buffer.add("I2");
        Assert.assertEquals(5, buffer.size());
        assertBufferHasStringValues(buffer, "E2", "F2", "G2", "H2", "I2");

        buffer.add("J2");
        buffer.add("K2");
        buffer.add("L2");
        buffer.add("M2");
        buffer.add("N2");
        Assert.assertEquals(5, buffer.size());
        assertBufferHasStringValues(buffer, "J2", "K2", "L2", "M2", "N2");

        Assert.assertTrue(buffer.removeLast());
        Assert.assertEquals(4, buffer.size());
        assertBufferHasStringValues(buffer, "J2", "K2", "L2", "M2", null);

        Assert.assertTrue(buffer.removeLast());
        Assert.assertEquals(3, buffer.size());
        assertBufferHasStringValues(buffer, "J2", "K2", "L2", null, null);

        Assert.assertTrue(buffer.removeLast());
        Assert.assertEquals(2, buffer.size());
        assertBufferHasStringValues(buffer, "J2", "K2", null, null, null);

        Assert.assertTrue(buffer.removeLast());
        Assert.assertEquals(1, buffer.size());
        assertBufferHasStringValues(buffer, "J2", null, null, null, null);

        Assert.assertTrue(buffer.removeLast());
        Assert.assertEquals(0, buffer.size());
        assertBufferHasStringValues(buffer, null, null, null, null, null);

        Assert.assertFalse(buffer.removeLast());
        Assert.assertEquals(0, buffer.size());
        assertBufferHasStringValues(buffer, null, null, null, null, null);
    }

    @Test
    public void testCirculate() {
        final int capacity = 5;
        final ICircularBuffer<String> buffer = CircularBuffer.create(capacity);

        buffer.add("A");
        assertBufferHasStringValues(buffer, "A");
        Assert.assertEquals(1, buffer.size());

        buffer.add("B");
        assertBufferHasStringValues(buffer, "A", "B");
        Assert.assertEquals(2, buffer.size());

        buffer.add("C");
        assertBufferHasStringValues(buffer, "A", "B", "C");
        Assert.assertEquals(3, buffer.size());

        buffer.add("D");
        assertBufferHasStringValues(buffer, "A", "B", "C", "D");
        Assert.assertEquals(4, buffer.size());

        buffer.add("E");
        assertBufferHasStringValues(buffer, "A", "B", "C", "D", "E");
        Assert.assertEquals(capacity, buffer.size());

        buffer.add("F");
        assertBufferHasStringValues(buffer, "B", "C", "D", "E", "F");
        Assert.assertEquals(capacity, buffer.size());

        buffer.add("G");
        assertBufferHasStringValues(buffer, "C", "D", "E", "F", "G");
        Assert.assertEquals(capacity, buffer.size());

        buffer.add("H");
        assertBufferHasStringValues(buffer, "D", "E", "F", "G", "H");
        Assert.assertEquals(capacity, buffer.size());

        buffer.add("I");
        assertBufferHasStringValues(buffer, "E", "F", "G", "H", "I");
        Assert.assertEquals(capacity, buffer.size());

        buffer.add("J");
        assertBufferHasStringValues(buffer, "F", "G", "H", "I", "J");
        Assert.assertEquals(capacity, buffer.size());

        buffer.add(null);
        assertBufferHasStringValues(buffer, "G", "H", "I", "J", null);
        Assert.assertEquals(capacity, buffer.size());

        buffer.add(null);
        assertBufferHasStringValues(buffer, "H", "I", "J", null, null);
        Assert.assertEquals(capacity, buffer.size());

        buffer.add(null);
        assertBufferHasStringValues(buffer, "I", "J", null, null, null);
        Assert.assertEquals(capacity, buffer.size());

        buffer.add(null);
        assertBufferHasStringValues(buffer, "J", null, null, null, null);
        Assert.assertEquals(capacity, buffer.size());

        buffer.add(null);
        assertBufferHasStringValues(buffer, null, null, null, null, null);
        Assert.assertEquals(capacity, buffer.size());
    }

    @Test
    public void testSetElement() {
        final int capacity = 5;
        final ICircularBuffer<String> buffer = CircularBuffer.create(capacity);

        buffer.add("A");
        assertBufferHasStringValues(buffer, "A");
        Assert.assertEquals(1, buffer.size());

        buffer.set(0, "A#");
        assertBufferHasStringValues(buffer, "A#");
        Assert.assertEquals(1, buffer.size());

        buffer.add("B");
        buffer.add("C");
        assertBufferHasStringValues(buffer, "A#", "B", "C");
        Assert.assertEquals(3, buffer.size());

        buffer.set(2, "C#");
        assertBufferHasStringValues(buffer, "A#", "B", "C#");
        Assert.assertEquals(3, buffer.size());

        buffer.add("D");
        buffer.add("E");
        assertBufferHasStringValues(buffer, "A#", "B", "C#", "D", "E");
        Assert.assertEquals(capacity, buffer.size());

        buffer.set(4, "E#");
        assertBufferHasStringValues(buffer, "A#", "B", "C#", "D", "E#");
        Assert.assertEquals(capacity, buffer.size());

        buffer.add("F");
        assertBufferHasStringValues(buffer, "B", "C#", "D", "E#", "F");

        buffer.set(4, "F#");
        assertBufferHasStringValues(buffer, "B", "C#", "D", "E#", "F#");

        buffer.set(0, "A");
        buffer.set(1, "B");
        buffer.set(2, "C");
        buffer.set(3, "D");
        buffer.set(4, "E");
        assertBufferHasStringValues(buffer, "A", "B", "C", "D", "E");
    }

    @Test
    public void testFillBufferWithNull() {
        final int capacity = 5;
        final ICircularBuffer<String> buffer = CircularBuffer.create(capacity);

        buffer.add("A");
        buffer.add("B");
        buffer.add("C");
        Assert.assertEquals(3, buffer.size());
        assertBufferHasStringValues(buffer, "A", "B", "C");

        buffer.fill();
        Assert.assertEquals(capacity, buffer.size());
        assertBufferHasStringValues(buffer, "A", "B", "C", null, null);

        buffer.add("D");
        assertBufferHasStringValues(buffer, "B", "C", null, null, "D");

        //buffer must remain unchanged if full buffer was filled again
        buffer.fill();
        assertBufferHasStringValues(buffer, "B", "C", null, null, "D");
    }

    @Test
    public void testFillEmptyBufferWithNull() {
        final int capacity = 5;
        final ICircularBuffer<String> buffer = CircularBuffer.create(capacity);
        Assert.assertEquals(0, buffer.size());

        buffer.fill();
        Assert.assertEquals(capacity, buffer.size());
        assertBufferHasStringValues(buffer, null, null, null, null, null);
    }

    @Test
    public void testFillBuffer() {
        final int capacity = 5;
        final ICircularBuffer<String> buffer = CircularBuffer.create(capacity);

        buffer.add("A");
        buffer.add("B");
        buffer.add("C");
        Assert.assertEquals(3, buffer.size());
        assertBufferHasStringValues(buffer, "A", "B", "C");

        buffer.fill("FOO");
        Assert.assertEquals(capacity, buffer.size());
        assertBufferHasStringValues(buffer, "A", "B", "C", "FOO", "FOO");

        buffer.add("D");
        assertBufferHasStringValues(buffer, "B", "C", "FOO", "FOO", "D");

        //buffer must remain unchanged if full buffer was filled again
        buffer.fill();
        assertBufferHasStringValues(buffer, "B", "C", "FOO", "FOO", "D");

        buffer.clear();
        buffer.add("1");
        buffer.add("2");
        buffer.add("3");
        buffer.add("4");
        buffer.add("5");
        buffer.add("6");
        buffer.removeLast();
        Assert.assertEquals(4, buffer.size());
        assertBufferHasStringValues(buffer, "2", "3", "4", "5", null);

        buffer.fill();
        Assert.assertEquals(5, buffer.size());
        assertBufferHasStringValues(buffer, "2", "3", "4", "5", null);

        buffer.add("7");
        assertBufferHasStringValues(buffer, "3", "4", "5", null, "7");

        buffer.setSize(2);
        Assert.assertEquals(2, buffer.size());
        assertBufferHasStringValues(buffer, "3", "4", null, null, null);

        buffer.fill();
        Assert.assertEquals(5, buffer.size());
        assertBufferHasStringValues(buffer, "3", "4", null, null, null);

        buffer.removeLast();
        buffer.removeLast();
        buffer.removeLast();
        Assert.assertEquals(2, buffer.size());
        assertBufferHasStringValues(buffer, "3", "4", null, null, null);

        buffer.fill();
        Assert.assertEquals(5, buffer.size());
        assertBufferHasStringValues(buffer, "3", "4", null, null, null);
    }

    @Test
    public void testFillEmptyBuffer() {
        final int capacity = 5;
        final ICircularBuffer<String> buffer = CircularBuffer.create(capacity);
        Assert.assertEquals(0, buffer.size());

        buffer.fill("FOO");
        Assert.assertEquals(capacity, buffer.size());
        assertBufferHasStringValues(buffer, "FOO", "FOO", "FOO", "FOO", "FOO");
    }

    @Test
    public void testSetBufferSize() {
        final int capacity = 6;
        final ICircularBuffer<String> buffer = CircularBuffer.create(capacity);
        Assert.assertEquals(0, buffer.size());

        buffer.add("A");
        buffer.add("B");
        buffer.add("C");
        buffer.add("D");
        buffer.add("E");
        Assert.assertEquals(5, buffer.size());
        assertBufferHasStringValues(buffer, "A", "B", "C", "D", "E", null);

        buffer.setSize(3);
        Assert.assertEquals(3, buffer.size());
        assertBufferHasStringValues(buffer, "A", "B", "C", null, null, null);

        buffer.setSize(5);
        Assert.assertEquals(5, buffer.size());
        assertBufferHasStringValues(buffer, "A", "B", "C", null, null, null);

        buffer.setSize(0);
        Assert.assertEquals(0, buffer.size());
        assertBufferHasStringValues(buffer, null, null, null, null, null, null);

        buffer.add("F");
        buffer.add("G");
        buffer.add("H");
        buffer.add("I");
        buffer.add("J");
        buffer.add("K");
        Assert.assertEquals(6, buffer.size());
        assertBufferHasStringValues(buffer, "F", "G", "H", "I", "J", "K");

        buffer.setSize(5);
        Assert.assertEquals(5, buffer.size());
        assertBufferHasStringValues(buffer, "F", "G", "H", "I", "J", null);

        buffer.add("K2");
        Assert.assertEquals(6, buffer.size());
        assertBufferHasStringValues(buffer, "F", "G", "H", "I", "J", "K2");

        buffer.add("L");
        Assert.assertEquals(6, buffer.size());
        assertBufferHasStringValues(buffer, "G", "H", "I", "J", "K2", "L");

        buffer.setSize(5);
        Assert.assertEquals(5, buffer.size());
        assertBufferHasStringValues(buffer, "G", "H", "I", "J", "K2", null);

        buffer.add("L2");
        Assert.assertEquals(6, buffer.size());
        assertBufferHasStringValues(buffer, "G", "H", "I", "J", "K2", "L2");

        buffer.setSize(5);
        Assert.assertEquals(5, buffer.size());
        assertBufferHasStringValues(buffer, "G", "H", "I", "J", "K2", null);

        buffer.setSize(3);
        Assert.assertEquals(3, buffer.size());
        assertBufferHasStringValues(buffer, "G", "H", "I", null, null, null);

        buffer.setSize(1);
        Assert.assertEquals(1, buffer.size());
        assertBufferHasStringValues(buffer, "G", null, null, null, null, null);

        buffer.setSize(0);
        Assert.assertEquals(0, buffer.size());
        assertBufferHasStringValues(buffer, null, null, null, null, null, null);

        buffer.setSize(3);
        Assert.assertEquals(3, buffer.size());
        assertBufferHasStringValues(buffer, null, null, null, null, null, null);

        buffer.add("A");
        Assert.assertEquals(4, buffer.size());
        assertBufferHasStringValues(buffer, null, null, null, "A", null, null);

        buffer.setSize(6);
        Assert.assertEquals(6, buffer.size());
        assertBufferHasStringValues(buffer, null, null, null, "A", null, null);

        buffer.add("B");
        Assert.assertEquals(6, buffer.size());
        assertBufferHasStringValues(buffer, null, null, "A", null, null, "B");

        buffer.setSize(2);
        Assert.assertEquals(2, buffer.size());
        assertBufferHasStringValues(buffer, null, null, null, null, null, null);

        buffer.add("C");
        Assert.assertEquals(3, buffer.size());
        assertBufferHasStringValues(buffer, null, null, "C", null, null, null);
    }

    @Test
    public void testClear() {
        final int capacity = 100;
        final ICircularBuffer<Integer> buffer = CircularBuffer.create(capacity);

        //add the elements from 0 to 199
        for (int i = 0; i < 2 * capacity; i++) {
            final Integer element = Integer.valueOf(i);
            buffer.add(element);
        }
        Assert.assertEquals(capacity, buffer.size());

        buffer.clear();
        assertBufferIsEmpty(buffer);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithNegativeIndex() {
        final int capacity = 100;
        final ICircularBuffer<Integer> buffer = CircularBuffer.create(capacity);
        buffer.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithIndexIsCapacity() {
        final int capacity = 100;
        final ICircularBuffer<Integer> buffer = CircularBuffer.create(capacity);
        buffer.get(capacity);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithIndexIsGreaterCapacity() {
        final int capacity = 100;
        final ICircularBuffer<Integer> buffer = CircularBuffer.create(capacity);
        buffer.get(capacity + 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEmptyBufferWithIndexIsGreaterSize() {
        final int capacity = 100;
        final ICircularBuffer<String> buffer = CircularBuffer.create(capacity);
        buffer.set(0, "FOO");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetBufferWithIndexIsGreaterSize() {
        final int capacity = 100;
        final ICircularBuffer<String> buffer = CircularBuffer.create(capacity);
        buffer.add("FOO");
        buffer.set(1, "FIGHTER");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetBufferWithNegativeIndex() {
        final int capacity = 100;
        final ICircularBuffer<String> buffer = CircularBuffer.create(capacity);
        buffer.add("FOO");
        buffer.set(-1, "FIGHTER");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetSizeWithNegativeIndex() {
        CircularBuffer.create(5).setSize(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetSizeWithGreaterThatCapacity() {
        CircularBuffer.create(5).setSize(6);
    }

    private void assertBufferHasStringValues(final ICircularBuffer<?> buffer, final String... values) {
        for (int i = 0; i < values.length; i++) {
            Assert.assertTrue(
                    "The value '" + values[i] + "' is expected but '" + buffer.get(i) + "' is set!",
                    NullCompatibleEquivalence.equals(values[i], buffer.get(i)));
        }
    }

    private void assertBufferIsEmpty(final ICircularBuffer<?> buffer) {
        Assert.assertEquals(0, buffer.size());
        for (int i = 0; i < buffer.capacity(); i++) {
            Assert.assertNull(buffer.get(i));
        }
    }

}
