/*
 * Copyright (c) 2015, MGrossmann
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

package org.jowidgets.common.types;

import junit.framework.Assert;

import org.junit.Test;

public class RectangleTest {

    @Test
    public void testIntersectItenticalRectangle() {
        final Rectangle r1 = new Rectangle(10, 12, 14, 15);
        Assert.assertEquals(r1, r1.intersect(r1));
        Assert.assertFalse(r1.isDisjunctWith(r1));
    }

    @Test
    public void testIntersectOverlapOfOneXRectangle() {
        final Rectangle r1 = new Rectangle(0, 0, 4, 1);
        final Rectangle r2 = new Rectangle(3, 0, 4, 1);
        final Rectangle expected = new Rectangle(3, 0, 1, 1);
        Assert.assertEquals(expected, r1.intersect(r2));
        Assert.assertEquals(expected, r2.intersect(r1));
        Assert.assertFalse(r1.isDisjunctWith(r2));
        Assert.assertFalse(r2.isDisjunctWith(r1));
    }

    @Test
    public void testIntersectNoOverlapXRectangle() {
        final Rectangle r1 = new Rectangle(0, 0, 3, 1);
        final Rectangle r2 = new Rectangle(3, 0, 3, 1);
        Assert.assertNull(r1.intersect(r2));
        Assert.assertNull(r2.intersect(r1));
        Assert.assertTrue(r1.isDisjunctWith(r2));
        Assert.assertTrue(r2.isDisjunctWith(r1));
    }

    @Test
    public void testIntersectOverlapOfOneYRectangle() {
        final Rectangle r1 = new Rectangle(0, 0, 1, 4);
        final Rectangle r2 = new Rectangle(0, 3, 1, 4);
        final Rectangle expected = new Rectangle(0, 3, 1, 1);
        Assert.assertEquals(expected, r1.intersect(r2));
        Assert.assertEquals(expected, r2.intersect(r1));
        Assert.assertFalse(r1.isDisjunctWith(r2));
        Assert.assertFalse(r2.isDisjunctWith(r1));
    }

    @Test
    public void testIntersectNoOverlapYRectangle() {
        final Rectangle r1 = new Rectangle(0, 0, 1, 3);
        final Rectangle r2 = new Rectangle(0, 3, 1, 3);
        Assert.assertNull(r1.intersect(r2));
        Assert.assertNull(r2.intersect(r1));
        Assert.assertTrue(r1.isDisjunctWith(r2));
        Assert.assertTrue(r2.isDisjunctWith(r1));
    }

    @Test
    public void testIntersectOverlapOneRectangle() {
        final Rectangle r1 = new Rectangle(0, 0, 3, 3);
        final Rectangle r2 = new Rectangle(2, 2, 3, 3);
        final Rectangle expected = new Rectangle(2, 2, 1, 1);
        Assert.assertEquals(expected, r1.intersect(r2));
        Assert.assertEquals(expected, r2.intersect(r1));
        Assert.assertFalse(r1.isDisjunctWith(r2));
        Assert.assertFalse(r2.isDisjunctWith(r1));
    }

    @Test
    public void testIntersectNoOverlapRectangle() {
        final Rectangle r1 = new Rectangle(0, 0, 3, 3);
        final Rectangle r2 = new Rectangle(3, 3, 3, 3);
        Assert.assertNull(r1.intersect(r2));
        Assert.assertNull(r2.intersect(r1));
        Assert.assertTrue(r1.isDisjunctWith(r2));
        Assert.assertTrue(r2.isDisjunctWith(r1));
    }

    @Test
    public void testIntersectOverlapOneRectangleWithShift() {
        final Rectangle r1 = new Rectangle(2, 2, 3, 3);
        final Rectangle r2 = new Rectangle(4, 4, 3, 3);
        final Rectangle expected = new Rectangle(4, 4, 1, 1);
        Assert.assertEquals(expected, r1.intersect(r2));
        Assert.assertEquals(expected, r2.intersect(r1));
        Assert.assertFalse(r1.isDisjunctWith(r2));
        Assert.assertFalse(r2.isDisjunctWith(r1));
    }
}
