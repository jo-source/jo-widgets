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
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.graphics.AntiAliasing;
import org.jowidgets.common.graphics.LineCap;
import org.jowidgets.common.graphics.LineJoin;
import org.jowidgets.common.graphics.Point;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.text.IFontMetrics;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.util.Assert;

public class GraphicContextWrapper implements IGraphicContext {

	private final IGraphicContext original;

	public GraphicContextWrapper(final IGraphicContext original) {
		Assert.paramNotNull(original, "original");
		this.original = original;
	}

	@Override
	public void setSolidLine() {
		original.setSolidLine();
	}

	@Override
	public void drawImage(final IImageConstant image, final int x, final int y, final int width, final int height) {
		original.drawImage(image, x, y, width, height);
	}

	@Override
	public void setAntiAliasing(final AntiAliasing antiAliasing) {
		original.setAntiAliasing(antiAliasing);
	}

	@Override
	public void drawImage(final IImageConstant image, final int x, final int y) {
		original.drawImage(image, x, y);
	}

	@Override
	public void setTextAntiAliasing(final AntiAliasing antiAliasing) {
		original.setTextAntiAliasing(antiAliasing);
	}

	@Override
	public void drawImage(final IImageConstant image) {
		original.drawImage(image);
	}

	@Override
	public void setLineWidth(final int width) {
		original.setLineWidth(width);
	}

	@Override
	public void setDashedLine(final float[] pattern, final float offset) {
		original.setDashedLine(pattern, offset);
	}

	@Override
	public void setLineCap(final LineCap lineCap) {
		original.setLineCap(lineCap);
	}

	@Override
	public void setLineJoin(final LineJoin lineJoin) {
		original.setLineJoin(lineJoin);
	}

	@Override
	public void setFontSize(final int size) {
		original.setFontSize(size);
	}

	@Override
	public void setFontName(final String fontName) {
		original.setFontName(fontName);
	}

	@Override
	public void setTextMarkup(final Markup markup) {
		original.setTextMarkup(markup);
	}

	@Override
	public void setForegroundColor(final IColorConstant color) {
		original.setForegroundColor(color);
	}

	@Override
	public void setBackgroundColor(final IColorConstant color) {
		original.setBackgroundColor(color);
	}

	@Override
	public void clearRectangle(final Rectangle bounds) {
		original.clearRectangle(bounds);
	}

	@Override
	public void clear() {
		original.clear();
	}

	@Override
	public void clearRectangle(final int x, final int y, final int width, final int height) {
		original.clearRectangle(x, y, width, height);
	}

	@Override
	public void drawPoint(final int x, final int y) {
		original.drawPoint(x, y);
	}

	@Override
	public void drawLine(final int x1, final int y1, final int x2, final int y2) {
		original.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void drawRectangle(final int x, final int y, final int width, final int height) {
		original.drawRectangle(x, y, width, height);
	}

	@Override
	public void drawPolygon(final Point[] points) {
		original.drawPolygon(points);
	}

	@Override
	public void drawPolyline(final Point[] points) {
		original.drawPolyline(points);
	}

	@Override
	public void drawOval(final int x, final int y, final int width, final int height) {
		original.drawOval(x, y, width, height);
	}

	@Override
	public void drawArc(final int x, final int y, final int width, final int height, final int startAngle, final int arcAngle) {
		original.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public void fillRectangle(final int x, final int y, final int width, final int height) {
		original.fillRectangle(x, y, width, height);
	}

	@Override
	public void fillPolygon(final Point[] points) {
		original.fillPolygon(points);
	}

	@Override
	public void fillOval(final int x, final int y, final int width, final int height) {
		original.fillOval(x, y, width, height);
	}

	@Override
	public void fillArc(final int x, final int y, final int width, final int height, final int startAngle, final int arcAngle) {
		original.fillArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public void drawText(final String text, final int x, final int y) {
		original.drawText(text, x, y);
	}

	@Override
	public void copyArea(
		final int sourceX,
		final int sourceY,
		final int sourceWidth,
		final int sourceHeight,
		final int destinationX,
		final int destinationY) {
		original.copyArea(sourceX, sourceY, sourceWidth, sourceHeight, destinationX, destinationY);
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
				destinationX,
				destinationY,
				destinationWidth,
				destinationHeight);
	}

	@Override
	public Rectangle getBounds() {
		return original.getBounds();
	}

	@Override
	public IColorConstant getForegroundColor() {
		return original.getForegroundColor();
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return original.getBackgroundColor();
	}

	@Override
	public IFontMetrics getFontMetrics() {
		return original.getFontMetrics();
	}

	@Override
	public int getTextWidth(final String text) {
		return original.getTextWidth(text);
	}

}
