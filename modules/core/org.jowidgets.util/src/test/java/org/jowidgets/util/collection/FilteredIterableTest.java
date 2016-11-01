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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jowidgets.util.Filter;
import org.jowidgets.util.IFilter;
import org.jowidgets.util.NullCompatibleEquivalence;
import org.jowidgets.util.StringUtils;
import org.junit.Test;

import junit.framework.Assert;

public class FilteredIterableTest {

    @Test
    public void testFilterNothing() {
        final List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        final List<Integer> expected = input;
        final IFilter<Integer> filter = Filter.acceptAll();
        testFilteredIterable(input, expected, filter);
    }

    @Test
    public void testFilterAll() {
        final List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        final List<Integer> expected = Collections.emptyList();
        final IFilter<Integer> filter = Filter.rejectAll();
        testFilteredIterable(input, expected, filter);
    }

    @Test
    public void testFilterEven() {
        final List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        final List<Integer> expected = Arrays.asList(1, 3, 5, 7, 9, 11);
        final IFilter<Integer> filter = new IFilter<Integer>() {
            @Override
            public boolean accept(final Integer value) {
                return value.intValue() % 2 != 0;
            }
        };
        testFilteredIterable(input, expected, filter);
    }

    @Test
    public void testFilterOdd() {
        final List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        final List<Integer> expected = Arrays.asList(2, 4, 6, 8, 10, 12);
        final IFilter<Integer> filter = new IFilter<Integer>() {
            @Override
            public boolean accept(final Integer value) {
                return value.intValue() % 2 == 0;
            }
        };
        testFilteredIterable(input, expected, filter);
    }

    private void testFilteredIterable(final List<Integer> input, final List<Integer> expected, final IFilter<Integer> filter) {
        final FilteredIterable<Integer> iterable = new FilteredIterable<Integer>(input, filter);
        int index = 0;
        for (final Integer integer : iterable) {
            if (!NullCompatibleEquivalence.equals(integer, expected.get(index++))) {
                fail(iterable, expected);
            }
        }
    }

    private void fail(final FilteredIterable<Integer> iterable, final List<Integer> expected) {
        Assert.fail("Expected: " + expected + " but was: [" + StringUtils.concat(",", iterable) + "]");
    }

}
