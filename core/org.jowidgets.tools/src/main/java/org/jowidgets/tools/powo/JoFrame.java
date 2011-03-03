/*
 * Copyright (c) 2010, grossmann
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

package org.jowidgets.tools.powo;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IMenuBar;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.api.widgets.descriptor.setup.IFrameSetup;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.controler.impl.WindowAdapter;
import org.jowidgets.util.Assert;

public class JoFrame extends Window<IFrame, IFrameBluePrint> implements IFrame {

	private JoMenuBar menuBar;

	public JoFrame() {
		super(Toolkit.getBluePrintFactory().frame());
	}

	public JoFrame(final String title) {
		super(Toolkit.getBluePrintFactory().frame(title));
	}

	public JoFrame(final String title, final IImageConstant icon) {
		super(Toolkit.getBluePrintFactory().frame(title, icon));
	}

	public JoFrame(final IFrameSetup setup) {
		super(Toolkit.getBluePrintFactory().frame().setSetup(setup));
	}

	public void finishLifecycleOnClose(final IApplicationLifecycle lifecycle) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed() {
				lifecycle.finish();
			}
		});
	}

	public final void setMenuBar(final JoMenuBar menuBar) {
		Assert.paramNotNull(menuBar, "menuBar");
		if (isInitialized()) {
			menuBar.initialize(createMenuBar());
		}
		else {
			this.menuBar = menuBar;
		}
	}

	@Override
	void initialize(final IFrame widget) {
		super.initialize(widget);
		if (menuBar != null) {
			menuBar.initialize(createMenuBar());
		}
	}

	@Override
	public IMenuBar createMenuBar() {
		if (isInitialized()) {
			return getWidget().createMenuBar();
		}
		else {
			menuBar = new JoMenuBar();
			return menuBar;
		}
	}

	public static IFrameBluePrint bluePrint() {
		return Toolkit.getBluePrintFactory().frame();
	}

	public static IFrameBluePrint bluePrint(final String title) {
		return Toolkit.getBluePrintFactory().frame(title);
	}

}
