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

package org.jowidgets.util;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class IntervalTest {

    @Test
    public void testStaticClosure() {
        final Interval<Integer> interval1 = new Interval<Integer>(2, 4);
        final Interval<Integer> interval2 = new Interval<Integer>(-3, -2);
        final Interval<Integer> interval3 = new Interval<Integer>(5, 7);
        final Interval<Integer> interval4 = new Interval<Integer>(-1, 8);
        final Interval<Integer> interval5 = new Interval<Integer>(-7, -2);

        @SuppressWarnings("unchecked")
        final List<Interval<Integer>> intervals = Arrays.asList(interval1, interval2, interval3, interval4, interval5);
        Assert.assertEquals(new Interval<Integer>(-7, 8), Interval.closureOf(intervals));
    }

    @Test
    public void testClosure() {
        final Interval<Integer> interval1 = new Interval<Integer>(3, 4);
        final Interval<Integer> interval2 = new Interval<Integer>(6, 6);
        @SuppressWarnings("unchecked")
        final List<Interval<Integer>> intervals = Arrays.asList(interval2);
        Assert.assertEquals(new Interval<Integer>(3, 6), interval1.closure(intervals));
    }

    @Test
    public void testIntersect() {
        final Interval<Integer> interval1 = new Interval<Integer>(3, 4);
        final Interval<Integer> interval2 = new Interval<Integer>(4, 6);
        Assert.assertEquals(new Interval<Integer>(4, 4), interval1.intersect(interval2));
    }

    @Test
    public void testIntersectWithCollection() {
        final Interval<Integer> interval1 = new Interval<Integer>(3, 4);
        final Interval<Integer> interval2 = new Interval<Integer>(4, 6);
        @SuppressWarnings("unchecked")
        final List<Interval<Integer>> intervals = Arrays.asList(interval2);
        Assert.assertEquals(new Interval<Integer>(4, 4), interval1.intersect(intervals));
    }

}
