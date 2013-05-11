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

package org.jowidgets.examples.common.demo;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.graphics.IGraphicContext;
import org.jowidgets.api.graphics.IPaintListener;
import org.jowidgets.api.widgets.ICanvas;
import org.jowidgets.common.graphics.AntiAliasing;
import org.jowidgets.common.graphics.Point;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.tools.powo.JoFrame;
import org.jowidgets.tools.widgets.blueprint.BPF;

public class DemoCanvasFrame extends JoFrame {

	public DemoCanvasFrame() {
		super("Canvas demo");

		setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "0[grow, 0::]0"));
		final ICanvas canvas = add(BPF.canvas(), "growx, w 0::, growy, h 0::");

		canvas.addPaintListener(new IPaintListener() {
			@Override
			public void paint(final IGraphicContext gc) {
				gc.setAntiAliasing(AntiAliasing.ON);
				gc.setTextAntiAliasing(AntiAliasing.ON);

				gc.setLineWidth(10);

				gc.setForegroundColor(Colors.ERROR);
				gc.drawRectangle(10, 10, 200, 70);

				gc.setForegroundColor(Colors.STRONG);
				gc.fillOval(30, 70, 40, 40);
				gc.fillOval(150, 70, 40, 40);

				gc.setForegroundColor(Colors.BLACK);
				final Point[] polyline = new Point[] {new Point(10, 115), new Point(220, 115), new Point(400, 60)};
				gc.drawPolyline(polyline);

				gc.setFontSize(20);
				gc.setFontName("Arial");
				gc.setTextMarkup(Markup.STRONG);
				gc.drawText("Canvas Demo", 18, 25);
			}
		});
	}
}
