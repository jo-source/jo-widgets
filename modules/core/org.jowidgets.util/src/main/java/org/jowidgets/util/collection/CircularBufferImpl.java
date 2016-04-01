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

import org.jowidgets.util.Assert;

final class CircularBufferImpl<ELEMENT_TYPE> implements ICircularBuffer<ELEMENT_TYPE> {

    private final int capacity;

    private ELEMENT_TYPE[] buffer;
    private int insertIndex;
    private int size;

    CircularBufferImpl(final int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Buffer must have at least capacity of one.");
        }
        this.capacity = capacity;
        clear();
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void setSize(final int size) {
        setSize(size, true);
    }

    @Override
    public void setSize(final int size, final boolean clear) {
        Assert.paramInBounds(capacity, size, "size");
        if (this.size < size) {
            setIcreasedSize(size);
        }
        else if (this.size > size) {
            setDecreasedSize(size, clear);
        }
        //else this.size == size, nothing to do
    }

    private void setIcreasedSize(final int size) {
        final int increasment = size - this.size;
        if (increasment <= 0) {
            throw new IllegalArgumentException("The new size must be creater than the current size");
        }
        insertIndex = insertIndex + increasment;
        if (insertIndex >= capacity) {
            insertIndex = insertIndex - capacity;
        }
        this.size = size;
    }

    private void setDecreasedSize(final int size, final boolean clear) {
        if (clear) {
            setDecreasedSizeWithClear(size);
        }
        else {
            setDecreasedSizeNoClear(size);
        }
    }

    private void setDecreasedSizeNoClear(final int size) {
        final int decreasement = this.size - size;
        if (decreasement <= 0) {
            throw new IllegalArgumentException("The new size must be less than the current size");
        }
        insertIndex = insertIndex - decreasement;
        if (insertIndex < 0) {
            insertIndex = insertIndex + capacity;
        }
        this.size = size;
    }

    private void setDecreasedSizeWithClear(final int size) {
        while (this.size > size) {
            removeLast();
        }
    }

    @Override
    public void add(final ELEMENT_TYPE element) {
        buffer[insertIndex] = element;
        insertIndex++;
        if (insertIndex >= capacity) {
            insertIndex = 0;
        }
        if (size < capacity) {
            size++;
        }
    }

    @Override
    public boolean removeLast() {
        if (size <= 0) {
            return false;
        }
        insertIndex--;
        size--;
        if (insertIndex < 0) {
            insertIndex = capacity - 1;
        }
        buffer[insertIndex] = null;
        return true;
    }

    @Override
    public void set(final int index, final ELEMENT_TYPE element) {
        Assert.paramInBounds(size - 1, index, "index");
        buffer[getTransferedIndex(index)] = element;
    }

    @Override
    public void fill(final ELEMENT_TYPE element) {
        final int elementsToFill = capacity - size();
        if (elementsToFill <= 0) {
            return;
        }
        else if (element == null) {
            insertIndex = getTransferedIndex(0);
            size = capacity;
        }
        else {
            for (int i = 0; i < elementsToFill; i++) {
                add(element);
            }
        }
    }

    @Override
    public void fill() {
        fill(null);
    }

    @Override
    public ELEMENT_TYPE get(final int index) {
        Assert.paramInBounds(capacity - 1, index, "index");
        if (index >= size) {
            return null;
        }
        else {
            return buffer[getTransferedIndex(index)];
        }
    }

    private int getTransferedIndex(final int index) {
        final int residualCapacity = capacity - size;
        int shiftedIndex = insertIndex + residualCapacity + index;
        if (shiftedIndex >= capacity) {
            shiftedIndex = shiftedIndex - capacity;
        }
        else if (shiftedIndex < 0) {
            shiftedIndex = shiftedIndex + capacity - 1;
        }
        return shiftedIndex;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        this.buffer = (ELEMENT_TYPE[]) new Object[capacity];
        insertIndex = 0;
        size = 0;
    }

    @Override
    public String toString() {
        return "CircularBufferImpl [capacity=" + capacity + ", insertIndex=" + insertIndex + ", size" + size + "]";
    }

}
