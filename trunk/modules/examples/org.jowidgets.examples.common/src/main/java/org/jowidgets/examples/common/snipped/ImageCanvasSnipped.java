/*
 * Copyright (c) 2013, grossmann
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

import java.io.File;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.controller.IPaintEvent;
import org.jowidgets.api.graphics.IGraphicContext;
import org.jowidgets.api.graphics.IPaintListener;
import org.jowidgets.api.image.IImage;
import org.jowidgets.api.image.ImageFactory;
import org.jowidgets.api.layout.FillLayout;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.ICanvas;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IScrollComposite;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.graphics.Point;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class ImageCanvasSnipped implements IApplication {

	@Override
	public void start(final IApplicationLifecycle lifecycle) {

		//create the root frame
		final IFrame frame = Toolkit.createRootFrame(BPF.frame("Image Canvas Snipped"), lifecycle);
		frame.setLayout(FillLayout.get());

		//create a scroll composite
		final IScrollComposite container = frame.add(BPF.scrollComposite());
		container.setLayout(FillLayout.get());

		//create a image from url
		final String path = "C:/projects/jo/jo-widgets/repo/trunk/docu/images/widgets_hierarchy_1.gif";
		final IImage image = ImageFactory.createImage(new File(path));

		//use a canvas to display the image
		final ICanvas canvas = container.add(BPF.canvas());

		//set the preferred size of the canvas to the image size
		canvas.setPreferredSize(image.getSize());

		//add a paint listener to draw the image and an arrow
		canvas.addPaintListener(new IPaintListener() {
			@Override
			public void paint(final IPaintEvent event) {
				final IGraphicContext gc = event.getGraphicContext();

				//draw the image
				gc.drawImage(image);

				//draw with green color
				gc.setForegroundColor(Colors.GREEN);

				//define a polygon that shapes an arrow
				final Point p1 = new Point(438, 205);
				final Point p2 = new Point(464, 205);
				final Point p3 = new Point(464, 199);
				final Point p4 = new Point(486, 211);
				final Point p5 = new Point(464, 223);
				final Point p6 = new Point(464, 217);
				final Point p7 = new Point(438, 217);
				final Point[] polygon = new Point[] {p1, p2, p3, p4, p5, p6, p7, p1};

				//fill the polygon 
				gc.fillPolygon(polygon);
			}
		});

		//set the root frame visible
		frame.setVisible(true);
	}
}
