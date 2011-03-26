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

package org.jowidgets.examples.common.workbench.widgets.views;

import java.awt.Desktop;
import java.net.URI;

import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.widgets.controler.IActionListener;
import org.jowidgets.examples.common.icons.SilkIcons;
import org.jowidgets.examples.common.workbench.base.AbstractDemoView;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;

public abstract class AbstractHowToView extends AbstractDemoView implements IView {

	public static final String ID = AbstractHowToView.class.getName();
	public static final String DEFAULT_LABEL = "Labels";

	private static final String URL_PREFIX = "http://code.google.com/p/jo-widgets/"
		+ "source/browse/trunk/examples/org.jowidgets.examples.common/"
		+ "src/main/java/org/jowidgets/examples/common/workbench/widgets/views/";

	private URI sourceUri;
	private URI migLayoutUri;
	private Desktop desktop;

	public AbstractHowToView(final IViewContext context) {
		super(ID);

		if (Desktop.isDesktopSupported()) {
			desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.BROWSE)) {
				try {
					this.sourceUri = new URI(URL_PREFIX + getClass().getSimpleName() + ".java");
					this.migLayoutUri = new URI("http://www.miglayout.com/");
				}
				catch (final Exception e1) {
					throw new RuntimeException();
				}
			}
		}

		final IActionItemModel sourceAction = context.getToolBar().addActionItem(SilkIcons.PAGE_WHITE_TEXT, "View Source");
		sourceAction.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				try {
					if (desktop != null && sourceUri != null) {
						desktop.browse(sourceUri);
					}
					else {
						Toolkit.getMessagePane().showError("Could not open browser. \n Maybe java desktop is not supported.");
					}
				}
				catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		final IActionItemModel migLayout = context.getToolBar().addActionItem(SilkIcons.WORLD, "MiGLayout Layout Manager");
		migLayout.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				try {
					if (desktop != null && sourceUri != null) {
						desktop.browse(migLayoutUri);
					}
					else {
						Toolkit.getMessagePane().showError("Could not open browser. \n Maybe java desktop is not supported.");
					}
				}
				catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		createViewContent(context.getContainer(), Toolkit.getBluePrintFactory());
	}

	public abstract void createViewContent(IContainer container, IBluePrintFactory bpf);
}
