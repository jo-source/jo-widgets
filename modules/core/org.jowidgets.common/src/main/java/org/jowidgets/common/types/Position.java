/*
 * Copyright (c) 2010, Michael Grossmann, Nikolaus Moll
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.common.types;

public final class Position {

    private final int x;
    private final int y;

    /**
     * Creates a new position
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public Position(final int x, final int y) {
        super();
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x coordinate
     * 
     * @return The x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y coordinate
     * 
     * @return The y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the absolute value of the position.
     * 
     * The absolute value is defined by sqrt(x^2 + y^2)
     * 
     * @return
     */
    public int getAbsoluteValue() {
        return (int) (Math.sqrt(x * x + y * y));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
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
        final Position other = (Position) obj;
        if (x != other.x) {
            return false;
        }
        if (y != other.y) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Position [x=" + x + ", y=" + y + "]";
    }

    public static Position subtract(final Position minuend, final Position subtrahend) {
        return new Position(minuend.x - subtrahend.x, minuend.y - subtrahend.y);
    }

    public static Position add(final Position... addends) {
        int x = 0;
        int y = 0;
        for (final Position addend : addends) {
            x = x + addend.x;
            y = y + addend.y;
        }
        return new Position(x, y);
    }

    public static Position convert(final Dimension dimension) {
        return new Position(dimension.getWidth(), dimension.getHeight());
    }

}
