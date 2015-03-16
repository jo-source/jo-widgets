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

import java.net.URL;

import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.image.IImage;
import org.jowidgets.api.image.ImageFactory;
import org.jowidgets.api.layout.FillLayout;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IIcon;
import org.jowidgets.api.widgets.IScrollComposite;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.widgets.controller.IMouseButtonEvent;
import org.jowidgets.tools.controller.MouseAdapter;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.util.url.UrlFactory;

public final class ImageIconSnipped implements IApplication {

	@Override
	public void start(final IApplicationLifecycle lifecycle) {

		//create the root frame
		final IFrame frame = Toolkit.createRootFrame(BPF.frame("Image Icon Snipped"), lifecycle);
		frame.setLayout(FillLayout.get());

		//create a scroll composite
		final IScrollComposite container = frame.add(BPF.scrollComposite());
		container.setLayout(FillLayout.get());

		//create a image
		final URL url = UrlFactory.create("http://www.jowidgets.org/docu/images/widgets_hierarchy_1.gif");
		final IImage image = ImageFactory.createImage(url);

		//use the icon widget to display the image
		final IIcon imageIcon = container.add(BPF.icon(image));

		//remove the icon on double click from its container to test dispose
		imageIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClicked(final IMouseButtonEvent mouseEvent) {
				imageIcon.dispose();
			}
		});

		//dispose the icon if it is no longer used
		imageIcon.addDisposeListener(new IDisposeListener() {
			@Override
			public void onDispose() {
				image.dispose();
				//CHECKSTYLE:OFF
				System.out.println("DISPOSED IMAGE");
				//CHECKSTYLE:ON
			}
		});

		//set the root frame visible
		frame.setVisible(true);

	}
}
