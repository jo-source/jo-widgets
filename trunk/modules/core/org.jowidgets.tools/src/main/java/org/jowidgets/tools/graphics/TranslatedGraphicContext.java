/*
 * Copyright (c) 2014, MGrossmann
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

package org.jowidgets.tools.graphics;

import org.jowidgets.api.graphics.IGraphicContext;
import org.jowidgets.common.graphics.Point;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.util.Assert;

public class TranslatedGraphicContext extends GraphicContextWrapper implements IGraphicContext {

    private final IGraphicContext original;
    private final int offsetX;
    private final int offsetY;

    public TranslatedGraphicContext(final IGraphicContext original, final int offsetX, final int offsetY) {
        this(original, new Point(offsetX, offsetY));
    }

    public TranslatedGraphicContext(final IGraphicContext original, final Point offset) {
        super(original);
        Assert.paramNotNull(offset, "offset");
        this.original = original;
        this.offsetX = offset.getX();
        this.offsetY = offset.getY();
    }

    @Override
    public void clearRectangle(final int x, final int y, final int width, final int height) {
        original.clearRectangle(x + offsetX, y + offsetY, width, height);
    }

    @Override
    public void drawPoint(final int x, final int y) {
        original.drawPoint(x + offsetX, y + offsetY);
    }

    @Override
    public void drawLine(final int x1, final int y1, final int x2, final int y2) {
        original.drawLine(x1 + offsetX, y1 + offsetY, x2 + offsetX, y2 + offsetY);
    }

    @Override
    public void drawImage(final IImageConstant image, final int x, final int y, final int width, final int height) {
        original.drawImage(image, x + offsetX, y + offsetY, width, height);
    }

    @Override
    public void drawImage(final IImageConstant image, final int x, final int y) {
        original.drawImage(image, x + offsetX, y + offsetY);
    }

    @Override
    public void drawImage(final IImageConstant image) {
        original.drawImage(image, offsetX, offsetY);
    }

    @Override
    public void drawRectangle(final int x, final int y, final int width, final int height) {
        original.drawRectangle(x + offsetX, y + offsetY, width, height);
    }

    @Override
    public void drawOval(final int x, final int y, final int width, final int height) {
        original.drawOval(x + offsetX, y + offsetY, width, height);
    }

    @Override
    public void drawArc(final int x, final int y, final int width, final int height, final int startAngle, final int arcAngle) {
        original.drawArc(x + offsetX, y + offsetY, width, height, startAngle, arcAngle);
    }

    @Override
    public void fillRectangle(final int x, final int y, final int width, final int height) {
        original.fillRectangle(x + offsetX, y + offsetY, width, height);
    }

    @Override
    public void fillOval(final int x, final int y, final int width, final int height) {
        original.fillOval(x + offsetX, y + offsetY, width, height);
    }

    @Override
    public void fillArc(final int x, final int y, final int width, final int height, final int startAngle, final int arcAngle) {
        original.fillArc(x + offsetX, y + offsetY, width, height, startAngle, arcAngle);
    }

    @Override
    public void drawText(final String text, final int x, final int y) {
        original.drawText(text, x + offsetX, y + offsetY);
    }

    @Override
    public void copyArea(
        final int sourceX,
        final int sourceY,
        final int sourceWidth,
        final int sourceHeight,
        final int destinationX,
        final int destinationY) {
        original.copyArea(sourceX + offsetX, sourceY + offsetY, sourceWidth, sourceHeight, destinationX + offsetX, destinationY
            + offsetY);
    }

    @Override
    public void drawImage(
        final IImageConstant image,
        final int sourceX,
        final int sourceY,
        final int sourceWidth,
        final int sourceHeight,
        final int destinationX,
        final int destinationY,
        final int destinationWidth,
        final int destinationHeight) {
        original.drawImage(
                image,
                sourceX,
                sourceY,
                sourceWidth,
                sourceHeight,
                destinationX + offsetX,
                destinationY + offsetY,
                destinationWidth,
                destinationHeight);
    }

    @Override
    public void drawPolygon(final Point[] points) {
        original.drawPolygon(getTranslatedPoints(points));
    }

    @Override
    public void drawPolyline(final Point[] points) {
        original.drawPolyline(getTranslatedPoints(points));
    }

    @Override
    public void fillPolygon(final Point[] points) {
        original.fillPolygon(getTranslatedPoints(points));
    }

    private Point[] getTranslatedPoints(final Point[] points) {
        if (points == null) {
            return points;
        }
        final Point[] result = new Point[points.length];
        for (int i = 0; i < result.length; i++) {
            final Point source = points[i];
            result[i] = new Point(source.getX() + offsetX, source.getY() + offsetY);
        }
        return result;
    }

    @Override
    public Rectangle getBounds() {
        return original.getBounds();
    }

}
