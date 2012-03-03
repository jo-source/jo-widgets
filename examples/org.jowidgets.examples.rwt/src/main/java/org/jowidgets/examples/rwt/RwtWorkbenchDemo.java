/*
 * Copyright (c) 2010, Michael Grossmann
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
package org.jowidgets.examples.rwt;

import org.jowidgets.addons.map.common.widget.IMapWidgetBlueprint;
import org.jowidgets.addons.map.swt.SwtGoogleEarthWidgetFactory;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.examples.common.map.MapDemoApplication;
import org.jowidgets.examples.common.workbench.demo1.WorkbenchDemo1Factory;
import org.jowidgets.spi.impl.rwt.RwtEntryPoint;
import org.jowidgets.workbench.impl.WorkbenchRunner;

public final class RwtWorkbenchDemo extends RwtEntryPoint {

	public RwtWorkbenchDemo() {
		super(new Runnable() {
			@Override
			public void run() {
				Toolkit.getWidgetFactory().register(
						IMapWidgetBlueprint.class,
						new SwtGoogleEarthWidgetFactory(MapDemoApplication.API_KEY));
				new WorkbenchRunner().run(new WorkbenchDemo1Factory(new Dimension(1024, 650)));
			}
		});
	}

}
