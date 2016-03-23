/*
 * Copyright (c) 2012, grossmann
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

import java.util.Iterator;

public final class Interval<NUMBER_TYPE extends Number> {

    private final NUMBER_TYPE leftBoundary;
    private final boolean leftOpen;

    private final NUMBER_TYPE rightBoundary;
    private final boolean rightOpen;

    private String stringRepresentation;

    public Interval(final Interval<NUMBER_TYPE> original) {
        this(
            Assert.getParamNotNull(original, "original").getLeftBoundary(),
            original.isLeftOpen(),
            original.getRightBoundary(),
            original.isRightOpen());
    }

    public Interval(final NUMBER_TYPE leftBoundary, final NUMBER_TYPE rightBoundary) {
        this(leftBoundary, false, rightBoundary, false);
    }

    public Interval(
        final NUMBER_TYPE leftBoundary,
        final boolean leftOpen,
        final NUMBER_TYPE rightBoundary,
        final boolean rightOpen) {

        this.leftBoundary = leftBoundary;
        this.leftOpen = leftOpen;
        this.rightBoundary = rightBoundary;
        this.rightOpen = rightOpen;
    }

    public Interval<NUMBER_TYPE> createCopy() {
        return new Interval<NUMBER_TYPE>(this);
    }

    public NUMBER_TYPE getLeftBoundary() {
        return leftBoundary;
    }

    public boolean isLeftOpen() {
        return leftOpen;
    }

    public NUMBER_TYPE getRightBoundary() {
        return rightBoundary;
    }

    public boolean isRightOpen() {
        return rightOpen;
    }

    /**
     * Calculates the intersection of this interval and the given interval
     * 
     * If the intersection is the empty interval, null will be returned
     * 
     * @param interval The interval to intersect with
     * 
     * @return The intersection interval or null, if the intersection is empty
     */
    public Interval<NUMBER_TYPE> intersect(final Interval<NUMBER_TYPE> interval) {
        Assert.paramNotNull(interval, "interval");

        final NUMBER_TYPE min = NumberUtils.max(leftBoundary, interval.getLeftBoundary());
        final NUMBER_TYPE max = NumberUtils.min(rightBoundary, interval.getRightBoundary());

        if (min != null && max != null && NumberUtils.compareTo(min, max) < 0) {
            return new Interval<NUMBER_TYPE>(min, max);
        }
        else {
            return null;
        }
    }

    /**
     * Calculates the intersection of this interval and the given intervas
     * 
     * If the intersection is the empty interval, null will be returned
     * 
     * @param interval The interval to intersect with
     * 
     * @return The intersection interval or null, if the intersection is empty
     */
    public Interval<NUMBER_TYPE> intersect(final Iterable<Interval<NUMBER_TYPE>> intervals) {
        if (EmptyCheck.isEmpty(intervals)) {
            return this;
        }
        Interval<NUMBER_TYPE> result;
        final Iterator<Interval<NUMBER_TYPE>> iterator = intervals.iterator();
        result = iterator.next();
        if (result == null) {
            return null;
        }
        while (iterator.hasNext()) {
            result = result.intersect(iterator.next());
            if (result == null) {
                return null;
            }
        }
        return result;
    }

    /**
     * Calculates the closure of this interval and the given interval
     * 
     * @param interval The interval to calculate the closure with
     * 
     * @return The closure interval, never null
     */
    public Interval<NUMBER_TYPE> closure(final Interval<NUMBER_TYPE> interval) {
        if (interval == null) {
            return this;
        }
        else {
            final NUMBER_TYPE min = NumberUtils.min(getLeftBoundary(), interval.getLeftBoundary());
            final NUMBER_TYPE max = NumberUtils.max(getRightBoundary(), interval.getRightBoundary());
            return new Interval<NUMBER_TYPE>(min, max);
        }
    }

    /**
     * Calculates the closure of this interval and the given intervals
     * 
     * @param intervals The intervals to calculate the closure with
     * 
     * @return The closure interval, never null
     */
    public Interval<NUMBER_TYPE> closure(final Iterable<Interval<NUMBER_TYPE>> intervals) {
        if (EmptyCheck.isEmpty(intervals)) {
            return this;
        }
        Interval<NUMBER_TYPE> result;
        final Iterator<Interval<NUMBER_TYPE>> iterator = intervals.iterator();
        result = iterator.next();
        while (iterator.hasNext()) {
            result = result.closure(iterator.next());
        }
        return result;
    }

    /**
     * Calculates the closure of the given intervals
     * 
     * @param interval1 The given iterval1, may be null
     * @param interval2 The given iterval12, may be null
     * 
     * @return The closure or null if the given intervals are both null
     */
    public static <T extends Number> Interval<T> closureOf(final Interval<T> interval1, final Interval<T> interval2) {
        if (interval1 == null && interval2 == null) {
            return null;
        }
        else if (interval1 == null) {
            return interval2;
        }
        else if (interval2 == null) {
            return interval1;
        }
        else {
            return interval1.closure(interval2);
        }

    }

    /**
     * Calculates the closure of the given intervals
     * 
     * @param intervals The given itervals, may be null or empty
     * 
     * @return The closure or null if intervals is empty
     */
    public static <T extends Number> Interval<T> closureOf(final Iterable<Interval<T>> intervals) {
        if (EmptyCheck.isEmpty(intervals)) {
            return null;
        }
        final Iterator<Interval<T>> iterator = intervals.iterator();
        final Interval<T> first = iterator.next();

        //this iterable only works until the implemenation of cluseWith will now be changed
        final Iterable<Interval<T>> iterable = new Iterable<Interval<T>>() {
            @Override
            public Iterator<Interval<T>> iterator() {
                return iterator;
            }
        };
        return first.closure(iterable);
    }

    /**
     * Calculates the union of this interval and the given interval
     * 
     * If the union can not be calculated, e.g. its not a single interval, null will be returned
     * 
     * @param interval The interval to calculate the union with
     * 
     * @return The union interval or null
     */
    public Interval<NUMBER_TYPE> union(final Interval<NUMBER_TYPE> interval) {
        Assert.paramNotNull(interval, "interval");

        if (intersect(interval) == null) {
            return null;
        }

        final NUMBER_TYPE min = NumberUtils.min(leftBoundary, interval.getLeftBoundary());
        final NUMBER_TYPE max = NumberUtils.max(rightBoundary, interval.getRightBoundary());

        if (min != null && max != null) {
            return new Interval<NUMBER_TYPE>(min, max);
        }
        else {
            return null;
        }
    }

    /**
     * Checks if a number is contained in an interval
     * 
     * @param number The number to check
     * 
     * @return True if contained, false otherwise
     */
    public boolean contains(final NUMBER_TYPE number) {
        return NumberUtils.compareTo(number, leftBoundary) >= 0 && NumberUtils.compareTo(number, rightBoundary) <= 0;
    }

    /**
     * Checks if the given interval is completely contained in this interval.
     * 
     * This is true if the left and the right boundary is conatined in this interval.
     * 
     * @param other The interval to check if contained
     * 
     * @return True if contained, false otherwise
     */
    public boolean contains(final Interval<NUMBER_TYPE> other) {
        Assert.paramNotNull(other, "other");
        return contains(other.getLeftBoundary()) && contains(other.getRightBoundary());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((leftBoundary == null) ? 0 : leftBoundary.hashCode());
        result = prime * result + (leftOpen ? 1231 : 1237);
        result = prime * result + ((rightBoundary == null) ? 0 : rightBoundary.hashCode());
        result = prime * result + (rightOpen ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Interval)) {
            return false;
        }
        final Interval<?> other = (Interval<?>) obj;
        if (leftBoundary == null) {
            if (other.leftBoundary != null) {
                return false;
            }
        }
        else if (!leftBoundary.equals(other.leftBoundary)) {
            return false;
        }
        if (leftOpen != other.leftOpen) {
            return false;
        }
        if (rightBoundary == null) {
            if (other.rightBoundary != null) {
                return false;
            }
        }
        else if (!rightBoundary.equals(other.rightBoundary)) {
            return false;
        }
        if (rightOpen != other.rightOpen) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (stringRepresentation == null) {
            final StringBuilder builder = new StringBuilder();
            if (leftOpen) {
                builder.append("(");
            }
            else {
                builder.append("[");
            }
            if (leftBoundary != null) {
                builder.append(leftBoundary);
            }
            else {
                builder.append("UNDEFINED");
            }
            builder.append(",");
            if (rightBoundary != null) {
                builder.append(rightBoundary);
            }
            else {
                builder.append("UNDEFINED");
            }
            if (rightOpen) {
                builder.append(")");
            }
            else {
                builder.append("]");
            }
            stringRepresentation = builder.toString();
        }

        return stringRepresentation;
    }

}
