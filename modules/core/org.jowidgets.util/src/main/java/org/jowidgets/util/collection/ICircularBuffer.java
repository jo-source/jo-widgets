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

package org.jowidgets.util.collection;

public interface ICircularBuffer<ELEMENT_TYPE> {

    /**
     * @return The capacity of the circular buffer.
     */
    int capacity();

    /**
     * The current size of the buffer.
     * 
     * If the buffer is not full, the size is the number of added elements,
     * otherwise the capacity and the size is equal.
     * 
     * @return The current size of the buffer
     */
    int size();

    /**
     * Sets the size to a new value.
     * 
     * After that, {@link #size()} returns the new size
     * and all invocations of get(i) for all i with size <= i < capacity will return null
     * 
     * This operation will be done in O(ABS(oldSize-newSize)).
     * 
     * @param size The size to set, must be between 0 and the capacity of the buffer
     */
    void setSize(int size);

    /**
     * Adds an element to the end of buffer.
     * 
     * If the buffer is full, all elements are shifted to the left and the first element will be removed.
     * This operation will be done in O(1).
     * 
     * @param element The element to add, may be null
     */
    void add(ELEMENT_TYPE element);

    /**
     * Removes the last element of the buffer if size > 0, otherwise nothing happens
     * 
     * @return True if the last element was removed, false otherwise
     */
    boolean removeLast();

    /**
     * Fills the buffer with the given element for the indices from size to capacity - 1.
     * 
     * After that, size() == capacity() is true
     * 
     * This operation will be done in O(1) if element == null and
     * otherwise in O(capacity - size)
     * 
     * @param element The element to fill the buffer with, may be null
     */
    void fill(ELEMENT_TYPE element);

    /**
     * Fills the buffer with null elements.
     * 
     * This operation will be done in O(1)
     * 
     * @see {@link ICircularBuffer#fill(Object)}
     */
    void fill();

    /**
     * Sets an element a a specific index.
     * 
     * The given index must be less than size and greater or equal zero (0 >= index < size),
     * so set can only be done for previously added elements.
     * 
     * If all elements should be writable, uses fill after creation to fill the buffer with null elements.
     * This ensures that the size of the buffer is equal with its capacity.
     * 
     * @param index The index to set the element at ((0 >= index < size)
     * @param element The element to set
     * 
     * @throws IndexOutOfBoundsException if index is not less than size and greater or equal zero
     */
    void set(int index, ELEMENT_TYPE element);

    /**
     * Gets the element at the specific index or null, if no element was added to the index before.
     * 
     * This operation will be done in O(1)
     * 
     * @param index The index (0 <= index < capacity)
     * 
     * @return The element
     */
    ELEMENT_TYPE get(int index);

    /**
     * Clears the current buffer.
     * 
     * This operation will be done in O(1).
     */
    void clear();
}
