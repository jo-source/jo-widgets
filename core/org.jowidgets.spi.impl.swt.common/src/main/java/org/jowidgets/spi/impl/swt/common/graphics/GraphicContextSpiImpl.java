/*
 * Copyright (c) 2013, grossmann
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

package org.jowidgets.spi.impl.swt.common.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.graphics.AntiAliasing;
import org.jowidgets.common.graphics.LineCap;
import org.jowidgets.common.graphics.LineJoin;
import org.jowidgets.common.graphics.Point;
import org.jowidgets.common.text.IFontMetrics;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.spi.graphics.IGraphicContextSpi;
import org.jowidgets.spi.impl.swt.common.color.ColorCache;
import org.jowidgets.spi.impl.swt.common.util.ColorConvert;
import org.jowidgets.spi.impl.swt.common.util.FontMetricsConvert;
import org.jowidgets.spi.impl.swt.common.util.FontProvider;
import org.jowidgets.util.Assert;

public final class GraphicContextSpiImpl implements IGraphicContextSpi {

	private final GC gc;
	private final Rectangle bounds;

	public GraphicContextSpiImpl(final GC gc, final Rectangle bounds) {
		super();
		this.gc = gc;
		this.bounds = bounds;
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public void setAntiAliasing(final AntiAliasing antiAliasing) {
		Assert.paramNotNull(antiAliasing, "antiAliasing");
		if (AntiAliasing.ON.equals(antiAliasing)) {
			gc.setAntialias(SWT.ON);
		}
		else if (AntiAliasing.OFF.equals(antiAliasing)) {
			gc.setAntialias(SWT.OFF);
		}
		else if (AntiAliasing.DEFAULT.equals(antiAliasing)) {
			gc.setAntialias(SWT.DEFAULT);
		}
		else {
			throw new IllegalArgumentException("AntiAliasing '" + antiAliasing + "' is not known.");
		}
	}

	@Override
	public void setTextAntiAliasing(final AntiAliasing antiAliasing) {
		Assert.paramNotNull(antiAliasing, "antiAliasing");
		if (AntiAliasing.ON.equals(antiAliasing)) {
			gc.setTextAntialias(SWT.ON);
		}
		else if (AntiAliasing.OFF.equals(antiAliasing)) {
			gc.setTextAntialias(SWT.OFF);
		}
		else if (AntiAliasing.DEFAULT.equals(antiAliasing)) {
			gc.setTextAntialias(SWT.DEFAULT);
		}
		else {
			throw new IllegalArgumentException("AntiAliasing '" + antiAliasing + "' is not known.");
		}
	}

	@Override
	public void setLineCap(final LineCap lineCap) {
		Assert.paramNotNull(lineCap, "lineCap");
		if (LineCap.FLAT.equals(lineCap)) {
			gc.setLineCap(SWT.CAP_FLAT);
		}
		else if (LineCap.ROUND.equals(lineCap)) {
			gc.setLineCap(SWT.CAP_ROUND);
		}
		else if (LineCap.SQUARE.equals(lineCap)) {
			gc.setLineCap(SWT.CAP_SQUARE);
		}
		else {
			throw new IllegalArgumentException("LineCap '" + lineCap + "' is not known.");
		}
	}

	@Override
	public void setLineJoin(final LineJoin lineJoin) {
		Assert.paramNotNull(lineJoin, "lineJoin");
		if (LineJoin.BEVEL.equals(lineJoin)) {
			gc.setLineJoin(SWT.JOIN_BEVEL);
		}
		else if (LineJoin.MITER.equals(lineJoin)) {
			gc.setLineJoin(SWT.JOIN_MITER);
		}
		else if (LineJoin.ROUND.equals(lineJoin)) {
			gc.setLineJoin(SWT.JOIN_ROUND);
		}
		else {
			throw new IllegalArgumentException("LineJoin '" + lineJoin + "' is not known.");
		}
	}

	@Override
	public void setLineWidth(final int width) {
		gc.setLineWidth(width);
	}

	@Override
	public void setTextMarkup(final Markup markup) {
		gc.setFont(FontProvider.deriveFont(gc.getFont(), markup));
	}

	@Override
	public void setFontSize(final int size) {
		gc.setFont(FontProvider.deriveFont(gc.getFont(), size));
	}

	@Override
	public void setFontName(final String fontName) {
		gc.setFont(FontProvider.deriveFont(gc.getFont(), fontName));
	}

	@Override
	public void setForegroundColor(final IColorConstant color) {
		gc.setForeground(ColorCache.getInstance().getColor(color));
	}

	@Override
	public void setBackgroundColor(final IColorConstant color) {
		gc.setBackground(ColorCache.getInstance().getColor(color));
	}

	@Override
	public void clearRectangle(final int x, final int y, final int width, final int height) {
		gc.fillRectangle(x, y, width, height);
	}

	@Override
	public void drawPoint(final int x, final int y) {
		gc.drawPoint(x, y);
	}

	@Override
	public void drawLine(final int x1, final int y1, final int x2, final int y2) {
		gc.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void drawRectangle(final int x, final int y, final int width, final int height) {
		gc.drawRectangle(x, y, width, height);
	}

	@Override
	public void drawPolygon(final Point[] points) {
		gc.drawPolygon(getCoordinates(points));
	}

	@Override
	public void drawPolyline(final Point[] points) {
		gc.drawPolyline(getCoordinates(points));
	}

	@Override
	public void drawOval(final int x, final int y, final int width, final int height) {
		gc.drawOval(x, y, width, height);
	}

	@Override
	public void drawArc(final int x, final int y, final int width, final int height, final int startAngle, final int arcAngle) {
		gc.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public void fillRectangle(final int x, final int y, final int width, final int height) {
		final Color background = gc.getBackground();
		gc.setBackground(gc.getForeground());
		gc.fillRectangle(x, y, width, height);
		gc.setBackground(background);
	}

	@Override
	public void fillPolygon(final Point[] points) {
		final Color background = gc.getBackground();
		gc.setBackground(gc.getForeground());
		gc.fillPolygon(getCoordinates(points));
		gc.setBackground(background);
	}

	@Override
	public void fillOval(final int x, final int y, final int width, final int height) {
		final Color background = gc.getBackground();
		gc.setBackground(gc.getForeground());
		gc.fillOval(x, y, width, height);
		gc.setBackground(background);
	}

	@Override
	public void fillArc(final int x, final int y, final int width, final int height, final int startAngle, final int arcAngle) {
		final Color background = gc.getBackground();
		gc.setBackground(gc.getForeground());
		gc.fillArc(x, y, width, height, startAngle, arcAngle);
		gc.setBackground(background);
	}

	@Override
	public void drawText(final String text, final int x, final int y) {
		gc.drawText(text, x, y, true);
	}

	@Override
	public IColorConstant getForegroundColor() {
		return ColorConvert.convert(gc.getForeground());
	}

	@Override
	public IColorConstant getBackgroundColor() {
		return ColorConvert.convert(gc.getBackground());
	}

	@Override
	public IFontMetrics getFontMetrics() {
		return FontMetricsConvert.convert(gc.getFontMetrics());
	}

	@Override
	public int getTextWidth(final String text) {
		if (text != null) {
			int result = 0;
			for (final char ch : text.toCharArray()) {
				result += gc.getAdvanceWidth(ch);
			}
			return result;
		}
		else {
			return 0;
		}
	}

	private static int[] getCoordinates(final Point[] points) {
		Assert.paramNotNull(points, "points");
		final int[] result = new int[points.length * 2];
		int index = 0;
		for (final Point point : points) {
			result[index] = point.getX();
			result[index + 1] = point.getY();
			index = index + 2;
		}
		return result;
	}

}
