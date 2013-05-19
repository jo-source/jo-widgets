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

	private final ELEMENT_TYPE[] buffer;
	private final int capacity;

	private int insertIndex;

	private boolean shiftMode;

	@SuppressWarnings("unchecked")
	CircularBufferImpl(final int capacity) {
		if (capacity < 1) {
			throw new IllegalArgumentException("Buffer must have at least capacity of one.");
		}
		this.buffer = (ELEMENT_TYPE[]) new Object[capacity];
		this.capacity = buffer.length;

		insertIndex = 0;
		shiftMode = false;
	}

	@Override
	public int capacity() {
		return capacity;
	}

	@Override
	public int size() {
		if (!shiftMode) {
			return insertIndex;
		}
		else {
			return capacity;
		}
	}

	@Override
	public void add(final ELEMENT_TYPE element) {
		if (insertIndex < capacity) {
			buffer[insertIndex] = element;
			insertIndex++;
		}
		else {
			shiftMode = true;
			insertIndex = 0;
			buffer[insertIndex] = element;
			insertIndex++;
		}
	}

	@Override
	public ELEMENT_TYPE get(final int index) {
		Assert.paramInBounds(capacity - 1, index, "index");
		if (!shiftMode) {
			return buffer[index];
		}
		else {
			int shiftedIndex = insertIndex + index;
			if (shiftedIndex >= capacity) {
				shiftedIndex = shiftedIndex - capacity;
			}
			return buffer[shiftedIndex];
		}
	}

}
