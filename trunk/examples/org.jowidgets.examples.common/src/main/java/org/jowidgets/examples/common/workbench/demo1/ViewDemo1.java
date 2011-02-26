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

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.examples.common.demo.DemoMenuProvider;
import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.examples.common.workbench.base.AbstractView;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.workbench.api.IViewContext;

public class ViewDemo1 extends AbstractView {

	public static final String ID = ViewDemo1.class.getName();
	public static final String DEFAULT_LABEL = "View1";
	public static final String DEFAULT_TOOLTIP = "View1 tooltip";
	public static final IImageConstant DEFAULT_ICON = SilkIcons.STATUS_ONLINE;

	public ViewDemo1(final IViewContext context) {
		super(ID);

		final DemoMenuProvider menuProvider = new DemoMenuProvider();
		context.getToolBar().addItemsOfModel(menuProvider.getToolBarModel());
		context.getToolBarMenu().addItemsOfModel(menuProvider.getMenuModel());

		final IContainer container = context.getContainer();
		container.setLayout(MigLayoutFactory.growingInnerCellLayout());
		final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();

		final IComposite content = container.add(
				bpf.composite().setBackgroundColor(Colors.WHITE),
				MigLayoutFactory.GROWING_CELL_CONSTRAINTS);

		content.setLayout(MigLayoutFactory.growingInnerCellLayout());
		content.add(bpf.textLabel("View content 1"), "alignx center, aligny center");

	}

}
