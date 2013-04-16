/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.examples.common.workbench.demo1;

import org.jowidgets.addons.icons.silkicons.SilkIcons;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.examples.common.demo.DemoMainComposite;
import org.jowidgets.examples.common.workbench.base.AbstractDemoView;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;

public class ViewDemo5 extends AbstractDemoView implements IView {

	public static final String ID = ViewDemo5.class.getName();
	public static final String DEFAULT_LABEL = "Demos";
	public static final String DEFAULT_TOOLTIP = "Demos view";
	public static final IImageConstant DEFAULT_ICON = SilkIcons.CHART_PIE;

	public ViewDemo5(final IViewContext context) {
		super(ID);
		final IContainer container = context.getContainer();
		new DemoMainComposite(container);
	}

}
