/*
 * Copyright (c) 2010, Michael Grossmann, Nikolaus Moll
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

import org.jowidgets.util.Assert;

public final class Rectangle {

    private final Position position;
    private final Dimension size;

    /**
     * Creates a new rectangle
     * 
     * @param x The x - coordinate
     * @param y The y - coordinate
     * @param width The width
     * @param height The height
     */
    public Rectangle(final int x, final int y, final int width, final int height) {
        this(new Position(x, y), new Dimension(width, height));
    }

    /**
     * Creates a new rectangle defined by position and dimension
     * 
     * @param position The position of the rectangle, must not be null
     * @param size The dimension of the rectangle, must not be null
     */
    public Rectangle(final Position position, final Dimension size) {
        Assert.paramNotNull(position, "position");
        Assert.paramNotNull(size, "size");
        this.position = position;
        this.size = size;
    }

    /**
     * Gets the position of the rectangle
     * 
     * @return The position, never null
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Gets the size of the rectangle
     * 
     * @return The size, never null
     */
    public Dimension getSize() {
        return size;
    }

    /**
     * Gets the width of the rectangle
     * 
     * @return The width
     */
    public int getWidth() {
        return size.getWidth();
    }

    /***
     * Gets the height of the rectangle
     * 
     * @return The height
     */
    public int getHeight() {
        return size.getHeight();
    }

    /**
     * Gets the x - coordinate of the rectangle
     * 
     * @return The x - coordinate
     */
    public int getX() {
        return position.getX();
    }

    /**
     * Gets the y - coordinate of the rectangle
     * 
     * @return The y - coordinate
     */
    public int getY() {
        return position.getY();
    }

    /**
     * Checks if a given position is contained in this rectangle
     * 
     * @param point The position to check
     * 
     * @return True if the position is contained in the rectangle, false otherwise
     */
    public boolean contains(final Position point) {
        Assert.paramNotNull(point, "point");

        return (point.getX() >= position.getX())
            && (point.getY() >= position.getY())
            && (point.getX() <= position.getX() + size.getWidth())
            && (point.getY() <= position.getY() + size.getHeight());
    }

    /**
     * Intersects this rectangle with another rectangle. If the rectangles are disjunct, null will be returned
     * 
     * @param rectangle The rectangle to check
     * @return The intersection or null if the rectangles are disjunct
     */

    public Rectangle intersect(final Rectangle rectangle) {
        return intersect(this, rectangle);
    }

    /**
     * Intersects to rectangles. If the rectangles are disjunct, null will be returned
     * 
     * @param r1 The first rectangle
     * @param r2 The second rectangle
     * @return The intersection or null if the rectangles are disjunct
     */
    public static Rectangle intersect(final Rectangle r1, final Rectangle r2) {
        Assert.paramNotNull(r1, "r1");
        Assert.paramNotNull(r2, "r2");

        final int r1X1 = r1.getX();
        final int r1X2 = r1X1 + r1.getWidth();
        final int r2X1 = r2.getX();
        final int r2X2 = r2X1 + r2.getWidth();

        final int minX = Math.max(r1X1, r2X1);
        final int maxX = Math.min(r1X2, r2X2);

        if (minX >= maxX) {
            return null;
        }

        final int r1Y1 = r1.getY();
        final int r1Y2 = r1Y1 + r1.getHeight();
        final int r2Y1 = r2.getY();
        final int r2Y2 = r2Y1 + r2.getHeight();

        final int minY = Math.max(r1Y1, r2Y1);
        final int maxY = Math.min(r1Y2, r2Y2);

        if (minY >= maxY) {
            return null;
        }

        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    /**
     * Checks is the given rectangle and this rectangle are disjunct
     * 
     * @param rectangle The rectangle to check
     * @return True if the given rectangle is disjunct with this rectangle, false otherwise
     */
    public boolean isDisjunctWith(final Rectangle rectangle) {
        return intersect(rectangle) == null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        result = prime * result + ((size == null) ? 0 : size.hashCode());
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rectangle other = (Rectangle) obj;
        if (position == null) {
            if (other.position != null) {
                return false;
            }
        }
        else if (!position.equals(other.position)) {
            return false;
        }
        if (size == null) {
            if (other.size != null) {
                return false;
            }
        }
        else if (!size.equals(other.size)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Rectangle [position=" + position + ", size=" + size + "]";
    }

}
