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

package org.jowidgets.impl.widgets.basic.graphics;

import org.jowidgets.api.graphics.IGraphicContext;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.graphics.AntiAliasing;
import org.jowidgets.common.graphics.LineCap;
import org.jowidgets.common.graphics.LineJoin;
import org.jowidgets.common.graphics.Point;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.spi.graphics.IGraphicContextSpi;
import org.jowidgets.util.Assert;

public final class GraphicContextAdapter implements IGraphicContext {

	private final IGraphicContextSpi contextSpi;

	public GraphicContextAdapter(final IGraphicContextSpi contextSpi) {
		Assert.paramNotNull(contextSpi, "contextSpi");
		this.contextSpi = contextSpi;
	}

	@Override
	public Rectangle getBounds() {
		return contextSpi.getBounds();
	}

	@Override
	public void setAntiAliasing(final AntiAliasing antiAliasing) {
		contextSpi.setAntiAliasing(antiAliasing);
	}

	@Override
	public void setLineCap(final LineCap lineCap) {
		contextSpi.setLineCap(lineCap);
	}

	@Override
	public void setLineJoin(final LineJoin lineJoin) {
		contextSpi.setLineJoin(lineJoin);
	}

	@Override
	public void setLineWidth(final int width) {
		contextSpi.setLineWidth(width);
	}

	@Override
	public void setForegroundColor(final IColorConstant color) {
		contextSpi.setForegroundColor(color);
	}

	@Override
	public void setBackgroundColor(final IColorConstant color) {
		contextSpi.setBackgroundColor(color);
	}

	@Override
	public void clearRectangle(final int x, final int y, final int width, final int height) {
		contextSpi.clearRectangle(x, y, width, height);
	}

	@Override
	public void drawPoint(final int x, final int y) {
		contextSpi.drawPoint(x, y);
	}

	@Override
	public void drawLine(final int x1, final int y1, final int x2, final int y2) {
		contextSpi.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void drawRectangle(final int x, final int y, final int width, final int height) {
		contextSpi.drawRectangle(x, y, width, height);
	}

	@Override
	public void drawPolygon(final Point[] points) {
		contextSpi.drawPolygon(points);
	}

	@Override
	public void drawPolyline(final Point[] points) {
		contextSpi.drawPolyline(points);
	}

	@Override
	public void drawOval(final int x, final int y, final int width, final int height) {
		contextSpi.drawOval(x, y, width, height);
	}

	@Override
	public void drawArc(final int x, final int y, final int width, final int height, final int startAngle, final int arcAngle) {
		contextSpi.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public void fillRectangle(final int x, final int y, final int width, final int height) {
		contextSpi.fillRectangle(x, y, width, height);
	}

	@Override
	public void fillPolygon(final Point[] points) {
		contextSpi.fillPolygon(points);
	}

	@Override
	public void fillOval(final int x, final int y, final int width, final int height) {
		contextSpi.fillOval(x, y, width, height);
	}

	@Override
	public void fillArc(final int x, final int y, final int width, final int height, final int startAngle, final int arcAngle) {
		contextSpi.fillArc(x, y, width, height, startAngle, arcAngle);
	}

}
