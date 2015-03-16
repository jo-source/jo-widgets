/*
 * Copyright (c) 2015, grossmann
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
package org.jowidgets.examples.common.snipped;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.graphics.IGraphicContext;
import org.jowidgets.api.image.IBufferedImage;
import org.jowidgets.api.image.ImageFactory;
import org.jowidgets.api.layout.FillLayout;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.graphics.AntiAliasing;
import org.jowidgets.common.graphics.Point;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class BufferedImageSnipped implements IApplication {

	@Override
	public void start(final IApplicationLifecycle lifecycle) {

		//create the root frame
		final IFrameBluePrint frameBp = BPF.frame("Buffered Image Snipped");
		final IFrame frame = Toolkit.createRootFrame(frameBp, lifecycle);
		frame.setSize(300, 200);
		frame.setBackgroundColor(Colors.WHITE);
		frame.setLayout(FillLayout.builder().margin(10).build());

		//create a arrow buffered image
		final IBufferedImage image = createArrowImage();

		//create a label using the buffered image as icon
		frame.add(BPF.label().setIcon(image).setText("Hello world"));

		//set the root frame visible
		frame.setVisible(true);
	}

	private static IBufferedImage createArrowImage() {
		//create a buffered image
		final IBufferedImage image = ImageFactory.createBufferedImage(52, 26);
		final IGraphicContext gc = image.getGraphicContext();

		//use anti aliasing
		gc.setAntiAliasing(AntiAliasing.ON);

		//define a polygon that shapes an arrow
		final Point p1 = new Point(0, 6);
		final Point p2 = new Point(26, 6);
		final Point p3 = new Point(26, 0);
		final Point p4 = new Point(48, 12);
		final Point p5 = new Point(26, 24);
		final Point p6 = new Point(26, 18);
		final Point p7 = new Point(0, 18);
		final Point[] polygon = new Point[] {p1, p2, p3, p4, p5, p6, p7, p1};

		//use white background for the image
		gc.setBackgroundColor(Colors.WHITE);
		gc.clear();

		//draw with green color
		gc.setForegroundColor(Colors.GREEN);

		//fill the polygon 
		gc.fillPolygon(polygon);

		return image;
	}
}
